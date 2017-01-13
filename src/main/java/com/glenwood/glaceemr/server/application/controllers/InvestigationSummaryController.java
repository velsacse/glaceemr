package com.glenwood.glaceemr.server.application.controllers;

import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.BodySite;
import com.glenwood.glaceemr.server.application.models.LabDescription;
import com.glenwood.glaceemr.server.application.models.LabEntries;
import com.glenwood.glaceemr.server.application.models.VaccinationConsentForm;
import com.glenwood.glaceemr.server.application.models.VaccineOrderDetails;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.investigation.ConsentDetails;
import com.glenwood.glaceemr.server.application.services.investigation.FrequentOrders;
import com.glenwood.glaceemr.server.application.services.investigation.InvestigationSummaryService;
import com.glenwood.glaceemr.server.application.services.investigation.LS_Bean;
import com.glenwood.glaceemr.server.application.services.investigation.OrderLogGroups;
import com.glenwood.glaceemr.server.application.services.investigation.OrderSetData;
import com.glenwood.glaceemr.server.application.services.investigation.Orders;
import com.glenwood.glaceemr.server.application.services.investigation.SaveLabDetails;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * @author yasodha
 *
 * Controller for Investigations in GlaceEMR, 
 * contains request to get the list of ordered labs, performed labs and reviewed labs.
 * and ability to order the lab.
 */
@Api(value = "InvestigationController", description = "Contains the methods to get and save the order details.", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value = "/user/Investigation")
public class InvestigationSummaryController {

	@Autowired
	AuditTrailService auditTrailService;

	@Autowired
	SessionMap sessionMap;

	@Autowired
	HttpServletRequest request;

	@Autowired
	InvestigationSummaryService investigationService;

	private Logger logger = Logger.getLogger(InvestigationSummaryController.class);

	/**
	 * Method to get order set data
	 * @throws Exception
	 */
	@RequestMapping(value = "/OrderSet",method = RequestMethod.GET)
	public EMRResponseBean getOrderSetList() throws Exception {
		logger.debug("in getting order set data");
//		List<OrderSetData> orderSetData = investigationService.findOrderSetData();
		  System.out.println("before request  for orderset "+System.currentTimeMillis());
	        long starttime=System.currentTimeMillis();
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(investigationService.findOrderSetData());
		System.out.println("After request  for orderset "+System.currentTimeMillis());
        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
		return emrResponseBean;
	}
	
