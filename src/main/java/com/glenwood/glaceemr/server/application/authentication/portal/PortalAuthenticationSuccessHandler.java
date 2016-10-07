package com.glenwood.glaceemr.server.application.authentication.portal;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.Assert;

import com.glenwood.glaceemr.server.application.services.portalLogin.PortalLoginService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.SessionMap;



public class PortalAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	@Autowired
	EMRResponseBean emrResponseBean;
	@Autowired
	SessionMap sessionMap;

	@Autowired
	PortalLoginService portalLoginService;


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication){
		try{
			System.out.println("In onAuthenticationSuccess PortalAuthenticationSuccessHandler");
			emrResponseBean.setLogin(true);
			sessionMap.setPortalUserID(portalLoginService.findByUserNameIgnoreCase(authentication.getName().toString()).getId());
			String dbAccountId = request.getParameter("dbname");
			String portal_login_url=request.getParameter("portal_login_url");
			String portal_login_context=request.getParameter("portal_login_context");
			String portal_apache_url = request.getParameter("portal_apache_url");
			String portal_tomcat_url = request.getParameter("portal_tomcat_url");
			String portal_tomcat_context = request.getParameter("portal_tomcat_context");
			String portal_spring_context = request.getParameter("portal_spring_context");
			String baseURL=request.getParameter("baseURL");
			Assert.notNull(dbAccountId, "dbAccountId is empty");
			Assert.notNull(portal_apache_url, "portal_apache_url is empty");
			sessionMap.setDbName(dbAccountId);
			sessionMap.setPracticeAccountId(dbAccountId);
			sessionMap.setPortalUser(authentication.getName().toString());
			sessionMap.setPortalSessionID(request.getSession().getId());
			sessionMap.setPortalDBName(dbAccountId);
			sessionMap.setPortalLoginUrl(portal_login_url);
			sessionMap.setPortalLoginContext(portal_login_context);
			sessionMap.setGlaceApacheUrl(portal_apache_url);
			sessionMap.setGlaceTomcatUrl(portal_tomcat_url);
			sessionMap.setGlaceTomcatContext(portal_tomcat_context);
			sessionMap.setGlaceSpringContext(portal_spring_context);


			if(sessionMap.getPortalPassworResetCount()<=0){

				response.sendRedirect("https://patientportal.glaceemr.com/glaceportal_login/PortalRegistration.jsp?patientId="+sessionMap.getPortalUserID()+"&practiceId="+dbAccountId);
				return;
			}

			if(sessionMap.getIsPortalUserActive()!=1){

				response.sendRedirect("https://patientportal.glaceemr.com/glaceportal_login/portal.jsp?practiceId="+dbAccountId+"&log=8");
				return;
			}

			if(sessionMap.getIsOldPortalUser()!=1){

				response.sendRedirect("https://patientportal.glaceemr.com/glaceportal_login/portal.jsp?practiceId="+dbAccountId+"&log=8");
				return;
			}
			
			response.addCookie(new Cookie("JSESSIONID", request.getSession().getId()));
			response.addHeader("PORTALSESSIONID", request.getSession().getId());
						
			if(dbAccountId.equalsIgnoreCase("glace"))
				response.sendRedirect(portal_apache_url+"/glaceportal.html?user="+request.getParameter("username")+"&tennantDB="+dbAccountId+"&ID="+request.getSession().getId());
			else
				response.sendRedirect(portal_apache_url+"/portal/glaceportal.html?user="+request.getParameter("username")+"&tennantDB="+dbAccountId+"&ID="+request.getSession().getId());

		}catch(Exception ex){
			try {
				throw ex;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
