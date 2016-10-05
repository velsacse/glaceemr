package com.glenwood.glaceemr.server.application.services.portal.portalSettings;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.Billinglookup;

public class PatientProfileSettingsFields {

	List<Billinglookup> availabelanguageOptions;
	List<Billinglookup> availabeRaceOptions;
	List<Billinglookup> availabeEthnicityOptions;
	List<Billinglookup> availabeReminderTypeOptions;
	List<Billinglookup> availabeMaritalStatusOptions;
	List<Billinglookup> availabeGenderTitleOptions;
	List<Billinglookup> paymentMethods;
	List<Billinglookup> payerTypes;
	
	
	public List<Billinglookup> getAvailabelanguageOptions() {
		return availabelanguageOptions;
	}
	public void setAvailabelanguageOptions(
			List<Billinglookup> availabelanguageOptions) {
		this.availabelanguageOptions = availabelanguageOptions;
	}
	public List<Billinglookup> getAvailabeRaceOptions() {
		return availabeRaceOptions;
	}
	public void setAvailabeRaceOptions(List<Billinglookup> availabeRaceOptions) {
		this.availabeRaceOptions = availabeRaceOptions;
	}
	public List<Billinglookup> getAvailabeEthnicityOptions() {
		return availabeEthnicityOptions;
	}
	public void setAvailabeEthnicityOptions(
			List<Billinglookup> availabeEthnicityOptions) {
		this.availabeEthnicityOptions = availabeEthnicityOptions;
	}
	public List<Billinglookup> getAvailabeReminderTypeOptions() {
		return availabeReminderTypeOptions;
	}
	public void setAvailabeReminderTypeOptions(
			List<Billinglookup> availabeReminderTypeOptions) {
		this.availabeReminderTypeOptions = availabeReminderTypeOptions;
	}
	public List<Billinglookup> getAvailabeMaritalStatusOptions() {
		return availabeMaritalStatusOptions;
	}
	public void setAvailabeMaritalStatusOptions(
			List<Billinglookup> availabeMaritalStatusOptions) {
		this.availabeMaritalStatusOptions = availabeMaritalStatusOptions;
	}
	public List<Billinglookup> getAvailabeGenderTitleOptions() {
		return availabeGenderTitleOptions;
	}
	public void setAvailabeGenderTitleOptions(
			List<Billinglookup> availabeGenderTitleOptions) {
		this.availabeGenderTitleOptions = availabeGenderTitleOptions;
	}
	public List<Billinglookup> getPaymentMethods() {
		return paymentMethods;
	}
	public void setPaymentMethods(List<Billinglookup> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}
	public List<Billinglookup> getPayerTypes() {
		return payerTypes;
	}
	public void setPayerTypes(List<Billinglookup> payerTypes) {
		this.payerTypes = payerTypes;
	}
	
}
