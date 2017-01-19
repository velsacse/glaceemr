package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.GrowthGraphVitalData;
import com.glenwood.glaceemr.server.application.models.GrowthGraphPatientData;
import com.glenwood.glaceemr.server.application.models.H650;
import com.glenwood.glaceemr.server.application.services.chart.growthgraph.GrowthGraphService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Controller for growth graph
 * @author Jeyanthkumar
 *
 */

@Api(value = "GrowthGraph", description = "To get list of vital for a patient and details about patient", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="/user/GrowthGraph")
public class GrowthGraphController {

	@Autowired
	GrowthGraphService growthGraphService;

	
	/**
	 * To get the default graph for the patient. (Based on patient's age)
	 * @param patientId
	 * @return default graph id
	 */
	@ApiOperation(value = "Get the default graph for the patient", notes = "Get the default graph for the patient based on patient's age")
	@RequestMapping(value = "/getdefaultgraphid", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of "),
		    @ApiResponse(code = 404, message = "when patient id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public EMRResponseBean defaultGraphId(
			@ApiParam(name="patientId",value="patient id") @RequestParam(value="patientid", required=false, defaultValue="true") String patientId){
		
			String defaultId=growthGraphService.getDefaultGraphId(patientId);
			EMRResponseBean result= new EMRResponseBean();
			result.setData(defaultId);
		return result;
	}
	
	/**
	 * To get patient details based on patient id
	 * @param patientId
	 * @return patient details from GrowthGraphPatientData bean.
	 */
	@RequestMapping(value = "/getpatientinfo", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of patient data"),
		    @ApiResponse(code = 404, message = "when patient id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public EMRResponseBean getpatientinfo(
			@ApiParam(name="patientId",value="patient id") @RequestParam(value="patientid", required=false, defaultValue="true") String patientId){
		
		GrowthGraphPatientData patientDetails=growthGraphService.getpatientinfo(patientId);
		EMRResponseBean result= new EMRResponseBean();
		result.setData(patientDetails);
		return result;
	}
	
	/**
	 * To get patient vital details based on patient id
	 * @param patientId
	 * @return list of patient vital details from GrowthGraphVitalData bean.
	 */
	@RequestMapping(value = "/getvitalvalues", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of graph vital data's"),
		    @ApiResponse(code = 404, message = "when patient id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public EMRResponseBean getVitalValues(
			@ApiParam(name="patientId",value="patient id") @RequestParam(value="patientid", required=false, defaultValue="true") String patientId,
			@ApiParam(name="wellvisit",value="well visit") @RequestParam(value="wellvisit", required=false, defaultValue="false") boolean wellvisit){
		
		List<GrowthGraphVitalData> patientDetails=growthGraphService.getVitalValues(patientId,wellvisit);
		EMRResponseBean result= new EMRResponseBean();
		result.setData(patientDetails);
		return result;
	}
	
	/**
	 * To get graph list based on patient id
	 * @param patientId
	 * @return list of graph details from h650 table
	 */
	@RequestMapping(value = "/getgraphlist", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of graph list"),
		    @ApiResponse(code = 404, message = "when patient id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public EMRResponseBean getGraphList(
			@ApiParam(name="patientId",value="patient id") @RequestParam(value="patientid", required=false, defaultValue="true") String patientId){
		
		List<H650> patientDetails=growthGraphService.getGraphList(patientId);
		EMRResponseBean result= new EMRResponseBean();
		result.setData(patientDetails);
		return result;
	}
}