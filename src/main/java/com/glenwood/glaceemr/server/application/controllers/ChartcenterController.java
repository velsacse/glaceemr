package com.glenwood.glaceemr.server.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.chartcenter.ChartcenterService;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;

@Api(value = "PatientLookup", description = "gets list of active patients", consumes="application/json")
@RestController
@RequestMapping(value="PatientSearch.Action")
public class ChartcenterController {

	@Autowired(required=true)
	private ChartcenterService patientSearchService;

	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	SessionMap sessionMap;
	@Autowired
	HttpServletRequest request;
	
	private Logger logger = Logger.getLogger(ChartcenterController.class);
	
	@RequestMapping(value = "",method = RequestMethod.GET)
	public Page<PatientRegistration>   getSearchResult(
			@RequestParam(value="toSearchData",required = false)String toSearchData,
			@RequestParam(value="searchType",required = false)String searchTypeParam) throws Exception{
		logger.debug("This is an info log entry");
        logger.error("This is an error log entry");
		Page<PatientRegistration>   patients = patientSearchService.getPatientSearchResult(toSearchData,searchTypeParam);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
			return patients;
	}










}
