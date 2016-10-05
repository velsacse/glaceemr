package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.H113;
import com.glenwood.glaceemr.server.application.models.H113_;
import com.glenwood.glaceemr.server.application.models.H809;
import com.glenwood.glaceemr.server.application.models.H809_;

public class PortalRecoverUserPasswordSpecifiction {

	/**
	 * @return Appointment Status List  
	 */	
	public static Specification<H809> authenticatePortalUser(final String username)
	{
		return new Specification<H809>() {

			@Override
			public Predicate toPredicate(Root<H809> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				
				root.fetch(H809_.chartH809Table, JoinType.INNER);

				Predicate securityQuestionsPredicate=cq.where(cb.equal(cb.upper(root.get(H809_.h809004)), username.toUpperCase()),cb.equal(root.get(H809_.h809006), 1)).getRestriction();

				return securityQuestionsPredicate;
			}

		};
	}
	
}
