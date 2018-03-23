package com.glenwood.glaceemr.server.application.services.commonPicker;

import org.json.JSONObject;


public interface CommonPickerService {

	JSONObject getReasonData(String searchCode, String offset, String limit,
			String group, String selectedVal, String userGroupsRequired,
			String dataRequired);

	JSONObject getAddNewReasonData();

	void saveReasonData(String categoryName, String comboId,
			String isActiveVal, String visitReason, String visitTypeId);

	JSONObject getReferralData(String searchCode, String lastName,
			String firstName, String npi, String address, String fax,
			String phone, String directEmail, String specialization,
			String group, String selectedValue, String userGroupsRequired,
			String dataRequired, String offset, String limit) throws Exception;

	void saveReferralData(String fromSave, String comboId,
			String isActiveVal, String midName, String address, String city,
			String state, String zip, String phoneno, String faxno,
			String firstname, String lastname, String uid, String mid,
			String bid, String credential, String special, String referraltype,
			String refDocname, String prefix, String taxonomy,
			String rdpracname, String txtTitle, String directemail, String emailid);

	JSONObject getAddtionalRefData(String mode, String docId);

	JSONObject getZipCodesData(String city, String state, String zip,
			String mode, String limit, String offset);

	JSONObject getPatientPickerData(String pageSize, String searchType,
			String searchMode, String currentPage, String firstName,
			String lastName, String dob, String ssn, String accountNo,
			String patientSearchType);

	void saveManageGroupData(String groupname, String defaultvalue,
			String activevalue, String newgroup, String updateindex,
			String groupIdToUpdate);

	
}
