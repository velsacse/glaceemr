package com.glenwood.glaceemr.server.application.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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



@RestController
@RequestMapping("/user/PortalSettings")
public class PortalSettingsController {
	
	@Autowired
	PortalSettingsService portalSettingsService;
	
	@Autowired
	EmployeeService employeeService;
	
	Logger logger=LoggerFactory.getLogger(PortalSettingsController.class);
	
	/**
	 * @return List of patient profile settings fields with available options.
	 * @throws Exception
	 */
	@RequestMapping(value = "/ProfileSettingsFields", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getPatientProfileSettingsFieldsOprions(){
		
		EMRResponseBean responseBean=new EMRResponseBean();
		
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
	@ResponseBody
	public EMRResponseBean getPortalBillingConfigFields(){

		EMRResponseBean responseBean=new EMRResponseBean();
		
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
	@ResponseBody
	public EMRResponseBean getEmployeesList(@RequestParam(value="groupId", required=false, defaultValue="") String groupId,
			 @RequestParam(value="sort", required=false, defaultValue="") String sort){

		EMRResponseBean responseBean=new EMRResponseBean();
		
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
	@ResponseBody
	public EMRResponseBean getActivePosList(){

		EMRResponseBean responseBean=new EMRResponseBean();
		
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
	@ResponseBody
	public EMRResponseBean getProvidersList(){

		EMRResponseBean responseBean=new EMRResponseBean();
		
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
	@ResponseBody
	public  EMRResponseBean saveDemographicChanges(@RequestBody PatientRegistrationBean regDetailsBean) throws Exception{

		EMRResponseBean responseBean=new EMRResponseBean();
		
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
	@ResponseBody
	public EMRResponseBean getActiveSessionForOldEMR(@RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@RequestParam(value="chartId", required=false, defaultValue="") int chartId) throws Exception{

		EMRResponseBean responseBean=new EMRResponseBean();
		
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
	@ResponseBody
	public EMRResponseBean getPortalConfigDetails(String JSESSIONID, @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@RequestParam(value="chartId", required=false, defaultValue="") int chartId) throws Exception{

		EMRResponseBean responseBean=new EMRResponseBean();
		
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
	@ResponseBody
	public EMRResponseBean getInsuranceList(@RequestBody InsuranceFilterBean insFilterBean)throws Exception{

		EMRResponseBean responseBean=new EMRResponseBean();
		
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
