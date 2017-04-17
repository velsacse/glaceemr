package com.glenwood.glaceemr.server.application.controllers;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.Bean.DiagnosisList;
import com.glenwood.glaceemr.server.application.Bean.MIPSPatientInformation;
import com.glenwood.glaceemr.server.application.models.MacraProviderConfiguration;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresProviderMapping;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.QPPConfigurationService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;


@RestController
@Transactional
@RequestMapping(value = "/user/QPPConfiguration")
public class QPPConfigurationController {
	@Autowired
	QPPConfigurationService QppConfigurationService;
	
	@RequestMapping(value = "/saveConfDetails", method = RequestMethod.GET)
	@ResponseBody
	public void saveConfDetails(
			@RequestParam(value = "prgmYear", required = true) Integer programYear,
			@RequestParam(value = "type", required = true) Integer type,
			@RequestParam(value = "providerId", required = false, defaultValue = "-1") Integer providerId,
			@RequestParam(value = "startDate", required = true) String startDate,
			@RequestParam(value = "endDate", required = true) String endDate,
			@RequestParam(value = "submissionMtd", required = true) Integer submissionMtd)
			throws Exception {
		
			SimpleDateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy");
			java.util.Date StartDate = originalFormat.parse(startDate);
			java.util.Date EndDate = originalFormat.parse(endDate);
			QppConfigurationService.saveConfDetails(programYear, type, providerId, StartDate, EndDate, submissionMtd);
	

}
	
	@RequestMapping(value = "/getProviderInfo", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getProviderInfo(
			@RequestParam(value = "provider", required = true) Integer provider)throws Exception {
		EMRResponseBean result=new EMRResponseBean();
		if(!provider.equals(null)){
		//	Integer providerId=QppConfigurationService.getProviderId(provider);
		List<MacraProviderConfiguration> groupData=QppConfigurationService.getProviderInfo(provider);
		
		result.setData(groupData);
		
		}
		return result;
	}
	
	@RequestMapping(value = "/getMeasureIds", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getIndividualMeasureIds(
			@RequestParam(value = "providerId", required = true) Integer providerId)throws Exception {
		EMRResponseBean result=new EMRResponseBean();
		List<QualityMeasuresProviderMapping> indiMeasureids=QppConfigurationService.getMeasureIds(providerId);
		result.setData(indiMeasureids);
		return result;
	}
	@RequestMapping(value = "/addMeasuresToProvider", method = RequestMethod.GET)
	@ResponseBody
	public void addMeasuresToProvider(
			@RequestParam(value = "measureIds", required = true) String measureIds,
			@RequestParam(value = "providerId", required = true) Integer providerId,
			@RequestParam(value = "prgmYear", required = true) Integer prgmYear)throws Exception {
		QppConfigurationService.addMeasuresToProvider(measureIds,providerId,prgmYear);
	}
	
	@RequestMapping(value = "/getFilterDetails", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getFilterDetails()throws Exception {
		EMRResponseBean result=new EMRResponseBean();
		HashMap<String,Object> filterDetails=QppConfigurationService.getFilterDetails();
		result.setData(filterDetails);
		return result;
	}
	
	@RequestMapping(value = "/getFilteredDetails", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getFilteredDetails(
			@RequestParam(value = "ageFrom", required = false, defaultValue = "-1") Integer ageFrom,
			@RequestParam(value = "ageTo", required = false, defaultValue = "-1") Integer ageTo,
			@RequestParam(value = "raceCode", required = false, defaultValue = "-1") String raceCode,
			@RequestParam(value = "ethnicityCode", required = false, defaultValue = "-1") String ethnicityCode,
			@RequestParam(value = "gender", required = false, defaultValue = "-1") String gender,
			@RequestParam(value = "patientId", required = true) String patientId,
			@RequestParam(value = "insCompanyId", required = false, defaultValue = "-1") Integer insCompanyId,
			@RequestParam(value = "ageCriteria", required = false, defaultValue = "-1") Integer ageCriteria,
			@RequestParam(value = "currMeasureId", required = true) String currMeasureId,
			@RequestParam(value = "dxCodes", required = false,defaultValue = "-1")String dxCodes
			)throws Exception {
		EMRResponseBean result=new EMRResponseBean();
		List<MIPSPatientInformation> filteredDetails=QppConfigurationService.getFilteredDetails(patientId,ageFrom,ageTo,ageCriteria,raceCode,ethnicityCode,gender,insCompanyId,currMeasureId,dxCodes);
		result.setData(filteredDetails);
		return result;
	}
	@RequestMapping(value = "/getDXList", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getDXList(
			@RequestParam(value = "measureId", required = false, defaultValue = "-1")String measureId,
			@RequestParam(value="sharedFolder", required=true) String sharedPath)throws Exception {
		EMRResponseBean result=new EMRResponseBean();
		DiagnosisList DXList=QppConfigurationService.getDXList(measureId,sharedPath); 
		result.setData(DXList);
		return result;
	}
	
	@RequestMapping(value = "/getPatientBasedOnDX", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getPatientBasedOnDX(
			@RequestParam(value = "patientId", required = true)String patientId,
			@RequestParam(value="dxCodes", required=true) String dxCodes)throws Exception {
		EMRResponseBean result=new EMRResponseBean();
		List<MIPSPatientInformation> DXList=QppConfigurationService.getPatientBasedOnDX(patientId,dxCodes); 
		result.setData(DXList);
		return result;
	}
	
}