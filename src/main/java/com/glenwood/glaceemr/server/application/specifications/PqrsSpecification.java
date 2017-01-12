package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;


import com.glenwood.glaceemr.server.application.models.QualityMeasuresProviderMapping;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresProviderMapping_;

public class PqrsSpecification {

	public static Specification<QualityMeasuresProviderMapping> getMeasurenumber(final Integer providerId)
	{
		return new Specification<QualityMeasuresProviderMapping>() {

			@Override
			public Predicate toPredicate(Root<QualityMeasuresProviderMapping> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate measureid = cb.equal(root.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingProviderId), providerId);
				return measureid;
			}
		};
	}

}