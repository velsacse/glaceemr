package com.glenwood.glaceemr.server.application.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.InsuranceFilterBean;
import com.glenwood.glaceemr.server.application.models.PatientRegistrationBean;
import com.glenwood.glaceemr.server.application.services.employee.EmployeeService;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.PortalSettingsService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;



@RestController
@RequestMapping("/user/PortalSettings")
@Api(value="PortalSettingsController", description="Can be used to retrieve patient profile "
		+ "settings fields details, set the preferences e.t.c..")
public class PortalSettingsController {
	
	@Autowired
	PortalSettingsService portalSettingsService;
	
	@Autowired
	EmployeeService employeeService;

	@Autowired
	EMRResponseBean responseBean;
	
	Logger logger=LoggerFactory.getLogger(PortalSettingsController.class);
	
	/**
	 * @return List of patient profile settings fields with available options.
	 * @throws Exception
	 */
	@RequestMapping(value = "/ProfileSettingsFields", method = RequestMethod.GET)
    @ApiOperation(value = "Returns list of patient profile settings fields with available options.", notes = "Returns list of patient profile settings fields with available options.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of list of patient profile settings fields with available options"),
		    @ApiResponse(code = 404, message = "Details doesn't exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getPatientProfileSettingsFieldsOprions(){

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalSettingsService.getPatientProfileSettingsFieldsList());
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving profile settings fields!");
			return responseBean;
		}
		
	}
	
	/**
	 * @return List of billing config fields with available options.
	 * @throws Exception
	 */
	@RequestMapping(value = "/PortalBillingConfigFields", method = RequestMethod.GET)
    @ApiOperation(value = "Returns list of billing config fields with available options.", notes = "Returns list of billing config fields with available options.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of billing config fields with available options"),
		    @ApiResponse(code = 404, message = "Details doesn't exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getPortalBillingConfigFields(){

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalSettingsService.getPortalBillingConfigFields());
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in portal billing config fields!");
			return responseBean;
		}
		
	}
	
	
	/**
	 * @return List of doctors and physicians
	 * @throws Exception
	 */
	@RequestMapping(value = "/EmployeesList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns list of doctors and physicians.", notes = "Returns list of doctors and physicians.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of list of available doctors and physicians."),
		    @ApiResponse(code = 404, message = "Details doesn't exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getEmployeesList(@ApiParam(name="groupId", value="groupId of the employees") @RequestParam(value="groupId", required=false, defaultValue="") String groupId,
			@ApiParam(name="sort", value="sort order of the result") @RequestParam(value="sort", required=false, defaultValue="") String sort){

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(employeeService.getEmployeeDetails(groupId, sort));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving the principal doctors list!");
			return responseBean;
		}
		
	}
	
	/**
	 * @return List of active pos
	 * @throws Exception
	 */
	@RequestMapping(value = "/POSList", method = RequestMethod.GET)
    @ApiOperation(value = "list of active place of services.", notes = "Returns list of active place of services.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of list of active place of services."),
		    @ApiResponse(code = 404, message = "Details doesn't exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getActivePosList(){

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalSettingsService.getActivePosList());
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving the pos list!");
			return responseBean;
		}
		
	}
	
	/**
	 * @return list of providers
	 * @throws Exception
	 */
	@RequestMapping(value = "/ProvidersList", method = RequestMethod.GET)
    @ApiOperation(value = "list of providers.", notes = "Returns list of providers.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of list of providers."),
		    @ApiResponse(code = 404, message = "Details doesn't exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getProvidersList(){

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalSettingsService.getProvidersList());
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving the providers list!");
			return responseBean;
		}
		
	}
	
	/**
	 * @param regDetailsBean : credit card payment details bean. 
	 * @return PatientRegistration details with saved changes.
	 * @throws Exception
	 */
	@RequestMapping(value = "/SaveDemographicChanges", method = RequestMethod.POST)
    @ApiOperation(value = "Returns patient's registration details", notes = "Returns a complete list of patient's registration details", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's registration details"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public  EMRResponseBean saveDemographicChanges(@RequestBody PatientRegistrationBean regDetailsBean) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalSettingsService.saveDemographicChanges(regDetailsBean));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in saving the demographic changes!");
			return responseBean;
		}
	}
	
	

	/**
	 * @return List of patient profile settings fields with available options.
	 * @throws Exception
	 */
	@RequestMapping(value = "/GetActiveSessionForOldEMR", method = RequestMethod.GET)
    @ApiOperation(value = "returns the old emr session details.", notes = "returns the old emr session details.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of old emr session details."),
		    @ApiResponse(code = 404, message = "Details doesn't exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getActiveSessionForOldEMR(@ApiParam(name="patientId", value="gets the session details from old emr") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="chartId", value="groupId of the employees") @RequestParam(value="chartId", required=false, defaultValue="") int chartId) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalSettingsService.getActiveSessionForOldEMR(patientId, chartId));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving the old emr actice session!");
			return responseBean;
		}
		
	}
	
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @param chartId 		: Required patient's chart id 
	 * @return Patient Portal Config Details.
	 * @throws Exception
	 */
	@RequestMapping(value = "/PortalConfigDetails", method = RequestMethod.GET)
    @ApiOperation(value = "Returns  portal config details.", notes = "Returns  portal config details.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of portal config details"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getPortalConfigDetails(String JSESSIONID, @ApiParam(name="patientId", value="patient's id whose portal config details are to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="chartId", value="chart id of a patient, whose portal config details are to be retrieved") @RequestParam(value="chartId", required=false, defaultValue="") int chartId) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalSettingsService.getPortalConfigDetails(patientId, chartId));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving portal config fields!");
			return responseBean;
		}
	}
	
	
	/**
	 * @return EMRResponseBean with insurance list
	 * @throws Exception
	 */
	@RequestMapping(value = "/InsuranceList", method = RequestMethod.POST)
    @ApiOperation(value = "Returns list of filtered insurances", notes = "Returns list of filtered insurances", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Insurance list retrieval successful"),
		    @ApiResponse(code = 404, message = "Insurance list retrieval failure"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getInsuranceList(@RequestBody InsuranceFilterBean insFilterBean)throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalSettingsService.getInsuranceListList(insFilterBean));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving pharmacy list!");
			return responseBean;
		}		
	}
	
	
}
