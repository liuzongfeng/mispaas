package rest.security;

import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;



import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;



//import rest.service.passService.PassUserDetailService;

@Configuration
@EnableWebSecurity
public class PassSecurity extends WebSecurityConfigurerAdapter{
//	@Autowired
//	private PassUserDetailService passUserDetailService;
	@Autowired(required=true)
    private CasProperties casProperties;
	@Override
	protected void configure(HttpSecurity http) throws Exception{
//		http.authorizeRequests()
//			.antMatchers("/testUploadFile").permitAll()
//			.antMatchers("/css/**","/index").permitAll()
//			.antMatchers("/yunweiService/**").hasRole("YUNWEI")
////			.antMatchers("/passService/**").hasRole("ADMIN")
//			.and()
//			.formLogin()
//			.loginPage("/login").failureUrl("/login-error").successForwardUrl("/hello")
//			.and()
//			.csrf().disable()
//			;
		http.authorizeRequests()//配置安全策略
		.antMatchers("/swagger-ui.html","/**/swagger*/**","/**/api*/**","/**/paasService/**").permitAll()
		.anyRequest().authenticated()
		.and().logout().permitAll()
		.and().formLogin();
		
		http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint())
		.and().addFilter(casAuthenticationFilter())
		.addFilterBefore(casLogoutFilter(), LogoutFilter.class);
		
		http.csrf().disable();
		
		
		
	}
	/**认证的入口*/  
    @Bean  
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {  
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();  
        casAuthenticationEntryPoint.setLoginUrl(casProperties.getCasServerLoginUrl());  
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());  
        return casAuthenticationEntryPoint;  
    }
    /**CAS认证过滤器*/  
    @Bean  
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {  
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();  
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());  
        casAuthenticationFilter.setFilterProcessesUrl(casProperties.getAppLoginUrl());  
        return casAuthenticationFilter;  
    }
    /**指定service相关信息*/  
    @Bean  
    public ServiceProperties serviceProperties() {  
        ServiceProperties serviceProperties = new ServiceProperties();  
        serviceProperties.setService(casProperties.getAppServerUrl() + casProperties.getAppLoginUrl());  
        serviceProperties.setAuthenticateAllArtifacts(true);  
        return serviceProperties;  
    }
    /**请求单点退出过滤器*/  
    @Bean  
    public LogoutFilter casLogoutFilter() {  
        LogoutFilter logoutFilter = new LogoutFilter(casProperties.getCasServerLogoutUrl(), new SecurityContextLogoutHandler());  
        logoutFilter.setFilterProcessesUrl(casProperties.getAppLogoutUrl());  
        return logoutFilter;  
    }
    /**cas 认证 Provider*/  
    @Bean  
    public CasAuthenticationProvider casAuthenticationProvider() {  
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();  
        casAuthenticationProvider.setAuthenticationUserDetailsService(customUserDetailsService());  
        //casAuthenticationProvider.setUserDetailsService(customUserDetailsService()); //这里只是接口类型，实现的接口不一样，都可以的。  
        casAuthenticationProvider.setServiceProperties(serviceProperties());  
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());  
        casAuthenticationProvider.setKey("casAuthenticationProviderKey");  
        return casAuthenticationProvider;  
    }
    /**用户自定义的AuthenticationUserDetailsService*/  
    @Bean  
    public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> customUserDetailsService(){  
        return new CustomUserDetailsService();  
    }
    @Bean  
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {  
        return new Cas20ServiceTicketValidator(casProperties.getCasServerUrl());  
    }
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/js/**","/css/**","/images/**");		
	}
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(new PassUserDetailService());
//		auth.inMemoryAuthentication().withUser("user").password("password").roles("TEST");
		super.configure(auth);
		auth.authenticationProvider(casAuthenticationProvider());
	}
	public CasProperties getCasProperties() {
		return casProperties;
	}
	public void setCasProperties(CasProperties casProperties) {
		this.casProperties = casProperties;
	}

}
