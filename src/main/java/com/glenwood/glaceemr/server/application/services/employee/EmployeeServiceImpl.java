package com.glenwood.glaceemr.server.application.services.employee;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.repositories.EmployeeProfileRepository;
import com.glenwood.glaceemr.server.application.specifications.EmployeeSpecification;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EntityManagerFactory emf ;

	@PersistenceContext
	EntityManager em;

	@Autowired
	EmployeeProfileRepository empProfileRepository;
	
	
	/**
	 * @param groupId 
	 * @param sort Example: asc
	 * @return  list of employee details based on groupid and sorting type
	 */
	@Override
	public List<EmployeeProfile> getEmployeeDetails(String groupId, String sort) {
		List<EmployeeProfile> employeeList=null;
		if(groupId.equalsIgnoreCase("all"))
		{
			employeeList=empProfileRepository.findAll(EmployeeSpecification.getUsersList(sort));
		}
		else
		{
			employeeList=empProfileRepository.findAll(EmployeeSpecification.getUsersList(groupId,sort));
		}
		return employeeList;
	}
}