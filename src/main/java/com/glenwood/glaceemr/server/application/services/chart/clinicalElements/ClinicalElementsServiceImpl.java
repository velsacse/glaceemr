package com.glenwood.glaceemr.server.application.services.chart.clinicalElements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import com.glenwood.glaceemr.server.application.models.ClinicalElementTemplateMapping;
import com.glenwood.glaceemr.server.application.models.ClinicalElements;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions;
import com.glenwood.glaceemr.server.application.models.ClinicalTextMapping;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.EncounterPlan;
import com.glenwood.glaceemr.server.application.models.LeafLibrary;
import com.glenwood.glaceemr.server.application.models.LeafPatient;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements;
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
	ClinicalElementsOptionsRepository clinicalElementsOptionsRepository;

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
	PatientRegistrationRepository patientRegRepository;

	@Autowired
	ClinicalDataBean clinicalDataBean;

	@Autowired
	LeafLibraryRepository leafLibraryRepository;

	@Autowired
	LeafPatientRepository leafPatientRepository;

	@Autowired
	EncounterPlanRepository encounterPlanRepository;
	
	@Autowired
	PatientDetailsBean patientDetailsBean;
	
	@Autowired
	ClinicalElementTemplateMappingRepository clinicalElementTemplateMappingRepository;
	
	@Override
	public List<ClinicalElementsOptions> getClinicalElementOptions(String gwid) {
		return clinicalElementsOptionsRepository.findAll(ClinicalElementsSpecification.getclinicalElementOptions(gwid));

	}

	@Override
	public ClinicalDataBean setVitalsClinicalData(String gwidPattern,Integer patientId,Integer encounterId,Boolean isDischargeVitals,Integer admssEpisode) {
		clinicalDataBean.setClinicalData(clinicalElementsRepository.findAll(Specifications.where(ClinicalElementsSpecification.getClinicalElementsByGWIDPattern(clinicalDataBean.getClientId(),gwidPattern,patientDetailsBean.getAgeinDay(), patientDetailsBean.getPatientSex())).and(ClinicalElementsSpecification.defaultAgePed(-1)).or(ClinicalElementsSpecification.patientAgePed(patientDetailsBean.getAgeinDay()))));
		List<Encounter> enc= encounterEntityRepository.findAll(EncounterEntitySpecification.getPrevEncHavingData(patientId,encounterId));
		List<PatientClinicalElements> patientData=null;
		if(isDischargeVitals){
			if(enc.size()>0 && enc.get(0)!=null){
				if(patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getPatClinicalDataByGWIDPattern(clinicalDataBean.getClientId(),patientId,encounterId,gwidPattern)).size()>0){
					patientData=patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getNonHistoryElemPatientData(clinicalDataBean.getClientId(),patientId,encounterId,gwidPattern));
				}
				else{
					patientData=patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getPrevEncounterPatVitalData(clinicalDataBean.getClientId(),patientId, enc.get(0).getEncounterId(), gwidPattern));
				}
			}else{
				patientData=patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getNonHistoryElemPatientData(clinicalDataBean.getClientId(),patientId,encounterId,gwidPattern));
			}
		}else{/*
			patientVitalsRepository.findAll(DischargeVitalSpecification.getDichargeVitalsByEncounterId(patientId, encounterId, admssEpisode));
		*/}

		List<PatientClinicalHistory> patientHistoryData=patientClinicalHistoryRepository.findAll(PatientClinicalElementsSpecification.getHistoryElemPatientData(clinicalDataBean.getClientId(),patientId, gwidPattern));
		clinicalDataBean.setPatientClinicalData(patientData);
		clinicalDataBean.setHistoryDatatoPatElementBean(patientHistoryData);
		return clinicalDataBean;
	}


	/**
	 * 
	 * Return true if clinical element is active else false
	 * @param gwid 
	 */ 

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
			patientDetailsBean.setPatientSex(patientRegistration.getPatientRegistrationSex().shortValue());
			patientDetailsBean.setPatDOB(patientRegistration.getPatientRegistrationDob());
			SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy");
			patientDetailsBean.setAgeinDay((int)((DateUtil.dateDiff( DateUtil.DATE , DateUtil.getDate(mdyFormat.format(patientDetailsBean.getPatDOB())) , new java.util.Date() ))));
		}
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
		List<ClinicalElements> clinicalelements=clinicalElementsRepository.findAll(ClinicalElementsSpecification.getClinicalElements(templateId, patientDetailsBean.getPatientSex(), tabType,leafPatient.get(0).getLeafPatientCreatedDate()==null?"-1":leafPatient.get(0).getLeafPatientCreatedDate().toString(), patientDetailsBean.getAgeinDay(),isAgeBased));
		clinicalDataBean.setClinicalData(clinicalelements);
		if(encounterId==-1){
			clinicalelements=clinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getPatClinicalHistoryData(clinicalDataBean.getClientId(),isAgeBased, patientId, patientDetailsBean.getPatientSex(), gwidPattern, patientDetailsBean.getAgeinDay()));
		}else{
			clinicalelements=clinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getEncPatientClinicalData(clinicalDataBean.getClientId(),isAgeBased, patientId,encounterId, patientDetailsBean.getPatientSex(), gwidPattern, patientDetailsBean.getAgeinDay()));
		}
		clinicalDataBean.setClinicalData(clinicalelements);

		/*
		 * set Patient clinical elements and patient History elements to PatientElementBean of ClinicalDataBean 
		 * 
		 * */

		List<PatientClinicalElements> patientData=patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getNonHistoryElemPatientData(clinicalDataBean.getClientId(),patientId,encounterId,gwidPattern));
		List<PatientClinicalHistory> patientHistoryData=patientClinicalHistoryRepository.findAll(PatientClinicalElementsSpecification.getHistoryElemPatientData(clinicalDataBean.getClientId(),patientId, gwidPattern));
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

		List<ClinicalElements> clinicalelements=clinicalElementsRepository.findAll(ClinicalElementsSpecification.getClinicalElements(templateId, patientDetailsBean.getPatientSex(), tabType,leafPatient.get(0).getLeafPatientCreatedDate()==null?"-1":leafPatient.get(0).getLeafPatientCreatedDate().toString(), patientDetailsBean.getAgeinDay(),isAgeBased));
		clinicalDataBean.setClinicalData(clinicalelements);
		if(encounterId==-1){
			clinicalelements=clinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getPatClinicalHistoryData(clinicalDataBean.getClientId(),isAgeBased, patientId, patientDetailsBean.getPatientSex(), gwidPattern, patientDetailsBean.getAgeinDay()));
		}else{
			clinicalelements=clinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getEncPatientClinicalData(clinicalDataBean.getClientId(),isAgeBased, patientId,encounterId, patientDetailsBean.getPatientSex(), gwidPattern, patientDetailsBean.getAgeinDay()));
		}
		clinicalDataBean.setClinicalData(clinicalelements);

		/*
		 * set Patient clinical elements or  patient History elements to PatientHistoryElementBean of ClinicalDataBean based on encounterId
		 * 
		 * */


		if(encounterId==-1){
			List<PatientClinicalHistory> patientHistoryData=patientClinicalHistoryRepository.findAll(PatientClinicalElementsSpecification.getHistoryElemPatientData(clinicalDataBean.getClientId(),patientId, gwidPattern));
			clinicalDataBean.setPatientHistoryData(patientHistoryData);
		}	else{
			List<PatientClinicalElements> patientData=patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getNonHistoryElemPatientData(clinicalDataBean.getClientId(),patientId,encounterId,gwidPattern));
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

	



}