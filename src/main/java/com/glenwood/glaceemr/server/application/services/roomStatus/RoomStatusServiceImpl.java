package com.glenwood.glaceemr.server.application.services.roomStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.models.ActivityLog;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.AlertEvent_;
import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.CurrentMedication;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Encounter_;
import com.glenwood.glaceemr.server.application.models.H076;
import com.glenwood.glaceemr.server.application.models.H076_;
import com.glenwood.glaceemr.server.application.models.LabEntries;
import com.glenwood.glaceemr.server.application.models.LabEntries_;
import com.glenwood.glaceemr.server.application.models.LabGroups;
import com.glenwood.glaceemr.server.application.models.LabGroups_;
import com.glenwood.glaceemr.server.application.models.LeafLibrary;
import com.glenwood.glaceemr.server.application.models.LeafPatient;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.PosTable_;
import com.glenwood.glaceemr.server.application.models.Prescription;
import com.glenwood.glaceemr.server.application.models.Room;
import com.glenwood.glaceemr.server.application.repositories.ActivityLogRepository;
import com.glenwood.glaceemr.server.application.repositories.AlertEventRepository;
import com.glenwood.glaceemr.server.application.repositories.ChartRepository;
import com.glenwood.glaceemr.server.application.repositories.CurrentMedicationRepository;
import com.glenwood.glaceemr.server.application.repositories.EmployeeProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.EncounterRepository;
import com.glenwood.glaceemr.server.application.repositories.LabEntriesRepository;
import com.glenwood.glaceemr.server.application.repositories.LeafLibraryRepository;
import com.glenwood.glaceemr.server.application.repositories.LeafPatientRepository;
import com.glenwood.glaceemr.server.application.repositories.PosTableRepository;
import com.glenwood.glaceemr.server.application.repositories.PrescriptionRepository;
import com.glenwood.glaceemr.server.application.repositories.RoomRepository;
import com.glenwood.glaceemr.server.application.specifications.RoomStatusSpecification;

@Service
public class RoomStatusServiceImpl implements  RoomStatusService {
	@Autowired
	EntityManager em;

	@Autowired
	PosTableRepository posTableRepository;
	
	@Autowired
	EmployeeProfileRepository employeeProfileRepository;
	
	@Autowired
	ChartRepository chartRepository;
	
	@Autowired
	EncounterRepository encounterRepository;

	@Autowired
	AlertEventRepository alertEventRepository;
	
	@Autowired	
	RoomRepository roomRepository;
	
	@Autowired
	ActivityLogRepository activityLogRepository;
	
	@Autowired
	PrescriptionRepository prescriptionRepository;
	
	@Autowired
	CurrentMedicationRepository currentMedicationRepository;
	
	@Autowired
	LabEntriesRepository labEntriesRepository;
	
	@Autowired
	LeafPatientRepository leafPatientRepository;
	
	@Autowired
	LeafLibraryRepository leafLibraryRepository;
	
