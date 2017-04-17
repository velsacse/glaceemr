package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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

import com.glenwood.glaceemr.server.application.Bean.DiagnosisList;
import com.glenwood.glaceemr.server.application.Bean.MIPSPatientInformation;
import com.glenwood.glaceemr.server.application.Bean.MacraProviderQDM;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.CQMSpecification;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.Category;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.Code;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.CodeSet;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.EMeasure;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.EMeasureUtils;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.Valueset;
import com.glenwood.glaceemr.server.application.models.Billinglookup;
import com.glenwood.glaceemr.server.application.models.Billinglookup_;
import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.InsCompAddr;
import com.glenwood.glaceemr.server.application.models.InsCompAddr_;
import com.glenwood.glaceemr.server.application.models.InsCompany;
import com.glenwood.glaceemr.server.application.models.InsCompany_;
import com.glenwood.glaceemr.server.application.models.LabEntriesParameter;
import com.glenwood.glaceemr.server.application.models.LabEntriesParameter_;
import com.glenwood.glaceemr.server.application.models.LabParameterCode;
import com.glenwood.glaceemr.server.application.models.LabParameterCode_;
import com.glenwood.glaceemr.server.application.models.LabParameters;
import com.glenwood.glaceemr.server.application.models.LabParameters_;
import com.glenwood.glaceemr.server.application.models.MacraConfiguration;
import com.glenwood.glaceemr.server.application.models.MacraProviderConfiguration;
import com.glenwood.glaceemr.server.application.models.MacraProviderConfiguration_;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.ProblemList;
import com.glenwood.glaceemr.server.application.models.ProblemList_;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresPatientEntries;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresPatientEntries_;
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
	public void addMeasuresToProvider(String measureIds, Integer providerId,Integer prgmYear) {
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
		qmpmObj.setQualityMeasuresProviderMappingReportingYear(prgmYear);
		qualityMeasuresProviderMappingRepository.saveAndFlush(qmpmObj);
		}
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
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<MacraProviderQDM> getProviderReportingInfo(Integer reportingYear){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<MacraProviderQDM> cq = builder.createQuery(MacraProviderQDM.class);
		Root<MacraProviderConfiguration> rootMacraProviderConfiguration = cq.from(MacraProviderConfiguration.class);
		Join<MacraProviderConfiguration,QualityMeasuresProviderMapping> joinProviderConfigurationProviderMapping=rootMacraProviderConfiguration.join(MacraProviderConfiguration_.qualityMeasuresProviderMappingTable,JoinType.INNER);
		Predicate predicateByReportingYear =builder.equal(rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingYear),reportingYear);
		joinProviderConfigurationProviderMapping.on(predicateByReportingYear);
		
		Selection[] selections= new Selection[] {
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId).alias("Provider"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingYear).alias("Year"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingStart).alias("Start Date"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingEnd).alias("End Date"),
				rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingMethod).alias("Rep method"),
				builder.function("string_agg", String.class, joinProviderConfigurationProviderMapping.get(QualityMeasuresProviderMapping_.qualityMeasuresProviderMappingMeasureId),builder.literal(","))
		};
		
		cq.multiselect(selections);
		
		cq.where(builder.notEqual(rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId), -1));
		
		cq.groupBy(rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationProviderId),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingYear),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingStart),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingEnd),
					rootMacraProviderConfiguration.get(MacraProviderConfiguration_.macraProviderConfigurationReportingMethod)
				);
		
		List<MacraProviderQDM> providerInfo=em.createQuery(cq).getResultList();
		
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

		return raceDetails;
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<MIPSPatientInformation> getFilteredDetails(String patientId,Integer ageFrom,Integer ageTo,Integer ageCriteria,String raceCode,String ethnicityCode,String gender,Integer insCompanyId,String currMeasureId,String dxCodes) throws Exception {
		List<Object> raceResult=new ArrayList<Object>();
		List<Object> ethnicityResult=new ArrayList<Object>();
		String raceResultCodes="";
		String ethnicityResultCodes="";
		raceResultCodes+="00000,";
		ethnicityResultCodes+="00000,";
		if(!raceCode.equals("-1"))
		{System.out.println("race codes.............."+raceCode);
			System.out.println("coming.............");
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
		System.out.println("raceResultCodes is..........."+raceResultCodes.toString());
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
		System.out.println("ethnicityResultCodes is..................."+ethnicityResultCodes.toString());
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<MIPSPatientInformation> cq = builder.createQuery(MIPSPatientInformation.class);
		Root<PatientRegistration> rootPatientRegistration = cq.from(PatientRegistration.class);
		Join<PatientRegistration,QualityMeasuresPatientEntries> joinQualityMeasuresPatientEntries=rootPatientRegistration.join(PatientRegistration_.qualityMeasuresPatientEntries,JoinType.INNER);
		Predicate byMeasureId=builder.equal(joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesMeasureId), currMeasureId);
		joinQualityMeasuresPatientEntries.on(byMeasureId);
		/*Join<PatientRegistration,QualityMeasuresPatientEntries> joinQualityMeasuresPatientEntries=rootPatientRegistration.join(PatientRegistration_.qualityMeasuresPatientEntries,JoinType.INNER);
		Join<QualityMeasuresPatientEntries,EmployeeProfile> joinEmployeeProfile=joinQualityMeasuresPatientEntries.join(QualityMeasuresPatientEntries_.empProfile,JoinType.INNER);
		Join<EmployeeProfile,H478> joinH478=joinEmployeeProfile.join(EmployeeProfile_.empProfile,JoinType.INNER);
		*/
		Join<PatientRegistration,ProblemList> joinProblemList=rootPatientRegistration.join(PatientRegistration_.problemList,JoinType.INNER);
		Join<PatientRegistration,PatientInsDetail> joinPatientInsDetail=rootPatientRegistration.join(PatientRegistration_.patientInsuranceTable,JoinType.INNER);
		Join<PatientInsDetail,InsCompAddr> joinInsCompAddr=joinPatientInsDetail.join(PatientInsDetail_.insCompAddr,JoinType.INNER);
		Join<InsCompAddr,InsCompany> joinInsCompany=joinInsCompAddr.join(InsCompAddr_.insCompany,JoinType.INNER);
		
		
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
		//Predicate byIns;
		if(!insCompanyId.equals(-1)){
			Predicate byInsCompany=builder.equal(joinInsCompany.get(InsCompany_.insCompanyId), insCompanyId);
			joinInsCompany.on(byInsCompany);
		}
		
		//Predicate byAge=builder.between(rootPatientRegistration.get(PatientRegistration_.), x, y)
		predicates.add(rootPatientRegistration.get(PatientRegistration_.patientRegistrationId).in(Arrays.asList(patientId.split(","))));
		Predicate byRaceEthnicity;
		if(!raceCode.equals("-1") && !ethnicityCode.equals("-1")){
			byRaceEthnicity=builder.and(builder.or(rootPatientRegistration.get(PatientRegistration_.patientRegistrationGranularRaceCode).in(Arrays.asList(raceCode.split(","))),rootPatientRegistration.get(PatientRegistration_.patientRegistrationRace).in(Arrays.asList(raceResultCodes.split(",")))),builder.or(rootPatientRegistration.get(PatientRegistration_.patientRegistrationGranularEthnicityCode).in(Arrays.asList(ethnicityCode.split(","))),rootPatientRegistration.get(PatientRegistration_.patientRegistrationEthnicity).in(Arrays.asList(ethnicityResultCodes.split(",")))));
			predicates.add(byRaceEthnicity);
		}
		else if(!raceCode.equals("-1") && ethnicityCode.equals("-1")){
			System.out.println("correct came");
			byRaceEthnicity=builder.or(rootPatientRegistration.get(PatientRegistration_.patientRegistrationGranularRaceCode).in(Arrays.asList(raceCode.split(","))),rootPatientRegistration.get(PatientRegistration_.patientRegistrationRace).in(Arrays.asList(raceResultCodes.split(","))));
			predicates.add(byRaceEthnicity);
		}
		else if(raceCode.equals("-1") && !ethnicityCode.equals("-1")){
			System.out.println("correct came 2");
			byRaceEthnicity=builder.or(rootPatientRegistration.get(PatientRegistration_.patientRegistrationGranularEthnicityCode).in(Arrays.asList(ethnicityCode.split(","))),rootPatientRegistration.get(PatientRegistration_.patientRegistrationEthnicity).in(Arrays.asList(ethnicityResultCodes.split(","))));
			predicates.add(byRaceEthnicity);
		}
		
		if(!dxCodes.equals("-1")){
			predicates.add(joinProblemList.get(ProblemList_.problemListDxCode).in(Arrays.asList(dxCodes.split(","))));
			predicates.add(builder.equal(joinProblemList.get(ProblemList_.problemListIsactive),true));
			predicates.add(builder.equal(joinProblemList.get(ProblemList_.problemListIsresolved),false));
		}
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
		List<EMeasure> emeasure = utils.getMeasureBeanDetails(measureId, sharedPath);
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


}
