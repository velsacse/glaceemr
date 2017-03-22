package com.glenwood.glaceemr.server.application.services.GroupTherapy;



import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
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

import com.glenwood.glaceemr.server.application.models.ClinicalElements;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions;
import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions_;
import com.glenwood.glaceemr.server.application.models.ClinicalElements_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.GeneralShortcut;
import com.glenwood.glaceemr.server.application.models.GeneralShortcut_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.PosTable_;
import com.glenwood.glaceemr.server.application.models.TherapyGroup;
import com.glenwood.glaceemr.server.application.models.TherapyGroupPatientMapping;
import com.glenwood.glaceemr.server.application.models.TherapyGroupPatientMapping_;
import com.glenwood.glaceemr.server.application.models.TherapyGroup_;
import com.glenwood.glaceemr.server.application.models.TherapySession;
import com.glenwood.glaceemr.server.application.models.TherapySessionDetails;
import com.glenwood.glaceemr.server.application.models.TherapySessionDetails_;
import com.glenwood.glaceemr.server.application.models.TherapySessionPatientDetails;
import com.glenwood.glaceemr.server.application.models.TherapySessionPatientDetails_;
import com.glenwood.glaceemr.server.application.models.TherapySession_;
import com.glenwood.glaceemr.server.application.repositories.EmpProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.PosTableRepository;
import com.glenwood.glaceemr.server.application.repositories.TherapyGroupPatientMappingRepository;
import com.glenwood.glaceemr.server.application.repositories.TherapyGroupRepository;
import com.glenwood.glaceemr.server.application.repositories.TherapySessionDetailsRepository;
import com.glenwood.glaceemr.server.application.repositories.TherapySessionPatientDetailsRepository;
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
	
	@Autowired
	TherapySessionPatientDetailsRepository therapySessionPatientDetailsRepository;
	
	@PersistenceContext
	EntityManager em;
	
	/**
	 * To get the data of providers,pos,groups
	 */
	@Override
	public Map<String, Object> listDefaults(Integer userId) {
		Map<String, Object> mapObject=new HashMap<String,Object>();
		List<PosTable>  posValue=posTableRepository.findAll(GroupTherapySpecification.getAllPosValue());
		List<TherapyGroup> groups=therapyGroupRepository.findAll(GroupTherapySpecification.getActiveGroups());
		List<TherapySessionBean> openSessions=getOpenSessions(userId,-1);
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<EmployeeProfile> root = cq.from(EmployeeProfile.class);
		cq.multiselect(root.get(EmployeeProfile_.empProfileEmpid),root.get(EmployeeProfile_.empProfileFullname),root.get(EmployeeProfile_.empProfileGroupid));
		Predicate predicateByIsActive=builder.equal(root.get(EmployeeProfile_.empProfileIsActive), true);
		Predicate predicateByEmpGroupId=root.get(EmployeeProfile_.empProfileGroupid).in(-1,-10,-2,-3);
		cq.where(predicateByIsActive,predicateByEmpGroupId).orderBy(builder.desc(root.get(EmployeeProfile_.empProfileGroupid)),builder.asc(root.get(EmployeeProfile_.empProfileFullname)));
		List<Object[]> resultset = em.createQuery(cq).getResultList();
		List<EmployeeProfile> phisicianList = new ArrayList<EmployeeProfile>();
		for(int i=0;i<resultset.size();i++){ 
			Integer empId = Integer.parseInt(resultset.get(i)[0].toString());
			String empName = resultset.get(i)[1].toString();
			Integer empGroupId = Integer.parseInt(resultset.get(i)[2].toString());
			EmployeeProfile eachObject = new EmployeeProfile();
			eachObject.setEmpProfileEmpid(empId);
			eachObject.setEmpProfileFullname(empName);
			eachObject.setEmpProfileGroupid(empGroupId);
			phisicianList.add(eachObject);			
		}
	  	mapObject.put("doctors",phisicianList);
		mapObject.put("pos",posValue);
		mapObject.put("therapy groups",groups);
		mapObject.put("therapy sessions",openSessions);
		return mapObject;
	}
	
	/**
	 * To get open sessions
	 */
	@Override
	public List<TherapySessionBean> getOpenSessions(Integer userId, Integer groupId) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<TherapySessionBean> cq = builder.createQuery(TherapySessionBean.class);
		Root<TherapySession> root = cq.from(TherapySession.class);
		Join<TherapySession,EmployeeProfile> supervisorJoin = root.join(TherapySession_.empProfileSupervisor,JoinType.INNER);
		Join<TherapySession,EmployeeProfile> leaderJoin = root.join(TherapySession_.empProfileLeader,JoinType.INNER);
		Join<TherapySession,TherapyGroup> groupJoin=root.join(TherapySession_.therapyGroup,JoinType.INNER);
		List<Predicate> predicateByOpenSession = new ArrayList<>();
		predicateByOpenSession.add(builder.equal(root.get(TherapySession_.therapySessionStatus), 1));
		if(groupId!=-1)
			predicateByOpenSession.add(builder.equal(root.get(TherapySession_.therapySessionGroupId), groupId));
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(root.get(TherapySession_.therapySessionStatus), 1));
		if(userId!=-1) {
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionProviderId), userId));
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionLeaderId), userId));
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionSupervisorId), userId));
		}
		Selection[] selections= new Selection[] {
				root.get(TherapySession_.therapySessionGroupId),
				root.get(TherapySession_.therapySessionId),
				root.get(TherapySession_.therapySessionDateValue),
				builder.function("to_char",String.class, root.get(TherapySession_.therapySessionDate),builder.literal("HH12:MI am")),
				groupJoin.get(TherapyGroup_.therapyGroupName),
				root.get(TherapySession_.therapySessionTopic),
				supervisorJoin.get(EmployeeProfile_.empProfileEmpid),
				supervisorJoin.get(EmployeeProfile_.empProfileFullname),
				leaderJoin.get(EmployeeProfile_.empProfileEmpid),
				leaderJoin.get(EmployeeProfile_.empProfileFullname),
		};
		cq.select(builder.construct(TherapySessionBean.class, selections));
		cq.where(builder.and(predicateByOpenSession.toArray(new Predicate[predicateByOpenSession.size()])),builder.or(predicates.toArray(new Predicate[predicates.size()]))).orderBy(builder.desc(root.get(TherapySession_.therapySessionDateValue)));
		List<TherapySessionBean> openSessionObj=new ArrayList<TherapySessionBean>();
		openSessionObj=em.createQuery(cq).getResultList();
		return openSessionObj;
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
        therapyGroup.setTherapyGroupLeaderId(dataToSave.getLeaderId());
        therapyGroup.setTherapyGroupSupervisorId(dataToSave.getSupervisorId());
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
	 * to save the notes
	 */
	@Override
	public void saveNotes(List<AddNoteBean> data) throws Exception {
		for(int i=0;i<data.size();i++){
			if(data.get(i).getPatientDetailsId()==-1){
				 TherapySessionPatientDetails therapySessionPatientDetails = new TherapySessionPatientDetails();
				 therapySessionPatientDetails.setTherapySessionPatientDetailsEnteredBy(data.get(i).getPatientDetailsModifiedBy());
			     therapySessionPatientDetails.setTherapySessionPatientDetailsEnteredOn(therapyGroupRepository.findCurrentTimeStamp());
			     therapySessionPatientDetails.setTherapySessionPatientDetailsGwid(data.get(i).getGwid());  
			 	 if(data.get(i).getPatientDetailsId()!=-1){ 
			 		therapySessionPatientDetails.setTherapySessionPatientDetailsId(data.get(i).getPatientDetailsId());
			 	 }
			 	 therapySessionPatientDetails.setTherapySessionPatientDetailsModifiedBy(data.get(i).getPatientDetailsModifiedBy());
			 	 therapySessionPatientDetails.setTherapySessionPatientDetailsModifiedOn(therapyGroupRepository.findCurrentTimeStamp());
			 	 therapySessionPatientDetails.setTherapySessionPatientDetailsPatientId(Integer.parseInt(data.get(i).getPatientId()));
			 	 therapySessionPatientDetails.setTherapySessionPatientDetailsSessionId(data.get(i).getSessionId());
			     therapySessionPatientDetails.setTherapySessionPatientDetailsValue(data.get(i).getValue());
			     therapySessionPatientDetailsRepository.saveAndFlush(therapySessionPatientDetails);
			}
		   else{
			CriteriaBuilder cb=em.getCriteriaBuilder();
			CriteriaUpdate<TherapySessionPatientDetails> cu=cb.createCriteriaUpdate(TherapySessionPatientDetails.class);
			Root<TherapySessionPatientDetails> root = cu.from(TherapySessionPatientDetails.class);
			cu.set(root.get(TherapySessionPatientDetails_.therapySessionPatientDetailsValue), data.get(i).getValue());
			cu.where(cb.and(root.get(TherapySessionPatientDetails_.therapySessionPatientDetailsGwid).in(data.get(i).getGwid()),
					cb.equal(root.get(TherapySessionPatientDetails_.therapySessionPatientDetailsSessionId),data.get(i).getSessionId()),
					cb.equal(root.get(TherapySessionPatientDetails_.therapySessionPatientDetailsPatientId),data.get(i).getPatientId())));
					this.em.createQuery(cu).executeUpdate();
			}
		}
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
	 * To get group and sessions data
	 */
	@Override
	public Map<String,Object> listGroupandSessionData(Integer groupId) {
		Map<String, Object> mapObject=new HashMap<String,Object>();
		List<TherapyGroup> getGroupData = listGroupData(groupId.toString());
		List<TherapySessionBean> getOpenSessions = getOpenSessions(-1,groupId);
		mapObject.put("groupData", getGroupData);
		mapObject.put("sessionData", getOpenSessions);
		return mapObject;
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
    public Map<String, Object>  createTherapySession(String dataToSave) throws Exception {
        JSONObject therapyData= new JSONObject(dataToSave);
        int providerId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("providerId").toString())).or("-1"));
        int posId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("posId").toString())).or("-1"));
        int groupId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("groupId").toString())).or("-1"));
        String therapyDate=Optional.fromNullable(Strings.emptyToNull(therapyData.get("sessionDate").toString())).or("");
        String patientIds=Optional.fromNullable(Strings.emptyToNull(therapyData.get("patientIds").toString())).or("");
        String sessionTopic=Optional.fromNullable(Strings.emptyToNull(therapyData.get("groupTopic").toString())).or("");
        int supervisorList=Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("supervisorList").toString())).or("-1"));
        int leaderList=Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("leaderList").toString())).or("-1"));
        int therapySessionStatus = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("sessionStatus").toString())).or("-1"));
        String therapySessionId =Optional.fromNullable(Strings.emptyToNull(therapyData.get("sessionId").toString())).or("");
        int therapyId = 0;
        if(therapySessionId.trim().equalsIgnoreCase("")  ||therapySessionId.trim().equalsIgnoreCase("0") ){
        TherapySession therapySession=new TherapySession();
        therapySession.setTherapySessionGroupId(groupId);
        if(providerId!=-1)
        	therapySession.setTherapySessionProviderId(providerId);
        therapySession.setTherapySessionPosId(posId);
        therapySession.setTherapySessionTopic(sessionTopic);
        therapySession.setTherapySessionLeaderId(leaderList);
        therapySession.setTherapySessionSupervisorId(supervisorList);
        SimpleDateFormat ft = new SimpleDateFormat ("MM/dd/yyyy kk:mm:ss");
        Date date = (Date) ft.parse(therapyDate);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        DateFormat mmformat=new SimpleDateFormat("yyyy-MM-dd");
        therapySession.setTherapySessionDate(new Timestamp(date.getTime()));
        therapySession.setTherapySessionDateValue(java.sql.Date.valueOf(mmformat.format(date)));
        therapySession.setTherapySessionStatus(therapySessionStatus);
        therapySession = therapySessionRepository.saveAndFlush(therapySession);
        therapyId=therapySession.getTherapySessionId();
        String[] patientsSplit=patientIds.split("~");
        if(!patientsSplit.equals("-1")) {
        for(int i=0;i<patientsSplit.length;i++){
            TherapySessionDetails therapyDetails = new TherapySessionDetails();
            therapyDetails.setTherapySessionDetailsPatientId(Integer.parseInt(patientsSplit[i]));
            if(therapyId==0)
                therapyDetails.setTherapySessionDetailsSessionId(Integer.parseInt(therapySessionId));
            else
                therapyDetails.setTherapySessionDetailsSessionId(therapyId);
            therapySessionDetailsRepository.saveAndFlush(therapyDetails);
        } }
        }
        List<TherapyPatientsBean> patientData;
        TherapySessionBean sessionData;
        if(therapyId==0) {
        	patientData = getPatientData(groupId,Integer.parseInt(therapySessionId));
        	sessionData = fetchSessionDataForPrint(Integer.parseInt(therapySessionId),groupId);
        }
        else {
        	 patientData = getPatientData(groupId,therapyId);
    		 sessionData = fetchSessionDataForPrint(therapyId,groupId);
        }
		
