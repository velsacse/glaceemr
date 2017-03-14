package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.glenwood.glaceemr.server.application.Bean.EPMeasureBean;
import com.glenwood.glaceemr.server.application.Bean.MeasureStatus;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Request;

public interface MeasureCalculationService {

	void saveMeasureDetails(int providerId, int patientId, List<MeasureStatus> measureStatus);

	Request getQDMRequestObject(Boolean isIndividual,int patientID, int providerId, HashMap<String, HashMap<String, String>> codeListForQDM);

	List<EPMeasureBean> getEPMeasuresResponseObject(Boolean isGroup,int patientID, int providerId, Date startDate, Date endDate);
	
	Boolean checkGroupOrIndividual(int year);
	
}