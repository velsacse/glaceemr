package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.AftercareInsMapping;
import com.glenwood.glaceemr.server.application.models.AftercareInsMapping_;
import com.glenwood.glaceemr.server.application.models.PatientAftercareData;
import com.glenwood.glaceemr.server.application.models.PatientAftercareData_;
import com.glenwood.glaceemr.server.application.models.PlanMapping;
import com.glenwood.glaceemr.server.application.models.PlanMapping_;
import com.glenwood.glaceemr.server.application.models.SoapElementDatalist;
import com.glenwood.glaceemr.server.application.models.SoapElementDatalist_;

public class PlanSpecification {

	public static Specification<SoapElementDatalist> bytabElementId(final Integer tabId, final String elementId){
		return new Specification<SoapElementDatalist>() {

			@Override
			public Predicate toPredicate(Root<SoapElementDatalist> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				query.where(cb.equal(root.get(SoapElementDatalist_.soapElementDatalistTabid), tabId),
							 cb.like(root.get(SoapElementDatalist_.soapElementDatalistElementid), elementId));
				
				return query.getRestriction();
			}
		};
	}

	public static Specification<PlanMapping> getDxMapping(final Integer insId, final String dxcode,
			final Integer mappingType) {
		return new Specification<PlanMapping>() {

			@Override
			public Predicate toPredicate(Root<PlanMapping> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				query.where(cb.equal(root.get(PlanMapping_.planMappingInstructionid), insId),
							 cb.equal(root.get(PlanMapping_.planMappingType), mappingType),
							  cb.equal(cb.trim(root.get(PlanMapping_.planMappingCode)), dxcode.trim()));
				
				return query.getRestriction();
			}
		}; 
	}

	/**
	 * Get After care instructions
	 * @param patientId
	 * @param encounterId
	 * @param insId
	 * @return
	 */
	public static Specification<PatientAftercareData> getAftercareIns(final Integer patientId,
			final Integer encounterId, final Integer insId) {

		return new Specification<PatientAftercareData>() {

			@Override
			public Predicate toPredicate(Root<PatientAftercareData> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				query.where(cb.equal(root.get(PatientAftercareData_.patientAftercareDataPatientId), patientId),
							 cb.equal(root.get(PatientAftercareData_.patientAftercareDataEncounterId), encounterId),
							  cb.equal(root.get(PatientAftercareData_.patientAftercareDataAftercareId), insId));
				
				return query.getRestriction();
			}
		};
	}

	/**
	 * Get After care instruction mapping codes
	 * @param insId
	 * @param dxCode
	 * @param insType
	 * @return
	 */
	public static Specification<AftercareInsMapping> getAftercareInsMapping(final Integer insId, final String dxCode,
			final int insType) {
		return new Specification<AftercareInsMapping>() {

			@Override
			public Predicate toPredicate(Root<AftercareInsMapping> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				query.where(cb.equal(root.get(AftercareInsMapping_.aftercareInsMappingInsId), insId),
						 	 cb.equal(cb.upper(cb.trim(root.get(AftercareInsMapping_.aftercareInsMappingCode))), dxCode),
						 	  cb.equal(root.get(AftercareInsMapping_.aftercareInsMappingInsType), insType));
				
				return query.getRestriction();
			}
		};
	}
}
