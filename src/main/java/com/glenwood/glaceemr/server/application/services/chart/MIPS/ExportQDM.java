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
import com.glenwood.glaceemr.server.application.Bean.MeasureStatus;
import com.glenwood.glaceemr.server.application.Bean.MedicationOrderQDM;
import com.glenwood.glaceemr.server.application.Bean.MedicationQDM;
import com.glenwood.glaceemr.server.application.Bean.ParameterDetails;
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
import com.glenwood.glaceemr.server.application.models.MeaningfuluseSettings;
import com.glenwood.glaceemr.server.application.models.MeaningfuluseSettings_;
import com.glenwood.glaceemr.server.application.models.MedStatus;
import com.glenwood.glaceemr.server.application.models.MedStatus_;
import com.glenwood.glaceemr.server.application.models.NdcPkgProduct;
import com.glenwood.glaceemr.server.application.models.NdcPkgProduct_;
import com.glenwood.glaceemr.server.application.models.PatientAftercareData;
import com.glenwood.glaceemr.server.application.models.PatientAftercareData_;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements_;
import com.glenwood.glaceemr.server.application.models.PatientClinicalHistory;
import com.glenwood.glaceemr.server.application.models.PatientClinicalHistory_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
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
import com.glenwood.glaceemr.server.application.specifications.ProblemListSpecification;

public class ExportQDM {

	Patient patientObj = new Patient();
	
	public Patient getRequestQDM(EntityManager em,PatientRegistrationRepository patientRepo, ProblemListRepository diagnosisRepo, int patientId, int providerId){
		
		getPatientInfoQDM(em,patientRepo, patientId);
		
		return patientObj;
		
	}
	
