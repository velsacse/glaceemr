package com.glenwood.glaceemr.server.application.services.portal.portalSettings;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.BillingConfigTable;
import com.glenwood.glaceemr.server.application.models.Billinglookup;
import com.glenwood.glaceemr.server.application.models.ClinicalIntakeFormBean;
import com.glenwood.glaceemr.server.application.models.DemographicmodifyStatus;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EncryptedPatientDetails;
import com.glenwood.glaceemr.server.application.models.FileDetails;
import com.glenwood.glaceemr.server.application.models.PatientPortalDemographicRequest;
import com.glenwood.glaceemr.server.application.models.PatientPortalUser;
import com.glenwood.glaceemr.server.application.models.PatientPortalAlertConfig;
import com.glenwood.glaceemr.server.application.models.InitialSettings;
import com.glenwood.glaceemr.server.application.models.InsCompAddr;
import com.glenwood.glaceemr.server.application.models.InsuranceFilterBean;
import com.glenwood.glaceemr.server.application.models.PatientPortalFeatureConfig;
import com.glenwood.glaceemr.server.application.models.PatientPortalMenuConfig;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistrationBean;
import com.glenwood.glaceemr.server.application.models.PortalNotificationAlertsBean;
import com.glenwood.glaceemr.server.application.models.PortalPatientPaymentsSummary;
import com.glenwood.glaceemr.server.application.models.PortalSchedulerAppointmentBean;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.SavePatientDemographicsBean;
import com.glenwood.glaceemr.server.application.repositories.AlertEventRepository;
import com.glenwood.glaceemr.server.application.repositories.BillingConfigTableRepository;
import com.glenwood.glaceemr.server.application.repositories.BillinglookupRepository;
import com.glenwood.glaceemr.server.application.repositories.DemographicmodifyStatusRepository;
import com.glenwood.glaceemr.server.application.repositories.EmpProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientPortalDemographicRequestRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientPortalUserRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientPortalAlertConfigRespository;
import com.glenwood.glaceemr.server.application.repositories.InitialSettingsRepository;
import com.glenwood.glaceemr.server.application.repositories.InsCompAddrRepository;
import com.glenwood.glaceemr.server.application.repositories.InsCompanyRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientClinicalElementsQuestionsRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientPortalFeatureConfigRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientPortalMenuConfigRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.repositories.PosTableRepository;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailSaveService;
import com.glenwood.glaceemr.server.application.services.portal.portalAppointments.PortalAppointmentsService;
import com.glenwood.glaceemr.server.application.services.portal.portalForms.PortalClinicalIntakeAndConsentFormsListBean;
import com.glenwood.glaceemr.server.application.services.portal.portalForms.PortalFormsService;
import com.glenwood.glaceemr.server.application.services.portal.portalMedicalSummary.PortalMedicalSummaryService;
import com.glenwood.glaceemr.server.application.services.portal.portalPayments.PortalPaymentsService;
import com.glenwood.glaceemr.server.application.services.portalLogin.PortalLoginService;
import com.glenwood.glaceemr.server.application.specifications.InsuranceSpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalLoginSpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalSettingsSpecification;
import com.glenwood.glaceemr.server.utils.SessionMap;


@Service
public class PortalSettingsServiceImpl implements PortalSettingsService{

	@Autowired
	BillinglookupRepository billinglookupRepository;

	@Autowired 
	PosTableRepository posTableRepository;

	@Autowired
	EmpProfileRepository empProfileRepository;

	@Autowired
	BillingConfigTableRepository billingConfigTableRepository;

	@Autowired
	PortalMedicalSummaryService portalMedicalSummaryService;

	@Autowired
	PatientRegistrationRepository patientRegistrationRepository;

	@Autowired
	PatientPortalDemographicRequestRepository h807Repository;

	@Autowired
	PatientPortalAlertConfigRespository h810Respository;
	
	@Autowired
	PatientClinicalElementsQuestionsRepository patientClinicalElementsQuestionsRepository;

	@Autowired
	DemographicmodifyStatusRepository demographicmodifyStatusRepository;

