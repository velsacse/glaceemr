package com.glenwood.glaceemr.server.application.services.chart.prescription;

import java.util.List;
import java.util.Map;

import org.json.JSONException; 

 
/**
 * @author selvakumar
 *
 */
public interface PrescriptionService {

	/**
	 * To get the prescribed medications by the doctor
	 * @param patientid
	 * @return the list of active medications of a patient
	 */
	Map<String, Object> getactivemedwithclass(Integer patientid);

	/**
	 * To get the patient's active medications between two dates
	 * @param patientid
	 * @param whichMonth
	 * @param year
	 * @return the list of active medications of a patient between two dates.
	 */
	Map<String, Object> getOneMonthActiveMeds(Integer patientid,Integer whichMonth,Integer year);
	
	/**
	 * To save the medications plan (Currently this is for medication log used by Behavioural health practices)
	 * @param dataToSave
	 * @throws JSONException
	 */
	void saveMedicationAdminPlan(String dataToSave) throws JSONException;
	
	/**
	 * To save the administration details of a patient. (Currently this is for medication log used by Behavioural health practices)
	 * @param dataToSave
	 * @throws Exception
	 */
	void saveMedicationAdminLog(String dataToSave) throws Exception;
	
	/**
	 * To get the log history of a medication plan (Currently this is for medication log used by Behavioural health practices).
	 * @param planId
	 * @return a medication admin log history for a specific plan 
	 */
	List getMedicationAdminLogHistory(int planId);
	
	/**
	 * To get the singly log history (Currently this is for medication log used by Behavioural health practices).
	 * @param logId
	 * @return the singly log history
	 */
	List getMedicationAdminLog(int logId);
	
	/**
	 * To delete the administered medication log (Currently this is for medication log used by Behavioural health practices).
	 * @param planId
	 * @param logId
	 */
	void deleteMedicationAdminLog(int planId,int logId);

	/**
	 * To edit the medications plan (Currently this is for medication log used by Behavioural health practices)
	 * @param dataToSave
	 * @throws JSONException
	 */
	void editMedicationAdminPlan(String dataToSave) throws JSONException;
	
	
	
	/*To get the medical supplies data
	 * @param patientid
	 * throws Exception
	 * 
	 */
	PrescriptionBean getactivemedicalsupplies(Integer patientid);


	
}