	/**
	 * to get locations
	 * @return locations
	 */
	@Override
	public JSONObject getPos() {
		List<PosTable> locations = posTableRepository.findAll(RoomStatusSpecification.getPos());
		JSONObject finalJson = new JSONObject();
		JSONArray loc = new JSONArray();
		try{
		for(int i=0;i<locations.size();i++){
			JSONObject jsono=new JSONObject();
			jsono.put("posid",locations.get(i).getPosTableRelationId());
  		    jsono.put("posName","("+locations.get(i).getPosTablePlaceOfService()+")".concat(locations.get(i).getPosTableFacilityComments()));
  		    loc.put(i,jsono);
  	  	} 
		finalJson.put("Locations",loc);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return finalJson;
		}
	
	/**
	 * to get provider information
	 * @return List<EmployeeProfile>
	 */
	@Override
	public JSONObject getproviders() {
		List<EmployeeProfile> empprofile = employeeProfileRepository.findAll(RoomStatusSpecification.getproviders());
		JSONObject finalJson = new JSONObject();
		JSONArray providers=new JSONArray();
		try{
		for(int i=0;i<empprofile.size();i++){
			JSONObject jsono=new JSONObject();
				jsono.put("id",empprofile.get(i).getEmpProfileEmpid());
				jsono.put("name",empprofile.get(i).getEmpProfileFullname());
			    providers.put(i,jsono);		
			    } 
		    finalJson.put("providers",providers);
		}catch (JSONException e) {
			e.printStackTrace();
		}
			return finalJson;
	}
 
	/**
	 * to get roomName
	 * @return List<Room>
	 */
	@Override
	public JSONObject getRoomName() {
		List<Room> room = roomRepository.findAll(RoomStatusSpecification.getRoomIsActive());
		JSONObject finalJson = new JSONObject();
		JSONArray roomDetail=new JSONArray();
		try{
		for(int i=0;i<room.size();i++){
			JSONObject jsono=new JSONObject();
				jsono.put("roomName",room.get(i).getRoomName());
				jsono.put("roomId",room.get(i).getRoomId());
				roomDetail.put(i,jsono);		
			    } 
		    finalJson.put("roomDetail",roomDetail);
		}catch (JSONException e) {
			e.printStackTrace();
		}
			return finalJson;
	}
	
    /**
     * to get todays patients data
     * @return patients data based on pos
     */
	@Override
	public List<RoomStatusBean> getTodaysPatientsData(Integer pos) {
		List<RoomStatusBean> roomStatusBean = null;
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq = builder.createQuery();
			Root<Encounter> root = cq.from(Encounter.class);
			Join<Encounter,Chart> encjoin = root.join(Encounter_.chart,JoinType.INNER);
			DateFormat date=new SimpleDateFormat("MM/dd/yyyy");
	        Calendar cal = Calendar.getInstance();
			String currentdate=date.format(cal.getTime());
			encjoin.on(builder.and(builder.equal(root.get(Encounter_.encounterType),1),
	        		builder.equal(root.get(Encounter_.encounterStatus),1), 
	        		builder.equal(root.get(Encounter_.encounterPos),pos),
	        		builder.equal(builder.function("to_char",String.class,root.get(Encounter_.encounterDate),builder.literal("MM/dd/yyyy")),currentdate)));
	        Join<Chart,PatientRegistration> chpatJoin = encjoin.join(Chart_.patientRegistrationTable,JoinType.INNER);
	        cq.select(builder.construct(RoomStatusBean.class,chpatJoin.get(PatientRegistration_.patientRegistrationId),
	        		builder.concat(chpatJoin.get(PatientRegistration_.patientRegistrationLastName),
	        		chpatJoin.get(PatientRegistration_.patientRegistrationFirstName))));         
	      	List<Object> resultSet = em.createQuery(cq).getResultList();
	      	roomStatusBean = new ArrayList<RoomStatusBean>();
			for (int i = 0; i < resultSet.size(); i++) {
				RoomStatusBean rsb = (RoomStatusBean) resultSet.get(i);
				roomStatusBean.add(rsb);
			}
		} 
		catch (Exception exception) {
			exception.printStackTrace();
		}
		return roomStatusBean;
	}
    
