package com.glenwood.glaceemr.server.application.services.portal.portalMedicalSummary;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.PatientPortalUser;
import com.glenwood.glaceemr.server.application.models.PortalConfigurationBean;
import com.glenwood.glaceemr.server.application.models.PatientAllergies;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PortalMedicalSummaryBean;
import com.glenwood.glaceemr.server.application.models.PortalMedicationBean;
import com.glenwood.glaceemr.server.application.models.PortalPlanOfCareBean;
import com.glenwood.glaceemr.server.application.models.PortalVitalsBean;
import com.glenwood.glaceemr.server.application.models.ProblemList;

public interface PortalMedicalSummaryService {
	
	/**
	 * @return SessionMap
	 * @throws JsonProcessingException 
	 */
	public PortalConfigurationBean getSessionMap(String username) throws JsonProcessingException;
	
	/**
	 * Chart Id list of a patient.
	 * @param patientId		: id of the required patient.
	 * @return List of problems of a patient.
	 */
	public List<Chart> getPatientChartId(int patientId);
	
	/**
	 * Personal details of a patient.
	 * @param patientId		: id of the required patient.
	 * @return List of personal details  of a patient.
	 */
	public PatientRegistration getPatientPersonalDetails(int patientId);
	
	/**
	 * Problems list of a patient.
	 * @param patientId		: id of the required patient.
	 * @return List of problems of a patient.
	 */
	public List<ProblemList> getPatientProblemList(int patientId, String problemType, int pageOffset, int pageIndex);
	
	/**
	 * Allergies list of a patient.
	 * @param cahrtId		: chart id of the required patient.
	 * @return List of problems of a patient.
	 */
	public List<PatientAllergies> getPatientAllergies(int chartId, int pageOffset, int pageIndex);
	
	/**
	 * Profile details of a patient.
	 * @param patientId		: id of the required patient.
	 * @param chartId		: id of the required patient.
	 * @return list of plan  of care of a patient.
	 */
	public List<PortalPlanOfCareBean> getPlanOfCare(int patientId, int chartId, int pageOffset, int pageIndex);
	
	/**
	 * Profile details of a patient.
	 * @param patientId		: id of the required patient.
	 * @param chartId		: id of the required patient.
	 * @return list of plan  of care details of a plan of a patient.
	 */
	public List<PatientClinicalElements> getPlanOfCareDetails(int patientId, int encounterId, int pageOffset, int pageIndex);
	
	/**
	 * details of a patient.
	 * @param username		: username of the required patient.
	 * @return list of details of a patient.
	 * @throws JsonProcessingException 
	 */
	public List<PatientPortalUser> getPatientDetailsByUsername(String username) throws JsonProcessingException;
	
	/**
	 * details of a patient.
	 * @param username		: username of the required patient.
	 * @return list of details of a patient.
	 */
	public PortalMedicalSummaryBean getPortalMedicalSummaryDetails(int patientId, int chartId);

	
	public List<PortalMedicationBean> getPatientMedication(int patientId, int chartId);
	
	public List<PortalVitalsBean> getPatientVital(int patientId, int chartId);
	
	
	public List<PatientAllergies> getPatientAllergiesByPatientIdNew(int patientId, int pageOffset, int pageIndex) throws JsonProcessingException;

	
	public void putLog(int patientId, int chartId);
	
}
