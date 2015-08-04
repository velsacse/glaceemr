package com.glenwood.glaceemr.server.filters;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import org.springframework.stereotype.Component;

import com.glenwood.glaceemr.server.datasource.TennantContextHolder;
import com.glenwood.glaceemr.server.utils.HUtil;
import com.glenwood.glaceemr.server.utils.MyRequestWrapper;
/**
 * Servlet Filter implementation class DataBaseAccessFilter
 */
@Component
public class DataBaseAccessFilter implements Filter {

	HttpServletRequest httpRequest;
	HttpSession session;

	/**
	 * Default constructor. 
	 */
	public DataBaseAccessFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


		/*httpRequest = (HttpServletRequest)request;
		session = httpRequest.getSession(false);
		if(session.getAttribute("databasename") == null ||session.getAttribute("databasename") == " "||session.getAttribute("databasename") == "") 

			;
		else{
			System.out.println("in tennetaware"+session.getAttribute("databasename").toString());
			TennantContextHolder.setTennantId(session.getAttribute("databasename").toString());
		}*/
		httpRequest = (HttpServletRequest)request;
		NDC.push("Request Method : "+httpRequest.getMethod());
		MDC.put("Request Method", httpRequest.getMethod());
		MDC.put("Request URL", httpRequest.getRequestURL());
		MDC.put("ClientIp", httpRequest.getRemoteAddr());
		MDC.put("ServerIp", InetAddress.getLocalHost());
		NDC.push("Request URL : "+httpRequest.getRequestURL());
		Enumeration<String> headerNames = httpRequest.getHeaderNames();
		Enumeration<String> parametersNames =httpRequest.getParameterNames();
		StringBuffer jb = new StringBuffer();
		
			MyRequestWrapper myRequestWrapper = new MyRequestWrapper((HttpServletRequest) request);
			String body = myRequestWrapper.getBody();
			
		if(body.length()>0){
			NDC.push("@RequestParamterSperator@");
			NDC.push(body+"&");
			NDC.push("@RequestParamterSperator@");
		}else
		{
			NDC.push("@RequestParamterSperator@");
			while(parametersNames.hasMoreElements())
			{
				String parameter = parametersNames.nextElement();
				NDC.push(parameter+"="+httpRequest.getParameter(parameter)+"&");
			}
			NDC.push("@RequestParamterSperator@");
		}
		NDC.push("@RequestHeaderSperator@");
		while(headerNames.hasMoreElements()){
			String header = headerNames.nextElement();
			NDC.push(header+"@HeaderSperator@");
			NDC.push(httpRequest.getHeader(header)+"@HeaderValueSperator@");
		}
		NDC.push("@RequestHeaderSperator@");
		String tennantId = HUtil.Nz(request.getParameter("dbname"),"-1");

		if(tennantId != "-1"){
			TennantContextHolder.setTennantId(tennantId);
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {

	}

}
