package rest.service.yunwei;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import rest.mybatis.dao.passDao.PaasInstanceMapper;
import rest.mybatis.model.passModel.PaasInstance;
import rest.security.UserInfo;

@Controller
public class InstanceService {

	@Autowired
	private PaasInstanceMapper paasInstanceMapper;
	
////////////////////////////////接口区域：start//////////////////////////////////////////////////////////////////////////
	/*@RequestMapping(value = "/obtainHelp", method = RequestMethod.GET)
	@ResponseBody
	public String obtainHelp(HttpSession session){
		
		SecurityContext context = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
		String attribute = (String)session.getAttribute("testSession");
		System.out.println(attribute);
		System.out.println(context);
		return null;
	}
	*/
	@RequestMapping(value = "/obtainUserInfo", method = RequestMethod.GET)
	@ResponseBody
//	@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = { "x-auth-token", "x-requested-with" })
	public UserInfo obtainUserInfo(HttpSession session){
		
		return (UserInfo)session.getAttribute(session.getId());
		
	}
	
	
	@RequestMapping(value = "/editInstance", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> editInstance(@RequestBody Map instance_s){
		String result = null;
		Map<String,String> map = new HashMap<String,String>();
		try {
			LinkedHashMap paasInstanceMap = (LinkedHashMap)instance_s.get("instance_s");
			
			String instanceId = (String)paasInstanceMap.get("instanceId");
			PaasInstance paasInstance = paasInstanceMapper.selectByPrimaryKey(instanceId);
			String instanceName = (String)paasInstanceMap.get("instanceName");       //实例名称
			Object instanceStatusObj = paasInstanceMap.get("instanceStatus"); //实例状态
			Integer instanceStatus = null;
			if(instanceStatusObj instanceof Integer){
				instanceStatus = (Integer)instanceStatusObj; 
			}
			if(instanceStatusObj instanceof String){
				String instanceStatus_str = (String)instanceStatusObj;
				instanceStatus = Integer.parseInt(instanceStatus_str);
			}
			/*Integer instanceStatusI = 0;
			if(null != instanceStatus || !"".equals(instanceStatus)){
				instanceStatusI = Integer.parseInt(instanceStatus);
			}*/
			String accessKey = (String)paasInstanceMap.get("accessKey");             //秘钥
			String pubDns = (String)paasInstanceMap.get("pubDns");                   //公共域名
			String urlPrefix = (String)paasInstanceMap.get("urlPrefix");             //前缀
			String version = (String)paasInstanceMap.get("version");                 //版本
			
			
			paasInstance.setInstanceName(instanceName);
			paasInstance.setInstanceStatus(instanceStatus);
			paasInstance.setAccessKey(accessKey);
			paasInstance.setPubDns(pubDns);
			paasInstance.setUrlPrefix(urlPrefix);
			paasInstance.setVersion(version);
			
			int upresult = paasInstanceMapper.updateByPrimaryKeySelective(paasInstance);
			if(upresult == 0){
				result = "更新失败";
				map.put("result", result);
				return map;
			}
			result = "updateOk";
			map.put("result", result);
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "系统异常，更新失败";
			map.put("result", result);
			return map;
		}
		
	}
	
	
	/**
	 * TODO 查询实例
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/obtainInstanceList", method = RequestMethod.GET)
	@ResponseBody
	public PageInfo obtainInstanceList(HttpServletRequest req){
		
		String pageNo = req.getParameter("pageNo");    //当前页
		String pageSize = req.getParameter("pageSize");//每页展示的条数
		String instanceStatus = req.getParameter("instanceStatus");
		String instanceName = req.getParameter("instanceName");
		
		Integer intPageNo = 1;
		Integer intpageSize = 10;
		if(null != pageSize && null != pageNo){
			intPageNo = Integer.parseInt(pageNo);
			intpageSize = Integer.parseInt(pageSize);
		}
		if("".equals(instanceName)){
			instanceName = null;
		}
		if("".equals(instanceStatus)){
			instanceStatus = null;
		}
		
		return queryListByPage(instanceName,instanceStatus,intPageNo,intpageSize);
		
	}
	
////////////////////////////////内部方法：start//////////////////////////////////////////////////////////////////////////
	public PageInfo queryListByPage(String instanceName,String instanceStatus, Integer pageNo,Integer pageSize) {
	
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo, pageSize);
		
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("instanceName", instanceName);
		paramMap.put("instanceStatus", instanceStatus);
		List<PaasInstance> instanceList = paasInstanceMapper.obtainInstanceList(paramMap);
		PageInfo pageInfo = new PageInfo(instanceList);
		
		return pageInfo;
	
	}
}
