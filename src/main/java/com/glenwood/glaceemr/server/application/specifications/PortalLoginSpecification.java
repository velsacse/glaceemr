package com.glenwood.glaceemr.server.application.specifications;

import java.sql.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.H213;
import com.glenwood.glaceemr.server.application.models.H213_;
import com.glenwood.glaceemr.server.application.models.H809;
import com.glenwood.glaceemr.server.application.models.H809_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.PortalUser;
import com.glenwood.glaceemr.server.application.models.PortalUser_;

public class PortalLoginSpecification {
	
	/**
	 * @param patientId	: used to get patient details of a patient of that particular id
	 * @return BooleanExpression is a  predicate  
	 */	
   public static Specification<PortalUser> patientByUsername(final String username)
   {
	   return new Specification<PortalUser>() {

		@Override
		public Predicate toPredicate(Root<PortalUser> root,
				CriteriaQuery<?> cq, CriteriaBuilder cb) {
				
			Predicate usernamePredicate=cb.and(cb.equal(cb.upper(root.get(PortalUser_.username)), username.toUpperCase()));
			return usernamePredicate;		
		}
		   
	};
   }
   
   public static Specification<H809> patientById(final String patientId)
   {
	   return new Specification<H809>() {

		@Override
		public Predicate toPredicate(Root<H809> root,
				CriteriaQuery<?> cq, CriteriaBuilder cb) {
				
			Predicate usernamePredicate=cb.and(cb.equal(root.get(H809_.h809002), Integer.parseInt(patientId)));
			return usernamePredicate;		
		}
		   
	};
   }
   
   public static Specification<H809> activePatientById(final int patientId)
   {
	   return new Specification<H809>() {

		@Override
		public Predicate toPredicate(Root<H809> root,
				CriteriaQuery<?> cq, CriteriaBuilder cb) {
				
			Predicate usernamePredicate=cb.and(cb.equal(root.get(H809_.h809002),patientId),cb.equal(root.get(H809_.h809009), 1));
			return usernamePredicate;		
		}
		   
	};
   }
   
   public static Specification<H809> inactivePatientById(final int patientId)
   {
	   return new Specification<H809>() {

		@Override
		public Predicate toPredicate(Root<H809> root,
				CriteriaQuery<?> cq, CriteriaBuilder cb) {
				
			Predicate usernamePredicate=cb.and(cb.equal(root.get(H809_.h809002),patientId),cb.equal(root.get(H809_.h809009), 0));
			return usernamePredicate;		
		}
		   
	};
   }
   
   public static Specification<H809> getAllPatientsByUsername(final String username)
   {
	   return new Specification<H809>() {

		@Override
		public Predicate toPredicate(Root<H809> root,
				CriteriaQuery<?> cq, CriteriaBuilder cb) {
				
			Predicate usernamePredicate=cb.and(cb.equal(cb.upper(root.get(H809_.h809004)), username.toUpperCase()));
			return usernamePredicate;		
		}
		   
	};
   }
   
   public static Specification<PatientRegistration> getPatientsList(final Date dob, final String firstName, final String lastName)
   {
	   return new Specification<PatientRegistration>() {

		@Override
		public Predicate toPredicate(Root<PatientRegistration> root,
				CriteriaQuery<?> cq, CriteriaBuilder cb) {
				
			Predicate usernamePredicate=cb.and(cb.equal(cb.upper(root.get(PatientRegistration_.patientRegistrationFirstName)), firstName.toUpperCase()),
					cb.equal(cb.upper(root.get(PatientRegistration_.patientRegistrationLastName)), lastName.toUpperCase()),
					cb.equal(root.get(PatientRegistration_.patientRegistrationDob), dob));
			return usernamePredicate;		
		}
		   
	};
   }
   
   public static Specification<PatientRegistration> getPatientsList(final Date dob, final String firstName, final String lastName, final String email)
   {
	   return new Specification<PatientRegistration>() {

		@Override
		public Predicate toPredicate(Root<PatientRegistration> root,
				CriteriaQuery<?> cq, CriteriaBuilder cb) {
				
			Predicate usernamePredicate=cb.and(cb.equal(cb.upper(root.get(PatientRegistration_.patientRegistrationFirstName)), firstName.toUpperCase()),
					cb.equal(cb.upper(root.get(PatientRegistration_.patientRegistrationLastName)), lastName.toUpperCase()),
					cb.equal(root.get(PatientRegistration_.patientRegistrationDob), dob),
					cb.equal(root.get(PatientRegistration_.patientRegistrationMailId), email));
			return usernamePredicate;		
		}
		   
	};
   }
   
   public static Specification<Chart> chartByPatientId(final int patientId)
   {
	   return new Specification<Chart>() {

		@Override
		public Predicate toPredicate(Root<Chart> root,
				CriteriaQuery<?> cq, CriteriaBuilder cb) {
				
			Predicate chartPredicate=cb.and(cb.equal(root.get(Chart_.chartPatientid), patientId));
			return chartPredicate;		
		}
		   
	};
   }
   
   
   public static Specification<H213> h213ByCategoryName(final String category)
   {
	   return new Specification<H213>() {

		@Override
		public Predicate toPredicate(Root<H213> root,
				CriteriaQuery<?> cq, CriteriaBuilder cb) {
				
			Predicate chartPredicate=cb.and(cb.equal(root.get(H213_.h213002), category));
			return chartPredicate;		
		}
		   
	};
   }


}
