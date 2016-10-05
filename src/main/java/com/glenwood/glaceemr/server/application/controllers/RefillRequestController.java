package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.Prescription;
import com.glenwood.glaceemr.server.application.models.SSMessageInbox;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.chart.RefillRequest.MedicationListBean;
import com.glenwood.glaceemr.server.application.services.chart.RefillRequest.PatientRefillDataBean;
import com.glenwood.glaceemr.server.application.services.chart.RefillRequest.RefillRequestService;
import com.glenwood.glaceemr.server.application.services.chart.RefillRequest.SSMessageBean;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


@Api(value = "/RefillRequest", description = "To get the denial drug details", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value = "/user/RefillRequest")
public class RefillRequestController {
	
	@Autowired
	RefillRequestService refillrequestservice;
	
	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	HttpServletRequest request;

	private Logger logger = Logger.getLogger(RefillRequestController.class);

	
	@RequestMapping(value ="/byId", method = RequestMethod.GET) 
	@ResponseBody
	public List<Prescription> getdenieddrugs(@RequestParam(value="encounterid",required=false,defaultValue="-1")Integer encounterid,@RequestParam(value="patientid",required=false,defaultValue="-1")Integer patientid)throws Exception
	{
		logger.debug("Getting  the encounter id"+encounterid);
		logger.debug("Getting  the patientid "+patientid);
		List<Prescription> drugs=refillrequestservice.getDeniedDrugs(encounterid,patientid);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return drugs;
	}
	
	
	/**
	 *  
	 * @return Bean consists of both inbox and outbox alerts count
	 * @throws Exception
	 */
	@RequestMapping(value ="/alertCount", method = RequestMethod.GET)
    @ApiOperation(value = "Returns refill requests count", response =SSMessageBean.class)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of User group details"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public SSMessageBean getCount()throws Exception
	{
		SSMessageBean drugs=refillrequestservice.getCount();
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return drugs;
	}
	
	
	/**
	 * 
	 * @param erxUserAlertType
	 * @param userId
	 * @param isNewPage
	 * @return Bean consists of both inbox and outbox alerts list
	 * @throws Exception
	 */
	@RequestMapping(value ="/alertInbox", method = RequestMethod.GET) 
	@ApiOperation(value = "Returns refill requests alert list", response =SSMessageBean.class)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of User group details"),
            @ApiResponse(code = 404, message = "when group id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public MedicationListBean getalertinbox(@ApiParam(name="erxUserAlertType", value="erx User Alert Type") @RequestParam(value="erxUserAlertType",required=false,defaultValue="MYALERT")String erxUserAlertType,@ApiParam(name="userId", value="userId") @RequestParam(value="userId",required=false,defaultValue="-1")String userId,@ApiParam(name="isNewPage", value="isNewPage") @RequestParam(value="isNewPage",required=false,defaultValue="false") boolean isNewPage)throws Exception
	{
		MedicationListBean drugs=refillrequestservice.getAlertInbox(erxUserAlertType,userId,isNewPage);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return drugs;
	}
	
	
	/**
	 * 
	 * @param userId
	 * @param alertId
	 * @param alertEventIds
	 * @return string represents successful closing of alerts
	 * @throws Exception
	 */
	@RequestMapping(value ="/isCloseAlert", method = RequestMethod.GET)
	@ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of User group details"),
            @ApiResponse(code = 404, message = "when group id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public String isCloseAlert(@ApiParam(name="userId", value="userId") @RequestParam(value="userId",required=false,defaultValue="-1")int userId,@ApiParam(name="alertId", value="alertId") @RequestParam(value="alertId",required=false,defaultValue="-1")String alertId,@ApiParam(name="alertEventIds", value="alertEventIds") @RequestParam(value="alertEventIds",required=false,defaultValue="-1")String alertEventIds) throws Exception
	{
		String responseString=refillrequestservice.isCloseAlert(userId,alertId,alertEventIds);
		return responseString;
	}
	
	
	/**
	 * 
	 * @param patientId
	 * @param alertId
	 * @return String represents successful merge of requests
	 * @throws Exception
	 */
	@RequestMapping(value ="/mergeRequest", method = RequestMethod.GET)
	@ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of User group details"),
            @ApiResponse(code = 404, message = "when group id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<SSMessageInbox> mergeRequest(@ApiParam(name="patientId", value="patientId") @RequestParam(value="patientId",required=false,defaultValue="-1")int patientId,@ApiParam(name="alertId", value="alertId") @RequestParam(value="alertId",required=false,defaultValue="-1")String alertId) throws Exception
	{
		List<SSMessageInbox> responseString=refillrequestservice.mergeRequest(patientId,alertId);
		return responseString;
		
	}
	
	
	/**
	 * 
	 * @param patientId
	 * @return Data bean contains POS,patient,medication details. 
	 * @throws Exception
	 */
	@RequestMapping(value ="/pendingRequest", method = RequestMethod.GET)
	@ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of User group details"),
            @ApiResponse(code = 404, message = "when group id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PatientRefillDataBean pendingRequest(@ApiParam(name="patientId", value="patientId") @RequestParam(value="patientId",required=false,defaultValue="-1")int patientId) throws Exception
	{
		String appt=refillrequestservice.BillingConfig(patientId);
		String lastVisitPOS=refillrequestservice.getPosDetails(patientId);
		PatientRegistration patdata=refillrequestservice.getPatientData(patientId);
		List<SSMessageInbox> pedingdata=refillrequestservice.patRequestsData(patientId);
		PatientRefillDataBean databean=new PatientRefillDataBean(appt,lastVisitPOS,patdata,pedingdata);
		return databean;
		
	}
	
}
