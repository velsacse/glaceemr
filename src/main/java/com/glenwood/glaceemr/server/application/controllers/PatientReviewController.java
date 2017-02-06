package com.glenwood.glaceemr.server.application.controllers;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.PatientReviewedDetails;
import com.glenwood.glaceemr.server.application.services.chart.patientreview.PatientReviewService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;

@RestController
@RequestMapping(value="/user/PatientReview")
public class PatientReviewController {
	
	@Autowired
	PatientReviewService patientReviewService; 

	private Logger logger = Logger.getLogger(PatientReviewController.class);

	/**
	 * Get reviewed information
	 * @param userId
	 * @param chartId
	 * @return
	 */
	@RequestMapping(value="/getReviewInfo", method=RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean getReviewInfo(
			 @RequestParam(value="chartId", required=false, defaultValue="-1") Integer chartId){
		
		logger.debug("Getting review information");
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(patientReviewService.getReviewInfo(chartId));
		return respBean;
	}

	/**
	 * Save reviewed information
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/saveReviewInfo", method=RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean getReviewInfo(
			 @RequestParam(value="userId", required=false, defaultValue="-1") Integer userId,
			 @RequestParam(value="patientId", required=false, defaultValue="-1") Integer patientId,
			@RequestParam(value="chartId", required=false, defaultValue="-1") Integer chartId,
			@RequestParam(value="encounterId", required=false, defaultValue="-1") Integer encounterId){
		
		logger.debug("Saving review information");
		
		PatientReviewedDetails patientReviewed = new PatientReviewedDetails();
		patientReviewed.setPatientReviewedDetailsBy(userId);
		patientReviewed.setPatientReviewedDetailsOn(new Timestamp(new Date().getTime()));
		patientReviewed.setPatientReviewedDetailsChartid(chartId);
		patientReviewed.setPatientReviewedDetailsPatientid(patientId);
		patientReviewed.setPatientReviewedDetailsEncounterid(encounterId);
		patientReviewService.saveReviewInfo(patientReviewed);
		
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(patientReviewService.getReviewInfo(chartId));
		return respBean; 
	}
}