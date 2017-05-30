package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.CarePlanConcern;
import com.glenwood.glaceemr.server.application.models.CarePlanGoal;
import com.glenwood.glaceemr.server.application.models.CarePlanIntervention;
import com.glenwood.glaceemr.server.application.models.CarePlanOutcome;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailSaveService;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogActionType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogModuleType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogUserType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.Log_Outcome;
import com.glenwood.glaceemr.server.application.services.chart.careplan.CarePlanConcernBean;
import com.glenwood.glaceemr.server.application.services.chart.careplan.CarePlanGoalBean;
import com.glenwood.glaceemr.server.application.services.chart.careplan.CarePlanOutcomeBean;
import com.glenwood.glaceemr.server.application.services.chart.careplan.CarePlanService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;

@RestController
@Transactional
@RequestMapping(value="/user/CarePlan")
public class CarePlanController {
	
	@Autowired
	CarePlanService carePlanService;
	
	@Autowired
	AuditTrailSaveService auditTrailSaveService;
	
	@Autowired
	HttpServletRequest request;
	
	/**
	 * To get the list of concerns which belongs to the given patientId and encounterId
	 * @param patientId
	 * @param encounterId
	 * @return CarePlanGoal
	 * @throws Exception
	 */
	@RequestMapping(value ="/GetCarePlanInitialData", method = RequestMethod.POST) 
	@ResponseBody
	public EMRResponseBean getCarePlanIntialData(@RequestParam(value="patientId",required=false,defaultValue="-1")Integer patientId,@RequestParam(value="encounterId",required=false,defaultValue="-1")Integer encounterId)throws Exception{
		System.out.println("entered in care plan request");
		Map<String, Object> lists=carePlanService.getCarePlanInitialData(patientId,encounterId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(lists);
	//	auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.CAREPLAN, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved list of concerns which belongs to the given PatientId", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
	
	/**
	 * To get the list of goals which belongs to the given patientId, encounterId, concernId and goalId
	 * @param goalId
	 * @param concernId
	 * @param patientId
	 * @param encounterId
	 * @return List
	 * @throws Exception
	 */
	@RequestMapping(value ="/GetCarePlanGoals", method = RequestMethod.GET) 
	@ResponseBody
	public List<CarePlanGoal> getCarePlanGoals(@RequestParam(value="goalId",required=false,defaultValue="-1")Integer goalId,@RequestParam(value="concernId",required=false,defaultValue="-1")Integer concernId,@RequestParam(value="patientId",required=false,defaultValue="-1")Integer patientId,@RequestParam(value="encounterId",required=false,defaultValue="-1")Integer encounterId)throws Exception{
		List<CarePlanGoal> listOfGoals=carePlanService.fetchCarePlanGoals(goalId,concernId,patientId,encounterId);
	//	auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.CAREPLAN, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved list of goals which belongs to the given PatientId", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return listOfGoals;
	}
	
	/**
	 * To get the list of outcomes which belongs to the given outcomeId, goalId, patientId and encounterId
	 * @param patientId
	 * @return List
	 * @throws Exception
	 */
	@RequestMapping(value ="/GetCarePlanOutcomes", method = RequestMethod.POST) 
	@ResponseBody
	public List<CarePlanOutcomeBean> getCarePlanOutcomes(@RequestParam(value="outcomeId",required=false,defaultValue="-1")Integer outcomeId,@RequestParam(value="goalId",required=false,defaultValue="-1")Integer goalId,@RequestParam(value="patientId",required=false,defaultValue="-1")Integer patientId,@RequestParam(value="encounterId",required=false,defaultValue="-1")Integer encounterId)throws Exception{
		List<CarePlanOutcomeBean> listOfGoals=carePlanService.fetchCarePlanOutcomes(outcomeId,goalId,patientId,encounterId);
		//	auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.CAREPLAN, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved list of goals which belongs to the given PatientId", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return listOfGoals;
	}
	
	/**
	 * To save given care plan concern
	 * @param carePlanConcernBean
	 * @return EMRResponseBean
	 * @throws Exception
	 */
	@RequestMapping(value="/SaveCarePlanConcern", method = RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean saveCarePlanConcern(@RequestBody CarePlanConcernBean carePlanConcernBean){
		List<CarePlanConcern> carePlanConcerns = carePlanService.saveCarePlanConcern(carePlanConcernBean);
		EMRResponseBean bean= new EMRResponseBean();
		bean.setData(carePlanConcerns);
		return bean;		
	}
	
	/**
	 * To save given care plan goal
	 * @param carePlanGoalBean
	 * @return EMRResponseBean
	 * @throws Exception
	 */
	@RequestMapping(value="/SaveCarePlanGoal", method = RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean saveCarePlanGoal(@RequestBody CarePlanGoalBean carePlanGoalBean){
		List<CarePlanGoalBean> carePlanGoals = carePlanService.saveCarePlanGoal(carePlanGoalBean);
		EMRResponseBean bean= new EMRResponseBean();
		bean.setData(carePlanGoals);
		return bean;		
	}
	
	/**
	 * To save given care plan outcome
	 * @param goalId
	 * @param providerId
	 * @param patientId
	 * @param encounterId
	 * @param outcomeProgress
	 * @param outcomeReview
	 * @param outcomeTarget
	 * @param outcomeNotes
	 * @param outcomeStatus
	 * @return EMRResponseBean
	 */
	@RequestMapping(value ="/SaveCarePlanOutcome", method = RequestMethod.POST) 
	@ResponseBody
	
	public EMRResponseBean SaveCarePlanOutcomes(@RequestParam(value="goalId",required=false,defaultValue="-1")Integer goalId,@RequestParam(value="providerId",required=false,defaultValue="-1")Integer providerId,@RequestParam(value="patientId",required=true,defaultValue="-1")Integer patientId,@RequestParam(value="encounterId",required=false,defaultValue="-1")Integer encounterId,@RequestParam(value="outcomeProgress",required=false,defaultValue="-1")Integer outcomeProgress,@RequestParam(value="outcomeReview",required=false,defaultValue="-1")String outcomeReview,@RequestParam(value="outcomeTarget",required=false,defaultValue="-1")String outcomeTarget,@RequestParam(value="outcomeNotes",required=false,defaultValue="-1")String outcomeNotes,@RequestParam(value="outcomeStatus",required=false,defaultValue="-1")Integer outcomeStatus){
		System.out.println("entered in save outcomes");
		List<CarePlanGoalBean> carePlanGoals = carePlanService.saveCarePlanOutcomes(goalId,providerId,patientId,encounterId,outcomeProgress,outcomeReview,outcomeTarget,outcomeNotes,outcomeStatus);
		EMRResponseBean bean= new EMRResponseBean();
		bean.setData(carePlanGoals);
		//auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.CAREPLAN, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved list of goals which belongs to the given PatientId", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return bean;
	}
	
	
	
	/**
	 * To get the list of vitals which belongs to the given encounterId
	 * @param patientId
	 * @param encounterId
	 * @return EMRResponseBean
	 * @throws Exception
	 */
	@RequestMapping(value ="/GetVitals", method = RequestMethod.GET) 
	@ResponseBody
	public EMRResponseBean getVitals(@RequestParam(value="patientId",required=true,defaultValue="-1")Integer patientId,@RequestParam(value="encounterId",required=false,defaultValue="-1")Integer encounterId){
		System.out.println("entered in save outcomes");
		String carePlanVitals = carePlanService.getVitals(patientId,encounterId);
		EMRResponseBean bean= new EMRResponseBean();
		bean.setData(carePlanVitals);
		//	auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.CAREPLAN, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved list of goals which belongs to the given PatientId", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return bean;
	}
	

	/**
	 * To import shortcuts based on given shortcut IDs,patientId
	 * @param patientId
	 * @param encounterId
	 * @param shortcutIDs
	 * @param providerId
	 * @return EMRResponseBean
	 * @throws Exception
	 */
	@RequestMapping(value ="/ImportCarePlanGoal", method = RequestMethod.POST) 
	@ResponseBody
	public EMRResponseBean importCarePlanGoal(@RequestParam(value="patientId",required=false,defaultValue="-1")Integer patientId,@RequestParam(value="encounterId",required=false,defaultValue="-1")Integer encounterId,@RequestParam(value="shortcutIDs",required=false,defaultValue="")String shortcutIDs,@RequestParam(value="providerId",required=false,defaultValue="-1")Integer providerId)throws Exception{
		System.out.println("entered in care plan shorcut request");
		Map<String, Object> lists=carePlanService.importCarePlanShortcuts(patientId,encounterId,shortcutIDs,providerId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(lists);
		//auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.CAREPLAN, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved list of concerns which belongs to the given PatientId", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
}