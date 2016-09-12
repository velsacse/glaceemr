package com.glenwood.glaceemr.server.application.services.Documents;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaQuery;

import com.glenwood.glaceemr.server.application.Bean.DocumentsCategoryBean;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.AlertEvent_;
import com.glenwood.glaceemr.server.application.models.FileDetails;
import com.glenwood.glaceemr.server.application.models.FileDetails_;
import com.glenwood.glaceemr.server.application.models.FileName;
import com.glenwood.glaceemr.server.application.models.PatientDocumentsCategory;
import com.glenwood.glaceemr.server.application.models.PatientDocumentsCategory_;
import com.glenwood.glaceemr.server.application.models.PatientDocumentsNotes;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.repositories.AlertInboxRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientDocumentsNotesRepository;
import com.glenwood.glaceemr.server.application.repositories.FileDetailsRepository;
import com.glenwood.glaceemr.server.application.repositories.FileNameRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientDocumentsCategoryRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.specifications.DocumentsSpecification;
import com.glenwood.glaceemr.server.application.specifications.PatientRegistrationSpecification;
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
	/**
	 * TO get category list based on patientId, If the patientId is not provided then it displays all category list
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
					builder.count(catid.get(PatientDocumentsCategory_.patientDocCategoryName))));
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
			patientDocumentsCategories=patientDocCategoryRepository.findAll();
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
	 * To get the list of files of a particular patient based on categoryId and fileNameId
	 * @param patientId
	 * @param categoryId
	 * @param fileNameId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<FileDetails> getFileList(int patientId, int categoryId, int fileNameId) {
		List<FileDetails> fileDetails = null;
		fileDetails = fileDetailsRepository.findAll(DocumentsSpecification.getFileList(patientId, categoryId, fileNameId));
		return fileDetails;

	}

	/**
	 * To get Document notes
	 * @param notesFilenameId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<PatientDocumentsNotes> getDocNotesDetails(int notesFilenameId) {
		List<PatientDocumentsNotes> patientDocNotes= null;
		patientDocNotes= docNotesRepository.findAll(DocumentsSpecification.getDocNotes(notesFilenameId));
		return patientDocNotes;
	}

	/**
	 * To delete a folder by passing its folderId
	 * @param fileDetailsId
	 * @throws Exception
	 */
	@Override
	public void deleteFile(int fileNameId) {
		List<FileName> fileNameList = fileNameRepository.findAll(DocumentsSpecification.deleteFiles(fileNameId));
		fileNameRepository.deleteInBatch(fileNameList);
	}

	/**
	 * To delete a file by passing its fileId
	 * @param fileNameId
	 */
	@Override
	public void deleteFolder(int fileDetailsId) {

		List<FileName> fileNameList= fileNameRepository.findAll(DocumentsSpecification.getFileNameDetails(fileDetailsId));
		fileNameRepository.deleteInBatch(fileNameList);

		List<FileDetails> fileDetails = fileDetailsRepository.findAll(DocumentsSpecification.deleteFolder( fileDetailsId));
		fileDetailsRepository.deleteInBatch(fileDetails);

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
			int status, int alertid,int docCategoryid,int refId, int patientId,
			int encounterId, String msg, int chartId, int roomId, int parentId) {

		String lastName="", firstName="", midInitial="";
		int  refid=refId;


		if(alertid==37){
			CriteriaBuilder qb = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq = qb.createQuery();

			Root<AlertEvent> root = cq.from(AlertEvent.class);
			cq.select(qb.max(root.get(AlertEvent_.alertEventRefId)));
			cq.where(qb.equal(root.get(AlertEvent_.alertEventCategoryId), 37));

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
			aug.setAlertEventCategoryId(alertid);
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

}




