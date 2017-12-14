package com.glenwood.glaceemr.server.application.services.patient;

import javax.annotation.Resource;

import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.specifications.PatientRegistrationSpecification;

public class PatientRegistrationServiceImpl implements  PatientRegistrationService{

	@Resource
	PatientRegistrationRepository patientRegistrationRepository;
	
	@SuppressWarnings("unused")
	@Override
	public PatientRegistration findByPatientId(int patientId) {
		// TODO Auto-generated method stub
		
		PatientRegistration patientRegistration = patientRegistrationRepository.findOne(PatientRegistrationSpecification.byPatientId(patientId));
				
		if(patientRegistration != null){
			return patientRegistration;
		}
		else
			return null;
	}

}
