package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.glenwood.glaceemr.server.application.Bean.ClinicalDataQDM;
import com.glenwood.glaceemr.server.application.Bean.EPMeasureBean;
import com.glenwood.glaceemr.server.application.Bean.InvestigationQDM;
import com.glenwood.glaceemr.server.application.Bean.MIPSPatientInformation;
import com.glenwood.glaceemr.server.application.Bean.MIPSPerformanceBean;
import com.glenwood.glaceemr.server.application.Bean.MeasureStatus;
import com.glenwood.glaceemr.server.application.Bean.ReferralQDM;
import com.glenwood.glaceemr.server.application.Bean.SharedFolderBean;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Assessment;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.DiagnosticStudy;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Intervention;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.LabTest;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Patient;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.PhysicalExam;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Procedure;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.QDM;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Request;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.EMeasureUtils;
import com.glenwood.glaceemr.server.application.Bean.mailer.GlaceMailer;
import com.glenwood.glaceemr.server.application.models.Billinglookup;
import com.glenwood.glaceemr.server.application.models.Billinglookup_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.H478;
import com.glenwood.glaceemr.server.application.models.H478_;
import com.glenwood.glaceemr.server.application.models.InsCompany;
import com.glenwood.glaceemr.server.application.models.InsCompany_;
import com.glenwood.glaceemr.server.application.models.MacraConfiguration;
import com.glenwood.glaceemr.server.application.models.MacraConfiguration_;
import com.glenwood.glaceemr.server.application.models.MacraMeasuresRate;
import com.glenwood.glaceemr.server.application.models.MacraMeasuresRate_;
import com.glenwood.glaceemr.server.application.models.MacraProviderConfiguration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresPatientEntries;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresPatientEntriesHistory;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresPatientEntries_;
import com.glenwood.glaceemr.server.application.repositories.MacraConfigurationRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientMeasureStatusLogRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientMeasureStatusRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.repositories.ProblemListRepository;
import com.glenwood.glaceemr.server.application.specifications.QPPPerformanceSpecification;

@Service
@Transactional
public class MeasureCalcServiceImpl implements MeasureCalculationService{
	
	@Autowired
	MacraConfigurationRepository macraConfRepo;
	
	@Autowired
	PatientMeasureStatusRepository patientDataRepo;
	
	@Autowired
	PatientMeasureStatusLogRepository patientLogRepo;
	
	@Autowired
	PatientRegistrationRepository patientInfoRepo;
	
	@Autowired
	ProblemListRepository diagnosisRepo;
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	SharedFolderBean sharedFolderBean;
	
	RestTemplate restTemplate = new RestTemplate();

	
	@Override
	public void saveMeasureDetails(int providerId, int patientId, List<MeasureStatus> measureStatus, boolean flag) {
		
		MeasureStatus patientObj = new MeasureStatus();
		
		QualityMeasuresPatientEntriesHistory patientLogObj = new QualityMeasuresPatientEntriesHistory();
		
		Date d = new Date();
		Timestamp curr_time = new Timestamp(d.getTime());
		
		String npi = getNPIForProvider(providerId);
		
		String tin = getTINForProvider(providerId);
		
		for(int i=0;i<measureStatus.size();i++){
			
			patientObj = measureStatus.get(i);
			
			String measureId = "";
			
			if(flag){
			
				measureId = ""+patientObj.getMeasureId();
				measureId = measureId.split("-")[0];
				
			}else{
				
				measureId = patientObj.getCmsId();
				
			}
			
			patientLogObj = new QualityMeasuresPatientEntriesHistory();
			
			QualityMeasuresPatientEntries patientData = patientDataRepo.findOne(Specifications.where(QPPPerformanceSpecification.isPatientExisting(providerId, measureId, patientId, patientObj.getReportingYear(),patientObj.getCriteria())));
			
			if(patientData == null || patientData.equals(null)){
				
				patientData = new QualityMeasuresPatientEntries();
				
				patientData.setQualityMeasuresPatientEntriesPatientId(patientId);
				patientData.setQualityMeasuresPatientEntriesMeasureId(measureId);
				patientData.setQualityMeasuresPatientEntriesCriteria(patientObj.getCriteria());
				patientData.setQualityMeasuresPatientEntriesProviderId(providerId);
				patientData.setQualityMeasuresPatientEntriesReportingYear(Integer.parseInt(patientObj.getReportingYear()));
				patientData.setQualityMeasuresPatientEntriesUpdatedOn(curr_time);
				patientData.setQualityMeasuresPatientEntriesIpp(patientObj.getIpp());
				patientData.setQualityMeasuresPatientEntriesDenominator(patientObj.getDenominator());
				patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(patientObj.getDenominatorExclusion());
				patientData.setQualityMeasuresPatientEntriesNumerator(patientObj.getNumerator());
				patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(patientObj.getNumeratorExclusion());
				patientData.setQualityMeasuresPatientEntriesDenominatorException(patientObj.getDenominatorException());
				patientData.setQualityMeasuresPatientEntriesMeasurePopulation(patientObj.getMeasurePopulation());
				patientData.setQualityMeasuresPatientEntriesMeasurePopulationExclusion(patientObj.getMeasurePopulationExclusion());
				patientData.setQualityMeasuresPatientEntriesMeasureObservation(new Double(patientObj.getMeasureObservation()).intValue());
				patientData.setQualityMeasuresPatientEntriesNpi(npi);
				patientData.setQualityMeasuresPatientEntriesTin(tin);
				
			}else{
				
				patientData.setQualityMeasuresPatientEntriesUpdatedOn(curr_time);
				patientData.setQualityMeasuresPatientEntriesIpp(patientObj.getIpp());
				patientData.setQualityMeasuresPatientEntriesDenominator(patientObj.getDenominator());
				patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(patientObj.getDenominatorExclusion());
				patientData.setQualityMeasuresPatientEntriesNumerator(patientObj.getNumerator());
				patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(patientObj.getNumeratorExclusion());
				patientData.setQualityMeasuresPatientEntriesDenominatorException(patientObj.getDenominatorException());
				patientData.setQualityMeasuresPatientEntriesMeasurePopulation(patientObj.getMeasurePopulation());
				patientData.setQualityMeasuresPatientEntriesMeasurePopulationExclusion(patientObj.getMeasurePopulationExclusion());
				patientData.setQualityMeasuresPatientEntriesMeasureObservation(new Double(patientObj.getMeasureObservation()).intValue());
				patientData.setQualityMeasuresPatientEntriesNpi(npi);
				patientData.setQualityMeasuresPatientEntriesTin(tin);
				
			}
			
			patientDataRepo.saveAndFlush(patientData);

			patientLogObj.setQualityMeasuresPatientEntriesPatientId(patientId);
			patientLogObj.setQualityMeasuresPatientEntriesMeasureId(measureId);
			patientLogObj.setQualityMeasuresPatientEntriesHistoryCriteria(patientObj.getCriteria());
			patientLogObj.setQualityMeasuresPatientEntriesProviderId(providerId);
			patientLogObj.setQualityMeasuresPatientEntriesReportingYear(Integer.parseInt(patientObj.getReportingYear()));
			patientLogObj.setQualityMeasuresPatientEntriesUpdatedOn(curr_time);
			patientLogObj.setQualityMeasuresPatientEntriesIpp(patientObj.getIpp());
			patientLogObj.setQualityMeasuresPatientEntriesDenominator(patientObj.getDenominator());
			patientLogObj.setQualityMeasuresPatientEntriesDenominatorExclusion(patientObj.getDenominatorExclusion());
			patientLogObj.setQualityMeasuresPatientEntriesNumerator(patientObj.getNumerator());
			patientLogObj.setQualityMeasuresPatientEntriesNumeratorExclusion(patientObj.getNumeratorExclusion());
			patientLogObj.setQualityMeasuresPatientEntriesDenominatorException(patientObj.getDenominatorException());
			patientLogObj.setQualityMeasuresPatientEntriesMeasurePopulation(patientObj.getMeasurePopulation());
			patientLogObj.setQualityMeasuresPatientEntriesMeasurePopulationExclusion(patientObj.getMeasurePopulationExclusion());
			patientLogObj.setQualityMeasuresPatientEntriesMeasureObservation(new Double(patientObj.getMeasureObservation()).intValue());
			patientLogObj.setQualityMeasuresPatientEntriesHistoryNpi(npi);
			patientLogObj.setQualityMeasuresPatientEntriesHistoryTin(tin);
			
			patientLogRepo.saveAndFlush(patientLogObj);
			
		}
		
	}

