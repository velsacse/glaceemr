package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;

public class EmployeeSpecification {

	/**
	 * @param sort Example: asc
	 * @return condition to get employee details based on sorting order
	 */
	public static Specification<EmployeeProfile> getUsersList(final String sort){
		return new Specification<EmployeeProfile>() {

			@Override
			public Predicate toPredicate(Root<EmployeeProfile> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate predicate=query.where(cb.isNotNull(root.get(EmployeeProfile_.empProfileEmpid))).getRestriction();
				if(sort.equalsIgnoreCase("asc"))
					query.orderBy(cb.asc(root.get(EmployeeProfile_.empProfileGroupid)));
				if(sort.equalsIgnoreCase("desc"))
					query.orderBy(cb.desc(root.get(EmployeeProfile_.empProfileGroupid)));	
				return predicate;
			}
		};
	}


	/**
	 * 
	 * @param groupId
	 * @param sort Example: asc
	 * @return condition to get employee details based on sorting order and groupId
	 */
	public static Specification<EmployeeProfile> getUsersList(final String groupId,final String sort){
		return new Specification<EmployeeProfile>() {

			@Override
			public Predicate toPredicate(Root<EmployeeProfile> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate predicate=cb.equal(root.get(EmployeeProfile_.empProfileEmpid), groupId);
				if(sort.equalsIgnoreCase("asc"))
					query.orderBy(cb.asc(root.get(EmployeeProfile_.empProfileGroupid)));
				if(sort.equalsIgnoreCase("desc"))
					query.orderBy(cb.desc(root.get(EmployeeProfile_.empProfileGroupid)));	
				return predicate;
			}

		};
	}
	
	/**
	 * This specification used to return employee details based on user id
	 * @param userId
	 * @return
	 */
	public static Specification<EmployeeProfile> getUserDetailsByUserId(final int userId){
		return new Specification<EmployeeProfile>() {

			@Override
			public Predicate toPredicate(Root<EmployeeProfile> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate predicate=query.where(cb.equal(root.get(EmployeeProfile_.empProfileEmpid), userId)).getRestriction();	
				return predicate;
			}

		};
	}



}
