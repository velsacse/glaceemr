package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PharmDetails;
import com.glenwood.glaceemr.server.application.models.ReceiptDetail;
import com.glenwood.glaceemr.server.application.services.chart.CurrentMedication.ActiveMedicationsBean;
import com.glenwood.glaceemr.server.application.services.chart.CurrentMedication.CurrentMedDataBean;
import com.glenwood.glaceemr.server.application.services.chart.CurrentMedication.CurrentMedicationService;
import com.glenwood.glaceemr.server.application.services.chart.CurrentMedication.EncounterDataBean;
import com.glenwood.glaceemr.server.application.services.chart.CurrentMedication.InactiveMedBean;
import com.glenwood.glaceemr.server.application.services.chart.CurrentMedication.MedicationDetailBean;
import com.glenwood.glaceemr.server.application.services.chart.CurrentMedication.PatientAllergiesBean;
import com.glenwood.glaceemr.server.application.services.chart.CurrentMedication.SearchBean;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


@Api(value = "/user/CurrentMedication", description = "To get all the details in current medication page", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value = "/user/CurrentMedication")
public class CurrentMedicationController {
		
	@Autowired
	CurrentMedicationService currentMedicationService;
	
	/**
	 *  
	 * @return Getting patient allergies data
	 * @param chartId
	 * @throws Exception
	 */
	@RequestMapping(value ="/allergyData", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of Allergy data"),
            @ApiResponse(code = 404, message = "when chart id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean getAllergyData(@ApiParam(name="chartId", value="chartId") @RequestParam(value="chartId",required=false,defaultValue="-1")int chartId)throws Exception
	{
		List<PatientAllergiesBean> allergies=currentMedicationService.getAllergies(chartId);
		EMRResponseBean allergyList = new EMRResponseBean();
		allergyList.setData(allergies);
		return allergyList;
	}
	
	
	/**
	 *  
	 * @return Getting Patient pharmacy details
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
		PharmDetails pharmData=currentMedicationService.getPharmacy(patientId);
		EMRResponseBean dataBean = new EMRResponseBean();
		dataBean.setData(pharmData);
		return dataBean;
	}
	
	
	/**
	 *  
	 * @return Getting Patient Encounter and copay details
	 * @param patientId
	 * @param encounterId
	 * @throws Exception
	 */
	@RequestMapping(value ="/encounterData", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of encounter data"),
            @ApiResponse(code = 404, message = "when patient id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean getEncounterData(@ApiParam(name="patientId", value="patientId") @RequestParam(value="patientId",required=false,defaultValue="-1")int patientId,@ApiParam(name="encounterId", value="encounterId") @RequestParam(value="encounterId",required=false,defaultValue="-1")int encounterId)throws Exception
	{
		List<Encounter> encData=currentMedicationService.getEncounterData(encounterId);
		PatientRegistration copay=currentMedicationService.getCopay(patientId);
		List<ReceiptDetail> copayment=currentMedicationService.getCopayment(encounterId,patientId);
		EncounterDataBean resultBean= new EncounterDataBean(encData,copay,copayment);
		EMRResponseBean dataBean = new EMRResponseBean();
		dataBean.setData(resultBean);
		return dataBean;
	}
	
	
	/**
	 *  
	 * @return Getting Patient current medications,transition of care and summary care details 
	 * @param patientId
	 * @param encounterId
	 * @param userId
	 * @throws Exception
	 */
	@RequestMapping(value ="/currentMedData", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of current medication data"),
            @ApiResponse(code = 404, message = "when patient id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean getCurrentMedData(@ApiParam(name="patientId", value="patientId") @RequestParam(value="patientId",required=false,defaultValue="-1")int patientId,@ApiParam(name="encounterId", value="encounterId") @RequestParam(value="encounterId",required=false,defaultValue="-1")int encounterId,@ApiParam(name="userId", value="userId") @RequestParam(value="userId",required=false,defaultValue="-1")int userId)throws Exception
	{
		int locEncounterId=encounterId;
		Chart chartDetails=currentMedicationService.getchartDetails(patientId);
		if(encounterId==-1){
		Encounter enc=currentMedicationService.getMaxEnc(chartDetails.getChartId().toString(),patientId,userId);
		locEncounterId=enc.getEncounterId();
		}
		List<MedicationDetailBean> medDetails=currentMedicationService.getMedDetails(locEncounterId,patientId);
		Encounter transitionSummaryCare=currentMedicationService.getTransitionSummaryCare(locEncounterId);
		Boolean summaryOfCare=false;
		Boolean transitionOfCare=false;
		if(transitionSummaryCare!=null){
			summaryOfCare=transitionSummaryCare.getSummaryOfCare();
			transitionOfCare=transitionSummaryCare.getTransitionOfCare();
		}
		Boolean noMedicationFlag=false;
		if(chartDetails!=null)
			noMedicationFlag=chartDetails.getNomedication();
		CurrentMedDataBean bean=new CurrentMedDataBean(transitionOfCare,summaryOfCare,noMedicationFlag,medDetails);
		EMRResponseBean dataBean = new EMRResponseBean();
		dataBean.setData(bean);
		return dataBean;
	}
	
	/**
	 *  
	 * @return Getting Active medications of the patient
	 * @param patientId
	 * @throws Exception
	 */
	@RequestMapping(value ="/activeMed", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of encounter data"),
            @ApiResponse(code = 404, message = "when patient id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean getActiveMedications(@ApiParam(name="patientId", value="patientId") @RequestParam(value="patientId",required=false,defaultValue="-1")int patientId)throws Exception
	{
		List<ActiveMedicationsBean> currentMeds =currentMedicationService.getActiveCurrentMeds(patientId);
		List<ActiveMedicationsBean> prescriptionMeds=currentMedicationService.getActivePrescMeds(patientId);
		for(int i=0;i<prescriptionMeds.size();i++){
			currentMeds.add(prescriptionMeds.get(i));
		}
		EMRResponseBean dataBean = new EMRResponseBean();
		dataBean.setData(currentMeds);
		return dataBean;
	}
	
	/**
	 *  
	 * @return Getting Inactive medications of the patient
	 * @param patientId
	 * @throws Exception
	 */
	@RequestMapping(value ="/inActiveMed", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of encounter data"),
            @ApiResponse(code = 404, message = "when patient id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean getInActiveMedications(@ApiParam(name="patientId", value="patientId") @RequestParam(value="patientId",required=false,defaultValue="-1")int patientId)throws Exception
	{
		List<InactiveMedBean> currentMeds =currentMedicationService.getInActiveCurrentMeds(patientId);
		EMRResponseBean dataBean = new EMRResponseBean();
		dataBean.setData(currentMeds);
		return dataBean;
	}
	
	/**
	 *  
	 * @return Getting medication list based on search keyword
	 * @param keyword
	 * @param prescriberspecific
	 * @param mapid
	 * @param userId
	 * @param offset
	 * @param limit
	 * @throws Exception
	 */
	@RequestMapping(value ="/medSearch", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of encounter data"),
            @ApiResponse(code = 404, message = "when patient id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean getMedSearchDetails(@ApiParam(name="keyword", value="keyword") @RequestParam(value="keyword",required=false,defaultValue="-1")String keyword, @ApiParam(name="prescriberspecific", value="prescriberspecific") @RequestParam(value="prescriberspecific",required=false,defaultValue="false")String prescriberspecific, @ApiParam(name="mapid", value="mapid") @RequestParam(value="mapid",required=false,defaultValue="-1")String mapid, @ApiParam(name="userId", value="userId") @RequestParam(value="userId",required=false,defaultValue="-1")int userId, @ApiParam(name="offset", value="offset") @RequestParam(value="offset",required=false,defaultValue="-1")int offset,@ApiParam(name="limit", value="limit") @RequestParam(value="limit",required=false,defaultValue="-1")int limit)throws Exception
	{
		List<SearchBean> drugList=currentMedicationService.getsearchData(keyword,prescriberspecific,mapid,userId,offset,limit);
		EMRResponseBean dataBean = new EMRResponseBean();
		dataBean.setData(drugList);
		return dataBean;
	}
}