	/**
	 * to update room number
	 * @return List<Encounter>
	 */
	@Override
	public List<Encounter> updateRoomNo(Integer pos, Integer addPatientId,Short roomtoAdd) {
		List<Encounter> encounterList = new ArrayList<Encounter>();
		Chart chart=chartRepository.findOne(RoomStatusSpecification.getChartId(addPatientId));
		Integer chartId=null;
		if(chart!=null){
			chartId=com.google.common.base.Optional.fromNullable(chart.getChartId()).or(-1);
		}
        try{
        	encounterList = encounterRepository.findAll(RoomStatusSpecification.getUpdateEncounter(pos,chartId,roomtoAdd));
			for (int j = 0; j < encounterList.size(); j++) {				
				Encounter encounter = encounterList.get(j);
				encounter.setEncounterRoom(roomtoAdd);
				encounter.setEncounterRoomIsactive(true);
				encounterRepository.saveAndFlush(encounter);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encounterList;
	}

	/**
	 * to get roomStatus
	 * @return List<RoomStatusBean>
	 */
	@Override
	public List<RoomStatusBean> getRoomStatus() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<PosTable> root = cq.from(PosTable.class);		
		Join<PosTable,Encounter> posJoin = root.join(PosTable_.encounter,JoinType.LEFT);
	    Join<Encounter,Room> encRoomJoin = posJoin.join(Encounter_.room,JoinType.LEFT);
		Join<Encounter,EmployeeProfile> empJoin = posJoin.join(Encounter_.empProfileEmpId,JoinType.LEFT);
	    Join<Encounter,Chart> chartJoin = posJoin.join(Encounter_.chart,JoinType.LEFT);
		posJoin.on(builder.and(builder.notEqual(posJoin.get(Encounter_.encounterRoom),1),
				builder.equal(posJoin.get(Encounter_.encounterType),1),
				builder.equal(posJoin.get(Encounter_.encounterStatus),1),
				builder.equal(posJoin.get(Encounter_.encounterRoomIsactive),true)));
	    Join<Chart,PatientRegistration> patJoin = chartJoin.join(Chart_.patientRegistrationTable,JoinType.INNER);
	    Join<PatientRegistration,H076> h076Join = patJoin.join(PatientRegistration_.referringPhyTable,JoinType.LEFT);
	    Join<PatientRegistration,AlertEvent> alertJoin = patJoin.join(PatientRegistration_.alertEvent,JoinType.LEFT);
		alertJoin.on(alertJoin.get(AlertEvent_.alertEventCategoryId).in(2,3,4));		
	    cq.select(builder.construct(RoomStatusBean.class,
	    		posJoin.get(Encounter_.encounterRoom),
	    		root.get(PosTable_.posTableRelationId),
	    		builder.concat(builder.concat("(",builder.concat(root.get(PosTable_.posTablePlaceOfService),")")),root.get(PosTable_.posTableFacilityComments)),
	    		empJoin.get(EmployeeProfile_.empProfileFullname),
	    		builder.concat(patJoin.get(PatientRegistration_.patientRegistrationFirstName),
	    		patJoin.get(PatientRegistration_.patientRegistrationLastName)),
	    		patJoin.get(PatientRegistration_.patientRegistrationId),
	    		h076Join.get(H076_.h076020),
	    		alertJoin.get(AlertEvent_.alertEventCategoryId),
	    		builder.function("to_char",String.class,builder.function("age",String.class,builder.literal(encounterRepository.findCurrentTimeStamp()),posJoin.get(Encounter_.encounterCreatedDate)),builder.literal("DD:HH24:MI:SS")),
	    		builder.function("to_char",String.class,builder.function("age",String.class,builder.literal(alertEventRepository.findCurrentTimeStamp()),alertJoin.get(AlertEvent_.alertEventCreatedDate)),builder.literal("DD:HH24:MI:SS"))));
	    cq.where(builder.and(builder.equal(root.get(PosTable_.posTablePosCode),11),builder.equal(root.get(PosTable_.posTableIsActive),true)));
	    cq.orderBy(builder.asc(posJoin.get(Encounter_.encounterPos)));
        List<Object> resultSet = em.createQuery(cq).getResultList();
		List<RoomStatusBean> roomStatusBean = new ArrayList<RoomStatusBean>();        
		for (int i = 0; i < resultSet.size(); i++) {
			RoomStatusBean problemreportBean = (RoomStatusBean) resultSet.get(i);
			roomStatusBean.add(problemreportBean);
		}
		return roomStatusBean;
	} 	
	
	/**
	 * to transfer patients
	 * @return List<Encounter>
	 */
	@Override
	public List<Encounter> updateTransferRooms(Short oldRoom,Short newRoom, Integer pos) {
		List<Encounter> encounterList = new ArrayList<Encounter>();
		encounterList = encounterRepository.findAll(RoomStatusSpecification.UpdateTransferRoom(oldRoom,newRoom,pos));
		for (int j=0;j<encounterList.size();j++) {				
			encounterList.get(j).setEncounterRoom(newRoom);
		}
		encounterRepository.save(encounterList);
		return encounterList;
	}
		
	/**
	 * to swap patients
	 * @return List<Encounter>
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	@Override
	public List<Encounter> updateSwapPatients(Short oldRoom,Short newRoom,Integer pos) {
        List<Encounter> chartidsfortoswap = new ArrayList<Encounter>();
        chartidsfortoswap = encounterRepository.findAll(RoomStatusSpecification.getchartIds(oldRoom, pos));
        List<Encounter> chartidswithRoom = new ArrayList<Encounter>();
        chartidswithRoom = encounterRepository.findAll(RoomStatusSpecification.chartidswithroom(newRoom,pos));        
        for(int i=0;i<chartidsfortoswap.size();i++)
        {    	  
        	chartidsfortoswap.get(i).setEncounterRoom(newRoom);  
    	  
        }
        encounterRepository.save(chartidswithRoom);
        for(int i=0;i<chartidswithRoom.size();i++)
        {
        	chartidswithRoom.get(i).setEncounterRoom(oldRoom);  
        }  
        encounterRepository.save(chartidsfortoswap);
        return chartidswithRoom;
	}
   
	/**
	 * to get ordered information
	 * @return orderedLabVaccines,orderedLabDetails
	 */
	@Override
	public List<OrderedBean> getOrdered(Integer patientId) {
		Chart chart=chartRepository.findOne(RoomStatusSpecification.getChartId(patientId));
		Integer chartId=null;
		if(chart!=null){
			chartId=com.google.common.base.Optional.fromNullable(chart.getChartId()).or(-1);
		}
		Encounter encounter=encounterRepository.findOne(RoomStatusSpecification.getEncId(chartId));
		Integer encId=null;
		if(encounter!=null){
			encId=com.google.common.base.Optional.fromNullable(encounter.getEncounterId()).or(-1);
		}
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<LabEntries> root = cq.from(LabEntries.class);
		Join<LabEntries,EmployeeProfile> empJoin = root.join(LabEntries_.empProfile,JoinType.LEFT);
		Join<LabEntries,Encounter> enclabJoin = root.join(LabEntries_.encounter,JoinType.LEFT);
		Join<LabEntries,LabGroups> labJoin = root.join(LabEntries_.labGroups,JoinType.INNER);
		cq.select(builder.construct(OrderedBean.class,
				builder.function("to_char",String.class,root.get(LabEntries_.labEntriesOrdOn),builder.literal("mm/dd/yyyy")),
				root.get(LabEntries_.labEntriesTestDesc),
				root.get(LabEntries_.labEntriesTestStatus),
				builder.coalesce(empJoin.get(EmployeeProfile_.empProfileFullname),""),
				root.get(LabEntries_.labEntriesResultNotes),
				root.get(LabEntries_.labEntriesLoinc),
				root.get(LabEntries_.labEntriesCpt),
				root.get(LabEntries_.labEntriesTestdetailId),
				root.get(LabEntries_.labEntriesTestId),
				root.get(LabEntries_.labEntriesGroupid),
			    labJoin.get(LabGroups_.labGroupsDesc)));
		cq.where(builder.and(builder.not(root.get(LabEntries_.labEntriesTestStatus).in(7)),
				builder.not(labJoin.get(LabGroups_.labGroupsId).in(36,5)),
				enclabJoin.get(Encounter_.encounterId).in(encId)));
		cq.orderBy(builder.desc(root.get(LabEntries_.labEntriesOrdOn)));
		List<Object> resultSet = em.createQuery(cq).getResultList();
		List<OrderedBean> orderedBean = new ArrayList<OrderedBean>();
       	  for(int i=0;i<resultSet.size();i++){
       		  OrderedBean orderedLab = (OrderedBean)resultSet.get(i);
       		  orderedBean.add(orderedLab);
       	  }
       	CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object> criteriaQuery=cb.createQuery();
		Root<LabEntries> CriteriaRoot = criteriaQuery.from(LabEntries.class);
		Join<LabEntries,EmployeeProfile> labEmpJoin = CriteriaRoot.join(LabEntries_.empProfile,JoinType.LEFT);
		Join<LabEntries,Encounter> labEncJoin = CriteriaRoot.join(LabEntries_.encounter,JoinType.LEFT);
		Join<LabEntries,LabGroups> LabGroupJoin = CriteriaRoot.join(LabEntries_.labGroups,JoinType.INNER);
		criteriaQuery.select(cb.construct(OrderedBean.class,cb.function("to_char",String.class,
				CriteriaRoot.get(LabEntries_.labEntriesOrdOn),cb.literal("mm/dd/yyyy")),
				CriteriaRoot.get(LabEntries_.labEntriesTestDesc),
				CriteriaRoot.get(LabEntries_.labEntriesTestStatus),
				cb.coalesce(labEmpJoin.get(EmployeeProfile_.empProfileFullname),""),
				CriteriaRoot.get(LabEntries_.labEntriesResultNotes),
				CriteriaRoot.get(LabEntries_.labEntriesLoinc),
				CriteriaRoot.get(LabEntries_.labEntriesCpt),
				CriteriaRoot.get(LabEntries_.labEntriesTestdetailId),
				CriteriaRoot.get(LabEntries_.labEntriesTestId),
				CriteriaRoot.get(LabEntries_.labEntriesGroupid),
				LabGroupJoin.get(LabGroups_.labGroupsDesc)));
		criteriaQuery.where(cb.and(cb.not(CriteriaRoot.get(LabEntries_.labEntriesTestStatus).in(7)),
				CriteriaRoot.get(LabEntries_.labEntriesGroupid).in(36,5)),
				labEncJoin.get(Encounter_.encounterId).in(encId));
		criteriaQuery.orderBy(cb.desc(CriteriaRoot.get(LabEntries_.labEntriesOrdOn)));
		List<Object> vacResult = em.createQuery(criteriaQuery).getResultList();
		for(int i=0;i<vacResult.size();i++){
			  OrderedBean orderedVac = (OrderedBean)vacResult.get(i);
			  orderedBean.add(orderedVac);
      	  }
		return orderedBean ;
	}
	 
	
	/**
	 * to get activity information
	 * @return JSONObject
	 */
	@Override
	public JSONObject getActivities(Integer patientId,Integer encounterId) {
        List<ActivityLog> activityLogList = new ArrayList<ActivityLog>();
        activityLogList=activityLogRepository.findAll(RoomStatusSpecification.getActivities(patientId,encounterId));
		JSONObject finalJson = new JSONObject();
	  	JSONArray activities = new JSONArray();	    
         try{
        	  for(int i=0;i<activityLogList.size();i++){
        		  JSONObject jsono = new JSONObject();
        		  jsono.put("moduleid",activityLogList.get(i).getModuleid());
        		  jsono.put("entityid",activityLogList.get(i).getEntityid());
        		  jsono.put("time",activityLogList.get(i).getTime());
        		  jsono.put("userid",activityLogList.get(i).getUserid());
        		  activities.put(i,jsono);
        	  }
        	JSONArray entityData = new JSONArray();
        	JSONArray userData = new JSONArray();
        	finalJson.put("patientId", patientId);
        	finalJson.put("activityJson",activities);
			finalJson.put("entityData",entityData);
			finalJson.put("userData",userData);
			List<Integer> entities = new ArrayList<Integer>();
			List<Integer> userids = new ArrayList<Integer>();
			for(int i=0;i<activities.length();i++){
				JSONObject actJSON = (JSONObject) activities.get(i);
				String module = actJSON.get("moduleid").toString();
				Integer entityId = Integer.parseInt(actJSON.get("entityid").toString());
				Integer userId = Integer.parseInt(actJSON.get("userid").toString());
				if(!userids.contains(userId)){
					userids.add(userId);
					EmployeeProfile empProfile=new EmployeeProfile();
					empProfile=employeeProfileRepository.findOne(RoomStatusSpecification.getUserName(userId));
					if(!empProfile.equals(null)){
					String userName=empProfile.getEmpProfileFullname();
                	JSONObject user=new JSONObject();
                	user.put("userid",userId);
                	user.put("userName",userName);
                	userData.put(user);
                	}
				}
				if(!entities.contains(entityId)){
					entities.add(entityId);
					/**
					 * To get drug name for prescription
					 **/
					if(Integer.parseInt(module)==3){
						Prescription prescription=new Prescription();
						prescription=prescriptionRepository.findOne(RoomStatusSpecification.getRxName(entityId));
						String rxname="";
						if(prescription!=null){
							rxname=com.google.common.base.Optional.fromNullable(prescription.getRxname()).or(" ");
						}
						CurrentMedication currentMedication=new CurrentMedication();
						currentMedication=currentMedicationRepository.findOne(RoomStatusSpecification.getCurMedRxName(entityId));
						String curMedRxName="";
						if(currentMedication!=null){
							curMedRxName = com.google.common.base.Optional.fromNullable(currentMedication.getCurrentMedicationRxName()).or("");
						}
						List<String> medName = new ArrayList<String>();
						medName.add(rxname);
						medName.add(curMedRxName);
						if(entityId!=-1){
							String entityValue = medName.toString();
							JSONObject entity=new JSONObject();
							entity.put("entityID",entityId);
							entity.put("entityValue",entityValue);
							entityData.put(entity);
						}
					}
          
					/**
					 *To get test name for investigation
					 **/
					if(Integer.parseInt(module)==2){ 
						LabEntries labEntries=new LabEntries();
						labEntries=labEntriesRepository.findOne(RoomStatusSpecification.getLabEntriesTestDesc(entityId));
						String labEntriesTestDesc="";
						if(labEntries!=null){
							labEntriesTestDesc = com.google.common.base.Optional.fromNullable(labEntries.getLabEntriesTestDesc()).or("");
						}
						if(entityId!=-1){
							String entityValue = labEntriesTestDesc;
							JSONObject entity=new JSONObject();
							entity.put("entityID",entityId);
							entity.put("entityValue",entityValue);
							entityData.put(entity);
						}
					}
					/**
					 * To get the template Name
					 **/
					if(Integer.parseInt(module)==4){ 
						LeafPatient leafPatient=new LeafPatient();
						leafPatient=leafPatientRepository.findOne(RoomStatusSpecification.getLeafPatLibId(entityId));
						Integer leafPatId = null;
						if(leafPatient!=null){
							leafPatId = com.google.common.base.Optional.fromNullable(leafPatient.getLeafPatientLeafLibraryId()).or(-1);
						}
						LeafLibrary leafLibrary=new LeafLibrary();
						leafLibrary=leafLibraryRepository.findOne(RoomStatusSpecification.getLeafLibName(leafPatId));
						String leafLibraryName="";
						if(leafLibrary!=null){
							leafLibraryName =com.google.common.base.Optional.fromNullable(leafLibrary.getLeafLibraryName()).or("");
						}
						if(entityId!=-1){
							String entityValue=leafLibraryName;
							JSONObject entity = new JSONObject();
							entity.put("entityID",entityId);
							entity.put("entityValue",entityValue);
							entityData.put(entity);
						}
					}
				}  
			}
          } catch (JSONException e) {
  				e.printStackTrace();
          }
         return finalJson;
	}
}
	
	

