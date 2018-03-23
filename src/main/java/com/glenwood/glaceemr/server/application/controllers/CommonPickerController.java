package com.glenwood.glaceemr.server.application.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailSaveService;
import com.glenwood.glaceemr.server.application.services.commonPicker.CommonPickerService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.SessionMap;

/**
 * 
 * @author Sai Swaroop
 *
 */
@RestController
@Transactional
@RequestMapping(value="/user/CommonPicker")
public class CommonPickerController {

	@Autowired
	CommonPickerService commonPickerService;

	@Autowired
	AuditTrailSaveService auditTrailSaveService;

	@Autowired
	SessionMap sessionMap;

	@Autowired
	HttpServletRequest request;

	/**
	 * To get the reason picker data.
	 * @return
	 */
	@RequestMapping(value="/getreasons",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getReasons(@RequestParam(value="searchCode",required=true,defaultValue="") String searchCode,
			@RequestParam(value="offset",required=true,defaultValue="0") String offset,
			@RequestParam(value="limit",required=true,defaultValue="10") String limit,
			@RequestParam(value="Group",required=true,defaultValue="-1") String group,
			@RequestParam(value="selectedValue",required=true,defaultValue="") String selectedVal,
			@RequestParam(value="userGroupsRequired",required=true,defaultValue="false") String userGroupsRequired,
			@RequestParam(value="dataRequired",required=true,defaultValue="true") String dataRequired){

		JSONObject data=commonPickerService.getReasonData(searchCode,offset,limit,group,selectedVal,userGroupsRequired,dataRequired);
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(data.toString());
		return emrResponseBean;
	}


	/**
	 * To get add new reason picker data
	 * @return
	 */
	@RequestMapping(value="/getAddReasonData",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getAddReasonData(){

		EMRResponseBean emrResponseBean=new EMRResponseBean();
		JSONObject apptTypes = commonPickerService.getAddNewReasonData();
		emrResponseBean.setData(apptTypes.toString());
		return emrResponseBean;
	}

	/**
	 * To save the new reason data
	 * @return
	 */
	@RequestMapping(value="/saveReasonData",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean saveReasonData(@RequestParam(value="categoryName",required=true,defaultValue="") String categoryName,
			@RequestParam(value="comboId",required=false,defaultValue="-1") String comboId,
			@RequestParam(value="isActiveVal",required=false,defaultValue="") String isActiveVal,
			@RequestParam(value="visitReason",required=false,defaultValue="") String visitReason,
			@RequestParam(value="visitTypeId",required=false,defaultValue="") String visitTypeId){

		EMRResponseBean emrResponseBean=new EMRResponseBean();
		commonPickerService.saveReasonData(categoryName,comboId,isActiveVal,visitReason,visitTypeId);
		return emrResponseBean;
	}


	/**
	 * To get the referral doctor data.
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/getRefferalDocDetails",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getRefferralDocDetails(@RequestParam(value="searchCode",required=false,defaultValue="") String searchCode,
			@RequestParam(value="lastName",required=false,defaultValue="") String lastName,
			@RequestParam(value="firstName",required=false,defaultValue="") String firstName,
			@RequestParam(value="npi",required=false,defaultValue="") String npi,
			@RequestParam(value="address",required=false,defaultValue="") String address,
			@RequestParam(value="fax",required=false,defaultValue="") String fax,
			@RequestParam(value="phone",required=false,defaultValue="") String phone,
			@RequestParam(value="directEmail",required=false,defaultValue="-1") String directEmail,
			@RequestParam(value="specialization",required=false,defaultValue="-1") String specialization,
			@RequestParam(value="group",required=false,defaultValue="-1") String group,
			@RequestParam(value="selectedValue",required=false,defaultValue="") String selectedValue,
			@RequestParam(value="userGroupsRequired",required=false,defaultValue="false") String userGroupsRequired,
			@RequestParam(value="dataRequired",required=false,defaultValue="true") String dataRequired,
			@RequestParam(value="offset",required=false,defaultValue="1") String offset,
			@RequestParam(value="limit",required=false,defaultValue="1") String limit) throws Exception{

		JSONObject data=commonPickerService.getReferralData(searchCode,lastName,firstName,npi,address,fax,phone,directEmail,specialization,group,selectedValue,userGroupsRequired,dataRequired,offset,limit);
		EMRResponseBean emrResponseBean=new EMRResponseBean();
		emrResponseBean.setData(data.toString());
		return emrResponseBean;
	}


	/**
	 * To save the referral doctor data
	 * @return
	 */
	@RequestMapping(value="/saveReferralData",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean saveReferralData(@RequestParam(value="fromSave",required=true,defaultValue="") String fromSave,
			@RequestParam(value="comboId",required=false,defaultValue="-1") String comboId,
			@RequestParam(value="isActiveVal",required=false,defaultValue="") String isActiveVal,
			@RequestParam(value="midName",required=false,defaultValue="") String midName,
			@RequestParam(value="address",required=false,defaultValue="") String address,
			@RequestParam(value="city",required=false,defaultValue="") String city,
			@RequestParam(value="state",required=false,defaultValue="") String state,
			@RequestParam(value="zip",required=false,defaultValue="") String zip,
			@RequestParam(value="phoneno",required=false,defaultValue="") String phoneno,
			@RequestParam(value="faxno",required=false,defaultValue="") String faxno,
			@RequestParam(value="firstname",required=false,defaultValue="") String firstname,
			@RequestParam(value="lastname",required=false,defaultValue="") String lastname,
			@RequestParam(value="uid",required=false,defaultValue="") String uid,
			@RequestParam(value="mid",required=false,defaultValue="") String mid,
			@RequestParam(value="bid",required=false,defaultValue="") String bid,
			@RequestParam(value="credential",required=false,defaultValue="") String credential,
			@RequestParam(value="special",required=false,defaultValue="") String special,
			@RequestParam(value="Referraltype",required=false,defaultValue="1") String Referraltype,
			@RequestParam(value="refDocname",required=false,defaultValue="") String refDocname,
			@RequestParam(value="prefix",required=false,defaultValue="") String prefix,
			@RequestParam(value="taxonomy",required=false,defaultValue="") String taxonomy,
			@RequestParam(value="rdpracname",required=false,defaultValue="") String rdpracname,
			@RequestParam(value="txtTitle",required=false,defaultValue="") String txtTitle,
			@RequestParam(value="directemail",required=false,defaultValue="") String directemail,
			@RequestParam(value="emailid",required=false,defaultValue="") String emailid){

		EMRResponseBean emrResponseBean=new EMRResponseBean();
		commonPickerService.saveReferralData(fromSave,comboId,isActiveVal,midName,address,city,state,zip,phoneno,faxno,firstname,lastname,uid,mid,bid,credential,special,Referraltype,
				refDocname,prefix,taxonomy,rdpracname,txtTitle,directemail,emailid);
		return emrResponseBean;
	}

	/**
	 * To get the prefix and state codes info & 
	 * particular referral doctor data based on refdoctorId
	 * @return
	 */
	@RequestMapping(value="/getAddtionalRefData",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getAddtionalRefData(@RequestParam(value="mode",required=true,defaultValue="") String mode,
			@RequestParam(value="docId",required=false,defaultValue="") String docId){

		EMRResponseBean emrResponseBean=new EMRResponseBean();
		JSONObject data =commonPickerService.getAddtionalRefData(mode,docId);
		emrResponseBean.setData(data.toString());
		return emrResponseBean;
	}

	/**
	 * To get the zip codes data
	 * @return
	 */
	@RequestMapping(value="/getZipCodesData",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getZipCodesData(@RequestParam(value="city",required=false,defaultValue="-1") String city,
			@RequestParam(value="state",required=false,defaultValue="-1") String state,
			@RequestParam(value="zip",required=false,defaultValue="-1") String zip,
			@RequestParam(value="mode",required=false,defaultValue="1") String mode,
			@RequestParam(value="limit",required=false,defaultValue="1") String limit,
			@RequestParam(value="offset",required=false,defaultValue="1") String offset){

		EMRResponseBean emrResponseBean=new EMRResponseBean();
		JSONObject zipCodes = commonPickerService.getZipCodesData(city,state,zip,mode,limit,offset);
		emrResponseBean.setData(zipCodes.toString());
		return emrResponseBean;
	}

	/**
	 * To get the patient picker data
	 * @return
	 */
	@RequestMapping(value="/getPatientPickerData",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getPatientPickerData(@RequestParam(value="pageSize",required=false,defaultValue="1") String pageSize,
			@RequestParam(value="searchType",required=false,defaultValue="0") String searchType,
			@RequestParam(value="searchMode",required=false,defaultValue="0") String searchMode,
			@RequestParam(value="currentPage",required=false,defaultValue="1") String currentPage,
			@RequestParam(value="firstName",required=false,defaultValue="") String firstName,
			@RequestParam(value="lastName",required=false,defaultValue="") String lastName,
			@RequestParam(value="dob",required=false,defaultValue="") String dob,
			@RequestParam(value="ssn",required=false,defaultValue="") String ssn,
			@RequestParam(value="accountNo",required=false,defaultValue="") String accountNo,
			@RequestParam(value="patientSearchType",required=false,defaultValue="active") String patientSearchType){

		EMRResponseBean emrResponseBean=new EMRResponseBean();
		JSONObject patientPickerData = commonPickerService.getPatientPickerData(pageSize,searchType,searchMode,currentPage,firstName,lastName,dob,ssn,accountNo,patientSearchType);
		emrResponseBean.setData(patientPickerData.toString());
		return emrResponseBean;
	}


	/**
	 * To save the manage group reason data
	 * @return
	 */
	@RequestMapping(value="/saveManageGroupData",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean saveManageGroupData(@RequestParam(value="groupName",required=false,defaultValue="") String groupname,
			@RequestParam(value="defaultValue",required=false,defaultValue="-1") String defaultvalue,
			@RequestParam(value="activeValue",required=false,defaultValue="-1") String activevalue,
			@RequestParam(value="newGroup",required=false,defaultValue="-1") String newgroup,
			@RequestParam(value="updateIndex",required=false,defaultValue="-1") String updateindex,
			@RequestParam(value="groupIdToUpdate",required=false,defaultValue="-1") String groupIdToUpdate){

		EMRResponseBean emrResponseBean=new EMRResponseBean();
		commonPickerService.saveManageGroupData(groupname,defaultvalue,activevalue,newgroup,updateindex,groupIdToUpdate);
		return emrResponseBean;
	}



}
