package com.glenwood.glaceemr.server.application.services.Documents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.persistence.criteria.CriteriaQuery;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import com.glenwood.glaceemr.server.application.Bean.DocumentsCategoryBean;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.AlertEvent_;
import com.glenwood.glaceemr.server.application.models.AlertPatientDocMapping;
import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.CurrentMedication;
import com.glenwood.glaceemr.server.application.models.CurrentMedication_;
import com.glenwood.glaceemr.server.application.models.DoctorSign;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Encounter_;
import com.glenwood.glaceemr.server.application.models.FileDetails;
import com.glenwood.glaceemr.server.application.models.FileDetails_;
import com.glenwood.glaceemr.server.application.models.FileName;
import com.glenwood.glaceemr.server.application.models.FileName_;
import com.glenwood.glaceemr.server.application.models.FormsTemplate;
import com.glenwood.glaceemr.server.application.models.Guarantor;
import com.glenwood.glaceemr.server.application.models.Guarantor_;
import com.glenwood.glaceemr.server.application.models.H076;
import com.glenwood.glaceemr.server.application.models.H076_;
import com.glenwood.glaceemr.server.application.models.H478;
import com.glenwood.glaceemr.server.application.models.H478_;
import com.glenwood.glaceemr.server.application.models.H611;
import com.glenwood.glaceemr.server.application.models.H611_;
import com.glenwood.glaceemr.server.application.models.InitialSettings;
import com.glenwood.glaceemr.server.application.models.InitialSettings_;
import com.glenwood.glaceemr.server.application.models.InsCompAddr;
import com.glenwood.glaceemr.server.application.models.InsCompAddr_;
import com.glenwood.glaceemr.server.application.models.InsCompany;
import com.glenwood.glaceemr.server.application.models.InsCompany_;
import com.glenwood.glaceemr.server.application.models.PatientAllergies;
import com.glenwood.glaceemr.server.application.models.PatientAllergies_;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements_;
import com.glenwood.glaceemr.server.application.models.PatientDocumentsCategory;
import com.glenwood.glaceemr.server.application.models.PatientDocumentsCategory_;
import com.glenwood.glaceemr.server.application.models.PatientDocumentsNotes;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.PatientSignature;
import com.glenwood.glaceemr.server.application.models.PatientSignature_;
import com.glenwood.glaceemr.server.application.models.PharmDetails;
import com.glenwood.glaceemr.server.application.models.PharmDetails_;
import com.glenwood.glaceemr.server.application.models.ServiceDetail;
import com.glenwood.glaceemr.server.application.models.ServiceDetail_;
import com.glenwood.glaceemr.server.application.repositories.AlertInboxRepository;
import com.glenwood.glaceemr.server.application.repositories.AlertPatientDocMappingRepository;
import com.glenwood.glaceemr.server.application.repositories.DoctorsignRepository;
import com.glenwood.glaceemr.server.application.repositories.EmployeeProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.FormsTemplateRepository;
import com.glenwood.glaceemr.server.application.repositories.InitialSettingsRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientDocumentsNotesRepository;
import com.glenwood.glaceemr.server.application.repositories.FileDetailsRepository;
import com.glenwood.glaceemr.server.application.repositories.FileNameRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientDocumentsCategoryRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientSignatureRepository;
import com.glenwood.glaceemr.server.application.specifications.DocumentsSpecification;
import com.glenwood.glaceemr.server.application.specifications.PatientRegistrationSpecification;
import com.glenwood.glaceemr.server.utils.HUtil;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.google.common.base.Optional;
/**
 * Service Implementation for PatientDocuments
 * @author Soundarya
 *
 */

@Service
@Transactional

public  class DocumentsServiceImpl implements DocumentsService{

	@Autowired
	EntityManagerFactory emf ;

	@PersistenceContext
	EntityManager em;

	@Autowired
	FileDetailsRepository fileDetailsRepository;

	@Autowired
	FileNameRepository fileNameRepository;


	@Autowired
	PatientDocumentsCategoryRepository patientDocCategoryRepository;

	@Autowired
	PatientRegistrationRepository patientRegistrationRepository;

	@Autowired
	AlertInboxRepository alertInboxRepository;

	@Autowired
	PatientDocumentsNotesRepository docNotesRepository;

	@Autowired
	AlertPatientDocMappingRepository alertPatientDocMappingRepository;

	@Autowired
	FormsTemplateRepository formsTemplateRepository;

	@Autowired
	EmployeeProfileRepository employeeProfileRepository;

	@Autowired
	DoctorsignRepository doctorsignRepository;

	@Autowired
	PatientSignatureRepository patientSignatureRepository ;

	@Autowired
	InitialSettingsRepository initialSettingsRepository;

	@Autowired
	SessionMap sessionMap;

	@Autowired
	HttpServletRequest request;

	String fileSeparator = "";
	String sharedFolder = "";
	String htmlFile = "";
	String physicianSignImg="";
	String patientSignImg="";
	String witnessSignImg="";
	private String practiceName="";
	private String practiceAddressLine1="";
	private String practiceAddressLine2="";
	private String practicephoneno="";
	private String practicemailid="";
	private String dxcode="";
	private String allergyname="";
	private String currdrugname="";
	private String serDocotorName="";
	private String serDocAddress="";
	private String serDocCity="";
	private String serDocState="";
	private String serDocZip="";
	private String serDocPhNumber="";
	private String serDocLicenseNo="";
	private String othDocotorName="";
	private String othSpeciality="";
	private String othDocCity="";
	private String othDocState="";
	private String othDocPhNumber="";
	String rdocname="";
	String height="";
	String inounce="";
	String Weight="";
	String temp="";
	String pulse="";
	String respiratory="";

	String cdurgname="";
	String cstrength="";
	String cform="";
	String sittingrasystolicbp="";
	String sittingradiastolicbp="";

	String standingrasystolicbp="";
	String standingradiastolicbp="";

	String sittinglasystolicbp="";
	String sittingladiastolicbp="";

	String standinglasystolicbp="";
	String standingladiastolicbp="";	

	String lyingrasystolicbp="";
	String lyingradiastolicbp="";

	String lyinglasystolicbp="";
	String lyingladiastolicbp="";

	String systolicbp="";
	String diastolicbp="";

	String sysbp="";
	String diabp="";

	JSONObject result=null;

	/**
	 * TO get category list based on patientId
	 * If the patientId is not provided then it displays all category list in Attach option
	 * @param patientId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getCategoryList(int patientId) {

		if(patientId!= -1 ){
			List<Object> getCategoryDetails= new ArrayList<Object>();
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq = builder.createQuery();
			Root<FileDetails> root = cq.from(FileDetails.class);
			Join<FileDetails, PatientDocumentsCategory> catid=root.join(FileDetails_.patientDocCategory,JoinType.INNER);
			cq.multiselect(builder.construct(DocumentsCategoryBean.class,
					catid.get(PatientDocumentsCategory_.patientDocCategoryId),
					catid.get(PatientDocumentsCategory_.patientDocCategoryName),
					builder.count(root.get(FileDetails_.filedetailsId))));
			Predicate activeDocCategory= builder.equal(catid.get(PatientDocumentsCategory_.patientDocCategoryIsactive),true);
			Predicate patpredicate= builder.equal(root.get(FileDetails_.filedetailsPatientid),patientId);
			cq.where(builder.and(activeDocCategory,patpredicate)).getRestriction();
			cq.groupBy(catid.get(PatientDocumentsCategory_.patientDocCategoryId),catid.get(PatientDocumentsCategory_.patientDocCategoryName));
			cq.orderBy(builder.asc(catid.get(PatientDocumentsCategory_.patientDocCategoryName)));
			getCategoryDetails=em.createQuery(cq).getResultList();
			return getCategoryDetails;

		}
		else{
			List<PatientDocumentsCategory> patientDocumentsCategories=new ArrayList<PatientDocumentsCategory>();
			patientDocumentsCategories=patientDocCategoryRepository.findAll(DocumentsSpecification.getAllCategories());
			List<Object> getAllCategories= new ArrayList<Object>();
			for (PatientDocumentsCategory patientDocumentsCategory : patientDocumentsCategories) {
				getAllCategories.add(patientDocumentsCategory);
			}
			return getAllCategories;
		}
	}



	/**
	 * To get particular folders of patient based on patientId and CategoryId
	 * @param patientId
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<FileDetails> getFileDetailsByPatientId(int patientId, int categoryId) {
		List<FileDetails> fileDetails = null;
		fileDetails = fileDetailsRepository.findAll(DocumentsSpecification.getFileCategoryList(patientId,categoryId));
		return fileDetails;
	}

	/**
	 * To get the list of files 
	 * @param fileDetailsId
	 * @return
	 * @throws Exception
	 */
	@Override
	public FileDetails getFileList(String fileDetailsId) {
		List<FileDetails> fileDetails=null;
		fileDetails=fileDetailsRepository.findAll(DocumentsSpecification.getFileList(fileDetailsId));
		FileDetails fileDetail=null;
		if(fileDetails.size()>0){
			fileDetail=fileDetails.get(0);
		}

		return fileDetail;
	}

