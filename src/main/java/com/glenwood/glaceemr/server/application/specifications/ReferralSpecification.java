package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.CodingSystems;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Encounter_;
import com.glenwood.glaceemr.server.application.models.H611;
import com.glenwood.glaceemr.server.application.models.H611_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.ProblemList;
import com.glenwood.glaceemr.server.application.models.ProblemList_;
import com.glenwood.glaceemr.server.application.models.Referral;
import com.glenwood.glaceemr.server.application.models.Referral_;

/**
 * Specification file for referral module
 * @author software
 */
public class ReferralSpecification {

	/**
	 * Get referrals based on encounter id
	 * @param encounterId
	 * @return
	 */
	public static Specification<Referral> findByEncounterId(final Integer encounterId)
	{
		return new Specification<Referral>() {
			
			@Override
			public Predicate toPredicate(Root<Referral> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate encounterPred = cb.equal(root.get(Referral_.h413003),encounterId);				
				return encounterPred;
			}
			
		};			
	}
	
	/**
	 * Get referral based on referral id
	 * @param refId
	 * @return
	 */
	public static Specification<Referral> findByRefId(final Integer refId)
	{
		return new Specification<Referral>() {
			
			@Override
			public Predicate toPredicate(Root<Referral> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate pred = cb.equal((root.get(Referral_.h413001)), refId);				
				return pred;
			}
		};
	}
	
	/**
	 * Get referral based on chart id
	 * @param chartId
	 * @return
	 */
	public static Specification<Referral> findByChartId(final Integer chartId)
	{
		return new Specification<Referral>() {
			
			@Override
			public Predicate toPredicate(Root<Referral> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				
				Join<Referral, EmployeeProfile> joinEmp = root.join("empprofileTable",JoinType.LEFT);
				Predicate pred = cb.equal((root.get(Referral_.h413002)), chartId);				
				return pred;
				
			}
		};
	}

	/**
	 * Get referral not having status 2
	 * @param status
	 * @return
	 */
	public static Specification<Referral> findByStatusNotEqual(final Integer status)
	{
		return new Specification<Referral>() {
			
			@Override
			public Predicate toPredicate(Root<Referral> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				
				Predicate pred = cb.notEqual(root.get(Referral_.h413041), status);
				
				return pred;
			}
		};
	}

	/**
	 * Ordering referrals based on referral id in ascending order
	 * @return
	 */
	public static Specification<Referral> orderById()
	{
		return new Specification<Referral>() {
			
			@Override
			public Predicate toPredicate(Root<Referral> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				query.orderBy(cb.asc(root.get(Referral_.h413001)));
				return query.getRestriction();
			}
		};
	}

	/**
	 * Get referrals based on id in descending order of ordered date
	 * @param refId
	 * @return
	 */
	public static Specification<Referral> getByRefId(final Integer refId)
	{
		return new Specification<Referral>() {
			
			@Override
			public Predicate toPredicate(Root<Referral> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate pred = cb.equal((root.get(Referral_.h413001)), refId);
				query.where(pred).orderBy(cb.desc(root.get(Referral_.h413004)));
				Join<Referral,PatientRegistration> patRegJoin= root.join("patientRegistrationTable",JoinType.INNER);
				Join<Referral,EmployeeProfile> empProfileJoin= root.join("empprofileTable",JoinType.LEFT);
				return query.getRestriction();
			}
		};
	}
	
	/**
	 * Get referrals having specific diagnosis
	 * @param dx
	 * @return
	 */
	public static Specification<Referral> getBydxCode(final String dx)
	{
		return new Specification<Referral>() {
			
			@Override
			public Predicate toPredicate(Root<Referral> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate pred = cb.like(root.get(Referral_.h413011), "%"+dx+"%");
				return pred;
			}
		};
	}
	
	/**
	 * To get present encounter diagnosis data
	 * @param encounterid
	 * @param patientid
	 * @param chartid
	 * @return
	 */
	public static Specification<H611> getcodingsystems(final int encounterid,final int chartid ) {
		return new Specification<H611>() {

			@Override
			public Predicate toPredicate(Root<H611> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {

				Join<H611,CodingSystems> rootjoin=root.join(H611_.codingsystemsTable,JoinType.INNER);
				Join<H611, Encounter> encounterjoin=root.join(H611_.encounter,JoinType.INNER);
				Predicate predicate=cb.equal(encounterjoin.get(Encounter_.encounterChartid), chartid);
				Predicate predicate1=cb.equal(encounterjoin.get(Encounter_.encounterType), 1);
				query.distinct(true);
				Predicate result=cb.and(predicate,predicate1);
				return result;


			}
		};

	}

	/**
	 * To get patient problems list
	 * @param patientid
	 * @return
	 */
	public static Specification<ProblemList> getproblemlist(final String patientid) {
		return new Specification<ProblemList>() {

			@Override
			public Predicate toPredicate(Root<ProblemList> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<ProblemList, CodingSystems> rootjoin=root.join(ProblemList_.codingSystems,JoinType.INNER);
				Predicate predicate=cb.equal(root.get(ProblemList_.problemListPatientId), patientid);
				Predicate predicate1=cb.equal(root.get(ProblemList_.problemListIsactive), true);
				Predicate result=cb.and(predicate,predicate1);
				return result;


			}
		};

	}
}