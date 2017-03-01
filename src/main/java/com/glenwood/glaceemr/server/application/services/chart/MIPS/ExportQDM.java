package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.data.jpa.domain.Specifications;

import com.glenwood.glaceemr.server.application.Bean.ClinicalDataQDM;
import com.glenwood.glaceemr.server.application.Bean.EncounterQDM;
import com.glenwood.glaceemr.server.application.Bean.ImmunizationQDM;
import com.glenwood.glaceemr.server.application.Bean.InvestigationQDM;
import com.glenwood.glaceemr.server.application.Bean.MedicationOrderQDM;
import com.glenwood.glaceemr.server.application.Bean.MedicationQDM;
import com.glenwood.glaceemr.server.application.Bean.ReferralQDM;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.ActiveMedication;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Communication;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Diagnosis;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Immunization;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Intervention;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.MedicationOrder;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Negation;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Patient;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Procedure;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.QDM;
import com.glenwood.glaceemr.server.application.models.CNMCodeSystem;
import com.glenwood.glaceemr.server.application.models.CNMCodeSystem_;
import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.ClinicalElements;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions_;
import com.glenwood.glaceemr.server.application.models.ClinicalElements_;
import com.glenwood.glaceemr.server.application.models.Cpt;
import com.glenwood.glaceemr.server.application.models.Cpt_;
import com.glenwood.glaceemr.server.application.models.CurrentMedication;
import com.glenwood.glaceemr.server.application.models.CurrentMedication_;
import com.glenwood.glaceemr.server.application.models.CvxVaccineGroupMapping;
import com.glenwood.glaceemr.server.application.models.CvxVaccineGroupMapping_;
import com.glenwood.glaceemr.server.application.models.DirectEmailLog;
import com.glenwood.glaceemr.server.application.models.DirectEmailLog_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Encounter_;
import com.glenwood.glaceemr.server.application.models.H413;
import com.glenwood.glaceemr.server.application.models.H413_;
import com.glenwood.glaceemr.server.application.models.H809;
import com.glenwood.glaceemr.server.application.models.H809_;
import com.glenwood.glaceemr.server.application.models.Hl7ExternalTest;
import com.glenwood.glaceemr.server.application.models.Hl7ExternalTest_;
import com.glenwood.glaceemr.server.application.models.Hl7ExternalTestmapping;
import com.glenwood.glaceemr.server.application.models.Hl7ExternalTestmapping_;
import com.glenwood.glaceemr.server.application.models.LabDescription;
import com.glenwood.glaceemr.server.application.models.LabDescription_;
import com.glenwood.glaceemr.server.application.models.LabEntries;
import com.glenwood.glaceemr.server.application.models.LabEntriesParameter;
import com.glenwood.glaceemr.server.application.models.LabEntriesParameter_;
import com.glenwood.glaceemr.server.application.models.LabEntries_;
import com.glenwood.glaceemr.server.application.models.LabParameterCode;
import com.glenwood.glaceemr.server.application.models.LabParameterCode_;
import com.glenwood.glaceemr.server.application.models.LabParameters;
import com.glenwood.glaceemr.server.application.models.LabParameters_;
import com.glenwood.glaceemr.server.application.models.MedStatus;
import com.glenwood.glaceemr.server.application.models.MedStatus_;
import com.glenwood.glaceemr.server.application.models.NdcPkgProduct;
import com.glenwood.glaceemr.server.application.models.NdcPkgProduct_;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements_;
import com.glenwood.glaceemr.server.application.models.PatientClinicalHistory;
import com.glenwood.glaceemr.server.application.models.PatientClinicalHistory_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PortalMessage;
import com.glenwood.glaceemr.server.application.models.PortalMessage_;
import com.glenwood.glaceemr.server.application.models.Prescription;
import com.glenwood.glaceemr.server.application.models.Prescription_;
import com.glenwood.glaceemr.server.application.models.ProblemList;
import com.glenwood.glaceemr.server.application.models.ReconciledMedication;
import com.glenwood.glaceemr.server.application.models.ReconciledMedication_;
import com.glenwood.glaceemr.server.application.models.ServiceDetail;
import com.glenwood.glaceemr.server.application.models.ServiceDetail_;
import com.glenwood.glaceemr.server.application.models.VaccineReport;
import com.glenwood.glaceemr.server.application.models.VaccineReport_;
import com.glenwood.glaceemr.server.application.models.XrefGenproductSynRxnorm;
import com.glenwood.glaceemr.server.application.models.XrefGenproductSynRxnorm_;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.repositories.ProblemListRepository;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.CVXVaccineGroupMappingBean;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.ImmunizationDetailsBean;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.VaccReportBean;
import com.glenwood.glaceemr.server.application.specifications.ProblemListSpecification;

public class ExportQDM {

	Patient patientObj = new Patient();
	
	public Patient getRequestQDM(PatientRegistrationRepository patientRepo, ProblemListRepository diagnosisRepo, int patientId, int providerId){
		
		getPatientInfoQDM(patientRepo, patientId);
		
		return patientObj;
		
	}
	
	public List<Procedure> getProcBasedOnCPT(EntityManager em,int patientID, int providerId,List<String> cptCodes){
		if(cptCodes.size()==0){
			cptCodes.add("000000");}
		else if(cptCodes.toString()=="[]")
			cptCodes.add("000000");
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<EncounterQDM> cq = builder.createQuery(EncounterQDM.class);
Root<Encounter> root = cq.from(Encounter.class);
		
		Join<Encounter, Chart> encounterChartJoin = root.join(Encounter_.chart,JoinType.INNER);
		Join<Chart, ServiceDetail> chartServiceJoin = encounterChartJoin.join(Chart_.serviceDetail,JoinType.INNER);
		Join<ServiceDetail, Cpt> serviceCptJoin = chartServiceJoin.join(ServiceDetail_.cpt,JoinType.INNER);
		
		@SuppressWarnings("rawtypes")
		Selection[] selections= new Selection[] {
				serviceCptJoin.get(Cpt_.cptCptcode),
				root.get(Encounter_.encounterCreatedDate),
				root.get(Encounter_.encounterClosedDate),
		};
		cq.select(builder.construct(EncounterQDM.class,selections));
		Predicate[] restrictions= new Predicate[] {
				builder.equal(encounterChartJoin.get(Chart_.chartPatientid), patientID),
				serviceCptJoin.get(Cpt_.cptCptcode).in(cptCodes),
				builder.equal(builder.function("DATE", Date.class, root.get(Encounter_.encounterCreatedDate)), builder.function("DATE", Date.class, chartServiceJoin.get(ServiceDetail_.serviceDetailDos))),
		};
		cq.where(restrictions);
		List<EncounterQDM> encounterObj = new ArrayList<EncounterQDM>();
		List<Procedure> procedureList = new ArrayList<Procedure>();
		encounterObj = em.createQuery(cq).getResultList();
		Procedure procedureObj;
		for(int i=0;i<encounterObj.size();i++){
			
			procedureObj = new Procedure();
			procedureObj.setCode(encounterObj.get(i).getCode());
			procedureObj.setCodeSystemOID("2.16.840.1.113883.6.12");
			procedureObj.setStartDate(encounterObj.get(i).getStartDate());
			procedureObj.setEndDate(encounterObj.get(i).getEndDate());
			procedureObj.setStatus(2);
			procedureList.add(i, procedureObj);
		}
		return procedureList;
	}
	
