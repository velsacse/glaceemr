package com.glenwood.glaceemr.server.application.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.AlertCategory;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.services.alertinbox.AlertCountBean;
import com.glenwood.glaceemr.server.application.services.alertinbox.AlertInboxBean;
import com.glenwood.glaceemr.server.application.services.alertinbox.AlertInboxService;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.SessionMap;

/**
 * Controller for alerts module. It has two main entities, <b>AlertEvent </b>and <b>AlertCategory</b>.
 * @author Manikandan Nachimuthu
 * <br> <br>
 * <b>Example requests:</b>
 * <br> <br>
 * <ul>
 * <li><b>Category details</b><i> AlertInbox.Action/getcategories?categoryid=1,2,3</i><br>
 * Gets the list of categories based on the category Id.</li><br>
 * <li><b>Highlight</b><i> AlertInbox.Action/highlight?alertid=18563,18557&userid=49</i><br>
 * Highlights one or more alerts and updates the modified user and date.</li><br>
 * <li><b>Un Highlight</b><i> AlertInbox.Action/unhighlight?alertid=18563,18557&userid=49</i><br>
 * Un Highlights one or more alerts and updates the modified user and date.</li><br>
 * <li><b>Read</b><i> AlertInbox.Action/markread?alertid=18563,18557&userid=49</i><br>
 * Mark one or more alerts to read and updates the modified user and date.</li><br>
 * <li><b>Un Read</b><i> AlertInbox.Action/markunread?alertid=18563,18557&userid=49</i><br>
 * Mark one or more alerts to un read and updates the modified user and date.</li><br>
 * <li><b>User Details</b><i> AlertInbox.Action/getusers</i><br>
 * Gets the list of users from employee table grouped by their group Id's.</li><br>
 * </ul>
 */
@RestController
@Transactional
@RequestMapping(value="/user/AlertInbox.Action")
public class AlertInboxController {

	@Autowired
	AlertInboxService alertInboxService;

	@Autowired
	AuditTrailService auditTrailService;

	@Autowired
	SessionMap sessionMap;

	@Autowired
	HttpServletRequest request;

	private Logger logger = Logger.getLogger(AlertInboxController.class);

