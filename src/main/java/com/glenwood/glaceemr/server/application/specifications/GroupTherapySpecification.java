package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.PosTable_;
import com.glenwood.glaceemr.server.application.models.TherapyGroup;
import com.glenwood.glaceemr.server.application.models.TherapyGroupPatientMapping;
import com.glenwood.glaceemr.server.application.models.TherapyGroupPatientMapping_;
import com.glenwood.glaceemr.server.application.models.TherapyGroup_;
import com.glenwood.glaceemr.server.application.models.TherapySession;
import com.glenwood.glaceemr.server.application.models.TherapySessionDetails;
import com.glenwood.glaceemr.server.application.models.TherapySessionDetails_;
import com.glenwood.glaceemr.server.application.models.TherapySession_;

/**
 * This class represents specifications needed for Group therapy   
 * @author Software
 *
 */
public class GroupTherapySpecification {
	
    /**
     * To get the list of employees
     * @return
     */
	public static Specification<EmployeeProfile> getListOfPhysicians() {
		return new Specification<EmployeeProfile>() {

			@Override
			public Predicate toPredicate(Root<EmployeeProfile> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate groupId=root.get(EmployeeProfile_.empProfileGroupid).in(-1);
				Predicate active=cb.equal(root.get(EmployeeProfile_.empProfileIsActive), true);
				Predicate finalPredicate=cb.and(groupId,active);
				return finalPredicate;
			}
		};
	}
	
	/**
	 * To get the place of service
	 * @return
	 */
	public static Specification<PosTable> getAllPosValue() {
		return new Specification<PosTable>() {

			@Override
			public Predicate toPredicate(Root<PosTable> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				Predicate active = cb.equal(root.get(PosTable_.posTableIsActive), true);
				return active;
			}
		};
	}
	
	/**
	 * To get the active groups
	 * @return
	 */
	public static Specification<TherapyGroup> getActiveGroups() {
		return new Specification<TherapyGroup>() {

			@Override
			public Predicate toPredicate(Root<TherapyGroup> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				Predicate active = cb.equal(root.get(TherapyGroup_.therapyGroupIsActive), true);
				cq.where(active).orderBy(cb.asc(root.get(TherapyGroup_.therapyGroupName)));
				return cq.getRestriction();
			}
		};
	}
	
	/**
	 * To get the group data by groupId
	 * @param groupId
	 * @return
	 */
	public static Specification<TherapyGroup> getGroupData(final int groupId) {
		return new Specification<TherapyGroup>() {

			@Override
			public Predicate toPredicate(Root<TherapyGroup> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				Predicate active = cb.equal(root.get(TherapyGroup_.therapyGroupId), groupId);
				return active;
			}
		};
	}

	/**
	 * To get the therapy session data by therapysessionid
	 * @param therapyId
	 * @return
	 */
	public static Specification<TherapySession> byTherapyId(final int therapyId) {
		return new Specification<TherapySession>() {

			@Override
			public Predicate toPredicate(Root<TherapySession> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				Predicate active = cb.equal(root.get(TherapySession_.therapySessionId), therapyId);
				return active;
			}
		};
	}

	/**
	 * To get the patient data by groupId
	 * @param therapyId
	 * @return
	 */
	public static Specification<TherapyGroupPatientMapping> getPatDataBygroup(final int therapyId) {
		return new Specification<TherapyGroupPatientMapping>() {
            @Override
			public Predicate toPredicate(Root<TherapyGroupPatientMapping> root, CriteriaQuery<?> cq,CriteriaBuilder cb) {
            	
				Predicate patientid = cb.equal(root.get(TherapyGroupPatientMapping_.therapyGroupPatientMappingGroupId), therapyId);
				root.fetch(TherapyGroupPatientMapping_.patientRegistration,JoinType.INNER);
				return patientid;
			}
		};
	}
	
	/**
	 * To get the patinent data (account#, last therapy sessiondate etc.)
	 * @param patientId
	 * @param groupId
	 * @return
	 */
	public static Specification<TherapyGroupPatientMapping> getPatientFromGroup(final int patientId,final int groupId) {
		return new Specification<TherapyGroupPatientMapping>() {
            @Override
			public Predicate toPredicate(Root<TherapyGroupPatientMapping> root, CriteriaQuery<?> cq,CriteriaBuilder cb) {
            	
				Predicate pred=cb.and(cb.equal(root.get(TherapyGroupPatientMapping_.therapyGroupPatientMappingGroupId), groupId),cb.equal(root.get(TherapyGroupPatientMapping_.therapyGroupPatientMappingPatientId),patientId));
				
				return pred;
			}
		};
	}
	
	/**
	 * 
	 * @param patientId
	 * @param sessionId
	 * @return
	 */
	public static Specification<TherapySessionDetails> getPatientFromSession(final int patientId,final int sessionId) {
		return new Specification<TherapySessionDetails>() {

			@Override
			public Predicate toPredicate(Root<TherapySessionDetails> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				Predicate pred =cb.and(cb.equal(root.get(TherapySessionDetails_.therapySessionDetailsSessionId), sessionId),cb.equal(root.get(TherapySessionDetails_.therapySessionDetailsPatientId), patientId));
				return pred;
			}
		};
	}
	/**
	 * To get the therapy session by group
	 * @param therapyId
	 * @return
	 */
	
	public static Specification<TherapySession> byGroupId(final int therapyId) {
		return new Specification<TherapySession>() {

			@Override
			public Predicate toPredicate(Root<TherapySession> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				Predicate active = cb.equal(root.get(TherapySession_.therapySessionGroupId), therapyId);
				return active;
			}
		};
	}


}