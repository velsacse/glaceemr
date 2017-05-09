package com.glenwood.glaceemr.server.application.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import com.glenwood.glaceemr.server.application.Bean.MIPSPerformanceBean;
import com.glenwood.glaceemr.server.application.Bean.MIPSResponse;
import com.glenwood.glaceemr.server.application.Bean.MacraProviderQDM;
import com.glenwood.glaceemr.server.application.Bean.MeasureStatus;
import com.glenwood.glaceemr.server.application.Bean.SharedFolderBean;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.HttpConnectionUtils;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Request;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Response;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.EMeasureUtils;
import com.glenwood.glaceemr.server.application.Bean.mailer.GlaceMailer;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.MUPerformanceRateService;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.MeasureCalculationService;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.QPPConfigurationService;
import com.glenwood.glaceemr.server.datasource.TennantContextHolder;

@RestController
@Transactional
@RequestMapping(value = "/glacemonitor/mipsperformance")
public class MUPerformanceRateController {

	@Autowired
	QPPConfigurationService providerConfService;
	
	@Autowired
	MUPerformanceRateService performanceService;
	
	@Autowired
	MeasureCalculationService measureService;
	
	@Autowired
	SharedFolderBean sharedFolderBean;
	
	/**
	 * Get List of patients seen by provider based on selected mode value
	 * 
	 * mode = 1 -> Get list of patients seen on current day
	 * mode = 2 -> Get list of patients seen in current month
	 * mode = 3 -> Get list of patients seen during reporting period
	 * mode = 4 -> Get list of patients seen during given period of time
	 * 
	 */
	
