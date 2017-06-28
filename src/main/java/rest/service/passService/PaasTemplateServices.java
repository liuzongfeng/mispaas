package rest.service.passService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rest.mybatis.dao.passDao.Imp.PaasInstanceImp;
import rest.mybatis.dao.passDao.Imp.PaasTemplateImp;
import rest.mybatis.model.passModel.PaasInstance;
import rest.mybatis.model.passModel.PaasOrder;
import rest.mybatis.model.passModel.PaasSubservice;
import rest.mybatis.model.passModel.PaasTemplate;
import rest.page.util.Message;
import rest.page.util.PageUtil;
import rest.page.util.Pageinfo;
import rest.page.util.RequestUtil;
@RestController
public class PaasTemplateServices {
	@Autowired
	private PageUtil pageUtil;
	@Autowired
	private PaasTemplateImp paasTemplateImp;
	@Autowired
	private PaasInstanceImp paasInstanceImp;
	@Autowired
	private RequestUtil requestUtil;
	@RequestMapping("/rest/productService/getAllproduct")
	public Pageinfo getAllproductList(@RequestParam(value="ProductName",defaultValue="",required=false) String ProductName,@RequestParam(value="userModel",defaultValue="",required=false) String userModel,@RequestParam(value="ispub",defaultValue="",required=false) Integer ispub,@RequestParam(value="page",defaultValue="1",required=false) String page,@RequestParam(value="shownum",defaultValue="10",required=false) Integer shownum)
	{
		PaasTemplate paasTemplate=new PaasTemplate();
		paasTemplate.setTemplateName(ProductName);
		paasTemplate.setUserMode(userModel);
//		paasTemplate.setTemplateCategory(templateCategory);
		paasTemplate.setIsPub(ispub);
		int resultnum=paasTemplateImp.selectByCondition(paasTemplate);
		Pageinfo pi=pageUtil.initpage(resultnum,page,shownum);
		pi.setConditionObj(paasTemplate);
		List<PaasTemplate> resultlist=paasTemplateImp.selectObjByCondition(pi);
		pi.setResultObj(resultlist);
		return pi;
	}
	@RequestMapping("/rest/productService/editProduct")
	public Message updateProduct(@RequestParam(value="id",required=true) Integer id,@RequestParam(value="templateId",required=true) String templateId,@RequestParam(value="Version",required=true) String Version,@RequestParam(value="templateType",required=true) String templateType,@RequestParam(value="templateName",required=true) String templateName,@RequestParam(value="templateCategory",required=true) String templateCategory,@RequestParam(value="isPub",required=true) Integer isPub)
	{
		PaasTemplate pt=new PaasTemplate();
		pt.setId(id);
		pt.setTemplateId(templateId);
		pt.setVersion(Version);
		pt.setTemplateType(templateType);
		pt.setTemplateName(templateName);
		pt.setTemplateCategory(templateCategory);
		pt.setIsPub(isPub);
		return paasTemplateImp.updateProduct(pt);
	}
	
	/**
	 * 查询分类和状态信息
	 */
	@RequestMapping("/rest/productService/getTypeAndCategory")
	public Map<String, List> getTypeAndCategory()
	{
		Map<String, List> map=paasTemplateImp.getTypeAndMode();
		return map;
	}
	
	/**
	 * 获取门户信息
	 * 更具用户 获取所属组织机构然后获取 实力列表
	 * @throws IOException 
	 */
	@RequestMapping("/rest/productService/getIndexPageElement/{userid}")
	public List<PaasOrder> getIndexPageElement(@PathVariable(value="userid") String userid) throws IOException
	{
		List<PaasOrder> reslut=new ArrayList<PaasOrder>();
		JSONObject jsono=requestUtil.getContent(userid);
		JSONArray ja=jsono.getJSONArray("userList");
		JSONObject joo=ja.getJSONObject(0);
		JSONArray organizationIdListja=joo.getJSONArray("organizationIdList");
		String orgstr=organizationIdListja.toString();
		orgstr=orgstr.substring(orgstr.indexOf("[")+1, orgstr.lastIndexOf("]"));
		String[] orgary=orgstr.split(",");
		for (int i = 0; i < orgary.length; i++) {
			String orjcode=orgary[i].toString();
			orjcode=orjcode.replaceAll("\"", "");
//			List<PaasOrder> list=paasInstanceImp.getInstanceBytenantId("0BBCB174-64F8-4592-870D-6C8F73A03961");
			//根据组织机构ID获取 订单
//			List<PaasOrder> list=paasInstanceImp.getInstanceBytenantId(orjcode);
			//表结构修改 逻辑修改 updata by zx at time:2017年6月28日10:49:01
			PaasOrder order=paasInstanceImp.getInstancesByOrdIdandUserid(orjcode,userid);
			reslut.add(order);
		}
		return reslut;
	}
	
	@RequestMapping("/rest/productService/getusersprivilegeIdList/{userid}")
	public JSONArray getusersprivilegeIdList(@PathVariable(value="userid") String userid) throws IOException
	{
		JSONObject jsono=requestUtil.getContent(userid);
		JSONArray ja=jsono.getJSONArray("userList");
		JSONObject joo=ja.getJSONObject(0);
		JSONArray privilegeIdList=joo.getJSONArray("privilegeIdList");
		return privilegeIdList;
	}
	
	/**
	 * 更具用户ID url 查询是否有访问权限
	 */
//	@RequestMapping("/rest/productService/ispermit/{userid}/{url}")
//	public Message ispermit(@PathVariable(value="userid") String userid,@PathVariable(value="instanceId") String instanceId) throws IOException
	public Message ispermit(String userid,String url) throws IOException
	{
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
			return new Message("success", "有访问权！", new Date());
		}else
		{
			return new Message("fail", "无访问权！", new Date());
		}
	}
	@RequestMapping("/rest/productService/getMessageNum/{userid}")
	public Integer getMessageNum(@PathVariable(value="userid") String userid) throws IOException
	{
		Integer messagenum=0;
		String[] messagetype={"DOWNLOAD", "DISTRIBUTE", "COPYRIGHTMESSAGE", "RECOMMEND", "RESOURCEMANAGER","ALERT"};
		JSONObject sendjson=new JSONObject();
		sendjson.element("targetType", 1);
		sendjson.element("sourceType", 1);
		sendjson.element("readFlag", 0);
		sendjson.element("sourceAppId", userid);
		for (int i = 0; i < messagetype.length; i++) {
			sendjson.element("sourceAppId", messagetype[i]);
			JSONObject result=requestUtil.getmessageNum(sendjson);
			Integer num=(Integer)result.get("count");
			messagenum+=num;
		}
		return messagenum;
	}
}
