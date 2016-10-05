package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsQuestions;
import com.glenwood.glaceemr.server.application.models.ClinicalQuestionsGroup;
import com.glenwood.glaceemr.server.application.models.FileDetails;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.portal.portalForms.ClinicalIntakeXMLBean;
import com.glenwood.glaceemr.server.application.services.portal.portalForms.PortalClinicalIntakeAndConsentFormsListBean;
import com.glenwood.glaceemr.server.application.services.portal.portalForms.PortalConsentFormDetailsBean;
import com.glenwood.glaceemr.server.application.services.portal.portalForms.PortalFormsService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;




@RestController
@Transactional
@RequestMapping("/user/PortalForms")
@Api(value="PortalAppointmentsController", description="Can be used to request, cancel, get list of appointments by the patient for doctor.")
public class PortalFormsController {

	@Autowired
	PortalFormsService portalFormsService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	AuditTrailService auditTrailService;
	
	Logger logger=LoggerFactory.getLogger(LoginController.class);
	
	/**
	 * Appointment list of a patient appointments.
	 * @param patientId		: id of the required patient.
	 * @param appointmentsType		    : indicates future or past or present day appointment .
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/ClinicalIntakeAndConsent/FormsList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's appointments list", notes = "Returns a complete list of appointments.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's appointments list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PortalClinicalIntakeAndConsentFormsListBean getPatientClinicalIntakeAndConsentFormsList(@ApiParam(name="patientId", value="Patient ID") @RequestParam(value="patientId", required=false, defaultValue="0") int patientId,
			@ApiParam(name="chartId", value="Patient Chart ID") @RequestParam(value="chartId", required=false, defaultValue="0") int chartId,
			@ApiParam(name="isXML", value="forms type") @RequestParam(value="isXML", required=false, defaultValue="0") int isXML) throws Exception{
			
		return portalFormsService.getPatientClinicalIntakeAndConsentFormsList(patientId, chartId, isXML);
	}

	/**
	 * Appointment list of a patient appointments.
	 * @param patientId		: id of the required patient.
	 * @param appointmentsType		    : indicates future or past or present day appointment .
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/ClinicalIntake/FormsList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's appointments list", notes = "Returns a complete list of appointments.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's appointments list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<ClinicalQuestionsGroup> getPatientClinicalIntakeFormsList(@ApiParam(name="isXML", value="forms type") @RequestParam(value="isXML", required=false, defaultValue="0") int isXML) throws Exception{
			
		return portalFormsService.getPatientClinicalIntakeFormsList(isXML);
	}
	
	/**
	 * Appointment list of a patient appointments.
	 * @param patientId		: id of the required patient.
	 * @param appointmentsType		    : indicates future or past or present day appointment .
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/IntakeFormsQuestionnaire", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's appointments list", notes = "Returns a complete list of appointments.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's appointments list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<ClinicalElementsQuestions> getPatientAppointmentsList(@ApiParam(name="patientId", value="patient's id whose appointments list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="0") int patientId,
			@ApiParam(name="groupId", value="type of appointment (Future, Past, Present)") @RequestParam(value="groupId", required=false, defaultValue="present") int groupId,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{
		
		
			
		return portalFormsService.getLibraryQustionnaireElements(patientId, groupId);
	}
	
	/**
	 * Appointment list of a patient appointments.
	 * @param patientId		: id of the required patient.
	 * @param appointmentsType		    : indicates future or past or present day appointment .
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/ClinicalIntake/Form", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's appointments list", notes = "Returns a complete list of appointments.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's appointments list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public ClinicalIntakeXMLBean getPatientClinicalIntakeForm(@ApiParam(name="patientId", value="patient's id whose appointments list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="0") int patientId,
			@ApiParam(name="requestId", value="type of appointment (Future, Past, Present)") @RequestParam(value="requestId", required=false, defaultValue="-1") int requestId,
			@ApiParam(name="mode", value="type of appointment (Future, Past, Present)") @RequestParam(value="mode", required=false, defaultValue="-1") int mode,
			@ApiParam(name="groupId", value="type of appointment (Future, Past, Present)") @RequestParam(value="groupId", required=false, defaultValue="-1") int groupId,
			@ApiParam(name="groupName", value="type of appointment (Future, Past, Present)") @RequestParam(value="groupName", required=false, defaultValue="") String groupName,
			@ApiParam(name="isXML", value="type of appointment (Future, Past, Present)") @RequestParam(value="isXML", required=false, defaultValue="-1") int isXML,
			@ApiParam(name="tabId", value="type of appointment (Future, Past, Present)") @RequestParam(value="tabId", required=false, defaultValue="-1") int tabId,
			@ApiParam(name="isXML", value="type of appointment (Future, Past, Present)") @RequestParam(value="isXML", required=false, defaultValue="-1") int retXML,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{
		
		
		return portalFormsService.getClinicalIntakePatientXml(patientId, requestId, mode, groupId, groupName, isXML, retXML,tabId, pageOffset, pageIndex);
	}

	/**
	 * Appointment list of a patient appointments.
	 * @param patientId		: id of the required patient.
	 * @param appointmentsType		    : indicates future or past or present day appointment .
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/Consent/FormsList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's appointments list", notes = "Returns a complete list of appointments.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's appointments list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<FileDetails> getIncompletePatientConsentFormsList(@ApiParam(name="patientId", value="Patient ID") @RequestParam(value="patientId", required=false, defaultValue="0") int patientId,
			@ApiParam(name="chartId", value="Patient Chart ID") @RequestParam(value="chartId", required=false, defaultValue="0") int chartId) throws Exception{
			
		return portalFormsService.getIncompletePatientConsentFormsList(patientId, chartId);
	}
	
	
	/**
	 * Appointment list of a patient appointments.
	 * @param patientId		: id of the required patient.
	 * @param appointmentsType		    : indicates future or past or present day appointment .
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/Consent/FormDetails", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's appointments list", notes = "Returns a complete list of appointments.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's appointments list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PortalConsentFormDetailsBean getIncompletePatientConsentFormsList(@ApiParam(name="patientId", value="Patient ID") @RequestParam(value="patientId", required=false, defaultValue="0") int patientId,
			@ApiParam(name="chartId", value="Patient Chart ID") @RequestParam(value="chartId", required=false, defaultValue="0") int chartId,
			@ApiParam(name="fileTemplateId", value="File Template ID") @RequestParam(value="fileTemplateId", required=false, defaultValue="0") int fileTemplateId) throws Exception{
			
		System.out.println(objectMapper.writeValueAsString(portalFormsService.getIncompletePatientConsentFormsDetails(patientId, chartId, fileTemplateId)));
		return portalFormsService.getIncompletePatientConsentFormsDetails(patientId, chartId, fileTemplateId);
	}
	

	/**
	 * Appointment list of a patient appointments.
	 * @param patientId		: id of the required patient.
	 * @param appointmentsType		    : indicates future or past or present day appointment .
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/Consent/SaveForm", method = RequestMethod.POST)
    @ApiOperation(value = "Returns patient's appointments list", notes = "Returns a complete list of appointments.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's appointments list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public AlertEvent savePatientConsentForm(@RequestBody PortalConsentFormDetailsBean consentSaveBean) throws Exception{
			
		return portalFormsService.savePatientConsentForm(consentSaveBean);
	}

}
