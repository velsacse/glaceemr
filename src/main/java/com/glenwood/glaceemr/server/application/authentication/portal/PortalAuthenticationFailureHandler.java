package com.glenwood.glaceemr.server.application.authentication.portal;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.Assert;

public class PortalAuthenticationFailureHandler implements AuthenticationFailureHandler{
	String request_origin_URL ="";
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		//logger.info("In custom authentication failure handler");
		try{
			System.out.println("In PortalAuthenticationFailureHandler");
			String portal_login_url = request.getParameter("portal_login_url");
			Assert.notNull(portal_login_url, "baseURL is empty");
			response.sendRedirect(portal_login_url+"&log=0");
			
		}catch(Exception ex){
			
			ex.printStackTrace();
			response.sendRedirect(request.getParameter("portal_login_url")+"&log=0");
		}
		
	}

}
