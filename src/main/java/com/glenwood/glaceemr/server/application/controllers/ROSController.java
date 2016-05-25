package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.chart.ros.ROSService;
import com.glenwood.glaceemr.server.application.services.chart.ros.ROSSystemBean;
import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;

@Api(value = "ROS", description = "ROS Data", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="/ROSElements")
public class ROSController {
	
	
	@Autowired
	ROSService rosService;
	
	@RequestMapping(value="/ROSElements",method=RequestMethod.GET)
	@ResponseBody
	public List<ROSSystemBean> getROSElement(@RequestParam(value="patientId") Integer patientId,
									   @RequestParam(value="chartId") Integer chartId,
									   @RequestParam(value="encounterId") Integer encounterId,
									   @RequestParam(value="templateId") Integer templateId,
									   @RequestParam(value="clientId") String clientId,
									   @RequestParam(value="tabId") Integer tabId) throws Exception{
				
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		chartId=Integer.parseInt(Optional.fromNullable(chartId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		templateId=Integer.parseInt(Optional.fromNullable(templateId+"").or("-1"));
		tabId=Integer.parseInt(Optional.fromNullable(tabId+"").or("-1"));
		return rosService.getROSElements(clientId,patientId,chartId,encounterId,templateId,tabId);
	}
	
	@RequestMapping(value="/ROSNotes",method=RequestMethod.GET)
	@ResponseBody
	public String getROSNotes(@RequestParam(value="patientId") Integer patientId,
							 @RequestParam(value="encounterId") Integer encounterId) throws Exception{
		
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		return rosService.getROSNotes(patientId,encounterId);
	}

	


	
}