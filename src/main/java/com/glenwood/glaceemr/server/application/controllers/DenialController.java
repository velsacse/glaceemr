package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.Denial.DenialBean;
import com.glenwood.glaceemr.server.application.services.Denial.DenialResponseBean;
import com.glenwood.glaceemr.server.application.services.Denial.DenialService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;

/**
 * @author Venu
 *
 * Denial Controller to download all pending denials and to take necessary actions acordingly
 * 
 */
@Api(value = "Denials", description = "Denial")
@RestController
@RequestMapping(value="/Denials")
public class DenialController {

	@Autowired
	DenialService denialService;
	
	//Method to Download all pending denials based on denial date of posting.
	@RequestMapping(value="/getAllDenials",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getAllDenail(@RequestParam(value="fromDate",
	required=false, defaultValue="2016-01-01") String fromDate,@RequestParam(value="toDate",required=false, defaultValue="") String toDate) throws Exception{

		List<DenialBean> resultList=denialService.getAllDenials(fromDate,toDate);
		DenialResponseBean denialResponse = new DenialResponseBean();
		denialResponse.setDenialResponse(resultList);
		denialResponse.setStatus(true);
		EMRResponseBean response=new EMRResponseBean();
		response.setData(denialResponse);
		return response;  
	}
}
