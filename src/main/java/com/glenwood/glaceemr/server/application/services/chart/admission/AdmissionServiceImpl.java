package com.glenwood.glaceemr.server.application.services.chart.admission;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.Admission;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.H496;
import com.glenwood.glaceemr.server.application.models.LeafPatient;
import com.glenwood.glaceemr.server.application.models.PatientAllergies;
import com.glenwood.glaceemr.server.application.models.PatientEpisode;
import com.glenwood.glaceemr.server.application.repositories.AdmissionRepository;
import com.glenwood.glaceemr.server.application.repositories.EmpProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.EncounterEntityRepository;
import com.glenwood.glaceemr.server.application.repositories.H496Repository;
import com.glenwood.glaceemr.server.application.repositories.LeafGroupRepository;
import com.glenwood.glaceemr.server.application.repositories.LeafLibraryRepository;
import com.glenwood.glaceemr.server.application.repositories.LeafPatientRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientAllergyRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientEpisodeRepository;
import com.glenwood.glaceemr.server.application.services.chart.CNMSettings.CNMSettingsServiceImpl;
import com.glenwood.glaceemr.server.application.specifications.AdmissionSpecification;
import com.glenwood.glaceemr.server.application.specifications.LeafPatientSpecfication;


@Service
public class AdmissionServiceImpl implements AdmissionService {

	private Logger logger = Logger.getLogger(AdmissionServiceImpl.class);

	@Autowired
	AdmissionRepository admissionRepository;
	
	@Autowired
	LeafGroupRepository leafgroupRepository;

	@Autowired
	LeafLibraryRepository leafLibraryRepository;

	@Autowired
	PatientEpisodeRepository patientEpisodeRepository;

	@Autowired
	EmpProfileRepository empProfileRepository;
	
	@Autowired
	EncounterEntityRepository encounterEntityRepository;
	
	@Autowired
	CNMSettingsServiceImpl cnmSettingsServiceImpl;
	
	@Autowired
	LeafPatientRepository leafPatientRepository;
	
	@Autowired
	PatientAllergyRepository patientAllergyRepository;
	
