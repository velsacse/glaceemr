
package com.glenwood.glaceemr.server.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.glenwood.glaceemr.server.application.services.fax.FaxService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;

/**
 * Controller for fax module. 
 * @author Anil Kumar
 *
 */
@Api(value = "Fax", description = "Fax", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="/user/Fax")
public class FaxController {

	@Autowired
	FaxService faxService;
	/**
	 * API to get Outboxdetails of fax
	 * @param h496004
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getOutboxDetails",method=RequestMethod.GET)
	@ResponseBody
	public  EMRResponseBean getOutboxDetails(@RequestParam(value="folderId",required=true,defaultValue="2")Integer h496004,
			@RequestParam(value="userId",required=true,defaultValue="-1")Integer userId,
			@RequestParam(value="pageNo",required=true,defaultValue="1")Integer pageNo) throws Exception{
		    EMRResponseBean emrResponse = new EMRResponseBean();
		    emrResponse.setData(faxService.getOutboxDetails(h496004,userId,pageNo));
		return emrResponse;
	}

	/**
	 * API to get Inboxdetails of fax
	 * @param h491010
	 * @param h491013
	 * @param h491014
	 * @param h491017_faxbox
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getInboxDetails",method=RequestMethod.GET)
	@ResponseBody
	public  EMRResponseBean getInboxDetails(
			@RequestParam(value="folderId",required=true,defaultValue="1")Integer h491010,
			@RequestParam(value="faxStatusId",required=false,defaultValue="-1")Integer h491013,
			@RequestParam(value="forwaredUserId",required=true,defaultValue="0")Integer h491014,
			@RequestParam(value="faxLocation",required=false,defaultValue="1")Integer h491017_faxbox,
			@RequestParam(value="pageNo",required=true,defaultValue="1")int pageNo) throws Exception{
		    EMRResponseBean  emrResponse = new EMRResponseBean();
		    emrResponse.setData(faxService.getInboxDetails(h491010,h491013,h491014,h491017_faxbox,pageNo));
		return emrResponse;
	}

	/**
	 * API to get Faxfolder count
	 * @param fax_location
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getFaxFolderCount",method=RequestMethod.GET)
	@ResponseBody
	public  EMRResponseBean getFaxFolderCount(
			@RequestParam(value="faxLocation",required=false,defaultValue="1")Integer fax_location,
			@RequestParam(value="userId",required=true,defaultValue="-1")Integer userId) throws Exception{
		    EMRResponseBean emrResponse=new EMRResponseBean();
		    emrResponse.setData(faxService.getFaxFolderCount(fax_location,userId));
		return emrResponse;
	}

	/**
	 * API to Deleteselected fax
	 * @param faxId
	 * @param folderID
	 * @param userId
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteSelectFax",method=RequestMethod.GET)
	@ResponseBody
	public void deleteSelectFax(
			@RequestParam(value="faxId" ,required=true,defaultValue="-1")String  faxId,
			@RequestParam(value="fileNames" ,required=false,defaultValue="")String fileNames,
			@RequestParam(value="folderId" ,required=false,defaultValue="3")Integer  folderID,
			@RequestParam(value="userId" ,required=true,defaultValue="-1")Integer  userId) throws Exception{
		faxService.deleteSelectFax(faxId,fileNames,folderID,userId);
	}

	/**
	 * API to deleteselscted fax from outbox
	 * @param faxId
	 * @param folderID
	 * @param userId
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteSelectOutboxFax",method=RequestMethod.GET)
	@ResponseBody
	public void deleteSelectOutboxFax (
			@RequestParam(value="faxId" ,required=true,defaultValue="-1")String faxId,
			@RequestParam(value="folderId" ,required=false,defaultValue="3")Integer folderId,
			@RequestParam(value="userId" ,required=true,defaultValue="-1")Integer userId) throws Exception{

		faxService.deleteSelectOutboxFax(faxId,folderId,userId);
	}

	/**
	 * API to get Fax forwarduserlist
	 * @param empProfileGroupid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getForwardUserList",method=RequestMethod.GET)
	@ResponseBody
	public  EMRResponseBean getForwardUserList(
			@RequestParam(value="emp_profile_groupid",required=false,defaultValue="-1")Integer empProfileGroupid)throws Exception{
		    EMRResponseBean emrResponse = new EMRResponseBean();
		    emrResponse.setData(faxService.getFaxUserList());
		return emrResponse;		
	}

	/**
	 * API to Forward a fax
	 * @param faxId
	 * @param forwardTO
	 * @param folderID
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/forwardFax",method=RequestMethod.GET)
	@ResponseBody
	public  EMRResponseBean forwardFax(
			@RequestParam(value="faxId",required=true,defaultValue="-1")String  faxId,
			@RequestParam(value="fileNames",required=false,defaultValue="")String  fileNames,
			@RequestParam(value="fowardTo",required=true,defaultValue="-1")Integer forwardTo,
			@RequestParam(value="folderId",required=true,defaultValue="-3")Integer folderId,
			@RequestParam(value="userId",required=true,defaultValue="-1")Integer userId)throws Exception{
		    EMRResponseBean emrResponse = new EMRResponseBean();
		    emrResponse.setData(faxService.forwardFax(faxId,fileNames,forwardTo,folderId,userId));
		return emrResponse;
	}
	
	/**
	 * API to get Inbound fax details
	 * @param userId
	 * @param faxId
	 * @param faxTab
	 * @param faxFolder
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getInFaxDetails",method=RequestMethod.GET)
	@ResponseBody
	public  EMRResponseBean getInFaxDetails(
			@RequestParam(value="userId",required=false,defaultValue="-1")Integer userId,
			@RequestParam(value="faxId",required=true,defaultValue="-1")Integer faxId,
			@RequestParam(value="faxTab",required=true,defaultValue="0")Integer faxTab,
			@RequestParam(value="faxFolder",required=true,defaultValue="1")Integer faxFolder)throws Exception{
		    EMRResponseBean emrResponse = new EMRResponseBean();
		    emrResponse.setData(faxService.getInFaxDetails(faxId, userId, faxTab, faxFolder));
		return emrResponse ;
	}

	/**
	 * API to get outbound fax details
	 * @param faxId
	 * @param faxTab
	 * @param faxFolder
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getOutFaxDetails",method=RequestMethod.GET)
	@ResponseBody
	public  EMRResponseBean getOutFaxDetails(
			@RequestParam(value="userId",required=false,defaultValue="-1")Integer userId,
			@RequestParam(value="faxId",required=true,defaultValue="-1")Integer faxId,
			@RequestParam(value="faxTab",required=true,defaultValue="0")Integer faxTab,
			@RequestParam(value="faxFolder",required=true,defaultValue="2")Integer faxFolder)throws Exception{
		    EMRResponseBean emrResponse = new EMRResponseBean();
		    emrResponse.setData(faxService.getOutFaxDetails(userId,faxId, faxTab, faxFolder));
		return emrResponse;
	}

	/**
	 * API to get signature details
	 * @param empProfileLoginid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getSignatureDetails",method=RequestMethod.GET)
	@ResponseBody
	public  EMRResponseBean getSignatureDetails(
			@RequestParam(value="empId",required=true,defaultValue="-1")Integer empId)throws Exception{
	       EMRResponseBean emrResponse = new EMRResponseBean();
	       emrResponse.setData(faxService.getSignatureDetails(empId));
		return  emrResponse;
	}
	
	/**
	 * API to get last fax received time
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/lastFaxReceivedTime",method=RequestMethod.GET)
	@ResponseBody
	public  EMRResponseBean lastFaxReceivedTime(
			@RequestParam(value="userId",required=false,defaultValue="-1")Integer userId)throws Exception{
		    EMRResponseBean emrResponse = new EMRResponseBean();
		    emrResponse.setData(faxService.lastFaxReceivedTime(userId));
		return emrResponse ;
	}
}