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

import com.glenwood.glaceemr.server.application.models.LetterHeaderEmp;
import com.glenwood.glaceemr.server.application.models.LetterHeaderPos;
import com.glenwood.glaceemr.server.application.models.print.GenericLetterHeader;
import com.glenwood.glaceemr.server.application.models.print.LetterHeaderContent;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.chart.print.genericheader.LetterHeaderEmployeeBean;
import com.glenwood.glaceemr.server.application.services.chart.print.genericheader.LetterHeaderPosBean;
import com.glenwood.glaceemr.server.application.services.chart.print.genericheader.LetterHeaderService;
import com.glenwood.glaceemr.server.application.services.employee.EmployeeDataBean;
import com.glenwood.glaceemr.server.application.services.pos.PosDataBean;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


@Api(value = "LetterHeaderConfiguration", description = "Letter Header", consumes="application/json")
@RestController
@RequestMapping(value="/LetterHeader.Action")
public class LetterHeaderController {
	
	@Autowired
	LetterHeaderService letterHeaderService;
	
	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	HttpServletRequest request;
	
	private Logger logger = Logger.getLogger(LetterHeaderController.class);
	
	/**
	 * Request to get the list of all letter headers for configuration
	 * 
	 */
	@ApiOperation(value = "Get the list of all letter headers", notes = "Get the list of all letter headers")
	@RequestMapping(value = "/FetchLetterHeader",method = RequestMethod.GET)
	public List<GenericLetterHeader> fetchLetterHeaderList() throws Exception{
		logger.debug("Begin of request to get the list of all letter headers.");
		List<GenericLetterHeader> letterHeaderList = letterHeaderService.getLetterHeaderList();
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded header list for configuration",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded header list for configuration");
		logger.debug("End of request to get the list of all letter headers.");
		return letterHeaderList;
		
	}
	
	/**
	 * Request to get letter header content details based on header id for configuration
	 * 
	 */
	@ApiOperation(value = "Get letter header content details based on header id", notes = "Get letter header content details based on header id")
	@RequestMapping(value = "/FetchLetterHeaderContent",method = RequestMethod.GET)
	public List<LetterHeaderContent> fetchPatientHeaderDetailsList(@RequestParam(value="headerId") Integer headerId) throws Exception{
		logger.debug("Begin of request to get letter header content details based on header id.");
		List<LetterHeaderContent> letterHeaderContent = letterHeaderService.getLetterHeaderContentList(headerId);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.VIEWED,1,AuditLogConstants.SUCCESS,"Successfully loaded letter headers details based on header id",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully loaded letter headers details based on header id");
		logger.debug("End of request to get letter header content details based on header id.");
		return letterHeaderContent;
		
	}
	
	/**
	 * Request to save Letter header
	 * 
	 */
	@ApiOperation(value = "Save Letter header", notes = "Save Letter header")
	@RequestMapping(value = "/SaveLetterHeader",method = RequestMethod.POST)
	public GenericLetterHeader saveLetterHeader(@RequestParam(value="headerName") String headerName,@RequestParam(value="headerType") Integer headerType,
			@RequestParam(value="leftFormat") Integer leftFormat,@RequestParam(value="leftFormatAddress") Integer leftFormatAddress,
			@RequestParam(value="addressFormat") Integer addressFormat,@RequestParam(value="isDefault") Boolean isDefault) throws Exception{
		logger.debug("Begin of request to Save Letter header.");
		
		GenericLetterHeader newLetterHeader=new GenericLetterHeader();
		newLetterHeader.setGenericLetterHeaderName(headerName);
		newLetterHeader.setGenericLetterHeaderType(headerType);
		newLetterHeader.setGenericLetterHeaderAddress(addressFormat);
		newLetterHeader.setGenericLetterHeaderLeft(leftFormat);
		newLetterHeader.setGenericLetterHeaderLeftAddress(leftFormatAddress);
		newLetterHeader.setGenericLetterHeaderIsDefault(isDefault);
		newLetterHeader.setGenericLetterHeaderIsActive(true);
		GenericLetterHeader letterHeader = letterHeaderService.saveLetterHeader(newLetterHeader);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.CREATED,1,AuditLogConstants.SUCCESS,"Successfully saved Letter header",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully saved Letter header");
		logger.debug("End of request to Save Letter header.");
		return letterHeader;
		
	}
	
	/**
	 * Request to save Letter header
	 * 
	 */
	@ApiOperation(value = "Update Letter header", notes = "Update Letter header")
	@RequestMapping(value = "/UpdateLetterHeader",method = RequestMethod.POST)
	public GenericLetterHeader updateLetterHeader(@RequestParam(value="headerName") String headerName,@RequestParam(value="headerType") Integer headerType,
			@RequestParam(value="leftFormat") Integer leftFormat,@RequestParam(value="leftFormatAddress") Integer leftFormatAddress,
			@RequestParam(value="addressFormat") Integer addressFormat,@RequestParam(value="headerId") Integer headerId,
			@RequestParam(value="isDefault") Boolean isDefault,@RequestParam(value="isActive") Boolean isActive) throws Exception{
		logger.debug("Begin of request to Save Letter header.");
		
		GenericLetterHeader newLetterHeader=new GenericLetterHeader();
		newLetterHeader.setGenericLetterHeaderId(headerId);
		newLetterHeader.setGenericLetterHeaderName(headerName);
		newLetterHeader.setGenericLetterHeaderType(headerType);
		newLetterHeader.setGenericLetterHeaderAddress(addressFormat);
		newLetterHeader.setGenericLetterHeaderLeft(leftFormat);
		newLetterHeader.setGenericLetterHeaderLeftAddress(leftFormatAddress);
		newLetterHeader.setGenericLetterHeaderIsDefault(isDefault);
		newLetterHeader.setGenericLetterHeaderIsActive(isActive);
		GenericLetterHeader letterHeader = letterHeaderService.saveLetterHeader(newLetterHeader);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.CREATED,1,AuditLogConstants.SUCCESS,"Successfully saved Letter header",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully saved Letter header");
		logger.debug("End of request to Save Letter header.");
		return letterHeader;
		
	}
	
	/**
	 * Request to delete letter header details
	 * 
	 */
	@ApiOperation(value = "Delete letter header details", notes = "Delete letter header details")
	@RequestMapping(value = "/deleteLetterHeaderContent",method = RequestMethod.POST)
	public void deleteLetterHeaderDetails(@RequestParam(value="headerId") Integer headerId) throws Exception{
		logger.debug("Begin of request to delete letter header details.");
		List<LetterHeaderContent> letterHeaderContent = letterHeaderService.getLetterHeaderContentList(headerId);
		if(letterHeaderContent!=null && !letterHeaderContent.isEmpty()){
			letterHeaderService.deleteLetterHeaderContent(letterHeaderContent);
		}
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.DELETED,1,AuditLogConstants.SUCCESS,"Successfully deleted letter header details",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully deleted letter header details");
		logger.debug("End of request to delete letter header details.");
		
	}
	
	/**
	 * Request to save letter header details
	 * 
	 */
	@ApiOperation(value = "Save letter header details", notes = "Save letter header details")
	@RequestMapping(value = "/saveLetterHeaderContent",method = RequestMethod.POST)
	public void saveLetterHeaderDetails(@RequestParam(value="headerId") Integer headerId,@RequestParam(value="variantId") Integer variantId,
			@RequestParam(value="flagId") Integer flagId,@RequestParam(value="customText") String customText,@RequestParam(value="style") String style) throws Exception{
		logger.debug("Begin of request to save letter header details.");
		LetterHeaderContent newLetterHeaderContent= new LetterHeaderContent();
		newLetterHeaderContent.setLetterHeaderContentStyleMapId(headerId);
		newLetterHeaderContent.setLetterHeaderContentVariant(variantId);
		newLetterHeaderContent.setLetterHeaderContentFlag(flagId);
		newLetterHeaderContent.setLetterHeaderContentCustom(customText);
		newLetterHeaderContent.setLetterHeaderContentStyle(style);
		letterHeaderService.saveLetterHeaderContent(newLetterHeaderContent);
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.PrintingAndReporting,AuditLogConstants.CREATED,1,AuditLogConstants.SUCCESS,"Successfully saved letter header details",-1,"127.0.0.1",request.getRemoteAddr(),-1,-1,-1,AuditLogConstants.PrintingAndReporting,request,"Successfully saved letter header details");
		logger.debug("End of request to save letter header details.");
		
	}
	
	/**
	 * To get employee list
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "Get list of employee", notes = "Get list of employee")
	@RequestMapping(value = "/FetchEmployeeList", method = RequestMethod.GET)
	@ResponseBody
	public List<EmployeeDataBean> getEmployeeList() throws Exception {
	
		logger.debug("Begin of request to get the list of employees details.");
		List<EmployeeDataBean> bean = letterHeaderService.getEmployeeDetails();
		logger.debug("End of request to get the list of employees details.");
		return bean;

	}
	/**
	 * To get POS list
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "Get list of POS", notes = "Get list of POS")
	@RequestMapping(value = "/FetchPOSList", method = RequestMethod.GET)
	@ResponseBody
	public List<PosDataBean> getPOSList() throws Exception {
	
		logger.debug("Begin of request to get the list of POS.");
		List<PosDataBean> bean = letterHeaderService.getPOSDetails();
		logger.debug("End of request to get the list of POS.");
		return bean;

	}
	
	@ApiOperation(value = "Fetch letter header pos details", notes = "fetch letter header pos details")
	@RequestMapping(value = "/FetchLetterHeaderPOSList",method = RequestMethod.GET)
	@ResponseBody
	public LetterHeaderPosBean fetchLetterHeaderPOSList(@RequestParam(value="headerId") Integer headerId,
			@RequestParam(value="variantId") Integer variantId) throws Exception{
		logger.debug("Begin of request to fetch letter header pos details.");
		
		List<PosDataBean> posList = letterHeaderService.getPOSDetails();
		List<LetterHeaderPos> headerPosList = letterHeaderService.fetchLetterHeaderPOSList(headerId,variantId);
		
		LetterHeaderPosBean bean = new LetterHeaderPosBean();
		bean.setPosList(posList);
		bean.setSavedPosList(headerPosList);
		
		logger.debug("End of request to fetch letter header pos details.");
		return bean;
	}
	
	@ApiOperation(value = "Fetch letter header employee details", notes = "Fetch letter header employee details")
	@RequestMapping(value = "/FetchLetterHeaderEmpList",method = RequestMethod.GET)
	@ResponseBody
	public LetterHeaderEmployeeBean fetchLetterHeaderEmpList(@RequestParam(value="headerId") Integer headerId,
			@RequestParam(value="variantId") Integer variantId) throws Exception{
		logger.debug("Begin of request to fetch letter header employee details.");
		
		List<EmployeeDataBean> empList = letterHeaderService.getEmployeeDetails();
		List<LetterHeaderEmp> headerEmpList = letterHeaderService.getLetterHeaderEmpList(headerId,variantId);
		
		LetterHeaderEmployeeBean bean = new LetterHeaderEmployeeBean();
		bean.setEmpList(empList);
		bean.setSavedEmpList(headerEmpList);
		
		logger.debug("End of request to fetch letter header employee details.");
		return bean;
	}
	
	@ApiOperation(value = "save letter header pos details", notes = "save letter header pos details")
	@RequestMapping(value = "/SaveLetterHeaderPOSList",method = RequestMethod.POST)
	public String saveLetterHeaderPOSList(@RequestParam(value="orderValue", defaultValue="") String orderValue,
			@RequestParam(value="value", defaultValue="") String value,
			@RequestParam(value="variantId", defaultValue="-1") Integer variantId,
			@RequestParam(value="headerId", defaultValue="-1") Integer headerId) throws Exception{
		logger.debug("Begin of request to fetch letter header pos details.");
		String[] valueArr = value.split("~");
		String[] orderArr = orderValue.split("~");
		List<LetterHeaderPos> letterHeaderContent = letterHeaderService.getLetterHeaderPOSList(headerId, variantId);
		if(letterHeaderContent!=null && !letterHeaderContent.isEmpty()){
			letterHeaderService.deleteLetterHeaderPos(letterHeaderContent);
		}
		for(int i=0; i<valueArr.length; i++){
			LetterHeaderPos letterHeaderPos = new LetterHeaderPos();
			letterHeaderPos.setLetterHeaderPosMapId(headerId);
			letterHeaderPos.setLetterHeaderPosPosid(Integer.parseInt(valueArr[i]));
			letterHeaderPos.setLetterHeaderPosOrder(Integer.parseInt(orderArr[i]));
			letterHeaderPos.setLetterHeaderPosVariant(variantId);
			letterHeaderService.saveLetterHeaderPOS(letterHeaderPos);
		}
		
		logger.debug("End of request to save letter header pos details.");
		return "Success";
	}
	
	@ApiOperation(value = "Save letter header employee details", notes = "Save letter header employee details")
	@RequestMapping(value = "/SaveLetterHeaderEmpList",method = RequestMethod.POST)
	public String saveLetterHeaderEmpList(@RequestParam(value="orderValue", defaultValue="") String orderValue,
			@RequestParam(value="value", defaultValue="") String value,
			@RequestParam(value="headerId", defaultValue="") Integer headerId,
			@RequestParam(value="variantId", defaultValue="-1") Integer variantId) throws Exception{
		logger.debug("Begin of request to fetch letter header employee details.");
		String[] valueArr = value.split("~");
		String[] orderArr = orderValue.split("~");
		List<LetterHeaderEmp> letterHeaderContent = letterHeaderService.getLetterHeaderEmpList(headerId,variantId);
		if(letterHeaderContent!=null && !letterHeaderContent.isEmpty()){
			letterHeaderService.deleteLetterHeaderEmp(letterHeaderContent);
		}
		for(int i=0; i<valueArr.length; i++){
			
			LetterHeaderEmp letterHeaderEmp = new LetterHeaderEmp();
			letterHeaderEmp.setLetterHeaderEmpMapId(headerId);
			letterHeaderEmp.setLetterHeaderEmpEmpid(Integer.parseInt(valueArr[i]));
			letterHeaderEmp.setLetterHeaderEmpOrder(Integer.parseInt(orderArr[i]));
			letterHeaderEmp.setLetterHeaderEmpVariant(variantId);
			letterHeaderService.saveLetterHeaderEmp(letterHeaderEmp);
		}
		
		return "Success";
	}	
}