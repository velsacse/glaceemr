package com.glenwood.glaceemr.server.application.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.portal.RecoverPortalPasswordBean;
import com.glenwood.glaceemr.server.application.services.portal.portalRecoverUserPasswordService;
import com.glenwood.glaceemr.server.application.services.portal.portalAppointments.PortalAppointmentsService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Transactional
@RequestMapping("/login/RecoverPortalPassword")
@Api(value="PortalRecoverUserPasswordController", description="Can be used to recover user passsword")
public class PortalRecoverUserPasswordController {

	@Autowired
	PortalAppointmentsService portalAppointmentsService;
	
	@Autowired
	portalRecoverUserPasswordService portalRecoverPasswordService;
	
	Logger logger=LoggerFactory.getLogger(PortalRecoverUserPasswordController.class);

	@Autowired
	EMRResponseBean responseBean;
	
	/**
	 * Appointment Requests list of a patient.
	 * @param patientId		: id of the required patient.
	 * @return List of Appointments Requests of a patient.
	 */
	@RequestMapping(value = "/AuthenticateUsername", method = RequestMethod.POST)
    @ApiOperation(value = "Returns  security questions of a patient", notes = "Returns  security questions of a patient", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's security questions"),
		    @ApiResponse(code = 404, message = "Patient with given username does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getPatientAppointmentRequestsList(@RequestBody RecoverPortalPasswordBean recoverPasswordBean) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(false);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalRecoverPasswordService.authenticateUsernameAndGetSecurityQuestions(recoverPasswordBean));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in authenticating the username!");
			return responseBean;
		}
	}
	
	/**
	 * Appointment list of a patient appointments.
	 * @param patientId		: id of the required patient.
	 * @param appointmentsType		    : indicates future or past or present day appointment .
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/AuthenticateSecurityQuestions", method = RequestMethod.POST)
    @ApiOperation(value = "Authenticates security questions and sends password to an email", notes = "Authenticates security questions and sends password to an email", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "sent the password to patient email successfully"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getAuthenticationDetails(@RequestBody RecoverPortalPasswordBean recoverPasswordBean) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(false);

		try {
			return portalRecoverPasswordService.authenticateSecurityQuestions(recoverPasswordBean);
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in authenticating security questions!");
			return responseBean;
		}
	}	
}
