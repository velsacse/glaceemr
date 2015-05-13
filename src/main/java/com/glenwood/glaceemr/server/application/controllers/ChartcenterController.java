package com.glenwood.glaceemr.server.application.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.services.chartcenter.ChartcenterService;

@RestController
@RequestMapping(value="PatientSearch.Action")
public class ChartcenterController {

	@Autowired(required=true)
	private ChartcenterService patientSearchService;


	@RequestMapping(value = "",method = RequestMethod.GET)
	public Page<PatientRegistration>   getSearchResult(
			@RequestParam(value="toSearchData",required = false)String toSearchData,
			@RequestParam(value="searchType",required = false)String searchTypeParam) throws Exception{
				
		Page<PatientRegistration>   patients = patientSearchService.getPatientSearchResult(toSearchData,searchTypeParam);

			return patients;
	}










}
