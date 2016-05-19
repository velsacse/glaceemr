package com.glenwood.glaceemr.server.application.services.chart.assessment;

import java.util.List;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import com.glenwood.glaceemr.server.application.models.H611;
import com.glenwood.glaceemr.server.application.models.Icdm;


public interface AssessmentSummaryService {

	/**
	 * Method to retrieve current visit data
	 * @param patienId
	 * @param encounterId
	 * @return
	 * @throws Exception
	 */
	List<H611> getCurrentDiagnosis(Integer patienId,Integer encounterId) throws Exception;
	
	/**
	 * Method to save current visit data
	 * @param assessListobj
	 * @return
	 * @throws Exception
	 */
	Boolean saveDiagnosis(JSONObject assessListobj) throws Exception;
	
	/**
	 * Method to fetch data for edit page
	 * @param patientId
	 * @param encounterId
	 * @param dxCode
	 * @param problemId
	 * @return
	 */
	List<H611> getEditData(Integer patientId, Integer encounterId,
			String dxCode, Integer problemId);
	
}
