package com.glenwood.glaceemr.server.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.glenwood.glaceemr.server.datasource.TennantContextHolder;
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
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
