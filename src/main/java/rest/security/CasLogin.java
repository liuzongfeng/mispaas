package rest.security;

import javax.servlet.annotation.WebServlet;

import com.dayang.cas.Login;
@SuppressWarnings("serial")
@WebServlet(name="login",urlPatterns="/login")
public class CasLogin extends Login{

}