	@Override
	public Request getQDMRequestObject(Boolean considerProvider,int patientID, int providerId, HashMap<String, HashMap<String, String>> codeListForQDM, Date repStartDate, Date repEndDate) {
		
		Request finalReqObject = new Request();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		try{

			ExportQDM qdmData = new ExportQDM();
			EMeasureUtils measureUtils = new EMeasureUtils();		
			Patient requestObj = new Patient();

			String startDate="12/31/2015";
			String endDate="12/31/2017";

			SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy");
			Date date1=format.parse(startDate);
			Date date2=format.parse(endDate);
			
			HashMap<String, String> codeListForCNM = measureUtils.getCodeListForCNM(codeListForQDM);

			String snomedCodesForCNM = codeListForCNM.get("SNOMED");

			String loincCodesForCNM = codeListForCNM.get("LOINC");

			requestObj = qdmData.getRequestQDM(em,patientInfoRepo, diagnosisRepo, patientID, providerId);
				
			List<Procedure> medicationsReviewed = qdmData.getMedicationsReviewed(em,considerProvider,providerId,patientID,repStartDate,repEndDate);

			requestObj.setEncounterList(qdmData.getEncounterQDM(repStartDate, repEndDate, em, considerProvider, patientID, providerId, codeListForQDM.get("Encounter")));

			requestObj.setDxList(qdmData.getPatientDiagnosisQDM(diagnosisRepo, patientID));

			List<QDM> tobaccoDetails=qdmData.getTobaccoDetails(em, patientID);
			
			requestObj.setTobaccoStatusList(tobaccoDetails);
			
			if(codeListForQDM.containsKey("Medication")){
				requestObj.setMedicationOrders(qdmData.getMedicationQDM(em,considerProvider,providerId,codeListForQDM.get("Medication").get("RXNORM"), patientID, 2));
				requestObj.setActiveMedicationsList(qdmData.getActiveMedications(em,considerProvider,providerId, codeListForQDM.get("Medication").get("RXNORM"), patientID, 2));
			}

			List<InvestigationQDM> investigationQDM = qdmData.getInvestigationQDM(em,considerProvider,patientID,providerId);

			List<ClinicalDataQDM> clinicalDataQDM =qdmData.getClinicalDataQDM(em,considerProvider,providerId,patientID,snomedCodesForCNM,loincCodesForCNM,true,date1,date2);

			if(codeListForQDM.containsKey("Immunization")){
				requestObj.setImmunizationList(qdmData.getImmuDetails(em,considerProvider,providerId, patientID));
			}

			if(codeListForQDM.containsKey("Diagnostic Study")){
				String codeList = measureUtils.getCodeListByCategory(codeListForQDM, "Diagnostic Study");
				List<DiagnosticStudy> diagnosisFromCNM = measureUtils.getDiagnosisFromCNM(clinicalDataQDM,codeList);
				requestObj.setDiagnosticStudyList(measureUtils.getDiagnosticStudyQDM(investigationQDM,codeList,diagnosisFromCNM));
			}

			if(codeListForQDM.containsKey("Laboratory Test")){
				List<LabTest> labTestQDM = measureUtils.getLabTestQDMForInvestigation(investigationQDM, measureUtils.getCodeListByCategory(codeListForQDM, "Laboratory Test"));
				requestObj.setLabTestList(labTestQDM);
			}

			if(codeListForQDM.containsKey("Risk Category/Assessment")){
				List<Assessment> riskAssessment =measureUtils.getAssessmentFromCNM(clinicalDataQDM,measureUtils.getCodeListByCategory(codeListForQDM, "Risk Category/Assessment"));
				requestObj.setRiskAssessmentList(riskAssessment);
			}

			if(codeListForQDM.containsKey("Physical Exam")){
				List<PhysicalExam> physicalExam=measureUtils.getPhysicalexamFromCNM(clinicalDataQDM,measureUtils.getCodeListByCategory(codeListForQDM, "Physical Exam"));
				requestObj.setPhysicalExamList(physicalExam);
			}

			if(codeListForQDM.containsKey("Procedure")){
				String codeList=measureUtils.getCodeListByCategory(codeListForQDM, "Procedure");
				List<String> cptCodes=measureUtils.getCPTCodes(codeListForQDM, "Procedure");
				List<Procedure> procBasedOnCPT=qdmData.getProcBasedOnCPT(em,considerProvider, patientID, providerId, cptCodes);
				List<Procedure>  procFromCNM = measureUtils.getProcFromCNM(clinicalDataQDM,codeList);
				requestObj.setProcedureList(measureUtils.getProcedureQDM(investigationQDM,codeList,medicationsReviewed,procFromCNM,procBasedOnCPT));
			}
			
			if(codeListForQDM.containsKey("Intervention")){

				String codeList = measureUtils.getCodeListByCategory(codeListForQDM, "Intervention");
				
				List<Intervention> interventionQDM = measureUtils.getInterventionQDM(investigationQDM,codeList);
	
				List<Intervention>  interventionFromCNM = measureUtils.getInterventionFromCNM(clinicalDataQDM,codeList);
				
				if(interventionFromCNM.size() > 0){
					interventionQDM.addAll(interventionFromCNM);
				}
				
				if(codeListForQDM.containsKey("Communication")){
					
					List<ReferralQDM> referralObj = qdmData.getReferrals(em,considerProvider,providerId,patientID);
					
					if(referralObj.size() > 0){
						
						interventionQDM.addAll(qdmData.setReferrals(referralObj));
						
						requestObj.setCommunications(qdmData.getCommunicationQDM(referralObj));
						
					}
					
				}
				
				requestObj.setInterventionList(interventionQDM);

			}

			finalReqObject.setPatient(requestObj);

		}
		catch(Exception e){
			
			try {
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat("glace", patientID,"Error occurred while generating QDM Object",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,"glace",GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();

			}
			
		}
		
		return finalReqObject;
	}
	
	@Override
	public List<EPMeasureBean> getEPMeasuresResponseObject(String accountId,Boolean isGroup,int patientID, int providerId, Date startDate, Date endDate, int reportingYear) {
		
		List<EPMeasureBean> epMeasureInfo = new ArrayList<EPMeasureBean>();
		
		ExportQDM qdmData = new ExportQDM();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		Map<String, MeasureStatus> measureStatusObj = new HashMap<String, MeasureStatus>();
		
		List<MeasureStatus> epMeasureBean = new ArrayList<MeasureStatus>();
		
		try{

			boolean flag = qdmData.checkIsVisited(isGroup, patientID, providerId, em, startDate, endDate);
			
			epMeasureInfo.add(setEPrescriptionDetails(accountId, isGroup, qdmData, patientID, providerId, startDate, endDate, measureStatusObj));
			
			epMeasureInfo.add(setMedicationReconcilatonDetails(accountId, isGroup, qdmData, patientID, providerId, startDate, endDate, measureStatusObj));

			epMeasureInfo.add(setSecureMessageInfoDetails(accountId, isGroup, qdmData, patientID, providerId, startDate, endDate, flag, measureStatusObj));
			
			epMeasureInfo.add(setPatientAccessInfoForPortal(accountId, qdmData, patientID, providerId, startDate, endDate, flag, measureStatusObj));
			
			epMeasureInfo.add(setPatientElectronicAccessInfo(accountId, isGroup, qdmData, patientID, providerId, startDate, endDate, flag, measureStatusObj));
			
			epMeasureInfo.add(setReferralExchangeInfo(accountId, isGroup, qdmData, patientID, providerId, startDate, endDate, measureStatusObj));
			
			epMeasureInfo.add(setPatientEducationMaterialDetails(accountId, isGroup, qdmData, patientID, providerId, startDate, endDate, flag, measureStatusObj));
			
			for(int i=0;i<measureStatusObj.keySet().size();i++){
				
				String key = measureStatusObj.keySet().toArray()[i].toString();
				
				measureStatusObj.get(key).setReportingYear(""+reportingYear);
				
				epMeasureBean.add(measureStatusObj.get(key));
				
			}
			
			saveMeasureDetails(providerId, patientID, epMeasureBean, false);
			
		}catch(Exception e){
			
			try {
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, patientID,"Error occurred while generating EP Measures Object",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();

			}
			
		}
		
		return epMeasureInfo;
		
		
	}
	
	private EPMeasureBean setEPrescriptionDetails(String accountId,Boolean isGroup,ExportQDM qdmData, int patientID, int providerId, Date startDate, Date endDate, Map<String, MeasureStatus> measureStatus){
		
		EPMeasureBean epObject = new EPMeasureBean();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);

		MeasureStatus measureObj = new MeasureStatus();
		
		try{

			String ePrescResult = qdmData.getEPrescribingDetails(isGroup, patientID, providerId, em, startDate, endDate, measureObj);

			epObject.setMeasureId("ACI_EP_1");
			epObject.setMeasureTitle("Electronic Prescribing");
			epObject.setShortDescription(ePrescResult.split("&&&&")[0]);
			epObject.setDescription(ePrescResult.split("&&&&")[0].concat(ePrescResult.split("&&&&")[1]));
			
			measureObj.setCmsId("ACI_EP_1");

			if(Integer.parseInt(ePrescResult.split("&&&&")[2]) == -1){
				epObject.setStatus("Not Applicable");
			}else{
				epObject.setStatus("Show Description");
			}

		}catch(Exception e){
			
			try{
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, patientID,"Error occurred while getting e-prescribing details",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();

			}
			
		}
		
		measureStatus.put("ACI_EP_1", measureObj);
		
		return epObject;
		
	}
	
