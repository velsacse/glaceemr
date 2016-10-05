package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.H810;
import com.glenwood.glaceemr.server.application.models.H810_;

public class PortalAlertSpecification {

	/**
	 * @param patientId	: used to get total messages list of a patient of that particular id
	 * @return BooleanExpression is a  predicate  
	 */	
	public static Specification<H810> getPortalAlertCategoryByName(final String alertCategory)
	   {
		   return new Specification<H810>() {

			@Override
			public Predicate toPredicate(Root<H810> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
								
				Predicate alertPredicate=cq.where(cb.equal(root.get(H810_.h810002), alertCategory)).getRestriction();
				
				return alertPredicate;
			}
			   
		};
	   }
	
	
}
