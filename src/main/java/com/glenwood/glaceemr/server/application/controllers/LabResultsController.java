package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.LabEntries;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.labresults.AttachLabData;
import com.glenwood.glaceemr.server.application.services.labresults.DocumentData;
import com.glenwood.glaceemr.server.application.services.labresults.LabResultsService;
import com.glenwood.glaceemr.server.application.services.labresults.ResultDetails;
import com.glenwood.glaceemr.server.application.services.labresults.ResultList;
import com.glenwood.glaceemr.server.application.services.labresults.SaveData;
import com.glenwood.glaceemr.server.application.services.labresults.UsersList;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


/**
 * @author yasodha
 *
 * Controller for Lab results in GlaceEMR, 
 * contains request to get the list of results, reviewed results.
 * and ability to filter results based on ordered date of the lab.
 */
@Api(value = "LabResultsController", description = "Contains the methods to get and save the results details.", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value = "/user/LabResults")
public class LabResultsController {
	
	@Autowired
	AuditTrailService auditTrailService;

	@Autowired
	SessionMap sessionMap;

	@Autowired
	HttpServletRequest request;

	@Autowired
	LabResultsService labResultsService;

	private Logger logger = Logger.getLogger(InvestigationSummaryController.class);
	
	/**
	 * Method to get users list
	 * @return usersList
	 * @throws Exception
	 */
	@ApiOperation(value = "Get List Of Users", notes = "To get the list of users.")
	@RequestMapping(value = "/ListOfUsers",method = RequestMethod.GET)
	public List<UsersList> getUsersList() throws Exception {
		logger.debug("in getting pending orders in investigations");
		List<UsersList> listOfUsers = labResultsService.getListOfUsers();		
		return listOfUsers;
	}
	
	/**
	 * Method to get todays orders
	 * @throws Exception
	 */
	@ApiOperation(value = "Get todays orders", notes = "To get the list of ordered labs.")
	@RequestMapping(value = "/TodaysOrders",method = RequestMethod.GET)
	public List<LabEntries> getTodaysOrders(@RequestParam(value="orderedOn", required=false, defaultValue="") String orderedOn,
			@RequestParam(value="patientId", required=false, defaultValue="") String patientId) throws Exception {
		logger.debug("in getting list of todays orders");
		List<LabEntries> orderedLabs = labResultsService.getOrderedList(orderedOn, patientId);
		return orderedLabs;
	}
	
	/**
	 * Method to get todays orders
	 * @throws Exception
	 */
	@ApiOperation(value = "Get pending orders", notes = "To get the list of pending labs.")
	@RequestMapping(value = "/PendingOrders",method = RequestMethod.GET)
	public List<LabEntries> getPendingOrders(@RequestParam(value="orderedOn", required=false, defaultValue="") String orderedOn,
			@RequestParam(value="patientId", required=false, defaultValue="") String patientId) throws Exception {
		logger.debug("in getting list od pending orders");
		List<LabEntries> pendingLabs = labResultsService.getPendingList(orderedOn, patientId);
		return pendingLabs;
	}
	
	/**
	 * Method to get todays orders
	 * @throws Exception
	 */
	@ApiOperation(value = "check the mapping", notes = "To verify the mappings of the lab.")
	@RequestMapping(value = "/LabMapping",method = RequestMethod.GET)
	public Boolean checkLabMapping(@RequestParam(value="patientId", required=false, defaultValue="") String patientId,
			@RequestParam(value="hl7Id", required=false, defaultValue="") String hl7Id,
			@RequestParam(value="testName", required=false, defaultValue="") String testName) throws Exception {
		logger.debug("in checking mapping for labs");
		Integer chartId = Integer.parseInt(labResultsService.getChartId(patientId));
		boolean isMapped = labResultsService.checkForMapping(chartId, Integer.parseInt(hl7Id), testName);
		return isMapped;
	}
	
	/**
	 * Method to get todays orders
	 * @throws Exception
	 */
	@ApiOperation(value = "delete unmapped lab", notes = "To delete the lab, if there is no mapping.")
	@RequestMapping(value = "/deleteLab",method = RequestMethod.GET)
	public String deleteLab(@RequestParam(value="hl7Id", required=false, defaultValue="") String hl7Id,
			@RequestParam(value="testName", required=false, defaultValue="") String testName, @RequestParam(value="orderedOn", required=false, defaultValue="") String orderedOn) throws Exception {
		logger.debug("in deleting unmapped labs");
		String result = labResultsService.deleteUnmappedLab(hl7Id, testName, orderedOn);
		return result;
	}
	