	@Autowired
	AlertEventRepository alertEventRepository;

	@Autowired
	InitialSettingsRepository initialSettingsRepository;
	
	@Autowired
	PatientPortalMenuConfigRepository patientPortalMenuConfigRepository;

	@Autowired
	PatientPortalFeatureConfigRepository patientPortalFeatureConfigRepository;
	
	@Autowired
	InsCompanyRepository insCompanyRepository;
	
	@Autowired
	InsCompAddrRepository insCompAddrRepository;

	@Autowired
	PortalLoginService portalLoginService;

	@Autowired
	PortalFormsService portalFormsService;
	
	@Autowired
	PortalAppointmentsService portalAppointmentsService;
	
	@Autowired
	PortalPaymentsService portalPaymentsService;

	@Autowired
	PatientPortalUserRepository h809Repository;

	@Autowired
	SessionMap sessionMap;

	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	AuditTrailSaveService auditTrailSaveService;
	
	@Autowired
	HttpServletRequest request;
	

	@Override
	public List<PatientPortalMenuConfig> getPortalMenuConfig(boolean isActiveMenuItemList) {
		
		List<PatientPortalMenuConfig> portalMenuItemList=patientPortalMenuConfigRepository.findAll(PortalSettingsSpecification.getPortalMenuConfig(isActiveMenuItemList));
		
		return portalMenuItemList;
	}

	@Override
	public List<PatientPortalFeatureConfig> getPortalFeatureConfig(boolean isActiveFeatureItemList) {
		
		List<PatientPortalFeatureConfig> portalFeatureItemList=patientPortalFeatureConfigRepository.findAll(PortalSettingsSpecification.getPortalFeatureConfig(isActiveFeatureItemList));
		
		return portalFeatureItemList;
	}

	@Override
	public PatientProfileSettingsFields getPatientProfileSettingsFieldsList() {

		PatientProfileSettingsFields patientProfileSettingsFieldsList=new PatientProfileSettingsFields();

		List<Billinglookup> availabelanguageOptions=billinglookupRepository.findAll(PortalSettingsSpecification.getLanguageList());
		List<Billinglookup> availabeRaceOptions=billinglookupRepository.findAll(PortalSettingsSpecification.getRaceList());
		List<Billinglookup> availabeEthnicityOptions=billinglookupRepository.findAll(PortalSettingsSpecification.getEthnicityList());
		List<Billinglookup> availabeReminderTypeOptions=billinglookupRepository.findAll(PortalSettingsSpecification.getPatientReminderTypeList());
		List<Billinglookup> availabeMaritalStatusOptions=billinglookupRepository.findAll(PortalSettingsSpecification.getMaritalStatusList());
		List<Billinglookup> availabeGenderTitleOptions=billinglookupRepository.findAll(PortalSettingsSpecification.getGenderTitleList());
		List<Billinglookup> paymentMethods=billinglookupRepository.findAll(PortalSettingsSpecification.getPaymentMethod());
		List<Billinglookup> payerTypes=billinglookupRepository.findAll(PortalSettingsSpecification.getPayerType());

		patientProfileSettingsFieldsList.setAvailabelanguageOptions(availabelanguageOptions);
		patientProfileSettingsFieldsList.setAvailabeRaceOptions(availabeRaceOptions);
		patientProfileSettingsFieldsList.setAvailabeEthnicityOptions(availabeEthnicityOptions);
		patientProfileSettingsFieldsList.setAvailabeReminderTypeOptions(availabeReminderTypeOptions);
		patientProfileSettingsFieldsList.setAvailabeMaritalStatusOptions(availabeMaritalStatusOptions);
		patientProfileSettingsFieldsList.setAvailabeGenderTitleOptions(availabeGenderTitleOptions);
		patientProfileSettingsFieldsList.setPaymentMethods(paymentMethods);
		patientProfileSettingsFieldsList.setPayerTypes(payerTypes);


		return patientProfileSettingsFieldsList;
	}

