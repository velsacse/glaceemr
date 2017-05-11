package com.glenwood.glaceemr.server.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogActionType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogModuleType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogUserType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.Log_Outcome;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailSaveService;
import com.glenwood.glaceemr.server.application.services.rulesengine.GlaceRulesService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;

/**
 * @author software
 *
 */

@RestController

@RequestMapping(value = "/user/GlaceRulesController")
public class GlaceRulesController {


	@Autowired
	GlaceRulesService glaceRulesService;
	
	@Autowired
	AuditTrailSaveService auditTrailSaveService;
	
	@Autowired
	HttpServletRequest request;

	@RequestMapping(value = "/getPQRSMeasure", method = RequestMethod.GET)
    @ResponseBody
	public	EMRResponseBean getPQRSMeasure(@RequestParam(value="providerId", required=true, defaultValue="-1") Integer providerId) throws Exception{
		
		String measure=glaceRulesService.getMeasures(providerId);
		EMRResponseBean result=new EMRResponseBean();
		result.setData(measure);
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.PQRS, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "Collected Configured Measures", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return result;
		
	}
	
}
