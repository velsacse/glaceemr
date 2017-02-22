package com.glenwood.glaceemr.server.application.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.Bean.MIPSResponse;
import com.glenwood.glaceemr.server.application.Bean.MacraProviderQDM;
import com.glenwood.glaceemr.server.application.Bean.MeasureStatus;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.HttpConnectionUtils;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Request;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Response;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.EMeasureUtils;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.MeasureCalculationService;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.QPPConfigurationService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;


@RestController
@Transactional
@RequestMapping(value = "/user/QPPPerformance")
public class QPPPerformanceController {
	
	@Autowired
	MeasureCalculationService measureService;
	
	@Autowired
	QPPConfigurationService providerConfService;
	
	@RequestMapping(value = "/insertPatientEntries", method = RequestMethod.GET)
	@ResponseBody
	public void saveMeasureDetails(
			@RequestParam(value="measureID", required=true) String measureID,
			@RequestParam(value="patientID", required=true) int patientID,
			@RequestParam(value="measureStatus", required=true) List<MeasureStatus> measureStatus)
	{
		measureService.saveMeasureDetails(measureID, patientID, measureStatus);
	}
	
	/**
	 * 
	 * @param patientID
	 * @param providerId
	 * @return
	 * @throws Exception
	 * 
	 * Step -1 : Using provider info, get the configuration details
	 * Step -2 : Get the performance rate as of now.
	 * Step -3 : Based on configured measures, get the EMeasure list
	 * Step -4 : Get the request QDM Object.
	 * Step -5 : Send the request QDM Object to central validation server.
	 * 
	 */
	
	@RequestMapping(value = "/getCQMStatusByPatient", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getQDMRequestObject(
			@RequestParam(value="accountId", required=true) String accountId,
			@RequestParam(value="patientID", required=true) int patientID,
			@RequestParam(value="providerId", required=true) int providerId) throws Exception
	{
	
		EMeasureUtils utils = new EMeasureUtils();
		List<Integer> cqmMeasures = new ArrayList<Integer>();		
		Request requestObj = new Request();
		Response responseFromCentralServer = new Response();
		MIPSResponse finalResponse = new MIPSResponse();
		EMRResponseBean response = new EMRResponseBean();
		
		String hub_url = "http://test.glaceemr.com/glacecds/ECQMServices/validateECQM";
		
		List<MacraProviderQDM> providerInfo = providerConfService.getCompleteProviderInfo(providerId);
		
		if(providerInfo!=null){
			
			System.out.println(new Date()+"provider info is not null");
			
			String[] measureIds = providerInfo.get(0).getMeasures().split(",");
			
			System.out.println(new Date()+"total measure length: "+measureIds.length);
			
			for(int i=0;i<measureIds.length;i++){
				cqmMeasures.add(Integer.parseInt(measureIds[i]));
			}
			
			System.out.println(new Date()+"sending request to get QDM codelist");
			
			HashMap<String, HashMap<String, String>> codeListForQDM = utils.getCodelist(utils.getMeasureBeanDetails(providerInfo.get(0).getMeasures()));
			
			System.out.println("\n\n resultant code list for QDM \n\n "+codeListForQDM.toString());
			
			finalResponse.setMeasureInfo(utils.getMeasureInfo());
			
			System.out.println(new Date()+"received codelist QDM from utils function");
			
			requestObj = measureService.getQDMRequestObject(patientID, providerId, codeListForQDM);

			requestObj.setAccountId(accountId);
			requestObj.setReportingYear(providerInfo.get(0).getMacraProviderConfigurationReportingYear());
			requestObj.setStartDate(providerInfo.get(0).getMacraProviderConfigurationReportingStart());
			requestObj.setEndDate(providerInfo.get(0).getMacraProviderConfigurationReportingEnd());
			requestObj.setMeasureIds(cqmMeasures);
			
			response.setData(utils.getMeasureDetails().toString());
			
			ObjectMapper objectMapper = new ObjectMapper();
			String requestString = objectMapper.writeValueAsString(requestObj);
			System.out.println(new Date()+"request string is::::::::::::;;;"+requestString.toString());
			String responseStr = HttpConnectionUtils.postData(hub_url, requestString, HttpConnectionUtils.HTTP_CONNECTION_MODE,"application/json");
			System.out.println(new Date()+"response: "+responseStr);
			responseFromCentralServer = objectMapper.readValue(responseStr, Response.class);
			
			Map<String,MeasureStatus> measureStatus = responseFromCentralServer.getMeasureStatus();
			
			if(measureStatus.containsKey("236")) measureStatus.get("236").setCmsId("CMS165v5");
			if(measureStatus.containsKey("113")) measureStatus.get("113").setCmsId("CMS130v5");
			if(measureStatus.containsKey("1"))   measureStatus.get("1").setCmsId("CMS122v5");
			if(measureStatus.containsKey("373")) measureStatus.get("373").setCmsId("CMS65v5");
			if(measureStatus.containsKey("110")) measureStatus.get("110").setCmsId("CMS147v6");
			if(measureStatus.containsKey("226")) measureStatus.get("226").setCmsId("CMS138v5");
			if(measureStatus.containsKey("130")) measureStatus.get("130").setCmsId("CMS68v6");
			if(measureStatus.containsKey("111")) measureStatus.get("111").setCmsId("CMS127v5");
			if(measureStatus.containsKey("112")) measureStatus.get("112").setCmsId("CMS125v5");
			if(measureStatus.containsKey("318")) measureStatus.get("318").setCmsId("CMS139v5");
			if(measureStatus.containsKey("134")) measureStatus.get("134").setCmsId("CMS2v6");
			if(measureStatus.containsKey("317")) measureStatus.get("317").setCmsId("CMS22v5");
			
			responseFromCentralServer.setMeasureStatus(measureStatus);
			
			finalResponse.setDataFromResponse(responseFromCentralServer);
			
			finalResponse.setEpMeasureStatus(measureService.getEPMeasuresResponseObject(patientID, providerId, providerInfo.get(0).getMacraProviderConfigurationReportingStart(), providerInfo.get(0).getMacraProviderConfigurationReportingEnd()));
			
		}else{
			
		}
		
		response.setData(finalResponse);
		
		return response;
		
	}
	
}
