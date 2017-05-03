package rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import rest.service.passService.PassUserDetailService;


@EnableWebSecurity
public class PassSecurity extends WebSecurityConfigurerAdapter{
	@Autowired
	private PassUserDetailService passUserDetailService;
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests()
			.antMatchers("/css/**","/index").permitAll()
			.antMatchers("/yunweiService/**").hasRole("YUNWEI")
			.antMatchers("/passService/**").hasRole("ADMIN")
			.and()
			.formLogin()
			.loginPage("/login").failureUrl("/login-error").successForwardUrl("/hello")
//			.and()
//			.csrf().disable()
			;
	}
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/js/**","/css/**","/images/**");		
	}
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(new PassUserDetailService());
//		auth.inMemoryAuthentication().withUser("user").password("password").roles("TEST");
	}

}
