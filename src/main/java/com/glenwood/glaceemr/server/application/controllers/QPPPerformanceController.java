package com.glenwood.glaceemr.server.application.controllers;

import java.util.ArrayList;
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
import com.glenwood.glaceemr.server.application.Bean.EPMeasureBean;
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
			@RequestParam(value="userId", required=true) int userId,
			@RequestParam(value="patientID", required=true) int patientID,
			@RequestParam(value="measureStatus", required=true) List<MeasureStatus> measureStatus)
	{
		measureService.saveMeasureDetails(userId, patientID, measureStatus);
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
			@RequestParam(value="userId", required=true) int userId,
			@RequestParam(value="sharedFolder", required=true) String sharedPath,
			@RequestParam(value="providerId", required=true) int providerId) throws Exception
	{
	
		EMeasureUtils utils = new EMeasureUtils();
		List<Integer> cqmMeasures = new ArrayList<Integer>();		
		Request requestObj = new Request();
		Response responseFromCentralServer = new Response();
		MIPSResponse finalResponse = new MIPSResponse();
		EMRResponseBean response = new EMRResponseBean();
		
		String hub_url = "http://test.glaceemr.com/glacecds/ECQMServices/validateECQM";
		
		Boolean isIndividual=measureService.checkGroupOrIndividual(2017);
		
		if(!isIndividual){
			providerId=-1;
			userId=-1;
		}
		
		List<MacraProviderQDM> providerInfo = providerConfService.getCompleteProviderInfo(providerId);
		if(providerInfo!=null){
			
			String[] measureIds = providerInfo.get(0).getMeasures().split(",");
			
			for(int i=0;i<measureIds.length;i++){
				cqmMeasures.add(Integer.parseInt(measureIds[i]));
			}
			
			HashMap<String, HashMap<String, String>> codeListForQDM = utils.getCodelist(utils.getMeasureBeanDetails(providerInfo.get(0).getMeasures(), sharedPath));
			finalResponse.setMeasureInfo(utils.getMeasureInfo());
			requestObj = measureService.getQDMRequestObject(isIndividual,patientID, userId, codeListForQDM);

			requestObj.setAccountId(accountId);
			requestObj.setReportingYear(providerInfo.get(0).getMacraProviderConfigurationReportingYear());
			requestObj.setStartDate(providerInfo.get(0).getMacraProviderConfigurationReportingStart());
			requestObj.setEndDate(providerInfo.get(0).getMacraProviderConfigurationReportingEnd());
			requestObj.setMeasureIds(cqmMeasures);
			
			response.setData(utils.getMeasureDetails().toString());
			
			ObjectMapper objectMapper = new ObjectMapper();
			String requestString = objectMapper.writeValueAsString(requestObj);
			
			System.out.println("request string is...................."+requestString.toString());
			
			String responseStr = HttpConnectionUtils.postData(hub_url, requestString, HttpConnectionUtils.HTTP_CONNECTION_MODE,"application/json");
			
			System.out.println("response string is...................."+responseStr.toString());
			
			responseFromCentralServer = objectMapper.readValue(responseStr, Response.class);
			
			Map<String,MeasureStatus> measureStatus = responseFromCentralServer.getMeasureStatus();
			
			responseFromCentralServer.setMeasureStatus(measureStatus);
			
			finalResponse.setDataFromResponse(responseFromCentralServer);
			
			List<MeasureStatus> responseToSave = new ArrayList<MeasureStatus>();
			
			for(int i=0;i<measureIds.length;i++){
				
				measureStatus.get(measureIds[i]).setReportingYear(""+providerInfo.get(0).getMacraProviderConfigurationReportingYear());
				responseToSave.add(measureStatus.get(measureIds[i]));
				
			}
			
			measureService.saveMeasureDetails(providerId, patientID, responseToSave);
			
		}else{
			
		}
		
		response.setData(finalResponse);
		
		return response;
		
	}
	
	/**
	 * Function to get EP Measure status for given patientID
	 */
	
	@RequestMapping(value = "/getEPStatusByPatient", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getEPRequestObject(
			@RequestParam(value="accountId", required=true) String accountId,
			@RequestParam(value="patientID", required=true) int patientID,
			@RequestParam(value="userId", required=true) int userId,
			@RequestParam(value="providerId", required=true) int providerId) throws Exception
	{
	
		EMRResponseBean response = new EMRResponseBean();
		
		List<EPMeasureBean> epMeasureStatus = new ArrayList<EPMeasureBean>();
		
		Boolean isGroup=measureService.checkGroupOrIndividual(2017);
		if(isGroup){
			providerId=-1;
			userId=-1;
		}
		
		List<MacraProviderQDM> providerInfo = providerConfService.getCompleteProviderInfo(providerId);
		
		if(providerInfo!=null){
			
			epMeasureStatus = measureService.getEPMeasuresResponseObject(isGroup,patientID, userId, providerInfo.get(0).getMacraProviderConfigurationReportingStart(), providerInfo.get(0).getMacraProviderConfigurationReportingEnd());
			
		}else{
			
		}
		
		response.setData(epMeasureStatus);
		
		return response;
		
	}
	
}