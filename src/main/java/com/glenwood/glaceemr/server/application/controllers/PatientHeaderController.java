package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.print.PatientHeader;
import com.glenwood.glaceemr.server.application.models.print.PatientHeaderDetails;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.chart.print.patientheader.PatientHeaderService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "PatientHeaderConfiguration", description = "Patient Header", consumes="application/json")
@RestController
@RequestMapping(value="/user/GenericPatientHeader.Action")
public class PatientHeaderController {
	
	@Autowired
    PatientHeaderService patientHeaderService;
	
	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	HttpServletRequest request;
	
	private Logger logger = Logger.getLogger(PatientHeaderController.class);
	
	/**
	 * Request to get the list of all patient headers for configuration
	 * 
	 */
	@ApiOperation(value = "Get the list of all patient headers", notes = "Get the list of all patient headers")
	@RequestMapping(value = "/FetchPatientHeader",method = RequestMethod.GET)
	public EMRResponseBean fetchPatientHeaderList() throws Exception{
		logger.debug("Begin of request to get the list of all patient headers.");
		List<PatientHeader> patientHeaderList = patientHeaderService.getPatientHeaderList();
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded header list for configuration",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded header list for configuration");
		logger.debug("End of request to get the list of all patient headers.");
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(patientHeaderList);
		return respBean;
		
	}
	
	/**
	 * Request to get the list of all patient header details based on header id and page id for configuration
	 * 
	 */
	@ApiOperation(value = "Get patient headers details based on header id and page id", notes = "Get patient headers details based on header id and page id")
	@RequestMapping(value = "/FetchPatientHeaderDetails",method = RequestMethod.GET)
	public EMRResponseBean fetchPatientHeaderDetailsList(@RequestParam(value="headerId") Integer headerId,@RequestParam(value="pageId") Integer pageId) throws Exception{
		logger.debug("Begin of request to get patient headers details based on header id and page id.");
		List<PatientHeaderDetails> patientHeaderDetails = patientHeaderService.getPatientHeaderDetailList(headerId,pageId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded patient headers details based on header id and page id",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded patient headers details based on header id and page id");
		logger.debug("End of request to get patient headers details based on header id and page id.");
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(patientHeaderDetails);
		return respBean;
		
	}
	
	/**
	 * Request to save Patient header
	 * 
	 */
	@ApiOperation(value = "Save patient header", notes = "Save patient header")
	@RequestMapping(value = "/savePatientHeader",method = RequestMethod.POST)
	public EMRResponseBean savePatientHeader(@RequestParam(value="headerName") String headerName,@RequestParam(value="headerType") Integer headerType,
			@RequestParam(value="isDefault") Boolean isDefault) throws Exception{
		logger.debug("Begin of request to Save patient header.");
		
		PatientHeader newPatientHeader=new PatientHeader();
		newPatientHeader.setPatientHeaderName(headerName);
		newPatientHeader.setPatientHeaderType(headerType);
		newPatientHeader.setPatientHeaderIsDefault(isDefault);
		newPatientHeader.setPatientHeaderIsActive(true);
		PatientHeader patientHeader = patientHeaderService.savePatientHeader(newPatientHeader);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.CREATED,1,AuditLogConstants.SUCCESS,"Successfully saved patient header",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully saved patient header");
		logger.debug("End of request to Save patient header.");
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(patientHeader);
		return respBean;
		
	}
	
	/**
	 * Request to update Patient header
	 * 
	 */
	@ApiOperation(value = "Update Patient header", notes = "Update Patient header")
	@RequestMapping(value = "/updatePatientHeader",method = RequestMethod.POST)
	public EMRResponseBean updatePatientHeader(@RequestParam(value="headerName") String headerName,@RequestParam(value="headerType") Integer headerType,
			@RequestParam(value="isDefault") Boolean isDefault,@RequestParam(value="isActive") Boolean isActive,@RequestParam(value="headerId") Integer headerId) throws Exception{
		logger.debug("Begin of request to Update Patient header.");
		PatientHeader updatePatientHeader=new PatientHeader();
		updatePatientHeader.setPatientHeaderId(headerId);
		updatePatientHeader.setPatientHeaderName(headerName);
		updatePatientHeader.setPatientHeaderType(headerType);
		updatePatientHeader.setPatientHeaderIsDefault(isDefault);
		updatePatientHeader.setPatientHeaderIsActive(isActive);
		PatientHeader patientHeader = patientHeaderService.savePatientHeader(updatePatientHeader);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.UPDATE,1,AuditLogConstants.SUCCESS,"Successfully updated Patient header",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully updated Patient header");
		logger.debug("End of request to Update Patient header.");
		
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(patientHeader);
		return respBean;
		
	}  
	
	/**
	 * Request to delete patient header details
	 * 
	 */
	@ApiOperation(value = "Delete patient header details", notes = "Delete patient header details")
	@RequestMapping(value = "/deletePatientHeaderDetails",method = RequestMethod.POST)
	public void deletePatientHeaderDetails(@RequestParam(value="headerId") Integer headerId,@RequestParam(value="pageId") Integer pageId) throws Exception{
		logger.debug("Begin of request to delete patient header details.");
		List<PatientHeaderDetails> patientHeaderDetails = patientHeaderService.getPatientHeaderDetailList(headerId,pageId);
		if(patientHeaderDetails!=null && !patientHeaderDetails.isEmpty()){
			patientHeaderService.deletePatientHeaderDetails(patientHeaderDetails);
		}
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.DELETED,1,AuditLogConstants.SUCCESS,"Successfully deleted patient header details",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully deleted patient header details");
		logger.debug("End of request to delete patient header details.");
	}
	
	/**
	 * Request to save patient header details
	 * 
	 */
	@ApiOperation(value = "Save patient header details", notes = "Save patient header details")
	@RequestMapping(value = "/savePatientHeaderDetails",method = RequestMethod.POST)
	public void savePatientHeaderDetails(@RequestParam(value="headerId") Integer headerId,@RequestParam(value="pageId") Integer pageId,
			@RequestParam(value="componentId") Integer componentId,@RequestParam(value="order") Integer order) throws Exception{
		logger.debug("Begin of request to save patient header details.");
	
		PatientHeaderDetails newPatientHeaderDetails=new PatientHeaderDetails();
		newPatientHeaderDetails.setPatientHeaderId(headerId);
		newPatientHeaderDetails.setPageId(pageId);
		newPatientHeaderDetails.setComponentId(componentId);
		newPatientHeaderDetails.setComponentOrder(order);
		patientHeaderService.savePatientHeaderDetails(newPatientHeaderDetails);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.CREATED,1,AuditLogConstants.SUCCESS,"Successfully saved patient header details",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully saved patient header details");
		logger.debug("End of request to save patient header details.");
		
	}

}
