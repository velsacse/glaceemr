package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

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
import com.glenwood.glaceemr.server.application.models.H068;
import com.glenwood.glaceemr.server.application.models.ImmunizationRecord;
import com.glenwood.glaceemr.server.application.models.PatientImmunizationInformation;
import com.glenwood.glaceemr.server.application.models.Vaccine;
import com.glenwood.glaceemr.server.application.models.VaccineUpdateBean;
import com.glenwood.glaceemr.server.application.models.Vis;
import com.glenwood.glaceemr.server.application.services.portal.portalImmunizationHistory.PortalImmunizationHistoryService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


@RestController
@Transactional
@RequestMapping("/user/PortalImmunizationHistory")
@Api(value="PortalImmunizationHistoryController", description="Used to get immunization history of a patient")
public class PortalImmunizationHistoryController {

	
	@Autowired
	PortalImmunizationHistoryService portalImmunizationHistoryService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	
	/**
	 * Appointment list of a patient appointments.
	 * @param patientId		: id of the required patient.
	 * @param appointmentsType		    : indicates future or past or present day appointment .
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/ImmunizationHistory", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's appointments list", notes = "Returns a complete list of appointments.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's appointments list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<ImmunizationRecord> getPatientAppointmentsList(@ApiParam(name="patientId", value="patient's id whose appointments list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="0") int patientId,
			@ApiParam(name="chartId", value="type of appointment (Future, Past, Present)") @RequestParam(value="chartId", required=false, defaultValue="present") int chartId,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{
		
		
		List<ImmunizationRecord> recordList=portalImmunizationHistoryService.getPatientImmunizationHistory(patientId, chartId, pageOffset, pageIndex);
		
		return recordList;
		
	}
	
	/**
	 * Appointment list of a vaccine vsi files
	 * @param patientId		: lab description cvx
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/VISFilesList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's appointments list", notes = "Returns a complete list of appointments.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of lab VIS files"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<Vis> getVaccineVISFilesList(@ApiParam(name="labDescCVX", value="lab cvx for which VIS files need to be retrieved") @RequestParam(value="labDescCVX", required=false, defaultValue="0") String labDescCVX) throws Exception{
		
		List<Vis> vaccineVISFilesList=portalImmunizationHistoryService.getVaccineVISFilesList(labDescCVX);
		
		return vaccineVISFilesList;
		
	}
	
	/**
	 * Vaccine List
	 * @param searchKey		: key to search
	 * @param pageOffset	: offset of the page.
	 * @param pageIndex		: index of the page.
	 * @return Vaccines list
	 */
	@RequestMapping(value = "/VaccineList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns vaccines list", notes = "Returns vaccines list.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of vaccines list"),
		    @ApiResponse(code = 404, message = "Not Found"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<Vaccine> getVaccineList(@ApiParam(name="searchKey", value="type of appointment (Future, Past, Present)") @RequestParam(value="searchKey", required=false, defaultValue="present") String searchKey) throws Exception{
		
		
		List<Vaccine> vaccineList=portalImmunizationHistoryService.getVaccineList(searchKey);
		
		return vaccineList;
		
	}
	
	/**
	 * Vaccine Update Reasons list of a patient appointments.
	 * @return reasons list.
	 */
	@RequestMapping(value = "/VaccineUpdateReasonsList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns vaccine update reasons list", notes = "Returns vaccine update reasons list", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of vaccine update reaons list"),
		    @ApiResponse(code = 404, message = "No Found"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<H068> getVaccUpdateReasonList() throws Exception{
		
		
		List<H068> vaccineUpdateReasonsList=portalImmunizationHistoryService.getVaccUpdateReasonList();
		
		return vaccineUpdateReasonsList;
		
	}
	
	/**
	 * @return Update vaccine info
	 * @throws Exception
	 */
	@RequestMapping(value = "/VaccineUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "Returns patient vaccine information", notes = "Returns patient vaccine information", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Vaccine information updated successfully."),
		    @ApiResponse(code = 404, message = "Vaccine updation failure"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public  PatientImmunizationInformation bookAppointment(@RequestBody VaccineUpdateBean vaccineUpdateBean) throws Exception{
		
		PatientImmunizationInformation patientImmunizationInformation=portalImmunizationHistoryService.requestVaccineUpdate(vaccineUpdateBean);
		
		return patientImmunizationInformation;
	}
	
}
