package com.glenwood.glaceemr.server.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.portal.portalMedicalSummary.PortalMedicalSummaryService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
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
	EMRResponseBean responseBean;
	
	
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
	public EMRResponseBean getSessionMap(@ApiParam(name="dbname", value="patient's username whose details are to be retrieved") @RequestParam(value="dbname", required=false, defaultValue="") String dbname,
			@ApiParam(name="username", value="patient's username whose details are to be retrieved") @RequestParam(value="username", required=false, defaultValue="") String username) throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMedicalSummaryService.getSessionMap(username));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving portal session!");
			return responseBean;
		}
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
	public EMRResponseBean getPatientDetailsByUsername(@ApiParam(name="username", value="patient's username whose details are to be retrieved") @RequestParam(value="username", required=false, defaultValue="") String username) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMedicalSummaryService.getPatientDetailsByUsername(username));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving patient details!");
			return responseBean;
		}
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
	public EMRResponseBean getPatientPersonalDetails(@ApiParam(name="patientId", value="patient's id whose personal details are to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMedicalSummaryService.getPatientPersonalDetails(patientId));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving patient personal details!");
			return responseBean;
		}
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
	public EMRResponseBean getPatientProblemsList(@ApiParam(name="patientId", value="patient's id whose problem list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="problemType", value="type of problem list is to be retrieved") @RequestParam(value="problemType", required=false, defaultValue="") String problemType,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMedicalSummaryService.getPatientProblemList(patientId, problemType, pageOffset, pageIndex));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving patient problems list!");
			return responseBean;
		}
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
	public EMRResponseBean getPatientAllergies(@ApiParam(name="chartId", value="chart id of a patient, whose allergies are to be retrieved") @RequestParam(value="chartId", required=false, defaultValue="") int chartId,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMedicalSummaryService.getPatientAllergies(chartId, pageOffset, pageIndex));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving patient allergies!");
			return responseBean;
		}
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
	public EMRResponseBean getPatientPlanOfCareList(@ApiParam(name="patientId", value="patient's id whose problem list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="chartId", value="chart id of a patient, whose allergies are to be retrieved") @RequestParam(value="chartId", required=false, defaultValue="") int chartId,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMedicalSummaryService.getPlanOfCare(patientId, chartId, pageOffset, pageIndex));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving patient plan of care!");
			return responseBean;
		}
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
	public EMRResponseBean getPatientPlanOfCarePlanTypesList(@ApiParam(name="patientId", value="patient's id whose problem list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="encounterId", value="chart id of a patient, whose allergies are to be retrieved") @RequestParam(value="encounterId", required=false, defaultValue="") int encounterId,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMedicalSummaryService.getPlanOfCareDetails(patientId, encounterId, pageOffset, pageIndex));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving plan of care plan types list!");
			return responseBean;
		}
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
	public EMRResponseBean getPortalMedicalSummaryDetails(@ApiParam(name="patientId", value="patient's id whose medical summary details are to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="chartId", value="chart id of a patient, whose medical summary details are to be retrieved") @RequestParam(value="chartId", required=false, defaultValue="") int chartId) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMedicalSummaryService.getPortalMedicalSummaryDetails(patientId, chartId));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving patient medical summary details!");
			return responseBean;
		}
	}
	
}
