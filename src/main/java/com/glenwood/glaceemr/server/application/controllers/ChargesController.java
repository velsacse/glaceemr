package com.glenwood.glaceemr.server.application.controllers;
/**
 * Charges page related springs API's
 * @author Tarun
 *
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.Cpt;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.H611;
import com.glenwood.glaceemr.server.application.models.InitialSettings;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.Quickcpt;
import com.glenwood.glaceemr.server.application.models.SaveServicesBean;
import com.glenwood.glaceemr.server.application.models.ServiceDetail;
import com.glenwood.glaceemr.server.application.models.SubmitStatus;
import com.glenwood.glaceemr.server.application.models.UpdateServiceBean;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.services.chart.charges.ChargesPageBasicDetailsBean;
import com.glenwood.glaceemr.server.application.services.chart.charges.ChargesServices;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value="Charges Page")
@RestController
@Transactional
@RequestMapping(value = "/user/Charges")
public class ChargesController {
	
	@Autowired
	ChargesServices chargesServices;
	
	@Autowired
	AuditTrailService auditTrailService;
	
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	HttpServletRequest request;

	@Autowired
	ObjectMapper objectmapper;
	
	private Logger logger = Logger.getLogger(ChargesController.class);
	
	/**
	 * Basic information to add new service
	 * @param patientId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="List of POS,Doctor,Insurance details",notes="Get all the basic details related to charges page")
	@RequestMapping(value="/getBasicDetails",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getBasicDetails(@RequestParam(value="patientId", required=true)Integer patientId) throws Exception{
		ChargesPageBasicDetailsBean posDataValue=chargesServices.findAllBasicDetails(patientId);
		EMRResponseBean posData=new EMRResponseBean();
		posData.setData(posDataValue);
		return posData;
	}
	
	/**
	 * Frequently used CPT codes list
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="To get all the quick cpt codes",notes="Get all the quick cpt codes with detailed cpt related information")
	@RequestMapping(value="/getQuickCPTCodes",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getQuickCptCodes() throws Exception{
		List<Quickcpt> cptCodes=chargesServices.findAllQuickCptCodes();
		EMRResponseBean cptCode=new EMRResponseBean();
		cptCode.setData(cptCodes);
		return cptCode;
	}
	
	/**
	 * Request to get all the services of a patient
	 * @param patientId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="To get all services",notes="Request to get all the service related to particular patient")
	@RequestMapping(value="/getServicesList",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getServicesList(@RequestParam(value="patientId", required=true)Integer patientId) throws Exception{
		List<ServiceDetail> servicesList=chargesServices.getAllServices(patientId);
		EMRResponseBean serviceList=new EMRResponseBean();
		serviceList.setData(servicesList);
		return serviceList;
	}
	
	/**
	 * Get all information of single service
	 * @param patientId
	 * @param serviceId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="Request to get service details",notes="Request to get particuler service details to a particular partient based on particuler service Id")
	@RequestMapping(value="/getServiceDetails",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getServiceDetails(@RequestParam(value="patientId", required=true)Integer patientId,
											@RequestParam(value="serviceId", required=true)Integer serviceId) throws Exception{
		ServiceDetail serviceDetails=chargesServices.getServiceDetails(patientId,serviceId);	
//		ServiceDetail returnValue=validateReturnEntity(serviceDetails);
		EMRResponseBean serviceDetail=new EMRResponseBean();
		serviceDetail.setData(serviceDetails);
		return serviceDetail;
	}
	
	/**
	 * Deletion of service from patient services list
	 * @param serviceIDs
	 * @param patientId
	 * @param loginId
	 * @param loginName
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="Request to delete services",notes="Request to delete all services which are mentoined in the string")
	@RequestMapping(value="/deleteServices",method=RequestMethod.DELETE)
	@ResponseBody
	public EMRResponseBean deleteServices(@RequestParam(value="deleteServiceIDs", required=true)String serviceIDs,
									@RequestParam(value="patientId", required=true)Integer patientId,
									@RequestParam(value="loginId", required=true)String loginId,
									@RequestParam(value="loginName", required=true)String loginName) throws Exception{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String modifiedDate=formatter.format(new Date());
		Boolean confirmation=chargesServices.deleteMentoinedServices(serviceIDs,patientId,modifiedDate,loginId,loginName);									
		EMRResponseBean confirmations=new EMRResponseBean();
		confirmations.setData(confirmation);
		return confirmations;
	}
	/**
	 * Previous visit Dx codes 
	 * @param patientId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="Request to get pervious Dx codes",notes="Request to get previous visist Dx codes list")
	@RequestMapping(value="/previousVisitDxCodes",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getPreviousVisitDxCodes(@RequestParam(value="patientId", required=true)Integer patientId) throws Exception{
		ServiceDetail dxDetails=chargesServices.getPreviousVisitDxCodes(patientId);								
		EMRResponseBean dxDetail=new EMRResponseBean();
		dxDetail.setData(dxDetails);
		return dxDetail;
	}
	
	/**
	 * EMR Diagnosis codes for a patient on particular date
	 * @param patientId
	 * @param dos
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="Request to get EMR diagnosis",notes="Request to get EMR diagnosis list")
	@RequestMapping(value="/getEMRDxCodes",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getEMRDxCodes(@RequestParam(value="patientId", required=true)Integer patientId,
										@RequestParam(value="dos", required=true)String dos) throws Exception{
		List<H611> dxDetails=chargesServices.getEMRDxCodes(patientId,dos);								
		EMRResponseBean dxDetail=new EMRResponseBean();
		dxDetail.setData(dxDetails);
		return dxDetail;
	}
	
	/**
	 * Color coding information 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="Request to get submit status",notes="Request to get submit status type information")
	@RequestMapping(value="/getSubmitStatusInfo",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getSubmitStatusInfo() throws Exception{
		List<SubmitStatus> statusInfo=chargesServices.getSubmitStausInfo();					
		EMRResponseBean statusInformation=new EMRResponseBean();
		statusInformation.setData(statusInfo);
		return statusInformation;
	}
	/**
	 * Save new service 
	 * @param saveServicesBean
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="Request to save services",notes="Request to save services")
	@RequestMapping(value="/saveServices",method=RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean saveServices(@RequestBody List<SaveServicesBean> saveServicesBean) throws Exception{
		Boolean confirmation=insertNewService(saveServicesBean);
		EMRResponseBean confirmations=new EMRResponseBean();
		confirmations.setData(confirmation);
		return confirmations;
	}
	
	/**
	 * Update service with updated values
	 * @param updateService
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="Request to update service",notes="Request to update service")
	@RequestMapping(value="/updateService",method=RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public EMRResponseBean updateService(@RequestBody UpdateServiceBean updateService) throws Exception{
		Boolean confirmation=chargesServices.updateServiceDetails(updateService);
		EMRResponseBean confirmations=new EMRResponseBean();
		confirmations.setData(confirmation);
		return confirmations;
	}
	
	public EMRResponseBean validateReturnEntity(ServiceDetail totalServiceDetails) {
		ServiceDetail finalResult=new ServiceDetail();
		EmployeeProfile empDetails=new EmployeeProfile();
		Cpt cptDetails=new Cpt();
		PosTable posDetails=new PosTable();
		PatientInsDetail insDetails=new PatientInsDetail();
		finalResult=totalServiceDetails;
		finalResult.setCpt(Optional.fromNullable(totalServiceDetails.getCpt()).or(cptDetails));
		finalResult.setBdoctors(Optional.fromNullable(totalServiceDetails.getBdoctors()).or(empDetails));
		finalResult.setSdoctors(Optional.fromNullable(totalServiceDetails.getSdoctors()).or(empDetails));
		finalResult.setPosTable(Optional.fromNullable(totalServiceDetails.getPosTable()).or(posDetails));
		finalResult.setPatientInsDetail(Optional.fromNullable(totalServiceDetails.getPatientInsDetail()).or(insDetails));
		finalResult.setSecInsDetail(totalServiceDetails.getSecInsDetail()==null?(new PatientInsDetail()) : totalServiceDetails.getSecInsDetail());
		EMRResponseBean finalResults=new EMRResponseBean();
		finalResults.setData(finalResult);
		return finalResults;
	}
	public Boolean insertNewService(List<SaveServicesBean> saveServicesBean) {
		Boolean confirmation=true;
		try {
			List<String> cptCodes=new ArrayList<String>();
			for(int i=0;i<saveServicesBean.size();i++){
				cptCodes.add(saveServicesBean.get(i).getCptCode());
			}
			List<Cpt> cptCodeDetails=chargesServices.getAllCptCodesDetails(cptCodes);
			PatientRegistration forGuarantor=chargesServices.getGuarantorDetail(saveServicesBean.get(0).getPatientid());
			InitialSettings initialBillingReason=chargesServices.getDefaultBillingReason();
			for(int i=0;i<saveServicesBean.size();i++){
				confirmation=chargesServices.insertEnityCreation(saveServicesBean.get(i),cptCodeDetails.get(i),forGuarantor,i,Optional.fromNullable(Strings.emptyToNull(initialBillingReason.getInitialSettingsOptionValue())).or("-1"));
			}
		} catch (Exception e) {
			confirmation=false;
			e.printStackTrace();
		}
		return confirmation;
	}
}