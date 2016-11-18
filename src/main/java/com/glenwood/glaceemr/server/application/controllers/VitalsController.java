package com.glenwood.glaceemr.server.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.chart.vitals.VitalsService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;


@Api(value = "Vitals", description = "Vitals Data", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="/user/VitalElements.Action")
public class VitalsController {
	
	
	@Autowired
	VitalsService vitalService;
	
	@Autowired
	EMRResponseBean emrResponseBean;
	
	@RequestMapping(value="/VitalGroups",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getVitalGroups(@RequestParam(value="patientId",required=false, defaultValue="") Integer patientId,
			@RequestParam(value="groupId") Integer groupId) throws Exception{
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		groupId=Integer.parseInt(Optional.fromNullable(groupId+"").or("-1"));
		emrResponseBean.setData(vitalService.getActiveVitalsGroup(patientId,groupId));
		return emrResponseBean;
	}
	
	
	@RequestMapping(value="/VitalGroups/Vital",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getGroupVitals(@RequestParam(value="patientId") Integer patientId,
									@RequestParam(value="chartId") Integer chartId,
									@RequestParam(value="encounterId") Integer encounterId,
									@RequestParam(value="groupId") Integer groupId,
									@RequestParam(value="dischargeVitals", defaultValue="false", required=false) Boolean isDischargeVitals,
									@RequestParam(value="admssEpisode", defaultValue="-1", required=false) Integer admssEpisode,
									@RequestParam(value="clientId", defaultValue="000", required=false) String clientId) throws Exception{
		
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		chartId=Integer.parseInt(Optional.fromNullable(chartId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		groupId=Integer.parseInt(Optional.fromNullable(groupId+"").or("-1"));
		isDischargeVitals=Boolean.parseBoolean(Optional.fromNullable(isDischargeVitals+"").or("false"));
		admssEpisode=Integer.parseInt(Optional.fromNullable(admssEpisode+"").or("-1"));
		emrResponseBean.setData(vitalService.setVitals(patientId,encounterId,groupId,isDischargeVitals,admssEpisode,clientId));
		return emrResponseBean;
	}
	
	@RequestMapping(value="/VitalGroups/VitalPrint",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getGroupVitalsPrint(@RequestParam(value="patientId") Integer patientId,
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
		emrResponseBean.setData(vitalService.setVitals(patientId,encounterId,groupId,isDischargeVitals,admssEpisode,clientId,1));
		return emrResponseBean;
	}
	
	
	@RequestMapping(value="/FetchNotes", method=RequestMethod.GET)
	public EMRResponseBean getVitalNotes(@RequestParam(value="patientId") Integer patientId,
								@RequestParam(value="encounterId") Integer encounterId,
								@RequestParam(value="gwId") String gwId) {
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		gwId=Optional.fromNullable(gwId+"").or("");
		emrResponseBean.setData(vitalService.getNotes(patientId,encounterId,gwId));
		return emrResponseBean;
		
		
	}
	
	
}
