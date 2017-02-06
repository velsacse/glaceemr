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
    @ResponseBody
	public EMRResponseBean getPharmacy( @RequestParam(value="patientId",required=false,defaultValue="-1")int patientId)throws Exception
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
    @ResponseBody
	public EMRResponseBean getERXSummaryData( @RequestParam(value="patientId",required=false,defaultValue="-1")int patientId, @RequestParam(value="chartUserGroupId",required=false,defaultValue="-1")int chartUserGroupId,@RequestParam(value="encounterId",required=false,defaultValue="-1")int encounterId, @RequestParam(value="userId",required=false,defaultValue="-1")int userId, @RequestParam(value="pharmId",required=false,defaultValue="-1")int pharmId, @RequestParam(value="prescId",required=false,defaultValue="-1")String prescId, @RequestParam(value="pos",required=false,defaultValue="-1")int pos)throws Exception
	{
		if(encounterId==-1){
			encounterId=erxSummaryService.getMaxEncounterId(patientId);
		}
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
    @ResponseBody
	public EMRResponseBean getProviderDetails( @RequestParam(value="userId",required=false,defaultValue="-1")int userId, @RequestParam(value="chartUserGroupId",required=false,defaultValue="-1")int chartUserGroupId, @RequestParam(value="encounterId",required=false,defaultValue="-1")int encounterId, @RequestParam(value="patientId",required=false,defaultValue="-1")int patientId)throws Exception
	{
		if(encounterId==-1){
			encounterId=erxSummaryService.getMaxEncounterId(patientId);
		}
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
    @ResponseBody
	public EMRResponseBean getPrescData( @RequestParam(value="userId",required=false,defaultValue="-1")int userId, @RequestParam(value="patientId",required=false,defaultValue="-1")int patientId, @RequestParam(value="encounterId",required=false,defaultValue="-1")int encounterId)throws Exception
	{
		if(encounterId==-1){
			encounterId=erxSummaryService.getMaxEncounterId(patientId);
		}
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
    @ResponseBody
	public EMRResponseBean checkCS(@RequestParam(value="userId",required=false,defaultValue="-1")int userId, @RequestParam(value="MedicationNames",required=false,defaultValue="-1")String MedicationNames, @RequestParam(value="pharmacyId",required=false,defaultValue="-1")int pharmacyId)throws Exception
	{
		String prescData=erxSummaryService.checkCS(MedicationNames,userId,pharmacyId);
		EMRResponseBean dataBean = new EMRResponseBean();
		dataBean.setData(prescData);
		return dataBean;
	}
}