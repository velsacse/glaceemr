package com.glenwood.glaceemr.server.application.specifications.print;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.H076;
import com.glenwood.glaceemr.server.application.models.InsCompAddr;
import com.glenwood.glaceemr.server.application.models.InsCompany;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;

public class GenericPrintSpecification {

	public static Specification<PatientRegistration> getPatientDetails(final int patientId){
		return new Specification<PatientRegistration>() {

			@SuppressWarnings({ "unused", "unchecked" })
			@Override
			public Predicate toPredicate(Root<PatientRegistration> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				
				Fetch<PatientRegistration, H076> patRefJoin = root.fetch(PatientRegistration_.referringPhyTable,JoinType.LEFT);
				Join<PatientRegistration, PatientInsDetail> patInsAddrJoin = (Join<PatientRegistration, PatientInsDetail>) root.fetch(PatientRegistration_.patientInsuranceTable, JoinType.LEFT);
				Fetch<PatientInsDetail, InsCompAddr> compJoin = patInsAddrJoin.fetch("insCompAddr",JoinType.LEFT);
				Fetch<InsCompAddr, InsCompany> insComp = compJoin.fetch("insCompany", JoinType.LEFT);
				Fetch<PatientRegistration, EmployeeProfile> empJoin = root.fetch("empProfile", JoinType.LEFT);
				
				/*if (Long.class != query.getResultType()) {
					root.fetch(PatientRegistration_.referringPhyTable,JoinType.LEFT);
					root.fetch(PatientRegistration_.patientInsuranceTable, JoinType.LEFT);
				}*/
				
				Predicate patientIdPred= cb.equal(root.get(PatientRegistration_.patientRegistrationId), patientId);
				Predicate insTypePred = cb.equal(patInsAddrJoin.get(PatientInsDetail_.patientInsDetailInstype), 2);
				Predicate insIsactivePred = cb.equal(patInsAddrJoin.get(PatientInsDetail_.patientInsDetailIsactive), true);
				
				Predicate finalPred = cb.and(patientIdPred, insTypePred, insIsactivePred);
				return finalPred;
			}

		};
	}

}
