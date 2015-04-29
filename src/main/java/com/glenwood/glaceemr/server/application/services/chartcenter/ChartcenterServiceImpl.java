package com.glenwood.glaceemr.server.application.services.chartcenter;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.specifications.PatientRegistrationSpecification;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

@Service
public class ChartcenterServiceImpl implements ChartcenterService {

	@Autowired
	PatientRegistrationRepository patientRegistrationRepository;
	
	String firstName="";
	String lastName="";
	int currentPage = 0;
	int pageSize = 0;
	int searchMode = 0;
	int searchType = 0;
	@Override
	public 	Page<PatientRegistration>  getPatientSearchResult(String toSearchData,String searchTypeParam)throws Exception {

		JSONObject data = new JSONObject(toSearchData);
		lastName=Optional.fromNullable(Strings.emptyToNull(data.get("txtLName").toString())).or("");
		firstName=Optional.fromNullable(Strings.emptyToNull(data.get("txtFName").toString())).or("");
		currentPage =Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(data.get("currentPage").toString())).or("20"));
		pageSize =Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(data.get("pageSize").toString())).or("20"));
		searchMode=Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(data.get("searchMode").toString())).or("1"));
		int keyDownFlag= Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(data.get("keyDownFlag").toString())).or("1"));
		searchType=Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(searchTypeParam)).or("1"));
		int offset = pageSize * ( currentPage - 1 );
		Page<PatientRegistration> patientList = null;
		
		/* query choosing based on searchmode and lastname/firstname */
		if (searchMode == 0) {
			if (lastName.length() > 0) {
				 patientList =patientRegistrationRepository.findAll(Specifications.where(PatientRegistrationSpecification.PatientByLastName(lastName)).and(PatientRegistrationSpecification.PatientIsactive(true)),new PageRequest(offset, pageSize));
				
			}
			if(firstName.length() > 0){
				 patientList =patientRegistrationRepository.findAll(Specifications.where(PatientRegistrationSpecification.PatientByFirstName(firstName)).and(PatientRegistrationSpecification.PatientIsactive(true)),new PageRequest(offset, pageSize));
			}
			
		}
		else {
			 patientList =patientRegistrationRepository.findAll(Specifications.where(PatientRegistrationSpecification.PatientByAccountNumber(lastName)).and(PatientRegistrationSpecification.PatientIsactive(true)),new PageRequest(offset, pageSize));

		}
		
		return patientList;
	}

}
