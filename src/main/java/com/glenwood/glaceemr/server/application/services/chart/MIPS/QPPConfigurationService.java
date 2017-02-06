package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.MacraProviderConfiguration;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresProviderMapping;


public interface QPPConfigurationService {
	
	void saveConfDetails(Integer programYear, Integer type, Integer providerId,
			java.util.Date startDate, java.util.Date endDate, Integer submissionMtd)throws Exception;
	
	List<MacraProviderConfiguration> getGroupConfData() throws Exception;	
	Integer getProviderId(String provider)throws Exception;	
	List<MacraProviderConfiguration> getProviderInfo(Integer providerId)throws Exception;
	List<QualityMeasuresProviderMapping> getGroupMeasureIds()throws Exception;
	List<QualityMeasuresProviderMapping> getIndividualMeasureIds(Integer providerId)throws Exception;
	void addMeasuresToProvider(String measureIds,Integer providerId);
	
}