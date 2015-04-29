package com.glenwood.glaceemr.server.application.services.patient;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.models.Address;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Encounter_;
import com.glenwood.glaceemr.server.application.models.Patient;
import com.glenwood.glaceemr.server.application.models.Patient_;
import com.glenwood.glaceemr.server.application.repositories.AddressRepository;
import com.glenwood.glaceemr.server.application.repositories.EncounterRepository;
import com.glenwood.glaceemr.server.application.repositories.InsuranceMasterRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientInsuranceRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRepository;
import com.glenwood.glaceemr.server.application.specifications.AddressSpecfication;
import com.glenwood.glaceemr.server.application.specifications.EncounterSpecification;
import com.glenwood.glaceemr.server.application.specifications.PatientInsuranceSpecification;
import com.glenwood.glaceemr.server.application.specifications.PatientSpecification;
import com.google.common.collect.ImmutableSet;

@Service
@Transactional
public class PatientServiceImpl implements PatientService{

	@Autowired
	PatientRepository patientTableRepository; 

	@Autowired
	AddressRepository addressTableRepository;

	@Autowired
	EncounterRepository encounterTableRepository; 

	@Autowired
	EntityManagerFactory emf ;

	@Autowired
	InsuranceMasterRepository insuranceMasterRepository;

	@Autowired
	PatientInsuranceRepository patientInsuranceRepository; 

	@PersistenceContext
	EntityManager em;


	/* (non-Javadoc)
	 * @see com.glenwood.glaceemr.server.application.services.patient.PatientService#findPatientByLastName(java.lang.String)
	 * This method returns list of Patients  satisfied by the condition (lastName should match)
	 */
	@Override
	public Iterable<Patient> findPatientByLastName(String lastName) {

		Iterable<Patient> patients= patientTableRepository.findAll(PatientSpecification.PatientByLastName(lastName));
		
		return patients;
	}

	
	/* (non-Javadoc)
	 * @see com.glenwood.glaceemr.server.application.services.patient.PatientService#findPatientByLastName(java.lang.String)
	 * This method returns list of Patients  satisfied by the condition (lastName should match)
	 */
	@Override
	public Iterable<Patient> findPatientByPatientId(Integer patientId) {

		Iterable<Patient> patients= patientTableRepository.findAll(PatientSpecification.PatientByPatientId(patientId));
		return patients;
	}




	/* (non-Javadoc)
	 * @see com.glenwood.glaceemr.server.application.services.patient.PatientService#findPatientByLastNameAndDob(java.lang.String, java.lang.String)
	 * This method returns list of Patients  satisfied by the condition (lastName and date of birth should match)
	 */
	@Override
	public Iterable<Patient> findPatientByLastNameAndDob(String lastName, String dob) {
		return  patientTableRepository.findAll(Specifications.where(PatientSpecification.PatientByLastName(lastName)).and(PatientSpecification.PatientByDob(dob)));
	}



	@Override
	public Iterable<Patient> findPatientByinsuranceName(String insuranceName) {

		Iterable<Patient> patients= patientTableRepository.findAll(PatientSpecification.PatientByInusranceName(insuranceName),new PageRequest(0, 2,Direction.DESC,"patientFName")).getContent();
		return patients;
	}


	/* (non-Javadoc)
	 * @see com.glenwood.glaceemr.server.application.services.patient.PatientService#findPatintByLastNameHavingInsuranceByName(java.lang.String, java.lang.String)
	 * This method returns list of  Patients  satisfied by the condition (lastName and insuranceName should match)
	 */
	@Override
	public Iterable<Patient> findPatintByLastNameHavingInsuranceByName(String lastName, String insuranceName) {
		Iterable<Patient> patients=patientTableRepository.findAll(Specifications.where(PatientSpecification.PatientByLastName(lastName)).and(PatientSpecification.PatientByInusranceName(insuranceName)));
		return  patients;
	}



	public Iterable<Encounter> findEncounterByPatientId(Integer PatientId) {

		return encounterTableRepository.findAll(EncounterSpecification.EncounterByPatientId(PatientId));

	}


	public Iterable<Patient> findInsuranceByPatientId(Integer PatientId) {
		
		Iterable<Patient> patients=patientTableRepository.findAll(PatientSpecification.PatientByPatientId(PatientId));
		
		for (Patient patient : patients) {
			patient.setPatientInsuranceTable(ImmutableSet.copyOf(patientInsuranceRepository.findAll(PatientInsuranceSpecification.InsuranceByPatientId(PatientId))));
		}
		
		

		return patients;

	}


