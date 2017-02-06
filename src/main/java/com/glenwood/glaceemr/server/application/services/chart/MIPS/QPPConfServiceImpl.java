package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.util.List;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.MacraConfiguration;
import com.glenwood.glaceemr.server.application.models.MacraProviderConfiguration;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresProviderMapping;
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
	
	@Override
	public void saveConfDetails(Integer programYear, Integer type,
			Integer providerId, java.util.Date startDate, java.util.Date endDate,
			Integer submissionMtd) throws Exception {
		
		Date sqlStartDate = new Date(startDate.getTime());
		Date sqlEndDate = new Date(endDate.getTime());
		MacraProviderConfiguration providerThereOrNot=macraProviderConfigurationRepository.findOne(Specifications.where(QPPConfigurationSpecification.getProviderObj(providerId)));
		if(providerThereOrNot==null || providerThereOrNot.equals(null)){
			MacraConfiguration macraConfObj=new MacraConfiguration();
			macraConfObj.setMacraConfigurationType(type);
			macraConfObj.setMacraConfigurationYear(programYear);
			macraConfigurationRepository.saveAndFlush(macraConfObj);
			System.out.println("comingoooooooooooooooooooooo");
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
	public List<MacraProviderConfiguration> getGroupConfData() throws Exception {

		List<MacraProviderConfiguration> groupData = macraProviderConfigurationRepository
				.findAll(Specifications.where(QPPConfigurationSpecification.getGroupData()));

		return groupData;
		
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
	public List<QualityMeasuresProviderMapping> getGroupMeasureIds()
			throws Exception {
		List<QualityMeasuresProviderMapping> measureIds=qualityMeasuresProviderMappingRepository.findAll(Specifications.where(QPPConfigurationSpecification.getMeasureIds()));
		return measureIds;
	}
	
	@Override
	public List<QualityMeasuresProviderMapping> getIndividualMeasureIds(Integer providerId)
			throws Exception {
		
		List<QualityMeasuresProviderMapping> measureIds=qualityMeasuresProviderMappingRepository.findAll(Specifications.where(QPPConfigurationSpecification.getIndividualMeasureIds(providerId)));
		return measureIds;
		
	}
	
	@Override
	public void addMeasuresToProvider(String measureIds, Integer providerId) {
		
		List<QualityMeasuresProviderMapping> objectsToDelete=qualityMeasuresProviderMappingRepository.findAll(Specifications.where(QPPConfigurationSpecification.getIndividualMeasureIds(providerId)));
		if(objectsToDelete!=null && !(objectsToDelete.equals(null))){
			qualityMeasuresProviderMappingRepository.deleteInBatch(objectsToDelete);
		}

		String[] measureid;
		measureid=measureIds.split(",");
		
		if(measureid.length > 0 && !measureid[0].equals("-1") ){
			
			for(int i=0;i<measureid.length;i++){
				QualityMeasuresProviderMapping qmpmObj=new QualityMeasuresProviderMapping();
				qmpmObj.setQualityMeasuresProviderMappingProviderId(providerId);
				qmpmObj.setQualityMeasuresProviderMappingMeasureId(measureid[i]);
				qualityMeasuresProviderMappingRepository.saveAndFlush(qmpmObj);
			}
			
		}
		
	}

}