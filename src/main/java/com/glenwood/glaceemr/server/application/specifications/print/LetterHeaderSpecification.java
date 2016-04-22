package com.glenwood.glaceemr.server.application.specifications.print;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.print.LetterHeaderContent;
import com.glenwood.glaceemr.server.application.models.print.LetterHeaderContent_;


public class LetterHeaderSpecification {
	
	public static Specification<LetterHeaderContent> getLetterHeaderContent(final Integer headerId){

		return new Specification<LetterHeaderContent>(){

			@Override
			public Predicate toPredicate(Root<LetterHeaderContent> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate headerCondition=cb.equal(root.get(LetterHeaderContent_.letterHeaderContentStyleMapId),headerId);
				
				return headerCondition;
			}
		};
	}

}
