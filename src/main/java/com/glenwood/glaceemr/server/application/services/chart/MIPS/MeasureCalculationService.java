package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.glenwood.glaceemr.server.application.Bean.EPMeasureBean;
import com.glenwood.glaceemr.server.application.Bean.MIPSPatientInformation;
import com.glenwood.glaceemr.server.application.Bean.MIPSPerformanceBean;
import com.glenwood.glaceemr.server.application.Bean.MeasureStatus;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Request;

public interface MeasureCalculationService {

	void saveMeasureDetails(int providerId, int patientId, List<MeasureStatus> measureStatus, boolean flag);

	Request getQDMRequestObject(Boolean isIndividual,int patientID, int providerId, HashMap<String, HashMap<String, String>> codeListForQDM, Date startDate, Date endDate);

	List<EPMeasureBean> getEPMeasuresResponseObject(String accountId,Boolean isGroup,int patientID, int providerId, Date startDate, Date endDate, int repYear);
	
	Boolean checkGroupOrIndividual(int year);
	
	List<MIPSPerformanceBean> getMeasureRateReport(int providerId, String accountId, String configuredMeasures,boolean isACIReport);
	
	List<MIPSPerformanceBean> getMeasureRateReportByNPI(int providerId, String accountId, String configuredMeasures,boolean isACIReport);
	
	List<MIPSPerformanceBean> getPerformanceCount(int providerId, String measureId, String configuredMeasures, String accountId);
	
	List<MIPSPerformanceBean> getGroupPerformanceCount(String tinValue, String configuredMeasures, String accountId,boolean isACIReport);

	HashMap<String, Object> generateFilterContents();

	List<MIPSPatientInformation> getPatient(String patientId, String measureId, int criteria,Integer provider, String empTin, int mode);

	List<MIPSPatientInformation> getPatientInformation(String patientsList);
	
}