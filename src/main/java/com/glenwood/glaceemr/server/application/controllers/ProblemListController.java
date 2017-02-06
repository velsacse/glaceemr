package com.glenwood.glaceemr.server.application.controllers;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.ProblemList;
import com.glenwood.glaceemr.server.application.services.chart.problemlist.ProblemListService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;

@RestController
@RequestMapping(value="/user/ProblemList")
public class ProblemListController {
	
	@Autowired
	ProblemListService problemListService;
	
	private Logger logger = Logger.getLogger(ProblemListController.class);
	
	/**
	 * Method to get list of active problems
	 * @param patientId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ActiveProblems",method = RequestMethod.GET)
	public EMRResponseBean activeProblems(
			@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId) throws Exception {
		logger.debug("Fetching active problems");
		logger.error("Fetching active problems");		
		List<ProblemList> currentProblems = problemListService.getActiveProblems(patientId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(currentProblems);
		return emrResponseBean;
	}
	
	/**
	 * Method to retrieve list of inactive and resolved problems
	 * @param patientId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/InactiveProblems",method = RequestMethod.GET)
	public EMRResponseBean inactiveProblems(
			@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId) throws Exception {
		logger.debug("Fetching inactive and resolved problems");
		logger.error("Fetching inactive and resolved problems");		
		List<ProblemList> currentProblems = problemListService.getInactiveProblems(patientId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(currentProblems);
		return emrResponseBean;
	}
	
	/**
	 * Method to fetch edit page data
	 * @param patientId
	 * @param problemId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/EditFetch",method = RequestMethod.GET)
	public EMRResponseBean loadEditData(
			@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId,
			@RequestParam(value="problemId", required=false, defaultValue="") Integer problemId) throws Exception {
		logger.debug("Fetching data corresponds to edit dx");
		logger.error("Fetching data corresponds to edit dx");
		
		List<ProblemList> editData = problemListService.getEditData(patientId,problemId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(editData);
		return emrResponseBean;
	}
	
	/**
	 * Method to edit a problem
	 * @param patientId
	 * @param userId
	 * @param saveData
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/EditSave",method = RequestMethod.GET)
	public EMRResponseBean saveEditData(
			@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId,
			@RequestParam(value="userId", required=false, defaultValue="") Integer userId,
			@RequestParam(value="saveData", required=false, defaultValue="") String saveData) throws Exception {
		logger.debug("Updating problem list data");
		logger.error("Updating problem list data");		
		problemListService.editDataSave(patientId,userId,saveData);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData("edited");
	    return emrResponseBean;
	}
	
	/**
	 * Method to delete a problem from problem list
	 * @param patientId
	 * @param deleteData
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/DeleteSave",method = RequestMethod.GET)
	public EMRResponseBean deleteDxData(
			@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId,
			@RequestParam(value="deleteData", required=false, defaultValue="") String deleteData) throws Exception {
		logger.debug("Problem list delete");
		logger.error("Problem list delete");
		problemListService.deleteDataSave(patientId,deleteData);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData("deleted");
	    return emrResponseBean;
	}
	
	
	/**
	 * Method to delete a problem from problem list while importing in Assessment
	 * @param patientId
	 * @param deleteData
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/DeleteSaveFetch",method = RequestMethod.GET)
	public EMRResponseBean deleteDxDataAndFetch(
			@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId,
			@RequestParam(value="deleteData", required=false, defaultValue="") String deleteData) throws Exception {
		logger.debug("Problem list delete");
		logger.error("Problem list delete");
		
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(problemListService.deleteDataSaveFetch(patientId,deleteData));
	    return emrResponseBean;
	}
	
}
