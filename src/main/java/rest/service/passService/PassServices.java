package rest.service.passService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rest.mybatis.dao.passDao.PassUserMapper;
import rest.mybatis.model.passModel.PassUser;

@RestController
//@Controller
public class PassServices {
	@Autowired
	private PassUserMapper passUserMapper;

	@RequestMapping(value = "/passService/searchUserByUserId/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public PassUser searchUserByUserId(@PathVariable("userId") Integer userId) {
		PassUser userT = passUserMapper.selectByPrimaryKey(userId);
		return userT;
	}
	@RequestMapping(value="/login")
	public String enterpass(){
		System.out.println("login-values");
		
		return "login";
	}
	@RequestMapping(value="/login-error")
	public String error(){
		System.out.println("login-error");
		
		return "login-error";
	}
	@RequestMapping(value="/hello")
	public String hello(){
		System.out.println("login-hello");
		
		return "hello";
	}
}
