package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.util.List;

import com.glenwood.glaceemr.server.application.Bean.ReportingInfo;
import com.glenwood.glaceemr.server.application.models.AttestationStatus;
import com.glenwood.glaceemr.server.application.services.employee.EmployeeDataBean;

public interface AttestationStatusService {

	List<AttestationStatus> getProviderReportingDetails(Integer reportingYear);
	
	List<EmployeeDataBean> getActiveProviderList();
	
	List<AttestationStatus> saveReportingDetails(Integer reportingYear,Integer reportingMethod,Integer reportingType,String comments,Integer reportingStatus,Integer providerId);
	
	ReportingInfo getAllActiveProviderStatus(Integer reportingYear);
	
}
