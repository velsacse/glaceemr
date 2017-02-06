package com.glenwood.glaceemr.server.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.chart.dischargeVitals.DischargeSaveVitalBean;
import com.glenwood.glaceemr.server.application.services.chart.dischargeVitals.DischargeVitalService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;

@RestController
@Transactional
@RequestMapping(value="/user/DischargeVitals")
public class DischargeVitalController {

	@Autowired
	DischargeVitalService dischargeVitalService;

	@RequestMapping(value="/saveVitals",method=RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean saveDischargeVitals(@RequestBody DischargeSaveVitalBean vitalDataBean) throws Exception{
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(dischargeVitalService.saveDischargeVitals(vitalDataBean));
		return respBean;
		
	}
	
	@RequestMapping(value="/getDischargeVitals",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getAdmissionEncDetails(@RequestParam(value="patientId",required=false, defaultValue="") Integer patientId,
								@RequestParam(value="chartId",required=false, defaultValue="") Integer encounterId,
								@RequestParam(value="admssEpisode",required=false, defaultValue="") Integer admssEpisode) throws Exception{
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(dischargeVitalService.getDischartgeVitals(patientId,encounterId,admssEpisode));
		return respBean;
	}
	
}
