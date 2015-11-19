package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.repositories.EncounterRepository;
import com.glenwood.glaceemr.server.application.services.phonemessages.PhoneMessagesService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "Phone messages", description = "API for phone messages encounter", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="PhoneMessages")
public class PhoneMessagesController {
	
	@Autowired
	PhoneMessagesService phoneMessageService;

	@Autowired
	EncounterRepository encounterEntityRepository;
	
	/**
	 * To get the list of phone messages based on the patient id
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@ApiOperation(value = "Get phone messages", notes = "To get the list of phone messages based on the patient id.")
	@RequestMapping(value = "/getPhoneEncounters", method = RequestMethod.GET)
	@ResponseBody
	public List<Encounter> getPhoneEncounters(@RequestParam(value="patientid", required=true, defaultValue="") String patientId,
			@RequestParam(value="startdate", required=false, defaultValue="-1") String startDate,
			@RequestParam(value="enddate", required=false, defaultValue="-1") String endDate){
		
			List<Encounter> encounterList=phoneMessageService.getPhoneEncounters(patientId, startDate, endDate);
			
		return encounterList;
	}
	
	/**
	 * To create phone message encounter
	 * @param patientId
	 * @param chartId
	 * @param severity
	 * @param serviceDr
	 * @param comments
	 * @param createdBy
	 * @return
	 */
	@ApiOperation(value = "Create phone message encounter", notes = "To create phone message encounter.")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	@ResponseBody
	public Encounter create(
			@RequestParam(value="patientid", required=false, defaultValue="") String patientId,
			@RequestParam(value="chartid", required=true, defaultValue="-1") String chartId,
			@RequestParam(value="severity", required=false, defaultValue="-1") String severity,
			@RequestParam(value="servicedr", required=true, defaultValue="-1") String serviceDr,
			@RequestParam(value="comments", required=false, defaultValue="-1") String comments,
			@RequestParam(value="createdby", required=true, defaultValue="-1") String createdBy){
		
			Short encounterType=2;
			Encounter encounterList=phoneMessageService.createEncounter(chartId, severity, encounterType, serviceDr, comments, createdBy);
			
		return encounterList;
	}
	
	/**
	 * To get the particular encounter details based on the encounter id
	 * @param encounterId
	 * @return
	 */
	@ApiOperation(value = "Get particular encounter details", notes = "To get the particular encounter details based on the encounter id")	
	@RequestMapping(value = "/getEncounterDetails", method = RequestMethod.GET)
	@ResponseBody
	public Encounter getEncounterDetails(
			@RequestParam(value="encounterid", required=true, defaultValue="1") String encounterId){
		
			Encounter encounter=phoneMessageService.getEncounterDetails(encounterId);
		return encounter;
	}
	
	/**
	 * To update the particular encounter details
	 * @param encounterId
	 * @param response
	 * @param status
	 * @param modifiedBy
	 * @param serviceDr
	 * @return
	 */
	@ApiOperation(value = "Update encounter details", notes = "To update the particular encounter details")
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	@ResponseBody
	public Encounter update(
			@RequestParam(value="encounterid", required=false, defaultValue="1") String encounterId,
			@RequestParam(value="response", required=true, defaultValue="-1") String response,
			@RequestParam(value="status", required=false, defaultValue="-1") String status,
			@RequestParam(value="modifiedby", required=true, defaultValue="-1") String modifiedBy,
			@RequestParam(value="servicedr", required=false, defaultValue="-1") String serviceDr){
		
			Encounter encounter=phoneMessageService.update(encounterId, response, status, modifiedBy, serviceDr);
			
		return encounter;
	}
	
	/**
	 * To send reply to a phone message encounter.
	 * @param encounterId
	 * @param replyMessage
	 * @param status
	 * @param modifiedBy
	 * @param severity
	 * @return
	 */
	@ApiOperation(value = "Send reply to a phone message", notes = "To send reply to a phone message encounter.")
	@RequestMapping(value = "/sendreply", method = RequestMethod.GET)
	@ResponseBody
	public Encounter sendReply(
			@RequestParam(value="encounterid", required=false, defaultValue="1") String encounterId,
			@RequestParam(value="replymsg", required=true, defaultValue="-1") String replyMessage,
			@RequestParam(value="status", required=false, defaultValue="-1") String status,
			@RequestParam(value="modifiedby", required=true, defaultValue="-1") String modifiedBy,
			@RequestParam(value="severity", required=false, defaultValue="-1") String severity){
		
			Encounter encounter=phoneMessageService.sendReply(encounterId, replyMessage, status, modifiedBy, severity);
			
		return encounter;
	}
}