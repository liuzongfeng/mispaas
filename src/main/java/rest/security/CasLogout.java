package rest.security;

import javax.servlet.annotation.WebServlet;

import com.dayang.cas.Logout;
@SuppressWarnings("serial")
@WebServlet(name="logout",urlPatterns="/logout")
public class CasLogout extends Logout{

}
