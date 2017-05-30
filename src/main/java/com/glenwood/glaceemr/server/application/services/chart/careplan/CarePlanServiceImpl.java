package com.glenwood.glaceemr.server.application.services.chart.careplan;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.CarePlanConcern;
import com.glenwood.glaceemr.server.application.models.CarePlanConcernShortcut;
import com.glenwood.glaceemr.server.application.models.CarePlanConcernShortcut_;
import com.glenwood.glaceemr.server.application.models.CarePlanConcern_;
import com.glenwood.glaceemr.server.application.models.CarePlanGoal;
import com.glenwood.glaceemr.server.application.models.CarePlanGoalShortcut;
import com.glenwood.glaceemr.server.application.models.CarePlanGoalShortcut_;
import com.glenwood.glaceemr.server.application.models.CarePlanGoal_;
import com.glenwood.glaceemr.server.application.models.CarePlanIntervention;
import com.glenwood.glaceemr.server.application.models.CarePlanIntervention_;
import com.glenwood.glaceemr.server.application.models.CarePlanOutcome;
import com.glenwood.glaceemr.server.application.models.CarePlanOutcome_;
import com.glenwood.glaceemr.server.application.models.ClinicalElements;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions_;
import com.glenwood.glaceemr.server.application.models.ClinicalElements_;
import com.glenwood.glaceemr.server.application.models.ClinicalTextMapping;
import com.glenwood.glaceemr.server.application.models.ClinicalTextMapping_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements_;
import com.glenwood.glaceemr.server.application.models.UnitsOfMeasure;
import com.glenwood.glaceemr.server.application.models.UnitsOfMeasure_;
import com.glenwood.glaceemr.server.application.models.VitalGroup;
import com.glenwood.glaceemr.server.application.models.VitalGroup_;
import com.glenwood.glaceemr.server.application.models.VitalsParameter;
import com.glenwood.glaceemr.server.application.models.VitalsParameter_;
import com.glenwood.glaceemr.server.application.repositories.CarePlanConcernRepository;
import com.glenwood.glaceemr.server.application.repositories.CarePlanGoalRepository;
import com.glenwood.glaceemr.server.application.repositories.CarePlanOutcomeRepository;
import com.glenwood.glaceemr.server.application.repositories.CarePlanInterventionRepository;
import com.glenwood.glaceemr.server.application.services.chart.clinicalElements.ClinicalConstants;
import com.glenwood.glaceemr.server.utils.HUtil;

@Service
public class CarePlanServiceImpl implements  CarePlanService  {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	CarePlanConcernRepository carePlanConcernRepository;

	@Autowired
	CarePlanOutcomeRepository carePlanOutcomeRepository;

	@Autowired
	CarePlanGoalRepository carePlanGoalRepository;

	@Autowired
	CarePlanInterventionRepository carePlanInterventionRepository;

