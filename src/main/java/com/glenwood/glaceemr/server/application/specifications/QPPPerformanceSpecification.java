package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.QualityMeasuresPatientEntries;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresPatientEntries_;

public class QPPPerformanceSpecification {

	public static Specification<QualityMeasuresPatientEntries> isPatientExisting(final String measureID, final int patientID, final String reportingYear){
		
		return new Specification<QualityMeasuresPatientEntries>() {
			
			public Predicate toPredicate(Root<QualityMeasuresPatientEntries> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				Predicate predicateByMeasureId = cb.equal(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesMeasureId), measureID);
				Predicate predicateByPatientId = cb.equal(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesPatientId), patientID);
				Predicate predicateByReportingYear = cb.equal(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesReportingYear), reportingYear);
				
				Predicate resultPredicate = cb.and(predicateByMeasureId,predicateByPatientId,predicateByReportingYear);
								
				return resultPredicate;
				
			}
			
		};
		
	}
	
}
