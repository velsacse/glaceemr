package com.glenwood.glaceemr.server.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.chart.reports.OverrideAlertsService;
import com.glenwood.glaceemr.server.application.services.chart.reports.OverrideBean;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * Controller to override the data
 * @author smita
 */

@Api(value = "OverrideAlerts", description = "Contains the methods to override the element details.", consumes="application/json")
@RestController
@RequestMapping(value="/user/OverrideAlerts")
public class OverrideAlertsController {
	
	@Autowired(required=true)
	OverrideAlertsService overrideAlertsService;

	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	HttpServletRequest request;
	
	private Logger logger = Logger.getLogger(OverrideAlertsController.class);
	
	/**
	 * Request to override the flowsheet elements(includes vitals, clinical elements and medication section)
	 * @param overrideBean
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Override the flowsheet elements(includes vitals, clinical elements and medication section)", notes = "Override the flowsheet elements(includes vitals, clinical elements and medication section)")
	@RequestMapping(value = "/OverrideFlowsheet",method = RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean getFlowsheets(@RequestBody OverrideBean overrideBean) throws Exception{
		logger.debug("Begin of request to override the Flowsheet data(includes vitals, clinical elements and medication section overriding functionality.)");
		String overrideAlertMessage = overrideAlertsService.insertAlert(overrideBean.getElementids(),overrideBean.getPatientid(),overrideBean.getFsElementType(),overrideBean.getFlowsheetId(),overrideBean.getUserId(),overrideBean.getReason(),overrideBean.getData());
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.HMR,AuditLogConstants.HMR,1,AuditLogConstants.SUCCESS,"Successfully overridden test from flowsheet view (elementids="+overrideBean.getElementids()+", PatientID="+overrideBean.getPatientid()+",InstanceId=-100 )",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.HMR,request,"Successfully overridden test from flowsheet view (elementids="+overrideBean.getElementids()+", PatientID="+overrideBean.getPatientid()+",InstanceId=-100 )");
		logger.debug("End of request to override the Flowsheet data(includes vitals, clinical elements and medication section overriding functionality.)");
		EMRResponseBean alertMessage=new EMRResponseBean();
		alertMessage.setData(overrideAlertMessage);
		return alertMessage;
	}
}