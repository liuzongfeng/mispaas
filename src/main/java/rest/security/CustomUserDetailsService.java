package rest.security;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import rest.page.util.OrgRequestUtil;
import rest.service.yunwei.CommonTool;
/** 
 * 用于加载用户信息 实现UserDetailsService接口，或者实现AuthenticationUserDetailsService接口 
 * @author ChengLi 
 * 
 */
public class CustomUserDetailsService //实现AuthenticationUserDetailsService，实现loadUserDetails方法  
implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {
	
//private static final String  userAuthorities = "http://100.0.10.100:8080/usermanager/api/authorization/users/";
@Autowired  
HttpSession session; //这里可以获取到request
@Override  
public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException { 
	String userAuthorities = "http://"+CommonTool.obtainUrl("USER_MANAGER")+"/usermanager/api/authorization/users/";
	System.out.println(userAuthorities);
	if(1 == 1){
		/* System.out.println("当前的用户名是："+token.getName()+
		";当前权限为："+token.getAuthorities()+";"
		+token.getCredentials()+";"
		+token.getDetails()+";principal:"
		+token.getPrincipal()+";"); 
		System.out.println(session.getId());
		
		System.out.println(session.getAttribute(CasUserService.TOKEN));*/
		
		
		//cas认证通过后，查询该用户的信息
		String persional = userAuthorities + (String)token.getPrincipal();
		
		OrgRequestUtil util = new OrgRequestUtil();
		Map<String,Object> person = null;
		try {
			person = util.getContentMap(persional);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UsernameNotFoundException("获取用户权限信息失败");
		}
		
		List<Map<String,Object>> listMap = (List)person.get("userList");
		Map<String,Object> userMap = listMap.get(0);              //含有用户信息的map.交给session 保管，存放到redis--暂时不存
		List<String> roleList = (List)userMap.get("roleIdList");
		String ticket = (String)token.getAssertion().getPrincipal().getAttributes().get("TOKEN");
		
		UserInfo userInfo = new UserInfo();
		
		userInfo.setUsername((String)userMap.get("id"));    //保存用户id
		userInfo.setName((String)userMap.get("name"));  
		userInfo.setPassword(ticket);              //保存用户token
		Set<AuthorityInfo> authorities = new HashSet<AuthorityInfo>();
		for(String role : roleList){
			AuthorityInfo authorityInfo = new AuthorityInfo(role);
			authorities.add(authorityInfo);
		}
		userInfo.setAuthorities(authorities); 
		//将用户信息userInfo交给session保管,存放到redis
		session.setAttribute(session.getId(), userInfo);
		//session.setAttribute(userInfo.getUsername(), userMap);
		return userInfo; 
	}else{
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername("admin");
		userInfo.setName("admin");
		userInfo.setPassword("admin");
		
		return userInfo;
	}
	
    
}  
}
