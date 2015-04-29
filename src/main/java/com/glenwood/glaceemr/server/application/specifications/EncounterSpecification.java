package com.glenwood.glaceemr.server.application.specifications;


import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Encounter_;
import com.glenwood.glaceemr.server.application.models.Patient;
import com.glenwood.glaceemr.server.application.models.Patient_;


public class EncounterSpecification {
	
	/**
	 * @param isActive	: used to search the Encounter Entity by given isActive state
	 * @return BooleanExpression is a predicate   
	 * 
	 * This predicate filter data with isActive state on Encounter Entity 
	 */
		public static Specification<Encounter> IsactiveEncounter() {
			return new Specification<Encounter>() {
				
				@Override
				public Predicate toPredicate(Root<Encounter> root, CriteriaQuery<?> query,
						CriteriaBuilder cb) {
					Predicate IsactiveEncounter=cb.isTrue(root.get(Encounter_.isActive));
					return IsactiveEncounter;
				}
			};
		}
	/**
	 * @param dob	: used to search the Encounter Entity by given encounterDate field
	 * @return BooleanExpression is a predicate   
	 * 
	 * This predicate filter data by encounterDate field on Encounter Entity 
	 */
	
	public static Specification<Encounter> EncounterByPatientId(final Integer patientId)
	{
		return new Specification<Encounter>() {
			
			@Override
			public Predicate toPredicate(Root<Encounter> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Join<Encounter, Patient> patient=root.join(Encounter_.patientTable);
				root.fetch(Encounter_.patientTable);
				Predicate EncounterByPatientid=cb.equal(patient.get(Patient_.patientId), patientId);
				return EncounterByPatientid;
			}
		};
	}
	
	
	public static Specification<Encounter> EncounterByPatientIds(final List<Integer> myList)
	{
		return new Specification<Encounter>() {
			
			@Override
			public Predicate toPredicate(Root<Encounter> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Join<Encounter, Patient> patient=root.join(Encounter_.patientTable);
				root.fetch(Encounter_.patientTable);
				Expression<Integer> integ=patient.get(Patient_.patientId);
				Predicate EncounterByPatientid=integ.in(myList);
				return EncounterByPatientid;
			}
		};
	}
	
	
	

}
