package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.SoapElementDatalist;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.shortcuts.ShortcutsService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;

/**
 * Shortcuts controller
 * @author software
 *
 */

@RestController
@RequestMapping(value="/user/Shortcuts.Action")
public class ShortcutsController {
	
	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	ShortcutsService shortcutsService;
	
	private Logger logger = Logger.getLogger(PlanReferralController.class);
	
	/**
	 * Method to add a new shortcut
	 * @param tabId
	 * @param elementId
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	
	@RequestMapping(value = "/AddShortcut", method = RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean addShort(@RequestParam(value="tabId",required = false, defaultValue="-1") Integer tabId,
			   @RequestParam(value="elementId",required = false, defaultValue="") String elementId,
			   @RequestParam(value="data",required = false, defaultValue="") String data) throws JSONException {
	
		logger.debug("Begin of request to Add shortcut @@tabId"+tabId+" elementId"+elementId+" data"+data);
		
		List<SoapElementDatalist> shortcutList = shortcutsService.addShortcut(tabId, elementId, data);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Leaf ,AuditLogConstants.CREATED,1,AuditLogConstants.SUCCESS,"Successfully loaded letter headers details based on header id",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Leaf,request,"Successfully added quick notes shortcut");
		logger.debug("End of request to Add shortcut");
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(shortcutList);
		
		return respBean;

	}
	
	/**
	 * Method to delete shortcut
	 * @param shortcutId
	 * @param tabId
	 * @param elementId
	 * @return
	 * @throws JSONException
	 */
	
	@RequestMapping(value = "/deleteShortcut", method = RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean deleteShort(@RequestParam(value="shortcutId",required = false, defaultValue="-1") Integer shortcutId,
			   @RequestParam(value="tabId",required = false, defaultValue="-1") Integer tabId,
			   @RequestParam(value="elementId",required = false, defaultValue="") String elementId)
			   throws JSONException {
		
		logger.debug("Begin of request to Delete shortcut @@shortcutId:"+shortcutId);
		List<SoapElementDatalist> list = shortcutsService.fetchShortcuts(shortcutId);
		if(list != null && !list.isEmpty())
			shortcutsService.deleteShortcut(list);

		List<SoapElementDatalist> shortcutList = shortcutsService.fetchShortcuts(tabId, elementId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.Leaf,AuditLogConstants.DELETED,1,AuditLogConstants.SUCCESS,"Successfully loaded letter headers details based on header id",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.Leaf,request,"Successfully deleted quick notes shortcut");
		logger.debug("End of request to Delete shortcut");
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(shortcutList);
		return respBean;

	}
		
}
