package com.glenwood.glaceemr.server.application.controllers;
import java.util.List;

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

@RestController
@Transactional
@RequestMapping(value="/user/WorkflowAlert.Action")
public class WorkflowAlertController {
	
	@Autowired
	WorkflowAlertService workFlowAlertService;
	
	@RequestMapping(value = "/getworkflowconfigstatus", method = RequestMethod.GET)
	@ResponseBody
	public boolean getWorkflowConfigStatus(
			 @RequestParam(value="optionname", required=true) String optionName) throws Exception
	{	
		return workFlowAlertService.getWorkflowConfigStatus(optionName);
	}
	
	@RequestMapping(value = "/getalerts", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> workflowAlertList(
			 @RequestParam(value="userid", required=true) int userId) throws Exception
	{
		List<Object> alerts=null;
		alerts=workFlowAlertService.workflowAlertList(userId); //get the alerts based on told in workflow alert table
		return alerts;
	}
	

	@RequestMapping(value = "/getalertsbyencounterid", method = RequestMethod.GET)
	@ResponseBody
	public Workflow workflowAlertListByEncounterId(
			 @RequestParam(value="encounterid", required=true) int encounterId) throws Exception
	{
		Workflow alerts=null;
		alerts=workFlowAlertService.workflowAlertListByencounterId(encounterId); //get the alerts based on toid in workflow alert table
		return alerts;
	}
	
	@RequestMapping(value = "/getalertbyid", method = RequestMethod.GET)
	@ResponseBody
	public Workflow workflowAlertListById(
			 @RequestParam(value="id", required=true) int workflowId) throws Exception
	{
		Workflow alerts=null;
		alerts=workFlowAlertService.workflowAlertListById(workflowId); //get the alerts based on toid in workflow alert table
		return alerts;
	}
	
	@RequestMapping(value = "/getalertcount", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> getAlertCount(
			@RequestParam(value="userid", required=true) int userId) throws Exception
	{
		List<Object> alerts=null;
		alerts=workFlowAlertService.getAlertCount(userId); //get the alerts based on toid in workflow alert table
		return alerts;
	}

	
	@RequestMapping(value = "/createalert", method = RequestMethod.GET)
	@ResponseBody
	public Workflow insertAlert(
			 @RequestParam(value="patientid", required=true) int patientId,
			 @RequestParam(value="encounterid", required=true) int encounterId,
			 @RequestParam(value="chartid", required=true) int chartId,
			 @RequestParam(value="roomid", required=true) int roomId,
			 @RequestParam(value="fromid", required=true) int fromId,
			@RequestParam(value="toid", required=true) int toId,
			 @RequestParam(value="msg", required=true) String msg,
			 @RequestParam(value="status", required=true) int status,
			@RequestParam(value="ishighpriority", required=true) boolean isHighPriority)
	{
		Workflow grpDetails=workFlowAlertService.insertAlert(patientId,encounterId,chartId,roomId,fromId,toId,msg,status,isHighPriority);
		return grpDetails;
	}
	
	@RequestMapping(value = "/highlightalert", method = RequestMethod.GET)
	@ResponseBody
	public Workflow highlightAlert(
			@RequestParam(value="alertid", required=true) int alertId,
			 @RequestParam(value="ishighpriority", required=true) boolean isHighPriority)
	{
		Workflow grpDetails=workFlowAlertService.highlightAlert(alertId,isHighPriority);
		return grpDetails;
	}
	
	@RequestMapping(value = "/forwardalert", method = RequestMethod.GET)
	@ResponseBody
	public Workflow forwardAlert(
			@RequestParam(value="alertid", required=true) int alertId,
			@RequestParam(value="forwardto", required=true) int forwardTo,
			@RequestParam(value="fromid", required=true) int fromId,
			@RequestParam(value="status", required=false,defaultValue="-1") int status,
			@RequestParam(value="roomid", required=false,defaultValue="-1") int roomId,
			@RequestParam(value="ishighpriority", required=false,defaultValue="-1") String isHighpriority,
			@RequestParam(value="message", required=false,defaultValue="-1") String msg)
	{
		return workFlowAlertService.fowardAlert(alertId,fromId,forwardTo,status,roomId,isHighpriority,msg);
	}
	
	@RequestMapping(value = "/closealert", method = RequestMethod.GET)
	@ResponseBody
	public Workflow closeAlert(
			@RequestParam(value="patientid", required=true) int patientId)
	{
		return workFlowAlertService.closeAlert(patientId);
	
	}
	
	@RequestMapping(value = "/getstatus", method = RequestMethod.GET)
	@ResponseBody
	public List<AlertCategory> getWorkflowCategory()
	{
		List<AlertCategory> grpDetails=workFlowAlertService.getWorkflowCategory();	//only if alert_category_isworkflow is true.
		return grpDetails;
	}
	
	
	@RequestMapping(value = "/createstatus", method = RequestMethod.GET)
	@ResponseBody
	public AlertCategory insertStatus(
			@RequestParam(value="name", required=true) String name,
			 @RequestParam(value="url", required=false, defaultValue="Chart") String url)
	{
	return	workFlowAlertService.insertStatus(name,url); //it will allow to create new category
	}
	
	
	@RequestMapping(value = "/deletestatus", method = RequestMethod.GET)
	@ResponseBody
	public void deleteStatus(
			 @RequestParam(value="categoryid", required=true) int categoryId)
	{
	workFlowAlertService.deleteStatus(categoryId);
	
	}

	
	@RequestMapping(value = "/updatestatus", method = RequestMethod.GET)
	@ResponseBody
	public AlertCategory updateStatus(
			 @RequestParam(value="categoryid", required=true) int categoryId,
			 @RequestParam(value="name", required=true,defaultValue="-1") String categoryName,
			 @RequestParam(value="url", required=false,defaultValue="-1") String url)
	{
	return workFlowAlertService.updateStatus(categoryId,categoryName,url);
	}
	
	@RequestMapping(value = "/getroomdetails", method = RequestMethod.GET)
	@ResponseBody
	public List<Room> getRoomDetails()	{
	return workFlowAlertService.getRoomDetails();
	}
	
	@RequestMapping(value = "/gettotaltimedetails", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> gettotaltimedetails(
			 @RequestParam(value="toid", required=true,defaultValue="-111") int toId,
			 @RequestParam(value="startdate", required=true) String startDate,
			 @RequestParam(value="enddate", required=true) String endDate)	{
	return workFlowAlertService.gettotaltimedetails(toId,startDate,endDate);
	}

}
