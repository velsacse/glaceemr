package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.util.List;

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
	void addMeasuresToProvider(String measureIds,Integer providerId);
	List<Chart>getLabDetails(Integer patientId)throws Exception;
//	List<ReferralQDM>getReferrals(Integer patientId)throws Exception;
//	List<ImmunizationBean>getImmuDetails(Integer patientId)throws Exception;
	List<MacraProviderQDM> getCompleteProviderInfo(Integer providerId)throws Exception;
}
