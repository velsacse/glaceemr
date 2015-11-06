package com.glenwood.glaceemr.server.application.specifications;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.ClinicalElements;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions_;
import com.glenwood.glaceemr.server.application.models.ClinicalElements_;

public class ClinicalElementsSpecification {
	
	/**
	 * Get the clinical element details of given GWID's
	 * 
	 * @param gwid
	 * 
	 */
	public static Specification<ClinicalElements> getClinicalElements(final List<String> gwid){

		return new Specification<ClinicalElements>(){
			@Override
			public Predicate toPredicate(Root<ClinicalElements> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				gwid.add("-1");
				return root.get(ClinicalElements_.clinicalElementsGwid).in(gwid);
			}

		};
	}

	/**
	 * 
	 *   Get Clinical element Details of given GWID
	 * 
	 * @param gwid
	 * 
	 */
	
	public static Specification<ClinicalElements> getClinicalElement(final String gwid){

		return new Specification<ClinicalElements>(){
			@Override
			public Predicate toPredicate(Root<ClinicalElements> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				return cb.equal(root.get(ClinicalElements_.clinicalElementsGwid),gwid);
			}

		};
	}
	
	/**
	 * Get clinical Element Options for the given GWID
	 * 
	 *  @param gwid
	 *
	 */
	public static Specification<ClinicalElementsOptions> getclinicalElementOptions(final String gwid){

		return new Specification<ClinicalElementsOptions>(){
			@Override
			public Predicate toPredicate(Root<ClinicalElementsOptions> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate isActive=cb.equal(root.get(ClinicalElementsOptions_.clinicalElementsOptionsActive),true);
				Predicate isGwid=cb.equal(root.get(ClinicalElementsOptions_.clinicalElementsOptionsGwid),gwid);
				query.orderBy(cb.asc(root.get(ClinicalElementsOptions_.clinicalElementsOptionsOrderBy)),cb.asc(root.get(ClinicalElementsOptions_.clinicalElementsOptionsValue)));
				return cb.and(isActive,isGwid);
			}

		};
	}
}