	@Autowired
	H496Repository H496Repository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	
	@Override
	public String saveAdmission( AdmissionBean admission) {


		Admission admssObj = null;
		Admission newadmssObj = null;

		try{
			admssObj = getAdmission(admission.getPatientId());
			boolean exists = false;
			if(admssObj !=null){
				admissionRepository.delete(admssObj);
				exists = true;
			}
			if(exists || admssObj == null){
				newadmssObj = new Admission();
				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
				Date parsed=null;

				try {
					parsed = format.parse(admission.getAdmissionDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}

				java.sql.Date admssDate = new java.sql.Date(parsed.getTime());
				newadmssObj.setAdmissionAdmittedDate(admssDate);
				newadmssObj.setAdmissionDoctorId(admission.getAdmssProvider());
				newadmssObj.setAdmissionPosId(admission.getPos());
				newadmssObj.setAdmissionPatientId(admission.getPatientId());
				newadmssObj.setAdmissionStatus(1);
				newadmssObj.setAdmissionRoom(admission.getRoomNo());
				newadmssObj.setAdmissionNotes(admission.getNotes());


				if(admssObj != null){
					newadmssObj.setAdmissionEpisode(admssObj.getAdmissionEpisode());
				}else{
					//open a admission type episode for the patient
					PatientEpisode patAdmsEpisode=new PatientEpisode();
					patAdmsEpisode.setPatientEpisodePatientid(admission.getPatientId());
					patAdmsEpisode.setPatientEpisodeName("Admission");
					patAdmsEpisode.setPatientEpisodeType(140); // 140 is the admission episode_type list id 
					patAdmsEpisode.setPatientEpisodeStartDate(admssDate);
					Timestamp patEpisodeCreatedtmp=new Timestamp(new Date().getTime());
					patAdmsEpisode.setPatientEpisodeCreatedDate(patEpisodeCreatedtmp);
					patAdmsEpisode.setPatientEpisodeCreatedBy(admission.getLoginId());
					patAdmsEpisode.setPatientEpisodeStatus(0);
					patAdmsEpisode.setPatientEpisodeIsactive(true);
					PatientEpisode patientEpisode=patientEpisodeRepository.save(patAdmsEpisode);
					newadmssObj.setAdmissionEpisode(patientEpisode.getPatientEpisodeId());
				}
				JSONArray selectedDxObj=new JSONArray(admission.getSelectedDx());

				for(int i=0;i<selectedDxObj.length();i++){
					Method targetMethodFordx;
					Method targetMethodFordxDesc;
					try {

						if(!((JSONObject)selectedDxObj.get(i)).getString("dx").equalsIgnoreCase("null")){

							targetMethodFordx = newadmssObj.getClass().getMethod("setAdmissionDx" + (i+1),String.class);
							targetMethodFordxDesc = newadmssObj.getClass().getMethod("setAdmissionDx" + (i+1)+"desc",String.class);

							targetMethodFordx.invoke(newadmssObj,((JSONObject)selectedDxObj.get(i)).getString("dx"));
							targetMethodFordxDesc.invoke(newadmssObj,((JSONObject)selectedDxObj.get(i)).getString("value"));
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				admissionRepository.save(newadmssObj);
			}
			return new JSONObject().put("patientEpisode",newadmssObj.getAdmissionEpisode().toString()).toString();

		}catch(Exception e){
			e.printStackTrace();
			return "failure";
		}

	}

	@Override
	public Admission getAdmission(Integer patientId) {
		List<Admission> admission=admissionRepository.findAll(AdmissionSpecification.getAdmissionByPatientId(patientId));
		if(admission.size() > 0){
			return admission.get(0);
		}else{
			return null;
		}
	}
	
	
	@Override
	public List<Admission> getPastAdmission(Integer patientId) {
		List<Admission> admission=admissionRepository.findAll(AdmissionSpecification.getPastAdmissionByPatientId(patientId));
		if(admission.size() > 0){
			return admission;
		}else{
			return Collections.<Admission>emptyList();
		}
	}

	public PatientEpisode getPatientEpisodebyEpisodeId(Integer patEpisodeId){
		
		List<PatientEpisode> patEpisode=null;
		if((patEpisode=patientEpisodeRepository.findAll(AdmissionSpecification.getPatientEpisodebyEpisodeId(patEpisodeId))).size()>0){
			return patEpisode.get(0);
		}else
			return null;
	}
	
	
	@Override
	public String dischargePatient(Integer patientId,Integer loginId,Integer userId) {

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			format.parse(format.format(date));
			java.sql.Date admssdisDate = new java.sql.Date(format.parse(format.format(date)).getTime());

			Admission admiss=getAdmission(patientId);
			if(admiss != null){
				admiss.setAdmissionDischargedDate(admssdisDate);
				admiss.setAdmissionDischargeDocId(userId);
				admiss.setAdmissionStatus(2);
				admissionRepository.saveAndFlush(admiss);
				
				
				//update admission episode status to 1 on discharge
				PatientEpisode patEpisode=null;
				if((patEpisode = getPatientEpisodebyEpisodeId(admiss.getAdmissionEpisode()))!=null){
					patEpisode.setPatientEpisodeStatus(1);
					patEpisode.setPatientEpisodeModifiedBy(loginId);
					patEpisode.setPatientEpisodeModifiedDate(new Timestamp(date.getTime()));
					patientEpisodeRepository.saveAndFlush(patEpisode);
				}
			}

			return "success";
		
		} catch (ParseException e) {
			e.printStackTrace();
			return "failure";
		}
	
	
	}

	

	

	
	
	public List<Admission> openAdmissionTemplate(Integer encounterId, Integer userId) {
		
		return null;
	}

	@Override
	public Encounter openAdmissionLeaf(AdmissionBean admission) {
		
		Encounter encEnt=new Encounter();
		encEnt.setEncounterChartid(admission.getChartId());
		encEnt.setEncounterDate(new Timestamp(new Date().getTime()));
		encEnt.setEncounterCreatedDate(new Timestamp(new Date().getTime()));
		encEnt.setEncounterType(new Short("5"));
		encEnt.setEncounterServiceDoctor(new Long(admission.getAdmssProvider()));
		encEnt.setEncounterPos(admission.getPos());
		encEnt.setEncounterReason(-1);
		encEnt.setEncounterStatus(new Short("3"));
		encEnt.setEncounterCreatedBy(new Long(admission.getLoginId()));
		encEnt.setEncounterClosedBy(new Long(admission.getLoginId()));
		encEnt.setEncounterClosedDate(encEnt.getEncounterCreatedDate());
		encEnt.setEncounterChargeable(false);
		encEnt.setEncounterAlreadySeen(false);
		encEnt.setEncounterInsReview(0);
		encEnt.setEncounterSeverity(new Short("1"));
		encEnt.setEncounterPatientEpisodeid(admission.getAdmissionEpisode());
		return encounterEntityRepository.save(encEnt);
		
		//openLeafForPatient(newenc.getEncounterId(),1695,admission);
	}

	private void openLeafForPatient(Integer encounterId,Integer leafLibrarId,AdmissionBean admission) {
		
		LeafPatient admissionLeaf = new LeafPatient();
		admissionLeaf.setLeafPatientId(leafPatientRepository.findAll(LeafPatientSpecfication.getLeafPatientOrderById()).get(0).getLeafPatientId()+1);
		admissionLeaf.setLeafPatientPatientId(admission.getPatientId());
		admissionLeaf.setLeafPatientEncounterId(encounterId);
		admissionLeaf.setLeafPatientLeafLibraryId(leafLibrarId);
		admissionLeaf.setLeafPatientCreatedBy(admission.getUserId());;
		admissionLeaf.setLeafPatientCreatedDate(new Timestamp(new Date().getTime()));
		admissionLeaf.setLeafPatientLastModyby(admission.getUserId());
		admissionLeaf.setLeafPatientLastmoddate(admissionLeaf.getLeafPatientCreatedDate());
		admissionLeaf.setLeafPatientIscomplete(false);
		admissionLeaf.setLeafPatientIsactive(true);
		admissionLeaf.setLeafPatientIsprinted(false);		
		admissionLeaf.setLeafPatientIsfaxed(false);
		admissionLeaf.setLeafPatientSignOne("");
		admissionLeaf.setLeafPatientProblemid(-1);
		admissionLeaf.setLeafPatientMaSignedUserid(-1);
		admissionLeaf.setLeafPatientPlanMode(cnmSettingsServiceImpl.getCNMSettingsIsActiveById(401));
		
		leafPatientRepository.save(admissionLeaf);
		
	}

	@Override
	public List<Admission> getLeafDetails(Integer encounterId, Integer userId) {
		leafLibraryRepository.findAll(Specifications.where(AdmissionSpecification.A()).and(AdmissionSpecification.B(encounterId, userId)));
		//leafLibraryRepository.findAll(AdmissionSpecification.getFequentList(userId));
		return null;
	}

	@Override
	public String getAdmissionEncDetails(Integer admssEpisode) {
		List<Encounter> admssEnc=encounterEntityRepository.findAll(AdmissionSpecification.getAdmissionEncByEpisodeId(admssEpisode));
		JSONArray encDataJson= new JSONArray();
		for (int i=0;i<admssEnc.size();i++) {
			try {
				encDataJson.put(i, new JSONObject().put("encounterId",((Encounter)admssEnc.get(i)).getEncounterId()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return encDataJson.toString();
	}

	@Override
	public AdmissionLeafBean getAdmissionLeafs(Integer admssEpisode) {
		List<LeafPatient> admissionLeafs = null;
		List<H496> admssLeafFaxDet = null;
		List<PatientAllergies> admissionAllergiesDet = null;
		try {
			List<Encounter> admssEnc=encounterEntityRepository.findAll(AdmissionSpecification.getAdmissionEncByEpisodeId(admssEpisode));
			List<Integer> encounterIds = new ArrayList<Integer>();
			for (Encounter encounter : admssEnc) {
				encounterIds.add(encounter.getEncounterId());
			}
			if(encounterIds.size() >= 1){
				admissionLeafs =  leafPatientRepository.findAll(Specifications.where(AdmissionSpecification.getPatientLeafsByEncId(encounterIds)).and(AdmissionSpecification.getAdmissionPatientLeafsByEncId(encounterIds)));
				admssLeafFaxDet =  H496Repository.findAll(AdmissionSpecification.getLeafFaxDetails(encounterIds));
				admissionAllergiesDet = patientAllergyRepository.findAll(AdmissionSpecification.getPatientAllergiesList(encounterIds));
			}else{
				admissionLeafs = Collections.emptyList();
				admssLeafFaxDet = Collections.emptyList();
				admissionAllergiesDet = Collections.emptyList();
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		AdmissionLeafBean admissionLeafBean = new AdmissionLeafBean();
		admissionLeafBean.setAdmissionLeafBean(admissionLeafs);
		admissionLeafBean.setAdmissionLeafFaxBean(admssLeafFaxDet);
		admissionLeafBean.setAllergyBean(admissionAllergiesDet);
		return admissionLeafBean;
	}


	
	

}