	/**
	 * To get Info about documents
	 * @param fileNameId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<FileName> getInfo(int fileNameId) {
		List<FileName> fileName=null;
		fileName = fileNameRepository.findAll(DocumentsSpecification.getInfo(fileNameId));
		return fileName;
	}



	/**
	 * To get Document notes
	 * @param notesFilenameId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<PatientDocumentsNotes> getDocNotesDetails(int notesFilenameId) {
		List<PatientDocumentsNotes> PatientDocumentsNotes=null;
		PatientDocumentsNotes=docNotesRepository.findAll(DocumentsSpecification.getDocNotes(notesFilenameId));
		return PatientDocumentsNotes;
	}

	/**
	 * To add comments in Notes option
	 * @param notesFilenameId
	 * @param notesPatientNotes
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<PatientDocumentsNotes> addDocNotesDetails(int notesFilenameId, String notesPatientNotes,int userId) {
		List<PatientDocumentsNotes> PatientDocumentsNotes=null;

		Date date= new Date();
		PatientDocumentsNotes pd=new PatientDocumentsNotes();
		pd.setNotesFilenameid(notesFilenameId);
		pd.setNotesPatientnotes(notesPatientNotes);
		pd.setNotesCreatedby(userId);
		pd.setNotesCreatedon(new Timestamp(date.getTime()));
		pd.setNotesModifiedby(userId);
		pd.setNotesModifiedon(new Timestamp(date.getTime()));
		docNotesRepository.saveAndFlush(pd);
		PatientDocumentsNotes=docNotesRepository.findAll(DocumentsSpecification.getDocNotes(notesFilenameId));

		return PatientDocumentsNotes;
	}

	/**
	 * To delete a folder by passing its folderId
	 * @param fileDetailsId
	 * @throws Exception
	 */
	@Override
	public void deleteFolder(String fileDetailsId) {

		List<FileName> fileNameList= fileNameRepository.findAll(DocumentsSpecification.getFileNameDetails(fileDetailsId));
		fileNameRepository.deleteInBatch(fileNameList);

		List<FileDetails> fileDetails = fileDetailsRepository.findAll(DocumentsSpecification.deleteFolder( fileDetailsId));
		fileDetailsRepository.deleteInBatch(fileDetails);

	}

	/**
	 * To delete a file by passing its fileId
	 * @param fileNameId
	 * @throws Exception
	 */
	@Override
	public void deleteFile(int fileNameId,int patientId) {
		List<FileName> fileNameList = fileNameRepository.findAll(DocumentsSpecification.deleteFiles(fileNameId));
		fileNameRepository.deleteInBatch(fileNameList);

		List<Integer> getDetails= new ArrayList<Integer>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<FileName> root = cq.from(FileName.class);
		cq.distinct(true);
		cq.select(builder.construct(Integer.class,root.get(FileName_.filenameScanid)));

		List<Object> getDetailsResultSet=em.createQuery(cq).getResultList();
		for(Object result:getDetailsResultSet){
			getDetails.add((Integer) result);
		}
		if(getDetails.size()>0){
			List<FileDetails> fileName = fileDetailsRepository.findAll(DocumentsSpecification.deleteAFile(fileNameId,getDetails,patientId));
			fileDetailsRepository.deleteInBatch(fileName);
		}
	}

	/**
	 * To review group of documents
	 * @param fileDetailsId
	 * @param patientId
	 * @param categoryId
	 * @param userId
	 * @return
	 */
	@Override
	public List<FileDetails> reviewGroupOfDocs(String fileDetailsId,
			int categoryId, int patientId, int userId) {
		List<FileDetails> fileDetails=fileDetailsRepository.findAll(DocumentsSpecification.byFileDetailsId(fileDetailsId,categoryId,patientId,userId));
		Date date= new Date();
		for(int i=0;i<fileDetails.size();i++){
			int fd=fileDetails.get(i).getFileName().size();
			for(int j=0;j<fd;j++){
				FileName fileName= fileDetails.get(i).getFileName().get(j);
				fileName.setFilenameIsreviewed(true);
				fileName.setFilenameReviewedby(userId);
				fileName.setFilenameReviewedon(new Timestamp(date.getTime()));
				fileNameRepository.saveAndFlush(fileName);
			}

		}

		return fileDetails;
	}

	/**
	 * To review a single file
	 * @param fileNameId
	 * @param userId
	 * @return
	 */
	@Override
	public List<FileName> reviewDocuments(int fileNameId, int userId) {
		List<FileName> fileNameList=fileNameRepository.findAll(DocumentsSpecification.byfileNameId(fileNameId));
		Date date=new Date();
		for(int i=0;i<fileNameList.size();i++){
			FileName fileName=fileNameList.get(i);
			fileName.setFilenameIsreviewed(true);
			fileName.setFilenameReviewedby(userId);
			fileName.setFilenameReviewedon(new Timestamp(date.getTime()));
			fileNameRepository.saveAndFlush(fileName);
		}

		return fileNameList;
	}

	/**
	 * To get details when a message is forwarded from patient documents
	 * @param alertId
	 * @param patientId
	 * @return
	 */
	@Override
	public FileDetails alertByCategory(String alertId,int patientId) {
		List<AlertPatientDocMapping> alertPatientDocMapping= alertPatientDocMappingRepository.findAll(DocumentsSpecification.getalertByCategory(alertId));
		List<FileName> fileName=null;
		FileDetails fileDetails=null;
		if(alertPatientDocMapping.size()>0){
			Integer fileNameId=Integer.parseInt(alertPatientDocMapping.get(0).getForwardedFiledetailsId());
			fileName=getInfo(fileNameId);
			for(int i=0;i<fileName.size();i++)	{
				String scanid=fileName.get(0).getFilenameScanid().toString();
				fileDetails=getFileList(scanid);
			}
		}
		return fileDetails;
	}

