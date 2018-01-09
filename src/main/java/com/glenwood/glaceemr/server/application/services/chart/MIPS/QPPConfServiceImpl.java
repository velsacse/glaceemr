package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.glenwood.glaceemr.server.application.Bean.DiagnosisList;
import com.glenwood.glaceemr.server.application.Bean.IAMeasureBean;
import com.glenwood.glaceemr.server.application.Bean.MIPSPatientInformation;
import com.glenwood.glaceemr.server.application.Bean.MacraProviderQDM;
import com.glenwood.glaceemr.server.application.Bean.SharedFolderBean;
import com.glenwood.glaceemr.server.application.Bean.getMeasureBean;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.CQMSpecification;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.Category;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.Code;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.CodeSet;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.EMeasure;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.EMeasureUtils;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.Valueset;
import com.glenwood.glaceemr.server.application.models.BillingConfigTable;
import com.glenwood.glaceemr.server.application.models.BillingConfigTable_;
import com.glenwood.glaceemr.server.application.models.Billinglookup;
import com.glenwood.glaceemr.server.application.models.Billinglookup_;
import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Encounter_;
import com.glenwood.glaceemr.server.application.models.HpiSymptom;
import com.glenwood.glaceemr.server.application.models.HpiSymptom_;
import com.glenwood.glaceemr.server.application.models.IAMeasures;
import com.glenwood.glaceemr.server.application.models.IAMeasures_;
import com.glenwood.glaceemr.server.application.models.LabEntriesParameter;
import com.glenwood.glaceemr.server.application.models.LabEntriesParameter_;
import com.glenwood.glaceemr.server.application.models.LabParameterCode;
import com.glenwood.glaceemr.server.application.models.LabParameterCode_;
import com.glenwood.glaceemr.server.application.models.LabParameters;
import com.glenwood.glaceemr.server.application.models.LabParameters_;
import com.glenwood.glaceemr.server.application.models.MacraConfiguration;
import com.glenwood.glaceemr.server.application.models.MacraConfiguration_;
import com.glenwood.glaceemr.server.application.models.MacraMeasuresRate;
import com.glenwood.glaceemr.server.application.models.MacraMeasuresRate_;
import com.glenwood.glaceemr.server.application.models.MacraProviderConfiguration;
import com.glenwood.glaceemr.server.application.models.MacraProviderConfiguration_;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.PosTable_;
import com.glenwood.glaceemr.server.application.models.PosType;
import com.glenwood.glaceemr.server.application.models.PosType_;
import com.glenwood.glaceemr.server.application.models.ProblemList;
import com.glenwood.glaceemr.server.application.models.ProblemList_;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresPatientEntries;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresPatientEntriesHistory;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresPatientEntriesHistory_;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresPatientEntries_;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresProviderMapping;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresProviderMapping_;
import com.glenwood.glaceemr.server.application.repositories.EmployeeProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.IAMeasuresRepository;
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
	@Autowired
	IAMeasuresRepository iaMeasuresRepository;
	@PersistenceContext
	EntityManager em;
	@Autowired
	SharedFolderBean sharedFolderBean;
	
	RestTemplate restTemplate = new RestTemplate();

	@Override
	public void saveConfDetails(Integer programYear, Integer type,
			Integer providerId, java.util.Date startDate, java.util.Date endDate,
			Integer submissionMtd,short reportType) throws Exception {
//		deletePerformanceEntries(programYear,providerId);
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
		
		MacraProviderConfiguration providerThereOrNot=macraProviderConfigurationRepository.findOne(Specifications.where(QPPConfigurationSpecification.getProviderObj(providerId,programYear)));
		
		if(providerThereOrNot==null || providerThereOrNot.equals(null)){
			MacraProviderConfiguration macraProviderConfObj=new MacraProviderConfiguration();
			macraProviderConfObj.setMacraProviderConfigurationProviderId(providerId);
			macraProviderConfObj.setMacraProviderConfigurationReportingMethod(submissionMtd);
			macraProviderConfObj.setMacraProviderConfigurationReportingStart(sqlStartDate);
			macraProviderConfObj.setMacraProviderConfigurationReportingEnd(sqlEndDate);
			macraProviderConfObj.setMacraProviderConfigurationReportingYear(programYear);
			macraProviderConfObj.setMacraProviderConfigurationReportType(reportType);
			macraProviderConfigurationRepository.saveAndFlush(macraProviderConfObj);
		}
		else{
			providerThereOrNot.setMacraProviderConfigurationReportingEnd(sqlEndDate);
			providerThereOrNot.setMacraProviderConfigurationReportingStart(sqlStartDate);
			providerThereOrNot.setMacraProviderConfigurationReportingYear(programYear);
			providerThereOrNot.setMacraProviderConfigurationReportingMethod(submissionMtd);
			providerThereOrNot.setMacraProviderConfigurationReportType(reportType);
			macraProviderConfigurationRepository.saveAndFlush(providerThereOrNot);
		}
		
	}
	
	private void deletePerformanceEntries(Integer programYear,Integer providerId) {
		//Delete Entries in QualityMeasuresPatientEntries table
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<QualityMeasuresPatientEntries> delete = cb.createCriteriaDelete(QualityMeasuresPatientEntries.class);
		Root<QualityMeasuresPatientEntries> rootQMPE = delete.from(QualityMeasuresPatientEntries.class);
		delete.where(cb.equal(rootQMPE.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesProviderId),providerId),cb.equal(rootQMPE.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesReportingYear),programYear));
		this.em.createQuery(delete).executeUpdate();
		
		//Delete Entries in QualityMeasuresPatientEntriesHistory table
		CriteriaBuilder cb1 = em.getCriteriaBuilder();
		CriteriaDelete<QualityMeasuresPatientEntriesHistory> delete1 = cb1.createCriteriaDelete(QualityMeasuresPatientEntriesHistory.class);
		Root<QualityMeasuresPatientEntriesHistory> rootQMPEH = delete1.from(QualityMeasuresPatientEntriesHistory.class);
		delete1.where(cb1.equal(rootQMPEH.get(QualityMeasuresPatientEntriesHistory_.qualityMeasuresPatientEntriesProviderId),providerId),cb1.equal(rootQMPEH.get(QualityMeasuresPatientEntriesHistory_.qualityMeasuresPatientEntriesReportingYear),programYear));
		this.em.createQuery(delete1).executeUpdate();
		
		//Delete Entries in MacraMeasureRate table
		CriteriaBuilder cb2 = em.getCriteriaBuilder();
		CriteriaDelete<MacraMeasuresRate> delete2 = cb2.createCriteriaDelete(MacraMeasuresRate.class);
		Root<MacraMeasuresRate> rootMMR = delete2.from(MacraMeasuresRate.class);
		delete2.where(cb2.equal(rootMMR.get(MacraMeasuresRate_.macraMeasuresRateProviderId),providerId),cb2.equal(rootMMR.get(MacraMeasuresRate_.macraMeasuresRateReportingYear),programYear));
		this.em.createQuery(delete2).executeUpdate();
		
	}

	@Override
	public List<ConfigurationDetails> getProviderInfo(Integer provider,Integer year)
			throws Exception {
		CriteriaBuilder builder1 = em.getCriteriaBuilder();
        CriteriaQuery<ConfigurationDetails> cq1 = builder1.createQuery(ConfigurationDetails.class);
        
        Root<MacraConfiguration> root1 = cq1.from(MacraConfiguration.class);
        Join<MacraConfiguration,MacraProviderConfiguration> joinMacraProviderConfiguration=root1.join(MacraConfiguration_.macraProviderConf,JoinType.INNER);
        
        Predicate byProvider=builder1.equal(joinMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId),provider);
        Predicate byYear=builder1.equal(root1.get(MacraConfiguration_.macraConfigurationYear),year);
        Selection[] selections= new Selection[] {
        		joinMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingStart),
        		joinMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingEnd),
        		joinMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingYear),
        		joinMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingMethod),
        		joinMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportType)
		};
		cq1.select(builder1.construct(ConfigurationDetails.class,selections));
		cq1.where(byProvider,byYear);
		List<ConfigurationDetails> result=em.createQuery(cq1).getResultList();
		return result;
	}
	@Override
	public Integer getProviderId(String provider) throws Exception {
		EmployeeProfile providerObj=employeeProfileRepository.findOne(Specifications.where(QPPConfigurationSpecification.getProviderId(provider)));
		Integer providerId=providerObj.getEmpProfileEmpid();
		return providerId;
	}
	
	@Override
	public List<ConfigurationDetails> getMeasureIds(Integer providerId,Integer year)
			throws Exception {
		CriteriaBuilder builder1 = em.getCriteriaBuilder();
        CriteriaQuery<ConfigurationDetails> cq1 = builder1.createQuery(ConfigurationDetails.class);
        Root<QualityMeasuresProviderMapping> root1 = cq1.from(QualityMeasuresProviderMapping.class);
        Predicate byProvider=builder1.equal(root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingProviderId),providerId);
        Predicate byYear=builder1.equal(root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingReportingYear),year);
        Predicate byMeasureId=builder1.notLike(root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingMeasureId),"IA_%");
        Selection[] selections= new Selection[] {
        root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingMeasureId)
        };
        cq1.select(builder1.construct(ConfigurationDetails.class,selections));
		cq1.where(byProvider,byYear,byMeasureId);
		List<ConfigurationDetails> result=em.createQuery(cq1).getResultList();
		return result;
	}
	@Override
	public void addMeasuresToProvider(String measureIds, Integer providerId, Integer reportingYear) {
		CriteriaBuilder cb1 = em.getCriteriaBuilder();
		CriteriaDelete<QualityMeasuresProviderMapping> delete1 = cb1.createCriteriaDelete(QualityMeasuresProviderMapping.class);
		Root<QualityMeasuresProviderMapping> rootCriteria1 = delete1.from(QualityMeasuresProviderMapping.class);
		delete1.where(cb1.equal(rootCriteria1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingProviderId),providerId),cb1.equal(rootCriteria1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingReportingYear),reportingYear),cb1.notLike(rootCriteria1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingMeasureId), "IA_%"));
		this.em.createQuery(delete1).executeUpdate();
		
		String[] measureid;
		measureid=measureIds.split(",");
		for(int i=0;i<measureid.length;i++){
		QualityMeasuresProviderMapping qmpmObj=new QualityMeasuresProviderMapping();

		qmpmObj.setQualityMeasuresProviderMappingProviderId(providerId);
		qmpmObj.setQualityMeasuresProviderMappingMeasureId(measureid[i]);
		qmpmObj.setQualityMeasuresProviderMappingReportingYear(reportingYear);
		qualityMeasuresProviderMappingRepository.saveAndFlush(qmpmObj);
		}
	}
	
	@Override
	public List<ConfigurationDetails> getImprovementActivityMeasureIds(Integer providerId,Integer year)
			throws Exception {
		CriteriaBuilder builder1 = em.getCriteriaBuilder();
        CriteriaQuery<ConfigurationDetails> cq1 = builder1.createQuery(ConfigurationDetails.class);
        Root<QualityMeasuresProviderMapping> root1 = cq1.from(QualityMeasuresProviderMapping.class);
        Predicate byProvider=builder1.equal(root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingProviderId),providerId);
        Predicate byYear=builder1.equal(root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingReportingYear),year);
        Predicate byMeasureId=builder1.like(root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingMeasureId),"IA_%");
        Selection[] selections= new Selection[] {
        root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingMeasureId),
        root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingTitle),
        root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingPriority)
        };
        cq1.select(builder1.construct(ConfigurationDetails.class,selections));
		cq1.where(byProvider,byYear,byMeasureId);
		List<ConfigurationDetails> result=em.createQuery(cq1).getResultList();
		return result;
	}
	
	@Override
	public void addImpMeasuresToProvider(List<getMeasureBean> requestBean)
			throws Exception {
		
		CriteriaBuilder cb1 = em.getCriteriaBuilder();
		CriteriaDelete<QualityMeasuresProviderMapping> delete1 = cb1.createCriteriaDelete(QualityMeasuresProviderMapping.class);
		Root<QualityMeasuresProviderMapping> rootCriteria1 = delete1.from(QualityMeasuresProviderMapping.class);
		delete1.where(cb1.equal(rootCriteria1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingProviderId),requestBean.get(0).getProviderId()),cb1.equal(rootCriteria1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingReportingYear),requestBean.get(0).getYear()),cb1.like(rootCriteria1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingMeasureId), "IA_%"));
		this.em.createQuery(delete1).executeUpdate();
		for(int i=0;i<requestBean.size();i++){
		QualityMeasuresProviderMapping qmpmObj=new QualityMeasuresProviderMapping();
		qmpmObj.setQualityMeasuresProviderMappingProviderId(requestBean.get(i).getProviderId());
		qmpmObj.setQualityMeasuresProviderMappingMeasureId(requestBean.get(i).getMeasureIds());
		qmpmObj.setQualityMeasuresProviderMappingTitle(requestBean.get(i).getMeasureNames());
		qmpmObj.setQualityMeasuresProviderMappingPriority(requestBean.get(i).getPriority());
		qmpmObj.setQualityMeasuresProviderMappingReportingYear(requestBean.get(i).getYear());
		qualityMeasuresProviderMappingRepository.saveAndFlush(qmpmObj);
		}
	
	}
	
	@Override
	public List<ConfigurationDetails> getConfiguredIameasures(Integer providerId, Integer year)
	{
		CriteriaBuilder builder1 = em.getCriteriaBuilder();
        CriteriaQuery<ConfigurationDetails> cq1 = builder1.createQuery(ConfigurationDetails.class);
        Root<QualityMeasuresProviderMapping> root1 = cq1.from(QualityMeasuresProviderMapping.class);
        Join< QualityMeasuresProviderMapping,IAMeasures> joinIAMeasures = root1.join(QualityMeasuresProviderMapping_.iaMeasures,JoinType.LEFT);
        Predicate byProvider=builder1.equal(root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingProviderId),providerId);
        Predicate byYear = builder1.equal(root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingReportingYear), year);
        Predicate byIaYear=builder1.equal(joinIAMeasures.get(IAMeasures_.IaMeasuresReportingYear),year);
        Predicate byMeasureId=builder1.like(root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingMeasureId),"IA_%");
       
        joinIAMeasures.on(byIaYear);
        
        Selection[] selections= new Selection[] {
        root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingMeasureId),
        root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingTitle),
        root1.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingPriority),
        builder1.coalesce(joinIAMeasures.get(IAMeasures_.IaMeasuresStatus),false),
        builder1.coalesce(joinIAMeasures.get(IAMeasures_.IaMeasuresPoints),0),
        };
        cq1.select(builder1.construct(ConfigurationDetails.class,selections));
		cq1.where(byProvider,byYear,byMeasureId);
		List<ConfigurationDetails> result=em.createQuery(cq1).getResultList();

		return result;
	}
	
	@Override
	public void addIAmeasures(List<IAMeasureBean> requestBean){
		Date date = new Date();
		Timestamp curr_time = new Timestamp(date.getTime());
		for(int i=0;i<requestBean.size();i++){
			String iaMeasure =	IAMeasureExist(requestBean.get(i).getMeasureIds(), requestBean.get(i).getProviderId(), requestBean.get(i).getYear());
			if(iaMeasure.equals("No Measure")){
				IAMeasures iameasuresObj = new IAMeasures();
				iameasuresObj.setIaMeasuresStatus(requestBean.get(i).getMeasureStatus());
				iameasuresObj.setIaMeasuresMeasureId(requestBean.get(i).getMeasureIds());
				iameasuresObj.setIaMeasuresLastModified(curr_time);
				iameasuresObj.setIaMeasuresReportingYear(requestBean.get(i).getYear());
				if(requestBean.get(i).getMeasureStatus()==true )
					iameasuresObj.setIaMeasuresPoints(10);
				else
					iameasuresObj.setIaMeasuresPoints(0);
				iameasuresObj.setIaMeasuresProviderId(requestBean.get(i).getProviderId());
				iaMeasuresRepository.saveAndFlush(iameasuresObj);

			}else{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaUpdate<IAMeasures> update = cb.createCriteriaUpdate(IAMeasures.class);
				Root<IAMeasures> rootCriteria = update.from(IAMeasures.class);
				Predicate ByProviderId=cb.equal(rootCriteria.get(IAMeasures_.IaMeasuresProviderId),requestBean.get(i).getProviderId());
				Predicate ByYear= cb.equal(rootCriteria.get(IAMeasures_.IaMeasuresReportingYear),requestBean.get(i).getYear());
				Predicate byMeasureId=cb.equal(rootCriteria.get(IAMeasures_.IaMeasuresMeasureId),requestBean.get(i).getMeasureIds());
				update.set(rootCriteria.get(IAMeasures_.IaMeasuresStatus), requestBean.get(i).getMeasureStatus());
				update.set(rootCriteria.get(IAMeasures_.IaMeasuresMeasureId), requestBean.get(i).getMeasureIds());
				update.set(rootCriteria.get(IAMeasures_.IaMeasuresLastModified),curr_time);
				if(requestBean.get(i).getMeasureStatus()==true )
					update.set(rootCriteria.get(IAMeasures_.IaMeasuresPoints), 10);
				else 
					update.set(rootCriteria.get(IAMeasures_.IaMeasuresPoints), 0);
				update.where(ByProviderId,ByYear,byMeasureId);
				this.em.createQuery(update).executeUpdate();
			}
		}
		
		
	}
	
	private String IAMeasureExist(String measureId,Integer providerId,Integer reportingYear)
	{
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<String> cq =builder.createQuery(String.class);
		Root<IAMeasures> rootIAMeasures = cq.from(IAMeasures.class);
		cq.select(rootIAMeasures.get(IAMeasures_.IaMeasuresMeasureId));
		Predicate ByProviderId=builder.equal(rootIAMeasures.get(IAMeasures_.IaMeasuresProviderId),providerId);
		Predicate ByYear= builder.equal(rootIAMeasures.get(IAMeasures_.IaMeasuresReportingYear),reportingYear);
		Predicate byMeasureId=builder.equal(rootIAMeasures.get(IAMeasures_.IaMeasuresMeasureId),measureId);
		cq.where(ByProviderId,ByYear,byMeasureId);
		List<String> iaMeasureName=em.createQuery(cq).getResultList();
		if(iaMeasureName.size()!=0)
			return iaMeasureName.get(0);
		else
			return "No Measure";
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
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

	@SuppressWarnings({ "rawtypes" })
	@Override
	public List<MacraProviderQDM> getCompleteProviderInfo(
			Integer providerId ,Integer year) throws Exception {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<MacraProviderQDM> cq = builder.createQuery(MacraProviderQDM.class);
		Root<MacraProviderConfiguration> rootMacraProviderConfiguration = cq.from(MacraProviderConfiguration.class);
		Predicate predicateByYear =builder.equal(rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingYear),year);
		Predicate predicateByProviderId =builder.equal(rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId),providerId);
		Selection[] selections= new Selection[] {
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId).alias("Provider"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingYear).alias("Year"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingStart).alias("Start Date"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingEnd).alias("End Date"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingMethod).alias("Rep method")
		};
		cq.multiselect(selections);
		cq.where(predicateByYear,predicateByProviderId);
		cq.groupBy(rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingYear),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingStart),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingEnd),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingMethod)
				);
		List<MacraProviderQDM> providerInfo=em.createQuery(cq).getResultList();
		
		CriteriaBuilder builder1 = em.getCriteriaBuilder();
		CriteriaQuery<String> cq1 = builder1.createQuery(String.class);
		Root<QualityMeasuresProviderMapping> rootQualityMeasuresProviderMapping = cq1.from(QualityMeasuresProviderMapping.class);
		Predicate predicateByYear1 =builder1.equal(rootQualityMeasuresProviderMapping.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingReportingYear),year);
		Predicate predicateByProviderId1 =builder1.equal(rootQualityMeasuresProviderMapping.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingProviderId),providerId);
		cq1.select(builder1.function("string_agg", String.class, builder1.notLike(rootQualityMeasuresProviderMapping.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingMeasureId),"IA_%"),builder1.literal(",")));
		cq1.where(predicateByYear1,predicateByProviderId1);
		List<String> measures=em.createQuery(cq1).getResultList();
		if(measures.size()>0 && providerInfo.size()>0)
		providerInfo.get(0).setMeasures(measures.get(0));
		
		return providerInfo;
		
	}
	
	@Override
	public String getCompleteTinInfo(String empTin, int reportingYear){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = builder.createQuery(String.class);
		Root<QualityMeasuresProviderMapping> root = cq.from(QualityMeasuresProviderMapping.class);
		
		Join<QualityMeasuresProviderMapping,EmployeeProfile> joinProviderEmployee = root.join(QualityMeasuresProviderMapping_.empProfile, JoinType.INNER);

		Predicate bySSN = builder.equal(joinProviderEmployee.get(EmployeeProfile_.empProfileSsn), empTin);
		Predicate byReportingYear = builder.equal(root.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingReportingYear), reportingYear);

		cq.where(builder.and(bySSN, byReportingYear));
		
		cq.groupBy(joinProviderEmployee.get(EmployeeProfile_.empProfileSsn));
		
		cq.multiselect(builder.function("string_agg",String.class, builder.notLike(root.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingMeasureId),"IA_%"),builder.literal(","))).distinct(true);
	
		List<String> providerInfo=em.createQuery(cq).getResultList();
		
		if(providerInfo.size() > 0){
			return providerInfo.get(0).toString();
		}
		
		return "";
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<MacraProviderQDM> getProviderReportingInfo(Integer reportingYear){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<MacraProviderQDM> cq = builder.createQuery(MacraProviderQDM.class);
		Root<MacraProviderConfiguration> rootMacraProviderConfiguration = cq.from(MacraProviderConfiguration.class);
				
		Selection[] selections= new Selection[] {
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId).alias("Provider"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingYear).alias("Year"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingStart).alias("Start Date"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingEnd).alias("End Date"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingMethod).alias("Rep method"),
		};
		
		cq.multiselect(selections);
		
		cq.where(builder.notEqual(rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId), -1),builder.equal(rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingYear),reportingYear));
		
		cq.groupBy(rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingYear),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingStart),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingEnd),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingMethod)
				);
		
		List<MacraProviderQDM> providerInfo=em.createQuery(cq).getResultList();
		
		CriteriaBuilder builder1 = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq1 = builder1.createQuery(Object[].class);
		Root<QualityMeasuresProviderMapping> rootQualityMeasuresProviderMapping = cq1.from(QualityMeasuresProviderMapping.class);
		Predicate predicateByYear1 =builder1.equal(rootQualityMeasuresProviderMapping.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingReportingYear),reportingYear);
		Predicate predicateByProviderId1 =builder1.notEqual(rootQualityMeasuresProviderMapping.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingProviderId),-1);
		cq1.multiselect(rootQualityMeasuresProviderMapping.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingProviderId),builder1.function("string_agg", String.class, builder1.notLike(rootQualityMeasuresProviderMapping.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingMeasureId),"IA_%"),builder1.literal(",")));
		cq1.where(predicateByYear1,predicateByProviderId1);
		cq1.groupBy(rootQualityMeasuresProviderMapping.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingProviderId)
			);

		List<Object[]> measures=em.createQuery(cq1).getResultList();
		
		for(int i=0;i<providerInfo.size();i++)
		{
			for(int j=0;j<measures.size();j++)
			{
				if(providerInfo.get(i).getMacraProviderConfigurationProviderId().equals(measures.get(j)[0]))
					providerInfo.get(i).setMeasures(measures.get(j)[1].toString());
			}
		}
		return providerInfo;
		
	}
	
	@Override
	public HashMap<String,Object> getFilterDetails() throws Exception {

		HashMap<String,Object> raceDetails=new HashMap<String,Object>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<Billinglookup> rootBillinglookup = cq.from(Billinglookup.class);
		cq.multiselect(rootBillinglookup.get(Billinglookup_.blookcodevalue),rootBillinglookup.get(Billinglookup_.blookName));
		cq.where(builder.equal(rootBillinglookup.get(Billinglookup_.blookGroup), 755),builder.equal(rootBillinglookup.get(Billinglookup_.billinglookupKeycode),builder.literal("1002-5")));
		List<Object> result=em.createQuery(cq).getResultList();
		raceDetails.put("AmericanIndianOrAlaskaNative", result);
		
		CriteriaBuilder builder1 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq1 = builder1.createQuery();
		Root<Billinglookup> rootBillinglookup1 = cq1.from(Billinglookup.class);
		cq1.multiselect(rootBillinglookup1.get(Billinglookup_.blookcodevalue),rootBillinglookup1.get(Billinglookup_.blookName));
		cq1.where(builder1.equal(rootBillinglookup1.get(Billinglookup_.blookGroup), 755),builder1.equal(rootBillinglookup1.get(Billinglookup_.billinglookupKeycode),builder1.literal("2076-8")));
		List<Object> result1=em.createQuery(cq1).getResultList();
		raceDetails.put("NativeHawaiianOrOtherPacificIslander", result1);
		
		CriteriaBuilder builder2 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq2 = builder2.createQuery();
		Root<Billinglookup> rootBillinglookup2 = cq2.from(Billinglookup.class);
		cq2.multiselect(rootBillinglookup2.get(Billinglookup_.blookcodevalue),rootBillinglookup2.get(Billinglookup_.blookName));
		cq2.where(builder2.equal(rootBillinglookup2.get(Billinglookup_.blookGroup), 755),builder2.equal(rootBillinglookup2.get(Billinglookup_.billinglookupKeycode),builder2.literal("2028-9")));
		List<Object> result2=em.createQuery(cq2).getResultList();
		raceDetails.put("Asian", result2);
		
		CriteriaBuilder builder3 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq3 = builder3.createQuery();
		Root<Billinglookup> rootBillinglookup3 = cq3.from(Billinglookup.class);
		cq3.multiselect(rootBillinglookup3.get(Billinglookup_.blookcodevalue),rootBillinglookup3.get(Billinglookup_.blookName));
		cq3.where(builder3.equal(rootBillinglookup3.get(Billinglookup_.blookGroup), 755),builder3.equal(rootBillinglookup3.get(Billinglookup_.billinglookupKeycode),builder3.literal("2054-5")));
		List<Object> result3=em.createQuery(cq3).getResultList();
		raceDetails.put("BlackOrAfricanAmerican", result3);
		
		CriteriaBuilder builder4 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq4 = builder4.createQuery();
		Root<Billinglookup> rootBillinglookup4 = cq4.from(Billinglookup.class);
		cq4.multiselect(rootBillinglookup4.get(Billinglookup_.blookcodevalue),rootBillinglookup4.get(Billinglookup_.blookName));
		cq4.where(builder4.equal(rootBillinglookup4.get(Billinglookup_.blookGroup), 755),builder4.equal(rootBillinglookup4.get(Billinglookup_.billinglookupKeycode),builder4.literal("2106-3")));
		List<Object> result4=em.createQuery(cq4).getResultList();
		raceDetails.put("White", result4);
		
		CriteriaBuilder builder5 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq5 = builder5.createQuery();
		Root<Billinglookup> rootBillinglookup5 = cq5.from(Billinglookup.class);
		cq5.multiselect(rootBillinglookup5.get(Billinglookup_.blookcodevalue),rootBillinglookup5.get(Billinglookup_.blookName));
		cq5.where(builder5.equal(rootBillinglookup5.get(Billinglookup_.blookGroup), 756),builder5.equal(rootBillinglookup5.get(Billinglookup_.billinglookupKeycode),builder5.literal("2135-2")));
		List<Object> result5=em.createQuery(cq5).getResultList();
		raceDetails.put("HispanicOrLatino", result5);
		
		CriteriaBuilder builder6 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq6 = builder6.createQuery();
		Root<Billinglookup> rootBillinglookup6 = cq6.from(Billinglookup.class);
		cq6.multiselect(rootBillinglookup6.get(Billinglookup_.blookcodevalue),rootBillinglookup6.get(Billinglookup_.blookName));
		cq6.where(builder6.equal(rootBillinglookup6.get(Billinglookup_.blookGroup), 756),builder6.equal(rootBillinglookup6.get(Billinglookup_.billinglookupKeycode),builder6.literal("2186-5")));
		List<Object> result6=em.createQuery(cq6).getResultList();
		raceDetails.put("NotHispanicOrLatino", result6);
		
		CriteriaBuilder builder7 = em.getCriteriaBuilder();
		CriteriaQuery<String> cq7 = builder.createQuery(String.class);
		Root<PosTable> rootPosTable = cq7.from(PosTable.class);
		Join<PosTable,PosType> joinPosTablePosType=rootPosTable.join(PosTable_.posType,JoinType.INNER);
		cq7.multiselect(joinPosTablePosType.get(PosType_.posTypeTypeName));
		cq7.where(builder7.equal(rootPosTable.get(PosTable_.posTableIsActive),true),
				builder7.notEqual(rootPosTable.get(PosTable_.posTablePosCode),40));
		cq7.distinct(true);
		List<String> result7=em.createQuery(cq7).getResultList();
		
		CriteriaBuilder builder8 = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq8 = builder.createQuery(Object[].class);
		Root<PosTable> rootPosTable1 = cq8.from(PosTable.class);
		Join<PosTable,PosType> joinPosTablePosType1=rootPosTable1.join(PosTable_.posType,JoinType.INNER);
		cq8.multiselect(joinPosTablePosType1.get(PosType_.posTypeTypeName),rootPosTable1.get(PosTable_.posTableFacilityComments),rootPosTable1.get(PosTable_.posTableRelationId));
		cq8.where(builder8.equal(rootPosTable1.get(PosTable_.posTableIsActive),true),
				builder8.notEqual(rootPosTable1.get(PosTable_.posTablePosCode),40));
		cq8.orderBy(builder8.asc(joinPosTablePosType1.get(PosType_.posTypeTypeId)));
		List<Object[]> result8=em.createQuery(cq8).getResultList();
		HashMap<Object, Object> posDetails=new HashMap<Object, Object>();
		
		for(int name=0;name<result7.size();name++)
		{
			String posType=result7.get(name);
			ArrayList<String> temp=new ArrayList<String>();
			for(int i=0;i<result8.size();i++){
				if(posType.equals(result8.get(i)[0].toString())){
					temp.add(result8.get(i)[1].toString()+"&&&"+result8.get(i)[2].toString());
				}
			}
			posDetails.put(posType, temp);
		}
		raceDetails.put("POS", posDetails);
		
		
		CriteriaBuilder builder9 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq9= builder.createQuery();
		Root<BillingConfigTable> rootBillingConfigTable = cq9.from(BillingConfigTable.class);
		cq9.multiselect(rootBillingConfigTable.get(BillingConfigTable_.billingConfigTableConfigId),rootBillingConfigTable.get(BillingConfigTable_.billingConfigTableLookupDesc));
		cq9.where(builder9.equal(rootBillingConfigTable.get(BillingConfigTable_.billingConfigTableLookupId), 5004),
				builder9.equal(rootBillingConfigTable.get(BillingConfigTable_.billingConfigTableIsActive), true));
		List<Object> result9 = em.createQuery(cq9).getResultList();	
		raceDetails.put("PatientInsurance", result9);

		return raceDetails;
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<MIPSPatientInformation> getFilteredDetails(String patientId,Integer ageFrom,Integer ageTo,Integer ageCriteria,String raceCode,String ethnicityCode,String gender,Integer insCompanyId,String currMeasureId,String dxCodes,int posId,int insId) throws Exception {
		List<Object> raceResult=new ArrayList<Object>();
		List<Object> ethnicityResult=new ArrayList<Object>();
		String raceResultCodes="";
		String ethnicityResultCodes="";
		raceResultCodes+="00000,";
		ethnicityResultCodes+="00000,";
		if(!raceCode.equals("-1"))
		{
		CriteriaBuilder builder1 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq1 = builder1.createQuery();
		Root<Billinglookup> rootBillinglookup = cq1.from(Billinglookup.class);
		cq1.select(rootBillinglookup.get(Billinglookup_.blookIntid));
		Predicate[] raceRestrictions=new Predicate[]{
				rootBillinglookup.get(Billinglookup_.blookcodevalue).in(Arrays.asList(raceCode.split(","))),
				builder1.equal(rootBillinglookup.get(Billinglookup_.blookGroup), 250)
		};
		cq1.where(raceRestrictions);
		raceResult=em.createQuery(cq1).getResultList();
		for(int i=0;i<raceResult.size();i++){
			raceResultCodes+=raceResult.get(i).toString();
			raceResultCodes+=",";
		}
		}
		raceResultCodes=raceResultCodes.substring(0,raceResultCodes.length()-1);
		if(!ethnicityCode.equals("-1")){
		CriteriaBuilder builder2 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq2 = builder2.createQuery();
		Root<Billinglookup> rootBillinglookup1 = cq2.from(Billinglookup.class);
		cq2.select(rootBillinglookup1.get(Billinglookup_.blookIntid));
		Predicate[] ethnicityRestrictions=new Predicate[]{
				rootBillinglookup1.get(Billinglookup_.blookcodevalue).in(Arrays.asList(ethnicityCode.split(","))),
				builder2.equal(rootBillinglookup1.get(Billinglookup_.blookGroup), 251)
		};
		cq2.where(ethnicityRestrictions);
		ethnicityResult=em.createQuery(cq2).getResultList();
		for(int i=0;i<ethnicityResult.size();i++){
			ethnicityResultCodes+=ethnicityResult.get(i).toString();
			ethnicityResultCodes+=",";
		}
		ethnicityResultCodes=ethnicityResultCodes.substring(0,ethnicityResultCodes.length()-1);
		}
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<MIPSPatientInformation> cq = builder.createQuery(MIPSPatientInformation.class);
		Root<PatientRegistration> rootPatientRegistration = cq.from(PatientRegistration.class);
		Join<PatientRegistration,QualityMeasuresPatientEntries> joinQualityMeasuresPatientEntries=rootPatientRegistration.join(PatientRegistration_.qualityMeasuresPatientEntries,JoinType.INNER);
		Predicate byMeasureId=builder.equal(joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesMeasureId), currMeasureId);
		joinQualityMeasuresPatientEntries.on(byMeasureId);
		
		Join<PatientRegistration,ProblemList> joinProblemList = null;
		if(!dxCodes.equals("-1")){
		joinProblemList=rootPatientRegistration.join(PatientRegistration_.problemList,JoinType.INNER);
		}
		
		Join<PatientRegistration,Chart> joinChartPatientEntries;
		Join<Chart,Encounter> encounterChartJoin = null;
		if(posId!=-1){
		joinChartPatientEntries=rootPatientRegistration.join(PatientRegistration_.chartIds,JoinType.INNER);
		encounterChartJoin = joinChartPatientEntries.join(Chart_.encounterTable,JoinType.INNER);
		}
		Join<PatientRegistration,PatientInsDetail> joinPatientInsDetail = null;
		if(insId!=-1){
		joinPatientInsDetail = rootPatientRegistration.join(PatientRegistration_.patientInsuranceTable,JoinType.INNER);
		}
		Selection[] selections= new Selection[] {
				rootPatientRegistration.get(PatientRegistration_.patientRegistrationId),
				rootPatientRegistration.get(PatientRegistration_.patientRegistrationAccountno),
				rootPatientRegistration.get(PatientRegistration_.patientRegistrationLastName),
				rootPatientRegistration.get(PatientRegistration_.patientRegistrationFirstName),
				builder.function("to_mmddyyyy",Date.class,rootPatientRegistration.get(PatientRegistration_.patientRegistrationDob)),
				builder.selectCase().when(builder.equal(rootPatientRegistration.get(PatientRegistration_.patientRegistrationSex),1),"Male").when(builder.equal(rootPatientRegistration.get(PatientRegistration_.patientRegistrationSex),2),"Female").otherwise("TG").as(String.class),
				rootPatientRegistration.get(PatientRegistration_.patientRegistrationRace),
				rootPatientRegistration.get(PatientRegistration_.patientRegistrationEthnicity),
				builder.coalesce(rootPatientRegistration.get(PatientRegistration_.patientRegistrationAddress1), ""),
				builder.coalesce(rootPatientRegistration.get(PatientRegistration_.patientRegistrationAddress2), ""),
				builder.coalesce(rootPatientRegistration.get(PatientRegistration_.patientRegistrationCity), "-"),
				builder.coalesce(rootPatientRegistration.get(PatientRegistration_.patientRegistrationStateName), "-"),
				builder.coalesce(rootPatientRegistration.get(PatientRegistration_.patientRegistrationZip), "-"),
				builder.coalesce(rootPatientRegistration.get(PatientRegistration_.patientRegistrationPhoneNo),""),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesIpp),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominator),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominatorExclusion),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesNumerator),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesNumeratorExclusion),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominatorException)
		};
		cq.multiselect(selections);
		List<Predicate> predicates = new ArrayList<>();
		//Predicate byAge;
		if(!ageFrom.equals(-1) && !ageTo.equals(-1) && ageCriteria.equals(1)){
			predicates.add(builder.between(builder.function("date_part",Integer.class,builder.literal("year"),builder.function("age", String.class, builder.literal(new Date()),rootPatientRegistration.get(PatientRegistration_.patientRegistrationDob))),ageFrom,ageTo));	
		}
		else if(!ageFrom.equals(-1) && ageCriteria.equals(2)){
			predicates.add(builder.greaterThan(builder.function("date_part",Integer.class,builder.literal("year"),builder.function("age", String.class, builder.literal(new Date()),rootPatientRegistration.get(PatientRegistration_.patientRegistrationDob))),ageFrom));
		}
		else if(!ageFrom.equals(-1) && ageCriteria.equals(3)){
			predicates.add(builder.lessThan(builder.function("date_part",Integer.class,builder.literal("year"),builder.function("age", String.class, builder.literal(new Date()),rootPatientRegistration.get(PatientRegistration_.patientRegistrationDob))),ageFrom));
		}
		else if(!ageFrom.equals(-1) && ageCriteria.equals(4)){
			predicates.add(builder.equal(builder.function("date_part",Integer.class,builder.literal("year"),builder.function("age", String.class, builder.literal(new Date()),rootPatientRegistration.get(PatientRegistration_.patientRegistrationDob))),ageFrom));
		}
		//Predicate byGender;
		if(!gender.equals("-1")){
			predicates.add(rootPatientRegistration.get(PatientRegistration_.patientRegistrationSex).in(Arrays.asList(gender.split(","))));
		}
		
		//Predicate byAge=builder.between(rootPatientRegistration.get(PatientRegistration_.), x, y)
		predicates.add(rootPatientRegistration.get(PatientRegistration_.patientRegistrationId).in(Arrays.asList(patientId.split(","))));
		Predicate byRaceEthnicity;
		if(!raceCode.equals("-1") && !ethnicityCode.equals("-1")){
			byRaceEthnicity=builder.and(builder.or(rootPatientRegistration.get(PatientRegistration_.patientRegistrationGranularRaceCode).in(Arrays.asList(raceCode.split(","))),rootPatientRegistration.get(PatientRegistration_.patientRegistrationRace).in(Arrays.asList(raceResultCodes.split(",")))),builder.or(rootPatientRegistration.get(PatientRegistration_.patientRegistrationGranularEthnicityCode).in(Arrays.asList(ethnicityCode.split(","))),rootPatientRegistration.get(PatientRegistration_.patientRegistrationEthnicity).in(Arrays.asList(ethnicityResultCodes.split(",")))));
			predicates.add(byRaceEthnicity);
		}
		else if(!raceCode.equals("-1") && ethnicityCode.equals("-1")){
			byRaceEthnicity=builder.or(rootPatientRegistration.get(PatientRegistration_.patientRegistrationGranularRaceCode).in(Arrays.asList(raceCode.split(","))),rootPatientRegistration.get(PatientRegistration_.patientRegistrationRace).in(Arrays.asList(raceResultCodes.split(","))));
			predicates.add(byRaceEthnicity);
		}
		else if(raceCode.equals("-1") && !ethnicityCode.equals("-1")){
			byRaceEthnicity=builder.or(rootPatientRegistration.get(PatientRegistration_.patientRegistrationGranularEthnicityCode).in(Arrays.asList(ethnicityCode.split(","))),rootPatientRegistration.get(PatientRegistration_.patientRegistrationEthnicity).in(Arrays.asList(ethnicityResultCodes.split(","))));
			predicates.add(byRaceEthnicity);
		}
		
		if(!dxCodes.equals("-1")){
			predicates.add(joinProblemList.get(ProblemList_.problemListDxCode).in(Arrays.asList(dxCodes.split(","))));
			predicates.add(builder.equal(joinProblemList.get(ProblemList_.problemListIsactive),true));
			predicates.add(builder.equal(joinProblemList.get(ProblemList_.problemListIsresolved),false));
		}
		if(posId!=-1)
			predicates.add(builder.equal(encounterChartJoin.get(Encounter_.encounterPos), posId));
		if(insId!=-1)	
			predicates.add(builder.equal(joinPatientInsDetail.get(PatientInsDetail_.patientInsDetailPlantype), insId));
		
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		cq.distinct(true);
		List<MIPSPatientInformation> patientsList=em.createQuery(cq).getResultList();
		
		return patientsList;
	}

	@Override
	public DiagnosisList getDXList(String measureId,String sharedPath)throws Exception 
	{
		
		DiagnosisList dxCodesForMeasure = new DiagnosisList();
		HashMap<String, HashMap<String, List<Object>>> diagnosisList = new HashMap<String, HashMap<String,List<Object>>>();
		HashMap<String, List<Object>> diagnosisCodeList;
		
		EMeasureUtils utils = new EMeasureUtils();
		List<EMeasure> emeasure = utils.getMeasureBeanDetails(2017,measureId, sharedPath,"-1");
		CQMSpecification specification = emeasure.get(0).getSpecification();
		HashMap<String, Category> qdmCatagory = specification.getQdmCategory();
		
		List<Object> icd9=new ArrayList<Object>();
		List<Object> icd10=new ArrayList<Object>();
		List<Object> snomed=new ArrayList<Object>();
		
		if(qdmCatagory.containsKey("Condition/Diagnosis/Problem")){
			
			Category diagnosisCategory = qdmCatagory.get("Condition/Diagnosis/Problem");
			
			List<Valueset> valueSet = diagnosisCategory.getValueSet();
			
			for(int i=0;i<valueSet.size();i++)
			{

				diagnosisCodeList = new HashMap<String, List<Object>>();
				
				icd9=new ArrayList<Object>();
				icd10=new ArrayList<Object>();
				snomed=new ArrayList<Object>();
				
				List<CodeSet> codeSetList=valueSet.get(i).getCodeSetList();

				for(int j=0;j<codeSetList.size();j++)
				{

					if(codeSetList.get(j).getCodeSystem().contains("ICD10CM")){
						List<Code> codeList=codeSetList.get(j).getCodeList();
						for(int k=0;k<codeList.size();k++)
							icd10.add(codeList.get(k));

					}
					if(codeSetList.get(j).getCodeSystem().contains("ICD9CM")){
						List<Code> codeList=codeSetList.get(j).getCodeList();
						for(int k=0;k<codeList.size();k++)
							icd9.add(codeList.get(k));
					}
					if(codeSetList.get(j).getCodeSystem().contains("SNOMEDCT")){
						List<Code> codeList=codeSetList.get(j).getCodeList();
						for(int k=0;k<codeList.size();k++)
							snomed.add(codeList.get(k));
					}
				}
				
				diagnosisCodeList.put("ICD10CM", icd10);
				diagnosisCodeList.put("ICD9CM", icd9);
				diagnosisCodeList.put("SNOMEDCT", snomed);
				
				diagnosisList.put(valueSet.get(i).getName(), diagnosisCodeList);

			}
			
			dxCodesForMeasure.setDiagnosisList(diagnosisList);
			
		}
		
		return dxCodesForMeasure;
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<MIPSPatientInformation> getPatientBasedOnDX(String patientId,String dxCodes) throws Exception {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<MIPSPatientInformation> cq = builder.createQuery(MIPSPatientInformation.class);
		Root<PatientRegistration> rootPatientRegistration = cq.from(PatientRegistration.class);
		Join<PatientRegistration,ProblemList> joinProblemList=rootPatientRegistration.join(PatientRegistration_.problemList,JoinType.INNER);
		Join<PatientRegistration,QualityMeasuresPatientEntries> joinQualityMeasuresPatientEntries=rootPatientRegistration.join(PatientRegistration_.qualityMeasuresPatientEntries,JoinType.INNER);
		Predicate[] restrictions=new Predicate[]{
				rootPatientRegistration.get(PatientRegistration_.patientRegistrationId).in(Arrays.asList(patientId.split(","))),
				builder.equal(joinProblemList.get(ProblemList_.problemListIsactive),false),
				builder.equal(joinProblemList.get(ProblemList_.problemListIsresolved),false),
				joinProblemList.get(ProblemList_.problemListDxCode).in(Arrays.asList(dxCodes.split(",")))
		};
		
		Selection[] selections= new Selection[] {
				rootPatientRegistration.get(PatientRegistration_.patientRegistrationId),
				rootPatientRegistration.get(PatientRegistration_.patientRegistrationAccountno),
				rootPatientRegistration.get(PatientRegistration_.patientRegistrationLastName),
				rootPatientRegistration.get(PatientRegistration_.patientRegistrationFirstName),
				builder.function("to_mmddyyyy",Date.class,rootPatientRegistration.get(PatientRegistration_.patientRegistrationDob)),
				builder.selectCase().when(builder.equal(rootPatientRegistration.get(PatientRegistration_.patientRegistrationSex),1),"Male").when(builder.equal(rootPatientRegistration.get(PatientRegistration_.patientRegistrationSex),2),"Female").otherwise("TG").as(String.class),
				rootPatientRegistration.get(PatientRegistration_.patientRegistrationRace),
				rootPatientRegistration.get(PatientRegistration_.patientRegistrationEthnicity),
				builder.coalesce(rootPatientRegistration.get(PatientRegistration_.patientRegistrationAddress1), ""),
				builder.coalesce(rootPatientRegistration.get(PatientRegistration_.patientRegistrationAddress2), ""),
				builder.coalesce(rootPatientRegistration.get(PatientRegistration_.patientRegistrationCity), "-"),
				builder.coalesce(rootPatientRegistration.get(PatientRegistration_.patientRegistrationStateName), "-"),
				builder.coalesce(rootPatientRegistration.get(PatientRegistration_.patientRegistrationZip), "-"),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesIpp),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominator),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominatorExclusion),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesNumerator),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesNumeratorExclusion),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominatorException)
		};
		cq.multiselect(selections);
		cq.where(restrictions);
		cq.distinct(true);
		List<MIPSPatientInformation> patientsList=em.createQuery(cq).getResultList();
		return patientsList;
	}

	private String getCMSIdAndTitle(String measureId, String accountId){

		String result = "";

		try{

			String sharedFolderPath = sharedFolderBean.getSharedFolderPath().get(accountId).toString();

			String jsonFolder = sharedFolderPath+File.separator+"ECQM"+File.separator;

			File file = new File(jsonFolder+measureId+".json");

			String apiUrl = "http://hub-icd10.glaceemr.com/DataGateway/eCQMServices/getECQMInfoById?ids="+measureId;

			if(file.exists()){

				String jsonContent = FileUtils.readFileToString(file);

				JSONArray jsonArray = new JSONArray(jsonContent);

				result = jsonArray.getJSONObject(0).getString("cmsId");
				result = result.concat("&&&".concat(jsonArray.getJSONObject(0).getString("title")));

			}else{

				JSONArray measureDetailsArray=new JSONArray(restTemplate.getForObject(apiUrl, String.class));

				for(int i=0;i<measureDetailsArray.length();i++){

					result = measureDetailsArray.getJSONObject(i).getString("cmsId");
					result = result.concat("&&&".concat(measureDetailsArray.getJSONObject(i).getString("title")));

				}

			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return result;

	}

	

}