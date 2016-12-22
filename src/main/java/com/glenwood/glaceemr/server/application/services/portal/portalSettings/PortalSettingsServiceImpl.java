package com.glenwood.glaceemr.server.application.services.portal.portalSettings;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

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
import com.glenwood.glaceemr.server.application.models.H807;
import com.glenwood.glaceemr.server.application.models.H809;
import com.glenwood.glaceemr.server.application.models.H810;
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
import com.glenwood.glaceemr.server.application.repositories.AlertEventRepository;
import com.glenwood.glaceemr.server.application.repositories.BillingConfigTableRepository;
import com.glenwood.glaceemr.server.application.repositories.BillinglookupRepository;
import com.glenwood.glaceemr.server.application.repositories.DemographicmodifyStatusRepository;
import com.glenwood.glaceemr.server.application.repositories.EmpProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.H807Repository;
import com.glenwood.glaceemr.server.application.repositories.H809Repository;
import com.glenwood.glaceemr.server.application.repositories.H810Respository;
import com.glenwood.glaceemr.server.application.repositories.InitialSettingsRepository;
import com.glenwood.glaceemr.server.application.repositories.InsCompAddrRepository;
import com.glenwood.glaceemr.server.application.repositories.InsCompanyRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientClinicalElementsQuestionsRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientPortalFeatureConfigRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientPortalMenuConfigRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.repositories.PosTableRepository;
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
	H807Repository h807Repository;

	@Autowired
	H810Respository h810Respository;
	
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
	H809Repository h809Repository;

	@Autowired
	SessionMap sessionMap;

	@Autowired
	ObjectMapper objectMapper;
	

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
	public PatientRegistration saveDemographicChanges(PatientRegistrationBean regSaveDetailsBean) {


		PatientRegistration registrationDetails=portalMedicalSummaryService.getPatientPersonalDetails(regSaveDetailsBean.getPatientRegistrationId());

		H807 demographicChanges=new H807();
		if(regSaveDetailsBean.getPatientRegistrationFirstName()!=null&&!regSaveDetailsBean.getPatientRegistrationFirstName().equals(registrationDetails.getPatientRegistrationFirstName()))
			demographicChanges.setH807002(regSaveDetailsBean.getPatientRegistrationFirstName());
		else
			demographicChanges.setH807002("");

		if(regSaveDetailsBean.getPatientRegistrationLastName()!=null&&!regSaveDetailsBean.getPatientRegistrationLastName().equals(registrationDetails.getPatientRegistrationLastName()))
			demographicChanges.setH807003(regSaveDetailsBean.getPatientRegistrationLastName());
		else 
			demographicChanges.setH807003("");

		if(regSaveDetailsBean.getPatientRegistrationMidInitial()!=null&&!regSaveDetailsBean.getPatientRegistrationMidInitial().equals(registrationDetails.getPatientRegistrationMidInitial()))
			demographicChanges.setH807004(regSaveDetailsBean.getPatientRegistrationMidInitial());
		else
			demographicChanges.setH807004("");

		if(regSaveDetailsBean.getPatientRegistrationWorkNo()!=null&&!regSaveDetailsBean.getPatientRegistrationWorkNo().equals(registrationDetails.getPatientRegistrationWorkNo()))
			demographicChanges.setH807005(regSaveDetailsBean.getPatientRegistrationWorkNo());
		else
			demographicChanges.setH807005("");

		if(regSaveDetailsBean.getPatientRegistrationDob()!=null&&!regSaveDetailsBean.getPatientRegistrationDob().equals(registrationDetails.getPatientRegistrationDob()))
			demographicChanges.setH807006(regSaveDetailsBean.getPatientRegistrationDob());
		else
			demographicChanges.setH807006("");

		if(regSaveDetailsBean.getPatientRegistrationSex()!=null&&!regSaveDetailsBean.getPatientRegistrationSex().equals(registrationDetails.getPatientRegistrationSex()))
			demographicChanges.setH807007(regSaveDetailsBean.getPatientRegistrationSex());
		else{
			if(regSaveDetailsBean.getPatientRegistrationSex()!=null)
				demographicChanges.setH807007(regSaveDetailsBean.getPatientRegistrationSex());
			else
				demographicChanges.setH807007(-1);
		}

		if(regSaveDetailsBean.getPatientRegistrationMaritalstatus()!=null&&!regSaveDetailsBean.getPatientRegistrationMaritalstatus().equals(registrationDetails.getPatientRegistrationMaritalstatus()))
			demographicChanges.setH807008(regSaveDetailsBean.getPatientRegistrationMaritalstatus());
		else{
			if(regSaveDetailsBean.getPatientRegistrationMaritalstatus()!=null)
				demographicChanges.setH807008(regSaveDetailsBean.getPatientRegistrationMaritalstatus());
			else
				demographicChanges.setH807008(-1);
		}

		if(regSaveDetailsBean.getPatientRegistrationPhoneNo()!=null&&!regSaveDetailsBean.getPatientRegistrationPhoneNo().equals(registrationDetails.getPatientRegistrationPhoneNo()))
			demographicChanges.setH807009(regSaveDetailsBean.getPatientRegistrationPhoneNo());
		else
			demographicChanges.setH807009("");

		if(!(regSaveDetailsBean.getPatientRegistrationAddress1()+regSaveDetailsBean.getPatientRegistrationAddress2()).equals(registrationDetails.getPatientRegistrationAddress1()+registrationDetails.getPatientRegistrationAddress2()))
			demographicChanges.setH807010(regSaveDetailsBean.getPatientRegistrationAddress1()+regSaveDetailsBean.getPatientRegistrationAddress2());
		else
			demographicChanges.setH807010("");

		if(regSaveDetailsBean.getPatientRegistrationCity()!=null&&!regSaveDetailsBean.getPatientRegistrationCity().equals(registrationDetails.getPatientRegistrationCity()))
			demographicChanges.setH807011(regSaveDetailsBean.getPatientRegistrationCity());
		else
			demographicChanges.setH807011("");

		if(regSaveDetailsBean.getPatientRegistrationState()!=null&&!regSaveDetailsBean.getPatientRegistrationState().equals(registrationDetails.getPatientRegistrationState()))
			demographicChanges.setH807012(regSaveDetailsBean.getPatientRegistrationState());
		else{
			if(regSaveDetailsBean.getPatientRegistrationState()!=null)
				demographicChanges.setH807012(regSaveDetailsBean.getPatientRegistrationState());
			else
				demographicChanges.setH807012("-1");
		}

		if(regSaveDetailsBean.getPatientRegistrationZip()!=null&&!regSaveDetailsBean.getPatientRegistrationZip().equals(registrationDetails.getPatientRegistrationZip()))
			demographicChanges.setH807013(regSaveDetailsBean.getPatientRegistrationZip());
		else
			demographicChanges.setH807013("");

		if(regSaveDetailsBean.getPatientRegistrationMailId()!=null&&!regSaveDetailsBean.getPatientRegistrationMailId().equals(registrationDetails.getPatientRegistrationMailId()))
			demographicChanges.setH807014(regSaveDetailsBean.getPatientRegistrationMailId());
		else
			demographicChanges.setH807014("");



		demographicChanges.setH807015("");
		demographicChanges.setH807016("");
		demographicChanges.setH807017("");
		demographicChanges.setH807018("-1");
		demographicChanges.setH807019(-1);
		demographicChanges.setH807020("");
		demographicChanges.setH807021(-1);
		demographicChanges.setH807022("");
		demographicChanges.setH807023("");
		demographicChanges.setH807024("-1");
		demographicChanges.setH807025("");
		demographicChanges.setH807026("");
		demographicChanges.setH807027("");
		demographicChanges.setH807028("");
		demographicChanges.setH807029("");
		demographicChanges.setH807030("");
		demographicChanges.setH807031("");
		demographicChanges.setH807032("");
		demographicChanges.setH807033(-1);
		demographicChanges.setH807034("");
		demographicChanges.setH807035("");
		demographicChanges.setH807036("");
		demographicChanges.setH807037("");
		demographicChanges.setH807038("");
		demographicChanges.setH807039("");
		demographicChanges.setH807040("");
		demographicChanges.setH807041("");
		demographicChanges.setH807042(-1);
		demographicChanges.setH807043("");
		demographicChanges.setH807044("");
		demographicChanges.setH807045(regSaveDetailsBean.getPatientRegistrationId());
		demographicChanges.setH807046(false);
		demographicChanges.setH807047(false);
		demographicChanges.setH807048(0.00);


		H807 demographicEntry=h807Repository.saveAndFlush(demographicChanges);


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


		H810 demographicAlertCategory=h810Respository.findOne(PortalSettingsSpecification.getPortalAlertCategory(3));
		boolean sendToAll =demographicAlertCategory.getH810005();  
		int provider = Integer.parseInt(demographicAlertCategory.getH810003());
		int forwardTo = Integer.parseInt(demographicAlertCategory.getH810004());

		AlertEvent alert=new AlertEvent();
		alert.setAlertEventCategoryId(22);
		alert.setAlertEventStatus(1);
		alert.setAlertEventPatientId(regSaveDetailsBean.getPatientRegistrationId());
		alert.setAlertEventPatientName(registrationDetails.getPatientRegistrationLastName()+", "+registrationDetails.getPatientRegistrationFirstName());
		alert.setAlertEventEncounterId(-1);
		alert.setAlertEventRefId(demographicEntry.getH807001());
		alert.setAlertEventMessage("Request to change the Demographics Details.");
		alert.setAlertEventRoomId(-1);
		alert.setAlertEventCreatedDate(new Timestamp(new Date().getTime()));
		alert.setAlertEventModifiedby(-100);
		alert.setAlertEventFrom(-100);

		if(provider==0 && forwardTo==0)
			alert.setAlertEventTo(-1);
		else {
			if(sendToAll){
				if(forwardTo!=provider){
					alert.setAlertEventTo(forwardTo);
					AlertEvent alert2=alert;
					alert2.setAlertEventTo(provider);
					alertEventRepository.saveAndFlush(alert2);
				} else {
					alert.setAlertEventTo(forwardTo);
				}            	 
			}else{
				if(forwardTo!=0){
					alert.setAlertEventTo(forwardTo);
				} else {
					alert.setAlertEventTo(provider);
				}
			}
		}

		alertEventRepository.saveAndFlush(alert);

		registrationDetails=portalMedicalSummaryService.getPatientPersonalDetails(regSaveDetailsBean.getPatientRegistrationId());

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

		H809 patientDetails=h809Repository.findOne(PortalLoginSpecification.activePatientById(patientId));

		EncryptedPatientDetails loginDetailsBean=new EncryptedPatientDetails();

		loginDetailsBean.setAuthString(patientDetails.getH809004()+"*&@#"+patientDetails.getH809005());
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
