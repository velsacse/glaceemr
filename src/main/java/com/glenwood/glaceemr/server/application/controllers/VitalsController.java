package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.VitalGroup;
import com.glenwood.glaceemr.server.application.services.chart.dischargeVitals.DischargeVitalBean;
import com.glenwood.glaceemr.server.application.services.chart.vitals.CustomVitalGroup;
import com.glenwood.glaceemr.server.application.services.chart.vitals.VitalsService;
import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;


@Api(value = "Vitals", description = "Vitals Data", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="/user/VitalElements.Action")
public class VitalsController {
	
	
	@Autowired
	VitalsService vitalService;
	
	@RequestMapping(value="/VitalGroups",method=RequestMethod.GET)
	@ResponseBody
	public List<VitalGroup> getVitalGroups(@RequestParam(value="patientId",required=false, defaultValue="") Integer patientId,
			@RequestParam(value="groupId") Integer groupId) throws Exception{
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		groupId=Integer.parseInt(Optional.fromNullable(groupId+"").or("-1"));
		return vitalService.getActiveVitalsGroup(patientId,groupId);
	}
	
	
	@RequestMapping(value="/VitalGroups/Vital",method=RequestMethod.GET)
	@ResponseBody
	public DischargeVitalBean getGroupVitals(@RequestParam(value="patientId") Integer patientId,
									@RequestParam(value="chartId") Integer chartId,
									@RequestParam(value="encounterId") Integer encounterId,
									@RequestParam(value="groupId") Integer groupId,
									@RequestParam(value="dischargeVitals") Boolean isDischargeVitals,
									@RequestParam(value="admssEpisode") Integer admssEpisode,
									@RequestParam(value="clientId") String clientId) throws Exception{
		
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		chartId=Integer.parseInt(Optional.fromNullable(chartId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		groupId=Integer.parseInt(Optional.fromNullable(groupId+"").or("-1"));
		isDischargeVitals=Boolean.parseBoolean(Optional.fromNullable(isDischargeVitals+"").or("false"));
		admssEpisode=Integer.parseInt(Optional.fromNullable(admssEpisode+"").or("-1"));
		return vitalService.setVitals(patientId,encounterId,groupId,isDischargeVitals,admssEpisode,clientId,0);
	}
	
	@RequestMapping(value="/VitalGroups/VitalPrint",method=RequestMethod.GET)
	@ResponseBody
	public DischargeVitalBean getGroupVitalsPrint(@RequestParam(value="patientId") Integer patientId,
									@RequestParam(value="chartId") Integer chartId,
									@RequestParam(value="encounterId") Integer encounterId,
									@RequestParam(value="groupId") Integer groupId,
									@RequestParam(value="dischargeVitals") Boolean isDischargeVitals,
									@RequestParam(value="admssEpisode") Integer admssEpisode,
									@RequestParam(value="clientId") String clientId) throws Exception{
		
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		chartId=Integer.parseInt(Optional.fromNullable(chartId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		groupId=Integer.parseInt(Optional.fromNullable(groupId+"").or("-1"));
		isDischargeVitals=Boolean.parseBoolean(Optional.fromNullable(isDischargeVitals+"").or("false"));
		admssEpisode=Integer.parseInt(Optional.fromNullable(admssEpisode+"").or("-1"));
		return vitalService.setVitals(patientId,encounterId,groupId,isDischargeVitals,admssEpisode,clientId,1);
	}
	
}
