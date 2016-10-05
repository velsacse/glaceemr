package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.H809;
import com.glenwood.glaceemr.server.application.models.PortalSessionBean;
import com.glenwood.glaceemr.server.application.models.PatientAllergies;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PortalMedicalSummaryBean;
import com.glenwood.glaceemr.server.application.models.PortalPlanOfCareBean;
import com.glenwood.glaceemr.server.application.models.ProblemList;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.portal.portalMedicalSummary.PortalMedicalSummaryService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


@RestController
@Transactional
@RequestMapping(value="/user/PortalMedicalSummary")
@Api(value="PortalMedicalSummaryController", description="Gets the patient medical summary details of a patient")
public class PortalMedicalSummaryController {

	@Autowired
	PortalMedicalSummaryService portalMedicalSummaryService;
	
	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	Logger logger=LoggerFactory.getLogger(LoginController.class);
	
	
	/**
	 * @param username 		: username of a patient
	 * @return List of patient details
	 * @throws Exception
	 */
	@RequestMapping(value = "/SessionMap", method = RequestMethod.GET)
    @ApiOperation(value = "Returns Session Map", notes = "Returns a complete list session details.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of session details"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PortalSessionBean getSessionMap() throws Exception{
		
		PortalSessionBean patientDetailsList=portalMedicalSummaryService.getSessionMap();
		return patientDetailsList;
	}
	
	
	/**
	 * @param username 		: username of a patient
	 * @return List of patient details
	 * @throws Exception
	 */
	@RequestMapping(value = "/PatientDetails", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient details", notes = "Returns a complete list of patient details by username.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient details"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<H809> getPatientDetailsByUsername(@ApiParam(name="username", value="patient's username whose details are to be retrieved") @RequestParam(value="username", required=false, defaultValue="") String username) throws Exception{
		
		List<H809> patientDetailsList=portalMedicalSummaryService.getPatientDetailsByUsername(username);
		return patientDetailsList;
	}
	
	
	
	
	
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @return List of patient's personal details
	 * @throws Exception
	 */
	@RequestMapping(value = "/PatientPersonalDetails", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's personal details", notes = "Returns a complete list of patient's personal details.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's personal details"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PatientRegistration getPatientPersonalDetails(@ApiParam(name="patientId", value="patient's id whose personal details are to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId) throws Exception{
		
		PatientRegistration personalDetailsList=portalMedicalSummaryService.getPatientPersonalDetails(patientId);
		
		
		return personalDetailsList;
	}
	

	/**
	 * @param patientId 		: Required patient's id 
	 * @return List of patient's problems
	 * @throws Exception
	 */
	@RequestMapping(value = "/PatientProblemsList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's problemList", notes = "Returns a complete list of patient's problems.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's problems"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<ProblemList> getPatientProblemsList(@ApiParam(name="patientId", value="patient's id whose problem list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="problemType", value="type of problem list is to be retrieved") @RequestParam(value="problemType", required=false, defaultValue="") String problemType,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{
		
		List<ProblemList> problemsList=portalMedicalSummaryService.getPatientProblemList(patientId, problemType, pageOffset, pageIndex);
		
		
		return problemsList;
	}
	
     
	
	/**
	 * @param chartId 		: Chart id of a required patient 
	 * @return List of patient's problems
	 * @throws Exception
	 */
	@RequestMapping(value = "/PatientAllergies", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's allergies list", notes = "Returns a complete list of patient's allergies.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's allergies"),
		    @ApiResponse(code = 404, message = "Patient with given chart id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<PatientAllergies> getPatientAllergies(@ApiParam(name="chartId", value="chart id of a patient, whose allergies are to be retrieved") @RequestParam(value="chartId", required=false, defaultValue="") int chartId,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{
		
		List<PatientAllergies> patientAllergies=portalMedicalSummaryService.getPatientAllergies(chartId, pageOffset, pageIndex);
		
		
		return patientAllergies;
	}
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @param chartId 		: Required patient's chart id 
	 * @return List of plans of care of a patient.
	 * @throws Exception
	 */
	@RequestMapping(value = "/PlanOfCare/PatientPlanOfCare", method = RequestMethod.GET)
    @ApiOperation(value = "Returns  list of plans of care of a patient.", notes = "Returns a complete  list of plans of care of a patient.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's problems"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<PortalPlanOfCareBean> getPatientPlanOfCareList(@ApiParam(name="patientId", value="patient's id whose problem list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="chartId", value="chart id of a patient, whose allergies are to be retrieved") @RequestParam(value="chartId", required=false, defaultValue="") int chartId,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{
		
		List<PortalPlanOfCareBean> planOfCareList=portalMedicalSummaryService.getPlanOfCare(patientId, chartId, pageOffset, pageIndex);
		return planOfCareList;
	}
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @param chartId 		: Required patient's chart id 
	 * @return List of plans of care plan types list of a patient.
	 * @throws Exception
	 */
	@RequestMapping(value = "/PlanOfCare/PatientPlanTypesList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns  list of plans of care of a patient.", notes = "Returns a complete  list of plans of care of a patient.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's problems"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<PatientClinicalElements> getPatientPlanOfCarePlanTypesList(@ApiParam(name="patientId", value="patient's id whose problem list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="encounterId", value="chart id of a patient, whose allergies are to be retrieved") @RequestParam(value="encounterId", required=false, defaultValue="") int encounterId,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{
		
		List<PatientClinicalElements> planOfCarePlanTypesList=portalMedicalSummaryService.getPlanOfCareDetails(patientId, encounterId, pageOffset, pageIndex);
		
		
		return planOfCarePlanTypesList;
	}
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @param chartId 		: Required patient's chart id 
	 * @return PortalMedicalSummaryBean.
	 * @throws Exception
	 */
	@RequestMapping(value = "/MedicalSummaryDetails", method = RequestMethod.GET)
    @ApiOperation(value = "Returns  MedicalSummaryDetails.", notes = "Returns MedicalSummaryDetails.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of MedicalSummaryDetails"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PortalMedicalSummaryBean getPortalMedicalSummaryDetails(@ApiParam(name="patientId", value="patient's id whose medical summary details are to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="chartId", value="chart id of a patient, whose medical summary details are to be retrieved") @RequestParam(value="chartId", required=false, defaultValue="") int chartId) throws Exception{
		
		PortalMedicalSummaryBean summaryDetails=portalMedicalSummaryService.getPortalMedicalSummaryDetails(patientId, chartId);
		
		
		return summaryDetails;
	}
	
}
