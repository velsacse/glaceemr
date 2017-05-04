package com.glenwood.glaceemr.server.application.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.glenwood.glaceemr.server.application.Bean.MIPSPatientInformation;
import com.glenwood.glaceemr.server.application.Bean.MIPSPerformanceBean;
import com.glenwood.glaceemr.server.application.Bean.MIPSResponse;
import com.glenwood.glaceemr.server.application.Bean.MacraProviderQDM;
import com.glenwood.glaceemr.server.application.Bean.MeasureStatus;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.HttpConnectionUtils;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Request;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Response;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.EMeasureUtils;
import com.glenwood.glaceemr.server.application.Bean.mailer.GlaceMailer;
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
		
		int savedUser = userId; //used to store logged userId to store the details in patient entries
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		try{
			
			Boolean isIndividual=measureService.checkGroupOrIndividual(Calendar.getInstance().get(Calendar.YEAR));

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
				requestObj = measureService.getQDMRequestObject(isIndividual,patientID, userId, codeListForQDM, providerInfo.get(0).getMacraProviderConfigurationReportingStart(), providerInfo.get(0).getMacraProviderConfigurationReportingEnd());

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

				List<MeasureStatus> responseToSave = new ArrayList<MeasureStatus>();

				for(int i=0;i<measureIds.length;i++){

					String measureID = measureStatus.keySet().toArray()[i].toString();
					
					measureStatus.get(measureID).setReportingYear(""+providerInfo.get(0).getMacraProviderConfigurationReportingYear());
					responseToSave.add(measureStatus.get(measureID));

				}

				measureService.saveMeasureDetails(savedUser, patientID, responseToSave);

			}else{

			}

			response.setData(finalResponse);

		}catch(Exception e){
			
			try {
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, patientID,"Error occurred while getting ECQM status",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();
				
			}
			
		}
		
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
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		try{
			
			Boolean isGroup=measureService.checkGroupOrIndividual(Calendar.getInstance().get(Calendar.YEAR));

			if(!isGroup){
				providerId=-1;
				userId=-1;
			}

			List<MacraProviderQDM> providerInfo = providerConfService.getCompleteProviderInfo(providerId);

			if(providerInfo!=null){

				epMeasureStatus = measureService.getEPMeasuresResponseObject(accountId,isGroup,patientID, userId, providerInfo.get(0).getMacraProviderConfigurationReportingStart(), providerInfo.get(0).getMacraProviderConfigurationReportingEnd());

			}else{

			}

			response.setData(epMeasureStatus);

		}catch(Exception e){
			
			try {
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, patientID,"Error occurred while getting EP Measure status",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();
				
			}
			
		}
		
		return response;
		
	}
	
	@RequestMapping(value = "/getMIPSPerformanceRate", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getMIPSPerformanceRate(
			@RequestParam(value="providerId", required=true) int providerId,
			@RequestParam(value="accountId", required=true) String accountId,
			@RequestParam(value="mode", required=false, defaultValue="1") int mode,
			@RequestParam(value="tinValue", required=false, defaultValue="") String tinValue,
			@RequestParam(value="measureId", required=false, defaultValue="") String measureId) throws Exception{
		
		EMRResponseBean response = new EMRResponseBean();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);

		String configuredMeasures = "";
		
		try{

			Calendar now = Calendar.getInstance();
			int reportingYear = now.get(Calendar.YEAR);	

			List<MacraProviderQDM> providerInfo = new ArrayList<MacraProviderQDM>();

			if(mode!=2){
				providerInfo = providerConfService.getCompleteProviderInfo(providerId);
				configuredMeasures = providerInfo.get(0).getMeasures();
			}else{
				configuredMeasures = providerConfService.getCompleteTinInfo(tinValue, reportingYear);
			}

			List<MIPSPerformanceBean> performanceObj = null;

			if(mode == 0){
				performanceObj = measureService.getMeasureRateReportByNPI(providerId, accountId, configuredMeasures);
			}else if(mode == 1){
				performanceObj = measureService.getMeasureRateReport(providerId, accountId, configuredMeasures);
			}else{
				performanceObj = measureService.getGroupPerformanceCount(tinValue,configuredMeasures, accountId);
			}

			response.setData(performanceObj);

		}catch(Exception e){
			
			try {
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, -1,"Error occurred while getting MIPSPerformance Report",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();
				
			}
			
		}
		
		return response;
		
	}
	
	@RequestMapping(value = "/getPatient", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getPatient(
			@RequestParam(value="patientId", required=true) String patientId,
			@RequestParam(value="measureId", required=true) String measureId,
			@RequestParam(value="criteria", required=true) int criteria,
			@RequestParam(value="mode", required=true) int mode,
			@RequestParam(value="empTIN", required=false, defaultValue="") String empTIN,
			@RequestParam(value="accountId", required=true) String accountId,
			@RequestParam(value="provider", required=true) Integer provider){
		
		EMRResponseBean response = new EMRResponseBean();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);

		try{
		
		List<MIPSPatientInformation> filtersInfo = measureService.getPatient(patientId,measureId,criteria,provider,empTIN, mode);
		
		response.setData(filtersInfo);
		
		}catch(Exception e){
			
			try {
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, -1,"Error occurred while getting filtered patients list",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();
				
			}
			
		}
		
		return response;
		
	}
	
	@RequestMapping(value = "/getMIPSPerformanceRateByNPI", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getMIPSPerformanceRateByNPI(
			@RequestParam(value="providerId", required=true) int providerId,
			@RequestParam(value="accountId", required=true) String accountId,
			@RequestParam(value="measureId", required=false, defaultValue="") String measureId) throws Exception{
		
		EMRResponseBean response = new EMRResponseBean();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);

		try{
		
		List<MacraProviderQDM> providerInfo = providerConfService.getCompleteProviderInfo(providerId);
		
		String configuredMeasures = providerInfo.get(0).getMeasures();
		
		List<MIPSPerformanceBean> performanceObj = measureService.getMeasureRateReport(providerId, accountId, configuredMeasures);

		response.setData(performanceObj);
		
		}catch(Exception e){
			
			try {
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, -1,"Error occurred while getting MIPSPerformance Report",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();
				
			}
			
		}
		
		return response;
		
	}
	
}