package com.glenwood.glaceemr.server.application.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.TesttableAdrs;
import com.glenwood.glaceemr.server.application.models.Testtableenctr;
import com.glenwood.glaceemr.server.application.models.TesttablePtn;
import com.glenwood.glaceemr.server.application.services.patient.PatientService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;



@RestController
@Transactional
@RequestMapping(value = "/user/PatientController.Action")
public class TestPatientController {


	@Autowired
	PatientService patientInfoService;
	
	@Autowired
	EMRResponseBean responseBean;
	
	@Autowired
	ObjectMapper objectmapper;
	private Logger logger = Logger.getLogger(TestPatientController.class);
	
	/**
	 * @param lastName 		: required patients lastName 
	 * @return List of Patients  satisfied by the condition (lastName should match)
	 * @throws Exception
	 */
	@RequestMapping(value = "/ByLastName", method = RequestMethod.GET)
    @ResponseBody
	public EMRResponseBean getPatientsByLastname(@RequestParam(value="lastName", required=false, defaultValue="") String lastName) throws Exception{
		
		logger.debug("in patient controller log ");
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(patientInfoService.findPatientByLastName(lastName));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Data retrieval failure");
			return responseBean;
		}
				
	}
	
	
	/**
	 * @param lastName 		: required patients lastName 
	 * @param dob	   		: required patients date of birth	 
	 * @return List of Patients  satisfied by the condition (lastName and date of birth should match)
	 * @throws Exception
	 */
	@RequestMapping(value = "/ByLastNameAndDob",method = RequestMethod.GET)
	public EMRResponseBean getPatientsBylastNameAndDob(@RequestParam(value="lastName", required=false, defaultValue="") String lastName,@RequestParam(value="dob", required=false, defaultValue="") String dob) throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(patientInfoService.findPatientByLastNameAndDob(lastName, dob));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Data retrieval failure");
			return responseBean;
		}
		
	}
	
	
	/**
	 * @param lastName 		: required patients lastName 
	 * @return List of Patients  satisfied by the condition (lastName should match)
	 * @throws Exception
	 */
	@RequestMapping(value = "/ByInsuranceName",method = RequestMethod.GET)
	public EMRResponseBean getPatientsByInsuranceName(@RequestParam(value="insuranceName", required=false, defaultValue="") String insuranceName) throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(patientInfoService.findPatientByinsuranceName(insuranceName));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Data retrieval failure");
			return responseBean;
		}
		
	}

	/**
	 * @param lastName  	: required patients lastName
	 * @param insuranceName : required patients insuranceName
	 * @return List of Patients  satisfied by the condition (lastName and insuranceName should match)
	 * @throws Exception
	 */
	@RequestMapping(value = "/ByLastNameHavingInsuranceByName",method = RequestMethod.GET)
	public EMRResponseBean getPatientsByLastNameHavingInsuranceByName(@RequestParam(value="lastName", required=false, defaultValue="") String lastName,@RequestParam(value="insuranceName", required=false, defaultValue="") String insuranceName) throws Exception{		
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(patientInfoService.findPatintByLastNameHavingInsuranceByName(lastName, insuranceName));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Data retrieval failure");
			return responseBean;
		}
		
	}

	@RequestMapping(value = "/AllEncountersByPatientId",method = RequestMethod.GET)
	public EMRResponseBean getAllEncountersbyPatientId(@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId) throws Exception{		
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(patientInfoService.findEncounterByPatientId(patientId));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Data retrieval failure");
			return responseBean;
		}
		
	}
	
	@RequestMapping(value = "/AllInsuranceByPatientId",method = RequestMethod.GET)
	public EMRResponseBean getAllInsurancebyPatientId(@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId) throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(patientInfoService.findInsuranceByPatientId(patientId));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Data retrieval failure");
			return responseBean;
		}
		
	}
	
	
	@RequestMapping(value = "/LatestEncountersbyPatientID",method = RequestMethod.GET)
	public EMRResponseBean  getLatestEncounterDateAndPatientNameByPatientId(@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId) throws Exception{		
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			responseBean.setData(patientInfoService.findLatestEncounterDateAndPatientNameByPatientId(patientId));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Data retrieval failure");
			return responseBean;
		}
		
	}
	
	

	@RequestMapping(value = "/EncountersbyPatientIDs",method = RequestMethod.GET)
	public EMRResponseBean  getEncountersbyPatientID(@RequestParam(value="patientId", required=false, defaultValue="") String patientId) throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			JSONArray array=new JSONArray(patientId);
			List<Integer> patientIdList = new ArrayList<Integer>();
			for(int i=0;i<array.length();i++)
				patientIdList.add((Integer)array.get(i));
			responseBean.setData(patientInfoService.findEncountersbyPatientID(patientIdList));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Data retrieval failure");
			return responseBean;
		}
		
	}
	
	@RequestMapping(value = "/LatestEncountersbyPatientIDs",method = RequestMethod.GET)
	public EMRResponseBean  getLatestEncountersbyPatientID(@RequestParam(value="patientId", required=false, defaultValue="") String patientId) throws Exception{
		
		responseBean.setCanUserAccess(true);
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		
		try {
			responseBean.setSuccess(true);
			JSONArray array=new JSONArray(patientId);
			List<Integer> patientIdList = new ArrayList<Integer>();
			for(int i=0;i<array.length();i++)
				patientIdList.add((Integer)array.get(i));
			responseBean.setData(patientInfoService.findLatestEncountersbyPatientIDs(patientIdList));
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
			responseBean.setData("Data retrieval failure");
			return responseBean;
		}
		
	}

	
	 
		@RequestMapping(value = "/ByPatientId",method = RequestMethod.GET)
		public EMRResponseBean getPatientsByPatientId(@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId) throws Exception{
			
			responseBean.setCanUserAccess(true);
			responseBean.setIsAuthorizationPresent(true);
			responseBean.setLogin(true);
			
			try {
				responseBean.setSuccess(true);
				responseBean.setData(patientInfoService.findPatientByPatientId(patientId));
				return responseBean;
			} catch (Exception e) {
				e.printStackTrace();
				responseBean.setSuccess(false);
				responseBean.setData("Data retrieval failure");
				return responseBean;
			}
			
		}
		
		 
		 @RequestMapping(value="/updateByPatient" ,method = RequestMethod.POST)
		 @ResponseBody
		    public EMRResponseBean updateByPatient(@RequestParam(value="patient")String patient) {
			
			responseBean.setCanUserAccess(true);
			responseBean.setIsAuthorizationPresent(true);
			responseBean.setLogin(true);
			
			try {
				responseBean.setSuccess(true);
				TesttablePtn updatepatient;
				System.out.println("In update By patientId>>"+patient);
				updatepatient = objectmapper.readValue(patient, TesttablePtn.class);
				responseBean.setData(patientInfoService.updateByPatient(updatepatient));
				return responseBean;
			} catch (Exception e) {
				e.printStackTrace();
				responseBean.setSuccess(false);
				responseBean.setData("Data retrieval failure");
				return responseBean;
			}
				
		    }
		 

		 @RequestMapping(value="/updateByAddress" ,method = RequestMethod.GET)
		 @ResponseBody
		    public EMRResponseBean updateByaddress(@RequestParam(value="address", required=false, defaultValue="") String address) throws Exception{
			
			 	System.out.println("In update By address>>"+address);
			
			 	responseBean.setCanUserAccess(true);
				responseBean.setIsAuthorizationPresent(true);
				responseBean.setLogin(true);
				
				try {
					responseBean.setSuccess(true);
					responseBean.setData(patientInfoService.updateByAddress(address));
					return responseBean;
				} catch (Exception e) {
					e.printStackTrace();
					responseBean.setSuccess(false);
					responseBean.setData("Data retrieval failure");
					return responseBean;
				}
			 
		    }
		 
		 
			/* for deleting a record in different ways */
			 @RequestMapping(value="/deleteById" ,method = RequestMethod.GET)
			 @ResponseBody
			    public void deleteById(@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId ) {
				 
				 System.out.println("In delete By patientId>>"+patientId);
				 
				 patientInfoService.deleteById(patientId);
				 
				 System.out.println(" After  Executing  delete By patientId>>"+patientId);
					
			    }
			 
			 
			 /* for deleting a record in different ways */
			 @RequestMapping(value="/deleteByLastName" ,method = RequestMethod.GET)
			 @ResponseBody
			    public void deleteByLastname(@RequestParam(value="lastName", required=false, defaultValue="") String lastName,@RequestParam(value="insuranceName", required=false, defaultValue="") String insuranceName ) throws Exception{
				 
				 System.out.println("In delete By lastName>>"+lastName);
				
				 patientInfoService.deleteByLastname(lastName);
					
				 System.out.println(" After  Executing  delete By lastName>>"+lastName);
			    }
	
			 
			 @RequestMapping(value="/deleteAllAddress" ,method = RequestMethod.GET)
			 @ResponseBody
			    public void deleteAllAddress( ) throws Exception{
				 
				 System.out.println("In delete By All Addresses>>");
				
				 patientInfoService.deleteAllAddress();
					
				 System.out.println(" After  Executing  delete By all Address>>");
			    }
			 
			 
			 @RequestMapping(value="/deleteByPatient" ,method = RequestMethod.POST)
			 @ResponseBody
			    public EMRResponseBean deleteByPatient(@RequestParam(value="patient")String patient) {
				
				responseBean.setCanUserAccess(true);
				responseBean.setIsAuthorizationPresent(true);
				responseBean.setLogin(true);
				
				try {
					responseBean.setSuccess(true);
					TesttablePtn updatepatient;
					System.out.println("In delete By patient>>"+patient);
					updatepatient = objectmapper.readValue(patient, TesttablePtn.class);
					patientInfoService.deleteByPatient(updatepatient);
					System.out.println(" Afte Executing delete By patient>>"+patient);
					responseBean.setData("success");
					return responseBean;
				} catch (Exception e) {
					e.printStackTrace();
					responseBean.setSuccess(false);
					responseBean.setData("failure");
					return responseBean;
				}
					
			    }
			 
			 
			 @RequestMapping(value="/insertByPatient" ,method = RequestMethod.POST)
			 @ResponseBody
			    public EMRResponseBean insertByPatient(@RequestParam(value="patient")String patient) {
				
				responseBean.setCanUserAccess(true);
				responseBean.setIsAuthorizationPresent(true);
				responseBean.setLogin(true);
				
				try {
					responseBean.setSuccess(true);
					TesttablePtn updatepatient;
					System.out.println("In insert By patient>>"+patient);
					updatepatient = objectmapper.readValue(patient, TesttablePtn.class);
					responseBean.setData(patientInfoService.insertByPatient(updatepatient));
					return responseBean;
				} catch (Exception e) {
					e.printStackTrace();
					responseBean.setSuccess(false);
					responseBean.setData("Data retrieval failure");
					return responseBean;
				}
			    }
			 
	
}
