package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import com.glenwood.glaceemr.server.application.models.LeafPatient;
import com.glenwood.glaceemr.server.application.models.LeafPatient_;

public class LeafPatientSpecfication {

	/**
	 * Get patient leaf details for given encounter and leafId
	 * 
	 * @param leafId
	 * @param encounterId
	 * @return
	 */
	public static Specification<LeafPatient> getPatLeafByLeafIdAndEncId(final Integer leafId,final Integer encounterId){

		return new Specification<LeafPatient>(){

			@Override
			public Predicate toPredicate(Root<LeafPatient> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate leafIdPred=cb.equal(root.get(LeafPatient_.leafPatientLeafLibraryId),leafId);
				Predicate encounterPred=cb.equal(root.get(LeafPatient_.leafPatientEncounterId),encounterId);
				return query.where(leafIdPred,encounterPred).getRestriction();
			}

		};
	}

}
