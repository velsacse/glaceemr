package com.glenwood.glaceemr.server.application.services.Denial;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.ServiceDetail;

public interface DenialService {

	List<ServiceDetail> serviceDetailId(String serviceDetailId);
	List<DenialBean> getAllDenials(String fromDate,String toDate);
}