	private EPMeasureBean setSecureMessageInfoDetails(String accountId,Boolean isGroup,ExportQDM qdmData, int patientId, int providerId, Date startDate, Date endDate, boolean visitFlag, Map<String, MeasureStatus> measureStatus){
		
		EPMeasureBean epObject = new EPMeasureBean();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		MeasureStatus measureObj = new MeasureStatus();
		
		try{

			epObject.setMeasureId("ACI_CCTPE_2");
			epObject.setMeasureTitle("Secure Messaging");
			
			measureObj.setCmsId("ACI_CCTPE_2");
			
			if(visitFlag){
				
				String result = qdmData.getSecureMessagingInfo(isGroup, providerId, patientId, startDate, endDate, em, measureObj);
				
				if((Integer.parseInt(result.split(":")[1].replace(" ","")) >= 1)){
					epObject.setStatus("Completed");
					epObject.setDescription(result);
				}else{
					epObject.setStatus("Not Completed");
					epObject.setDescription("Total Messages sent: 0");
				}
				
			}else{
				
				epObject.setDescription("No office visits done for this patient");
				epObject.setStatus("Not Applicable");				
				
			}
			
		}catch(Exception e){
			
			try{
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, patientId,"Error occurred while getting messaging details",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();

			}
			
		}
		
		measureStatus.put("ACI_CCTPE_2", measureObj);
		
		return epObject;
		
	}
	
	private EPMeasureBean setPatientAccessInfoForPortal(String accountId,ExportQDM qdmData, int patientId, int providerId, Date startDate, Date endDate, boolean visitFlag, Map<String, MeasureStatus> measureStatus){
		
		EPMeasureBean epObject = new EPMeasureBean();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		MeasureStatus measureObj = new MeasureStatus();
		
		try{

			epObject.setMeasureId("ACI_PEA_1");
			epObject.setMeasureTitle("Patient Electronic Access");

			measureObj.setCmsId("ACI_PEA_1");
			
			if(!visitFlag){
				
				epObject.setDescription("No office visits done for this patient");
				epObject.setStatus("Not Applicable");
				
			}else{
				
				if(qdmData.getPatientPortalAccess(patientId, em)){
					
					String portalLastAccessed = qdmData.getPatientAccessInfoForPortal(patientId, startDate, endDate, em, measureObj);
					
					epObject.setStatus("Completed");
					
					if(portalLastAccessed.length() > 0){
						epObject.setDescription("Last Accessed Portal on: "+portalLastAccessed);
					}
					
				}else{
					
					epObject.setDescription("Patient Portal Access : Inactive");
					epObject.setStatus("Not Completed");
					
				}
				
			}
			
		}catch(Exception e){
			
			try{
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, patientId,"Error occurred while getting portal access info",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();

			}
			
		}
		
		measureStatus.put("ACI_PEA_1", measureObj);
		
		return epObject;
		
	}
	
	private EPMeasureBean setPatientElectronicAccessInfo(String accountId,Boolean isGroup,ExportQDM qdmData, int patientId, int providerId, Date startDate, Date endDate, boolean visitFlag, Map<String, MeasureStatus> measureStatus){
		
		EPMeasureBean epObject = new EPMeasureBean();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		MeasureStatus measureObj = new MeasureStatus();
		
		try{

			epObject.setMeasureId("ACI_CCTPE_1");
			epObject.setMeasureTitle("View, Download and Transmit (VDT)");

			measureObj.setCmsId("ACI_CCTPE_1");
			
			if(!visitFlag){
				
				epObject.setDescription("No office visits done for this patient");
				epObject.setStatus("Not Applicable");
				
			}else{
				
				if(qdmData.getPatientPortalAccess(patientId, em)){
					
					String portalLastAccessed = qdmData.getPatientElectronicAccessInfo(isGroup,patientId, startDate, endDate, em, measureObj);
					
					if(portalLastAccessed.length() > 0){
						epObject.setStatus("Completed");
						epObject.setDescription("Last Accessed Portal on: "+portalLastAccessed);
					}else{
						epObject.setDescription("No views/downloads recorded");
						epObject.setStatus("Not Completed");
					}
					
				}else{
					
					epObject.setDescription("Patient Portal Access : Inactive");
					epObject.setStatus("Not Completed");
					
				}
				
			}
			
		}catch(Exception e){
			
			try{
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, patientId,"Error occurred while getting patient VDT info",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();

			}
			
		}
		
		measureStatus.put("ACI_CCTPE_1", measureObj);
		
		return epObject;
		
	}
	
	private EPMeasureBean setReferralExchangeInfo(String accountId,Boolean isGroup,ExportQDM qdmData, int patientId, int providerId, Date startDate, Date endDate, Map<String, MeasureStatus> measureStatus){
		
		EPMeasureBean epObject = new EPMeasureBean();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		MeasureStatus measureObj = new MeasureStatus();
		
		try{

			String result = qdmData.getReferralInfoExchangeByProvider(isGroup, providerId, patientId, startDate, endDate, em, measureObj);

			epObject.setMeasureId("ACI_HIE_1");
			epObject.setMeasureTitle("Health Information Exchange");

			measureObj.setCmsId("ACI_HIE_1");
			
			if(result.equals("No referrals have been sent by provider")){
				epObject.setDescription("No referrals have been sent by provider");
				epObject.setStatus("Not Applicable");
			}else{
				epObject.setStatus("Show Description");
				epObject.setDescription(result.split(" &&&& ")[0].concat(" ".concat(result.split(" &&&& ")[1])));
				epObject.setShortDescription(result.split(" &&&& ")[0]);
			}

		}catch(Exception e){
			
			try{
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, patientId,"Error occurred while getting referral details",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();

			}
			
		}
		
		measureStatus.put("ACI_HIE_1", measureObj);
		
		return epObject;
		
	}
	
