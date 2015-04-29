package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.Address;
import com.glenwood.glaceemr.server.application.models.Address_;





public class AddressSpecfication {
	
	/**
	 * @param lastName	: used to search the patients 
	 * @return BooleanExpression is a  predicate  
	 */
	public static Specification<Address> byAddress(final String address)
	{
		return new Specification<Address>() {
			
			@Override
			public Predicate toPredicate(Root<Address> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate byAddress = cb.like(cb.upper(root.get(Address_.address)), address.toUpperCase());
				return byAddress;
			}
		};
	}

}
