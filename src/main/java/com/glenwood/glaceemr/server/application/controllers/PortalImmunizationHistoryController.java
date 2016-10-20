package com.glenwood.glaceemr.server.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.VaccineUpdateBean;
import com.glenwood.glaceemr.server.application.services.portal.portalImmunizationHistory.PortalImmunizationHistoryService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Transactional
@RequestMapping("/user/PortalImmunizationHistory")
@Api(value = "PortalImmunizationHistoryController", description = "Used to get immunization history of a patient")
public class PortalImmunizationHistoryController {

	@Autowired
	PortalImmunizationHistoryService portalImmunizationHistoryService;

	@Autowired
	EMRResponseBean responseBean;

	/**
	 * Appointment list of a patient appointments.
	 * 
	 * @param patientId
	 *            : id of the required patient.
	 * @param appointmentsType
	 *            : indicates future or past or present day appointment .
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/ImmunizationHistory", method = RequestMethod.GET)
	@ApiOperation(value = "Returns patient's appointments list", notes = "Returns a complete list of appointments.", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful retrieval of patient's appointments list"),
			@ApiResponse(code = 404, message = "Patient with given id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error") })
	@ResponseBody
	public EMRResponseBean getImmunizationHistory(
			@ApiParam(name = "patientId", value = "patient's id whose appointments list is to be retrieved") @RequestParam(value = "patientId", required = false, defaultValue = "0") int patientId,
			@ApiParam(name = "chartId", value = "type of appointment (Future, Past, Present)") @RequestParam(value = "chartId", required = false, defaultValue = "present") int chartId,
			@ApiParam(name = "pageOffset", value = "offset of the page") @RequestParam(value = "pageOffset", required = false, defaultValue = "5") int pageOffset,
			@ApiParam(name = "pageIndex", value = "index of the page") @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex)
			throws Exception {

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalImmunizationHistoryService
					.getPatientImmunizationHistory(patientId, chartId,
							pageOffset, pageIndex));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean
					.setData("Error in retrieving patient's immunization history!");
			return responseBean;
		}

	}

	/**
	 * Appointment list of a vaccine vsi files
	 * 
	 * @param patientId
	 *            : lab description cvx
	 * @return List of Appointments of a patient.
	 */
	@RequestMapping(value = "/VISFilesList", method = RequestMethod.GET)
	@ApiOperation(value = "Returns patient's appointments list", notes = "Returns a complete list of appointments.", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful retrieval of lab VIS files"),
			@ApiResponse(code = 404, message = "Patient with given id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error") })
	@ResponseBody
	public EMRResponseBean getVaccineVISFilesList(
			@ApiParam(name = "labDescCVX", value = "lab cvx for which VIS files need to be retrieved") @RequestParam(value = "labDescCVX", required = false, defaultValue = "0") String labDescCVX)
			throws Exception {

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalImmunizationHistoryService
					.getVaccineVISFilesList(labDescCVX));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving vaccine VIS files list!");
			return responseBean;
		}

	}

	/**
	 * Vaccine List
	 * 
	 * @param searchKey
	 *            : key to search
	 * @param pageOffset
	 *            : offset of the page.
	 * @param pageIndex
	 *            : index of the page.
	 * @return Vaccines list
	 */
	@RequestMapping(value = "/VaccineList", method = RequestMethod.GET)
	@ApiOperation(value = "Returns vaccines list", notes = "Returns vaccines list.", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful retrieval of vaccines list"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	@ResponseBody
	public EMRResponseBean getVaccineList(
			@ApiParam(name = "searchKey", value = "type of appointment (Future, Past, Present)") @RequestParam(value = "searchKey", required = false, defaultValue = "present") String searchKey)
			throws Exception {

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalImmunizationHistoryService
					.getVaccineList(searchKey));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving vaccines list!");
			return responseBean;
		}

	}

	/**
	 * Vaccine Update Reasons list of a patient appointments.
	 * 
	 * @return reasons list.
	 */
	@RequestMapping(value = "/VaccineUpdateReasonsList", method = RequestMethod.GET)
	@ApiOperation(value = "Returns vaccine update reasons list", notes = "Returns vaccine update reasons list", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful retrieval of vaccine update reaons list"),
			@ApiResponse(code = 404, message = "No Found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	@ResponseBody
	public EMRResponseBean getVaccUpdateReasonList() throws Exception {

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalImmunizationHistoryService
					.getVaccUpdateReasonList());
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean
					.setData("Error in retrieving vaccine update reasons list!");
			return responseBean;
		}

	}

	/**
	 * @return Update vaccine info
	 * @throws Exception
	 */
	@RequestMapping(value = "/VaccineUpdate", method = RequestMethod.POST)
	@ApiOperation(value = "Returns patient vaccine information", notes = "Returns patient vaccine information", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Vaccine information updated successfully."),
			@ApiResponse(code = 404, message = "Vaccine updation failure"),
			@ApiResponse(code = 500, message = "Internal server error") })
	@ResponseBody
	public EMRResponseBean bookAppointment(
			@RequestBody VaccineUpdateBean vaccineUpdateBean) throws Exception {

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalImmunizationHistoryService
					.requestVaccineUpdate(vaccineUpdateBean));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in updating vaccine!");
			return responseBean;
		}
	}

}
