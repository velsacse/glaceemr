package com.glenwood.glaceemr.server.application.controllers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/login")
public class LoginController {

	HttpServletRequest httpRequest;
	HttpSession session;
	@RequestMapping(value = "/database/{name}", method = RequestMethod.GET)
	public @ResponseBody String greeting( @PathVariable String name,HttpServletRequest request) {

		httpRequest = (HttpServletRequest)request;
		session = httpRequest.getSession(false);
		session.setAttribute("databasename", name);
		return new String("Added to "+"session id ->"+session.getId()+" : "+session.getAttribute("databasename")+" in session");
	}
	
}
