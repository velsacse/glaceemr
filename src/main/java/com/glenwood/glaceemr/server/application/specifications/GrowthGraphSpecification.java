package com.glenwood.glaceemr.server.application.specifications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.Cpt;
import com.glenwood.glaceemr.server.application.models.Cpt_;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Encounter_;
import com.glenwood.glaceemr.server.application.models.H650;
import com.glenwood.glaceemr.server.application.models.H650_;
import com.glenwood.glaceemr.server.application.models.ServiceDetail;
import com.glenwood.glaceemr.server.application.models.ServiceDetail_;

public class GrowthGraphSpecification {

	/**
	 * Specification to fetch all graph list based on ageInMonth and sex
	 * @param ageInMonth
	 * @param sex (1 is male 0 is female)
	 * @return
	 */
	public static Specification<H650> byAgeandSex(final Integer ageInMonth, final Integer sex)
	{
		return new Specification<H650>() {

			@Override
			public Predicate toPredicate(Root<H650> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {


				Predicate fromId=cb.greaterThanOrEqualTo(root.get(H650_.h650001), 1);
				Predicate toId=cb.lessThanOrEqualTo(root.get(H650_.h650001), 10);
				Predicate cond=cb.equal(root.get(H650_.h650017), true);
				Predicate ageInMonthLeast=cb.greaterThanOrEqualTo(root.get(H650_.h650004), 0);
				Predicate ageInMonthHighest=cb.lessThanOrEqualTo(root.get(H650_.h650004), ageInMonth);
				Predicate sexPredicate=null;
				if(sex==1)
					sexPredicate=cb.equal(root.get(H650_.h650003), "M");
				else
					sexPredicate=cb.equal(root.get(H650_.h650003), "F");
				Predicate predicate=cb.and(fromId,toId,cond,ageInMonthLeast,ageInMonthHighest,sexPredicate);
				cq.orderBy(cb.asc(root.get(H650_.h650007)));
				return predicate;
			}

		};
	}
	public static Specification<Encounter> getWellVisitDetails(final int chartId)
	{
		return new Specification<Encounter>() {

			@Override
			public Predicate toPredicate(Root<Encounter> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				
				Join<Encounter,Chart> chart=root.join(Encounter_.chart,JoinType.INNER);
				Join<Chart, ServiceDetail> serviceDetail = chart.join(Chart_.serviceDetail,JoinType.INNER);
				Join<ServiceDetail,Cpt> cpt = serviceDetail.join(ServiceDetail_.cpt,JoinType.INNER);
				
				List<String> cptCode = new ArrayList<String>(Arrays.asList("99381","99382","99383","99384","99391","99392","99393","99394"));
				cq.where(cb.and(
						cb.equal(cpt.get(Cpt_.cptIsactive), true),
						cpt.get(Cpt_.cptCptcode).in(cptCode),
						cb.equal(cb.function("date", Date.class, root.get(Encounter_.encounterDate)),cb.function("date", Date.class, serviceDetail.get(ServiceDetail_.serviceDetailDos))),
						cb.equal(root.get(Encounter_.encounterChartid), chartId)));
				
				cq.orderBy(cb.desc(root.get(Encounter_.encounterDate)));
				return null;

			}

		};
	}
}