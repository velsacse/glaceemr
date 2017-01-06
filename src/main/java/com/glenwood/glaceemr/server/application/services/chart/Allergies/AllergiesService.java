package com.glenwood.glaceemr.server.application.services.chart.Allergies;

import java.util.List;

import org.json.JSONArray;
import com.glenwood.glaceemr.server.application.models.PatientAllergies;

public interface AllergiesService {

	List<AllergiesTypeBean> retrievingAllergyType(int gender);

	AllergyBean searchAllergy(String value, int mode, int offset,
			int limit);

	AllergyBean getReactionsOfAllergies(String criteria, int offset, int limit);

	List<PatientAllergies> getNKDAandNKACheckDetails(Integer chartId);

	void saveNewAllergies(Integer save,
			String insertParameters);

	void editAllergies(String editData);

	void reviewAllergies(String reviewData);

	String deleteAllergy(String allergyid);

	void nkdaUpdate(String nkdaData);

	void nkaUpdate(String nkaData) throws Exception;

	void nkdauncheck(String nkdaData);

	void nkauncheck(String nkaData);

	List<PatientAllergies> retrieveDataForEditAllerg(String chartId, String patId);

	JSONArray lastReviewDetails(String chartId);

	List<PatientAllergies> retrieveInActiveAllerg(String chartId);

}