	/**
	 * Method to get result list
	 * @param encounterId
	 * @param chartId
	 * @return the entity LabEntries
	 * @throws Exception
	 */
	@RequestMapping(value = "/ListOfResults",method = RequestMethod.GET)
	public EMRResponseBean getResultsList(@RequestParam(value="encounterId", required=false, defaultValue="") Integer encounterId,
			@RequestParam(value="chartId", required=false, defaultValue="") Integer chartId) throws Exception {
		logger.debug("in getting pending orders in investigations");
		System.out.println("before request  for results "+System.currentTimeMillis());
        long starttime=System.currentTimeMillis();
//		List<LabEntries> labInfo = investigationService.findResultsLabs(chartId);
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(investigationService.findResultsLabs(chartId));
		 System.out.println("After request  for pendingresults "+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
        return emrResponseBean;
	}
	
	/**
	 * Method to get pending order list based on
	 * @param encounterId
	 * @return the entity LabEntries
	 * @throws Exception
	 */
	@RequestMapping(value = "/PendingOrders",method = RequestMethod.GET)
	public EMRResponseBean getPendingOrdersList(@RequestParam(value="encounterId", required=false, defaultValue="") Integer encounterId,
			@RequestParam(value="chartId", required=false, defaultValue="") Integer chartId) throws Exception {
		logger.debug("in getting pending orders in investigations");
//		List<LabEntries> labInfo = investigationService.findPendingOrders(encounterId, chartId);
		 System.out.println("before request  for pendingorders "+System.currentTimeMillis());
	        long starttime=System.currentTimeMillis();
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(investigationService.findPendingOrders(encounterId, chartId));
		System.out.println("After request  for pendingorders "+System.currentTimeMillis());
        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
        return emrResponseBean;
	}

	/**
	 * Method to update test status
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateTestStatus",method = RequestMethod.PUT)
	public EMRResponseBean updateTestStatus(@RequestParam(value="saveObject", required=false, defaultValue="") String saveObject) throws Exception {
		logger.debug("in updating test status");
		saveObject = URLDecoder.decode(saveObject, "UTF-8");
		String testDetailIds = saveObject.split("#@@#")[0];
		String encounterId = saveObject.split("#@@#")[1];
		String currentEnId = saveObject.split("#@@#")[2];
		String chartId = saveObject.split("#@@#")[3];
		String testStatus = saveObject.split("#@@#")[4];
		System.out.println("before request  for performing delete lab "+System.currentTimeMillis());
		long starttime=System.currentTimeMillis();
		investigationService.performOrDeleteLab(testDetailIds , encounterId.split("@##@")[0], testStatus);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,
				AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,
				"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,
				AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		 System.out.println("After request  for flowsheet "+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
//		List<LabEntries> labs = null;
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		if( encounterId.split("@##@")[1].equalsIgnoreCase("Pending")) {
//			labs = getPendingOrdersList(Integer.parseInt(currentEnId), Integer.parseInt(chartId));
			System.out.println("before request  for findpendingorders "+System.currentTimeMillis());
			long starttime1=System.currentTimeMillis();
			emrResponseBean.setData(investigationService.findPendingOrders(Integer.parseInt(currentEnId), Integer.parseInt(chartId)));
			System.out.println("After request  for findpendingorders "+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime1));
		} else if( encounterId.split("@##@")[1].equalsIgnoreCase("Todays")) {
//			labs = getTodaysOrdersList(Integer.parseInt(currentEnId));
			System.out.println("before request  for findtodayorders "+System.currentTimeMillis());
			long starttime1=System.currentTimeMillis();
			emrResponseBean.setData(investigationService.findTodaysOrders(Integer.parseInt(currentEnId)));
			System.out.println("After request  for findtodayorders "+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime1));
		}
		emrResponseBean.setLogin(true);
		return emrResponseBean;
	}
	/**
	 * Method to get body site information
	 * @param searchStr
	 * @param limit
	 * @param offset
	 * @return siteInfo
	 * @throws Exception
	 */
	@RequestMapping(value = "/BodySite",method = RequestMethod.GET)
	public EMRResponseBean getBodySiteData(@RequestParam(value="searchStr", required=false, defaultValue="") String searchStr,
			@RequestParam(value="limit", required=false, defaultValue="") Integer limit,
			@RequestParam(value="offset", required=false, defaultValue="") Integer offset) throws Exception {
		logger.debug("in getting body site details");
//		Page<BodySite> siteInfo = investigationService.findBodySiteData(searchStr, limit, offset);
		System.out.println("before request  for body site details "+System.currentTimeMillis());
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(investigationService.findBodySiteData(searchStr, limit, offset));
		   long starttime=System.currentTimeMillis();
	         System.out.println("After request  for body site details "+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
        return emrResponseBean;
	}
	
	/**
	 * Method to get vaccine details which has to take consent
	 * @return the entity LabDescription
	 * @throws Exception
	 */
	@RequestMapping(value = "/LotInfo",method = RequestMethod.GET)
	public EMRResponseBean getLotNumberData(@RequestParam(value="vaccineId", required=false, defaultValue="") Integer vaccineId, 
			@RequestParam(value="onLoad", required=false, defaultValue="") String onLoad) throws Exception {
		logger.debug("in getting lot number details");
		
		vaccineId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull("" + vaccineId)).or("-1"));
		System.out.println("before request  for LotInfo "+System.currentTimeMillis());
		long starttime=System.currentTimeMillis();
//		List<VaccineOrderDetails> lotDeatils = investigationService.findLotDetails(vaccineId,onLoad);
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(investigationService.findLotDetails(vaccineId,onLoad));
		 System.out.println("After request  for LotInfo "+System.currentTimeMillis());
	       System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
        return emrResponseBean;
	}
	
