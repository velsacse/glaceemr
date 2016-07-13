package com.glenwood.glaceemr.server.application.specifications;

import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;

public class EmployeeSpecification {

	static ArrayList<Integer> groupIdList=new ArrayList<Integer>(Arrays.asList(-1,-2,-3,-5,-6,-7,-10,-25));
	/**
	 * @param sort Example: asc
	 * @return condition to get employee details based on sorting order
	 */
	public static Specification<EmployeeProfile> getUsersList(final String sort){
		return new Specification<EmployeeProfile>() {

			@Override
			public Predicate toPredicate(Root<EmployeeProfile> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				
				Expression<Integer> exprToId=root.get(EmployeeProfile_.empProfileGroupid);
				Predicate activeEmployee=cb.and(cb.equal(root.get(EmployeeProfile_.empProfileIsActive), true),cb.notLike(cb.lower(root.get(EmployeeProfile_.empProfileFullname)), "%demo%"),exprToId.in(groupIdList));
				Predicate predicate=query.where(cb.and(cb.isNotNull(root.get(EmployeeProfile_.empProfileEmpid))),activeEmployee).getRestriction();

				if(sort.equalsIgnoreCase("asc"))
					query.orderBy(cb.asc(root.get(EmployeeProfile_.empProfileGroupid)),cb.asc(root.get(EmployeeProfile_.empProfileFullname)));
				if(sort.equalsIgnoreCase("desc"))
					query.orderBy(cb.desc(root.get(EmployeeProfile_.empProfileGroupid)),cb.asc(root.get(EmployeeProfile_.empProfileFullname)));
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
				
				Expression<Integer> exprToId=root.get(EmployeeProfile_.empProfileGroupid);
				Predicate activeEmployee=cb.and(cb.equal(root.get(EmployeeProfile_.empProfileIsActive), true),cb.notLike(cb.lower(root.get(EmployeeProfile_.empProfileFullname)), "%demo%"),exprToId.in(groupId));
				Predicate predicate=query.where(cb.and(cb.isNotNull(root.get(EmployeeProfile_.empProfileEmpid))),activeEmployee).getRestriction();
				
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