/*		List<TherapySession> sessionData = therapySessionRepository.findAll(GroupTherapySpecification.byTherapyId(therapyId));
*/		Map<String, Object> mapObject=new HashMap<String,Object>();
		mapObject.put("patientData",patientData);
		mapObject.put("sessionData",sessionData);
		return mapObject;
    }
	
    /**
	 * To get the patient data by groupid
	 */
	@Override
	public Map<String, Object> getPatientData(String dataToSearch) throws Exception {
		Map<String, Object> mapObject=new HashMap<String,Object>();
		JSONObject therapyData= new JSONObject(dataToSearch);
		int therapyGroupId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("groupId").toString())).or("-1"));
		int therapySessionId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("sessionId").toString())).or("-1"));
		List<TherapyPatientsBean> patientData = getPatientData(therapyGroupId,therapySessionId);
		TherapySessionBean sessionData = fetchSessionDataForPrint(therapySessionId,therapyGroupId);
		mapObject.put("patientData",patientData);
		mapObject.put("sessionData",sessionData);
		return mapObject;
	}

	/*
	 * To get Patient data
	 */
	private List<TherapyPatientsBean> getPatientData(int therapyGroupId,
			int therapySessionId) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<TherapyPatientsBean> cq = builder.createQuery(TherapyPatientsBean.class);
		Root<TherapySessionDetails> root = cq.from(TherapySessionDetails.class);
		Join<TherapySessionDetails,TherapySession> sessionJoin=root.join(TherapySessionDetails_.therapySession,JoinType.INNER);
		Join<TherapySession,TherapyGroupPatientMapping> groupJoin=sessionJoin.join(TherapySession_.therapyGroupPatientMapping,JoinType.INNER);
		Join<TherapySessionDetails,PatientRegistration> patientJoin=root.join(TherapySessionDetails_.patientRegistration,JoinType.INNER);
	    Join<TherapySessionDetails,TherapySessionPatientDetails> patientDetailsJoin=root.join(TherapySessionDetails_.therapySessionPatientDetails,JoinType.LEFT);
	    patientDetailsJoin.on(builder.equal(patientDetailsJoin.get(TherapySessionPatientDetails_.therapySessionPatientDetailsSessionId), therapySessionId));
	    Predicate predicateByGroupId=builder.equal(groupJoin.get(TherapyGroupPatientMapping_.therapyGroupPatientMappingGroupId), therapyGroupId);
		Predicate predicateBySessionId=builder.equal(root.get(TherapySessionDetails_.therapySessionDetailsSessionId), therapySessionId);
		Selection[] selections= new Selection[] {
				builder.count(patientDetailsJoin.get(TherapySessionPatientDetails_.therapySessionPatientDetailsId)),
				groupJoin.get(TherapyGroupPatientMapping_.therapyGroupPatientMappingGroupId),
				sessionJoin.get(TherapySession_.therapySessionId),
				patientJoin.get(PatientRegistration_.patientRegistrationId),
				patientJoin.get(PatientRegistration_.patientRegistrationAccountno),
				builder.concat(patientJoin.get(PatientRegistration_.patientRegistrationLastName),builder.concat(" ,",patientJoin.get(PatientRegistration_.patientRegistrationFirstName))),
				patientJoin.get(PatientRegistration_.patientRegistrationDob),
				sessionJoin.get(TherapySession_.therapySessionDateValue),
				builder.concat(builder.concat(builder.concat(builder.concat(builder.concat(builder.coalesce(root.get(TherapySessionDetails_.therapySessionDetailsDx1),""),builder.coalesce(builder.concat(",",root.get(TherapySessionDetails_.therapySessionDetailsDx2)),"")),
						builder.coalesce(builder.concat(",",root.get(TherapySessionDetails_.therapySessionDetailsDx3)),"")),builder.coalesce(builder.concat(",",root.get(TherapySessionDetails_.therapySessionDetailsDx4)),"")),builder.coalesce(builder.concat(",",root.get(TherapySessionDetails_.therapySessionDetailsDx5)),"")),builder.coalesce(builder.concat(",",root.get(TherapySessionDetails_.therapySessionDetailsDx6)),""))	,
				builder.concat(builder.concat(builder.concat(builder.concat(builder.concat(builder.coalesce(root.get(TherapySessionDetails_.therapySessionDetailsDx1desc),""),builder.coalesce(builder.concat(",",root.get(TherapySessionDetails_.therapySessionDetailsDx2desc)),"")),
						builder.coalesce(builder.concat(",",root.get(TherapySessionDetails_.therapySessionDetailsDx3desc)),"")),builder.coalesce(builder.concat(",",root.get(TherapySessionDetails_.therapySessionDetailsDx4desc)),"")),builder.coalesce(builder.concat(",",root.get(TherapySessionDetails_.therapySessionDetailsDx5desc)),"")),builder.coalesce(builder.concat(",",root.get(TherapySessionDetails_.therapySessionDetailsDx6desc)),""))
					
		};
		cq.distinct(true);
		cq.select(builder.construct(TherapyPatientsBean.class, selections));
		cq.where(predicateByGroupId,predicateBySessionId).groupBy(groupJoin.get(TherapyGroupPatientMapping_.therapyGroupPatientMappingGroupId),
				sessionJoin.get(TherapySession_.therapySessionId),
				patientJoin.get(PatientRegistration_.patientRegistrationId),
				patientJoin.get(PatientRegistration_.patientRegistrationAccountno),
				patientJoin.get(PatientRegistration_.patientRegistrationLastName),
				patientJoin.get(PatientRegistration_.patientRegistrationFirstName),
				patientJoin.get(PatientRegistration_.patientRegistrationDob),
				sessionJoin.get(TherapySession_.therapySessionDateValue),
				root.get(TherapySessionDetails_.therapySessionDetailsDx1),
				root.get(TherapySessionDetails_.therapySessionDetailsDx2),
				root.get(TherapySessionDetails_.therapySessionDetailsDx3),
				root.get(TherapySessionDetails_.therapySessionDetailsDx4),
				root.get(TherapySessionDetails_.therapySessionDetailsDx5),
				root.get(TherapySessionDetails_.therapySessionDetailsDx6),
				root.get(TherapySessionDetails_.therapySessionDetailsDx1desc),
				root.get(TherapySessionDetails_.therapySessionDetailsDx2desc),
				root.get(TherapySessionDetails_.therapySessionDetailsDx3desc),
				root.get(TherapySessionDetails_.therapySessionDetailsDx4desc),
				root.get(TherapySessionDetails_.therapySessionDetailsDx5desc),
				root.get(TherapySessionDetails_.therapySessionDetailsDx6desc)					
		);
		List<TherapyPatientsBean> patientData=new ArrayList<TherapyPatientsBean>();

		patientData=em.createQuery(cq).getResultList();
		return patientData;
	}




	/**
	 * To get the therapy session log data
	 */
	@Override
	public List<TherapyLogBean> therapySearchLog(String dataToSearch) throws Exception {
		JSONObject therapyData= new JSONObject(dataToSearch);
		int providerId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("providerId").toString())).or("-1"));
		int leaderId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("leaderId").toString())).or("-1"));
		int supervisorId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("supervisorId").toString())).or("-1"));
		int groupId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("groupId").toString())).or("-1"));
		String fromDate=Optional.fromNullable(Strings.emptyToNull(therapyData.get("fromDate").toString())).or("");
		String toDate=Optional.fromNullable(Strings.emptyToNull(therapyData.get("toDate").toString())).or("");
        SimpleDateFormat ft = new SimpleDateFormat ("MM/dd/yyyy");		
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<TherapyLogBean> cq = builder.createQuery(TherapyLogBean.class);
		Root<TherapySession> root = cq.from(TherapySession.class);
		Join<TherapySession,EmployeeProfile> docJoin=root.join(TherapySession_.empProfile,JoinType.LEFT);
		Join<TherapySession,EmployeeProfile> leaderJoin=root.join(TherapySession_.empProfileLeader,JoinType.LEFT);
		Join<TherapySession,EmployeeProfile> supervisorJoin=root.join(TherapySession_.empProfileSupervisor,JoinType.LEFT);
		Join<TherapySession,PosTable> posJoin=root.join(TherapySession_.posTable,JoinType.INNER);
		Join<TherapySession,TherapySessionDetails> sessionJoin=root.join(TherapySession_.therapySessionDetails,JoinType.INNER);
		Join<TherapySession,TherapyGroup> groupJoin=root.join(TherapySession_.therapyGroup,JoinType.INNER);
	    List<Predicate> predicates = new ArrayList<>();
		if(groupId!=-1)
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionGroupId), groupId));
		if(providerId!=-1)
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionProviderId), providerId));
		if(leaderId!=-1)
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionLeaderId), providerId));
		if(supervisorId!=-1)
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionSupervisorId), providerId));
		if(!fromDate.equals("")){
			Date frdate = (Date)  ft.parse(fromDate);
			predicates.add(builder.greaterThanOrEqualTo(root.get(TherapySession_.therapySessionDateValue),frdate));
		}
		if(!toDate.equals("")){
			Date todate = (Date)  ft.parse(toDate);
			predicates.add(builder.lessThanOrEqualTo(root.get(TherapySession_.therapySessionDateValue), todate));
		}
		Selection[] selections= new Selection[] {
			    root.get((TherapySession_.therapySessionId)),
			    root.get((TherapySession_.therapySessionDateValue)),
				docJoin.get(EmployeeProfile_.empProfileFullname),
				leaderJoin.get(EmployeeProfile_.empProfileFullname),
			    supervisorJoin.get(EmployeeProfile_.empProfileFullname),
				groupJoin.get(TherapyGroup_.therapyGroupName),
				posJoin.get(PosTable_.posTableFacilityComments),
			    builder.count(sessionJoin.get(TherapySessionDetails_.therapySessionDetailsPatientId)),
			    groupJoin.get(TherapyGroup_.therapyGroupId),
			    docJoin.get(EmployeeProfile_.empProfileEmpid),
			    leaderJoin.get(EmployeeProfile_.empProfileEmpid),
			    supervisorJoin.get(EmployeeProfile_.empProfileEmpid),
			    posJoin.get(PosTable_.posTableRelationId),
			    root.get((TherapySession_.therapySessionEndTime)),
			    root.get(TherapySession_.therapySessionDate),
			    root.get(TherapySession_.therapySessionStatus)
				
		};
		cq.select(builder.construct(TherapyLogBean.class, selections));
		cq.where(predicates.toArray(new Predicate[predicates.size()])).groupBy(root.get((TherapySession_.therapySessionId)),root.get(TherapySession_.therapySessionDateValue),docJoin.get(EmployeeProfile_.empProfileFullname),groupJoin.get(TherapyGroup_.therapyGroupName),posJoin.get(PosTable_.posTableFacilityComments),groupJoin.get(TherapyGroup_.therapyGroupId),docJoin.get(EmployeeProfile_.empProfileEmpid),posJoin.get(PosTable_.posTableRelationId),root.get((TherapySession_.therapySessionEndTime)),root.get(TherapySession_.therapySessionDate),root.get(TherapySession_.therapySessionStatus),leaderJoin.get(EmployeeProfile_.empProfileFullname),
			    supervisorJoin.get(EmployeeProfile_.empProfileFullname),leaderJoin.get(EmployeeProfile_.empProfileEmpid),
			    supervisorJoin.get(EmployeeProfile_.empProfileEmpid));
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
		int therapyId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(therapyData.get("therapyId").toString())).or("-1"));
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
		if(therapyId!=-1) {
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionId),therapyId));
		}
		Selection[] selections= new Selection[] {
				patientJoin.get(PatientRegistration_.patientRegistrationId),
				patientJoin.get(PatientRegistration_.patientRegistrationAccountno),
				patientJoin.get(PatientRegistration_.patientRegistrationLastName),
				patientJoin.get(PatientRegistration_.patientRegistrationFirstName),
				patientJoin.get(PatientRegistration_.patientRegistrationDob),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsSessionId),
				sessionJoin.get(TherapySessionDetails_.therapySessionEndTime),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx1),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx1desc),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx2),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx2desc),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx3),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx3desc),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx4),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx4desc),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx5),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx5desc),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx6),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx6desc),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx7),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx7desc),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx8),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx8desc)
				
		};
		cq.select(builder.construct(TherapyPatientsBean.class, selections));
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		List<TherapyPatientsBean> confData=new ArrayList<TherapyPatientsBean>();

		 confData=em.createQuery(cq).getResultList();
		 return confData;

	}
	
	/**
	 * To fetch shortCut Code
	 */
	@Override
	public List<String> fetchShortcutCode(Integer group){
		CriteriaBuilder builder=em.getCriteriaBuilder();
		CriteriaQuery<Object> cq=builder.createQuery();
		Root<GeneralShortcut> root=cq.from(GeneralShortcut.class);
		cq.select(root.get(GeneralShortcut_.generalShortcutCode));
		cq.where(builder.and(root.get(GeneralShortcut_.generalShortcutMapGroupId).in(group)),
				 builder.equal(root.get(GeneralShortcut_.generalShortcutIsactive),true));
		cq.orderBy(builder.asc(root.get(GeneralShortcut_.generalShortcutCode)));
		List<Object> resultSet=em.createQuery(cq).getResultList();
	  	 List<String> addNotes = new ArrayList<String>();
	  	for(int i=0;i<resultSet.size();i++)
		{
	  		addNotes.add(resultSet.get(i).toString());
		}
	  	return addNotes;
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
	
	/**
	 * To fetch shortCut Description
	 */
	@Override
	public List<String> fetchShortcutData(String shortcutId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<GeneralShortcut> root = cq.from(GeneralShortcut.class);
		cq.select(root.get(GeneralShortcut_.generalShortcutDescription));
		cq.where(builder.and(root.get(GeneralShortcut_.generalShortcutMapGroupId).in(1,2,3),
					builder.equal(root.get(GeneralShortcut_.generalShortcutIsactive),true),
					root.get(GeneralShortcut_.generalShortcutCode).in(shortcutId)));
		cq.orderBy(builder.asc(root.get(GeneralShortcut_.generalShortcutCode)));
		List<Object> resultSet=em.createQuery(cq).getResultList();
	  	List<String> addNotes = new ArrayList<String>();
	  	for(int i=0;i<resultSet.size();i++)
		{
	  		addNotes.add(resultSet.get(i).toString());
		}
	  	return addNotes;
	}

	/**
	 * To fetch the elements data
	 */
	@Override
	public List<AddTherapyBean> fetchNotesData(String gwid,Integer patientId,Integer sessionId,Boolean isPatient) {
		List<String> gwidList = new ArrayList<String>(Arrays.asList(gwid.split(",")));
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<ClinicalElements> root = cq.from(ClinicalElements.class);
		Join<ClinicalElements, ClinicalElementsOptions> clinicalJoin=root.join(ClinicalElements_.clinicalElementsOptions ,JoinType.LEFT);
		Join<ClinicalElements, TherapySessionPatientDetails> patientJoin=root.join(ClinicalElements_.therupeticElements ,JoinType.LEFT);
		Predicate checkPatientId=builder.equal(patientJoin.get(TherapySessionPatientDetails_.therapySessionPatientDetailsPatientId),patientId);
		Predicate checkSessionId=builder.equal(patientJoin.get(TherapySessionPatientDetails_.therapySessionPatientDetailsSessionId),sessionId);
		patientJoin.on(builder.and(checkPatientId,checkSessionId));
		
		if(isPatient){//EditNotes
		cq.select(builder.construct(AddTherapyBean.class,root.get(ClinicalElements_.clinicalElementsGwid),
					root.get(ClinicalElements_.clinicalElementsName),
					root.get(ClinicalElements_.clinicalElementsDatatype),
					clinicalJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsName),
					clinicalJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsValue),
					patientJoin.get(TherapySessionPatientDetails_.therapySessionPatientDetailsValue).alias("result"),
					patientJoin.get(TherapySessionPatientDetails_.therapySessionPatientDetailsId)));
		}
		else{//AddNotes
			cq.select(builder.construct(AddTherapyBean.class,root.get(ClinicalElements_.clinicalElementsGwid),
					root.get(ClinicalElements_.clinicalElementsName),
					root.get(ClinicalElements_.clinicalElementsDatatype),
					clinicalJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsName),
					clinicalJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsValue),
					patientJoin.get(TherapySessionPatientDetails_.therapySessionPatientDetailsId)));
		}
		cq.where(root.get(ClinicalElements_.clinicalElementsGwid).in(gwidList));
		cq.orderBy(builder.asc(root.get(ClinicalElements_.clinicalElementsId)));
		List<Object> resultset  = em.createQuery(cq).getResultList();
		List<AddTherapyBean> notesData = new ArrayList<AddTherapyBean>();
		for(int i=0;i<resultset.size();i++){
			AddTherapyBean gw = (AddTherapyBean)resultset.get(i);
			notesData.add(gw);
		}
		return notesData;
	}
	
	/**
	 * To fetch data for addTherapeuticIntervention
	 */
	@Override
	public List<AddTherapyBean> fetchDataforTherapy(String gwid,Integer sessionId,Boolean isPatient) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<AddTherapyBean> cq = builder.createQuery(AddTherapyBean.class);
		Root<ClinicalElements> root = cq.from(ClinicalElements.class);
		Join<ClinicalElements, TherapySessionPatientDetails> patientJoin=root.join(ClinicalElements_.therupeticElements ,JoinType.LEFT);
		Predicate checkSessionId=builder.equal(patientJoin.get(TherapySessionPatientDetails_.therapySessionPatientDetailsSessionId),sessionId);
		patientJoin.on(checkSessionId);
		
		if(isPatient){//Add Therapy
			cq.select(builder.construct(AddTherapyBean.class,root.get(ClinicalElements_.clinicalElementsGwid),
					root.get(ClinicalElements_.clinicalElementsName),
					root.get(ClinicalElements_.clinicalElementsDatatype),
					patientJoin.get(TherapySessionPatientDetails_.therapySessionPatientDetailsValue).alias("result"),
					patientJoin.get(TherapySessionPatientDetails_.therapySessionPatientDetailsId)));
		}
		else{//Edit therapy
			cq.select(builder.construct(AddTherapyBean.class,root.get(ClinicalElements_.clinicalElementsGwid),
					root.get(ClinicalElements_.clinicalElementsName),
					root.get(ClinicalElements_.clinicalElementsDatatype),
					patientJoin.get(TherapySessionPatientDetails_.therapySessionPatientDetailsId)));
		}
		cq.where(builder.and(root.get(ClinicalElements_.clinicalElementsGwid).in(gwid)));
		List<AddTherapyBean> resultset  = em.createQuery(cq).setMaxResults(1).getResultList();
		return resultset;
	}
	
	/**
	 * To fetch Group therapy complete session details of each patient
	 */
	@Override
	public TherapyPrintBean fetchGrouptherapyPrintData(Integer groupId,Integer sessionId,Integer paientId,String gwids) {
		List<String> gwidList = new ArrayList<String>(Arrays.asList(gwids.split(",")));
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<TherapyPrintBean> cq = builder.createQuery(TherapyPrintBean.class);
		Root<TherapyGroup> root = cq.from(TherapyGroup.class);
		Selection[] selections= new Selection[] {
				root.get(TherapyGroup_.therapyGroupId),
				root.get(TherapyGroup_.therapyGroupName),
				root.get(TherapyGroup_.therapyGroupProviderId),
				root.get(TherapyGroup_.therapyGroupLeaderId),
				root.get(TherapyGroup_.therapyGroupSupervisorId),
				root.get(TherapyGroup_.therapyGroupDescription),
				root.get(TherapyGroup_.therapyGroupPosId),
				root.get(TherapyGroup_.therapyGroupDefaulttime),
				root.get(TherapyGroup_.therapyGroupIsActive)
		};
		cq.select(builder.construct(TherapyPrintBean.class,selections));
		cq.where(builder.equal(root.get(TherapyGroup_.therapyGroupId),groupId));
		List<TherapyPrintBean> therapyPrintBeanList =new ArrayList<TherapyPrintBean>(); 
		therapyPrintBeanList=em.createQuery(cq).getResultList();
		TherapyPrintBean therapyPrintBean=null;
		if(therapyPrintBeanList.size()>0)
		therapyPrintBean=therapyPrintBeanList.get(0);
		therapyPrintBean.setTherapyElementsDetails(fetchClinicalDataForPrint(sessionId,paientId,gwidList));
		therapyPrintBean.setTherapyPatientDetails(fetchPatientDataForPrint(sessionId,groupId));
		therapyPrintBean.setTherapySessionDetails(fetchSessionDataForPrint(sessionId,groupId));
		return therapyPrintBean;
	}
	
	/**
	 * To fetch Group therapy session data based on given sessionId
	 */
	public TherapySessionBean fetchSessionDataForPrint(Integer sessionId,Integer groupId){
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<TherapySessionBean> cq = builder.createQuery(TherapySessionBean.class);
		Root<TherapySession> root = cq.from(TherapySession.class);
		Join<TherapySession,EmployeeProfile> docJoin=root.join(TherapySession_.empProfile,JoinType.LEFT);
		Join<TherapySession,PosTable> posJoin=root.join(TherapySession_.posTable,JoinType.INNER);
		
		Selection[] selections= new Selection[] {
				root.get(TherapySession_.therapySessionId),
				builder.function("to_char",String.class, root.get(TherapySession_.therapySessionDate),builder.literal("HH:MI am")),
				root.get(TherapySession_.therapySessionProviderId),
				docJoin.get(EmployeeProfile_.empProfileFullname),
				root.get(TherapySession_.therapySessionLeaderId),
				root.get(TherapySession_.therapySessionSupervisorId),
				root.get(TherapySession_.therapySessionTopic),
				root.get(TherapySession_.therapySessionPosId),
				posJoin.get(PosTable_.posTableFacilityComments),
				root.get(TherapySession_.therapySessionDateValue),
				root.get(TherapySession_.therapySessionStatus),
				root.get(TherapySession_.therapySessionEndTime),
				
		};
		
		cq.select(builder.construct(TherapySessionBean.class,selections));
		cq.where(builder.equal(root.get(TherapySession_.therapySessionId),sessionId));	
		List<TherapySessionBean> therapySessionBeanList =new ArrayList<TherapySessionBean>();
		therapySessionBeanList=em.createQuery(cq).getResultList();

		return therapySessionBeanList.get(0);
	}		
	
	/**
	 * To fetch list of patients with details participated in session
	 */
	
	public List<TherapyPatientsBean> fetchPatientDataForPrint(Integer sessionId, Integer groupId){
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<TherapyPatientsBean> cq = builder.createQuery(TherapyPatientsBean.class);
		Root<TherapySession> root = cq.from(TherapySession.class);
		Join<TherapySession,TherapySessionDetails> sessionJoin=root.join(TherapySession_.therapySessionDetails,JoinType.INNER);
		Join<TherapySessionDetails,PatientRegistration> patientJoin=sessionJoin.join(TherapySessionDetails_.patientRegistration,JoinType.INNER);
	    List<Predicate> predicates = new ArrayList<>();
		if(groupId!=-1)
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionGroupId), groupId));
		if(sessionId!=-1)
			predicates.add(builder.equal(root.get(TherapySession_.therapySessionId), sessionId));

		Selection[] selections= new Selection[] {
				patientJoin.get(PatientRegistration_.patientRegistrationId),
				patientJoin.get(PatientRegistration_.patientRegistrationAccountno),
				patientJoin.get(PatientRegistration_.patientRegistrationLastName),
				patientJoin.get(PatientRegistration_.patientRegistrationFirstName),
				patientJoin.get(PatientRegistration_.patientRegistrationDob),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsSessionId),
				sessionJoin.get(TherapySessionDetails_.therapySessionEndTime),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx1),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx1desc),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx2),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx2desc),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx3),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx3desc),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx4),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx4desc),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx5),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx5desc),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx6),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx6desc),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx7),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx7desc),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx8),
				sessionJoin.get(TherapySessionDetails_.therapySessionDetailsDx8desc),
				
				
				
				
		};
		cq.select(builder.construct(TherapyPatientsBean.class, selections));
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		List<TherapyPatientsBean> therapyPatientsBean=new ArrayList<TherapyPatientsBean>();		
		therapyPatientsBean=em.createQuery(cq).getResultList();		
		return therapyPatientsBean;
	}

	/**
	 * To fetch list of clinical elements documents for patient in session
	 */
	public List<AddTherapyBean> fetchClinicalDataForPrint(Integer sessionId,Integer paientId,List<String> gwidList){
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<AddTherapyBean> cq = builder.createQuery(AddTherapyBean.class);
		Root<ClinicalElements> root = cq.from(ClinicalElements.class);
		Join<ClinicalElements, TherapySessionPatientDetails> patientJoin=root.join(ClinicalElements_.therupeticElements ,JoinType.LEFT);
		Join<ClinicalElements, ClinicalElementsOptions> optionsJoin=root.join(ClinicalElements_.clinicalElementsOptions ,JoinType.LEFT);
		
		Predicate checkSessionId=builder.equal(patientJoin.get(TherapySessionPatientDetails_.therapySessionPatientDetailsSessionId),sessionId);
		Predicate checkPatientId=builder.equal(patientJoin.get(TherapySessionPatientDetails_.therapySessionPatientDetailsPatientId),paientId);
		patientJoin.on(builder.and(checkSessionId,checkPatientId));
				
		cq.select(builder.construct(AddTherapyBean.class,root.get(ClinicalElements_.clinicalElementsGwid),
							root.get(ClinicalElements_.clinicalElementsName),
							root.get(ClinicalElements_.clinicalElementsDatatype),
							optionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsName),
							optionsJoin.get(ClinicalElementsOptions_.clinicalElementsOptionsValue),
							patientJoin.get(TherapySessionPatientDetails_.therapySessionPatientDetailsValue).alias("result")));
		cq.where(root.get(ClinicalElements_.clinicalElementsGwid).in(gwidList));
		cq.orderBy(builder.asc(root.get(ClinicalElements_.clinicalElementsId)));
		List<AddTherapyBean> clinicalData  =new ArrayList<AddTherapyBean>();
		clinicalData=em.createQuery(cq).getResultList();
		return clinicalData;
	}
	
}