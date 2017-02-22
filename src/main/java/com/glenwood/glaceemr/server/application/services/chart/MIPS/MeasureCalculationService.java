package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.glenwood.glaceemr.server.application.Bean.EPMeasureBean;
import com.glenwood.glaceemr.server.application.Bean.MeasureStatus;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Request;

public interface MeasureCalculationService {

	void saveMeasureDetails(String measureID, int patientId, List<MeasureStatus> measureStatus);

	Request getQDMRequestObject(int patientID, int providerId, HashMap<String, HashMap<String, String>> codeListForQDM);

	List<EPMeasureBean> getEPMeasuresResponseObject(int patientID, int providerId, Date startDate, Date endDate);
	
}
