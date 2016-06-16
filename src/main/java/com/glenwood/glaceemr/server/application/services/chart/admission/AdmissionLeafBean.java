package com.glenwood.glaceemr.server.application.services.chart.admission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.glenwood.glaceemr.server.application.models.H496;
import com.glenwood.glaceemr.server.application.models.LeafPatient;
import com.glenwood.glaceemr.server.application.models.PatientAllergies;


public class AdmissionLeafBean {
	
	List<LeafPatient> admissionLeafBean = Collections.emptyList();
	List<H496> admissionLeafFaxBean = Collections.emptyList();
	List<PatientAllergies> allergyBean = Collections.emptyList();
	
	public AdmissionLeafBean(){
		admissionLeafBean = new ArrayList<LeafPatient>();
		admissionLeafFaxBean = new ArrayList<H496>();
		allergyBean = new ArrayList<PatientAllergies>();
	}

	
	public List<PatientAllergies> getAllergyBean() {
		return allergyBean;
	}

	public void setAllergyBean(List<PatientAllergies> allergyBean) {
		this.allergyBean = allergyBean;
	}


	public List<LeafPatient> getAdmissionLeafBean() {
		return admissionLeafBean;
	}

	public void setAdmissionLeafBean(List<LeafPatient> admissionLeafBean) {
		this.admissionLeafBean = admissionLeafBean;
	}

	public List<H496> getAdmissionLeafFaxBean() {
		return admissionLeafFaxBean;
	}

	public void setAdmissionLeafFaxBean(List<H496> admissionLeafFaxBean) {
		this.admissionLeafFaxBean = admissionLeafFaxBean;
	}
	
	
}
