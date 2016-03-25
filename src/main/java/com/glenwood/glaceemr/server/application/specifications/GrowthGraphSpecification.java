package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.H650;
import com.glenwood.glaceemr.server.application.models.H650_;

public class GrowthGraphSpecification {
	
	/**
	 * Specification to fetch all graph list based on ageInMonth and sex
	 * @param ageInMonth
	 * @param sex (1 is male 0 is female)
	 * @return
	 */
	public static Specification<H650> byAgeandSex(final Integer ageInMonth, final Integer sex)
	{
		return new Specification<H650>() {

			@Override
			public Predicate toPredicate(Root<H650> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {


				Predicate fromId=cb.greaterThanOrEqualTo(root.get(H650_.h650001), 1);
				Predicate toId=cb.lessThanOrEqualTo(root.get(H650_.h650001), 10);
				Predicate cond=cb.equal(root.get(H650_.h650017), true);
				Predicate ageInMonthLeast=cb.greaterThanOrEqualTo(root.get(H650_.h650004), 0);
				Predicate ageInMonthHighest=cb.lessThanOrEqualTo(root.get(H650_.h650004), ageInMonth);
				Predicate sexPredicate=null;
				if(sex==1)
					sexPredicate=cb.equal(root.get(H650_.h650003), "M");
				else
					sexPredicate=cb.equal(root.get(H650_.h650003), "F");
				Predicate predicate=cb.and(fromId,toId,cond,ageInMonthLeast,ageInMonthHighest,sexPredicate);
				cq.orderBy(cb.asc(root.get(H650_.h650007)));
				return predicate;
			}
			
		};
	}
}
