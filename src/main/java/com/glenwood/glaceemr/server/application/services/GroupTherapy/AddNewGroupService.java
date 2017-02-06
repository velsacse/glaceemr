package com.glenwood.glaceemr.server.application.services.GroupTherapy;

import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.glenwood.glaceemr.server.application.models.TherapyGroup;
import com.glenwood.glaceemr.server.application.models.TherapySession;


/**
 * 
 * @author software
 *
 */
public interface AddNewGroupService {

	/**
	 * To get the data of providers,pos,groups
	 * @return
	 */
	Map<String, Object> listDefaults();

	/**
	 * To save new Group
	 * @param data
	 * @throws JSONException
	 */
	void saveNewGroup(TherapyGroupBean data) throws JSONException;
	
	/**
	 * To list group data
	 * @param groupId
	 * @return
	 */
	List<TherapyGroup> listGroupData(String groupId);

    /**
     * To create therapy session
     * @param dataToSave
     * @return
     * @throws JSONException
     * @throws Exception
     */
	List<TherapySession> createTherapySession(String dataToSave) throws JSONException, Exception;

	/**
	 * 
	 * @param dataToSave
	 * @throws JSONException
	 */
	void addPatientToTherapyGroup(String dataToSave) throws JSONException;

	/**
	 * To get the patient data by groupid
	 * @param groupId
	 * @return
	 */
	Map<String, Object> getPatientData(String groupId);

	/**
	 * To get the therapy session log data
	 * @param dataToSave
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	List<TherapyLogBean> therapySearchLog(String dataToSave) throws JSONException, Exception;

	/**
	 * To get the patient data for therapy session details page
	 * @param dataToSave
	 * @return
	 * @throws Exception
	 */
	List<TherapyPatientsBean> getTherapyPatients(String dataToSave) throws Exception;

	/**
	 * To delete the patient data from group
	 * @param dataToDelete
	 * @throws JSONException
	 */
	void deleteTherapyPatient(String dataToDelete) throws JSONException;
	
	/**
	 * To fetch the shortcutcode 
	 * @param mode
	 * @return
	 */
	List<String> fetchShortcutCode(Integer group);
	
	/**
	 * To fetch the shortcutData
	 * @param shortcutId
	 * @return
	 */
	List<String> fetchShortcutData(String shortcutId);

	/**
	 * To save notes
	 * @param data
	 * @throws Exception
	 */
	void saveNotes(AddNoteBean data) throws Exception;
	
	/**
	 * to get the data for addNotes and EditNotes
	 * @param gwid
	 * @param patientId
	 * @param sessionId
	 * @param isPatient
	 * @return
	 */
	List<AddTherapyBean> fetchNotesData(String gwid,Integer patientId,Integer sessionId,Boolean isPatient);

	/**
	 * to get the data for addTherapeutic Intervention
	 * @param gwid
	 * @param sessionId
	 * @param isPatient
	 * @return
	 */
	List<AddTherapyBean> fetchDataforTherapy(String gwid, Integer sessionId, Boolean isPatient);

}
