package com.glenwood.glaceemr.server.application.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Patient;
import com.glenwood.glaceemr.server.application.services.patient.PatientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationTestContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	DbUnitTestExecutionListener.class })
//@DbUnitConfiguration(dataSetLoader = ColumnSensingReplacementDataSetLoader.class)
@DatabaseSetup("patientData.xml")
@Transactional
public class PatientServiceTest {

	@Autowired
	PatientService patientInfoService;

	@Test
	public void testfindPatientByLastName()
	{
		List<Patient> patients= (List<Patient>) patientInfoService.findPatientByLastName("Smith");
		assertEquals(4, patients.size());
		assertEquals(2, patients.get(1).getEncounterTable().size());
		assertEquals("Theodre", patients.get(0).getPatientFName());
		
	}
	
	
	@Test
	public void testfindPatientByPatientId()
	{
		List<Patient> patients= (List<Patient>) patientInfoService.findPatientByPatientId(1);
		assertEquals(1, patients.size());
	}
	
	@Test
	public void testfindPatientByLastNameAndDob()
	{
		List<Patient> patients=(List<Patient>) patientInfoService.findPatientByLastNameAndDob("Smith", "2006-09-03");
		assertEquals(1, patients.size());
	}
	
	
	@Test
	public void  testfindPatintByLastNameHavingInsuranceByName()
	{
		
		List<Patient> patients=(List<Patient>) patientInfoService.findPatintByLastNameHavingInsuranceByName("Smith", "AnthemBluecross");
		assertEquals(3, patients.size());
		
	}
	

	@Test
	public void testfindEncounterByPatientId() 
	{
		List<Encounter> encounters=(List<Encounter>) patientInfoService.findEncounterByPatientId(1);
		assertEquals(2, encounters.size());
		
	}
	
	

	@Test
	public void  testfindInsuranceByPatientId() 
	{
	
		List<Patient> patients=(List<Patient>) patientInfoService.findInsuranceByPatientId(1);
		assertEquals(1, patients.size());
		
	}
	
	
	@Test
	public void  testfindLatestEncounterDateAndPatientNameByPatientId()
	{
		List<Map<String, Object>> patients=patientInfoService.findLatestEncounterDateAndPatientNameByPatientId(1);
		assertEquals(1, patients.size());
	}
	
	
	@Test
	public void  testfindEncountersbyPatientID()
	{
		List<Integer> patientId=new ArrayList<Integer>();
		patientId.add(1);
		patientId.add(2);
		List<Encounter> encounters=(List<Encounter>) patientInfoService.findEncountersbyPatientID(patientId);
		assertEquals(4, encounters.size());
	}
	

	@Test
	public void testfindLatestEncountersbyPatientIDs()
	{
		
		List<Integer> patientId=new ArrayList<Integer>();
		patientId.add(1);
		patientId.add(2);
		List<Iterable<Object>> encounters=	patientInfoService.findLatestEncountersbyPatientIDs(patientId);
		assertEquals(2, encounters.size());
	}
	
}