package com.glenwood.glaceemr.server.application.services.portal.portalMedicalSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Encounter_;
import com.glenwood.glaceemr.server.application.models.PatientPortalUser;
import com.glenwood.glaceemr.server.application.models.InitialSettings;
import com.glenwood.glaceemr.server.application.models.PatientPortalMenuConfig;
import com.glenwood.glaceemr.server.application.models.PortalConfigurationBean;
import com.glenwood.glaceemr.server.application.models.PatientAllergies;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements_;
import com.glenwood.glaceemr.server.application.models.PatientEncounterType;
import com.glenwood.glaceemr.server.application.models.PatientEncounterType_;
import com.glenwood.glaceemr.server.application.models.PatientPortalFeatureConfig;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PlanInstruction;
import com.glenwood.glaceemr.server.application.models.PortalMedicalSummaryBean;
import com.glenwood.glaceemr.server.application.models.PortalPlanOfCareBean;
import com.glenwood.glaceemr.server.application.models.ProblemList;
import com.glenwood.glaceemr.server.application.repositories.BillinglookupRepository;
import com.glenwood.glaceemr.server.application.repositories.ChartRepository;
import com.glenwood.glaceemr.server.application.repositories.EmpProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.EncounterEntityRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientPortalUserRepository;
import com.glenwood.glaceemr.server.application.repositories.InitialSettingsRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientAllergiesRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientClinicalElementsRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientPortalMenuConfigRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.repositories.PlanTypeRepository;
import com.glenwood.glaceemr.server.application.repositories.ProblemListRepository;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailSaveService;
import com.glenwood.glaceemr.server.application.services.portal.portalAppointments.PortalAppointmentsService;
import com.glenwood.glaceemr.server.application.services.portal.portalDocuments.SharedFolderUtil;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.PortalBillingConfigFields;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.PortalSettingsService;
import com.glenwood.glaceemr.server.application.specifications.ChartSpecification;
import com.glenwood.glaceemr.server.application.specifications.PatientRegistrationSpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalMedicalSummarySpecification;
import com.glenwood.glaceemr.server.utils.SessionMap;


@Service
public class PortalMedicalSummaryServiceImpl implements PortalMedicalSummaryService{

	@Autowired
	ProblemListRepository problemListRepository;

	@Autowired
	PatientAllergiesRepository patientAllergiesRepository;

	@Autowired
	EmpProfileRepository empProfileRepository;

	@Autowired
	ChartRepository chartRepository;

	@Autowired
	PatientRegistrationRepository patientRegistrationRepository;
	
	@Autowired
	PatientPortalUserRepository h809Repository;
	
	@Autowired
	BillinglookupRepository billinglookupRepository;
	
	@Autowired
	PatientClinicalElementsRepository patientClinicalElementsRepository;
	
	@Autowired
	EncounterEntityRepository encounterEntityRepository;
	
	@Autowired
	PlanTypeRepository planTypeRepository;
	
	@Autowired
	PortalSettingsService portalSettingsService;
	
	@Autowired
	PortalAppointmentsService portalAppointmentsService;
	
	@Autowired
	InitialSettingsRepository initialSettingsRepository;

	@Autowired
	EntityManagerFactory emf ;

	@PersistenceContext
	EntityManager em;
	
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	AuditTrailSaveService auditTrailSaveService;
	
	@Autowired
	HttpServletRequest request;


