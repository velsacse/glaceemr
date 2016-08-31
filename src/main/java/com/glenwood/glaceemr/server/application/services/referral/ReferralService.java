package com.glenwood.glaceemr.server.application.services.referral;

import java.util.List;

import org.json.JSONException;
import com.glenwood.glaceemr.server.application.models.Referral;

/**
 * Service Interface for Referral module 
 * @author software
 */
public interface ReferralService {
	
	public void cancelReferral(Integer loginID, Integer refId);
	
	public List<Referral> getListOfReferralsPlan(Integer encounterId,Integer patientId, String dx);
	
	public Referral getReferralPlan(Integer referralId);
	
	public void saveReferralPlan(Integer patientId, String reason, String notes, String dx);
	
	public ReferralListBean getListOfReferrals(Integer chartId);
	
	public ReferralBean getReferral(Integer referralId,Integer charId,Boolean fromEdit);
	
	public ReferralBean getReferral(Integer referralId,Integer charId,Integer encounterId);
	
	public String saveReferral(Integer chartId, Integer encounterId, Integer patientId, Integer referralId, Boolean isMedSumm, Integer editReferral, 
		   Integer loginId, Integer userId, Integer reftoId, String reason, String apptBy, String apptNotes, Short apptConfirmed, String apptContactPerson, String apptDate, String apptTime, 
		   String authBy, String authNotes, String authDate, String authExpDate, Short authNeeded, String authNum, Integer numofVisits, 
		   Short authDone, String consNotes, Short isConsReportReceived, String consReportReceivedDate, Short isConsReportReviewed, 
		   String consReportReviewedDate, String guarantorName, String hospitalName, Boolean isSummCare, String orderedDate, 
		   Short patientNotified, String address, String fax, String phone, String specialization, String dx, String referredBy, 
		   String referredTo, Integer status, String referralNotes, String printLeaf) throws JSONException;

}
