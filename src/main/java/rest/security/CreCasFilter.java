package rest.security;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;


import com.dayang.cas.client.filter.DYCASFilter;

@WebFilter(urlPatterns="/*",filterName="Dayang-CAS-Filter",initParams={@WebInitParam(name="casServerUrlPrefix",value="http://100.0.10.202:8080/cas/"),@WebInitParam(name="redirectUrl",value="/dayang/login.jsp")})
public class CreCasFilter extends DYCASFilter{
	
}
