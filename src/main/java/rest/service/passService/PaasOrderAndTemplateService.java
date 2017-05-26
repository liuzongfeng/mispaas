package rest.service.passService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rest.mybatis.dao.passDao.PaasInstanceMapper;
import rest.mybatis.dao.passDao.PaasOrdTenantOrgRMapper;
import rest.mybatis.dao.passDao.PaasOrderMapper;
import rest.mybatis.dao.passDao.PaasTemplateMapper;
import rest.mybatis.model.passModel.PaasInstance;
import rest.mybatis.model.passModel.PaasOrdTenantOrgR;
import rest.mybatis.model.passModel.PaasOrder;
import rest.mybatis.model.passModel.PaasTemplate;
import rest.page.util.OrgRequestUtil;
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
	@RequestMapping(value="/passService/createPaasOrder",method=RequestMethod.POST)
	public void createPaasOrder(@RequestBody PaasOrder paasOrder,@RequestParam(value="ids",required=false)ArrayList<String> ids,
			@RequestParam("tenantId") String tenantId,
			@RequestParam(value="names",required=false)ArrayList<String> names){
		PaasOrdTenantOrgR orgR = new PaasOrdTenantOrgR();
		String uuid = UUID.randomUUID().toString();
		paasOrder.setBillNo(uuid);
		passordermapper.insertSelective(paasOrder);
		PaasOrder order = passordermapper.selectByUUID(uuid);
		orgR.setOrdId(order.getId());
		orgR.setTenantId(tenantId);
		if(ids!=null){
			int size = ids.size();
			for(int i=0;i<size;i++){
				String id = ids.get(i);
				String name = names.get(i);
				orgR.setOrgId(id);
				orgR.setOrgName(name);paasOrdTenantOrgMapper.insert(orgR);
			}
		}
	}
	//查询已购买的应用（订单，模板，实例）
	@RequestMapping(value="/passService/showApplicationList",method=RequestMethod.POST)
	@ResponseBody
	public Pageinfo showApplicationList(@RequestParam(value="page",defaultValue="1") String page,
			@RequestParam(value="tenantId") String tenantId,
			@RequestParam(value="instanceName") String instanceName,
			@RequestParam(value="templateCategory") String templateCategory,
			@RequestParam(value="counm",defaultValue="10") Integer counm){
			
			Integer num = passordermapper.selectOrderCount(tenantId);
			/*PageHelper.startPage(page, counm);*/
			Pageinfo pi=pageutil.initpage(num, page);
			pi.setBegin(num);
			int i = Integer.parseInt(page);
			List<PaasOrder> list = passordermapper.selectByCondition(i,tenantId, instanceName, templateCategory,counm);
			/*PageInfo pageInfo = new PageInfo(list);*/
			pi.setResultObj(list);
			return pi;
	}
	//查询应用详情(实例详情)
	@RequestMapping(value="/passService/showApplicationDetails",method=RequestMethod.GET)
	@ResponseBody
	public Object showApplicationDetails(@RequestParam("orderId") Integer orderId){
		PaasOrder paasOrder = passordermapper.selectByPrimaryId(orderId);
		return paasOrder;
	}
	//获取应用实例和组织机构的关系
	@RequestMapping(value="/passService/getInstanceAndOrgShip",method=RequestMethod.GET)
	@ResponseBody
	public List<PaasOrdTenantOrgR> getInstanceAndOrgShip(@RequestParam("orderId") Integer orderId){
		List<PaasOrdTenantOrgR> list = paasOrdTenantOrgMapper.selectPaasOrdTenantOrgRByOrderID(orderId);
		return list;
	}
	//插入订单组织机构关系
	@RequestMapping(value="/passService/addInstanceAndOrgShip",method=RequestMethod.POST)
	@ResponseBody
	public void addInstanceAndOrgShip(@RequestParam(value="ids",required=false)ArrayList<String> ids,@RequestParam("tenantId") String tenantId,
			@RequestParam("orderId") Integer orderId,
			@RequestParam(value="names",required=false)ArrayList<String> names){
		PaasOrdTenantOrgR orgR = new PaasOrdTenantOrgR();
		orgR.setTenantId(tenantId);
		orgR.setOrdId(orderId);
		if(ids!=null){
			int size = ids.size();
			for(int i=0;i<size;i++){
				String id = ids.get(i);
				String name = names.get(i);
				orgR.setOrgId(id);
				orgR.setOrgName(name);
				paasOrdTenantOrgMapper.insert(orgR);
			}
		}
	}
	
	//删除订单组织机构关系
	@RequestMapping(value="/passService/deleteInstanceAndOrgShip",method=RequestMethod.DELETE)
	@ResponseBody
	public void deleteInstanceAndOrgShip(@RequestParam("orderId") Integer orderId){
		paasOrdTenantOrgMapper.deleteByOrderID(orderId);
	}
	
	//查询已发布的产品列表（订单，模板，实例）
		@RequestMapping(value="/passService/showTempliteList",method=RequestMethod.POST)
		@ResponseBody
		public Pageinfo showTempliteList(@RequestParam(value="page",defaultValue="1") Integer page,
				@RequestParam(value="id",required=false) Integer id,
				@RequestParam(value="productName",required=false) String productName,
				@RequestParam(value="templateCategory",required=false) String templateCategory,
				@RequestParam(value="counm",defaultValue="10") Integer counm,HttpServletResponse res){
				res.setHeader("Access-Control-Allow-Origin", "*");
				Integer num = paasTemplateMapper.selecttemplateCount(id, productName, templateCategory);
				Pageinfo pi=pageutil.initpage(num, page.toString());
				pi.setBegin(num);
				List<PaasTemplate> list = paasTemplateMapper.selectpaasTemplateList(page,id,productName, templateCategory, counm);
				pi.setResultObj(list);
				return pi;
		}
		//名称模糊查询应用列表
		@RequestMapping(value="/passService/showApplicationListByInstanceName",method=RequestMethod.POST)
		@ResponseBody
		public Pageinfo showApplicationListByInstanceName(@RequestParam(value="page",defaultValue="1") String page,
				@RequestParam(value="tenantId") String tenantId,
				@RequestParam(value="instanceName") String instanceName,
				@RequestParam(value="templateCategory") String templateCategory,
				@RequestParam(value="counm",defaultValue="10") Integer counm){
				
				List countBycondition = passordermapper.selectOrderCountBycondition(tenantId, instanceName, templateCategory);
				Integer num = countBycondition.size();
				
				/*PageHelper.startPage(page, counm);*/
				Pageinfo pi=pageutil.initpage(num, page);
				int i = Integer.parseInt(page);
				List<PaasOrder> list = passordermapper.selectByInstanceName(i,tenantId, instanceName, templateCategory,counm);
				/*PageInfo pageInfo = new PageInfo(list);*/
				pi.setResultObj(list);
				pi.setBegin(num);
				return pi;
		}
		//获取模板分类
		@RequestMapping(value="/passService/getTemplateCategorys",method=RequestMethod.GET)
		@ResponseBody
		public List<PaasTemplate> getTemplateCategorys(){
			List<PaasTemplate> list = paasTemplateMapper.selecttemplateCategory();
			return list;
		}
		
		//撤销定单
		@RequestMapping(value="/passService/repealOrder",method=RequestMethod.PUT)
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
		//获取组织机构
		@RequestMapping(value="/passService/getOrgtree",method=RequestMethod.GET)
		@ResponseBody
		public String getOrgtree(@RequestParam("geturl") String geturl) throws IOException{
			OrgRequestUtil util = new OrgRequestUtil();
			String orgjson = util.getContent(geturl);
			return orgjson;
		}
		//获取某用户的租户身份群
		@RequestMapping(value="/passService/gettenantList",method=RequestMethod.GET)
		@ResponseBody
		public String gettenantList(@RequestParam("userurl") String userurl,@RequestParam("tenanturl") String tenanturl) throws IOException{
			OrgRequestUtil util = new OrgRequestUtil();
			String orgjson = util.getContent(userurl);
			JSONObject jo=JSONObject.fromObject(orgjson);
			JSONArray userList = jo.getJSONArray("userList");
			JSONObject object = (JSONObject)userList.get(0);
			JSONArray tenantidList = object.getJSONArray("adminTenantList");
			ArrayList<JSONObject> tenantList = new ArrayList<JSONObject>();
			for (Object object2 : tenantidList) {
				String string = object2.toString();
				String geturl = tenanturl+string;
				String tentant = util.getContent(geturl);
				JSONObject tentantjo=JSONObject.fromObject(tentant);
				tenantList.add(tentantjo);
			}
			return tenantList.toString();
		}
		
}
