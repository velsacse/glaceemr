package com.glenwood.glaceemr.server.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.rulesengine.GlaceRulesService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;

/**
 * @author software
 *
 */

@RestController

@RequestMapping(value = "/user/GlaceRulesController")
public class GlaceRulesController {


	@Autowired
	GlaceRulesService glaceRulesService;

	@RequestMapping(value = "/getPQRSMeasure", method = RequestMethod.GET)
    @ResponseBody
	public	EMRResponseBean getPQRSMeasure(@RequestParam(value="providerId", required=true, defaultValue="-1") Integer providerId) throws Exception{
		
		String measure=glaceRulesService.getMeasures(providerId);
		EMRResponseBean result=new EMRResponseBean();
		result.setData(measure);
		return result;
		
	}
	
}
