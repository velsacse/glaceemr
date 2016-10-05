package com.glenwood.glaceemr.server.application.services.portal.portalDocuments;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.FileDetails;
import com.glenwood.glaceemr.server.application.models.FileName;
import com.glenwood.glaceemr.server.application.models.PatientPortalSharedDocs;
import com.glenwood.glaceemr.server.application.models.PatientSharedDocumentLog;

public interface PortalDocumentsService {

	List<PatientPortalSharedDocs> getPatientSharedDocs(int patientId, int pageOffset, int pageIndex);
	
	List<PatientSharedDocumentLog> getPatientVisitSummary(int patientId, int pageOffset, int pageIndex) throws Exception;

	FileDetails getFileDetails(int patientId, int fileId);
	
	List<FileName> getFileNameDetails(int patientId, int fileId);

	PortalFileDetailBean getPatientCDAFileDetails(int patientId, int encounterId, String fileName) throws Exception;

}
