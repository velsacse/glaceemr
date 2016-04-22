package com.glenwood.glaceemr.server.application.services.chart.print;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.print.GenericPrintStyle;

public interface GenericPrintService {

	public List<GenericPrintStyle> getGenericPrintStyleList();
	public GenericPrintStyle getGenericPrintStyle(Integer styleId);
	public GenericPrintStyle saveGenericPrintStyle(GenericPrintStyle genericPrintStyle);
	public void generatePDFPreview(Integer styleId);
	public void generatePDFPreview(Integer styleId,Integer patientId);
	
	/*
	 * <b> Purpose: </b> To get patient details, employee details and pos details
	 */
	public GenericPrintBean getCompleteDetails(Integer patientId, Integer encounterId) throws Exception;
	
}
