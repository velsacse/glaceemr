package com.glenwood.glaceemr.server.application.services.referral;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import com.glenwood.glaceemr.server.application.controllers.PlanReferralController;
import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.H611;
import com.glenwood.glaceemr.server.application.models.ProblemList;
import com.glenwood.glaceemr.server.application.models.Referral;
import com.glenwood.glaceemr.server.application.repositories.EncounterRepository;
import com.glenwood.glaceemr.server.application.repositories.H611Repository;
import com.glenwood.glaceemr.server.application.repositories.ProblemListRepository;
import com.glenwood.glaceemr.server.application.repositories.ReferralRepository;
import com.glenwood.glaceemr.server.application.specifications.ReferralSpecification;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

/**
 * Service implementation file for PlanReferralController
 * @author software
 */

@Service
public class ReferralServiceImpl implements ReferralService{

	@PersistenceContext
	EntityManager em;

	@Autowired
	ReferralRepository referralRepository; 
	
	@Autowired
	EncounterRepository encounterRepository;
	
	@Autowired
	H611Repository h611Repository;
	
	@Autowired
	ProblemListRepository problemListRepository;
	
	private Logger logger = Logger.getLogger(PlanReferralController.class);
	
	/* 
	 * Get list of referrals
	 * @see com.glenwood.glaceemr.server.application.services.referral.ReferralService#getListOfReferralsPlan(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Referral> getListOfReferralsPlan(Integer encounterID, Integer chartID, String dx) {
		
		Integer encounterId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(encounterID.toString())).or("-1"));
		Integer chartId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(chartID.toString())).or("-1"));
		
		List<Referral> result = null;
		
		if(!dx.trim().isEmpty()){
			
			  result= referralRepository.findAll(Specifications
							.where(ReferralSpecification.findByEncounterId(encounterId))
							.and(ReferralSpecification.findByChartId(chartId))
							.and(ReferralSpecification.findByStatusNotEqual(2))
							.and(ReferralSpecification.getBydxCode(dx))
							.and(ReferralSpecification.orderById()));
		
		}
		else{

			  result= referralRepository.findAll(Specifications
							.where(ReferralSpecification.findByEncounterId(encounterId))
							.and(ReferralSpecification.findByChartId(chartId))
							.and(ReferralSpecification.findByStatusNotEqual(2))
							.and(ReferralSpecification.orderById()));

		}
		logger.debug("Getting list of referrals");
		
		return result;
	}
	
	/* 
	 * Get referral
	 * (non-Javadoc)
	 * @see com.glenwood.glaceemr.server.application.services.referral.ReferralService#getReferral(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ReferralBean getReferral(Integer referralID, Integer chartID, Integer encounterID ) {

		ReferralBean result=null;
		
		Integer referralId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(referralID.toString())).or("-1"));
		Integer chartId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(chartID.toString())).or("-1"));
		Integer encounterId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(chartID.toString())).or("-1"));
		
		String patientId = getpatientid(chartId);
		List<Referral> referralList = referralRepository.findAll(ReferralSpecification.getByRefId(referralId));
		
		List<H611> cdingsystems=h611Repository.findAll(ReferralSpecification.getcodingsystems(encounterId,chartId));
		List<ProblemList> problemlist=problemListRepository.findAll(ReferralSpecification.getproblemlist(patientId));
		
		List<H611> cdingsystems1=new ArrayList<>();
		for(int z=0;z<cdingsystems.size()-1;z++){
			String dxcode1=cdingsystems.get(z).getH611005();
			String oid=cdingsystems.get(z).getH611CodingSystemid();
			for(int z1=z+1;z1<cdingsystems.size();z1++){
				String dxcode2=cdingsystems.get(z1).getH611005();
				String oid2=cdingsystems.get(z1).getH611CodingSystemid();
				if(((dxcode1.replaceAll("\\s+","")).equalsIgnoreCase((dxcode2).replaceAll("\\s+","")))&&(oid.replaceAll("\\s+","").equalsIgnoreCase(oid2.replaceAll("\\s+","")))){

					if(cdingsystems.get(z).getH611001()>cdingsystems.get(z1).getH611001()){
						cdingsystems.remove(cdingsystems.get(z1));
					}else{
						cdingsystems.remove(cdingsystems.get(z));
						z--;
					}
				}

			}

		}

		for(int z=0;z<problemlist.size()-1;z++){
			String dxcode1=problemlist.get(z).getProblemListDxCode();
			for(int z1=z+1;z1<problemlist.size();z1++){
				String dxcode2=problemlist.get(z1).getProblemListDxCode();
				if((dxcode1.replaceAll("\\s+","")).equalsIgnoreCase((dxcode2).replaceAll("\\s+",""))){
					if(problemlist.get(z).getProblemListId()>problemlist.get(z1).getProblemListId()){
						problemlist.remove(problemlist.get(z1));
					}else{
						problemlist.remove(problemlist.get(z));
						z--;
					}
				}


			}

		}

		for(int i=0;i<problemlist.size();i++){
			String dxcode=problemlist.get(i).getProblemListDxCode();
			for(int j=0;j<cdingsystems.size();j++){
				String dxcode1=cdingsystems.get(j).getH611005();
				if(dxcode.replaceAll("\\s+","").equalsIgnoreCase(dxcode1.replaceAll("\\s+",""))){
					if(problemlist.get(i).getProblemListId()>cdingsystems.get(j).getH611001()){
						cdingsystems.remove(cdingsystems.get(j));
					}else{
						problemlist.remove(problemlist.get(i));
					}
				}
			}
		}

		result = new ReferralBean(referralList,cdingsystems,problemlist);
		
		logger.debug("Getting referral details");
		
		return result;
	}
	
	/*
	 * Get referral based on referral id
	 * @see com.glenwood.glaceemr.server.application.services.referral.ReferralService#getReferralPlan(java.lang.Integer)
	 */
	@Override
	public Referral getReferralPlan(Integer referralID) {
		
		Integer referralId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(referralID.toString())).or("-1"));
		Referral result = referralRepository.findOne(Specifications.where(ReferralSpecification.findByRefId(referralId)).and(ReferralSpecification.orderById()));
		logger.debug("Getting referral");
		
		return result;
	}
	
	/*
	 * Save referral
	 * @see com.glenwood.glaceemr.server.application.services.referral.ReferralService#saveReferralPlan(java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public void saveReferralPlan(Integer referralID, String reason, String notes, String dx) {
		
		Integer referralId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(referralID.toString())).or("-1"));
		Iterable<Referral> referralList = referralRepository.findAll(ReferralSpecification.findByRefId(referralId));
		
		Referral referral =null;
		
		for(Referral ref: referralList){
//			Date date = new Date();
			Referral refUpdate = ref;
//			refUpdate.setH413004(new Timestamp(date.getTime()));
			refUpdate.setH413037(reason);
			refUpdate.setH413013(notes);
			refUpdate.setH413011(dx);
			referral = refUpdate;
			referralRepository.saveAndFlush(referral);
		}
		
		logger.debug("Saving referral");
	}
	
	/* 
	 * Cancel referral - changing status to cancelled
	 * @see com.glenwood.glaceemr.server.application.services.referral.ReferralService#cancelReferral(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void cancelReferral(Integer loginID, Integer refID) {
		
		Integer loginId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(loginID.toString())).or("-1"));
		Integer refId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(refID.toString())).or("-1"));
		if(loginId==-1)
			loginId=null;
		Iterable<Referral> referralList = referralRepository.findAll(ReferralSpecification.findByRefId(refId));
		Referral referral = null;
		
		for(Referral ref: referralList){
			Date date = new Date();
			Referral refUpdate = ref;
			refUpdate.setH413041(2);
			refUpdate.setReferralCancelBy(loginId);
			refUpdate.setReferralCancelOn(new Timestamp(date.getTime()));
			referral = refUpdate;
			referralRepository.saveAndFlush(referral);
		}
		
		logger.debug("Cancelling referral");
		
	}
	
	public String getpatientid(int chartId){
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<Chart> root = cq.from(Chart.class);
		cq.select(root.get(Chart_.chart_patientid));
		cq.where(builder.equal(root.get(Chart_.chart_id), chartId));
		List <Object> resultList = em.createQuery(cq).getResultList();
		String patientId="-1";
		if(resultList.size()>0)
			patientId=resultList.get(0).toString();
		return patientId;

	}
}
