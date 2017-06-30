package rest.service.otherSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.annotations.ApiOperation;

import rest.mybatis.dao.passDao.PaasOrdTenantOrgRMapper;
import rest.mybatis.dao.passDao.PaasInstanceMapper;
import rest.mybatis.dao.passDao.PaasTemplateMapper;
import rest.mybatis.dao.passDao.PaasUserSubOrgMapper;
import rest.mybatis.dao.passDao.Imp.PaasInstanceImp;
import rest.mybatis.model.passModel.PaasOrdTenantOrgR;
import rest.mybatis.dao.passDao.PaasOrderMapper;
import rest.mybatis.dao.passDao.PaasSubserviceMapper;
import rest.mybatis.model.passModel.PaasInstance;
import rest.mybatis.model.passModel.PaasOrder;
import rest.mybatis.model.passModel.PaasSubservice;
import rest.mybatis.model.passModel.PaasTemplate;
import rest.mybatis.model.passModel.PaasUserSubOrg;
import rest.otherSystem.Obj.InstanceidTenentid;
import rest.otherSystem.Obj.OrgidsInstanceid;
import rest.page.util.Message;
import rest.page.util.RequestUtil;

@RestController
public class PaasForOtherService {
	@Autowired
	private PaasInstanceImp paasInstanceImp;
	@Autowired
	private RequestUtil requestUtil;
	
