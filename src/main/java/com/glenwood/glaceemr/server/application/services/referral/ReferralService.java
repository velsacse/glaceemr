package com.glenwood.glaceemr.server.application.services.referral;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.Referral;

/**
 * Service Interface for Referral module 
 * @author software
 */
public interface ReferralService {
	
	public void cancelReferral(Integer loginID, Integer refId);
	
	public List<Referral> getListOfReferralsPlan(Integer encounterId,Integer patientId, String dx);
	
	public Referral getReferralPlan(Integer referralId);
	
	public ReferralBean getReferral(Integer referralID, Integer chartID, Integer encounterId);
	
	public void saveReferralPlan(Integer patientId, String reason, String notes, String dx);
	
}
