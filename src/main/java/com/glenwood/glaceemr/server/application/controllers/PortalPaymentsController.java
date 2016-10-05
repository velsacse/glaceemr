package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.CreditCardPaymentBean;
import com.glenwood.glaceemr.server.application.models.H093;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PortalPatientPaymentsSummary;
import com.glenwood.glaceemr.server.application.models.PortalPatientStatementBean;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.ReceiptDetail;
import com.glenwood.glaceemr.server.application.services.portal.portalPayments.PortalPaymentsService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Transactional
@RequestMapping(value="/user/PortalPayments")
@Api(value="PortalPaymentsController", description="Handles all the requests regarding bill payment of"
		+ " a patient such as Bill Payment ,Oustanding Bill, Payment History, Statement History e.t.c.")
public class PortalPaymentsController {

	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	PortalPaymentsService portalPaymentsService;
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @param chartId 		: Required patient's chart id 
	 * @return List of patient's bill history
	 * @throws Exception
	 */
	@RequestMapping(value = "/Statements/History", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's Statement History", notes = "Returns a complete list of patient's Statement History.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's Payments History"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<H093> getPatientStatementHistory(@ApiParam(name="patientId", value="patient's id whose statement history is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="chartId", value="chart id whose statement history are to be retrieved") @RequestParam(value="chartId", required=false, defaultValue="") int chartId,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{
		
		return portalPaymentsService.getPatientStatementHistory(patientId, chartId, pageOffset, pageIndex);
	}
	
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @param chartId 		: Required patient's chart id 
	 * @return List of patient's bill history
	 * @throws Exception
	 */
	@RequestMapping(value = "/Payments/History", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's Bill Payment History", notes = "Returns a complete list of patient's Bill Payment History.", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's Bill Payment History"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<ReceiptDetail> getPatientPaymentHistory(@ApiParam(name="patientId", value="patient's id whose bill history is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="chartId", value="chart id whose bill history are to be retrieved") @RequestParam(value="chartId", required=false, defaultValue="") int chartId,
			@ApiParam(name="pageOffset", value="offset of the page") @RequestParam(value="pageOffset", required=false, defaultValue="5") int pageOffset,
			@ApiParam(name="pageIndex", value="index of the page") @RequestParam(value="pageIndex", required=false, defaultValue="0") int pageIndex) throws Exception{
		
		return portalPaymentsService.getPatientPaymentHistory(patientId, chartId, pageOffset, pageIndex);
	}
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @param chartId 		: Required patient's chart id 
	 * @return List of patient's bill history
	 * @throws Exception
	 */
	@RequestMapping(value = "/Payments/Summary", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's Bill Payment History", notes = "Returns a complete list of patient's payment summary details", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's payments summary details"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PortalPatientPaymentsSummary getPatientPaymentsDeails(@ApiParam(name="patientId", value="patient's id whose payments summary is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId) throws Exception{
		
		return portalPaymentsService.getPatientPaymentsSummary(patientId);
	}
	
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @return List of patient's insurances
	 * @throws Exception
	 */
	@RequestMapping(value = "/Payments/PatientInsurances", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's  Patient Insurances list", notes = "Returns a complete list of patient's Patient Insurances list", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's insurances list"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<PatientInsDetail> getPatientInsDetails(@ApiParam(name="patientId", value="patient's id whose payments summary is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId) throws Exception{
		
		return portalPaymentsService.getPatientInsDetails(patientId);
	}
	
	
	
	/**
	 * @param patientId : credit card payment details bean 
	 * @return Transaction summary details bean
	 * @throws Exception
	 */
	@RequestMapping(value = "/Payments/CreditCardPayment", method = RequestMethod.POST)
    @ApiOperation(value = "Returns patient's Bill Payment History", notes = "Returns a complete list of patient's payment summary details", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's payments summary details"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public  ReceiptDetail processPaymentTransaction(@RequestBody CreditCardPaymentBean paymentDetailsBean) throws Exception{
		
		return portalPaymentsService.processPaymentTransaction(paymentDetailsBean);
	}
	
	
	/**
	 * @param patientId 		: Required patient's id 
	 * @param patientId 		: Required billId 
	 * @param patientId 		: Required billType 
	 * @return List of patient's insurances
	 * @throws Exception
	 */
	@RequestMapping(value = "/Statements/PatientStatement", method = RequestMethod.GET)
    @ApiOperation(value = "Returns patient's statement details bean", notes = "Returns a complete list of patient's statement details bean", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's statement details bean"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public PortalPatientStatementBean getPatientStatementDetails(@ApiParam(name="patientId", value="patient's id whose  statement details bean is to be retrieved") @RequestParam(value="patientId", required=false, defaultValue="") int patientId,
			@ApiParam(name="billId", value="bill id of the statement details bean to be retrieved") @RequestParam(value="billId", required=false, defaultValue="") int billId,
			@ApiParam(name="billType", value="bill type of the statement details bean to be retrieved") @RequestParam(value="billType", required=false, defaultValue="") String billType) throws Exception{
		
		PortalPatientStatementBean portalPatientStatementBean=portalPaymentsService.getPatientStatementFileDetails(patientId, billId, billType);
		return portalPatientStatementBean;
	}
	
	/**
	 * @return List of TransFirst configured POS
	 * @throws Exception
	 */
	@RequestMapping(value = "/POS/TransFirst", method = RequestMethod.GET)
    @ApiOperation(value = "Returns list of TransFirst configured POS", notes = "Returns list of TransFirst configured POS", response = User.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient's statement details bean"),
		    @ApiResponse(code = 404, message = "Patient with given id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public List<PosTable> getTransFirstConfiguredPosList() throws Exception{
		
		return portalPaymentsService.getTransFirstConfiguredPosList();
	}
	
	
}
