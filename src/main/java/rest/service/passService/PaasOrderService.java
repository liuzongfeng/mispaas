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
/*import rest.mybatis.dao.passDao.Imp.PaasOrderImp;*/
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
	public Pageinfo getAllOrder(@RequestParam(name="TenantName",required=false,defaultValue="") String TenantName,@RequestParam(name="Status",required=false,defaultValue="") Integer Status,@RequestParam(name="CreateTime",required=false,defaultValue="") String CreateTime,@RequestParam(name="page",required=false,defaultValue="1") String page) throws ParseException
	{
		PaasOrder po=new PaasOrder();
		//从远端那到租户ID 然后封装
		po.setTenantName(TenantName);
		po.setStatus(Status);
		if(CreateTime!=null && !"".equals(CreateTime))
		{
			po.setCrateDate(new SimpleDateFormat("yyyy-MM-dd").parse(CreateTime));
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
	public Message updateOrder(@RequestParam(name="userId",required=false,defaultValue="1") Integer userId,@RequestParam(name="orderId",required=true) Integer orderId,@RequestParam(name="statue",required=true) Integer statue,@RequestParam(name="resion",required=false) String resion,@RequestParam(name="proId") Integer proId)
	{
		PaasOrder po=new PaasOrder();
		po.setId(orderId);
		po.setProId(proId);
		po.setStatus(statue);
		po.setApproveDescibe(resion);
		po.setApproveDate(new Date());
		po.setApproveId(userId);
		if(statue == 1)
		{
			PaasTemplate pt=paasTemplateImp.selectByPrimaryKey(proId);
			//创建实例
			PaasInstance pi=new PaasInstance();
			pi.setTemplateId(pt.getId());
			pi.setInstanceName("defaule name");
			pi.setInstanceStatus(0);
			pi.setOrderId(orderId);
			try
			{
				paasInstanceImp.insert(pi);
			}catch(Exception e)
			{
				return new Message("fail","创建实例失败！",new Date());
			}
		}
		try {
			int updateOrder=paasOrderImp.updateByPrimaryKeySelective(po);
		} catch (Exception e) {
			return new Message("fail","更新订单状态失败！",new Date());
		}
		return new Message("success","更新订单状态成功！",new Date());
	}
}
