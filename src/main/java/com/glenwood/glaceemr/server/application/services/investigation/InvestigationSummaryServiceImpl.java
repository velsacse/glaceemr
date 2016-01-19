package com.glenwood.glaceemr.server.application.services.investigation;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.AlertCategory;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.AssociatedOrderPblm;
import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Encounter_;
import com.glenwood.glaceemr.server.application.models.Hl7ResultInbox;
import com.glenwood.glaceemr.server.application.models.Hl7Unmappedresults;
import com.glenwood.glaceemr.server.application.models.InitialSettings;
import com.glenwood.glaceemr.server.application.models.LabAlertforwardstatus;
import com.glenwood.glaceemr.server.application.models.LabDescription;
import com.glenwood.glaceemr.server.application.models.LabEntries;
import com.glenwood.glaceemr.server.application.models.LabEntriesParameter;
import com.glenwood.glaceemr.server.application.models.LabEntries_;
import com.glenwood.glaceemr.server.application.models.LabIncludePrevious;
import com.glenwood.glaceemr.server.application.models.LabParameterCode;
import com.glenwood.glaceemr.server.application.models.LabParameters;
import com.glenwood.glaceemr.server.application.models.LoginUsers;
import com.glenwood.glaceemr.server.application.models.PatientImmunityVaccineMapping;
import com.glenwood.glaceemr.server.application.models.PatientImmunityVaccineMapping_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientVisEntries;
import com.glenwood.glaceemr.server.application.models.PatientVisEntries_;
import com.glenwood.glaceemr.server.application.models.Specimen;
import com.glenwood.glaceemr.server.application.models.VaccineOrderDetails;
import com.glenwood.glaceemr.server.application.models.VaccineReport;
import com.glenwood.glaceemr.server.application.models.VaccineReport_;
import com.glenwood.glaceemr.server.application.repositories.AlertCategoryRepository;
import com.glenwood.glaceemr.server.application.repositories.AssociatedOrderPblmRepository;
import com.glenwood.glaceemr.server.application.repositories.ChartRepository;
import com.glenwood.glaceemr.server.application.repositories.CptRepository;
import com.glenwood.glaceemr.server.application.repositories.EmpProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.EncounterEntityRepository;
import com.glenwood.glaceemr.server.application.repositories.Hl7ResultInboxRepository;
import com.glenwood.glaceemr.server.application.repositories.Hl7UnmappedResultsRepository;
import com.glenwood.glaceemr.server.application.repositories.IcdmRepository;
import com.glenwood.glaceemr.server.application.repositories.InitialSettingsRepository;
import com.glenwood.glaceemr.server.application.repositories.LabAlertforwardstatusRepository;
import com.glenwood.glaceemr.server.application.repositories.LabDescriptionRepository;
import com.glenwood.glaceemr.server.application.repositories.LabEntriesParameterRepository;
import com.glenwood.glaceemr.server.application.repositories.LabEntriesRepository;
import com.glenwood.glaceemr.server.application.repositories.LabIncludePreviousRepository;
import com.glenwood.glaceemr.server.application.repositories.LabParameterCodeRepository;
import com.glenwood.glaceemr.server.application.repositories.LabParametersRepository;
import com.glenwood.glaceemr.server.application.repositories.LoginUsersRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientImmunityVaccineMappingRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientVisEntriesRepository;
import com.glenwood.glaceemr.server.application.repositories.SpecimenRepository;
import com.glenwood.glaceemr.server.application.repositories.VaccineOrderDetailsRepository;
import com.glenwood.glaceemr.server.application.repositories.VaccineReportRepository;
import com.glenwood.glaceemr.server.application.services.alertinbox.AlertInboxService;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.specifications.AlertCategorySpecification;
import com.glenwood.glaceemr.server.application.specifications.ChartSpecification;
import com.glenwood.glaceemr.server.application.specifications.EncounterEntitySpecification;
import com.glenwood.glaceemr.server.application.specifications.InitialSettingsSpecification;
import com.glenwood.glaceemr.server.application.specifications.InvestigationSpecification;
import com.glenwood.glaceemr.server.application.specifications.LoginSpecfication;
import com.glenwood.glaceemr.server.application.specifications.VaccineReportSpecification;
import com.glenwood.glaceemr.server.utils.HUtil;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;

/**
 * @author yasodha
 * 
 * InvestigationSummaryServiceImpl gives the data required for 
 * investigation summary
 */
@Service
public class InvestigationSummaryServiceImpl implements	InvestigationSummaryService {

	@Autowired
	LabDescriptionRepository labDescriptionRepository;

	@Autowired
	LabParametersRepository labParametersRepository;

	@Autowired
	LabParameterCodeRepository labParameterCodeRepository;

	@Autowired
	EncounterEntityRepository encounterEntityRepository;

	@Autowired
	LabEntriesRepository labEntriesRepository;

	@Autowired
	EntityManagerFactory emf ;

	@Autowired
	EmpProfileRepository empProfileRepository;

	@Autowired
	PatientRegistrationRepository patientRegistrationRepository;

	@Autowired
	ChartRepository chartRepository;

	@Autowired
	CptRepository cptRepository;

	@Autowired
	Hl7UnmappedResultsRepository hl7UnmappedResultsRepository;

	@Autowired
	IcdmRepository icdmRepository;

	@Autowired
	InitialSettingsRepository initialSettingsRepository;

	@Autowired
	VaccineReportRepository vaccineReportRepository;

	@Autowired
	VaccineOrderDetailsRepository vaccineOrderDetailsRepository;

	@Autowired
	LabEntriesParameterRepository labEntriesParameterRepository;

	@Autowired
	LabIncludePreviousRepository labIncludePreviousRepository;

	@Autowired
	PatientVisEntriesRepository patientVisEntriesRepository;
	
	@Autowired
	PatientImmunityVaccineMappingRepository patientImmunityVaccineMappingRepository;

	@Autowired
	Hl7ResultInboxRepository hl7ResultInboxRepository;

	@Autowired
	AlertCategoryRepository alertCategoryRepository;

	@Autowired
	LabAlertforwardstatusRepository labAlertforwardstatusRepository;

	@Autowired
	AssociatedOrderPblmRepository associatedOrderPblmRepository;

	@Autowired
	LoginUsersRepository loginUsersRepository;

	@Autowired
	SpecimenRepository specimenRepository;

	@Autowired
	AlertInboxService alertInboxService;
	
	@PersistenceContext
	EntityManager em;

	@Autowired
	AuditTrailService auditTrailService;

	@Autowired
	SessionMap sessionMap;

	@Autowired
	HttpServletRequest request;

	private Logger logger = Logger.getLogger(InvestigationSummaryServiceImpl.class);
	ResourceBundle resBun = null;
	String savedIds ="";
	int encounterId ;
	String edate="";
	int patientId ;
	int userId;
	int chartId;
	int rDoctorId;
	int defaultDoctor;
	int pos;
	String toDay;
	String loginid; 
	String fullData;
	String isforward;
	int toId;
	String ishighpriority = "false";
	String testId = new String();
	Integer empId = -1;

