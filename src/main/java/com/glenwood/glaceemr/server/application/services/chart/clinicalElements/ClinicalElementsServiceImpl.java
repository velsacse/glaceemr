package com.glenwood.glaceemr.server.application.services.chart.clinicalElements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.ClinicalElementTemplateMapping;
import com.glenwood.glaceemr.server.application.models.ClinicalElements;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsCondition;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsCondition_;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions_;
import com.glenwood.glaceemr.server.application.models.ClinicalTextMapping;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.EncounterPlan;
import com.glenwood.glaceemr.server.application.models.LeafLibrary;
import com.glenwood.glaceemr.server.application.models.LeafPatient;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements_;
import com.glenwood.glaceemr.server.application.models.PatientClinicalHistory;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.repositories.ClinicalElementTemplateMappingRepository;
import com.glenwood.glaceemr.server.application.repositories.ClinicalElementsOptionsRepository;
import com.glenwood.glaceemr.server.application.repositories.ClinicalElementsRepository;
import com.glenwood.glaceemr.server.application.repositories.ClinicalTextMappingRepository;
import com.glenwood.glaceemr.server.application.repositories.EncounterEntityRepository;
import com.glenwood.glaceemr.server.application.repositories.EncounterPlanRepository;
import com.glenwood.glaceemr.server.application.repositories.LeafLibraryRepository;
import com.glenwood.glaceemr.server.application.repositories.LeafPatientRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientClinicalElementsRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientClinicalHistoryRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientVitalsRepository;
import com.glenwood.glaceemr.server.application.services.chart.HPI.ClinicalElementsOptionBean;
import com.glenwood.glaceemr.server.application.specifications.ClinicalElementsSpecification;
import com.glenwood.glaceemr.server.application.specifications.EncounterEntitySpecification;
import com.glenwood.glaceemr.server.application.specifications.EncounterSpecification;
import com.glenwood.glaceemr.server.application.specifications.LeafLibrarySpecification;
import com.glenwood.glaceemr.server.application.specifications.LeafPatientSpecfication;
import com.glenwood.glaceemr.server.application.specifications.PatientClinicalElementsSpecification;
import com.glenwood.glaceemr.server.application.specifications.PatientRegistrationSpecification;
import com.glenwood.glaceemr.server.utils.DateUtil;
import com.glenwood.glaceemr.server.utils.HUtil;


@Service
public class ClinicalElementsServiceImpl implements ClinicalElementsService{

	
	@Autowired
	ClinicalElementsRepository clinicalElementsRepository;

	@Autowired
	PatientClinicalElementsRepository patientClinicalElementsRepository;

	@Autowired
	PatientClinicalHistoryRepository patientClinicalHistoryRepository;

	@Autowired
	EncounterEntityRepository encounterEntityRepository;

	@Autowired
	ClinicalTextMappingRepository clinicalTextMappingRepository;

	@Autowired
	ClinicalElementsOptionsRepository clinicalElementsOptionsRepository;

	@Autowired 
	PatientRegistrationRepository patientRegRepository;

	@Autowired
	ClinicalDataBean clinicalDataBean;

	@Autowired
	LeafLibraryRepository leafLibraryRepository;

	@Autowired
	LeafPatientRepository leafPatientRepository;

	@Autowired
	ClinicalElementTemplateMappingRepository clinicalElementTemplateMappingRepository;

	@Autowired
	EncounterPlanRepository encounterPlanRepository;
	
	@Autowired
	PatientVitalsRepository patientVitalsRepository;
	
	@PersistenceContext
	EntityManager em;


	Short patientSex=0;
	Date patDOB=new Date();
	Integer ageinDay=0;
	
	
	
	@Override
	public List<ClinicalElementsOptions> getClinicalElementOptions(String gwid) {
		return clinicalElementsOptionsRepository.findAll(ClinicalElementsSpecification.getclinicalElementOptions(gwid));

	}

