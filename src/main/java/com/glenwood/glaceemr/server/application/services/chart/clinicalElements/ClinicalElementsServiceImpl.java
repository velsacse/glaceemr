package com.glenwood.glaceemr.server.application.services.chart.clinicalElements;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions;
import com.glenwood.glaceemr.server.application.repositories.ClinicalElementsOptionsRepository;
import com.glenwood.glaceemr.server.application.specifications.ClinicalElementsSpecification;


@Service
public class ClinicalElementsServiceImpl implements ClinicalElementsService{

	@Autowired
	ClinicalElementsOptionsRepository clinicalElementsOptionsRepository;
	
	@Override
	public List<ClinicalElementsOptions> getClinicalElementOptions(String gwid) {
		return clinicalElementsOptionsRepository.findAll(ClinicalElementsSpecification.getclinicalElementOptions(gwid));

	}
}