	@Autowired
	private PaasTemplateMapper paasTemplateMapper;
	@Autowired
	private PaasInstanceMapper paasInstanceMapper;
	@Autowired
	private PaasOrdTenantOrgRMapper paasOrdTenantOrgRMapper;
	@Autowired
	private PaasUserSubOrgMapper paasUserSubOrgMapper;
	@Autowired
	private PaasOrderMapper paasOrderMapper;
	@Autowired
	private PaasSubserviceMapper paasSubserviceMapper;
	/**
	 * 根据组织机构id数组和应用实例id获取租户id列表。
	 * 需要运营管理平台提供 根据应用实例id和组织机构（用户群）获取租户列表的接口；组织机构可以为空，此时返回购买了当前应用实例的所有租户
	 */
	@ApiOperation(value="获取租户id列表",notes="根据组织机构id数组和应用实例id获取租户id列表")
	@RequestMapping(value="/paasService/findTenentsByOrgsAppid",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public Message findTenentsByOrgsTenentid(@RequestBody OrgidsInstanceid orgidsInstanceid ){
		List<String> list = null;
		list=new ArrayList<String>();
		List<PaasOrdTenantOrgR> paasOrdTenantOrgRsList = paasOrdTenantOrgRMapper.findTenentsByOrgsAppid(orgidsInstanceid);
		for (PaasOrdTenantOrgR paasOrdTenantOrgR : paasOrdTenantOrgRsList) {
			String tenantId = paasOrdTenantOrgR.getTenantId();
			list.add(tenantId);
		}
		return new Message("200","success:查询成功" ,new Date(),list);
	}
	
	/**
	 * SaaS服务根据应用实例id和租户id同步用户信息。
	 * 当SaaS服务为非共享服务时，可以仅传递应用实例id，否则用户id和应用实例id需要同时传递。
	 * 需要运营管理平台提供 根据应用实例id和租户id返回组织机构（用户群）的接口，应用实例id为非共享时，租户id可以为空
	 */
	@ApiOperation(value="获取组织机构id列表",notes="根据应用实例id和租户id返回组织机构（用户群）;应用实例id为非共享时，租户id可以为空")
	@RequestMapping(value="/paasService/findOrgidsByInstanceidTenentid",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Message findOrgidsByInstanceidTenentid(@RequestBody InstanceidTenentid instanceidTenentid){
		
		/*list=new ArrayList<String>();
		list.add("11C5EDB2-8B09-4457-BF20-19D5336C8513");
		list.add("161C994E-5650-448E-AB49-26E027E9D26C");*/
		Map<String,String> selMap = new HashMap<String,String>();
		//1.获取参数
		String instanceId = instanceidTenentid.getInstanceid();
		String tenentId = instanceidTenentid.getTenantid();
		selMap.put("instanceId", instanceId);
		selMap.put("tenenId", tenentId);
		if(null == instanceId || "".equals(instanceId)){
			return	new Message("001","fail:实例id为空" ,new Date(),null);
		}
		List<PaasOrdTenantOrgR> resultList = new ArrayList<PaasOrdTenantOrgR>();
		List<String> resultStrs = new ArrayList<String>();
		//2.根据实例id 查询模板，判断是否共享
		PaasInstance instance_T = paasInstanceMapper.selectByPrimaryKey(instanceId);
		if(null != instance_T){
			PaasTemplate template_t = paasTemplateMapper.selectByPrimaryKey(instance_T.getTemplateId());
			if(null != template_t){
				//根据实例id查询出orderId集合
				List<Integer> orderIds = paasOrderMapper.selectByInstanceId(instanceId);
				//根据orderids 查询关系表
				resultList = paasOrdTenantOrgRMapper.selectByOrderIdstemp(orderIds); 
				String userMode = template_t.getUserMode();
				//判断是否共享
				if("share".equals(userMode)){
					//3.1是共享:不再通过租户id过滤
					for(PaasOrdTenantOrgR r :resultList){
						resultStrs.add(r.getOrgId());
					}
					
				}else{
					if(null != tenentId){
					//3.2根据实例id和租户id查询用户群：根据实例查询订单，根据订单和租户查询组织机构
						for(PaasOrdTenantOrgR r :resultList){
							if(null != r.getTenantId() && tenentId.equals(r.getTenantId())){
								resultStrs.add(r.getOrgId());
							}
						}
					}else{
					     return	new Message("000","fail:该实例非共享,但租户id为null" ,new Date(),null);
					}
				}
			}
		}
		
		return new Message("111","success:查询成功" ,new Date(),resultStrs);
	}
	
	/**
	  * 判断用户是否可以访问应用实例url,cas服务器端需根据此判断是否直接跳转系统而不需要登陆
	  * 根据用户id和实例url判断用户是否可以访问
	 * @throws IOException 
	  */
	@ApiOperation(value="根据应用实例id和租户id返回是否可以访问",notes="根据应用实例id和租户id返回是否可以访问")
	@RequestMapping(value="/rest/productService/ispermit",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public Message ispermit(@RequestParam(value="userid") String userid,@RequestParam(value="url") String url) throws IOException{
		List<PaasOrder> reslut=new ArrayList<PaasOrder>();
		JSONObject jsono=requestUtil.getContent(userid);
		JSONArray ja=jsono.getJSONArray("userList");
		JSONObject joo=ja.getJSONObject(0);
		JSONArray organizationIdListja=joo.getJSONArray("organizationIdList");
		String orgstr=organizationIdListja.toString();
		orgstr=orgstr.substring(orgstr.indexOf("[")+1, orgstr.lastIndexOf("]"));
		String[] orgary=orgstr.split(",");
		boolean result=false;
		for (int i = 0; i < orgary.length; i++) {
			String orjcode=orgary[i].toString();
			orjcode=orjcode.replaceAll("\"", "");
			boolean conresult=paasInstanceImp.getInstancesByorgid(orjcode,url);
			if(conresult)
			{
				result=true;
				break;
			}
		}
		if(result)
		{
			return new Message("200", "有访问权！", new Date());
		}else
		{
			return new Message("401", "无访问权！", new Date());
		}
	}
	
		@ApiOperation(value="根据应用实例id和租户id返回是否可以访问",notes="根据应用实例id和租户id返回是否可以访问")
		@RequestMapping(value="/rest/productService/ispermitnew",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
		public Message ispermitnew(@RequestParam(value="userid") String userid,@RequestParam(value="url") String url) throws IOException{
			List<PaasOrder> reslut=new ArrayList<PaasOrder>();
			JSONObject jsono=requestUtil.getContent(userid);
			JSONArray ja=jsono.getJSONArray("userList");
			JSONObject joo=ja.getJSONObject(0);
			JSONArray organizationIdListja=joo.getJSONArray("organizationIdList");
			String orgstr=organizationIdListja.toString();
			orgstr=orgstr.substring(orgstr.indexOf("[")+1, orgstr.lastIndexOf("]"));
			String st = userid;
			orgstr = orgstr+","+st;
			String[] userOrOrdIds = orgstr.split(",");
			ArrayList<String> arrayList = new ArrayList<String>();
			for (String string : userOrOrdIds) {
				string=string.replaceAll("\"", "");
				arrayList.add(string);
				System.out.println(string);
			}
			boolean result=false;
			List<PaasUserSubOrg> list = paasUserSubOrgMapper.selectModelsByuserOrOrdIds(arrayList);
			for (PaasUserSubOrg paasUserSubOrg : list) {
				String billNo = paasUserSubOrg.getBillNo();
				PaasOrder order = paasOrderMapper.selectByUUID(billNo);
				String instanceId = order.getInstanceId();
				PaasInstance paasInstance = paasInstanceMapper.selectByPrimaryKey(instanceId);
				Integer templateId = paasInstance.getTemplateId();
				String pubDns = paasInstance.getPubDns();
				String urlPrefix = paasInstance.getUrlPrefix();
				List<PaasSubservice> sublist = paasSubserviceMapper.selectSubServiceBytemplateId(templateId);
				for (PaasSubservice paasSubservice : sublist) {
					String moduleUrl = paasSubservice.getModuleUrl();
					String pjModleUrl=pubDns+urlPrefix+moduleUrl;
					String pjUrl = pubDns+urlPrefix;
					if(url.equals(pjModleUrl)||url.equals(pjUrl)){
						result=true;
						break;
					}
				}
			}
			if(result)
			{
				return new Message("200", "有访问权！", new Date());
			}else
			{
				return new Message("401", "无访问权！", new Date());
			}
		}
}
