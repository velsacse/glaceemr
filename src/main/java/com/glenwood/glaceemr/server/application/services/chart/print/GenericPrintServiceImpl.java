package com.glenwood.glaceemr.server.application.services.chart.print;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.BillingConfigTable;
import com.glenwood.glaceemr.server.application.models.Billinglookup;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.H076;
import com.glenwood.glaceemr.server.application.models.InitialSettings;
import com.glenwood.glaceemr.server.application.models.InsCompAddr;
import com.glenwood.glaceemr.server.application.models.InsCompany;
import com.glenwood.glaceemr.server.application.models.LeafLibrary;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PlaceOfService;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.PosType;
import com.glenwood.glaceemr.server.application.models.print.GenericPrintStyle;
import com.glenwood.glaceemr.server.application.repositories.BillingConfigTableRepository;
import com.glenwood.glaceemr.server.application.repositories.BillinglookupRepository;
import com.glenwood.glaceemr.server.application.repositories.EmpProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.EncounterRepository;
import com.glenwood.glaceemr.server.application.repositories.InitialSettingsRepository;
import com.glenwood.glaceemr.server.application.repositories.LeafLibraryRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientInsDetailsRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.repositories.PosTableRepository;
import com.glenwood.glaceemr.server.application.repositories.print.GenericPrintStyleRepository;
import com.glenwood.glaceemr.server.application.services.chart.insurance.InsuranceDataBean;
import com.glenwood.glaceemr.server.application.services.employee.EmployeeDataBean;
import com.glenwood.glaceemr.server.application.services.patient.PatientDataBean;
import com.glenwood.glaceemr.server.application.services.pos.DefaultPracticeBean;
import com.glenwood.glaceemr.server.application.services.pos.PosDataBean;
import com.glenwood.glaceemr.server.application.specifications.BillingConfigTableSpecification;
import com.glenwood.glaceemr.server.application.specifications.BillingLookupSpecification;
import com.glenwood.glaceemr.server.application.specifications.EmployeeSpecification;
import com.glenwood.glaceemr.server.application.specifications.EncounterSpecification;
import com.glenwood.glaceemr.server.application.specifications.InitialSettingsSpecification;
import com.glenwood.glaceemr.server.application.specifications.LeafLibrarySpecification;
import com.glenwood.glaceemr.server.application.specifications.PatientInsDetailsSpecification;
import com.glenwood.glaceemr.server.application.specifications.PatientRegistrationSpecification;
import com.glenwood.glaceemr.server.application.specifications.PosTableSpecification;
import com.glenwood.glaceemr.server.application.specifications.print.GenericPrintSpecification;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

@Service
public class GenericPrintServiceImpl implements GenericPrintService{

	@Autowired
	GenericPrintStyleRepository genericPrintStyleRepository;

	@Autowired
	PatientRegistrationRepository patientRegistrationRepository;
	
	@Autowired
	EncounterRepository encounterRepository;
	
	@Autowired
	EmpProfileRepository employeeProfileRepository;
	
	@Autowired
	PosTableRepository posTableRepository;
	
	@Autowired
	BillingConfigTableRepository billingConfigTableRepository;
	
	@Autowired
	BillinglookupRepository billinglookupRepository;
	
	@Autowired
	GenerateHeaderBean generateHeaderBean;

	@Autowired
	GenerateFooterBean generateFooterBean;

	@Autowired
	GeneratePDFBean generatePDFBean;
	
	@Autowired
	TextFormatter textFormat;

	@Autowired
	InitialSettingsRepository initialSettingsRepository;
	
	@Autowired
	LeafLibraryRepository leafLibraryRepository;
	
	@Autowired
	PatientInsDetailsRepository patientInsDetailsRepository;
	
	@Override
	public List<GenericPrintStyle> getGenericPrintStyleList() {
		List<GenericPrintStyle> genericPrintStyleList=genericPrintStyleRepository.findAll();
		return genericPrintStyleList;
	}

	@Override
	public GenericPrintStyle getGenericPrintStyle(Integer styleId) {
		GenericPrintStyle genericPrintStyle=genericPrintStyleRepository.findOne(styleId);
		return genericPrintStyle;
	}

	@Override
	public GenericPrintStyle saveGenericPrintStyle(GenericPrintStyle genericPrintStyle) {
		GenericPrintStyle savedGenericPrintStyle=genericPrintStyleRepository.save(genericPrintStyle);
		return savedGenericPrintStyle;
	}

