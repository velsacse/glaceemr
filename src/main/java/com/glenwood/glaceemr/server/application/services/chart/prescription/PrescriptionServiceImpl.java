package com.glenwood.glaceemr.server.application.services.chart.prescription;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.models.CurrentMedication;
import com.glenwood.glaceemr.server.application.models.MedsAdminLog;
import com.glenwood.glaceemr.server.application.models.MedsAdminPlan;
import com.glenwood.glaceemr.server.application.models.Prescription;
import com.glenwood.glaceemr.server.application.repositories.CurrentMedicationRepository;
import com.glenwood.glaceemr.server.application.repositories.MedAdministrationLogRepository;
import com.glenwood.glaceemr.server.application.repositories.MedAdministrationPlanRepository;
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
	MedAdministrationPlanRepository medAdminPlanRepository;
	
	@Autowired
	MedAdministrationLogRepository medAdminLogRepository;
	
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

}

