package com.glenwood.glaceemr.server.application.specifications;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.CurrentMedication;
import com.glenwood.glaceemr.server.application.models.CurrentMedication_;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Encounter_;
import com.glenwood.glaceemr.server.application.models.MedStatus;
import com.glenwood.glaceemr.server.application.models.MedStatus_;
import com.glenwood.glaceemr.server.application.models.MedsAdminLog;
import com.glenwood.glaceemr.server.application.models.MedsAdminLog_;
import com.glenwood.glaceemr.server.application.models.MedsAdminPlan;
import com.glenwood.glaceemr.server.application.models.MedsAdminPlan_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.Prescription;
import com.glenwood.glaceemr.server.application.models.Prescription_;

/**
 * This class represents specifications needed for prescribing/viewing medications for a patient.   
 * @author selvakumar
 *
 */
@Component
@Transactional 
public class PrescripitonSpecification {
	/**
	 * To get the patient's active medications which are reported by patient at the visit.
	 * @param patientid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Specification<CurrentMedication> getactivemedwithclass( final Integer patientid,final Date startDate,final Date endDate) {
		return new Specification<CurrentMedication>() {
			@Override
			public Predicate toPredicate(Root<CurrentMedication> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
			
				
				Join<CurrentMedication,MedStatus> currjoin=root.join(CurrentMedication_.medstatus,JoinType.INNER);
				Join<CurrentMedication,Encounter> currjoin1=root.join(CurrentMedication_.encounter,JoinType.INNER);
				Join<Encounter, Chart> cc=currjoin1.join(Encounter_.chart, JoinType.INNER);
				Join<Chart, PatientRegistration> patientjoin=cc.join(Chart_.patientRegistrationTable,JoinType.INNER);
				
							
						
				String likePattern = getLikePattern("active");
				Predicate patientId=cb.equal(root.get(CurrentMedication_.currentMedicationPatientId), patientid);
				Predicate isactive=cb.equal(root.get(CurrentMedication_.currentMedicationIsActive),true);
				Predicate medstatus=cb.like(cb.lower(currjoin.get(MedStatus_.medStatusGroup)),likePattern);
				Predicate orderDate = null;
				if(startDate!=null&&endDate!=null) {
					orderDate = cb.lessThanOrEqualTo(root.get(CurrentMedication_.currentMedicationOrderOn), endDate);
//					orderDate = cb.between(root.get(CurrentMedication_.currentMedicationOrderOn), startDate, endDate);
				}
				root.fetch(CurrentMedication_.medstatus,JoinType.INNER);
				root.fetch(CurrentMedication_.encounter,JoinType.INNER);
				if(startDate!=null&&endDate!=null) {
					cq.where(cb.and(patientId, isactive,medstatus,orderDate));
				}
				else 
					cq.where(cb.and(patientId, isactive,medstatus));
				return cq.getRestriction();
				
				
				
				
			}
		};
	}
	
	/**
	 * To get the patient's active medication which are being prescribed by the provider.
	 * @param patientid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	 public static Specification<Prescription> getactivemedwithclasspresc( final Integer patientid,final Date startDate,final Date endDate) {
			return new Specification<Prescription>() {
				@Override
				public Predicate toPredicate(Root<Prescription> root, CriteriaQuery<?> cq,
						CriteriaBuilder cb) {
				
					
					Join<Prescription,Encounter> currjoin1=root.join(Prescription_.encounter,JoinType.INNER);
//					Join<Prescription,MedsAdminPlan> medPlanJoin = root.join(Prescription_.medsAdminPlan,JoinType.INNER);
					Join<Prescription,MedStatus> currjoin=root.join(Prescription_.medstatus,JoinType.INNER);
					Join<Encounter, Chart> cc=currjoin1.join(Encounter_.chart, JoinType.INNER);
//					Join<Chart, PatientRegistration> patientjoin=cc.join(Chart_.patientRegistrationTable,JoinType.INNER);
					
					String likePattern = getLikePattern("active");
					Predicate patientId=cb.equal(root.get(Prescription_.docPrescPatientId), patientid);
					Predicate isactive=cb.equal(root.get(Prescription_.docPrescIsActive),true);
					Predicate ismedsup=cb.equal(root.get(Prescription_.docPrescIsMedSup),false);
					Predicate medstatus=cb.like(cb.lower(currjoin.get(MedStatus_.medStatusGroup)),likePattern);
					Predicate orderDate = null;
					if(startDate!=null&&endDate!=null) {
						orderDate = cb.lessThanOrEqualTo((root.get(Prescription_.docPrescOrderedDate)).as(Date.class), endDate);
//						orderDate = cb.between(root.get(Prescription_.docPrescOrderedDate), startDate, endDate);
					}
					root.fetch(Prescription_.medstatus,JoinType.INNER);
					root.fetch(Prescription_.encounter,JoinType.INNER);
					
//					root.fetch(Prescription_.medsAdminPlan,JoinType.LEFT);
					if(startDate!=null&&endDate!=null) {
						cq.where(cb.and(patientId, isactive,medstatus,ismedsup,orderDate));
					}
					else
						cq.where(cb.and(patientId, isactive,medstatus,ismedsup));
					return cq.getRestriction();
				}
			};
		}
	 
	 /**
	  * To get the plan Ids for a single medication of a patient.
	  * @param prescId
	  * @return
	  */
	public static Specification<MedsAdminPlan> getMedsPlanIds(final Integer prescId) {
		return new Specification<MedsAdminPlan>() {

			@Override
			public Predicate toPredicate(Root<MedsAdminPlan> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Predicate planId = cb.equal(root.get(MedsAdminPlan_.medsAdminPlanMedicationId),prescId);
				return planId;
			}
		};
	}
	 
	/**
	 * Retunrs the formatted the pattern
	 * @param searchTerm
	 * @return
	 */
    private static String getLikePattern(final String searchTerm) {
        StringBuilder pattern = new StringBuilder();
        pattern.append(searchTerm.toLowerCase());
        pattern.append("%");
        return pattern.toString();
    }
    
    /**
     * To get the specification for getting the med administered history based on the plan Id
     * @param planid
     * @return
     */
    public static Specification<MedsAdminLog> getMedAdministrationLogHistoryData(final Integer planid) {
 	   return new Specification<MedsAdminLog>() {

		@Override
		public Predicate toPredicate(Root<MedsAdminLog> root,
				CriteriaQuery<?> cq, CriteriaBuilder cb) {
			Predicate planIdExists = cb.equal(root.get(MedsAdminLog_.medsAdminLogPlanId),planid);
			root.fetch(MedsAdminLog_.empprofile);
			return planIdExists;
		}
 	};
 	   
    }
    
    /**
     * To get the single log of a administered medications based on its Id
     * @param logid
     * @return
     */
    public static Specification<MedsAdminLog> getMedAdministrationLogData(final Integer logid) {
  	   return new Specification<MedsAdminLog>() {

 		@Override
 		public Predicate toPredicate(Root<MedsAdminLog> root,
 				CriteriaQuery<?> cq, CriteriaBuilder cb) {
 			Predicate planIdExists = cb.equal(root.get(MedsAdminLog_.medsAdminLogId),logid);
 			root.fetch(MedsAdminLog_.empprofile);
 			return planIdExists;
 		}
  	};
  	   
     }
}


