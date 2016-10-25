package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.DrugSchedule;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.MedsAdminLog;
import com.glenwood.glaceemr.server.application.models.PharmacyFilterBean;
import com.glenwood.glaceemr.server.application.models.PortalRefillRequestBean;
import com.glenwood.glaceemr.server.application.models.Prescription;
import com.glenwood.glaceemr.server.application.services.chart.prescription.IntakeBean;
import com.glenwood.glaceemr.server.application.services.chart.prescription.PrescriptionBean;
import com.glenwood.glaceemr.server.application.models.MedsAdminPlanShortcut;
import com.glenwood.glaceemr.server.application.services.chart.prescription.PrescriptionService;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Controller for Prescriptions in GlaceEMR, 
 * contains the methods to get the patient medications and performing different operations on those. 
 * 
 * @author software
 *
 */
@Api(value = "Prescriptions", description = "contains the methods to get the patient medications and performing different operations on those", consumes="application/json")
@RestController
@RequestMapping(value = "/user/Prescription.Action")
public class PrescriptionController {
	
	@Autowired
	PrescriptionService prescriptionService;
	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	EMRResponseBean responseBean;
	
	private Logger logger = Logger.getLogger(PrescriptionController.class);
	
	/**
	 * To get the list of active medications with class details
	 * @param patientid
	 * @return the list of active medications of the given patient
	 * @throws Exception
	 */
	
	@RequestMapping(value ="/Bypatientid", method = RequestMethod.GET) 
	@ResponseBody
	
