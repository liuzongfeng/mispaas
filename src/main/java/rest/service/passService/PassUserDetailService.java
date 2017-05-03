package rest.service.passService;


import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.mysql.fabric.xmlrpc.base.Array;
@Configuration
public class PassUserDetailService implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		System.out.println(username);
		List list=new ArrayList<GrantedAuthority>();
		User user=new User("hj", "123", list.add(new SimpleGrantedAuthority("TEst")), false, false, false, list);
		return user;
	}
	
}
