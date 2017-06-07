package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.glenwood.glaceemr.server.application.Bean.EPMeasureBean;
import com.glenwood.glaceemr.server.application.Bean.MIPSPatientInformation;
import com.glenwood.glaceemr.server.application.Bean.MIPSPerformanceBean;
import com.glenwood.glaceemr.server.application.Bean.MUDashboardBean;
import com.glenwood.glaceemr.server.application.Bean.MUPerformanceBean;
import com.glenwood.glaceemr.server.application.Bean.MeasureStatus;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Request;
import com.glenwood.glaceemr.server.application.Bean.pqrs.PqrsMeasureBean;
import com.glenwood.glaceemr.server.application.Bean.pqrs.QualityMeasureBean;

public interface MeasureCalculationService {

	/**
	 * 
	 * Function to save provider reporting details and selected MIPS measures
	 * 
	 * @param providerId
	 * @param patientId
	 * @param measureStatus
	 * @param flag
	 */
	
	void saveMeasureDetails(int providerId, int patientId, List<MeasureStatus> measureStatus, boolean flag);

	/**
	 * Function to get Request Object from patient details to validate QDM data
	 * 
	 * @param isIndividual
	 * @param patientID
	 * @param providerId
	 * @param codeListForQDM
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	
	Request getQDMRequestObject(Boolean isIndividual,int patientID, int providerId, HashMap<String, HashMap<String, String>> codeListForQDM, Date startDate, Date endDate);

	/**
	 * 
	 * Function to get EP Measures details for the given provider and patient in current year
	 * 
	 * @param accountId
	 * @param isGroup
	 * @param patientID
	 * @param providerId
	 * @param startDate
	 * @param endDate
	 * @param repYear
	 * @return
	 */
	
	List<EPMeasureBean> getEPMeasuresResponseObject(String accountId,Boolean isGroup,int patientID, int providerId, Date startDate, Date endDate, int repYear, boolean flag);
	
	/**
	 * Function to check whether the MACRA configuration is individual or group
	 * 
	 * @param year
	 * @return
	 */
	
	Boolean checkGroupOrIndividual(int year);
	
	/**
	 * Function to get MIPS Performance details based on the providerId
	 * 
	 * @param providerId
	 * @param accountId
	 * @param configuredMeasures
	 * @param isACIReport
	 * @return
	 */
	
	List<MIPSPerformanceBean> getMeasureRateReport(int providerId, String accountId, String configuredMeasures,boolean isACIReport, boolean isOrderBy);
	
	/**
	 * Function to return provider based performance for analytics dashboard 
	 * 
	 * @param providerId
	 * @param accountId
	 * @param configuredMeasures
	 * @return
	 */
	
	List<MUPerformanceBean> getAnalyticsPerformanceReport(int providerId, String accountId, String configuredMeasures);
	
	/**
	 * Function to get MIPS Performance details based on NPI value
	 * 
	 * @param providerId
	 * @param accountId
	 * @param configuredMeasures
	 * @param isACIReport
	 * @return
	 */
	
	List<MIPSPerformanceBean> getMeasureRateReportByNPI(int providerId, String accountId, String configuredMeasures,boolean isACIReport, boolean isOrderBy);
	
	/**
	 * Function to get provider based performance details and store it in macra_measures_rate table
	 * 
	 * @param providerId
	 * @param measureId
	 * @param configuredMeasures
	 * @param accountId
	 * @return
	 */
	
	List<MIPSPerformanceBean> getPerformanceCount(int providerId, String measureId, String configuredMeasures, String accountId);
	
	/**
	 * Function to get MIPS Performance of group of providers falling under same TIN/SSN
	 * 
	 * @param tinValue
	 * @param configuredMeasures
	 * @param accountId
	 * @param isACIReport
	 * @return
	 */
	
	List<MIPSPerformanceBean> getGroupPerformanceCount(String tinValue, String configuredMeasures, String accountId,boolean isACIReport, boolean isOrderBy);

	/**
	 * 
	 *  Function to return filter options based on gender, race, ethnicity.
	 *  
	 */
	
	HashMap<String, Object> generateFilterContents();

	/**
	 * Function to get patient details based on given measure and selected performance indicator
	 * 
	 * @param patientId
	 * @param measureId
	 * @param criteria
	 * @param provider
	 * @param empTin
	 * @param mode
	 * @param isNotMet
	 * @return
	 */
	
	List<MIPSPatientInformation> getPatient(String patientId, String measureId, int criteria,Integer provider, String empTin, int mode, boolean isNotMet);

	/**
	 * Function to get resultant PQRSResponse bean for given patient and provider after getting validated 
	 * 
	 * @param patientID
	 * @param providerId
	 * @param startDate
	 * @param endDate
	 * @param codeListForQDM
	 * @return
	 */
	
	List<PqrsMeasureBean> getPqrsResponseObject(int patientID, int providerId, Date startDate, Date endDate,HashMap<String, String> codeListForQDM);

	/**
	 * Function to receive measure related information to be displayed in MIPS Flowsheet page
	 * 
	 * @param userId
	 * @param codeListForQDM
	 * @return
	 */
	
	List<QualityMeasureBean> getQualityMeasureResponseObject(int userId, HashMap<String, String> codeListForQDM);

	/**
	 * Function to get selected provider's MU Dashboard details
	 * 
	 * @param providerId
	 * @param accountId
	 * @param tinValue
	 * @param configuredMeasures
	 * @param aciMeasures
	 * @param byNpi
	 * @param providerDashboard
	 */
	
	void getDashBoardDetails(int providerId,  String accountId, String tinValue, String configuredMeasures, String aciMeasures, boolean byNpi, MUDashboardBean providerDashboard);
	
}