	@Override
	public void generatePDFPreview(Integer styleId) {
		try{
			GenericPrintStyle genericPrintStyle=genericPrintStyleRepository.findOne(styleId);
			int letterHeaderId=genericPrintStyle.getGenericPrintStyleHeaderId();
			int patientHeaderId=genericPrintStyle.getGenericPrintStylePatientHeaderId();
			String patientHeaderPage1;
			String patientHeaderHTML=generateHeaderBean.generatePatientHeader(patientHeaderId, 1);
			if(generateHeaderBean.getPatientHeaderType(patientHeaderId)==2){
				patientHeaderPage1=generateHeaderBean.generatePatientHeader(patientHeaderId, 2);
			}else{
				patientHeaderPage1=patientHeaderHTML;
			}

			String headerHTML="";
			if(letterHeaderId>0){
				headerHTML=generateHeaderBean.generateHeader(letterHeaderId);
			}
			int footerId=genericPrintStyle.getGenericPrintStyleFooterId();
			String footerHTML="";
			int pageVariant=0;
			if(footerId>0){
				footerHTML=generateFooterBean.generateFooter(footerId);
				pageVariant=generateFooterBean.getPageFormatForFooter(footerId);
			}

			generatePDFBean.generatePDF(headerHTML,patientHeaderPage1,patientHeaderHTML,footerHTML,pageVariant);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public String[] generatePatentDetailsArr(PatientDataBean patientBean){
		String[] patientDetailsArr=new String[20];
		//"Patient Name", "Age", "DOS", "Gender","Account #","Phone #","DOB","Mobile #","Address","Referring Doctor","Insurance Details","Supervising doctor",
		//"Service/Performing Doctor","Ethinicity","Race","Preffered/Primary Language","Care Group"
		patientDetailsArr[0]= isNull(patientBean.getPatientName());
		patientDetailsArr[1]= isNull(patientBean.getAge());
		patientDetailsArr[2]= isNull(patientBean.getDos());
		patientDetailsArr[3]= isNull(patientBean.getGender());
		patientDetailsArr[4]= isNull(patientBean.getAccountId());
		patientDetailsArr[5]= isNull(patientBean.getPhNum());
		patientDetailsArr[6]= isNull(patientBean.getDob());
		patientDetailsArr[7]= isNull(patientBean.getMobileNum());
		patientDetailsArr[8]= isNull(patientBean.getAddress());
		patientDetailsArr[9]= isNull(patientBean.getServiceReferral());
		patientDetailsArr[10]= getInsuranceName(patientBean);
		patientDetailsArr[11]=isNull(patientBean.getPrincipalDr().getEmpFullname());
		patientDetailsArr[12]=isNull(patientBean.getServiceDr().getEmpFullname());
		patientDetailsArr[13]=isNull(patientBean.getEthinicity());
		patientDetailsArr[14]=isNull(patientBean.getRace());
		patientDetailsArr[15]=isNull(patientBean.getPrefLang());
		
		return patientDetailsArr;
		
	}

	@Override
	public void generatePDFPreview(Integer styleId,Integer patientId/*,	PrintDetailsDataBean printDetails*/) {
		try{
			GenericPrintStyle genericPrintStyle=genericPrintStyleRepository.findOne(styleId);
			int letterHeaderId=genericPrintStyle.getGenericPrintStyleHeaderId();
			int patientHeaderId=genericPrintStyle.getGenericPrintStylePatientHeaderId();
			String patientHeaderPage1;
			
			PatientRegistration patientDetails= patientRegistrationRepository.findOne(GenericPrintSpecification.getPatientDetails(patientId));
			List<PatientInsDetail> insuranceList= patientInsDetailsRepository.findAll(PatientInsDetailsSpecification.getByPatientId(patientId));
			int encounterId=7328;//printDetails.getEncounterId();
			Encounter encounter = encounterRepository.findOne(EncounterSpecification.EncounterById(encounterId, true));
			PatientDataBean patientBean = parsePatientDetails(patientDetails, encounter, insuranceList);
			String[] patientDetailsArr=generatePatentDetailsArr(patientBean);
			String patientHeaderHTML=generateHeaderBean.generatePatientHeader(patientHeaderId, 1,patientDetailsArr);
			if(generateHeaderBean.getPatientHeaderType(patientHeaderId)==2){
				patientHeaderPage1=generateHeaderBean.generatePatientHeader(patientHeaderId, 2,patientDetailsArr);
			}else{
				patientHeaderPage1=patientHeaderHTML;
			}

			String headerHTML="";
			if(letterHeaderId>0){
				headerHTML=generateHeaderBean.generateHeader(letterHeaderId);
			}
			int footerId=genericPrintStyle.getGenericPrintStyleFooterId();
			String footerHTML="";
			int pageVariant=0;
			if(footerId>0){
				footerHTML=generateFooterBean.generateFooter(footerId);
				pageVariant=generateFooterBean.getPageFormatForFooter(footerId);
			}
			int pageOrientation=1;//landscape
			Rectangle pageSize=PageSize.A4;
			generatePDFBean.generatePDF(headerHTML,patientHeaderPage1,patientHeaderHTML,footerHTML,pageVariant,pageSize,pageOrientation);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public GenericPrintBean getCompleteDetails(Integer patientId, Integer encounterId) throws Exception {
		
		List<Integer> insTypeList = new ArrayList<Integer>();
		insTypeList.add(1);
		insTypeList.add(2);
		
		PatientRegistration patientDetails= patientRegistrationRepository.findOne(GenericPrintSpecification.getPatientDetails(patientId,insTypeList));
		List<PatientInsDetail> insuranceList= patientInsDetailsRepository.findAll(PatientInsDetailsSpecification.getByPatientId(patientId));
		Encounter encounter = encounterRepository.findOne(EncounterSpecification.EncounterById(encounterId, true));
		List<EmployeeProfile> empList =  employeeProfileRepository.findAll(EmployeeSpecification.getUsersList("asc"));
		List<PosTable> posList = posTableRepository.findAll(PosTableSpecification.getPOSDetails());
		
		PatientDataBean patientBean = parsePatientDetails(patientDetails, encounter, insuranceList);
		List<EmployeeDataBean> empolyeeBean = parseEmployeeDetails(empList);
		List<PosDataBean> posBean = parsePOSDetails(posList);
		List<InitialSettings> initialList= initialSettingsRepository.findAll(Specifications.where(InitialSettingsSpecification.optionType(4)).and(InitialSettingsSpecification.optionVisible(true)));
		
		DefaultPracticeBean practiceBean = parsePracticeDetails(initialList);
		GenericPrintBean genericBean = new GenericPrintBean(patientBean, empolyeeBean, posBean, practiceBean);
		return genericBean;
				
	}

	/**
	 * Parsing Practice Details
	 * @param initialList
	 * @return
	 */
	private DefaultPracticeBean parsePracticeDetails(List<InitialSettings> initialList) {
		
		DefaultPracticeBean practiceBean = new DefaultPracticeBean();
		for(int i=0; i<initialList.size(); i++){
			if(initialList.get(i).getInitialSettingsOptionName() != null && !initialList.get(i).getInitialSettingsOptionName().trim().isEmpty()){
				if(initialList.get(i).getInitialSettingsOptionName().trim().equalsIgnoreCase("Doctor Message"))
					practiceBean.setPracticeDrMsg(initialList.get(i).getInitialSettingsOptionValue());
				else if(initialList.get(i).getInitialSettingsOptionName().trim().equalsIgnoreCase("Practice Name"))
					practiceBean.setPracticeName(initialList.get(i).getInitialSettingsOptionValue());
				else if(initialList.get(i).getInitialSettingsOptionName().trim().equalsIgnoreCase("Address"))
					practiceBean.setPracticeStreet(initialList.get(i).getInitialSettingsOptionValue());
				else if(initialList.get(i).getInitialSettingsOptionName().trim().equalsIgnoreCase("city"))
					practiceBean.setPracticeCity(initialList.get(i).getInitialSettingsOptionValue());
				else if(initialList.get(i).getInitialSettingsOptionName().trim().equalsIgnoreCase("state")){
					practiceBean.setPracticeName(initialList.get(i).getInitialSettingsOptionValue());
					List<BillingConfigTable> billing = billingConfigTableRepository.findAll(BillingConfigTableSpecification.getState(initialList.get(i).getInitialSettingsOptionValue()));
					if(billing != null && billing.size()>0)
						practiceBean.setPracticeState(billing.get(0).getBillingConfigTableLookupDesc());
				}
				else if(initialList.get(i).getInitialSettingsOptionName().trim().equalsIgnoreCase("zipCode"))
					practiceBean.setPracticeZip(initialList.get(i).getInitialSettingsOptionValue());
				else if(initialList.get(i).getInitialSettingsOptionName().trim().equalsIgnoreCase("Web Address"))
					practiceBean.setPracticeWebAddress(initialList.get(i).getInitialSettingsOptionValue());
				else if(initialList.get(i).getInitialSettingsOptionName().trim().equalsIgnoreCase("email Address"))
					practiceBean.setPracticeEmail(initialList.get(i).getInitialSettingsOptionValue());
				else if(initialList.get(i).getInitialSettingsOptionName().trim().equalsIgnoreCase("Phone No"))
					practiceBean.setPracticePhoneNum(initialList.get(i).getInitialSettingsOptionValue());
				else if(initialList.get(i).getInitialSettingsOptionName().trim().equalsIgnoreCase("Fax Number"))
					practiceBean.setPracticeFaxNum(initialList.get(i).getInitialSettingsOptionValue());
			}
		}
		return practiceBean;
	}

	/**
	 * Parsing Patient Details
	 * @param patientDetails
	 * @param encounter
	 * @return
	 * @throws Exception
	 */
	private PatientDataBean parsePatientDetails(PatientRegistration patientDetails, Encounter encounter, List<PatientInsDetail> practiceList) throws Exception {
		String patientName = textFormat.getFormattedName(patientDetails.getPatientRegistrationFirstName(), patientDetails.getPatientRegistrationMidInitial(), patientDetails.getPatientRegistrationLastName(), "");
		String age = textFormat.getAge(patientDetails.getPatientRegistrationDob());
		String dos = null;
		String gender = patientDetails.getPatientRegistrationSex().toString();
		String accountId = patientDetails.getPatientRegistrationAccountno();
		String phNum = formatPhoneNum(patientDetails.getPatientRegistrationPhoneNo());
		String dob = textFormat.getFormattedDate(patientDetails.getPatientRegistrationDob());
		String mobileNum = formatPhoneNum(patientDetails.getPatientRegistrationCellno());
		String state = patientDetails.getPatientRegistrationState();
		String address = null;
		String refPhyName = null;
		String serviceRefName = null;
		String ethinicity = patientDetails.getPatientRegistrationEthnicity().toString();
		String race = patientDetails.getPatientRegistrationRace();
		String prefLang = patientDetails.getPatientRegistrationPreferredLan();
		
		if(encounter != null) {
			dos = textFormat.getFormattedDate(encounter.getEncounterDate());
		}
		
		List<BillingConfigTable> billing = billingConfigTableRepository.findAll(BillingConfigTableSpecification.getStateGender(state, gender));
		state = "";
		gender = "";
		if(billing != null) {
			for(int i=0; i<billing.size(); i++) {
				BillingConfigTable bill = billing.get(i);
				if(bill.getBillingConfigTableLookupId() != null && bill.getBillingConfigTableLookupId() == 5001) {
					state = bill.getBillingConfigTableLookupDesc();
				}
				if(bill.getBillingConfigTableLookupId() != null && bill.getBillingConfigTableLookupId() == 51) {
					gender = bill.getBillingConfigTableLookupDesc();
				}
			}
		}
		
		address = textFormat.getAddress(patientDetails.getPatientRegistrationAddress1(),patientDetails.getPatientRegistrationAddress2(),patientDetails.getPatientRegistrationCity(),state,patientDetails.getPatientRegistrationZip());
		H076 refPhyEntity = patientDetails.getReferringPhyTable();
		refPhyName = "";
		if(refPhyEntity != null)
			refPhyName = textFormat.getFormattedName(refPhyEntity.getH076005(), refPhyEntity.getH076004(), refPhyEntity.getH076003(), refPhyEntity.getH076021());
		H076 serviceRefEntity = encounter.getReferringTable();
		serviceRefName = "";
		if(serviceRefEntity != null)
			serviceRefName = textFormat.getFormattedName(serviceRefEntity.getH076005(), serviceRefEntity.getH076004(), serviceRefEntity.getH076003(), serviceRefEntity.getH076021());
		
		List<InsuranceDataBean> insuranceBean = parseInsuranceDetails(practiceList);
		
		EmployeeDataBean principalDrData = null;
		EmployeeProfile employee = patientDetails.getEmpProfile();
		if(employee != null){
			principalDrData = parseDoctorDetails(employee);
		}
		
		List<Billinglookup> billingEthinicity = billinglookupRepository.findAll(BillingLookupSpecification.getDetails(ethinicity, race, prefLang));
		ethinicity = "";
		race = "";
		prefLang = "";
		if(billingEthinicity != null) {
			for(int i=0; i<billingEthinicity.size(); i++) {
				Billinglookup bill = billingEthinicity.get(i);
				if(bill.getBlookGroup() != null && bill.getBlookGroup() == 251) {
					ethinicity = bill.getBlookName();
				}
				if(bill.getBlookGroup() != null && bill.getBlookGroup() == 250) {
					race = bill.getBlookName();
				}
				if(bill.getBlookGroup() != null && bill.getBlookGroup() == 253) {
					prefLang = bill.getBlookName();
				}
			}
		}
		
		EmployeeDataBean serviceDrData = null;
		if(encounter != null) {
			EmployeeProfile emp = encounter.getEmpProfileEmpId();
			serviceDrData = parseDoctorDetails(emp);
            
        }
		
		
		Integer encounterId = null, patientId = null;
		if(encounter != null)
			encounterId = encounter.getEncounterId();
		if(patientDetails != null) {
			patientId = patientDetails.getPatientRegistrationId();
		}
		List<PosTable> posList = null;		
		if(encounter != null)
			posList = posTableRepository.findAll(PosTableSpecification.getPOSDetailsById(encounter.getEncounterPos()));
		List<PosDataBean> posBean = parsePOSDetails(posList);
        PatientDataBean bean = new PatientDataBean(patientName, age, dos, gender, accountId, phNum, dob,
                                                   mobileNum, address, refPhyName, serviceRefName, insuranceBean, posBean, principalDrData,
                                                   serviceDrData, ethinicity, race, prefLang, patientId, encounterId);
		
		return bean;
	}


	/**
	 * Parsing service doctor details
	 * @param encounter
	 * @return
	 * @throws Exception
	 */
	private EmployeeDataBean parseDoctorDetails(EmployeeProfile emp) throws Exception {
		EmployeeDataBean doctorData = null;		
		String name = "", state = "";
        if(emp != null) {
            name = textFormat.getFormattedName(emp.getEmpProfileFname(), emp.getEmpProfileMi(), emp.getEmpProfileLname(), emp.getEmpProfileCredentials());
        }
        List<BillingConfigTable> billing = billingConfigTableRepository.findAll(BillingConfigTableSpecification.getState(emp.getEmpProfileState()));
		if(billing != null && billing.size()>0)
			state = billing.get(0).getBillingConfigTableLookupDesc();
        doctorData = new EmployeeDataBean(emp.getEmpProfileEmpid(), -1, name, emp.getEmpProfileAddress(), state, emp.getEmpProfileCity(), emp.getEmpProfileZip(), emp.getEmpProfilePhoneno(), emp.getEmpProfileMailid());
        return doctorData;
	}

	/**
	 * Parsing Employee Details
	 * @param empList
	 * @param textFormat
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<EmployeeDataBean> parseEmployeeDetails(List<EmployeeProfile> empList) throws Exception {
		
		List<EmployeeDataBean> empBean = new ArrayList<EmployeeDataBean>();
		
		for(int i=0; i<empList.size(); i++) {
			EmployeeProfile emp = empList.get(i);
			
			if(emp != null) {
				int empId = emp.getEmpProfileEmpid();
				int loginId = emp.getEmpProfileLoginid();
				String state = emp.getEmpProfileState();
				BillingConfigTable billing = billingConfigTableRepository.findOne(BillingConfigTableSpecification.getState(state));
				state = "";
				if(billing != null)
					state = billing.getBillingConfigTableLookupDesc();
				String fullName = textFormat.getFormattedName(emp.getEmpProfileFname(), emp.getEmpProfileMi(), emp.getEmpProfileLname(), emp.getEmpProfileCredentials());
//				String fullAddress = textFormat.getAddress(emp.getEmpProfileAddress(), "", emp.getEmpProfileCity(), state, emp.getEmpProfileZip());

				empBean.add(new EmployeeDataBean(empId, loginId, fullName, emp.getEmpProfileAddress(), state, emp.getEmpProfileCity(), emp.getEmpProfileZip(), emp.getEmpProfilePhoneno(), emp.getEmpProfileMailid()));
			}
		}
		
		return empBean;
	}

	@Override
	public EmployeeDataBean parseEmployeeDetail(EmployeeProfile emp) throws Exception {
		
		EmployeeDataBean empBean = null;

		if(emp != null) {
			int empId = emp.getEmpProfileEmpid();
			int loginId = emp.getEmpProfileLoginid();
			String state = emp.getEmpProfileState();
			BillingConfigTable billing = billingConfigTableRepository.findOne(BillingConfigTableSpecification.getState(state));
			state = "";
			if(billing != null)
				state = billing.getBillingConfigTableLookupDesc();
			String fullName = textFormat.getFormattedName(emp.getEmpProfileFname(), emp.getEmpProfileMi(), emp.getEmpProfileLname(), emp.getEmpProfileCredentials());
//			String fullAddress = textFormat.getAddress(emp.getEmpProfileAddress(), "", emp.getEmpProfileCity(), state, emp.getEmpProfileZip());

			empBean = new EmployeeDataBean(empId, loginId, fullName, emp.getEmpProfileAddress(), state, emp.getEmpProfileCity(), emp.getEmpProfileZip(), emp.getEmpProfilePhoneno(), emp.getEmpProfileMailid());
		}

		return empBean;
	}

	
	/**
	 * Parsing Insurance Details
	 * @param patientDetails
	 * @param textFormat
	 * @return
	 */
	private List<InsuranceDataBean> parseInsuranceDetails(List<PatientInsDetail> patientIns) {
		List<InsuranceDataBean> insuranceBean = new ArrayList<InsuranceDataBean>();
		if (patientIns != null)
			for (int i = 0; i < patientIns.size(); i++) {
				PatientInsDetail insCompany = patientIns.get(i);
				String insId = insCompany.getPatientInsDetailPatientinsuranceid();
				Integer insType = insCompany.getPatientInsDetailInstype();
				if (insCompany != null) {
					InsCompAddr insCompAddr = insCompany.getInsCompAddr();
					if (insCompAddr != null) {
						String insAddress = isNull(insCompAddr.getInsCompAddrAddress());
						String insCity = isNull(insCompAddr.getInsCompAddrCity());
						String insState = isNull(insCompAddr.getInsCompAddrState());
						String insZip = isNull(insCompAddr.getInsCompAddrZip());
						String insCompId = null;
						String insCompName = null;						
						InsCompany insComp = insCompAddr.getInsCompany();
						if (insComp != null) {
							insCompId = insComp.getInsCompanyId().toString();
							insCompName = insComp.getInsCompanyName();
						}

						insuranceBean.add(new InsuranceDataBean(insId, insType, insCompId, insCompName, insAddress, insCity, insState, insZip));
					}
				}
			}
		return insuranceBean;
	}
	
	private String isNull(String value) {
		if(value == null)
			return "";
		else
			return value.trim();
	}

	/**
     * Parsing POS details
     * @param posList
     * @param textFormat
     * @return
     */
	@Override
	public List<PosDataBean> parsePOSDetails(List<PosTable> posList) {

		List<PosDataBean> posBean = new ArrayList<PosDataBean>();
		if(posList != null){
			for(int i = 0; i < posList.size(); i++) {
				PosTable pos = posList.get(i);
				int relationId = pos.getPosTableRelationId();
				int placeId = pos.getPosTablePlaceId();
				String practice = pos.getPosTablePlaceOfService();
				String comments = pos.getPosTableFacilityComments();
				String name = "";
				if(practice != null && !practice.isEmpty())
					name = "(" + practice + ") ";
				if(comments != null && !comments.isEmpty())
					name = name + comments;

				String address = null, state = null, city = null, zip = null, phNum = null, faxNum = null;           
				PlaceOfService placeofService = pos.getPlaceOfService();
				if(placeofService != null) {
					state = placeofService.getPlaceOfServiceState();
					BillingConfigTable billing = billingConfigTableRepository.findOne(BillingConfigTableSpecification.getState(state));
					state = "";
					if(billing != null)
						state = billing.getBillingConfigTableLookupDesc();

					address = placeofService.getPlaceOfServiceAddress();
					city = placeofService.getPlaceOfServiceCity();
					zip = placeofService.getPlaceOfServiceZip();
					phNum = placeofService.getPlaceOfServicePhone();
					faxNum = placeofService.getPlaceOfServiceFax();
				}

				String type = "";
				PosType posType = pos.getPosType();
				if(posType != null) {
					type = posType.getPosTypeTypeName();
				}

				posBean.add(new PosDataBean(relationId, placeId, name, address, state, city, zip, phNum, faxNum, type));
			}
		}
		return posBean;
	}

	@Override
	public PosDataBean parsePOSDetail(PosTable pos) {

		PosDataBean posBean = null;
		if(pos != null) {
			int relationId = pos.getPosTableRelationId();
			int placeId = pos.getPosTablePlaceId();
			String practice = pos.getPosTablePlaceOfService();
			String comments = pos.getPosTableFacilityComments();
			String name = "";
			if(practice != null && !practice.isEmpty())
				name = "(" + practice + ") ";
			if(comments != null && !comments.isEmpty())
				name = name + comments;

			String address = null, state = null, city = null, zip = null, phNum = null, faxNum = null;           
			PlaceOfService placeofService = pos.getPlaceOfService();
			if(placeofService != null) {
				state = placeofService.getPlaceOfServiceState();
				BillingConfigTable billing = billingConfigTableRepository.findOne(BillingConfigTableSpecification.getState(state));
				state = "";
				if(billing != null)
					state = billing.getBillingConfigTableLookupDesc();

				address = placeofService.getPlaceOfServiceAddress();
				city = placeofService.getPlaceOfServiceCity();
				zip = placeofService.getPlaceOfServiceZip();
				phNum = placeofService.getPlaceOfServicePhone();
				faxNum = placeofService.getPlaceOfServiceFax();
			}

			String type = "";
			PosType posType = pos.getPosType();
			if(posType != null) {
				type = posType.getPosTypeTypeName();
			}

			posBean= new PosDataBean(relationId, placeId, name, address, state, city, zip, phNum, faxNum, type);
		}
		return posBean;
	}
	
    /**
     * Method to get Header HTML as String
     * @throws Exception 
     */
    @Override
    public String getHeaderHTML(Integer styleId, Integer patientId, Integer encounterId,  String sharedPath) throws Exception{
    	String headerHTML = "";
    	try{

    		GenericPrintBean genericPrintBean = getCompleteDetails(patientId, encounterId);
    		GenericPrintStyle genericPrintStyle=genericPrintStyleRepository.findOne(styleId);
    		int letterHeaderId=genericPrintStyle.getGenericPrintStyleHeaderId();
    		if(letterHeaderId>0){
    			headerHTML=generateHeaderBean.generateHeader(letterHeaderId, sharedPath, genericPrintBean);
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		return "failure";
    	}
    	return headerHTML;
    }
  
    @Override
    public String getPatientHeaderHTML(Integer styleId, Integer patientId, Integer encounterId) throws Exception {
    	
    	List<Integer> insTypeList = new ArrayList<Integer>();
		insTypeList.add(1);
		insTypeList.add(2);
		
    	GenericPrintStyle genericPrintStyle=genericPrintStyleRepository.findOne(styleId);
		int patientHeaderId=genericPrintStyle.getGenericPrintStylePatientHeaderId();
		
    	String patientHeaderHTML = "";
    	PatientRegistration patientDetails= patientRegistrationRepository.findOne(GenericPrintSpecification.getPatientDetails(patientId, insTypeList));
    	List<PatientInsDetail> insuranceList= patientInsDetailsRepository.findAll(PatientInsDetailsSpecification.getByPatientId(patientId));
//		int encounterId=7328;//printDetails.getEncounterId();
		Encounter encounter = encounterRepository.findOne(EncounterSpecification.EncounterById(encounterId, true));
		PatientDataBean patientBean = parsePatientDetails(patientDetails, encounter, insuranceList);
		String[] patientDetailsArr=generatePatentDetailsArr(patientBean);
		
		if(generateHeaderBean.getPatientHeaderType(patientHeaderId)==2){
			patientHeaderHTML = generateHeaderBean.generatePatientHeader(patientHeaderId, 2,patientDetailsArr);
		}else{
			patientHeaderHTML = generateHeaderBean.generatePatientHeader(patientHeaderId, 1,patientDetailsArr);
		}
		
		return patientHeaderHTML;
    }
    
    @Override
    public String getFooterHTML(Integer styleId){
    	
    	String footerHTML = "";
    	GenericPrintStyle genericPrintStyle=genericPrintStyleRepository.findOne(styleId);
    	int footerId=genericPrintStyle.getGenericPrintStyleFooterId();
//		int pageVariant=0;
		if(footerId>0){
			footerHTML=generateFooterBean.generateFooter(footerId);
//			pageVariant=generateFooterBean.getPageFormatForFooter(footerId);
		}    	
    	return footerHTML;
    }

	@Override
	public PatientRegistration getPatientData(int patientId) {
		return patientRegistrationRepository.findOne(PatientRegistrationSpecification.byPatientId(patientId));
	}
	
	@Override
	public PatientRegistration getTesData(int patientId){
		return patientRegistrationRepository.findOne(GenericPrintSpecification.getPatientDetails(patientId));
	}
	
	@Override
	public Encounter getEncounterData(int encounterId) {
		return encounterRepository.findOne(EncounterSpecification.EncounterById(encounterId, true));
	}

	@Override
	public String getLeftHeaderHTML(Integer styleId) {
		String leftHeaderHTML="";
		GenericPrintStyle genericPrintStyle=genericPrintStyleRepository.findOne(styleId);
    	int headerId=genericPrintStyle.getGenericPrintStyleHeaderId();
    	/*if(headerId>0)
    		leftHeaderHTML=generateHeaderBean.generateLeftHeader(headerId);*/
		return leftHeaderHTML;
	}
	
	@Override
	public CustomGenericBean getCustomeGenericData(Integer styleId, Integer patientId, Integer encounterId, String sharedFolderPath) throws Exception{
		
		String headerHTML="",patientHeaderHTML="",leftHeaderHTML="",footerHTML="";
		CustomGenericBean customGenericBean = new CustomGenericBean();
		
		GenericPrintBean genericPrintBean = getCompleteDetails(patientId, encounterId);
		GenericPrintStyle genericPrintStyle=genericPrintStyleRepository.findOne(styleId);
		int letterHeaderId=genericPrintStyle.getGenericPrintStyleHeaderId();
		int patientHeaderId=genericPrintStyle.getGenericPrintStylePatientHeaderId();
		int footerId=genericPrintStyle.getGenericPrintStyleFooterId();

		PatientDataBean patientDataBean=genericPrintBean.getPatientBean(); 
		String[] patientDetailsArr=generatePatentDetailsArr(patientDataBean);

		if(letterHeaderId>0){
			headerHTML=generateHeaderBean.generateHeader(letterHeaderId, sharedFolderPath, genericPrintBean);
		}

		if(generateHeaderBean.getPatientHeaderType(patientHeaderId)==2){
			patientHeaderHTML = generateHeaderBean.generatePatientHeader(patientHeaderId, 2,patientDetailsArr);
		}else{
			patientHeaderHTML = generateHeaderBean.generatePatientHeader(patientHeaderId, 1,patientDetailsArr);
		}

		if(footerId>0){
			footerHTML=generateFooterBean.generateFooter(footerId);
		}

		if(letterHeaderId>0)
    		leftHeaderHTML=generateHeaderBean.generateLeftHeader(letterHeaderId,genericPrintBean);
		
		customGenericBean.setHeaderHTML(headerHTML);
		customGenericBean.setPatientHeaderHTML(patientHeaderHTML);
		customGenericBean.setLeftHeaderHTML(leftHeaderHTML);
		customGenericBean.setFooterHTML(footerHTML);
		
		return customGenericBean;
	}

	@Override
	public void saveLeafLibrary(LeafLibrary leafLibrary) {
		leafLibraryRepository.save(leafLibrary);
	}

	@Override
	public List<LeafLibrary> getTemplatesList() {		
		return leafLibraryRepository.findAll(LeafLibrarySpecification.getAllTemplates());
	}

	@Override
	public List<LeafLibrary> getStyleTemplatesList(Integer styleId) {
		return leafLibraryRepository.findAll(LeafLibrarySpecification.getByPrintStyleId(styleId));
	}

	@Override
	public LeafLibrary getLeafLibrary(int templateId) {
		return leafLibraryRepository.findOne(LeafLibrarySpecification.getLeafDetailsById(templateId)) ;
	}
	
	public String getInsuranceName(PatientDataBean patientBean){
		String primInsId="";
		try{
			List<InsuranceDataBean> insuranceList= patientBean.getInsuranceDetails();
			if(insuranceList!= null){
				for(int i=0;i<insuranceList.size();i++){
					if(insuranceList.get(i).getInsType()== 1)
						primInsId= insuranceList.get(i).getInsCompName();
				}
			}
			return primInsId;
		}catch(Exception e){
			return "";
		}
	}

	/**
	 * Get template based on generic print style id 
	 */
	@Override
	public List<LeafLibrary> getLeafLibraryStyle(Integer styleId) {
		return leafLibraryRepository.findAll(LeafLibrarySpecification.getByPrintStyleId(styleId));
	}
	
	/**
	 * Formatting phone number
	 * @param phoneNum
	 * @return
	 */
	private String formatPhoneNum(String phoneNum) {
		try{
			if(phoneNum == null)
				return null;
			else{
				if(phoneNum.indexOf("-")!=-1){
					String[] arr= phoneNum.split("-");
					if(arr.length == 3)
						phoneNum = "("+ arr[0] + ") " + arr[1] + "-" + arr[2];
				}
			}
		}catch(Exception e){
			return phoneNum;
		}
		return phoneNum;
	}
}
