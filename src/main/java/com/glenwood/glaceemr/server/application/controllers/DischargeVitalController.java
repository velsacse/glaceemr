package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.PatientVitals;
import com.glenwood.glaceemr.server.application.services.chart.dischargeVitals.DischargeSaveVitalBean;
import com.glenwood.glaceemr.server.application.services.chart.dischargeVitals.DischargeVitalService;
import com.wordnik.swagger.annotations.Api;

@Api(value = "DischargeVitals", description = "Multiple Vital Recording", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="/DischargeVitals")
public class DischargeVitalController {

	
	
	@Autowired
	DischargeVitalService dischargeVitalService;

	@RequestMapping(value="/saveVitals",method=RequestMethod.POST)
	@ResponseBody
	public Boolean saveDischargeVitals(@RequestBody DischargeSaveVitalBean vitalDataBean) throws Exception{
		return dischargeVitalService.saveDischargeVitals(vitalDataBean);
		
	}
	
	@RequestMapping(value="/getDischargeVitals",method=RequestMethod.GET)
	@ResponseBody
	public List<PatientVitals> getAdmissionEncDetails(@RequestParam(value="patientId",required=false, defaultValue="") Integer patientId,
								@RequestParam(value="chartId",required=false, defaultValue="") Integer encounterId,
								@RequestParam(value="admssEpisode",required=false, defaultValue="") Integer admssEpisode) throws Exception{
		System.out.println(dischargeVitalService.getDischartgeVitals(patientId,encounterId,admssEpisode).size());
		return dischargeVitalService.getDischartgeVitals(patientId,encounterId,admssEpisode);
	}
	
}