	@Override
	public ClinicalDataBean setVitalsClinicalData(String gwidPattern,Integer patientId, Integer encounterId, Boolean isDischargeVitals,Integer admssEpisode, short patientSex, Integer ageinDay) {
		clinicalDataBean.setClinicalData(clinicalElementsRepository.findAll(Specifications.where(ClinicalElementsSpecification.getClinicalElementsByGWIDPattern(clinicalDataBean.clientId,gwidPattern,ageinDay, patientSex)).and(ClinicalElementsSpecification.defaultAgePed(-1)).or(ClinicalElementsSpecification.patientAgePed(ageinDay))));
		
		List<PatientClinicalElements> patientData=Collections.emptyList();
		List<PatientClinicalHistory> patientHistoryData=null;
		if(!isDischargeVitals.booleanValue()){
			List<Encounter> enc= encounterEntityRepository.findAll(EncounterEntitySpecification.getPrevEncHavingData(patientId,encounterId));
			if(enc.size()>0 && enc.get(0)!=null){
				if(patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getPatClinicalDataByGWIDPattern(clinicalDataBean.clientId,patientId,encounterId,gwidPattern)).size()>0){
					patientData=patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getNonHistoryElemPatientData(clinicalDataBean.clientId,patientId,encounterId,gwidPattern));
				}
				else{
					patientData=patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getPrevEncounterPatVitalData(clinicalDataBean.clientId,patientId, enc.get(0).getEncounterId(), gwidPattern));
				}
			}else{
				patientData=patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getNonHistoryElemPatientData(clinicalDataBean.clientId,patientId,encounterId,gwidPattern));
			}
			clinicalDataBean.setPatientClinicalData(patientData);
			patientHistoryData=patientClinicalHistoryRepository.findAll(PatientClinicalElementsSpecification.getHistoryElemPatientData(clinicalDataBean.clientId,patientId, gwidPattern));
			clinicalDataBean.setHistoryDatatoPatElementBean(patientHistoryData);
		}else{
			/*System.out.println("YES DISCHARGE VITALS");
			List<PatientVitals> patientVitals = patientVitalsRepository.findAll(DischargeVitalSpecification.getDichargeVitalsByEncounterId(patientId, encounterId, admssEpisode));
			clinicalDataBean.setPatientVitalsClinicalData(patientVitals);*/
		}
		
		return clinicalDataBean;
		
	}

	@Override
	public boolean isClinicalElemActive(String gwid){
		List<ClinicalElements> element=null;
		boolean isActive=false;

		if((element=clinicalElementsRepository.findAll(ClinicalElementsSpecification.getClinicalElement(gwid))).size()>0){
			isActive=Boolean.parseBoolean(HUtil.nz(element.get(0).getClinicalElementsIsactive(),false)+"");
		}
		return isActive;
	}

	@Override
	public ClinicalElements getClinicalElement(String gwid){
		List<ClinicalElements> element=null;
		if((element=clinicalElementsRepository.findAll(ClinicalElementsSpecification.getClinicalElement(gwid))).size()>0){
			return element.get(0);
		}else
			return null;
	}


	public void setClinicalDataBean(ClinicalDataBean clinicalDataBean) {
		this.clinicalDataBean = clinicalDataBean;
	}

	@Override
	public ClinicalTextMapping getClinicalTextMapping(String gwid) {
		List<ClinicalTextMapping> element=null;
		if((element=clinicalTextMappingRepository.findAll(ClinicalElementsSpecification.getClinicalTextMapping(gwid))).size()>0){
			return element.get(0);
		}else
			return null;
	}


	public void getPatientDetails(Integer patientId){
		List<PatientRegistration> patRegistration=patientRegRepository.findAll(PatientRegistrationSpecification.getPatientPersonalDetails(patientId));
		for (PatientRegistration patientRegistration : patRegistration) {
			patientSex=patientRegistration.getPatientRegistrationSex().shortValue();
			patDOB=patientRegistration.getPatientRegistrationDob();
		}
		SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy");
		ageinDay = (int)((DateUtil.dateDiff( DateUtil.DATE , DateUtil.getDate(mdyFormat.format(patDOB)) , new java.util.Date() )));
	}

	public List<ClinicalElements> setClinicalDataBean(Integer patientId,Integer encounterId,Integer templateId,Integer tabType,String gwidPattern){

		getPatientDetails(patientId);
		
		List<LeafLibrary> leafDetails=leafLibraryRepository.findAll(LeafLibrarySpecification.getLeafDetailsByTemplateId(templateId));
		List<LeafPatient> leafPatient=leafPatientRepository.findAll(LeafPatientSpecfication.getPatLeafByLeafIdAndEncId(leafDetails.get(0)==null?-1:leafDetails.get(0).getLeafLibraryId(),encounterId));
		boolean isAgeBased=leafDetails.get(0).getLeafLibraryIsagebased()==null?false:leafDetails.get(0).getLeafLibraryIsagebased();

		/*
		 * Set clinical elements to ClinicalDataBean
		 * 
		 * */

		List<ClinicalElements> clinicalelements=clinicalElementsRepository.findAll(ClinicalElementsSpecification.getClinicalElements(templateId, patientSex, tabType,leafPatient.get(0).getLeafPatientCreatedDate()==null?"-1":leafPatient.get(0).getLeafPatientCreatedDate().toString(), ageinDay,isAgeBased));
		clinicalDataBean.setClinicalData(clinicalelements);
		if(encounterId==-1){
			clinicalelements=clinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getPatClinicalHistoryData(clinicalDataBean.clientId,isAgeBased, patientId, patientSex, gwidPattern, ageinDay));
		}else{
			clinicalelements=clinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getEncPatientClinicalData(clinicalDataBean.clientId,isAgeBased, encounterId, patientSex, gwidPattern, ageinDay));
		}
		clinicalDataBean.setClinicalData(clinicalelements);

		/*
		 * set Patient clinical elements and patient History elements to PatientElementBean of ClinicalDataBean 
		 * 
		 * */

		List<PatientClinicalElements> patientData=patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getNonHistoryElemPatientData(clinicalDataBean.clientId,patientId,encounterId,gwidPattern));
		List<PatientClinicalHistory> patientHistoryData=patientClinicalHistoryRepository.findAll(PatientClinicalElementsSpecification.getHistoryElemPatientData(clinicalDataBean.clientId,patientId, gwidPattern));
		clinicalDataBean.setPatientClinicalData(patientData);
		clinicalDataBean.setHistoryDatatoPatElementBean(patientHistoryData);



		return clinicalelements;
	}




	public List<ClinicalElements> setHistoryClinicalDataBean(Integer patientId,Integer encounterId,Integer templateId,Integer tabType,String gwidPattern){

		getPatientDetails(patientId);
		
		List<LeafLibrary> leafDetails=leafLibraryRepository.findAll(LeafLibrarySpecification.getLeafDetailsByTemplateId(templateId));
		List<LeafPatient> leafPatient=leafPatientRepository.findAll(LeafPatientSpecfication.getPatLeafByLeafIdAndEncId(leafDetails.get(0)==null?-1:leafDetails.get(0).getLeafLibraryId(),encounterId));
		boolean isAgeBased=leafDetails.get(0).getLeafLibraryIsagebased()==null?false:leafDetails.get(0).getLeafLibraryIsagebased();

		/*
		 * Set clinical elements to ClinicalDataBean
		 * 
		 * */

		List<ClinicalElements> clinicalelements=clinicalElementsRepository.findAll(ClinicalElementsSpecification.getClinicalElements(templateId, patientSex, tabType,leafPatient.get(0).getLeafPatientCreatedDate()==null?"-1":leafPatient.get(0).getLeafPatientCreatedDate().toString(), ageinDay,isAgeBased));
		clinicalDataBean.setClinicalData(clinicalelements);
		if(encounterId==-1){
			clinicalelements=clinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getPatClinicalHistoryData(clinicalDataBean.clientId,isAgeBased, patientId, patientSex, gwidPattern, ageinDay));
		}else{
			clinicalelements=clinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getEncPatientClinicalData(clinicalDataBean.clientId,isAgeBased, encounterId, patientSex, gwidPattern, ageinDay));
		}
		clinicalDataBean.setClinicalData(clinicalelements);

		/*
		 * set Patient clinical elements or  patient History elements to PatientHistoryElementBean of ClinicalDataBean based on encounterId
		 * 
		 * */


		if(encounterId==-1){
			List<PatientClinicalHistory> patientHistoryData=patientClinicalHistoryRepository.findAll(PatientClinicalElementsSpecification.getHistoryElemPatientData(clinicalDataBean.clientId,patientId, gwidPattern));
			clinicalDataBean.setPatientHistoryData(patientHistoryData);
		}	else{
			List<PatientClinicalElements> patientData=patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getNonHistoryElemPatientData(clinicalDataBean.clientId,patientId,encounterId,gwidPattern));
			clinicalDataBean.setPatientClinData(patientData);
		}
		return clinicalelements;
	}



	@Override
	public List<String> delPatientElementByEncID(Integer patientId,Integer encounterId, Integer tabId,Integer templateId) {
		List<ClinicalElementTemplateMapping> mappedClinicalElementsList=clinicalElementTemplateMappingRepository.findAll(ClinicalElementsSpecification.getclinicalElementByTabAndTempId(tabId, templateId));
		ArrayList<String> mappedGwids=new ArrayList<String>();
		mappedGwids.add("-1");
		for (ClinicalElementTemplateMapping mapping : mappedClinicalElementsList) {
			mappedGwids.add(mapping.getClinicalElementTemplateMappingGwid());
		}
		patientClinicalElementsRepository.deleteInBatch(patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getPatClinicalDataByGWID(patientId, encounterId, mappedGwids)));
		return mappedGwids;
	}



	@Override
	public void deleteNotesData(Integer patientId, Integer encounterId,Integer tabId, Integer templateId) {
		String notesGWID=null;
		if(tabId==3){
			notesGWID="0000100400000000001";
		}if(tabId==5){
			notesGWID="0000200100000000000";
		}if(tabId==7){
			notesGWID="0000400000000000001";
		}
		if(notesGWID!=null)
			patientClinicalElementsRepository.deleteInBatch(patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getPatClinEleByGWID(encounterId, patientId, notesGWID)));
	}



	@Override
	public void insertDataForImport(Integer patientId,Integer encounterId,Integer prevEncounterId, Integer tabId, Integer templateId,List<String> mappedGwids) {
		List<PatientClinicalElements> patclinicalElems=patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getPatClinicalDataByGWID(patientId, prevEncounterId, mappedGwids));

		for (PatientClinicalElements patclincialElem : patclinicalElems) {
			PatientClinicalElements patElem=new PatientClinicalElements();
			patElem.setPatientClinicalElementsPatientid(patclincialElem.getPatientClinicalElementsPatientid());
			patElem.setPatientClinicalElementsChartid(patclincialElem.getPatientClinicalElementsChartid());
			patElem.setPatientClinicalElementsEncounterid(encounterId);
			patElem.setPatientClinicalElementsGwid(patclincialElem.getPatientClinicalElementsGwid());
			patElem.setPatientClinicalElementsValue(patclincialElem.getPatientClinicalElementsValue());
			patientClinicalElementsRepository.save(patElem);
		}

		patientClinicalElementsRepository.save(patclinicalElems);
		String notesGWID=null;
		if(tabId==3){
			if(isClinicalElemActive("0000100400000000001")){
				notesGWID="0000100400000000001";
			}
		}if(tabId==5){
			if(isClinicalElemActive("0000200100000000000")){
				notesGWID="0000200100000000000";
			}
		}if(tabId==7){
			if(isClinicalElemActive("0000400000000000001")){
				notesGWID="0000400000000000001";
			}
			List<EncounterPlan>  encounterPlan=encounterPlanRepository.findAll(EncounterSpecification.getEncounterPlanByEncId(prevEncounterId));

			for (EncounterPlan encPlan : encounterPlan) {
				if(encPlan.getPlantext()==null){
					encPlan.setPlantext(encPlan.getPlantext());
				}else{
					encPlan.setPlantext("");
				}
				encPlan.setEncounterid(encounterId);
				encounterPlanRepository.saveAndFlush(encPlan);
			}

		}
		if(notesGWID!=null){
			patclinicalElems = patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getPatClinEleByGWID(prevEncounterId, patientId,notesGWID));
			for (PatientClinicalElements patclincialElem : patclinicalElems) {
				PatientClinicalElements patElem=new PatientClinicalElements();
				patElem.setPatientClinicalElementsPatientid(patclincialElem.getPatientClinicalElementsPatientid());
				patElem.setPatientClinicalElementsChartid(patclincialElem.getPatientClinicalElementsChartid());
				patElem.setPatientClinicalElementsEncounterid(encounterId);
				patElem.setPatientClinicalElementsGwid(patclincialElem.getPatientClinicalElementsGwid());
				patElem.setPatientClinicalElementsValue(patclincialElem.getPatientClinicalElementsValue());
				patientClinicalElementsRepository.save(patElem);
			}
		}
	}

	@Override
	public List<ClinicalElementsOptionBean> getSymptomClinicalElementOptions(
			String elementGWId, Integer patientId, Integer encounterId,
			int patientGender, int ageinDay, boolean isAgeBased, int flag) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<ClinicalElementsOptions> root = cq.from(ClinicalElementsOptions.class);
		Join<ClinicalElementsOptions,PatientClinicalElements> gwIdJoin = root.join(ClinicalElementsOptions_.patientClinicalElements,JoinType.LEFT);
		Predicate patientClinicalGWId = builder.equal(gwIdJoin.get(PatientClinicalElements_.patientClinicalElementsGwid),elementGWId);
		Predicate patientIdPred = builder.equal(gwIdJoin.get(PatientClinicalElements_.patientClinicalElementsPatientid),patientId);
		Predicate encId = builder.equal(gwIdJoin.get(PatientClinicalElements_.patientClinicalElementsEncounterid),encounterId);
		gwIdJoin.on(patientClinicalGWId,patientIdPred,encId);
		Join<ClinicalElementsOptions,ClinicalElementsCondition> rootJoin = root.join(ClinicalElementsOptions_.clinicalElementsConditions,JoinType.LEFT);
		Predicate clinicalElemOptionGwid = builder.like(root.get(ClinicalElementsOptions_.clinicalElementsOptionsGwid), elementGWId);
		Predicate clinicalElemOptionActive= builder.equal(root.get(ClinicalElementsOptions_.clinicalElementsOptionsActive), true);
		Predicate clinicalElemOptionGender= root.get(ClinicalElementsOptions_.clinicalElementsOptionsGender).in("0",patientGender);
		Predicate optionalcondition;
		cq.select(builder.construct(ClinicalElementsOptionBean.class,
				root.get(ClinicalElementsOptions_.clinicalElementsOptionsId),
				root.get(ClinicalElementsOptions_.clinicalElementsOptionsOrderBy),
				root.get(ClinicalElementsOptions_.clinicalElementsOptionsGwid),
				root.get(ClinicalElementsOptions_.clinicalElementsOptionsName),
				root.get(ClinicalElementsOptions_.clinicalElementsOptionsValue),
				root.get(ClinicalElementsOptions_.clinicalElementsOptionsRetaincase),
				gwIdJoin.get(PatientClinicalElements_.patientClinicalElementsValue)));
		
		
		if(flag == 1)
		{
			optionalcondition = builder.and(gwIdJoin.get(PatientClinicalElements_.patientClinicalElementsValue).isNotNull(),clinicalElemOptionGwid,clinicalElemOptionActive,clinicalElemOptionGender);
		}
		else
		{
			optionalcondition = builder.and(clinicalElemOptionGwid,clinicalElemOptionActive,clinicalElemOptionGender);
		}
		
		if(ageinDay != -1){
			Predicate ageInDayCondition;
			Predicate p1 = builder.and(rootJoin.get(ClinicalElementsCondition_.clinicalElementsConditionStartage).isNull(),rootJoin.get(ClinicalElementsCondition_.clinicalElementsConditionEndage).isNull());
			Predicate p2 = builder.and(builder.lessThan(rootJoin.get(ClinicalElementsCondition_.clinicalElementsConditionStartage),ageinDay),builder.greaterThanOrEqualTo(rootJoin.get(ClinicalElementsCondition_.clinicalElementsConditionEndage), ageinDay));
			ageInDayCondition = builder.or(p1,p2);
			cq.where(optionalcondition,ageInDayCondition);
		}
		else
		{
			cq.where(optionalcondition);
		}
		
		List<Object> hpinotes=em.createQuery(cq).getResultList();
		List<ClinicalElementsOptionBean> clinicalElemOptBean = new ArrayList<ClinicalElementsOptionBean>();
			for(int i=0; i<hpinotes.size();i++)
			{
				ClinicalElementsOptionBean clinicalElemOpt = (ClinicalElementsOptionBean) hpinotes.get(i);              
				clinicalElemOptBean.add(clinicalElemOpt);
			}
			
		return clinicalElemOptBean;
	}

	@Override
	public List<ClinicalElementsOptionBean> getSymptomElementOptionAfterUnion(
			String elementGWId, Integer patientId, Integer encounterId,
			int patientGender, int ageinDay, boolean isAgeBased, int flag) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = cb.createQuery();
		Root<ClinicalElementsOptions> root = cq.from(ClinicalElementsOptions.class);
		Join<ClinicalElementsOptions,PatientClinicalElements> gwIdJoin = root.join(ClinicalElementsOptions_.patientClinicalElements,JoinType.INNER);
		Predicate patientClinicalGWId = cb.equal(gwIdJoin.get(PatientClinicalElements_.patientClinicalElementsGwid),elementGWId);
		Predicate patientIdPred = cb.equal(gwIdJoin.get(PatientClinicalElements_.patientClinicalElementsPatientid),patientId);
		Predicate encId = cb.equal(gwIdJoin.get(PatientClinicalElements_.patientClinicalElementsEncounterid),encounterId);
		gwIdJoin.on(patientClinicalGWId,patientIdPred,encId);
		Predicate clinicalElemOptionGwid = cb.like(root.get(ClinicalElementsOptions_.clinicalElementsOptionsGwid), elementGWId);
		Predicate clinicalElemOptionActive= cb.equal(root.get(ClinicalElementsOptions_.clinicalElementsOptionsActive), true);
		Predicate clinicalElemOptionGender= root.get(ClinicalElementsOptions_.clinicalElementsOptionsGender).in("0",patientGender);
		cq.where(clinicalElemOptionGwid,clinicalElemOptionActive,clinicalElemOptionGender);
		cq.orderBy(cb.asc(root.get(ClinicalElementsOptions_.clinicalElementsOptionsOrderBy)),cb.asc(root.get(ClinicalElementsOptions_.clinicalElementsOptionsValue)));
		cq.select(cb.construct(ClinicalElementsOptionBean.class,
				root.get(ClinicalElementsOptions_.clinicalElementsOptionsId),
				root.get(ClinicalElementsOptions_.clinicalElementsOptionsOrderBy),
				root.get(ClinicalElementsOptions_.clinicalElementsOptionsGwid),
				root.get(ClinicalElementsOptions_.clinicalElementsOptionsName),
				root.get(ClinicalElementsOptions_.clinicalElementsOptionsValue),
				root.get(ClinicalElementsOptions_.clinicalElementsOptionsRetaincase),
				gwIdJoin.get(PatientClinicalElements_.patientClinicalElementsValue)));
		List<Object> hpinotes=em.createQuery(cq).getResultList();
		List<ClinicalElementsOptionBean> gsBean = new ArrayList<ClinicalElementsOptionBean>();
			for(int i=0; i<hpinotes.size();i++)
			{
				ClinicalElementsOptionBean gs = (ClinicalElementsOptionBean) hpinotes.get(i);              
				gsBean.add(gs);
			}
			
		return gsBean;
	}
}
