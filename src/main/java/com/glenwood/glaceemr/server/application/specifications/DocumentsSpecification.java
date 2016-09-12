package com.glenwood.glaceemr.server.application.specifications;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.AlertEvent_;
import com.glenwood.glaceemr.server.application.models.FileDetails;
import com.glenwood.glaceemr.server.application.models.FileDetails_;
import com.glenwood.glaceemr.server.application.models.FileName;
import com.glenwood.glaceemr.server.application.models.FileName_;
import com.glenwood.glaceemr.server.application.models.PatientDocumentsCategory;
import com.glenwood.glaceemr.server.application.models.PatientDocumentsCategory_;
import com.glenwood.glaceemr.server.application.models.PatientDocumentsNotes;
import com.glenwood.glaceemr.server.application.models.PatientDocumentsNotes_;
/**
 * Specification for Patient Documents
 * @author Soundarya
 *
 */
public class DocumentsSpecification {
	/**
	 * Specification to get the file details based on patientId and categoryId
	 * @param patientId
	 * @param categoryId
	 */
	static ArrayList<Integer> patientIdList=new ArrayList<Integer>();

	public static Specification<FileDetails> getFileCategoryList(final int patientId,final int categoryId){
		return new Specification<FileDetails>() {

			@Override
			public Predicate toPredicate(Root<FileDetails> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				root.fetch(FileDetails_.fileName,JoinType.INNER);
				root.fetch(FileDetails_.patientDocCategory,JoinType.INNER);
				Predicate catpredicate=cb.equal(root.get(FileDetails_.filedetailsCategoryid),categoryId);
				Predicate patpredicate=cb.equal(root.get(FileDetails_.filedetailsPatientid),patientId);
				return query.where(cb.and(catpredicate,patpredicate)).getRestriction();
			}
		};

	}	

	/**
	 * Specification to get the Document notes of a patient
	 * @param notesFilenameId
	 * @return
	 */
	public static Specification<PatientDocumentsNotes> getDocNotes(final int notesFilenameId){
		return new Specification<PatientDocumentsNotes>() {

			@Override
			public Predicate toPredicate(Root<PatientDocumentsNotes> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate notesPredicate=cb.equal(root.get(PatientDocumentsNotes_.notesFilenameid), notesFilenameId);
				return notesPredicate;
			}
		};
	}

	/**
	 * Specification to delete a file
	 * @param fileNameId
	 * @return
	 */
	public static Specification<FileName> deleteFiles(final int fileNameId){
		return new Specification<FileName>() {

			@Override
			public Predicate toPredicate(Root<FileName> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate delFiles=root.get(FileName_.filenameId).in(fileNameId);
				return delFiles;
			}

		};
	}

	/**
	 * Specification to delete a folder
	 * @param fileDetailsId
	 * @return
	 */
	public static Specification<FileDetails> deleteFolder(final int fileDetailsId){
		return new Specification<FileDetails>() {

			@Override
			public Predicate toPredicate(Root<FileDetails> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate delFolders = root.get(FileDetails_.filedetailsId).in(fileDetailsId);
				return delFolders;
			}

		};
	}

	/**
	 * Specification to get the details of a file
	 * @param fileDetailsId
	 * @return
	 */
	public static Specification<FileName> getFileNameDetails(final int fileDetailsId){
		return new Specification<FileName>() {

			@Override
			public Predicate toPredicate(Root<FileName> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate getFileData = root.get(FileName_.filenameScanid).in(fileDetailsId);
				return getFileData;
			}
		};
	}

	/**
	 * Specification to get the details a particular file by passing patientId,categoryId,fileNameId
	 * @param patientId
	 * @param categoryId
	 * @param fileNameId
	 * @return
	 */
	public static Specification<FileDetails> getFileList(final int patientId,final int categoryId,final int fileNameId){
		return new Specification<FileDetails>() {

			@Override
			public Predicate toPredicate(Root<FileDetails> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				Join<FileDetails, FileName> fileid=root.join(FileDetails_.fileName,JoinType.INNER);
				root.fetch(FileDetails_.patientDocCategory,JoinType.INNER);
				Predicate catpredicate=cb.equal(root.get(FileDetails_.filedetailsCategoryid),categoryId);
				Predicate patpredicate=cb.equal(root.get(FileDetails_.filedetailsPatientid),patientId);
				Predicate filepredicate=cb.equal(fileid.get(FileName_.filenameId),fileNameId);
				return query.where(cb.and(catpredicate,patpredicate,filepredicate)).getRestriction();
			}
		};
	}

	/**
	 * Specification to forward the documents using alerts
	 * @param alertIdList
	 * @return
	 */
	public static Specification<AlertEvent> byAlertId(final List<Integer> alertIdList) {
		return new Specification<AlertEvent>() {

			@Override
			public Predicate toPredicate(Root<AlertEvent> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {

				Expression<Integer> expr=root.get(AlertEvent_.alertEventId);
				Predicate predicate=query.where(expr.in(alertIdList)).getRestriction();
				return predicate;
			}
		};
	}

	/**
	 * Specification to forward the documents using alerts
	 * @param docCategoryid
	 * @return
	 */
	public static Specification<PatientDocumentsCategory> CatId1(final int docCategoryid){
		return new Specification<PatientDocumentsCategory>() {

			@Override
			public Predicate toPredicate(Root<PatientDocumentsCategory> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				query.where(cb.equal(root.get(PatientDocumentsCategory_.patientDocCategoryId), docCategoryid));
				return query.getRestriction();
			}
		};
	}

}
