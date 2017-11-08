package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.util.HashMap;
import java.util.List;

import com.glenwood.glaceemr.server.application.Bean.DiagnosisList;
import com.glenwood.glaceemr.server.application.Bean.MIPSPatientInformation;
import com.glenwood.glaceemr.server.application.Bean.MacraProviderQDM;
import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.MacraProviderConfiguration;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresProviderMapping;


public interface QPPConfigurationService {
	
	void saveConfDetails(Integer programYear, Integer type, Integer providerId,
			java.util.Date startDate, java.util.Date endDate, Integer submissionMtd,short reportType)throws Exception;
	
	/**
	 * Function to get employee profile empid based on provider
	 * 
	 * @param provider
	 * @return
	 * @throws Exception
	 */
	
	Integer getProviderId(String provider)throws Exception;	
	
	/**
	 * Function to get MACRA configuration details for selected provider   
	 *  
	 * @param providerId
	 * @return
	 * @throws Exception
	 */
	
	List<MacraProviderConfiguration> getProviderInfo(Integer providerId)throws Exception;
	
	/**
	 * Function to get configured measures info for employee
	 * based on TIN and reporting year
	 * 
	 * @param empTin
	 * @param reportingYear
	 * @return
	 */
	
	String getCompleteTinInfo(String empTin, int reportingYear);
	
	/**
	 * Function to get all measures configured for the selected provider
	 * 
	 * @param providerId
	 * @return
	 * @throws Exception
	 */
	
	List<QualityMeasuresProviderMapping> getMeasureIds(Integer providerId)throws Exception;
	
	/**
	 * Function to save measure configuration for the selected provider 
	 * for the chosen reporting year 
	 * 
	 * @param measureIds
	 * @param providerId
	 * @param reportingYear
	 */
	
	void addMeasuresToProvider(String measureIds,Integer providerId, Integer reportingYear);
	
	/**
	 * Function to get Lab Information Details of the given patientId
	 * 
	 * @param patientId
	 * @return
	 * @throws Exception
	 */
	
	List<Chart>getLabDetails(Integer patientId)throws Exception;
	
	/**
	 * Function to get MACRA configuration info for the selected provider
	 * 
	 * @param providerId
	 * @return
	 * @throws Exception
	 */
	
	List<MacraProviderQDM> getCompleteProviderInfo(Integer providerId)throws Exception;
	
	/**
	 * Function to get MACRA configuration for all providers in given reporting year
	 * 
	 * @param providerId
	 * @return
	 * @throws Exception
	 */
	
	List<MacraProviderQDM> getProviderReportingInfo(Integer reportingYear);
	
	/**
	 * 
	 * Function to draw details required for filtering patient details in MIPS Performance Report
	 * 
	 * @return
	 * @throws Exception
	 */
	
	HashMap<String,Object> getFilterDetails()throws Exception;
	
	/**
	 * 
	 * Function to get filtered patient details from the selected filter criteria
	 * 
	 * @param patientId
	 * @param ageFrom
	 * @param ageTo
	 * @param ageCriteria
	 * @param raceCode
	 * @param ethnicityCode
	 * @param gender
	 * @param insCompanyId
	 * @param currMeasureId
	 * @param dxCodes
	 * @return
	 * @throws Exception
	 */
	
	List<MIPSPatientInformation> getFilteredDetails(String patientId,Integer ageFrom,Integer ageTo,Integer ageCriteria,String raceCode,String ethnicityCode,String gender,Integer insCompanyId,String currMeasureId,String dxCodes,int posId,int insId) throws Exception;
	
	/**
	 * Function to get diagnosis list for filtering patient details in MIPS Performance Report
	 * 
	 * @param measureId
	 * @param sharedPath
	 * @return
	 * @throws Exception
	 */
	
	DiagnosisList getDXList(String measureId,String sharedPath)throws Exception;
	
	/**
	 * Function to get patient list based on selected diagnosis codes
	 * 
	 * @param patientId
	 * @param dxCodes
	 * @return
	 * @throws Exception
	 */
	
	List<MIPSPatientInformation> getPatientBasedOnDX(String patientId,String dxCodes)throws Exception;
	
}