	public List<Procedure> getProcBasedOnCPT(EntityManager em,Boolean considerProvider,int patientID, int providerId,List<String> cptCodes){
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
		Predicate[] restrictions=null;
		if(considerProvider)
		restrictions= new Predicate[] {
				builder.equal(root.get(Encounter_.encounter_service_doctor),providerId),
				builder.equal(encounterChartJoin.get(Chart_.chartPatientid), patientID),
				serviceCptJoin.get(Cpt_.cptCptcode).in(cptCodes),
				
				builder.equal(builder.function("DATE", Date.class, root.get(Encounter_.encounterCreatedDate)), builder.function("DATE", Date.class, chartServiceJoin.get(ServiceDetail_.serviceDetailDos))),
		};
		else
			restrictions= new Predicate[] {
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
	public List<com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Encounter> getEncounterQDM(Date startDate, Date endDate, EntityManager em, boolean considerProvider,int patientID, int providerId, HashMap<String, String> encounterCodeList){
		
		List<String> cptCodes = new ArrayList<String>();
		
		String cptCodeListString = "", hcpcsCodeListString = ""; 
		
		if(encounterCodeList.get("CPT").length() > 0){
			
			cptCodeListString = encounterCodeList.get("CPT").toString();
			
			cptCodes = Arrays.asList(cptCodeListString.split(","));
			
		}
		
		if(encounterCodeList.get("HCPCS").length() > 0){
			
			hcpcsCodeListString = encounterCodeList.get("HCPCS").toString();
			
			if(cptCodes.size() == 0){
				cptCodes = Arrays.asList(hcpcsCodeListString.split(","));
			}else{
				String completeCodeList = encounterCodeList.get("CPT").toString().concat(",".concat(hcpcsCodeListString));
				cptCodes = Arrays.asList(completeCodeList.split(","));
			}
			
		}
		
		if(cptCodes.size()==0)
			cptCodes.add("000000");
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
					builder.between(builder.function("DATE", Date.class, root.get(Encounter_.encounterDate)), startDate, endDate),
					builder.equal(builder.function("DATE", Date.class, root.get(Encounter_.encounterDate)),builder.function("DATE", Date.class,chartServiceJoin.get(ServiceDetail_.serviceDetailDos))),
			};
			
		}else{
			
			restrictions = new Predicate[] {
					builder.equal(encounterChartJoin.get(Chart_.chartPatientid), patientID),
					serviceCptJoin.get(Cpt_.cptCptcode).in(cptCodes),
					builder.between(builder.function("DATE", Date.class, root.get(Encounter_.encounterDate)), startDate, endDate),
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
				
				encObject = new com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Encounter();
				
				if(hcpcsCodeListString.length() > 0 && hcpcsCodeListString.contains(encounterObj.get(i).getCode())) {
					
					encObject.setCode(encounterObj.get(i).getCode());
					encObject.setCodeSystemOID("2.16.840.1.113883.6.285");
					encObject.setStartDate(encounterObj.get(i).getStartDate());
					encObject.setEndDate(encounterObj.get(i).getStartDate());
					encounterQDM.add(i, encObject);
					
					cptThere=true;
					
				}else if(cptCodeListString.length() > 0 && cptCodeListString.contains( encounterObj.get(i).getCode() )){
					
					encObject.setCode(encounterObj.get(i).getCode());
					encObject.setCodeSystemOID("2.16.840.1.113883.6.12");
					encObject.setStartDate(encounterObj.get(i).getStartDate());
					encObject.setEndDate(encounterObj.get(i).getStartDate());
					encounterQDM.add(i, encObject);
					
					cptThere=true;
					
				}
				
			}
			
			/*int encCount = getEncounterCountForPatient(em, patientID, providerId, startDate, endDate);
			
			if(cptThere==false && encCount > 0){
			
				encObject = new com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Encounter();
				encObject.setCode("99213");
				encObject.setCodeSystemOID("2.16.840.1.113883.6.12");
				if(encounterObj1.size()!=0)
				encObject.setStartDate(encounterObj1.get(0).getStartDate());
				encounterQDM.add(encObject);
				
			}*/
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return encounterQDM;
		
	}
	
	/**
	 * Function to get total no of encounters for the patient for the given provider in reporting year
	 * 
	 * @param patientId
	 * @param providerId
	 * @return
	 */
	
	private int getEncounterCountForPatient(EntityManager em, int patientId, int providerId, Date startDate, Date endDate){
		
		int count = 0;
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = builder.createQuery(Integer.class);
		Root<Encounter> root = cq.from(Encounter.class);
		
		Join<Encounter, Chart> encounterChartJoin = root.join(Encounter_.chart,JoinType.INNER);
		
		cq.where(
				builder.between(builder.function("DATE", Date.class, root.get(Encounter_.encounterDate)), startDate, endDate),
				builder.equal(root.get(Encounter_.encounter_service_doctor), providerId),
				builder.equal(encounterChartJoin.get(Chart_.chartPatientid), patientId)
				);

		cq.multiselect(root.get(Encounter_.encounterId));
		
		count = em.createQuery(cq).getResultList().size();
		
		return count;
		
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

	
	
	public List<MedicationOrder> getMedicationQDM(EntityManager em,Boolean considerProvider,int providerId,String rxNormCodes,int patientId, int range) {
	       
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
        
        Predicate byProvider=builder.equal(root.get(Prescription_.docPrescProviderId), providerId);
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
	
	public List<ActiveMedication> getActiveMedications(EntityManager em,Boolean considerProvider,int providerId,String rxNormCodes,int patientId, int range) {

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
		
		Predicate byProvider=builder.equal(root.get(Prescription_.docPrescProviderId), providerId);
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
	public List<InvestigationQDM> getInvestigationQDM(EntityManager em,Boolean considerProvider, int patientID, int providerId, Date startDate, Date endDate) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<InvestigationQDM> cq = builder.createQuery(InvestigationQDM.class);
		
		Root<LabEntries> root = cq.from(LabEntries.class);
		
		Join<LabEntries, Encounter> EncLabJoin = root.join(LabEntries_.encounter, JoinType.INNER);
		Join<Encounter, Chart> EncChartJoin = EncLabJoin.join(Encounter_.chartTable, JoinType.INNER);
		Join<LabEntries, LabDescription> labEntriesDescJoin = root.join(LabEntries_.labDescriptionTable, JoinType.INNER);
		Join<LabDescription, Hl7ExternalTestmapping> labDescExtrnTestMappingJoin = labEntriesDescJoin.join(LabDescription_.hl7ExternalTestmappingTable, JoinType.INNER);
		Join<Hl7ExternalTestmapping, Hl7ExternalTest> hl7ExtTestMappingJoin = labDescExtrnTestMappingJoin.join(Hl7ExternalTestmapping_.hl7ExternalTestTable, JoinType.INNER);
		Expression<String> labCode = builder.<String>selectCase().when(labEntriesDescJoin.get(LabDescription_.labDescriptionLoinc).isNotNull(),labEntriesDescJoin.get(LabDescription_.labDescriptionLoinc)).otherwise(hl7ExtTestMappingJoin.get(Hl7ExternalTest_.hl7ExternalTestCode));
		
		Selection[] selections= new Selection[] {
				root.get(LabEntries_.labEntriesStatus),
				root.get(LabEntries_.labEntriesConfirmTestStatus),
				root.get(LabEntries_.labEntriesTestdetailId),
				labCode,
				labEntriesDescJoin.get(LabDescription_.labDescriptionTestDesc),
				root.get(LabEntries_.labEntriesTestStatus),
				hl7ExtTestMappingJoin.get(Hl7ExternalTest_.hl7ExternalTestLabcompanyid),
				root.get(LabEntries_.labEntriesOrdOn),
				root.get(LabEntries_.labEntriesPerfOn),
		};
		
		cq.select(builder.construct(InvestigationQDM.class,selections));
		Predicate[] restrictions= new Predicate[] {
					builder.equal(EncChartJoin.get(Chart_.chartPatientid), patientID),
					builder.notEqual(root.get(LabEntries_.labEntriesTestStatus), 2),
					builder.notEqual(root.get(LabEntries_.labEntriesTestStatus), 7),
					builder.between(root.get(LabEntries_.labEntriesPerfOn), startDate, endDate),
					builder.equal(hl7ExtTestMappingJoin.get(Hl7ExternalTest_.hl7ExternalTestIsactive), true),
					hl7ExtTestMappingJoin.get(Hl7ExternalTest_.hl7ExternalTestLabcompanyid).in(54, 51),
					labCode.isNotNull(),
			};
		
		cq.where(restrictions);
		
		List<InvestigationQDM> procedureForLabs = new ArrayList<InvestigationQDM>();
		
		try{
			
			procedureForLabs = em.createQuery(cq).getResultList();
			
		}catch(Exception e){
			e.printStackTrace();			
		}

		List<Integer> labEntriesTestDetailId=new ArrayList<Integer>();
		labEntriesTestDetailId.add(000000);
		for(int i=0;i<procedureForLabs.size();i++){
			labEntriesTestDetailId.add(procedureForLabs.get(i).getLabEntriesTestdetailId());
		}
		CriteriaBuilder builder1 = em.getCriteriaBuilder();
		CriteriaQuery<ParameterDetails> cq1 = builder1.createQuery(ParameterDetails.class);
		
		Root<LabEntriesParameter> root1 = cq1.from(LabEntriesParameter.class);
		Predicate byIsActive=builder.equal(root1.get(LabEntriesParameter_.labEntriesParameterIsactive), true);
		Join<LabEntriesParameter, LabParameters> joinLabentriesparameterLabparameter = root1.join(LabEntriesParameter_.labParametersTable, JoinType.INNER);
		Predicate byIsActive1=builder.equal(joinLabentriesparameterLabparameter.get(LabParameters_.labParametersIsactive), true);
		joinLabentriesparameterLabparameter.on(byIsActive1);
		Join<LabParameters, LabParameterCode> joinLabparametersLabparametercode=joinLabentriesparameterLabparameter.join(LabParameters_.labParamCode, JoinType.INNER);
		Selection[] selection= new Selection[] {
				root1.get(LabEntriesParameter_.labEntriesParameterTestdetailid),
				joinLabparametersLabparametercode.get(LabParameterCode_.labParameterCodeValue),
				root1.get(LabEntriesParameter_.labEntriesParameterValue)
		};
		cq1.select(builder1.construct(ParameterDetails.class,selection));
		Predicate byCode=builder1.equal(joinLabparametersLabparametercode.get(LabParameterCode_.labParameterCodeSystem), builder1.literal("LOINC"));
		joinLabparametersLabparametercode.on(byCode);
		
		Predicate predicateForParameters = root1.get(LabEntriesParameter_.labEntriesParameterTestdetailid).in(labEntriesTestDetailId);
		Predicate predicateForParametersByDate = builder.between(root1.get(LabEntriesParameter_.labEntriesParameterDate), startDate, endDate);
		
		cq1.where(byIsActive,predicateForParameters,predicateForParametersByDate);
		List<ParameterDetails> parameterDetails=em.createQuery(cq1).getResultList();
		List<InvestigationQDM> completeInvestigationDetails=new ArrayList<InvestigationQDM>();
		String code="";
		String codeDescription;
		Integer status;
		Integer companyId;
		Date createdOn;
		Date performeOn;
		String resultValue;
		InvestigationQDM investigationObj=null;
		for(int i=0;i<procedureForLabs.size();i++){
			for(int j=0;j<parameterDetails.size();j++){
					
					if(procedureForLabs.get(i).getLabEntriesTestdetailId()==parameterDetails.get(j).getLab_entries_parameter_testdetailid()){
					if(parameterDetails.get(j).getLab_parameter_code_value()!=null)
					code=parameterDetails.get(j).getLab_parameter_code_value();
					}
					else
					code=procedureForLabs.get(i).getCode();
					codeDescription=procedureForLabs.get(i).getCodeDescription();
					status=procedureForLabs.get(i).getStatus();
					companyId=procedureForLabs.get(i).getCompanyId();
					createdOn=procedureForLabs.get(i).getCreatedOn();
					performeOn=procedureForLabs.get(i).getPerformeOn();
					if(procedureForLabs.get(i).getLabEntriesConfirmTestStatus()!=0)
					resultValue=Integer.toString(procedureForLabs.get(i).getLabEntriesConfirmTestStatus());
					else
					resultValue=Integer.toString(procedureForLabs.get(i).getLabEntriesStatus());
					if(procedureForLabs.get(i).getLabEntriesTestdetailId()==parameterDetails.get(j).getLab_entries_parameter_testdetailid())
					resultValue=parameterDetails.get(j).getLab_entries_parameter_value();
					investigationObj=new InvestigationQDM(resultValue, code, codeDescription, status, companyId, createdOn, performeOn);
					
					
				
				
			}
			
			if(parameterDetails.size() == 0){
				completeInvestigationDetails = procedureForLabs;
			}else{
				completeInvestigationDetails.add(investigationObj);
			}
			
		}
		
		return completeInvestigationDetails;
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
			if(obj.get(i).getOrderedDate()!=null)
			interventionObj.setStartDate(obj.get(i).getOrderedDate());
			else
			interventionObj.setStartDate(obj.get(i).getRefDate());
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
	
	private void getPatientInfoQDM(EntityManager em,PatientRegistrationRepository patientRepo, int patientId){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<PatientRegistration> root = cq.from(PatientRegistration.class);
		cq.multiselect(root.get(PatientRegistration_.patientRegistrationId),root.get(PatientRegistration_.patientRegistrationFirstName),root.get(PatientRegistration_.patientRegistrationLastName),root.get(PatientRegistration_.patientRegistrationDob),root.get(PatientRegistration_.patientRegistrationSex));
		Predicate byPatient=builder.equal(root.get(PatientRegistration_.patientRegistrationId), patientId);
		cq.where(byPatient);
		List<Object[]> result = em.createQuery(cq).setMaxResults(1).getResultList();
		patientObj.setPatientId(Integer.parseInt((result.get(0)[0].toString())));
		patientObj.setFirstName(result.get(0)[1].toString());
		patientObj.setLastName(result.get(0)[2].toString());
		patientObj.setDob((Date)result.get(0)[3]);
		
		if((int)result.get(0)[4] == 2){
			patientObj.setGender("F");
		}else if((int)result.get(0)[4] == 1){
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
	public List<ReferralQDM> getReferrals(EntityManager em,Boolean considerProvider,int providerId,Integer patientId, Date startDate, Date endDate) {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ReferralQDM> cq = builder.createQuery(ReferralQDM.class);
		Root<H413> rooth413 = cq.from(H413.class);
		Selection[] selections= new Selection[] {
				rooth413.get(H413_.h413001).alias("Referral id"),
				rooth413.get(H413_.h413004).alias("Referred Date"),
				rooth413.get(H413_.referralOrderOn).alias("Ordered Date"),
				rooth413.get(H413_.referralReviewedOn).alias("Reviewed On"),
				rooth413.get(H413_.h413041).alias("Status")
		};
		
		cq.select(builder.construct(ReferralQDM.class,selections));
		
		Predicate predicateByPatientId=builder.equal(rooth413.get(H413_.h413035),patientId);
		Predicate byProvider=builder.equal(rooth413.get(H413_.referralOrderBy), providerId);
		Predicate byDateRange = builder.between(rooth413.get(H413_.referralOrderOn), startDate, endDate);
		
		if(considerProvider)
			cq.where(predicateByPatientId,byProvider,byDateRange);
		else
			cq.where(predicateByPatientId,byDateRange);
		
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
	
	public List<Immunization> getImmuDetails(EntityManager em,Boolean considerProvider,int providerId,Integer patientId, Date startDate, Date endDate)
	{
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ImmunizationDetailsBean> cq = builder.createQuery(ImmunizationDetailsBean.class);
		
		Root<LabDescription> rootlabDescription= cq.from(LabDescription.class);
		Join<LabDescription, LabEntries> joinLabdescLabentries=rootlabDescription.join(LabDescription_.labEntriesTable,JoinType.INNER);
		Join<LabEntries, Encounter> EncLabJoin = joinLabdescLabentries.join(LabEntries_.encounter, JoinType.INNER);
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
		Predicate byProvider=builder.equal(EncLabJoin.get(Encounter_.encounter_service_doctor), providerId);
		Predicate byDateRange = builder.between(joinLabdescLabentries.get(LabEntries_.labEntriesPerfOn), startDate, endDate);
		
		cq.where(predicateBytestStatus,predicateBytestStatus1,byDateRange);
		
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
	
	/*public List<MedicationQDM> getMedicationsReviewed(EntityManager em,Boolean considerProvider,int providerId,int patientId,Date date1,Date date2){
		CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
        Root<Encounter> root = cq.from(Encounter.class);
        Join<Encounter, Chart> encChartJoin=root.join(Encounter_.chartTable,JoinType.INNER);
        
        encChartJoin.on(builder.equal(encChartJoin.get(Chart_.chartPatientid),patientId));
        Predicate date=builder.between(root.get(Encounter_.encounterDate), date1, date2);
        Predicate byProvider=builder.equal(root.get(Encounter_.encounter_service_doctor), providerId);
        Predicate attest=builder.gt(builder.length(builder.trim(root.get(Encounter_.medicationAttestationStatus))), 0);
        cq.multiselect(root.get(Encounter_.encounterDate),root.get(Encounter_.medicationAttestationStatus));
        if(considerProvider)
        	cq.where(date,attest,byProvider);
        else
        cq.where(date,attest);
        
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
    }*/
	
	public List<Procedure> getMedicationsReviewed(EntityManager em,Boolean considerProvider,int providerId,int patientId,Date date1,Date date2){
		
		List<Procedure> reviewedVisits = new ArrayList<Procedure>();
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
        Root<Encounter> root = cq.from(Encounter.class);
        
        Join<Encounter, Chart> encChartJoin=root.join(Encounter_.chartTable,JoinType.INNER);
        encChartJoin.on(builder.equal(encChartJoin.get(Chart_.chartPatientid),patientId));
        
        Predicate date = builder.between(root.get(Encounter_.encounterDate), date1, date2);
        Predicate byProvider = builder.equal(root.get(Encounter_.encounter_service_doctor), providerId);
        Predicate attest = builder.gt(builder.length(builder.trim(root.get(Encounter_.medicationAttestationStatus))), 0);
        
        cq.multiselect(root.get(Encounter_.encounterDate),root.get(Encounter_.medicationAttestationStatus));
        
        if(considerProvider)
        	cq.where(date,attest,byProvider);
        else
        	cq.where(date,attest);
        
        List<Object[]> result=em.createQuery(cq).getResultList();
        
        for(int i=0;i<result.size();i++){
        	
        	Procedure proc = new Procedure();
        	
        	if((result.get(i)[1].toString()).equals("428191000124101")){
        		
        		proc.setCode("428191000124101");
        		proc.setDescription("Documentation of current medications (procedure)");
        		proc.setCodeSystemOID("2.16.840.1.113883.6.96");
        		proc.setStatus(2);
        		proc.setStartDate((Date)result.get(i)[0]);
        		proc.setEndDate((Date)result.get(i)[0]);
        		
        		reviewedVisits.add(proc);
        		
        	}else if((result.get(i)[1].toString()).equals("183932001")){
        		
        		proc.setCode("183932001");
        		proc.setDescription("Documentation of current medications (procedure)");
        		proc.setCodeSystemOID("2.16.840.1.113883.6.96");
        		proc.setStatus(2);
        		proc.setStartDate((Date)result.get(i)[0]);
        		proc.setEndDate((Date)result.get(i)[0]);
        		
        		reviewedVisits.add(proc);
        		
        	}
        	
        }
        
        return reviewedVisits;
        
	}
	
	@SuppressWarnings("rawtypes")
	public List<ClinicalDataQDM> getClinicalDataQDM(EntityManager em,Boolean considerProvider,int providerId,int patientId, String snomedCodes,String loincCodes,Boolean range, Date startDate, Date endDate)    {     
		
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
			if(range){
				cqForSnomed.where(restrictionsWithDate);
			}
			else{
				cqForSnomed.where(restrictions);
			}
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

	@SuppressWarnings({ "rawtypes", "serial" })
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
	
	/*
	 * 	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  * * * * * * * * * * * * * * * * * * EP Measure Related Code Start * * * * * * * * * * * * * * * * * * * 
	 *  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 */
	
	/**
	 * 
	 * Function to check whether a patient has been by the provider during reporting period or not
	 * 
	 * @param patientId
	 * @param providerId
	 * @param em
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	
	public boolean checkIsVisited(boolean isGroup, int patientId, int providerId, EntityManager em, Date startDate, Date endDate){
		
		boolean result = false;
		
		try{
			
			CriteriaBuilder builder1 = em.getCriteriaBuilder();
			CriteriaQuery<String> cq1 = builder1.createQuery(String.class);
			Root<MeaningfuluseSettings> root1 = cq1.from(MeaningfuluseSettings.class);
			
			cq1.multiselect(builder1.coalesce(root1.get(MeaningfuluseSettings_.meaningfuluseSettingsOptionValue), "false"));
			
			cq1.where( builder1.equal(root1.get(MeaningfuluseSettings_.meaningfuluseSettingsType), "EP_RULES"), 
					builder1.equal(root1.get(MeaningfuluseSettings_.meaningfuluseSettingsOptionId), 1));
			
			String allowOnlyOfficeVisits = em.createQuery(cq1).getResultList().get(0);
			
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = builder.createQuery(Long.class);
			Root<Encounter> root = cq.from(Encounter.class);
			
			if(allowOnlyOfficeVisits.equals("t") || allowOnlyOfficeVisits.equals("true")){
				
				String cptCodes = "99201,99202,99203,99204,99205,99212,99213,99214,99215,99241,99242,99243,99244,99245,99324,99325,99326,99327,99328,99334,99335,99336,99337,99341,99342,99343,99344,99345,99347,99348,99349,99350,99401,99402,99403,99404,99406,99407,99408,99409,99411,99412,99420,99429,99381,99382,99383,99384,99385,99386,99387,99391,99392,99393,99394,99395,99396,99397,99212-MD,99213-MD,99214-MD,99215-MD,99212-RN,99213-RN,99214-RN,99215-RN";
				
				Join<Encounter, Chart> chartJoin = root.join(Encounter_.chartTable, JoinType.INNER);
				Join<Chart, ServiceDetail> chartServiceDetailJoin = chartJoin.join(Chart_.serviceDetail, JoinType.INNER);
				Join<ServiceDetail, Cpt> cptJoin = chartServiceDetailJoin.join(ServiceDetail_.cpt, JoinType.INNER);
				
				cq.multiselect(builder.countDistinct(root.get(Encounter_.encounterId)));
				
				Predicate byEncDate = builder.between(root.get(Encounter_.encounterDate), startDate, endDate);
				Predicate byProviderId = builder.equal(root.get(Encounter_.encounter_service_doctor), providerId);
				Predicate byEncType = builder.equal(root.get(Encounter_.encounterType), 1);
				Predicate byPatientId = builder.equal(chartJoin.get(Chart_.chartPatientid), patientId);
				Predicate byCptType = builder.equal(cptJoin.get(Cpt_.cptCpttype), 1);
				Predicate byDOS = builder.between(builder.function("DATE", Date.class, chartServiceDetailJoin.get(ServiceDetail_.serviceDetailDos)), startDate, endDate);				
				Predicate byEnMCodes = cptJoin.get(Cpt_.cptCptcode).in(Arrays.asList(cptCodes.split(",")));
				
				if(isGroup)
					cq.where(byEncDate,byProviderId,byPatientId,byEncType,byCptType,byDOS,byEnMCodes);
				else
					cq.where(byEncDate,byPatientId,byEncType,byCptType,byDOS,byEnMCodes);
				
			}else{
				
				Join<Encounter, Chart> chartJoin = root.join(Encounter_.chartTable, JoinType.INNER);
				
				cq.multiselect(builder.countDistinct(root.get(Encounter_.encounterId)));
				
				Predicate byEncDate = builder.between(root.get(Encounter_.encounterDate), startDate, endDate);
				Predicate byProviderId = builder.equal(root.get(Encounter_.encounter_service_doctor), providerId);
				Predicate byEncType = builder.equal(root.get(Encounter_.encounterType), 1);
				Predicate byPatientId = builder.equal(chartJoin.get(Chart_.chartPatientid), patientId);
				
				if(isGroup)
					cq.where(byEncDate,byProviderId,byPatientId,byEncType);
				else
					cq.where(byEncDate,byPatientId,byEncType);
				
			}
			
			List<Long> encounterCount = em.createQuery(cq).getResultList();
			
			if(encounterCount.get(0) > 0){
				
				result = true;
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	/**
	 * Function to get patient portal access for an active patient
	 * 
	 * @param patientId
	 * @param em
	 * @return
	 */
	
	public boolean getPatientPortalAccess(int patientId, EntityManager em){
		
		boolean result = false;
		
		try{
			
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Boolean> cq = builder.createQuery(Boolean.class);
			Root<PatientRegistration> root = cq.from(PatientRegistration.class);
			
			cq.select(builder.coalesce(root.get(PatientRegistration_.patientRegistrationIspatientportal), false));
			
			cq.where(builder.equal(root.get(PatientRegistration_.patientRegistrationId), patientId), 
					builder.equal(root.get(PatientRegistration_.patientRegistrationActive), true));
			
			result = em.createQuery(cq).getResultList().get(0);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	/**
	 * Function to get medications prescribed by a provider during reporting period for that patient
	 * 
	 * @param patientId
	 * @param providerId
	 * @param em
	 * @param startDate
	 * @param endDate
	 * @param measureObj
	 */
	
	public String getEPrescribingDetails(boolean isGroup, int patientId, int providerId, EntityManager em, Date startDate, Date endDate, MeasureStatus measureObj){
		
		int electronicallySentMedications = 0, ePrescStatus = -1, i = 0;
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<Prescription> root = cq.from(Prescription.class);
		
		Predicate byProviderId  = builder.equal(root.get(Prescription_.docPrescOrderedBy), providerId);
		Predicate byOrderedDate = builder.between(root.get(Prescription_.docPrescOrderedDate), startDate, endDate);
		Predicate byIsActive 	= builder.equal(root.get(Prescription_.docPrescIsActive), true);
		Predicate byPatientId	= builder.equal(root.get(Prescription_.docPrescPatientId), patientId);
		
		cq.multiselect(root.get(Prescription_.rxname).alias("Medication"),
				builder.coalesce(root.get(Prescription_.docPrescIsEPrescSent),0).alias("IsSentElectronically"));
		
		if(isGroup)
			cq.where(byProviderId,byOrderedDate,byPatientId,byIsActive);
		else
			cq.where(byOrderedDate,byPatientId,byIsActive);
		
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
		
		measureObj.setIpp(i);
		measureObj.setDenominator(i);
		
		if(ePrescStatus == 1){
			measureObj.setNumerator(electronicallySentMedications);
		}
		
		return electronicallySentMedications+" / "+i+" &&&& medications sent electronically &&&& "+ePrescStatus;
		
	}
	
	/**
	 * 
	 * Function to get total no of messages sent by the provider to that patient during calendar year
	 * 
	 * @param isGroup
	 * @param providerId
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @param em
	 * @param measureObj
	 * @return
	 */
	
	@SuppressWarnings("rawtypes")
	public String getSecureMessagingInfo(Boolean isGroup,int providerId, int patientId, Date startDate, Date endDate, EntityManager em, MeasureStatus measureObj){
		
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
			
			Predicate byProviderId  = builder.equal(root.get(PortalMessage_.messageBy), providerId); 
			Predicate byRepPeriod 	= builder.between(builder.function("DATE", Date.class, root.get(PortalMessage_.mdate)), startDate, endDate);
			Predicate byPatientId	= builder.equal(root.get(PortalMessage_.messageTo), patientId);
			
			if(isGroup)
				cq.where(byProviderId,byRepPeriod, byPatientId);
			else
				cq.where(byRepPeriod, byPatientId);
			
			cq.groupBy(root.get(PortalMessage_.portalMessageHasreplied));
			
			List<Object[]> result = em.createQuery(cq).getResultList();
			
			if(result.size() == 2){
			
				int total = Integer.parseInt(result.get(0)[1].toString())+Integer.parseInt(result.get(1)[1].toString());
				
				returnString = "No. of messages sent: "+total;
				
				measureObj.setIpp(1);
				measureObj.setDenominator(1);
				measureObj.setNumerator(1);
				
			}else if(result.size() == 1){
				
				measureObj.setIpp(1);
				measureObj.setDenominator(1);
				measureObj.setNumerator(1);
				
				if(result.get(0)[0].equals("t")){
					returnString = "No. of messages sent: "+result.get(0)[1];
				}else{
					returnString = "No. of messages sent: "+result.get(0)[1];
				}
				
			}else{
				
				measureObj.setIpp(1);
				measureObj.setDenominator(1);
				
				returnString = "No. of messages sent: 0";
				
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			returnString = "No. of messages sent: 0";
			
		}
		
		return returnString;
		
	}
	
	/**
	 * Function to get patient portal usage status for the patient during the calendar year
	 * 
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @param em
	 * @param measureObj
	 * @return
	 */
	
	public String getPatientAccessInfoForPortal(int patientId, Date startDate, Date endDate, EntityManager em, MeasureStatus measureObj){
		
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

			measureObj.setIpp(1);
			measureObj.setDenominator(1);
			measureObj.setNumerator(1);
			
			if(count > 0){
				
				result = accessedTime.get(0);
				
			}else{
				
				result = "";
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
			result = "";
		}
		
		return result;
		
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
	
	public String getPatientElectronicAccessInfo(Boolean isGroup, int patientId, int providerId, Date startDate, Date endDate, EntityManager em, MeasureStatus measureObj){
		
		long patientCount = 0;
		
		String lastAccessedTime = "";
		
		try{
			
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<String> cq = builder.createQuery(String.class);
			Root<Chart> root = cq.from(Chart.class);
			
			Join<Chart, Encounter> encounterTable = root.join(Chart_.encounterTable, JoinType.INNER);
			Join<Chart, DirectEmailLog> directEmailLogTable = root.join(Chart_.directEmailLogTable, JoinType.INNER);
			
			List<Integer> directEmailAction = new ArrayList<Integer>();
			directEmailAction.add(0, 1);
			directEmailAction.add(1, 2);
			
			Predicate[] restrictions= new Predicate[] {
					
				directEmailLogTable.get(DirectEmailLog_.directEmailLogActionType).in(directEmailAction),
				builder.between(builder.function("DATE", Date.class, encounterTable.get(Encounter_.encounterDate)), startDate, endDate),
				builder.between(builder.function("DATE", Date.class, directEmailLogTable.get(DirectEmailLog_.directEmailLogSentOn)), startDate, endDate),
				builder.equal(directEmailLogTable.get(DirectEmailLog_.directEmailLogSentBy), patientId),
				builder.equal(directEmailLogTable.get(DirectEmailLog_.directEmailLogUserType), 2)
					
			};
				
			Predicate[] restriction=new Predicate[] {
						
				directEmailLogTable.get(DirectEmailLog_.directEmailLogActionType).in(directEmailAction),
				builder.between(builder.function("DATE", Date.class, encounterTable.get(Encounter_.encounterDate)), startDate, endDate),
				builder.between(builder.function("DATE", Date.class, directEmailLogTable.get(DirectEmailLog_.directEmailLogSentOn)), startDate, endDate),
				builder.equal(directEmailLogTable.get(DirectEmailLog_.directEmailLogSentBy), patientId),
				builder.equal(encounterTable.get(Encounter_.encounter_service_doctor), providerId),
				builder.equal(directEmailLogTable.get(DirectEmailLog_.directEmailLogUserType), 2)
						
			};
			
			cq.multiselect(builder.function("to_char", String.class, directEmailLogTable.get(DirectEmailLog_.directEmailLogSentOn), builder.literal("MM/DD/YYYY HH:MI:SS am")));
			
			cq.orderBy(builder.desc(directEmailLogTable.get(DirectEmailLog_.directEmailLogSentOn)));
			
			if(isGroup)
				cq.where(restrictions);
			else
				cq.where(restriction);
			
			List<String> queryResult = em.createQuery(cq).getResultList();
			
			patientCount = queryResult.size();
			
			measureObj.setIpp(1);
			measureObj.setDenominator(1);
			
			if(patientCount > 0){
				
				measureObj.setNumerator(1);
				lastAccessedTime = queryResult.get(0);
				
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			patientCount = 0;
			
		}
		
		
		return lastAccessedTime;
		
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
	public String getReferralInfoExchangeByProvider(Boolean isGroup,int providerId, int patientId, Date startDate, Date endDate, EntityManager em, MeasureStatus measureObj){

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
			
			if(isGroup)
				cq.where(builder.equal(root.get(H413_.referralById), providerId),builder.between(builder.function("DATE", Date.class, root.get(H413_.referralOrderOn)), startDate, endDate), builder.equal(root.get(H413_.h413035), patientId));
			else
				cq.where(builder.between(builder.function("DATE", Date.class, root.get(H413_.referralOrderOn)), startDate, endDate), builder.equal(root.get(H413_.h413035), patientId));
			
			cq.groupBy(root.get(H413_.summaryCareRecordProvidedElectronic));
			
			List<Object[]> result = em.createQuery(cq).getResultList();
			
			
			if(result.size() == 2){
				
				int total = Integer.parseInt(result.get(0)[1].toString())+Integer.parseInt(result.get(1)[1].toString());
				
				measureObj.setIpp(total);
				measureObj.setDenominator(total);
				measureObj.setNumerator(Integer.parseInt(result.get(0)[1].toString()));
				
				returnString = result.get(0)[1]+" / "+total+" &&&& referrals sent electronically";
				
			}else if(result.size() == 1){
				
				System.out.println(" "+result.get(0)[0]+" and "+result.get(0)[1]);
				
				if(result.get(0)[0].toString().equals("1")){

					returnString = result.get(0)[1]+" / "+result.get(0)[1]+" &&&& referrals sent electronically";
					
					measureObj.setIpp(Integer.parseInt(result.get(0)[1].toString()));
					measureObj.setDenominator(Integer.parseInt(result.get(0)[1].toString()));
					measureObj.setNumerator(Integer.parseInt(result.get(0)[1].toString()));
					
				}else{
					
					returnString = "0 / "+result.get(0)[1]+" &&&& referrals sent electronically";
					
					measureObj.setIpp(Integer.parseInt(result.get(0)[1].toString()));
					measureObj.setDenominator(Integer.parseInt(result.get(0)[1].toString()));
				}
				
			}else{
				
				returnString = "No referrals have been sent by provider";
				
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			returnString = "No referrals have been sent by provider";
			
		}
		
		return returnString;
		
	}

	/**
	 * Function provides the details of handouts given to patients by provider during reporting year
	 * 
	 * @param isGroup
	 * @param providerId
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @param em
	 * @param measureObj
	 * @return
	 */
	
	public String getPatientEducationDetails(Boolean isGroup, int providerId, int patientId, Date startDate, Date endDate, EntityManager em, MeasureStatus measureObj) {
		
		String returnString = "";
		
		try{
			
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq = builder.createQuery(Object.class);
			Root<PatientAftercareData> root = cq.from(PatientAftercareData.class);
			
			Join<PatientAftercareData, Encounter> encounterJoin = root.join(PatientAftercareData_.encounterTbl, JoinType.INNER);
			
			cq.multiselect(builder.function("to_char", String.class, encounterJoin.get(Encounter_.encounterDate), builder.literal("MM/DD/YYYY HH:MI:SS am")));
			
			if(isGroup){
				cq.where(builder.between(encounterJoin.get(Encounter_.encounterDate), startDate, endDate),
					builder.equal(encounterJoin.get(Encounter_.encounter_service_doctor), providerId),	
					builder.equal(root.get(PatientAftercareData_.patientAftercareDataPatientId), patientId));
			}else{
				cq.where(builder.between(encounterJoin.get(Encounter_.encounterDate), startDate, endDate),
						builder.equal(root.get(PatientAftercareData_.patientAftercareDataPatientId), patientId));
			}
			
			cq.orderBy(builder.desc(encounterJoin.get(Encounter_.encounterDate)));
			
			List<Object> handoutsCount = em.createQuery(cq).getResultList();
			
			measureObj.setIpp(1);
			measureObj.setDenominator(1);
			
			if(handoutsCount.size() > 0){
				returnString = "Handout provided to patient on : "+handoutsCount.get(0);
				measureObj.setNumerator(1);
			}else{
				returnString = "No handouts have been given by provider";
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			returnString = "No handouts have been given by provider";
			
		}
		
		return returnString;
		
	}
	
	/**
	 * 
	 * Function provides the information regarding no. of visits marked as transition of care and no.of visits has reconciled medications
	 * 
	 * @param isGroup
	 * @param providerId
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @param em
	 * @param measureObj
	 * @return
	 */
	
	public String getMedicationReconcilcationStatus(Boolean isGroup, int providerId, int patientId, Date startDate, Date endDate, EntityManager em, MeasureStatus measureObj){
		
		String resultString = "";

		try{
			
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Integer> cq = builder.createQuery(Integer.class);
			Root<Chart> root = cq.from(Chart.class);
			
			Join<Chart, Encounter> chartJoin = root.join(Chart_.encounterTable, JoinType.INNER);
			
			Predicate byProvider  = builder.equal(chartJoin.get(Encounter_.encounter_service_doctor), providerId);
			Predicate byPatientId = builder.equal(root.get(Chart_.chartPatientid), patientId);
			Predicate byDateRange = builder.between(chartJoin.get(Encounter_.encounterDate), startDate, endDate);
			Predicate isTransitionOfCareChecked = builder.equal(chartJoin.get(Encounter_.transitionOfCare), true);
			
			if(isGroup)
				cq.where(byProvider, byPatientId, byDateRange, isTransitionOfCareChecked);
			else
				cq.where(byPatientId, byDateRange, isTransitionOfCareChecked);
			
			cq.multiselect(chartJoin.get(Encounter_.encounterId)).distinct(true);
			
			List<Integer> encountersWithTransitionOfCare = em.createQuery(cq).getResultList();
			
			if(encountersWithTransitionOfCare.size() == 0){
				
				resultString = "No Encounters with Transition of Care marked";
				
			}else{
				
				CriteriaQuery<Integer> cq1 = builder.createQuery(Integer.class);
				Root<ReconciledMedication> root1 = cq1.from(ReconciledMedication.class);
				
				cq1.where(root1.get(ReconciledMedication_.reconciledMedicationEncounterid).in(encountersWithTransitionOfCare));
				
				cq1.multiselect(root1.get(ReconciledMedication_.reconciledMedicationEncounterid)).distinct(true);
				
				List<Integer> finalCount =  em.createQuery(cq1).getResultList();
				
				if(finalCount.size() == 0){
					
					measureObj.setIpp(encountersWithTransitionOfCare.size());
					measureObj.setDenominator(encountersWithTransitionOfCare.size());
					
					resultString = "0 / "+encountersWithTransitionOfCare.size()+" &&&& visits in which medication reconcilation is performed";
					
				}else{
					
					measureObj.setIpp(encountersWithTransitionOfCare.size());
					measureObj.setDenominator(encountersWithTransitionOfCare.size());
					measureObj.setNumerator(finalCount.size());
					
					resultString = finalCount.size()+" / "+encountersWithTransitionOfCare.size()+" &&&& visits in which medication reconcilation is performed";
					
				}
				
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			resultString = "No Encounters with Transition of Care marked";
			
		}
		
		return resultString;
		
	}
	
}