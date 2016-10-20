package com.glenwood.glaceemr.server.application.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.PortalMessageBean;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.portal.portalMessages.PortalMessagesService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;



@RestController
@Transactional
@RequestMapping(value="/user/PortalMessages")
@Api(value="PortalMessagesController", description="Gets the inbox message list of a patient", consumes="application/json")
public class PortalMessagesController {
	
	@Autowired
	PortalMessagesService portalMessagesService;
	
	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	Logger logger=LoggerFactory.getLogger(LoginController.class);

	@Autowired
	EMRResponseBean responseBean;
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @return List of patient's messages details
	 * @throws Exception
	 */
	@RequestMapping(value = "/PatientMessagesDetails", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's messages details", notes = "Returns a complete list of patient's messages details.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's messages details"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getMessagesDetailsByPatientId(@ApiParam(name="patientId", value="patient's id whose messages details is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMessagesService.getMessagesDetailsByPatientId(patientId));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in patient message details!");
			return responseBean;
		}
	}
	
	
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @return List of patient's inbox messages
	 * @throws Exception
	 */
	@RequestMapping(value = "/PatientMessagesList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's inbox messages", notes = "Returns a complete list of patient's inbox messages.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's inbox messages list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getPatientMessagesList(@ApiParam(name="patientId", value="patient's id whose inbox messages list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="offset", value="messages offset size value") @RequestParam(value="offset", required=false, defaultValue="5") int offset,
			@ApiParam(name="pageIndex", value="page number of the offset(offset number)") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMessagesService.getMessagesByPatientId(patientId,offset,pageIndex));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in patient messages list!");
			return responseBean;
		}
	}
	
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @return List of patient's inbox messages
	 * @throws Exception
	 */
	@RequestMapping(value = "/PatientMessageThreadsList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's inbox messages", notes = "Returns a complete list of patient's inbox messages.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's inbox messages list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getPatientMessagesThreadsList(@ApiParam(name="patientId", value="patient's id whose inbox messages list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="offset", value="messages offset size value") @RequestParam(value="offset", required=false, defaultValue="5") int offset,
			@ApiParam(name="pageIndex", value="page number of the offset(offset number)") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMessagesService.getPatientMessageThreadsByPatientId(patientId,offset,pageIndex));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in patient messages thread list!");
			return responseBean;
		}
	}
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @return List of patient's inbox messages
	 * @throws Exception
	 */
	@RequestMapping(value = "/PatientInboxMessagesList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's inbox messages", notes = "Returns a complete list of patient's inbox messages.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's inbox messages list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getPatientInboxMessagesList(@ApiParam(name="patientId", value="patient's id whose inbox messages list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="offset", value="messages offset size value") @RequestParam(value="offset", required=false, defaultValue="5") int offset,
			@ApiParam(name="pageIndex", value="page number of the offset(offset number)") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMessagesService.getInboxMessagesByPatientId(patientId,offset,pageIndex));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in patient inbox messages list!");
			return responseBean;
		}
	}

	/**
	 * @param patientId 		: Required patient's id 
	 * @return List of patient's sent messages
	 * @throws Exception
	 */
	@RequestMapping(value = "/PatientSentMessagesList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's sent messages", notes = "Returns a complete list of patient's sent messages.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's sent messages list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getPatientSentMessagesList(@ApiParam(name="patientId", value="patient's id whose sent messages list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="offset", value="messages offset size value") @RequestParam(value="offset", required=false, defaultValue="5") int offset,
			@ApiParam(name="pageIndex", value="page number of the offset(offset number)") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMessagesService.getSentMessagesByPatientId(patientId,offset,pageIndex));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in patient sent messages list!");
			return responseBean;
		}
	}
	
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @return List of patient's sent messages
	 * @throws Exception
	 */
	@RequestMapping(value = "/DeletePortalMessageByMessageId", method = RequestMethod.GET)
    @ApiOperation(value = "delete patient message by message id", notes = "deletes the patient's message by message id and returns the updated details of portal messages.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's portal message details, after the deletion of the message."),
		    @ApiResponse(code = 404, message = "message or patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean deletePatientMessage(@ApiParam(name="patientId", value="patient's id whose sent messages list is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="messageId", value="id of the message to be deleted") @RequestParam(value="messageId", required=false, defaultValue="-1") int messageId) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMessagesService.deletePortalMessageByMessageId(patientId,messageId));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in deleting patient message!");
			return responseBean;
		}
	}
	
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @return List of patient's messages details
	 * @throws Exception
	 */
	@RequestMapping(value = "/DeletePortalMessageThread", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's messages details after deleting the portal message", notes = "deletes the portal message based on message id and returns the new updated details of the portal messages.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's messages details after message deletion"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean deletePortalMessageThread(@ApiParam(name="patientId", value="patient's id whose message details is to be retrieved after deletion of message") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="threadId", value="delete portal message by message id") @RequestParam(value="threadId", required=false, defaultValue="-1") int threadId) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMessagesService.deletePortalMessageThread(patientId, threadId));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in deleting patient message thread!");
			return responseBean;
		}
	}
	
	
	
	/**
	 * @param portalmessageBean : PortalMessage Model which contains all details to save a message. 
	 * @return Message saving response bean
	 * @throws Exception
	 */
	@RequestMapping(value = "/Message/Create", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new message", notes = "Creates a new message.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Message saved successfully."),
		    @ApiResponse(code = 404, message = "Operation to save new message is not available"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public  EMRResponseBean saveNewMessage(@RequestBody PortalMessageBean portalMessageBean) throws Exception{

		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);

		try {
			responseBean.setSuccess(true);
			responseBean.setData(portalMessagesService.saveNewMessage(portalMessageBean));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Error in creating a message!");
			return responseBean;
		}
	}
	
	
	
}