	private EPMeasureBean setPatientEducationMaterialDetails(String accountId, Boolean isGroup, ExportQDM qdmData, int patientId, int providerId, Date startDate, Date endDate, boolean visitFlag, Map<String, MeasureStatus> measureStatus){
		
		EPMeasureBean epObject = new EPMeasureBean();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		MeasureStatus measureObj = new MeasureStatus();
		
		try{
			
			epObject.setMeasureId("ACI_PEA_2");
			epObject.setMeasureTitle("Patient-Specific Education");

			measureObj.setCmsId("ACI_PEA_2");

			if(!visitFlag){
				
				epObject.setDescription("No office visits done for this patient");
				epObject.setStatus("Not Applicable");
				
			}else{

				String result = qdmData.getPatientEducationDetails(isGroup, providerId, patientId, startDate, endDate, em, measureObj);
				
				epObject.setDescription(result);
				
				if(result.equals("No handouts have been given by provider")){
					epObject.setStatus("Not Completed");
				}else{
					epObject.setStatus("Completed");
				}
				
			}
			
		}catch(Exception e){
			
			try{
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, patientId,"Error occurred while getting medication reconcilation details",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();

			}
			
		}
		
		measureStatus.put("ACI_PEA_2", measureObj);
		
		return epObject;
		
	}
	
	private EPMeasureBean setMedicationReconcilatonDetails(String accountId,Boolean isGroup,ExportQDM qdmData, int patientId, int providerId, Date startDate, Date endDate, Map<String, MeasureStatus> measureStatus){
		
		EPMeasureBean epObject = new EPMeasureBean();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		MeasureStatus measureObj = new MeasureStatus();
		
		try{

			String result = qdmData.getMedicationReconcilcationStatus(isGroup, providerId, patientId, startDate, endDate, em, measureObj);

			epObject.setMeasureId("ACI_HIE_3");
			epObject.setMeasureTitle("Medication Reconcilation");

			measureObj.setCmsId("ACI_HIE_3");
			
			if(result.equals("No Encounters with Transition of Care marked")){
				epObject.setDescription("No Encounters with Transition of Care marked");
				epObject.setStatus("Not Applicable");
			}else{
				epObject.setStatus("Show Description");
				epObject.setDescription(result.split(" &&&& ")[0].concat(" ".concat(result.split(" &&&& ")[1])));
				epObject.setShortDescription(result.split(" &&&& ")[0]);
			}

		}catch(Exception e){
			
			try{
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, patientId,"Error occurred while getting referral details",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();

			}
			
		}
		
		measureStatus.put("ACI_HIE_3", measureObj);
		
		return epObject;
		
	}
	
