package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.Bean.ClaimInfoBean;
import com.glenwood.glaceemr.server.application.Bean.CommonActionBean;
import com.glenwood.glaceemr.server.application.Bean.CommonResponseBean;
import com.glenwood.glaceemr.server.application.Bean.PatientInsuranceInfoBean;
import com.glenwood.glaceemr.server.application.services.Denial.DenialBean;
import com.glenwood.glaceemr.server.application.services.Denial.DenialResponseBean;
import com.glenwood.glaceemr.server.application.services.Denial.DenialService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;

@Api(value = "Denials", description = "Denial")
@RestController
@RequestMapping(value="/Denials")
public class DenialController {

	@Autowired
	DenialService dservice;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@RequestMapping(value="/getAllDenials",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getAllDenail(@RequestParam(value="fromDate",
	required=false, defaultValue="2016-01-01") String fromDate,@RequestParam(value="toDate",required=false, defaultValue="") String toDate) throws Exception{

		List<DenialBean> resultList=dservice.getAllDenial(fromDate,toDate);
		DenialResponseBean denialResponse = new DenialResponseBean();
		denialResponse.setDenialResponse(resultList);
		denialResponse.setStatus(true);
		EMRResponseBean response=new EMRResponseBean();
		response.setData(denialResponse);
		return response;  
	}
	
	@RequestMapping(value="/actions",method=RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean actionSaveAndUpdate(@RequestBody CommonActionBean commonActionBean) throws Exception
	{
		EMRResponseBean response=new EMRResponseBean();
		dservice.getDenialReasonId(commonActionBean);
		dservice.getBillingReasonId(commonActionBean);
		dservice.getDenialTypeId(commonActionBean);
		dservice.getDenialCategoryId(commonActionBean);
		dservice.getProblemTypeId(commonActionBean);
		switch(commonActionBean.getActionId())
		{
			case 9: response.setData(dservice.billToPatAction(commonActionBean));return response;
			
			case 15:response.setData(dservice.changeCptAction(commonActionBean));return response;
			
			case 19:response.setData(dservice.changeCptChargesAction(commonActionBean));return response;
			
			case 32:response.setData(dservice.changeDxAction(commonActionBean));return response;
			
			case 39:response.setData(dservice.changeMod1Action(commonActionBean));return response;
			
			case 40:response.setData(dservice.changeMod2Action(commonActionBean));return response;
			
			case 6:response.setData(dservice.changePrimaryInsuranceAction(commonActionBean));return response;
			
			case 20:response.setData(dservice.changeSecondaryInsuranceAction(commonActionBean));return response;
			
			case 1:response.setData(dservice.claimToPrimaryAction(commonActionBean));return response;
			
			case 2:response.setData(dservice.claimToSecondaryAction(commonActionBean));return response;
			
			case 27:response.setData(dservice.followUpAction(commonActionBean));return response;
			
			case 7:break;
			
			case 22:response.setData(dservice.markAsApppealAction(commonActionBean));return response;
			
			case 5:response.setData(dservice.markAsCapitationAction(commonActionBean));return response;
			
			case 24:response.setData(dservice.markAsDuplicateAction(commonActionBean));return response;
			
			case 37:response.setData(dservice.markAsFullySettledAction(commonActionBean));return response;
			
			case 26:response.setData(dservice.markAsOnHoldAction(commonActionBean));return response;
			
			case 25:response.setData( dservice.markAsUncollectableAction(commonActionBean));return response;
			
			case 18: response.setData(dservice.reportAProblem(commonActionBean));return response;
			
			case 36: response.setData(dservice.toBeCalledAction(commonActionBean));return response;
			
			case 52:response.setData(dservice.toBeCalledCompletedAction(commonActionBean));return response;
		
			case 4: response.setData(dservice.writeoffAction(commonActionBean));return response;
			
		}
		return null;
	}
	
	@RequestMapping(value="/getInsid",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getPrimaryInsurance(@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId, 
			@RequestParam(value="insType", required=false, defaultValue="") Integer insType) throws Exception{

		EMRResponseBean response=new EMRResponseBean();
		response.setData(dservice.getPatientInsInfo(patientId,insType));
		return response;  
	}
	
	@RequestMapping(value="/getClaimInfo",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getClaimInfo(@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId, 
			@RequestParam(value="claimNo", required=false, defaultValue="") String claimNo) throws Exception{

		EMRResponseBean response=new EMRResponseBean();
		response.setData(dservice.getServicesByClaim(patientId, claimNo));
		return response;
	}
	
	
}
