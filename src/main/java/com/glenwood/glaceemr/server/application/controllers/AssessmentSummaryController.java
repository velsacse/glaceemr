package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.H611;
import com.glenwood.glaceemr.server.application.models.ProblemList;
import com.glenwood.glaceemr.server.application.services.chart.assessment.AssessmentSummaryService;
import com.glenwood.glaceemr.server.application.services.chart.problemlist.ProblemListService;
import com.wordnik.swagger.annotations.Api;


@Api(value = "Assessment", description = "gets list of active problems of a patient", consumes="application/json")
@RestController
@RequestMapping(value="/user/Assessment.Action")
public class AssessmentSummaryController {
	
	@Autowired
	AssessmentSummaryService assessmentService;
	
	@Autowired
	ProblemListService problemListService;
	
	private Logger logger = Logger.getLogger(AssessmentSummaryController.class);
	
	/**
	 * Method to retrieve current visit problems
	 * @param patientId
	 * @param encounterId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/CurrentDiagnosis",method = RequestMethod.GET)
	public List<H611> getCurrentDiagnosis(
			@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId,
			@RequestParam(value="encounterId", required=false, defaultValue="") Integer encounterId) throws Exception {
		logger.debug("getting current diagnosis");
		logger.error("getting current diagnosis");
		List<H611> currentProblems = assessmentService.getCurrentDiagnosis(patientId,encounterId);
		
		return currentProblems;
	}
	
	/**
	 * Method to save current diagnosis data
	 * @param assessListobj
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/SaveCodes",method = RequestMethod.GET)
	public boolean saveCurrentDiagnosis(
			@RequestParam(value="assessListobj", required=false, defaultValue="") JSONObject assessListobj) throws Exception {
		logger.debug("getting current diagnosis");
		logger.error("getting current diagnosis");
		Boolean success = assessmentService.saveDiagnosis(assessListobj);
		return success;		
	}
	
	
	/**
	 * Method to fetch active problems data
	 * @param patientId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ActiveProblems",method = RequestMethod.GET)
	public List<ProblemList> activeProblems(
			@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId) throws Exception {
		logger.debug("Fetching active problems");
		logger.error("Fetching active problems");		
		List<ProblemList> currentProblems = problemListService.getActiveProblems(patientId);
		
		return currentProblems;
	}
	
	/**
	 * Method to fetch edit page data
	 * @param patientId
	 * @param encounterId
	 * @param dxCode
	 * @param problemId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/EditFetch",method = RequestMethod.GET)
	public List<H611> loadEditData(
			@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId,
			@RequestParam(value="encounterId", required=false, defaultValue="") Integer encounterId,
			@RequestParam(value="dxCode", required=false, defaultValue="") String dxCode,
			@RequestParam(value="problemId", required=false, defaultValue="") Integer problemId) throws Exception {
		logger.debug("Fetching data corresponds to edit dx");
		logger.error("Fetching data corresponds to edit dx");
		
		List<H611> editData = assessmentService.getEditData(patientId,encounterId,dxCode,problemId);
		return editData;
	}

	/**
	 * Method to send current encounter assessments to problem list 
	 * @param patientId
	 * @param encounterId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ProblemlistData",method = RequestMethod.GET)
	public String loadProblemlist(
			@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId,
			@RequestParam(value="encounterId", required=false, defaultValue="") Integer encounterId,
			@RequestParam(value="userId", required=false, defaultValue="") Integer userId) throws Exception {
		logger.debug("load data corresponds to current dx");		
		String message = assessmentService.moveToProblemList(patientId,encounterId,userId);
		return message;
	}

}
