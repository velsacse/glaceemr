package com.glenwood.glaceemr.server.application.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.MacraProviderConfiguration;
import com.glenwood.glaceemr.server.application.models.MacraProviderConfiguration_;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresProviderMapping;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresProviderMapping_;


public class QPPConfigurationSpecification {
	public static Specification<MacraProviderConfiguration> getGroupData() {
		return new Specification<MacraProviderConfiguration>() {
			public Predicate toPredicate(Root<MacraProviderConfiguration> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicateByProviderId = cb.equal(
						root.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId),-1);
				Predicate groupDetails=cb.and(predicateByProviderId);
				return groupDetails;
			}
		};

	}
	public static Specification<MacraProviderConfiguration> getProviderData(final Integer providerId){
		return new Specification<MacraProviderConfiguration>() {

			@Override
			public Predicate toPredicate(Root<MacraProviderConfiguration> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicateByProviderId = cb.equal(
						root.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId),providerId);
				Predicate providerDetails=cb.and(predicateByProviderId);
				return providerDetails;
			}
		};
		
	}
	public static Specification<EmployeeProfile> getProviderId(final String provider){
		return new Specification<EmployeeProfile>() {

			@Override
			public Predicate toPredicate(Root<EmployeeProfile> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicateByProviderName = cb.equal(
						root.get(EmployeeProfile_.empProfileFullname),provider);
				Predicate providerId=cb.and(predicateByProviderName);

				return providerId;
			}
		};
		
	}
	public static Specification<QualityMeasuresProviderMapping> getMeasureIds(){
		return new Specification<QualityMeasuresProviderMapping>() {

			@Override
			public Predicate toPredicate(
					Root<QualityMeasuresProviderMapping> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicateByproviderId=cb.equal(root.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingProviderId),"-1");
				Predicate measureID=cb.and(predicateByproviderId);

				return measureID;
			}
		};
	}
	public static Specification<QualityMeasuresProviderMapping> getIndividualMeasureIds(final Integer providerId){
		return new Specification<QualityMeasuresProviderMapping>() {

			@Override
			public Predicate toPredicate(
					Root<QualityMeasuresProviderMapping> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicateByproviderId=cb.equal(root.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingProviderId),providerId);
				Predicate indiMeasureID=cb.and(predicateByproviderId);
				return indiMeasureID;
			}
		};
		
	}
	public static Specification<MacraProviderConfiguration> getProviderObj(final Integer providerId){
		return new Specification<MacraProviderConfiguration>() {

			@Override
			public Predicate toPredicate(Root<MacraProviderConfiguration> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicateByproviderId=cb.equal(root.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId),providerId);
				Predicate providerObj=cb.and(predicateByproviderId);
				return providerObj;
			}
		};
		
	}
	
}
