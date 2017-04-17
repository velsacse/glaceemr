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
			java.util.Date startDate, java.util.Date endDate, Integer submissionMtd)throws Exception;
	
	Integer getProviderId(String provider)throws Exception;	
	List<MacraProviderConfiguration> getProviderInfo(Integer providerId)throws Exception;
	List<QualityMeasuresProviderMapping> getMeasureIds(Integer providerId)throws Exception;
	void addMeasuresToProvider(String measureIds,Integer providerId,Integer prgmYear);
	List<Chart>getLabDetails(Integer patientId)throws Exception;
	List<MacraProviderQDM> getCompleteProviderInfo(Integer providerId)throws Exception;
	List<MacraProviderQDM> getProviderReportingInfo(Integer reportingYear);
	HashMap<String,Object> getFilterDetails()throws Exception;
	List<MIPSPatientInformation> getFilteredDetails(String patientId,Integer ageFrom,Integer ageTo,Integer ageCriteria,String raceCode,String ethnicityCode,String gender,Integer insCompanyId,String currMeasureId,String dxCodes) throws Exception;
	DiagnosisList getDXList(String measureId,String sharedPath)throws Exception;
	List<MIPSPatientInformation> getPatientBasedOnDX(String patientId,String dxCodes)throws Exception;
	
}
