package com.glenwood.glaceemr.server.application.controllers;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
			@RequestParam(value = "providerId", required = true) Integer providerId)throws Exception {
		QppConfigurationService.addMeasuresToProvider(measureIds,providerId);
	}
}