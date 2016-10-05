package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.Referral;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.referral.ReferralBean;
import com.glenwood.glaceemr.server.application.services.referral.ReferralService;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * Controller for plan referral. It has one main entity, <b>H413 </b>.
 * @author Chandrahas
 * <br> <br>
 * <b>Example requests:</b>
 * <br> <br>
 * <ul>
 * <li><b>Get Referral list</b><i> ReferralSummary.Action/listReferral?encounterId=6701&chartId=1196</i><br>
 * Gets the list of categories based on the category Id.</li><br>
 * <li><b>Get Referral</b><i> ReferralSummary.Action/GetReferral?referralId=12173</i><br>
 * Highlights one or more alerts and updates the modified user and date.</li><br>
 * <li><b>Save Referral</b><i> ReferralSummary.Action/SaveReferral?referralId=12173&reason=knee pain&notes=not able to walk</i><br>
 * Un Highlights one or more alerts and updates the modified user and date.</li><br>
 * <li><b>Cancel Referral</b><i> ReferralSummary.Action/CancelReferral?referralId=12173&loginId=49</i><br>
 * Mark one or more alerts to read and updates the modified user and date.</li><br>
 * </ul>
 */
@Api(value = "ReferralSummary", description = "To get referral details", consumes="application/json")

@RestController
@RequestMapping(value="/user/ReferralSummary.Action")
public class PlanReferralController {
	
	@Autowired
	ReferralService referralService;
	
	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	HttpServletRequest request;
	
	private Logger logger = Logger.getLogger(PlanReferralController.class);
	
		
	/**
	 * To get list of referrals
	 * @param encounterId
	 * @param chartId
	 * @return list of referrals
	 * @throws JSONException
	 */
	@ApiOperation(value = "Getting referral list", notes = "Getting referral list based on encounterid and chartId")
	@RequestMapping(value = "/listReferral", method = RequestMethod.GET)
	@ResponseBody
	public List<Referral> getReferralList(@RequestParam(value="encounterId",required = false) Integer encounterId,
			   @RequestParam(value="chartId",required = false) Integer chartId,
			   @RequestParam(value="dxCode",required = false,defaultValue="") String dx) throws JSONException {
	
		logger.debug("Getting referral list::encounterId"+encounterId+" chartId"+chartId+" dxCode"+dx);
		List<Referral> result = referralService.getListOfReferralsPlan(encounterId,chartId,dx);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Referral,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"View Referrals details",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Referral,request,"Referrals details viewed");		
		return result;

	}
	
	
	/**
	 * To get referral and diagnosis
	 * @param referralId
	 * @return
	 * @throws JSONException
	 */
	@ApiOperation(value = "Getting referral and diagnosis", notes = "Getting referral details and patient dianosis data")
	@RequestMapping(value = "/GetReferralWithDiagnosis", method = RequestMethod.GET)
	@ResponseBody
	public ReferralBean getReferral(@RequestParam(value="referralId",required = false,defaultValue="-1") Integer referralId,
			@RequestParam(value="chartId",required = false,defaultValue="-1") Integer chartId,
			@RequestParam(value="encounterId",required = false,defaultValue="-1") Integer encounterId)
			    throws JSONException {
		
		logger.debug("Getting referral::referralId"+referralId);
		ReferralBean result = referralService.getReferral(referralId, chartId, encounterId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Referral,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"View Referral details",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Referral,request,"Referral details viewed");
		return result;

	}
	
	/**
	 * To get referral based on referral id
	 * @param referralId
	 * @return
	 * @throws JSONException
	 */
	@ApiOperation(value = "Getting referral", notes = "Getting referral based on referralId")
	@RequestMapping(value = "/GetReferral", method = RequestMethod.GET)
	@ResponseBody
	public Referral getReferral(@RequestParam(value="referralId",required = false,defaultValue="-1") Integer referralId)
			    throws JSONException {
		
		logger.debug("Getting referral::referralId"+referralId);
		Referral result = referralService.getReferralPlan(referralId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Referral,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"View Referral details",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Referral,request,"Referral details viewed");
		return result;

	}
	
	/**
	 * To save the referral
	 * @param referralId
	 * @param reason
	 * @param notes
	 * @return
	 * @throws JSONException
	 */
	@ApiOperation(value = "Saving referral", notes = "Saving referral details based on referralId")
	@RequestMapping(value = "/SaveReferral", method = RequestMethod.GET)
	@ResponseBody
	public String saveReferral(
			  @RequestParam(value="referralId",required = false) Integer referralId,
			  @RequestParam(value="reason",required = false) String reason,
			  @RequestParam(value="notes",required = false) String notes,
			  @RequestParam(value="criticalstatus",required = false) Integer criticalstatus,
			  @RequestParam(value="diagnosis",required = false, defaultValue="") String diagnosis) throws JSONException {
		
		logger.debug("Saving referral::referralId"+referralId+" reason"+reason+" notes"+notes+"criticalstatus"+criticalstatus);
		referralService.saveReferralPlan(referralId,reason,notes,diagnosis,criticalstatus);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Referral,AuditLogConstants.UPDATE,1,AuditLogConstants.SUCCESS,"Update Referral Details",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Referral,request,"Referral details updated");
		return "success";
	}
		
	/** 
	 * To update the referral status to cancelled
	 * @param loginID
	 * @param refId
	 * @return
	 * @throws JSONException
	 */
	@ApiOperation(value = "Cancel referral", notes = "Change referral status to cancel and save active user id in cancelled by field")
	@RequestMapping(value = "/CancelReferral", method = RequestMethod.GET)
	@ResponseBody
	public String cancelReferral(@RequestParam(value="loginId",required = false, defaultValue = "-1") Integer loginID,
			   @RequestParam(value="referralId",required = false, defaultValue = "-1") Integer referralId) throws JSONException {
		
		logger.debug("Cancelling referral::referralId"+referralId+" loginId"+loginID);
		referralService.cancelReferral(loginID,referralId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Referral,AuditLogConstants.CANCELED,1,AuditLogConstants.SUCCESS,"Cancel Referral",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Referral,request,"Referral cancelled");
		return "success";

	}
	
}
