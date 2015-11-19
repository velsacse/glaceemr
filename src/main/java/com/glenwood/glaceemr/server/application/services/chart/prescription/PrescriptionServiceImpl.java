package com.glenwood.glaceemr.server.application.services.chart.prescription;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.models.CurrentMedication;
import com.glenwood.glaceemr.server.application.models.DrugSchedule;
import com.glenwood.glaceemr.server.application.models.MedsAdminLog;
import com.glenwood.glaceemr.server.application.models.MedsAdminPlan;
import com.glenwood.glaceemr.server.application.models.MedsAdminPlanShortcut;
import com.glenwood.glaceemr.server.application.models.Prescription;
import com.glenwood.glaceemr.server.application.models.Prescription_;
import com.glenwood.glaceemr.server.application.repositories.CurrentMedicationRepository;
import com.glenwood.glaceemr.server.application.repositories.DrugScheduleRepository;
import com.glenwood.glaceemr.server.application.repositories.MedAdministrationLogRepository;
import com.glenwood.glaceemr.server.application.repositories.MedAdministrationPlanRepository;
import com.glenwood.glaceemr.server.application.repositories.MedAdministrationPlanShortcutRepository;
import com.glenwood.glaceemr.server.application.repositories.PrescriptionRepository;
import com.glenwood.glaceemr.server.application.specifications.PrescripitonSpecification;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

  
@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService{
	@Autowired
	PrescriptionRepository prescriptionRepository;
	
	@Autowired
	CurrentMedicationRepository currentMedicationRepository;
	
	@Autowired
	DrugScheduleRepository drugScheduleRepository;
	
	
	@Autowired
	MedAdministrationPlanRepository medAdminPlanRepository;
	
	@Autowired
	MedAdministrationLogRepository medAdminLogRepository;
	
	@Autowired
	MedAdministrationPlanShortcutRepository medAdministrationPlanShortcutRepository;
	
	@PersistenceContext
	EntityManager em;
	/**
	 * To get the prescribed medications by the doctor
	 */
	@Override
	public Map<String, Object> getactivemedwithclass(Integer patientid) {
		List<CurrentMedication> currentmedlist=currentMedicationRepository.findAll(PrescripitonSpecification.getactivemedwithclass(patientid,null,null));
		List<Prescription> prescmedlist=prescriptionRepository.findAll(PrescripitonSpecification.getactivemedwithclasspresc(patientid,null,null));
		Map<String, Object> mapobject=new HashMap<String,Object>();
		mapobject.put("currentmed", currentmedlist);
		mapobject.put("prescmed", prescmedlist);
		return mapobject;
	}

	/**
	 * To save the medications plan (Currently this is for medication log used by Behavioural health practices)
	 */
	@Override
	public void saveMedicationAdminPlan(String dataToSave) throws JSONException {
	       JSONObject medPlanData = new JSONObject(dataToSave);
	       int patientId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(medPlanData.get("patientId").toString())).or("-1"));
	       int prescId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(medPlanData.get("prescId").toString())).or("-1"));
	       int userId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(medPlanData.get("loginId").toString())).or("-1"));
	       String planTime =  Optional.fromNullable(Strings.emptyToNull(medPlanData.get("planTimeList").toString())).or("-1");
	       String planTimeList[] = null;
			try {
				if(planTime!=null&&planTime!=null) {
					planTimeList = planTime.split(",");
				}
			} catch (Exception e) {
				System.out.println("Exception in plan time set up \n"+e);
			}
			for(int i=0;i<planTimeList.length;i++) {
				String planTimeTemp = planTimeList[i].replace("[", "").replace("]", "").trim();
				MedsAdminPlan newPlan = new MedsAdminPlan();
			    newPlan.setMedsAdminPlanMedicationId(prescId);
			    newPlan.setMedsAdminPlanPatientId(patientId);
			    newPlan.setMedsAdminPlanTime(planTimeTemp);
			    newPlan.setMedsAdminPlanSavedTime(medAdminLogRepository.findCurrentTimeStamp());
			    newPlan.setMedsAdminPlanOrderedBy(userId);
			    newPlan.setMedsAdminPlanModifiedOn(medAdminLogRepository.findCurrentTimeStamp());
			    newPlan.setMedsAdminPlanModifiedBy(userId);
			    newPlan.setMedsAdminPlanIsActive(true);
			    medAdminPlanRepository.saveAndFlush(newPlan);
			}
	}

	/**
	 * To save the administration details of a patient. (Currently this is for medication log used by Behavioural health practices)
	 */
	@Override
	public void saveMedicationAdminLog(String dataToSave) throws Exception {
		JSONObject medAdminLogData = new JSONObject(dataToSave);
	    int patientId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(medAdminLogData.get("patientId").toString())).or("-1"));
	    int planId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(medAdminLogData.get("planId").toString())).or("-1"));
	    int administeredBy = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(medAdminLogData.get("administeredBy").toString())).or("-1"));
	    String actualplanDate = Optional.fromNullable(Strings.emptyToNull(medAdminLogData.get("actualplanDate").toString())).or("");
	    int logId = -1;
	    boolean isDuplicate = Boolean.parseBoolean(Optional.fromNullable(Strings.emptyToNull(medAdminLogData.get("isDuplicate").toString())).or("false"));
	    if(isDuplicate) 
	    	logId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(medAdminLogData.get("logId").toString())).or("-1"));
	    int medAdminCategory = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(medAdminLogData.get("selectedAdminCategory").toString())).or("-1"));
	    String adminNotes = Optional.fromNullable(Strings.emptyToNull(medAdminLogData.get("adminNotes").toString())).or("");
		MedsAdminLog newAdminLog = new MedsAdminLog();
		newAdminLog.setMedsAdminLogPatientId(patientId);
		if(logId!= -1)
			newAdminLog.setMedsAdminLogRouteId(logId);
		else
			newAdminLog.setMedsAdminLogRouteId(-1);
		newAdminLog.setMedsAdminLogPlanId(planId);
		newAdminLog.setMedsAdminLogActualPlanDate(actualplanDate);
		newAdminLog.setMedsAdminLogAdministeredBy(administeredBy);
		newAdminLog.setMedsAdminLogAdministrationDate(medAdminLogRepository.findCurrentTimeStamp());
		newAdminLog.setMedsAdminLogModifiedBy(administeredBy);
		newAdminLog.setMedsAdminLogModifiedDate(medAdminLogRepository.findCurrentTimeStamp());
		newAdminLog.setMedsAdminLogIsDeleted(false);
		newAdminLog.setMedsAdminLogAdminCategory(medAdminCategory);
		newAdminLog.setMedsAdminLogAdminNotes(adminNotes);
		MedsAdminLog savedLog = medAdminLogRepository.saveAndFlush(newAdminLog);
		if(logId==-1) {
			int logRouteId = savedLog.getMedsAdminLogId();
			savedLog.setMedsAdminLogRouteId(logRouteId);
			medAdminLogRepository.saveAndFlush(savedLog);
		}	
	}


	/**
	 * To get the log history of a medication plan (Currently this is for medication log used by Behavioural health practices).
	 */
	@Override
	public List<MedsAdminLog> getMedicationAdminLogHistory(int planId) {
		List<MedsAdminLog> administeredlist=medAdminLogRepository.findAll(PrescripitonSpecification.getMedAdministrationLogHistoryData(planId));
		return administeredlist;
	}


	/**
	 * To delete the administered medication log (Currently this is for medication log used by Behavioural health practices).
	 */
	@Override
	public void deleteMedicationAdminLog(int deletedBy,int logId) {
		MedsAdminLog adminLog = medAdminLogRepository.findOne(logId);
		adminLog.setMedsAdminLogIsDeleted(true);
		adminLog.setMedsAdminLogDeletedOn(medAdminLogRepository.findCurrentTimeStamp());
		adminLog.setMedsAdminLogDeletedBy(deletedBy);
		adminLog.setMedsAdminLogModifiedBy(deletedBy);
		adminLog.setMedsAdminLogModifiedDate(medAdminLogRepository.findCurrentTimeStamp());
		medAdminLogRepository.saveAndFlush(adminLog);
	}

	/**
	 * To get the particular log history (Currently this is for medication log used by Behavioural health practices)
	 */
	@Override
	public List getMedicationAdminLog(int logId) {
		List<MedsAdminLog> administeredlist=medAdminLogRepository.findAll(PrescripitonSpecification.getMedAdministrationLogData(logId));
		return administeredlist;
	}



	/**
	 * To get the patient's active medications between two dates
	 */
	@Override
	public Map<String, Object> getOneMonthActiveMeds(Integer patientid,
			Integer whichMonth,Integer year) {
		Calendar startDate = Calendar.getInstance();
		startDate.set(year, whichMonth, 1);
		int endDay = startDate.getActualMaximum(Calendar.DAY_OF_MONTH); 
		if(Calendar.getInstance().get(Calendar.MONTH)==whichMonth)
			endDay = Calendar.getInstance().get(Calendar.DATE);
		Calendar endDate = Calendar.getInstance();
		endDate.set(year, whichMonth, endDay);
		List<CurrentMedication> currentmedlist=currentMedicationRepository.findAll(PrescripitonSpecification.getactivemedwithclass(patientid,startDate.getTime(),endDate.getTime()));
		List<Prescription> prescmedlist=prescriptionRepository.findAll(PrescripitonSpecification.getactivemedwithclasspresc(patientid,startDate.getTime(),endDate.getTime()));
		for(CurrentMedication obj:currentmedlist) {
			obj.setMedsAdminPlan(medAdminPlanRepository.findAll(PrescripitonSpecification.getMedsPlanIds(obj.getCurrentMedicationId())));
		}
		for(Prescription obj:prescmedlist) {
			obj.setMedsAdminPlan(medAdminPlanRepository.findAll(PrescripitonSpecification.getMedsPlanIds(obj.getDocPrescId())));
		}
		Map<String, Object> mapobject=new HashMap<String,Object>();
		mapobject.put("currentmed", currentmedlist);
		mapobject.put("prescmed", prescmedlist);
		return mapobject;
	}

	/**
	 * To edit the medication plan (Currently this is for medication log used by Behavioural health practices)
	 */
	@Override
	public void editMedicationAdminPlan(String dataToSave) throws JSONException {
	       JSONObject medPlanData = new JSONObject(dataToSave);
	       int patientId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(medPlanData.get("patientId").toString())).or("-1"));
	       int prescId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(medPlanData.get("prescId").toString())).or("-1"));
	       int planId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(medPlanData.get("planId").toString())).or("-1"));
	       boolean isPlanDeleted = Boolean.parseBoolean(Optional.fromNullable(Strings.emptyToNull(medPlanData.get("isPlanDeleted").toString())).or("false"));
	       int userId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(medPlanData.get("loginId").toString())).or("-1"));
	       String planTime =  Optional.fromNullable(Strings.emptyToNull(medPlanData.get("planTimeList").toString())).or("");
	       String planTimeList[] = null;
	       if(isPlanDeleted) {
	    	   MedsAdminPlan newPlan = medAdminPlanRepository.findOne(planId);
	    	   newPlan.setMedsAdminPlanModifiedOn(medAdminLogRepository.findCurrentTimeStamp());
	    	   newPlan.setMedsAdminPlanModifiedBy(userId);
	    	   newPlan.setMedsAdminPlanIsActive(false);
	    	   newPlan.setMedsAdminPlanDeletedBy(userId);
	    	   newPlan.setMedsAdminPlanDeletedOn(medAdminLogRepository.findCurrentTimeStamp());
	    	   medAdminPlanRepository.saveAndFlush(newPlan);
		   }
			try {
				if(planTime!=null&&planTime!="") {
					planTimeList = planTime.split(",");
				}
			} catch (Exception e) {
				System.out.println("Exception in plan time set up \n"+e);
			}
			if(planTimeList.length>0) {
				for(int i=0;i<planTimeList.length;i++) {
					String planTimeTemp = planTimeList[i].replace("[", "").replace("]", "").trim();
				if (!planTimeTemp.equalsIgnoreCase("")) {
					MedsAdminPlan newPlan = new MedsAdminPlan();
					newPlan.setMedsAdminPlanMedicationId(prescId);
					newPlan.setMedsAdminPlanPatientId(patientId);
					newPlan.setMedsAdminPlanTime(planTimeTemp);
					newPlan.setMedsAdminPlanIsActive(true);
					newPlan.setMedsAdminPlanSavedTime(medAdminLogRepository.findCurrentTimeStamp());
					newPlan.setMedsAdminPlanModifiedOn(medAdminLogRepository.findCurrentTimeStamp());
					newPlan.setMedsAdminPlanModifiedBy(userId);
					medAdminPlanRepository.saveAndFlush(newPlan);
				}
			}
		}
	}
	
	/**
	 * To get the medical supplies for the particular patient
	 * @param patientid
	 * throws {@link Exception}
	 */
	@Override
	public PrescriptionBean getactivemedicalsupplies(Integer patientid) {
		List<CurrentMedication> currentmedlist=currentMedicationRepository.findAll(PrescripitonSpecification.getactivemedicalsupplieswithclass(patientid,null,null));
		List<Prescription> prescmedlist=prescriptionRepository.findAll(PrescripitonSpecification.getactivemedicalsupplieswithclasspresc(patientid,null,null));
		PrescriptionBean beanobj=new PrescriptionBean(prescmedlist, currentmedlist);
		/*	Map<String, Object> mapobject=new HashMap<String,Object>();
		mapobject.put("currentmed", currentmedlist);
		mapobject.put("prescmed", prescmedlist);*/
		return beanobj;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.glenwood.glaceemr.server.application.services.chart.prescription.PrescriptionService#getfrequencylist(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DrugSchedule> getfrequencylist(String brandname,String mode) {
		// TODO Auto-generated method stub
		List<DrugSchedule> schedulelist=drugScheduleRepository.findAll(PrescripitonSpecification.getfrequencylist(brandname,mode));
		return schedulelist;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.glenwood.glaceemr.server.application.services.chart.prescription.PrescriptionService#getfrequencylistall(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DrugSchedule> getfrequencylistall(String brandname,String mode) {
		// TODO Auto-generated method stub
		List<DrugSchedule> schedulelist=drugScheduleRepository.findAll(PrescripitonSpecification.getfrequencylist(brandname,mode));
		return schedulelist;
	}

	/*
	 * (non-Javadoc)
	 * @see com.glenwood.glaceemr.server.application.services.chart.prescription.PrescriptionService#gettake(java.lang.String)
	 */
	@Override
	public List<IntakeBean> gettake(String brandname) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<Prescription> root = cq.from(Prescription.class);
		cq.multiselect(builder.construct(IntakeBean.class, root.get(Prescription_.docPrescIntake),
				root.get(Prescription_.docPrescIntake)));
		cq.where(builder.like((builder.lower(root.get(Prescription_.rxname))),getLikePattern(brandname)));
		cq.orderBy(builder.asc(root.get(Prescription_.docPrescIntake)));
		cq.distinct(true);
		List<Object> dd=em.createQuery(cq).getResultList();
		List<IntakeBean> bean1 = new ArrayList<IntakeBean>();
		for(int i=0;i<dd.size();i++){
		IntakeBean takebean=(IntakeBean)dd.get(i);
		String name=takebean.getName();
		String value=takebean.getValue();
		takebean.setName(name);
		takebean.setValue(value);
		bean1.add(takebean);
		}
		
		return bean1;
	}
	
	
	/**
	 * Retunrs the formatted the pattern
	 * @param searchTerm
	 * @return
	 */
	private static String getLikePattern(final String searchTerm) {
		StringBuilder pattern = new StringBuilder();
		pattern.append(searchTerm.toLowerCase());
		pattern.append("%");
		return pattern.toString();
	}
	
	/**
	 * To modify the administered medication log notes
	 */
	@Override
	public void updateMedicationAdminLogNotes(int modifiedBy, int logId,String notes) {
		MedsAdminLog adminLog = medAdminLogRepository.findOne(logId);
		adminLog.setMedsAdminLogModifiedDate(medAdminLogRepository.findCurrentTimeStamp());
		adminLog.setMedsAdminLogModifiedBy(modifiedBy);
		adminLog.setMedsAdminLogAdminNotes(notes);
		medAdminLogRepository.saveAndFlush(adminLog);
	}

	/**
	 * To get the medication administration plan shortcuts
	 */
	@Override
	public List<MedsAdminPlanShortcut> getMedAdminPlanShortcuts() {
		List<MedsAdminPlanShortcut> shortcutsList = medAdministrationPlanShortcutRepository.findAll();
		return shortcutsList;
	}
	
}
