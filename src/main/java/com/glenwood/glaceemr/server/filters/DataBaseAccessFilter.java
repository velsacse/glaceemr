package com.glenwood.glaceemr.server.filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import org.springframework.stereotype.Component;

import com.glenwood.glaceemr.server.datasource.TennantContextHolder;
import com.glenwood.glaceemr.server.utils.HUtil;
import com.glenwood.glaceemr.server.utils.MultiReadHttpServletRequest;

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

	public static String password="";
	
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
		
		MultiReadHttpServletRequest multiReadRequest = new MultiReadHttpServletRequest((HttpServletRequest) request);

			/*String body = myRequestWrapper.getBody();*/
			
			
			String body="";
			
			///////////////////////
			  StringBuilder stringBuilder = new StringBuilder();
			   BufferedReader bufferedReader = null;
			   try {
			     InputStream inputStream = multiReadRequest.getInputStream();
			     if (inputStream != null) {
			       bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			       char[] charBuffer = new char[128];
			       int bytesRead = -1;
			       while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
			         stringBuilder.append(charBuffer, 0, bytesRead);
			       }
			     } else {
			       stringBuilder.append("");
			     }
			   } catch (IOException ex) {
			       throw ex;
			   } finally {/*
			     if (bufferedReader != null) {
			       try {
			         bufferedReader.close();
			       } catch (IOException ex) {
			         throw ex;
			       }
			     }
			   */}
			   
			   body = stringBuilder.toString();
			
			
			///////////////////////
			
			
			
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
			if(header.trim().equalsIgnoreCase("authorization")){
			System.out.println(">>>"+header+"value>>>>>>>>"+httpRequest.getHeader(header));
			String basecut[]=httpRequest.getHeader(header).split("Basic");
			byte[] valueDecoded= Base64.decodeBase64(basecut[1].getBytes() );
			password=new String(valueDecoded).split(":")[1];

			}
			
			if(header.trim().equalsIgnoreCase("Database")){
				String tennantId = HUtil.Nz(httpRequest.getHeader(header).toString(),"-1");
				if(tennantId != "-1"){
					TennantContextHolder.setTennantId(tennantId);
				}

				}
		}
		NDC.push("@RequestHeaderSperator@");
	
	
		chain.doFilter(multiReadRequest, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {

	}

}
