package com.glenwood.glaceemr.server.application.services.portal.portalForms;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.ClinicalIntakeFormBean;
import com.glenwood.glaceemr.server.application.models.FileDetails;

public class PortalClinicalIntakeAndConsentFormsListBean {

	List<ClinicalIntakeFormBean> clinicalIntakeFormsList;
	
	List<FileDetails> consentFormsList;

	public List<ClinicalIntakeFormBean> getClinicalIntakeFormsList() {
		return clinicalIntakeFormsList;
	}

	public void setClinicalIntakeFormsList(
			List<ClinicalIntakeFormBean> clinicalIntakeFormsList) {
		this.clinicalIntakeFormsList = clinicalIntakeFormsList;
	}

	public List<FileDetails> getConsentFormsList() {
		return consentFormsList;
	}

	public void setConsentFormsList(List<FileDetails> consentFormsList) {
		this.consentFormsList = consentFormsList;
	}
	
}
