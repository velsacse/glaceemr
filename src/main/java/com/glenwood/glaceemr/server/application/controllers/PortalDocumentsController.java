package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.FileDetails;
import com.glenwood.glaceemr.server.application.models.FileName;
import com.glenwood.glaceemr.server.application.models.PatientPortalSharedDocs;
import com.glenwood.glaceemr.server.application.models.PatientSharedDocumentLog;
import com.glenwood.glaceemr.server.application.services.portal.portalDocuments.PortalDocumentsService;
import com.glenwood.glaceemr.server.application.services.portal.portalDocuments.PortalFileDetailBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Transactional
@RequestMapping(value="/user/PortalPatientDocuments")
@Api(value="DocumentsController", description="Performs all actions related to patient documents")
public class PortalDocumentsController {
	
	@Autowired
	PortalDocumentsService documentsService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	/**
	 * Shared Documents list of a patient.
	 * @param patientId		: id of the required patient.
	 * @return List of shared documents of a patient.
	 */
	@RequestMapping(value = "/Documents/PatientSharedDocs", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's Appointments", notes = "Returns a list of shared documents of a patient.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of shared documents list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<PatientPortalSharedDocs> getPatientSharedDocs(@ApiParam(name="patientId", value="patient's id whose shared docs list is to be retrieved") @RequestParam(value="patientId", required=true, defaultValue="") int patientId,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{
		
		List<PatientPortalSharedDocs> sharedDocsList=documentsService.getPatientSharedDocs(patientId, pageOffset, pageIndex);
		
		return sharedDocsList;
	}
	
	
	
	/**
	 * Shared Documents list of a patient.
	 * @param patientId		: id of the required patient.
	 * @return List of shared documents of a patient.
	 */
	@RequestMapping(value = "/VisitSummary/PatientVisitSummary", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's Visit Summary", notes = "Returns Visit Summary of a patient.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of Visit Summary of a patient"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<PatientSharedDocumentLog> getPatientVisitSummary(@ApiParam(name="patientId", value="patient's id whose Visit Summary is to be retrieved") @RequestParam(value="patientId", required=true, defaultValue="") int patientId,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{
		
		List<PatientSharedDocumentLog> visitSummary=documentsService.getPatientVisitSummary(patientId, pageOffset, pageIndex);
		
		return visitSummary;
	}
	
	
	
	/**
	 * File Details of a Patient Document.
	 * @param patientId		: id of the required patient.
	 * @param fileId		: id of the file.
	 * @return File Details of a patient document.
	 */
	@RequestMapping(value = "/Documents/FileDetails", method = RequestMethod.GET)
    @ApiOperation(value = "Returns file details of a patient document.", notes = "Returns file details of a patient document.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of file details of a patient document"),
		    @ApiResponse(code = 404, message = "file with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public FileDetails getPatientFileDetails(@ApiParam(name="patientId", value="patient's id whose Visit Summary is to be retrieved") @RequestParam(value="patientId", required=true, defaultValue="") int patientId,
			@ApiParam(name="fileId", value="patient's id whose Visit Summary is to be retrieved") @RequestParam(value="fileId", required=true, defaultValue="") int fileId) throws Exception{
		
		FileDetails fileDetails=documentsService.getFileDetails(patientId, fileId);
		
		return fileDetails;
	}
	
	
	/**
	 * File Details of a Patient Document.
	 * @param patientId		: id of the required patient.
	 * @param fileId		: id of the file.
	 * @return File Details of a patient document.
	 */
	@RequestMapping(value = "/Documents/FileNamesList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns file details of a patient document.", notes = "Returns file details of a patient document.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of file details of a patient document"),
		    @ApiResponse(code = 404, message = "file with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<FileName> getPatientFileNameDetails(@ApiParam(name="patientId", value="patient's id whose Visit Summary is to be retrieved") @RequestParam(value="patientId", required=true, defaultValue="") int patientId,
			@ApiParam(name="fileDetailsId", value="patient's id whose Visit Summary is to be retrieved") @RequestParam(value="fileDetailsId", required=true, defaultValue="") int fileDetailsId) throws Exception{
		
		List<FileName> fileNameDetails=documentsService.getFileNameDetails(patientId, fileDetailsId);
		
		return fileNameDetails;
	}
	
	/**
	 * File Details of a Patient Document.
	 * @param patientId		: id of the required patient.
	 * @param fileId		: id of the file.
	 * @return File Details of a patient document.
	 */
	@RequestMapping(value = "/VisitSummary/PatientCDA", method = RequestMethod.GET)
    @ApiOperation(value = "Returns file details of a patient CDA file details.", notes = "Returns file details of a patient CDA file details.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of file details of a patient CDA file details"),
		    @ApiResponse(code = 404, message = "file with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PortalFileDetailBean getPatientCDAFileDetails(@ApiParam(name="patientId", value="patient's id whose Visit Summary is to be retrieved") @RequestParam(value="patientId", required=true, defaultValue="") int patientId,
			@ApiParam(name="encounterId", value="patient's encounter id whose Visit Summary is to be retrieved") @RequestParam(value="encounterId", required=true, defaultValue="") int encounterId,
			@ApiParam(name="fileName", value="fileName of the cda raw(XML) data") @RequestParam(value="fileName", required=true, defaultValue="") String fileName) throws Exception{
		
		PortalFileDetailBean fileDetails=documentsService.getPatientCDAFileDetails(patientId, encounterId, fileName);
		
		return fileDetails;
	}
	

}
