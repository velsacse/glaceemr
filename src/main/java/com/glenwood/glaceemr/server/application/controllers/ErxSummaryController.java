package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.chart.ErxSummary.DoctorDetailBean;
import com.glenwood.glaceemr.server.application.services.chart.ErxSummary.ErxDataBean;
import com.glenwood.glaceemr.server.application.services.chart.ErxSummary.ErxPatientDataBean;
import com.glenwood.glaceemr.server.application.services.chart.ErxSummary.ErxSummaryService;
import com.glenwood.glaceemr.server.application.services.chart.ErxSummary.NewRxBean;
import com.glenwood.glaceemr.server.application.services.chart.ErxSummary.PharmacyBean;
import com.glenwood.glaceemr.server.application.services.chart.ErxSummary.PrescribedMedBean;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Api(value = "/user/ErxSummary", description = "To get all the details for Erx summary popup", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value = "/user/ErxSummary")
public class ErxSummaryController {

	@Autowired
	ErxSummaryService erxSummaryService;
	
	/**
	 *  
	 * @return getting Patient pharmacy details
	 * @param patientId
	 * @throws Exception
	 */
	@RequestMapping(value ="/pharmacyData", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of Pharmacy data"),
            @ApiResponse(code = 404, message = "when patient id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean getPharmacy(@ApiParam(name="patientId", value="patientId") @RequestParam(value="patientId",required=false,defaultValue="-1")int patientId)throws Exception
	{
		PharmacyBean pharmData=erxSummaryService.getPatientPharmacy(patientId);
		EMRResponseBean dataBean = new EMRResponseBean();
		dataBean.setData(pharmData);
		return dataBean;
	}
	
	/**
	 *  
	 * @return getting Erx summary popup details
	 * @param patientId
	 * @param chartUserGroupId
	 * @param encounterId
	 * @param userId
	 * @param pharmId
	 * @param prescId
	 * @param pos
	 * @throws Exception
	 */
	@RequestMapping(value ="/ERXSummaryData", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of Provider data"),
            @ApiResponse(code = 404, message = "when patient id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean getERXSummaryData(@ApiParam(name="patientId", value="patientId") @RequestParam(value="patientId",required=false,defaultValue="-1")int patientId,@ApiParam(name="chartUserGroupId", value="chartUserGroupId") @RequestParam(value="chartUserGroupId",required=false,defaultValue="-1")int chartUserGroupId,@ApiParam(name="encounterId", value="encounterId") @RequestParam(value="encounterId",required=false,defaultValue="-1")int encounterId,@ApiParam(name="userId", value="userId") @RequestParam(value="userId",required=false,defaultValue="-1")int userId,@ApiParam(name="pharmId", value="pharmId") @RequestParam(value="pharmId",required=false,defaultValue="-1")int pharmId,@ApiParam(name="prescId", value="prescId") @RequestParam(value="prescId",required=false,defaultValue="-1")String prescId,@ApiParam(name="pos", value="pos") @RequestParam(value="pos",required=false,defaultValue="-1")int pos)throws Exception
	{
		
		List<ErxPatientDataBean> patientData=erxSummaryService.getPatientData(patientId);
		List<DoctorDetailBean> providerData=erxSummaryService.getDoctorDetails(encounterId,userId,chartUserGroupId,pos);
		List<PharmacyBean> pharmData=erxSummaryService.getPharmDetails(pharmId);
		List<NewRxBean> medDetails=erxSummaryService.getNewRxDetails(encounterId,prescId);
		ErxDataBean finalData=new ErxDataBean(patientData,providerData,pharmData,medDetails);
		EMRResponseBean dataBean = new EMRResponseBean();
		dataBean.setData(finalData);
		return dataBean;
	}
	
	
	/**
	 *  
	 * @return getting Provider details
	 * @param chartUserGroupId
	 * @param encounterId
	 * @param userId
	 * @throws Exception
	 */
	@RequestMapping(value ="/providerData", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of Provider data"),
            @ApiResponse(code = 404, message = "when patient id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean getProviderDetails(@ApiParam(name="userId", value="userId") @RequestParam(value="userId",required=false,defaultValue="-1")int userId,@ApiParam(name="chartUserGroupId", value="chartUserGroupId") @RequestParam(value="chartUserGroupId",required=false,defaultValue="-1")int chartUserGroupId,@ApiParam(name="encounterId", value="encounterId") @RequestParam(value="encounterId",required=false,defaultValue="-1")int encounterId)throws Exception
	{
		String poviderData=erxSummaryService.getProvider(userId,chartUserGroupId,encounterId);
		EMRResponseBean dataBean = new EMRResponseBean();
		dataBean.setData(poviderData);
		return dataBean;
	}
	
	/**
	 *  
	 * @return getting Prescribed medication details of currenrt encounter
	 * @param patientId
	 * @param encounterId
	 * @param userId
	 * @throws Exception
	 */
	@RequestMapping(value ="/PrescData", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of Provider data"),
            @ApiResponse(code = 404, message = "when patient id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean getPrescData(@ApiParam(name="userId", value="userId") @RequestParam(value="userId",required=false,defaultValue="-1")int userId,@ApiParam(name="patientId", value="patientId") @RequestParam(value="patientId",required=false,defaultValue="-1")int patientId,@ApiParam(name="encounterId", value="encounterId") @RequestParam(value="encounterId",required=false,defaultValue="-1")int encounterId)throws Exception
	{
		List<PrescribedMedBean> prescData=erxSummaryService.getPrescData(encounterId);
		EMRResponseBean dataBean = new EMRResponseBean();
		dataBean.setData(prescData);
		return dataBean;
	}
	
	
	/**
	 *  
	 * @return controlled substances check
	 * @param pharmacyId
	 * @param MedicationNames
	 * @param userId
	 * @throws Exception
	 */
	@RequestMapping(value ="/checkControlledSubstance", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of Provider data"),
            @ApiResponse(code = 404, message = "when patient id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean checkCS(@ApiParam(name="userId", value="userId") @RequestParam(value="userId",required=false,defaultValue="-1")int userId,@ApiParam(name="MedicationNames", value="MedicationNames") @RequestParam(value="MedicationNames",required=false,defaultValue="-1")String MedicationNames,@ApiParam(name="pharmacyId", value="pharmacyId") @RequestParam(value="pharmacyId",required=false,defaultValue="-1")int pharmacyId)throws Exception
	{
		String prescData=erxSummaryService.checkCS(MedicationNames,userId,pharmacyId);
		EMRResponseBean dataBean = new EMRResponseBean();
		dataBean.setData(prescData);
		return dataBean;
	}
}
