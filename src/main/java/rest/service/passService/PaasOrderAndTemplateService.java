package rest.service.passService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import rest.mybatis.dao.passDao.PaasInstanceMapper;
import rest.mybatis.dao.passDao.PaasOrdTenantOrgRMapper;
import rest.mybatis.dao.passDao.PaasOrderMapper;
import rest.mybatis.dao.passDao.PaasTemplateMapper;
import rest.mybatis.model.passModel.PaasInstance;
import rest.mybatis.model.passModel.PaasOrdTenantOrgR;
import rest.mybatis.model.passModel.PaasOrder;
import rest.mybatis.model.passModel.PaasTemplate;
import rest.page.util.PageUtil;
import rest.page.util.Pageinfo;


@RestController
public class PaasOrderAndTemplateService {
	
	@Autowired
	private PaasOrderMapper passordermapper;
	@Autowired
	private  PaasInstanceMapper paasInstanceMapper;
	@Autowired
	private PaasOrdTenantOrgRMapper paasOrdTenantOrgMapper;
	@Autowired
	private PaasTemplateMapper paasTemplateMapper;
	@Autowired
	private PageUtil pageutil;
	//创建订单he维护订单租户和组织机构的关系
	@RequestMapping(value="/passService/createPaasOrder",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public void createPaasOrder(@RequestBody PaasOrder paasOrder,@RequestParam("ids")ArrayList<String> ids,@RequestParam("tenantId") String tenantId){
		PaasOrdTenantOrgR orgR = new PaasOrdTenantOrgR();
		String uuid = UUID.randomUUID().toString();
		paasOrder.setBillNo(uuid);
		passordermapper.insertSelective(paasOrder);
		PaasOrder order = passordermapper.selectByUUID(uuid);
		orgR.setOrdId(order.getId());
		orgR.setTenantId(tenantId);
		for (String integer : ids) {
			orgR.setOrgId(integer);
			paasOrdTenantOrgMapper.insert(orgR);
		}
	}
	//查询已购买的应用（订单，模板，实例）
	@RequestMapping(value="/passService/showApplicationList",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Pageinfo showApplicationList(@RequestParam(value="page",defaultValue="1") String page,
			@RequestParam(value="tenantId") String tenantId,
			@RequestParam(value="instanceName") String instanceName,
			@RequestParam(value="templateCategory") String templateCategory,
			@RequestParam(value="counm",defaultValue="10") Integer counm){
			
			Integer num = passordermapper.selectOrderCount(tenantId);
			/*PageHelper.startPage(page, counm);*/
			Pageinfo pi=pageutil.initpage(num, page);
			int i = Integer.parseInt(page);
			List<PaasOrder> list = passordermapper.selectByCondition(i,tenantId, instanceName, templateCategory,counm);
			/*PageInfo pageInfo = new PageInfo(list);*/
			pi.setResultObj(list);
			return pi;
	}
	//查询应用详情(实例详情)
	@RequestMapping(value="/passService/showApplicationDetails",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaasInstance showApplicationDetails(@RequestParam("orderId") Integer orderId){
		PaasInstance paasInstance = paasInstanceMapper.selectByorderId(orderId);
		return paasInstance;
	}
	//获取应用实例和组织机构的关系
	@RequestMapping(value="/passService/getInstanceAndOrgShip",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<PaasOrdTenantOrgR> getInstanceAndOrgShip(@RequestParam("orderId") Integer orderId){
		List<PaasOrdTenantOrgR> list = paasOrdTenantOrgMapper.selectPaasOrdTenantOrgRByOrderID(orderId);
		return list;
	}
	//插入订单组织机构关系
	@RequestMapping(value="/passService/addInstanceAndOrgShip",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void addInstanceAndOrgShip(@RequestParam("ids")ArrayList<String> ids,@RequestParam("tenantId") String tenantId,@RequestParam("orderId") Integer orderId){
		PaasOrdTenantOrgR orgR = new PaasOrdTenantOrgR();
		orgR.setTenantId(tenantId);
		orgR.setOrdId(orderId);
		for (String integer : ids) {
			orgR.setOrgId(integer);
			paasOrdTenantOrgMapper.insert(orgR);
		}
	}
	
	//删除订单组织机构关系
	@RequestMapping(value="/passService/deleteInstanceAndOrgShip",method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void deleteInstanceAndOrgShip(@RequestParam("orderId") Integer orderId){
		paasOrdTenantOrgMapper.deleteByOrderID(orderId);
	}
	
	//查询已发布的产品列表（订单，模板，实例）
		@RequestMapping(value="/passService/showTempliteList",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public Pageinfo showTempliteList(@RequestParam(value="page",defaultValue="1") Integer page,
				@RequestParam(value="id",required=false) Integer id,
				@RequestParam(value="templateName",required=false) String templateName,
				@RequestParam(value="templateCategory",required=false) String templateCategory,
				@RequestParam(value="counm",defaultValue="10") Integer counm){
				
				Integer num = paasTemplateMapper.selecttemplateCount();
				Pageinfo pi=pageutil.initpage(num, page.toString());
				List<PaasTemplate> list = paasTemplateMapper.selectpaasTemplateList(page,id,templateName, templateCategory, counm);
				pi.setResultObj(list);
				return pi;
		}
		
		//获取模板分类
		@RequestMapping(value="/passService/getTemplateCategorys",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public List<PaasTemplate> getTemplateCategorys(){
			List<PaasTemplate> list = paasTemplateMapper.selecttemplateCategory();
			return list;
		}
		
		//撤销定单
		@RequestMapping(value="/passService/repealOrder",method=RequestMethod.PUT,produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public void repealOrder(@RequestParam("orderId") Integer orderId){
			PaasOrder record = new PaasOrder();
			record.setId(orderId);
			record.setStatus(3);//3 状态“撤销”
			PaasInstance instance = new PaasInstance();
			instance.setInstanceStatus(2);//2状态“撤销”
			passordermapper.updateByPrimaryKeySelective(record);
			PaasOrder order = passordermapper.selectByPrimaryKey(orderId);
			String instanceId = order.getInstanceId();
			instance.setInstanceId(instanceId);
			paasInstanceMapper.updateByPrimaryKeySelective(instance);
		}
}
