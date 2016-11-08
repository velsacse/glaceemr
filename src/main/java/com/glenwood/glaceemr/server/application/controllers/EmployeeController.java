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
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;

@Api(value = "/Employee", description = "To deal with employee details.", consumes="application/json")
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
	@ApiOperation(value = "Returns employee details", response = EmployeeProfile.class)
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of Employee details"),
		    @ApiResponse(code = 404, message = "when group id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public EMRResponseBean getEmployeeDetailsByGroupId(@ApiParam(name="groupid", value="particular user group id" ) @RequestParam(value="groupid", required=false, defaultValue="all") String groupId, @ApiParam(name="sortType" ,value="sorting type i.e asc or desc") @RequestParam(value="sortby", required=false, defaultValue="desc") String sortType)
	{
		List<EmployeeProfile> grpDetails=employeeService.getEmployeeDetails(groupId,sortType);
		EMRResponseBean grpDetail=new EMRResponseBean();
		grpDetail.setData(grpDetails);
		return grpDetail;
	}
	
}