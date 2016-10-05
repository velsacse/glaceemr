package com.glenwood.glaceemr.server.application.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.H809;
import com.glenwood.glaceemr.server.application.models.PortalUser;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailServiceImpl;
import com.glenwood.glaceemr.server.application.services.employee.EmployeeService;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.PatientRegistrationSetupFields;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.PortalPatientRegistrationBean;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.PortalRegistrationResponse;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.PortalSettingsService;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.PrincipalDoctor;
import com.glenwood.glaceemr.server.application.services.portalLogin.PortalLoginService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


@RestController
@RequestMapping("/login/PatientRegistration")
@Api(value="PatientRegistrationController", description="Can be used to retrieve patient profile "
		+ "settings fields details, set the preferences e.t.c.., register patient, activate patient.")
public class PortalPatientRegistrationController {


	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	AuditTrailServiceImpl auditTrailService;

	Logger logger=LoggerFactory.getLogger(LoginController.class);

	@Autowired
	PortalSettingsService portalSettingsService;

	@Autowired
	PortalLoginService portalLoginService;

	@Autowired
	EmployeeService employeeService;


	/**
	 * @return List of patient profile settings fields with available options.
	 * @throws Exception
	 */
	@RequestMapping(value = "/RegistrationSetupFields", method = RequestMethod.GET)
	@ApiOperation(value = "Returns list of registration settings fields with available options.", notes = "Returns list of registration settings fields with available options.", response = User.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Successful retrieval of list of registration settings fields with available options"),
			@ApiResponse(code = 404, message = "Details doesn't exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PatientRegistrationSetupFields getPatientProfileSettingsFieldsOprions(){

		PatientRegistrationSetupFields setupFields=new PatientRegistrationSetupFields();
		setupFields.setPatientProfileSettingsFields(portalSettingsService.getPatientProfileSettingsFieldsList());
		setupFields.setPortalBillingConfigFields(portalSettingsService.getPortalBillingConfigFields());

		List<EmployeeProfile> doctorsList=employeeService.getEmployeeDetails("-1", "asc");

		List<PrincipalDoctor> principalDoctorsList=new ArrayList<PrincipalDoctor>();

		for(int i=0;i<doctorsList.size();i++){
			PrincipalDoctor doctor=new PrincipalDoctor();
			doctor.setPrincipalDoctorId(doctorsList.get(i).getEmpProfileEmpid());
			doctor.setPrincipalDoctorName(doctorsList.get(i).getEmpProfileFullname());
			principalDoctorsList.add(doctor);
		}

		setupFields.setPrincipalDoctorsList(principalDoctorsList);

		return setupFields;

	}


	/**
	 * @return List of patient profile settings fields with available options.
	 * @throws Exception
	 */
	@RequestMapping(value = "/UsernameVerification", method = RequestMethod.GET)
	@ApiOperation(value = "Returns list of registration settings fields with available options.", notes = "Returns list of registration settings fields with available options.", response = User.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Successful retrieval of list of registration settings fields with available options"),
			@ApiResponse(code = 404, message = "Details doesn't exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PortalRegistrationResponse verifyUsername(@ApiParam(name="username", value="username to be verified") @RequestParam(value="username", required=false, defaultValue="-1") String username,
			@ApiParam(name="dob", value="username to be verified") @RequestParam(value="dob", required=false, defaultValue="-1") String dob,
			@ApiParam(name="firstName", value="username to be verified") @RequestParam(value="firstName", required=false, defaultValue="-1") String firstName,
			@ApiParam(name="lastName", value="username to be verified") @RequestParam(value="lastName", required=false, defaultValue="-1") String lastName){

		PortalRegistrationResponse response=portalLoginService.checkDuplicatePatientData(username, dob, firstName, lastName);
		return response;

	}

	/**
	 * @return List of patient profile settings fields with available options.
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws JSONException 
	 * @throws Exception
	 */
	@RequestMapping(value = "/RegisterNewUserForPortal", method = RequestMethod.POST)
	@ApiOperation(value = "Register the patient", notes = "Register the patient.", response = User.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Patient registered successfully."),
			@ApiResponse(code = 404, message = "Details doesn't exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PortalRegistrationResponse registerNewUser(@ApiParam(name="registrationDetails", value="username to be verified") @RequestParam(value="registrationDetails", required=false, defaultValue="0") String registrationDetailsString,
			@ApiParam(name="dbname", value="username to be verified") @RequestParam(value="dbname", required=false, defaultValue="0") String dbname) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, JSONException{

		PortalRegistrationResponse response=portalLoginService.registerNewUserForPortal(objectMapper.readValue(registrationDetailsString, PortalPatientRegistrationBean.class), dbname);				
		return response;
	}
	
	/**
	 * @return List of patient profile settings fields with available options.
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws JSONException 
	 * @throws Exception
	 */
	@RequestMapping(value = "/RegisterExistingUserForPortal", method = RequestMethod.POST)
	@ApiOperation(value = "Register the patient", notes = "Register the patient.", response = User.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Patient signed up successfully."),
			@ApiResponse(code = 404, message = "Details doesn't exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PortalRegistrationResponse registerExistingUser(@ApiParam(name="registrationDetails", value="username to be verified") @RequestParam(value="registrationDetails", required=false, defaultValue="0") String registrationDetailsString,
			@ApiParam(name="dbname", value="username to be verified") @RequestParam(value="dbname", required=false, defaultValue="0") String dbname) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, JSONException{

		PortalRegistrationResponse response=portalLoginService.registerExistingUserForPortal(objectMapper.readValue(registrationDetailsString, PortalPatientRegistrationBean.class), dbname);				
		return response;
	}
	
	/**
	 * @return List of patient profile settings fields with available options.
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws JSONException 
	 * @throws Exception
	 */
	@RequestMapping(value = "/RequestSignupCredentials", method = RequestMethod.POST)
	@ApiOperation(value = "Register the patient", notes = "Register the patient.", response = User.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Patient signed up successfully."),
			@ApiResponse(code = 404, message = "Details doesn't exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PortalRegistrationResponse requestSignupCredentials(@ApiParam(name="registrationDetails", value="username to be verified") @RequestParam(value="registrationDetails", required=false, defaultValue="0") String registrationDetailsString,
			@ApiParam(name="dbname", value="username to be verified") @RequestParam(value="dbname", required=false, defaultValue="0") String dbname) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, JSONException{

		PortalRegistrationResponse response=portalLoginService.requestSignupCredentials(objectMapper.readValue(registrationDetailsString, PortalPatientRegistrationBean.class), dbname);				
		return response;
	}
	
	
	/**
	 * @return List of patient profile settings fields with available options.
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws JSONException 
	 * @throws Exception
	 */
	@RequestMapping(value = "/ActivateAccount", method = RequestMethod.POST)
	@ApiOperation(value = "Register the patient", notes = "Register the patient.", response = User.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Patient registered successfully."),
			@ApiResponse(code = 404, message = "Details doesn't exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PortalRegistrationResponse activateAccount(@ApiParam(name="patientId", value="patient id to activate the account") @RequestParam(value="patientId", required=false, defaultValue="0") int patientId,
			@ApiParam(name="practiceId", value="practice id") @RequestParam(value="practiceId", required=false, defaultValue="0") String practiceId) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, JSONException{
		
		PortalRegistrationResponse response=portalLoginService.activateUserAccount(patientId);
		return response;
	}

}
