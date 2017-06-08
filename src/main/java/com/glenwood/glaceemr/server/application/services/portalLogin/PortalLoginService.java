package com.glenwood.glaceemr.server.application.services.portalLogin;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import com.glenwood.glaceemr.server.application.models.PatientPortalUser;
import com.glenwood.glaceemr.server.application.models.PortalUser;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.PortalPatientRegistrationBean;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.PortalRegistrationResponse;


public interface PortalLoginService {

	PortalUser findByUserNameIgnoreCase(String userName);
	
	PatientPortalUser findByPatientId(int patientId);

	PortalRegistrationResponse checkDuplicatePatientData(String userName, String dob, String firstName, String lastName);	
	
	PortalRegistrationResponse registerNewUserForPortal(PortalPatientRegistrationBean registrationBean, String practiceId) throws IOException, JSONException;
	
	PortalRegistrationResponse registerExistingUserForPortal(PortalPatientRegistrationBean registrationBean, String practiceId) throws IOException, JSONException;
	
	PortalRegistrationResponse requestSignupCredentials(PortalPatientRegistrationBean registrationBean, String practiceId) throws IOException, JSONException;
	
	PortalRegistrationResponse activateUserAccount(int patientId);
	
}
