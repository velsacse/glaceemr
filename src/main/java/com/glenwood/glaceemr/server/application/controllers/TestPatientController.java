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



@RestController
@Transactional
@RequestMapping(value = "/user/PatientController.Action")
public class TestPatientController {


	@Autowired
	PatientService patientInfoService;
	
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
	public Iterable<TesttablePtn> getPatientsByLastname(@RequestParam(value="lastName", required=false, defaultValue="") String lastName) throws Exception{
		
		
		logger.debug("in patient controller log ");
		Iterable<TesttablePtn> patients=patientInfoService.findPatientByLastName(lastName);
		
		
		return patients;
	}
	
	
	/**
	 * @param lastName 		: required patients lastName 
	 * @param dob	   		: required patients date of birth	 
	 * @return List of Patients  satisfied by the condition (lastName and date of birth should match)
	 * @throws Exception
	 */
	@RequestMapping(value = "/ByLastNameAndDob",method = RequestMethod.GET)
	public Iterable<TesttablePtn> getPatientsBylastNameAndDob(@RequestParam(value="lastName", required=false, defaultValue="") String lastName,@RequestParam(value="dob", required=false, defaultValue="") String dob) throws Exception{
		return patientInfoService.findPatientByLastNameAndDob(lastName, dob);
	}
	
	
	/**
	 * @param lastName 		: required patients lastName 
	 * @return List of Patients  satisfied by the condition (lastName should match)
	 * @throws Exception
	 */
	@RequestMapping(value = "/ByInsuranceName",method = RequestMethod.GET)
	public Iterable<TesttablePtn> getPatientsByInsuranceName(@RequestParam(value="insuranceName", required=false, defaultValue="") String insuranceName) throws Exception{
		return patientInfoService.findPatientByinsuranceName(insuranceName);
	}

	/**
	 * @param lastName  	: required patients lastName
	 * @param insuranceName : required patients insuranceName
	 * @return List of Patients  satisfied by the condition (lastName and insuranceName should match)
	 * @throws Exception
	 */
	@RequestMapping(value = "/ByLastNameHavingInsuranceByName",method = RequestMethod.GET)
	public Iterable<TesttablePtn> getPatientsByLastNameHavingInsuranceByName(@RequestParam(value="lastName", required=false, defaultValue="") String lastName,@RequestParam(value="insuranceName", required=false, defaultValue="") String insuranceName) throws Exception{
		Iterable<TesttablePtn> patients=patientInfoService.findPatintByLastNameHavingInsuranceByName(lastName, insuranceName);
		return patients;
	}

	@RequestMapping(value = "/AllEncountersByPatientId",method = RequestMethod.GET)
	public Iterable<Testtableenctr> getAllEncountersbyPatientId(@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId) throws Exception{
		Iterable<Testtableenctr> encounter=patientInfoService.findEncounterByPatientId(patientId);
		return encounter;
	}
	
	@RequestMapping(value = "/AllInsuranceByPatientId",method = RequestMethod.GET)
	public Iterable<TesttablePtn> getAllInsurancebyPatientId(@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId) throws Exception{
		Iterable<TesttablePtn> insurance=patientInfoService.findInsuranceByPatientId(patientId);
		return insurance;
	}
	
	
	@RequestMapping(value = "/LatestEncountersbyPatientID",method = RequestMethod.GET)
	public List<Map<String, Object>>  getLatestEncounterDateAndPatientNameByPatientId(@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId) throws Exception{
		List<Map<String, Object>> encounter=patientInfoService.findLatestEncounterDateAndPatientNameByPatientId(patientId);
		return encounter;
	}
	
	

	@RequestMapping(value = "/EncountersbyPatientIDs",method = RequestMethod.GET)
	public Iterable<Testtableenctr>  getEncountersbyPatientID(@RequestParam(value="patientId", required=false, defaultValue="") String patientId) throws Exception{
		JSONArray array=new JSONArray(patientId);
		List<Integer> patientIdList = new ArrayList<Integer>();
		for(int i=0;i<array.length();i++)
			patientIdList.add((Integer)array.get(i));
		Iterable<Testtableenctr> encounter=patientInfoService.findEncountersbyPatientID(patientIdList);
		return encounter;
	}
	
	@RequestMapping(value = "/LatestEncountersbyPatientIDs",method = RequestMethod.GET)
	public List<Iterable<Object>>  getLatestEncountersbyPatientID(@RequestParam(value="patientId", required=false, defaultValue="") String patientId) throws Exception{
		JSONArray array=new JSONArray(patientId);
		List<Integer> patientIdList = new ArrayList<Integer>();
		for(int i=0;i<array.length();i++)
			patientIdList.add((Integer)array.get(i));
		List<Iterable<Object>>encounter=patientInfoService.findLatestEncountersbyPatientIDs(patientIdList);
		return encounter;
	}

	
	 
		@RequestMapping(value = "/ByPatientId",method = RequestMethod.GET)
		public Iterable<TesttablePtn> getPatientsByPatientId(@RequestParam(value="patientId", required=false, defaultValue="") Integer patientId) throws Exception{
			return patientInfoService.findPatientByPatientId(patientId);
		}
		
		 
		 @RequestMapping(value="/updateByPatient" ,method = RequestMethod.POST)
		 @ResponseBody
		    public TesttablePtn updateByPatient(@RequestParam(value="patient")String patient) {
			 TesttablePtn updatepatient;
			 System.out.println("In update By patientId>>"+patient);
			try {
				updatepatient = objectmapper.readValue(patient, TesttablePtn.class);

			 return patientInfoService.updateByPatient(updatepatient);
			 
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} 
				
		    }
		 

		 @RequestMapping(value="/updateByAddress" ,method = RequestMethod.GET)
		 @ResponseBody
		    public Iterable<TesttableAdrs> updateByaddress(@RequestParam(value="address", required=false, defaultValue="") String address) throws Exception{
			
			 System.out.println("In update By address>>"+address);

			 return patientInfoService.updateByAddress(address);
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
			    public String deleteByPatient(@RequestParam(value="patient")String patient) {
				 TesttablePtn updatepatient;
				 System.out.println("In delete By patient>>"+patient);
				try {
					updatepatient = objectmapper.readValue(patient, TesttablePtn.class);

				 patientInfoService.deleteByPatient(updatepatient);
				 
				 System.out.println(" Afte Executing delete By patient>>"+patient);
				 return "success";
				 
				} catch (Exception e) {
					e.printStackTrace();
					return "failure";
					
				} 
					
			    }
			 
			 
			 @RequestMapping(value="/insertByPatient" ,method = RequestMethod.POST)
			 @ResponseBody
			    public TesttablePtn insertByPatient(@RequestParam(value="patient")String patient) {
				 TesttablePtn updatepatient;
				 System.out.println("In insert By patient>>"+patient);
				try {
					updatepatient = objectmapper.readValue(patient, TesttablePtn.class);

				 return patientInfoService.insertByPatient(updatepatient);
				 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				} 
					
			    }
			 
	
}
