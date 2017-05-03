package rest.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/myPassLogin").setViewName("login");
//        registry.addViewController("/error").setViewName("login-error");
//        registry.addViewController("/hello").setViewName("mylogin");
    }
}