	@Override
	public Boolean checkGroupOrIndividual(int year) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object> cq = builder.createQuery(Object.class);
        Root<MacraConfiguration> root = cq.from(MacraConfiguration.class);
        Predicate yearPredicate=builder.equal(root.get(MacraConfiguration_.macraConfigurationYear),year);
        cq.select(root.get(MacraConfiguration_.macraConfigurationType));
        cq.where(yearPredicate);
        List<Object> result=em.createQuery(cq).getResultList();
        if(Integer.parseInt(result.get(0).toString())==0){
        	return true;}
        else{
        	return false;
        }
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<MIPSPerformanceBean> getMeasureRateReport(int providerId, String accountId, String configuredMeasures,boolean isACIReport){
		
		Calendar now = Calendar.getInstance();
		int reportingYear = now.get(Calendar.YEAR);
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<MIPSPerformanceBean> cq = builder.createQuery(MIPSPerformanceBean.class);
		Root<MacraMeasuresRate> root = cq.from(MacraMeasuresRate.class);

		Predicate byProviderId = builder.equal(root.get(MacraMeasuresRate_.macraMeasuresRateProviderId), providerId);
		Predicate byReportingYear = builder.equal(root.get(MacraMeasuresRate_.macraMeasuresRateReportingYear), reportingYear);		
		Predicate byConfiguredMeasures = root.get(MacraMeasuresRate_.macraMeasuresRateMeasureId).in(Arrays.asList(configuredMeasures.split(",")));
		
		cq.where(builder.and(byProviderId, byReportingYear, byConfiguredMeasures));
		
		Selection[] cqm_selections= new Selection[] {
			root.get(MacraMeasuresRate_.macraMeasuresRateMeasureId),
			root.get(MacraMeasuresRate_.macraMeasuresRateCriteria),
			root.get(MacraMeasuresRate_.macraMeasuresRateReportingYear),
			builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateIpp),0),
			root.get(MacraMeasuresRate_.macraMeasuresRateIpplist),
			builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateDenominator),0),
			root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorlist),
			builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorExclusion),0),
			root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorExclusionlist),
			builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateNumerator),0),
			root.get(MacraMeasuresRate_.macraMeasuresRateNumeratorlist),
			builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateNumeratorExclusion),0),
			root.get(MacraMeasuresRate_.macraMeasuresRateNumeratorExclusionlist),
			builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorException),0),
			root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorExceptionlist),
			root.get(MacraMeasuresRate_.macraMeasuresRatePerformance),
			root.get(MacraMeasuresRate_.macraMeasuresRateReporting)
		};
		
		Selection[] aci_selections= new Selection[] {
				root.get(MacraMeasuresRate_.macraMeasuresRateMeasureId),
				root.get(MacraMeasuresRate_.macraMeasuresRateCriteria),
				root.get(MacraMeasuresRate_.macraMeasuresRateReportingYear),
				builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateIpp),0),
				root.get(MacraMeasuresRate_.macraMeasuresRateIpplist),
				builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateDenominator),0),
				root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorlist),
				builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorExclusion),0),
				root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorExclusionlist),
				builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateNumerator),0),
				root.get(MacraMeasuresRate_.macraMeasuresRateNumeratorlist),
				builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateNumeratorExclusion),0),
				root.get(MacraMeasuresRate_.macraMeasuresRateNumeratorExclusionlist),
				builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorException),0),
				root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorExceptionlist),
				root.get(MacraMeasuresRate_.macraMeasuresRatePerformance),
				root.get(MacraMeasuresRate_.macraMeasuresRateReporting),
				root.get(MacraMeasuresRate_.macraMeasuresRatePoints)
		};
		
		if(isACIReport){
			cq.multiselect(aci_selections);
		}else{
			cq.multiselect(cqm_selections);
		}
		
		List<MIPSPerformanceBean> results = em.createQuery(cq).getResultList();
		
		for(int i=0;i<results.size();i++){
			
			MIPSPerformanceBean resultObject = results.get(i);
			
			String measureId = resultObject.getMeasureId();

			String cmsIdNTitle = "";
			
			if(configuredMeasures.contains("ACI_EP_1")){
				
				if(measureId.equals("ACI_EP_1")){
					cmsIdNTitle = "ACI_EP_1&&&Electronic Prescribing";						
				}else if(measureId.equals("ACI_CCTPE_2")){
					cmsIdNTitle = "ACI_CCTPE_2&&&Secure Messaging";						
				}else if(measureId.equals("ACI_PEA_1")){
					cmsIdNTitle = "ACI_PEA_1&&&Patient Electronic Access";						
				}else if(measureId.equals("ACI_CCTPE_1")){
					cmsIdNTitle = "ACI_CCTPE_1&&&View, Download and Transmit (VDT)";						
				}else if(measureId.equals("ACI_HIE_1")){
					cmsIdNTitle = "ACI_HIE_1&&&Health Information Exchange";						
				}else if(measureId.equals("ACI_PEA_2")){
					cmsIdNTitle = "ACI_PEA_2&&&Patient-Specific Education";						
				}else if(measureId.equals("ACI_HIE_3")){
					cmsIdNTitle = "ACI_HIE_3&&&Medication Reconcilation";						
				}
				
			}else{
				cmsIdNTitle = getCMSIdAndTitle(measureId, accountId);
			}
			
			resultObject.setCmsId(cmsIdNTitle.split("&&&")[0]);
			resultObject.setTitle(cmsIdNTitle.split("&&&")[1]);
			
		}
		
		return results;
		
	}
	
	/**
	 * Function to return NPI value for provider
	 * @param providerId
	 * @return
	 */
	
	private String getNPIForProvider(int providerId){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<H478> root = cq.from(H478.class);
		
		cq.select(root.get(H478_.h478006));
		cq.where(builder.equal(root.get(H478_.h478001), providerId));
		
		List<Object> results = em.createQuery(cq).getResultList();
		
		String npi = "";
		
		if(results.size() > 0){
			npi = results.get(0).toString();
		}
		
		return npi;
		
	}
	
	/**
	 * Function to get TIN number based on selected providerId
	 * 
	 * @param providerId
	 * @param accountId
	 * @param configuredMeasures
	 * @return
	 */
	
	public String getTINForProvider(int providerId){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<EmployeeProfile> root = cq.from(EmployeeProfile.class);
		
		cq.select(root.get(EmployeeProfile_.empProfileSsn));
		cq.where(builder.equal(root.get(EmployeeProfile_.empProfileEmpid), providerId));
		
		List<Object> results = em.createQuery(cq).getResultList();
		
		String ssn = "";
		
		if(results.size() > 0){
			ssn = results.get(0).toString();
		}
		
		return ssn;
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<MIPSPerformanceBean> getMeasureRateReportByNPI(int providerId, String accountId, String configuredMeasures,boolean isACIReport){
		
		Calendar now = Calendar.getInstance();
		int reportingYear = now.get(Calendar.YEAR);
		
		String npiId = getNPIForProvider(providerId);
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<MIPSPerformanceBean> cq = builder.createQuery(MIPSPerformanceBean.class);
		Root<MacraMeasuresRate> root = cq.from(MacraMeasuresRate.class);

		Predicate byNpiId = builder.equal(root.get(MacraMeasuresRate_.macraMeasuresRateNpi), npiId);
		Predicate byReportingYear = builder.equal(root.get(MacraMeasuresRate_.macraMeasuresRateReportingYear), reportingYear);		
		Predicate byConfiguredMeasures = root.get(MacraMeasuresRate_.macraMeasuresRateMeasureId).in(Arrays.asList(configuredMeasures.split(",")));
		
		cq.where(builder.and(byNpiId, byReportingYear, byConfiguredMeasures));
		
		cq.groupBy(root.get(MacraMeasuresRate_.macraMeasuresRateMeasureId),
				root.get(MacraMeasuresRate_.macraMeasuresRateCriteria),
				root.get(MacraMeasuresRate_.macraMeasuresRateReportingYear));
		
		Selection[] selections= new Selection[] {
				
			root.get(MacraMeasuresRate_.macraMeasuresRateMeasureId),
			root.get(MacraMeasuresRate_.macraMeasuresRateCriteria),
			root.get(MacraMeasuresRate_.macraMeasuresRateReportingYear),
			builder.sum(builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateIpp),0)),
			builder.function("string_agg",String.class,root.get(MacraMeasuresRate_.macraMeasuresRateIpplist),builder.literal(",")),
			builder.sum(builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateDenominator),0)),
			builder.function("string_agg",String.class,root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorlist),builder.literal(",")),
			builder.sum(builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorExclusion),0)),
			builder.function("string_agg",String.class,root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorExclusionlist),builder.literal(",")),
			builder.sum(builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateNumerator),0)),
			builder.function("string_agg",String.class,root.get(MacraMeasuresRate_.macraMeasuresRateNumeratorlist),builder.literal(",")),
			builder.sum(builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateNumeratorExclusion),0)),
			builder.function("string_agg",String.class,root.get(MacraMeasuresRate_.macraMeasuresRateNumeratorExclusionlist),builder.literal(",")),
			builder.sum(builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorException),0)),
			builder.function("string_agg",String.class,root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorExceptionlist),builder.literal(",")),

		};
		
		cq.multiselect(selections);
		
		List<MIPSPerformanceBean> results = em.createQuery(cq).getResultList();
		
		for(int i=0;i<results.size();i++){
			
			MIPSPerformanceBean resultObject = results.get(i);
			
			String measureId = resultObject.getMeasureId();
			
			String cmsIdNTitle = "";
			
			if(configuredMeasures.contains("ACI_EP_1")){
				
				if(measureId.equals("ACI_EP_1")){
					cmsIdNTitle = "ACI_EP_1&&&Electronic Prescribing";						
				}else if(measureId.equals("ACI_CCTPE_2")){
					cmsIdNTitle = "ACI_CCTPE_2&&&Secure Messaging";						
				}else if(measureId.equals("ACI_PEA_1")){
					cmsIdNTitle = "ACI_PEA_1&&&Patient Electronic Access";						
				}else if(measureId.equals("ACI_CCTPE_1")){
					cmsIdNTitle = "ACI_CCTPE_1&&&View, Download and Transmit (VDT)";						
				}else if(measureId.equals("ACI_HIE_1")){
					cmsIdNTitle = "ACI_HIE_1&&&Health Information Exchange";						
				}else if(measureId.equals("ACI_PEA_2")){
					cmsIdNTitle = "ACI_PEA_2&&&Patient-Specific Education";						
				}else if(measureId.equals("ACI_HIE_3")){
					cmsIdNTitle = "ACI_HIE_3&&&Medication Reconcilation";						
				}
				
			}else{
				cmsIdNTitle = getCMSIdAndTitle(measureId, accountId);
			}
			
			DecimalFormat newFormat = new DecimalFormat("#0.00");

			double performanceRate = 0;
			double reportingRate = 0;

			int score = 0, points = 0;
			
			long numeratorCount = resultObject.getNumeratorCount();
			long denominatorCount = resultObject.getDenominatorCount();
			long denominatorExclusionCount = resultObject.getDenominatorExclusionCount();
			long denominatorExceptionCount = resultObject.getDenominatorExceptionCount();

			try{

				if(numeratorCount != 0 || denominatorExceptionCount != 0 || denominatorExclusionCount!=0){

					reportingRate =  Double.valueOf(""+numeratorCount+denominatorExceptionCount+denominatorExclusionCount) / denominatorCount;
					reportingRate =  Double.valueOf(newFormat.format(reportingRate));

				}

				if(numeratorCount > 0){

					denominatorCount = denominatorCount - denominatorExclusionCount - denominatorExceptionCount;

					performanceRate =  Double.valueOf(""+numeratorCount) / denominatorCount;
					performanceRate =  Double.valueOf(newFormat.format(performanceRate));

				}
				
				if(reportingRate > 0 && reportingRate%10 != 0){
					score = (int) ((reportingRate / 10)+1);
				}else if(reportingRate > 0 && reportingRate%10 == 0){
					score = (int) (reportingRate / 10);
				}else{
					score = 0;
				}

				if((measureId.equals("ACI_HIE_1")) || (measureId.equals("ACI_PEA_1"))){
					points = score *2;
				}else{
					points = score;
				}

			}catch(Exception e){
				performanceRate = 0;
				reportingRate = 0;
			}
			
			resultObject.setReportingRate(reportingRate);
			resultObject.setPerformanceRate(performanceRate);
			resultObject.setCmsId(cmsIdNTitle.split("&&&")[0]);
			resultObject.setTitle(cmsIdNTitle.split("&&&")[1]);
			resultObject.setPoints(points);
			
		}
		
		return results;
		
	}
	
	/**
	 * Function to calculate performance of group of providers based on TIN value
	 * @param configuredMeasures
	 * @param accountId
	 * @return
	 * MIPSPerformanceBean to save entries in macra_measures_rate table
	 */
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<MIPSPerformanceBean> getGroupPerformanceCount(String tinValue, String configuredMeasures, String accountId,boolean isACIReport){
		
		List<MIPSPerformanceBean> results = new ArrayList<MIPSPerformanceBean>();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		try{
		
			Calendar now = Calendar.getInstance();
			int reportingYear = now.get(Calendar.YEAR);

			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<MIPSPerformanceBean> cq = builder.createQuery(MIPSPerformanceBean.class);
			Root<MacraMeasuresRate> root = cq.from(MacraMeasuresRate.class);
			
			Predicate byEmpTin = builder.equal(root.get(MacraMeasuresRate_.macraMeasuresRateTin), tinValue);
			Predicate byReportingYear = builder.equal(root.get(MacraMeasuresRate_.macraMeasuresRateReportingYear), reportingYear);		
			Predicate byConfiguredMeasures = root.get(MacraMeasuresRate_.macraMeasuresRateMeasureId).in(Arrays.asList(configuredMeasures.split(",")));
			
			cq.where(builder.and(byEmpTin,byReportingYear, byConfiguredMeasures));
			
			cq.groupBy(root.get(MacraMeasuresRate_.macraMeasuresRateTin),
					root.get(MacraMeasuresRate_.macraMeasuresRateMeasureId),
					root.get(MacraMeasuresRate_.macraMeasuresRateCriteria),
					root.get(MacraMeasuresRate_.macraMeasuresRateReportingYear));
			
			Selection[] selections= new Selection[] {

				root.get(MacraMeasuresRate_.macraMeasuresRateTin),
				root.get(MacraMeasuresRate_.macraMeasuresRateMeasureId),
				root.get(MacraMeasuresRate_.macraMeasuresRateCriteria),
				root.get(MacraMeasuresRate_.macraMeasuresRateReportingYear),
				builder.sum(builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateIpp),0)),
				builder.function("string_agg",String.class,root.get(MacraMeasuresRate_.macraMeasuresRateIpplist),builder.literal(",")),
				builder.sum(builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateDenominator),0)),
				builder.function("string_agg",String.class,root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorlist),builder.literal(",")),
				builder.sum(builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorExclusion),0)),
				builder.function("string_agg",String.class,root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorExclusionlist),builder.literal(",")),
				builder.sum(builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateNumerator),0)),
				builder.function("string_agg",String.class,root.get(MacraMeasuresRate_.macraMeasuresRateNumeratorlist),builder.literal(",")),
				builder.sum(builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateNumeratorExclusion),0)),
				builder.function("string_agg",String.class,root.get(MacraMeasuresRate_.macraMeasuresRateNumeratorExclusionlist),builder.literal(",")),
				builder.sum(builder.coalesce(root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorException),0)),
				builder.function("string_agg",String.class,root.get(MacraMeasuresRate_.macraMeasuresRateDenominatorExceptionlist),builder.literal(",")),

			};
			
			cq.multiselect(selections);
			
			results = em.createQuery(cq).getResultList();
			
			for(int i=0;i<results.size();i++){
				
				MIPSPerformanceBean resultObject = results.get(i);
				
				String measureId = resultObject.getMeasureId();
				
				String cmsIdNTitle = "";
				
				if(configuredMeasures.contains("ACI_EP_1")){
					
					if(measureId.equals("ACI_EP_1")){
						cmsIdNTitle = "ACI_EP_1&&&Electronic Prescribing";						
					}else if(measureId.equals("ACI_CCTPE_2")){
						cmsIdNTitle = "ACI_CCTPE_2&&&Secure Messaging";						
					}else if(measureId.equals("ACI_PEA_1")){
						cmsIdNTitle = "ACI_PEA_1&&&Patient Electronic Access";						
					}else if(measureId.equals("ACI_CCTPE_1")){
						cmsIdNTitle = "ACI_CCTPE_1&&&View, Download and Transmit (VDT)";						
					}else if(measureId.equals("ACI_HIE_1")){
						cmsIdNTitle = "ACI_HIE_1&&&Health Information Exchange";						
					}else if(measureId.equals("ACI_PEA_2")){
						cmsIdNTitle = "ACI_PEA_2&&&Patient-Specific Education";						
					}else if(measureId.equals("ACI_HIE_3")){
						cmsIdNTitle = "ACI_HIE_3&&&Medication Reconcilation";						
					}
					
				}else{
					cmsIdNTitle = getCMSIdAndTitle(measureId, accountId);
				}
				
				DecimalFormat newFormat = new DecimalFormat("#0.00");

				double performanceRate = 0;
				double reportingRate = 0;
				
				int score = 0;
				int points = 0;
				
				long numeratorCount = resultObject.getNumeratorCount();
				long denominatorCount = resultObject.getDenominatorCount();
				long denominatorExclusionCount = resultObject.getDenominatorExclusionCount();
				long denominatorExceptionCount = resultObject.getDenominatorExceptionCount();

				try{

					if(numeratorCount != 0 || denominatorExceptionCount != 0 || denominatorExclusionCount!=0){

						reportingRate =  Double.valueOf(""+numeratorCount+denominatorExceptionCount+denominatorExclusionCount) / denominatorCount;
						reportingRate =  Double.valueOf(newFormat.format(reportingRate));

					}

					if(numeratorCount > 0){

						denominatorCount = denominatorCount - denominatorExclusionCount - denominatorExceptionCount;

						performanceRate =  Double.valueOf(""+numeratorCount) / denominatorCount;
						performanceRate =  Double.valueOf(newFormat.format(performanceRate));

					}

					if(reportingRate > 0 && reportingRate%10 != 0){
						score = (int) ((reportingRate / 10)+1);
					}else if(reportingRate > 0 && reportingRate%10 == 0){
						score = (int) (reportingRate / 10);
					}else{
						score = 0;
					}

					if((measureId.equals("ACI_HIE_1")) || (measureId.equals("ACI_PEA_1"))){
						points = score *2;
					}else{
						points = score;
					}
					
				}catch(Exception e){
					performanceRate = 0;
					reportingRate = 0;
				}
				
				resultObject.setReportingRate(reportingRate);
				resultObject.setPerformanceRate(performanceRate);
				resultObject.setCmsId(cmsIdNTitle.split("&&&")[0]);
				resultObject.setTitle(cmsIdNTitle.split("&&&")[1]);
				resultObject.setPoints(points);
				
			}
			
		}catch(Exception e){
			
			try{
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, -1,"Error occurred while calculating MIPS performance",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();

			}
			
		}
		
		return results;
		
	}
	
	/**
	 * Function to calculate measure performance  
	 * 
	 * @param providerId
	 * @param measureId
	 * @return
	 */
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<MIPSPerformanceBean> getPerformanceCount(int providerId, String measureId, String configuredMeasures, String accountId){
		
		List<MIPSPerformanceBean> results = new ArrayList<MIPSPerformanceBean>();
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		try{

			Calendar now = Calendar.getInstance();
			int reportingYear = now.get(Calendar.YEAR);

			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<MIPSPerformanceBean> cq = builder.createQuery(MIPSPerformanceBean.class);
			Root<QualityMeasuresPatientEntries> root = cq.from(QualityMeasuresPatientEntries.class);

			Predicate byProviderId = builder.equal(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesProviderId), providerId);
			Predicate byReportingYear = builder.equal(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesReportingYear), reportingYear);		
			Predicate byConfiguredMeasures = root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesMeasureId).in(Arrays.asList(configuredMeasures.split(",")));

			if(!measureId.equals("")){
				Predicate byMeasureId = builder.equal(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesMeasureId), measureId);
				cq.where(builder.and(byProviderId, byMeasureId, byReportingYear, byConfiguredMeasures));
			}else{
				cq.where(builder.and(byProviderId, byReportingYear, byConfiguredMeasures));
			}

			cq.groupBy(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesProviderId),
					root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesMeasureId),
					root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesCriteria),
					root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesReportingYear));

			Selection[] selections= new Selection[] {
					root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesMeasureId),
					root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesCriteria),
					root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesReportingYear),
					builder.coalesce(builder.sum(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesIpp)),0),
					builder.coalesce(builder.sum(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominator)),0),
					builder.coalesce(builder.sum(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominatorExclusion)),0),
					builder.coalesce(builder.sum(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesNumerator)),0),
					builder.coalesce(builder.sum(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesNumeratorExclusion)),0),
					builder.coalesce(builder.sum(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominatorException)),0)
			};

			cq.multiselect(selections);

			results = em.createQuery(cq).getResultList();

			for(int i=0;i<results.size();i++){

				MIPSPerformanceBean resultObject = results.get(i);

				measureId = resultObject.getMeasureId();

				String cmsIdNTitle = "";
				
				if(configuredMeasures.contains("ACI_EP_1")){
					
					if(measureId.equals("ACI_EP_1")){
						cmsIdNTitle = "ACI_EP_1&&&Electronic Prescribing";						
					}else if(measureId.equals("ACI_CCTPE_2")){
						cmsIdNTitle = "ACI_CCTPE_2&&&Secure Messaging";						
					}else if(measureId.equals("ACI_PEA_1")){
						cmsIdNTitle = "ACI_PEA_1&&&Patient Electronic Access";						
					}else if(measureId.equals("ACI_CCTPE_1")){
						cmsIdNTitle = "ACI_CCTPE_1&&&View, Download and Transmit (VDT)";						
					}else if(measureId.equals("ACI_HIE_1")){
						cmsIdNTitle = "ACI_HIE_1&&&Health Information Exchange";						
					}else if(measureId.equals("ACI_PEA_2")){
						cmsIdNTitle = "ACI_PEA_2&&&Patient-Specific Education";						
					}else if(measureId.equals("ACI_HIE_3")){
						cmsIdNTitle = "ACI_HIE_3&&&Medication Reconcilation";						
					}
					
				}else{
					cmsIdNTitle = getCMSIdAndTitle(measureId, accountId);
				}
				
				DecimalFormat newFormat = new DecimalFormat("#0.00");

				double performanceRate = 0;
				double reportingRate = 0;
				
				int score = 0;
				int points = 0;

				long numeratorCount = resultObject.getNumeratorCount();
				long denominatorCount = resultObject.getDenominatorCount();
				long denominatorExclusionCount = resultObject.getDenominatorExclusionCount();
				long denominatorExceptionCount = resultObject.getDenominatorExceptionCount();

				try{

					if(numeratorCount != 0 || denominatorExceptionCount != 0 || denominatorExclusionCount!=0){

						reportingRate =  Double.valueOf(""+numeratorCount+denominatorExceptionCount+denominatorExclusionCount) / denominatorCount;
						reportingRate =  Double.valueOf(newFormat.format(reportingRate));

					}

					if(numeratorCount > 0){

						denominatorCount = denominatorCount - denominatorExclusionCount - denominatorExceptionCount;

						performanceRate =  Double.valueOf(""+numeratorCount) / denominatorCount;
						performanceRate =  Double.valueOf(newFormat.format(performanceRate));

					}
					
					if(reportingRate > 0 && reportingRate%10 != 0){
						score = (int) ((reportingRate / 10)+1);
					}else if(reportingRate > 0 && reportingRate%10 == 0){
						score = (int) (reportingRate / 10);
					}else{
						score = 0;
					}

					if((measureId.equals("ACI_HIE_1")) || (measureId.equals("ACI_PEA_1"))){
						points = score *2;
					}else{
						points = score;
					}
					
				}catch(Exception e){
					performanceRate = 0;
					reportingRate = 0;
				}

				resultObject.setPoints(points);
				resultObject.setPerformanceRate(performanceRate);
				resultObject.setReportingRate(reportingRate);
				resultObject.setCmsId(cmsIdNTitle.split("&&&")[0]);
				resultObject.setTitle(cmsIdNTitle.split("&&&")[1]);
				resultObject.setIppPatientsList(getPatientListByCriteria(1, providerId, measureId));
				resultObject.setDenominatorPatientsList(getPatientListByCriteria(2, providerId, measureId));
				resultObject.setDenominatorExclusionPatientsList(getPatientListByCriteria(3, providerId, measureId));
				resultObject.setDenominatorExceptionPatientsList(getPatientListByCriteria(4, providerId, measureId));
				resultObject.setNumeratorPatientsList(getPatientListByCriteria(5, providerId, measureId));
				resultObject.setNumeratorExclusionPatientsList(getPatientListByCriteria(6, providerId, measureId));

			}

		}catch(Exception e){
			
			try{
				
				e.printStackTrace(printWriter);

				String responseMsg = GlaceMailer.buildMailContentFormat(accountId, -1,"Error occurred while calculating MIPS performance",writer.toString());
				
				GlaceMailer.sendFailureReport(responseMsg,accountId,GlaceMailer.Configure.MU);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
				
			}finally{

				printWriter.flush();
				printWriter.close();
				
				e.printStackTrace();

			}
			
		}
		
		return results;
		
	}
	
	private String getCMSIdAndTitle(String measureId, String accountId){

		String result = "";
		
		try{
			
			String sharedFolderPath = sharedFolderBean.getSharedFolderPath().get(accountId).toString();
			
			String jsonFolder = sharedFolderPath+File.separator+"ECQM"+File.separator;

			File file = new File(jsonFolder+measureId+".json");

			String apiUrl = "http://hub-icd10.glaceemr.com/DataGateway/eCQMServices/getECQMInfoById?ids="+measureId;
			
			if(file.exists()){
				
				String jsonContent = FileUtils.readFileToString(file);
				
				JSONArray jsonArray = new JSONArray(jsonContent);
				
				result = jsonArray.getJSONObject(0).getString("cmsId");
                result = result.concat("&&&".concat(jsonArray.getJSONObject(0).getString("title")));
				
			}else{

				JSONArray measureDetailsArray=new JSONArray(restTemplate.getForObject(apiUrl, String.class));
				
				for(int i=0;i<measureDetailsArray.length();i++){
	                
	                result = measureDetailsArray.getJSONObject(i).getString("cmsId");
	                result = result.concat("&&&".concat(measureDetailsArray.getJSONObject(i).getString("title")));
	                
	            }
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	private String getPatientListByCriteria(int criteriaId, int providerId, String measureId){
		
		String patientsList = "";
		
		Calendar now = Calendar.getInstance();
		int reportingYear = now.get(Calendar.YEAR);
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = builder.createQuery(String.class);
		Root<QualityMeasuresPatientEntries> root = cq.from(QualityMeasuresPatientEntries.class);

		Predicate condition = null;
		
		if(criteriaId == 1){
			condition = builder.greaterThanOrEqualTo(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesIpp), 1);
		}else if(criteriaId == 2){
			condition = builder.greaterThanOrEqualTo(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominator), 1);
		}else if(criteriaId == 3){
			condition = builder.greaterThanOrEqualTo(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominatorExclusion), 1);
		}else if(criteriaId == 4){
			condition = builder.greaterThanOrEqualTo(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominatorException), 1);
		}else if(criteriaId == 5){
			condition = builder.greaterThanOrEqualTo(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesNumerator), 1);
		}else if(criteriaId == 6){
			condition = builder.greaterThanOrEqualTo(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesNumeratorExclusion), 1);
		}
		
		Predicate byProviderId = builder.equal(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesProviderId), providerId);
		Predicate byReportingYear = builder.equal(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesReportingYear), reportingYear);		
		
		if(!measureId.equals("")){
			Predicate byMeasureId = builder.equal(root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesMeasureId), measureId);
			cq.where(builder.and(byProviderId, byMeasureId, byReportingYear, condition));
		}else{
			cq.where(builder.and(byProviderId, byReportingYear, condition));
		}
		
		cq.select(builder.coalesce(builder.function("string_agg", String.class, root.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesPatientId).as(String.class),builder.literal(",")),""));

		patientsList = em.createQuery(cq).getResultList().get(0); 
		
		return patientsList;
		
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public List<MIPSPatientInformation> getPatientInformation(String patientsList){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<MIPSPatientInformation> cq = builder.createQuery(MIPSPatientInformation.class);
		Root<PatientRegistration> root = cq.from(PatientRegistration.class);
		
		Selection[] selections= new Selection[] {
				root.get(PatientRegistration_.patientRegistrationId).alias("patientId"),
				root.get(PatientRegistration_.patientRegistrationAccountno).alias("accountNo"),
				root.get(PatientRegistration_.patientRegistrationLastName).alias("lastName"),
				root.get(PatientRegistration_.patientRegistrationFirstName).alias("firstName"),
				builder.function("to_mmddyyyy",Date.class,root.get(PatientRegistration_.patientRegistrationDob)),
				builder.selectCase().when(builder.equal(root.get(PatientRegistration_.patientRegistrationSex),1),"Male").when(builder.equal(root.get(PatientRegistration_.patientRegistrationSex),2),"Female").otherwise("TG").as(String.class).alias("gender"),
				root.get(PatientRegistration_.patientRegistrationRace).alias("race"),
				root.get(PatientRegistration_.patientRegistrationEthnicity).alias("ethnicity"),
				builder.coalesce(root.get(PatientRegistration_.patientRegistrationAddress1), "").alias("address1"),
				builder.coalesce(root.get(PatientRegistration_.patientRegistrationAddress2), "").alias("address2"),
				builder.coalesce(root.get(PatientRegistration_.patientRegistrationCity), "-").alias("city"),
				builder.coalesce(root.get(PatientRegistration_.patientRegistrationStateName), "-").alias("state"),
				builder.coalesce(root.get(PatientRegistration_.patientRegistrationZip), "-").alias("zip"),
		};
		
		cq.multiselect(selections);
		
		Predicate byPatientId = root.get(PatientRegistration_.patientRegistrationId).in(Arrays.asList(patientsList.split(",")));
		
		cq.where(byPatientId);
		
		cq.orderBy(builder.asc(root.get(PatientRegistration_.patientRegistrationLastName)));
		
		List<MIPSPatientInformation> patientDetails = em.createQuery(cq).getResultList();
		
		return patientDetails;
		
	}
	
	@SuppressWarnings("unused")
	@Override
	public HashMap<String,Object> generateFilterContents(){
		
		HashMap<String,Object> filterDetails=new HashMap<String,Object>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<Billinglookup> rootBillinglookup = cq.from(Billinglookup.class);
		cq.select(rootBillinglookup.get(Billinglookup_.blookName));
		Predicate forRace=builder.equal(rootBillinglookup.get(Billinglookup_.blookGroup), 250);
		cq.where(forRace);
		List<Object> result=em.createQuery(cq).getResultList();
		filterDetails.put("race", result);

		CriteriaBuilder builder1 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq1 = builder1.createQuery();
		Root<Billinglookup> rootBillinglookup1 = cq1.from(Billinglookup.class);
		cq1.select(rootBillinglookup1.get(Billinglookup_.blookName));
		Predicate forEthnicity=builder1.equal(rootBillinglookup1.get(Billinglookup_.blookGroup), 251);
		cq1.where(forEthnicity);
		List<Object> result1=em.createQuery(cq1).getResultList();
		filterDetails.put("ethnicity", result1);

		CriteriaBuilder builder2 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq2 = builder2.createQuery();
		Root<H478> rooth478 = cq2.from(H478.class);
		Join<H478,MacraProviderConfiguration> joinh478MacraProviderConfiguration=rooth478.join(H478_.macraProviderConfiguration,JoinType.INNER);
		cq2.select(rooth478.get(H478_.h478006));
		List<Object> result2=em.createQuery(cq2).getResultList();
		filterDetails.put("npi", result2);

		CriteriaBuilder builder3 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq3 = builder3.createQuery();
		Root<EmployeeProfile> rootemployeeProfile = cq3.from(EmployeeProfile.class);
		Join<EmployeeProfile,MacraProviderConfiguration> joinEmployeeProfileMacraProviderConfiguration=rootemployeeProfile.join(EmployeeProfile_.macraProviderConfiguration,JoinType.INNER);
		cq3.select(rootemployeeProfile.get(EmployeeProfile_.empProfileSsn));
		Predicate byIsActive=builder3.equal(rootemployeeProfile.get(EmployeeProfile_.empProfileIsActive),true);
		cq3.where(byIsActive);
		List<Object> result3=em.createQuery(cq3).getResultList();
		filterDetails.put("ssn", result3);

		CriteriaBuilder builder4 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq4 = builder4.createQuery();
		Root<InsCompany> rootinsCompany = cq4.from(InsCompany.class);
		cq4.multiselect(rootinsCompany.get(InsCompany_.insCompanyName),rootinsCompany.get(InsCompany_.insCompanyId));
		cq4.distinct(true);
		List<Object> result4=em.createQuery(cq4).getResultList();
		filterDetails.put("insurance", result4);

		return filterDetails;
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<MIPSPatientInformation> getPatient(String patientId, String measureId, int criteria,Integer provider, String empTin, int mode) {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<MIPSPatientInformation> cq = builder.createQuery(MIPSPatientInformation.class);
		Root<PatientRegistration> root = cq.from(PatientRegistration.class);
		Join<PatientRegistration,QualityMeasuresPatientEntries> joinQualityMeasuresPatientEntries=root.join(PatientRegistration_.qualityMeasuresPatientEntries,JoinType.INNER);
		
		String npiValue = getNPIForProvider(provider);
		
		Predicate byPatientId = root.get(PatientRegistration_.patientRegistrationId).in(Arrays.asList(patientId.split(",")));
		Predicate byMeasureId = builder.equal(joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesMeasureId), measureId);
		Predicate byCriteria = builder.equal(joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesCriteria), criteria);
		Predicate byProvider = builder.equal(joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesProviderId), provider);
		Predicate byNpi = builder.equal(joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesNpi), npiValue);
		Predicate byEmpTin=builder.equal(joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesTin), empTin);
		
		if(mode == 0){
			joinQualityMeasuresPatientEntries.on(byMeasureId, byCriteria,byNpi);
		}else if(mode == 1){
			joinQualityMeasuresPatientEntries.on(byMeasureId, byCriteria,byProvider);
		}else{
			joinQualityMeasuresPatientEntries.on(byMeasureId, byCriteria, byEmpTin);
		}
		
		Selection[] selections= new Selection[] {
				root.get(PatientRegistration_.patientRegistrationId),
				root.get(PatientRegistration_.patientRegistrationAccountno),
				root.get(PatientRegistration_.patientRegistrationLastName),
				root.get(PatientRegistration_.patientRegistrationFirstName),
				builder.function("to_mmddyyyy",Date.class,root.get(PatientRegistration_.patientRegistrationDob)),
				builder.selectCase().when(builder.equal(root.get(PatientRegistration_.patientRegistrationSex),1),"Male").when(builder.equal(root.get(PatientRegistration_.patientRegistrationSex),2),"Female").otherwise("TG").as(String.class),
				root.get(PatientRegistration_.patientRegistrationRace),
				root.get(PatientRegistration_.patientRegistrationEthnicity),
				builder.coalesce(root.get(PatientRegistration_.patientRegistrationAddress1), ""),
				builder.coalesce(root.get(PatientRegistration_.patientRegistrationAddress2), ""),
				builder.coalesce(root.get(PatientRegistration_.patientRegistrationCity), "-"),
				builder.coalesce(root.get(PatientRegistration_.patientRegistrationStateName), "-"),
				builder.coalesce(root.get(PatientRegistration_.patientRegistrationZip), "-"),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesIpp),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominator),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominatorExclusion),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesNumerator),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesNumeratorExclusion),
				joinQualityMeasuresPatientEntries.get(QualityMeasuresPatientEntries_.qualityMeasuresPatientEntriesDenominatorException)
		
		};
		cq.where(byPatientId);
		cq.distinct(true);
		cq.orderBy(builder.asc(root.get(PatientRegistration_.patientRegistrationLastName)),builder.asc(root.get(PatientRegistration_.patientRegistrationFirstName)));
		cq.select(builder.construct(MIPSPatientInformation.class,selections));
		List<MIPSPatientInformation> patientDetailsWithResults = em.createQuery(cq).getResultList();
		return patientDetailsWithResults;
		
	}
	
}