package com.glenwood.glaceemr.server.application.services.patient;

import java.util.List;
import java.util.Map;

import com.glenwood.glaceemr.server.application.models.Address;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Insurance;
import com.glenwood.glaceemr.server.application.models.Patient;
import com.glenwood.glaceemr.server.application.models.PatientInsurance;

public interface PatientService {


	/**
	 * @param lastName		: required patients lastName
	 * @return List of Patients  satisfied by the condition (lastName should match)
	 */
	public Iterable<Patient> findPatientByLastName(String lastName);

	public Iterable<Patient> findPatientByinsuranceName(String insuranceName);
	
	/**
	 * @param lastname		: required patients lastName 
	 * @param Dob			: required patients date of birth	 
	 * @return List of Patients  satisfied by the condition (lastName and date of birth should match)
	 */
	public Iterable<Patient> findPatientByLastNameAndDob(String lastname,String Dob);

	/**
	 * @param lastname		: required patients lastName
	 * @param insuranceName	: required patients insuranceName
	 * @return List of Patients  satisfied by the condition (lastName and insuranceName should match)
	 */
	public Iterable<Patient> findPatintByLastNameHavingInsuranceByName(String lastname,String insuranceName);
	
	
	
	public Iterable<Encounter> findEncounterByPatientId(Integer PatientId) ;
	
	
	
	public Iterable<Patient> findInsuranceByPatientId(Integer PatientId) ;
	
	public List<Map<String, Object>> findLatestEncounterDateAndPatientNameByPatientId(Integer PatientId);
	
	public Iterable<Encounter> findEncountersbyPatientID(List<Integer> patientId);
	public List<Iterable<Object>>findLatestEncountersbyPatientIDs(List<Integer> PatientId);
	
	
	public Iterable<Patient> findPatientByPatientId(Integer patientId);
	
	public void deleteById(Integer patientId);
	
	public void deleteByLastname(String  lastName);
	
	public void deleteAllAddress();
	
	public void deleteByPatient(Patient patient);
	
	public Patient updateByPatient(Patient patient);
	
	public Patient insertByPatient(Patient patient);
	
	
	public Iterable<Address> updateByAddress(String address);
	
	public Patient updateByAddressId(Integer addressId);
	
	public void insertorupdaterecord();
	
	
	
	
}
