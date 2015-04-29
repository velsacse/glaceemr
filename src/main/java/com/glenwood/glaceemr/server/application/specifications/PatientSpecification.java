package com.glenwood.glaceemr.server.application.specifications;


import java.sql.Timestamp;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Insurance_;
import com.glenwood.glaceemr.server.application.models.PatientInsurance_;
import com.glenwood.glaceemr.server.application.models.Patient_;
import com.glenwood.glaceemr.server.application.models.Insurance;
import com.glenwood.glaceemr.server.application.models.Patient;
import com.glenwood.glaceemr.server.application.models.PatientInsurance;


@Component
@Transactional 
public class PatientSpecification {

	/**
	 * @param lastName	: used to search the patients 
	 * @return BooleanExpression is a  predicate  
	 */
	public static Specification<Patient> PatientByLastName(final String lastName)
	{
		return new Specification<Patient>() {
			
			@Override
			public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate PatientByLastName = cb.like(cb.upper(root.get(Patient_.patientLName)), lastName.toUpperCase());
				return PatientByLastName;
			}
		};
	}
	
	
	
	/**
	 * @param lastName	: used to search the patients 
	 * @return BooleanExpression is a  predicate  
	 */
	public static Specification<Patient> PatientByFirstName(final String firstName)
	{
		return new Specification<Patient>() {
			
			@Override
			public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate PatientByFirstName = cb.like(cb.upper(root.get(Patient_.patientFName)), firstName.toUpperCase());
				return PatientByFirstName;
			}
		};
	}
	

	/**
	 * @param dob	: used to search the patients Entity by given date of birth
	 * @return BooleanExpression is a predicate   
	 * 
	 * This predicate filter Patients data by dateofbirth  on Patient Entity 
	 */
	
	public static Specification<Patient> PatientByDob(final String dob)
	{
		return new Specification<Patient>() {
			
			@Override
			public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Timestamp toTimeStamp = Timestamp.valueOf(dob+" 00:00:00");
				Predicate PatientByDob=cb.equal(root.get(Patient_.patientDob),toTimeStamp);
				return PatientByDob;
			}
		};
	}
	
	
	public static Specification<Patient> PatientByPatientId(final Integer PatietnId)
	{
		return new Specification<Patient>() {
			
			@Override
			public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				
				Predicate PatientById=cb.equal(root.get(Patient_.patientId),PatietnId);
				return PatientById;
			}
		};
	}

	/**
	 * @param lastName		: used to search the patients Entity by given lastName
	 * @param insuranceName	: used to search the insurance Entity by given insurance
	 * @return BooleanBuilder is a predicate   
	 * 
	 * This predicate filter Patients data with lastName on patients Entity  and insuranceName on Insurance Entity
	 */
	
	public static Specification<Patient> PatientByInusranceName(final String insuranceName)
	{
		return new Specification<Patient>() {
			
			@Override
			public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
//				Join<Patient,Encounter> encounter=root.join(Patient_.encounterTable);
				Join<Patient,PatientInsurance> patientinsurance= root.join(Patient_.patientInsuranceTable);
				Join<PatientInsurance,Insurance> insurance= patientinsurance.join(PatientInsurance_.insuranceMasterTable);
				if (Long.class != query.getResultType()) {
//				root.fetch(Patient_.patientInsuranceTable).fetch(PatientInsurance_.insuranceMasterTable);
//				root.fetch(Patient_.encounterTable);
				}
				Predicate PatientByInsuranceName=cb.like(cb.upper(insurance.get(Insurance_.inuranceName)),insuranceName.toUpperCase());
				return PatientByInsuranceName;
			}
		};
	}
	
	
	
}
