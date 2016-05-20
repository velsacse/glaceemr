package com.glenwood.glaceemr.server.application.services.GroupTherapy;



import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.PosTable_;
import com.glenwood.glaceemr.server.application.models.TherapyGroup;
import com.glenwood.glaceemr.server.application.models.TherapyGroupPatientMapping;
import com.glenwood.glaceemr.server.application.models.TherapyGroup_;
import com.glenwood.glaceemr.server.application.models.TherapySession;
import com.glenwood.glaceemr.server.application.models.TherapySessionDetails;
import com.glenwood.glaceemr.server.application.models.TherapySessionDetails_;
import com.glenwood.glaceemr.server.application.models.TherapySession_;
import com.glenwood.glaceemr.server.application.repositories.EmpProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.PosTableRepository;
import com.glenwood.glaceemr.server.application.repositories.TherapyGroupPatientMappingRepository;
import com.glenwood.glaceemr.server.application.repositories.TherapyGroupRepository;
import com.glenwood.glaceemr.server.application.repositories.TherapySessionDetailsRepository;
import com.glenwood.glaceemr.server.application.repositories.TherapySessionRepository;
import com.glenwood.glaceemr.server.application.specifications.GroupTherapySpecification;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

@Service
public class AddNewGroupServiceImpl implements AddNewGroupService{
	@Autowired
	EmpProfileRepository empProfileRepository;
	
	@Autowired
	PosTableRepository posTableRepository;
	
	@Autowired
	TherapyGroupRepository therapyGroupRepository;
	
	@Autowired
	TherapySessionRepository therapySessionRepository;
	
	@Autowired
	TherapyGroupPatientMappingRepository therapyGroupPatientMappingRepository;
	
	@Autowired
	TherapySessionDetailsRepository therapySessionDetailsRepository;
	
	@PersistenceContext
	EntityManager em;
	
	/**
	 * To get the data of providers,pos,groups
	 */
	@Override
	public Map<String, Object> listDefaults() {
		Map<String, Object> mapObject=new HashMap<String,Object>();
		List<EmployeeProfile> phisicianList=empProfileRepository.findAll(GroupTherapySpecification.getListOfPhysicians());
		List<PosTable>  posValue=posTableRepository.findAll(GroupTherapySpecification.getAllPosValue());
		List<TherapyGroup> groups=therapyGroupRepository.findAll(GroupTherapySpecification.getActiveGroups());
		mapObject.put("doctors",phisicianList);
		mapObject.put("pos",posValue);
		mapObject.put("therapy groups",groups);
		return mapObject;
	}
   
