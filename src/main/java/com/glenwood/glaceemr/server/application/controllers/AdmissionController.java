package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.Admission;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.LeafPatient;
import com.glenwood.glaceemr.server.application.services.chart.admission.AdmissionBean;
import com.glenwood.glaceemr.server.application.services.chart.admission.AdmissionLeafBean;
import com.glenwood.glaceemr.server.application.services.chart.admission.AdmissionService;
import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;


@Api(value = "Admission", description = "Admission", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="/Admission")
public class AdmissionController {


	@Autowired
	AdmissionService admissionService;

	@RequestMapping(value="/saveAdmission",method=RequestMethod.POST)
	@ResponseBody
	public String createAdmission(@RequestBody AdmissionBean dataJson) throws Exception{
		dataJson = getAdmissionBeanData(dataJson);
		return admissionService.saveAdmission(dataJson);
	}
	
	@RequestMapping(value="/getAdmission",method=RequestMethod.GET)
	@ResponseBody
	public Admission getAdmission(@RequestParam(value="patientId",required=false, defaultValue="") Integer patientId) throws Exception{
		return admissionService.getAdmission(patientId);
	}
	
	
	
	@RequestMapping(value="/dischargePatient",method=RequestMethod.GET)
	@ResponseBody
	public String dischargePatient(@RequestParam(value="patientId",required=false, defaultValue="") Integer patientId,
			 					   @RequestParam(value="loginId",required=false, defaultValue="") Integer loginId,						 
			 					   @RequestParam(value="userId",required=false, defaultValue="") Integer userId) throws Exception{
		return admissionService.dischargePatient(patientId,loginId,userId);
	}
	

	@RequestMapping(value="/getPastAdmission",method=RequestMethod.GET)
	@ResponseBody
	public List<Admission> getPastAdmission(@RequestParam(value="patientId",required=false, defaultValue="") Integer patientId) throws Exception{
		return admissionService.getPastAdmission(patientId);
	}
	
	@RequestMapping(value="/getLeafDetails",method=RequestMethod.GET)
	@ResponseBody
	public List<Admission> getLeafDetails(@RequestParam(value="encounterId",required=false, defaultValue="") Integer encounterId,
										  @RequestParam(value="userId",required=false, defaultValue="") Integer userId) throws Exception{
		return admissionService.getLeafDetails(encounterId,userId);
	}
	
	@RequestMapping(value="/getAdmissionEncounterDetails",method=RequestMethod.GET)
	@ResponseBody
	public String getAdmissionEncDetails(@RequestParam(value="admssEpisode",required=false, defaultValue="") Integer admssEpisode) throws Exception{
		System.out.println("admssEpisodein controlle"+admssEpisode);
		return admissionService.getAdmissionEncDetails(admssEpisode);
	}
	
	
	@RequestMapping(value="/openLeaf",method=RequestMethod.POST)
	@ResponseBody
	public Encounter openAdmissionLeaf(@RequestBody AdmissionBean dataJson) throws Exception{
		dataJson = getAdmissionBeanData(dataJson);
		return admissionService.openAdmissionLeaf(dataJson);
	}
	
	
	@RequestMapping(value="/AdmissionLeafs",method=RequestMethod.GET)
	@ResponseBody
	public AdmissionLeafBean getAdmissionLeafs(@RequestParam(value="admssEpisode",required=false, defaultValue="") Integer admssEpisode) throws Exception{
		return admissionService.getAdmissionLeafs(admssEpisode);
	}
	
	
	
	public AdmissionBean getAdmissionBeanData(AdmissionBean dataJson){
		
		dataJson.setAdmissionDate(Optional.fromNullable(dataJson.getAdmissionDate()+"").or("-1"));
		dataJson.setAdmssProvider(Integer.parseInt(Optional.fromNullable(dataJson.getAdmssProvider()+"").or("-1")));
		dataJson.setPos(Integer.parseInt(Optional.fromNullable(dataJson.getPos()+"").or("-1")));
		dataJson.setPatientId(Integer.parseInt(Optional.fromNullable(dataJson.getPatientId()+"").or("-1")));
		dataJson.setSelectedDx(Optional.fromNullable(dataJson.getSelectedDx()+"").or("[]"));
		dataJson.setChartId(Integer.parseInt(Optional.fromNullable(dataJson.getChartId()+"").or("-1")));
		dataJson.setUserId(Integer.parseInt(Optional.fromNullable(dataJson.getUserId()+"").or("-1")));
		dataJson.setLoginId(Integer.parseInt(Optional.fromNullable(dataJson.getLoginId()+"").or("-1")));
		dataJson.setAdmissionEpisode(Integer.parseInt(Optional.fromNullable(dataJson.getAdmissionEpisode()+"").or("-1")));
		
		return dataJson;
	}
	
	
}