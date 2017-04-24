package com.glenwood.glaceemr.server.application.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.RequestBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.RestDispatcherTemplate;

@RestController
@Transactional
@RequestMapping(value="/user/Connect")
public class LegacyRequestController {
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	RestDispatcherTemplate requestDispatcher;
	
	@Autowired
	EMRResponseBean emrResponseBean;
	
	/**
	 * Method to contact legacy using rest template
	 * @return
	 */
	@RequestMapping(value="/connectToLegacy", method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean connectToLegacy(
			 @RequestParam(value="url", required=true, defaultValue="true") String urlPrefix, 
			 @RequestParam(value="method", required=true, defaultValue="GET") String httpMethod, 
			 @RequestParam(value="params", required=false, defaultValue="") String params){

		Map<String,String> paramMap=new HashMap<String, String>();
		String startTime = ""; //emrResponseBean.getStartTime();
		try {
			String[] paramNames=JSONObject.getNames(params);
			JSONObject paramsObj = new JSONObject(params);
			for(String param: paramNames){
				paramMap.put(param, paramsObj.getString(param));
			}
		
			if(httpMethod.equalsIgnoreCase("GET")){
				emrResponseBean = requestDispatcher.sendGet(request, urlPrefix, paramMap);
			}
			else if(httpMethod.equalsIgnoreCase("POST")){
				emrResponseBean = requestDispatcher.sendPost(request, urlPrefix, paramMap);
			}
//			emrResponseBean.setStartTime(startTime);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return emrResponseBean;
		
	}
}