	@Override
	public PortalConfigurationBean getSessionMap(String username) throws JsonProcessingException {
							
		PortalConfigurationBean configurationBean=new PortalConfigurationBean();
		
		List<InitialSettings> practiceDetails=portalSettingsService.getPracticeDetails();
		
		PortalBillingConfigFields configFields=portalSettingsService.getPortalBillingConfigFields();
		
		List<PatientPortalMenuConfig> portalMenuItemList=portalSettingsService.getPortalMenuConfig(true);
		
		List<PatientPortalFeatureConfig> portalFeatureConfigList=portalSettingsService.getPortalFeatureConfig(true);
				
		configurationBean.setSessionID(sessionMap.getPortalSessionID());
		configurationBean.setUsername(sessionMap.getPortalUser());
		configurationBean.setTennantDB(sessionMap.getPortalDBName());
		configurationBean.setUserID(sessionMap.getPortalUserID());
		configurationBean.setGlaceApacheUrl(sessionMap.getGlaceApacheUrl());
		configurationBean.setGlaceSpringContext(sessionMap.getGlaceSpringContext());
		configurationBean.setGlaceTomcatContext(sessionMap.getGlaceTomcatContext());
		configurationBean.setGlaceTomcatUrl(sessionMap.getGlaceTomcatUrl());
		configurationBean.setPortalLoginUrl(sessionMap.getPortalLoginUrl());
		configurationBean.setPortalLoginContext(sessionMap.getPortalLoginContext());
		configurationBean.setPortalMenuItemList(portalMenuItemList);
		configurationBean.setPortalFeatureItemList(portalFeatureConfigList);
		configurationBean.setLoggedIn(true);		
		
		for(int p=0;p<practiceDetails.size();p++){
			
			if(practiceDetails.get(p).getInitialSettingsOptionName().equalsIgnoreCase("Address")){
				sessionMap.setPracticeAddress(practiceDetails.get(p).getInitialSettingsOptionValue());
				configurationBean.setPracticeAddress(sessionMap.getPracticeAddress());
			}
				
			if(practiceDetails.get(p).getInitialSettingsOptionName().equalsIgnoreCase("Doctor Message")){
				sessionMap.setPracticeDoctorMessage(practiceDetails.get(p).getInitialSettingsOptionValue());
				configurationBean.setPracticeDoctorMessage(sessionMap.getPracticeDoctorMessage());
			}
				
			if(practiceDetails.get(p).getInitialSettingsOptionName().equalsIgnoreCase("city")){
				sessionMap.setPracticeCity(practiceDetails.get(p).getInitialSettingsOptionValue());
				configurationBean.setPracticeCity(sessionMap.getPracticeCity());
			}
					
			if(practiceDetails.get(p).getInitialSettingsOptionName().equalsIgnoreCase("Web Address")){
				sessionMap.setPracticeWebAddress(practiceDetails.get(p).getInitialSettingsOptionValue());
				configurationBean.setPracticeWebAddress(sessionMap.getPracticeWebAddress());
			}
						
			if(practiceDetails.get(p).getInitialSettingsOptionName().equalsIgnoreCase("Fax Number")){
				sessionMap.setPracticeFax(practiceDetails.get(p).getInitialSettingsOptionValue());
				configurationBean.setPracticeFax(sessionMap.getPracticeFax());
			}
							
			if(practiceDetails.get(p).getInitialSettingsOptionName().equalsIgnoreCase("state")){
				
				if(practiceDetails.get(p).getInitialSettingsOptionValue()!=null)
				for(int s=0;s<configFields.getStatesList().size();s++){
					if(configFields.getStatesList().get(s).getBillingConfigTableConfigId().equals(Integer.parseInt(practiceDetails.get(p).getInitialSettingsOptionValue())))
						sessionMap.setPracticeState(configFields.getStatesList().get(s).getBillingConfigTableLookupDesc());
				}
				configurationBean.setPracticeState(sessionMap.getPracticeState());
			}
				
			if(practiceDetails.get(p).getInitialSettingsOptionName().equalsIgnoreCase("zipCode")){
				sessionMap.setPracticeZipcode(practiceDetails.get(p).getInitialSettingsOptionValue());
				configurationBean.setPracticeZipcode(sessionMap.getPracticeZipcode());
			}
					
			if(practiceDetails.get(p).getInitialSettingsOptionName().equalsIgnoreCase("Practice Name")){
				sessionMap.setPracticeName(practiceDetails.get(p).getInitialSettingsOptionValue());
				configurationBean.setPracticeName(sessionMap.getPracticeName());
			}
							
			if(practiceDetails.get(p).getInitialSettingsOptionName().equalsIgnoreCase("email Address")){
				sessionMap.setPracticeEmail(practiceDetails.get(p).getInitialSettingsOptionValue());
				configurationBean.setPracticeEmail(sessionMap.getPracticeEmail());
			}
								
			if(practiceDetails.get(p).getInitialSettingsOptionName().equalsIgnoreCase("Phone No")){
				sessionMap.setPracticePhone(practiceDetails.get(p).getInitialSettingsOptionValue());
				configurationBean.setPracticePhone(sessionMap.getPracticePhone());
			}
									
			if(practiceDetails.get(p).getInitialSettingsOptionName().equalsIgnoreCase("Shared Folder Path")){
				sessionMap.setPracticeSharedFolderPath(practiceDetails.get(p).getInitialSettingsOptionValue());
				configurationBean.setPracticeSharedFolderPath(sessionMap.getPracticeSharedFolderPath());
			}
		}
		
		SharedFolderUtil.sharedFolderPath=sessionMap.getPracticeSharedFolderPath()+"/";

		return configurationBean;
	}