	@Override
	public List<PosTable> getActivePosList() {

		List<PosTable> posList=posTableRepository.findAll(PortalSettingsSpecification.getActivePosList());
		return posList;
	}

	@Override
	public List<EmployeeProfile> getProvidersList() {

		List<EmployeeProfile> providersList=empProfileRepository.findAll(PortalSettingsSpecification.getProvidersList());

		return providersList;
	}

	@Override
	public PortalBillingConfigFields getPortalBillingConfigFields() {

		List<BillingConfigTable> statesList=billingConfigTableRepository.findAll(PortalSettingsSpecification.getStateList());

		PortalBillingConfigFields portalBillingConfigFields=new PortalBillingConfigFields();

		portalBillingConfigFields.setStatesList(statesList);

		return portalBillingConfigFields;
	}

	@Override
	public PatientRegistration saveDemographicChanges(SavePatientDemographicsBean savePatientDemographicsBean) {

		PatientRegistrationBean regSaveDetailsBean=savePatientDemographicsBean.getSavePatientRegistrationBean();
		PatientRegistration registrationDetails=portalMedicalSummaryService.getPatientPersonalDetails(regSaveDetailsBean.getPatientRegistrationId());

		PatientPortalDemographicRequest demographicChanges=new PatientPortalDemographicRequest();
		if(regSaveDetailsBean.getPatientRegistrationFirstName()!=null&&!regSaveDetailsBean.getPatientRegistrationFirstName().equals(registrationDetails.getPatientRegistrationFirstName()))
			demographicChanges.setdemographic_request_first_name(WordUtils.capitalize(regSaveDetailsBean.getPatientRegistrationFirstName()));
		else
			demographicChanges.setdemographic_request_first_name("");

		if(regSaveDetailsBean.getPatientRegistrationLastName()!=null&&!regSaveDetailsBean.getPatientRegistrationLastName().equals(registrationDetails.getPatientRegistrationLastName()))
			demographicChanges.setdemographic_request_last_name(WordUtils.capitalize(regSaveDetailsBean.getPatientRegistrationLastName()));
		else 
			demographicChanges.setdemographic_request_last_name("");

		if(regSaveDetailsBean.getPatientRegistrationMidInitial()!=null&&!regSaveDetailsBean.getPatientRegistrationMidInitial().equals(registrationDetails.getPatientRegistrationMidInitial()))
			demographicChanges.setdemographic_request_mid_initial(WordUtils.capitalize(regSaveDetailsBean.getPatientRegistrationMidInitial()));
		else
			demographicChanges.setdemographic_request_mid_initial("");

		if(regSaveDetailsBean.getPatientRegistrationWorkNo()!=null&&!regSaveDetailsBean.getPatientRegistrationWorkNo().equals(registrationDetails.getPatientRegistrationWorkNo()))
			demographicChanges.setdemographic_request_work_phone(regSaveDetailsBean.getPatientRegistrationWorkNo());
		else
			demographicChanges.setdemographic_request_work_phone("");

		if(regSaveDetailsBean.getPatientRegistrationDob()!=null&&!regSaveDetailsBean.getPatientRegistrationDob().equals(registrationDetails.getPatientRegistrationDob()))
			demographicChanges.setdemographic_request_date_of_birth(regSaveDetailsBean.getPatientRegistrationDob());
		else
			demographicChanges.setdemographic_request_date_of_birth("");

		if(regSaveDetailsBean.getPatientRegistrationSex()!=null&&!regSaveDetailsBean.getPatientRegistrationSex().equals(registrationDetails.getPatientRegistrationSex()))
			demographicChanges.setdemographic_request_gender(regSaveDetailsBean.getPatientRegistrationSex());
		else{
			if(regSaveDetailsBean.getPatientRegistrationSex()!=null)
				demographicChanges.setdemographic_request_gender(regSaveDetailsBean.getPatientRegistrationSex());
			else
				demographicChanges.setdemographic_request_gender(-1);
		}

		if(regSaveDetailsBean.getPatientRegistrationMaritalstatus()!=null&&!regSaveDetailsBean.getPatientRegistrationMaritalstatus().equals(registrationDetails.getPatientRegistrationMaritalstatus()))
			demographicChanges.setdemographic_request_marital_status(regSaveDetailsBean.getPatientRegistrationMaritalstatus());
		else{
			if(regSaveDetailsBean.getPatientRegistrationMaritalstatus()!=null)
				demographicChanges.setdemographic_request_marital_status(regSaveDetailsBean.getPatientRegistrationMaritalstatus());
			else
				demographicChanges.setdemographic_request_marital_status(-1);
		}

		if(regSaveDetailsBean.getPatientRegistrationPhoneNo()!=null&&!regSaveDetailsBean.getPatientRegistrationPhoneNo().equals(registrationDetails.getPatientRegistrationPhoneNo()))
			demographicChanges.setdemographic_request_home_phone(regSaveDetailsBean.getPatientRegistrationPhoneNo());
		else
			demographicChanges.setdemographic_request_home_phone("");

		if(!(regSaveDetailsBean.getPatientRegistrationAddress1()+regSaveDetailsBean.getPatientRegistrationAddress2()).equals(registrationDetails.getPatientRegistrationAddress1()+registrationDetails.getPatientRegistrationAddress2()))
			demographicChanges.setdemographic_request_address(WordUtils.capitalize(regSaveDetailsBean.getPatientRegistrationAddress1()+regSaveDetailsBean.getPatientRegistrationAddress2()));
		else
			demographicChanges.setdemographic_request_address("");

		if(regSaveDetailsBean.getPatientRegistrationCity()!=null&&!regSaveDetailsBean.getPatientRegistrationCity().equals(registrationDetails.getPatientRegistrationCity()))
			demographicChanges.setdemographic_request_city(regSaveDetailsBean.getPatientRegistrationCity());
		else
			demographicChanges.setdemographic_request_city("");

		if(regSaveDetailsBean.getPatientRegistrationState()!=null&&!regSaveDetailsBean.getPatientRegistrationState().equals(registrationDetails.getPatientRegistrationState()))
			demographicChanges.setdemographic_request_state(regSaveDetailsBean.getPatientRegistrationState());
		else{
			if(regSaveDetailsBean.getPatientRegistrationState()!=null)
				demographicChanges.setdemographic_request_state(regSaveDetailsBean.getPatientRegistrationState());
			else
				demographicChanges.setdemographic_request_state("-1");
		}

		if(regSaveDetailsBean.getPatientRegistrationZip()!=null&&!regSaveDetailsBean.getPatientRegistrationZip().equals(registrationDetails.getPatientRegistrationZip()))
			demographicChanges.setdemographic_request_zip(regSaveDetailsBean.getPatientRegistrationZip());
		else
			demographicChanges.setdemographic_request_zip("");

		if(regSaveDetailsBean.getPatientRegistrationMailId()!=null&&!regSaveDetailsBean.getPatientRegistrationMailId().equals(registrationDetails.getPatientRegistrationMailId()))
			demographicChanges.setdemographic_request_email(regSaveDetailsBean.getPatientRegistrationMailId());
		else
			demographicChanges.setdemographic_request_email("");


		demographicChanges.setdemographic_request_employer("");
		demographicChanges.setdemographic_request_cell_no("");
		demographicChanges.setdemographic_request_driver_license("");
		demographicChanges.setdemographic_request_refer_physician("-1");
		demographicChanges.setdemographic_request_how_intro(-1);
		demographicChanges.setdemographic_request_contact_person("");
		demographicChanges.setdemographic_request_contact_person_relation(-1);
		demographicChanges.setdemographic_request_contact_person_address("");
		demographicChanges.setdemographic_request_contact_person_city("");
		demographicChanges.setdemographic_request_contact_person_state("-1");
		demographicChanges.setdemographic_request_contact_person_zip("");
		demographicChanges.setdemographic_request_primary_ins_company("");
		demographicChanges.setdemographic_request_primary_ins_company_id("");
		demographicChanges.setdemographic_request_primary_ins_patient_id("");
		demographicChanges.setdemographic_request_primary_ins_valid_from("");
		demographicChanges.setdemographic_request_primary_ins_valid_to("");
		demographicChanges.setdemographic_request_primary_ins_group_number("");
		demographicChanges.setdemographic_request_primary_ins_group_name("");
		demographicChanges.setdemographic_request_primary_ins_relation(-1);
		demographicChanges.setdemographic_request_primary_ins_subscriber_name("");
		demographicChanges.setdemographic_request_secondary_ins_company("");
		demographicChanges.setdemographic_request_secondary_ins_company_id("");
		demographicChanges.setdemographic_request_secondary_ins_patient_id("");
		demographicChanges.setdemographic_request_secondary_ins_valid_from("");
		demographicChanges.setdemographic_request_secondary_ins_valid_to("");
		demographicChanges.setdemographic_request_secondary_ins_group_number("");
		demographicChanges.setdemographic_request_secondary_ins_group_name("");
		demographicChanges.setdemographic_request_secondary_ins_relation(-1);
		demographicChanges.setdemographic_request_secondary_ins_subscriber_name("");
		demographicChanges.setdemographic_request_user_name("");
		demographicChanges.setdemographic_request_patient_id(regSaveDetailsBean.getPatientRegistrationId());
		demographicChanges.setdemographic_request_email_reminder(false);
		demographicChanges.setdemographic_request_phone_reminder(false);
		demographicChanges.setdemographic_request_primary_ins_copay(0.00);

		PatientPortalDemographicRequest demographicEntry=h807Repository.saveAndFlush(demographicChanges);


		if(regSaveDetailsBean.getPatientRegistrationSiblingsAges()!=null)
			registrationDetails.setPatientRegistrationSiblingsAges(regSaveDetailsBean.getPatientRegistrationSiblingsAges());
		if(regSaveDetailsBean.getPatientRegistrationRace()!=null)
			registrationDetails.setPatientRegistrationRace(regSaveDetailsBean.getPatientRegistrationRace());
		if(regSaveDetailsBean.getPatientRegistrationEthnicity()!=null)
			registrationDetails.setPatientRegistrationEthnicity(regSaveDetailsBean.getPatientRegistrationEthnicity());
		if(regSaveDetailsBean.getPatientRegistrationPreferredLan()!=null)
			registrationDetails.setPatientRegistrationPreferredLan(regSaveDetailsBean.getPatientRegistrationPreferredLan());
		if(regSaveDetailsBean.getPatientRegistrationReminderPreference()!=null)
			registrationDetails.setPatientRegistrationReminderPreference(regSaveDetailsBean.getPatientRegistrationReminderPreference());
		patientRegistrationRepository.saveAndFlush(registrationDetails);




		DemographicmodifyStatus demographicStatus=new DemographicmodifyStatus();
		demographicStatus.setDemographicmodifyStatusDisplay(-1);
		demographicStatus.setDemographicmodifyStatusPatientid(regSaveDetailsBean.getPatientRegistrationId());
		demographicStatus.setDemographicmodifyStatusRequestdate(new java.sql.Date(new Date().getTime()));
		demographicStatus.setDemographicmodifyStatusStatus("Requested");
		demographicmodifyStatusRepository.saveAndFlush(demographicStatus);


		PatientPortalAlertConfig demographicAlertCategory=h810Respository.findOne(PortalSettingsSpecification.getPortalAlertCategory(3));
		boolean sendToAll =demographicAlertCategory.getpatient_portal_alert_config_send_to_all();  
		int provider = Integer.parseInt(demographicAlertCategory.getpatient_portal_alert_config_provider());
		int forwardTo = Integer.parseInt(demographicAlertCategory.getpatient_portal_alert_config_forward_to());

		AlertEvent alert1=new AlertEvent();
		alert1.setAlertEventCategoryId(22);
		alert1.setAlertEventStatus(1);
		alert1.setAlertEventPatientId(regSaveDetailsBean.getPatientRegistrationId());
		alert1.setAlertEventPatientName(registrationDetails.getPatientRegistrationLastName()+", "+registrationDetails.getPatientRegistrationFirstName());
		alert1.setAlertEventEncounterId(-1);
		alert1.setAlertEventRefId(demographicEntry.getdemographic_request_id());
		alert1.setAlertEventMessage("Request to change the Demographics Details.");
		alert1.setAlertEventRoomId(-1);
		alert1.setAlertEventCreatedDate(new Timestamp(new Date().getTime()));
		alert1.setAlertEventModifiedby(-100);
		alert1.setAlertEventFrom(-100);
		
		

		if(provider==0 && forwardTo==0)
			alert1.setAlertEventTo(-1);
		else {
			if(sendToAll){
				if(forwardTo!=provider){
					alert1.setAlertEventTo(forwardTo);
					AlertEvent alert2=new AlertEvent();
					alert2.setAlertEventCategoryId(22);
					alert2.setAlertEventStatus(1);
					alert2.setAlertEventPatientId(regSaveDetailsBean.getPatientRegistrationId());
					alert2.setAlertEventPatientName(registrationDetails.getPatientRegistrationLastName()+", "+registrationDetails.getPatientRegistrationFirstName());
					alert2.setAlertEventEncounterId(-1);
					alert2.setAlertEventRefId(demographicEntry.getdemographic_request_id());
					alert2.setAlertEventMessage("Request to change the Demographics Details.");
					alert2.setAlertEventRoomId(-1);
					alert2.setAlertEventCreatedDate(new Timestamp(new Date().getTime()));
					alert2.setAlertEventModifiedby(-100);
					alert2.setAlertEventFrom(-100);
					alert2.setAlertEventTo(provider);
					alertEventRepository.saveAndFlush(alert2);
				} else {
					alert1.setAlertEventTo(forwardTo);
				}            	 
			}else{
				if(forwardTo!=0){
					alert1.setAlertEventTo(forwardTo);
				} else {
					alert1.setAlertEventTo(provider);
				}
			}
		}

		alertEventRepository.saveAndFlush(alert1);

		registrationDetails=portalMedicalSummaryService.getPatientPersonalDetails(regSaveDetailsBean.getPatientRegistrationId());
		
		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.CREATEORUPDATE,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Patient with id "+registrationDetails.getPatientRegistrationId()+" requested to update his/her demographic details.",-1,
				request.getRemoteAddr(),registrationDetails.getPatientRegistrationId(),"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Patient with id "+registrationDetails.getPatientRegistrationId()+" requested to update his/her demographic details.","");