	/**
	 * To forward the documents using alerts
	 * @param fromid
	 * @param toid
	 * @param status
	 * @param alertid
	 * @param docCategoryid
	 * @param refid
	 * @param patientid
	 * @param encounterid
	 * @param msg
	 * @param chartid
	 * @param roomid
	 * @param parentid
	 * @return
	 */
	@Override
	public List<AlertEvent> forwardAlert(int fromId, List<Integer> toIdList,
			int status, String alertid,int docCategoryid,int refId, int patientId,
			int encounterId, String msg, int chartId, int roomId, int parentId) {

		String lastName="", firstName="", midInitial="";
		int  refid=refId;
		if(alertid.equals("37")){
			CriteriaBuilder qb = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq = qb.createQuery();

			Root<AlertEvent> root = cq.from(AlertEvent.class);
			cq.select(qb.max(root.get(AlertEvent_.alertEventRefId)));
			cq.where(qb.equal(root.get(AlertEvent_.alertEventCategoryId),37));

			Query query=em.createQuery(cq);
			Object maxIdObj=query.getSingleResult();

			if(maxIdObj!=null)
				refid = (int) maxIdObj;

			if(refid < 1)
				refid = 1;
		}
		PatientDocumentsCategory category=patientDocCategoryRepository.findOne(DocumentsSpecification.CatId1(docCategoryid));
		PatientRegistration patient=patientRegistrationRepository.findOne(PatientRegistrationSpecification.PatientId(patientId+""));
		if(patient!=null){

			lastName=Optional.fromNullable(patient.getPatientRegistrationLastName()).or(" ");
			firstName=Optional.fromNullable(patient.getPatientRegistrationFirstName()).or(" ");
			midInitial=Optional.fromNullable(patient.getPatientRegistrationMidInitial()).or(" ");
		}
		List<AlertEvent> alertEventList=null;
		List<Integer> alertId=new ArrayList<>();
		for(int i=0;i<toIdList.size();i++)
		{
			String categoryName=category.getPatientDocCategoryName();
			PatientDocumentsCategory docCat=new PatientDocumentsCategory();
			AlertEvent aug=new AlertEvent();
			aug.setAlertEventFrom(fromId);
			aug.setAlertEventTo(toIdList.get(i));
			aug.setAlertEventStatus(status);
			aug.setAlertEventCategoryId(Integer.parseInt(alertid));
			aug.setAlertEventRefId(refid);
			aug.setAlertEventPatientId(patientId);
			if(patient!=null)
				aug.setAlertEventPatientName(lastName+", "+firstName+" "+midInitial);		//Lastname, Firstname MidInitial
			aug.setAlertEventEncounterId(encounterId);
			aug.setAlertEventCreatedDate(new Timestamp(new Date().getTime()));
			docCat.setPatientDocCategoryId(docCategoryid);
			msg+="--"+categoryName+" category.";
			aug.setAlertEventMessage(msg);
			aug.setAlertEventModifiedby(fromId);
			aug.setAlertEventChartId(chartId);
			aug.setAlertEventRoomId(roomId);
			aug.setAlertEventParentalertid(parentId);
			alertInboxRepository.save(aug);
			alertId.add(aug.getAlertEventId());

		}
		alertEventList=alertInboxRepository.findAll(DocumentsSpecification.byAlertId(alertId));
		return alertEventList;
	}



	@Override
	public List<FormsTemplate> getConsentForms() {
		List<FormsTemplate> formsTemplate=formsTemplateRepository.findAll(DocumentsSpecification.getConsentFormDetails());
		return formsTemplate;
	}



	@Override
	public List<Object> getSavedForms(String patientId) {
		List<Object> getForms= new ArrayList<Object>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<FileDetails> root = cq.from(FileDetails.class);
		Join<FileDetails, FileName> filename=root.join(FileDetails_.fileName,JoinType.INNER);
		Join<FileDetails, PatientDocumentsCategory> patDocCategory=root.join(FileDetails_.patientDocCategory,JoinType.INNER);
		Join<FileName, EmployeeProfile> empprofile=filename.join(FileName_.createdByEmpProfileTable);
		cq.multiselect(builder.construct(ConsentFormsBean.class, 
				patDocCategory.get(PatientDocumentsCategory_.patientDocCategoryName),
				root.get(FileDetails_.filedetailsId),
				root.get(FileDetails_.filedetailsDescription),
				root.get(FileDetails_.filedetailsComments),
				builder.function("to_char", String.class, root.get(FileDetails_.filedetailsCreationdate),builder.literal("MM/dd/yyyy HH:MI:ss am")),
				builder.coalesce(filename.get(FileName_.filenameIsreviewed), false),
				builder.count(root.get(FileDetails_.filedetailsId)),
				filename.get(FileName_.filenameName),
				empprofile.get(EmployeeProfile_.empProfileFullname)
				));
		Predicate patId=builder.equal(root.get(FileDetails_.filedetailsPatientid),patientId);
		Predicate patIsActive=builder.equal(patDocCategory.get(PatientDocumentsCategory_.patientDocCategoryIsactive), true);
		Predicate fileNameIsActive=builder.equal(filename.get(FileName_.filenameIsactive), true);
		Predicate catId=builder.equal(root.get(FileDetails_.filedetailsCategoryid), 3);
		cq.where(builder.and(patId,patIsActive,fileNameIsActive,catId));
		cq.groupBy(patDocCategory.get(PatientDocumentsCategory_.patientDocCategoryName),
				root.get(FileDetails_.filedetailsId),
				root.get(FileDetails_.filedetailsDescription),
				root.get(FileDetails_.filedetailsComments),
				root.get(FileDetails_.filedetailsCreationdate),
				filename.get(FileName_.filenameIsreviewed),
				patDocCategory.get(PatientDocumentsCategory_.patientDocCategoryOrder),
				patDocCategory.get(PatientDocumentsCategory_.patientDocCategoryId),
				root.get(FileDetails_.filedetailsBuffer3),
				filename.get(FileName_.filenameName),
				empprofile.get(EmployeeProfile_.empProfileFullname));
		cq.orderBy(builder.asc(patDocCategory.get(PatientDocumentsCategory_.patientDocCategoryName)),
				builder.asc(patDocCategory.get(PatientDocumentsCategory_.patientDocCategoryOrder)),
				builder.asc(patDocCategory.get(PatientDocumentsCategory_.patientDocCategoryId)),
				builder.desc(root.get(FileDetails_.filedetailsBuffer3)),
				builder.desc(root.get(FileDetails_.filedetailsCreationdate)),
				builder.asc(root.get(FileDetails_.filedetailsId)));
		getForms=em.createQuery(cq).getResultList();
		return getForms;
	}