	/**
	 * To save new Group
	 */
	@Override
	public void saveNewGroup(TherapyGroupBean dataToSave) throws JSONException {
		
		
        TherapyGroup therapyGroup=new TherapyGroup();
        if(dataToSave.getGroupId()!=-1)
        	therapyGroup.setTherapyGroupId(dataToSave.getGroupId());
        therapyGroup.setTherapyGroupProviderId(dataToSave.getProviderId());
        therapyGroup.setTherapyGroupName(dataToSave.getGroupName());
        therapyGroup.setTherapyGroupDescription(dataToSave.getGroupDesc());
        therapyGroup.setTherapyGroupCreatedDate(therapyGroupRepository.findCurrentTimeStamp());
        therapyGroup.setTherapyGroupCreatedBy(dataToSave.getLoginId());
        therapyGroup.setTherapyGroupModifiedBy(dataToSave.getLoginId());
        therapyGroup.setTherapyGroupModifiedDate(therapyGroupRepository.findCurrentTimeStamp());
        therapyGroup.setTherapyGroupIsActive(dataToSave.getIsActive());
        therapyGroup.setTherapyGroupPosId(dataToSave.getPosId());
        therapyGroup.setTherapyGroupDefaulttime(dataToSave.getDefaultTherapyTime());
        if(!dataToSave.getDiagnosis().equals("-1")){
        JSONArray dxCodesArr=new JSONArray(dataToSave.getDiagnosis());
        
		for(int i=0;i<dxCodesArr.length();i++){
			Method targetMethodFordx;
			Method targetMethodFordxDesc;
			try {
				
				if(!((JSONObject)dxCodesArr.get(i)).getString("dx").equalsIgnoreCase("null")){

				targetMethodFordx = therapyGroup.getClass().getMethod("setTherapyGroupDx" + (i+1),String.class);
				targetMethodFordxDesc = therapyGroup.getClass().getMethod("setTherapyGroupDx" + (i+1)+"desc",String.class);

				targetMethodFordx.invoke(therapyGroup,((JSONObject)dxCodesArr.get(i)).getString("dx"));
				targetMethodFordxDesc.invoke(therapyGroup,((JSONObject)dxCodesArr.get(i)).getString("dxdesc"));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        }
        therapyGroupRepository.saveAndFlush(therapyGroup);
	}
	/**
	 * To list group data
	 */
	@Override
	public List<TherapyGroup> listGroupData(String groupId) {
		int therapyGroupId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(groupId)).or("-1"));
		List<TherapyGroup> data=therapyGroupRepository.findAll(GroupTherapySpecification.getGroupData(therapyGroupId));
		return data;
	}

	/**
	 * To add patient to selected group
	 */
	@Override
	public void addPatientToTherapyGroup(String dataToSave) throws JSONException {
		JSONObject patientData= new JSONObject(dataToSave);
		String patientIds = Optional.fromNullable(Strings.emptyToNull(patientData.get("patientIds").toString())).or("-1");
		int groupId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(patientData.get("groupId").toString())).or("-1"));
		String sessionId = Optional.fromNullable(Strings.emptyToNull(patientData.get("sessionId").toString())).or("");
		int addToGroup= Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(patientData.get("addToGroup").toString())).or("-1"));
		String[] patientsSplit=patientIds.split("~");
		for(int i=0;i<patientsSplit.length;i++){
			if(addToGroup==1){
		TherapyGroupPatientMapping therapyGroupPatientMapping=new TherapyGroupPatientMapping();
        therapyGroupPatientMapping.setTherapyGroupPatientMappingGroupId(groupId);
        therapyGroupPatientMapping.setTherapyGroupPatientMappingPatientId(Integer.parseInt(patientsSplit[i]));
        therapyGroupPatientMappingRepository.saveAndFlush(therapyGroupPatientMapping);
        }
        if(!sessionId.equalsIgnoreCase("")){
        	TherapySessionDetails therapyDetails = new TherapySessionDetails();
        	therapyDetails.setTherapySessionDetailsPatientId(Integer.parseInt(patientsSplit[i]));
        	therapyDetails.setTherapySessionDetailsSessionId(Integer.parseInt(sessionId));
        	therapySessionDetailsRepository.saveAndFlush(therapyDetails);
        }
		}
	}

	/**
	 * To create therapy session
	 */
	@Override
	public List<TherapySession> createTherapySession(String dataToSave) throws Exception {
		JSONObject therapyData= new JSONObject(dataToSave);
		int providerId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("providerId").toString())).or("-1"));
		int posId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("posId").toString())).or("-1"));
		int groupId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("groupId").toString())).or("-1"));
		String therapyDate=Optional.fromNullable(Strings.emptyToNull(therapyData.get("sessionDate").toString())).or("");
		String patientIds=Optional.fromNullable(Strings.emptyToNull(therapyData.get("patientIds").toString())).or("");
		int therapySessionStatus = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("sessionStatus").toString())).or("-1"));
		String therapySessionId =Optional.fromNullable(Strings.emptyToNull(therapyData.get("sessionId").toString())).or("");
		 int  therapyId = 0;
		if(therapySessionId.equalsIgnoreCase("")){
        TherapySession therapySession=new TherapySession();
        therapySession.setTherapySessionGroupId(groupId);
        therapySession.setTherapySessionProviderId(providerId);
        therapySession.setTherapySessionPosId(posId);
		SimpleDateFormat ft = new SimpleDateFormat ("MM/dd/yyyy kk:mm:ss");
		
		Date date = (Date)  ft.parse(therapyDate);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		DateFormat mmformat=new SimpleDateFormat("yyyy-MM-dd");
         therapySession.setTherapySessionDate(new Timestamp(date.getTime()));
         therapySession.setTherapySessionDateValue(java.sql.Date.valueOf(mmformat.format(date)));
        therapySession.setTherapySessionStatus(therapySessionStatus);
        therapySession = therapySessionRepository.saveAndFlush(therapySession);
        therapyId=therapySession.getTherapySessionId();
        String[] patientsSplit=patientIds.split("~");
        for(int i=0;i<patientsSplit.length;i++){
        	TherapySessionDetails therapyDetails = new TherapySessionDetails();
        	therapyDetails.setTherapySessionDetailsPatientId(Integer.parseInt(patientsSplit[i]));
        	if(therapyId==0)
        	    therapyDetails.setTherapySessionDetailsSessionId(Integer.parseInt(therapySessionId));
        	else
        		therapyDetails.setTherapySessionDetailsSessionId(therapyId);
        	therapySessionDetailsRepository.saveAndFlush(therapyDetails);
        }
		}
		return therapySessionRepository.findAll(GroupTherapySpecification.byTherapyId(therapyId));
		
	}

	/**
	 * To get the patient data by groupid
	 */
	@Override
	public Map<String, Object> getPatientData(String groupId) {
		Map<String, Object> mapObject=new HashMap<String,Object>();
		int therapyGroupId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(groupId)).or("-1"));
		List<TherapyGroup> data=therapyGroupRepository.findAll(GroupTherapySpecification.getGroupData(therapyGroupId));
		List<TherapyGroupPatientMapping> patData=therapyGroupPatientMappingRepository.findAll(GroupTherapySpecification.getPatDataBygroup(therapyGroupId));
		List<TherapySession> sessionData=therapySessionRepository.findAll(GroupTherapySpecification.byGroupId(therapyGroupId));
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<TherapyLogBean> cq = builder.createQuery(TherapyLogBean.class);
		Root<TherapySession> root = cq.from(TherapySession.class);
		Join<TherapySession,TherapySessionDetails> sessionJoin=root.join(TherapySession_.therapySessionDetails,JoinType.INNER);

		mapObject.put("groupData",data);
		mapObject.put("patientData",patData);
		mapObject.put("sessionData",sessionData);
		return mapObject;
	}

	/**
	 * To get the therapy session log data
	 */
	@Override
	public List<TherapyLogBean> therapySearchLog(String dataToSearch) throws Exception {
		JSONObject therapyData= new JSONObject(dataToSearch);
		int providerId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("providerId").toString())).or("-1"));
		int groupId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("groupId").toString())).or("-1"));
		String fromDate=Optional.fromNullable(Strings.emptyToNull(therapyData.get("fromDate").toString())).or("");
		String toDate=Optional.fromNullable(Strings.emptyToNull(therapyData.get("toDate").toString())).or("");
        SimpleDateFormat ft = new SimpleDateFormat ("MM/dd/yyyy");		
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<TherapyLogBean> cq = builder.createQuery(TherapyLogBean.class);
		Root<TherapySession> root = cq.from(TherapySession.class);
		Join<TherapySession,EmployeeProfile> docJoin=root.join(TherapySession_.empProfile,JoinType.INNER);
		Join<TherapySession,PosTable> posJoin=root.join(TherapySession_.posTable,JoinType.INNER);
		Join<TherapySession,TherapySessionDetails> sessionJoin=root.join(TherapySession_.therapySessionDetails,JoinType.INNER);
		Join<TherapySession,TherapyGroup> groupJoin=root.join(TherapySession_.therapyGroup,JoinType.INNER);
	    List<Predicate> predicates = new ArrayList<>();
		if(groupId!=-1)
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionGroupId), groupId));
		if(providerId!=-1)
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionProviderId), providerId));
		if(!fromDate.equals("")){
			Date frdate = (Date)  ft.parse(fromDate);
			predicates.add(builder.greaterThanOrEqualTo(root.get(TherapySession_.therapySessionDateValue),frdate));
		}
		if(!toDate.equals("")){
			Date todate = (Date)  ft.parse(toDate);
			predicates.add(builder.lessThanOrEqualTo(root.get(TherapySession_.therapySessionDateValue), todate));
		}
		Selection[] selections= new Selection[] {
			    root.get((TherapySession_.therapySessionDateValue)),
				docJoin.get(EmployeeProfile_.empProfileFullname),
				groupJoin.get(TherapyGroup_.therapyGroupName),
				posJoin.get(PosTable_.posTableFacilityComments),
			    builder.count(sessionJoin.get(TherapySessionDetails_.therapySessionDetailsPatientId)),
			    groupJoin.get(TherapyGroup_.therapyGroupId),
			    docJoin.get(EmployeeProfile_.empProfileEmpid),
			    posJoin.get(PosTable_.posTableRelationId),
			    root.get((TherapySession_.therapySessionEndTime)),
			    root.get(TherapySession_.therapySessionDate)
				
		};
		cq.select(builder.construct(TherapyLogBean.class, selections));
		cq.where(predicates.toArray(new Predicate[predicates.size()])).groupBy(root.get(TherapySession_.therapySessionDateValue),docJoin.get(EmployeeProfile_.empProfileFullname),groupJoin.get(TherapyGroup_.therapyGroupName),posJoin.get(PosTable_.posTableFacilityComments),groupJoin.get(TherapyGroup_.therapyGroupId),docJoin.get(EmployeeProfile_.empProfileEmpid),posJoin.get(PosTable_.posTableRelationId),root.get((TherapySession_.therapySessionEndTime)),root.get(TherapySession_.therapySessionDate));
		List<TherapyLogBean> confData=new ArrayList<TherapyLogBean>();

		 confData=em.createQuery(cq).getResultList();

		return confData;
		}

	/**
	 * To get the patient data for therapy session details page
	 */
	@Override
	public List<TherapyPatientsBean> getTherapyPatients(String dataToSearch) throws Exception {
		JSONObject therapyData= new JSONObject(dataToSearch);
		int providerId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("providerId").toString())).or("-1"));
		int groupId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("groupId").toString())).or("-1"));
		String fromDate=Optional.fromNullable(Strings.emptyToNull(therapyData.get("date").toString())).or("");
		int posId=Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("posId").toString())).or(""));
        SimpleDateFormat ft = new SimpleDateFormat ("MM/dd/yyyy");		
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<TherapyPatientsBean> cq = builder.createQuery(TherapyPatientsBean.class);
		Root<TherapySession> root = cq.from(TherapySession.class);
		Join<TherapySession,TherapySessionDetails> sessionJoin=root.join(TherapySession_.therapySessionDetails,JoinType.INNER);
		Join<TherapySessionDetails,PatientRegistration> patientJoin=sessionJoin.join(TherapySessionDetails_.patientRegistration,JoinType.INNER);
	    List<Predicate> predicates = new ArrayList<>();
		if(groupId!=-1)
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionGroupId), groupId));
		if(providerId!=-1)
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionProviderId), providerId));
		if(!fromDate.equals("")){
			Date frdate = (Date)  ft.parse(fromDate);
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionDateValue),frdate));
		}
		if(posId!=-1){
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionPosId),posId));
		}
		Selection[] selections= new Selection[] {
				patientJoin.get(PatientRegistration_.patientRegistrationId),
				patientJoin.get(PatientRegistration_.patientRegistrationAccountno),
				patientJoin.get(PatientRegistration_.patientRegistrationLastName),
				patientJoin.get(PatientRegistration_.patientRegistrationFirstName),
				patientJoin.get(PatientRegistration_.patientRegistrationDob),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsSessionId)
				
		};
		cq.select(builder.construct(TherapyPatientsBean.class, selections));
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		List<TherapyPatientsBean> confData=new ArrayList<TherapyPatientsBean>();

		 confData=em.createQuery(cq).getResultList();

		return confData;

	}

	/**
	 * To delete the patient data from group
	 */
	@Override
	public void deleteTherapyPatient(String dataToDelete) throws JSONException {
		JSONObject therapyData= new JSONObject(dataToDelete);
		int patientId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("patientId").toString())).or("-1"));
		int groupId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("groupId").toString())).or("-1"));
		int sessionId=Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("sessionId").toString())).or("-1"));
		int groupFlag=Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("groupFlag").toString())).or("-1"));
		if(groupFlag==1){
			therapyGroupPatientMappingRepository.delete(therapyGroupPatientMappingRepository.findAll(GroupTherapySpecification.getPatientFromGroup(patientId, groupId)));
		}
		if(sessionId!=-1){
		    therapySessionDetailsRepository.delete(therapySessionDetailsRepository.findAll(GroupTherapySpecification.getPatientFromSession(patientId, sessionId)));	
		}
		
	}
	

}
