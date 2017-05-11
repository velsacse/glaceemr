package com.glenwood.glaceemr.server.application.services.portal.portalLabResults;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.FileDetails;
import com.glenwood.glaceemr.server.application.models.H068;
import com.glenwood.glaceemr.server.application.models.LabEntries;
import com.glenwood.glaceemr.server.application.models.LabEntriesParameter;
import com.glenwood.glaceemr.server.application.models.LabParameterCode;
import com.glenwood.glaceemr.server.application.models.PortalLabParametersBean;
import com.glenwood.glaceemr.server.application.models.PortalLabResultBean;
import com.glenwood.glaceemr.server.application.models.PortalLabResultsConfigBean;
import com.glenwood.glaceemr.server.application.repositories.ChartRepository;
import com.glenwood.glaceemr.server.application.repositories.EmpProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.FileDetailsRepository;
import com.glenwood.glaceemr.server.application.repositories.FileNameRepository;
import com.glenwood.glaceemr.server.application.repositories.H068Repository;
import com.glenwood.glaceemr.server.application.repositories.Hl7ResultInboxRepository;
import com.glenwood.glaceemr.server.application.repositories.Hl7UnmappedResultsRepository;
import com.glenwood.glaceemr.server.application.repositories.LabEntriesParameterRepository;
import com.glenwood.glaceemr.server.application.repositories.LabEntriesRepository;
import com.glenwood.glaceemr.server.application.repositories.LabParameterCodeRepository;
import com.glenwood.glaceemr.server.application.repositories.LabParametersRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailSaveService;
import com.glenwood.glaceemr.server.application.services.portal.portalDocuments.SharedFolderUtil;
import com.glenwood.glaceemr.server.application.specifications.PortalLabResultsSpecification;

@Service
public class PortalLabResultsServiceImpl implements PortalLabResultsService{

	@Autowired
	EmpProfileRepository empProfileRepository;

	@Autowired
	Hl7ResultInboxRepository resultsRepository;

	@Autowired
	Hl7UnmappedResultsRepository unmappedResultsRepository;

	@Autowired
	FileNameRepository fileNameRepository;

	@Autowired
	ChartRepository chartRepository;

	@Autowired
	PatientRegistrationRepository registrationRepository;

	@Autowired
	LabParametersRepository labParametersRepository;

	@Autowired
	LabEntriesRepository labEntriesRepository;
	
	@Autowired
	H068Repository h068Repository;

	@Autowired
	LabEntriesParameterRepository labEntriesParametersRepository;
	
	@Autowired
	LabParameterCodeRepository labParameterCodeRepository;

	@Autowired
	EntityManagerFactory emf ;
	
	@Autowired
	FileDetailsRepository fileDetailsRepository;
	
	
	@Autowired
	AuditTrailSaveService auditTrailSaveService;
	
	@Autowired
	HttpServletRequest request;
	
	@Override
	public List<LabEntries> getPatientLabResultsList(int patientId, int chartId, int pageOffset, int pageIndex) {
		
		List<LabEntries> labResultsList=labEntriesRepository.findAll(PortalLabResultsSpecification.getPatientLabResults(patientId, chartId), PortalLabResultsSpecification.createPortalLabResultListRequestByDescDate(pageIndex, pageOffset)).getContent();
		
		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Patient with:"+patientId+"requested patient lab results list",-1,
				request.getRemoteAddr(),patientId,"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Requested patient lab results list with patientId:"+patientId,"");
		
		return labResultsList;
	}
	
	@Override
	public PortalLabResultsConfigBean getPortalLabResultsConfigBean() {
		
		List<H068> labResultsList=h068Repository.findAll(PortalLabResultsSpecification.getLabStatusConfig());
		
		PortalLabResultsConfigBean labResultConfigBean=new PortalLabResultsConfigBean();
		labResultConfigBean.setLabResultStatusList(labResultsList);	
		
		return labResultConfigBean;
	}

	@Override
	public PortalLabResultBean getPatientLabResultParametersList(
			int testDetailId, int chartId, int patientId,int pageOffset, int pageIndex) throws IOException {
		
		
		PortalLabResultBean portalLabResultBean=new PortalLabResultBean();
		
		List<LabEntriesParameter> labResultParametersList = labEntriesParametersRepository.findAll(PortalLabResultsSpecification.getLabResultParameters(testDetailId, chartId));
		
		List<PortalLabParametersBean> prametersList=new ArrayList<PortalLabParametersBean>();
				
		for(int i=0;i<labResultParametersList.size();i++){
			
			/*for(int j=0;j<codeSystemParamList.size();j++){
				
				if(labResultParametersList.get(i).getLabEntriesParameterId()==codeSystemParamList.get(j).getLabParameterCodeParamid())
				{*/
					PortalLabParametersBean parametersBean=new PortalLabParametersBean();
					parametersBean.setLabParameter(labResultParametersList.get(i));
					parametersBean.setParameterHistory(labEntriesParametersRepository.findAll(PortalLabResultsSpecification.getLabParameterHistory(labResultParametersList.get(i).getLabEntriesParameterId(), chartId)));
					prametersList.add(parametersBean);
				
				/*}
			}*/
		}
		
		portalLabResultBean.setLabParametersList(prametersList);
		portalLabResultBean.setLabAttachments(getLabAttachmentsFileDetails(patientId, testDetailId));
		
		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Requested patient lab results parameter list with patientId:"+patientId,-1,
				request.getRemoteAddr(),patientId,"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Requested patient lab results parameter list with patientId:"+patientId,"");
		
		return portalLabResultBean;
	}
	
	@Override
	public List<LabEntriesParameter> getPortalLabResultParameterHistory(int parameterId, int chartId) {
		
		List<LabEntriesParameter> labResultParamterHistoryList=labEntriesParametersRepository.findAll(PortalLabResultsSpecification.getLabParameterHistory(parameterId, chartId));	
		
		return labResultParamterHistoryList;
	}

	@Override
	public FileDetails getLabAttachmentsFileDetails(int patientId, int fileDetailEntityId) throws IOException {
		
		FileDetails fileDetails=fileDetailsRepository.findOne(PortalLabResultsSpecification.getLabAttachmentsFileDetails(patientId, fileDetailEntityId));
		
		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Requested patient lab attachment file details with patientId:"+patientId,-1,
				request.getRemoteAddr(),patientId,"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Requested patient lab attachment file details with patientId:"+patientId,"");
		
		return fileDetails;
	}

}
