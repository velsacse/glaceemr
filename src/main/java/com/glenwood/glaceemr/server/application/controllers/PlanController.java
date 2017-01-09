package com.glenwood.glaceemr.server.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.chart.plan.PlanService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * Controller for plan module
 * @author Chandrahas
 *
 */
@Api(value = "/user/Plan", description = "Plan", consumes="application/json")
@RestController
@RequestMapping(value="/user/Plan")
public class PlanController {
	
	@Autowired
	EMRResponseBean emrResponseBean;
	
	@Autowired
	PlanService service;
	
	/**
	 * Get systems
	 * @param patientId
	 * @param chartId
	 * @param encounterId
	 * @param templateId
	 * @param tabId
	 * @param clientId
	 * @return
	 */
	@ApiOperation(value="/FetchSystems")
	@RequestMapping(value="/FetchSystems", method=RequestMethod.GET)	
	public EMRResponseBean getSystems(
		@RequestParam(value="patientId", defaultValue="-1", required=false) Integer patientId,
		@RequestParam(value="chartId", defaultValue="-1", required=false) Integer chartId,
		@RequestParam(value="encounterId", defaultValue="-1", required=false) Integer encounterId,
		@RequestParam(value="templateId", defaultValue="-1", required=false) Integer templateId,
		@RequestParam(value="tabId", defaultValue="-1", required=false) Integer tabId,
		@RequestParam(value="clientId", defaultValue="-1", required=false) String clientId){
			
		emrResponseBean.setData(service.getSystems(patientId, chartId, encounterId, templateId, clientId, tabId));
		return emrResponseBean;
	}
	
	/**
	 * Get instructions
	 * @param patientId
	 * @param encounterId
	 * @param templateId
	 * @return
	 */
	@ApiOperation(value="/FetchInstructions")
	@RequestMapping(value="/FetchInstructions", method=RequestMethod.GET)	
	public EMRResponseBean getInstructions(
		@RequestParam(value="patientId", defaultValue="-1", required=true) Integer patientId,
		@RequestParam(value="chartId", defaultValue="-1", required=true) Integer chartId,
		@RequestParam(value="encounterId", defaultValue="-1", required=true) Integer encounterId,
		@RequestParam(value="templateId", defaultValue="-1", required=true) Integer templateId,
		@RequestParam(value="tabId", defaultValue="-1", required=true) Integer tabId,
		@RequestParam(value="clientId", defaultValue="-1", required=false) String clientId,
		@RequestParam(value="dxcode", defaultValue="", required=false) String dxcode){
			
		emrResponseBean.setData(service.getInstructions(patientId, chartId, encounterId, templateId, clientId, tabId, dxcode));
		return emrResponseBean;
	}
	
	/**
	 * Get notes
	 * @param patientId
	 * @param encounterId
	 * @param dxcode
	 * @return
	 */
	@ApiOperation(value="/FetchNotes")
	@RequestMapping(value="/FetchNotes", method=RequestMethod.GET)
	public EMRResponseBean getNotes(
		@RequestParam(value="patientId", defaultValue="-1", required=false) Integer patientId,
		@RequestParam(value="encounterId", defaultValue="-1", required=false) Integer encounterId,
		@RequestParam(value="dxcode", defaultValue="", required=false) String dxcode){
		
		emrResponseBean.setData(service.getNotes(patientId, encounterId, dxcode));
		return emrResponseBean;
		
	}
	
	/**
	 * Get plan shortcuts
	 * @param limit
	 * @param offset
	 * @return
	 */
	@ApiOperation(value="/FetchPlanShortcuts")
	@RequestMapping(value="/FetchPlanShortcuts", method= RequestMethod.GET)
	public EMRResponseBean getPlanShortcus(
		@RequestParam(value="limit", defaultValue="10", required=false) Integer limit,
		@RequestParam(value="offset", defaultValue="0", required=false) Integer offset,
		@RequestParam(value="key", defaultValue="", required=false) String key){
		
		emrResponseBean.setData(service.getPlanShortcuts(limit, offset, key));
		return emrResponseBean;
	}
	
	/**
	 * Get Patient instructions
	 * @param encounterId
	 * @return
	 */
	@ApiOperation(value="/FetchPatientInstructions")
	@RequestMapping(value="/FetchPatientInstructions", method= RequestMethod.GET)
	public EMRResponseBean fetchPatientInstructions(
		@RequestParam(value="encounterId", defaultValue="-1", required= true) Integer encounterId){

		emrResponseBean.setData(service.getPatientIns(encounterId, "0000400000000000007"));
		return emrResponseBean;
	}
	