	/**
	 * To get the alerts based on the offset and limits.
	 * @param userId
	 * @param categoryIds
	 * @param pageno
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getalerts", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getalerts(
			 @RequestParam(value="userid", required=false, defaultValue="true") String userId, 
			 @RequestParam(value="categoryids", required=false, defaultValue="-1") String categoryIds, 
			 @RequestParam(value="pageno", required=false, defaultValue="0") int pageno, 
			 @RequestParam(value="pagesize", required=false, defaultValue="10") int pagesize) throws Exception{

		logger.debug("Enters into get alerts");

		List<String> categoryIdList=new ArrayList<String>();
		String[] categoryIdArray = {"-1"};

		List<Map<String, List<Map<String, List<Object>>>>> alertsList;

		if(!categoryIds.equals("-1")){
			categoryIdArray=categoryIds.split(",");
		}

		for (String s : categoryIdArray) {
			categoryIdList.add(s);
		}

		alertsList=alertInboxService.getAlerts(userId,categoryIdList,pageno,pagesize);
		EMRResponseBean alertsLists=new EMRResponseBean();
		alertsLists.setData(alertsList);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");

		return alertsLists;
	}
	/**
	 * Update the status based on the list of AlertEventId's.
	 * @param alerteventid		    AlertEvent id's are separated by ","
	 * @param alertEventStatus		status of an alert.
	 * @return				        List of modified entities.
	 */
	@RequestMapping(value = "/updateStatus", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean updateStatusbyAlerEventId(
			 @RequestParam(value="statusId", required=false, defaultValue="0") String statusId,
			 @RequestParam(value="alertIds",required=true) String alertEventIds,
			 @RequestParam(value="userId",required=true) String userId){
		List<Integer> alertEventIdList=new ArrayList<Integer>();
		String[] alertEventIdsArray = {"-1"};
		List<Map<String, List<Map<String, List<Object>>>>> alertsList;
		if(!alertEventIds.equals("-1")){
			alertEventIdsArray=alertEventIds.split(",");
		}
		for (String alertEvent : alertEventIdsArray) {
			alertEventIdList.add(Integer.parseInt(alertEvent));
		}
		List<AlertEvent> alertEventLists= alertInboxService.updateStatusbyAlertEventIds(alertEventIdList,statusId,userId);
		EMRResponseBean alertsLists=new EMRResponseBean();
		alertsLists.setData(alertEventLists);
		 return alertsLists;
	}
	
	/**
	 * To get the alerts based on the offset and limits.
	 * @param userId
	 * @param categoryIds
	 * @param pageno
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getalertsBasedOnSearch(
			 @RequestParam(value="userid", required=false, defaultValue="true") String userId, 
			 @RequestParam(value="categoryids", required=false, defaultValue="-1") String categoryIds, 
			 @RequestParam(value="patientNameSearchValue", required=false, defaultValue="") String patientNameSearchValue,
			 @RequestParam(value="senderNameSearchValue", required=false, defaultValue="") String senderNameSearchValue,
			 @RequestParam(value="receiverNameSearchValue", required=false, defaultValue="") String receiverNameSearchValue,
			 @RequestParam(value="messageSearchValue", required=false, defaultValue="") String messageSearchValue,
			 @RequestParam(value="fromDateSearchValue", required=false, defaultValue="") String fromDateSearchValue,
			 @RequestParam(value="toDateSearchValue", required=false, defaultValue="") String toDateSearchValue) throws Exception{
		logger.debug("Enters into get alerts");
		List<String> categoryIdList=new ArrayList<String>();
		String[] categoryIdArray = {"-1"};
		List<Map<String, List<Object>>> alertsList;
		if(!categoryIds.equals("-1")){
			categoryIdArray=categoryIds.split(",");
		}
		for (String s : categoryIdArray) {
			categoryIdList.add(s);
		}
		alertsList = alertInboxService.searchAlerts(userId,categoryIdList,patientNameSearchValue,
				senderNameSearchValue,receiverNameSearchValue,messageSearchValue,
				fromDateSearchValue,toDateSearchValue);
		EMRResponseBean alertsLists=new EMRResponseBean();
		alertsLists.setData(alertsList);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return alertsLists;
	}
	/**
	 * Get the category list with count based on the user id
	 * @param userId 	User Id to get the list of categories with count
	 * @return
	 */
	@RequestMapping(value="/alertcount", method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getAlertCount(
			 @RequestParam(value="userid", required=false, defaultValue="") String userId){
		logger.debug("Getting alert counts");
		List<AlertCountBean> shortcutInfoBeans=alertInboxService.getAlertCount(userId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		EMRResponseBean shortcutInfoBean=new EMRResponseBean();
		shortcutInfoBean.setData(shortcutInfoBeans);
		return shortcutInfoBean;
	}
	/**
	 * Gets the alerts by categories (Pagination)
	 * @param userId		Logged in user Id
	 * @param categoryIds	Category Id to fetch details
	 * @param pageno		Page no Ex:: For first page page no will be 1
	 * @param pagesize		Size of the Page (limit)
	 * @return
	 */
	@RequestMapping(value="/alertbycategory",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean alertsByCategory(
			 @RequestParam(value="userid", required=false, defaultValue="") String userId, 
			 @RequestParam(value="categoryid", required=false, defaultValue="-1") String categoryId, 
			 @RequestParam(value="pageno", required=false, defaultValue="1") int pageno, 
			 @RequestParam(value="pagesize", required=false, defaultValue="20") int pagesize){
		logger.debug("Getting alerts by category id "+categoryId);
		List<AlertInboxBean> alertInboxBeans=alertInboxService.getAlertsByCategory(userId,categoryId,pageno,pagesize);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		EMRResponseBean alertInboxBean=new EMRResponseBean();
		alertInboxBean.setData(alertInboxBeans);
		return alertInboxBean;
	}
	/**
	 * Highlights the alerts
	 * @param alertids 		Alert id's are separated by ","
	 * @param userId   		Currently logged in users.
	 * @return 				List of modified entities.
	 */
	@RequestMapping(value = "/highlight", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean highlight(
			 @RequestParam(value="alertid",required=true) String alertids, 
			 @RequestParam(value="userid",required=true) Integer userId){
		logger.debug("Highlighting the alert with id "+alertids);
		List<Integer> alertIdList=new ArrayList<Integer>();
		String[] alertIdArray=alertids.split(",");
		for (String s : alertIdArray) {
			alertIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.hightlightAlert(alertIdList,userId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		EMRResponseBean alertEvents=new EMRResponseBean();
		alertEvents.setData(alertEvent);
		return alertEvents;
	}
	/**
	 * Un Highlights the alerts
	 * @param alertids 		Alert id's are separated by ","
	 * @param userId   		Currently logged in users.
	 * @return 				List of modified entities.
	 */
	@RequestMapping(value = "/unhighlight", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean unHighlight(
			@RequestParam(value="alertid",required=true) String alertids,
			 @RequestParam(value="userid",required=true) Integer userId){
		logger.debug("Un Highlighting the alert with id "+alertids);
		List<Integer> alertIdList=new ArrayList<Integer>();
		String[] alertIdArray=alertids.split(",");
		for (String s : alertIdArray) {
			alertIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.unHightlightAlert(alertIdList,userId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		EMRResponseBean alertEvents=new EMRResponseBean();
		alertEvents.setData(alertEvent);
		return alertEvents;
	}
	/**
	 * Marks the list of alerts to read 
	 * @param alertids		Alert id's are separated by ","
	 * @param userId		Currently logged in users.
	 * @return				List of modified entities.
	 */
	@RequestMapping(value = "/markread", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean markRead(
			 @RequestParam(value="alertid",required=true) String alertids, 
			 @RequestParam(value="userid",required=true) Integer userId){
		logger.debug("Read the alert with id "+alertids);
		List<Integer> alertIdList=new ArrayList<Integer>();
		String[] alertIdArray=alertids.split(",");
		for (String s : alertIdArray) {
			alertIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.markReadAlert(alertIdList,userId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		EMRResponseBean alertEvents=new EMRResponseBean();
		alertEvents.setData(alertEvent);
		return alertEvents;
	}
	/**
	 * Marks the list of alerts to unread 
	 * @param alertids		Alert id's are separated by ","
	 * @param userId		Currently logged in users.
	 * @return				List of modified entities.
	 */
	@RequestMapping(value = "/markunread", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean markUnRead(
			 @RequestParam(value="alertid",required=true) String alertids, 
			 @RequestParam(value="userid",required=false) Integer userId){
		logger.debug("Un Read the alert with id "+alertids);
		List<Integer> alertIdList=new ArrayList<Integer>();
		String[] alertIdArray=alertids.split(",");
		for (String s : alertIdArray) {
			alertIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.markUnReadAlert(alertIdList,userId);
		EMRResponseBean alertEvents=new EMRResponseBean();
		alertEvents.setData(alertEvent);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return alertEvents;
	}
	/**
	 * Gets the list of categories based on the category id
	 * @param categoryIds 	Category id's are separated by ","
	 * @return				List of required category details.
	 */
	@RequestMapping(value = "/getcategories", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getCategories(
			 @RequestParam(value="categoryid",required=true) String categoryIds){
		logger.debug("Getting the categories with id "+categoryIds);
		List<Integer> categoryIdList=new ArrayList<Integer>();
		String[] categoryIdArray=categoryIds.split(",");
		for (String s : categoryIdArray) {
			categoryIdList.add(Integer.parseInt(s));
		}
		List<AlertCategory> alertEvent=alertInboxService.getCategories(categoryIdList);
		EMRResponseBean alertEvents=new EMRResponseBean();
		alertEvents.setData(alertEvent);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return alertEvents;
	}
	/**
	 * Forward one or more alerts based on the alert id
	 * @param categoryIds 	Category id's are separated by ","
	 * @return				List of required category details.
	 */
	@RequestMapping(value = "/forward", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean forwardAlerts(
			 @RequestParam(value="userid",required=true) String userId, 
			 @RequestParam(value="alertid",required=true) String alertIds, 
			 @RequestParam(value="categoryid",required=true) String categoryId, 
			 @RequestParam(value="message",required=true) String message, 
			 @RequestParam(value="forwardto",required=true) String forwardTo, 
			 @RequestParam(value="ishighpriority",required=false) String ishighpriority){
		logger.debug("Forwarding the alerts with id "+alertIds);
		List<Integer> alertIdList=new ArrayList<Integer>();
		String[] alertIdArray=alertIds.split(",");
		for (String s : alertIdArray) {
			alertIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.forwardAlerts(alertIdList,categoryId, message, userId, forwardTo, ishighpriority);
		EMRResponseBean alertEvents=new EMRResponseBean();
		alertEvents.setData(alertEvent);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return alertEvents;
	}
	/**
	 * Delete the alerts
	 * @param alertids 		Alert id's are separated by ","
	 * @param userId   		Currently logged in users.
	 * @return 				List of modified entities.
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean deleteAlerts(
			 @RequestParam(value="alertid",required=true) String alertids,
			 @RequestParam(value="userid",required=true) Integer userId){
		logger.debug("Deleting the alert with id "+alertids);
		List<Integer> alertIdList=new ArrayList<Integer>();
		String[] alertIdArray=alertids.split(",");
		for (String s : alertIdArray) {
			alertIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=null;
		if(userId!=null)
			alertEvent=alertInboxService.deleteAlert(alertIdList,userId);
		EMRResponseBean alertEvents=new EMRResponseBean();
		alertEvents.setData(alertEvent);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return alertEvents;
	}
	/**
	 * Delete the alerts by encounter id.
	 * @param encounterIds 	Encounter id's are separated by ","
	 * @param userId   		Currently logged in users.
	 * @return 				List of modified entities.
	 */
	@RequestMapping(value = "/deleteByEncounterId", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean deleteByEncounterId(
			 @RequestParam(value="encounterid",required=true) String encounterIds,
			 @RequestParam(value="userid",required=true) Integer userId){
		logger.debug("Deleting the alert with encounter id "+encounterIds);
		List<Integer> encounterIdList=new ArrayList<Integer>();
		String[] alertIdArray=encounterIds.split(",");
		for (String s : alertIdArray) {
			encounterIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.deleteAlertByEncounter(encounterIdList,userId);
		EMRResponseBean alertEvents=new EMRResponseBean();
		alertEvents.setData(alertEvent);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return alertEvents;
	}
	/**
	 * Get alerts by encounter id.
	 * @param encounterId 	Encounter id's are separated by ","
	 * @return 				List of AlertEvent entities.
	 */
	@RequestMapping(value = "/alertsByEncounterId", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean byEncounterId(
			@RequestParam(value="encounterid",required=true) String encounterId){
		List<Integer> encounterIdList=new ArrayList<Integer>();
		String[] alertIdArray=encounterId.split(",");
		for (String s : alertIdArray) {
			encounterIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.alertByEncounter(encounterIdList);
		EMRResponseBean alertEvents=new EMRResponseBean();
		alertEvents.setData(alertEvent);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return alertEvents;
	}
	/**
	 * Creates alert in AlertEvent Table.
	 * @param fromid required employee fromId
	 * @param toid required employee toId
	 * @param parentid required alert parentId
	 * @param categoryid required alert categoryId
	 * @return newly inserted alert information
	 * Example url: AlertInbox.Action/compose?fromid=49&toid=-1,2&msg=test msg&parentid=123&categoryid=37
	 */
	@RequestMapping(value = "/compose", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean createAlerts(
			 @RequestParam(value="fromid",required=true) String fromid,
			@RequestParam(value="toid",required=true) String toid,
			@RequestParam(value="status",required=true, defaultValue="1") String status,
			@RequestParam(value="categoryid",required=true) String categoryid,
			@RequestParam(value="refid",required=true, defaultValue="-1") String refid,
			 @RequestParam(value="patientid",required=true, defaultValue="-1") String patientid,
			@RequestParam(value="encounterid",required=true, defaultValue="-1") String encounterid,
			@RequestParam(value="msg",required=true, defaultValue="") String msg,
			 @RequestParam(value="chartid",required=true, defaultValue="-1") String chartid,
			 @RequestParam(value="roomid",required=true, defaultValue="-1") String roomid,
			 @RequestParam(value="parentid",required=true, defaultValue="-1") String parentid){
		List<Integer> toIdList=new ArrayList<Integer>();
		String[] toIdArray=toid.split(",");
		for (String s : toIdArray) {
			toIdList.add(Integer.parseInt(s));
		}
		logger.debug("Creating the alert with id "+fromid);	
		List<AlertEvent> alertEvent=alertInboxService.composeAlert(Integer.parseInt(fromid),toIdList,Integer.parseInt(status),Integer.parseInt(categoryid),Integer.parseInt(refid),Integer.parseInt(patientid),Integer.parseInt(encounterid),msg,Integer.parseInt(chartid),Integer.parseInt(roomid),Integer.parseInt(parentid));
		EMRResponseBean alertEvents=new EMRResponseBean();
		alertEvents.setData(alertEvent);
		return alertEvents;
	}
	@RequestMapping(value = "/getConversation", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getConversation(
			 @RequestParam(value="alertid",required=true) String alertid){
		List<AlertEvent> alerts=alertInboxService.getConversion(alertid);
		EMRResponseBean alert=new EMRResponseBean();
		alert.setData(alerts);
		return alert;
	}
	@RequestMapping(value = "/forwardIcmAlert", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean forwardIcmAlert(
			 @RequestParam(value="alertid",required=true) Integer alertid,
			 @RequestParam(value="userid",required=true) Integer userId,
			 @RequestParam(value="categoryid",required=true, defaultValue="37" ) Integer categoryid,
			 @RequestParam(value="patientid",required=true, defaultValue="-1111") Integer patientid,
			 @RequestParam(value="encounterid",required=true, defaultValue="-1" ) Integer encounterid,
			@RequestParam(value="forwardto",required=true) Integer forwardto,
			 @RequestParam(value="message",required=true, defaultValue="") String message,
			 @RequestParam(value="parentalertid",required=true, defaultValue="-1" ) Integer parentalertid){
		List<AlertEvent> alerts=alertInboxService.forwardIcmAlert(alertid,userId,encounterid,patientid,categoryid,forwardto,message,parentalertid);
		EMRResponseBean alert=new EMRResponseBean();
		alert.setData(alerts);
		return alert;
	}
}