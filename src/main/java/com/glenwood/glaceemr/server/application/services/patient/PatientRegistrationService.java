package com.glenwood.glaceemr.server.application.services.patient;

import org.springframework.stereotype.Component;

import com.glenwood.glaceemr.server.application.models.PatientRegistration;

@Component
public interface PatientRegistrationService {

	public PatientRegistration findByPatientId(int patientId);
	
}
