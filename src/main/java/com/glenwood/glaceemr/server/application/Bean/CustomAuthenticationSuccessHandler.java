package com.glenwood.glaceemr.server.application.Bean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.Assert;

import com.glenwood.glaceemr.server.application.services.users.UsersService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.SessionMap;



public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	@Autowired
	EMRResponseBean emrResponseBean;
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	UsersService usersService;
	

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication){
		try{
			emrResponseBean.setLogin(true);
			System.out.println(authentication.getName().toString());
			sessionMap.setUserID(usersService.findByUserName(authentication.getName().toString()));
			String dbName = request.getParameter("dbname");
			String apache_url = request.getParameter("apache_url");
			String tomcat_context = request.getParameter("tomcat_context");
			Assert.notNull(dbName, "dbName is empty");
			Assert.notNull(apache_url, "apache_url is empty");
			sessionMap.setDbName(dbName);
			response.sendRedirect(apache_url);
			
		}catch(Exception ex){
			try {
				throw ex;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
