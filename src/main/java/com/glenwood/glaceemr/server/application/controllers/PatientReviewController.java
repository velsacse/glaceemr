package com.glenwood.glaceemr.server.application.controllers;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.PatientReviewedDetails;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.chart.patientreview.PatientReviewDetailsBean;
import com.glenwood.glaceemr.server.application.services.chart.patientreview.PatientReviewService;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "PatientReview", description = "To get patient review information", consumes="application/json")
@RestController
@RequestMapping(value="PatientReview")
public class PatientReviewController {

	@Autowired
	AuditTrailService auditTrailService;

	@Autowired
	SessionMap sessionMap;

	@Autowired
	HttpServletRequest request;
	
	@Autowired
	PatientReviewService patientReviewService; 

	private Logger logger = Logger.getLogger(PatientReviewController.class);

	/**
	 * Get reviewed information
	 * @param userId
	 * @param chartId
	 * @return
	 */
	@ApiOperation(value = "Get reviewed information", notes = "Get reviewed user information")
	@RequestMapping(value="/getReviewInfo", method=RequestMethod.POST)
	@ResponseBody
	public PatientReviewDetailsBean getReviewInfo(
			@ApiParam(name="chartId",value="chart id") @RequestParam(value="chartId", required=false, defaultValue="-1") Integer chartId){
		
		logger.debug("Getting review information");		  
		return patientReviewService.getReviewInfo(chartId);
	}

	/**
	 * Save reviewed information
	 * @param userId
	 * @return
	 */
	@ApiOperation(value = "Save reviewed userid", notes = "Save reviewed userid")
	@RequestMapping(value="/saveReviewInfo", method=RequestMethod.POST)
	@ResponseBody
	public PatientReviewDetailsBean getReviewInfo(
			@ApiParam(name="userId",value="login user id") @RequestParam(value="userId", required=false, defaultValue="-1") Integer userId,
			@ApiParam(name="patientId",value="patient id") @RequestParam(value="patientId", required=false, defaultValue="-1") Integer patientId,
			@ApiParam(name="chartId",value="chart id") @RequestParam(value="chartId", required=false, defaultValue="-1") Integer chartId,
			@ApiParam(name="encounterId",value="encounter id") @RequestParam(value="encounterId", required=false, defaultValue="-1") Integer encounterId){
		
		logger.debug("Saving review information");
		
		PatientReviewedDetails patientReviewed = new PatientReviewedDetails();
		patientReviewed.setPatientReviewedDetailsBy(userId);
		patientReviewed.setPatientReviewedDetailsOn(new Timestamp(new Date().getTime()));
		patientReviewed.setPatientReviewedDetailsChartid(chartId);
		patientReviewed.setPatientReviewedDetailsPatientid(patientId);
		patientReviewed.setPatientReviewedDetailsEncounterid(encounterId);
		patientReviewService.saveReviewInfo(patientReviewed);
		
		return patientReviewService.getReviewInfo(chartId); 
	}
}