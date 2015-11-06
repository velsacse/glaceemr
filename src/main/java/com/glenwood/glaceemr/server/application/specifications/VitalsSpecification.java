package com.glenwood.glaceemr.server.application.specifications;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.Cpt;
import com.glenwood.glaceemr.server.application.models.Cpt_;
import com.glenwood.glaceemr.server.application.models.UnitsOfMeasure;
import com.glenwood.glaceemr.server.application.models.UnitsOfMeasure_;
import com.glenwood.glaceemr.server.application.models.VitalGroup;
import com.glenwood.glaceemr.server.application.models.VitalGroup_;
import com.glenwood.glaceemr.server.application.models.VitalsParameter;
import com.glenwood.glaceemr.server.application.models.VitalsParameter_;



public class VitalsSpecification {
	
	public static Specification<VitalsParameter> vitalsActiveGwId(final List<String> gwid){
		return new Specification<VitalsParameter>() {
			@Override
			public Predicate toPredicate(Root<VitalsParameter> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate isGwid=root.get(VitalsParameter_.vitalsParameterGwId).in(gwid);
				Predicate isActive=cb.equal(root.get(VitalsParameter_.vitalsParameterIsactive),true);
				return cb.and(isGwid,isActive);
			}
		};
	}

	public static Specification<VitalsParameter> getUnitsForVitals(final List<String> gwids){
		return new Specification<VitalsParameter>() {
			@Override
			public Predicate toPredicate(Root<VitalsParameter> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				root.fetch(VitalsParameter_.unitsOfMeasureTable,JoinType.INNER);
				Predicate elementPred=root.get(VitalsParameter_.vitalsParameterGwId).in(gwids);
				return elementPred;
			}
		};
	}
	
	/**
	 * Specification to get the list of distinct vital parameters which are active
	 * @param isActive
	 * @return Specification<VitalsParameter>
	 */
	public static Specification<VitalsParameter> vitalParametersIsActiveDistinct(final Boolean isActive)
	{
		return new Specification<VitalsParameter>() {

			@Override
			public Predicate toPredicate(Root<VitalsParameter> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				query.distinct(true);
				query.orderBy(cb.asc(root.get(VitalsParameter_.vitalsParameterName)));
				Predicate vitalsIsactive = cb.equal(root.get(VitalsParameter_.vitalsParameterIsactive),isActive);
				return vitalsIsactive;
			}
		};
	}

	
	
	
	
}
