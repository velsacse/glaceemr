package com.glenwood.glaceemr.server.application.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.Bean.MUAnalyticsBean;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.AttestationStatusService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;

@RestController
@Transactional
@RequestMapping(value = "/glacemonitor/ReportingStatus")
public class GlaceAttestationController {

	@Autowired
	AttestationStatusService statusSerivce;
	
	@RequestMapping(value = "/getAllReportingDetails", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getAllProviderReportingStatus() throws JSONException{
		
		EMRResponseBean response=new EMRResponseBean();
		
		List<MUAnalyticsBean> reportingInfo = statusSerivce.getAllActiveProviderStatus();
		
		Map<String, List<MUAnalyticsBean>> result = new HashMap<String, List<MUAnalyticsBean>>();
		
		result.put("reportingYears", reportingInfo);
		
		response.setData(result);
		
		return response;
		
	}
	
}