	public EMRResponseBean getactivemedwithclass(@RequestParam(value="patientid",required=false,defaultValue="-1")Integer patientid)throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			Map<String, Object> drugs=prescriptionService.getactivemedwithclass(patientid);
			responseBean.setSuccess(true);
			responseBean.setData(drugs);
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving active meds!");
			return responseBean;
		}		
	}
	
	/**
	 * To get the list of active medications with class details 
	 * @param patientid
	 * @param whichMonth
	 * @param year
	 * @return the list of medications for a particular month
	 * @throws Exception
	 */
		
	@RequestMapping(value ="/ByMonth", method = RequestMethod.GET) 
	@ResponseBody
	
	public EMRResponseBean getOneMonthActiveMeds(@RequestParam(value="patientid",required=false,defaultValue="-1")Integer patientid,
			@RequestParam(value="month",required=false,defaultValue="-1")Integer whichMonth,
			@RequestParam(value="year",required=false,defaultValue="-1")Integer year)throws Exception{
		Map<String, Object> drugs=prescriptionService.getOneMonthActiveMeds(patientid,whichMonth,year);
		EMRResponseBean activeMedsByMonth = new EMRResponseBean();
		activeMedsByMonth.setData(drugs);
		return activeMedsByMonth;
	}
	
	/**
	 * To save the medication's new administration plan 
	 * @param dataToSave
	 * @throws Exception
	 */
	@RequestMapping(value ="/saveMedPlan", method = RequestMethod.GET) 
	@ResponseBody
	
	public void savemedAdministrationNewPlan(@RequestParam(value="dataToSave",required=false,defaultValue="-1")String dataToSave)throws Exception{
		logger.debug("In saveMedPlan - new plan is going to save");
		prescriptionService.saveMedicationAdminPlan(dataToSave);
		logger.debug("In saveMedPlan - saved");
	}
	
	/**
	 * To save the medication's administered log 
	 * @param dataToSave
	 * @throws Exception
	 */
	@RequestMapping(value ="/saveMedAdminLog", method = RequestMethod.GET) 
	@ResponseBody
	
	public void savemedAdministrationLog(@RequestParam(value="dataToSave",required=false,defaultValue="-1")String dataToSave)throws Exception{
		logger.debug("In saveMedAdminLog - administration log for a plan is going to save");
		prescriptionService.saveMedicationAdminLog(dataToSave);
		logger.debug("In saveMedAdminLog - administration log for a plan got saved");
	}
	
	/**
	 * To get the Medication full with previous administered log 
	 * @param dataToSave
	 * @throws Exception
	 */
	@RequestMapping(value ="/getMedAdminLogHistory", method = RequestMethod.GET) 
	@ResponseBody
	
	public EMRResponseBean getMedAdministrationLogHistory(@RequestParam(value="planId",required=false,defaultValue="-1")Integer planId)throws Exception{
		@SuppressWarnings("unchecked")
		List<MedsAdminLog> logDetails = prescriptionService.getMedicationAdminLogHistory(planId);
		EMRResponseBean logHistory = new EMRResponseBean();
		logHistory.setData(logDetails);
		return logHistory;
	}

	/**
	 * To get the Medication administered log 
	 * @param dataToSave
	 * @throws Exception
	 */
	@RequestMapping(value ="/getMedAdminLog", method = RequestMethod.GET) 
	@ResponseBody
	
	public EMRResponseBean getMedAdminLog(@RequestParam(value="logId",required=false,defaultValue="-1")Integer logId)throws Exception{
		@SuppressWarnings("unchecked")
		List<MedsAdminLog> logDetails = prescriptionService.getMedicationAdminLog(logId);
		EMRResponseBean logData = new EMRResponseBean();
		logData.setData(logDetails);
		return logData;
	}
	
	/**
	 * To delete the medication administered log 
	 * @param dataToSave
	 * @throws Exception
	 */
	@RequestMapping(value ="/deleteMedAdminLog", method = RequestMethod.GET) 
	@ResponseBody
	
	public void deletemedAdministrationLog(@RequestParam(value="deletedBy",required=false,defaultValue="-1")Integer deletedBy,
			@RequestParam(value="logId",required=false,defaultValue="-1")Integer logId)throws Exception{
		logger.debug("In deleteMedAdminLog - "+logId+"is going to be deleted");
		prescriptionService.deleteMedicationAdminLog(deletedBy,logId);
		logger.debug("In deleteMedAdminLog - "+logId+"got deleted");
	}

	/**
	 * To edit the medication's administration plan 
	 * @param dataToSave
	 * @throws Exception
	 */
	@RequestMapping(value ="/editDeleteMedAdminPlan", method = RequestMethod.GET) 
	@ResponseBody
	public void editmedAdministrationPlan(@RequestParam(value="dataToSave",required=false,defaultValue="-1")String dataToSave)throws Exception{
		logger.debug("In editDeleteMedAdminPlan - plan is going to be edited");
		prescriptionService.editMedicationAdminPlan(dataToSave);
		logger.debug("In editDeleteMedAdminPlan - saved");
	}
	
	
	/*
	 *To get the medical supplies details for the selected patient
	 *@param patientid
	 *@throws Exception
	 */
	@RequestMapping(value ="/patientid", method = RequestMethod.GET) 
	@ResponseBody
	
	public EMRResponseBean getactivemedicalsupplies(@RequestParam(value="patientid",required=false,defaultValue="-1")Integer patientid)throws Exception{
		logger.debug("In medical supplies");
		PrescriptionBean drugs=prescriptionService.getactivemedicalsupplies(patientid);
		logger.debug("Got the medical supplies data");
		EMRResponseBean activeMedSupplies = new EMRResponseBean();
		activeMedSupplies.setData(drugs);
		return activeMedSupplies;
	}
	
	/*To get the take and frequency list based on selected medications
	 * 
	 */
		@RequestMapping(value="/getTakeAndFrequencyList", method = RequestMethod.GET)
		@ResponseBody
		public EMRResponseBean getTakeAndFrequencyList(@RequestParam(value="brandname",required=false,defaultValue="-1")String brandname,@RequestParam(value="mode",required=false,defaultValue="-1")String mode)throws Exception{

			logger.debug("In getting frequency list");
			List<DrugSchedule> schedulename=prescriptionService.getfrequencylist(brandname.replaceAll("'","''"),mode);
			EMRResponseBean frequencyList = new EMRResponseBean();
			frequencyList.setData(schedulename);
			return frequencyList;

		}

		
		/*To get the all the frequency list based on selected medications
		 * 
		 */
		@RequestMapping(value="/getFrequencyListall", method = RequestMethod.GET)
		@ResponseBody
		public EMRResponseBean getFrequencyListall(@RequestParam(value="brandname",required=false,defaultValue="-1")String brandname,@RequestParam(value="mode",required=false,defaultValue="-1")String mode)throws Exception{
			logger.debug("In getting all  frequency list");
			List<DrugSchedule> schedulename=prescriptionService.getfrequencylistall(brandname.replaceAll("'","''"),mode);
			EMRResponseBean frequencyList = new EMRResponseBean();
			frequencyList.setData(schedulename);
			return frequencyList;

		}

		/*To get the take list based on selected medications
		 * 
		 */
		@RequestMapping(value="/gettake", method = RequestMethod.GET)
		@ResponseBody
		public EMRResponseBean gettake(@RequestParam(value="brandname",required=false,defaultValue="-1")String brandname)throws Exception{
			logger.debug("In getting t list");
			List<IntakeBean> takevalues=prescriptionService.gettake(brandname.replaceAll("'","''"));
			EMRResponseBean intakeData = new EMRResponseBean();
			intakeData.setData(takevalues);
			return intakeData;

		}
	
	/**
	 * To modify the medication administered log notes 
	 * @param dataToSave
	 * @throws Exception
	 */
	@RequestMapping(value ="/updateMedAdminLogNotes", method = RequestMethod.GET) 
	@ResponseBody
	public void updateMedAdministrationLogNotes(@RequestParam(value="modifiedBy",required=false,defaultValue="-1")Integer modifiedBy,
			@RequestParam(value="logId",required=false,defaultValue="-1")Integer logId,
			@RequestParam(value="notes",required=false,defaultValue="")String notes)throws Exception{
		logger.debug("In updateMedAdministrationLogNotes - "+logId+"is going to be modified");
		prescriptionService.updateMedicationAdminLogNotes(modifiedBy,logId,notes);
		logger.debug("In updateMedAdministrationLogNotes - "+logId+"got modified");
	}
	
	/**
	 * To get the medication administration plan shortcuts 
	 * * @throws Exception
	 */
	@RequestMapping(value ="/getMedAdminPlanShortcuts", method = RequestMethod.GET) 
	@ResponseBody
	public EMRResponseBean getMedAdminPlanShortcuts()throws Exception{
		logger.debug("In getMedAdminPlanShortcuts");
		List<MedsAdminPlanShortcut> shortcutsList = prescriptionService.getMedAdminPlanShortcuts();
		EMRResponseBean medShortcuts = new EMRResponseBean();
		medShortcuts.setData(shortcutsList);
		return medShortcuts;
	}
	
	/**
	 * @return EMRResponseBean with pharmacy list
	 * @throws Exception
	 */
	@RequestMapping(value = "/PharmacyList", method = RequestMethod.POST)
    @ApiOperation(value = "Returns list of filtered pharmacies", notes = "Returns list of filtered pharmacies", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Pharmacy list retrieval successful"),
		    @ApiResponse(code = 404, message = "Pharmacy list retrieval failure"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getPharmacyList(@RequestBody PharmacyFilterBean pharmacyFilterBean)throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(prescriptionService.getPharmacyList(pharmacyFilterBean));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving pharmacy list!");
			return responseBean;
		}		
	}
	
	
	/**
	 * @param patientId 
	 * @param chartId
	 * @return EMRResponseBEan with completed prescription list
	 * @throws Exception
	 */
	@RequestMapping(value = "/RefillRequestHistory", method = RequestMethod.GET)
    @ApiOperation(value = "Returns list of completed medications", notes = "Returns list of filtered pharmacies", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Pharmacy list retrieval successful"),
		    @ApiResponse(code = 404, message = "Pharmacy list retrieval failure"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getPatientRefillRequestHistory(@ApiParam(name="patientId", value="patient's id whose refill request history is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="chartId", value="chart id of a patient, whose refill request history is to be retrieved") @RequestParam(value="chartId", required=false, defaultValue="") int chartId)throws Exception{
		
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(prescriptionService.getPatientRefillRequestHistory(patientId, chartId));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving refill request history!");
			return responseBean;
		}	
	}
	
	/**
	 * @param patientId 
	 * @param chartId
	 * @return EMRResponseBean with refill request medications
	 * @throws Exception
	 */
	@RequestMapping(value = "/RefillRequestMedications", method = RequestMethod.GET)
    @ApiOperation(value = "Returns list of completed medications", notes = "Returns list of filtered pharmacies", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Pharmacy list retrieval successful"),
		    @ApiResponse(code = 404, message = "Pharmacy list retrieval failure"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getPatientRefillRequestMedications(@ApiParam(name="patientId", value="patient's id whose completed medications list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="chartId", value="chart id of a patient, whose completed medications are to be retrieved") @RequestParam(value="chartId", required=false, defaultValue="") int chartId)throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(prescriptionService.getPatientRefillRequestMedications(patientId, chartId));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in retrieving refill request medications!");
			return responseBean;
		}	
	}
	
	/**
	 * @return EMRResponseBean with request status
	 * @throws Exception
	 */
	@RequestMapping(value = "/RefillRequest", method = RequestMethod.POST)
    @ApiOperation(value = "Returns list of filtered pharmacies", notes = "Returns list of filtered pharmacies", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Pharmacy list retrieval successful"),
		    @ApiResponse(code = 404, message = "Pharmacy list retrieval failure"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean requestFillFromPortal(@RequestBody PortalRefillRequestBean portalRefillRequestBean)throws Exception{
		
		return prescriptionService.requestRefill(portalRefillRequestBean);		
	}
}