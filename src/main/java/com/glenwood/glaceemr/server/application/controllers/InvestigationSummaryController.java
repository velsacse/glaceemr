package com.glenwood.glaceemr.server.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.investigation.InvestigationSummaryService;
import com.glenwood.glaceemr.server.application.services.investigation.SaveLabDetails;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @author yasodha
 *
 * Controller for Investigations in GlaceEMR, 
 * contains request to get the list of ordered labs, performed labs and reviewed labs.
 * and ability to order the lab.
 */
@Api(value = "Investigation", description = "Contains the methods to get and save the order details.", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value = "/Investigation")
public class InvestigationSummaryController {

	@Autowired
	AuditTrailService auditTrailService;

	@Autowired
	SessionMap sessionMap;

	@Autowired
	HttpServletRequest request;

	@Autowired
	InvestigationSummaryService investigationService;

	private Logger logger = Logger.getLogger(InvestigationSummaryController.class);
	
	/**
	 * Request to save the details of an order
	 * @param labDetails
	 * @return the message
	 * @throws Exception
	 */
	@ApiOperation(value = "Save the details of an order", notes = "Save the details of an order")
	@RequestMapping(value = "/SaveTestDetails",method = RequestMethod.POST)
	public String saveTestDetails(@RequestBody SaveLabDetails labDetails) throws Exception {
		logger.debug("Begin of save the complete details for a particular lab");
		investigationService.savelab(labDetails.getRequestToSave(), labDetails.getEncounterId(), labDetails.getPatientId(), labDetails.getChartId(), labDetails.getUserId(), labDetails.getFullData(), "-1", "-1", "false", "" + labDetails.getTestId());
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Investigations,AuditLogConstants.Investigations,
				1,AuditLogConstants.SUCCESS,"Successfully save data for the patient having PatientId=" + labDetails.getPatientId(),
				-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Investigations,request,
				"Successfully save data for the patient having PatientId=" + labDetails.getPatientId());
		logger.debug("End of save the complete details for a particular lab");
		return "success";
	}
}