	private Integer getLoginUserId(Integer empid) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<EmployeeProfile> root = cq.from(EmployeeProfile.class);
		cq.select(root.get(EmployeeProfile_.empProfileLoginid));
		cq.where(builder.equal(root.get(EmployeeProfile_.empProfileEmpid),empid));
		Integer loginId = (Integer) em.createQuery(cq).getSingleResult();
		return loginId;
	}
	
	private Integer getEmpId(int encounterId2) {
		Integer employeeId;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<EmployeeProfile> root = cq.from(EmployeeProfile.class);
		Join<EmployeeProfile, Encounter> join = root.join(EmployeeProfile_.encounterServiceDr);
		cq.select(root.get(EmployeeProfile_.empProfileEmpid));
		cq.where(builder.equal(join.get(Encounter_.encounterId), encounterId2));
		employeeId =  (Integer) em.createQuery(cq).getSingleResult();
		return employeeId;
	}
	
	@Override
	public void  savelab(String requesttosave,Integer encounterIdParam,Integer patientIdParam,Integer chartIdParam,
			Integer userIdParam,String fullDataParam,String isforwardParam,String forwardto,
			String ishighpriorityParam,String testidParam) throws Exception{
		LoginUsers login=loginUsersRepository.findOne(LoginSpecfication.byUserId(userIdParam));
		encounterId = encounterIdParam;
		patientId   = patientIdParam;
		chartId     = chartIdParam;
		userId      = userIdParam;
		loginid		=""+userIdParam;
		toDay = new String();
		Calendar cal = new GregorianCalendar();
		toDay = (cal.get(Calendar.MONTH)+ 1) + "/" + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
		fullData = URLDecoder.decode(fullDataParam,"UTF-8");
		defaultDoctor = Optional.fromNullable(login.getLoginUsersDefaultDoctor()).or(-1);
		isforward=isforwardParam;
		toId=Integer.parseInt(Optional.fromNullable(forwardto).or("-1"));
		ishighpriority = Optional.fromNullable(ishighpriorityParam).or("false");
		testId = Optional.fromNullable(testidParam).or("");
		if(requesttosave.equals("2")) {
			logger.debug("Enters into save parameter section");
			SaveParameters (chartId, -1,fullData);
		} else {
			logger.debug("Enters into save order section");
			saveInvestigation();
		}
	}

	public void saveInvestigation() throws Exception{ 
		setResourceBundle();
		String[] labRecord = fullData.split("!@@!"); 
		String[] testdetidarray=new String[labRecord.length];
		int len=0;
		List<Encounter> encounterList=encounterEntityRepository.findAll(EncounterEntitySpecification.EncounterById(encounterId));
		String dateString=HUtil.currentDate();
		Integer posInteger=1;
		if(encounterList.size()>0){
			dateString=MoreObjects.firstNonNull(encounterList.get(0).getEncounterDate(), HUtil.currentDate()).toString();
			DateFormat parser = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date = (Date) parser.parse(dateString);
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			dateString=formatter.format(date);
			posInteger=Optional.fromNullable(encounterList.get(0).getEncounterPos()).or(1);
		}
		edate  = dateString; 
		pos    = posInteger;
		for (int i=0; i < labRecord.length && !fullData.equals(""); i++){
			String tempRecord = labRecord[i];
			String[] labFieldValuePair = tempRecord.split("#@@#");
			String[] dataArray = new String[dataValueMap("MAXVARIABLECOUNT")];
			for (int j=0 ; j < (labFieldValuePair.length); j++ )
			{
				int k = 0;		    	
				String[] data = labFieldValuePair[j].split("~@@~");
				for(int m=0;m<data.length;m++)
					k = data.length;
				String fieldName = data[0];			
				String fieldValue ="";
				if ( k > 1)
					fieldValue = Optional.fromNullable(data[1]).or("");
				if (fieldName.substring(0,4).equals("lab_"))
					dataArray[dataValueMap("int_" + fieldName)] = fieldValue;
			}	

			String ids = moveDataToDataBase(dataArray);
			if ( ids != null){
				savedIds = savedIds + dataArray[dataValueMap("int_lab_uniquekey")] + "~^" + ids  + "!!!";
				testdetidarray[len] = ids.split("##")[0];
				len++;
			}
		}   
		for(int k=0;k<testdetidarray.length;k++){
			int hl7fileid=getUnmappedResult(Integer.parseInt(Optional.fromNullable(testdetidarray[k]).or("-1")));
			if(hl7fileid!=-1)
			{
				List<Hl7Unmappedresults> detail=getUnmappedResultTestDetailId(hl7fileid);
				String[] temptestidarray=new String[detail.size()];
				for(int l=0;l<detail.size();l++)
				{
					Hl7Unmappedresults temp=detail.get(l);
					temptestidarray[l]=temp.getHl7UnmappedresultsTestdetailId().toString();
				}	
				boolean isreviewed=true;
				for(int m=0;m<temptestidarray.length;m++)
				{
					LabEntries labEntriesList=labEntriesRepository.findOne(InvestigationSpecification.testdetailIds(Integer.parseInt(Optional.fromNullable(temptestidarray[m]).or("-1").toString())));
					if(labEntriesList!=null){
						int teststatus=Optional.fromNullable(labEntriesList.getLabEntriesTestStatus()).or(-1);
						if(teststatus<=3)
							isreviewed=false;
					}
				}
				if(isreviewed)
				{
					List<Hl7ResultInbox> hl7ResultInboxList=hl7ResultInboxRepository.findAll(InvestigationSpecification.hl7ResultsId(hl7fileid));
					for(int g=0;g<hl7ResultInboxList.size();g++){
						hl7ResultInboxList.get(g).setHl7ResultInboxReviewed(1);
						hl7ResultInboxRepository.saveAndFlush(hl7ResultInboxList.get(g));
					}
				}
			}
		}
	}    

	@SuppressWarnings("unused")
	public String moveDataToDataBase(String[] dataStr) throws Exception{
		logger.debug("Enters into move to database function.");
		int labEncounterId ; 
		String dxStr="";
		String SaveDxIds = "";
		String Units= "1";
		String saveLabIsNewLotUnit = "";
		int testEncounterId;
		rDoctorId = Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_referredto")]).or("-1").trim());
		String labencounterdate  = "";
		String labPerformedDate = "";
		String dx1code = "";
		String dx2code = "";
		String dx3code = "";
		String dx4code = "";
		String dx5code = "";
		String dx6code = "";
		String dx7code = "";
		String dx8code = "";
		String  saveid ="";
		int servicedoctor;
		int lab_status = -1;
		String toDay = new String();
		Calendar cal = new GregorianCalendar();
		toDay = (cal.get(Calendar.MONTH)+ 1) + "/" + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
		lab_status = Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_teststatus")]).or("-1"));//HTil************
		String[] CPTArray = Optional.fromNullable(dataStr[dataValueMap("int_lab_cpt")]).or("").split(",");
		String[] CPTUnits = Optional.fromNullable(dataStr[dataValueMap("int_lab_qnty")]).or("").split("\\^");
		labPerformedDate = Optional.fromNullable(dataStr[dataValueMap("int_lab_performon")]).or(toDay).split(" ")[0];
		if(dataStr[dataValueMap("int_lab_testdetailid")].equals("-1")){
			logger.debug("Save a new order.");
			labEncounterId=encounterId;
			labencounterdate=edate;
		} else {
			logger.debug("Save an existing order.");
			List<LabEntries> labEntriesList=labEntriesRepository.findAll(InvestigationSpecification.testdetailIds(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_testdetailid")]).or("-1").toString())));
			int encount=Integer.parseInt(MoreObjects.firstNonNull(labEntriesList.get(0).getLabEntriesEncounterId(),-1).toString());
			labEncounterId=encount;
			List<Encounter> encounterList=encounterEntityRepository.findAll(EncounterEntitySpecification.EncounterById(labEncounterId));
			String dateString=HUtil.currentDate();
			if(encounterList.size()>0){
				dateString=MoreObjects.firstNonNull(encounterList.get(0).getEncounterDate(), HUtil.currentDate()).toString();
				DateFormat parser = new SimpleDateFormat("yyyy-MM-dd"); 
				Date date = (Date) parser.parse(dateString);
				DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				dateString=formatter.format(date);
			}
			labencounterdate=dateString;
		}
		List<Encounter> encounterListOuter=encounterEntityRepository.findAll(EncounterEntitySpecification.EncounterById(labEncounterId));
		servicedoctor=defaultDoctor;
		if(encounterListOuter.size()>0)
			servicedoctor = Integer.parseInt(MoreObjects.firstNonNull(encounterListOuter.get(0).getEncounterServiceDoctor(),defaultDoctor).toString());
		dx1code = MoreObjects.firstNonNull(dataStr[dataValueMap("int_lab_dx1code")],"").trim();
		if(!dx1code.equals("") && !dx1code.equals(null)) {
			dx1code = dx1code.trim();
		} else {
			dx1code = "NODX";
			dataStr[dataValueMap("int_lab_dx1code_codesystem")] = "2.16.840.1.113883.6.90";//ICD 10
		}
		dx2code = MoreObjects.firstNonNull(dataStr[dataValueMap("int_lab_dx2code")],"").trim();
		if(!dx2code.equals("") && !dx2code.equals(null)){
			dx2code =  dx2code.trim();
		} else {
			dataStr[dataValueMap("int_lab_dx2code_codesystem")]="";
		}
		dx3code = MoreObjects.firstNonNull(dataStr[dataValueMap("int_lab_dx3code")],"").trim();
		if(!dx3code.equals("") && !dx3code.equals(null)){
			dx3code = dx3code.trim();
		}else{
			dataStr[dataValueMap("int_lab_dx3code_codesystem")]="";
		}
		dx4code = MoreObjects.firstNonNull(dataStr[dataValueMap("int_lab_dx4code")],"").trim();
		if(!dx4code.equals("") && !dx4code.equals(null)){
			dx4code = dx4code.trim();
		}else{
			dataStr[dataValueMap("int_lab_dx4code_codesystem")]="";
		}
		dx5code = MoreObjects.firstNonNull(dataStr[dataValueMap("int_lab_dx5code")],"").trim();
		if(!dx5code.equals("") && !dx5code.equals(null)){
			dx5code = dx5code.trim();
		}else{
			dataStr[dataValueMap("int_lab_dx5code_codesystem")]="";
		}
		dx6code = MoreObjects.firstNonNull(dataStr[dataValueMap("int_lab_dx6code")],"").trim();
		if(!dx6code.equals("") && !dx6code.equals(null)){
			dx6code = dx6code.trim();
		}else{
			dataStr[dataValueMap("int_lab_dx6code_codesystem")]="";
		}
		dx7code =MoreObjects.firstNonNull(dataStr[dataValueMap("int_lab_dx7code")],"").trim();
		if(!dx7code.equals("") && !dx7code.equals(null)){
			dx7code = dx7code.trim();
		}else{
			dataStr[dataValueMap("int_lab_dx7code_codesystem")]="";
		}
		dx8code = MoreObjects.firstNonNull(dataStr[dataValueMap("int_lab_dx8code")],"").trim();
		if(!dx8code.equals("") && !dx8code.equals(null)){
			dx8code = dx8code.trim();
		}else{
			dataStr[dataValueMap("int_lab_dx8code_codesystem")]="";
		}
		//Service entry
		if(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_teststatus")]).or("-1").trim())>=3 
				&& Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_teststatus")]).or("-1").trim()) <7 
				&& !Optional.fromNullable(dataStr[dataValueMap("int_lab_cpt")]).or("").equals("") 
				&& (Optional.fromNullable(dataStr[dataValueMap("int_lab_isbillable")]).or("true").equals("true")
						|| Optional.fromNullable(dataStr[dataValueMap("int_lab_isbillable")]).or("t").equals("t"))
						&& !Optional.fromNullable(dataStr[dataValueMap("int_lab_qnty")]).or("").equals("") 
						&& Optional.fromNullable(dataStr[dataValueMap("int_lab_isbilled")]).or("0").equals("0") ){/*
			CPTDAO cptDAO = new CPTDAO();
			CPTDTO cptDTO;
			ServiceDetailDAO serviceDAO = new ServiceDetailDAO(dbUtils);
			boolean insert = true;
			for(int i = 0; i< CPTArray.length ; i++){
				insert = true;
				//Calculate units 
				Units="1";
				for(int j=0;j<CPTUnits.length;j++){
					if(CPTArray[i].split("-").length > 1){
						if(CPTArray[i].split("-")[1].equalsIgnoreCase("SL")){
							if(CPTUnits[j].split("~")[0].equals(CPTArray[i].split("-")[0]+"-SL")){
								if(CPTUnits[j].split("~").length >1){
									if(!CPTUnits[j].split("~")[1].trim().equals(""))
										Units=CPTUnits[j].split("~")[1].trim();
								}
							}
						}else{
							if(CPTUnits[j].split("~")[0].equals(CPTArray[i].split("-")[0])){
								if(CPTUnits[j].split("~").length >1){
									if(!CPTUnits[j].split("~")[1].trim().equals(""))
										Units=CPTUnits[j].split("~")[1].trim();
								}
							}
						}
					}else{
						if(CPTUnits[j].split("~")[0].equals(CPTArray[i].split("-")[0])){
							if(CPTUnits[j].split("~").length >1){
								if(!CPTUnits[j].split("~")[1].trim().equals(""))
									Units=CPTUnits[j].split("~")[1].trim();
							}
						}
					}
				}
				if((dataStr[dataValueMap("int_lab_dosage")] != null) && (Integer.parseInt(Units) <= 1)){	//For calculating units from dosage for injections and vaccinations
					Units= ""+calculateUnit(HUtil.Nz(dataStr[dataValueMap("int_lab_dosage")],""), CPTArray[i],dbUtils);
				}
				if(Integer.parseInt(Units) <= 0){			//Finally when unit value is lessthan or equal to 0, then unit is set to default value 1. 
					Units="1";
				}
				String ndccode=HUtil.Nz(dataStr[dataValueMap("int_lab_ndc")],"");
				String loinccode=HUtil.Nz(dataStr[dataValueMap("int_lab_loinc")],"");
				//Visit type and Plan type in superbill
				int visittype = 2;
				int plantype  = -1;
				String cptQuery = "select groupid,plantype from patient_encounter_type inner join encounter on encounter_visittype=categoryid and encounter_id ="+encounterId;
				ResultSet rst = dbUtils.executeQuery(cptQuery);
				if (rst.next()){
					visittype = Integer.parseInt(HUtil.Nz(rst.getString("groupid"),"2"));
					plantype  = Integer.parseInt(HUtil.Nz(rst.getString("plantype"),"-1"));
				}

				Vector cptVect = new Vector();
				if(CPTArray[i].split("-").length > 1){
					if(CPTArray[i].split("-")[1].equalsIgnoreCase("SL")){
						cptVect = cptDAO.getCpts(CPTArray[i].split("-")[0]+"-"+CPTArray[i].split("-")[1],true,plantype);
					}else{
						cptVect = cptDAO.getCpts(CPTArray[i].split("-")[0],true,plantype);
					}
				}else{
					cptVect = cptDAO.getCpts(CPTArray[i],true,plantype);
				}
				if(cptVect.size() > 0 ){ 
					cptDTO =((CPTDTO)(cptVect.get(0)));
					cptDTO.getCost();
					//createEMRServiceDetail(long patientId,String dos_from,String date_of_posting,String scptcode,String modifier1,String comments,String modifier2,int servicedoctor,int billeddoctor,int refdoctor,int pos,int units,double charges,int howpaid,double copay,double payments,int pri_insdetailId,int sec_insdetailId,String dx1,String dx2,String dx3,String dx4,int placedby,String placedon,int modifiedby,String modifiedon,int encounterId,int serviceid,boolean isupdate)

					CompletePatientDetailModel patientModel = CompletePatientDetailModel.getNewInstance(patientId,session.getAttribute("connectString").toString());
					BillingServiceDetailModel billingServiceModel = new BillingServiceDetailModel();
					billingServiceModel.setPatientId(patientId);
					billingServiceModel.setDosFrom(labPerformedDate);
					billingServiceModel.setDop(labPerformedDate);	                
					billingServiceModel.setCptCode(cptDTO.getCPTCode());
					if(CPTArray[i].split("-").length>1){
						if(CPTArray[i].split("-").length>3){
							billingServiceModel.setMod1(CPTArray[i].split("-")[2]);
							billingServiceModel.setMod2(CPTArray[i].split("-")[3]);
						}
						else if(CPTArray[i].split("-").length>2){
							if(!(CPTArray[i].split("-")[1].equalsIgnoreCase("SL"))){
								billingServiceModel.setMod1(CPTArray[i].split("-")[1]);
								billingServiceModel.setMod2(CPTArray[i].split("-")[2]);
							}else{
								billingServiceModel.setMod1(CPTArray[i].split("-")[2]);
							}
						}
						else if(!CPTArray[i].split("-")[1].equalsIgnoreCase("SL"))
							billingServiceModel.setMod1(CPTArray[i].split("-")[1]);		
					}
					billingServiceModel.setSdoctorId(servicedoctor);
					billingServiceModel.setBdoctorId(servicedoctor);
					billingServiceModel.setReferralId(rDoctorId);
					billingServiceModel.setPosId(pos);
					billingServiceModel.setDx1(dx1code);
					billingServiceModel.setDx2(dx2code);
					billingServiceModel.setDx3(dx3code);
					billingServiceModel.setDx4(dx4code);
					billingServiceModel.setDx5(dx5code);
					billingServiceModel.setDx6(dx6code);
					billingServiceModel.setDx7(dx7code);
					billingServiceModel.setDx8(dx8code);
					String dxSystem="";
					billingServiceModel.setDxCodeSystemId(dxSystem);
					billingServiceModel.setServiceId(-1);
					billingServiceModel.setVisitType(visittype);
					billingServiceModel.setCostPlanId(plantype);
					if(!ndccode.trim().equalsIgnoreCase(""))
						billingServiceModel.setRlu_claim(ndccode);
					else
						billingServiceModel.setRlu_claim(HUtil.Nz(dbUtils.newTableLookUp("coalesce(cpt_ndc,'')","cpt","cpt_cptcode ilike '"+cptDTO.getCPTCode()+"'"),""));
					String insuranceName=HUtil.Nz(patientModel.getPrimaryInsuranceName(),"");
					billingServiceModel.setPriInsId(patientModel.getPatientPrimaryInsId());
					int billingReason = Integer.parseInt(HUtil.Nz(dbUtils.newTableLookUp("initial_settings_option_value", "initial_settings", "initial_settings_option_name = 'Default Billing Reason'"),"-1"));
					if(insuranceName.equalsIgnoreCase("Self Pay") || insuranceName.equalsIgnoreCase("SelfPay") || insuranceName.equalsIgnoreCase("Self-Pay")  ){
						serviceDAO.updatePrimaryInsToSelfPay(billingServiceModel,dbUtils);	
						if(billingServiceModel.getPriInsId() < 0)
							throw new Exception("Unable to generate Self Pay Insurance ID .....");
						billingServiceModel.setBillingReason(billingReason);
					}
					else{
						billingServiceModel.setSubmitStatus("0");
						if(visittype == 9){
							billingServiceModel.setBillingReason(billingReason);
						}
					}
					billingServiceModel.setSecInsId(Integer.parseInt(HUtil.nz(patientModel.getPatientSecondaryInsId(),"-1").toString()));
					billingServiceModel.setCharges(cptDTO.getCost()*Integer.parseInt(Units));
					billingServiceModel.setScomments(cptDTO.getDescription());
					billingServiceModel.setUnits((Float.parseFloat(Units)));
					billingServiceModel.setScreenName("From Investigation");
					serviceDAO.createOrUpdateServiceDetail(billingServiceModel,session.getAttribute("loginName").toString());
					String intMessage = "Service added from investigation for lab "+HUtil.Nz(dataStr[dataValueMap("int_lab_labname")],"")+" with cpt "+CPTArray[i];
					EventLog.LogEvent(AuditTrail.GLACE_LOG,AuditTrail.Investigations,AuditTrail.UPDATE,Integer.parseInt(session.getAttribute("parent_event").toString()),AuditTrail.SUCCESS,intMessage,Integer.parseInt(session.getAttribute("loginId").toString()),"127.0.01",request_Audit.getRemoteAddr(),patientId,chartId,encounterId,AuditTrail.USER_LOGIN,request_Audit,dbUtils,"Service added from investigation.");
				}
			}
						 */}

		LabEntries labEntriesSave=new LabEntries();
		Specimen specimenSave=new Specimen();
		if(dataStr[dataValueMap("int_lab_testdetailid")].equals("-1")){
			//labEntriesSave.setLabEntriesTestdetailId(-1);
		}else{
			labEntriesSave=labEntriesRepository.findOne(InvestigationSpecification.testdetailIds(Integer.parseInt(dataStr[dataValueMap("int_lab_testdetailid")])));
			testEncounterId = Optional.fromNullable(labEntriesSave.getLabEntriesEncounterId()).or(-1);
			if(testEncounterId != encounterId){
				if(Integer.parseInt(dataStr[dataValueMap("int_lab_teststatus")]) >= 3) {
					int labid = Integer.parseInt(dataStr[dataValueMap("int_lab_testdetailid")]);
					PreviousVisitLabLog(labid);
				}
			}
		}
		labEntriesSave.setLabEntriesEncounterId(labEncounterId);
		if(dataStr[dataValueMap("int_lab_testid")] != null)
			labEntriesSave.setLabEntriesTestId(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_testid")]).or("-1").toString()));
		if(dataStr[dataValueMap("int_lab_intext")] != null)
			labEntriesSave.setLabEntriesTestType(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_intext")]).or("0").toString()));
		if(dataStr[dataValueMap("int_lab_printxslurl")] != null)    	
			labEntriesSave.setLabEntriesPrintxsl(Optional.fromNullable(dataStr[dataValueMap("int_lab_printxslurl")]).or(""));
		// 	0 - Internal 1 - External  Results.
		if(dataStr[dataValueMap("int_lab_orderby")] != null)
			labEntriesSave.setLabEntriesOrdBy(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_orderby")]).or("-1")));
		if(dataStr[dataValueMap("int_lab_orderon")] != null){
			labEntriesSave.setLabEntriesOrdOn(Timestamp.valueOf(dataStr[dataValueMap("int_lab_orderon")]));
		}
		//[DHC for notes col
		if(dataStr[dataValueMap("int_lab_short_order_notes")] != null){
			if(dataStr[dataValueMap("int_lab_short_order_notes")].equals("Order Notes")){
				dataStr[dataValueMap("int_lab_short_order_notes")]="";
			}
			labEntriesSave.setLabEntriesShortorderNotes(HUtil.ValidateSingleQuote(dataStr[dataValueMap("int_lab_short_order_notes")]));
		}
		if(dataStr[dataValueMap("int_lab_scheduled_date")] != null)
			labEntriesSave.setLabEntriesScheduleddate(HUtil.ValidateSingleQuote(dataStr[dataValueMap("int_lab_scheduled_date")]));

		//DHC]
		if(dataStr[dataValueMap("int_lab_site")] != null){
			if (!dataStr[dataValueMap("int_lab_site")].equals("") )
				labEntriesSave.setLabEntriesLabSite(dataStr[dataValueMap("int_lab_site")]);
		}
		if(dataStr[dataValueMap("int_lab_route")] != null){
			if (!dataStr[dataValueMap("int_lab_route")].equals("") )
				labEntriesSave.setLabEntriesLabRoute(dataStr[dataValueMap("int_lab_route")]);
		}
		if(dataStr[dataValueMap("int_lab_vaccinevis")] != null){
			if (!dataStr[dataValueMap("int_lab_vaccinevis")].equals("") )
				labEntriesSave.setLabEntriesVaccineVis(dataStr[dataValueMap("int_lab_vaccinevis")]);
		}
		if(dataStr[dataValueMap("int_lab_performby")] != null){
			if (!dataStr[dataValueMap("int_lab_performby")].equals("") && !dataStr[dataValueMap("int_lab_performby")].equalsIgnoreCase("null") )
				labEntriesSave.setLabEntriesPerfBy(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_performby")]).or("-1").toString()));
		}
		if(dataStr[dataValueMap("int_lab_performon")] != null){
			if (!dataStr[dataValueMap("int_lab_performon")].equals(""))
				labEntriesSave.setLabEntriesPerfOn(Timestamp.valueOf(dataStr[dataValueMap("int_lab_performon")]));
		}
		if(dataStr[dataValueMap("int_lab_reviewby")] != null){
			if (!dataStr[dataValueMap("int_lab_reviewby")].equals(""))
				labEntriesSave.setLabEntriesRevBy(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_reviewby")]).or("-1").toString()));
		}
		if(dataStr[dataValueMap("int_lab_reviewon")] != null){
			if (!dataStr[dataValueMap("int_lab_reviewon")].equals(""))
				labEntriesSave.setLabEntriesRevOn(Timestamp.valueOf(dataStr[dataValueMap("int_lab_reviewon")]));
		}
		if(dataStr[dataValueMap("int_lab_informby")] != null){
			if (!dataStr[dataValueMap("int_lab_informby")].equals(""))
				labEntriesSave.setLabEntriesInformedBy(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_informby")]).or("-1").toString()));
		}
		if(dataStr[dataValueMap("int_lab_informon")] != null){
			if (!dataStr[dataValueMap("int_lab_informon")].equals("")){
				labEntriesSave.setLabEntriesInformedOn(Timestamp.valueOf(dataStr[dataValueMap("int_lab_informon")]));
			}
		}
		if(dataStr[dataValueMap("int_lab_prelimresult")] != null){
			if (!dataStr[dataValueMap("int_lab_prelimresult")].equals(""))
				labEntriesSave.setLabEntriesPrelimresult(dataStr[dataValueMap("int_lab_prelimresult")]);
		}
		if(dataStr[dataValueMap("int_lab_confirmresult")] != null){
			if (!dataStr[dataValueMap("int_lab_confirmresult")].equals(""))
				labEntriesSave.setLabEntriesConfirmTestStatus(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_confirmresult")]).or("-1").toString()));
		}
		labEntriesSave.setLabEntriesModifiedby(userId);
		Date dateMod= new Date();
		Timestamp timeStampMod=new Timestamp(dateMod.getTime());
		labEntriesSave.setLabEntriesModifiedon(timeStampMod);
		labEntriesSave.setLabEntriesChartid(chartId);
		if(dataStr[dataValueMap("int_lab_dx1id")] != null || dataStr[dataValueMap("int_lab_dx2id")] != null){ 
			int dxid1 = Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx1id")]).or("-1"));
			int dxid2 = Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx2id")]).or("-1"));
			if(dxid1 != -1)
				labEntriesSave.setLabEntriesDx1(dxid1);
			else
				labEntriesSave.setLabEntriesDx1(null);
			if(dxid2 != -1)
				labEntriesSave.setLabEntriesDx2(dxid2);
			else
				labEntriesSave.setLabEntriesDx2(null);
		}
		if(dataStr[dataValueMap("int_lab_dx1code")] != null)
			labEntriesSave.setLabEntriesDx1code(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx1code")]).or("NODX").trim());
		if(dataStr[dataValueMap("int_lab_dx2code")] != null)
			labEntriesSave.setLabEntriesDx2code(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx2code")]).or("").trim());
		if(dataStr[dataValueMap("int_lab_dx3code")] != null)
			labEntriesSave.setLabEntriesDx3code(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx3code")]).or("").trim());
		if(dataStr[dataValueMap("int_lab_dx4code")] != null)
			labEntriesSave.setLabEntriesDx4code(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx4code")]).or("").trim());
		if(dataStr[dataValueMap("int_lab_dx5code")] != null)
			labEntriesSave.setLabEntriesDx5code(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx5code")]).or("").trim());
		if(dataStr[dataValueMap("int_lab_dx6code")] != null)
			labEntriesSave.setLabEntriesDx6code(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx6code")]).or("").trim());
		if(dataStr[dataValueMap("int_lab_dx7code")] != null)
			labEntriesSave.setLabEntriesDx7code(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx7code")]).or("").trim());
		if(dataStr[dataValueMap("int_lab_dx8code")] != null)
			labEntriesSave.setLabEntriesDx8code(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx8code")]).or("").trim());

		if(dataStr[dataValueMap("int_lab_dx1code_codedesc")] != null)
			labEntriesSave.setLabEntriesDx1codeCodedesc(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx1code_codedesc")]).or("").trim());
		if(dataStr[dataValueMap("int_lab_dx2code_codedesc")] != null)
			labEntriesSave.setLabEntriesDx2codeCodedesc(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx2code_codedesc")]).or("").trim());
		if(dataStr[dataValueMap("int_lab_dx3code_codedesc")] != null)
			labEntriesSave.setLabEntriesDx3codeCodedesc(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx3code_codedesc")]).or("").trim());
		if(dataStr[dataValueMap("int_lab_dx4code_codedesc")] != null)
			labEntriesSave.setLabEntriesDx4codeCodedesc(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx4code_codedesc")]).or("").trim());
		if(dataStr[dataValueMap("int_lab_dx5code_codedesc")] != null)
			labEntriesSave.setLabEntriesDx5codeCodedesc(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx5code_codedesc")]).or("").trim());
		if(dataStr[dataValueMap("int_lab_dx6code_codedesc")] != null)
			labEntriesSave.setLabEntriesDx6codeCodedesc(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx6code_codedesc")]).or("").trim());
		if(dataStr[dataValueMap("int_lab_dx7code_codedesc")] != null)
			labEntriesSave.setLabEntriesDx7codeCodedesc(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx7code_codedesc")]).or("").trim());
		if(dataStr[dataValueMap("int_lab_dx8code_codedesc")] != null)
			labEntriesSave.setLabEntriesDx8codeCodedesc(Optional.fromNullable(dataStr[dataValueMap("int_lab_dx8code_codedesc")]).or("").trim());

		if ((dataStr[dataValueMap("int_lab_teststatus")]!=null) && (!dataStr[dataValueMap("int_lab_teststatus")].equals("")))
			labEntriesSave.setLabEntriesTestStatus(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_teststatus")]).or("-1").toString()));
		if(dataStr[dataValueMap("int_lab_labname")] != null){
			if (!dataStr[dataValueMap("int_lab_labname")].equals(""))
				labEntriesSave.setLabEntriesTestDesc(dataStr[dataValueMap("int_lab_labname")]);
		}
		if(dataStr[dataValueMap("int_lab_referredto")] != null){
			if (! dataStr[dataValueMap("int_lab_referredto")].equals(""))
				labEntriesSave.setLabEntriesRefdocId(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_referredto")]).or("-1").toString()));
		}
		if(dataStr[dataValueMap("int_lab_cpt")] != null)
			labEntriesSave.setLabEntriesCpt(dataStr[dataValueMap("int_lab_cpt")]);
		if(dataStr[dataValueMap("int_lab_shortnotes")] != null)
			labEntriesSave.setLabEntriesResultNotes(URLDecoder.decode(Optional.fromNullable(dataStr[dataValueMap("int_lab_shortnotes")]).or(""),"UTF-8"));
		if(dataStr[dataValueMap("int_lab_status")] != null)
		{
			if (! dataStr[dataValueMap("int_lab_status")].equals(""))
				labEntriesSave.setLabEntriesStatus(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_status")]).or("-1").toString()));
		}

		if(dataStr[dataValueMap("int_lab_dx1code_codesystem")] != null)
		{
			if (! dataStr[dataValueMap("int_lab_dx1code_codesystem")].equals(""))
				labEntriesSave.setLabEntriesDx1codeCodesystem(dataStr[dataValueMap("int_lab_dx1code_codesystem")]);
			else
				labEntriesSave.setLabEntriesDx1codeCodesystem("2.16.840.1.113883.6.90");
		}
		if(dataStr[dataValueMap("int_lab_dx2code_codesystem")] != null)
		{
			if (! dataStr[dataValueMap("int_lab_dx2code_codesystem")].equals(""))
				labEntriesSave.setLabEntriesDx2codeCodesystem(dataStr[dataValueMap("int_lab_dx2code_codesystem")]);
		}
		if(dataStr[dataValueMap("int_lab_dx3code_codesystem")] != null)
		{
			if (! dataStr[dataValueMap("int_lab_dx3code_codesystem")].equals(""))
				labEntriesSave.setLabEntriesDx3codeCodesystem(dataStr[dataValueMap("int_lab_dx3code_codesystem")]);
		}
		if(dataStr[dataValueMap("int_lab_dx4code_codesystem")] != null)
		{
			if (! dataStr[dataValueMap("int_lab_dx4code_codesystem")].equals(""))
				labEntriesSave.setLabEntriesDx4codeCodesystem(dataStr[dataValueMap("int_lab_dx4code_codesystem")]);
		}
		if(dataStr[dataValueMap("int_lab_dx5code_codesystem")] != null)
		{
			if (! dataStr[dataValueMap("int_lab_dx5code_codesystem")].equals(""))
				labEntriesSave.setLabEntriesDx5codeCodesystem(dataStr[dataValueMap("int_lab_dx5code_codesystem")]);
		}
		if(dataStr[dataValueMap("int_lab_dx6code_codesystem")] != null)
		{
			if (! dataStr[dataValueMap("int_lab_dx6code_codesystem")].equals(""))
				labEntriesSave.setLabEntriesDx6codeCodesystem(dataStr[dataValueMap("int_lab_dx6code_codesystem")]);
		}
		if(dataStr[dataValueMap("int_lab_dx7code_codesystem")] != null)
		{
			if (! dataStr[dataValueMap("int_lab_dx7code_codesystem")].equals(""))
				labEntriesSave.setLabEntriesDx7codeCodesystem(dataStr[dataValueMap("int_lab_dx7code_codesystem")]);
		}
		if(dataStr[dataValueMap("int_lab_dx8code_codesystem")] != null)
		{
			if (! dataStr[dataValueMap("int_lab_dx8code_codesystem")].equals(""))
				labEntriesSave.setLabEntriesDx8codeCodesystem(dataStr[dataValueMap("int_lab_dx8code_codesystem")]);
		}
		if(dataStr[dataValueMap("int_lab_administration_notes")] != null)
		{
			if (! dataStr[dataValueMap("int_lab_administration_notes")].equals("")){
				String administrationvalue = dataStr[dataValueMap("int_lab_administration_notes")]; 
				if(!administrationvalue.equals("-1"))
					labEntriesSave.setLabEntriesAdministrationNotes(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_administration_notes")]).or("-1").toString()));
				else
					labEntriesSave.setLabEntriesAdministrationNotes(0);
			}
		}
		if(dataStr[dataValueMap("int_lab_refusalreason")] != null)
		{
			if (! dataStr[dataValueMap("int_lab_refusalreason")].equals(""))
				labEntriesSave.setLabEntriesRefusalreason(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_refusalreason")]).or("-1").toString()));
		}		
		if((dataStr[dataValueMap("int_lab_enteredby")] != null)){
			if(!dataStr[dataValueMap("int_lab_enteredby")].equals(""))
				labEntriesSave.setLabEntriesEnteredBy(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_enteredby")]).or("-1").toString()));
			else
				labEntriesSave.setLabEntriesEnteredBy(-1);
		}
		if(dataStr[dataValueMap("int_lab_enteredon")] != null){
			if(!dataStr[dataValueMap("int_lab_enteredon")].equals(""))
				labEntriesSave.setLabEntriesEnteredOn(Timestamp.valueOf(dataStr[dataValueMap("int_lab_enteredon")]));
			else{
				Date dateEntered= new Date();
				Timestamp timeStampEntered=new Timestamp(dateEntered.getTime());
				labEntriesSave.setLabEntriesEnteredOn(timeStampEntered);
			}
		}

		if((dataStr[dataValueMap("int_lab_bodysitecode")] != null)){
			if(!dataStr[dataValueMap("int_lab_bodysitecode")].equals(""))
				labEntriesSave.setLabEntriesBodysiteCode(dataStr[dataValueMap("int_lab_bodysitecode")]);
			else
				labEntriesSave.setLabEntriesBodysiteCode("");
		}
		if(dataStr[dataValueMap("int_lab_bodysitedesc")] != null){
			if(!dataStr[dataValueMap("int_lab_bodysitedesc")].equals(""))
				labEntriesSave.setLabEntriesBodysiteDesc(dataStr[dataValueMap("int_lab_bodysitedesc")]);
			else	
				labEntriesSave.setLabEntriesBodysiteDesc("");
		}
		logger.debug("Save logic for vaccines and injections starts here.");
		/*
		 *	For Vaccines and Injestions alone 
		 *     
		 */
		List<InitialSettings> initialSettingVacc=initialSettingsRepository.findAll(InitialSettingsSpecification.optionId(22));
		String vacgroupid = "";
		if(initialSettingVacc.size()>0)
			vacgroupid=Optional.fromNullable(initialSettingVacc.get(0).getInitialSettingsOptionValue()).or("");
		List<InitialSettings> initialSettingChemo=initialSettingsRepository.findAll(InitialSettingsSpecification.optionId(6335));
		String chemogroupid="";
		if(initialSettingChemo.size()>0)
			chemogroupid=Optional.fromNullable(initialSettingChemo.get(0).getInitialSettingsOptionValue()).or("");
		List<InitialSettings> initialSettingSub=initialSettingsRepository.findAll(InitialSettingsSpecification.optionId(1020));
		String groupid=Optional.fromNullable(dataStr[dataValueMap("int_lab_groupid")]).or("-1");
		String supGroupId ="";
		if(initialSettingSub.size()>0)
			supGroupId=Optional.fromNullable(initialSettingSub.get(0).getInitialSettingsOptionValue()).or("");
		LabEntries labStatus=labEntriesRepository.findOne(InvestigationSpecification.testdetailIds(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_testdetailid")]).or("-1").toString())));
		int tmp_lab_or_vac_status= -1;
		if(labStatus!=null){
			tmp_lab_or_vac_status=Optional.fromNullable(labStatus.getLabEntriesTestStatus()).or(-1);
		}
		//Putting entry in vaccination records for performed vaccinations
		if(groupid.equals(vacgroupid) && !Optional.fromNullable(dataStr[dataValueMap("int_lab_testid")]).or("-1").equals("-1")){
			if(lab_status==7 ||lab_status==8 ){
				if((tmp_lab_or_vac_status >=3) && (tmp_lab_or_vac_status <=6)){					//Deleted lab is in performed status, Entry in vaccination report have to be removed. 
					deleteVaccineDosage(labEncounterId, chartId, Optional.fromNullable(dataStr[dataValueMap("int_lab_testid")]).or("-1"));
				}
			}else if(lab_status>=3){
				if ((dataStr[dataValueMap("int_lab_Dosagelevel")]!=null) && (!dataStr[dataValueMap("int_lab_Dosagelevel")].equals(""))){
					saveVaccineDosage(dataStr[dataValueMap("int_lab_Dosagelevel")],dataStr[dataValueMap("int_lab_testid")],Integer.parseInt(dataStr[dataValueMap("int_lab_performby")]), labEncounterId,  dataStr[dataValueMap("int_lab_performon")].split(" ")[0],Integer.parseInt(dataStr[dataValueMap("int_lab_testdetailid")]));
				}
			}
		}

		if((dataStr[dataValueMap("int_lab_lotno")]!=null) && (!dataStr[dataValueMap("int_lab_lotno")].equals(""))){
			saveLabIsNewLotUnit = "0"+"~"+Optional.fromNullable(dataStr[dataValueMap("int_lab_unitno")]).or("1")+"~"+Optional.fromNullable(dataStr[dataValueMap("int_lab_oldunitno")]).or("1")+"~"+dataStr[dataValueMap("int_lab_lotno")]+"~"+Optional.fromNullable(dataStr[dataValueMap("int_lab_oldlotno")]).or("-1");
			labEntriesSave.setLabEntriesLotNo(Integer.parseInt(dataStr[dataValueMap("int_lab_lotno")].toString()));

			if(groupid.equals(supGroupId) && dataStr[dataValueMap("int_lab_unitno")]!=null){
				labEntriesSave.setLabEntriesUnits(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_unitno")]).or("-1").toString()));
			}else{
				labEntriesSave.setLabEntriesUnits(1);
			}
			if(lab_status==7)
			{
				if(!dataStr[dataValueMap("int_lab_lotno")].equals("-1")){
					if(groupid.equals(vacgroupid)||groupid.equals("5") ){
						decreaseVaccineCount(dataStr[dataValueMap("int_lab_lotno")],dataStr[dataValueMap("int_lab_dosage")]);	    		
					}
				}
			}
			if(lab_status >= 3){
				int curunit = Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_unitno")]).or("1"));
				int prevunit = Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_oldunitno")]).or("1"));
				if(tmp_lab_or_vac_status >=3){							//Already Performed Labs
					if(lab_status < 7){
						if(!dataStr[dataValueMap("int_lab_lotno")].equals(dataStr[dataValueMap("int_lab_oldlotno")])){				//Lot number is changed
							if(groupid.equals(vacgroupid)){
								if(!dataStr[dataValueMap("int_lab_lotno")].equals("-1"))
									increaseVaccineCount(dataStr[dataValueMap("int_lab_lotno")]);
								if(dataStr[dataValueMap("int_lab_oldlotno")] != null && !dataStr[dataValueMap("int_lab_oldlotno")].equals("-1") )
									decreaseVaccineCount(dataStr[dataValueMap("int_lab_oldlotno")]);
							}else{
								if(!dataStr[dataValueMap("int_lab_lotno")].equals("-1"))
									increaseVaccineCount(dataStr[dataValueMap("int_lab_lotno")],curunit);
								if( dataStr[dataValueMap("int_lab_oldlotno")] != null && !dataStr[dataValueMap("int_lab_oldlotno")].equals("-1") )
									decreaseVaccineCount(dataStr[dataValueMap("int_lab_oldlotno")],prevunit);
							}
						}else{																										//Only doses changed
							if(!groupid.equals(vacgroupid) && !dataStr[dataValueMap("int_lab_lotno")].equals("-1"))																			//For injections alone
								increaseVaccineCount(dataStr[dataValueMap("int_lab_lotno")],(curunit - prevunit));
						}
					}else if(tmp_lab_or_vac_status < 7 && dataStr[dataValueMap("int_lab_oldlotno")] != null && !dataStr[dataValueMap("int_lab_oldlotno")].equals("-1") ){					//Delete already performed labs
						if(groupid.equals(vacgroupid))
							decreaseVaccineCount(dataStr[dataValueMap("int_lab_oldlotno")]);
					}
				}else if(lab_status < 7 && !dataStr[dataValueMap("int_lab_lotno")].equals("-1")){													//Labs that are not already performed
					if(groupid.equals(vacgroupid)){
						increaseVaccineCount(dataStr[dataValueMap("int_lab_lotno")]);
					}else{
						increaseVaccineCount(dataStr[dataValueMap("int_lab_lotno")],dataStr[dataValueMap("int_lab_dosage")]);	    		
					}
				}

				if(groupid.equals(vacgroupid)){
					if ((dataStr[dataValueMap("int_lab_oldunitno")]!=null) && (!dataStr[dataValueMap("int_lab_oldunitno")].equals(""))){
						labEntriesSave.setLabEntriesZunits(1);
					}
				}else{
					if ((dataStr[dataValueMap("int_lab_oldunitno")]!=null) && (!dataStr[dataValueMap("int_lab_oldunitno")].equals(""))){
						labEntriesSave.setLabEntriesZunits(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_unitno")]).or("-1").toString()));
					}
				}
			}
		}

		if ((dataStr[dataValueMap("int_lab_groupid")]!=null) && (!dataStr[dataValueMap("int_lab_groupid")].equals("")))
			labEntriesSave.setLabEntriesGroupid(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_groupid")]).or("-1").toString()));
		if ((dataStr[dataValueMap("int_lab_isbillable")]!=null) && (!dataStr[dataValueMap("int_lab_isbillable")].equals("")))
			labEntriesSave.setLabEntriesIsBillable(Boolean.parseBoolean(dataStr[dataValueMap("int_lab_isbillable")]));

		if ((dataStr[dataValueMap("int_lab_iscritical")]!=null))
			labEntriesSave.setLabEntriesCriticalStatus(Integer.parseInt(MoreObjects.firstNonNull(dataStr[dataValueMap("int_lab_iscritical")],0).toString()));

		if ((dataStr[dataValueMap("int_lab_reminder_date")]!=null) && (dataStr[dataValueMap("int_lab_teststatus")].equalsIgnoreCase("1")) && (dataStr[dataValueMap("int_lab_groupid")].equalsIgnoreCase("1")))
			labEntriesSave.setLabEntriesNextReminderDate(Timestamp.valueOf(dataStr[dataValueMap("int_lab_reminder_date")]));

		if ((dataStr[dataValueMap("int_lab_delinquency_days")]!=null) && (!dataStr[dataValueMap("int_lab_delinquency_days")].equals("")) && (dataStr[dataValueMap("int_lab_teststatus")].equalsIgnoreCase("1")) && (dataStr[dataValueMap("int_lab_groupid")].equalsIgnoreCase("1")))
			labEntriesSave.setLabEntriesDelinquencyDays(Integer.parseInt(MoreObjects.firstNonNull(dataStr[dataValueMap("int_lab_delinquency_days")],0).toString()));


		//[dhc added for observed units
		if(dataStr[dataValueMap("int_lab_observedunits")] != null){
			if (!dataStr[dataValueMap("int_lab_observedunits")].equals(""))
				labEntriesSave.setLabEntriesObsUnits(dataStr[dataValueMap("int_lab_observedunits")]);
		}
		if(dataStr[dataValueMap("int_lab_observationidentifier")] != null){
			if (!dataStr[dataValueMap("int_lab_observationidentifier")].equals(""))
				labEntriesSave.setLabEntriesObsId(Integer.parseInt(Optional.fromNullable(dataStr[dataValueMap("int_lab_observationidentifier")]).or("-1").toString()));
		}
		if(dataStr[dataValueMap("int_lab_observationvalue")] != null){
			if (!dataStr[dataValueMap("int_lab_observationvalue")].equals(""))
				labEntriesSave.setLabEntriesObsValue(dataStr[dataValueMap("int_lab_observationvalue")]);
		}
		//dhc]
		//[dhc- HL7 lab_sourceofspecimen     int_lab_sourceofspecimen
		if(dataStr[dataValueMap("int_lab_ndc")] != null){
			if (!dataStr[dataValueMap("int_lab_ndc")].equals(""))
				labEntriesSave.setLabEntriesNdc(dataStr[dataValueMap("int_lab_ndc")]);
		}
		if(dataStr[dataValueMap("int_lab_cvx")] != null){	       
			if (!dataStr[dataValueMap("int_lab_cvx")].equals(""))
				labEntriesSave.setLabEntriesCvx(dataStr[dataValueMap("int_lab_cvx")]);
		}
		if ((dataStr[dataValueMap("int_lab_isfasting")]!=null) && (!dataStr[dataValueMap("int_lab_isfasting")].equals("")))
			labEntriesSave.setLabEntriesIsFasting(Boolean.parseBoolean(dataStr[dataValueMap("int_lab_isfasting")]));
		if(dataStr[dataValueMap("int_lab_qnty")] != null){ 
			if (!dataStr[dataValueMap("int_lab_qnty")].equals(""))
				labEntriesSave.setLabEntriesCptUnits(dataStr[dataValueMap("int_lab_qnty")]);
		}
		if(dataStr[dataValueMap("int_lab_ischdp")] != null)
			labEntriesSave.setLabEntriesIschdplab(Integer.parseInt(dataStr[dataValueMap("int_lab_ischdp")]));
		if(dataStr[dataValueMap("int_lab_instruction")] != null)
			labEntriesSave.setLabEntriesInstruction(dataStr[dataValueMap("int_lab_instruction")]);
		if(dataStr[dataValueMap("int_lab_strength")] != null){
			labEntriesSave.setLabEntriesStrength(dataStr[dataValueMap("int_lab_strength")]);
		}
		if(dataStr[dataValueMap("int_lab_strengthunit")] != null){
			labEntriesSave.setLabEntriesStrengthUnit(dataStr[dataValueMap("int_lab_strengthunit")]);
		}
		if(dataStr[dataValueMap("int_lab_basedose")] != null && !dataStr[dataValueMap("int_lab_basedose")].equals("")){
			labEntriesSave.setLabEntriesBasedose(dataStr[dataValueMap("int_lab_basedose")]);
		}
		if(dataStr[dataValueMap("int_lab_basedoseunit")] != null && !dataStr[dataValueMap("int_lab_basedoseunit")].equals("")){
			labEntriesSave.setLabEntriesBasedoseUnit(dataStr[dataValueMap("int_lab_basedoseunit")]);
		}
		if(dataStr[dataValueMap("int_lab_dosage")] != null && !dataStr[dataValueMap("int_lab_dosage")].equals("")){
			labEntriesSave.setLabEntriesDosage(dataStr[dataValueMap("int_lab_dosage")]);
		}
		if(dataStr[dataValueMap("int_lab_drug")] != null){
			if (!dataStr[dataValueMap("int_lab_drug")].equals(""))
				labEntriesSave.setLabEntriesDrugxml(dataStr[dataValueMap("int_lab_drug")]);
		}
		if(dataStr[dataValueMap("int_lab_loinc")] != null){
			if (!dataStr[dataValueMap("int_lab_loinc")].equals(""))
				labEntriesSave.setLabEntriesLoinc(dataStr[dataValueMap("int_lab_loinc")]);
		}
		if(dataStr[dataValueMap("int_lab_collectionvolume")]!=null){
			if(!dataStr[dataValueMap("int_lab_collectionvolume")].equals(""))
				labEntriesSave.setLabEntriesSpecimenCollVol(dataStr[dataValueMap("int_lab_collectionvolume")]);
		}
		//MPK
		if(dataStr[dataValueMap("int_lab_sourceofspecimen")] != null && !dataStr[dataValueMap("int_lab_sourceofspecimen")].equals("")){
			Specimen specimen = null;
			if(dataStr[dataValueMap("int_lab_specimendate")] != null && !(dataStr[dataValueMap("int_lab_specimendate")].equals(""))){
				specimen = specimenRepository.findOne(Specifications.where(InvestigationSpecification.specimenSource(dataStr[dataValueMap("int_lab_sourceofspecimen")]))
						.and(InvestigationSpecification.specimenColVol(dataStr[dataValueMap("int_lab_collectionvolume")]))
						.and(InvestigationSpecification.specimenColVolUnit(dataStr[dataValueMap("int_lab_collectionunits")]))
						.and(InvestigationSpecification.specimenDate(Timestamp.valueOf(dataStr[dataValueMap("int_lab_specimendate")]))));
			} else {
				specimen = specimenRepository.findOne(Specifications.where(InvestigationSpecification.specimenSource(dataStr[dataValueMap("int_lab_sourceofspecimen")]))
						.and(InvestigationSpecification.specimenColVol(dataStr[dataValueMap("int_lab_collectionvolume")]))
						.and(InvestigationSpecification.specimenColVolUnit(dataStr[dataValueMap("int_lab_collectionunits")])));
			}
			long specimenId = -1;
			if( specimen != null ) {
				specimenId = Optional.fromNullable(specimen.getSpecimenId()).or(-1);
			}
			if(specimenId == -1) {
				if(dataStr[dataValueMap("int_lab_collectionunits")] != null){
					if (!dataStr[dataValueMap("int_lab_collectionunits")].equals(""))
						specimenSave.setSpecimenCollVolUnit(dataStr[dataValueMap("int_lab_collectionunits")]);
				}
				if(dataStr[dataValueMap("int_lab_specimendate")] != null){
					if (!dataStr[dataValueMap("int_lab_specimendate")].equals(""))
						specimenSave.setSpecimenDate(Timestamp.valueOf(dataStr[dataValueMap("int_lab_specimendate")]));
				}
				if(dataStr[dataValueMap("int_lab_sourceofspecimen")] != null){
					if (!dataStr[dataValueMap("int_lab_sourceofspecimen")].equals(""))
						specimenSave.setSpecimenSource(dataStr[dataValueMap("int_lab_sourceofspecimen")]);
				}
				if(dataStr[dataValueMap("int_lab_collectionvolume")] != null){
					if (!dataStr[dataValueMap("int_lab_collectionvolume")].equals(""))
						specimenSave.setSpecimenCollVol(dataStr[dataValueMap("int_lab_collectionvolume")]);
				}
				specimenId = specimenRepository.saveAndFlush(specimenSave).getSpecimenId();
			}
			labEntriesSave.setLabEntriesSepcimenId(Integer.parseInt(specimenId+""));
		}
		if(dataStr[dataValueMap("int_lab_testdetailid")].equals("-1")){
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq = builder.createQuery();
			Root<LabEntries> root = cq.from(LabEntries.class);
			cq.multiselect(builder.coalesce(builder.max(root.get(LabEntries_.labEntriesTestdetailId)),-1));
			int testDetTemp=Integer.parseInt(MoreObjects.firstNonNull(Integer.parseInt(em.createQuery(cq).getSingleResult().toString()),-1).toString());
			labEntriesRepository.saveAndFlush(labEntriesSave);
			CriteriaBuilder builder1 = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq1 = builder1.createQuery();
			Root<LabEntries> root1 = cq1.from(LabEntries.class);
			cq1.multiselect(builder1.coalesce(builder1.max(root1.get(LabEntries_.labEntriesTestdetailId)),-1));
			int testDetAfterSave=Integer.parseInt(MoreObjects.firstNonNull(em.createQuery(cq1).getSingleResult().toString(),-1).toString());
			if(testDetAfterSave==(testDetTemp+1))
				saveid=testDetAfterSave+"";
			else
				saveid="-1";
		}else{
			LabEntries saveidLab=labEntriesRepository.saveAndFlush(labEntriesSave);
			saveid=saveidLab.getLabEntriesTestdetailId()+"";
		}
		if(dataStr[dataValueMap("int_lab_testdetailid")].equals("-1")){
			updateLabHitCount(Integer.parseInt(dataStr[dataValueMap("int_lab_testid")]));
		}

		String paramString = dataStr[dataValueMap("int_lab_resultdata")];
		String visPatientInfo = "";
		if(dataStr[dataValueMap("int_lab_patientvisinfoentries")]!=null)
			visPatientInfo = (dataStr[dataValueMap("int_lab_patientvisinfoentries")].toString()).replace("@cell@", "#~@~").replace("@row@", "~#~");
		String persumeimmunityinfo="";
		if(dataStr[dataValueMap("int_lab_persumeimmunityifo")]!=null)
			persumeimmunityinfo = dataStr[dataValueMap("int_lab_persumeimmunityifo")];
		SavePatientVISinformation(Integer.parseInt(saveid),visPatientInfo);
		List<Chart> chart=chartRepository.findAll(ChartSpecification.findByChartId(chartId));
		int tmpPatientId = -1;
		if( chart.size() > 0 ) {
			tmpPatientId = chart.get(0).getChart_patientid();
		}
		SavePatientImmunityinformation(labEncounterId,tmpPatientId,chartId,persumeimmunityinfo);
		SaveParameters(chartId, Integer.parseInt(saveid), paramString);
		if(dataStr[dataValueMap("int_lab_testdetailid")].equals("-1")){
			if(!dx1code.equals("")){
				insert_assc_prblm(dx1code, Integer.parseInt(saveid), 1, userId);
				if(!dx2code.equals("")){
					insert_assc_prblm(dx2code, Integer.parseInt(saveid), 1, userId);
				}
			}
		}
		int refId = Integer.parseInt(saveid);
		int isExternal = Integer.parseInt(HUtil.Nz(dataStr[dataValueMap("int_lab_intext")],"0"));
		String Message =  dataStr[dataValueMap("int_lab_labname")];
		String isDefault = "t";	//If lab_alertforwardstatus_todefault field in lab_alertforwardstatus is false, then alert shouldnot be forwarded.
		logger.debug("Adding alert for the lab.");
		LabAlertforwardstatus labAlertforwardstatus = labAlertforwardstatusRepository.findOne(InvestigationSpecification.labAlertForwardLabStatus(lab_status));
		if( labAlertforwardstatus != null ) {
			isDefault = "" + labAlertforwardstatus.getLabAlertforwardstatusTodefault();
		} else {
			isDefault = "t";
		}
		List<Integer> isSpecificList = new ArrayList<Integer>();
		int isSpecific = 0;
		int speGrpId = 0;
		if ( !groupid.equals("") ) {
			List<AlertCategory> alertCategory = alertCategoryRepository.findAll(AlertCategorySpecification.getAlertCatByType(Integer.parseInt(groupid)));
			for (int i = 0; i < alertCategory.size(); i++) {
				AlertCategory alerts = alertCategory.get(i);
				isSpecific = Integer.parseInt(HUtil.Nz(alerts.getAlertCategoryId(),"-1"));
				isSpecificList.add(isSpecific);
			}
			speGrpId = Integer.parseInt(groupid);
		}
		this.empId = getEmpId(labEncounterId);
		this.userId = getLoginUserId(this.empId);
		AlertEvent alertEvent = alertInboxService.getAlertId(patientId, labEncounterId, refId);
		if ( isforward.equals("-1") ) {
			if ( tmp_lab_or_vac_status != lab_status ) {
				for (int i = 0; i < isSpecificList.size(); i++) {
					if ( isSpecificList.get(i) != -1 ) {
						try {
							switch ( lab_status ) {
							case 1:
								if ( isDefault.equalsIgnoreCase("t") ) {
									Message = Message + " has to be done"; 
									if(!dataStr[dataValueMap("int_lab_scheduled_date")].equals(""))
										Message = Message + ".\nScheduled Date: " + dataStr[dataValueMap("int_lab_scheduled_date")];
									putLabAlert(labEncounterId, refId, lab_status, speGrpId, isExternal, Message, alertEvent);
								}
								break;
							case 2:
								deleteLabAlert(alertEvent);
								break;
							case 3:Message = Message + " has been done,result need to be reviewed"; 
							putLabAlert(labEncounterId, refId, lab_status, speGrpId, isExternal, Message, alertEvent);
							break;
							case 4:Message = Message + " result has been reviewed,Patient has to be informed."; 
							putLabAlert(labEncounterId, refId, lab_status, speGrpId, isExternal, Message, alertEvent);
							break;
							case 5:
								deleteLabAlert(alertEvent);
								break;
							case 6:
								deleteLabAlert(alertEvent);
								break;
							case 7:
								deleteLabAlert(alertEvent);
								break;
							case 8:
								deleteLabAlert(alertEvent);
							}	
						} catch( Exception e ) {
							e.printStackTrace();
							throw e;
						}
					} else {
						try {
							switch( lab_status ) {
							case 1:
								if(isDefault.equalsIgnoreCase("t")){
									Message = Message + " has to be done"; 
									if(!dataStr[dataValueMap("int_lab_scheduled_date")].equals(""))
										Message = Message + ".\nScheduled Date: "+dataStr[dataValueMap("int_lab_scheduled_date")];
									putLabAlert(labEncounterId, refId, lab_status, -1, isExternal, Message, alertEvent);
								}
								break;
							case 2:
								deleteLabAlert(alertEvent);
								break;
							case 3:Message = Message + " has been done,result need to be reviewed"; 
							putLabAlert(labEncounterId, refId, lab_status, -1, isExternal, Message, alertEvent);
							break;
							case 4:Message = Message + " result has been reviewed,Patient has to be informed."; 
							putLabAlert(labEncounterId, refId, lab_status, -1, isExternal, Message, alertEvent);
							break;
							case 5:
								deleteLabAlert(alertEvent);
								break;
							case 6:
								deleteLabAlert(alertEvent);
								break;
							case 7:
								deleteLabAlert(alertEvent);
								break;
							case 8:
								deleteLabAlert(alertEvent);
							}
						} catch( Exception e ) {
							e.printStackTrace();
							throw e;
						}
					}				
				}				
			}
		} else {
			int CategoryId = -2;
			int section = -1;
			if ( isExternal == 1 ) {
				section = 3;
			} else {
				section = 2;
			}
			CategoryId = alertInboxService.getCategoryId(section,lab_status);
			boolean isHighPriority = false;  
			String[] labdet = testId.split("['@']");
			for(int i = 0 ; i < labdet.length ; i++) { 
				try{
					if(!labdet[i].equals("")){
						int test_id = Integer.parseInt(labdet[i]);
						int curTestId = Integer.parseInt(HUtil.Nz(dataStr[dataValueMap("int_lab_testid")],"-1"));
						if(test_id == curTestId){
							if(ishighpriority.toString().equals("true"))
								isHighPriority = true;
							try{
								deleteLabAlert(alertEvent);
								switch(lab_status){
								case 1:Message = Message + " has to be done"; 
								if(!dataStr[dataValueMap("int_lab_scheduled_date")].equals(""))
									Message = Message + "\nScheduled Date: "+dataStr[dataValueMap("int_lab_scheduled_date")];
								List<Integer> alertIdList = new ArrayList<Integer>();
								alertIdList.add(alertEvent.getAlertEventId());
								alertInboxService.forwardAlerts(getAlertIdList(alertEvent), "" + CategoryId, Message, "" + userId, "" + toId, "" + isHighPriority);
								break;
								case 2:
									deleteLabAlert(alertEvent);
									break;
								case 3:Message = Message + " has been done,result need to be reviewed"; 
								alertInboxService.forwardAlerts(getAlertIdList(alertEvent), "" + CategoryId, Message, "" + userId, "" + toId, "" + isHighPriority);
								break;
								case 4:Message = Message + " result has been reviewed,Patient has to be informed."; 
								alertInboxService.forwardAlerts(getAlertIdList(alertEvent), "" + CategoryId, Message, "" + userId, "" + toId, "" + isHighPriority);
								break;
								case 5:
								case 6:
								case 7:
								case 8:deleteLabAlert(alertEvent);

								}
							} catch ( Exception e ) {
								e.printStackTrace();
								throw e;
							}
						}	
					}
				} catch( Exception e ) {
					e.printStackTrace();	    
				}
			}
		}
		logger.debug("End of adding alert for the lab.");
		// For changing the LabIsNew to 0 and LotUnit data to new Unit
		saveid = saveid + "##" + saveLabIsNewLotUnit; 

		if(!SaveDxIds.equals("")){
			saveid = saveid + "~^" + SaveDxIds; 
		}
		String message = "";
		String intMessage = "";
		if(dataStr[dataValueMap("int_lab_testdetailid")].equals("-1")){
			message = "Test details saved";
			intMessage = "New test "+Optional.fromNullable(dataStr[dataValueMap("int_lab_labname")]).or("")+ " added with status "+lab_status;
		}else{
			if(lab_status == 7){
				DeleteParameters(Integer.parseInt(dataStr[dataValueMap("int_lab_testdetailid")]));
				message = "Test deleted";
				intMessage = Optional.fromNullable(dataStr[dataValueMap("int_lab_labname")]).or("")+ " deleted";
			}else if(lab_status == 2){
				message = "Test cancelled";
				intMessage = Optional.fromNullable(dataStr[dataValueMap("int_lab_labname")]).or("")+ " cancelled";
			}else if(lab_status == 8){
				message = "Patient Declined the Test";
				intMessage = Optional.fromNullable(dataStr[dataValueMap("int_lab_labname")]).or("")+ " Patient Declined";
			}else{
				message = "Test details updated";
				intMessage = Optional.fromNullable(dataStr[dataValueMap("int_lab_labname")]).or("")+ " updated";
			}
		}
		return saveid;

	}

	private void putLabAlert(int labEncounterId, int refId, int status, int labType, int isExternal, String message, AlertEvent alertEvent) {
		int forwardTo = 0;
		int CategoryId = -2;
		switch( status ) {
		case 1: forwardTo = -2;
		break;
		case 3: forwardTo = -1;
		break;
		case 4: forwardTo = -2;
		break;
		}
		int section = -1;
		if ( isExternal == 1 ) {
			section = 3;
		} else {
			section = 2;
		}
		try {
			//Alert should go to nurse even if ordered by a nurse
			if ( status == 3 ) {	//Test waiting for review alert should go to ordered provider alone.
				LabEntries labData = labEntriesRepository.findOne(InvestigationSpecification.findTest("" + labEncounterId,"" + refId)); 
				if( labData.getLabEntriesOrdBy() == null ) {
					forwardTo = -1;
				} else {
					forwardTo = labData.getLabEntriesOrdBy();
				}
				if(forwardTo == -1 && isExternal == 1) {
					PatientRegistration patRegistration = patientRegistrationRepository.findOne(InvestigationSpecification.checkPatientId(patientId));
					if( patRegistration.getPatientRegistrationPrincipalDoctor() == null ) {
						forwardTo = -1;
					} else {
						forwardTo  = patRegistration.getPatientRegistrationPrincipalDoctor();
					}
				}
			}
			if( labType != -1) {
				CategoryId = alertInboxService.getAlertCategoryId(status, labType);
			}
			if( CategoryId == -1 ) {
				CategoryId = alertInboxService.getCategoryId(section,status);
			}
			deleteLabAlert(alertEvent);
			alertInboxService.forwardAlerts(getAlertIdList(alertEvent), "" + CategoryId, message, "" + userId, "" + forwardTo, ishighpriority);
		} catch(Exception e) {
			logger.debug("error in put lab alert");
		}
	}

	private List<Integer> getAlertIdList(AlertEvent alertEvent) {
		List<Integer> alertIdList = new ArrayList<Integer>();
		alertIdList.add(alertEvent.getAlertEventId());
		return alertIdList;
	}

	private void deleteLabAlert(AlertEvent alertEvent) {	
		if(alertEvent!=null){
			List<Integer> alertIdList = new ArrayList<Integer>();
			alertIdList.add(alertEvent.getAlertEventId());
			alertInboxService.deleteAlert(alertIdList, userId);
		}
	}
	public List<Hl7Unmappedresults> getUnmappedResultTestDetailId(Integer hl7fileid){
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Hl7Unmappedresults> cq = builder.createQuery(Hl7Unmappedresults.class);
		Root<Hl7Unmappedresults> root=cq.from(Hl7Unmappedresults.class);
		cq.where(builder.equal(root.get("hl7UnmappedresultsFilewiseId"), hl7fileid));
		List<Hl7Unmappedresults> unmappedResults= em.createQuery(cq).getResultList();

		return unmappedResults;
	}

	public void SavePatientVISinformation(int testDetailId,String visInfo) throws Exception{
		List<PatientVisEntries> pat=new ArrayList<PatientVisEntries>();
		pat=patientVisEntriesRepository.findAll(VaccineReportSpecification.patientVisAdminisId(testDetailId));
		if(pat.size()>0){
			patientVisEntriesRepository.delete(pat);
		}
		if(!visInfo.equals("")){
			String[] visDataPair = visInfo.split("~#~");
			for(int i=0;i<visDataPair.length;i++){
				if( !visDataPair[i].equals("") ) {
					insertVISEntriesDetails(testDetailId,visDataPair[i]);
				}
			}
		}
	}

	public void insertVISEntriesDetails(int testDetailId,String visDataPair) throws Exception{
		String[] visvalues = visDataPair.split("#~@~");
		String visCVXCode =visvalues[1].toString();
		String visGroupCode =visvalues[2].toString();
		String visPublicationDate =visvalues[3].toString();
		String vispatiententiresid =visvalues[4].toString();
		String visPresentationDate =visvalues[5].toString();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<PatientVisEntries> root = cq.from(PatientVisEntries.class);
		cq.multiselect(builder.coalesce(builder.max(root.get(PatientVisEntries_.patientVisEntriesId)),0));
		int uniqueid =Integer.parseInt(MoreObjects.firstNonNull(em.createQuery(cq).getSingleResult(),0).toString())+1;
		int loginId = sessionMap.getUserID();
		PatientVisEntries patientVisEntries=new PatientVisEntries();
		patientVisEntries.setPatientVisEntriesId(uniqueid);
		patientVisEntries.setPatientVisEntriesAdministrationId(testDetailId);
		patientVisEntries.setPatientVisEntriesCvx(visCVXCode);
		patientVisEntries.setPatientVisEntriesVaccineGroupCode(visGroupCode);
		patientVisEntries.setPatientVisEntriesPublicationDate(Timestamp.valueOf(visPublicationDate));
		patientVisEntries.setPatientVisEntriesPresentationDate(Timestamp.valueOf(visPresentationDate));
		patientVisEntries.setPatientVisEntriesIsActive(true);
		patientVisEntries.setPatientVisEntriesId(Integer.parseInt(vispatiententiresid));
		patientVisEntries.setPatientVisEntriesCreatedBy(loginId);
		Date dateCreated=new Date();
		Timestamp timeStampCreated=new Timestamp(dateCreated.getTime());
		patientVisEntries.setPatientVisEntriesCreatedOn(timeStampCreated);
		patientVisEntries.setPatientVisEntriesModifiedBy(loginId);
		Date dateModified=new Date();
		Timestamp timeStampModified=new Timestamp(dateModified.getTime());
		patientVisEntries.setPatientVisEntriesModifiedOn(timeStampModified);
		patientVisEntriesRepository.saveAndFlush(patientVisEntries);
	}

	public void SavePatientImmunityinformation(int encounterId,int patientId,int chartid,String persumeimmunityinfo) throws Exception{
		if(!persumeimmunityinfo.equals("")){
			String[] immunityDataPair = persumeimmunityinfo.split("~#~");
			for(int i=0;i<immunityDataPair.length;i++){
				insertImmunityDetails(encounterId,patientId,chartid,immunityDataPair[i]);
			}
		}
	}

	public void insertImmunityDetails(int encounterId,int patientId,int chartid,String immunityDataPair) throws Exception{
		String[] immunityValues = immunityDataPair.split("#~@~");
		int groupcode =Integer.parseInt(immunityValues[1].toString());
		int diseasesCode =Integer.parseInt(immunityValues[2].toString());
		String systemOID =immunityValues[3].toString();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<PatientImmunityVaccineMapping> root = cq.from(PatientImmunityVaccineMapping.class);
		cq.multiselect(builder.coalesce(builder.max(root.get(PatientImmunityVaccineMapping_.patientImmunityVaccineMappingId)),0));
		int uniqueid =Integer.parseInt(MoreObjects.firstNonNull(em.createQuery(cq).getSingleResult(),0).toString())+1;
		int loginId = sessionMap.getUserID();
		PatientImmunityVaccineMapping patientImmunityVaccineMappingtemp=new PatientImmunityVaccineMapping();
		patientImmunityVaccineMappingtemp.setPatientImmunityVaccineMappingId(uniqueid);
		patientImmunityVaccineMappingtemp.setPatientImmunityVaccineMappingPatientId(patientId);
		patientImmunityVaccineMappingtemp.setPatientImmunityVaccineMappingEncounterId(encounterId);
		patientImmunityVaccineMappingtemp.setPatientImmunityVaccineGroupCode(groupcode);
		patientImmunityVaccineMappingtemp.setPatientImmunityVaccineMappingDiseaseCode(diseasesCode+"");
		patientImmunityVaccineMappingtemp.setPatientImmunityVaccineMappingDiseaseCodeSystemOid(systemOID);
		patientImmunityVaccineMappingtemp.setPatientImmunityVaccineMappingCreatedBy(loginId);
		Date dateCre=new Date();
		Timestamp timeStampCre=new Timestamp(dateCre.getTime());
		patientImmunityVaccineMappingtemp.setPatientImmunityVaccineMappingCreatedOn(timeStampCre);
		patientImmunityVaccineMappingtemp.setPatientImmunityVaccineMappingChartid(chartid);
		patientImmunityVaccineMappingRepository.saveAndFlush(patientImmunityVaccineMappingtemp);
	}

	public Integer getUnmappedResult(Integer testdetid){
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<Hl7Unmappedresults> root=cq.from(Hl7Unmappedresults.class);
		cq.distinct(true);
		cq.multiselect(root.get("hl7UnmappedresultsFilewiseId"));
		cq.where(builder.equal(root.get("hl7UnmappedresultsTestdetailId"), testdetid));
		cq.orderBy(builder.desc(root.get("hl7UnmappedresultsFilewiseId")));
		Integer unmappedResult=-1;
		List<Object> list=em.createQuery(cq).getResultList();
		if(list.size()>0)
			unmappedResult= Integer.parseInt(MoreObjects.firstNonNull(list.get(0),"-1").toString());
		return unmappedResult;
	}

	public void deleteVaccineDosage(int encounterid, int chartid, String testid){
		List<VaccineReport> vaccineReportList=vaccineReportRepository.findAll(Specifications.where(VaccineReportSpecification.getChartId(chartid)).and(VaccineReportSpecification.getEncounterId(encounterid)).and(VaccineReportSpecification.getVaccineId(Integer.parseInt(testid))));
		if(vaccineReportList.size()>0){
			vaccineReportRepository.delete(vaccineReportRepository.findAll(Specifications.where(VaccineReportSpecification.getEncounterId(encounterid)).and(VaccineReportSpecification.getVaccineId(Integer.parseInt(testid))).and(VaccineReportSpecification.getChartId(chartid))));
		}
	}

	public void saveVaccineDosage(String dosage,String testid, int performedby, int labencId, String givenDate,int testdetailid){
		LabEntries labEntriesList=labEntriesRepository.findOne(InvestigationSpecification.testdetailIds(testdetailid));
		int status=1;
		if(labEntriesList!=null)
			status= Optional.fromNullable(labEntriesList.getLabEntriesTestStatus()).or(1);
		List<VaccineReport> vaccineReportList=vaccineReportRepository.findAll(Specifications.where(VaccineReportSpecification.getEncounterId(labencId)).and(VaccineReportSpecification.getVaccineId(Integer.parseInt(testid))));
		int dosagelevel = -1;
		if(vaccineReportList.size()>0)
			dosagelevel=Optional.fromNullable(vaccineReportList.get(0).getVaccineReportDosageLevel()).or(-1);
		if((testdetailid != -1)&& status>2 &&status<7 ){
			if(!dosage.equals("-1")){
				List<VaccineReport> vaccineReportListUpdate=vaccineReportRepository.findAll(Specifications.where(VaccineReportSpecification.getEncounterId(labencId)).and(VaccineReportSpecification.getVaccineId(Integer.parseInt(testid)))
						.and(VaccineReportSpecification.getDosageLevel(dosagelevel)));
				for(int j=0;j<vaccineReportListUpdate.size();j++){
					vaccineReportListUpdate.get(j).setVaccineReportDosageLevel(Integer.parseInt(dosage));
					if(!givenDate.equals(""))
						vaccineReportListUpdate.get(j).setVaccineReportGivenDate(Timestamp.valueOf(givenDate+" 00:00:00"));
					Date dateMod=new Date();
					Timestamp timeStampModified=new Timestamp(dateMod.getTime());
					vaccineReportListUpdate.get(j).setVaccineReportLastModified(timeStampModified);
					vaccineReportRepository.saveAndFlush(vaccineReportListUpdate.get(j));
				}

			}
		}else{
			if(!dosage.equals("-1")){
				List<VaccineReport> vaccineReportCount=vaccineReportRepository.findAll(Specifications.where(VaccineReportSpecification.getChartId(chartId)).and(VaccineReportSpecification.getDosageLevel(Integer.parseInt(dosage)))
						.and(VaccineReportSpecification.getVaccineId(Integer.parseInt(testid))));
				if(vaccineReportCount.size()>0){
					vaccineReportRepository.delete(vaccineReportRepository.findAll(Specifications.where(VaccineReportSpecification.getChartId(chartId)).and(VaccineReportSpecification.getDosageLevel(Integer.parseInt(dosage)))
							.and(VaccineReportSpecification.getVaccineId(Integer.parseInt(testid)))));
				}
				if(givenDate.equals("")){
					VaccineReport vaccineReportTemp=new VaccineReport();
					CriteriaBuilder builder = em.getCriteriaBuilder();
					CriteriaQuery<Object> cq = builder.createQuery();
					Root<VaccineReport> root = cq.from(VaccineReport.class);
					cq.multiselect(builder.coalesce(builder.max(root.get(VaccineReport_.vaccineReportId)),0));
					vaccineReportTemp.setVaccineReportId(Integer.parseInt(MoreObjects.firstNonNull(em.createQuery(cq).getSingleResult(),0).toString())+1);
					vaccineReportTemp.setVaccineReportChartId(chartId);
					vaccineReportTemp.setVaccineReportEncounterId(labencId);
					vaccineReportTemp.setVaccineReportVaccineId(Integer.parseInt(testid));
					Date dateGiven=new Date();
					Timestamp timeStampGiven=new Timestamp(dateGiven.getTime());
					vaccineReportTemp.setVaccineReportGivenDate(timeStampGiven);
					vaccineReportTemp.setVaccineReportDosageLevel(Integer.parseInt(dosage));
					vaccineReportTemp.setVaccineReportComments("");
					vaccineReportTemp.setVaccineReportIsemr(1);
					vaccineReportTemp.setVaccineReportUserid(performedby);
					Date dateMod=new Date();
					Timestamp timeStampMod=new Timestamp(dateMod.getTime());
					vaccineReportTemp.setVaccineReportLastModified(timeStampMod);
					vaccineReportRepository.saveAndFlush(vaccineReportTemp);
				}
				else{
					VaccineReport vaccineReportTemp=new VaccineReport();
					CriteriaBuilder builder = em.getCriteriaBuilder();
					CriteriaQuery<Object> cq = builder.createQuery();
					Root<VaccineReport> root = cq.from(VaccineReport.class);
					cq.multiselect(builder.coalesce(builder.max(root.get(VaccineReport_.vaccineReportId)),0));
					vaccineReportTemp.setVaccineReportId(Integer.parseInt(MoreObjects.firstNonNull(em.createQuery(cq).getSingleResult(),0).toString())+1);
					vaccineReportTemp.setVaccineReportChartId(chartId);
					vaccineReportTemp.setVaccineReportEncounterId(labencId);
					vaccineReportTemp.setVaccineReportVaccineId(Integer.parseInt(testid));
					vaccineReportTemp.setVaccineReportGivenDate(Timestamp.valueOf(givenDate+" 00:00:00"));
					vaccineReportTemp.setVaccineReportDosageLevel(Integer.parseInt(dosage));
					vaccineReportTemp.setVaccineReportComments("");
					vaccineReportTemp.setVaccineReportIsemr(1);
					vaccineReportTemp.setVaccineReportUserid(performedby);
					Date dateMod=new Date();
					Timestamp timeStampMod=new Timestamp(dateMod.getTime());
					vaccineReportTemp.setVaccineReportLastModified(timeStampMod);
					vaccineReportRepository.saveAndFlush(vaccineReportTemp);
				}
			}
			else{
				CriteriaBuilder builder = em.getCriteriaBuilder();
				CriteriaQuery<Object> cq = builder.createQuery();
				Root<VaccineReport> root = cq.from(VaccineReport.class);
				cq.multiselect(builder.coalesce(builder.max(root.get(VaccineReport_.vaccineReportDosageLevel)),0));
				Predicate[] rest=new Predicate[]{
						builder.equal(root.get(VaccineReport_.vaccineReportChartId),chartId)
						,builder.equal(root.get(VaccineReport_.vaccineReportVaccineId),Integer.parseInt(testid))
				};
				cq.where(rest);
				dosage =  Optional.fromNullable(MoreObjects.firstNonNull(em.createQuery(cq).getSingleResult(),0).toString()).or("-1");
				int currdos =Integer.parseInt(dosage)+1;
				if(givenDate.equals("")){
					VaccineReport vaccineReportTemp=new VaccineReport();
					CriteriaBuilder builder1 = em.getCriteriaBuilder();
					CriteriaQuery<Object> cq1 = builder1.createQuery();
					Root<VaccineReport> root1 = cq1.from(VaccineReport.class);
					cq1.multiselect(builder1.coalesce(builder1.max(root1.get(VaccineReport_.vaccineReportId)),0));
					vaccineReportTemp.setVaccineReportId(Integer.parseInt(MoreObjects.firstNonNull(em.createQuery(cq1).getSingleResult(),0).toString())+1);
					vaccineReportTemp.setVaccineReportChartId(chartId);
					vaccineReportTemp.setVaccineReportEncounterId(labencId);
					vaccineReportTemp.setVaccineReportVaccineId(Integer.parseInt(testid));
					Date dateGiven=new Date();
					Timestamp timeStampGiven=new Timestamp(dateGiven.getTime());
					vaccineReportTemp.setVaccineReportGivenDate(timeStampGiven);
					vaccineReportTemp.setVaccineReportDosageLevel(currdos);
					vaccineReportTemp.setVaccineReportComments("");
					vaccineReportTemp.setVaccineReportIsemr(1);
					vaccineReportTemp.setVaccineReportUserid(performedby);
					Date dateMod=new Date();
					Timestamp timeStampMod=new Timestamp(dateMod.getTime());
					vaccineReportTemp.setVaccineReportLastModified(timeStampMod);
					vaccineReportRepository.saveAndFlush(vaccineReportTemp);
				}
				else{

					VaccineReport vaccineReportTemp=new VaccineReport();
					CriteriaBuilder builder1 = em.getCriteriaBuilder();
					CriteriaQuery<Object> cq1 = builder1.createQuery();
					Root<VaccineReport> root1 = cq1.from(VaccineReport.class);
					cq1.multiselect(builder1.coalesce(builder1.max(root1.get(VaccineReport_.vaccineReportId)),0));
					vaccineReportTemp.setVaccineReportId(Integer.parseInt(MoreObjects.firstNonNull(em.createQuery(cq1).getSingleResult(),0).toString())+1);
					vaccineReportTemp.setVaccineReportChartId(chartId);
					vaccineReportTemp.setVaccineReportEncounterId(labencId);
					vaccineReportTemp.setVaccineReportVaccineId(Integer.parseInt(testid));
					vaccineReportTemp.setVaccineReportGivenDate(Timestamp.valueOf(givenDate+" 00:00:00"));
					vaccineReportTemp.setVaccineReportDosageLevel(Integer.parseInt(dosage));
					vaccineReportTemp.setVaccineReportComments("");
					vaccineReportTemp.setVaccineReportIsemr(1);
					vaccineReportTemp.setVaccineReportUserid(performedby);
					Date dateMod=new Date();
					Timestamp timeStampMod=new Timestamp(dateMod.getTime());
					vaccineReportTemp.setVaccineReportLastModified(timeStampMod);
					vaccineReportRepository.saveAndFlush(vaccineReportTemp);
				}
			}
		}
	}

	public void increaseVaccineCount(String lotno)
	{
		Integer lotNoInt=-1;
		if(!lotno.trim().contentEquals(""))
			lotNoInt=Integer.parseInt(lotno);
		List<VaccineOrderDetails> vaccineReportList=vaccineOrderDetailsRepository.findAll(Specifications.where(VaccineReportSpecification.getLotNo(lotNoInt)));
		for(int j=0;j<vaccineReportList.size();j++){
			VaccineOrderDetails vaccineOrderTemp=vaccineReportList.get(j);
			vaccineOrderTemp.setVaccineOrderDetailsQtyUsed(vaccineReportList.get(j).getVaccineOrderDetailsQtyUsed()+1);
			vaccineOrderDetailsRepository.saveAndFlush(vaccineOrderTemp);
		}
	}
	public void increaseVaccineCount(String lotno,int incr)
	{
		Integer lotNoInt=-1;
		if(!lotno.trim().contentEquals(""))
			lotNoInt=Integer.parseInt(lotno);
		List<VaccineOrderDetails> vaccineReportList=vaccineOrderDetailsRepository.findAll(Specifications.where(VaccineReportSpecification.getLotNo(lotNoInt)));
		for(int j=0;j<vaccineReportList.size();j++){
			VaccineOrderDetails vaccineOrderTemp=vaccineReportList.get(j);
			vaccineOrderTemp.setVaccineOrderDetailsQtyUsed(vaccineReportList.get(j).getVaccineOrderDetailsQtyUsed()+incr);
			vaccineOrderDetailsRepository.saveAndFlush(vaccineOrderTemp);
		}
	}
	@SuppressWarnings("unused")
	public void increaseVaccineCount(String lotno,String dose) {
		String[] m1= dose.split("~");
		String unit = new String();
		int incr;
		try{
			unit = Optional.fromNullable(m1[1]).or("");
			incr = Integer.parseInt(m1[0]);
		}catch(Exception e)
		{
			incr = 1;
		}
		Integer lotNoInt=-1;
		if(!lotno.trim().contentEquals(""))
			lotNoInt=Integer.parseInt(lotno);
		List<VaccineOrderDetails> vaccineReportList=vaccineOrderDetailsRepository.findAll(Specifications.where(VaccineReportSpecification.getLotNo(lotNoInt)));
		for(int j=0;j<vaccineReportList.size();j++){
			VaccineOrderDetails vaccineOrderTemp=vaccineReportList.get(j);
			vaccineOrderTemp.setVaccineOrderDetailsQtyUsed(vaccineReportList.get(j).getVaccineOrderDetailsQtyUsed()+incr);
			vaccineOrderDetailsRepository.saveAndFlush(vaccineOrderTemp);
		}
	}

	public void decreaseVaccineCount(String lotno,String  dose)
	{
		String[] m1= dose.split("~");
		int incr;
		try{
			incr = Integer.parseInt(m1[0]);
		}catch(Exception e){
			incr = 0;
		}
		decreaseVaccineCount(lotno,incr);
	}

	public void decreaseVaccineCount(String lotno,int incr)
	{
		Integer lotNoInt=-1;
		if(!lotno.trim().contentEquals(""))
			lotNoInt=Integer.parseInt(lotno);
		List<VaccineOrderDetails> vaccineReportList=vaccineOrderDetailsRepository.findAll(Specifications.where(VaccineReportSpecification.getLotNo(lotNoInt)).and(VaccineReportSpecification.getQtyGreaterThan(0)));
		for(int j=0;j<vaccineReportList.size();j++){
			VaccineOrderDetails vaccineOrderTemp=vaccineReportList.get(j);
			vaccineOrderTemp.setVaccineOrderDetailsQtyUsed(vaccineReportList.get(j).getVaccineOrderDetailsQtyUsed()-incr);
			vaccineOrderDetailsRepository.saveAndFlush(vaccineOrderTemp);
		}
	}

	public void decreaseVaccineCount(String lotno)
	{
		Integer lotNoInt=-1;
		if(!lotno.trim().contentEquals(""))
			lotNoInt=Integer.parseInt(lotno);
		List<VaccineOrderDetails> vaccineReportList=vaccineOrderDetailsRepository.findAll(Specifications.where(VaccineReportSpecification.getLotNo(lotNoInt)).and(VaccineReportSpecification.getQtyGreaterThan(0)));
		for(VaccineOrderDetails vaccineOrder:vaccineReportList){
			VaccineOrderDetails vaccineOrderTemp=vaccineOrder;
			vaccineOrderTemp.setVaccineOrderDetailsQtyUsed(vaccineOrder.getVaccineOrderDetailsQtyUsed()-1);
			vaccineOrderDetailsRepository.saveAndFlush(vaccineOrderTemp);
		}
	}

	public void SaveParameters (int chartId, int testDetailId, String paramString) throws Exception {
		setResourceBundle();
		logger.debug("Begin of save parameter logic.");
		String[] paramSet = paramString.split("@#@");
		String[] param;
		int paramId = -1;
		int paramLen = 0;
		int sortOrder = 1;
		for(int i=0; i<paramSet.length; i++) {
			param = paramSet[i].split("\\|~\\|");// |~|
			paramLen = param.length;
			if(paramLen > paramValueMap("PARAM_ID")){
				paramId = Integer.parseInt(Optional.fromNullable(param[paramValueMap("PARAM_ID")]).or("-1").trim());
			}
			if(paramId == -1) {
				String paramName	= "";
				String paramUnits	= "";
				String NormalRange	= "";
				String paramCode	= "";
				String paramCodeSys	= "";
				if(paramLen > paramValueMap("PARAM_NAME"))
					paramName = HUtil.ValidateSingleQuote(Optional.fromNullable(param[paramValueMap("PARAM_NAME")]).or("")).trim();
				if(paramLen > paramValueMap("PARAM_UNITS"))
					paramUnits = HUtil.ValidateSingleQuote(Optional.fromNullable(param[paramValueMap("PARAM_UNITS")]).or("")).trim();
				if(paramLen > paramValueMap("PARAM_NORMALRANGE"))
					NormalRange = HUtil.ValidateSingleQuote(Optional.fromNullable(param[paramValueMap("PARAM_NORMALRANGE")]).or("")).trim();
				if(paramLen > paramValueMap("PARAM_CODE"))
					paramCode = HUtil.ValidateSingleQuote(Optional.fromNullable(param[paramValueMap("PARAM_CODE")]).or("")).trim();
				if(paramLen > paramValueMap("PARAM_CODESYSTEM"))
					paramCodeSys = HUtil.ValidateSingleQuote(Optional.fromNullable(param[paramValueMap("PARAM_CODESYSTEM")]).or("")).trim();
				paramId = insertParamDesc(paramName, paramUnits, NormalRange, paramCode, paramCodeSys);
			}
			if(paramId != -1) {
				int labEntryParamId = -1;					//lab_entries_parameter_id - to identify insert or update in lab_entries_parameter table
				String paramValue = "";
				String paramDate  = "";
				String paramStatus = "";
				String paramNotes = "";
				String paramResultStatus = "";
				String normalRange	= "";
				if(paramLen > paramValueMap("PARAM_LABENTRYID"))
					labEntryParamId = Integer.parseInt(Optional.fromNullable(param[paramValueMap("PARAM_LABENTRYID")]).or("-1").trim());
				if(paramLen > paramValueMap("PARAM_VALUE"))
					paramValue = HUtil.ValidateSingleQuote(Optional.fromNullable(param[paramValueMap("PARAM_VALUE")]).or("")).trim();
				if(paramLen > paramValueMap("PARAM_DATE")){
					String dateString=HUtil.ValidateSingleQuote(Optional.fromNullable(param[paramValueMap("PARAM_DATE")]).or("")).trim();
					if(!dateString.equalsIgnoreCase("")) {
						DateFormat parser = new SimpleDateFormat("MM/dd/yyyy"); 
						Date date = (Date) parser.parse(dateString);
						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						dateString=formatter.format(date);
					}
					paramDate=dateString;

				}
				if(paramLen > paramValueMap("PARAM_STATUS"))
					paramStatus = HUtil.ValidateSingleQuote(Optional.fromNullable(param[paramValueMap("PARAM_STATUS")]).or("")).trim();
				if(paramLen > paramValueMap("PARAM_NOTES"))
					paramNotes = HUtil.ValidateSingleQuote(Optional.fromNullable(param[paramValueMap("PARAM_NOTES")]).or(""));
				if(paramLen > paramValueMap("PARAM_RESULTSTATUS"))
					paramResultStatus = HUtil.ValidateSingleQuote(Optional.fromNullable(param[paramValueMap("PARAM_RESULTSTATUS")]).or("")).trim();
				if(labEntryParamId==-1)
					DeleteParameters(testDetailId,paramId);
				if(paramLen > paramValueMap("PARAM_NORMALRANGE"))
					normalRange = HUtil.ValidateSingleQuote(Optional.fromNullable(param[paramValueMap("PARAM_NORMALRANGE")]).or("")).trim();
				insertParamValue(chartId, labEntryParamId, testDetailId, paramId, paramValue, paramDate, paramStatus, paramNotes, paramResultStatus, sortOrder , normalRange);
				sortOrder++;
			}
		}
		logger.debug("End of save parameter logic.");
	}

	public void insertParamValue(int chartId, int labEntryParamId, int testDetailId, int paramId, String paramValue, String performedDate, String paramStatus, String paramNotes, String paramResultStatus, int sortOrder , String normalRange) throws Exception {
		if(labEntryParamId == -1) {
			List<LabEntriesParameter> labEntriesParameterList=labEntriesParameterRepository.findAll(Specifications.where(InvestigationSpecification.getParamEntriesIsActive(true)).and(InvestigationSpecification.getParamEntriesTestDetailIdNot(-1)).and(InvestigationSpecification.getParamEntriesTestDetailId(testDetailId)).and(InvestigationSpecification.getParamEntriesMapId(paramId)));
			if(labEntriesParameterList.size()>0)
				labEntryParamId = Optional.fromNullable(labEntriesParameterList.get(0).getLabEntriesParameterId()).or(-1);
		}
		if(labEntryParamId != -1) {
			List<LabEntriesParameter> labEntriesParameterList=labEntriesParameterRepository.findAll(InvestigationSpecification.getParamEntriesId(labEntryParamId));
			for(int h=0;h<labEntriesParameterList.size();h++){
				labEntriesParameterList.get(h).setLabEntriesParameterValue(paramValue);
				labEntriesParameterList.get(h).setLabEntriesParameterStatus(paramStatus);
				labEntriesParameterList.get(h).setLabEntriesParameterNotes(paramNotes);
				labEntriesParameterList.get(h).setLabEntriesParameterChartid(chartId);
				if(!performedDate.equals(""))
					labEntriesParameterList.get(h).setLabEntriesParameterDate(Timestamp.valueOf(performedDate));
				labEntriesParameterList.get(h).setLabEntriesParameterResultstatus(paramResultStatus);
				labEntriesParameterList.get(h).setLabEntriesParameterSortorder(sortOrder);
				if(!normalRange.equals(""))
					labEntriesParameterList.get(h).setLabEntriesParameterNormalrange(normalRange);
				labEntriesParameterRepository.saveAndFlush(labEntriesParameterList.get(h));
			}
		}else {
			LabEntriesParameter labEntriesParameterTemp=new LabEntriesParameter();
			labEntriesParameterTemp.setLabEntriesParameterTestdetailid(testDetailId);
			labEntriesParameterTemp.setLabEntriesParameterMapid(paramId);
			labEntriesParameterTemp.setLabEntriesParameterValue(paramValue);
			labEntriesParameterTemp.setLabEntriesParameterStatus(paramStatus);
			labEntriesParameterTemp.setLabEntriesParameterNotes(paramNotes);
			labEntriesParameterTemp.setLabEntriesParameterChartid(chartId);
			if(!performedDate.equals(""))
				labEntriesParameterTemp.setLabEntriesParameterDate(Timestamp.valueOf(performedDate));
			labEntriesParameterTemp.setLabEntriesParameterResultstatus(paramResultStatus);
			labEntriesParameterTemp.setLabEntriesParameterSortorder(sortOrder);
			if(!normalRange.equals(""))
				labEntriesParameterTemp.setLabEntriesParameterNormalrange(normalRange);
			labEntriesParameterRepository.saveAndFlush(labEntriesParameterTemp);
		}
	}

	public int insertParamDesc(String paramName, String paramUnits, String NormalRange, String paramCode, String paramCodeSys) throws Exception {
		if(paramCodeSys.equals("LN")){
			paramCodeSys = "LOINC"; 
		}
		int paramId = -1;
		if( (!paramCode.equals("")) && (!paramCode.equalsIgnoreCase("null")) && (!paramCode.equalsIgnoreCase("undefined")) && (!paramCodeSys.equals("")) ){
			List<LabParameterCode> labParameterCodeList=labParameterCodeRepository.findAll(Specifications.where(InvestigationSpecification.getCode(paramCode)).and(InvestigationSpecification.getCodeSystem(paramCodeSys)));
			if( labParameterCodeList.size() > 0 ) {
				paramId = Optional.fromNullable(labParameterCodeList.get(0).getLabParameterCodeParamid()).or(-1);
			}
		}
		List<LabParameters> labParametersList=labParametersRepository.findAll(InvestigationSpecification.getParamDetails(paramId));
		int count=Optional.fromNullable(labParametersList.size()).or(0);
		if(paramId == -1 || count==0) {
			if(!paramName.equals("")) {
				if(!paramUnits.equals("")){
					List<LabParameters> labParameterList=labParametersRepository.findAll(Specifications.where(InvestigationSpecification.getParamDisplayname(paramName, true)).and(InvestigationSpecification.getParamUnits(paramUnits, true)));
					paramId = Optional.fromNullable(labParameterList.get(0).getLabParametersId()).or(-1);	
				}
				if(paramId == -1 || count==0) {					//If both paramName and LOINC Code doesn't matches, then new entry is inserted in lab_parameters table.
					LabParameters labParametersTemp=new LabParameters();
					labParametersTemp.setLabParametersName(paramName);
					labParametersTemp.setLabParametersDisplayname(paramName);
					labParametersTemp.setLabParametersUnits(paramUnits);
					labParametersTemp.setLabParametersNormalrange(NormalRange);
					labParametersTemp.setLabParametersIsactive(true);
					LabParameters labParam=labParametersRepository.saveAndFlush(labParametersTemp);
					paramId = labParam.getLabParametersId();
					insertParamCodeSystem(paramId, paramCode, paramCodeSys);
				}else {
					insertParamCodeSystem(paramId, paramCode, paramCodeSys);
					updateParamRange(paramId, NormalRange);
				}
			}
		}else {
			updateParamRange(paramId, NormalRange);
			updateParamUnits(paramId, paramUnits);
		}
		return paramId;
	}

	public void updateParamRange(int paramId, String NormalRange) throws Exception {
		if(paramId != -1 && !NormalRange.equals("")) {				//For updating normal range values
			List<LabParameters> labParametersList=labParametersRepository.findAll(InvestigationSpecification.getParamDetails(paramId));
			for(int j=0;j<labParametersList.size();j++){
				if(Optional.fromNullable(labParametersList.get(j).getLabParametersNormalrange()).or("").equals("")) {
					LabParameters labParametersTemp=labParametersList.get(j);
					labParametersTemp.setLabParametersNormalrange(NormalRange);
					labParametersRepository.saveAndFlush(labParametersTemp);
				}
			}
		}
	}

	public void updateParamUnits(int paramId, String paramUnits) throws Exception {
		if(paramId != -1 && !paramUnits.equals("")) {				//For updating normal range values
			List<LabParameters> labParametersList=labParametersRepository.findAll(InvestigationSpecification.getParamDetails(paramId));
			for(int j=0;j<labParametersList.size();j++){
				if(Optional.fromNullable(labParametersList.get(j).getLabParametersUnits()).or("").equals("")) {
					LabParameters labParametersTemp=labParametersList.get(j);
					labParametersTemp.setLabParametersUnits(paramUnits);
					labParametersRepository.saveAndFlush(labParametersTemp);
				}
			}
		}
	}

	public void insertParamCodeSystem(int paramId, String paramCode, String paramCodeSys) throws Exception {
		if(paramId != -1 && !paramCode.equals("") && !paramCodeSys.equals("")) {				//For inserting code and codesystem values
			LabParameterCode labParameterCode=new LabParameterCode();
			labParameterCode.setLabParameterCodeParamid(paramId);
			labParameterCode.setLabParameterCodeValue(paramCode);
			labParameterCode.setLabParameterCodeSystem(paramCodeSys);
			labParameterCodeRepository.saveAndFlush(labParameterCode);
		}
	}

	public void DeleteParameters (int testDetailId) throws Exception {
		if(testDetailId != -1) {
			List<LabEntriesParameter> labEntriesParameterList=labEntriesParameterRepository.findAll(Specifications.where(InvestigationSpecification.getParamEntriesTestDetailId(testDetailId)));
			for(int j=0;j<labEntriesParameterList.size();j++){
				LabEntriesParameter labEntriesParameterTemp=labEntriesParameterList.get(j);
				labEntriesParameterTemp.setLabEntriesParameterIsactive(false);
				labEntriesParameterRepository.saveAndFlush(labEntriesParameterTemp);
			}
		}
	}

	public void DeleteParameters (int testDetailId,int paramId) throws Exception {
		if(testDetailId != -1) {
			List<LabEntriesParameter> labEntriesParameterList=labEntriesParameterRepository.findAll(Specifications.where(InvestigationSpecification.getParamEntriesTestDetailId(testDetailId)).and(InvestigationSpecification.getParamEntriesMapId(paramId)));
			for(int j=0;j<labEntriesParameterList.size();j++){
				LabEntriesParameter labEntriesParameterTemp=labEntriesParameterList.get(j);
				labEntriesParameterTemp.setLabEntriesParameterIsactive(false);
				labEntriesParameterRepository.saveAndFlush(labEntriesParameterTemp);
			}
		}
	}

	public void PreviousVisitLabLog(int labid)throws Exception{		//Labs that are ordered in previous visit and performed in this visit 
		List<LabIncludePrevious> labIncludePreviousList=labIncludePreviousRepository.findAll(Specifications.where(InvestigationSpecification.labIncludeLabId(labid)).and(InvestigationSpecification.labIncludeEncounterId(encounterId)));
		long count = labIncludePreviousList.size();
		if(count <= 0){
			LabIncludePrevious labIncludePreviousTemp=new LabIncludePrevious();
			labIncludePreviousTemp.setLabIncludePreviousLabid(labid);
			labIncludePreviousTemp.setLabIncludePreviousEncounterid(encounterId);
			labIncludePreviousRepository.saveAndFlush(labIncludePreviousTemp);
		}
	}

	public void updateLabHitCount(int labTestId)throws Exception{
		List<LabDescription> labDescriptionList=labDescriptionRepository.findAll(InvestigationSpecification.testIdsLabDescription(new Integer[]{labTestId}));
		for(int i=0;i<labDescriptionList.size();i++){
			labDescriptionList.get(i).setLabDescriptionHitcount(Optional.fromNullable(labDescriptionList.get(i).getLabDescriptionHitcount()).or(0)+1);
			labDescriptionRepository.saveAndFlush(labDescriptionList.get(i));
		}
	}

	public int paramValueMap(String xStr) throws Exception {
		return Integer.parseInt(resBun.getString(xStr));
	}

	public int dataValueMap(String xStr) throws Exception{
		String str = xStr.toLowerCase();
		String[] qstr = str.split("_col_");
		String tmpstr = qstr[0];
		return Integer.parseInt(resBun.getString(tmpstr));
	}

	public void setResourceBundle() throws Exception{
		logger.debug("Begin of setting resource bundle.");
		Locale locale   =  LocaleContextHolder.getLocale();
		locale = (locale == null)?Locale.getDefault():locale;
		resBun = ResourceBundle.getBundle("com.glenwood.glaceemr.server.application.services.investigation.Investigation",locale);
		if ( resBun == null )
			throw new Exception("Unable to load the resource");
		logger.debug("End of setting resource bundle.");
	}

	public void insert_assc_prblm (String dxcode,int entityid,int entitytype,int userid)
	{
		AssociatedOrderPblm associatedOrderPblm=new AssociatedOrderPblm();
		associatedOrderPblm.setAssociatedOrderPblmDxcode(dxcode);
		associatedOrderPblm.setAssociatedOrderPblmEntityId(entityid);
		associatedOrderPblm.setAssociatedOrderPblmEntityType(entitytype);
		associatedOrderPblm.setAssociatedOrderPblmAssocBy(userid);
		Date date=new Date();
		Timestamp timeStamp=new Timestamp(date.getTime());
		associatedOrderPblm.setAssociatedOrderPblmAssocOn(timeStamp);
		associatedOrderPblm.setAssociatedOrderPblmFlag(true);
		associatedOrderPblmRepository.saveAndFlush(associatedOrderPblm);
	}
}