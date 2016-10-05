package com.glenwood.glaceemr.server.application.services.portal.portalLabResults;

import java.io.IOException;
import java.util.List;

import com.glenwood.glaceemr.server.application.models.FileDetails;
import com.glenwood.glaceemr.server.application.models.LabEntries;
import com.glenwood.glaceemr.server.application.models.LabEntriesParameter;
import com.glenwood.glaceemr.server.application.models.PortalLabResultBean;
import com.glenwood.glaceemr.server.application.models.PortalLabResultsConfigBean;

public interface PortalLabResultsService {

	List<LabEntries> getPatientLabResultsList(int patientId, int chartId, int pageOffset, int pageIndex);

	PortalLabResultsConfigBean getPortalLabResultsConfigBean();

	PortalLabResultBean getPatientLabResultParametersList(int testDetailId, int chartId, int patientId,int pageOffset, int pageIndex) throws IOException;

	List<LabEntriesParameter> getPortalLabResultParameterHistory(int parameterId, int chartId);

	FileDetails getLabAttachmentsFileDetails(int patientId, int fileDetailEntityId) throws IOException;

}
