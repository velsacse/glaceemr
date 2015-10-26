package com.glenwood.glaceemr.server.application.controllers;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.AlertCategory;
import com.glenwood.glaceemr.server.application.models.Room;
import com.glenwood.glaceemr.server.application.models.Workflow;
import com.glenwood.glaceemr.server.application.services.workflowalerts.WorkflowAlertService;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Api(value = "/WorkflowAlert.Action", description = "To get list of WorkFlowAlert and also to create,update and delete the WorkFlowAlerts", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="WorkflowAlert.Action")
public class WorkflowAlertController {
	
	@Autowired
	WorkflowAlertService workFlowAlertService;

	@Autowired
	SessionMap sessionMap;

	@Autowired
	HttpServletRequest request;
	
	@RequestMapping(value = "/getworkflowconfigstatus", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of workflow config status"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public boolean getWorkflowConfigStatus(
			@ApiParam(name="optionName",value="option name") @RequestParam(value="optionname", required=true) String optionName) throws Exception
	{	
		return workFlowAlertService.getWorkflowConfigStatus(optionName);
	}
	
	@RequestMapping(value = "/getalerts", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of alerts"),
		    @ApiResponse(code = 404, message = "when user id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<Object> workflowAlertList(
			@ApiParam(name="userId",value="login user id") @RequestParam(value="userid", required=true) int userId) throws Exception
	{
		List<Object> alerts=null;
		alerts=workFlowAlertService.workflowAlertList(userId); //get the alerts based on told in workflow alert table
		return alerts;
	}
	

	@RequestMapping(value = "/getalertsbyencounterid", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of alerts"),
		    @ApiResponse(code = 404, message = "when encounter id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public Workflow workflowAlertListByEncounterId(
			@ApiParam(name="encounterId",value="encounter id") @RequestParam(value="encounterid", required=true) int encounterId) throws Exception
	{
		Workflow alerts=null;
		alerts=workFlowAlertService.workflowAlertListByencounterId(encounterId); //get the alerts based on toid in workflow alert table
		return alerts;
	}
	
	@RequestMapping(value = "/getalertbyid", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of alerts"),
		    @ApiResponse(code = 404, message = "when workflow id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public Workflow workflowAlertListById(
			@ApiParam(name="workflowId",value="workflow id") @RequestParam(value="id", required=true) int workflowId) throws Exception
	{
		Workflow alerts=null;
		alerts=workFlowAlertService.workflowAlertListById(workflowId); //get the alerts based on toid in workflow alert table
		return alerts;
	}
	
	@RequestMapping(value = "/getalertcount", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of alert count"),
		    @ApiResponse(code = 404, message = "when user id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<Object> getAlertCount(
			@ApiParam(name="userId",value="login user id") @RequestParam(value="userid", required=true) int userId) throws Exception
	{
		List<Object> alerts=null;
		alerts=workFlowAlertService.getAlertCount(userId); //get the alerts based on toid in workflow alert table
		return alerts;
	}

	
	@RequestMapping(value = "/createalert", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successfully alert created"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public Workflow insertAlert(
			@ApiParam(name="patientId",value="patient id") @RequestParam(value="patientid", required=true) int patientId,
			@ApiParam(name="encounterId",value="encounter id") @RequestParam(value="encounterid", required=true) int encounterId,
			@ApiParam(name="chartId",value="chart id") @RequestParam(value="chartid", required=true) int chartId,
			@ApiParam(name="roomId",value="room id") @RequestParam(value="roomid", required=true) int roomId,
			@ApiParam(name="fromId",value="sender id") @RequestParam(value="fromid", required=true) int fromId,
			@ApiParam(name="toId",value="receiver id") @RequestParam(value="toid", required=true) int toId,
			@ApiParam(name="msg",value="message") @RequestParam(value="msg", required=true) String msg,
			@ApiParam(name="status",value="category id") @RequestParam(value="status", required=true) int status,
			@ApiParam(name="isHighPriority",value="") @RequestParam(value="ishighpriority", required=true) boolean isHighPriority)
	{
		Workflow grpDetails=workFlowAlertService.insertAlert(patientId,encounterId,chartId,roomId,fromId,toId,msg,status,isHighPriority);
		return grpDetails;
	}
	
	@RequestMapping(value = "/highlightalert", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of alert with updated highlight status"),
		    @ApiResponse(code = 404, message = "when alert id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public Workflow highlightAlert(
			@ApiParam(name="alertId",value="work flow alert id") @RequestParam(value="alertid", required=true) int alertId,
			@ApiParam(name="isHighPriority",value="status of priority") @RequestParam(value="ishighpriority", required=true) boolean isHighPriority)
	{
		Workflow grpDetails=workFlowAlertService.highlightAlert(alertId,isHighPriority);
		return grpDetails;
	}
	
	@RequestMapping(value = "/forwardalert", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of forwarded alert details"),
		    @ApiResponse(code = 404, message = "when alert id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public Workflow forwardAlert(
			@ApiParam(name="alertId",value="work flow alert id") @RequestParam(value="alertid", required=true) int alertId,
			@ApiParam(name="forwardTo",value="to id") @RequestParam(value="forwardto", required=true) int forwardTo,
			@ApiParam(name="fromId",value="from id") @RequestParam(value="fromid", required=true) int fromId,
			@ApiParam(name="status",value="category status") @RequestParam(value="status", required=false,defaultValue="-1") int status,
			@ApiParam(name="roomId",value="room number") @RequestParam(value="roomid", required=false,defaultValue="-1") int roomId,
			@ApiParam(name="isHighpriority",value="high priority status") @RequestParam(value="ishighpriority", required=false,defaultValue="-1") String isHighpriority,
			@ApiParam(name="message",value="message") @RequestParam(value="message", required=false,defaultValue="-1") String msg)
	{
		return workFlowAlertService.fowardAlert(alertId,fromId,forwardTo,status,roomId,isHighpriority,msg);
	}
	
	@RequestMapping(value = "/closealert", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successfully closed active alerts for particular patient"),
		    @ApiResponse(code = 404, message = "when patient id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public Workflow closeAlert(
			@ApiParam(name="patientId",value="patient id") @RequestParam(value="patientid", required=true) int patientId)
	{
		return workFlowAlertService.closeAlert(patientId);
	
	}
	
	@RequestMapping(value = "/getstatus", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of alert category status"),
		    @ApiResponse(code = 404, message = "when category id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertCategory> getWorkflowCategory()
	{
		List<AlertCategory> grpDetails=workFlowAlertService.getWorkflowCategory();	//only if alert_category_isworkflow is true.
		return grpDetails;
	}
	
	
	@RequestMapping(value = "/createstatus", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successfully status created"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public AlertCategory insertStatus(
			@ApiParam(name="name",value="category name") @RequestParam(value="name", required=true) String name,
			@ApiParam(name="url",value="request page url") @RequestParam(value="url", required=false, defaultValue="Chart") String url)
	{
	return	workFlowAlertService.insertStatus(name,url); //it will allow to create new category
	}
	
	
	@RequestMapping(value = "/deletestatus", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successfully status deleted"),
		    @ApiResponse(code = 404, message = "when category id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public void deleteStatus(
			@ApiParam(name="categoryId",value="category id") @RequestParam(value="categoryid", required=true) int categoryId)
	{
	workFlowAlertService.deleteStatus(categoryId);
	
	}

	
	@RequestMapping(value = "/updatestatus", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of updated status details"),
		    @ApiResponse(code = 404, message = "when category id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public AlertCategory updateStatus(
			@ApiParam(name="categoryId",value="category id") @RequestParam(value="categoryid", required=true) int categoryId,
			@ApiParam(name="categoryName",value="category name") @RequestParam(value="name", required=true,defaultValue="-1") String categoryName,
			@ApiParam(name="url",value="page url") @RequestParam(value="url", required=false,defaultValue="-1") String url)
	{
	return workFlowAlertService.updateStatus(categoryId,categoryName,url);
	}
	
	@RequestMapping(value = "/getroomdetails", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of room details"),
		    @ApiResponse(code = 404, message = "data's not available"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<Room> getRoomDetails()	{
	return workFlowAlertService.getRoomDetails();
	}
	
	@RequestMapping(value = "/gettotaltimedetails", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of category total time details"),
		    @ApiResponse(code = 404, message = "data's not available"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<Object> gettotaltimedetails(
			@ApiParam(name="toId",value="toId") @RequestParam(value="toid", required=true,defaultValue="-111") int toId,
			@ApiParam(name="startDate",value="start date") @RequestParam(value="startdate", required=true) String startDate,
			@ApiParam(name="endDate",value="end date") @RequestParam(value="enddate", required=true) String endDate)	{
	return workFlowAlertService.gettotaltimedetails(toId,startDate,endDate);
	}

}
