package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import com.glenwood.glaceemr.server.application.models.LeafLibrary;
import com.glenwood.glaceemr.server.application.models.LeafLibrary_;


public class LeafLibrarySpecification {

	
	
	public static Specification<LeafLibrary> getLeafDetailsByTemplateId(final Integer templateId){

		return new Specification<LeafLibrary>(){

			@Override
			public Predicate toPredicate(Root<LeafLibrary> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(LeafLibrary_.leafLibrarySoaptemplateId),templateId);
			}

		};
	}
	
	
	
	
}
