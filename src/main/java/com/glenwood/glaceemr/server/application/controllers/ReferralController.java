package com.glenwood.glaceemr.server.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.referral.ReferralBean;
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
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
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
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return result;

	}

	
	/**
	 * Save referral details
	 * @throws JSONException
	 */
	@RequestMapping(value = "/SaveReferral", method = RequestMethod.POST)
	@ResponseBody
	public String saveReferral(
		   @RequestParam(value="chartId",required = false, defaultValue="-1") Integer chartId,
		   @RequestParam(value="encounterId",required = false, defaultValue="-1") Integer encounterId,
		   @RequestParam(value="patientId",required = false, defaultValue="-1") Integer patientId,
		   @RequestParam(value="refId",required = false, defaultValue="-1") Integer refId,
		   @RequestParam(value="isMedSumm",required = false, defaultValue="false") Boolean isMedSumm,
		   @RequestParam(value="editReferral",required = false, defaultValue="-1") Integer editReferral,
		   @RequestParam(value="loginId",required = false, defaultValue="-1") Integer loginId,
		   @RequestParam(value="userId",required = false, defaultValue="-1") Integer userId,
		   @RequestParam(value="reftoId",required = false, defaultValue="-999") Integer reftoId,
		   
		   @RequestParam(value="Referral_For",required = false, defaultValue="") String reason,
		   @RequestParam(value="appt_by",required = false, defaultValue="0") String apptBy,
		   @RequestParam(value="appt_comment",required = false, defaultValue="") String apptNotes,
		   @RequestParam(value="appt_confirmed",required = false, defaultValue="0") Short apptConfirmed,
		   @RequestParam(value="appt_contact_person",required = false, defaultValue="") String apptContactPerson,
		   @RequestParam(value="appt_date",required = false, defaultValue="") String apptDate,
		   @RequestParam(value="appt_time",required = false, defaultValue="") String apptTime,
		   @RequestParam(value="auth_by",required = false, defaultValue="0") String authBy,
		   @RequestParam(value="auth_comment",required = false, defaultValue="") String authNotes,
		   @RequestParam(value="auth_date",required = false, defaultValue="") String authDate,
		   @RequestParam(value="auth_expiration_date",required = false, defaultValue="") String authExpDate,
		   @RequestParam(value="auth_needed",required = false, defaultValue="0") Short authNeeded,
		   @RequestParam(value="auth_no",required = false, defaultValue="") String authNum,
		   @RequestParam(value="auth_no_visits",required = false, defaultValue="0") Integer numofVisits,
		   @RequestParam(value="authorized",required = false, defaultValue="0") Short authDone,
		   @RequestParam(value="cons_comment",required = false, defaultValue="") String consNotes,
		   @RequestParam(value="cons_report_received",required = false, defaultValue="0") Short isConsReportReceived,
		   @RequestParam(value="cons_report_received_date",required = false, defaultValue="") String consReportReceivedDate,
		   @RequestParam(value="cons_report_reviewed",required = false, defaultValue="0") Short isConsReportReviewed,
		   @RequestParam(value="cons_report_reviewed_date",required = false, defaultValue="") String consReportReviewedDate,
		   @RequestParam(value="guarantorname",required = false, defaultValue="") String guarantorName,
		   @RequestParam(value="hospitalname",required = false, defaultValue="") String hospitalName,
		   @RequestParam(value="isSummCare",required = false, defaultValue="false") Boolean isSummCare,
		   @RequestParam(value="ordered_date",required = false, defaultValue="") String orderedDate,
		   @RequestParam(value="patientnotified",required = false, defaultValue="0") Short patientNotified,
		   @RequestParam(value="r_docaddress",required = false, defaultValue="") String address,
		   @RequestParam(value="r_docfax",required = false, defaultValue="") String fax,
		   @RequestParam(value="r_docphone",required = false, defaultValue="") String phone,
		   @RequestParam(value="r_docspec",required = false, defaultValue="") String specialization,
		   @RequestParam(value="r_dx",required = false, defaultValue="") String dx,
		   @RequestParam(value="rdoctor_from",required = false, defaultValue="") String referredBy,
		   @RequestParam(value="rdoctor_to",required = false, defaultValue="") String referredTo,
		   @RequestParam(value="referralStatus",required = false, defaultValue="-1") Integer status,
		   @RequestParam(value="referring_comment",required = false, defaultValue="") String referralNotes,
		   @RequestParam(value="PrintPatientLeafDetails",required = false, defaultValue="") String printLeaf
		   ) throws JSONException {
		
				logger.debug("Saving referral");
				String result = referralService.saveReferral(chartId,encounterId,patientId,refId,isMedSumm,editReferral,loginId,userId,reftoId,
						reason,apptBy,apptNotes,apptConfirmed,apptContactPerson,apptDate,apptTime,authBy,authNotes,authDate,authExpDate,authNeeded,authNum,numofVisits,authDone,
						consNotes,isConsReportReceived,consReportReceivedDate,isConsReportReviewed,consReportReviewedDate,guarantorName,hospitalName,isSummCare,orderedDate,
						patientNotified,address,fax,phone,specialization,dx,referredBy,referredTo,status,referralNotes,printLeaf);
				
				auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
				return result;
	}

}
