package com.glenwood.glaceemr.server.application.controllers.print;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.LeafLibrary;
import com.glenwood.glaceemr.server.application.models.print.GenericPrintStyle;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.chart.print.CustomGenericBean;
import com.glenwood.glaceemr.server.application.services.chart.print.GenericPrintBean;
import com.glenwood.glaceemr.server.application.services.chart.print.GenericPrintService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "GenericPrintConfiguration", description = "Generic Print Styles", consumes="application/json")
@RestController
@RequestMapping(value="/user/GenericPrintStyle.Action")
public class GenericPrintController {

	@Autowired
	GenericPrintService genericPrintService;
	
	@Autowired
	ObjectMapper objectMapper; 
	
	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	HttpServletRequest request;
	
	private Logger logger = Logger.getLogger(GenericPrintController.class);
	

	/**
	 * Request to get the list of all generic print styles for configuration
	 * 
	 */
	@ApiOperation(value = "Get the list of all generic print styles", notes = "Get the list of all generic print styles")
	@RequestMapping(value = "/FetchGenericPrintStyleList",method = RequestMethod.GET)
	@ResponseBody
	public List<GenericPrintStyle> fetchgenericPrintStyleList() throws Exception{
		logger.debug("Begin of request to get the list of all generic print styles.");
		List<GenericPrintStyle> genericPrintStyleList = genericPrintService.getGenericPrintStyleList();
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded header list for configuration",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded header list for configuration");
		logger.debug("End of request to get the list of all generic print styles.");
		return genericPrintStyleList;
		
	}
	

	/**
	 * Request to get generic print styles based on id for configuration
	 * 
	 */
	@ApiOperation(value = "Get generic print styles", notes = "Get generic print styles")
	@RequestMapping(value = "/FetchGenericPrintStyle",method = RequestMethod.GET)
	public GenericPrintStyle fetchgenericPrintStyle(@RequestParam(value="styleId") Integer styleId) throws Exception{
		logger.debug("Begin of request to get the list of all generic print styles.");
		GenericPrintStyle genericPrintStyleList = genericPrintService.getGenericPrintStyle(styleId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded header list for configuration",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded header list for configuration");
		logger.debug("End of request to get the list of all generic print styles.");
		return genericPrintStyleList;
		
	}
	
	/**
	 * Request to save generic footer
	 * 
	 */
	@ApiOperation(value = "Save generic print style", notes = "Save generic print style")
	@RequestMapping(value = "/saveGenericPrintStyle",method = RequestMethod.POST)
	public GenericPrintStyle saveGenericPrintStyle(@RequestParam(value="styleName") String styleName,@RequestParam(value="letterHeaderId") Integer letterHeaderId,
			@RequestParam(value="patientHeaderId") Integer patientHeaderId,@RequestParam(value="footerId") Integer footerId,
			@RequestParam(value="isDefault") Boolean isDefault) throws Exception{
		
		logger.debug("Begin of request to Save generic print style.");
		
		GenericPrintStyle newGenericPrintStyle=new GenericPrintStyle();
		newGenericPrintStyle.setGenericPrintStyleName(styleName);
		newGenericPrintStyle.setGenericPrintStyleHeaderId(letterHeaderId);
		newGenericPrintStyle.setGenericPrintStylePatientHeaderId(patientHeaderId);
		newGenericPrintStyle.setGenericPrintStyleFooterId(footerId);
		newGenericPrintStyle.setGenericPrintStyleIsDefault(isDefault);
		newGenericPrintStyle.setGenericPrintStyleIsActive(true);
		
		GenericPrintStyle genericPrintStyle = genericPrintService.saveGenericPrintStyle(newGenericPrintStyle);			
		
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.CREATED,1,AuditLogConstants.SUCCESS,"Successfully saved generic print style",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully saved generic print style");
		logger.debug("End of request to Save generic print style.");
		return genericPrintStyle;
		
	}
	
	/**
	 * Request to save generic footer
	 * 
	 */
	@ApiOperation(value = "Update generic print style", notes = "Update generic print style")
	@RequestMapping(value = "/updateGenericPrintStyle",method = RequestMethod.POST)
	public GenericPrintStyle updateGenericPrintStyle(@RequestParam(value="styleName") String styleName,@RequestParam(value="letterHeaderId") Integer letterHeaderId,
			@RequestParam(value="patientHeaderId") Integer patientHeaderId,@RequestParam(value="footerId") Integer footerId,@RequestParam(value="styleId") Integer styleId,
			@RequestParam(value="isDefault") Boolean isDefault,@RequestParam(value="isActive") Boolean isActive) throws Exception{
		
		logger.debug("Begin of request to Update generic print style.");
		
		GenericPrintStyle updateGenericPrintStyle=new GenericPrintStyle();
		updateGenericPrintStyle.setGenericPrintStyleId(styleId);
		updateGenericPrintStyle.setGenericPrintStyleName(styleName);
		updateGenericPrintStyle.setGenericPrintStyleHeaderId(letterHeaderId);
		updateGenericPrintStyle.setGenericPrintStylePatientHeaderId(patientHeaderId);
		updateGenericPrintStyle.setGenericPrintStyleFooterId(footerId);
		updateGenericPrintStyle.setGenericPrintStyleIsDefault(isDefault);
		updateGenericPrintStyle.setGenericPrintStyleIsActive(isActive);
		
		GenericPrintStyle genericPrintStyle = genericPrintService.saveGenericPrintStyle(updateGenericPrintStyle);
				
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.CREATED,1,AuditLogConstants.SUCCESS,"Successfully saved generic print style",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully saved generic print style");
		logger.debug("End of request to Update generic print style.");
		return genericPrintStyle;
		
	}
	
	/**
	 * Request to get generic print styles based on id for configuration
	 * 
	 */
	@ApiOperation(value = "Get generic print styles", notes = "Get generic print styles")
	@RequestMapping(value = "/FetchGenericPrintPDF",method = RequestMethod.GET)
	public void fetchgenericPrintPDF(@RequestParam(value="styleId") Integer styleId) throws Exception{
		logger.debug("Begin of request to get the list of all generic print styles.");
		 genericPrintService.generatePDFPreview(styleId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded header list for configuration",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded header list for configuration");
		logger.debug("End of request to get the list of all generic print styles.");
		
		
	}
	
	/**
	 * Request to get generic print styles based on id for configuration
	 * 
	 */
	@ApiOperation(value = "Get generic print styles", notes = "Get generic print styles")
	@RequestMapping(value = "/FetchGenericPrintPDFData",method = RequestMethod.GET)
	public void fetchgenericPrintPDFData(@RequestParam(value="styleId") Integer styleId,
			@RequestParam(value="patientId") Integer patientId/*,@RequestParam(value="printDetails")String printDetails*/) throws Exception{
		logger.debug("Begin of request to get the list of all generic print styles.");
		//http://localhost:7080/glaceemr_backend/api/GenericPrintStyle.Action/GetPrint?patientId=7514&encounterId=7328
		//,objectMapper.readValue(printDetails, PrintDetailsDataBean.class)
		 genericPrintService.generatePDFPreview(styleId,patientId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded header list for configuration",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded header list for configuration");
		logger.debug("End of request to get the list of all generic print styles.");
		
		
	}

	/**
	 * To get complete details of patient for print
	 * @param encounterId
	 * @param patientId
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "Getting note print", notes = "Getting note print for specific patient")
	@RequestMapping(value = "/GetPrint", method = RequestMethod.GET)
	@ResponseBody
	public GenericPrintBean getCompleteDetails(@RequestParam(value="patientId",required = false) Integer patientId,
			@RequestParam(value="encounterId",required = false) Integer encounterId) throws Exception {
	
		logger.debug("Begin of request to get the list of employees and patient details.");
		GenericPrintBean bean = genericPrintService.getCompleteDetails(patientId, encounterId);
		logger.debug("End of request to get the list of employees and patient details.");
		return bean;

	}
	
	@ApiOperation(value = "Get generic print header data", notes = "Get generic print header data")
	@RequestMapping(value = "/FetchGenericPrintHeaderData",method = RequestMethod.GET)
	@ResponseBody
	public String fetchGenericPrintHeader(@RequestParam(value="styleId") Integer styleId,
			@RequestParam(value="patientId", defaultValue="-1") Integer patientId,
			@RequestParam(value="encounterId", defaultValue="-1") Integer encounterId,
			@RequestParam(value="sharedFolderPath", defaultValue="", required=false) String sharedFolderPath) throws Exception{
		logger.debug("Begin of request to get generic print header data.");
    	String headerHTML = genericPrintService.getHeaderHTML(styleId, patientId, encounterId, sharedFolderPath);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded header list for configuration",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded header data");
		logger.debug("End of request to get generic print header data.");
		return headerHTML;
		
	}
	
	@ApiOperation(value = "Get generic print patient header data", notes = "Get generic print patient header data")
	@RequestMapping(value = "/FetchGenericPrintPatientHeaderData",method = RequestMethod.GET)
	@ResponseBody
	public String fetchGenericPrintPatientHeader(@RequestParam(value="styleId") Integer styleId,
			@RequestParam(value="patientId", defaultValue="-1") Integer patientId,
			@RequestParam(value="encounterId", defaultValue="-1") Integer encounterId) throws Exception{
		logger.debug("Begin of request to get generic print patient header data.");
    	String headerHTML = genericPrintService.getPatientHeaderHTML(styleId, patientId, encounterId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded header list for configuration",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded patient header data");
		logger.debug("End of request to get generic print patient header data.");
		return headerHTML;
		
	}
	
	@ApiOperation(value = "Get generic print footer data", notes = "Get generic print footer data")
	@RequestMapping(value = "/FetchGenericPrintFooterData",method = RequestMethod.GET)
	@ResponseBody
	public String fetchGenericPrintFooter(@RequestParam(value="styleId") Integer styleId) throws Exception{
		logger.debug("Begin of request to get generic print footer data.");
    	String headerHTML = genericPrintService.getFooterHTML(styleId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded header list for configuration",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded footer data");
		logger.debug("End of request to get generic print footer data.");
		return headerHTML;
		
	}
	
	@ApiOperation(value = "Get generic print left data", notes = "Get generic print left data")
	@RequestMapping(value = "/FetchGenericPrintLeftHeaderData",method = RequestMethod.GET)
	@ResponseBody
	public String fetchGenericPrintLeftData(@RequestParam(value="styleId") Integer styleId) throws Exception{
		logger.debug("Begin of request to get generic print left data.");
    	String headerHTML = genericPrintService.getLeftHeaderHTML(styleId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded header list for configuration",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded footer data");
		logger.debug("End of request to get generic print left data.");
		return headerHTML;
		
	}
	
	@ApiOperation(value = "Get generic print data", notes = "Get generic print data")
	@RequestMapping(value = "/FetchGenericPrintData",method = RequestMethod.GET)
	@ResponseBody
	public CustomGenericBean fetchGenericPrint(@RequestParam(value="styleId") Integer styleId,
			@RequestParam(value="patientId", defaultValue="-1") Integer patientId,
			@RequestParam(value="encounterId", defaultValue="-1") Integer encounterId,
			@RequestParam(value="sharedFolderPath", defaultValue="", required=false) String sharedFolderPath) throws Exception{
		logger.debug("Begin of request to get generic print header data.");
    	CustomGenericBean bean = genericPrintService.getCustomeGenericData(styleId, patientId, encounterId, sharedFolderPath);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded header list for configuration",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded header data");
		logger.debug("End of request to get generic print header data.");
		return bean;
		
	}

	/**
	 * Request to get template list
	 * 
	 */
	@ApiOperation(value = "Get template list", notes = "Get template list")
	@RequestMapping(value = "/FetchTemplatesList",method = RequestMethod.GET)
	public GenericPrintTemplateBean fetchTemplatesList(@RequestParam(value="styleId", defaultValue="-1") Integer styleId) throws Exception{
		logger.debug("Begin of request to get template list.");
		
		List<LeafLibrary> templateList=genericPrintService.getTemplatesList();
		List<LeafLibrary> styleTemplateList=genericPrintService.getStyleTemplatesList(styleId);
		
		GenericPrintTemplateBean bean=new GenericPrintTemplateBean();
		bean.setTemplateList(templateList);
		bean.setStyleTemplateList(styleTemplateList);
		
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded templates list for configuration",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded templates list for configuration");
		logger.debug("End of request to get template list.");
		return bean;
		
	}
	
	/**
	 * Request to save style for template
	 * 
	 */
	@ApiOperation(value = "Update style for template", notes = "Update style for template")
	@RequestMapping(value = "/UpdateTemplatesList",method = RequestMethod.POST)
	public void updateTemplatesList(@RequestParam(value="styleId") Integer styleId,
			@RequestParam(value="value") String value) throws Exception{
		
		logger.debug("Begin of request to Update style for template.");
		
		List<LeafLibrary> leafLibraryStyle = genericPrintService.getLeafLibraryStyle(styleId);
		for(int i=0; i<leafLibraryStyle.size(); i++){
			LeafLibrary leafLibrary = leafLibraryStyle.get(i);
			leafLibrary.setLeafLibraryPrintStyleId(null);
			genericPrintService.saveLeafLibrary(leafLibrary);
		}
		String[] valueArr=value.split("~");
		for(int i=0; i<valueArr.length; i++){
			int templateId=-1;
			try{
				templateId= Integer.parseInt(valueArr[i]);
			}catch(Exception e){
				templateId=-1;
			}
			
			LeafLibrary leafLibrary=genericPrintService.getLeafLibrary(templateId);			
			leafLibrary.setLeafLibraryPrintStyleId(styleId);
			genericPrintService.saveLeafLibrary(leafLibrary);
		}		
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.CREATED,1,AuditLogConstants.SUCCESS,"Successfully saved style for template",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully saved style for template");
		logger.debug("End of request to Update style for template.");
	}
}

