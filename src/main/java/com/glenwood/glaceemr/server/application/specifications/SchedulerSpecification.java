package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.SchedulerResource;
import com.glenwood.glaceemr.server.application.models.SchedulerResource_;
/**
 * 
 * @author Manikandan
 *
 */
public class SchedulerSpecification {

	public static Specification<SchedulerResource> getResources() {
		return new Specification<SchedulerResource>() {
			
			@Override
			public Predicate toPredicate(Root<SchedulerResource> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
						Predicate isDoctorPredicate=query.where(cb.equal(root.get(SchedulerResource_.schResourceIsdoctor), 1)).getRestriction();
				return isDoctorPredicate;
			}
		};
	}
}