	@Override
	public String getForms(String templateId,String patientId) {
		FormsTemplate template=formsTemplateRepository.findOne(DocumentsSpecification.templateDetails(templateId));
		PDFFormTemplateBean templateBean=new PDFFormTemplateBean();
		if(template != null){
			templateBean.setTemplateId(template.getFormsTemplateId());
			templateBean.setTemplateName(template.getFormsTemplateName());
			templateBean.setTemplateFileName(template.getFormsTemplateFilename());
			templateBean.setTemplateType(template.getFormsTemplateType());
			templateBean.setTemplateThumbnail(template.getFormsTemplateThumbnail());
			templateBean.setTemplateIsactive(template.getFormsTemplateIsactive());
		}
		
		int principalDrId=getPatientRegistrationPrincipalDoctor(patientId);

		EmployeeProfile empProfile=employeeProfileRepository.findOne(DocumentsSpecification.getloginUserId(principalDrId));
		int loginUserId	=empProfile.getEmpProfileLoginid();	

		DoctorSign doctorSign=doctorsignRepository.findOne(DocumentsSpecification.getphysicianSignImg(loginUserId));

		if(!(doctorSign==null)){
			physicianSignImg=doctorSign.getFilename();
		}
		else{
			physicianSignImg="-1";
		}

		PatientSignature patSignature=patientSignatureRepository.findOne(DocumentsSpecification.getpatientSignId(physicianSignImg));
		int patientSignId;
		if(!(patSignature==null)){
			patientSignId=patSignature.getId();
		}
		else {
			patientSignId=-1;
		}

		getPracticeDetails();
		getPracticePhoneNo();
		getPracticeEmail();
		getPatientDxDetails(patientId);
		getPatientAllerDetails(patientId);
		getPatientMedDetails(patientId);
		getDoctorDetails(patientId);
		//getOtherDoctorDetails(patientId);

		HashMap<Integer, PDFFormFieldBean> bean=getPatientDetails(patientId);

		PDFFormFieldBean pdfFieldBean =bean.get(Integer.parseInt(patientId));
		sharedFolder="/home/software/Documents";
		fileSeparator="/";
		htmlFile=sharedFolder + fileSeparator + "pdfforms" + fileSeparator + templateBean.getTemplateFileName();
		File templatefile    =  new File(htmlFile);
		Document templateDoc =  convertFileToDocument(templatefile);
		NodeList datalist		= templateDoc.getElementsByTagName("input");
		for (int i=0;i < datalist.getLength();i++){
			Node currentnode = datalist.item(i);
			Node dbfield = currentnode.getAttributes().getNamedItem("dbfield");
			if (dbfield != null){
				for (Field targetField : pdfFieldBean.getClass().getDeclaredFields()){
					if(targetField.getName().equals(dbfield.getNodeValue()))
						try {
							currentnode.getAttributes().getNamedItem("value").setNodeValue(targetField.get(pdfFieldBean).toString());
						} catch (DOMException | IllegalArgumentException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
				}
			}
		}
		String context = request.getScheme() + "://" + request.getServerName()+ ":" + request.getServerPort() + request.getContextPath();
		NodeList datalist1		= templateDoc.getElementsByTagName("img");
		int k=0;
		for (int i=0;i < datalist1.getLength();i++){

			Node currentnode = datalist1.item(i);
			Node dbfield = currentnode.getAttributes().getNamedItem("id");

			if (dbfield.getNodeValue().equals("imag"+k)){
				currentnode.getAttributes().getNamedItem("src").setNodeValue((context+ "/shared/pdfforms" + fileSeparator)+currentnode.getAttributes().getNamedItem("src").getNodeValue());
				k++;
			}

		}

		String html=convertDOMToString(templateDoc);
		result=new JSONObject();
		try {
			result.put("result",html);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return html;
	}


	private int getPatientRegistrationPrincipalDoctor(String patientId) {
		CriteriaBuilder builder=em.getCriteriaBuilder();
		CriteriaQuery<Object> cq=builder.createQuery(Object.class);
		Root<PatientRegistration> root=cq.from(PatientRegistration.class);
		cq.select(root.get(PatientRegistration_.patientRegistrationPrincipalDoctor)).
		where(builder.equal(root.get(PatientRegistration_.patientRegistrationId),Integer.parseInt(patientId)));
		Object principalDoctor=em.createQuery(cq).getSingleResult();
		int prncDoctor=Integer.parseInt(principalDoctor.toString());
		return prncDoctor;
	}



	private Document convertFileToDocument(File Filename) {
		InputSource is2 = null;
		Document template = null;

		try{
			String xmldata = "";
			//GlaceOutLoggingStream.console(Filename.getAbsolutePath());
			FileReader fr = new FileReader(Filename);
			BufferedReader br = new BufferedReader(fr);
			String content = "";

			while (content != null){
				xmldata = xmldata + content;
				content = br.readLine();
			}

			is2	=	new InputSource();
			is2.setByteStream(new ByteArrayInputStream(xmldata.getBytes()));
			is2.setEncoding("UTF-8");

			DocumentBuilderFactory docBuilderFactory	=	DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder				    =	docBuilderFactory.newDocumentBuilder();

			template =	docBuilder.parse(is2);			

		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return template;

	}

	private HashMap<Integer, PDFFormFieldBean> getPatientDetails(String patientId) {

		HashMap<Integer, PDFFormFieldBean> hmFormFields = new HashMap<Integer, PDFFormFieldBean>();
		getRdocname(patientId);
		getheight(patientId);
		getWeight(patientId);
		getInounce(patientId);
		getTemp(patientId);
		getPulse(patientId);
		getRespiratory(patientId);
		getCdurgname(patientId);
		getCstrength(patientId);
		getCfrom(patientId);

		getsittingrasystolicbp(patientId);
		getsittingradiastolicbp(patientId);

		getStandingrasystolicbp(patientId);
		getStandingradiastolicbp(patientId);

		getSittinglasystolicbp(patientId);
		getSittingladiastolicbp(patientId);

		getStandinglasystolicbp(patientId);
		getStandingladiastolicbp(patientId);

		getLyingrasystolicbp(patientId);
		getLyingradiastolicbp(patientId);

		getLyinglasystolicbp(patientId);
		getLyingladiastolicbp(patientId);

		getSystolicbp(patientId);
		getdiastolicbp(patientId);

		if(!rdocname.equals("")){
			rdocname=rdocname;
		}
		else if(rdocname.equals(""))
		{
			rdocname="";
		}


		if(!sittingrasystolicbp.equals("")&& !sittingradiastolicbp.equals("")){
			sysbp=sittingrasystolicbp;
			diabp=sittingradiastolicbp;
		}
		else if(!standingrasystolicbp.equals("")&& !standingradiastolicbp.equals("")){
			sysbp=standingrasystolicbp;
			diabp=standingradiastolicbp;
		}
		else if(!sittinglasystolicbp.equals("")&& !sittingladiastolicbp.equals("")){
			sysbp=sittinglasystolicbp;
			diabp=sittingladiastolicbp;
		}
		else if(!standinglasystolicbp.equals("")&& !standingladiastolicbp.equals("")){
			sysbp=standinglasystolicbp;
			diabp=standingladiastolicbp;
		}
		else if(!lyingrasystolicbp.equals("")&& !lyingradiastolicbp.equals("")){
			sysbp=lyingrasystolicbp;
			diabp=lyingradiastolicbp;
		}
		else if(!lyinglasystolicbp.equals("")&& !lyingladiastolicbp.equals("")){
			sysbp=lyinglasystolicbp;
			diabp=lyingladiastolicbp;
		}
		else if(!systolicbp.equals("")&& !diastolicbp.equals("")){
			sysbp=systolicbp;
			diabp=diastolicbp;
		}

		if(!pulse.equals("")){
			pulse=pulse;

		}
		else if(pulse.equals(""))
		{
			pulse="";
		}

		if(!respiratory.equals("")){
			respiratory=respiratory;

		}
		else if(respiratory.equals(""))
		{
			respiratory="";
		}

		if(!cdurgname.equals("")){
			cdurgname=cdurgname;
		}
		else if(cdurgname.equals(""))
		{
			cdurgname="";
		}
		if(!cstrength.equals("")){
			cstrength=cstrength;
		}
		else if(cstrength.equals(""))
		{
			cstrength="";
		}
		if(!cform.equals("")){
			cform=cform;
		}
		else if(cform.equals(""))
		{
			cform="";
		}
		if(!height.equals("")){
			height=height+"fts";
		}
		else if(height.equals(""))
		{
			height="";
		}

		if(!Weight.equals("")){
			Weight=Weight+"lbs" + "  "+inounce +"oz";
		}
		else if(Weight.equals(""))
		{
			Weight="";
		}

		if(!temp.equals("")){
			temp =temp+"\u00B0"+"\u0192";

		}
		else if(temp.equals("")){
			temp="";
		}
		CriteriaBuilder builder=em.getCriteriaBuilder();
		CriteriaQuery<PatientConsentFormDetailsBean> cq=builder.createQuery(PatientConsentFormDetailsBean.class);
		Root<PatientRegistration> root=cq.from(PatientRegistration.class);
		Join<PatientRegistration, EmployeeProfile> empProfile=root.join(PatientRegistration_.empProfile,JoinType.LEFT);
		Join<PatientRegistration, Guarantor> guarantor=root.join(PatientRegistration_.guaranatorDetails,JoinType.LEFT);
		/*Join<PatientRegistration, SchAppt> sch=root.join(PatientRegistration_.schappt,JoinType.LEFT);
		sch.on(builder.greaterThan(sch.get(SchAppt_.schApptStarttime), builder.currentTimestamp()));
		sch.on(builder.equal(sch.get(SchAppt_.schApptPatientId), Integer.parseInt(patientId)));*/
		Join<PatientRegistration,PatientSignature> patientSignature=root.join(PatientRegistration_.patientSignature,JoinType.LEFT);
		Join<PatientRegistration, Chart> chart=root.join(PatientRegistration_.chartTable,JoinType.LEFT);
		/*Join<Chart, Encounter> encounter=chart.join(Chart_.encounter,JoinType.LEFT);
		chart.on(builder.equal(chart.get(Chart_.chartPatientid), patientId));
		encounter.on(builder.equal(encounter.get(Encounter_.encounterStatus), 1));*/
		Join<PatientRegistration, PharmDetails> pharmacy=root.join(PatientRegistration_.pharmDetails,JoinType.LEFT);
		cq.select(builder.construct(PatientConsentFormDetailsBean.class,
				root.get(PatientRegistration_.patientRegistrationId),
				builder.concat(
						builder.concat(
								builder.concat(builder.coalesce(empProfile.get(EmployeeProfile_.empProfileLname), ""), ", "), 
								builder.concat(builder.coalesce(empProfile.get(EmployeeProfile_.empProfileFname), ""), " ")), 
								builder.coalesce(empProfile.get(EmployeeProfile_.empProfileCredentials), "")),
								empProfile.get(EmployeeProfile_.empProfilePhoneno),
								root.get(PatientRegistration_.patientRegistrationLastName),
								root.get(PatientRegistration_.patientRegistrationAccountno),
								root.get(PatientRegistration_.patientRegistrationFirstName),
								root.get(PatientRegistration_.patientRegistrationMidInitial),
								root.get(PatientRegistration_.patientRegistrationCspno),
								builder.concat(
										builder.concat(root.get(PatientRegistration_.patientRegistrationLastName), ", "), root.get(PatientRegistration_.patientRegistrationFirstName)),
										root.get(PatientRegistration_.patientRegistrationDob),
										builder.coalesce(root.get(PatientRegistration_.patientRegistrationAddress1), ""),
										builder.coalesce(root.get(PatientRegistration_.patientRegistrationCity), ""),
										builder.coalesce(root.get(PatientRegistration_.patientRegistrationZip), ""),
										builder.function("age", String.class, builder.currentTimestamp(),builder.function("to_timestamp", Timestamp.class,builder.function("to_char", String.class, root.get(PatientRegistration_.patientRegistrationDob),builder.literal("YYYY-MM-DD HH24:MI:SS")),builder.literal("YYYY-MM-DD HH24:MI:SS"))),
										builder.function("to_char", String.class, builder.function("now", Timestamp.class),builder.literal("DY MM/DD/YYYY")),
										builder.concat(
												builder.concat(
														builder.concat(guarantor.get(Guarantor_.guarantorLastName), ", "), 
														builder.concat(guarantor.get(Guarantor_.guarantorFirstName), " ")), 
														builder.coalesce(guarantor.get(Guarantor_.guarantorMiddleInitial), "")),
														builder.function("replace", String.class, root.get(PatientRegistration_.patientRegistrationPhoneNo),builder.literal("-"),builder.literal("")),
														builder.function("replace", String.class, root.get(PatientRegistration_.patientRegistrationCellno),builder.literal("-"),builder.literal("")),
														root.get(PatientRegistration_.patientRegistrationSsn),
														patientSignature.get(PatientSignature_.signaturefilename),

														/*builder.selectCase().when(sch.get(SchAppt_.schApptStarttime).isNotNull(),
						builder.function("to_char", String.class,sch.get(SchAppt_.schApptStarttime),builder.literal("DY MM/DD/YYYY"))).otherwise(
								builder.function("to_char", String.class,builder.currentTimestamp(), builder.literal("DY MM/DD/YYYY"))),
														 */
														root.get(PatientRegistration_.patientRegistrationMailId),
														root.get(PatientRegistration_.patientRegistrationEcontactperson),
														root.get(PatientRegistration_.patientRegistrationEphoneno),
														pharmacy.get(PharmDetails_.pharmDetailsPrimaryPhone),
														pharmacy.get(PharmDetails_.pharmDetailsStoreName)));
		Predicate predicate=builder.equal(root.get(PatientRegistration_.patientRegistrationId), patientId);
		cq.where(predicate).getRestriction();
		cq.distinct(true);
		
		List<PatientConsentFormDetailsBean> bean=em.createQuery(cq).getResultList();
		List<PatientInsConsentFormDetailsBean> insBean=getPatientInsDetails(patientId);

		for(int i=0;i<bean.size();i++){

			PatientConsentFormDetailsBean row = bean.get(i);
			PDFFormFieldBean formFieldBean = new PDFFormFieldBean();
			formFieldBean.setLastname(row.getPatientRegistrationLastName());
			formFieldBean.setFirstname(row.getPatientRegistrationFirstName());
			formFieldBean.setMi(row.getPatientRegistrationMidInitial());
			formFieldBean.setPatientname(row.getPatientFullName());
			formFieldBean.setDob(row.getPatientRegistrationDob().toString());
			formFieldBean.setAddress1(row.getPatientRegistrationAddress1());
			formFieldBean.setCity(row.getPatientRegistrationCity());
			formFieldBean.setZip(row.getPatientRegistrationZip());
			formFieldBean.setCspno(row.getPatientRegistrationCspno());
			formFieldBean.setGuarantor(row.getGuarantorName());
			formFieldBean.setPracticename(practiceName);
			formFieldBean.setPracticeaddress1(practiceAddressLine1);
			formFieldBean.setPracticeaddress2(practiceAddressLine2);
			formFieldBean.setPatientsign(patientSignImg);
			formFieldBean.setWitnesssign(witnessSignImg);
			formFieldBean.setPhysiciansign(physicianSignImg);
			formFieldBean.setCurrentdate(builder.currentDate().toString());
			//formFieldBean.setAppointmentTime();
			formFieldBean.setApptdate(row.getSchApptStarttime()==null?"":row.getCurrentDate().toString());
			formFieldBean.setDay_currentdate(row.getCurrentDate()==null?"":row.getCurrentDate());
			formFieldBean.setHomephone(row.getPatientRegistrationPhoneNo());
			formFieldBean.setCellno(row.getPatientRegistrationCellno());
			formFieldBean.setPatientssn(row.getPatientRegistrationSsn());
			formFieldBean.setSign(row.getSignaturefilename());
			//formFieldBean.setCurrent_date12hrformat(row.getString("current_date12hrformat"));
			//formFieldBean.setEncounter_indate(row.getString("encounter_indate"));
			formFieldBean.setPracticephoneno(practicephoneno);
			formFieldBean.setDocname(row.getEmployerName());
			formFieldBean.setDocphoneno(row.getEmpProfilePhoneno());
			formFieldBean.setPracticemail(practicemailid);
			formFieldBean.setDxcode(dxcode);
			formFieldBean.setAllergyname(allergyname);
			formFieldBean.setCurrdrugname(currdrugname);
			formFieldBean.setSerDocotorName(serDocotorName);
			formFieldBean.setSerDocAddress(serDocAddress);
			formFieldBean.setSerDocCity(serDocCity);
			formFieldBean.setSerDocState(serDocState);
			formFieldBean.setSerDocZip(serDocZip);
			formFieldBean.setSerDocPhNumber(serDocPhNumber);
			formFieldBean.setSerDocLicenseNo(serDocLicenseNo);
			formFieldBean.setMailid(row.getPatientRegistrationMailId());
			formFieldBean.setEmergencyperson(row.getPatientRegistrationEcontactperson());
			formFieldBean.setEmergencynumber(row.getPatientRegistrationEphoneno());
			formFieldBean.setPharmacynumber(row.getPharmDetailsPrimaryPhone());
			formFieldBean.setPharmacyname(row.getPharmDetailsStoreName());
			formFieldBean.setAccountno(row.getPatientRegistrationAccountno());
			formFieldBean.setHeight(height);
			formFieldBean.setWeight(Weight);
			formFieldBean.setTemp(temp);
			formFieldBean.setSysbp(sysbp);
			formFieldBean.setDiabp(diabp);
			formFieldBean.setCdurgname(cdurgname);
			formFieldBean.setCform(cform);
			formFieldBean.setCstrength(cstrength);
			formFieldBean.setPulse(pulse);
			formFieldBean.setRespiratory(respiratory);    		
			formFieldBean.setRdocname(rdocname);
			for(int index=0;index<insBean.size();index++)
			{
				PatientInsConsentFormDetailsBean insDetailRow=insBean.get(index);
				if(insDetailRow.getPatientInsDetailInstype()==1)
				{
					formFieldBean.setPrimaryInsDetail(insDetailRow.getInsCompanyName(),insDetailRow.getPatientInsDetailPatientinsuranceid(),insDetailRow.getPatientInsDetailSubscribername(),insDetailRow.getPatientInsDetailSubscriberDob(),insDetailRow.getPatientInsDetailGroupno(),Integer.parseInt(HUtil.Nz(insDetailRow.getPatientInsDetailRelationtosubs(), "-1")));
				}
				else {
					formFieldBean.setSecondaryInsDetail(insDetailRow.getInsCompanyName(),insDetailRow.getPatientInsDetailPatientinsuranceid(),insDetailRow.getPatientInsDetailSubscribername(),insDetailRow.getPatientInsDetailSubscriberDob(),insDetailRow.getPatientInsDetailGroupno(),insDetailRow.getPatientInsDetailRelationtosubs());
				}
			}

			hmFormFields.put(row.getPatientRegistrationId(), formFieldBean);
		}
		return hmFormFields;
	}



	private List<PatientInsConsentFormDetailsBean> getPatientInsDetails(String patientId) {
		CriteriaBuilder builder=em.getCriteriaBuilder();
		CriteriaQuery<Object> cq=builder.createQuery(Object.class);
		Root<PatientRegistration> root=cq.from(PatientRegistration.class);
		Join<PatientRegistration, PatientInsDetail> patientInsDetails=root.join(PatientRegistration_.patientInsuranceTable);
		Join<PatientInsDetail, InsCompAddr> insCompAddr=patientInsDetails.join(PatientInsDetail_.insCompAddr);
		Join<InsCompAddr, InsCompany> insCompany=insCompAddr.join(InsCompAddr_.insCompany);
		cq.select(builder.construct(PatientInsConsentFormDetailsBean.class,
				root.get(PatientRegistration_.patientRegistrationId),
				insCompany.get(InsCompany_.insCompanyName),
				patientInsDetails.get(PatientInsDetail_.patientInsDetailPatientinsuranceid),
				patientInsDetails.get(PatientInsDetail_.patientInsDetailSubscribername),
				patientInsDetails.get(PatientInsDetail_.patientInsDetailRelationtosubs),
				patientInsDetails.get(PatientInsDetail_.patientInsDetailGroupno),
				patientInsDetails.get(PatientInsDetail_.patientInsDetailInstype),
				builder.function("to_mmddyyyy", String.class, patientInsDetails.get(PatientInsDetail_.patientInsDetailSubscriberDob))));
		Predicate patId=builder.equal(root.get(PatientRegistration_.patientRegistrationId), patientId);
		Predicate insName=builder.notLike(builder.lower(builder.trim(insCompany.get(InsCompany_.insCompanyName))), "%self%pay%|%unreg%self%" );
		Predicate isactive=builder.equal(patientInsDetails.get(PatientInsDetail_.patientInsDetailIsactive), true);
		cq.where(patId,insName,isactive).getRestriction();
		List<Object> obj1=em.createQuery(cq).getResultList();
		List<PatientInsConsentFormDetailsBean> bean=new ArrayList<PatientInsConsentFormDetailsBean>();
		for(int i=0;i<obj1.size();i++){
			PatientInsConsentFormDetailsBean obj2=(PatientInsConsentFormDetailsBean) obj1.get(i);
			bean.add(obj2);
		}
		return bean;
	}



	private void getdiastolicbp(String patientId) {
		String gwtId="0000200200100076000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			diastolicbp=object.get(0).toString();
	}



	private void getSystolicbp(String patientId) {
		String gwtId="0000200200100075000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			systolicbp=object.get(0).toString();
	}



	private void getLyingladiastolicbp(String patientId) {
		String gwtId="0000200200100037000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			lyingladiastolicbp=object.get(0).toString();
	}


	private void getLyinglasystolicbp(String patientId) {
		String gwtId="0000200200100036000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			lyinglasystolicbp=object.get(0).toString();
	}



	private void getLyingradiastolicbp(String patientId) {
		String gwtId="0000200200100035000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			lyingradiastolicbp=object.get(0).toString();
	}



	private void getLyingrasystolicbp(String patientId) {
		String gwtId="0000200200100034000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			lyingrasystolicbp=object.get(0).toString();
	}



	private void getStandingladiastolicbp(String patientId) {
		String gwtId="0000200200100033000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			standingladiastolicbp=object.get(0).toString();

	}



	private void getStandinglasystolicbp(String patientId) {
		String gwtId="0000200200100032000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			standinglasystolicbp=object.get(0).toString();

	}



	private void getSittingladiastolicbp(String patientId) {
		String gwtId="0000200200100031000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			sittingladiastolicbp=object.get(0).toString();

	}



	private void getSittinglasystolicbp(String patientId) {
		String gwtId="0000200200100030000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			sittinglasystolicbp=object.get(0).toString();
	}



	private void getStandingradiastolicbp(String patientId) {
		String gwtId="0000200200100029000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			standingradiastolicbp=object.get(0).toString();

	}



	private void getStandingrasystolicbp(String patientId) {
		String gwtId="0000200200100028000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			standingrasystolicbp=object.get(0).toString();
	}



	private void getsittingradiastolicbp(String patientId) {
		String gwtId="0000200200100027000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			sittingradiastolicbp=object.get(0).toString();

	}



	private void getsittingrasystolicbp(String patientId) {
		String gwtId="0000200200100026000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			sittingrasystolicbp=object.get(0).toString();
	}



	private List<Object> getClinicalElementsValue(String patientId, String gwtId) {

		CriteriaBuilder builder=em.getCriteriaBuilder();
		CriteriaQuery<Object> cq=builder.createQuery(Object.class);
		Root<PatientClinicalElements> root=cq.from(PatientClinicalElements.class);
		cq.multiselect(root.get(PatientClinicalElements_.patientClinicalElementsValue));
		Object obj = patEncounterId(patientId,gwtId);
		Predicate encounterId = builder.equal(root.get(PatientClinicalElements_.patientClinicalElementsEncounterid),Integer.parseInt(obj.toString()));
		Predicate patId=builder.equal(root.get(PatientClinicalElements_.patientClinicalElementsPatientid), patientId);
		Predicate gwt=builder.equal(root.get(PatientClinicalElements_.patientClinicalElementsGwid),gwtId);
		cq.where(builder.and(encounterId,patId,gwt));
		List<Object> object=em.createQuery(cq).getResultList();
		return object;
	}



	private void getCfrom(String patientId) {
		List<Object> getCfrom=new ArrayList<Object>();
		CriteriaBuilder builder=em.getCriteriaBuilder();
		CriteriaQuery<Object> cq=builder.createQuery(Object.class);
		Root<CurrentMedication> root=cq.from(CurrentMedication.class);
		cq.multiselect(root.get(CurrentMedication_.currentMedicationForm));
		Object obj= getDrug(patientId);
		Predicate predicate=builder.equal(root.get(CurrentMedication_.currentMedicationEncounterId),Integer.parseInt(obj.toString()));
		cq.where(builder.and(predicate));
		getCfrom=em.createQuery(cq).getResultList();
		if(getCfrom.size()>0)
			cform=getCfrom.get(0).toString();
	}



	private void getCstrength(String patientId) {
		List<Object> getCstrength=new ArrayList<Object>();
		CriteriaBuilder builder=em.getCriteriaBuilder();
		CriteriaQuery<Object> cq=builder.createQuery(Object.class);
		Root<CurrentMedication> root=cq.from(CurrentMedication.class);
		cq.multiselect(root.get(CurrentMedication_.currentMedicationDosageWithUnit));
		Object obj= getDrug(patientId);
		Predicate predicate=builder.equal(root.get(CurrentMedication_.currentMedicationEncounterId),Integer.parseInt(obj.toString()));
		cq.where(builder.and(predicate));
		getCstrength=em.createQuery(cq).getResultList();
		if(getCstrength.size()>0)
			cstrength=getCstrength.get(0).toString();
	}



	private void getCdurgname(String patientId) {
		List<Object> getCdurgname=new ArrayList<Object>();
		CriteriaBuilder builder=em.getCriteriaBuilder();
		CriteriaQuery<Object> cq=builder.createQuery(Object.class);
		Root<CurrentMedication> root=cq.from(CurrentMedication.class);
		cq.multiselect(root.get(CurrentMedication_.currentMedicationRxName));
		Object obj= getDrug(patientId);
		Predicate predicate=builder.equal(root.get(CurrentMedication_.currentMedicationEncounterId),Integer.parseInt(obj.toString()));
		cq.where(builder.and(predicate));
		getCdurgname=em.createQuery(cq).getResultList();
		if(getCdurgname.size()>0)
			cdurgname=getCdurgname.get(0).toString();
	}



	private Object getDrug(String patientId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<Encounter> root=cq.from(Encounter.class);
		Join<Encounter, Chart> chart=root.join(Encounter_.chart,JoinType.INNER);
		cq.multiselect(builder.max(root.get(Encounter_.encounterId))).
		where(builder.equal(chart.get(Chart_.chartPatientid), patientId));
		Object obj = em.createQuery(cq).getSingleResult();
		return obj;
	}



	private void getRespiratory(String patientId) {
		String gwtId="0000200200100038000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			pulse=object.get(0).toString();
	}



	private void getPulse(String patientId) {
		String gwtId="0000200200100020000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			pulse=object.get(0).toString();
	}



	private void getTemp(String patientId) {
		String gwtId="0000200200100022000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			temp=object.get(0).toString();
	}



	private void getWeight(String patientId) {
		String gwtId="0000200200100024000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			Weight=object.get(0).toString();
		Double newWeight=((Double.valueOf(Weight)/1000.00)*2.21);
	}



	private void getInounce(String patientId) {
		String gwtId="0000200200100024000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			inounce=object.get(0).toString();
		Double newinounce=(((((Double.valueOf(inounce)*453.59237)*3.5274)/100)%16)*100)/100;
	}



	private void getheight(String patientId) {
		String gwtId="0000200200100023000";
		List<Object> object=getClinicalElementsValue(patientId,gwtId);
		if(!(object==null))
			height=object.get(0).toString();
		Double newHeight=Double.valueOf(Double.valueOf(height)/2.54/12);
	}

	private Object patEncounterId(String patientId, String gwtId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<PatientClinicalElements> root=cq.from(PatientClinicalElements.class);
		cq.multiselect(builder.max(root.get(PatientClinicalElements_.patientClinicalElementsEncounterid)));
		Predicate patId=builder.equal(root.get(PatientClinicalElements_.patientClinicalElementsPatientid), patientId);
		Predicate gwtid=builder.equal(root.get(PatientClinicalElements_.patientClinicalElementsGwid),gwtId);
		cq.where(builder.and(patId,gwtid));
		Object obj = em.createQuery(cq).getSingleResult();
		return obj;
	}



	private void getRdocname(String patientId) {
		List<Object> getRdocname=new ArrayList<Object>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<PatientRegistration> root=cq.from(PatientRegistration.class);
		Join<PatientRegistration, H076> h076=root.join(PatientRegistration_.referringPhyTable);
		cq.select(builder.concat(
				builder.concat(builder.coalesce(h076.get(H076_.h076003),""),", " ), 
				builder.coalesce(h076.get(H076_.h076005), ""))).
				where(builder.equal(root.get(PatientRegistration_.patientRegistrationId), patientId));
		getRdocname=em.createQuery(cq).getResultList();
		rdocname=getRdocname.toString();
	}

	/*private void getOtherDoctorDetails(String patientId) {
		List<Object[]> doctorDetails=new ArrayList<Object[]>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<PatientRegistration> root=cq.from(PatientRegistration.class);
		Join<PatientRegistration, H076> h076=root.join(PatientRegistration_.h076,JoinType.INNER);
		Join<H076, H077> h077=h076.join(H076_.h077,JoinType.INNER);
		cq.multiselect(
				builder.concat(builder.coalesce(h076.get(H076_.h076005), ""), 
						builder.coalesce(h076.get(H076_.h076003), "")),
						builder.coalesce(h077.get(H077_.h077002), ""),
						builder.coalesce(h076.get(H076_.h076007), ""),
						builder.coalesce(h076.get(H076_.h076010), "")).
						where(builder.equal(root.get(PatientRegistration_.patientRegistrationId),patientId));
		em.createQuery(cq).getResultList();

	}
	 */
	private void getDoctorDetails(String patientId) {
		List<Object[]> doctorDetails=new ArrayList<Object[]>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<ServiceDetail> root=cq.from(ServiceDetail.class);
		Join<ServiceDetail, EmployeeProfile> empProfile=root.join(ServiceDetail_.	sdoctors,JoinType.INNER);
		Join<EmployeeProfile, H478> h478=empProfile.join(EmployeeProfile_.h478,JoinType.INNER);
		cq.multiselect(
				empProfile.get(EmployeeProfile_.empProfileFullname),
				empProfile.get(EmployeeProfile_.empProfileAddress),
				empProfile.get(EmployeeProfile_.empProfileCity),
				empProfile.get(EmployeeProfile_.empProfileState),
				empProfile.get(EmployeeProfile_.empProfilePhoneno),
				h478.get(H478_.h478005)).
				where(builder.equal(root.get(ServiceDetail_.serviceDetailPatientid), patientId));
		doctorDetails=em.createQuery(cq).getResultList();
		if(doctorDetails.size()>0){
			for(Object[] values : doctorDetails) {
				try {
					this.serDocotorName=values[0].toString();
					this.serDocAddress=values[1].toString();
					this.serDocCity=values[2].toString();
					this.serDocState=values[3].toString();
					this.serDocPhNumber=values[4].toString();
					this.serDocLicenseNo=values[5].toString();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	private void getPatientMedDetails(String patientId) {
		List<Object> patientMedDetails=new ArrayList<Object>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<CurrentMedication> root=cq.from(CurrentMedication.class);
		Join<CurrentMedication, Encounter> encounter=root.join(CurrentMedication_.encounter,JoinType.INNER);
		Join<Encounter, Chart> chart=encounter.join(Encounter_.chart,JoinType.INNER);
		Join<Chart, PatientRegistration> patReg=chart.join(Chart_.patientRegistrationTable,JoinType.INNER);
		cq.select(
				root.get(CurrentMedication_.currentMedicationRxName));
		Predicate patId=builder.equal(patReg.get(PatientRegistration_.patientRegistrationId), patientId);
		Predicate isActive=builder.equal(root.get(CurrentMedication_.currentMedicationIsActive), true);		
		cq.where(builder.and(patId,isActive));
		patientMedDetails=em.createQuery(cq).getResultList();
		if(patientMedDetails.size()>0){
			for(int i=0;i<patientMedDetails.size();i++){
				this.currdrugname +="\n"+patientMedDetails.get(i).toString().replace("null", "");
			}
			this.currdrugname=currdrugname.substring(0	,currdrugname.length()).replace("null,", "").replace("null", "").toUpperCase().toString();
		}
		else{
			this.currdrugname=" ";
		}
	}

	private void getPatientAllerDetails(String patientId) {
		List<Object> patientAllerDetails=new ArrayList<Object>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<PatientAllergies> root=cq.from(PatientAllergies.class);
		Join<PatientAllergies, Encounter> encounter=root.join(PatientAllergies_.encounter,JoinType.INNER);
		Join<PatientAllergies, Chart> chart=root.join(PatientAllergies_.chart,JoinType.INNER);
		Join<Chart, PatientRegistration> patReg=chart.join(Chart_.patientRegistrationTable,JoinType.INNER);
		cq.select(
				root.get(PatientAllergies_.patAllergAllergicTo)).
				where(builder.equal(patReg.get(PatientRegistration_.patientRegistrationId), patientId));
		patientAllerDetails=em.createQuery(cq).getResultList();
		if(patientAllerDetails.size()>0){
			for(int i=0;i<patientAllerDetails.size();i++){
				this.allergyname+="\n"+patientAllerDetails.get(i).toString().replace("null", "");
			}

			this.allergyname=allergyname.substring(0	,allergyname.length()).replace("null,", "").replace("null", "").toUpperCase().toString();
		}
		else{
			allergyname=" ";
		}
	}

	private void getPatientDxDetails(String patientId) {
		List<Object> patientDxDetails=new ArrayList<Object>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<H611> root=cq.from(H611.class);
		cq.multiselect(
				builder.concat(builder.concat(root.get(H611_.h611005), "-"), root.get(H611_.h611006))).
				where(builder.equal(root.get(H611_.h611003),patientId));
		patientDxDetails=em.createQuery(cq).getResultList();
		if(patientDxDetails.size()>0){
			for(int i=0;i<patientDxDetails.size();i++){
				this.dxcode+=","+patientDxDetails.get(i).toString().replace("null", "");
			}
			this.dxcode=dxcode.substring(0	,dxcode.length()).replace("null,", "").replace("null", "").toUpperCase().toString();
		}
		else{
			dxcode=" ";
		}

	}

	private void getPracticeEmail() {
		List<Object> practiceEmail=new ArrayList<Object>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<InitialSettings> root = cq.from(InitialSettings.class);
		cq.select(
				root.get(InitialSettings_.initialSettingsOptionValue)).
				where(builder.equal(root.get(InitialSettings_.initialSettingsOptionName), "email Address"));
		practiceEmail=em.createQuery(cq).getResultList();
		if(practiceEmail.size()>0){
			this.practicemailid=practiceEmail.toString();
		}

	}

	private void getPracticePhoneNo() {
		List<Object> practicePhoneNo = null;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<InitialSettings> root = cq.from(InitialSettings.class);
		cq.select(
				root.get(InitialSettings_.initialSettingsOptionValue)).
				where(builder.equal(root.get(InitialSettings_.initialSettingsOptionName), "Phone No"));
		practicePhoneNo=em.createQuery(cq).getResultList();
		if(!practicePhoneNo.isEmpty()){
			this.practicephoneno=practicePhoneNo.toString();
		}
	}


	private void getPracticeDetails() {
		List<Object[]> practiceDetails=new ArrayList<Object[]>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<InitialSettings> root = cq.from(InitialSettings.class);
		cq.multiselect(
				root.get(InitialSettings_.initialSettingsOptionValue),
				root.get(InitialSettings_.initialSettingsOptionSource),
				root.get(InitialSettings_.initialSettingsOptionNameDesc)).
				where(builder.equal(root.get(InitialSettings_.initialSettingsOptionType), 105));
		practiceDetails=em.createQuery(cq).getResultList();
		if(practiceDetails.size()>0){
			for(Object[] values : practiceDetails) {
				try {
					this.practiceName=values[0].toString();
					this.practiceAddressLine1=values[1].toString();
					this.practiceAddressLine2=values[2].toString();
				}
				catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}



	@Override
	public String saveSignature(String htmlString,String patientId, String imageId, String chartId,String imgBase64Data, String imageURL, String templateId) {

		saveSignByTemplateId(templateId,imageId, imgBase64Data, patientId);
		String filepath=saveDecodeImage(imgBase64Data,patientId);
		filepath=imageURL+filepath;

		Document templateDoc =  convertStringToDocument(htmlString);
		NodeList datalist		= templateDoc.getElementsByTagName("img");
		for (int i=0;i < datalist.getLength();i++){
			Node currentnode = datalist.item(i);
			Node dbfield = currentnode.getAttributes().getNamedItem("id");

			if (dbfield.getNodeValue().equals(imageId)){
				currentnode.getAttributes().getNamedItem("src").setNodeValue(filepath);
			}

	}
		String html=convertDOMToString(templateDoc);
		return html;
	}



	private String convertDOMToString(Document templateDoc) {
		String result = "" + templateDoc.getFirstChild();

		return result;
	}



	private Document convertStringToDocument(String htmlString) {
		Document template = null;
		InputSource is2	  =	new InputSource();
		is2.setByteStream(new ByteArrayInputStream(htmlString.getBytes()));
		is2.setEncoding("UTF-8");
		DocumentBuilderFactory docBuilderFactory =	DocumentBuilderFactory.newInstance();

		try{

			DocumentBuilder docBuilder	=	docBuilderFactory.newDocumentBuilder();
			template =	docBuilder.parse(is2);
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}

		return template;
	}



	private String saveDecodeImage(String imgBase64Data, String patientId) {
		try {
			String sharedFolderPath="/home/software/Documents";
			File dirStructure=new File(sharedFolderPath+"/PatientSignatures");
			java.util.Date date= new java.util.Date();
			SimpleDateFormat ft = 
					new SimpleDateFormat ("yyMMddhhmmss");
			if(!dirStructure.exists()){
				dirStructure.setReadable(true,false);
				dirStructure.setWritable(true,false);
				dirStructure.setExecutable(true,false);
				dirStructure.mkdir();
			}
			Base64 decoder = new Base64();
			imgBase64Data=imgBase64Data.replaceAll("@","/");
			String str="";
			for(int i=0;i<imgBase64Data.length();i++){
				if(imgBase64Data.charAt(i)==' ')
					str=str+"+";
				else
					str=str+imgBase64Data.charAt(i);
			}
			byte[] imgBytes = decoder.decode(str);
			FileOutputStream osf;
			osf = new FileOutputStream(new File(sharedFolderPath+"/PatientSignatures/"+patientId+"_"+ft.format(date)+"_consentform.png"));
			osf.write(imgBytes);
			osf.flush();
			osf.close();
			//String context = request.getScheme() + "://" + request.getServerName()+ ":" + request.getServerPort() + request.getContextPath();
			return "shared/PatientSignatures/"+patientId+"_"+ft.format(date)+"_consentform.png";
			//return context+"/shared/PatientSignatures/"+patientId+"_"+ft.format(date)+"_consentform.png";
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return "";
	}



	private String saveSignByTemplateId(String templateId, String imageId,
			String imgBase64Data, String patientId) {
		try{
			PDFFormTemplateBean  templateBean = getPDFFormTemplateBean(templateId);
			String sharedFolderPath="/home/software/Documents";

			File rootFolder = new File(sharedFolderPath+"/PatientConsentForms/");
			if(!rootFolder.exists()){
				rootFolder.mkdir();
				rootFolder.setReadable(true,false);
				rootFolder.setWritable(true,false);
				rootFolder.setExecutable(true,false);
			}

			File patientFolder = new File(sharedFolderPath+"/PatientConsentForms/"+patientId+"/");
			if(!patientFolder.exists()){
				patientFolder.mkdir();
				patientFolder.setReadable(true,false);
				patientFolder.setWritable(true,false);
				patientFolder.setExecutable(true,false);
			}

			File patientSignature = new File(sharedFolderPath+"/PatientConsentForms/"+patientId+"/Signatures/");
			if(!patientSignature.exists()){
				patientSignature.mkdir();
				patientSignature.setReadable(true,false);
				patientSignature.setWritable(true,false);
				patientSignature.setExecutable(true,false);
			}

			Writer writer = null;
			try {
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(sharedFolderPath+"/PatientConsentForms/"+patientId+"/Signatures/"+templateBean.getTemplateName()+"$$$"+templateId+"$$$"+imageId+".txt"), "utf-8"));
				writer.write(imgBase64Data);
			} catch (IOException ex) {
				// report
			} finally {
				try {writer.close();} catch (Exception ex) {/*ignore*/}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 

		try {

			PDFFormTemplateBean  templateBean = getPDFFormTemplateBean(templateId);

			String sharedFolderPath="/home/software/Documents";

			Base64 decoder = new Base64();
			imgBase64Data=imgBase64Data.replaceAll("@","/");
			String str="";

			for(int i=0;i<imgBase64Data.length();i++){
				if(imgBase64Data.charAt(i)==' ')
					str=str+"+";
				else
					str=str+imgBase64Data.charAt(i);
			}

			byte[] imgBytes = decoder.decode(str);
			File convertedSignature = new File(sharedFolderPath+"/PatientConsentForms/"+patientId+"/ConvertedSignature/");
			if(!convertedSignature.exists()){
				convertedSignature.mkdir();
				convertedSignature.setReadable(true,false);
				convertedSignature.setWritable(true,false);
				convertedSignature.setExecutable(true,false);
			}

			File lastSignature = new File(sharedFolderPath+"/PatientConsentForms/"+patientId+"/ConvertedSignature/"+templateBean.getTemplateName()+"_"+imageId+".png");
			if(lastSignature.exists())
				lastSignature.delete();

			FileOutputStream osf;
			osf = new FileOutputStream(new File(sharedFolderPath+"/PatientConsentForms/"+patientId+"/ConvertedSignature/"+templateBean.getTemplateName()+"_"+imageId+".png"));
			osf.write(imgBytes);
			osf.flush();
			osf.close();
		}
		catch(Exception ex){

		}

		return "";

	}



	private PDFFormTemplateBean getPDFFormTemplateBean(String templateId) {

		FormsTemplate formstemplate=formsTemplateRepository.findOne(DocumentsSpecification.templateDetails(templateId));
		PDFFormTemplateBean templateBean=new PDFFormTemplateBean();
		if(formstemplate != null){
			templateBean.setTemplateId(formstemplate.getFormsTemplateId());
			templateBean.setTemplateName(formstemplate.getFormsTemplateName());
			templateBean.setTemplateFileName(formstemplate.getFormsTemplateFilename());
			templateBean.setTemplateType(formstemplate.getFormsTemplateType());
			templateBean.setTemplateThumbnail(formstemplate.getFormsTemplateThumbnail());
			templateBean.setTemplateIsactive(formstemplate.getFormsTemplateIsactive());
		}
		return templateBean;
	}
}



