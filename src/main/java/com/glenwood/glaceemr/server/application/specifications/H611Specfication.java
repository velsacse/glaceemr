package com.glenwood.glaceemr.server.application.specifications;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.CodingSystems;
import com.glenwood.glaceemr.server.application.models.H611;
import com.glenwood.glaceemr.server.application.models.H611_;


public class H611Specfication {
	
	/**
	 * Get assessments of patient in all his encounters 
	 * @param encounterList
	 * @return
	 */
	public static Specification<H611> getByEncounterId(final List<Integer> encounterList)
	{
		return new Specification<H611>() {
			
			@Override
			public Predicate toPredicate(Root<H611> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Join<H611,CodingSystems> dxCodingSystemJoin= root.join("codingsystemsTable",JoinType.INNER);
				Predicate pred=null;
				if(encounterList.size()>0)
					pred= root.get(H611_.h611002).in(encounterList);
				else
					pred= root.get(H611_.h611002).in(-999);
				
				query.where(pred).orderBy(cb.asc(root.get(H611_.h611005)));
				return query.getRestriction();
			}
		};
	}

}
