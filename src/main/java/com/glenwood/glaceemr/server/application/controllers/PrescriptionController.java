package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.MedsAdminLog;
import com.glenwood.glaceemr.server.application.services.chart.prescription.PrescriptionService;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;

/**
 * Controller for Prescriptions in GlaceEMR, 
 * contains the methods to get the patient medications and performing different operations on those. 
 * 
 * @author software
 *
 */
@Api(value = "Prescriptions", description = "contains the methods to get the patient medications and performing different operations on those", consumes="application/json")
@RestController
@RequestMapping(value = "/Prescription.Action")
public class PrescriptionController {
	
	@Autowired
	PrescriptionService prescriptionService;
	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	HttpServletRequest request;
	
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
		Map<String, Object> drugs=prescriptionService.getactivemedwithclass(patientid);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(drugs);
		return emrResponseBean;
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
	
	public Map<String, Object> getOneMonthActiveMeds(@RequestParam(value="patientid",required=false,defaultValue="-1")Integer patientid,
			@RequestParam(value="month",required=false,defaultValue="-1")Integer whichMonth,
			@RequestParam(value="year",required=false,defaultValue="-1")Integer year)throws Exception{
		Map<String, Object> drugs=prescriptionService.getOneMonthActiveMeds(patientid,whichMonth,year);
		return drugs;
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
	
	public List getMedAdministrationLogHistory(@RequestParam(value="planId",required=false,defaultValue="-1")Integer planId)throws Exception{
		@SuppressWarnings("unchecked")
		List<MedsAdminLog> logDetails = prescriptionService.getMedicationAdminLogHistory(planId);
		return logDetails;
	}

	/**
	 * To get the Medication administered log 
	 * @param dataToSave
	 * @throws Exception
	 */
	@RequestMapping(value ="/getMedAdminLog", method = RequestMethod.GET) 
	@ResponseBody
	
	public List getMedAdminLog(@RequestParam(value="logId",required=false,defaultValue="-1")Integer logId)throws Exception{
		@SuppressWarnings("unchecked")
		List<MedsAdminLog> logDetails = prescriptionService.getMedicationAdminLog(logId);
		return logDetails;
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
}