	/**
	 * Method to get list of results
	 * @throws Exception
	 */
	@ApiOperation(value = "Get list of results received for the user", notes = "To get the list of results received for the user.")
	@RequestMapping(value = "/ResultsList",method = RequestMethod.GET)
	public List<ResultList> getResultList(@RequestParam(value="doctorId", required=false, defaultValue="") String doctorId,
			@RequestParam(value="isReviewed", required=false, defaultValue="") String isReviewed,
			@RequestParam(value="orderedDate", required=false, defaultValue="") String orderedDate,
			@RequestParam(value="pageNo", required=false, defaultValue="") Integer pageNo,
			@RequestParam(value="pageSize", required=false, defaultValue="") Integer pageSize) throws Exception {
		logger.debug("in getting list of results");
		List<ResultList> orderSetData = labResultsService.getListOfResults(doctorId, isReviewed, orderedDate, pageNo, pageSize);
		return orderSetData;
	}
	
	/**
	 * Method to get patient result data
	 * @throws Exception
	 */
	@ApiOperation(value = "Get complete details of a received result", notes = "To get the complete details of the received result.")
	@RequestMapping(value = "/ResultsDetails",method = RequestMethod.GET)
	public ResultDetails getResultDetails(@RequestParam(value="hl7FileId", required=false, defaultValue="") String hl7FileId) {
		logger.debug("in getting complete details of each patient result");
		ResultDetails patientResults = labResultsService.getPatientResultData(hl7FileId);
		return patientResults;
	}
	
	/**
	 * Method to attach result to patient
	 * @throws Exception
	 */
	@ApiOperation(value = "Attach the result to a Patient", notes = "To attach the result to a Patient.")
	@RequestMapping(value = "/attachToPatient",method = RequestMethod.GET)
	public String attachResults(@RequestParam(value="hl7FileId", required=false, defaultValue="") String hl7FileId,
			@RequestParam(value="patientId", required=false, defaultValue="") String patientId,
			@RequestParam(value="userId", required=false, defaultValue="") String userId,
			@RequestParam(value="sharedFolderPath", required=false, defaultValue="") String sharedFodlerPath) {
		logger.debug("in attaching result to patient");
		labResultsService.attachToPatient(hl7FileId, patientId, userId, sharedFodlerPath);
		return "successfully attached";
	}
	
	@ApiOperation(value = "Get previous orders for a result", notes = "To get previous orders for a result.")
	@RequestMapping(value = "/getPreviousOrders", method = RequestMethod.GET)
	public ResultDetails getPreviousOrders(@RequestParam(value="hl7FileId", required=false, defaultValue="-1") String hl7FileId) {
		logger.debug("in getting previous orders");		
		return labResultsService.findPreviousOrders(hl7FileId);
	}
	
	/**
	 * Method to attach result to patient
	 * @throws Exception
	 */
	@ApiOperation(value = "Attach unknown result to a lab.", notes = "To attach unknown result to a lab.")
	@RequestMapping(value = "/attachToLabs",method = RequestMethod.PUT)
	public String attachLabs(@RequestBody AttachLabData attachLabData) throws Exception {
		logger.debug("in attaching result to ordered labs");
		labResultsService.attachToOrderedLabs(attachLabData);
		return "successfully attached";
	}
	
	@ApiOperation(value = "Save the details of a lab from Hl7", notes = "Save the details of a lab from Hl7")
	@RequestMapping(value = "/SaveLabData",method = RequestMethod.POST)
	public String saveLabData(@RequestBody SaveData labDetails) throws Exception {
		logger.debug("Begin of save the complete details for a particular lab from Hl7");
		labResultsService.saveLabDetails(labDetails);
		logger.debug("End of save the complete details for a particular lab");
		return "Successfully saved";
	}
	
	@ApiOperation(value = "Get documents data.", notes = "To get the document data which includes file name shared folder path, etc.,.")
	@RequestMapping(value = "/DocumentsData", method = RequestMethod.GET)
	public DocumentData getDocumentsInfo(@RequestParam(value="fileType", required=false, defaultValue="-1") String fileType,
			@RequestParam(value="patientId", required=false, defaultValue="-1") String patientId,
			@RequestParam(value="documentId", required=false, defaultValue="-1") String documentId,
			@RequestParam(value="sharedFolderPath", required=false, defaultValue="") String sharedFolderPath) {
		logger.debug("in getting document data");		
		return labResultsService.getDocumentData(fileType, patientId, documentId, sharedFolderPath);
	}
	
	@ApiOperation(value = "Review documents data.", notes = "To Review the received documents.")
	@RequestMapping(value = "/ReviewDocuments", method = RequestMethod.GET)
	public String reviewDocuments(@RequestParam(value="fileId", required=false, defaultValue="-1") String fileId,
			@RequestParam(value="userId", required=false, defaultValue="-1") String userId) {
		logger.debug("in reviewing documents");		
		return labResultsService.reviewFile(fileId, userId);
	}
	
	@ApiOperation(value = "Get PDF details.", notes = "To get the PDF details.")
	@RequestMapping(value = "/PdfInfo", method = RequestMethod.GET)
	public String getPDFData(@RequestParam(value="testDetailId", required=false, defaultValue="-1") String testDetailId) {
		logger.debug("in getting PDF name");		
		return labResultsService.getPDFName(testDetailId);
	}
}
