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
import com.glenwood.glaceemr.server.application.services.chart.careplan.CarePlanInterventionBean;
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
	public EMRResponseBean getCarePlanIntialData(@RequestParam(value="patientId",required=false,defaultValue="-1")Integer patientId,@RequestParam(value="encounterId",required=false,defaultValue="-1")Integer encounterId,@RequestParam(value="episodeId",required=false,defaultValue="-1")Integer episodeId,@RequestParam(value="episodeTypeId",required=false,defaultValue="0")Integer episodeTypeId,@RequestParam(value="previousEpisodeId",required=false,defaultValue="-1")Integer previousEpisodeId)throws Exception{
		Map<String, Object> lists=carePlanService.getCarePlanInitialData(patientId,encounterId,episodeId,episodeTypeId,previousEpisodeId);
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
	public List<CarePlanOutcomeBean> getCarePlanOutcomes(@RequestParam(value="outcomeId",required=false,defaultValue="-1")Integer outcomeId,@RequestParam(value="goalId",required=false,defaultValue="-1")Integer goalId,@RequestParam(value="patientId",required=false,defaultValue="-1")Integer patientId,@RequestParam(value="encounterId",required=false,defaultValue="-1")Integer encounterId,@RequestParam(value="episodeId",required=false,defaultValue="-1")Integer episodeId)throws Exception{
		List<CarePlanOutcomeBean> listOfGoals=carePlanService.fetchCarePlanOutcomes(outcomeId,goalId,patientId,encounterId,episodeId);
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
	
	public EMRResponseBean SaveCarePlanOutcomes(@RequestParam(value="goalId",required=false,defaultValue="-1")Integer goalId,@RequestParam(value="providerId",required=false,defaultValue="-1")Integer providerId,@RequestParam(value="patientId",required=true,defaultValue="-1")Integer patientId,@RequestParam(value="encounterId",required=false,defaultValue="-1")Integer encounterId,@RequestParam(value="outcomeProgress",required=false,defaultValue="-1")Integer outcomeProgress,@RequestParam(value="outcomeReview",required=false,defaultValue="-1")String outcomeReview,@RequestParam(value="outcomeTarget",required=false,defaultValue="-1")String outcomeTarget,@RequestParam(value="outcomeNotes",required=false,defaultValue="")String outcomeNotes,@RequestParam(value="outcomeStatus",required=false,defaultValue="-1")Integer outcomeStatus,@RequestParam(value="episodeId",required=false,defaultValue="-1")Integer episodeId){
		System.out.println("entered in save outcomes");
		List<CarePlanGoalBean> carePlanGoals = carePlanService.saveCarePlanOutcomes(goalId,providerId,patientId,encounterId,outcomeProgress,outcomeReview,outcomeTarget,outcomeNotes,outcomeStatus,episodeId);
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
	public EMRResponseBean importCarePlanGoal(@RequestParam(value="patientId",required=false,defaultValue="-1")Integer patientId,@RequestParam(value="encounterId",required=false,defaultValue="-1")Integer encounterId,@RequestParam(value="shortcutIDs",required=false,defaultValue="")String shortcutIDs,@RequestParam(value="providerId",required=false,defaultValue="-1")Integer providerId,@RequestParam(value="episodeId",required=false,defaultValue="-1")Integer episodeId,@RequestParam(value="shortcutTerm",required=false,defaultValue="-1")Integer shortcutTerm,@RequestParam(value="categoryId",required=false,defaultValue="0")Integer categoryId,@RequestParam(value="previousEpisodeId",required=false,defaultValue="-1")Integer previousEpisodeId,@RequestParam(value="summaryMode",required=false,defaultValue="-1")Integer summaryMode)throws Exception{
		Map<String, Object> lists=carePlanService.importCarePlanShortcuts(patientId,encounterId,shortcutIDs,providerId,episodeId,shortcutTerm,categoryId,previousEpisodeId,summaryMode);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(lists);
		//auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.CAREPLAN, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved list of concerns which belongs to the given PatientId", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
	
	@RequestMapping(value ="/SaveProgressPlanNotes", method = RequestMethod.POST) 
	@ResponseBody
	public void saveProgressPlanNotes(@RequestParam(value="encounterId",required=true,defaultValue="-1") Integer encounterId,@RequestParam(value="planNotes",required=false,defaultValue="") String planText) {
		carePlanService.saveProgressPlanNotes(encounterId, planText);
	}
	
	@RequestMapping(value ="/ClearProgressPlanNotes", method = RequestMethod.GET) 
	@ResponseBody
	public void clearProgressPlanNotes(@RequestParam(value="encounterId",required=true,defaultValue="-1") Integer encounterId) {
		carePlanService.clearProgressPlanNotes(encounterId);
	}
	
	@RequestMapping(value ="/SaveProgressSubjectiveNotes", method = RequestMethod.POST) 
	@ResponseBody
	public void saveProgressSubjectiveNotes(@RequestParam(value="gwid",required=true,defaultValue="-1") String gwid,@RequestParam(value="chartId",required=true,defaultValue="-1") Integer chartId,@RequestParam(value="patientId",required=true,defaultValue="-1") Integer patientId,@RequestParam(value="encounterId",required=true,defaultValue="-1") Integer encounterId,@RequestParam(value="subjectiveNotes",required=false,defaultValue="") String subjectiveNotes) {
		carePlanService.saveProgressSubjectiveNotes(gwid,chartId,patientId,encounterId, subjectiveNotes);
	}
	
	@RequestMapping(value ="/ClearProgressSubjectiveNotes", method = RequestMethod.GET) 
	@ResponseBody
	public void clearProgressSubjectiveNotes(@RequestParam(value="gwid",required=true,defaultValue="-1") String gwid,@RequestParam(value="patientId",required=true,defaultValue="-1") Integer patientId,@RequestParam(value="encounterId",required=true,defaultValue="-1") Integer encounterId) {
		carePlanService.clearProgressSubjectiveNotes(gwid,patientId,encounterId);
	}

		/**
	 * To import shortcuts based on given shortcut IDs,patientId from previous visits
	 * @param patientId
	 * @param encounterId
	 * @param shortcutIDs
	 * @param providerId
	 * @return EMRResponseBean
	 * @throws Exception
	 */
	@RequestMapping(value ="/ImportCarePlanGoalFromPreviousVisits", method = RequestMethod.POST) 
	@ResponseBody
	public EMRResponseBean importCarePlanGoalFromPreviousVisits(@RequestParam(value="patientId",required=false,defaultValue="-1")Integer patientId,@RequestParam(value="encounterId",required=false,defaultValue="-1")Integer encounterId,@RequestParam(value="previousVisistShortcutIDs",required=false,defaultValue="")String previousVisistShortcutIDs,@RequestParam(value="providerId",required=false,defaultValue="-1")Integer providerId,@RequestParam(value="episodeId",required=false,defaultValue="-1")Integer episodeId,@RequestParam(value="shortcutTermPrevious",required=false,defaultValue="-1")Integer shortcutTermPrevious,@RequestParam(value="categoryId",required=false,defaultValue="-1")Integer categoryId,@RequestParam(value="previousEpisodeId",required=false,defaultValue="-1")Integer previousEpisodeId)throws Exception{
		Map<String, Object> lists=carePlanService.importCarePlanShortcutsFromPrevious(patientId,encounterId,previousVisistShortcutIDs,providerId,episodeId,shortcutTermPrevious,categoryId,previousEpisodeId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(lists);
		//auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.CAREPLAN, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved list of concerns which belongs to the given PatientId", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
	
	@RequestMapping(value ="/GetCarePlanProgressInitialData", method = RequestMethod.POST) 
	@ResponseBody
	
	public EMRResponseBean getCarePlanProgressIntialData(@RequestParam(value="patientId",required=false,defaultValue="-1")Integer patientId,@RequestParam(value="encounterId",required=false,defaultValue="-1")Integer encounterId,@RequestParam(value="episodeId",required=false,defaultValue="-1")Integer episodeId,@RequestParam(value="gwid",required=false,defaultValue="-1")String gwid)throws Exception{
		Map<String, Object> lists=carePlanService.getCarePlanProgressInitialData(patientId,encounterId,episodeId,gwid);
	//	auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.CAREPLAN, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved list of concerns which belongs to the given PatientId", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(lists);
		//auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved providers,pos,groups data", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
	

	/**
	 * To insert intervention data from care plan tab
	 * @param patientId,encounterId,description,snomedCode
	 * @throws Exception
	 */
	@RequestMapping(value ="/SaveCarePlanInterventionData", method = RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean saveInterventionData(@RequestBody CarePlanInterventionBean carePlanInterventionBean){
		List<CarePlanInterventionBean> carePlanInterventions = carePlanService.saveInterventionData(carePlanInterventionBean);
		EMRResponseBean bean= new EMRResponseBean();
		bean.setData(carePlanInterventions);
		return bean;		
	}
	
	
	/**
	 * To insert care Plan Intervention data for patient
	 * @param patientId,encounterId,description,snomedCode
	 * @throws Exception
	 */
	
	@RequestMapping(value ="/InsertCarePlanIntervention", method = RequestMethod.GET)
	@ResponseBody
	
	public EMRResponseBean SaveCarePlanIntervention(
			@RequestParam(value="patientId", required=true) Integer patientId,
			@RequestParam(value="encounterId", required=true) Integer encounterId,
			@RequestParam(value="elementName", required=true) String description,
			@RequestParam(value="elementGwid", required=true) String snomedCode,
			@RequestParam(value="status", required=true) Integer status,
			@RequestParam(value="userId", required=true) Integer userId,
			@RequestParam(value="interventionMode", required=true) Integer interventionMode,
			@RequestParam(value="intervenDxCode", required=true) String intervenDxCode,
			@RequestParam(value="intervenDxDesc", required=true) String intervenDxDesc,
			@RequestParam(value="intervenDxCodeSystem", required=true) String intervenDxCodeSystem){
			carePlanService.saveCarePlanIntervention(patientId,encounterId,description,snomedCode,status,userId,interventionMode,intervenDxCode,intervenDxDesc,intervenDxCodeSystem);
			List<CarePlanInterventionBean> getData = carePlanService.fetchCarePlanInterventions(patientId,encounterId,interventionMode,intervenDxCode);
		//	auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.CAREPLAN, LogActionType.CREATE, 0, Log_Outcome.SUCCESS, "Intervention saved for patient:"+intervenDxDesc, -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
			EMRResponseBean bean= new EMRResponseBean();
			bean.setData(getData);
				return bean;
	}
	
	/**
	 * To get care Plan Intervention data of a patient for the encounter
	 * @param patientId,encounterId
	 * @throws Exception
	 */
	@RequestMapping(value="/FetchCarePlanIntervention", method = RequestMethod.GET)
	@ResponseBody
	
	public EMRResponseBean getCarePlanData(
			@RequestParam(value="patientId", required=true) Integer patientId,
			@RequestParam(value="encounterId", required=true) Integer encounterId,
			@RequestParam(value="interventionMode", required=true) Integer interventionMode,
			@RequestParam(value="intervenDxCode", required=true) String intervenDxCode){
			List<CarePlanInterventionBean> getData = carePlanService.fetchCarePlanInterventions(patientId,encounterId,interventionMode,intervenDxCode);
			EMRResponseBean bean= new EMRResponseBean();
			bean.setData(getData);
				return bean;
		
	}
	
	/**
	 * To delete selected intervention data of a patient
	 * @param patientId,encounterId,interventionId
	 * @throws Exception
	 */
	@RequestMapping(value="/DeleteCarePlanIntervention", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean deleteCarePlan(
			@RequestParam(value="patientId", required=true) Integer patientId,
			@RequestParam(value="encounterId", required=true) Integer encounterId,
			@RequestParam(value="delVal", required=true) Integer delVal,
			@RequestParam(value="interventionMode", required=true) Integer interventionMode,
			@RequestParam(value="intervenDxCode", required=true) String intervenDxCode){
			carePlanService.deleteCarePlanIntervention(patientId,encounterId,delVal);
			List<CarePlanInterventionBean> getData = carePlanService.fetchCarePlanInterventions(patientId,encounterId,interventionMode,intervenDxCode);
			//auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.CAREPLAN, LogActionType.DELETE, 0, Log_Outcome.SUCCESS, "Intervention Deleted for patient with Intervention id:"+delVal, -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
			EMRResponseBean bean= new EMRResponseBean();
			bean.setData(getData);
				return bean;
	}
	
	
	/**
	 * To select interventions for edit
	 * @param patientId,encounterId,interventionId
	 * @throws Exception
	 */
	@RequestMapping(value="/GetEditCarePlanIntervention", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getEditCarePlanIntervention(@RequestParam(value="patientId", required=true) Integer patientId,
			@RequestParam(value="encounterId", required=true) Integer encounterId,
			@RequestParam(value="intervenId", required=true) Integer intervenId){
			Map<String, Object> lists=carePlanService.getEditCarePlanIntervention(patientId,encounterId,intervenId);
			EMRResponseBean bean= new EMRResponseBean();
			bean.setData(lists);
			return bean;
	}
	
	/**
	 * To update intervention data 
	 * @param patientId,encounterId,interventionId
	 * @throws Exception
	 */	
	@RequestMapping(value="/UpdateCarePlanInterventionData", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean updateCarePlanIntervention(@RequestParam(value="patientId", required=true) Integer patientId,
			@RequestParam(value="encounterId", required=true) Integer encounterId,
			@RequestParam(value="code", required=true) Integer interventionId,
			@RequestParam(value="editedNotes", required=true) String editedNotes,
			@RequestParam(value="orderedBy", required=true) Integer orderedBy,
			@RequestParam(value="performedBy", required=true) Integer performedBy,
			@RequestParam(value="notDoneBy", required=true) Integer notDoneBy,
			@RequestParam(value="notDoneReason", required=true) String notDoneReason,
			@RequestParam(value="userId", required=true) Integer userId,
			@RequestParam(value="status", required=true) Integer status,
			@RequestParam(value="perfOn", required=true) String perfOn,
			@RequestParam(value="orderedOn", required=true) String orderedOn,
			@RequestParam(value="interventionMode", required=true) Integer interventionMode,
			@RequestParam(value="intervenDxCode", required=true) String intervenDxCode){
			carePlanService.updateCarePlanIntervention(patientId,encounterId,interventionId,editedNotes,orderedBy,performedBy,notDoneBy,notDoneReason,userId,status,perfOn,orderedOn);
			//auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.CAREPLAN, LogActionType.UPDATE, 0, Log_Outcome.SUCCESS, "Intervention updated for patient with Intervention id:"+interventionId, -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
			List<CarePlanInterventionBean> getData = carePlanService.fetchCarePlanInterventions(patientId,encounterId,interventionMode,intervenDxCode);
			EMRResponseBean bean= new EMRResponseBean();
			bean.setData(getData);
				return bean;
	}
	@RequestMapping(value="/GetCarePlanSummary",method= RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getCarePlanSummary(@RequestParam(value="patientId", required=true) Integer patientId,
			@RequestParam(value="episodeId",required=false,defaultValue="-1")Integer episodeId,
			@RequestParam(value="episodeTypeId",required=false,defaultValue="-1")Integer episodeTypeId,
			@RequestParam(value="encounterId",required=false,defaultValue="-1")Integer encounterId){
			Map<String, Object> lists=carePlanService.getCarePlanSummaryData(patientId,episodeId,encounterId,episodeTypeId);
			EMRResponseBean emrResponseBean = new EMRResponseBean();
			emrResponseBean.setData(lists);
			return emrResponseBean;
				
	}
	
	@RequestMapping(value="/SaveCarePlanSummary",method= RequestMethod.POST)
	@ResponseBody
	public void saveCarePlanSummary(@RequestParam(value="patientId", required=true) Integer patientId,
			@RequestParam(value="episodeId",required=false,defaultValue="-1")Integer episodeId,
			@RequestParam(value="userId",required=false,defaultValue="-1")Integer userId,
			@RequestParam(value="completeJSON", required=true) String completeJSON) throws Exception{
		carePlanService.saveCarePlanSummaryData(completeJSON,userId);
	}
	
}