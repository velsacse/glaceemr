package com.glenwood.glaceemr.server.application.services.chart.print;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.BillingConfigTable;
import com.glenwood.glaceemr.server.application.models.Billinglookup;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.H076;
import com.glenwood.glaceemr.server.application.models.InsCompAddr;
import com.glenwood.glaceemr.server.application.models.InsCompany;
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
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.repositories.PosTableRepository;
import com.glenwood.glaceemr.server.application.repositories.print.GenericPrintStyleRepository;
import com.glenwood.glaceemr.server.application.services.chart.insurance.InsuranceDataBean;
import com.glenwood.glaceemr.server.application.services.employee.EmployeeDataBean;
import com.glenwood.glaceemr.server.application.services.patient.PatientDataBean;
import com.glenwood.glaceemr.server.application.services.pos.PosDataBean;
import com.glenwood.glaceemr.server.application.specifications.BillingConfigTableSpecification;
import com.glenwood.glaceemr.server.application.specifications.BillingLookupSpecification;
import com.glenwood.glaceemr.server.application.specifications.EmployeeSpecification;
import com.glenwood.glaceemr.server.application.specifications.EncounterSpecification;
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
		patientDetailsArr[0]=patientBean.getPatientName();
		patientDetailsArr[1]=patientBean.getAge();
		patientDetailsArr[2]=patientBean.getDos();
		patientDetailsArr[3]=patientBean.getGender();
		patientDetailsArr[4]=patientBean.getAccountId();
		patientDetailsArr[5]=patientBean.getPhNum();
		patientDetailsArr[6]=patientBean.getDob();
		patientDetailsArr[7]=patientBean.getMobileNum();
		patientDetailsArr[8]=patientBean.getAddress();
		patientDetailsArr[9]=patientBean.getRefPhyName();
		patientDetailsArr[11]=patientBean.getPrincipalDr();
		patientDetailsArr[12]=patientBean.getServiceDr();
		patientDetailsArr[13]=patientBean.getEthinicity();
		patientDetailsArr[14]=patientBean.getRace();
		patientDetailsArr[15]=patientBean.getPrefLang();
		
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
			int encounterId=7328;//printDetails.getEncounterId();
			Encounter encounter = encounterRepository.findOne(EncounterSpecification.EncounterById(encounterId, true));
			TextFormatter textFormat = new TextFormatter();
			PatientDataBean patientBean = parsePatientDetails(patientDetails, encounter, textFormat);
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
		
		TextFormatter textFormat = new TextFormatter();
		PatientRegistration patientDetails= patientRegistrationRepository.findOne(GenericPrintSpecification.getPatientDetails(patientId));
		Encounter encounter = encounterRepository.findOne(EncounterSpecification.EncounterById(encounterId, true));
		List<EmployeeProfile> empList =  employeeProfileRepository.findAll(EmployeeSpecification.getUsersList("asc"));
		List<PosTable> posList = posTableRepository.findAll(PosTableSpecification.getPOSDetails());
		
		PatientDataBean patientBean = parsePatientDetails(patientDetails, encounter, textFormat);
		List<EmployeeDataBean> empolyeeBean = parseEmployeeDetails(empList, textFormat);
		List<PosDataBean> posBean = parsePOSDetails(posList, textFormat);
		
		GenericPrintBean genericBean = new GenericPrintBean(patientBean, empolyeeBean, posBean);
		return genericBean;
				
	}

	
	/**
	 * Parsing Patient Details
	 * @param patientDetails
	 * @param encounter
	 * @return
	 * @throws Exception
	 */
	private PatientDataBean parsePatientDetails(PatientRegistration patientDetails, Encounter encounter, TextFormatter textFormat) throws Exception {
		String patientName = textFormat.getFormattedName(patientDetails.getPatientRegistrationFirstName(), patientDetails.getPatientRegistrationMidInitial(), patientDetails.getPatientRegistrationLastName(), "");
		String age = textFormat.getAge(patientDetails.getPatientRegistrationDob());
		String dos = null;
		String gender = patientDetails.getPatientRegistrationSex().toString();
		String accountId = patientDetails.getPatientRegistrationAccountno();
		String phNum = patientDetails.getPatientRegistrationPhoneNo();
		String dob = textFormat.getFormattedDate(patientDetails.getPatientRegistrationDob());
		String mobileNum = patientDetails.getPatientRegistrationCellno();
		String state = patientDetails.getPatientRegistrationState();
		String address = null;
		String refPhyName = null;
		String principalDr = null;
		String serviceDr = null;
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
		
		
		List<InsuranceDataBean> insuranceBean = parseInsuranceDetails(patientDetails, textFormat);
				
		EmployeeProfile employee = patientDetails.getEmpProfile();
		principalDr = "";
		if(employee != null)
			principalDr = textFormat.getFormattedName(employee.getEmpProfileFname(), employee.getEmpProfileMi(), employee.getEmpProfileLname(), employee.getEmpProfileCredentials());
		
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
		
		serviceDr = "";
		if(encounter != null) {
            EmployeeProfile emp = encounter.getEmpProfileEmpId();
            if(emp != null) {
                serviceDr = textFormat.getFormattedName(emp.getEmpProfileFname(), emp.getEmpProfileMi(), emp.getEmpProfileLname(), emp.getEmpProfileCredentials());
            }
        }
       
        PatientDataBean bean = new PatientDataBean(patientName, age, dos, gender, accountId, phNum, dob,
                                                   mobileNum, address, refPhyName, insuranceBean, principalDr,
                                                   serviceDr, ethinicity, race, prefLang);
		
		return bean;
	}


	/**
	 * Parsing Employee Details
	 * @param empList
	 * @param textFormat
	 * @return
	 * @throws Exception
	 */
	private List<EmployeeDataBean> parseEmployeeDetails(List<EmployeeProfile> empList, TextFormatter textFormat) throws Exception {
		
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
				String fullAddress = textFormat.getAddress(emp.getEmpProfileAddress(), "", emp.getEmpProfileCity(), state, emp.getEmpProfileZip());

				empBean.add(new EmployeeDataBean(empId, loginId, fullName, fullAddress));
			}
		}
		
		return empBean;
	}


	/**
	 * Parsing Insurance Details
	 * @param patientDetails
	 * @param textFormat
	 * @return
	 */
	private List<InsuranceDataBean> parseInsuranceDetails(PatientRegistration patientDetails, TextFormatter textFormat) {
		List<InsuranceDataBean> insuranceBean = new ArrayList<InsuranceDataBean>();
		List<PatientInsDetail> patientIns = patientDetails
				.getPatientInsuranceTable();
		if (patientIns != null)
			for (int i = 0; i < patientIns.size(); i++) {
				PatientInsDetail insCompany = patientIns.get(i);
				if (insCompany != null) {
					InsCompAddr insCompAddr = insCompany.getInsCompAddr();
					if (insCompAddr != null) {
						String insAddress = insCompAddr.getInsCompAddrAddress();
						String insCity = insCompAddr.getInsCompAddrCity();
						String insState = insCompAddr.getInsCompAddrState();
						String insZip = insCompAddr.getInsCompAddrZip();
						String insCompId = null;
						String insCompName = null;
						String finalAddress = textFormat.getAddress(insAddress,
								"", insCity, insState, insZip);
						InsCompany insComp = insCompAddr.getInsCompany();
						if (insComp != null) {
							insCompId = insComp.getInsCompanyId().toString();
							insCompName = insComp.getInsCompanyName();
						}

						insuranceBean.add(new InsuranceDataBean(insCompId, insCompName, finalAddress));
					}
				}
			}
		return insuranceBean;
	}
	
	/**
     * Parsing POS details
     * @param posList
     * @param textFormat
     * @return
     */
    private List<PosDataBean> parsePOSDetails(List<PosTable> posList, TextFormatter textFormat) {
       
        List<PosDataBean> posBean = new ArrayList<PosDataBean>();
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
        return posBean;
    }

}