	/**
	 * Get Current diagnosis
	 * @param encounterId
	 * @param patientId
	 * @return
	 */
	@ApiOperation(value="/FetchCurrentDiagnosis")
	@RequestMapping(value="/FetchCurrentDiagnosis", method= RequestMethod.GET)
	public EMRResponseBean fetchCurrentDiagnosis(
		@RequestParam(value="encounterId", defaultValue="-1", required= true) Integer encounterId,
		@RequestParam(value="patientId", defaultValue="-1", required= true) Integer patientId){

		emrResponseBean.setData(service.getCurrentDx(patientId, encounterId));
		return emrResponseBean;
	}
	
	/**
	 * Get Return visit elements
	 * @param encounterId
	 * @param templateId
	 * @return
	 */
	@ApiOperation(value="/FetchReturnVisit")
	@RequestMapping(value="/FetchReturnVisit", method= RequestMethod.GET)
	public EMRResponseBean fetchReturnVisit(
		@RequestParam(value="encounterId", defaultValue="-1", required= true) Integer encounterId,
		@RequestParam(value="templateId", defaultValue="-1", required= true) Integer templateId){

		emrResponseBean.setData(service.getReturnVisit(templateId, encounterId));
		return emrResponseBean;
	}
	
	/**
	 * Map plan instruction to dx code
	 * @param insId
	 * @param mappingType
	 * @param dxcode
	 * @param codingsystem
	 * @return
	 */
	@ApiOperation(value="/mapInstructiontoDx")
	@RequestMapping(value="/mapInstructiontoDx", method= RequestMethod.GET)
	public EMRResponseBean mapInstructiontoDx(
		@RequestParam(value="insId", defaultValue="-1", required=true) Integer insId,
		@RequestParam(value="mappingType", defaultValue="-1", required=true) Integer mappingType,
		@RequestParam(value="dxcode", defaultValue="", required=true) String dxcode,
		@RequestParam(value="codingsystem", defaultValue="-1", required=true) String codingsystem){
		
		service.mapInstructiontoDx(insId, dxcode, mappingType, codingsystem);
		emrResponseBean.setData("success");
		return emrResponseBean;
	}
	
	@ApiOperation(value="/updateAftercareIns")
	@RequestMapping(value="/updateAftercareIns", method= RequestMethod.GET)
	public EMRResponseBean updateAftercareIns(
		@RequestParam(value="insId", defaultValue="-1", required=true) Integer insId,
		@RequestParam(value="insName", defaultValue="", required=true) String insName,
		@RequestParam(value="insStatus", defaultValue="false", required=true) Boolean insStatus,
		@RequestParam(value="encounterId", defaultValue="-1", required=true) Integer encounterId,
		@RequestParam(value="patientId", defaultValue="-1", required=true) Integer patientId,
		@RequestParam(value="otherIns", defaultValue="-1", required=false) Integer otherIns,
		@RequestParam(value="dxHandout", defaultValue="-1", required=false) Integer dxHandout,
		@RequestParam(value="dxCode", defaultValue="", required=false) String dxCode,
		@RequestParam(value="codingsystem", defaultValue="", required=false) String codingsystem){
		
		service.updatePatientAftercareIns(insId, insName, insStatus, encounterId, patientId, otherIns, dxHandout, dxCode, codingsystem);
		emrResponseBean.setData("success");
		return emrResponseBean;
	}
	
	@ApiOperation(value="/getLanguages")
	@RequestMapping(value="/getLanguages", method= RequestMethod.GET)
	public EMRResponseBean getLanguages(
		@RequestParam(value="insId", defaultValue="-1", required=true) Integer insId,
		@RequestParam(value="patientId", defaultValue="-1", required=true) Integer patientId){
		
		service.getLanguages(insId, patientId);
		emrResponseBean.setData("success");
		return emrResponseBean;
	}
	
	@ApiOperation(value="/getReferringPhysicians")
	@RequestMapping(value="/getReferringPhysicians", method= RequestMethod.GET)
	public EMRResponseBean getReferringPhysicians(){
		
		emrResponseBean.setData(service.getReferringPhysicians());
		return emrResponseBean;
	}
		
}
