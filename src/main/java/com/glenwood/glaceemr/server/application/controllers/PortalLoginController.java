package com.glenwood.glaceemr.server.application.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.datasource.TennantContextHolder;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;

@RestController
@RequestMapping(value="/login/PortalLoginAndLogout")
@Api(value="PortalLoginController", description="Manages the login mechanism")

public class PortalLoginController {

	
	@Autowired
	HttpServletRequest request;

	@Autowired
	EMRResponseBean responseBean;
	
	@RequestMapping(value = "/LoggedIn",method = RequestMethod.GET)
	public EMRResponseBean logIn() throws Exception 
	{		
				
		responseBean.setSuccess(true);
		responseBean.setCanUserAccess(true);
		responseBean.setLogin(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setData("Logged In");
		
		return responseBean;
	}
	
	@RequestMapping(value = "/LoggedOut",method = RequestMethod.GET)
	public EMRResponseBean logOut() throws Exception 
	{		
				
		responseBean.setSuccess(true);
		responseBean.setCanUserAccess(true);
		responseBean.setLogin(false);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setData("Logged Out");
		
		return responseBean;
	}
	
	@RequestMapping(value = "/LoginFailed",method = RequestMethod.GET)
	public void loginFailure(HttpServletResponse response) throws Exception 
	{		
				
		response.sendRedirect("https://patientportal.glaceemr.com/glaceportal_login/portal.jsp?practiceid="+TennantContextHolder.getTennantId()+"&log=0");
	}
	
	@RequestMapping(value = "/SessionExpired",method = RequestMethod.GET)
	public void redirectToLoginOnSessionExpiry(HttpServletResponse response) throws Exception 
	{		
			response.sendRedirect("https://patientportal.glaceemr.com/glaceportal_login/portal.jsp?practiceid="+TennantContextHolder.getTennantId()+"&log=4");	
	}
	
	@RequestMapping(value = "/InvalidSession",method = RequestMethod.GET)
	public void redirectToLoginOnInvalidSession(HttpServletResponse response) throws Exception 
	{		
		response.sendRedirect("https://patientportal.glaceemr.com/glaceportal_login/portal.jsp?practiceid="+TennantContextHolder.getTennantId()+"&log=5");	
	}
	
	@RequestMapping(value = "/ValidateSession",method = RequestMethod.GET)
	public EMRResponseBean validateSession() throws Exception 
	{
		
	    EMRResponseBean emrResponseBean = new EMRResponseBean();
	   
	    if(request.isRequestedSessionIdValid())
	    	emrResponseBean.setLogin(true);
	    else
	    	emrResponseBean.setLogin(false);

		return emrResponseBean;
		
	}
}
