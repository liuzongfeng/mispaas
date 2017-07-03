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

import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rest.mybatis.dao.passDao.PaasInstanceMapper;
import rest.mybatis.dao.passDao.PaasOrdTenantOrgRMapper;
import rest.mybatis.dao.passDao.PaasOrderMapper;
import rest.mybatis.dao.passDao.PaasTemplateMapper;
import rest.mybatis.dao.passDao.PaasUserSubOrgMapper;
import rest.mybatis.model.passModel.PaasInstance;
import rest.mybatis.model.passModel.PaasOrdTenantOrgR;
import rest.mybatis.model.passModel.PaasOrder;
import rest.mybatis.model.passModel.PaasTemplate;
import rest.mybatis.model.passModel.PaasUserSubOrg;
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
	@Autowired
	private PaasUserSubOrgMapper paasUserSubOrgMapper;
	//创建订单he维护订单租户和组织机构的关系
	@ApiOperation(value="创建订单",notes="创建订单(PaasOrder)并且维护订单，租户，组织机构的关系")
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
	@ApiOperation(value="创建订单",notes="创建订单(PaasOrder)并且维护订单，租户，组织机构的关系")
	@RequestMapping(value="/passService/createPaasOrderNew",method=RequestMethod.POST)
	public void createPaasOrderNew(@RequestBody PaasOrder paasOrder,@RequestParam("tenantId") String tenantId,@RequestParam(value="clicklist",required=false)ArrayList<Object> clicklist,
			@RequestParam("adduser") String adduser){
		
		PaasUserSubOrg paasUserSubOrg = new PaasUserSubOrg();
		String uuid = UUID.randomUUID().toString();
		paasOrder.setBillNo(uuid);
		passordermapper.insertSelective(paasOrder);
		paasUserSubOrg.setBillNo(uuid);
		paasUserSubOrg.setTenantId(tenantId);
		if(clicklist!=null){
			JSONArray array = JSONArray.fromObject(clicklist);
			for (Object object : array) {
					JSONObject jo=JSONObject.fromObject(object);
					JSONArray jsonArray = jo.getJSONArray("clicklist");
					if(jsonArray!=null){
						for (Object object2 : jsonArray) {
							JSONObject jo2=JSONObject.fromObject(object2);
							String modelId = jo2.getString("modelId");
							//paasUserSubOrgMapper.deleteByBiilNumAndMoldeId(uuid, modelId);
							paasUserSubOrg.setSubserviceId(modelId);
							JSONArray jsonArray2 = jo2.getJSONArray("checkIds");
							for (Object object3 : jsonArray2) {
								String id = object3.toString();
								String[] split = id.split("\\|");
								if(!split[0].equals("")){
									paasUserSubOrg.setUserOrOrdId(split[0]);
									paasUserSubOrg.setOrgId(split[1]);
									if(id.indexOf("/")==-1){
										paasUserSubOrg.setGranularity(1);
										
									}else{
										paasUserSubOrg.setGranularity(0);
									}
									paasUserSubOrgMapper.insert(paasUserSubOrg);
								}
							}
						}
					}
			}
		}
		if(adduser!=null){
			
			JSONObject userObject = JSONObject.fromObject(adduser);
			if(userObject!=null){
				
				JSONArray userList = userObject.getJSONArray("userList");
				if(userList.size()>0){
					for (Object object : userList) {
						JSONObject jo2=JSONObject.fromObject(object);
						String modelId = (String) jo2.get("modelId");
						String userId = (String) jo2.get("userId");
						String userName = (String) jo2.get("userName");
						paasUserSubOrg.setGranularity(0);
						paasUserSubOrg.setOrgId(userName);
						paasUserSubOrg.setSubserviceId(modelId);
						paasUserSubOrg.setUserOrOrdId(userId);
					
						paasUserSubOrgMapper.insert(paasUserSubOrg);
					}
				}
				
			}
		}
	}
	//查询已购买的应用（订单，模板，实例）
	@ApiOperation(value="查询用户已购买的应用",notes="根据用户查到用户所拥有的的租户列表id，在根据租户列表id获取购买的应用")
	@RequestMapping(value="/passService/showApplicationList",method=RequestMethod.POST)
	@ResponseBody
	public Pageinfo showApplicationList(@RequestParam(value="page",defaultValue="1") String page,
			@RequestBody String tetantList,
			@RequestParam(value="instanceName") String instanceName,
			@RequestParam(value="templateCategory") String templateCategory,
			@RequestParam(value="counm",defaultValue="10") Integer counm){
			Integer num = 0;
			/*PageHelper.startPage(page, counm);*/
			
			int i = Integer.parseInt(page);
			List<PaasOrder> list = new ArrayList<PaasOrder>();
			JSONObject jo=JSONObject.fromObject(tetantList);
			JSONArray jsonArray = jo.getJSONArray("tenantList");
			for (Object object : jsonArray) {
				JSONObject jsonObject = (JSONObject) object;
				Object object2 = jsonObject.get("id");
				String tenantId = object2.toString();
				 num = num+passordermapper.selectOrderCount(tenantId);
				 List<PaasOrder> passlist = passordermapper.selectByCondition(i,tenantId,instanceName, templateCategory, counm);
					for (PaasOrder paasOrder : passlist) {
						list.add(paasOrder);
					}
			}
			Pageinfo pi=pageutil.initpage(num, page,counm);
			pi.setBegin(num);
			/*PageInfo pageInfo = new PageInfo(list);*/
			pi.setResultObj(list);
			return pi;
	}
	//查询应用详情(实例详情)
	@ApiOperation(value="查询应用详情",notes="根据订单id获取该订单购买的产品id，根据产品Id获取详情")
	@RequestMapping(value="/passService/showApplicationDetails",method=RequestMethod.GET)
	@ResponseBody
	public Object showApplicationDetails(@RequestParam("orderId") Integer orderId){
		PaasOrder paasOrder = passordermapper.selectByPrimaryId(orderId);
		return paasOrder;
	}
	//获取应用实例和组织机构的关系
	@ApiOperation(value="获取应用实例和组织机构的关系",notes="根据订单id获取该订单与应用和组织机构的关系")
	@RequestMapping(value="/passService/getInstanceAndOrgShip",method=RequestMethod.GET)
	@ResponseBody
	public List<PaasOrdTenantOrgR> getInstanceAndOrgShip(@RequestParam("orderId") Integer orderId){
		List<PaasOrdTenantOrgR> list = paasOrdTenantOrgMapper.selectPaasOrdTenantOrgRByOrderID(orderId);
		return list;
	}
	@ApiOperation(value="获取应用实例和组织机构的关系",notes="根据订单id获取该订单与应用和组织机构的关系")
	@RequestMapping(value="/passService/getInstanceAndOrgShip_new",method=RequestMethod.GET)
	@ResponseBody
	public List<PaasUserSubOrg> getInstanceAndOrgShip_new(@RequestParam("orderId") Integer orderId){
		PaasOrder paasOrder = passordermapper.selectByPrimaryId(orderId);
		String billNo = paasOrder.getBillNo();
		List<PaasUserSubOrg> list = paasUserSubOrgMapper.selectByBillno(billNo);
		return list;
	}
	//获取用户和组织机构的以及模块的关系
	@ApiOperation(value="获取用户和组织机构的以及模块的关系",notes="根据订单ID获取用户和组织机构的以及模块的关系")
	@RequestMapping(value="/passService/getuserAndOrgShip_new",method=RequestMethod.GET)
	@ResponseBody
	public List<PaasUserSubOrg> getuserAndOrgShip_new(@RequestParam("orderId") Integer orderId){
		PaasOrder paasOrder = passordermapper.selectByPrimaryId(orderId);
		String billNo = paasOrder.getBillNo();
		List<PaasUserSubOrg> list = paasUserSubOrgMapper.selectByBillnoandType(billNo);
		return list;
	}
	@ApiOperation(value="获取用户和组织机构的以及模块的关系",notes="根据订单ID获取用户和组织机构的以及模块的关系")
	@RequestMapping(value="/passService/getuserAndOrgShip2",method=RequestMethod.GET)
	@ResponseBody
	public List<PaasUserSubOrg> getuserAndOrgShip2(@RequestParam("billNo") String billNo){
		List<PaasUserSubOrg> list = paasUserSubOrgMapper.selectByBillnoandType(billNo);
		return list;
	}
	//插入订单组织机构关系
	@ApiOperation(value="插入订单组织机构关系",notes="为某个订单根据订单ID添加组织机构的关系")
	@RequestMapping(value="/passService/addInstanceAndOrgShip",method=RequestMethod.POST)
	@ResponseBody
	public void addInstanceAndOrgShip(@RequestParam(value="ids",required=false)ArrayList<String> ids,@RequestParam("tenantId") String tenantId,
			@RequestParam("orderId") Integer orderId,
			@RequestParam(value="names",required=false)ArrayList<String> names){
		PaasOrder order = passordermapper.selectByPrimaryId(orderId);
		String tenantId2 = order.getTenantId();
		PaasOrdTenantOrgR orgR = new PaasOrdTenantOrgR();
		orgR.setTenantId(tenantId2);
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
	//插入订单组织机构关系2
		@ApiOperation(value="插入订单组织机构关系",notes="为某个订单根据订单ID添加组织机构的关系")
		@RequestMapping(value="/passService/addInstanceAndOrgShip_New",method=RequestMethod.POST)
		@ResponseBody
		public void addInstanceAndOrgShip_New(
				@RequestParam("orderId") Integer orderId,
				@RequestParam(value="clicklist",required=false)ArrayList<Object> clicklist){
			PaasUserSubOrg paasUserSubOrg = new PaasUserSubOrg();
			PaasOrder order = passordermapper.selectByPrimaryId(orderId);
			String tenantId2 = order.getTenantId();
			String billNo = order.getBillNo();
			paasUserSubOrg.setBillNo(billNo);
			paasUserSubOrg.setTenantId(tenantId2);
			if(clicklist!=null){
				JSONArray array = JSONArray.fromObject(clicklist);
				for (Object object : array) {
						JSONObject jo=JSONObject.fromObject(object);
						JSONArray jsonArray = jo.getJSONArray("clicklist");
						if(jsonArray!=null){
							for (Object object2 : jsonArray) {
								JSONObject jo2=JSONObject.fromObject(object2);
								String modelId = jo2.getString("modelId");
								paasUserSubOrgMapper.deleteByBiilNumAndMoldeId(billNo, modelId);
								paasUserSubOrg.setSubserviceId(modelId);
								JSONArray jsonArray2 = jo2.getJSONArray("checkIds");
								for (Object object3 : jsonArray2) {
									String id = object3.toString();
									String[] split = id.split("\\|");
									if(!split[0].equals("")){
										paasUserSubOrg.setUserOrOrdId(split[0]);
										paasUserSubOrg.setOrgId(split[1]);
										if(id.indexOf("/")==-1){
											paasUserSubOrg.setGranularity(1);
											
										}else{
											paasUserSubOrg.setGranularity(0);
										}
										paasUserSubOrgMapper.insert(paasUserSubOrg);
									}
								}
							}
				}
			}
		}
	}
		//插入用户订单模块关系
		@ApiOperation(value="插入用户订单模块关系",notes="插入用户订单模块关系")
		@RequestMapping(value="/passService/adduserAndOrgShip",method=RequestMethod.POST)
		@ResponseBody
		public void adduserAndOrgShip(@RequestBody String adduser){
			PaasUserSubOrg paasUserSubOrg = new PaasUserSubOrg();
			JSONObject userObject = JSONObject.fromObject(adduser);
			if(userObject!=null){
				Integer orderId = (Integer) userObject.get("orderId");
				//Integer valueOf = Integer.valueOf(orderId);
				PaasOrder order = passordermapper.selectByPrimaryId(orderId);
				String tenantId = order.getTenantId();
				String billNo = order.getBillNo();
				paasUserSubOrg.setBillNo(billNo);
				paasUserSubOrg.setTenantId(tenantId);
				JSONArray userList = userObject.getJSONArray("userList");
				if(userList.size()>0){
					for (Object object : userList) {
						JSONObject jo2=JSONObject.fromObject(object);
						String modelId = (String) jo2.get("modelId");
						String userId = (String) jo2.get("userId");
						String userName = (String) jo2.get("userName");
						paasUserSubOrg.setGranularity(0);
						paasUserSubOrg.setOrgId(userName);
						paasUserSubOrg.setSubserviceId(modelId);
						paasUserSubOrg.setUserOrOrdId(userId);
						paasUserSubOrgMapper.deleteByBillNumMoeIdUserId(billNo, modelId, userId);
						paasUserSubOrgMapper.insert(paasUserSubOrg);
					}
				}
				
			}
		}
	//删除订单组织机构关系
	@ApiOperation(value="删除订单组织机构关系",notes="为某个订单根据订单ID删除组织机构的关系")
	@RequestMapping(value="/passService/deleteInstanceAndOrgShip",method=RequestMethod.DELETE)
	@ResponseBody
	public void deleteInstanceAndOrgShip(@RequestParam("orderId") Integer orderId){
		paasOrdTenantOrgMapper.deleteByOrderID(orderId);
	}
	//删除订单组织机构关系
		@ApiOperation(value="删除订单组织机构关系",notes="为某个订单根据订单ID删除组织机构的关系")
		@RequestMapping(value="/passService/deleteInstanceAndUserOrg",method=RequestMethod.DELETE)
		@ResponseBody
		public void deleteInstanceAndUserOrg(@RequestParam("orderId") Integer orderId,@RequestParam(value="clicklist",required=false)ArrayList<Object> clicklist){
			PaasOrder order = passordermapper.selectByPrimaryId(orderId);
			String billNo = order.getBillNo();
			if(clicklist!=null){
				JSONArray array = JSONArray.fromObject(clicklist);
				for (Object object : array) {
						JSONObject jo=JSONObject.fromObject(object);
						JSONArray jsonArray = jo.getJSONArray("clicklist");
						if(jsonArray!=null){
							for (Object object2 : jsonArray) {
								JSONObject jo2=JSONObject.fromObject(object2);
								String modelId = jo2.getString("modelId");
								paasUserSubOrgMapper.deleteByBiilNumAndMoldeId(billNo, modelId);
							}
				}
			}
		}
		}
		//批量删除用户订单模块关系
		@ApiOperation(value="批量删除用户订单模块关系",notes="维护用户模块和订单的关系")
		@RequestMapping(value="/passService/deleteuserAndOrgShip",method=RequestMethod.DELETE)
		@ResponseBody
		public void deleteuserAndOrgShip(@RequestParam("orgIds") ArrayList<Integer> orgIds){
			for (Integer integer : orgIds) {
				if(integer!=null){
					paasUserSubOrgMapper.deleteByPrimaryKey(integer);
				}
			}
		}
	//重置模块用户关系
		@ApiOperation(value="删除订单组织机构关系",notes="为某个订单根据订单ID删除组织机构的关系")
		@RequestMapping(value="/passService/deleteAllInstanceAndUserOrg",method=RequestMethod.DELETE)
		@ResponseBody
		public void deleteAllInstanceAndUserOrg(@RequestParam("orderId") Integer orderId){
			PaasOrder order = passordermapper.selectByPrimaryId(orderId);
			String billNo = order.getBillNo();
			paasUserSubOrgMapper.deleteByBillNum(billNo);
		}
	//查询已发布的产品列表（订单，模板，实例）
		@ApiOperation(value="查询已发布的产品列表",notes="查询状态为发布的产品列表，应用了动态SQl实现，同时具有名字模糊查询和产品类别过滤的功能")
		@RequestMapping(value="/passService/showTempliteList",method=RequestMethod.POST)
		@ResponseBody
		public Pageinfo showTempliteList(@RequestParam(value="page",defaultValue="1") Integer page,
				@RequestParam(value="id",required=false) Integer id,
				@RequestParam(value="productName",required=false) String productName,
				@RequestParam(value="templateCategory",required=false) String templateCategory,
				@RequestParam(value="counm",defaultValue="10") Integer counm,HttpServletResponse res){
				res.setHeader("Access-Control-Allow-Origin", "*");
				Integer num = paasTemplateMapper.selecttemplateCount(id, productName, templateCategory);
				Pageinfo pi=pageutil.initpage(num, page.toString(),counm);
				pi.setBegin(num);
				List<PaasTemplate> list = paasTemplateMapper.selectpaasTemplateList(page,id,productName, templateCategory, counm);
				pi.setResultObj(list);
				return pi;
		}
		//名称模糊查询应用列表
		@ApiOperation(value="名称模糊查询应用列表",notes="名字模糊查询应用，和根据类别过滤应用")
		@RequestMapping(value="/passService/showApplicationListByInstanceName",method=RequestMethod.POST)
		@ResponseBody
		public Pageinfo showApplicationListByInstanceName(@RequestParam(value="page",defaultValue="1") String page,
				@RequestBody String tetantList,
				@RequestParam(value="instanceName") String instanceName,
				@RequestParam(value="templateCategory") String templateCategory,
				@RequestParam(value="counm",defaultValue="10") Integer counm){
					
				Integer num =0;
				/*PageHelper.startPage(page, counm);*/
				int i = Integer.parseInt(page);
				List<PaasOrder> list= new ArrayList<PaasOrder>();
				JSONObject jo=JSONObject.fromObject(tetantList);
				JSONArray jsonArray = jo.getJSONArray("tenantList");
				for (Object object : jsonArray) {
					JSONObject JSONObject = (JSONObject) object;
					Object object2 = JSONObject.get("id");
					String tenantId = object2.toString();
					List countBycondition = passordermapper.selectOrderCountBycondition(tenantId, instanceName, templateCategory);
					 num = num+countBycondition.size();
					 List<PaasOrder> passlist = passordermapper.selectByInstanceName(i,tenantId, instanceName, templateCategory,counm);
						for (PaasOrder paasOrder : passlist) {
							list.add(paasOrder);
						}
				}
				Pageinfo pi=pageutil.initpage(num, page,counm);
				
				/*PageInfo pageInfo = new PageInfo(list);*/
				pi.setResultObj(list);
				pi.setBegin(num);
				return pi;
		}
		//获取模板分类
		@ApiOperation(value="获取模板分类",notes="动态查询表中的所有模板列别，用于生成列别过滤条件下拉框")
		@RequestMapping(value="/passService/getTemplateCategorys",method=RequestMethod.GET)
		@ResponseBody
		public List<PaasTemplate> getTemplateCategorys(){
			List<PaasTemplate> list = paasTemplateMapper.selecttemplateCategory();
			return list;
		}
		
		//撤销定单
		@ApiOperation(value="撤销订单",notes="根据订单ID撤销订单，修改订单状态，和实例状态")
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
		@ApiOperation(value="获取组织结构",notes="底层调用第三方接口获取组织结构列表，用于生成组织机构树")
		@RequestMapping(value="/passService/getOrgtree",method=RequestMethod.GET)
		@ResponseBody
		public String getOrgtree(@RequestParam("geturl") String geturl) throws IOException{
			OrgRequestUtil util = new OrgRequestUtil();
			String orgjson = util.getContent(geturl);
			return orgjson;
		}
		//获取某用户的租户身份群
		@ApiOperation(value="获取某用户的租户身份群",notes="根据用户ID获取，用户的租户身份群用于加载已购买的应用列表")
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
		//根据组机构ID获取用户群
		@ApiOperation(value="获取组织结构",notes="底层调用第三方接口获取组织结构列表，用于生成组织机构树")
		@RequestMapping(value="/passService/getOrgWithUser",method=RequestMethod.GET)
		@ResponseBody
		public String getOrgWithUser(@RequestParam("OrgId")String orgId) throws IOException{
			String url="http://100.0.10.100:8080/usermanager/api/authorization/organization/users?pageStart=1&pageSize=1000&organizationId="+orgId;
			OrgRequestUtil util = new OrgRequestUtil();
			String userjson = util.getContent(url);
			return userjson;
		}
		//根据用户ID获取用户信息
		
		@ApiOperation(value="根据用户ID获取用户信息",notes="底层调用第三方接口获取用户信息，用于获取组织机构ID")
		@RequestMapping(value="/passService/getUserDetails",method=RequestMethod.GET)
		@ResponseBody
		public String getUserDetails(@RequestParam("userId")String userId) throws IOException{
			if(userId.indexOf("/key")>0){
				return userId;
			}else{
				String[] split = userId.split("/");
				String string = split[1];
				 String url="http://100.0.10.100:8080/usermanager/api/authorization/users/"+string;
					System.out.println(url);
					OrgRequestUtil util = new OrgRequestUtil();
					String userjson = util.getContent(url);
					return userjson;
			}
		}
		//根据用户名称模糊用户信息
		@ApiOperation(value="根据用户ID获取用户信息",notes="底层调用第三方接口获取用户信息，用于获取组织机构ID")
		@RequestMapping(value="/passService/getUserDetailsByName",method=RequestMethod.GET)
		@ResponseBody
		public String getUserDetailsByName(@RequestParam("userName")String userName,@RequestParam("page")String page) throws IOException{
			if(userName!=null){
				 String url="http://100.0.10.100:8080/usermanager/api/authorization/users/query?username="+userName+"&pageStart="+page+"&pageSize=5";
					System.out.println(url);
					OrgRequestUtil util = new OrgRequestUtil();
					String userjson = util.getContent(url);
					return userjson;
			}else{
				return null;
			}
		}
}