	public List<Map<String, Object>> findLatestEncounterDateAndPatientNameByPatientId(Integer PatientId) {

		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<Object> cq = builder.createQuery();

		Root<Encounter> root = cq.from(Encounter.class);
		Join<Encounter,Patient> patient=root.join(Encounter_.patientTable,JoinType.INNER);
		Predicate Bypatientid=builder.equal(patient.get(Patient_.patientId), PatientId);
		cq.multiselect(builder.max(root.get(Encounter_.encounterId))).where(Bypatientid);
		
		Integer id=(Integer) em.createQuery(cq).getSingleResult();
		Predicate Bymaxencoutnerid=builder.equal(root.get(Encounter_.encounterId), id);
		
		cq.multiselect(root.get(Encounter_.encounterId),root.get(Encounter_.encounterDate),patient.get(Patient_.patientLName),patient.get(Patient_.patientId)).where(Bymaxencoutnerid).distinct(true);  //using metamodel
		
		List<String> listofproperties=new ArrayList<String>();
		listofproperties.add("encounterId");
		listofproperties.add("encoutnerDate");
		listofproperties.add("patientLastName");
		listofproperties.add("patientId");	
		List<Map<String, Object>> listofobj=CustomQueryResult(listofproperties, cq);
		for (int i = 0; i < listofobj.size(); i++) {
			if(listofobj.get(i).containsKey("encoutnerDate")){
				String test=listofobj.get(i).get("encoutnerDate").toString();
//				listofobj.get(i).replace("encoutnerDate",test );
			}
		}
		return listofobj;

	}

	/* Method to execute the custom queries */
	public List<Map<String, Object>> CustomQueryResult(List<String> listinorder,CriteriaQuery<Object> cq) 
	{

		try{

			Iterator iter= em.createQuery(cq).getResultList().iterator();
			List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();

			while ( iter.hasNext() ) {
				Object[] row = (Object[]) iter.next();
				Map<String,Object> newMap=  new HashMap<String,Object>();
				for(int i=0;i<row.length;i++)
				{
					newMap.put(listinorder.get(i),(Object)row[i]);
				}
				resultList.add(newMap);

			}
			return resultList;
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}

	public Iterable<Encounter> findEncountersbyPatientID(List<Integer> patientId) {
		
		return encounterTableRepository.findAll(EncounterSpecification.EncounterByPatientIds(patientId));
	
	}

	
	public List<Iterable<Object>> findLatestEncountersbyPatientIDs(List<Integer> PatientId) {

		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<Object> cq = builder.createQuery();
		Root<Encounter> root = cq.from(Encounter.class);
		Join<Encounter,Patient> patient=root.join(Encounter_.patientTable,JoinType.INNER);
		List<Iterable<Object>> newlist= new ArrayList<Iterable<Object>>();
		for(int i=0;i<PatientId.size();i++){
			Predicate Bypatientid=builder.equal(patient.get(Patient_.patientId), PatientId.get(i));
			cq.multiselect(builder.max(root.get(Encounter_.encounterId))).where(Bypatientid);
			Integer id=(Integer) em.createQuery(cq).getSingleResult();
			Predicate Bymaxencoutnerid=builder.equal(root.get(Encounter_.encounterId), id);
			cq.multiselect(root).where(Bymaxencoutnerid).distinct(true);  //using metamodel
			Iterable<Object> test=em.createQuery(cq).getResultList();
			newlist.add(test);

		}
		return newlist;

	}
	
	
	/*
	 * For inserting the data into the accounts
	 */

	public void insertorupdaterecord()
	{
		Patient testinsert=new Patient();
		testinsert.setIsActive(false);
//		Timestamp patientDob = Timestamp.valueOf("12-12-1999 00:00:00");
		testinsert.setPatientDob("12-12-1999");
		testinsert.setPatientFName("rakesh");
		testinsert.setPatientLName("sai");
		testinsert.setPatientGender(1);
		testinsert.setPatientId(10);
		patientTableRepository.saveAndFlush(testinsert);
	}
	
	@Override
	public Patient updateByPatient(Patient patient) {
		
		return patientTableRepository.saveAndFlush(patient);
	}
	
	
	public void deleteById(Integer patientId)
	{
		patientTableRepository.delete(patientTableRepository.findOne(PatientSpecification.PatientByPatientId(patientId)));
		
		
	}


	@Override
	public void deleteByLastname(String lastName) {
		patientTableRepository.delete(patientTableRepository.findAll(PatientSpecification.PatientByLastName(lastName)));
		
	}


	@Override
	public void deleteAllAddress() {
		addressTableRepository.deleteAll();
		
	}
	
	


	@Override
	public Iterable<Address> updateByAddress(String address) {
		
		Iterable<Address> addresses=addressTableRepository.findAll(AddressSpecfication.byAddress(address));
		for (Address address2 : addresses) {
			address2.setAddress(address2.getAddress()+" USA");
			address2=addressTableRepository.saveAndFlush(address2);	
		}
		 return addresses;
	}


	@Override
	public Patient insertByPatient(Patient patient) {
		return patientTableRepository.save(patient);
	}
	
	

	@Override
	public Patient updateByAddressId(Integer addressId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void deleteByPatient(Patient patient) {
		patientTableRepository.delete(patient);
		
	}
	
}
