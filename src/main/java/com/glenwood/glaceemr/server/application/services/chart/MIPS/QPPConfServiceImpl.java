package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.Bean.MacraProviderQDM;
import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.LabEntriesParameter;
import com.glenwood.glaceemr.server.application.models.LabEntriesParameter_;
import com.glenwood.glaceemr.server.application.models.LabParameterCode;
import com.glenwood.glaceemr.server.application.models.LabParameterCode_;
import com.glenwood.glaceemr.server.application.models.LabParameters;
import com.glenwood.glaceemr.server.application.models.LabParameters_;
import com.glenwood.glaceemr.server.application.models.MacraConfiguration;
import com.glenwood.glaceemr.server.application.models.MacraProviderConfiguration;
import com.glenwood.glaceemr.server.application.models.MacraProviderConfiguration_;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresProviderMapping;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresProviderMapping_;
import com.glenwood.glaceemr.server.application.repositories.EmployeeProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.MacraConfigurationRepository;
import com.glenwood.glaceemr.server.application.repositories.MacraProviderConfigurationRepository;
import com.glenwood.glaceemr.server.application.repositories.QualityMeasuresProviderMappingRepository;
import com.glenwood.glaceemr.server.application.specifications.QPPConfigurationSpecification;


@Service
@Transactional
public class QPPConfServiceImpl implements QPPConfigurationService{
	
	@Autowired
	MacraConfigurationRepository macraConfigurationRepository;
	@Autowired
	MacraProviderConfigurationRepository macraProviderConfigurationRepository;
	@Autowired
	EmployeeProfileRepository employeeProfileRepository;
	@Autowired
	QualityMeasuresProviderMappingRepository qualityMeasuresProviderMappingRepository;
	@PersistenceContext
	EntityManager em;
	
	@Override
	public void saveConfDetails(Integer programYear, Integer type,
			Integer providerId, java.util.Date startDate, java.util.Date endDate,
			Integer submissionMtd) throws Exception {
		java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
		java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
		MacraConfiguration yearThereOrNot=macraConfigurationRepository.findOne(Specifications.where(QPPConfigurationSpecification.getConfObj(programYear)));
		if(yearThereOrNot==null || yearThereOrNot.equals(null)){
			MacraConfiguration macraConfObj=new MacraConfiguration();
			macraConfObj.setMacraConfigurationType(type);
			macraConfObj.setMacraConfigurationYear(programYear);
			macraConfigurationRepository.saveAndFlush(macraConfObj);
		}
		else{
			yearThereOrNot.setMacraConfigurationType(type);
			macraConfigurationRepository.saveAndFlush(yearThereOrNot);
		}
		
		MacraProviderConfiguration providerThereOrNot=macraProviderConfigurationRepository.findOne(Specifications.where(QPPConfigurationSpecification.getProviderObj(providerId)));
		
		if(providerThereOrNot==null || providerThereOrNot.equals(null)){
			MacraProviderConfiguration macraProviderConfObj=new MacraProviderConfiguration();
			macraProviderConfObj.setMacraProviderConfigurationProviderId(providerId);
			macraProviderConfObj.setMacraProviderConfigurationReportingMethod(submissionMtd);
			macraProviderConfObj.setMacraProviderConfigurationReportingStart(sqlStartDate);
			macraProviderConfObj.setMacraProviderConfigurationReportingEnd(sqlEndDate);
			macraProviderConfObj.setMacraProviderConfigurationReportingYear(programYear);
			macraProviderConfigurationRepository.saveAndFlush(macraProviderConfObj);
		}
		else{
			providerThereOrNot.setMacraProviderConfigurationReportingEnd(sqlEndDate);
			providerThereOrNot.setMacraProviderConfigurationReportingStart(sqlStartDate);
			providerThereOrNot.setMacraProviderConfigurationReportingYear(programYear);
			providerThereOrNot.setMacraProviderConfigurationReportingMethod(submissionMtd);
			macraProviderConfigurationRepository.saveAndFlush(providerThereOrNot);
		}
		
	}
	
	@Override
	public List<MacraProviderConfiguration> getProviderInfo(Integer provider)
			throws Exception {
		List<MacraProviderConfiguration> groupData = macraProviderConfigurationRepository
				.findAll(Specifications.where(QPPConfigurationSpecification.getProviderData(provider)));
		if(!groupData.equals(null))
		return groupData;
		else
			return null;
	}
	@Override
	public Integer getProviderId(String provider) throws Exception {
		EmployeeProfile providerObj=employeeProfileRepository.findOne(Specifications.where(QPPConfigurationSpecification.getProviderId(provider)));
		Integer providerId=providerObj.getEmpProfileEmpid();
		return providerId;
	}
	
