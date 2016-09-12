package com.glenwood.glaceemr.server.application.services.Documents;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.FileDetails;
import com.glenwood.glaceemr.server.application.models.PatientDocumentsNotes;

public interface DocumentsService {

	List<Object> getCategoryList(int patientId);
	List<FileDetails> getFileDetailsByPatientId(int patientId,int categoryId );
	List<FileDetails> getFileList(int patientId,int categoryId, int fileNameId);
	List<PatientDocumentsNotes> getDocNotesDetails(int notesFilenameId);
	void deleteFile(int fileNameId);
	void deleteFolder(int fileDetailsId);
	List<AlertEvent> forwardAlert(int fromId, List<Integer> toIdList,int status, int alertid,int docCategoryid, int refId, int patientId,int encounterId, String msg, int chartId, int roomId,int parentId);
}
