package com.glenwood.glaceemr.server.application.services.chartcenter;

import org.springframework.data.domain.Page;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;

public interface ChartcenterService {
	
	/**
	 * @param toSearchData:json object with lastname,firstname,pagesize,searchmode
	 * @return
	 * @throws Exception
	 */
	public Page<PatientRegistration>  getPatientSearchResult(String toSearchData,String searchTypeParam)throws Exception;
	
}
