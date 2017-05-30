package com.glenwood.glaceemr.server.application.services.chart.careplan;

import java.util.List;
import java.util.Map;
import com.glenwood.glaceemr.server.application.models.CarePlanConcern;
import com.glenwood.glaceemr.server.application.models.CarePlanGoal;

public interface CarePlanService{
	
	List<CarePlanConcern> fetchCarePlanConcerns(Integer concernId,Integer patientId,Integer categoryId);
	
	List<CarePlanGoal> fetchCarePlanGoals(Integer goalId,Integer concernId,Integer patientId,Integer encoutnerId);
	
	List<CarePlanOutcomeBean> fetchCarePlanOutcomes(Integer outcomeId,Integer goalId,Integer patientId,Integer encoutnerId);

	Map<String,Object> getCarePlanInitialData(Integer patientId,Integer encounterId);
	
	List<CarePlanConcern> saveCarePlanConcern(CarePlanConcernBean carePlanConcerns);
	
	List<CarePlanGoalBean>  saveCarePlanGoal(CarePlanGoalBean carePlanGoal);
	
	List<CarePlanGoalBean>  saveCarePlanOutcomes(Integer goalId,Integer providerId,Integer patientId,Integer encounterId,Integer progress,String reviewDate,String targetDate,String notes,Integer outcomeStatus);

	String getVitals(Integer patientId,Integer encounterId);
		
    List<Object> fetchCarePlanShortcuts();

	Map<String,Object> importCarePlanShortcuts(Integer patientId,Integer encounterId,String shortcutIDs,Integer providerId);
	
}