	/**
	 * Method to get frequent orders list
	 * @return the entity LabDescription
	 * @throws Exception
	 */
	@RequestMapping(value = "/FrequentOrders",method = RequestMethod.GET)
	public EMRResponseBean getFrequentOrdersList(@RequestParam(value="encounterId", required=false, defaultValue="") Integer encounterId) throws Exception {
		logger.debug("in getting frequent orders list");
//		List<FrequentOrders> frequentOrders = investigationService.findFrequentOrders(encounterId);
		System.out.println("before request  for frequentOrders "+System.currentTimeMillis());
		long starttime=System.currentTimeMillis();
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(investigationService.findFrequentOrders(encounterId));
		
        System.out.println("After request  for frequentOrders "+System.currentTimeMillis());
       System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
        return emrResponseBean;
	}
		
	/**
	 * Method to get vaccine details which has to take consent
	 * @return the entity LabDescription
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveConsent",method = RequestMethod.POST)
	@ResponseBody
	public List<VaccinationConsentForm> saveVaccineInfo(@RequestBody ConsentDetails consentData) throws Exception {
		logger.debug("in saving consent details vaccine");
		System.out.println("before request  for saveconsent "+System.currentTimeMillis());
		long starttime=System.currentTimeMillis();
		List<VaccinationConsentForm> consentList = investigationService.saveVaccineConsent(consentData);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,
				AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,
				"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,
				AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		 System.out.println("After request  for saveconsent "+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
		return consentList;
	}
		
	/**
	 * Method to get todays order list based on
	 * @param encounterId
	 * @return the entity LabEntries
	 * @throws Exception
	 */
	@RequestMapping(value = "/TestDetails",method = RequestMethod.GET)
	public EMRResponseBean getCompleteTestDetails(@RequestParam(value="testDetailId", required=false, defaultValue="") Integer testDetailId,
			@RequestParam(value="testId", required=false, defaultValue="") Integer testId, @RequestParam(value="groupId", required=false, defaultValue="") String groupId) throws Exception {
		logger.debug("in getting complete test details");
//		Orders labInfo = investigationService.findCompleteTestDetails(testDetailId, testId, groupId);
		System.out.println("before request  for TestDetails "+System.currentTimeMillis());
		long starttime=System.currentTimeMillis();
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(investigationService.findCompleteTestDetails(testDetailId, testId, groupId));
		   
	         System.out.println("After request  for TestDetails "+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
        return emrResponseBean;
	}
	
	/**
	 * Method to get todays order list based on
	 * @param encounterId
	 * @return the entity LabEntries
	 * @throws Exception
	 */
	@RequestMapping(value = "/TodaysOrders",method = RequestMethod.GET)
	public EMRResponseBean getTodaysOrdersList(@RequestParam(value="encounterId", required=false, defaultValue="") Integer encounterId) throws Exception {
		logger.debug("in getting todays orders in investigations");
//		List<LabEntries> labInfo = investigationService.findTodaysOrders(encounterId);
		System.out.println("before request  for getting todaysorders "+System.currentTimeMillis());
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		
		emrResponseBean.setData(investigationService.findTodaysOrders(encounterId));
	       long starttime=System.currentTimeMillis();
	         System.out.println("After request  for todayOrders "+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
        return emrResponseBean;
        
	}
	
	/**
	 * Request to save the details of an order
	 * @param labDetails
	 * @return the message
	 * @throws Exception
	 */
	@ApiOperation(value = "Save the details of an order", notes = "Save the details of an order")
	@RequestMapping(value = "/SaveTestDetails",method = RequestMethod.POST)
	public String saveTestDetails(@RequestBody SaveLabDetails labDetails) throws Exception {
		logger.debug("Begin of save the complete details for a particular lab");
		System.out.println("before request  for Investigation "+System.currentTimeMillis());
		long starttime=System.currentTimeMillis();
		investigationService.savelab(labDetails.getRequestToSave(), labDetails.getEncounterId(), labDetails.getPatientId(), labDetails.getChartId(), labDetails.getUserId(), labDetails.getFullData(), "-1", "-1", "false", "" + labDetails.getTestId());
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Investigations,AuditLogConstants.Investigations,
				1,AuditLogConstants.SUCCESS,"Successfully save data for the patient having PatientId=" + labDetails.getPatientId(),
				-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Investigations,request,
				"Successfully save data for the patient having PatientId=" + labDetails.getPatientId());
		logger.debug("End of save the complete details for a particular lab");
		System.out.println("After request  for Investigation "+System.currentTimeMillis());
        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
		return "success";
	}
	
	@ApiOperation(value = "on saving new order", notes = "on saving new order")
	@RequestMapping(value = "/SaveNewOrder", method = RequestMethod.GET)
	public String saveNewOrder(@RequestParam(value = "encounterId", required = false, defaultValue = "") Integer encounterId,
			@RequestParam(value = "chartId", required = false, defaultValue = "") Integer chartId,
			@RequestParam(value = "patientId", required = false, defaultValue = "") Integer patientId,
			@RequestParam(value = "labTestIds", required = false, defaultValue = "") String labTestIds,
			@RequestParam(value = "userId", required = false, defaultValue = "-1") int userId)
			throws Exception {
		String testIdArray[] = labTestIds.split("','");
		for (int i = 1; i < testIdArray.length; i++) {
			  System.out.println("before request  for getnewtestdetails "+System.currentTimeMillis());
		        long starttime=System.currentTimeMillis();
			LabDescription testDetails = investigationService.getNewTestDetails(Integer.parseInt(testIdArray[i]));
			System.out.println("After request  for getnewtestdetail "+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
	        System.out.println("before request  for saveneworder "+System.currentTimeMillis());
	        long starttime1=System.currentTimeMillis();
			investigationService.saveNewLab(testDetails, encounterId, Integer.parseInt(testIdArray[i]),userId, chartId, patientId);
			System.out.println("After request  for saveneworder "+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime1));
		}
		return "success";
	}
	
	@ApiOperation(value = "Get the complete details for a patient", notes = "Get the complete details for a patient")
	@RequestMapping(value = "/OrdersHistoryByChart",method = RequestMethod.GET)
	public EMRResponseBean getLabDetailsByChart(@RequestParam(value="chartId", required=false, defaultValue="-1") Integer chartId) throws Exception {
		logger.debug("Get the complete details for a patient");
//		LS_Bean labInfo = investigationService.findPatientLabDataByChart(chartId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Investigations,AuditLogConstants.Investigations,
				1,AuditLogConstants.SUCCESS,"Successfully loaded data for patient having chart id="+chartId,-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Investigations,request,
				"Successfully loaded data for patient having chart id="+chartId);
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(investigationService.findPatientLabDataByChart(chartId));
        return emrResponseBean;
	}
	
