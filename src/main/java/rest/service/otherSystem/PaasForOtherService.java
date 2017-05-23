package rest.service.otherSystem;

import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rest.otherSystem.Obj.InstanceidTenentid;
import rest.otherSystem.Obj.OrgidsInstanceid;

@RestController
public class PaasForOtherService {
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
		list=new ArrayList<String>();
		list.add("11C5EDB2-8B09-4457-BF20-19D5336C8513");
		list.add("161C994E-5650-448E-AB49-26E027E9D26C");
		return list;
	}

}