	@RequestMapping(value = "/getPatientsSeen", method = RequestMethod.GET)
	@ResponseBody
	public HashMap<Integer, List<Integer>> getPatientsSeen(
			@RequestParam(value="reportingYear", required=true) int reportingYear,
			@RequestParam(value="accountID", required=true) String accountID,
			@RequestParam(value="startDate", required=false) Date startDate,
			@RequestParam(value="endDate", required=false) Date endDate,
			@RequestParam(value="mode", required=true) int mode){

		HashMap<Integer, List<Integer>> patientsList = new HashMap<Integer, List<Integer>>();
		
		List<MacraProviderQDM> providers = new ArrayList<MacraProviderQDM>();
		
		Integer providerId = -1;
		
		String responseMsg = "Success";
		
		Writer writer = new StringWriter();
		
		PrintWriter printWriter = new PrintWriter(writer);
		
		try{

			providers = providerConfService.getProviderReportingInfo(reportingYear);

			for(int i=0;i<providers.size();i++){

				providerId = providers.get(i).getMacraProviderConfigurationProviderId();

				if(mode == 4){

					if(startDate == null || endDate == null){

						responseMsg = "No startDate and endDate specified for mode 4";

						throw new Exception();

					}else{

						patientsList.put(providerId, performanceService.getPatientsSeen(providerId, startDate, endDate));

					}

				}else if(mode == 1){

					patientsList.put(providerId, performanceService.getPatientsSeen(providerId, new Date(), null));

				}else if(mode == 2){

					Calendar calendar = getCalendarForNow();
					calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
					setTimeToBeginningOfDay(calendar);
					startDate = calendar.getTime();

					calendar = getCalendarForNow();
					calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					setTimeToEndofDay(calendar);
					endDate = calendar.getTime();

					patientsList.put(providerId, performanceService.getPatientsSeen(providerId, startDate, endDate));

				}else if(mode == 3){

					startDate = providers.get(i).getMacraProviderConfigurationReportingStart();
					endDate = providers.get(i).getMacraProviderConfigurationReportingEnd();

					patientsList.put(providerId, performanceService.getPatientsSeen(providerId, startDate, endDate));

				}else{

					responseMsg = "Please send valid mode";

					throw new Exception();

				}

			}

		}catch(Exception e){
			
			try {
				
				e.printStackTrace(printWriter);

				responseMsg = GlaceMailer.buildMailContentFormat(accountID, -1,responseMsg,writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountID,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();
				
			}
			
		}
		
		return patientsList;
		
	}
	
	@RequestMapping(value = "/generateAndValidateQDM", method = RequestMethod.GET)
	@ResponseBody
	public boolean generateAndValidateQDM(
			@RequestParam(value="accountId", required=true) String accountId,
			@RequestParam(value="patientID", required=true) int patientID,
			@RequestParam(value="providerId", required=true) int providerId) throws Exception
	{
	
		EMeasureUtils utils = new EMeasureUtils();
		List<Integer> cqmMeasures = new ArrayList<Integer>();		
		Request requestObj = new Request();
		Response responseFromCentralServer = new Response();
		MIPSResponse finalResponse = new MIPSResponse();
		
		String hub_url = "http://test.glaceemr.com/glacecds/ECQMServices/validateECQM";
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		try{

			Boolean isIndividual=measureService.checkGroupOrIndividual(Calendar.getInstance().get(Calendar.YEAR));

			int userIdForEntries = providerId;

			if(!isIndividual){
				providerId=-1;
			}

			List<MacraProviderQDM> providerInfo = providerConfService.getCompleteProviderInfo(providerId);

			if(providerInfo!=null){

				String[] measureIds = providerInfo.get(0).getMeasures().split(",");

				for(int i=0;i<measureIds.length;i++){
					cqmMeasures.add(Integer.parseInt(measureIds[i]));
				}

				HashMap<String, HashMap<String, String>> codeListForQDM = utils.getCodelist(utils.getMeasureBeanDetails(providerInfo.get(0).getMeasures(), sharedFolderBean.getSharedFolderPath().get(TennantContextHolder.getTennantId()).toString()));
				finalResponse.setMeasureInfo(utils.getMeasureInfo());
				requestObj = measureService.getQDMRequestObject(isIndividual,patientID, providerId, codeListForQDM, providerInfo.get(0).getMacraProviderConfigurationReportingStart(), providerInfo.get(0).getMacraProviderConfigurationReportingEnd());

				requestObj.setAccountId(accountId);
				requestObj.setReportingYear(providerInfo.get(0).getMacraProviderConfigurationReportingYear());
				requestObj.setStartDate(providerInfo.get(0).getMacraProviderConfigurationReportingStart());
				requestObj.setEndDate(providerInfo.get(0).getMacraProviderConfigurationReportingEnd());
				requestObj.setMeasureIds(cqmMeasures);

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

				measureService.saveMeasureDetails(userIdForEntries, patientID, responseToSave, true);
				
				measureService.getEPMeasuresResponseObject(accountId, isIndividual, patientID, userIdForEntries, providerInfo.get(0).getMacraProviderConfigurationReportingStart(), providerInfo.get(0).getMacraProviderConfigurationReportingEnd(), providerInfo.get(0).getMacraProviderConfigurationReportingYear());

			}

		}catch(Exception e){
			
			try {
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, patientID,"Error occurred while QDM validation",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();

			}
			
			return false;
			
		}
		
		return true;
		
	}
	
	@RequestMapping(value = "/calculateMIPSPerformance", method = RequestMethod.GET)
	@ResponseBody
	public boolean calculateMIPSPerformance(
			@RequestParam(value="reportingYear", required=true) int reportingYear,
			@RequestParam(value="accountID", required=true) String accountID,
			@RequestParam(value="isMonthlyReport", required=false, defaultValue="false") boolean isMonthlyReport){
		
		List<MacraProviderQDM> providerInfo = null;
		List<MIPSPerformanceBean> providerPerformance = null;
		HashMap<Integer, List<Integer>> patientsSeenByProvider = null;
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		try{
			
			
			patientsSeenByProvider = new HashMap<Integer, List<Integer>>();
			providerPerformance = new ArrayList<MIPSPerformanceBean>();
			
			if(!isMonthlyReport){
				patientsSeenByProvider = getPatientsSeen(reportingYear, accountID, null, null, 3);
			}else{
				patientsSeenByProvider = getPatientsSeen(reportingYear, accountID, null, null, 2);
			}
			
			for(int i=0;i<patientsSeenByProvider.keySet().size();i++){
				
				Integer providerId = (Integer) patientsSeenByProvider.keySet().toArray()[i]; 
				
				providerInfo = providerConfService.getCompleteProviderInfo(providerId);
				
				providerPerformance = measureService.getPerformanceCount(providerId, "", providerInfo.get(0).getMeasures(), accountID);

				performanceService.addToMacraMeasuresRate(providerId,  providerPerformance, reportingYear, providerInfo.get(0).getMacraProviderConfigurationReportingStart(), providerInfo.get(0).getMacraProviderConfigurationReportingEnd(), false);
				
				providerPerformance = measureService.getPerformanceCount(providerId, "", "ACI_EP_1,ACI_CCTPE_2,ACI_PEA_1,ACI_CCTPE_1,ACI_HIE_1,ACI_PEA_2,ACI_HIE_3", accountID);
				
				performanceService.addToMacraMeasuresRate(providerId,  providerPerformance, reportingYear, providerInfo.get(0).getMacraProviderConfigurationReportingStart(), providerInfo.get(0).getMacraProviderConfigurationReportingEnd(), true);
				
			}
			
		}catch(Exception e){
			
			try {
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountID, -1,"Error occurred while calculating performance",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountID,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();

			}
			
			return false;
			
		}
		
		return true;
		
	}
	
	private static Calendar getCalendarForNow() {
	    Calendar calendar = GregorianCalendar.getInstance();
	    calendar.setTime(new Date());
	    return calendar;
	}

	private static void setTimeToBeginningOfDay(Calendar calendar) {
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	}

	private static void setTimeToEndofDay(Calendar calendar) {
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	}
	
}
