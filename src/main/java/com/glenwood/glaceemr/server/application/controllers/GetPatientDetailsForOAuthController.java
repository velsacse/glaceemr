package com.glenwood.glaceemr.server.application.controllers;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.chart.Allergies.AllergiesService;
import com.glenwood.glaceemr.server.application.services.chartcenter.ChartcenterService;
import com.glenwood.glaceemr.server.application.services.patient.PatientRegistrationService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;

@RestController
@RequestMapping(value="/oauth")
public class GetPatientDetailsForOAuthController {

	@Autowired
	EMRResponseBean responseBean;
	
	@Autowired
	PatientRegistrationService patientRegistrationService;

	@Autowired
	ChartcenterService chartCenterService;
	
	@Autowired
	AllergiesService allergiesService;
	
	@Autowired
	HttpServletRequest request;
	
	
	@RequestMapping(value="/getPatientInfo",method = RequestMethod.POST)
	public EMRResponseBean getPatientInfoForOAuth(@RequestParam("masterPatientId") Integer mpi) throws Exception{
		
		boolean isValid = Boolean.valueOf(request.getAttribute("isAccessTokenValid").toString());
		
		if(isValid){
			responseBean.setLogin(true);
			responseBean.setIsAuthorizationPresent(true);
			responseBean.setData(patientRegistrationService.findByPatientId(mpi));
			responseBean.setSuccess(true);
			responseBean.setStartTime(Calendar.getInstance().getTime().toString());
		}
		else{
			responseBean.setLogin(false);
			responseBean.setIsAuthorizationPresent(false);
			responseBean.setData(new JSONObject());
			responseBean.setSuccess(false);
			responseBean.setStartTime(Calendar.getInstance().getTime().toString());
		}
		
		return responseBean;
		
	}
	
	@RequestMapping(value="/getPatientAllergyInfo",method = RequestMethod.POST)
	public EMRResponseBean getPatientAllergyInfoForOAuth(@RequestParam("masterPatientId") Integer mpi) throws Exception{
		
		boolean isValid = Boolean.valueOf(request.getAttribute("isAccessTokenValid").toString());
		
		if(isValid){
			int chartId = chartCenterService.getChartIdByPatientId(mpi);
			responseBean.setLogin(true);
			responseBean.setIsAuthorizationPresent(true);
			responseBean.setData(allergiesService.patientAllergiesHistory(String.valueOf(chartId)));
			responseBean.setSuccess(true);
			responseBean.setStartTime(Calendar.getInstance().getTime().toString());
		}
		else{
			responseBean.setLogin(false);
			responseBean.setIsAuthorizationPresent(false);
			responseBean.setData(new JSONObject());
			responseBean.setSuccess(false);
			responseBean.setStartTime(Calendar.getInstance().getTime().toString());
		}
		
		
		return responseBean;
		
	}
}
