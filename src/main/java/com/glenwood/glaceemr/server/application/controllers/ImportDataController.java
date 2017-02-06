package com.glenwood.glaceemr.server.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.chart.ImportData.ImportDataService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.google.common.base.Optional;

@RestController
@Transactional
@RequestMapping(value="/user/ImportData.Action")
public class ImportDataController {
	
	@Autowired
	ImportDataService importDataService;
	
	@RequestMapping(value="/ImportEncounterList",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getImportEncList(@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="encounterId") Integer encounterId) throws Exception{
		
		    patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		    encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		    EMRResponseBean emrResponseBean=new EMRResponseBean();
		    emrResponseBean.setData(importDataService.getImportEncList(patientId,encounterId));
	return emrResponseBean;
	}


	@RequestMapping(value="/ImportData",method=RequestMethod.GET)
	@ResponseBody
	public void  getImportData(@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="chartId") Integer chartId,
			@RequestParam(value="encounterId") Integer encounterId,
			@RequestParam(value="templateId") Integer templateId,
			@RequestParam(value="tabId") Integer tabId,
			@RequestParam(value="prevEncounterId") Integer prevEncounterId) throws Exception{

		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		chartId=Integer.parseInt(Optional.fromNullable(chartId+"").or("-1"));
		templateId=Integer.parseInt(Optional.fromNullable(templateId+"").or("-1"));
		tabId=Integer.parseInt(Optional.fromNullable(tabId+"").or("-1"));
		prevEncounterId=Integer.parseInt(Optional.fromNullable(prevEncounterId+"").or("-1"));
		importDataService.importData(patientId,encounterId,templateId,chartId,tabId,prevEncounterId);
	}


}