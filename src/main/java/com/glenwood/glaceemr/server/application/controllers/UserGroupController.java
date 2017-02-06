package com.glenwood.glaceemr.server.application.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.UserGroup;
import com.glenwood.glaceemr.server.application.services.alertinbox.UserGroupService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;

/**
 * Controller for UserGroup module. It has one main entities, <b> UserGroup </b>.
 * @author Jeyanthkumar S
 */
@RestController
@Transactional 
@RequestMapping(value="/user/UserGroup.Action")
public class UserGroupController{

	@Autowired
	UserGroupService userGroupService;


	/**
	 * 
	 * @return list of all user group
	 * Example url: UserGroup.Action/getusergroup
	 */
	
	@RequestMapping(value = "/getusergroup", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean userGroupList()
	{
		List<UserGroup> grpDetails;
		grpDetails=userGroupService.getUserGroup();
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(grpDetails);
		return emrResponseBean;
	}


	/**
	 * 
	 * @param groupid :required UserGroup groupId
	 * @return list the group details from UserGroup based upon the Group Id
	 * Example url: UserGroup.Action/getusergroupbyid?groupid=1
	 */
	@RequestMapping(value = "/getusergroupbyid", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean userGroupListById(@RequestParam(value="groupid", required=true) String groupid)
	{
		List<UserGroup> grpDetails;
		grpDetails=userGroupService.getUserGroupByGroupId(Integer.parseInt(groupid));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(grpDetails);
		return emrResponseBean;
	}


	/**
	 * 
	 * @param groupId :required UserGroup groupId 
	 * @return list the deleted group details from UserGroup based upon the Group Id
	 * Example url: UserGroup.Action/delete?groupid=1
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean deleteUserGroupByGroupId(
			 @RequestParam(value="groupid", required=true) String groupId )
	{
		List<UserGroup> grpDetails=userGroupService.deleteUserGroupByGroupId(Integer.parseInt(groupId));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(grpDetails);
		return emrResponseBean;
	}


	/**
	 * 
	 * @param groupId :required UserGroup groupId
	 * @param groupName :required UserGroup groupName
	 * @param userId :required UserGroup userId
	 * @param userName :required UserGroup userName
	 * @return list the updated group details from UserGroup
	 * Example url: UserGroup.Action/update?groupid=1&groupname=test doctor&userid=1,2,3&username=doctor~~demodoctor~~testdoctor
	 */
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean updateUserGroupByGroupId(
			@RequestParam(value="groupid", required=true) String groupId,
			 @RequestParam(value="groupname", required=true) String groupName,
			 @RequestParam(value="userid", required=false) String userId,
			 @RequestParam(value="username", required=false) String userName )
			{
		List<Integer> userIdList=new ArrayList<Integer>();
		String[] userIdArray=userId.split(",");

		for (String s : userIdArray) {
			userIdList.add(Integer.parseInt(s));
		}
		List<String> userNameList=new ArrayList<String>();
		String[] userNameArray=userName.split("~~");
		for (String s : userNameArray) {
			userNameList.add(s);
		}
		List<UserGroup> grpDetails=userGroupService.updateUserGroupByGroupId(Integer.parseInt(groupId),groupName,userIdList,userNameList);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(grpDetails);
		return emrResponseBean;
			}


	/**
	 * 
	 * @param groupName :required UserGroup groupName
	 * @param userId :required UserGroup userId
	 * @param userName :required UserGroup userName
	 * @return list the created group details from UserGroup
	 * Example url: UserGroup.Action/create?groupname=test doctor&userid=1,2,3&username=doctor~~demodoctor~~demodoctor2
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean insertUserGroup(
			@RequestParam(value="groupname", required=false) String groupName,
			 @RequestParam(value="userid", required=false) String userId,
			 @RequestParam(value="username", required=false) String userName )
			{
		List<Integer> userIdList=new ArrayList<Integer>();
		String[] userIdArray=userId.split(",");

		for (String s : userIdArray) {
			userIdList.add(Integer.parseInt(s));
		}
		List<String> userNameList=new ArrayList<String>();
		String[] userNameArray=userName.split("~~");
		for (String s : userNameArray) {
			userNameList.add(s);
		}
		List<UserGroup> grpDetails=userGroupService.insertUserGroup(groupName,userIdList,userNameList);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(grpDetails);
		return emrResponseBean;
			}


}