	@SuppressWarnings("rawtypes")
	public List<com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Encounter> getEncounterQDM(EntityManager em, boolean considerProvider,int patientID, int providerId, HashMap<String, String> encounterCodeList){
		
		List<String> cptCodes = new ArrayList<String>(Arrays.asList(encounterCodeList.get("CPT").toString().split(",")));
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<EncounterQDM> cq = builder.createQuery(EncounterQDM.class);
		
		Root<Encounter> root = cq.from(Encounter.class);
		
		Join<Encounter, Chart> encounterChartJoin = root.join(Encounter_.chart,JoinType.INNER);
		Join<Chart, ServiceDetail> chartServiceJoin = encounterChartJoin.join(Chart_.serviceDetail,JoinType.INNER);
		Join<ServiceDetail, Cpt> serviceCptJoin = chartServiceJoin.join(ServiceDetail_.cpt,JoinType.INNER);
		
		Selection[] selections= new Selection[] {
				serviceCptJoin.get(Cpt_.cptCptcode),
				root.get(Encounter_.encounterDate),
				root.get(Encounter_.encounterClosedDate),
		};
		
		cq.select(builder.construct(EncounterQDM.class,selections));
		
		Predicate[] restrictions = null;
		
		if(considerProvider){
			
			restrictions = new Predicate[] {
					builder.equal(encounterChartJoin.get(Chart_.chartPatientid), patientID),
					serviceCptJoin.get(Cpt_.cptCptcode).in(cptCodes),
					builder.equal(chartServiceJoin.get(ServiceDetail_.sdoctors), providerId),
					builder.equal(builder.function("DATE", Date.class, root.get(Encounter_.encounterDate)),builder.function("DATE", Date.class,chartServiceJoin.get(ServiceDetail_.serviceDetailDos))),
			};
			
		}else{
			
			restrictions = new Predicate[] {
					builder.equal(encounterChartJoin.get(Chart_.chartPatientid), patientID),
					serviceCptJoin.get(Cpt_.cptCptcode).in(cptCodes),
					builder.equal(builder.function("DATE", Date.class, root.get(Encounter_.encounterDate)),builder.function("DATE", Date.class,chartServiceJoin.get(ServiceDetail_.serviceDetailDos))),
			};
			
		}
		
		cq.where(restrictions);
		
		CriteriaBuilder builder1 = em.getCriteriaBuilder();
		CriteriaQuery<EncounterQDM> cq1 = builder1.createQuery(EncounterQDM.class);
		Root<Encounter> encounterRoot = cq1.from(Encounter.class);
		Join<Encounter, Chart> encounterChartJoin1 = encounterRoot.join(Encounter_.chart,JoinType.INNER);
		Predicate prediByPatientId=builder.equal(encounterChartJoin1.get(Chart_.chartPatientid), patientID);
		encounterChartJoin1.on(prediByPatientId);
		cq1.orderBy(builder1.desc(encounterRoot.get(Encounter_.encounterId)));
		Selection[] selections1= new Selection[] {
				encounterRoot.get(Encounter_.encounterDate),
				encounterRoot.get(Encounter_.encounterClosedDate),
		};
		cq1.select(builder1.construct(EncounterQDM.class,selections1));
		List<EncounterQDM> encounterObj = new ArrayList<EncounterQDM>();
		List<EncounterQDM> encounterObj1 = new ArrayList<EncounterQDM>();
		
		List<com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Encounter> encounterQDM = new ArrayList<com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Encounter>();
		
		try{
			
			encounterObj = em.createQuery(cq).getResultList();
			encounterObj1= em.createQuery(cq1).setMaxResults(1).getResultList();
			Boolean cptThere=false;
			
			com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Encounter encObject;
			
			for(int i=0;i<encounterObj.size();i++){
				
				if(encounterObj.get(i).getCode().equals("99213")){
					cptThere=true;
				}
				
				encObject = new com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Encounter();
				
				encObject.setCode(encounterObj.get(i).getCode());
				encObject.setCodeSystemOID("2.16.840.1.113883.6.12");
				encObject.setStartDate(encounterObj.get(i).getStartDate());
				encObject.setEndDate(encounterObj.get(i).getEndDate());
				
				encounterQDM.add(i, encObject);
				
			}
			
			if(cptThere==false){
				
				encObject = new com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Encounter();
				encObject.setCode("99213");
				encObject.setCodeSystemOID("2.16.840.1.113883.6.12");
				encObject.setStartDate(encounterObj1.get(0).getStartDate());
				encounterQDM.add(encObject);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return encounterQDM;
		
	}
	
	public List<Diagnosis> getPatientDiagnosisQDM(ProblemListRepository diagnosisRepo, int patientId) {
		
		List<Diagnosis> assessmentQDM = new ArrayList<Diagnosis>();
		
		List<ProblemList> diagnosisObj = diagnosisRepo.findAll(Specifications.where(ProblemListSpecification.getAllproblemlist(patientId)));
		
		for(int i=0;i<diagnosisObj.size();i++){
			
			Diagnosis assessmentObj = new Diagnosis();
			
			assessmentObj.setCode(diagnosisObj.get(i).getProblemListDxCode());
			assessmentObj.setCodeSystemOID(diagnosisObj.get(i).getProblemListCodingSystemid());
			assessmentObj.setDescription(diagnosisObj.get(i).getProblemListDxDescp());

			if(diagnosisObj.get(i).getProblemListCodingSystemid().equals("2.16.840.1.113883.6.90")){
				assessmentObj.setCodeSystem("ICD10CM");
			}else if(diagnosisObj.get(i).getProblemListCodingSystemid().equals("2.16.840.1.113883.6.96")){
				assessmentObj.setCodeSystem("SNOMEDCT");
			}else if(diagnosisObj.get(i).getProblemListCodingSystemid().equals("2.16.840.1.113883.6.103")){
				assessmentObj.setCodeSystem("ICD9CM");
			}
			
			Calendar c = Calendar.getInstance();
			if(diagnosisObj.get(i).getProblemListCreatedon()!=null)
			c.setTimeInMillis(diagnosisObj.get(i).getProblemListCreatedon().getTime());
			
			if(diagnosisObj.get(i).getProblemListOnsetDate()==null){
				assessmentObj.setStartDate(c.getTime());
			}else{
				assessmentObj.setStartDate(diagnosisObj.get(i).getProblemListOnsetDate());
			}
			
			if(diagnosisObj.get(i).getProblemListResolvedDate()!=null){
				assessmentObj.setEndDate(diagnosisObj.get(i).getProblemListResolvedDate());
			}
			
			assessmentQDM.add(i, assessmentObj);
						
		}
		
		return assessmentQDM;
		
	}

	
	
	public List<MedicationOrder> getMedicationQDM(EntityManager em,String rxNormCodes,int patientId, int range) {
	       
        String [] codes=rxNormCodes.split(",");
        
        List<String> codeList=new ArrayList<String>();
        for(int i=0;i<codes.length;i++){
            codeList.add(codes[i]);
        }
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object> cq = builder.createQuery();
        
        Root<Prescription> root = cq.from(Prescription.class);
        
        Join<Prescription, NdcPkgProduct> prescNdcJoin=root.join(Prescription_.ndcPkgProduct,JoinType.INNER);
        Join<NdcPkgProduct, XrefGenproductSynRxnorm> ndcNormJoin=prescNdcJoin.join(NdcPkgProduct_.xrefGenproductSynRxnorm,JoinType.INNER);
        Join<Prescription, MedStatus> prescStatusJoin=root.join(Prescription_.medstatus,JoinType.INNER);
        ndcNormJoin.on(ndcNormJoin.get(XrefGenproductSynRxnorm_.rxcui).in(codeList));
        
        Predicate patId=builder.equal(root.get(Prescription_.docPrescPatientId), patientId);
        Predicate status=root.get(Prescription_.docPrescStatus).in(1,2);
       
        cq.select(builder.construct(MedicationQDM.class, root.get(Prescription_.rxname),
                root.get(Prescription_.rxstrength),
                root.get(Prescription_.rxform),
                root.get(Prescription_.docPrescRoute),
                prescStatusJoin.get(MedStatus_.medStatusName),
                root.get(Prescription_.rxfreq),
                ndcNormJoin.get(XrefGenproductSynRxnorm_.rxcui),
                builder.selectCase().when(builder.notEqual(builder.coalesce(root.get(Prescription_.noofrefills), ""), ""), root.get(Prescription_.noofrefills)).otherwise("0"),
                builder.selectCase().when(builder.notEqual(builder.coalesce(root.get(Prescription_.noofdays), ""), ""), root.get(Prescription_.noofdays)).otherwise("0"),
                root.get(Prescription_.docPrescOrderedDate),
                builder.selectCase().when(root.get(Prescription_.docPrescStartDate).isNotNull(), root.get(Prescription_.docPrescStartDate)).otherwise(root.get(Prescription_.docPrescOrderedDate))));
       
        if(range!=0){
            range=range* (-1);
            Calendar cal = Calendar.getInstance();
            Date endDate = cal.getTime();
            cal.add(Calendar.YEAR, range);
            Date startDate = cal.getTime();
            Predicate dateRange=builder.between(root.get(Prescription_.docPrescOrderedDate),startDate , endDate);
            cq.where(builder.and(patId,status,dateRange));
        }
        else{
            cq.where(builder.and(patId,status));
        }
        
        cq.distinct(true);
        
        List<Object> result=em.createQuery(cq).getResultList();
        
        List<MedicationOrder> medicationObj = new ArrayList<MedicationOrder>();
        
        for(int i=0;i<result.size();i++){
        	
        	MedicationOrder eachMedObj = new MedicationOrder();
        	MedicationQDM eachData=(MedicationQDM) result.get(i);
            
        	int cmd=(Integer.parseInt(eachData.getDays()) * (Integer.parseInt(eachData.getRefills())+1));
            eachData.setCMD(cmd);
            
            eachMedObj.setCode(eachData.getCode());
            eachMedObj.setCodeSystem("RXNORM");
            eachMedObj.setCodeSystemOID("2.16.840.1.113883.6.88");
            eachMedObj.setDescription(eachData.getDescription());
            eachMedObj.setStartDate(eachData.getStartDate());
            
            eachMedObj.setCMD(cmd);
            eachMedObj.setDose(eachData.getDose());
            eachMedObj.setFrequency(eachData.getFrequency());
            eachMedObj.setRefills(Integer.parseInt(eachData.getRefills()));
            eachMedObj.setRoute(eachData.getRoute());
            eachMedObj.setOrderDate(eachData.getOrderDate());
            
            medicationObj.add(eachMedObj);
            
        }
        
        return medicationObj;
        
    }
	
	public List<ActiveMedication> getActiveMedications(EntityManager em,String rxNormCodes,int patientId, int range) {

		String [] codes=rxNormCodes.split(",");

		List<String> codeList=new ArrayList<String>();
		for(int i=0;i<codes.length;i++){
			codeList.add(codes[i]);
		}

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();

		Root<Prescription> root = cq.from(Prescription.class);

		Join<Prescription, NdcPkgProduct> prescNdcJoin=root.join(Prescription_.ndcPkgProduct,JoinType.INNER);
		Join<NdcPkgProduct, XrefGenproductSynRxnorm> ndcNormJoin=prescNdcJoin.join(NdcPkgProduct_.xrefGenproductSynRxnorm,JoinType.INNER);
		Join<Prescription, MedStatus> prescStatusJoin=root.join(Prescription_.medstatus,JoinType.INNER);
		ndcNormJoin.on(ndcNormJoin.get(XrefGenproductSynRxnorm_.rxcui).in(codeList));

		Predicate patId=builder.equal(root.get(Prescription_.docPrescPatientId), patientId);
		Predicate status=root.get(Prescription_.docPrescStatus).in(1,2);
		Predicate activeMed=builder.equal(root.get(Prescription_.docPrescIsActive), true);
		cq.select(builder.construct(MedicationQDM.class, root.get(Prescription_.rxname),
				root.get(Prescription_.rxstrength),
				root.get(Prescription_.rxform),
				root.get(Prescription_.docPrescRoute),
				prescStatusJoin.get(MedStatus_.medStatusName),
				root.get(Prescription_.rxfreq),
				ndcNormJoin.get(XrefGenproductSynRxnorm_.rxcui),
				builder.selectCase().when(builder.notEqual(builder.coalesce(root.get(Prescription_.noofrefills), ""), ""), root.get(Prescription_.noofrefills)).otherwise("0"),
				builder.selectCase().when(builder.notEqual(builder.coalesce(root.get(Prescription_.noofdays), ""), ""), root.get(Prescription_.noofdays)).otherwise("0"),
				root.get(Prescription_.docPrescOrderedDate),
				builder.selectCase().when(root.get(Prescription_.docPrescStartDate).isNotNull(), root.get(Prescription_.docPrescStartDate)).otherwise(root.get(Prescription_.docPrescOrderedDate))));

		if(range!=0){
			range=range* (-1);
			Calendar cal = Calendar.getInstance();
			Date endDate = cal.getTime();
			cal.add(Calendar.YEAR, range);
			Date startDate = cal.getTime();
			Predicate dateRange=builder.between(root.get(Prescription_.docPrescOrderedDate),startDate , endDate);
			cq.where(builder.and(patId,status,dateRange,activeMed));
		}
		else{
			cq.where(builder.and(patId,status,activeMed));	
		}

		cq.distinct(true);

		List<Object> result=em.createQuery(cq).getResultList();

		List<ActiveMedication> medicationObj = new ArrayList<ActiveMedication>();

		for(int i=0;i<result.size();i++){

			ActiveMedication eachMedObj = new ActiveMedication();
			MedicationQDM eachData=(MedicationQDM) result.get(i);

			int cmd=(Integer.parseInt(eachData.getDays()) * (Integer.parseInt(eachData.getRefills())+1));
			eachData.setCMD(cmd);

			eachMedObj.setCode(eachData.getCode());
			eachMedObj.setCodeSystem("RXNORM");
			eachMedObj.setCodeSystemOID("2.16.840.1.113883.6.88");
			eachMedObj.setDescription(eachData.getDescription());
			eachMedObj.setStartDate(eachData.getStartDate());

			eachMedObj.setCMD(cmd);
			eachMedObj.setDose(eachData.getDose());
			eachMedObj.setFrequency(eachData.getFrequency());
			eachMedObj.setRefills(Integer.parseInt(eachData.getRefills()));
			eachMedObj.setRoute(eachData.getRoute());

			medicationObj.add(eachMedObj);

		}

		return medicationObj;

	}
	
	@SuppressWarnings("rawtypes")
	public List<InvestigationQDM> getInvestigationQDM(EntityManager em, int patientID, int providerId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<InvestigationQDM> cq = builder.createQuery(InvestigationQDM.class);
		
		Root<LabEntries> root = cq.from(LabEntries.class);
		
		Join<LabEntries, Encounter> EncLabJoin = root.join(LabEntries_.encounter, JoinType.INNER);
		Join<Encounter, Chart> EncChartJoin = EncLabJoin.join(Encounter_.chartTable, JoinType.INNER);
		Join<LabEntries, LabDescription> labEntriesDescJoin = root.join(LabEntries_.labDescriptionTable, JoinType.INNER);
		Join<LabDescription, Hl7ExternalTestmapping> labDescExtrnTestMappingJoin = labEntriesDescJoin.join(LabDescription_.hl7ExternalTestmappingTable, JoinType.INNER);
		Join<Hl7ExternalTestmapping, Hl7ExternalTest> hl7ExtTestMappingJoin = labDescExtrnTestMappingJoin.join(Hl7ExternalTestmapping_.hl7ExternalTestTable, JoinType.INNER);
		
		Selection[] selections= new Selection[] {
				hl7ExtTestMappingJoin.get(Hl7ExternalTest_.hl7ExternalTestCode),
				labEntriesDescJoin.get(LabDescription_.labDescriptionTestDesc),
				root.get(LabEntries_.labEntriesTestStatus),
				hl7ExtTestMappingJoin.get(Hl7ExternalTest_.hl7ExternalTestLabcompanyid),
				root.get(LabEntries_.labEntriesOrdOn),
				root.get(LabEntries_.labEntriesPerfOn),
		};
		
		cq.select(builder.construct(InvestigationQDM.class,selections));
		
		Predicate[] restrictions = new Predicate[] {
				builder.equal(EncChartJoin.get(Chart_.chartPatientid), patientID),
				builder.notEqual(root.get(LabEntries_.labEntriesTestStatus), 2),
				builder.notEqual(root.get(LabEntries_.labEntriesTestStatus), 7),
				builder.equal(hl7ExtTestMappingJoin.get(Hl7ExternalTest_.hl7ExternalTestIsactive), true),
				hl7ExtTestMappingJoin.get(Hl7ExternalTest_.hl7ExternalTestLabcompanyid).in(54, 51),
		};
		
		cq.where(restrictions);
		
		List<InvestigationQDM> procedureForLabs = new ArrayList<InvestigationQDM>();

		try{
			
			procedureForLabs = em.createQuery(cq).getResultList();
			
		}catch(Exception e){
			e.printStackTrace();			
		}
		return procedureForLabs;
		
	}
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Chart> getLabDetails(EntityManager em,final Integer patientId) throws Exception {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<Chart> rootChart = cq.from(Chart.class);
		
		Join<Chart,LabEntriesParameter> joinChartLabentriesparameter=rootChart.join(Chart_.labEntriesParameter,JoinType.INNER);
		Join<LabEntriesParameter,LabParameters> joinLabentriesparamLabparam=joinChartLabentriesparameter.join(LabEntriesParameter_.labParametersTable,JoinType.INNER);
		Join<LabParameters,LabParameterCode> joinLabparametersLabparametercode=joinLabentriesparamLabparam.join(LabParameters_.labParamCode,JoinType.INNER);
		
		Predicate predicateByPatientId=builder.equal(rootChart.get(Chart_.chartPatientid),patientId);
		joinChartLabentriesparameter.on(predicateByPatientId);
		
		Predicate[] restrictions = new Predicate[] {
				builder.equal(joinChartLabentriesparameter.get(LabEntriesParameter_.labEntriesParameterIsactive),true),
				builder.equal(joinLabentriesparamLabparam.get(LabParameters_.labParametersIsactive),true)
		};

		Predicate loincPredicate=builder.like((builder.upper(joinLabparametersLabparametercode.get(LabParameterCode_.labParameterCodeSystem))),builder.literal("LOINC"));
		joinLabparametersLabparametercode.on(loincPredicate);
		Selection[] selections= new Selection[] {
				joinLabparametersLabparametercode.get(LabParameterCode_.labParameterCodeValue).alias("Code"),
				joinLabentriesparamLabparam.get(LabParameters_.labParametersName).alias("Description"),
				joinChartLabentriesparameter.get(LabEntriesParameter_.labEntriesParameterDate).alias("Start Date"),
				builder.literal("2").alias("Status"),
				joinChartLabentriesparameter.get(LabEntriesParameter_.labEntriesParameterValue).alias("Result Value")
		};
		cq.multiselect(selections);
		cq.where(restrictions);
		Query query=em.createQuery(cq);
		List<Chart> alertsResultList=query.getResultList();
		return alertsResultList;
		
	}
	
	public List<Intervention> setReferrals(List<ReferralQDM> obj){
		
		List<Intervention> interventionList=new ArrayList<Intervention>();
		
		for(int i=0;i<obj.size();i++){
			
			Intervention interventionObj=new Intervention();
			
			interventionObj.setCode("306253008");
			interventionObj.setCodeSystem("Referral to doctor (procedure)");
			interventionObj.setCodeSystemOID("2.16.840.1.113883.6.96");
			interventionObj.setStartDate(obj.get(i).getOrderedDate());
			interventionObj.setEndDate(obj.get(i).getReviewedDate());
			interventionObj.setId(obj.get(i).getRefId().toString());
			
			interventionList.add(interventionObj);
			
		}
		
		return interventionList;
		
	}
	
	public List<Communication> getCommunicationQDM(List<ReferralQDM> referralObj){
		
		List<Communication> communicationQDM = new ArrayList<Communication>();

		for(int i=0;i<referralObj.size();i++){

			Integer status=referralObj.get(i).getStatus();

			if(status==4){
				
				Communication eachObj = new Communication();
				
				eachObj.setFulFillmentId(""+referralObj.get(i).getRefId());
				eachObj.setStartDate(referralObj.get(i).getReviewedDate());
				eachObj.setCode("371530004");
				eachObj.setCodeSystemOID("2.16.840.1.113883.6.96");
				eachObj.setCodeSystem("Clinical consultation report (record artifact)");
				
				communicationQDM.add(eachObj);
				
			}

		}
		
		return communicationQDM;
		
	}
	
	/**
	 * Gets the details of given patientId
	 * @param patientRepo
	 * @param patientId
	 */
	
	private void getPatientInfoQDM(PatientRegistrationRepository patientRepo, int patientId){
		
		PatientRegistration patientDetail = patientRepo.findOne(patientId);
		
		patientObj.setPatientId(patientDetail.getPatientRegistrationId());
		patientObj.setFirstName(patientDetail.getPatientRegistrationFirstName());
		patientObj.setLastName(patientDetail.getPatientRegistrationLastName());
		patientObj.setDob(patientDetail.getPatientRegistrationDob());
		
		if(patientDetail.getPatientRegistrationSex() == 2){
			patientObj.setGender("F");
		}else if(patientDetail.getPatientRegistrationSex() == 1){
			patientObj.setGender("M");
		}else{
			patientObj.setGender("TG");
		}
		
	}
	
	/**
	 * 
	 * Function to get information regarding referrals for selected 
	 * @param patientId
	 * @param em
	 * @return
	 * object containing data regarding referral_id,referred_date and status
	 * 
	 */
	
	@SuppressWarnings("rawtypes")
	public List<ReferralQDM> getReferrals(EntityManager em,Integer patientId) {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ReferralQDM> cq = builder.createQuery(ReferralQDM.class);
		Root<H413> rooth413 = cq.from(H413.class);
		Selection[] selections= new Selection[] {
				rooth413.get(H413_.h413001).alias("Referral id"),
				rooth413.get(H413_.referralOrderOn).alias("Ordered Date"),
				rooth413.get(H413_.referralReviewedOn).alias("Reviewed On"),
				rooth413.get(H413_.h413041).alias("Status")
		};
		cq.select(builder.construct(ReferralQDM.class,selections));
		Predicate predicateByPatientId=builder.equal(rooth413.get(H413_.h413035),patientId);
		cq.where(predicateByPatientId);
		List<ReferralQDM> alertsResultList = em.createQuery(cq).getResultList();
		return alertsResultList;
		
	}
	
	/**
	 * 
	 * This function gives the list of all the immunizations performed
	 * to the patientID
	 * 
	 * @param em
	 * @param patientId
	 * @return
	 */
	
	public List<Immunization> getImmuDetails(EntityManager em,Integer patientId)
	{
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ImmunizationDetailsBean> cq = builder.createQuery(ImmunizationDetailsBean.class);
		
		Root<LabDescription> rootlabDescription= cq.from(LabDescription.class);
		Join<LabDescription, LabEntries> joinLabdescLabentries=rootlabDescription.join(LabDescription_.labEntriesTable,JoinType.INNER);
		Join<LabEntries, Chart> joinLabentriesChart=joinLabdescLabentries.join(LabEntries_.chart,JoinType.INNER);
		
		Predicate predicateByPatientId=builder.equal(joinLabentriesChart.get(Chart_.chartPatientid),patientId);
		joinLabentriesChart.on(predicateByPatientId);
		
		cq.select(builder.construct(ImmunizationDetailsBean.class,
				joinLabdescLabentries.get(LabEntries_.labEntriesTestId),
				rootlabDescription.get(LabDescription_.labDescriptionTestDesc),
				joinLabdescLabentries.get(LabEntries_.labEntriesPerfOn),
				rootlabDescription.get(LabDescription_.labDescriptionCvx),
				joinLabdescLabentries.get(LabEntries_ .labEntriesTestStatus),
				joinLabdescLabentries.get(LabEntries_.labEntriesRefusalreason)
				));
		Predicate predicateBytestStatus=builder.greaterThan(joinLabdescLabentries.get(LabEntries_.labEntriesTestStatus),2);
		Predicate predicateBytestStatus1=builder.notEqual(joinLabdescLabentries.get(LabEntries_.labEntriesTestStatus),7);
		
		cq.where(predicateBytestStatus,predicateBytestStatus1);
		cq.distinct(true);
		
		List<ImmunizationDetailsBean> immunizationDetails = em.createQuery(cq).getResultList();
		
		CriteriaBuilder builder1 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq1 = builder1.createQuery();
		Root<CvxVaccineGroupMapping> rootTable=cq1.from(CvxVaccineGroupMapping.class);
		cq1.select(builder1.construct(CVXVaccineGroupMappingBean.class,
				rootTable.get(CvxVaccineGroupMapping_.cvxVaccineGroupMappingCvxCode).alias("CVX"),
				rootTable.get(CvxVaccineGroupMapping_.cvxVaccineGroupMappingUncertainFormulationCvx).alias("Vaccine Group CVX")
				));
		List<Object> resultlist1=em.createQuery(cq1).getResultList();
		List<CVXVaccineGroupMappingBean> vaccGroupMappingDetails=new ArrayList<CVXVaccineGroupMappingBean>();
		for(int i=0;i<resultlist1.size();i++){
			CVXVaccineGroupMappingBean obj=(CVXVaccineGroupMappingBean) resultlist1.get(i);
			vaccGroupMappingDetails.add(obj);
		}

		Integer vaccId=0;
		String vaccName="";
		Date performedDate=null;
		String cvx="";
		String groupCVX="";
		int status=0;
		int refusalReason=-1;
		List<ImmunizationQDM> immuFirstQueryResult=new ArrayList<ImmunizationQDM>();

		for(int i=0;i<immunizationDetails.size();i++){

			for(int j=0;j<vaccGroupMappingDetails.size();j++){
				if(immunizationDetails.get(i)!=null && vaccGroupMappingDetails.get(j)!=null){
					if((immunizationDetails.get(i).getLabDescriptionCvx()!=null) && (vaccGroupMappingDetails.get(j).getCvx_vaccine_group_mapping_cvx_code()!=null)){
						if(immunizationDetails.get(i).getLabDescriptionCvx().equals(vaccGroupMappingDetails.get(j).getCvx_vaccine_group_mapping_cvx_code())){
							vaccId=immunizationDetails.get(i).getLabEntriesTestId();
							vaccName=immunizationDetails.get(i).getLabDescriptionTestDesc();
							performedDate=immunizationDetails.get(i).getLabEntriesPerfOn();
							status=immunizationDetails.get(i).getStatus();
							refusalReason=immunizationDetails.get(i).getRefusalReason();
							cvx=vaccGroupMappingDetails.get(j).getCvx_vaccine_group_mapping_cvx_code();
							groupCVX=vaccGroupMappingDetails.get(j).getCvx_vaccine_group_mapping_uncertain_formulation_cvx();
							ImmunizationQDM immuBeanObj=new ImmunizationQDM(vaccId,vaccName,performedDate,cvx,groupCVX,status,refusalReason);
							immuFirstQueryResult.add(immuBeanObj);
						}
					}
				}

			}
		}

		CriteriaBuilder builder2 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq2 = builder2.createQuery();
		Root<VaccineReport> vaccReportTable=cq2.from(VaccineReport.class);
		Join<VaccineReport, Chart> joinvaccReportChart=vaccReportTable.join(VaccineReport_.chartTable,JoinType.INNER);
		Join<VaccineReport, LabDescription> joinvaccReportLabdesc=vaccReportTable.join(VaccineReport_.labDescriptionTable,JoinType.INNER);
		Predicate predicateByPatientId1=builder2.equal(joinvaccReportChart.get(Chart_.chartPatientid),patientId);
		joinvaccReportChart.on(predicateByPatientId1);
		Predicate predicateByIsEMR=builder2.equal(vaccReportTable.get(VaccineReport_.vaccineReportIsemr),2);
		cq2.select(builder2.construct(VaccReportBean.class,
				vaccReportTable.get(VaccineReport_.vaccineReportVaccineId),
				vaccReportTable.get(VaccineReport_.vaccineReportChartId),
				vaccReportTable.get(VaccineReport_.vaccineReportGivenDate),
				joinvaccReportLabdesc.get(LabDescription_.labDescriptionTestDesc),
				joinvaccReportLabdesc.get(LabDescription_.labDescriptionCvx)
				));
		cq2.where(predicateByIsEMR);
		cq2.distinct(true);
		List<Object> resultlist2=em.createQuery(cq2).getResultList();
		List<VaccReportBean> vaccReportObj=new ArrayList<VaccReportBean>();
		for(int i=0;i<resultlist2.size();i++){
			VaccReportBean obj=(VaccReportBean) resultlist2.get(i);
			vaccReportObj.add(obj);
		}
		List<ImmunizationQDM> immuSecondQueryResult=new ArrayList<ImmunizationQDM>();
		for(int i=0;i<vaccReportObj.size();i++){
			for(int j=0;j<vaccGroupMappingDetails.size();j++){
				if(!(vaccReportObj.get(i).getLabDescriptionCvx().equals(null)) && (!vaccGroupMappingDetails.get(j).getCvx_vaccine_group_mapping_cvx_code().equals(null)))
					if(vaccReportObj.get(i).getLabDescriptionCvx().equals(vaccGroupMappingDetails.get(j).getCvx_vaccine_group_mapping_cvx_code())){
						vaccId=vaccReportObj.get(i).getVaccineReportVaccineId();
						vaccName=vaccReportObj.get(i).getLabDescriptionTestDesc();
						performedDate=vaccReportObj.get(i).getVaccineReportGivenDate();
						cvx=vaccGroupMappingDetails.get(j).getCvx_vaccine_group_mapping_cvx_code();
						groupCVX=vaccGroupMappingDetails.get(j).getCvx_vaccine_group_mapping_uncertain_formulation_cvx();
						ImmunizationQDM obj=new ImmunizationQDM(vaccId,vaccName,performedDate,cvx,groupCVX,3,-1);
						immuSecondQueryResult.add(obj);
					}
			}
		}
		immuFirstQueryResult.addAll(immuSecondQueryResult);		
		List<com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Immunization> immuQDM = new ArrayList<com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Immunization>();

		for(int i=0;i<immuFirstQueryResult.size();i++){
			com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Immunization immuObject = new com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Immunization();
			immuObject.setCode(immuFirstQueryResult.get(i).getCvx());
			immuObject.setCodeSystem(immuFirstQueryResult.get(i).getVaccName());
			immuObject.setCodeSystemOID("2.16.840.1.113883.12.292");
			immuObject.setStartDate(immuFirstQueryResult.get(i).getPerformedDate());
			immuObject.setEndDate(immuFirstQueryResult.get(i).getPerformedDate());
			immuObject.setStatus(immuFirstQueryResult.get(i).getStatus());
			if(immuFirstQueryResult.get(i).getStatus()==3 || immuFirstQueryResult.get(i).getStatus()==4 || immuFirstQueryResult.get(i).getStatus()==5 || immuFirstQueryResult.get(i).getStatus()==6)
				immuObject.setStatus(2);
			else if(immuFirstQueryResult.get(i).getStatus()==8 && immuFirstQueryResult.get(i).getRefusalReason()==1){
				immuObject.setStatus(2);
				Negation negationObj=new Negation();
				negationObj.setCode("183945002");
				negationObj.setCodeSystemOID("2.16.840.1.113883.6.96");
				negationObj.setDescription("Religious reason");
				negationObj.setCodeSystem("SNOMED");
				immuObject.setNegation(negationObj);
			}
			else if(immuFirstQueryResult.get(i).getStatus()==8){
				immuObject.setStatus(2);
				Negation negationObj=new Negation();
				negationObj.setCode("183944003");
				negationObj.setCodeSystemOID("2.16.840.1.113883.6.96");
				negationObj.setDescription("Other reasons for refusal");
				negationObj.setCodeSystem("SNOMED");
				immuObject.setNegation(negationObj);
			}
			immuQDM.add(immuObject);
		}
		return immuQDM;
	}
	
	@SuppressWarnings("unused")
	public List<ActiveMedication> getMedDataRange(EntityManager em,int patientId,Date date1,Date date2) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object> cq = builder.createQuery();
        Root<Prescription> root = cq.from(Prescription.class);
        Join<Prescription, NdcPkgProduct> prescNdcJoin=root.join(Prescription_.ndcPkgProduct,JoinType.INNER);
        Join<Prescription, MedStatus> prescStatusJoin=root.join(Prescription_.medstatus,JoinType.INNER);
        Predicate patId=builder.equal(root.get(Prescription_.docPrescPatientId), patientId);
        Predicate status=root.get(Prescription_.docPrescStatus).in(1,2,3,17,21);
        cq.select(builder.construct(MedicationOrderQDM.class, root.get(Prescription_.rxname),
                root.get(Prescription_.rxstrength),
                root.get(Prescription_.rxform),
                root.get(Prescription_.docPrescRoute),
                prescStatusJoin.get(MedStatus_.medStatusName),
                builder.selectCase().when(builder.notEqual(builder.coalesce(root.get(Prescription_.noofrefills), ""), ""), root.get(Prescription_.noofrefills)).otherwise("0"),
                builder.selectCase().when(builder.notEqual(builder.coalesce(root.get(Prescription_.noofdays), ""), ""), root.get(Prescription_.noofdays)).otherwise("0"),
                builder.function("DATE", Date.class, root.get(Prescription_.docPrescOrderedDate)),
                builder.selectCase().when(root.get(Prescription_.docPrescStartDate).isNotNull(),builder.function("to_char", String.class, root.get(Prescription_.docPrescStartDate),builder.literal("MM/DD/YYYY"))).otherwise(builder.function("to_char", String.class, root.get(Prescription_.docPrescOrderedDate),builder.literal("MM/DD/YYYY")))));
        Predicate dateRange=builder.between(root.get(Prescription_.docPrescOrderedDate),date1 , date2);
        Predicate dateRange2=builder.between(root.get(Prescription_.docPrescInactivatedOn),date1 , date2);
        Predicate result1=builder.or(dateRange,dateRange2);
        cq.where(result1,patId,status);
        cq.distinct(true);
        List<Object> result=em.createQuery(cq).getResultList();
        List<MedicationOrderQDM> dataList= new ArrayList<MedicationOrderQDM>();
        for(int i=0;i<result.size();i++){
        	MedicationOrderQDM eachData=(MedicationOrderQDM) result.get(i);
            int cmd=(Integer.parseInt(eachData.getDays()) * (Integer.parseInt(eachData.getRefills())+1));
            eachData.setCMD(cmd);
            dataList.add(eachData);
        }
        
        CriteriaBuilder builder1 = em.getCriteriaBuilder();
        CriteriaQuery<Object> cq1 = builder1.createQuery();
        Root<CurrentMedication> root1 = cq1.from(CurrentMedication.class);
        Join<CurrentMedication, MedStatus> rootJoin=root1.join(CurrentMedication_.medstatus,JoinType.INNER);
        Predicate patId1=builder1.equal(root1.get(CurrentMedication_.currentMedicationPatientId), patientId);
        Predicate currstatus=root1.get(CurrentMedication_.currentMedicationStatus).in(14);
        cq1.select(builder1.construct(MedicationOrderQDM.class, root1.get(CurrentMedication_.currentMedicationRxName),
                root1.get(CurrentMedication_.currentMedicationDosageWithUnit),
                root1.get(CurrentMedication_.currentMedicationForm),
                root1.get(CurrentMedication_.currentMedicationRoute),
                rootJoin.get(MedStatus_.medStatusName),
                builder1.selectCase().when(builder1.notEqual(builder1.coalesce(root1.get(CurrentMedication_.currentMedicationRefills), ""), ""), root1.get(CurrentMedication_.currentMedicationRefills)).otherwise("0"),
                builder1.selectCase().when(builder1.notEqual(builder1.coalesce(root1.get(CurrentMedication_.currentMedicationDays), ""), ""), root1.get(CurrentMedication_.currentMedicationDays)).otherwise("0"),
                builder1.function("to_char", String.class, root1.get(CurrentMedication_.currentMedicationOrderOn),builder1.literal("MM/DD/YYYY HH:MI:SS am")),
                builder1.selectCase().when(root1.get(CurrentMedication_.currentMedicationStartDate).isNotNull(),builder1.function("to_char", String.class, root1.get(CurrentMedication_.currentMedicationStartDate),builder1.literal("MM/DD/YYYY"))).otherwise(builder1.function("to_char", String.class, root1.get(CurrentMedication_.currentMedicationOrderOn),builder1.literal("MM/DD/YYYY")))));
        Predicate currdateRange=builder1.between(root1.get(CurrentMedication_.currentMedicationOrderOn),date1 , date2);
        Predicate currdateRange2=builder1.between(root1.get(CurrentMedication_.currentMedicationInactivatedOn),date1 , date2);
        Predicate currresult=builder.or(currdateRange,currdateRange2);
        cq1.where(currresult,patId1,currstatus);
        cq1.distinct(true);
        List<Object> resultcurrent=em.createQuery(cq1).getResultList();
        for(int i=0;i<resultcurrent.size();i++){
        	MedicationOrderQDM eachData=(MedicationOrderQDM) resultcurrent.get(i);
            int cmd=(Integer.parseInt(eachData.getDays()) * (Integer.parseInt(eachData.getRefills())+1));
            eachData.setCMD(cmd);
            dataList.add(eachData);
        }
		List<com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.ActiveMedication> medQDM = new ArrayList<com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.ActiveMedication>();

        for(int i=0;i<dataList.size();i++){
			com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.ActiveMedication medObject = new com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.ActiveMedication();
			medObject.setCMD(dataList.get(i).getCMD());
			medObject.setDose(dataList.get(i).getDose());
			medObject.setRefills(Integer.parseInt(dataList.get(i).getRefills()));
			medObject.setRoute(dataList.get(i).getRoute());
			medObject.setStartDate(dataList.get(i).getOrderDate());

			medQDM.add(medObject);
			
        }
        return medQDM;
    }
	
	/**
	 * This function gives the details of the encounters for 
	 * which the medication has been reviewed
	 * 
	 * @param em
	 * @param patientId
	 * @param date1 -- reporting year end date
	 * @param date2 -- reporting year end date - 3 years
	 * @return
	 * 
	 * list of encounter dates
	 * 
	 */
	
	public List<MedicationQDM> getMedicationsReviewed(EntityManager em,int patientId,Date date1,Date date2){
	     
		CriteriaBuilder builder1 = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq1 = builder1.createQuery(Object.class);
		Root<Encounter> encounterRoot = cq1.from(Encounter.class);
		Join<Encounter, Chart> encounterChartJoin1 = encounterRoot.join(Encounter_.chart,JoinType.INNER);
		Predicate prediByPatientId=builder1.equal(encounterChartJoin1.get(Chart_.chartPatientid), patientId);
		
		encounterChartJoin1.on(prediByPatientId);
		cq1.orderBy(builder1.desc(encounterRoot.get(Encounter_.encounterId)));
		cq1.select(encounterRoot.get(Encounter_.encounterId));
		
		List<Object> currentEncounter=em.createQuery(cq1).setMaxResults(1).getResultList();
        
		CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
        Root<Encounter> root = cq.from(Encounter.class);
        Integer currentEncounterId=Integer.parseInt(currentEncounter.get(0).toString());
        Predicate currentEncounterPredicate=builder.equal(root.get(Encounter_.encounterId),currentEncounterId);
        Join<Encounter, Chart> encChartJoin=root.join(Encounter_.chartTable,JoinType.INNER);
        
        encChartJoin.on(builder.equal(encChartJoin.get(Chart_.chartPatientid),patientId));
        Predicate date=builder.between(root.get(Encounter_.encounterDate), date1, date2);
        Predicate attest=builder.gt(builder.length(builder.trim(root.get(Encounter_.medicationAttestationStatus))), 0);
        cq.multiselect(root.get(Encounter_.encounterDate),root.get(Encounter_.medicationAttestationStatus));
        cq.where(date,attest,currentEncounterPredicate);
        
        List<Object[]> result=em.createQuery(cq).getResultList();
        List<MedicationQDM> attestList=new ArrayList<MedicationQDM>();
        
        for(int i=0;i<result.size();i++){
        	
        	Date attastationDate=(Date) result.get(i)[0];
        	boolean status=false;
        	if((result.get(i)[1].toString()).equals("428191000124101"))
        		status=true;
        		MedicationQDM eachObj=new MedicationQDM(attastationDate, status);
        		attestList.add(eachObj);
        }
       
        return attestList;
    }
	
	@SuppressWarnings("rawtypes")
	public List<ClinicalDataQDM> getClinicalDataQDM(EntityManager em,int patientId, String snomedCodes,String loincCodes,Boolean range, Date startDate, Date endDate)    {     
		
		String []  codeListString=snomedCodes.split(",");
		String [] loincCodeListString=loincCodes.split(",");
		List<String> snomedCodeList= Arrays.asList(codeListString);
		List<String> loincCodeList=Arrays.asList(loincCodeListString);
		List<ClinicalDataQDM> clinicalDataSNOMED = new ArrayList<ClinicalDataQDM>();
		List<ClinicalDataQDM> clinicalDataLOINC = new ArrayList<ClinicalDataQDM>();
		List<ClinicalDataQDM> clinicaldataFinal = new ArrayList<ClinicalDataQDM>();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		if(snomedCodeList.size()>0){
			CriteriaQuery<ClinicalDataQDM> cqForSnomed = builder.createQuery(ClinicalDataQDM.class);      
			Root<PatientClinicalElements> root = cqForSnomed.from(PatientClinicalElements.class);
			Join<PatientClinicalElements, Encounter> EncElementJoin = root.join(PatientClinicalElements_.encounter, JoinType.INNER);
			Join<Encounter, Chart> EncChartJoin = EncElementJoin.join(Encounter_.chartTable, JoinType.INNER);
			Join<PatientClinicalElements, ClinicalElements> clinicalElementsJoin = root.join(PatientClinicalElements_.clinicalElement, JoinType.INNER);
			Join<ClinicalElements, ClinicalElementsOptions> clinicalElementsOptionsJoin = clinicalElementsJoin.join(ClinicalElements_.clinicalElementsOptions, JoinType.LEFT);
			Join<ClinicalElements,CNMCodeSystem> codeSystemJoin = clinicalElementsJoin.join(ClinicalElements_.cnmCodeSystems, JoinType.LEFT);

			Predicate checkOid=codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemOid).in(builder.literal("2.16.840.1.113883.6.96"));
			Predicate checkCodeList=codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemCode).in(snomedCodeList);
			Predicate checkCodeNotNull=codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemCode).isNotNull();
			Predicate checkCodeNotEmpty=builder.notEqual(codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemCode),builder.literal(""));
			Predicate checkSnomedCodeNotNull=clinicalElementsJoin.get(ClinicalElements_.clinicalElementsSnomed).isNotNull();
			Predicate checkSnomedCodeNotEmpty=builder.notEqual(clinicalElementsJoin.get(ClinicalElements_.clinicalElementsSnomed),builder.literal(""));  
			codeSystemJoin.on(builder.and(checkOid,checkCodeList,checkCodeNotNull));  

			Expression<String> rcode = builder.<String>selectCase().when(clinicalElementsJoin.get(ClinicalElements_.clinicalElementsSnomed).isNotNull(),clinicalElementsJoin.get(ClinicalElements_.clinicalElementsSnomed)).otherwise(codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemCode));
			Expression<String> rvalue = builder.<String>selectCase().when(clinicalElementsJoin.get(ClinicalElements_.clinicalElementsDatatype).in("4","5"),clinicalElementsOptionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsName)).otherwise(root.get(PatientClinicalElements_.patientClinicalElementsValue));

			Selection[] selections= new Selection[] {
					root.get(PatientClinicalElements_.patientClinicalElementsPatientid).alias("patientId"),       
					rcode.alias("code"),
					codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemOid).alias("codesystem"),// value set for comparison
					clinicalElementsJoin.get(ClinicalElements_.clinicalElementsName).alias("elementName"),
					EncElementJoin.get(Encounter_.encounterDate).alias("recordedDate"),
					clinicalElementsOptionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsSnomed).alias("resultCode"),
					rvalue.alias("resultValue"),
					clinicalElementsOptionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsValue).alias("optionValue"),
					root.get(PatientClinicalElements_.patientClinicalElementsValue).alias("patientResult")
			};


			Predicate[] restrictions = new Predicate[] {
					builder.equal(EncChartJoin.get(Chart_.chartPatientid), patientId),
					builder.equal(clinicalElementsJoin.get(ClinicalElements_.clinicalElementsIsactive), true),            
					builder.or(builder.and(checkCodeNotNull,checkCodeNotEmpty),builder.and(checkSnomedCodeNotNull,checkSnomedCodeNotEmpty)),  
					builder.or((clinicalElementsJoin.get(ClinicalElements_.clinicalElementsSnomed).in(snomedCodeList)),(codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemCode).in(snomedCodeList))),  

			};

			Predicate[] restrictionsWithDate = new Predicate[] {
					builder.equal(EncChartJoin.get(Chart_.chartPatientid), patientId),
					builder.equal(clinicalElementsJoin.get(ClinicalElements_.clinicalElementsIsactive), true),              
					builder.or(builder.and(checkCodeNotNull,checkCodeNotEmpty),builder.and(checkSnomedCodeNotNull,checkSnomedCodeNotEmpty)),      
					builder.greaterThanOrEqualTo(builder.function("DATE", Date.class,EncElementJoin.get(Encounter_.encounterDate)),startDate),
					builder.lessThanOrEqualTo(builder.function("DATE", Date.class,EncElementJoin.get(Encounter_.encounterDate)),endDate),                                                
					builder.or((clinicalElementsJoin.get(ClinicalElements_.clinicalElementsSnomed).in(snomedCodeList)),(codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemCode).in(snomedCodeList)))  

			};

			cqForSnomed.select(builder.construct(ClinicalDataQDM.class,selections));
			if(range)
				cqForSnomed.where(restrictionsWithDate);
			else
				cqForSnomed.where(restrictions);
			cqForSnomed.distinct(true);
			clinicalDataSNOMED = em.createQuery(cqForSnomed).getResultList();

			for(ClinicalDataQDM clinicalDataQDM: clinicalDataSNOMED){
				clinicalDataQDM.setEndDate(endDate);
				clinicalDataQDM.setStartDate(startDate);
				if(clinicalDataQDM.getOptionValue()==null ){              
					clinicalDataQDM.setCodeSystemOID("2.16.840.1.113883.6.96");
					clinicalDataQDM.setCodeSystem("2.16.840.1.113883.6.96");
					clinicalDataQDM.setResultCodeSystem("2.16.840.1.113883.6.96");
					clinicalDataQDM.setResultValue(clinicalDataQDM.getPatientResult());
					clinicaldataFinal.add(clinicalDataQDM);
				}  
				else if(clinicalDataQDM.getOptionValue().equalsIgnoreCase(clinicalDataQDM.getPatientResult())){
					clinicalDataQDM.setCodeSystemOID("2.16.840.1.113883.6.96");
					clinicalDataQDM.setCodeSystem("2.16.840.1.113883.6.96");
					clinicalDataQDM.setResultCodeSystem("2.16.840.1.113883.6.96");  
					clinicaldataFinal.add(clinicalDataQDM);
				}
			}
		}

		if(loincCodeList.size()>0){                                  
			CriteriaQuery<ClinicalDataQDM> cqForLoinc = builder.createQuery(ClinicalDataQDM.class);      
			Root<PatientClinicalElements> root = cqForLoinc.from(PatientClinicalElements.class);
			Join<PatientClinicalElements, Encounter> EncElementJoin = root.join(PatientClinicalElements_.encounter, JoinType.INNER);
			Join<Encounter, Chart> EncChartJoin = EncElementJoin.join(Encounter_.chartTable, JoinType.INNER);
			Join<PatientClinicalElements, ClinicalElements> clinicalElementsJoin = root.join(PatientClinicalElements_.clinicalElement, JoinType.INNER);
			Join<ClinicalElements, ClinicalElementsOptions> clinicalElementsOptionsJoin = clinicalElementsJoin.join(ClinicalElements_.clinicalElementsOptions, JoinType.LEFT);
			Join<ClinicalElements,CNMCodeSystem> codeSystemJoin = clinicalElementsJoin.join(ClinicalElements_.cnmCodeSystems, JoinType.INNER);      

			Predicate checkOid=codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemOid).in(builder.literal("2.16.840.1.113883.6.1"));
			Predicate checkCodeList=codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemCode).in(loincCodeList);
			Predicate checkCodeNotNull=codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemCode).isNotNull();
			Predicate checkCodeNotEmpty=builder.notEqual(codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemCode),builder.literal(""));      
			codeSystemJoin.on(builder.and(checkOid,checkCodeList,checkCodeNotNull));  

			Expression<String> rcode=codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemCode);
			Expression<String> rvalue=    builder.<String>selectCase().when(clinicalElementsJoin.get(ClinicalElements_.clinicalElementsDatatype).in("3","4"),clinicalElementsOptionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsName)).otherwise(root.get(PatientClinicalElements_.patientClinicalElementsValue));

			Selection[] selections= new Selection[] {
					root.get(PatientClinicalElements_.patientClinicalElementsPatientid).alias("patientId"),       
					rcode.alias("code"),
					codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemOid).alias("codesystem"),// value set for comparison
					clinicalElementsJoin.get(ClinicalElements_.clinicalElementsName).alias("elementName"),
					EncElementJoin.get(Encounter_.encounterDate).alias("recordedDate"),
					clinicalElementsOptionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsSnomed).alias("resultCode"),
					rvalue.alias("resultValue"),
					clinicalElementsOptionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsValue).alias("optionValue"),
					root.get(PatientClinicalElements_.patientClinicalElementsValue).alias("patientResult")
			};  
			Predicate[] restrictions = new Predicate[] {
					builder.equal(EncChartJoin.get(Chart_.chartPatientid), patientId),
					builder.equal(clinicalElementsJoin.get(ClinicalElements_.clinicalElementsIsactive), true),              
					checkCodeNotNull,
					checkCodeNotEmpty
			};
			Predicate[] restrictionsWithDate = new Predicate[] {
					builder.equal(EncChartJoin.get(Chart_.chartPatientid), patientId),
					builder.equal(clinicalElementsJoin.get(ClinicalElements_.clinicalElementsIsactive), true),              
					checkCodeNotNull,checkCodeNotEmpty,
					builder.greaterThanOrEqualTo(builder.function("DATE", Date.class,EncElementJoin.get(Encounter_.encounterDate)),startDate),
					builder.lessThanOrEqualTo(builder.function("DATE", Date.class,EncElementJoin.get(Encounter_.encounterDate)),endDate)                                                  
			};

			cqForLoinc.select(builder.construct(ClinicalDataQDM.class,selections));
			if(range)
				cqForLoinc.where(restrictionsWithDate);
			else
				cqForLoinc.where(restrictions);
			cqForLoinc.distinct(true);   
			clinicalDataLOINC = em.createQuery(cqForLoinc).setMaxResults(500).getResultList();

			for(ClinicalDataQDM clinicalDataQDM: clinicalDataLOINC){

				clinicalDataQDM.setEndDate(endDate);
				clinicalDataQDM.setStartDate(startDate);
				if(clinicalDataQDM.getOptionValue()==null ){
					clinicalDataQDM.setCodeSystemOID("2.16.840.1.113883.6.1");
					clinicalDataQDM.setCodeSystem("2.16.840.1.113883.6.1");
					clinicalDataQDM.setResultCodeSystem("2.16.840.1.113883.6.1");
					clinicaldataFinal.add(clinicalDataQDM);
				}  
				else if(clinicalDataQDM.getOptionValue().equalsIgnoreCase(clinicalDataQDM.getPatientResult())){
					clinicalDataQDM.setCodeSystemOID("2.16.840.1.113883.6.1");
					clinicalDataQDM.setCodeSystem("2.16.840.1.113883.6.1");
					clinicalDataQDM.setResultCodeSystem("2.16.840.1.113883.6.96");  
					clinicaldataFinal.add(clinicalDataQDM);
				}
			}
		}  
		
		return clinicaldataFinal;
	}

	@SuppressWarnings("rawtypes")
	public List<QDM> getTobaccoDetails(EntityManager em,int patientId)    {      

		List<ClinicalDataQDM> clinicalDataSNOMED = new ArrayList<ClinicalDataQDM>();
		ArrayList<String> gwids=new ArrayList<String>(){{
			add("0000100303000000015");
			add("0000100303000000013");
		}};

		CriteriaBuilder builder=em.getCriteriaBuilder();
		CriteriaQuery<ClinicalDataQDM> cq=builder.createQuery(ClinicalDataQDM.class);
		Root<PatientClinicalHistory> root = cq.from(PatientClinicalHistory.class);
		Join<PatientClinicalHistory, ClinicalElements> clinicalElementsJoin = root.join(PatientClinicalHistory_.clinicalElement, JoinType.INNER);
		clinicalElementsJoin.on(clinicalElementsJoin.get(ClinicalElements_.clinicalElementsGwid).in(gwids));
		Join<ClinicalElements, ClinicalElementsOptions> clinicalElementsOptionsJoin = clinicalElementsJoin.join(ClinicalElements_.clinicalElementsOptions, JoinType.INNER);
		Join<ClinicalElements,CNMCodeSystem> codeSystemJoin = clinicalElementsJoin.join(ClinicalElements_.cnmCodeSystems, JoinType.LEFT);
		//clinicalElementsOptionsJoin.on(builder.equal(root.get(PatientClinicalHistory_.patientClinicalHistoryValue),ClinicalElementsOptions_.clinicalElementsOptionsValue));

		Predicate checkOid=codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemOid).in(builder.literal("2.16.840.1.113883.6.96"));
		Predicate checkCodeNotNull=codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemCode).isNotNull();
		Predicate checkCodeNotEmpty=builder.notEqual(codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemCode),builder.literal(""));
		codeSystemJoin.on(builder.and(checkOid,checkCodeNotNull,checkCodeNotEmpty));  
		Predicate checkPatientId=builder.equal(root.get(PatientClinicalHistory_.patientClinicalHistoryPatientid),patientId);

		Expression<String> rcode = builder.<String>selectCase().when(clinicalElementsJoin.get(ClinicalElements_.clinicalElementsSnomed).isNotNull(),clinicalElementsJoin.get(ClinicalElements_.clinicalElementsSnomed)).otherwise(codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemCode));
		Expression<String> rvalue = builder.<String>selectCase().when(clinicalElementsJoin.get(ClinicalElements_.clinicalElementsDatatype).in("4","5"),clinicalElementsOptionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsName)).otherwise(root.get(PatientClinicalHistory_.patientClinicalHistoryValue));

		Selection selections[]=new Selection[]{
				root.get(PatientClinicalHistory_.patientClinicalHistoryPatientid).alias("patientId"),
				rcode.alias("code"),           
				codeSystemJoin.get(CNMCodeSystem_.cnmCodeSystemOid).alias("codesystem"),
				clinicalElementsJoin.get(ClinicalElements_.clinicalElementsName).alias("description"),
				clinicalElementsOptionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsSnomed).alias("resultcode"),
				clinicalElementsOptionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsValue).alias("optionvalue"),
				root.get(PatientClinicalHistory_.patientClinicalHistoryValue).alias("value"),
				clinicalElementsOptionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsName).alias("resultvalue")
		};

		cq.select(builder.construct(ClinicalDataQDM.class,selections));
		cq.where(checkPatientId);
		cq.distinct(true);

		clinicalDataSNOMED = em.createQuery(cq).getResultList();

		List<ClinicalDataQDM> clinicalDataFinal=new ArrayList<ClinicalDataQDM>();
		for(ClinicalDataQDM clinicalDataQdm:clinicalDataSNOMED){
			if(clinicalDataQdm.getOptionValue()==null || clinicalDataQdm.getOptionValue().isEmpty())
			{
				clinicalDataQdm.setResultValue(clinicalDataQdm.getPatientResult());
				clinicalDataQdm.setResultCodeSystem("2.16.840.1.113883.6.96");  
				clinicalDataFinal.add(clinicalDataQdm);
			}
			else if(clinicalDataQdm.getOptionValue().equalsIgnoreCase(clinicalDataQdm.getPatientResult())){
				clinicalDataQdm.setResultCodeSystem("2.16.840.1.113883.6.96");  
				clinicalDataFinal.add(clinicalDataQdm);
			}   
		}


		List<QDM> tobaccoStatus=new ArrayList<QDM>();
		QDM QDMObj;
		ClinicalDataQDM eachObj= null;
		for(int i=0;i<clinicalDataFinal.size();i++){
			eachObj=clinicalDataFinal.get(i);
			QDMObj=new QDM();
			QDMObj.setCode(eachObj.getResultCode());
			QDMObj.setCodeSystem(eachObj.getCodeSystem());
			QDMObj.setCodeSystemOID("2.16.840.1.113883.6.96");
			tobaccoStatus.add(QDMObj);
		}



		return tobaccoStatus;
	}
	
	/**
	 * Function to return max encounter id for a given patient ID
	 * 
	 * @param patientId
	 * @param em
	 * @return
	 */
	
	public Integer getMaxEncounterIdByPatient(int patientId, EntityManager em){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = builder.createQuery(Integer.class);
		Root<Encounter> root = cq.from(Encounter.class);
		
		Join<Encounter, Chart> encounterChartJoin = root.join(Encounter_.chart,JoinType.INNER);
		
		cq.select(root.get(Encounter_.encounterId));
		
		Predicate[] restrictions= new Predicate[] {
				builder.equal(encounterChartJoin.get(Chart_.chartPatientid), patientId),
		};
		cq.where(restrictions);
		
		cq.orderBy(builder.desc(root.get(Encounter_.encounterId)));
		
		Integer maxEncId = em.createQuery(cq).getResultList().get(0);
		
		return maxEncId;
		
	}
	
	/**
	 * Function which gives the result to E-Prescribing EP Measure
	 * 
	 * @param patientId
	 * @param providerId
	 * @param em
	 * @return
	 */
	
	public String getEPrescribingDetails(Integer encounterId, EntityManager em) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<Prescription> root = cq.from(Prescription.class);

		int ePrescStatus = -1, i = 0;
		
		int electronicallySentMedications = 0;
		
		cq.multiselect(root.get(Prescription_.rxname).alias("Medication"),
				builder.coalesce(root.get(Prescription_.docPrescIsEPrescSent),0).alias("IsSentElectronically")).where(builder.equal(root.get(Prescription_.docPrescIsActive), true),builder.equal(root.get(Prescription_.docPrescEncounterId), encounterId));
		
		List<Object[]> result=em.createQuery(cq).getResultList();
		
		for(i=0;i<result.size();i++){
			
			if(Integer.parseInt(result.get(i)[1].toString())==1){
				
				result.get(i)[1]="Yes";
				electronicallySentMedications++;
				ePrescStatus = 1;
				
			}else{
				
				result.get(i)[1]="No";
				ePrescStatus = 0;
				
			}
		}
		
		return "Medications sent electronically: "+electronicallySentMedications+"\n Total Medications prescribed: "+i+"&&&&"+ePrescStatus;
		
	}
	
	/**
	 * Function to check and find whether transition of care is checked or not for particular encounter
	 * 
	 * @param encounterId
	 * @param em
	 * @return
	 */
	
	public boolean checkTransitionOfCareByEncId(int encounterId, EntityManager em){
	
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Boolean> cq = builder.createQuery(Boolean.class);
		Root<Encounter> root = cq.from(Encounter.class);
		
		cq.select(builder.coalesce(root.get(Encounter_.transitionOfCare),false));
		
		cq.where(builder.equal(root.get(Encounter_.encounterId), encounterId));
		
		cq.orderBy(builder.desc(root.get(Encounter_.encounterId)));
		
		Boolean isTransitionOfCareChecked = em.createQuery(cq).getResultList().get(0);
		
		return isTransitionOfCareChecked;
		
	}
	
	/**
	 * Function to get medication reconcilation status for given encounter id
	 * 
	 * @param encounterId
	 * @param em
	 * @return
	 */
	
	public boolean getReconcilationStatusByEncId(int encounterId, EntityManager em){

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = builder.createQuery(Integer.class);
		Root<ReconciledMedication> root = cq.from(ReconciledMedication.class);			
		
		boolean isMedicationReconciled = false;
		
		cq.select(root.get(ReconciledMedication_.reconciledMedicationBy));
		
		cq.where(builder.equal(root.get(ReconciledMedication_.reconciledMedicationEncounterid), encounterId));
		
		List<Integer> reconciledBy = em.createQuery(cq).getResultList();
		
		if(reconciledBy.size() > 0){
			isMedicationReconciled = true;
		}
		
		return isMedicationReconciled;
		
	}
	
	/**
	 * Function to get list of referrals transmitted by a physician electronically in given reporting period.
	 * 
	 * @param providerId
	 * @param startDate
	 * @param endDate
	 * @param em
	 * @return
	 */
	
	@SuppressWarnings("rawtypes")
	public String getReferralInfoExchangeByProvider(int providerId, int patientId, Date startDate, Date endDate, EntityManager em){

		String returnString = "";
		
		try{
			
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
			Root<H413> root = cq.from(H413.class);
			
			Selection[] selections= new Selection[] {
				
				builder.coalesce(root.get(H413_.summaryCareRecordProvidedElectronic),0),
				builder.count(root),
								
			};
			
			cq.multiselect(selections);
			
			cq.where(builder.equal(root.get(H413_.referralById), providerId), 
					builder.between(builder.function("DATE", Date.class, root.get(H413_.referralOrderOn)), startDate, endDate));
			
			cq.groupBy(root.get(H413_.summaryCareRecordProvidedElectronic));
			
			List<Object[]> result = em.createQuery(cq).getResultList();
			
			
			if(result.size() == 2){
			
				int total = Integer.parseInt(result.get(0)[1].toString())+Integer.parseInt(result.get(1)[1].toString());
				
				returnString = "No. of referrals sent electronically: "+result.get(1)[1]+"\nNo. of referrals sent in total: "+total;
				
			}else if(result.size() == 1){
				
				if(result.get(0)[0].equals("1")){
					returnString = "No. of referrals sent electronically: "+result.get(0)[1]+"\nNo. of referrals sent in total: "+result.get(0)[1];
				}else{
					returnString = "No. of referrals sent electronically: 0\nNo. of referrals sent in total: "+result.get(0)[1];
				}
				
			}else{
				
				returnString = "No. of referrals sent electronically: 0\nNo. of referrals sent in total: 0";
				
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			returnString = "0/0";
			
		}
		
		return returnString;
		
	}
	
	/**
	 * Function to get count of messages sent/replied by a provider in given period of time
	 * 
	 * @param providerId
	 * @param startDate
	 * @param endDate
	 * @param em
	 * @return
	 */
	
	@SuppressWarnings("rawtypes")
	public String getSecureMessagingInfo(int providerId, int patientId, Date startDate, Date endDate, EntityManager em){
		
		String returnString = "";
		
		try{
			
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
			Root<PortalMessage> root = cq.from(PortalMessage.class);
			
			Selection[] selections= new Selection[] {
				
				root.get(PortalMessage_.portalMessageHasreplied),
				builder.count(root),
								
			};
			
			cq.multiselect(selections);
			
			cq.where(builder.equal(root.get(PortalMessage_.messageBy), providerId), 
					builder.between(builder.function("DATE", Date.class, root.get(PortalMessage_.mdate)), startDate, endDate),
					builder.equal(root.get(PortalMessage_.messageTo), patientId));
			
			cq.groupBy(root.get(PortalMessage_.portalMessageHasreplied));
			
			List<Object[]> result = em.createQuery(cq).getResultList();
			
			
			if(result.size() == 2){
			
				int total = Integer.parseInt(result.get(0)[1].toString())+Integer.parseInt(result.get(1)[1].toString());
				
				returnString = "No. of messages sent as reply: "+result.get(1)[1]+"\n No.of messages composed: "+total;
				
			}else if(result.size() == 1){
				
				if(result.get(0)[0].equals("t")){
					returnString = "No. of messages sent as reply: "+result.get(0)[1]+"\n No.of messages composed: "+result.get(0)[1];
				}else{
					returnString = "No. of messages sent as reply: 0\nNo.of messages composed: "+result.get(0)[1];
				}
				
			}else{
				
				returnString = "No. of messages sent as reply: 0\nNo.of messages composed: 0";
				
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			returnString = "0/0";
			
		}
		
		return returnString;
		
	}
	
	/**
	 * Function to get count of patients who have viewed/downloaded their clinical data from patient portal 
	 * 
	 * @param providerId
	 * @param startDate
	 * @param endDate
	 * @param em
	 * @return
	 */
	
	@SuppressWarnings("unused")
	public String getPatientElectronicAccessInfo(int providerId, int patientId, Date startDate, Date endDate, EntityManager em){
		
		long patientCount = 0;
		
		String lastAccessedTime = "";
		
		try{
			
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<String> cq = builder.createQuery(String.class);
			Root<Chart> root = cq.from(Chart.class);
			
			Join<Chart, Encounter> encounterTable = root.join(Chart_.encounterTable, JoinType.INNER);
			Join<Encounter, EmployeeProfile> empProfileTable = encounterTable.join(Encounter_.empProfileEmpId, JoinType.INNER);
			Join<Chart, DirectEmailLog> directEmailLogTable = root.join(Chart_.directEmailLogTable, JoinType.INNER);
			
			List<Integer> encounterStatus = new ArrayList<Integer>();
			encounterStatus.add(0, 1);
			encounterStatus.add(1, 3);
			
			List<Integer> directEmailAction = new ArrayList<Integer>();
			directEmailAction.add(0, 1);
			directEmailAction.add(1, 2);
			
			Predicate[] restrictions= new Predicate[] {
				
				builder.equal(encounterTable.get(Encounter_.encounterType), 1),
				encounterTable.get(Encounter_.encounterStatus).in(encounterStatus),
				directEmailLogTable.get(DirectEmailLog_.directEmailLogActionType).in(directEmailAction),
				builder.equal(encounterTable.get(Encounter_.encounter_service_doctor), providerId),
				builder.between(builder.function("DATE", Date.class, encounterTable.get(Encounter_.encounterDate)), startDate, endDate),
				builder.equal(root.get(Chart_.chartPatientid), patientId)
				
			};
			
			cq.multiselect(builder.function("to_char", String.class, directEmailLogTable.get(DirectEmailLog_.directEmailLogSentOn), builder.literal("MM/DD/YYYY HH:MI:SS am")));
			
			cq.orderBy(builder.desc(directEmailLogTable.get(DirectEmailLog_.directEmailLogSentOn)));
			
			cq.where(restrictions);
			
			List<String> queryResult = em.createQuery(cq).getResultList();
			
			patientCount = queryResult.size();
			
			if(patientCount > 0){
				lastAccessedTime = queryResult.get(0);
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			patientCount = 0;
			
		}
		
		
		return lastAccessedTime+"&"+patientCount;
		
	}
	
	public String getPatientAccessInfoForPortal(int providerId, int patientId, Date startDate, Date endDate, EntityManager em){
		
		String result = "";
		
		try{
			
			Date defaultDate = new Date(1486109049);
			
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<String> cq = builder.createQuery(String.class);
			Root<H809> root = cq.from(H809.class);
			
			cq.multiselect(builder.function("to_char", String.class, root.get(H809_.h809011), builder.literal("MM/DD/YYYY HH:MI:SS am")));
			
			cq.where(builder.between(builder.coalesce(root.get(H809_.h809011), defaultDate), startDate, endDate),
					builder.equal(builder.coalesce(root.get(H809_.h809009), 0), 1),
					builder.equal(root.get(H809_.h809002), patientId));
			
			cq.orderBy(builder.desc(root.get(H809_.h809011)));
			
			List<String> accessedTime = em.createQuery(cq).getResultList();
			
			long count = accessedTime.size();
			
			if(count > 0){
				result = accessedTime.get(0);
			}else{
				result = "";
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
		
	}
	
}