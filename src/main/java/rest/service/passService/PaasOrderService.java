package rest.service.passService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rest.mybatis.dao.passDao.Imp.PaasInstanceImp;
import rest.mybatis.dao.passDao.Imp.PaasOrderImp;
import rest.mybatis.dao.passDao.Imp.PaasTemplateImp;
import rest.mybatis.model.passModel.PaasInstance;
import rest.mybatis.model.passModel.PaasOrder;
import rest.mybatis.model.passModel.PaasTemplate;
import rest.page.util.Message;
import rest.page.util.PageUtil;
import rest.page.util.Pageinfo;

@RestController
public class PaasOrderService {
	@Autowired
	private PaasOrderImp paasOrderImp;
	@Autowired
	private PaasTemplateImp paasTemplateImp;
	@Autowired
	private PaasInstanceImp paasInstanceImp;
	@Autowired
	private PageUtil pageutil;
	@RequestMapping("/rest/orderService/getAllOrder")
	public Pageinfo getAllOrder(@RequestParam(name="TenantName",required=false,defaultValue="") String TenantName,@RequestParam(name="Status",required=false,defaultValue="") Integer Status,@RequestParam(name="startTime",required=false,defaultValue="") String startTime,@RequestParam(name="endTime",required=false,defaultValue="") String endTime,@RequestParam(name="page",required=false,defaultValue="1") String page) throws ParseException
	{
		PaasOrder po=new PaasOrder();
		//从远端那到租户ID 然后封装
		po.setTenantName(TenantName);
		po.setStatus(Status);
		if(startTime!=null && !"".equals(startTime))
		{
			po.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse(startTime));
		}
		if(endTime!=null && !"".equals(endTime))
		{
			po.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse(endTime));
		}
		int num=paasOrderImp.getOrderNumbyCondition(po);
		Pageinfo pi=pageutil.initpage(num, page);
		pi.setConditionObj(po);
		List<PaasOrder> list=paasOrderImp.getOrderListbyCondition(pi);
		pi.setResultObj(list);
		return pi;
	}
	/**
	 * 审核信息
	 */
	@RequestMapping("/rest/orderService/updateOrder")
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Message updateOrder(@RequestParam(name="userId",required=false,defaultValue="1") Integer userId,@RequestParam(name="orderId",required=true) Integer orderId,@RequestParam(name="statue",required=true) Integer statue,@RequestParam(name="resion",required=false) String resion,@RequestParam(name="proId") Integer proId,@RequestParam(name="instanceid",defaultValue="") String instanceid)
	{
		PaasOrder po=new PaasOrder();
		po.setId(orderId);
		po.setProId(proId);
		po.setStatus(statue);
		po.setApproveDescibe(resion);
		po.setApproveDate(new Date());
		po.setApproveId(userId);
		po.setInstanceId(instanceid);
		PaasTemplate pt=paasTemplateImp.selectByPrimaryKey(proId);
		//判断实例是否存在处于未创建 已运行状态的实例 true else false
		boolean result=paasInstanceImp.isexistInstance(pt.getId());
		//通过 且订单没有关联过实例
		if("".equals(instanceid) && statue == 1)
		{
			//判断模板是否共享
			PaasInstance pubpi=null;
			boolean needcreateInstance=false;
			if("must".equals(pt.getUserMode()))
			{
				//是共享模板查找共享实例
				pubpi=paasInstanceImp.getInstanceByTemplateId(pt.getId());
				if(pubpi == null)
				{
					needcreateInstance=true;
				}
			}else
			{
				needcreateInstance=true;
			}
			if(needcreateInstance)
			{
				//创建实例
				PaasInstance pi=new PaasInstance();
				pi.setTemplateId(pt.getId());
				pi.setInstanceName("default name");
				pi.setInstanceStatus(0);
				try
				{
					paasInstanceImp.insert(pi);
					po.setInstanceId(pi.getInstanceId());
				}catch(Exception e)
				{
					return new Message("fail","创建实例失败！",new Date());
				}
			}else
			{
				//更新订单与实例关联
				po.setInstanceId(pubpi.getInstanceId());
			}
		}
		if(statue == 2 && !"".equals(instanceid))
		{
			//驳回 删除实例信息 -- 不是共享实例 置为撤销状态
			List<PaasInstance> list=paasInstanceImp.selectInstanceByirderud(instanceid);
			if(list.size()>0)
			{
				return new Message("fail","存在实例处于运行中，无法驳回！",new Date());
			}else
			{
				paasInstanceImp.deleteAllinstanceByOrderid(pt.getId());
			}
			//实例置空
			po.setInstanceId("");
		}
		try {
			int updateOrder=paasOrderImp.updateByPrimaryKeySelective(po);
		} catch (Exception e) {
			return new Message("fail","更新订单状态失败！",new Date());
		}
		return new Message("success","更新订单状态成功！",new Date());
	}
}
