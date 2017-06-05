package com.glenwood.glaceemr.server.application.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.Bean.MacraProviderQDM;
import com.glenwood.glaceemr.server.application.Bean.MeasureStatus;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.HttpConnectionUtils;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Request;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Response;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.EMeasureUtils;
import com.glenwood.glaceemr.server.application.Bean.pqrs.PqrsResponseBean;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogActionType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogModuleType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogUserType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.Log_Outcome;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailSaveService;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.MeasureCalculationService;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.QPPConfigurationService;
import com.glenwood.glaceemr.server.application.services.pqrsreport.PqrsReportService;
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
	QPPConfigurationService providerConfService;

	@Autowired
	MeasureCalculationService measureService;

	@Autowired
	PqrsReportService pqrsreportservice;
	
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
	
	/**
	 * Method for Integrating PQRS with Macra tab
	 * @param accountId
	 * @param patientID
	 * @param providerId
	 * @param userId
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/getPQRSProviderConfiguredMeasures", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getProviderConfiguration(
			@RequestParam(value="accountId", required=true) String accountId,
			@RequestParam(value="patientID", required=true) int patientID,
			@RequestParam(value="providerId", required=true) int providerId,
			@RequestParam(value="userId", required=true) int userId) throws Exception
	{
		EMeasureUtils utils = new EMeasureUtils();
		List<Integer> cqmMeasures = new ArrayList<Integer>();		
		Request requestObj = new Request();
		Response responseFromCentralServer = new Response();
		PqrsResponseBean finalResponse = new PqrsResponseBean();
		EMRResponseBean response = new EMRResponseBean();
		String hub_url = "http://test.glaceemr.com/glacecds/ECQMServices/validateECQM";

		List<MacraProviderQDM> providerInfo = providerConfService.getCompleteProviderInfo(providerId);

		if(providerInfo!=null){

			String[] measureIds = providerInfo.get(0).getMeasures().split(",");

			for(int i=0;i<measureIds.length;i++){
				cqmMeasures.add(Integer.parseInt(measureIds[i]));
			}
			String data = "";
			HashMap<String, String> codeListForQDM = utils.getPqrsCodeList(utils.getPpqrsHubReponse(providerInfo.get(0).getMeasures()));


			finalResponse.setMeasureInfo(utils.getMeasureInfo());

			requestObj.setAccountId(accountId);
			requestObj.setReportingYear(providerInfo.get(0).getMacraProviderConfigurationReportingYear());
			requestObj.setStartDate(providerInfo.get(0).getMacraProviderConfigurationReportingStart());
			requestObj.setEndDate(providerInfo.get(0).getMacraProviderConfigurationReportingEnd());
			requestObj.setMeasureIds(cqmMeasures);

			response.setData(utils.getMeasureDetails().toString());

			ObjectMapper objectMapper = new ObjectMapper();
			String requestString = objectMapper.writeValueAsString(requestObj);

			String responseStr = HttpConnectionUtils.postData(hub_url, requestString, HttpConnectionUtils.HTTP_CONNECTION_MODE,"application/json");

			responseFromCentralServer = objectMapper.readValue(responseStr, Response.class);

			Map<String,MeasureStatus> measureStatus = responseFromCentralServer.getMeasureStatus();

			responseFromCentralServer.setMeasureStatus(measureStatus);

			finalResponse.setDataFromResponse(responseFromCentralServer);

			finalResponse.setPqrsresponse(measureService.getPqrsResponseObject(patientID, userId, providerInfo.get(0).getMacraProviderConfigurationReportingStart(), providerInfo.get(0).getMacraProviderConfigurationReportingEnd(),codeListForQDM));

			finalResponse.setQualityMeasures(measureService.getQualityMeasureResponseObject(userId,codeListForQDM));

		}else{

		}
		response.setData(finalResponse);

		return response;
	}
	
	
}
