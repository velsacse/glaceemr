package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.services.employee.EmployeeService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;

@RestController
@Transactional
@RequestMapping(value="/user/Employee.Action")
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	/**
	 * @param groupid
	 * @param sort
	 * @return get the employee details based on group id
	 */
	@RequestMapping(value="/bygroupid", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getEmployeeDetailsByGroupId(@RequestParam(value="groupid", required=false, defaultValue="all") String groupId,  @RequestParam(value="sortby", required=false, defaultValue="desc") String sortType)
	{
		List<EmployeeProfile> grpDetails=employeeService.getEmployeeDetails(groupId,sortType);
		EMRResponseBean grpDetail=new EMRResponseBean();
		grpDetail.setData(grpDetails);
		return grpDetail;
	}
	
}