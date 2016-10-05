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
import com.glenwood.glaceemr.server.application.services.alertinbox.AlertInboxBean;
import com.glenwood.glaceemr.server.application.services.alertinbox.AlertInboxService;
import com.glenwood.glaceemr.server.application.services.alertinbox.AlertCountBean;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

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
@Api(value = "AlertInbox", description = "To get list of alerts and update the alerts", consumes="application/json")
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
	@ApiOperation(value = "Get the list of alerts with categories", notes = "Get the list of alerts with categories based on the userid, categoryid, page number and page size")
	@RequestMapping(value = "/getalerts", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of alerts"),
		    @ApiResponse(code = 404, message = "when user id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<Map<String, List<Map<String, List<Object>>>>> getalerts(
			@ApiParam(name="userid",value="login user id") @RequestParam(value="userid", required=false, defaultValue="true") String userId, 
			@ApiParam(name="categoryids",value="list of category id") @RequestParam(value="categoryids", required=false, defaultValue="-1") String categoryIds, 
			@ApiParam(name="pageno",value="no of page") @RequestParam(value="pageno", required=false, defaultValue="0") int pageno, 
			@ApiParam(name="pagesize",value="page length") @RequestParam(value="pagesize", required=false, defaultValue="10") int pagesize) throws Exception{

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

		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");

		return alertsList;
	}


	/**
	 * Get the category list with count based on the user id
	 * @param userId 	User Id to get the list of categories with count
	 * @return
	 */
	@ApiOperation(value = "Get the alert counts", notes = "Get the list of categories with alerts count.")
	@RequestMapping(value="/alertcount", method=RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of alert count"),
		    @ApiResponse(code = 404, message = "when user id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertCountBean> getAlertCount(
			@ApiParam(name="userid",value="login user id") @RequestParam(value="userid", required=false, defaultValue="") String userId){
		logger.debug("Getting alert counts");
		List<AlertCountBean> shortcutInfoBeans=alertInboxService.getAlertCount(userId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return shortcutInfoBeans;
	}

	/**
	 * Gets the alerts by categories (Pagination)
	 * @param userId		Logged in user Id
	 * @param categoryIds	Category Id to fetch details
	 * @param pageno		Page no Ex:: For first page page no will be 1
	 * @param pagesize		Size of the Page (limit)
	 * @return
	 */
	@ApiOperation(value = "Get the list of alerts by category", notes = "Get the list of alerts by category")
	@RequestMapping(value="/alertbycategory",method=RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of alerts"),
		    @ApiResponse(code = 404, message = "when user id or category id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertInboxBean> alertsByCategory(
			@ApiParam(name="userid",value="login user id") @RequestParam(value="userid", required=false, defaultValue="") String userId, 
			@ApiParam(name="categoryid",value="category id") @RequestParam(value="categoryid", required=false, defaultValue="-1") String categoryId, 
			@ApiParam(name="pageno",value="no of the page") @RequestParam(value="pageno", required=false, defaultValue="1") int pageno, 
			@ApiParam(name="pagesize",value="size ofthe page") @RequestParam(value="pagesize", required=false, defaultValue="10") int pagesize){
		logger.debug("Getting alerts by category id "+categoryId);

		List<AlertInboxBean> alertInboxBeans=alertInboxService.getAlertsByCategory(userId,categoryId,pageno,pagesize);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return alertInboxBeans;
	}


	/**
	 * Highlights the alerts
	 * @param alertids 		Alert id's are separated by ","
	 * @param userId   		Currently logged in users.
	 * @return 				List of modified entities.
	 */
	@ApiOperation(value = "Highlight alerts", notes = "Highlight one or more alerts based on the alert id")
	@RequestMapping(value = "/highlight", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of modified alerts"),
		    @ApiResponse(code = 404, message = "when user id or alert id  does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertEvent> highlight(
			@ApiParam(name="alertid",value="list of alert id") @RequestParam(value="alertid",required=true) String alertids, 
			@ApiParam(name="userid",value="login user id") @RequestParam(value="userid",required=true) Integer userId){

		logger.debug("Highlighting the alert with id "+alertids);

		List<Integer> alertIdList=new ArrayList<Integer>();
		String[] alertIdArray=alertids.split(",");

		for (String s : alertIdArray) {
			alertIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.hightlightAlert(alertIdList,userId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return alertEvent;
	}


	/**
	 * Un Highlights the alerts
	 * @param alertids 		Alert id's are separated by ","
	 * @param userId   		Currently logged in users.
	 * @return 				List of modified entities.
	 */
	@ApiOperation(value = "Un Highlight alerts", notes = "Un Highlight one or more alerts based on the alert id")
	@RequestMapping(value = "/unhighlight", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of modified alerts"),
		    @ApiResponse(code = 404, message = "when user id or alert id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertEvent> unHighlight(
			@ApiParam(name="alertid",value="list of alert id") @RequestParam(value="alertid",required=true) String alertids,
			@ApiParam(name="userid",value="login user id") @RequestParam(value="userid",required=true) Integer userId){

		logger.debug("Un Highlighting the alert with id "+alertids);

		List<Integer> alertIdList=new ArrayList<Integer>();
		String[] alertIdArray=alertids.split(",");

		for (String s : alertIdArray) {
			alertIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.unHightlightAlert(alertIdList,userId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");

		return alertEvent;
	}


	/**
	 * Marks the list of alerts to read 
	 * @param alertids		Alert id's are separated by ","
	 * @param userId		Currently logged in users.
	 * @return				List of modified entities.
	 */
	@ApiOperation(value = "Mark read alerts", notes = "Mark read one or more alerts based on the alert id")
	@RequestMapping(value = "/markread", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of modified alerts"),
		    @ApiResponse(code = 404, message = "when user id or alert id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertEvent> markRead(
			@ApiParam(name="alertid",value="list of alert id") @RequestParam(value="alertid",required=true) String alertids, 
			@ApiParam(name="userid",value="login user id") @RequestParam(value="userid",required=true) Integer userId){

		logger.debug("Read the alert with id "+alertids);

		List<Integer> alertIdList=new ArrayList<Integer>();
		String[] alertIdArray=alertids.split(",");

		for (String s : alertIdArray) {
			alertIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.markReadAlert(alertIdList,userId);

		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");

		return alertEvent;
	}


	/**
	 * Marks the list of alerts to unread 
	 * @param alertids		Alert id's are separated by ","
	 * @param userId		Currently logged in users.
	 * @return				List of modified entities.
	 */
	@ApiOperation(value = "Mark unread alerts", notes = "Mark unread one or more alerts based on the alert id")
	@RequestMapping(value = "/markunread", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of modified alerts"),
		    @ApiResponse(code = 404, message = "when user id or alert id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertEvent> markUnRead(
			@ApiParam(name="alertid",value="list of alert id") @RequestParam(value="alertid",required=true) String alertids, 
			@ApiParam(name="userid",value="login user id") @RequestParam(value="userid",required=false) Integer userId){

		logger.debug("Un Read the alert with id "+alertids);

		List<Integer> alertIdList=new ArrayList<Integer>();
		String[] alertIdArray=alertids.split(",");

		for (String s : alertIdArray) {
			alertIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.markUnReadAlert(alertIdList,userId);

		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");

		return alertEvent;
	}


	/**
	 * Gets the list of categories based on the category id
	 * @param categoryIds 	Category id's are separated by ","
	 * @return				List of required category details.
	 */
	@ApiOperation(value = "Get list of categories", notes = "Get list of categories based on category id")
	@RequestMapping(value = "/getcategories", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of categories"),
		    @ApiResponse(code = 404, message = "when category id  does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertCategory> getCategories(
			@ApiParam(name="categoryid",value="list of category id") @RequestParam(value="categoryid",required=true) String categoryIds){

		logger.debug("Getting the categories with id "+categoryIds);

		List<Integer> categoryIdList=new ArrayList<Integer>();
		String[] categoryIdArray=categoryIds.split(",");

		for (String s : categoryIdArray) {
			categoryIdList.add(Integer.parseInt(s));
		}
		List<AlertCategory> alertEvent=alertInboxService.getCategories(categoryIdList);

		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");

		return alertEvent;
	}


	/**
	 * Forward one or more alerts based on the alert id
	 * @param categoryIds 	Category id's are separated by ","
	 * @return				List of required category details.
	 */
	@ApiOperation(value = "Forward the alerts", notes = "Forward the list of alerts with message and priority level")
	@RequestMapping(value = "/forward", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of modified alerts"),
		    @ApiResponse(code = 404, message = "when user id or alert id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertEvent> forwardAlerts(
			@ApiParam(name="userid",value="logged in user id") @RequestParam(value="userid",required=true) String userId, 
			@ApiParam(name="alertid",value="alert id") @RequestParam(value="alertid",required=true) String alertIds, 
			@ApiParam(name="categoryid",value="category id") @RequestParam(value="categoryid",required=true) String categoryId, 
			@ApiParam(name="message",value="message") @RequestParam(value="message",required=true) String message, 
			@ApiParam(name="forwardto",value="to id") @RequestParam(value="forwardto",required=true) String forwardTo, 
			@ApiParam(name="ishighpriority",value="priority status") @RequestParam(value="ishighpriority",required=false) String ishighpriority){

		logger.debug("Forwarding the alerts with id "+alertIds);

		List<Integer> alertIdList=new ArrayList<Integer>();
		String[] alertIdArray=alertIds.split(",");

		for (String s : alertIdArray) {
			alertIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.forwardAlerts(alertIdList,categoryId, message, userId, forwardTo, ishighpriority);

		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");

		return alertEvent;
	}


	/**
	 * Delete the alerts
	 * @param alertids 		Alert id's are separated by ","
	 * @param userId   		Currently logged in users.
	 * @return 				List of modified entities.
	 */
	@ApiOperation(value = "Delete alerts", notes = "Delete one or more alerts based on the alert id (update alert_event_status to 2)")
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of modified alerts"),
		    @ApiResponse(code = 404, message = "when user id or alert id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertEvent> deleteAlerts(
			@ApiParam(name="alertid",value="alert id") @RequestParam(value="alertid",required=true) String alertids,
			@ApiParam(name="userid",value="logged in user id") @RequestParam(value="userid",required=true) Integer userId){

		logger.debug("Deleting the alert with id "+alertids);

		List<Integer> alertIdList=new ArrayList<Integer>();
		String[] alertIdArray=alertids.split(",");

		for (String s : alertIdArray) {
			alertIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.deleteAlert(alertIdList,userId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");

		return alertEvent;
	}


	/**
	 * Delete the alerts by encounter id.
	 * @param encounterIds 	Encounter id's are separated by ","
	 * @param userId   		Currently logged in users.
	 * @return 				List of modified entities.
	 */
	@ApiOperation(value = "Delete alerts", notes = "Delete one or more alerts based on the alert id (update alert_event_status to 2)")
	@RequestMapping(value = "/deleteByEncounterId", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of modified alerts"),
		    @ApiResponse(code = 404, message = "when user id or alert id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertEvent> deleteByEncounterId(
			@ApiParam(name="encounterid",value="encounter id") @RequestParam(value="encounterid",required=true) String encounterIds,
			@ApiParam(name="userid",value="logged in user id") @RequestParam(value="userid",required=true) Integer userId){

		logger.debug("Deleting the alert with encounter id "+encounterIds);

		List<Integer> encounterIdList=new ArrayList<Integer>();
		String[] alertIdArray=encounterIds.split(",");

		for (String s : alertIdArray) {
			encounterIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.deleteAlertByEncounter(encounterIdList,userId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");

		return alertEvent;
	}


	/**
	 * Get alerts by encounter id.
	 * @param encounterId 	Encounter id's are separated by ","
	 * @return 				List of AlertEvent entities.
	 */
	@ApiOperation(value = "Get alerts by encounter id", notes = "Delete one or more alerts based on the alert id (update alert_event_status to 2)")
	@RequestMapping(value = "/alertsByEncounterId", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of modified alerts"),
		    @ApiResponse(code = 404, message = "when user id or alert id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertEvent> byEncounterId(
			@ApiParam(name="encounterid",value="encounter id") @RequestParam(value="encounterid",required=true) String encounterId){

		List<Integer> encounterIdList=new ArrayList<Integer>();
		String[] alertIdArray=encounterId.split(",");

		for (String s : alertIdArray) {
			encounterIdList.add(Integer.parseInt(s));
		}
		List<AlertEvent> alertEvent=alertInboxService.alertByEncounter(encounterIdList);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");

		return alertEvent;
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
	@ApiOperation(value = "Create alerts", notes = "Create one alert (insert new alert into alert_event table)")
	@RequestMapping(value = "/compose", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of modified alerts"),
		    @ApiResponse(code = 404, message = "when user id or alert id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertEvent> createAlerts(
			@ApiParam(name="fromid",value="logged in user id") @RequestParam(value="fromid",required=true) String fromid,
			@ApiParam(name="toid",value="to id") @RequestParam(value="toid",required=true) String toid,
			@ApiParam(name="status",value="alert status") @RequestParam(value="status",required=true, defaultValue="1") String status,
			@ApiParam(name="categoryid",value="category id") @RequestParam(value="categoryid",required=true) String categoryid,
			@ApiParam(name="refid",value="reference id") @RequestParam(value="refid",required=true, defaultValue="-1") String refid,
			@ApiParam(name="patientid",value="patient id") @RequestParam(value="patientid",required=true, defaultValue="-1") String patientid,
			@ApiParam(name="encounterid",value="encounter id") @RequestParam(value="encounterid",required=true, defaultValue="-1") String encounterid,
			@ApiParam(name="msg",value="message") @RequestParam(value="msg",required=true, defaultValue="") String msg,
			@ApiParam(name="chartid",value="chart id") @RequestParam(value="chartid",required=true, defaultValue="-1") String chartid,
			@ApiParam(name="roomid",value="room id") @RequestParam(value="roomid",required=true, defaultValue="-1") String roomid,
			@ApiParam(name="parentid",value="parent id") @RequestParam(value="parentid",required=true, defaultValue="-1") String parentid){
		
		List<Integer> toIdList=new ArrayList<Integer>();
		String[] toIdArray=toid.split(",");
		for (String s : toIdArray) {
			toIdList.add(Integer.parseInt(s));
		}
		logger.debug("Creating the alert with id "+fromid);	
		List<AlertEvent> alertEvent=alertInboxService.composeAlert(Integer.parseInt(fromid),toIdList,Integer.parseInt(status),Integer.parseInt(categoryid),Integer.parseInt(refid),Integer.parseInt(patientid),Integer.parseInt(encounterid),msg,Integer.parseInt(chartid),Integer.parseInt(roomid),Integer.parseInt(parentid));
		return alertEvent;
	}
	
	@ApiOperation(value = "getConversation", notes = "get internal communication message conversion")
	@RequestMapping(value = "/getConversation", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of modified alerts"),
		    @ApiResponse(code = 404, message = "when user alert id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertEvent> getConversation(
			@ApiParam(name="alertid",value="alert id") @RequestParam(value="alertid",required=true) String alertid){
		
		List<AlertEvent> alerts=alertInboxService.getConversion(alertid);
		return alerts;
	}
	
	@ApiOperation(value = "forwardIcmAlert", notes = "forward internal communication message")
	@RequestMapping(value = "/forwardIcmAlert", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
		    @ApiResponse(code = 200, message = "Successful retrieval of modified alerts"),
		    @ApiResponse(code = 404, message = "when user alert id does not exist"),
		    @ApiResponse(code = 500, message = "Internal server error")})
	public List<AlertEvent> forwardIcmAlert(
			@ApiParam(name="alertid",value="alert id") @RequestParam(value="alertid",required=true) Integer alertid,
			@ApiParam(name="userid",value="logged in user id") @RequestParam(value="userid",required=true) Integer userId,
			@ApiParam(name="categoryid",value="category id") @RequestParam(value="categoryid",required=true, defaultValue="37" ) Integer categoryid,
			@ApiParam(name="patientid",value="patient id") @RequestParam(value="patientid",required=true, defaultValue="-1111") Integer patientid,
			@ApiParam(name="encounterid",value="encounter id") @RequestParam(value="encounterid",required=true, defaultValue="-1" ) Integer encounterid,
			@ApiParam(name="forwardto",value="to id") @RequestParam(value="forwardto",required=true) Integer forwardto,
			@ApiParam(name="message",value="message") @RequestParam(value="message",required=true, defaultValue="") String message,
			@ApiParam(name="parentalertid",value="parent alertid") @RequestParam(value="parentalertid",required=true, defaultValue="-1" ) Integer parentalertid){
	
		List<AlertEvent> alerts=alertInboxService.forwardIcmAlert(alertid,userId,encounterid,patientid,categoryid,forwardto,message,parentalertid);
		return alerts;
	}
}