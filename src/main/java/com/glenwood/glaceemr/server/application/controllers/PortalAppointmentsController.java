package com.glenwood.glaceemr.server.application.controllers;

import java.util.Date;
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

import com.glenwood.glaceemr.server.application.models.ApptRequestBean;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointment;
import com.glenwood.glaceemr.server.application.models.SchedulerApptBookingBean;
import com.glenwood.glaceemr.server.application.services.portal.portalAppointments.PortalAppointmentsService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


@RestController
@Transactional
@RequestMapping("/user/PortalAppointments")
@Api(value="PortalAppointmentsController", description="Can be used to request, cancel, get list of appointments by the patient for doctor.")
public class PortalAppointmentsController {

	@Autowired
	PortalAppointmentsService portalAppointmentsService;
	
	@Autowired
	EMRResponseBean responseBean;
	
	Logger logger=LoggerFactory.getLogger(PortalAppointmentsController.class);
	
	
	/**
	 * Appointment list of a patient appointments.
	 * @param patientId		: id of the required patient.
	 * @param appointmentsType		    : indicates future or past or present day appointment .
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/AppointmentsList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's appointments list", notes = "Returns a complete list of appointments.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's appointments list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getPatientAppointmentsList(@ApiParam(name="patientId", value="patient's id whose appointments list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="0") int patientId,
			@ApiParam(name="appointmentsType", value="type of appointment (Future, Past, Present)") @RequestParam(value="appointmentsType", required=false, defaultValue="present") String appointmentsType,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			List<SchedulerAppointment> patientAppointmentsList;
			if(appointmentsType.equalsIgnoreCase("all"))
				patientAppointmentsList=portalAppointmentsService.getPatientTotalAppointmentsList(patientId, pageOffset, pageIndex);
			else if(appointmentsType.equalsIgnoreCase("future"))
				 patientAppointmentsList=portalAppointmentsService.getPatientFutureAppointmentsList(patientId, pageOffset, pageIndex);
			else if(appointmentsType.equalsIgnoreCase("past"))
				patientAppointmentsList=portalAppointmentsService.getPatientPastAppointmentsList(patientId, pageOffset, pageIndex);
			else 
				patientAppointmentsList=portalAppointmentsService.getPatientTodaysAppointmentsList(patientId, pageOffset, pageIndex);
			responseBean.setSuccess(true);
			responseBean.setData(patientAppointmentsList);
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving patient appointments list!");
			return responseBean;
		}
	}
	
	/**
	 * Appointment list of a patient appointments.
	 * @param patientId		: id of the required patient.
	 * @param appointmentsType		    : indicates future or past or present day appointment .
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/PatientAppointments", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's appointments list", notes = "Returns a complete list of appointments.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's appointments list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getPatientAppointments(@ApiParam(name="patientId", value="patient's id whose appointments list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="0") int patientId,
			@ApiParam(name="appointmentsType", value="type of appointment (Future, Past, Present)") @RequestParam(value="appointmentsType", required=false, defaultValue="present") String appointmentsType,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalAppointmentsService.getPatientAppointments(patientId, pageOffset, pageIndex, appointmentsType));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving patient appointments list!");
			return responseBean;
		}
			
	}
	
	/**
	 * Appointment Requests list of a patient.
	 * @param patientId		: id of the required patient.
	 * @return List of Appointments Requests of a patient.
	 */
	@RequestMapping(value = "/AppointmentRequestsList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns  list of Appointments Requests of a patient", notes = "Returns  list of Appointments Requests of a patient", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's appointment requests list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getPatientAppointmentRequestsList(@ApiParam(name="patientId", value="patient's id whose appointment requests list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="0") int patientId,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalAppointmentsService.getPortalApptRequestList(patientId, pageOffset, pageIndex));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving patient appointment requests list!");
			return responseBean;
		}
		
	}
	
	/**
	 * Appointment list of a patient appointments.
	 * @param patientId		: id of the required patient.
	 * @param appointmentsType		    : indicates future or past or present day appointment .
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/AppointmentDetails", method = RequestMethod.GET)
    @ApiOperation(value = "Returns appointment details bean", notes = "Returns a complete bean of appointment details .", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's appointments list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getAppointmentDetails() throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalAppointmentsService.getAppointmentDetails());
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving appointment details!");
			return responseBean;
		}
		
	}
	
	
	/**
	 * Booked Slots List by resourceId and apptDate
	 * @return List of Booked Slots
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/BookedSlots", method = RequestMethod.GET)
    @ApiOperation(value = "List of booked slots", notes = "List of booked slots", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of List of booked slots"),
		    @ApiResponse(code = 404, message = "No locations exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getBookedSlotsList(@ApiParam(name="resourceId", value="") @RequestParam(value="resourceId", required=false, defaultValue="0") int resourceId,
			@ApiParam(name="apptDate", value="") @RequestParam(value="apptDate", required=false, defaultValue="") String apptDate) throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalAppointmentsService.getBookedSlots(resourceId, new Date(apptDate)));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving provider's booked slots list1");
			return responseBean;
		}
	}
	
	
	/**
	 * Locked Slots List by resourceId and apptDate
	 * @return List of Locked Slots
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/LockedSlots", method = RequestMethod.GET)
    @ApiOperation(value = "List of Locked Slots", notes = "List of Locked Slots", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of List of Locked Slots"),
		    @ApiResponse(code = 404, message = "No locations exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getLockedSlotsList(@ApiParam(name="resourceId", value="") @RequestParam(value="resourceId", required=false, defaultValue="0") int resourceId,
			@ApiParam(name="apptDate", value="") @RequestParam(value="apptDate", required=false, defaultValue="") String apptDate) throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalAppointmentsService.getLockedSlots(resourceId, new Date(apptDate)));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving provider's locked slots list!");
			return responseBean;
		}
	}
	
	/**
	 * Appointment Booking Locations.
	 * @return List of Appointment Booking Locations
	 */
	@RequestMapping(value = "/AppointmentBookLocationsList", method = RequestMethod.GET)
    @ApiOperation(value = "List of Appointment Booking Locations", notes = "Returns List of Appointment Booking Locations.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of List of Appointment Booking Locations"),
		    @ApiResponse(code = 404, message = "No locations exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getAppointmentBookLocationsList() throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalAppointmentsService.getApptBookLocationList());
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving appointment booking locations list!");
			return responseBean;
		}
	}
	

	/**
	 * Appointment Booking Locations.
	 * @return List of Appointment Booking Categories
	 */
	@RequestMapping(value = "/AppointmentBookCategoriesList", method = RequestMethod.GET)
    @ApiOperation(value = "List of Appointment Booking Categories", notes = "Returns List of Appointment Booking Categories.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of List of Appointment Booking Categories"),
		    @ApiResponse(code = 404, message = "No locations exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getAppointmentBookCategoriesList() throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalAppointmentsService.getSchResourceCategoriesList());
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving appointment booking categories list!");
			return responseBean;
		}
	}
	
	
	
	/**
	 * Appointment Booking Doctors.
	 * @return List of Appointment Booking Doctors
	 */
	@RequestMapping(value = "/AppointmentBookDoctorsList", method = RequestMethod.GET)
    @ApiOperation(value = "List of Appointment Booking Locations", notes = "Returns List of Appointment Booking Doctors.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of List of Appointment Booking Doctors"),
		    @ApiResponse(code = 404, message = "No locations exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getApptBookDoctorsList(@ApiParam(name="posId", value="place of service id form where we want the doctors List") @RequestParam(value="posId", required=false, defaultValue="0") int posId) throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalAppointmentsService.getApptBookDoctorsList(posId));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving appointment providers list!");
			return responseBean;
		}
		
	}
	
	
	/**
	 * Free Slots for Appointment Booking
	 * @param providerId  :
	 * @param apptDate  :
	 * @return List of Free Slots
	 */
	@RequestMapping(value = "/Appointment/FreeSlots", method = RequestMethod.GET)
    @ApiOperation(value = "List of Appointment Booking Locations", notes = "Returns List of Appointment Booking Locations.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of List of free slots"),
		    @ApiResponse(code = 404, message = "Not Found"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getApptFreeSlotsByProviderIdAndDate(@ApiParam(name="providerId", value="") @RequestParam(value="providerId", required=false, defaultValue="0") int providerId,
			@ApiParam(name="apptDate", value="") @RequestParam(value="apptDate", required=false, defaultValue="") String apptDate){

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalAppointmentsService.getApptFreeSlotsByProviderIdAndDate(providerId, new Date(apptDate)));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving provider's free slots list!");
			return responseBean;
		}
		
	}
	
	
	/**
	 * @param patientId : credit card payment details bean 
	 * @return Transaction summary details bean
	 * @throws Exception
	 */
	@RequestMapping(value = "/Appointment/AppointmentRequest", method = RequestMethod.POST)
    @ApiOperation(value = "Returns appointment request alert event details", notes = "Returns appointment request alert event details", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful creation alert for appointment request"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public  EMRResponseBean createPortalAppointmentRequestAlert(@RequestBody ApptRequestBean apptRequestAlertEventBean) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
			
		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalAppointmentsService.createPortalAppointmentRequest(apptRequestAlertEventBean));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in requesting an appointment!");
			return responseBean;
		}
		
	}
	
	/**
	 * @return Booked Appointment Details bean
	 * @throws Exception
	 */
	@RequestMapping(value = "/Appointment/BookAppointment", method = RequestMethod.POST)
    @ApiOperation(value = "Returns Booked Appointment Details", notes = "Returns Booked Appointment Details", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Appointment Booking Successful"),
		    @ApiResponse(code = 404, message = "Appointment Booking Failure"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public  EMRResponseBean bookAppointment(@RequestBody SchedulerApptBookingBean schedulerApptBookingBean) throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalAppointmentsService.bookAppointment(schedulerApptBookingBean));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in booking an appointment!");
			return responseBean;
		}
		
	}

	
}
