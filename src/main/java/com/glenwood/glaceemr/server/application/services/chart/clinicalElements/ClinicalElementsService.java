package com.glenwood.glaceemr.server.application.services.chart.clinicalElements;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.ClinicalElements;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions;
import com.glenwood.glaceemr.server.application.models.ClinicalTextMapping;

public interface ClinicalElementsService {
	
	public List<ClinicalElementsOptions> getClinicalElementOptions(String gwid);
	public ClinicalDataBean setVitalsClinicalData(String gwidPattern,Integer patientId,Integer encounterId,Boolean isDischargeVitals,Integer admssEpisode,short patientSex,Integer ageinDay);
	public boolean isClinicalElemActive(String gwid);
	public ClinicalTextMapping getClinicalTextMapping(String gwid);
	public ClinicalElements getClinicalElement(String gwid);
	public List<ClinicalElements> setClinicalDataBean(Integer patientId,Integer encounterId,Integer templateId,Integer tabType,String gwidPattern);
	public List<String> delPatientElementByEncID(Integer patientId,Integer encounterId, Integer tabId,Integer templateId);
	public void deleteNotesData(Integer patientId, Integer encounterId,Integer tabId, Integer templateId);
	public void insertDataForImport(Integer patientId,Integer encounterId,Integer prevEncounterId, Integer tabId, Integer templateId,List<String> mappedGwids);
	public List<ClinicalElements> setHistoryClinicalDataBean(Integer patientId,Integer encounterId,Integer templateId,Integer tabType,String gwidPattern);

}
