package com.glenwood.glaceemr.server.application.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.chart.FocalShortcuts.FocalShortcutsService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.HUtil;
import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;

/**
 * Controller for FocalShortcuts
 * @author Bhagya Lakshmi
 *
 */
@Api(value="/user/FocalShortcuts",description="To get Symptoms data",consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="/user/FocalShortcuts.Action")
public class FocalShortcutsController {
	
	@Autowired
	FocalShortcutsService FocalShortcutsService;

	private Logger logger = Logger.getLogger(FocalShortcutsController.class);

	/**
	 * Method to get focal shortcuts Available
	 * @param tabId
	 * @return
	 */
	@RequestMapping(value="/GetFocalShortcutsAvailable",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean GetFocalshortcutsAvailable(@RequestParam(value="tabId") String tabId)
	{
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(FocalShortcutsService.getFocalshortcutsAvailable(tabId).toString());
		return emrResponseBean;
	}
	
	/**
	 * Method to delete focal elements
	 * @param shortcutId
	 */
	@RequestMapping(value="/DeleteFocalElements",method=RequestMethod.POST)
	@ResponseBody
	public void deleteSymptomDetails(@RequestParam(value="shortcutId") String shortcutId){
		String FocalShortcutId=HUtil.Nz(shortcutId,"-1");
		FocalShortcutsService.deleteElementsInFocalShortcut(FocalShortcutId);
		return;
	}
	
	/**
	 * Method to update focal shortcut
	 * @param description
	 * @param isActive
	 * @param focalId
	 */
	@RequestMapping(value="/UpdateFocalShortcut",method=RequestMethod.POST)
	@ResponseBody
	public void updateFocalShortcut(@RequestParam(value="description") String description,
			@RequestParam(value="isActive") Boolean isActive,
			@RequestParam(value="focalId") String focalId){
		String descriptionShortcut=HUtil.Nz(description,"-1");
		focalId=HUtil.Nz(focalId,"-1");
		FocalShortcutsService.updateFocalShortcut(descriptionShortcut,isActive,focalId);
		return;
	}
	
	/**
	 * Method to get focal shortcut data
	 * @param focalIndex
	 * @param tabId
	 * @param focalId
	 * @param patientId
	 * @param chartId
	 * @param encounterId
	 * @param templateId
	 * @return 
	 */
	@RequestMapping(value="/GetFocalShortcutData",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getFocalShortcutData(@RequestParam(value="focalIndex") String focalIndex,
			@RequestParam(value="tabId") Integer tabId,
			@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="chartId") Integer chartId,
			@RequestParam(value="encounterId") Integer encounterId,
			@RequestParam(value="templateId") Integer templateId){
		focalIndex=Optional.fromNullable(focalIndex).or("-1");
		tabId=Integer.parseInt(Optional.fromNullable(tabId+"").or("-1"));
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		chartId=Integer.parseInt(Optional.fromNullable(chartId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(FocalShortcutsService.getFocalShortcutData(focalIndex,tabId,patientId,chartId,encounterId,templateId).toString());
		return emrResponseBean;
	}
	
	/**
	 * Method to add new focal shortcut
	 * @param tabId
	 * @param patientId
	 * @param encounterId
	 * @param symptomIds
	 * @return
	 */
	@RequestMapping(value="/AddNewFocalShorctut",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean AddNewFocalShorctut(@RequestParam(value="tabId") Integer tabId,
			@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="encounterId") Integer encounterId,
			@RequestParam(value="symptomIds") String symptomIds){
		tabId=Integer.parseInt(Optional.fromNullable(tabId+"").or("-1"));
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		symptomIds=Optional.fromNullable(symptomIds+"").or("-1");
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(FocalShortcutsService.addNewFocalShorctut(tabId,patientId,encounterId,symptomIds).toString());
		return emrResponseBean;
	}
	
	/**
	 * Method to save focal data
	 * @param tabid
	 * @param shortcutName
	 * @param focalDescription
	 * @param xmlData
	 * @return 
	 */
	@RequestMapping(value="/SaveFocalData",method=RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean SaveFocalData(@RequestParam(value="tabid") String tabid,
			@RequestParam(value="shortcutName") String shortcutName,
			@RequestParam(value="focalDescription") String focalDescription,
			@RequestParam(value="xmlData") String xmlData){
		tabid=Optional.fromNullable(tabid+"").or("-1");
		shortcutName=Optional.fromNullable(shortcutName+"").or("-1");
		focalDescription=Optional.fromNullable(focalDescription+"").or("-1");
		xmlData=Optional.fromNullable(xmlData+"").or("-1");
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(FocalShortcutsService.saveFocalData(tabid,shortcutName,focalDescription,xmlData).toString());
		return emrResponseBean;
	}
	
	/**
	 * Method to save focal data
	 * @param tabid
	 * @param shortcutName
	 * @param focalDescription
	 * @param xmlData
	 * @return 
	 */
	@RequestMapping(value="/SearchFocalShortcuts",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean SearchFocalShortcuts(@RequestParam(value="tabId") Integer tabId,
			@RequestParam(value="focalsearch") String focalsearch){
		tabId=Optional.fromNullable(tabId).or(-1);
		focalsearch=Optional.fromNullable(focalsearch+"").or("-1");
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(FocalShortcutsService.searchFocalShortcuts(tabId,focalsearch).toString());
		return emrResponseBean;
	}
	
}