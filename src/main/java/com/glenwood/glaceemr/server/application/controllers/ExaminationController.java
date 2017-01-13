package com.glenwood.glaceemr.server.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.chart.Examination.ExaminationService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;


@Api(value = "Examination", description = "Examination Data", consumes="application/json")
@RestController
@RequestMapping(value="/user/ExaminationElements.Action")
public class ExaminationController {

	@Autowired
	ExaminationService examinationService;

	@RequestMapping(value="/ExaminationSystems",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getExaminationSystems(@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="chartId") Integer chartId,
			@RequestParam(value="encounterId") Integer encounterId,
			@RequestParam(value="templateId") Integer templateId,
			@RequestParam(value="clientId") String clientId) throws Exception{

		long start= System.currentTimeMillis();
		System.out.println("Getting PE systems Start ###################### "+start);
		System.out.println("patientId::"+patientId+"chartId::"+chartId+"encounterId::"+encounterId+"templateid::"+templateId);
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		chartId=Integer.parseInt(Optional.fromNullable(chartId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		templateId=Integer.parseInt(Optional.fromNullable(templateId+"").or("-1"));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(examinationService.getActiveSystems(clientId,patientId,chartId,encounterId,templateId));
		long end= System.currentTimeMillis();
		System.out.println("Getting PE systems End ###################### "+end+"\nTotal time taken:: "+(end-start)+"ms");
		System.out.println("======================================");
		System.out.println("Total time taken:: "+(end-start)+"ms");
		System.out.println("======================================");
		return emrResponseBean;
	}
	
	@RequestMapping(value="/ExaminationSystems/ExaminationElements",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getExaminationElements(@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="chartId") Integer chartId,
			@RequestParam(value="encounterId") Integer encounterId,
			@RequestParam(value="templateId") Integer templateId,
			@RequestParam(value="systemId") Integer systemId,
			@RequestParam(value="clientId") String clientId) throws Exception{
		
		long start= System.currentTimeMillis();
		System.out.println("Getting PE elements Start ###################### "+start);
		System.out.println("patientId::"+patientId+"chartId::"+chartId+"encounterId::"+encounterId+"templateid::"+templateId+"systemId::"+systemId);
		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		chartId=Integer.parseInt(Optional.fromNullable(chartId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		templateId=Integer.parseInt(Optional.fromNullable(templateId+"").or("-1"));
		systemId=Integer.parseInt(Optional.fromNullable(systemId+"").or("-1"));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(examinationService.getSystemActiveElements(clientId,patientId, chartId, encounterId, templateId, systemId));
		long end= System.currentTimeMillis();
		System.out.println("Getting PE elements End ###################### "+end);
		System.out.println("======================================");
		System.out.println("Total time taken:: "+(end-start)+"ms");
		System.out.println("======================================");
		return emrResponseBean;
	}
	
	
	@RequestMapping(value="/MarkAllNormal",method=RequestMethod.GET)
	@ResponseBody
	public void markAllNormal(@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="chartId") Integer chartId,
			@RequestParam(value="encounterId") Integer encounterId,
			@RequestParam(value="templateId") Integer templateId,
			@RequestParam(value="systemIds") String systemIds) throws Exception{

		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		templateId=Integer.parseInt(Optional.fromNullable(templateId+"").or("-1"));
		chartId=Integer.parseInt(Optional.fromNullable(chartId+"").or("-1"));
		examinationService.markAllNormal(patientId, encounterId, templateId,chartId,systemIds);
	}
	
	
	@RequestMapping(value="/ClearAllNormal",method=RequestMethod.GET)
	@ResponseBody
	public void clearAllNormal(@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="chartId") Integer chartId,
			@RequestParam(value="encounterId") Integer encounterId,
			@RequestParam(value="templateId") Integer templateId,
			@RequestParam(value="systemIds") String systemIds) throws Exception{

		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		templateId=Integer.parseInt(Optional.fromNullable(templateId+"").or("-1"));
		chartId=Integer.parseInt(Optional.fromNullable(chartId+"").or("-1"));
		examinationService.clearAllNormal(patientId, encounterId, templateId,chartId,systemIds);
	}
	
	@RequestMapping(value="/PeNotes",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getPeNotes(@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="encounterId") Integer encounterId) throws Exception{

		patientId=Integer.parseInt(Optional.fromNullable(patientId+"").or("-1"));
		encounterId=Integer.parseInt(Optional.fromNullable(encounterId+"").or("-1"));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(examinationService.getPENotes(patientId,encounterId));
		return emrResponseBean;
	}
	
	@RequestMapping(value="/getQuickNotes",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getQuickNotes(@RequestParam(value="tabId") Integer tabId,
			@RequestParam(value="elementId") String elementId) throws Exception{
		long start= System.currentTimeMillis();
		System.out.println("Getting quick notes Start ###################### "+start);
		System.out.println("tabId::"+tabId+"elementId::"+elementId);

		tabId=Integer.parseInt(Optional.fromNullable(tabId+"").or("-1"));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(examinationService.getQuickNotes(tabId,elementId));
		
		long end= System.currentTimeMillis();
		System.out.println("Getting quick notes End ###################### "+end);
		System.out.println("======================================");
		System.out.println("Total time taken:: "+(end-start)+"ms");
		System.out.println("======================================");
		return emrResponseBean;
	}
	
	@RequestMapping(value="/addQuickNotes",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getQuickNotes(@RequestParam(value="tabId") Integer tabId,
			@RequestParam(value="elementId") String elementId,
			@RequestParam(value="elementData") String elementData) throws Exception{

		tabId=Integer.parseInt(Optional.fromNullable(tabId+"").or("-1"));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(examinationService.addQuickNotes(tabId,elementId,elementData));
		return emrResponseBean;
	}
	
	@RequestMapping(value="/deleteQuickNotes",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean deleteQuickNotes(@RequestParam(value="dataListId") Integer dataListId) throws Exception{

		dataListId=Integer.parseInt(Optional.fromNullable(dataListId+"").or("-1"));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(examinationService.deleteQuickNotes(dataListId));
		return emrResponseBean;
	}
	
	
}