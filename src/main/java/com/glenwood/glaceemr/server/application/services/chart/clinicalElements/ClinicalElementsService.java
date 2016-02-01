package com.glenwood.glaceemr.server.application.services.chart.clinicalElements;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions;

public interface ClinicalElementsService {
	public List<ClinicalElementsOptions> getClinicalElementOptions(String gwid);
}
