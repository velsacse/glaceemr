package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.print.GenericLetterFooter;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.chart.print.genericfooter.GenericFooterService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.SessionMap;

 
@RestController
@RequestMapping(value="/user/LetterFooter.Action")
public class GenericFooterController {
	
	@Autowired
	GenericFooterService genericFooterService;

	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	HttpServletRequest request;
	
	private Logger logger = Logger.getLogger(GenericFooterController.class);
	
	/**
	 * Request to get the list of all generic footers
	 * 
	 */
	@RequestMapping(value = "/FetchGenericFooterList",method = RequestMethod.GET)
	public EMRResponseBean fetchGenericFooterList() throws Exception{
		logger.debug("Begin of request to get the list of all generic footers.");
		List<GenericLetterFooter> genericLetterFooterList = genericFooterService.getGenericFooterList();
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded generic footers",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded generic footers");
		logger.debug("End of request to get the list of all generic footers.");
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(genericLetterFooterList);
		return respBean;
		
	}
	
	/**
	 * Request to save generic footer
	 * 
	 */
	@RequestMapping(value = "/saveGenericFooter",method = RequestMethod.POST)
	public EMRResponseBean saveGenericFooter(@RequestParam(value="footerName") String footerName,@RequestParam(value="footerVariant") Integer footerVariant,
			@RequestParam(value="footerText") String footerText,@RequestParam(value="footerStyle") String footerStyle,@RequestParam(value="isDefault") Boolean isDefault) throws Exception{
		logger.debug("Begin of request to Save generic footer.");
		
		GenericLetterFooter newGenericLetterFooter=new GenericLetterFooter();
		newGenericLetterFooter.setGenericLetterFooterName(footerName);
		newGenericLetterFooter.setGenericLetterFooterVariant(footerVariant);
		newGenericLetterFooter.setGenericLetterFooterCustom(footerText);
		newGenericLetterFooter.setGenericLetterFooterStyle(footerStyle);
		newGenericLetterFooter.setGenericLetterFooterIsDefault(isDefault);
		newGenericLetterFooter.setGenericLetterFooterIsActive(true);
		GenericLetterFooter genericLetterFooter = genericFooterService.saveGenericFooter(newGenericLetterFooter);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.CREATED,1,AuditLogConstants.SUCCESS,"Successfully saved generic footer",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully saved generic footer");
		logger.debug("End of request to Save generic footer.");
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(genericLetterFooter);
		return respBean;
		
	}
	
	/**
	 * Request to update generic footer
	 * 
	 */
	@RequestMapping(value = "/updateGenericFooter",method = RequestMethod.POST)
	public EMRResponseBean updateGenericFooter(@RequestParam(value="footerName") String footerName,@RequestParam(value="footerVariant") Integer footerVariant,
			@RequestParam(value="footerText") String footerText,@RequestParam(value="footerStyle") String footerStyle,
			@RequestParam(value="isActive") Boolean isActive,@RequestParam(value="isDefault") Boolean isDefault,
			@RequestParam(value="footerId") Integer footerId) throws Exception{
		logger.debug("Begin of request to Update generic footer.");
		
		GenericLetterFooter updateGenericLetterFooter=new GenericLetterFooter();
		updateGenericLetterFooter.setGenericLetterFooterId(footerId);
		updateGenericLetterFooter.setGenericLetterFooterName(footerName);
		updateGenericLetterFooter.setGenericLetterFooterVariant(footerVariant);
		updateGenericLetterFooter.setGenericLetterFooterCustom(footerText);
		updateGenericLetterFooter.setGenericLetterFooterStyle(footerStyle);
		updateGenericLetterFooter.setGenericLetterFooterIsDefault(isDefault);
		updateGenericLetterFooter.setGenericLetterFooterIsActive(isActive);
		GenericLetterFooter genericLetterFooter = genericFooterService.saveGenericFooter(updateGenericLetterFooter);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.CREATED,1,AuditLogConstants.UPDATE,"Successfully saved generic footer",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully saved generic footer");
		logger.debug("End of request to Update generic footer.");
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(genericLetterFooter);
		return respBean;
		
	}
	
	/**
	 * Request to get generic footer based on id
	 * 
	 */
	@RequestMapping(value = "/FetchGenericFooter",method = RequestMethod.GET)
	public EMRResponseBean getGenericFooter(@RequestParam(value="footerId") Integer footerId) throws Exception{
		logger.debug("Begin of request to Fetch generic footer.");
		GenericLetterFooter genericLetterFooter = genericFooterService.getGenericFooter(footerId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.UPDATE,"Successfully fetch generic footer",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully fetch generic footer");
		logger.debug("End of request to Fetch generic footer.");
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(genericLetterFooter);
		return respBean;
		
	}
	
	
}
