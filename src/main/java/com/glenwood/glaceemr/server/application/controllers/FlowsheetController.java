package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.Flowsheet;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.chart.flowsheet.FS_ClinicalElementOptionBean;
import com.glenwood.glaceemr.server.application.services.chart.flowsheet.FlowsheetBean;
import com.glenwood.glaceemr.server.application.services.chart.flowsheet.FlowsheetService;
import com.glenwood.glaceemr.server.application.services.chart.flowsheet.OverdueAlert;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Controller to get the flowsheet data
 * @author smita
 *
 */

@Api(value = "Flowsheet", description = "Contains the methods to get and save the details of flowsheet.", consumes="application/json")
@RestController
@RequestMapping(value="Flowsheet")
public class FlowsheetController {
	
	@Autowired(required=true)
	FlowsheetService flowsheetService;

	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	HttpServletRequest request;
	
	private Logger logger = Logger.getLogger(FlowsheetController.class);
	
	/**
	 * Request to get the list of flowsheets configured
	 * @param flowsheetType
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Get the list of flowsheets configured", notes = "Get the list of flowsheets configured based on the flowsheet type")
	@RequestMapping(value = "/Flowsheets",method = RequestMethod.GET)
	public List<Flowsheet> getFlowsheets(
			@ApiParam(name="flowsheetType",value="flowsheet type id") @RequestParam(value="flowsheetType", required=true, defaultValue="-1") Integer flowsheetType) throws Exception{
		logger.debug("Begin of request to get the list of flowsheets based on type.");
		List<Flowsheet> flowsheetPatData = flowsheetService.getFlowsheetPatientData(flowsheetType);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Flowsheet,AuditLogConstants.Flowsheet,1,AuditLogConstants.SUCCESS,"Successfully loaded data for flowsheet type="+flowsheetType,-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Flowsheet,request,"Successfully loaded data for flowsheet type="+flowsheetType);
		logger.debug("End of request to get the list of flowsheets based on type.");
		return flowsheetPatData;
	}
	
	/**
	 * Request to get the complete flowsheet details for a particular flowsheet for a patient.
	 * @param flowsheetId
	 * @param patientId
	 * @param encounterId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Get the complete flowsheet details for a particular flowsheet for a patient.", notes = "Get the complete flowsheet details for a particular flowsheet for a patient.")
	@RequestMapping(value = "/FlowsheetData",method = RequestMethod.GET)
	public FlowsheetBean getFlowsheetsDetails(
			@ApiParam(name="flowsheetId",value="flowsheet id") @RequestParam(value="flowsheetId", required=true, defaultValue="-1") Integer flowsheetId,
			@ApiParam(name="patientId",value="patient id") @RequestParam(value="patientId", required=true, defaultValue="-1") Integer patientId,
			@ApiParam(name="encounterId",value="encounter id") @RequestParam(value="encounterId", required=true, defaultValue="-1") Integer encounterId) throws Exception{
		logger.debug("Begin of request to get the complete flowsheet details based on flowsheet id and patient Id.");
		  System.out.println("before request  for Flowsheet "+System.currentTimeMillis());
	        long starttime=System.currentTimeMillis();
		FlowsheetBean flowsheetData = flowsheetService.getFlowsheetData(-1,flowsheetId,patientId,encounterId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Flowsheet,AuditLogConstants.Flowsheet,1,AuditLogConstants.SUCCESS,"Successfully loaded data for flowsheet="+flowsheetId+" for the patient having PatientId="+patientId,-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Flowsheet,request,"Successfully loaded data for flowsheet="+flowsheetId+" for the patient having PatientId="+patientId);
		logger.debug("End of request to get the complete flowsheet details based on flowsheet id and patient Id.");
		 System.out.println("After request  for flowsheet "+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));

		
		return flowsheetData;
	}
	
	/**
	 * Request to get the list of complete flowsheet details for a particular flowsheet for a patient for multiple sheets
	 * @param flowsheetType
	 * @param patientId
	 * @param encounterId
	 * @param dxCode
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Get the list of complete flowsheet details for a particular flowsheet for a patient for multiple sheets.", notes = "Get the list of complete flowsheet details for a particular flowsheet for a patient for multiple sheets.")
	@RequestMapping(value = "/FlowsheetDataMultipleSheets",method = RequestMethod.GET)
	public List<FlowsheetBean> getFlowsheetsDetailsList(
			@ApiParam(name="flowsheetType",value="flowsheet type") @RequestParam(value="flowsheetType", required=true, defaultValue="-1") Integer flowsheetType,
			@ApiParam(name="patientId",value="patient id")  @RequestParam(value="patientId", required=true, defaultValue="-1") Integer patientId,
			@ApiParam(name="encounterId",value="encounter id")  @RequestParam(value="encounterId", required=true, defaultValue="-1") Integer encounterId,
			@ApiParam(name="dxCode",value="dx code")  @RequestParam(value="dxCode", required=true, defaultValue="") String dxCode) throws Exception{
		logger.debug("Begin of request to get the complete flowsheet details based on flowsheet id and patient Id for multiple sheets.");
		  System.out.println("before request  for MEANINGFULUSE "+System.currentTimeMillis());
	        long starttime=System.currentTimeMillis();
		List<FlowsheetBean> flowsheetData = flowsheetService.getFlowsheetDataList(flowsheetType,dxCode,patientId,encounterId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Flowsheet,AuditLogConstants.Flowsheet,1,AuditLogConstants.SUCCESS,"Successfully loaded data for flowsheet type="+flowsheetType+" for the patient having PatientId="+patientId,-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Flowsheet,request,"Successfully loaded data for flowsheet type="+flowsheetType+" for the patient having PatientId="+patientId);
		logger.debug("End of request to get the complete flowsheet details based on flowsheet id and patient Id for multiple sheets.");
	     System.out.println("After request  for MEANINGFULUSE"+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
		return flowsheetData;
	}
	
	/**
	 * Request to get the clinical details for a particular clinical element.
	 * @param chartId
	 * @param encounterId
	 * @param gwIds
	 * @param isVital
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Get the clinical details for a particular clinical element.", notes = "Get the clinical details for a particular clinical element.")
	@RequestMapping(value = "/FlowsheetClinicalData",method = RequestMethod.GET)
	public List<FS_ClinicalElementOptionBean> getClinicalDetails(
			@ApiParam(name="chartId",value="chart id") @RequestParam(value="chartId", required=true, defaultValue="-1") Integer chartId,
			@ApiParam(name="encounterId",value="encounter id") @RequestParam(value="encounterId", required=true, defaultValue="-1") Integer encounterId,
			@ApiParam(name="elementId",value="element id") @RequestParam(value="elementId", required=true, defaultValue="-1") String gwIds,
			@ApiParam(name="isVital",value="is vital is 1 for vital section else its clinical elements section") @RequestParam(value="isVital", required=true, defaultValue="-1") Integer isVital) throws Exception{
		logger.debug("Begin of request to get the clinical details for a particular clinical element based on glenwood id, patient id and encounter id.");
		List<FS_ClinicalElementOptionBean> flowsheetData = flowsheetService.getClinicalElements(chartId, encounterId, gwIds, isVital);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Flowsheet,AuditLogConstants.Flowsheet,1,AuditLogConstants.SUCCESS,"Successfully loaded data for clinical element="+gwIds+" for the patient having chartId="+chartId+" and encounterId="+encounterId,-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Flowsheet,request,"Successfully loaded data for clinical element="+gwIds+" for the patient having chartId="+chartId+" and encounterId="+encounterId);
		logger.debug("End of request to get the clinical details for a particular clinical element based on glenwood id, patient id and encounter id.");
		return flowsheetData;
	}
	
	/**
	 * Request to get the lab details for recommended labs.
	 * @param chartId
	 * @param patientId
	 * @param encounterId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Get the lab details for recommended labs.", notes = "Get the lab details for recommended labs.")
	@RequestMapping(value = "/FlowsheetRecommendedLabDetails",method = RequestMethod.GET)
	public List<FlowsheetBean> getRecommendedLabDetails(
			@ApiParam(name="chartId",value="chart id") @RequestParam(value="chartId", required=true, defaultValue="-1") Integer chartId,
			@ApiParam(name="patientId",value="patient id") @RequestParam(value="patientId", required=true, defaultValue="-1") Integer patientId,
			@ApiParam(name="encounterId",value="encounter id") @RequestParam(value="encounterId", required=true, defaultValue="-1") Integer encounterId) throws Exception{
		logger.debug("Begin of request to get the lab details for recommended labs.");
		List<FlowsheetBean> flowsheetData = flowsheetService.getRecommendedLabs(chartId, patientId, encounterId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Flowsheet,AuditLogConstants.Flowsheet,1,AuditLogConstants.SUCCESS,"Successfully loaded data for recommended labs for the patient having PatientId="+patientId,-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Flowsheet,request,"Successfully loaded data for recommended labs for the patient having PatientId="+patientId);
		logger.debug("End of request to get the lab details for recommended labs.");
		return flowsheetData;
	}

	/**
	 * request to get the list of overdue alerts.
	 * @param chartId
	 * @param patientId
	 * @param encounterId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Get the list of overdue alerts.", notes = "Get the list of overdue alerts.")
	@RequestMapping(value = "/FlowsheetOverdueAlerts",method = RequestMethod.GET)
	public List<OverdueAlert> getFlowsheetOverdueAlerts(
			@ApiParam(name="chartId",value="chart id") @RequestParam(value="chartId", required=true, defaultValue="-1") Integer chartId,
			@ApiParam(name="patientId",value="patient id") @RequestParam(value="patientId", required=true, defaultValue="-1") Integer patientId,
			@ApiParam(name="encounterId",value="encounter id") @RequestParam(value="encounterId", required=true, defaultValue="-1") Integer encounterId) throws Exception{
		logger.debug("Begin of request to get the list of overdue alerts.");
		List<OverdueAlert> flowsheetData = flowsheetService.overdueLabs(patientId,chartId, encounterId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Flowsheet,AuditLogConstants.Flowsheet,1,AuditLogConstants.SUCCESS,"Successfully loaded data for the list of overdue alerts.",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Flowsheet,request,"Successfully loaded data for the list of overdue alerts.");
		logger.debug("End of request to get the list of overdue alerts.");
		return flowsheetData;
	}
	
	/**
	 * Request to get the flowsheet details for labs and params section(as on 2016/01/21) for a particular flowsheet for a patient.
	 * @param flowsheetId
	 * @param patientId
	 * @param encounterId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Get the flowsheet details for labs and params section for a particular flowsheet for a patient.", notes = "Get the flowsheet details for labs and params section for a particular flowsheet for a patient")
	@RequestMapping(value = "/FlowsheetDataSOAP",method = RequestMethod.GET)
	public FlowsheetBean getFlowsheetsDetailsSOAP(
			@ApiParam(name="flowsheetId",value="flowsheet id") @RequestParam(value="flowsheetId", required=true, defaultValue="-1") Integer flowsheetId,
			@ApiParam(name="patientId",value="patient id") @RequestParam(value="patientId", required=true, defaultValue="-1") Integer patientId,
			@ApiParam(name="encounterId",value="encounter id") @RequestParam(value="encounterId", required=true, defaultValue="-1") Integer encounterId) throws Exception{
		logger.debug("Begin of request to get the flowsheet details for labs and params section for a particular flowsheet for a patient");
		FlowsheetBean flowsheetData = flowsheetService.getFlowsheetDataSOAP(-1,flowsheetId,patientId,encounterId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Flowsheet,AuditLogConstants.Flowsheet,1,AuditLogConstants.SUCCESS,"Successfully loaded data for flowsheet="+flowsheetId+" for the patient having PatientId="+patientId,-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Flowsheet,request,"Successfully loaded data for flowsheet="+flowsheetId+" for the patient having PatientId="+patientId);
		logger.debug("End of request to get the complete flowsheet details based on flowsheet id and patient Id.");
		return flowsheetData;
	}
	
	
}
