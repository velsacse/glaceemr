package com.glenwood.glaceemr.server.application.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.chart.HPI.HPIService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.HUtil;
import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;
/**
 * Controller for HPI
 * @author Bhagya Lakshmi
 *
 */

@Api(value="/user/HPI",description="To get history of present illness of Particular patient",consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="/user/HPIElements.Action")
public class HPIController {
	
	@Autowired
	HPIService HPIService;

	private Logger logger = Logger.getLogger(HPIController.class);
	
	
	/**
	 * TO get System Details
	 * @param patientId
	 * @return 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/HPISymptomData",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getHPISystemDetails(@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="chartId") Integer chartId,
			@RequestParam(value="encounterId") Integer encounterId,
			@RequestParam(value="templateId") Integer templateId,
			@RequestParam(value="symptomGWId") String symptomGWId,
			@RequestParam(value="symptomId") String symptomId,
			@RequestParam(value="isFollowUp") String isFollowUp,
			@RequestParam(value="tabId") String tabId,
			@RequestParam(value="clientId") String clientId) throws Exception{

		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		chartId=Integer.parseInt(Optional.fromNullable(chartId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		templateId=Integer.parseInt(Optional.fromNullable(templateId+"").or("-1"));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(HPIService.getSymptomData(clientId,patientId,chartId,encounterId,templateId,symptomGWId,symptomId,isFollowUp,tabId).toString());
		return emrResponseBean;
	}
	
	/**
	 * To delete Symptom details
	 * @param symptomToRemoveGWID
	 * @param patientId
	 * @param chartId
	 * @param encounterId
	 * @param isFollowUp
	 * @param templateId
	 * @param isCompleted
	 */
	@RequestMapping(value="/DeleteSymptomDetails",method=RequestMethod.POST)
	@ResponseBody
	public void deleteSymptomDetails(@RequestParam(value="symptomToRemoveGWID") String symptomToRemoveGWID,
			@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="chartId") Integer chartId,
			@RequestParam(value="encounterId") Integer encounterId,
			@RequestParam(value="isFollowUp") Integer isFollowUp,
			@RequestParam(value="templateId") Integer templateId,
			@RequestParam(value="isCompleted") Boolean isCompleted){
		String symptomGWId=HUtil.Nz(symptomToRemoveGWID,"-1");
		symptomGWId=symptomGWId.substring(0, 14)+"%";
		HPIService.deleteSymptomDetails(symptomGWId,patientId,encounterId);
		return;
	}
	
	
	/**
	 * TO get Chief Complaints Data
	 * @param patientId
	 * @return 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/FetchCC",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getHPICC(@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="chartId") Integer chartId,
			@RequestParam(value="encounterId") Integer encounterId){
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		chartId=Integer.parseInt(Optional.fromNullable(chartId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(HPIService.fetchCCData(patientId,chartId,encounterId).toString().replaceAll("\\\\", ""));
		return emrResponseBean;
	}

	/**
	 * TO get Patient HPI Notes
	 * @param patientId
	 * @return 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/FetchPatientHPINotesData",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getPatientHPINotesData(@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="chartId") Integer chartId,
			@RequestParam(value="encounterId") Integer encounterId){
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		chartId=Integer.parseInt(Optional.fromNullable(chartId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(HPIService.fetchPatientHPINotesData(patientId,chartId,encounterId));
		return emrResponseBean;
	}

	/**
	 * TO fetch default shortcut
	 * @param patientId
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value="/FetchDefaultShortcut",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean fetchDefaultShortcutData(@RequestParam(value="mode") Integer mode){
		mode=Integer.parseInt(Optional.fromNullable(mode+"").or("-1"));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(HPIService.fetchDefaultShortcutData(mode).toString());
		return emrResponseBean;
	}

	/**
	 * To Fetch Shortcut Data
	 * @param shortcutId
	 * @return
	 */
	//check
	@RequestMapping(value="/FetchShortcutData",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean fetchShortcutData(@RequestParam(value="shortcutId") String shortcutId)
	{
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(HPIService.fetchShortcutData(shortcutId).toString());
		return emrResponseBean;
	}
	
	/**
	 * Method to Fetch Shortcut list based on search string
	 * @param mode
	 * @param searchStr
	 * @param limit
	 * @param offset
	 * @return
	 */
	@RequestMapping(value="/FetchShortCutListBasedOnSearchString",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean FetchShortCutListBasedOnSearchString(@RequestParam(value="mode") Integer mode,
			@RequestParam(value="searchStr") String searchStr,
			@RequestParam(value="limit") Integer limit,
			@RequestParam(value="offset") Integer offset)
	{
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(HPIService.fetchShortCutListBasedOnSearchString(mode,searchStr,limit,offset).toString());
		return emrResponseBean;
	}
	
	
	/**
	 * Method to add shortcut in chief complaints
	 * @param complaint
	 */
	@RequestMapping(value="/AddShorctutInCC",method=RequestMethod.POST)
	@ResponseBody
	public void AddShorctutInCCAndNote(@RequestParam(value="complaint") String complaint){
		complaint=Optional.fromNullable(complaint+"").or("-1");
		HPIService.addShorctutInCC(complaint);
		return;
	}
	
	/**
	 * Method to add shortcut in HPI Notes
	 * @param Id
	 * @param ShortCutCode
	 * @param Description
	 * @param categoryId
	 */
	@RequestMapping(value="/AddShorctutInNotes",method=RequestMethod.POST)
	@ResponseBody
	public void AddShorctutInNotes(@RequestParam(value="Id") String Id,
			@RequestParam(value="ShortCutCode") String ShortCutCode,
			@RequestParam(value="Description") String Description,
			@RequestParam(value="categoryId") String categoryId){
		Id=Optional.fromNullable(Id+"").or("-1");
		ShortCutCode=Optional.fromNullable(ShortCutCode+"").or("-1");
		Description=Optional.fromNullable(Description+"").or("-1");
		categoryId=Optional.fromNullable(categoryId+"").or("-1");
		HPIService.addShorctutInNotes(Id,ShortCutCode,Description,categoryId);
		return;
	}
	
	
	/**
	 * Method to fetch All Symptoms and Data
	 * @param patientId
	 * @param chartId
	 * @param encounterId
	 * @param templateId
	 * @param isCompleted
	 * @param symptomTypeList
	 * @param isFollowUp
	 * @param clientId
	 * @return
	 */
	@RequestMapping(value="/FetchSymptom",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean FetchSymptom(@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="chartId") Integer chartId,
			@RequestParam(value="encounterId") Integer encounterId,
			@RequestParam(value="templateId") Integer templateId,
			@RequestParam(value="isCompleted") String isCompleted,
			@RequestParam(value="symptomTypeList") String symptomTypeList,
			@RequestParam(value="isFollowUp") String isFollowUp,
			@RequestParam(value="clientId") String clientId){
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		chartId=Integer.parseInt(Optional.fromNullable(chartId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		templateId=Integer.parseInt(Optional.fromNullable(templateId+"").or("-1"));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(HPIService.fetchSymptom(patientId,chartId,encounterId,templateId,isCompleted,symptomTypeList,isFollowUp,clientId));
		return emrResponseBean;
	}
	
}
