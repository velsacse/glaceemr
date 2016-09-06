package com.glenwood.glaceemr.server.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.referral.ReferralBean;
import com.glenwood.glaceemr.server.application.services.referral.ReferralForm;
import com.glenwood.glaceemr.server.application.services.referral.ReferralListBean;
import com.glenwood.glaceemr.server.application.services.referral.ReferralService;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;

/**
 * Controller for referral module
 * @author software
 */
@Api(value = "ReferralTabletSummary", description = "To get referral details", consumes="application/json")

@RestController
@RequestMapping(value="ReferralTabletSummary.Action")
public class ReferralController {
	
	@Autowired
	ReferralService referralService;
	
	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	HttpServletRequest request;
	
	private Logger logger = Logger.getLogger(ReferralController.class);
	
	
	/**
	 * Get list of referrals
	 * @param chartId
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(value = "/listReferral", method = RequestMethod.GET)
	@ResponseBody
	public ReferralListBean getReferralList(@RequestParam(value="chartId",required = false) Integer chartId) throws JSONException {
		logger.debug("Getting list of referrals:: chartId"+chartId);
		ReferralListBean result = referralService.getListOfReferrals(chartId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Referral,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"View Referrals details",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Referral,request,"Referrals details viewed");
		return result;

	}	
	
	
	/**
	 * Get referral details
	 * @param referralId
	 * @param chartId
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(value = "/GetReferral", method = RequestMethod.GET)
	@ResponseBody
	public ReferralBean getReferral(@RequestParam(value="referralId",required = false) Integer referralId,
			   @RequestParam(value="chartId",required = false) Integer chartId,
			   @RequestParam(value="fromEdit",required = false,defaultValue="false") Boolean fromEdit) throws JSONException {
		logger.debug("Getting referral:: chartId"+chartId+" referralId"+referralId);
		ReferralBean result = referralService.getReferral(referralId,chartId,fromEdit);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Referral,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"View Referral details",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Referral,request,"Referral details viewed");
		return result;

	}

	
	/**
	 * Save referral details
	 * @throws JSONException
	 */
	@RequestMapping(value = "/SaveReferralForm", method = RequestMethod.POST)
	public String saveReferral(@RequestBody ReferralForm formBean) throws JSONException {

		logger.debug("Saving referral");
		String result = referralService.saveReferral(formBean);

		if(formBean != null && formBean.getEditReferral().equals("1"))
			auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Referral,AuditLogConstants.UPDATE,1,AuditLogConstants.SUCCESS,"Update Referral Details",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Referral,request,"Referral details updated");
		else
			auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Referral,AuditLogConstants.CREATED,1,AuditLogConstants.SUCCESS,"Add Referral Details",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Referral,request,"Referral details added");
		return result;
	}
}