	@Override
	public List<QualityMeasuresProviderMapping> getMeasureIds(Integer providerId)
			throws Exception {
		List<QualityMeasuresProviderMapping> measureIds=qualityMeasuresProviderMappingRepository.findAll(Specifications.where(QPPConfigurationSpecification.getMeasureIds(providerId)));
		return measureIds;
	}
	@Override
	public void addMeasuresToProvider(String measureIds, Integer providerId) {
		List<QualityMeasuresProviderMapping> objectsToDelete=qualityMeasuresProviderMappingRepository.findAll(Specifications.where(QPPConfigurationSpecification.getMeasureIds(providerId)));
		if(objectsToDelete!=null && !(objectsToDelete.equals(null))){
			qualityMeasuresProviderMappingRepository.deleteInBatch(objectsToDelete);
		}
			
		String[] measureid;
		measureid=measureIds.split(",");
		for(int i=0;i<measureid.length;i++){
			QualityMeasuresProviderMapping qmpmObj=new QualityMeasuresProviderMapping();

			qmpmObj.setQualityMeasuresProviderMappingProviderId(providerId);
		qmpmObj.setQualityMeasuresProviderMappingMeasureId(measureid[i]);
		qualityMeasuresProviderMappingRepository.saveAndFlush(qmpmObj);
		}
	}
	@Override
	public List<Chart> getLabDetails(final Integer patientId) throws Exception {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<Chart> rootChart = cq.from(Chart.class);
		Join<Chart,LabEntriesParameter> joinChartLabentriesparameter=rootChart.join(Chart_.labEntriesParameter,JoinType.INNER);
		Join<LabEntriesParameter,LabParameters> joinLabentriesparamLabparam=joinChartLabentriesparameter.join(LabEntriesParameter_.labParametersTable,JoinType.INNER);
		Join<LabParameters,LabParameterCode> joinLabparametersLabparametercode=joinLabentriesparamLabparam.join(LabParameters_.labParamCode,JoinType.INNER);
		Predicate predicateByPatientId=builder.equal(rootChart.get(Chart_.chartPatientid),patientId);
		joinChartLabentriesparameter.on(predicateByPatientId);
		Predicate[] restrictions = new Predicate[] {
				builder.equal(joinChartLabentriesparameter.get(LabEntriesParameter_.labEntriesParameterIsactive),true),
				builder.equal(joinLabentriesparamLabparam.get(LabParameters_.labParametersIsactive),true)
		};
		//joinLabentriesparamLabparam.on(restrictions1);
		Predicate loincPredicate=builder.like((builder.upper(joinLabparametersLabparametercode.get(LabParameterCode_.labParameterCodeSystem))),builder.literal("LOINC"));
		joinLabparametersLabparametercode.on(loincPredicate);
		Selection[] selections= new Selection[] { 
				joinLabparametersLabparametercode.get(LabParameterCode_.labParameterCodeValue).alias("Code"),
				joinLabentriesparamLabparam.get(LabParameters_.labParametersName).alias("Description"),
				joinChartLabentriesparameter.get(LabEntriesParameter_.labEntriesParameterDate).alias("Start Date"),
				builder.literal("2").alias("Status"),
				joinChartLabentriesparameter.get(LabEntriesParameter_.labEntriesParameterValue).alias("Result Value")
		};
		cq.multiselect(selections);
		cq.where(restrictions);
		Query query=em.createQuery(cq);
		List<Chart> alertsResultList=query.getResultList();
		return alertsResultList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<MacraProviderQDM> getCompleteProviderInfo(
			Integer providerId) throws Exception {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<MacraProviderQDM> cq = builder.createQuery(MacraProviderQDM.class);
		Root<MacraProviderConfiguration> rootMacraProviderConfiguration = cq.from(MacraProviderConfiguration.class);
		Join<MacraProviderConfiguration,QualityMeasuresProviderMapping> joinProviderConfigurationProviderMapping=rootMacraProviderConfiguration.join(MacraProviderConfiguration_.qualityMeasuresProviderMappingTable,JoinType.INNER);
		Predicate predicateByProviderId =builder.equal(rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId),providerId);
		joinProviderConfigurationProviderMapping.on(predicateByProviderId);
		Selection[] selections= new Selection[] {
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId).alias("Provider"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingYear).alias("Year"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingStart).alias("Start Date"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingEnd).alias("End Date"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingMethod).alias("Rep method"),
				builder.function("string_agg", String.class, joinProviderConfigurationProviderMapping.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingMeasureId),builder.literal(","))
		};
		cq.multiselect(selections);
		cq.groupBy(rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingYear),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingStart),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingEnd),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingMethod)
				);
		List<MacraProviderQDM> providerInfo=em.createQuery(cq).getResultList();
		return providerInfo;
		
	}

}