	/**
	 * To fetch care plan concerns
	 * @param concernId
	 * @param patientId
	 * @param categoryId
	 * @return List
	 */
	@Override
	public List<CarePlanConcern> fetchCarePlanConcerns(Integer concernId,Integer patientId,Integer categoryId) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CarePlanConcern> cq = builder.createQuery(CarePlanConcern.class);
		Root<CarePlanConcern> root = cq.from(CarePlanConcern.class);
		List<Predicate> predicates = new ArrayList<>();
		if(patientId!=-1)
			predicates.add(builder.equal(root.get(CarePlanConcern_.carePlanConcernPatientId), patientId));
		if(categoryId!=-1)
			predicates.add(builder.equal(root.get(CarePlanConcern_.carePlanConcernCategoryId), categoryId));
		if(concernId!=-1)
			predicates.add(builder.equal(root.get(CarePlanConcern_.carePlanConcernId), concernId));
		predicates.add(builder.equal(root.get(CarePlanConcern_.carePlanConcernStatus),1));
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		List<CarePlanConcern> concerns=entityManager.createQuery(cq).getResultList();
		return concerns;
	}
	
	/**
	 * To fetch care plan goals
	 * @param goalId
	 * @param concernId
	 * @param patientId
	 * @param encounterId
	 * @return List
	 */
	@Override
	public List<CarePlanGoal> fetchCarePlanGoals(Integer goalId,Integer concernId,
			Integer patientId, Integer encounterId) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CarePlanGoal> cq = builder.createQuery(CarePlanGoal.class);
		Root<CarePlanGoal> root = cq.from(CarePlanGoal.class);
		List<Predicate> predicates = new ArrayList<>();
		if(patientId!=-1)
			predicates.add(builder.equal(root.get(CarePlanGoal_.carePlanGoalPatientId), patientId));
		if(encounterId!=-1)
			predicates.add(builder.equal(root.get(CarePlanGoal_.carePlanGoalEncounterId), encounterId));
		if(concernId!=-1)
			predicates.add(builder.equal(root.get(CarePlanGoal_.carePlanGoalConcernId), concernId));
		if(goalId!=-1)
			predicates.add(builder.equal(root.get(CarePlanGoal_.carePlanGoalId), goalId));
		predicates.add(builder.equal(root.get(CarePlanGoal_.carePlanGoalStatus),1));
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		List<CarePlanGoal> concerns=entityManager.createQuery(cq).getResultList();
		return concerns;
	}

	/**
	 * To fetch care plan goals (bean structure)
	 * @param goalId
	 * @param concernId
	 * @param patientId
	 * @param encounterId
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List<CarePlanGoalBean> fetchCarePlanGoalBean(Integer goalId,Integer concernId,
			Integer patientId, Integer encounterId) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CarePlanGoalBean> cq = builder.createQuery(CarePlanGoalBean.class);
		Root<CarePlanGoal> root = cq.from(CarePlanGoal.class);
		Join<CarePlanGoal,CarePlanConcern> concernJoin=root.join(CarePlanGoal_.carePlanConcern,JoinType.LEFT);
		Join<CarePlanGoal,CarePlanOutcome> outcomeJoin=root.join(CarePlanGoal_.carePlanOutcome,JoinType.LEFT);

		final Subquery<Integer> subquery = cq.subquery(Integer.class);
		final Root<CarePlanOutcome> carePlanOutcome = subquery.from(CarePlanOutcome.class);
		subquery.select(builder.max(carePlanOutcome.get(CarePlanOutcome_.carePlanOutcomeId)));
		subquery.groupBy(carePlanOutcome.get(CarePlanOutcome_.carePlanOutcomeGoalId));

		List<Predicate> predicates = new ArrayList<>();
		if(patientId!=-1)
			predicates.add(builder.equal(root.get(CarePlanGoal_.carePlanGoalPatientId), patientId));
		if(encounterId!=-1)
			predicates.add(builder.equal(root.get(CarePlanGoal_.carePlanGoalEncounterId), encounterId));
		if(concernId!=-1)
			predicates.add(builder.equal(root.get(CarePlanGoal_.carePlanGoalConcernId), concernId));
		if(goalId!=-1)
			predicates.add(builder.equal(root.get(CarePlanGoal_.carePlanGoalId), goalId));
		predicates.add(builder.equal(root.get(CarePlanGoal_.carePlanGoalStatus),1));
		predicates.add(builder.or(builder.in(outcomeJoin.get(CarePlanOutcome_.carePlanOutcomeId)).value(subquery),builder.isNull(outcomeJoin.get(CarePlanOutcome_.carePlanOutcomeId))));

		Selection[] selections=new Selection[]{
				root.get(CarePlanGoal_.carePlanGoalId),
				root.get(CarePlanGoal_.carePlanGoalPatientId),
				root.get(CarePlanGoal_.carePlanGoalEncounterId),
				builder.coalesce(root.get(CarePlanGoal_.carePlanGoalConcernId),-1),
				concernJoin.get(CarePlanConcern_.carePlanConcernDesc),
				builder.coalesce(root.get(CarePlanGoal_.carePlanGoalPriority),0),
				builder.coalesce(root.get(CarePlanGoal_.carePlanGoalType),-1),
				builder.coalesce(root.get(CarePlanGoal_.carePlanGoalTerm),0),
				root.get(CarePlanGoal_.carePlanGoalProviderId),
				root.get(CarePlanGoal_.carePlanGoalDesc),
				builder.coalesce(root.get(CarePlanGoal_.carePlanGoalCode),""),
				builder.coalesce(root.get(CarePlanGoal_.carePlanGoalCodeDescription),""),
				builder.coalesce(root.get(CarePlanGoal_.carePlanGoalCodeOperator),""),
				builder.coalesce(root.get(CarePlanGoal_.carePlanGoalValue),""),
				builder.coalesce(root.get(CarePlanGoal_.carePlanGoalUnit),""),
				builder.coalesce(root.get(CarePlanGoal_.carePlanGoalStatus),0),
				root.get(CarePlanGoal_.carePlanGoalTargetDate),
				root.get(CarePlanGoal_.carePlanGoalNextReviewDate),
				root.get(CarePlanGoal_.carePlanGoalNotes),
				builder.coalesce(outcomeJoin.get(CarePlanOutcome_.carePlanOutcomeProgress),0),
		};

		cq.select(builder.construct(CarePlanGoalBean.class, selections));
		cq.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
		List<CarePlanGoalBean> concerns=entityManager.createQuery(cq).getResultList();
		return concerns;
	}
	
	/**
	 * To fetch care plan outcomes
	 * @param outcomeId
	 * @param goalId
	 * @param patientId
	 * @param encounterId
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<CarePlanOutcomeBean> fetchCarePlanOutcomes(Integer outcomeId,Integer goalId, Integer patientId, Integer encounterId) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CarePlanOutcomeBean> cq = builder.createQuery(CarePlanOutcomeBean.class);
		Root<CarePlanOutcome> root = cq.from(CarePlanOutcome.class);
		Join<CarePlanOutcome,EmployeeProfile> employeeJoin=root.join(CarePlanOutcome_.empProfile,JoinType.INNER);
		Join<CarePlanOutcome,CarePlanGoal> outcomeJoin=root.join(CarePlanOutcome_.carePlanGoal,JoinType.INNER);

		Selection[] selections=new Selection[]{
				root.get(CarePlanOutcome_.carePlanOutcomeId),
				root.get(CarePlanOutcome_.carePlanOutcomeGoalId),
				root.get(CarePlanOutcome_.carePlanOutcomeReviewDate),
				outcomeJoin.get(CarePlanGoal_.carePlanGoalTargetDate),
				root.get(CarePlanOutcome_.carePlanOutcomeNotes),
				root.get(CarePlanOutcome_.carePlanOutcomeProgress),
				employeeJoin.get(EmployeeProfile_.empProfileFullname),
				outcomeJoin.get(CarePlanGoal_.carePlanGoalStatus)
		};
		
		List<Predicate> predicates = new ArrayList<>();
		if(patientId!=-1)
			predicates.add(builder.equal(root.get(CarePlanOutcome_.carePlanOutcomePatientId), patientId));
		if(encounterId!=-1)
			predicates.add(builder.equal(root.get(CarePlanOutcome_.carePlanOutcomeEncounterId), encounterId));
		if(goalId!=-1)
			predicates.add(builder.equal(root.get(CarePlanOutcome_.carePlanOutcomeGoalId), goalId));
		if(outcomeId!=-1)
			predicates.add(builder.equal(root.get(CarePlanOutcome_.carePlanOutcomeId), outcomeId));
		
		cq.select(builder.construct(CarePlanOutcomeBean.class, selections));
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
	
		List<CarePlanOutcomeBean> outcomes=entityManager.createQuery(cq).getResultList();
		return outcomes;
	}
	
	/**
	 * To save care plan concern from given Bean
	 * @param CarePlanConcernBean
	 * @return List
	 */
	@Override
	public List<CarePlanConcern> saveCarePlanConcern(CarePlanConcernBean carePlanConcernJSON) {
		
		CarePlanConcern carePlanConcern=new CarePlanConcern();
		if(carePlanConcernJSON.getConcernId()!=-1)
			carePlanConcern.setCarePlanConcernId(carePlanConcernJSON.getConcernId());
		carePlanConcern.setCarePlanConcernCategoryId(carePlanConcernJSON.getConcernCategoryId());
		carePlanConcern.setCarePlanConcernPatientId(carePlanConcernJSON.getConcernPatientId());
		carePlanConcern.setCarePlanConcernProviderId(carePlanConcernJSON.getConcernProviderId());
		carePlanConcern.setCarePlanConcernType(carePlanConcernJSON.getConcernType());
		carePlanConcern.setCarePlanConcernCode(carePlanConcernJSON.getConcernCode());
		carePlanConcern.setCarePlanConcernCodeSystem(carePlanConcernJSON.getConcernCodeSystem());
		carePlanConcern.setCarePlanConcernCodeSystemName(carePlanConcernJSON.getConcernCodeSystemName());
		carePlanConcern.setCarePlanConcernCodeDesc(carePlanConcernJSON.getConcernCodeDesc());
		carePlanConcern.setCarePlanConcernPriority(carePlanConcernJSON.getConcernPriority());
		carePlanConcern.setCarePlanConcernValue(carePlanConcernJSON.getConcernValue());
		carePlanConcern.setCarePlanConcernUnit(carePlanConcernJSON.getConcernUnit());
		carePlanConcern.setCarePlanConcernDesc(carePlanConcernJSON.getConcernDesc());
		carePlanConcern.setCarePlanConcernNotes(carePlanConcernJSON.getConcernNotes());
		carePlanConcern.setCarePlanConcernStatus(carePlanConcernJSON.getConcernStatus());
		carePlanConcern.setCarePlanConcernStatusUpdatedDate(carePlanConcernRepository.findCurrentTimeStamp());
		carePlanConcern.setCarePlanConcernCreatedBy(carePlanConcernJSON.getConcernCreatedBy());
		carePlanConcern.setCarePlanConcernModifiedBy(carePlanConcernJSON.getConcernModifiedBy());
		carePlanConcern.setCarePlanConcernCreatedOn(carePlanConcernRepository.findCurrentTimeStamp());
		carePlanConcern.setCarePlanConcernModifiedOn(carePlanConcernRepository.findCurrentTimeStamp());
		carePlanConcernRepository.saveAndFlush(carePlanConcern);
		
		List<CarePlanConcern> carePlanConcerns=fetchCarePlanConcerns(-1,carePlanConcernJSON.getConcernPatientId(),-1);
		return carePlanConcerns;
	}

	/**
	 * To save care plan goal from given Bean
	 * @param CarePlanGoalBean
	 * @return List
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<CarePlanGoalBean> saveCarePlanGoal(CarePlanGoalBean carePlanGoalData) {
		
		CarePlanGoal carePlanGoal=new CarePlanGoal();
		if(carePlanGoalData.getCarePlanGoalId()!=-1)
			carePlanGoal.setCarePlanGoalId(carePlanGoalData.getCarePlanGoalId());
		carePlanGoal.setCarePlanGoalPatientId(carePlanGoalData.getCarePlanGoalPatientId());
		carePlanGoal.setCarePlanGoalEncounterId(carePlanGoalData.getCarePlanGoalEncounterId());
		carePlanGoal.setCarePlanGoalConcernId(carePlanGoalData.getCarePlanGoalConcernId());
		carePlanGoal.setCarePlanGoalParentId(-1);
		carePlanGoal.setCarePlanGoalGoalType(-1);
		carePlanGoal.setCarePlanGoalPriority(carePlanGoalData.getCarePlanGoalPriority());
		carePlanGoal.setCarePlanGoalType(carePlanGoalData.getCarePlanGoalType());
		carePlanGoal.setCarePlanGoalTerm(carePlanGoalData.getCarePlanGoalTerm());
		carePlanGoal.setCarePlanGoalProviderId(carePlanGoalData.getCarePlanGoalProviderId());
		carePlanGoal.setCarePlanGoalDesc(carePlanGoalData.getCarePlanGoalDesc());
		carePlanGoal.setCarePlanGoalCode(carePlanGoalData.getCarePlanGoalCode());
		carePlanGoal.setCarePlanGoalCodeDescription(carePlanGoalData.getCarePlanGoalCodeDescription());
		carePlanGoal.setCarePlanGoalCodeSystem("2.16.840.1.113883.6.96");
		carePlanGoal.setCarePlanGoalCodeSystemName("SNOMED");
		carePlanGoal.setCarePlanGoalCodeOperator(carePlanGoalData.getCarePlanGoalCodeOperator());
		carePlanGoal.setCarePlanGoalValue(carePlanGoalData.getCarePlanGoalValue());
		carePlanGoal.setCarePlanGoalUnit(carePlanGoalData.getCarePlanGoalUnit());		
		carePlanGoal.setCarePlanGoalStatus(carePlanGoalData.getCarePlanGoalStatus());

		try{
			SimpleDateFormat ft = new SimpleDateFormat ("MM/dd/yyyy HH:mm:ss");
			if(carePlanGoalData.getCarePlanGoalTargetDate()!=null &carePlanGoalData.getCarePlanGoalTargetDate()!="")
			{
				Date targetDateString=new Date(carePlanGoalData.getCarePlanGoalTargetDate());
				Date targetDate = new Date(ft.format(targetDateString));
				carePlanGoal.setCarePlanGoalTargetDate((new Timestamp(targetDate.getTime())));
			}
			else
				carePlanGoal.setCarePlanGoalTargetDate(null);
			if(carePlanGoalData.getCarePlanGoalNextReviewDate()!=null &carePlanGoalData.getCarePlanGoalNextReviewDate()!="")
			{
				Date reviewDateString=new Date(carePlanGoalData.getCarePlanGoalNextReviewDate());
				Date reviewDate = new Date(ft.format(reviewDateString));
				carePlanGoal.setCarePlanGoalNextReviewDate(new Timestamp(reviewDate.getTime()));
			}
			else
				carePlanGoal.setCarePlanGoalNextReviewDate(null);
		}
		catch(Exception e){
		}
		carePlanGoal.setCarePlanGoalNotes(carePlanGoalData.getCarePlanGoalNotes());
		carePlanGoal.setCarePlanGoalCreatedBy(carePlanGoalData.getCarePlanGoalCreatedBy());
		carePlanGoal.setCarePlanGoalModifiedBy(carePlanGoalData.getCarePlanGoalModifiedBy());
		carePlanGoal.setCarePlanGoalCreatedOn(carePlanConcernRepository.findCurrentTimeStamp());
		carePlanGoal.setCarePlanGoalCreatedOn(carePlanConcernRepository.findCurrentTimeStamp());
		carePlanGoalRepository.saveAndFlush(carePlanGoal);
	
		List<CarePlanGoalBean> carePlanGoals=fetchCarePlanGoalBean(-1,-1,carePlanGoalData.getCarePlanGoalPatientId(),-1);
		return carePlanGoals;
	}

	/**
	 * To save outcome of particular care plan goal
	 * @param goalId
	 * @param providerId
	 * @param patientId
	 * @param encounterId
	 * @param progress
	 * @param reviewDate
	 * @param targetDate
	 * @param notes
	 * @param status
	 * @return List
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<CarePlanGoalBean>  saveCarePlanOutcomes(Integer goalId,Integer providerId,Integer patientId,Integer encounterId,Integer progress,String reviewDate,String targetDate,String notes,Integer status) {
		
		CarePlanOutcome carePlanOutcome=new CarePlanOutcome();
		carePlanOutcome.setCarePlanOutcomeGoalId(goalId);
		carePlanOutcome.setCarePlanOutcomePatientId(patientId);
		carePlanOutcome.setCarePlanOutcomeEncounterId(encounterId);
		carePlanOutcome.setCarePlanOutcomeProviderId(providerId);
		carePlanOutcome.setCarePlanOutcomeProgress(progress);
		
		SimpleDateFormat ft = new SimpleDateFormat ("MM/dd/yyyy HH:mm:ss");
		try{
			Date reviewDateString=new Date(reviewDate);
			Date reviewDateToSave = new Date(ft.format(reviewDateString));
			carePlanOutcome.setCarePlanOutcomeReviewDate((new Timestamp(reviewDateToSave.getTime())));
		}
		catch(Exception e){}
		
		carePlanOutcome.setCarePlanOutcomeCreatedBy(providerId);
		carePlanOutcome.setCarePlanOutcomeModifiedBy(providerId);
		carePlanOutcome.setCarePlanOutcomeCreatedOn(carePlanOutcomeRepository.findCurrentTimeStamp());
		carePlanOutcome.setCarePlanOutcomeModifiedOn(carePlanOutcomeRepository.findCurrentTimeStamp());
		carePlanOutcome.setCarePlanOutcomeNotes(notes);
		
		if(goalId!=-1){		
				Date targetDateString=new Date(targetDate);
				Date targetDateToSave = new Date(ft.format(targetDateString));
				CriteriaBuilder cb = entityManager.getCriteriaBuilder();
				CriteriaUpdate<CarePlanGoal> cu = cb.createCriteriaUpdate(CarePlanGoal.class);
				Root<CarePlanGoal> rootCriteria = cu.from(CarePlanGoal.class);
				cu.set(rootCriteria.get(CarePlanGoal_.carePlanGoalTargetDate),new Timestamp(targetDateToSave.getTime()) );
				cu.set(rootCriteria.get(CarePlanGoal_.carePlanGoalStatus), status);
				cu.where(cb.equal(rootCriteria.get(CarePlanGoal_.carePlanGoalId),goalId),
						cb.equal(rootCriteria.get(CarePlanGoal_.carePlanGoalPatientId),patientId));
				this.entityManager.createQuery(cu).executeUpdate();
		}	
		carePlanOutcomeRepository.saveAndFlush(carePlanOutcome);
		
		List<CarePlanGoalBean> carePlanGoals=fetchCarePlanGoalBean(-1,-1,patientId,-1);
		return carePlanGoals;
	}

	/**
	 * To frame initial data for Care plan tab with concerns,goals,outcomes of particular patient
	 * @param patientId
	 * @param encounterId
	 * @return List
	 */
	@Override
	public Map<String, Object> getCarePlanInitialData(Integer patientId,
			Integer encounterId) {
		Map<String,Object> listsMap=new HashMap<String,Object>();
		listsMap.put("concernsList", fetchCarePlanConcerns(-1,patientId,-1));
		listsMap.put("goalsList", fetchCarePlanGoalBean(-1,-1,patientId,encounterId));
		listsMap.put("interventionsList", fetchInterventionData(-1,-1,-1,patientId,encounterId));
		listsMap.put("outcomesList", fetchCarePlanOutcomes(-1,-1,patientId,encounterId));
		listsMap.put("unitsList",getUnitsOfMeasures() );
		listsMap.put("vitalsList", getVitalParameters());
		listsMap.put("shortcutsList", fetchCarePlanShortcuts());
		return listsMap;
	}
	
	/**
	 * To fetch Intervention data
	 * @param goalId
	 * @param concernId
	 * @param categoryId
	 * @param patientId
	 * @param encounterId
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List<CarePlanInterventionBean> fetchInterventionData(Integer goalId,Integer concernId,
			Integer categoryId, Integer patientId, Integer encounterId){
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CarePlanInterventionBean> cq = builder.createQuery(CarePlanInterventionBean.class);
		Root<CarePlanIntervention> root = cq.from(CarePlanIntervention.class);

		List<Predicate> predicates = new ArrayList<>();
		if(patientId!=-1)
			predicates.add(builder.equal(root.get(CarePlanIntervention_.carePlanInterventionPatientId), patientId));
		if(encounterId!=-1)
			predicates.add(builder.equal(root.get(CarePlanIntervention_.carePlanInterventionEncounterId), encounterId));
		if(concernId!=-1)
			predicates.add(builder.equal(root.get(CarePlanIntervention_.carePlanInterventionConcernId), concernId));
		if(goalId!=-1)
			predicates.add(builder.equal(root.get(CarePlanIntervention_.carePlanInterventionGoalId), goalId));
		if(categoryId!=-1)
			predicates.add(builder.equal(root.get(CarePlanIntervention_.carePlanInterventionCategoryId), categoryId));

		Selection[] selectedColumns = new Selection[]{
				root.get(CarePlanIntervention_.carePlanInterventionId),
				root.get(CarePlanIntervention_.carePlanInterventionPatientId),
				root.get(CarePlanIntervention_.carePlanInterventionEncounterId),
				root.get(CarePlanIntervention_.carePlanInterventionConcernId),
				root.get(CarePlanIntervention_.carePlanInterventionGoalId),
				root.get(CarePlanIntervention_.carePlanInterventionCategoryId),
				root.get(CarePlanIntervention_.carePlanInterventionDescription),
				root.get(CarePlanIntervention_.carePlanInterventionCode),
				root.get(CarePlanIntervention_.carePlanInterventionCodeName),
				root.get(CarePlanIntervention_.carePlanInterventionProblemCode),
				root.get(CarePlanIntervention_.carePlanInterventionProblemCodeSystem),
				root.get(CarePlanIntervention_.carePlanInterventionProblemCodeSystemDescription),
				root.get(CarePlanIntervention_.carePlanInterventionStatus),
				root.get(CarePlanIntervention_.carePlanInterventionOrderedOn),
				root.get(CarePlanIntervention_.carePlanInterventionPerformedOn),
				root.get(CarePlanIntervention_.carePlanInterventionNotDoneType),
				root.get(CarePlanIntervention_.carePlanInterventionNotDoneDescription),
				root.get(CarePlanIntervention_.carePlanInterventionNotDoneCode),
				root.get(CarePlanIntervention_.carePlanInterventionNotDoneCodeSystem),
				root.get(CarePlanIntervention_.carePlanInterventionNotes)
		};

		cq.select(builder.construct(CarePlanInterventionBean.class, selectedColumns));
		cq.where(predicates.toArray(new Predicate[predicates.size()]));

		List<CarePlanInterventionBean> interventions=entityManager.createQuery(cq).getResultList();
		return interventions;
	}

	/**
	 * To fetch Intervention data for Plan tab
	 * @param goalId
	 * @param concernId
	 * @param categoryId
	 * @param patientId
	 * @param encounterId
	 * @param intervenId
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List<CarePlanInterventionBean> fetchInterventionData(Integer goalId,Integer concernId,
			Integer CategoryId, Integer patientId, Integer encounterId,Integer intervenId){
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CarePlanInterventionBean> cq = builder.createQuery(CarePlanInterventionBean.class);
		Root<CarePlanIntervention> root = cq.from(CarePlanIntervention.class);

		List<Predicate> predicates = new ArrayList<>();
		if(patientId!=-1)
			predicates.add(builder.equal(root.get(CarePlanIntervention_.carePlanInterventionPatientId), patientId));
		if(encounterId!=-1)
			predicates.add(builder.equal(root.get(CarePlanIntervention_.carePlanInterventionEncounterId), encounterId));
		if(concernId!=-1)
			predicates.add(builder.equal(root.get(CarePlanIntervention_.carePlanInterventionConcernId), concernId));
		if(goalId!=-1)
			predicates.add(builder.equal(root.get(CarePlanIntervention_.carePlanInterventionGoalId), goalId));
		if(CategoryId!=-1)
			predicates.add(builder.equal(root.get(CarePlanIntervention_.carePlanInterventionCategoryId), CategoryId));
		if(intervenId!=-1)
			predicates.add(builder.equal(root.get(CarePlanIntervention_.carePlanInterventionId), intervenId));
		Selection[] selectedColumns = new Selection[]{
				root.get(CarePlanIntervention_.carePlanInterventionId),
				root.get(CarePlanIntervention_.carePlanInterventionPatientId),
				root.get(CarePlanIntervention_.carePlanInterventionEncounterId),
				root.get(CarePlanIntervention_.carePlanInterventionConcernId),
				root.get(CarePlanIntervention_.carePlanInterventionGoalId),
				root.get(CarePlanIntervention_.carePlanInterventionCategoryId),
				root.get(CarePlanIntervention_.carePlanInterventionDescription),
				root.get(CarePlanIntervention_.carePlanInterventionCode),
				root.get(CarePlanIntervention_.carePlanInterventionCodeName),
				root.get(CarePlanIntervention_.carePlanInterventionProblemCode),
				root.get(CarePlanIntervention_.carePlanInterventionProblemCodeSystem),
				root.get(CarePlanIntervention_.carePlanInterventionProblemCodeSystemDescription),
				root.get(CarePlanIntervention_.carePlanInterventionStatus),
				root.get(CarePlanIntervention_.carePlanInterventionOrderedBy),
				root.get(CarePlanIntervention_.carePlanInterventionOrderedOn),
				root.get(CarePlanIntervention_.carePlanInterventionPerformedBy),
				root.get(CarePlanIntervention_.carePlanInterventionPerformedOn),
				root.get(CarePlanIntervention_.carePlanInterventionNotDoneType),
				root.get(CarePlanIntervention_.carePlanInterventionNotDoneDescription),
				root.get(CarePlanIntervention_.carePlanInterventionNotDoneCode),
				root.get(CarePlanIntervention_.carePlanInterventionNotDoneCodeSystem),
				root.get(CarePlanIntervention_.carePlanInterventionNotes)
		};


		cq.select(builder.construct(CarePlanInterventionBean.class, selectedColumns));
		cq.where(predicates.toArray(new Predicate[predicates.size()]));

		List<CarePlanInterventionBean> interventions=entityManager.createQuery(cq).getResultList();
		return interventions;
	}

	/**
	 * To get vital elements of particular encounterId
	 * @param encounterId
	 * @return List
	 */
	private List<Object[]> getVitals(Integer encounterId) {
		CriteriaBuilder builder= entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query= builder.createQuery(Object[].class);
		Root<PatientClinicalElements> root= query.from(PatientClinicalElements.class);
		Join<PatientClinicalElements, VitalsParameter> vitalJoin= root.join(PatientClinicalElements_.vitalsParameter, JoinType.INNER);
		Join<PatientClinicalElements, ClinicalElements> clinicalJoin= root.join(PatientClinicalElements_.clinicalElement, JoinType.INNER);
		Join<VitalsParameter, UnitsOfMeasure> unitsJoin= vitalJoin.join(VitalsParameter_.unitsOfMeasureTable, JoinType.LEFT);
		Join<VitalsParameter, VitalGroup> groupJoin= vitalJoin.join(VitalsParameter_.vitalGroup, JoinType.INNER);
		Join<ClinicalElements, ClinicalElementsOptions> optionsJoin= clinicalJoin.join(ClinicalElements_.clinicalElementsOptions, JoinType.LEFT);
		Join<ClinicalElements, ClinicalTextMapping> textJoin= clinicalJoin.join(ClinicalElements_.clinicalTextMappings, JoinType.LEFT);

		query.multiselect(vitalJoin.get(VitalsParameter_.vitalsParameterName),
				root.get(PatientClinicalElements_.patientClinicalElementsGwid),
				root.get(PatientClinicalElements_.patientClinicalElementsValue),
				unitsJoin.get(UnitsOfMeasure_.unitsOfMeasureCode),
				optionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsName),
				clinicalJoin.get(ClinicalElements_.clinicalElementsDatatype),
				textJoin.get(ClinicalTextMapping_.clinicalTextMappingAssociatedElement));

		query.where(builder.equal(root.get(PatientClinicalElements_.patientClinicalElementsEncounterid),encounterId),
				builder.or(builder.equal(root.get(PatientClinicalElements_.patientClinicalElementsValue),optionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsValue)),
						builder.isNull(optionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsGwid))));

		query.orderBy(builder.asc(groupJoin.get(VitalGroup_.vitalGroupOrderby)),
				builder.asc(vitalJoin.get(VitalsParameter_.vitalsParameterId)));

		List<Object[]> result= entityManager.createQuery(query).getResultList();

		return result;
	}

	/**
	 * To get units of vital elements
	 * @return List
	 */
	private List<UnitsOfMeasure> getUnitsOfMeasures(){
		CriteriaBuilder builder= entityManager.getCriteriaBuilder();
		CriteriaQuery<UnitsOfMeasure> query= builder.createQuery(UnitsOfMeasure.class);
		Root<UnitsOfMeasure> root = query.from(UnitsOfMeasure.class);
		query.orderBy(builder.asc(root.get(UnitsOfMeasure_.unitsOfMeasureCode)));
		List<UnitsOfMeasure> units=entityManager.createQuery(query).getResultList();
		return units;
	}

	/**
	 * To get vital parameters
	 * @param vitalsList
	 * @return List
	 */
	private List<VitalsParameter> getVitalParameters(){
		CriteriaBuilder builder= entityManager.getCriteriaBuilder();
		CriteriaQuery<VitalsParameter> query= builder.createQuery(VitalsParameter.class);
		Root<VitalsParameter> root = query.from(VitalsParameter.class);
		query.where(builder.equal(root.get(VitalsParameter_.vitalsParameterIsactive),true));
		query.orderBy(builder.asc(root.get(VitalsParameter_.vitalsParameterName)));
		List<VitalsParameter> vitals=entityManager.createQuery(query).getResultList();
		return vitals;
	}
	
	/**
	 * To parse vitals to JSON
	 * @param vitalsList
	 * @return
	 * @throws JSONException
	 */
	private JSONArray parseVitals(List<Object[]> vitalsList) throws JSONException {
		JSONArray vitals= new JSONArray();
		for(int i=0; i<vitalsList.size(); i++){
			JSONObject vital= new JSONObject();
			Object[] vitalsObj= vitalsList.get(i);
			String name= vitalsObj[0].toString();
			String gwid= vitalsObj[1].toString();
			String value= vitalsObj[2].toString();
			String units= vitalsObj[3].toString();
			String optionname= vitalsObj[4]!= null? vitalsObj[4].toString(): "";
			int datatype= Integer.parseInt(vitalsObj[5].toString());
			String assgwid= vitalsObj[6]!= null? vitalsObj[6].toString(): "";
			if(datatype == ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_SINGLEOPTION){
				value= optionname;
			}else if(datatype == ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_SINGLEOPTION){
				if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t"))
					value= "Yes";
				else
					value= "No";
			}
			else if(name.toLowerCase().contains("systolic")){
				for(int j=0; j<vitalsList.size(); j++){
					Object[] tmpvitalsObj= vitalsList.get(j);
					String tmpassgwid= tmpvitalsObj[1]!= null? tmpvitalsObj[1].toString(): "";
					if(assgwid.equals(tmpassgwid)){
						String tmpvalue= tmpvitalsObj[2].toString();
						value= value+"/"+tmpvalue;
						break;
					}
				}
			}else if(name.equalsIgnoreCase("height")){
				String dispUnit= getDisplayUnit("0000200200100023000");
				value= HUtil.heightConversion(value, dispUnit);
			}else if(name.equalsIgnoreCase("weight")){
				String dispUnit= getDisplayUnit("0000200200100024000");
				value= HUtil.weightConversion(value, dispUnit);
			}

			if(!name.toLowerCase().contains("diastolic")){
				if(!name.equalsIgnoreCase("Height") && !name.equalsIgnoreCase("Weight")){
					if(name.contains("systolic")){
						name = name.substring(0,name.indexOf("systolic"))+"BP (mmHg)";
					}else if(name.contains("Systolic")){
						name = name.substring(0,name.indexOf("Systolic"))+"BP (mmHg)";
					}
					if(value.indexOf(".")!=-1 && value.indexOf(",")==-1){
						double doubleValue = Double.parseDouble(value);
						DecimalFormat f = new DecimalFormat("##.00");
						value = f.format(doubleValue).toString();
					}
				}
				vital.put("Unit", units);
				vital.put("DisplayName", name);
				vital.put("Value", value);
				vitals.put(vital);
			}
		}
		return vitals;
	}
	
	/**
	 * To get vital display units
	 * @param gwid
	 * @return String
	 */
	private String getDisplayUnit(String gwid) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<VitalsParameter> root = cq.from(VitalsParameter.class);
		cq.select(builder.coalesce(root.get(VitalsParameter_.vitalsParameterDisplayUnit),1));
		cq.where(builder.equal(root.get(VitalsParameter_.vitalsParameterGwId), gwid));
		List<Object> resultList = entityManager.createQuery(cq).getResultList();
		String unit= "1";
		if(resultList.size()>0)
			unit= resultList.get(0).toString();
		return unit;
	}

	/**
	 * To frame vitals with given patientId, encounterId
	 * @param patientId
	 * @param encounterId
	 * @return String
	 */
	public String getVitals(Integer patientId,Integer encounterId){
		List<Object[]> vitals= getVitals(encounterId);
		JSONArray vitalsArr=null;
		try {
			vitalsArr = parseVitals(vitals);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return vitalsArr.toString();
	}

	
	/**
	 * To fetch care plan shortcuts
	 * @return List
	 */
	@Override
	public List<Object> fetchCarePlanShortcuts(){
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<CarePlanGoalShortcut> root=cq.from(CarePlanGoalShortcut.class);
	    Join<CarePlanGoalShortcut,CarePlanConcernShortcut> concernJoin=root.join(CarePlanGoalShortcut_.carePlanConcernShortcut,JoinType.INNER);
		cq.multiselect(root.get(CarePlanGoalShortcut_.carePlanGoalShortcutId).alias("carePlanGoalShortcutId"),
		root.get(CarePlanGoalShortcut_.carePlanGoalShortcutDesc).alias("carePlanGoalShortcutDesc"),
		root.get(CarePlanGoalShortcut_.carePlanGoalShortcutTerm).alias("carePlanGoalShortcutTerm"),
		root.get(CarePlanGoalShortcut_.carePlanGoalShortcutPriority).alias("carePlanGoalShortcutPriority"),
		concernJoin.get(CarePlanConcernShortcut_.carePlanConcernShortcutId).alias("carePlanConcernShortcutId"),
		concernJoin.get(CarePlanConcernShortcut_.carePlanConcernShortcutDesc).alias("carePlanConcernShortcutDesc")	
		);
		cq.orderBy(builder.desc(concernJoin. get(CarePlanConcernShortcut_.carePlanConcernShortcutDesc)),builder.desc(root.get(CarePlanGoalShortcut_.carePlanGoalShortcutDesc)));
	    List<Object[]> shortcuts=entityManager.createQuery(cq).getResultList();
	    List<Object>  parsedShrotcuts=new ArrayList<Object>();
	    for(Object[]  shortcut:shortcuts){
	    	Map<String, String> parsedObject=new HashMap<String, String>();
	    	try {
				parsedObject.put("carePlanGoalShortcutId", shortcut[0].toString());
				parsedObject.put("carePlanGoalShortcutDesc", shortcut[1].toString());	
				parsedObject.put("carePlanGoalShortcutTerm", shortcut[2].toString());	
				parsedObject.put("carePlanGoalShortcutPriority", shortcut[3].toString());
				parsedObject.put("carePlanConcernShortcutId", shortcut[4].toString());
				parsedObject.put("carePlanConcernShortcutDesc", shortcut[5].toString());
				parsedShrotcuts.add(parsedObject);
			} catch (Exception e) {
			}
	    }
	  
		
		return parsedShrotcuts;
	}

	/**
	 * To import care plan shortcuts for given patient details
	 * @param patientId
	 * @param encounterId
	 * @param shortcutIDs
	 * @param providerId
	 * @return Map
	 */
	@Override
	public Map<String, Object> importCarePlanShortcuts(Integer patientId,
			Integer encounterId, String shortcutIDs,Integer providerId) {
		List<String> shortcutIdList=Arrays.asList(shortcutIDs.split(","));
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		for(String shortcutId:shortcutIdList){
			CriteriaQuery<CarePlanGoalShortcut> cq = builder.createQuery(CarePlanGoalShortcut.class);
			Root<CarePlanGoalShortcut> root=cq.from(CarePlanGoalShortcut.class);
			cq.where(builder.equal(root.get(CarePlanGoalShortcut_.carePlanGoalShortcutId),shortcutId));
			CarePlanGoalShortcut shortcut=(CarePlanGoalShortcut) entityManager.createQuery(cq).getResultList().get(0);
		    Integer concernShortcutId=-1;
			if(shortcut.getCarePlanGoalShortcutConcernShortcutId()!=-1){
				CriteriaQuery<CarePlanConcernShortcut> concernQuery = builder.createQuery(CarePlanConcernShortcut.class);
				Root<CarePlanConcernShortcut> rootConcern=concernQuery.from(CarePlanConcernShortcut.class);
				concernQuery.where(builder.equal(rootConcern.get(CarePlanConcernShortcut_.carePlanConcernShortcutId),shortcut.getCarePlanGoalShortcutConcernShortcutId()));
				CarePlanConcernShortcut concernShortcut=(CarePlanConcernShortcut) entityManager.createQuery(concernQuery).getResultList().get(0);
			    CarePlanConcern carePlanConcern=new CarePlanConcern();
			    carePlanConcern.setCarePlanConcernCategoryId(concernShortcut.getCarePlanConcernShortcutCategoryId());
				carePlanConcern.setCarePlanConcernPatientId(patientId);
				carePlanConcern.setCarePlanConcernProviderId(providerId);
				carePlanConcern.setCarePlanConcernType(concernShortcut.getCarePlanConcernShortcutType());
				carePlanConcern.setCarePlanConcernCode(concernShortcut.getCarePlanConcernShortcutCode());
				carePlanConcern.setCarePlanConcernCodeSystem(concernShortcut.getCarePlanConcernShortcutCodeSystem());
				carePlanConcern.setCarePlanConcernCodeSystemName(concernShortcut.getCarePlanConcernShortcutCodeSystemName());
				carePlanConcern.setCarePlanConcernCodeDesc(concernShortcut.getCarePlanConcernShortcutCodeDesc());
				carePlanConcern.setCarePlanConcernPriority(concernShortcut.getCarePlanConcernShortcutPriority());
				carePlanConcern.setCarePlanConcernValue(concernShortcut.getCarePlanConcernShortcutValue());
				carePlanConcern.setCarePlanConcernUnit(concernShortcut.getCarePlanConcernShortcutUnit());
				carePlanConcern.setCarePlanConcernDesc(concernShortcut.getCarePlanConcernShortcutDesc());
				carePlanConcern.setCarePlanConcernNotes(concernShortcut.getCarePlanConcernShortcutNotes());
				carePlanConcern.setCarePlanConcernStatus(concernShortcut.getCarePlanConcernShortcutStatus());
				carePlanConcern.setCarePlanConcernStatusUpdatedDate(carePlanConcernRepository.findCurrentTimeStamp());
				carePlanConcern.setCarePlanConcernCreatedBy(providerId);
				carePlanConcern.setCarePlanConcernModifiedBy(providerId);
				carePlanConcern.setCarePlanConcernCreatedOn(carePlanConcernRepository.findCurrentTimeStamp());
				carePlanConcern.setCarePlanConcernModifiedOn(carePlanConcernRepository.findCurrentTimeStamp());
				carePlanConcernRepository.saveAndFlush(carePlanConcern);
				concernShortcutId=carePlanConcern.getCarePlanConcernId();
			}
			
				
			
			System.out.println("concernShortcutId>>>>>"+concernShortcutId);
			CarePlanGoal carePlanGoal=new CarePlanGoal();
			carePlanGoal.setCarePlanGoalPatientId(patientId);
			carePlanGoal.setCarePlanGoalEncounterId(encounterId);
			carePlanGoal.setCarePlanGoalConcernId(concernShortcutId);
			carePlanGoal.setCarePlanGoalParentId(-1);
			carePlanGoal.setCarePlanGoalGoalType(-1);
			carePlanGoal.setCarePlanGoalPriority(shortcut.getCarePlanGoalShortcutPriority());
			carePlanGoal.setCarePlanGoalType(shortcut.getCarePlanGoalShortcutGoalType());
			carePlanGoal.setCarePlanGoalTerm(shortcut.getCarePlanGoalShortcutTerm());
			carePlanGoal.setCarePlanGoalProviderId(providerId);
			carePlanGoal.setCarePlanGoalDesc(shortcut.getCarePlanGoalShortcutDesc());
			carePlanGoal.setCarePlanGoalCode(shortcut.getCarePlanGoalShortcutCode());
			carePlanGoal.setCarePlanGoalCodeDescription(shortcut.getCarePlanGoalShortcutCodeDescription());
			carePlanGoal.setCarePlanGoalCodeSystem("2.16.840.1.113883.6.96");
			carePlanGoal.setCarePlanGoalCodeSystemName("SNOMED");
			carePlanGoal.setCarePlanGoalCodeOperator(shortcut.getCarePlanGoalShortcutCodeOperator());
			carePlanGoal.setCarePlanGoalValue(shortcut.getCarePlanGoalShortcutValue());
			carePlanGoal.setCarePlanGoalUnit(shortcut.getCarePlanGoalShortcutUnit());		
			carePlanGoal.setCarePlanGoalStatus(shortcut.getCarePlanGoalShortcutStatus());
			carePlanGoal.setCarePlanGoalTargetDate(null);
			carePlanGoal.setCarePlanGoalNextReviewDate(null);
			carePlanGoal.setCarePlanGoalNotes(shortcut.getCarePlanGoalShortcutNotes());
			carePlanGoal.setCarePlanGoalCreatedBy(providerId);
			carePlanGoal.setCarePlanGoalModifiedBy(providerId);
			carePlanGoal.setCarePlanGoalCreatedOn(carePlanConcernRepository.findCurrentTimeStamp());
			carePlanGoal.setCarePlanGoalCreatedOn(carePlanConcernRepository.findCurrentTimeStamp());
			carePlanGoalRepository.saveAndFlush(carePlanGoal);
		}
		Map<String,Object> listsMap=new HashMap<String,Object>();
		listsMap=getCarePlanInitialData(patientId,encounterId);
		return listsMap;
	}
	
}