	@ApiOperation(value = "Get the complete details for a patient", notes = "Get the complete details for a patient")
	@RequestMapping(value = "/OrdersLogByChart",method = RequestMethod.GET)
	public EMRResponseBean getOrderLogByChart(@RequestParam(value="chartId", required=false, defaultValue="-1") Integer chartId) throws Exception {
//		OrderLogGroups labInfo = investigationService.findOrdersSummary(chartId);
		
		 System.out.println("before request  for orderHistory "+System.currentTimeMillis());
	        long starttime=System.currentTimeMillis();EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(investigationService.findOrdersSummary(chartId));
		 System.out.println("After request  for orderset "+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
        return emrResponseBean;
	}
	
	@ApiOperation(value = "Get the complete reviewed lab list for a patient", notes = "Get the complete reviewed lab list for a patient")
	@ApiResponses(value={@ApiResponse(code = 200, message = "Successful retrieval of reviewed orders for a patient"),
            @ApiResponse(code = 404, message = "Labs with given chart id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
	@ApiParam(name="chartId", value="id for each patient chart")
	@RequestMapping(value = "/ReviewedLogByChart",method = RequestMethod.GET)
	public EMRResponseBean getReviewedLogByChart(@RequestParam(value="chartId", required=false, defaultValue="-1") Integer chartId) throws Exception {
//		OrderLogGroups labInfo = investigationService.findReviewedSummary(chartId);
		  System.out.println("before request  for ReviewedLogByChart "+System.currentTimeMillis());
	        long starttime=System.currentTimeMillis();
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(investigationService.findReviewedSummary(chartId));
		 System.out.println("After request  for ReviewedLogByChart "+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
        return emrResponseBean;
	}
	
	@ApiOperation(value = "Get the complete pending orders for a patient", notes = "Get the complete pending orders for a patient")
	@ApiResponses(value={@ApiResponse(code = 200, message = "Successful retrieval of pending orders for a patient"),
            @ApiResponse(code = 404, message = "Labs with given chart id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
	@ApiParam(name="chartId", value="id for each patient chart")
	@RequestMapping(value = "/PendingLogByChart",method = RequestMethod.GET)
	
	public EMRResponseBean getPendingLogByChart(@RequestParam(value="chartId", required=false, defaultValue="-1") Integer chartId) throws Exception {
//		OrderLogGroups labInfo = investigationService.findPendingSummary(chartId);
		  System.out.println("before request  for PendingLogByChart "+System.currentTimeMillis());
	        long starttime=System.currentTimeMillis();
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(investigationService.findPendingSummary(chartId));
		 System.out.println("After request  for PendingLogByChart "+System.currentTimeMillis());
	        System.out.println("Final time taken::::::::"+(System.currentTimeMillis()-starttime));
        return emrResponseBean;
	}
	
	@ApiOperation(value = "Get the complete parameter details for a patient in between the selected dates", notes = "Get the complete parameter details for a patient in between the selected dates")
	@RequestMapping(value = "/ParamDataByDate",method = RequestMethod.GET)
	public EMRResponseBean getLabDetailsByDate(@RequestParam(value="chartId", required=false, defaultValue="-1") Integer chartId,
			@RequestParam(value="fromDate", required=false, defaultValue="") String fromDate,
			@RequestParam(value="toDate", required=false, defaultValue="") String toDate) throws Exception {
		logger.debug("Get the complete details for a patient");
//		LS_Bean labInfo = investigationService.getResultsByDate(chartId, fromDate, toDate);
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(investigationService.getResultsByDate(chartId, fromDate, toDate));
        return emrResponseBean;
	}
	
	@ApiOperation(value = "Get the complete details based on category", notes = "Get the complete details based on category")
	@RequestMapping(value = "/OrdersHistoryByCategory",method = RequestMethod.GET)
	public EMRResponseBean getLabDetailsByCategory(@RequestParam(value="chartId", required=false, defaultValue="-1") Integer chartId,
			@RequestParam(value="categoryId", required=false, defaultValue="-1") Integer catetoryId) throws Exception {
		logger.debug("Get the complete details for a patient");
//		LS_Bean labInfo = investigationService.findPatientLabDataByCategory(chartId,catetoryId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Investigations,AuditLogConstants.Investigations,
				1,AuditLogConstants.SUCCESS,"Successfully loaded data for patient having chart id="+chartId+" and category id="+catetoryId,-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Investigations,request,
				"Successfully loaded data for patient having chart id="+chartId+" and category id="+catetoryId);
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(investigationService.findPatientLabDataByCategory(chartId,catetoryId));
        return emrResponseBean;
	}
	
	@ApiOperation(value = "Get the complete details based on test", notes = "Get the complete details based on test")
	@RequestMapping(value = "/OrdersHistoryByTest",method = RequestMethod.GET)
	public EMRResponseBean getLabDetailsByTest(@RequestParam(value="chartId", required=false, defaultValue="-1") Integer chartId,
			@RequestParam(value="testId", required=false, defaultValue="-1") Integer testId) throws Exception {
		logger.debug("Get the complete details for a patient");
//		LS_Bean labInfo = investigationService.findPatientLabDataByTest(chartId, testId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Investigations,AuditLogConstants.Investigations,
				1,AuditLogConstants.SUCCESS,"Successfully loaded data for patient having chart id="+chartId+" and test id="+testId,-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Investigations,request,
				"Successfully loaded data for patient having chart id="+chartId+" and test id="+testId);
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(investigationService.findPatientLabDataByTest(chartId, testId));
        return emrResponseBean;
	}
}