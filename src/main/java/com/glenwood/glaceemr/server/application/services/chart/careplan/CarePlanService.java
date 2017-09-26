package com.glenwood.glaceemr.server.application.services.chart.careplan;

import java.util.List;
import java.util.Map;

import com.glenwood.glaceemr.server.application.models.CarePlanConcern;
import com.glenwood.glaceemr.server.application.models.CarePlanGoal;
import com.glenwood.glaceemr.server.application.models.CarePlanIntervention;

public interface CarePlanService{
	
	List<CarePlanConcern> fetchCarePlanConcerns(Integer concernId,Integer patientId,Integer categoryId,Integer episodeId);
	
	List<CarePlanGoal> fetchCarePlanGoals(Integer goalId,Integer concernId,Integer patientId,Integer encoutnerId);
	
	List<CarePlanOutcomeBean> fetchCarePlanOutcomes(Integer outcomeId,Integer goalId,Integer patientId,Integer encoutnerId,Integer episodeId);

	Map<String,Object> getCarePlanInitialData(Integer patientId,Integer encounterId,Integer episodeId,Integer episodeTypeId,Integer previousEpisodeId);	
	List<CarePlanConcern> saveCarePlanConcern(CarePlanConcernBean carePlanConcerns);
	
	List<CarePlanGoalBean>  saveCarePlanGoal(CarePlanGoalBean carePlanGoal);
	
	List<CarePlanGoalBean>  saveCarePlanOutcomes(Integer goalId,Integer providerId,Integer patientId,Integer encounterId,Integer progress,String reviewDate,String targetDate,String notes,Integer outcomeStatus,Integer episodeId);

	String getVitals(Integer patientId,Integer encounterId);
		
	List<Object> fetchCarePlanShortcuts(Integer categoryId);

	Map<String,Object> importCarePlanShortcuts(Integer patientId,Integer encounterId,String shortcutIDs,Integer providerId,Integer episodeId,Integer shortcutTerm,Integer categoryId,Integer previousEpisodeId,Integer summaryMode);
	
	void saveProgressPlanNotes(Integer encounterId,String planText);
	
	void clearProgressPlanNotes(Integer encounterId);
	
	void saveProgressSubjectiveNotes(String gwid,Integer chartId,Integer patientId,Integer encounterId,String planText);
	
	void clearProgressSubjectiveNotes(String gwid,Integer patientId,Integer encounterId);
	
	Map<String,Object> getCarePlanProgressInitialData(Integer patientId,Integer encounterId,Integer episodeId,String gwid);
	
	Map<String,Object> importCarePlanShortcutsFromPrevious(Integer patientId,Integer encounterId,String previousGoalShortcutIDs,Integer providerId,Integer episodeId,Integer shortcutTerm,Integer categoryId,Integer previousEpisodeId);
	
	CarePlanIntervention saveCarePlanIntervention(Integer patientId, Integer encounterId, String description, String snomedCode, Integer status, Integer userId, Integer interventionMode, String intervenDxCode, String intervenDxDesc, String intervenDxCodeSystem);

	List<CarePlanInterventionBean> fetchCarePlanInterventions(Integer patientId, Integer encounterId, Integer interventionMode, String intervenDxCode);

	void deleteCarePlanIntervention(Integer patientId, Integer encounterId, Integer delVal);
	
	void updateCarePlanIntervention(Integer patientId, Integer encounterId, Integer interventionId,  String editedNotes, Integer orderedBy,	Integer performedBy,Integer notDoneBy, String notDoneReason, Integer userId, Integer status, String perfOn, String orderedOn);

	Map<String, Object> getEditCarePlanIntervention(Integer patientId, Integer encounterId, Integer intervenId);
	
	void saveInterventionData(List<CarePlanInterventionBean> carePlanInterventionBean);
	//List<CarePlanInterventionBean> saveInterventionData(CarePlanInterventionBean carePlanInterventionBean);
	
	Map<String, Object> getCarePlanSummaryData(Integer patientId, Integer episodeId, Integer encounterId, Integer episodeTypeId);
	
	void saveCarePlanSummaryData(String completeJSON,Integer userId) throws Exception;

	List<CarePlanInterventionBean> fetchInterventionPlanData(Integer goalId,Integer concernId,
			Integer CategoryId, Integer patientId, Integer encounterId,Integer intervenId);
	
}