		return registrationDetails;
	}

	@Override
	public EncryptedPatientDetails getActiveSessionForOldEMR(int patientId, int chartId) throws Exception {



		/*SSORequester sso=new SSORequester();
		String ssoString=sso.getUrlFromSSO(sessionMap.getPortalDBName()).toString();

		SSODetailsBean ssoBean=objectMapper.readValue(ssoString, SSODetailsBean.class);



		String postParameters="GWTMODE=1&txtUserName="+patientDetails.getH809004()+"&txtPassWord="+patientDetails.getH809010()+"&WCAG=1&accountId="+sessionMap.getPortalDBName();
		String connectionPath =ssoBean.getGlace_tomcat_URL()"http://192.168.2.98:8081/R14/jsp/GWTPortalLogin";

		AjaxConnect connect=new AjaxConnect();
		String responseString=connect.sendPost(connectionPath, postParameters);

		KioskLoginDetails responseBean=objectMapper.readValue(responseString.substring(1,responseString.length()-1), KioskLoginDetails.class);*/

		PatientPortalUser patientDetails=h809Repository.findOne(PortalLoginSpecification.activePatientById(patientId));

		EncryptedPatientDetails loginDetailsBean=new EncryptedPatientDetails();

		loginDetailsBean.setAuthString(patientDetails.getpatient_portal_user_name()+"*&@#"+patientDetails.getpatient_portal_user_password_hash());
		loginDetailsBean.setTennantDB(sessionMap.getPortalDBName());
		loginDetailsBean.setPatientId(patientId);

		return loginDetailsBean;
	}

	@Override
	public List<InitialSettings> getPracticeDetails() {

		List<InitialSettings> practiceDetails=initialSettingsRepository.findAll(PortalSettingsSpecification.getPracticeDetails());

		return practiceDetails;
	}
	
	@Override
	public InitialSettings getPracticeDetailsItem(String optionName) {

		 InitialSettings practiceDetails=initialSettingsRepository.findOne(PortalSettingsSpecification.getPracticeDetails(optionName));

		return practiceDetails;
	}

	@Override
	public PortalNotificationAlertsBean getPortalConfigDetails(int patientId, int chartId) throws ParseException, JsonProcessingException {

		PortalNotificationAlertsBean configBean=new PortalNotificationAlertsBean();
		configBean.setPatientId(patientId);
		configBean.setChartId(chartId);
		
		PortalClinicalIntakeAndConsentFormsListBean formsBean=portalFormsService.getPatientClinicalIntakeAndConsentFormsList(patientId, chartId, -2);
		
		int newIntakeFormsCount=0;
		/*New Forms*/
		List<ClinicalIntakeFormBean> intakeFormsList=formsBean.getClinicalIntakeFormsList();
		
		for(int i=0;i<intakeFormsList.size();i++){
			
			if(intakeFormsList.get(i).getPatientFormDetails()==null||intakeFormsList.get(i).getPatientFormDetails().getPatientClinicalElementsQuestionsModifiedOn()==null)
				newIntakeFormsCount++;
		}

		List<FileDetails> consentFormsList = formsBean.getConsentFormsList();
		
		if(newIntakeFormsCount!=0||consentFormsList.size()!=0)
			configBean.setHavingNewForms(true);
		else 
			configBean.setHavingNewForms(false);
		
		if(newIntakeFormsCount<=0)
			configBean.setHavingNewIntakeForms(false);
		else
			configBean.setHavingNewIntakeForms(true);
		
		if(consentFormsList.size()<=0)
			configBean.setHavingNewConsentForms(false);
		else
			configBean.setHavingNewConsentForms(true);
		
		/*Todays Appointments*/
		List<PortalSchedulerAppointmentBean> todaysApptList=portalAppointmentsService.getPatientAppointments(patientId, 15, 0,"PRESENT");
		
		if(todaysApptList.size()>0){
			
			configBean.setHavingAppointmentToday(true);
			configBean.setTodaysApptList(todaysApptList);
		}else
			configBean.setHavingAppointmentToday(false);
		
		/*Patient Balance*/
		
		PortalPatientPaymentsSummary portalPaymentsSummary=portalPaymentsService.getPatientPaymentsSummary(patientId);
		
		configBean.setPortalPatientPaymentsSummary(portalPaymentsSummary);
		
		String patientDue;
		
		if(portalPaymentsSummary.getPatientBalance()!=null&&portalPaymentsSummary.getDepositBalance()!=null){
			patientDue="$"+String.format("%.2f", (portalPaymentsSummary.getPatientBalance()-portalPaymentsSummary.getDepositBalance()));
			if((portalPaymentsSummary.getPatientBalance()-portalPaymentsSummary.getDepositBalance())>0.00)
				configBean.setHavingPaymentPending(true);
		}
		else{
			patientDue="$0.00";
			configBean.setHavingPaymentPending(false);
		}
		
		if(patientDue.equals("$.0"))
			patientDue="$0.00";
		configBean.setTotalPatientDue(patientDue);
		
		
		
		return configBean;
	}

	@Override
	public InsuranceFilterBean getInsuranceListList(InsuranceFilterBean insFilterBean) {
		
		List<InsCompAddr> insList=insCompAddrRepository.findAll(InsuranceSpecification.getInsurancesBy(insFilterBean));
		insFilterBean.setInsuranceList(insList);
		
		insFilterBean.setTotalInsurancesCount(insCompAddrRepository.count(InsuranceSpecification.getInsurancesBy(insFilterBean)));
				
		return insFilterBean;
	}

}