	@Override
	public List<PatientPortalUser> getPatientDetailsByUsername(String username) throws JsonProcessingException {
		
		System.out.println("username:::***************************************:::"+username);
		
		List<PatientPortalUser> patientPersonalDetails= h809Repository.findAll(PatientRegistrationSpecification.getPatientDetailsByUsername(username));
		
		System.out.println("patientPersonalDetailsSize::::*********************************************:::"+patientPersonalDetails.size());
		
		return patientPersonalDetails;

	}


	@Override
	public PatientRegistration getPatientPersonalDetails(int patientId) {

		PatientRegistration patientPersonalDetails= patientRegistrationRepository.findOne(PatientRegistrationSpecification.getPatientPersonalDetails(patientId));

		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Retrieving patient's personal details with patientId:"+patientId,-1,
				request.getRemoteAddr(),patientId,"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Retrieving patient's personal details with patientId:"+patientId,"");
		
		return patientPersonalDetails;

	}

	@Override
	public List<Chart> getPatientChartId(int patientId) {

		List<Chart> chartId=chartRepository.findAll(ChartSpecification.getChartId(patientId));

		return chartId;

	}
	
	
	@Override
	public List<PortalPlanOfCareBean> getPlanOfCare(int patientId, int chartId, int pageOffset, int pageIndex) {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<PatientClinicalElements> root = cq.from(PatientClinicalElements.class);
		Join<PatientClinicalElements, Encounter> clinicalElementsEncounterJoin=root.join(PatientClinicalElements_.encounter,JoinType.INNER);
		Join<Encounter, PatientEncounterType> encounterTypeJoin=clinicalElementsEncounterJoin.join(Encounter_.patientEncounterType);
		Join<PatientClinicalElements, PlanInstruction> planInstructionClinicalElementsJoin=root.join(PatientClinicalElements_.planInstruction, JoinType.INNER);
		Join<Encounter, EmployeeProfile> encounterServiceDoctorJoin=clinicalElementsEncounterJoin.join(Encounter_.empProfileEmpId, JoinType.LEFT);
		
		cq.select(builder.construct(PortalPlanOfCareBean.class,root.get(PatientClinicalElements_.patientClinicalElementsEncounterid),clinicalElementsEncounterJoin.get(Encounter_.encounterDate), encounterTypeJoin.get(PatientEncounterType_.encounterTypename), encounterServiceDoctorJoin.get(EmployeeProfile_.empProfileEmpid),encounterServiceDoctorJoin.get(EmployeeProfile_.empProfileFullname))).distinct(true);
		cq.where(builder.equal(root.get(PatientClinicalElements_.patientClinicalElementsPatientid), patientId)).orderBy(builder.desc(clinicalElementsEncounterJoin.get(Encounter_.encounterDate)));
		
		
		Query query=em.createQuery(cq);
		query.setFirstResult(pageIndex*pageOffset);	// Page no (offset)
		query.setMaxResults(pageOffset);		// Page size (No of results)
		
		List<Object> planOfCareResultList=query.getResultList();
		List<PortalPlanOfCareBean> detailsBeanList = new ArrayList<PortalPlanOfCareBean>();
		for(int i=0;i<planOfCareResultList.size();i++){
			PortalPlanOfCareBean detailsBean=(PortalPlanOfCareBean)planOfCareResultList.get(i);
		detailsBeanList.add(detailsBean);
		}
		
		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Retrieving patient's plan of care with patientId:"+patientId,-1,
				request.getRemoteAddr(),patientId,"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Retrieving patient's plan of care with patientId:"+patientId,"");
	
		return detailsBeanList;
	}
	
	/*@Override
	public List<PlanType> getPlanOfCareDetails(int patientId, int encounterId, int pageOffset, int pageIndex) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = cb.createQuery();
		Root<PlanType> root = cq.from(PlanType.class);
		Join<PlanType, PlanInstruction> planInstructionTypeJoin=root.join(PlanType_.planTypeInstructions,JoinType.INNER);
		Join<PlanInstruction, PatientClinicalElements> planInstructionClinicalElementsJoin=planInstructionTypeJoin.join(PlanInstruction_.patientPlanClinicalElements, JoinType.INNER);
		
		cq.multiselect(cb.construct(PlanType.class), root.get(PlanType_.planTypeId), root.get(PlanType_.planTypeName), root.get(PlanType_.planTypeGwid), root.get(PlanType_.planTypeInstructions));
		cq.where(cb.notEqual(root.get(PlanType_.planTypeId), 6),cb.equal(planInstructionClinicalElementsJoin.get(PatientClinicalElements_.patientClinicalElementsPatientid), patientId), cb.equal(planInstructionClinicalElementsJoin.get(PatientClinicalElements_.patientClinicalElementsEncounterid), encounterId));
		List<Object> planOfCareResultList=em.createQuery(cq).getResultList();
		List<PlanType> detailsBeanList = new ArrayList<PlanType>();
		for(int i=0;i<planOfCareResultList.size();i++){
			PlanType detailsBean=(PlanType)planOfCareResultList.get(i);
		detailsBeanList.add(detailsBean);
		}
		
		return detailsBeanList;
	}*/

	

	@Override
	public List<PatientClinicalElements> getPlanOfCareDetails(int patientId, int encounterId, int pageOffset, int pageIndex) {
		
		List<PatientClinicalElements> planTyleList=patientClinicalElementsRepository.findAll(PortalMedicalSummarySpecification.getPlanOfCareInstructionsByPatientId(patientId, encounterId));
		
		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Retrieving patient's plan of care details with patientId:"+patientId,-1,
				request.getRemoteAddr(),patientId,"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Retrieving patient's plan of care details with patientId:"+patientId,"");
	
		return planTyleList;
	}
	
	@Override
	public List<ProblemList> getPatientProblemList(int patientId, String problemType, int pageOffset, int pageIndex) {

		List<ProblemList> problemsList=problemListRepository.findAll(PortalMedicalSummarySpecification.getPatientProblemList(patientId, problemType)/*, PortalMedicalSummarySpecification.createPortalProblemsListPageRequest(pageOffset, pageOffset)*/)/*.getContent()*/;

		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Retrieving patient's problem list with patientId:"+patientId,-1,
				request.getRemoteAddr(),patientId,"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Retrieving patient's problem list with patientId:"+patientId,"");
	
		return problemsList;

	}
	
	@Override
	public List<PatientAllergies> getPatientAllergies(int chartId, int pageOffset, int pageIndex) {

		List<PatientAllergies> allergiesList=patientAllergiesRepository.findAll(PortalMedicalSummarySpecification.getPatientAllergiesList(chartId), PortalMedicalSummarySpecification.createPortalAllergiesPageRequest(pageOffset, pageIndex)).getContent();

		return allergiesList;

	}
	
	
	

	 /*Method to execute the custom queries*/ 
	public List<Map<String, Object>> CustomQueryResult(List<String> listinorder,CriteriaQuery<Object> cq) 
	{

		try{

			Iterator iter= em.createQuery(cq).getResultList().iterator();
			List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();

			while ( iter.hasNext() ) {
				Object[] row = (Object[]) iter.next();
				Map<String,Object> newMap=  new HashMap<String,Object>();
				for(int i=0;i<row.length;i++)
				{
					newMap.put(listinorder.get(i),(Object)row[i]);
				}
				resultList.add(newMap);

			}
			return resultList;
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}
	
	
	@Override
	public PortalMedicalSummaryBean getPortalMedicalSummaryDetails(int patientId, int chartId) {
		
		int totalActiveProblemsCount=(int) problemListRepository.count(PortalMedicalSummarySpecification.getPatientProblemList(patientId, "Active"));
		int totalResolvedProblemsCount=(int) problemListRepository.count(PortalMedicalSummarySpecification.getPatientProblemList(patientId, "Resolved"));
		int totalProblemsCount=(int) problemListRepository.count(PortalMedicalSummarySpecification.getPatientProblemList(patientId, "All"));
		int totalAllergiesCount=(int) patientAllergiesRepository.count(PortalMedicalSummarySpecification.getPatientAllergiesList(chartId));
		int totalPlanOfCareCount=10;
		
		
		PortalMedicalSummaryBean summaryBean=new PortalMedicalSummaryBean(totalActiveProblemsCount,totalResolvedProblemsCount,totalProblemsCount,totalAllergiesCount,totalPlanOfCareCount);
		
		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Retrieving patient's medical summary details with patientId:"+patientId,-1,
				request.getRemoteAddr(),patientId,"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Retrieving patient's medical summary details with patientId:"+patientId,"");
	
		return summaryBean;
	}
	
	public static Pageable createPortalPlanOfCareByDescDate(int pageIndex, int offset) {

		return new PageRequest(pageIndex, offset, Sort.Direction.DESC,"encounterDate");
	}

}
