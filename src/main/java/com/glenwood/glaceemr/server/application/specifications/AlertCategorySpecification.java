package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.AlertCategory;
import com.glenwood.glaceemr.server.application.models.AlertCategory_;

public class AlertCategorySpecification {

	/**
	 * Specification to fetch all category status if alertCategoryIsworkflow is true
	 * @return
	 */
	public static Specification<AlertCategory> getWorkflowCategory(){
		return new Specification<AlertCategory>() {

			@Override
			public Predicate toPredicate(Root<AlertCategory> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {

				Predicate predicate=cq.where(cb.equal(root.get(AlertCategory_.alertCategoryIsworkflow),new Boolean(true))).getRestriction();
				cq.orderBy(cb.asc(root.get(AlertCategory_.alertCategoryId)));
				return predicate;
			}
		};
		
	}

	/**
	 * Specification to fetch category status by category id 
	 * @return
	 */
	public static Specification<AlertCategory> getWorkflowCategoryById(final int categoryId){
		return new Specification<AlertCategory>() {

			@Override
			public Predicate toPredicate(Root<AlertCategory> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {

				Predicate predicate=cq.where(cb.equal(root.get(AlertCategory_.alertCategoryId),categoryId)).getRestriction();
				return predicate;
			}
		};
		
	}
}
