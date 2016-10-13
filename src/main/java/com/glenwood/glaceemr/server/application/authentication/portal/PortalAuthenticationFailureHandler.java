package com.glenwood.glaceemr.server.application.authentication.portal;

import java.io.IOException;
import java.io.PrintWriter;

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
		
		PrintWriter out=response.getWriter();
		response.setContentType("application/json"); 
		
		try{
			
			System.out.println("In PortalAuthenticationFailureHandler");
			out.println("{\"sessionId\":\"-1\",\"userId\":-1,\"login\":false,\"isAuthenticated\":false,\"log\":0}");
			
		}catch(Exception ex){
			
			ex.printStackTrace();
			out.println("{\"sessionId\":\"-1\",\"userId\":-1,\"login\":false,\"isAuthenticated\":false,\"log\":0}");
		}
		
	}

}
