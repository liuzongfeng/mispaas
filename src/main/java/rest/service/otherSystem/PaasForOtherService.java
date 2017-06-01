package rest.service.otherSystem;

import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rest.mybatis.dao.passDao.PaasInstanceMapper;
import rest.mybatis.dao.passDao.PaasTemplateMapper;
import rest.mybatis.dao.passDao.Imp.PaasInstanceImp;
import rest.mybatis.model.passModel.PaasInstance;
import rest.mybatis.model.passModel.PaasOrder;
import rest.mybatis.model.passModel.PaasTemplate;
import rest.otherSystem.Obj.InstanceidTenentid;
import rest.otherSystem.Obj.OrgidsInstanceid;
import rest.page.util.Message;
import rest.page.util.PageUtil;
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
	/**
	 * 根据组织机构id数组和应用实例id获取租户id列表。
	 * 需要运营管理平台提供 根据应用实例id和组织机构（用户群）获取租户列表的接口；组织机构可以为空，此时返回购买了当前应用实例的所有租户
	 */
	@ApiOperation(value="获取租户id列表",notes="根据组织机构id数组和应用实例id获取租户id列表")
	@RequestMapping(value="/paasService/findTenentsByOrgsAppid",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public List<String> findTenentsByOrgsTenentid(@RequestBody OrgidsInstanceid orgidsInstanceid ){
		System.out.println(orgidsInstanceid.getInstanceId());
		String a[]=orgidsInstanceid.getOrgIds();
		System.out.println(a);
		List<String> list = null;
		list=new ArrayList<String>();
		list.add("admin");
		list.add("54FAE94D1FFA3VN77D");
		return list;
	}
	
	/**
	 * SaaS服务根据应用实例id和租户id同步用户信息。
	 * 当SaaS服务为非共享服务时，可以仅传递应用实例id，否则用户id和应用实例id需要同时传递。
	 * 需要运营管理平台提供 根据应用实例id和租户id返回组织机构（用户群）的接口，应用实例id为非共享时，租户id可以为空
	 */
	@ApiOperation(value="获取组织机构id列表",notes="根据应用实例id和租户id返回组织机构（用户群）;应用实例id为非共享时，租户id可以为空")
	@RequestMapping(value="/paasService/findOrgidsByInstanceidTenentid",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public List<String> findOrgidsByInstanceidTenentid(@RequestBody InstanceidTenentid instanceidTenentid){
		List<String> list=null;
		/*list=new ArrayList<String>();
		list.add("11C5EDB2-8B09-4457-BF20-19D5336C8513");
		list.add("161C994E-5650-448E-AB49-26E027E9D26C");*/
		//1.获取参数
		String instanceId = instanceidTenentid.getInstanceid();
		String tenentId = instanceidTenentid.getTenentid();
		//2.根据实例id 查询模板，判断是否共享
		PaasInstance instance_T = paasInstanceMapper.selectByPrimaryKey(instanceId);
		
		PaasTemplate template_t = paasTemplateMapper.selectByPrimaryKey(instance_T.getTemplateId());
		String userMode = template_t.getUserMode();
		if("share".equals(userMode)){
			//3.1根据实例id查询用户群
			
		}else{
			if(null != tenentId){
			//3.2根据实例id和租户id查询用户群
			}
		}
		
		return list;
	}
	
	/**
	  * 判断用户是否可以访问应用实例url,cas服务器端需根据此判断是否直接跳转系统而不需要登陆
	  * 根据用户id和实例url判断用户是否可以访问
	 * @throws IOException 
	  */
	@ApiOperation(value="根据应用实例id和租户id返回是否可以访问",notes="根据应用实例id和租户id返回是否可以访问")
	@RequestMapping(value="/rest/productService/ispermit/{userid}/{instanceId}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public Message ispermit(@PathVariable(value="userid") String userid,@PathVariable(value="instanceId") String instanceId) throws IOException{
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
			boolean conresult=paasInstanceImp.getInstancesByorgid(orjcode,instanceId);
			if(conresult)
			{
				result=true;
				break;
			}
		}
		if(result)
		{
			return new Message("success", "有访问权！", new Date());
		}else
		{
			return new Message("fail", "无访问权！", new Date());
		}
	}
}
