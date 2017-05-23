package rest.security;
import java.util.HashSet;  
import java.util.Set;  
  





import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;  
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;  
import org.springframework.security.core.userdetails.UserDetails;  
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import org.springframework.web.bind.annotation.SessionAttributes;

import com.dayang.cas.client.service.CasUserService;
/** 
 * 用于加载用户信息 实现UserDetailsService接口，或者实现AuthenticationUserDetailsService接口 
 * @author ChengLi 
 * 
 */
public class CustomUserDetailsService //实现AuthenticationUserDetailsService，实现loadUserDetails方法  
implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {
@Autowired  
HttpSession session; //这里可以获取到request
@Override  
public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {  
    System.out.println("当前的用户名是："+token.getName()+
    		";当前权限为："+token.getAuthorities()+";"
    		+token.getCredentials()+";"
    		+token.getDetails()+";principal:"
    		+token.getPrincipal()+";"); 
    session.setAttribute(session.getId(), token.getPrincipal());
    System.out.println(session.getAttribute(CasUserService.TOKEN));
    /*这里我为了方便，就直接返回一个用户信息，实际当中这里修改为查询数据库或者调用服务什么的来获取用户信息*/  
    UserInfo userInfo = new UserInfo();  
    userInfo.setUsername("admin");  
    userInfo.setName("admin");  
    Set<AuthorityInfo> authorities = new HashSet<AuthorityInfo>();  
    AuthorityInfo authorityInfo = new AuthorityInfo("TEST");  
    authorities.add(authorityInfo);  
    userInfo.setAuthorities(authorities);  
    return userInfo;  
}  
}
