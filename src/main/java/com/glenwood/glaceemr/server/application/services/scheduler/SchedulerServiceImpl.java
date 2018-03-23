package com.glenwood.glaceemr.server.application.services.scheduler;

import java.sql.Timestamp;
import java.text.ParseException;
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
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.AlertCategory;
import com.glenwood.glaceemr.server.application.models.AlertCategory_;
import com.glenwood.glaceemr.server.application.models.AppReferenceValues;
import com.glenwood.glaceemr.server.application.models.AppReferenceValues_;
import com.glenwood.glaceemr.server.application.models.Appointment;
import com.glenwood.glaceemr.server.application.models.InitialSettings;
import com.glenwood.glaceemr.server.application.models.InitialSettings_;
import com.glenwood.glaceemr.server.application.models.PatientEligibility;
import com.glenwood.glaceemr.server.application.models.PatientEligibility_;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.PrimarykeyGenerator;
import com.glenwood.glaceemr.server.application.models.ReferringDoctor;
import com.glenwood.glaceemr.server.application.models.ReferringDoctor_;
import com.glenwood.glaceemr.server.application.models.ReminderCallStatus;
import com.glenwood.glaceemr.server.application.models.ReminderCallStatus_;
import com.glenwood.glaceemr.server.application.models.ReminderDetail;
import com.glenwood.glaceemr.server.application.models.ReminderDetail_;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointment;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointmentBean;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointmentParameter;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointmentParameter_;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointment_;
import com.glenwood.glaceemr.server.application.models.SchedulerLock;
import com.glenwood.glaceemr.server.application.models.SchedulerLock_;
import com.glenwood.glaceemr.server.application.models.SchedulerResource;
import com.glenwood.glaceemr.server.application.models.SchedulerResourceCategory;
import com.glenwood.glaceemr.server.application.models.SchedulerResource_;
import com.glenwood.glaceemr.server.application.models.SchedulerTemplate;
import com.glenwood.glaceemr.server.application.models.SchedulerTemplateDetail;
import com.glenwood.glaceemr.server.application.models.SchedulerTemplateDetail_;
import com.glenwood.glaceemr.server.application.models.SchedulerTemplateTimeMapping;
import com.glenwood.glaceemr.server.application.models.SchedulerTemplateTimeMapping_;
import com.glenwood.glaceemr.server.application.models.SchedulerUserDefault;
import com.glenwood.glaceemr.server.application.models.SchedulerUserDefault_;
import com.glenwood.glaceemr.server.application.models.SchedulerUserSettings;
import com.glenwood.glaceemr.server.application.models.SchedulerUserSettings_;
import com.glenwood.glaceemr.server.application.models.Workflow;
import com.glenwood.glaceemr.server.application.models.Workflow_;
import com.glenwood.glaceemr.server.application.repositories.InitialSettingsRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerAppointmentParameterRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerAppointmentRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerResourceCategoryRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerResourcesRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerTemplateRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerUserDefaultRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerUserSettingsRepository;
import com.glenwood.glaceemr.server.application.specifications.PortalAppointmentsSpecification;
import com.glenwood.glaceemr.server.application.specifications.SchedulerSpecification;
import com.glenwood.glaceemr.server.utils.DateUtil;
/**
 * 
 * @author Manikandan
 *
 */
@Service
@Transactional
public class SchedulerServiceImpl implements SchedulerService{

	@Autowired
	SchedulerAppointmentRepository schedulerAppointmentRepository;

	@Autowired
	SchedulerResourcesRepository schedulerResourcesRepository;

	@Autowired
	SchedulerResourceCategoryRepository schedulerResourceCategoryRepository;

	@Autowired
	SchedulerUserDefaultRepository schedulerUserDefaultRepository;

	@Autowired
	SchedulerTemplateRepository schedulerTemplateRepository;

	@Autowired
	SchedulerAppointmentParameterRepository schedulerAppointmentParameterRepository;

	@Autowired
	SchedulerUserSettingsRepository schedulerUserSettingsRepository;
	
	@Autowired
	InitialSettingsRepository initialSettingsRepository;

	@PersistenceContext
	EntityManager em;

	@Override
	public List<SchedulerResource> getResources() {
		List<Integer> resourceList = new ArrayList<Integer>();
		List<SchedulerResourceCategory> resources = schedulerResourceCategoryRepository.findAll(SchedulerSpecification.getResourcesByCategory());

		for (SchedulerResourceCategory schedulerResourceCategory : resources) {
			resourceList.add(schedulerResourceCategory.getSchResourceCategoryResourceId());
		}

		List<SchedulerResource> schedulerResources=schedulerResourcesRepository.findAll(SchedulerSpecification.getResources(resourceList));
		return schedulerResources;
	}

	@Override
	public List<SchedulerResourceCategory> getResourceCategories() {
		List<SchedulerResourceCategory> schedulerResourceCategories=schedulerResourceCategoryRepository.findAll();
		return schedulerResourceCategories;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SchedulerAppointmentBean> getAppointments(Date apptDate, Integer[] resourceId) {

		List<Integer> resourceList = Arrays.asList(resourceId);
		List<Long> list = new ArrayList<Long>();
		list.add((long)6);
		list.add((long)12);
		list.add((long)45);
		list.add((long)33);
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();


		Root<SchedulerAppointment> root = cq.from(SchedulerAppointment.class);
		Join<SchedulerAppointment, PatientRegistration> joinPatientRegistration=root.join("patRegPatientId",JoinType.LEFT);
		Join<PatientRegistration,PatientInsDetail> patientInsJoin=joinPatientRegistration.join("patientInsuranceTable",JoinType.LEFT);
		Join<PatientRegistration,PatientEligibility> patientEligibleJoin=joinPatientRegistration.join("patientEligibility",JoinType.LEFT);
		Join<PatientRegistration,ReminderDetail> reminderDetailJoin=joinPatientRegistration.join("reminderDetails",JoinType.LEFT);
		Join<ReminderDetail,ReminderCallStatus> reminderCallStatusJoin=reminderDetailJoin.join("reminderCallStatus",JoinType.LEFT);
		Join<SchedulerAppointment, AppReferenceValues> joinH113Status=root.join("App_Reference_ValuesApptStatus",JoinType.LEFT);
		Join<SchedulerAppointment, AppReferenceValues> joinH113Type=root.join("App_Reference_ValuesApptType",JoinType.LEFT);
		//Join<SchedulerAppointment, SchedulerAppointmentParameter> joinSchApptParam=root.join("schApptParam",JoinType.LEFT);
		Join<SchedulerAppointment, SchedulerResource> joinSchResLoc=root.join("schResLoc",JoinType.LEFT);
		Join<SchedulerAppointment, SchedulerResource> joinResProvider=root.join("schResProvider",JoinType.LEFT);
		Join<SchedulerAppointment, ReferringDoctor> joinSchRefDrId=root.join("schRefDrId",JoinType.LEFT);
		Join<SchedulerAppointment, Workflow> joinWorkflow=root.join("workflowPatientId",JoinType.LEFT);
		Join<Join<SchedulerAppointment, Workflow>, AlertCategory> joinWorkflowStatusName=joinWorkflow.join("alertCategoryName",JoinType.LEFT);

		//patientInsJoin.equals(obj)
		Predicate[] patientInsCondition=new Predicate[]{
				builder.equal(patientInsJoin.get(PatientInsDetail_.patientInsDetailIsactive), new Boolean(true)),
				builder.equal(patientInsJoin.get(PatientInsDetail_.patientInsDetailInstype), new Integer(1)),
		};
		patientInsJoin.on(patientInsCondition);

		Predicate[] patientEligibleCondition=new Predicate[]{
				builder.equal(patientEligibleJoin.get(PatientEligibility_.patientEligibilityActive), new Boolean(true)),
				builder.equal(patientEligibleJoin.get(PatientEligibility_.insuranceType), new String("1")),
		};
		patientEligibleJoin.on(patientEligibleCondition);


		Predicate[] reminderDetailCondition=new Predicate[]{
				builder.greaterThan(reminderDetailJoin.get(ReminderDetail_.reminderApptId),0),
				builder.equal(reminderDetailJoin.get(ReminderDetail_.reminderDetailIsMultiple), new Boolean(false)),
		};
		reminderDetailJoin.on(reminderDetailCondition);


		Predicate[] predicateStatus = new Predicate[] {
				builder.equal(joinH113Status.get(AppReferenceValues_.App_Reference_Values_tableId), new Integer(1)),
				builder.equal(joinH113Status.get(AppReferenceValues_.App_Reference_Values_isActive), new Boolean(true)),
		};
		joinH113Status.on(predicateStatus);
		Predicate[] predicateType = new Predicate[] {
				builder.equal(joinH113Type.get(AppReferenceValues_.App_Reference_Values_tableId), new Integer(2)),
				builder.equal(joinH113Type.get(AppReferenceValues_.App_Reference_Values_isActive), new Boolean(true)),
		};
		joinH113Type.on(predicateType);
		/*Predicate[] predicateParam = new Predicate[] {
				builder.equal(joinSchApptParam.get(SchedulerAppointmentParameter_.schApptParameterIsactive), new Boolean(true)),
		};
		joinSchApptParam.on(predicateParam);
			Predicate[] predicateReason = new Predicate[] {
					builder.equal(joinH113Reason.get(AppReferenceValues_.App_Reference_Values_tableId), new Integer(402)),
					builder.equal(joinH113Reason.get(AppReferenceValues_.h113008), new Integer(1)),
					builder.equal(joinH113Reason.get(AppReferenceValues_.App_Reference_Values_isActive), new Boolean(true)),
			};
			joinH113Reason.on(predicateReason);*/
		Predicate[] predicateLocation = new Predicate[] {
				builder.equal(joinSchResLoc.get(SchedulerResource_.schResourceIsdoctor), new Integer(0)),
				builder.equal(joinSchResLoc.get(SchedulerResource_.schResourceLocalserver), new Boolean(true)),
		};
		joinSchResLoc.on(predicateLocation);
		Predicate[] predicateWorkflow = new Predicate[] {
				builder.equal(joinWorkflow.get(Workflow_.workflowIsactive), new Boolean(true)),
		};
		joinWorkflow.on(predicateWorkflow);

		//cq.distinct(true);
		cq.select(builder.construct(SchedulerAppointmentBean.class,
				root.get(SchedulerAppointment_.schApptId),
				builder.function("to_char", String.class, root.get(SchedulerAppointment_.schApptStarttime),builder.literal("HH12:MI:SS AM")),
				builder.function("to_char", String.class, root.get(SchedulerAppointment_.schApptEndtime),builder.literal("HH12:MI:SS AM")),
				root.get(SchedulerAppointment_.schApptDate),
				root.get(SchedulerAppointment_.schApptPatientId),
				joinPatientRegistration.get(PatientRegistration_.patientRegistrationAccountno),
				root.get(SchedulerAppointment_.schApptPatientname),
				root.get(SchedulerAppointment_.schApptHomephone),
				builder.coalesce(root.get(SchedulerAppointment_.schApptHomeextn),builder.literal("-")),
				root.get(SchedulerAppointment_.schApptWorkphone),
				builder.coalesce(root.get(SchedulerAppointment_.schApptWorkextn),builder.literal("-")),
				root.get(SchedulerAppointment_.schApptResource),
				joinH113Status.get(AppReferenceValues_.App_Reference_Values_statusName),
				root.get(SchedulerAppointment_.schApptStatus),
				joinH113Status.get(AppReferenceValues_.App_Reference_Values_option_Color),
				joinH113Type.get(AppReferenceValues_.App_Reference_Values_statusName),
				root.get(SchedulerAppointment_.schApptType),
				joinSchResLoc.get(SchedulerResource_.schResourceName),
				root.get(SchedulerAppointment_.schApptLocation),
				builder.function("getapptpara", String.class,builder.literal(1),root.get(SchedulerAppointment_.schApptId)),
				root.get(SchedulerAppointment_.schApptReason),
				joinSchRefDrId.get(ReferringDoctor_.referring_doctor_referringdoctor),
				root.get(SchedulerAppointment_.schApptReferringdoctorId),
				joinSchRefDrId.get(ReferringDoctor_.referring_doctor_phoneno),
				joinSchRefDrId.get(ReferringDoctor_.referring_doctor_fax_number),
				joinWorkflow.get(Workflow_.workflowStatus),
				joinWorkflowStatusName.get(AlertCategory_.alertCategoryDisplayName),
				joinWorkflow.get(Workflow_.workflowStarttime),
				joinResProvider.get(SchedulerResource_.schResourceConnectionstring),
				patientInsJoin.get(PatientInsDetail_.patientInsDetailCopay),
				builder.coalesce(patientEligibleJoin.get(PatientEligibility_.eligibleStatus),"Not Verified"),
				reminderCallStatusJoin.get(ReminderCallStatus_.reminderCallStatusName),
				reminderDetailJoin.get(ReminderDetail_.reminderDetailReminderStatus)
				)).where(builder.and(builder.equal(root.get(SchedulerAppointment_.schApptDate),apptDate),
						root.get(SchedulerAppointment_.schApptResource).in(resourceList),
						builder.not(root.get(SchedulerAppointment_.App_Reference_ValuesApptStatus).in(list))
						)).orderBy(builder.asc(root.get(SchedulerAppointment_.schApptStarttime)));


		Query query=em.createQuery(cq);

		List<SchedulerAppointmentBean> schedulerAppointments=query.getResultList();

		return schedulerAppointments;
	}

	@Override
	public String getDefaultResource(String userId) {

		List<SchedulerUserDefault> userList =schedulerUserDefaultRepository.findAll(SchedulerSpecification.getDefaultUserList(userId)); 
		String defaultResource="";

		for (SchedulerUserDefault schedulerUserDefault : userList) {
			defaultResource+=schedulerUserDefault.getSchUserDefaultResourceId()+",";
		}

		return defaultResource;
	}

	/**
	 * Frame template and lock details
	 */
	@Override
	public Object getTemplates(int userId, Date date) {

		Map<String, List<Object>> tlMap=new HashMap<String, List<Object>>();	// Template and lock objects


		tlMap=new HashMap<String, List<Object>>();
		/**
		 * To get template details
		 */
		List<Object> templateList= getTemplateData(userId, date);

		/**
		 * To get lock data
		 * it will fetch latest lock id for the particular time slot
		 */
		List<Object> lockResultList=getLockData(userId, date);

		/**
		 * Extracting ids alone to get sch_lock details
		 */
		List<Integer> maxLockIds=new ArrayList<Integer>();
		for (Object obj: lockResultList) {
			try {
				JSONArray ja=new JSONArray(obj);
				maxLockIds.add(ja.getInt(0));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		List<Object> locksList=getLocksById(maxLockIds);
		tlMap.put("lockdetails", locksList==null?new ArrayList<Object>():locksList);
		tlMap.put("templatedetails", templateList);
		return	tlMap;
	}

	/**
	 * Get template details
	 * @param userId
	 * @param date
	 * @return
	 */
	public List<Object> getTemplateData(int userId, Date date) {

		SchedulerTemplate schedulerTemplate=schedulerTemplateRepository.findOne(PortalAppointmentsSpecification.getSchTemplateId(userId, date));
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object> cq=cb.createQuery();
		Root<SchedulerTemplateTimeMapping> root=cq.from(SchedulerTemplateTimeMapping.class);
		Join<SchedulerTemplateTimeMapping, SchedulerTemplateDetail>	joinSttmStd=root.join("schedulerTemplateDetail",JoinType.INNER);
		Predicate predicateStdSttm = cb.equal(root.get(SchedulerTemplateTimeMapping_.schTemplateTimeMappingTemplateId), cb.literal(schedulerTemplate.getSchTemplateId()));
		cq.multiselect(cb.construct(SchedulerTemplateDetailBean.class,
				joinSttmStd.get(SchedulerTemplateDetail_.schTemplateDetailId),
				joinSttmStd.get(SchedulerTemplateDetail_.schTemplateDetailStarttime),
				joinSttmStd.get(SchedulerTemplateDetail_.schTemplateDetailEndtime),
				joinSttmStd.get(SchedulerTemplateDetail_.schTemplateDetailInterval),
				joinSttmStd.get(SchedulerTemplateDetail_.schTemplateDetailApptType),
				joinSttmStd.get(SchedulerTemplateDetail_.schTemplateDetailSecownerId),
				joinSttmStd.get(SchedulerTemplateDetail_.schTemplateDetailIslocked),
				joinSttmStd.get(SchedulerTemplateDetail_.schTemplateDetailNotes),
				joinSttmStd.get(SchedulerTemplateDetail_.schTemplateDetailSameDaySlot)));
		cq.where(predicateStdSttm);

		return em.createQuery(cq).getResultList();
	}

	/**
	 * For single slot there may be more than one entry, this piece of code will take the latest entry for the particular slot. 
	 * @param userId
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unused")
	public List<Object> getLockData(int userId, Date date) {

		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object> cq=cb.createQuery();
		Root<SchedulerLock> root=cq.from(SchedulerLock.class);
		Predicate predicateResource=cb.equal(root.get(SchedulerLock_.schLockResourceId), cb.literal(userId));
		Predicate predicateLockStatus=cb.equal(root.get(SchedulerLock_.schLockStatusId), cb.literal(4));		//Status 4=Lock, 0=Free
		Expression<Date> exprLockedDate=cb.function("date", Date.class, root.get(SchedulerLock_.schLockDate));
		Predicate predicateLockedDate=cb.equal(exprLockedDate, date);
		cq.select(cb.tuple(cb.max(root.get(SchedulerLock_.schLockId)),root.get(SchedulerLock_.schLockStarttime),root.get(SchedulerLock_.schLockEndtime)));
		cq.where(predicateResource,predicateLockedDate).groupBy(root.get(SchedulerLock_.schLockStarttime),root.get(SchedulerLock_.schLockEndtime));
		return em.createQuery(cq).getResultList();
	}

	/**
	 * Get locks details based on id
	 * @param lockIdList
	 * @return
	 */
	public List<Object> getLocksById(List<Integer> lockIdList) {

		if(lockIdList.size()<1)	
			return null;

		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object> cq=cb.createQuery();
		Root<SchedulerLock> root=cq.from(SchedulerLock.class);
		cq.where(root.get(SchedulerLock_.schLockId).in(lockIdList));
		cq.select(root);

		List<Object> locksList=em.createQuery(cq).getResultList();
		return (locksList.size()>0) ? locksList:null;
	}

	public List<String> getInitialReportData() {

		List<String> reportData=new ArrayList<String>();
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object> cq=cb.createQuery();
		Root<InitialSettings> root=cq.from(InitialSettings.class);
		Predicate typePred=cb.equal(root.get(InitialSettings_.initialSettingsOptionType),-104);
		Predicate isActive=cb.equal(root.get(InitialSettings_.initialSettingsVisible), true);	
		cq.select(root.get(InitialSettings_.initialSettingsOptionName));
        cq.where(typePred,isActive);
		List<Object> locksList=em.createQuery(cq).getResultList();
		for(Object data:locksList){
			String reportName=(String)data;
			reportData.add(reportName);
		}
		return reportData;
	}
	
	@Override
	public Appointment createAppointment(Appointment appointment) {
	
		int schApptId=getNewSchApptId();
		int schApptStatusGrpId = getApptStatusGrpId(appointment.getSchApptStatus());
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date parsed = null;
		try {
			parsed = format.parse(appointment.getSchApptDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		java.sql.Date sql = new java.sql.Date(parsed.getTime());

		SchedulerAppointment schApptBean=new SchedulerAppointment();
		schApptBean.setSchApptId(schApptId);
		schApptBean.setSchApptDate(sql);
		schApptBean.setSchApptStarttime(Timestamp.valueOf(appointment.getSchApptStarttime()));
		schApptBean.setSchApptEndtime(Timestamp.valueOf(appointment.getSchApptEndtime()));
		schApptBean.setSchApptInterval(appointment.getSchApptInterval());
		schApptBean.setSchApptPatientId(appointment.getSchApptPatientId());
		schApptBean.setSchApptPatientname(appointment.getSchApptPatientname());
		schApptBean.setSchApptLocation(appointment.getSchApptLocation());
		schApptBean.setSchApptResource(appointment.getSchApptResource());
		schApptBean.setSchApptStatus(appointment.getSchApptStatus());
		schApptBean.setSchApptReason(-2);
		schApptBean.setSchApptType(appointment.getSchApptType());
		schApptBean.setSchApptNextconsId(-1);	//for new appointment always -1.
		schApptBean.setSchApptComments(appointment.getSchApptComments());
		schApptBean.setSchApptLastmodifiedTime(Timestamp.valueOf(appointment.getSchApptLastmodifiedTime()));
		schApptBean.setSchApptLastmodifiedUserId(appointment.getSchApptLastmodifiedUserId());
		schApptBean.setSchApptLastmodifiedUsername(appointment.getSchApptLastmodifiedUsername());
		schApptBean.setSchApptRoomId(appointment.getSchApptRoomId());
		schApptBean.setH555555(appointment.getH555555());
		schApptBean.setSchApptRescheduledfromDate(Timestamp.valueOf("1900-01-01 00:00:00"));
		schApptBean.setSchApptRescheduledfromTime(Timestamp.valueOf("1900-01-01 00:00:00"));
		schApptBean.setSchApptStatusgrpId(schApptStatusGrpId);

		SchedulerAppointment schAppt=schedulerAppointmentRepository.saveAndFlush(schApptBean);

		SchedulerAppointmentParameter schApptParam=new SchedulerAppointmentParameter();
		schApptParam.setSchApptParameterApptId(schAppt.getSchApptId());
		schApptParam.setSchApptParameterIsactive(true);
		schApptParam.setSchApptParameterOn(new Timestamp(schAppt.getSchApptDate().getTime()));
		schApptParam.setSchApptParameterType(1);
		schApptParam.setSchApptParameterValueId(appointment.getSchApptReason());
		schApptParam.setSchApptParameterId(getNewSchApptParameterId());
		schedulerAppointmentParameterRepository.saveAndFlush(schApptParam);

		executeTesttablePrimaryKeyGenerator();

		return appointment;
	}

	private int getApptStatusGrpId(Integer schApptStatus) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<AppReferenceValues> root = cq.from(AppReferenceValues.class);
		cq.select(root.get(AppReferenceValues_.App_Reference_Values_reason_type)).where(builder.equal(root.get(AppReferenceValues_.App_Reference_Values_tableId), builder.literal(1)),
				builder.equal(root.get(AppReferenceValues_.App_Reference_Values_statusId), builder.literal(schApptStatus)));
		List<Object> obj = em.createQuery(cq).getResultList();
		return Integer.parseInt(obj.get(0).toString());
	}

	@SuppressWarnings("unused")
	public void executeTesttablePrimaryKeyGenerator(){
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<PrimarykeyGenerator> root = cq.from(PrimarykeyGenerator.class);
		cq.select(builder.function("testtableprimarykey_generator", String.class));
		em.createQuery(cq).getResultList();
	}

	public Integer getNewSchApptParameterId() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<SchedulerAppointmentParameter> root = cq.from(SchedulerAppointmentParameter.class);
		cq.select(builder.max(root.get(SchedulerAppointmentParameter_.schApptParameterId)));
		Integer apptParamID=(Integer) em.createQuery(cq).getSingleResult();
		return apptParamID+1;
	}

	public Integer getNewSchApptId() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<SchedulerAppointment> root = cq.from(SchedulerAppointment.class);
		cq.select(builder.max(root.get(SchedulerAppointment_.schApptId)));
		Integer apptID=(Integer) em.createQuery(cq).getSingleResult();
		return apptID+1;
	}

	@Override
	public List<Object> getApptBookLocationList() {
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object> cq=cb.createQuery();
		Root<SchedulerResource> root=cq.from(SchedulerResource.class);
		cq.select(root);
		Predicate locationList=cb.equal(root.get(SchedulerResource_.schResourceIsdoctor), 0);
		Predicate isActive=cb.equal(root.get(SchedulerResource_.schResourceLocalserver), true);
		cq.where(locationList,isActive);
		List<Object> schResource = em.createQuery(cq).getResultList();
		return schResource;
	}

	@Override
	public List<Object> getApptTypes() {
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object> cq=cb.createQuery();
		Root<AppReferenceValues> root=cq.from(AppReferenceValues.class);
		cq.select(root);
		Predicate typePred=cb.equal(root.get(AppReferenceValues_.App_Reference_Values_tableId), 2);
		Predicate isActive=cb.equal(root.get(AppReferenceValues_.App_Reference_Values_isActive), true);	
		cq.where(typePred,isActive);
		List<Object> apptTypes = em.createQuery(cq).getResultList();
		return apptTypes;
	}


	@Override
	public List<Object> getApptStatus() {
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object> cq=cb.createQuery();
		Root<AppReferenceValues> root=cq.from(AppReferenceValues.class);
		cq.select(cb.construct(AppReferenceValues.class, root.get(AppReferenceValues_.App_Reference_Values_statusName),
				root.get(AppReferenceValues_.App_Reference_Values_statusId)));
		Predicate typePred=cb.equal(root.get(AppReferenceValues_.App_Reference_Values_tableId), 1);
		cq.where(typePred);
		List<Object> apptTypes = em.createQuery(cq).getResultList();
		return apptTypes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<List<SchedulerAppointmentBean>> getWeekAppointments(Date apptDate,
			Integer[] resourceId, String viewType) {

		List<Integer> resourceList = Arrays.asList(resourceId);
		List<SchedulerAppointmentBean> schedulerAppointments = null;
		List<List<SchedulerAppointmentBean>> schedulerWeekList=new ArrayList<List<SchedulerAppointmentBean>>();
		List<Long> list = new ArrayList<Long>();
		list.add((long)6);
		list.add((long)12);
		list.add((long)45);
		list.add((long)33);
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();

		Root<SchedulerAppointment> root = cq.from(SchedulerAppointment.class);
		Join<SchedulerAppointment, PatientRegistration> joinPatientRegistration=root.join("patRegPatientId",JoinType.LEFT);
		Join<PatientRegistration,PatientInsDetail> patientInsJoin=joinPatientRegistration.join("patientInsuranceTable",JoinType.LEFT);
		Join<PatientRegistration,PatientEligibility> patientEligibleJoin=joinPatientRegistration.join("patientEligibility",JoinType.LEFT);
		Join<PatientRegistration,ReminderDetail> reminderDetailJoin=joinPatientRegistration.join("reminderDetails",JoinType.LEFT);
		Join<ReminderDetail,ReminderCallStatus> reminderCallStatusJoin=reminderDetailJoin.join("reminderCallStatus",JoinType.LEFT);
		Join<SchedulerAppointment, AppReferenceValues> joinH113Status=root.join("App_Reference_ValuesApptStatus",JoinType.LEFT);
		Join<SchedulerAppointment, AppReferenceValues> joinH113Type=root.join("App_Reference_ValuesApptType",JoinType.LEFT);
		//Join<SchedulerAppointment, SchedulerAppointmentParameter> joinSchApptParam=root.join("schApptParam",JoinType.LEFT);
		Join<SchedulerAppointment, SchedulerResource> joinSchResLoc=root.join("schResLoc",JoinType.LEFT);
		Join<SchedulerAppointment, SchedulerResource> joinResProvider=root.join("schResProvider",JoinType.LEFT);
		Join<SchedulerAppointment, ReferringDoctor> joinSchRefDrId=root.join("schRefDrId",JoinType.LEFT);
		Join<SchedulerAppointment, Workflow> joinWorkflow=root.join("workflowPatientId",JoinType.LEFT);
		Join<Join<SchedulerAppointment, Workflow>, AlertCategory> joinWorkflowStatusName=joinWorkflow.join("alertCategoryName",JoinType.LEFT);


		Predicate[] patientInsCondition=new Predicate[]{
				builder.equal(patientInsJoin.get(PatientInsDetail_.patientInsDetailIsactive), new Boolean(true)),
				builder.equal(patientInsJoin.get(PatientInsDetail_.patientInsDetailInstype), new Integer(1)),
		};
		patientInsJoin.on(patientInsCondition);

		Predicate[] patientEligibleCondition=new Predicate[]{
				builder.equal(patientEligibleJoin.get(PatientEligibility_.patientEligibilityActive), new Boolean(true)),
				builder.equal(patientEligibleJoin.get(PatientEligibility_.insuranceType), new String("1")),
		};
		patientEligibleJoin.on(patientEligibleCondition);


		Predicate[] reminderDetailCondition=new Predicate[]{
				builder.greaterThan(reminderDetailJoin.get(ReminderDetail_.reminderApptId),0),
				builder.equal(reminderDetailJoin.get(ReminderDetail_.reminderDetailIsMultiple), new Boolean(false)),
		};
		reminderDetailJoin.on(reminderDetailCondition);


		Predicate[] predicateStatus = new Predicate[] {
				builder.equal(joinH113Status.get(AppReferenceValues_.App_Reference_Values_tableId), new Integer(1)),
				builder.equal(joinH113Status.get(AppReferenceValues_.App_Reference_Values_isActive), new Boolean(true)),
		};
		joinH113Status.on(predicateStatus);
		Predicate[] predicateType = new Predicate[] {
				builder.equal(joinH113Type.get(AppReferenceValues_.App_Reference_Values_tableId), new Integer(2)),
				builder.equal(joinH113Type.get(AppReferenceValues_.App_Reference_Values_isActive), new Boolean(true)),
		};
		joinH113Type.on(predicateType);
		/*Predicate[] predicateParam = new Predicate[] {
				builder.equal(joinSchApptParam.get(SchedulerAppointmentParameter_.schApptParameterIsactive), new Boolean(true)),
		};
		joinSchApptParam.on(predicateParam);*/
		Predicate[] predicateLocation = new Predicate[] {
				builder.equal(joinSchResLoc.get(SchedulerResource_.schResourceIsdoctor), new Integer(0)),
				builder.equal(joinSchResLoc.get(SchedulerResource_.schResourceLocalserver), new Boolean(true)),
		};
		joinSchResLoc.on(predicateLocation);
		Predicate[] predicateWorkflow = new Predicate[] {
				builder.equal(joinWorkflow.get(Workflow_.workflowIsactive), new Boolean(true)),
		};
		joinWorkflow.on(predicateWorkflow);

		//cq.distinct(true);

		if(viewType.equals("WEEK")){
			for(int i=0;i<7;i++){
				schedulerAppointments =new  ArrayList<SchedulerAppointmentBean>();
				cq.select(builder.construct(SchedulerAppointmentBean.class,
						root.get(SchedulerAppointment_.schApptId),
						builder.function("to_char", String.class, root.get(SchedulerAppointment_.schApptStarttime),builder.literal("HH12:MI:SS AM")),
						builder.function("to_char", String.class, root.get(SchedulerAppointment_.schApptEndtime),builder.literal("HH12:MI:SS AM")),
						root.get(SchedulerAppointment_.schApptDate),
						root.get(SchedulerAppointment_.schApptPatientId),
						joinPatientRegistration.get(PatientRegistration_.patientRegistrationAccountno),
						root.get(SchedulerAppointment_.schApptPatientname),
						root.get(SchedulerAppointment_.schApptHomephone),
						builder.coalesce(root.get(SchedulerAppointment_.schApptHomeextn),builder.literal("-")),
						root.get(SchedulerAppointment_.schApptWorkphone),
						builder.coalesce(root.get(SchedulerAppointment_.schApptWorkextn),builder.literal("-")),
						root.get(SchedulerAppointment_.schApptResource),
						joinH113Status.get(AppReferenceValues_.App_Reference_Values_statusName),
						root.get(SchedulerAppointment_.schApptStatus),
						joinH113Status.get(AppReferenceValues_.App_Reference_Values_option_Color),
						joinH113Type.get(AppReferenceValues_.App_Reference_Values_statusName),
						root.get(SchedulerAppointment_.schApptType),
						joinSchResLoc.get(SchedulerResource_.schResourceName),
						root.get(SchedulerAppointment_.schApptLocation),
						builder.function("getapptpara", String.class,builder.literal(1),root.get(SchedulerAppointment_.schApptId)),
						root.get(SchedulerAppointment_.schApptReason),
						joinSchRefDrId.get(ReferringDoctor_.referring_doctor_referringdoctor),
						root.get(SchedulerAppointment_.schApptReferringdoctorId),
						joinSchRefDrId.get(ReferringDoctor_.referring_doctor_phoneno),
						joinSchRefDrId.get(ReferringDoctor_.referring_doctor_fax_number),
						joinWorkflow.get(Workflow_.workflowStatus),
						joinWorkflowStatusName.get(AlertCategory_.alertCategoryDisplayName),
						joinWorkflow.get(Workflow_.workflowStarttime),
						joinResProvider.get(SchedulerResource_.schResourceConnectionstring),
						patientInsJoin.get(PatientInsDetail_.patientInsDetailCopay),
						builder.coalesce(patientEligibleJoin.get(PatientEligibility_.eligibleStatus),"Not Verified"),
						reminderCallStatusJoin.get(ReminderCallStatus_.reminderCallStatusName),
						reminderDetailJoin.get(ReminderDetail_.reminderDetailReminderStatus)
						)).where(builder.and(builder.equal(root.get(SchedulerAppointment_.schApptDate),apptDate),
								root.get(SchedulerAppointment_.schApptResource).in(resourceList),
								builder.not(root.get(SchedulerAppointment_.App_Reference_ValuesApptStatus).in(list))
								)).orderBy(builder.asc(root.get(SchedulerAppointment_.schApptStarttime)));
				apptDate = DateUtil.addDays(apptDate, 1);
				Query query=em.createQuery(cq);
				schedulerAppointments=query.getResultList();
				if(!schedulerAppointments.isEmpty())
					schedulerWeekList.add(schedulerAppointments);
			}
		}
		else if(viewType.equals("MONTH")){
			Date getMonthDate=getFirstDateOfMonth(apptDate);
			Calendar calendar = Calendar.getInstance();
			int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			for(int i=0;i<maxDay;i++){
				schedulerAppointments =new  ArrayList<SchedulerAppointmentBean>();
				cq.select(builder.construct(SchedulerAppointmentBean.class,
						root.get(SchedulerAppointment_.schApptId),
						builder.function("to_char", String.class, root.get(SchedulerAppointment_.schApptStarttime),builder.literal("HH12:MI:SS AM")),
						builder.function("to_char", String.class, root.get(SchedulerAppointment_.schApptEndtime),builder.literal("HH12:MI:SS AM")),
						root.get(SchedulerAppointment_.schApptDate),
						root.get(SchedulerAppointment_.schApptPatientId),
						joinPatientRegistration.get(PatientRegistration_.patientRegistrationAccountno),
						root.get(SchedulerAppointment_.schApptPatientname),
						root.get(SchedulerAppointment_.schApptHomephone),
						builder.coalesce(root.get(SchedulerAppointment_.schApptHomeextn),builder.literal("-")),
						root.get(SchedulerAppointment_.schApptWorkphone),
						builder.coalesce(root.get(SchedulerAppointment_.schApptWorkextn),builder.literal("-")),
						root.get(SchedulerAppointment_.schApptResource),
						joinH113Status.get(AppReferenceValues_.App_Reference_Values_statusName),
						root.get(SchedulerAppointment_.schApptStatus),
						joinH113Status.get(AppReferenceValues_.App_Reference_Values_option_Color),
						joinH113Type.get(AppReferenceValues_.App_Reference_Values_statusName),
						root.get(SchedulerAppointment_.schApptType),
						joinSchResLoc.get(SchedulerResource_.schResourceName),
						root.get(SchedulerAppointment_.schApptLocation),
						builder.function("getapptpara", String.class,builder.literal(1),root.get(SchedulerAppointment_.schApptId)),
						root.get(SchedulerAppointment_.schApptReason),
						joinSchRefDrId.get(ReferringDoctor_.referring_doctor_referringdoctor),
						root.get(SchedulerAppointment_.schApptReferringdoctorId),
						joinSchRefDrId.get(ReferringDoctor_.referring_doctor_phoneno),
						joinSchRefDrId.get(ReferringDoctor_.referring_doctor_fax_number),
						joinWorkflow.get(Workflow_.workflowStatus),
						joinWorkflowStatusName.get(AlertCategory_.alertCategoryDisplayName),
						joinWorkflow.get(Workflow_.workflowStarttime),
						joinResProvider.get(SchedulerResource_.schResourceConnectionstring),
						patientInsJoin.get(PatientInsDetail_.patientInsDetailCopay),
						builder.coalesce(patientEligibleJoin.get(PatientEligibility_.eligibleStatus),"Not Verified"),
						reminderCallStatusJoin.get(ReminderCallStatus_.reminderCallStatusName),
						reminderDetailJoin.get(ReminderDetail_.reminderDetailReminderStatus)
						)).where(builder.and(builder.equal(root.get(SchedulerAppointment_.schApptDate),getMonthDate),
								root.get(SchedulerAppointment_.schApptResource).in(resourceList),
								builder.not(root.get(SchedulerAppointment_.App_Reference_ValuesApptStatus).in(list))
								)).orderBy(builder.asc(root.get(SchedulerAppointment_.schApptStarttime)));
				getMonthDate = DateUtil.addDays(getMonthDate, 1);
				Query query=em.createQuery(cq);
				schedulerAppointments=query.getResultList();
				if(!schedulerAppointments.isEmpty())
					schedulerWeekList.add(schedulerAppointments);
			}
		}
		return schedulerWeekList;
	}

	public Date getFirstDateOfMonth(Date apptDate) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(apptDate);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	@Override
	public Object getWeekTemplates(Integer userId, Date date) {

		List<Map<Integer,Object>> tlMapList=new ArrayList<Map<Integer,Object>>();	
		Map<Integer,Object> resultObject = new HashMap<Integer,Object>();
		Map<String, List<Object>> tlMap=new HashMap<String, List<Object>>();	// Template and lock objects

		for(int i=0;i<7;i++){
			tlMap=new HashMap<String, List<Object>>();
			/**
			 * To get template details
			 */
			List<Object> templateList= getTemplateData(userId, date);

			/**
			 * To get lock data
			 * it will fetch latest lock id for the particular time slot
			 */
			List<Object> lockResultList=getLockData(userId, date);

			/**
			 * Extracting ids alone to get sch_lock details
			 */
			List<Integer> maxLockIds=new ArrayList<Integer>();
			for (Object obj: lockResultList) {
				try {
					JSONArray ja=new JSONArray(obj);
					maxLockIds.add(ja.getInt(0));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			List<Object> locksList=getLocksById(maxLockIds);
			tlMap.put("lockdetails", locksList==null?new ArrayList<Object>():locksList);
			tlMap.put("templatedetails", templateList);
			resultObject.put(userId, tlMap);
			tlMapList.add(resultObject);
			date = DateUtil.addDays(date, 1);
		}
		return	tlMapList;
	}

	@Override
	public Appointment pasteappointment(Appointment fromJson,Appointment toJson) {

		toJson.setSchApptId(fromJson.getSchApptId());
		toJson.setSchApptDate(fromJson.getSchApptDate());
		toJson.setSchApptStarttime(fromJson.getSchApptStarttime());
		toJson.setSchApptEndtime(fromJson.getSchApptEndtime());
		toJson.setSchApptInterval(fromJson.getSchApptInterval());
		toJson.setSchApptPatientId(fromJson.getSchApptPatientId());
		toJson.setSchApptPatientname(fromJson.getSchApptPatientname());
		toJson.setSchApptLocation(fromJson.getSchApptLocation());
		toJson.setSchApptResource(fromJson.getSchApptResource());
		toJson.setSchApptStatus(fromJson.getSchApptStatus());
		toJson.setSchApptType(fromJson.getSchApptType());
		toJson.setSchApptReason(-2);
		toJson.setSchApptNextconsId(-1);
		toJson.setSchApptComments("");
		toJson.setSchApptLastmodifiedTime(fromJson.getSchApptLastmodifiedTime());
		toJson.setSchApptLastmodifiedUserId(fromJson.getSchApptLastmodifiedUserId());
		toJson.setSchApptRoomId(1);
		toJson.setSchApptRescheduledfromDate("1900-01-01 00:00:00");
		toJson.setSchApptRescheduledfromTime("1900-01-01 00:00:00");	
		toJson.setOperationStatus("CUT");


		if ( toJson.getOperationStatus().equals("CUT")||toJson.getOperationStatus().equals("COPY")  )//either cut or copy
		{
			createAppointment(toJson);
		}
		if ( toJson.getOperationStatus().equals("CUT"))
		{
			updateAppointment(toJson);
		}
		return toJson;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<SchedulerAppointmentBean> updateAppointment(Appointment appointment) {
		int schApptStatusGrpId = getApptStatusGrpId(appointment.getSchApptStatus());
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date parsed = null;
		try {
			parsed = format.parse(appointment.getSchApptDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		java.sql.Date sql = new java.sql.Date(parsed.getTime());

		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaUpdate<SchedulerAppointment> cu = cb.createCriteriaUpdate(SchedulerAppointment.class);
		Root<SchedulerAppointment> root=cu.from(SchedulerAppointment.class);
		cu.set(root.get(SchedulerAppointment_.schApptDate),sql);
		cu.set(root.get(SchedulerAppointment_.schApptStarttime),Timestamp.valueOf(appointment.getSchApptStarttime()));
		cu.set(root.get(SchedulerAppointment_.schApptEndtime),Timestamp.valueOf(appointment.getSchApptEndtime()));	
		cu.set(root.get(SchedulerAppointment_.schApptInterval),appointment.getSchApptInterval());
		cu.set(root.get(SchedulerAppointment_.schApptPatientId),appointment.getSchApptPatientId());
		cu.set(root.get(SchedulerAppointment_.schApptPatientname),appointment.getSchApptPatientname());
		cu.set(root.get(SchedulerAppointment_.schApptLocation),appointment.getSchApptLocation());
		cu.set(root.get(SchedulerAppointment_.schApptResource),appointment.getSchApptResource());
		cu.set(root.get(SchedulerAppointment_.schApptStatus),appointment.getSchApptStatus());
		cu.set(root.get(SchedulerAppointment_.schApptReason),-2);
		cu.set(root.get(SchedulerAppointment_.schApptReferringdoctorId), appointment.getSchApptReferringdoctorId());
		cu.set(root.get(SchedulerAppointment_.schApptType),appointment.getSchApptType());
		cu.set(root.get(SchedulerAppointment_.schApptNextconsId),appointment.getSchApptNextconsId());
		cu.set(root.get(SchedulerAppointment_.schApptComments),appointment.getSchApptComments());
		cu.set(root.get(SchedulerAppointment_.schApptLastmodifiedTime),Timestamp.valueOf(appointment.getSchApptLastmodifiedTime()));
		cu.set(root.get(SchedulerAppointment_.schApptLastmodifiedUserId),appointment.getSchApptLastmodifiedUserId());
		cu.set(root.get(SchedulerAppointment_.schApptLastmodifiedUsername),appointment.getSchApptLastmodifiedUsername());
		cu.set(root.get(SchedulerAppointment_.schApptRoomId),appointment.getSchApptRoomId());
		cu.set(root.get(SchedulerAppointment_.h555555),appointment.getH555555());
		cu.set(root.get(SchedulerAppointment_.schApptRescheduledfromDate),Timestamp.valueOf("1900-01-01 00:00:00"));
		cu.set(root.get(SchedulerAppointment_.schApptRescheduledfromTime),Timestamp.valueOf("1900-01-01 00:00:00"));
		cu.set(root.get(SchedulerAppointment_.schApptStatusgrpId),schApptStatusGrpId);
		cu.where(cb.equal(root.get(SchedulerAppointment_.schApptId),appointment.getSchApptId()));
		em.createQuery(cu).executeUpdate();	

		Integer[] appoitmentResource = new Integer[1];
		appoitmentResource[0]=appointment.getSchApptResource();
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
		Date date=null;
		date=new Date(appointment.getSchApptDate());
		sdf.format(date);
		List<SchedulerAppointmentBean> data =getAppointments(date,appoitmentResource);
		return data;
	}


	@SuppressWarnings("deprecation")
	@Override
	public List<SchedulerAppointmentBean> changeAppointmentStatus(Appointment appt) {

		int schApptStatusGrpId = getApptStatusGrpId(appt.getSchApptStatus());
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaUpdate<SchedulerAppointment> cu = cb.createCriteriaUpdate(SchedulerAppointment.class);
		Root<SchedulerAppointment> root=cu.from(SchedulerAppointment.class);
		cu.set(root.get(SchedulerAppointment_.schApptStatus),appt.getSchApptStatus());
		cu.set(root.get(SchedulerAppointment_.schApptStatusgrpId),schApptStatusGrpId);
		cu.set(root.get(SchedulerAppointment_.schApptLastmodifiedTime),schedulerAppointmentRepository.findCurrentTimeStamp());
		cu.set(root.get(SchedulerAppointment_.schApptLastmodifiedUsername),appt.getSchApptLastmodifiedUsername());
		cu.set(root.get(SchedulerAppointment_.schApptLastmodifiedUserId),appt.getSchApptLastmodifiedUserId());
		cu.where(cb.equal(root.get(SchedulerAppointment_.schApptId),appt.getSchApptId()));
		em.createQuery(cu).executeUpdate();	
		Integer[] appoitmentResource = new Integer[1];
		appoitmentResource[0]=appt.getSchApptResource();
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
		Date date=null;
		date=new Date(appt.getSchApptDate());
		sdf.format(date);
		List<SchedulerAppointmentBean> data =getAppointments(date,appoitmentResource);
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SchedulerAppointmentBean> getDetailAppointments(Date apptDate,String patientId, String apptId) {

		List<Long> list = new ArrayList<Long>();
		list.add((long)6);
		list.add((long)12);
		list.add((long)45);
		list.add((long)33);
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();


		Root<SchedulerAppointment> root = cq.from(SchedulerAppointment.class);
		Join<SchedulerAppointment, PatientRegistration> joinPatientRegistration=root.join("patRegPatientId",JoinType.LEFT);
		Join<PatientRegistration,PatientInsDetail> patientInsJoin=joinPatientRegistration.join("patientInsuranceTable",JoinType.LEFT);
		Join<PatientRegistration,PatientEligibility> patientEligibleJoin=joinPatientRegistration.join("patientEligibility",JoinType.LEFT);
		Join<PatientRegistration,ReminderDetail> reminderDetailJoin=joinPatientRegistration.join("reminderDetails",JoinType.LEFT);
		Join<ReminderDetail,ReminderCallStatus> reminderCallStatusJoin=reminderDetailJoin.join("reminderCallStatus",JoinType.LEFT);
		Join<SchedulerAppointment, AppReferenceValues> joinH113Status=root.join("App_Reference_ValuesApptStatus",JoinType.LEFT);
		Join<SchedulerAppointment, AppReferenceValues> joinH113Type=root.join("App_Reference_ValuesApptType",JoinType.LEFT);
		//Join<SchedulerAppointment, SchedulerAppointmentParameter> joinSchApptParam=root.join("schApptParam",JoinType.LEFT);
		Join<SchedulerAppointment, SchedulerResource> joinSchResLoc=root.join("schResLoc",JoinType.LEFT);
		Join<SchedulerAppointment, SchedulerResource> joinResProvider=root.join("schResProvider",JoinType.LEFT);
		Join<SchedulerAppointment, ReferringDoctor> joinSchRefDrId=root.join("schRefDrId",JoinType.LEFT);
		Join<SchedulerAppointment, Workflow> joinWorkflow=root.join("workflowPatientId",JoinType.LEFT);
		Join<Join<SchedulerAppointment, Workflow>, AlertCategory> joinWorkflowStatusName=joinWorkflow.join("alertCategoryName",JoinType.LEFT);

		Predicate[] patientInsCondition=new Predicate[]{
				builder.equal(patientInsJoin.get(PatientInsDetail_.patientInsDetailIsactive), new Boolean(true)),
				builder.equal(patientInsJoin.get(PatientInsDetail_.patientInsDetailInstype), new Integer(1)),
		};
		patientInsJoin.on(patientInsCondition);

		Predicate[] patientEligibleCondition=new Predicate[]{
				builder.equal(patientEligibleJoin.get(PatientEligibility_.patientEligibilityActive), new Boolean(true)),
				builder.equal(patientEligibleJoin.get(PatientEligibility_.insuranceType), new String("1")),
		};
		patientEligibleJoin.on(patientEligibleCondition);


		Predicate[] reminderDetailCondition=new Predicate[]{
				builder.greaterThan(reminderDetailJoin.get(ReminderDetail_.reminderApptId),0),
				builder.equal(reminderDetailJoin.get(ReminderDetail_.reminderDetailIsMultiple), new Boolean(false)),
		};
		reminderDetailJoin.on(reminderDetailCondition);


		Predicate[] predicateStatus = new Predicate[] {
				builder.equal(joinH113Status.get(AppReferenceValues_.App_Reference_Values_tableId), new Integer(1)),
				builder.equal(joinH113Status.get(AppReferenceValues_.App_Reference_Values_isActive), new Boolean(true)),
		};
		joinH113Status.on(predicateStatus);
		Predicate[] predicateType = new Predicate[] {
				builder.equal(joinH113Type.get(AppReferenceValues_.App_Reference_Values_tableId), new Integer(2)),
				builder.equal(joinH113Type.get(AppReferenceValues_.App_Reference_Values_isActive), new Boolean(true)),
		};
		joinH113Type.on(predicateType);
	/*	Predicate[] predicateParam = new Predicate[] {
				builder.equal(joinSchApptParam.get(SchedulerAppointmentParameter_.schApptParameterIsactive), new Boolean(true)),
		};
		joinSchApptParam.on(predicateParam);*/
		Predicate[] predicateLocation = new Predicate[] {
				builder.equal(joinSchResLoc.get(SchedulerResource_.schResourceIsdoctor), new Integer(0)),
				builder.equal(joinSchResLoc.get(SchedulerResource_.schResourceLocalserver), new Boolean(true)),
		};
		joinSchResLoc.on(predicateLocation);
		Predicate[] predicateWorkflow = new Predicate[] {
				builder.equal(joinWorkflow.get(Workflow_.workflowIsactive), new Boolean(true)),
		};
		joinWorkflow.on(predicateWorkflow);

		//cq.distinct(true);
		cq.select(builder.construct(SchedulerAppointmentBean.class,
				root.get(SchedulerAppointment_.schApptId),
				builder.function("to_char", String.class, root.get(SchedulerAppointment_.schApptStarttime),builder.literal("HH12:MI:SS AM")),
				builder.function("to_char", String.class, root.get(SchedulerAppointment_.schApptEndtime),builder.literal("HH12:MI:SS AM")),
				root.get(SchedulerAppointment_.schApptDate),
				root.get(SchedulerAppointment_.schApptPatientId),
				joinPatientRegistration.get(PatientRegistration_.patientRegistrationAccountno),
				root.get(SchedulerAppointment_.schApptPatientname),
				root.get(SchedulerAppointment_.schApptHomephone),
				builder.coalesce(root.get(SchedulerAppointment_.schApptHomeextn),builder.literal("-")),
				root.get(SchedulerAppointment_.schApptWorkphone),
				builder.coalesce(root.get(SchedulerAppointment_.schApptWorkextn),builder.literal("-")),
				root.get(SchedulerAppointment_.schApptResource),
				joinH113Status.get(AppReferenceValues_.App_Reference_Values_statusName),
				root.get(SchedulerAppointment_.schApptStatus),
				joinH113Status.get(AppReferenceValues_.App_Reference_Values_option_Color),
				joinH113Type.get(AppReferenceValues_.App_Reference_Values_statusName),
				root.get(SchedulerAppointment_.schApptType),
				joinSchResLoc.get(SchedulerResource_.schResourceName),
				root.get(SchedulerAppointment_.schApptLocation),
				builder.function("getapptpara", String.class,builder.literal(1),root.get(SchedulerAppointment_.schApptId)),
				root.get(SchedulerAppointment_.schApptReason),
				joinSchRefDrId.get(ReferringDoctor_.referring_doctor_referringdoctor),
				root.get(SchedulerAppointment_.schApptReferringdoctorId),
				joinSchRefDrId.get(ReferringDoctor_.referring_doctor_phoneno),
				joinSchRefDrId.get(ReferringDoctor_.referring_doctor_fax_number),
				joinWorkflow.get(Workflow_.workflowStatus),
				joinWorkflowStatusName.get(AlertCategory_.alertCategoryDisplayName),
				joinWorkflow.get(Workflow_.workflowStarttime),
				joinResProvider.get(SchedulerResource_.schResourceConnectionstring),
				patientInsJoin.get(PatientInsDetail_.patientInsDetailCopay),
				builder.coalesce(patientEligibleJoin.get(PatientEligibility_.eligibleStatus),"Not Verified"),
				reminderCallStatusJoin.get(ReminderCallStatus_.reminderCallStatusName),
				reminderDetailJoin.get(ReminderDetail_.reminderDetailReminderStatus)
				)).where(builder.and(builder.equal(root.get(SchedulerAppointment_.schApptDate),apptDate),
						builder.and(builder.equal(root.get(SchedulerAppointment_.schApptPatientId), Integer.parseInt(patientId))),
						builder.and(builder.equal(root.get(SchedulerAppointment_.schApptId), Integer.parseInt(apptId))),
						builder.not(root.get(SchedulerAppointment_.App_Reference_ValuesApptStatus).in(list))
						)).orderBy(builder.asc(root.get(SchedulerAppointment_.schApptStarttime)));


		Query query=em.createQuery(cq);

		List<SchedulerAppointmentBean> schedulerAppointments=query.getResultList();

		return schedulerAppointments;

	}

	@Override
	public String updateDefaultUsers(String zoomValue, String group,
			String resource, String defSize,String userId) {
		
			String []resArray=resource.split(",");
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaDelete<SchedulerUserDefault> delete = builder.createCriteriaDelete(SchedulerUserDefault.class);
			Root<SchedulerUserDefault> root = delete.from(SchedulerUserDefault.class);
			delete.where(builder.and(builder.equal(root.get(SchedulerUserDefault_.schUserDefaultUserId), Integer.parseInt(userId))));
			this.em.createQuery(delete).executeUpdate();
			for(String res:resArray){
				SchedulerUserDefault userDefault=new SchedulerUserDefault();
				userDefault.setSchUserDefaultUserId(Integer.parseInt(userId));
				userDefault.setSchUserDefaultResourceId(Integer.parseInt(res));
				userDefault.setSchUserDefaultTodisplay(true);
				schedulerUserDefaultRepository.saveAndFlush(userDefault);
			}
			CriteriaDelete<SchedulerUserSettings> settingsDelete = builder.createCriteriaDelete(SchedulerUserSettings.class);
			Root<SchedulerUserSettings> schUserSettings = settingsDelete.from(SchedulerUserSettings.class);
			settingsDelete.where(builder.and(builder.equal(schUserSettings.get(SchedulerUserSettings_.schUserSettingsUserId), Integer.parseInt(userId))));
			this.em.createQuery(settingsDelete).executeUpdate();
			SchedulerUserSettings schUserInsert=new SchedulerUserSettings();
			schUserInsert.setSchUserSettingsUserId(Integer.parseInt(userId));
			schUserInsert.setSchUserSettingsZoom(Integer.parseInt(zoomValue));
			schUserInsert.setSchUserSettingsFontSize(defSize);
			schUserInsert.setSchUserSettingsIsSession(false);
			schedulerUserSettingsRepository.saveAndFlush(schUserInsert);

			CriteriaQuery<Object> cq = builder.createQuery();
			Root<SchedulerUserDefault> schuserDefault = cq.from(SchedulerUserDefault.class);
			cq.select(schuserDefault.get(SchedulerUserDefault_.schUserDefaultResourceId));
			cq.where(builder.and(builder.equal(schuserDefault.get(SchedulerUserDefault_.schUserDefaultUserId),userId)));
			
			List<Object> resourceList=em.createQuery(cq).getResultList();
			String resourceString="";
			for(int i=0;i<resourceList.size();i++){
				String res = new Integer((Integer)resourceList.get(i)).toString();
				resourceString=resourceString+res+",";
			}	
			return resourceString;
	}

}