package com.glenwood.glaceemr.server.application.services.portal.portalAppointments;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.AlertCategory;
import com.glenwood.glaceemr.server.application.models.AlertCategory_;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.AppReferenceValues;
import com.glenwood.glaceemr.server.application.models.AppReferenceValues_;
import com.glenwood.glaceemr.server.application.models.AppointmentDetailsBean;
import com.glenwood.glaceemr.server.application.models.ApptRequestBean;
import com.glenwood.glaceemr.server.application.models.BookingConfig;
import com.glenwood.glaceemr.server.application.models.PatientPortalAlertConfig;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.PortalApptRequest;
import com.glenwood.glaceemr.server.application.models.PortalApptRequestDetail;
import com.glenwood.glaceemr.server.application.models.PortalApptRequestDetailBean;
import com.glenwood.glaceemr.server.application.models.PortalApptRequest_;
import com.glenwood.glaceemr.server.application.models.PortalSchedulerAppointmentBean;
import com.glenwood.glaceemr.server.application.models.PrimarykeyGenerator;
import com.glenwood.glaceemr.server.application.models.ReferringDoctor;
import com.glenwood.glaceemr.server.application.models.ReferringDoctor_;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointment;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointmentParameter;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointmentParameter_;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointment_;
import com.glenwood.glaceemr.server.application.models.SchedulerApptBookingBean;
import com.glenwood.glaceemr.server.application.models.SchedulerDateTemplate;
import com.glenwood.glaceemr.server.application.models.SchedulerLock;
import com.glenwood.glaceemr.server.application.models.SchedulerResource;
import com.glenwood.glaceemr.server.application.models.SchedulerResourceCategory;
import com.glenwood.glaceemr.server.application.models.SchedulerResource_;
import com.glenwood.glaceemr.server.application.models.SchedulerTemplate;
import com.glenwood.glaceemr.server.application.models.SchedulerTemplateDetail;
import com.glenwood.glaceemr.server.application.models.SchedulerTemplateTimeMapping;
import com.glenwood.glaceemr.server.application.models.Workflow;
import com.glenwood.glaceemr.server.application.models.Workflow_;
import com.glenwood.glaceemr.server.application.repositories.BookingConfigRepository;
import com.glenwood.glaceemr.server.application.repositories.AlertEventRepository;
import com.glenwood.glaceemr.server.application.repositories.AppReferenceValuesRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientPortalAlertConfigRespository;
import com.glenwood.glaceemr.server.application.repositories.PortalApptRequestDetailRepository;
import com.glenwood.glaceemr.server.application.repositories.PortalApptRequestRepository;
import com.glenwood.glaceemr.server.application.repositories.SchDateTemplateRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerAppointmentParameterRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerAppointmentRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerLockRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerResourceCategoryRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerResourcesRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerTemplateDetailRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerTemplateRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerTemplateTimeMappingRepository;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailSaveService;
import com.glenwood.glaceemr.server.application.specifications.PortalAppointmentsSpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalSettingsSpecification;
import com.glenwood.glaceemr.server.utils.SessionMap;

@Service
public class PortalAppointmentsServiceImpl implements PortalAppointmentsService{


	@Autowired
	SchedulerAppointmentRepository schedulerAppointmentRepository;
	
	@Autowired
	PortalApptRequestRepository portalApptRequestRepository;
	
	@Autowired
	PortalApptRequestDetailRepository portalApptRequestDetailRepository;

	@Autowired
	SchedulerResourcesRepository schedulerResourcesRepository;

	@Autowired
	SchedulerResourceCategoryRepository schedulerResourceCategoryRepository;

	@Autowired
	SchedulerTemplateDetailRepository schedulerTemplateDetailRepository;

	@Autowired
	SchDateTemplateRepository schDateTemplateRepository;
	
	@Autowired
	SchedulerTemplateRepository schedulerTemplateRepository;
	
	@Autowired
	SchedulerLockRepository schedulerLockRepository;

	@Autowired
	SchedulerTemplateTimeMappingRepository schedulerTemplateTimeMappingRepository;
	
	@Autowired
	SchedulerAppointmentParameterRepository schedulerAppointmentParameterRepository;

	@Autowired
	AppReferenceValuesRepository h113Repository;
	
	@Autowired
	PatientPortalAlertConfigRespository patient_portal_alert_configRespository;
	
	@Autowired
	AlertEventRepository alertEventRepository;

	@Autowired
	EntityManager em;

	@Autowired
	EntityManagerFactory emf;
	
	@Autowired
	ObjectMapper objectMapper; 
	
	@Autowired
	AuditTrailSaveService auditTrailSaveService;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	BookingConfigRepository BookingConfigRepository;

	@Override
	public List<SchedulerAppointment> getPatientFutureAppointmentsList(int patientId, int pageOffset, int pageIndex) {

		List<SchedulerAppointment> apptList=schedulerAppointmentRepository.findAll(PortalAppointmentsSpecification.getPatientFutureAppointmentsByDate(patientId),PortalAppointmentsSpecification.createPortalApptListRequestByDescDate(pageIndex, pageOffset)).getContent();

		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Requested future appointments",-1,
				request.getRemoteAddr(),patientId,"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Patient with id "+patientId+"requested future appointments.","");
		
		return apptList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PortalSchedulerAppointmentBean> getPatientAppointments(int patientId, int pageOffset, int pageIndex, String apptType) throws ParseException {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<SchedulerAppointment> root = cq.from(SchedulerAppointment.class);
		Join<SchedulerAppointment, PatientRegistration> joinPatientRegistration=root.join("patRegPatientId",JoinType.LEFT);
		Join<SchedulerAppointment, AppReferenceValues> joinH113Status=root.join("App_Reference_ValuesApptStatus",JoinType.LEFT);
		Join<SchedulerAppointment, AppReferenceValues> joinH113Type=root.join("App_Reference_ValuesApptType",JoinType.LEFT);
		Join<SchedulerAppointment, SchedulerAppointmentParameter> joinSchApptParam=root.join("schApptParam",JoinType.LEFT);
		Join<Join<SchedulerAppointment, SchedulerAppointmentParameter>, AppReferenceValues> joinH113Reason=joinSchApptParam.join("App_Reference_ValuesReason",JoinType.LEFT);
		Join<SchedulerAppointment, SchedulerResource> joinSchResLoc=root.join("schResLoc",JoinType.LEFT);
		Join<SchedulerAppointment, SchedulerResource> joinSchResProvider=root.join("schResProvider",JoinType.LEFT);
		Join<SchedulerAppointment, ReferringDoctor> joinSchRefDrId=root.join("schRefDrId",JoinType.LEFT);
		Join<SchedulerAppointment, Workflow> joinWorkflow=root.join("workflowPatientId",JoinType.LEFT);
		Join<Join<SchedulerAppointment, Workflow>, AlertCategory> joinWorkflowStatusName=joinWorkflow.join("alertCategoryName",JoinType.LEFT);
		
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
		Predicate[] predicateParam = new Predicate[] {
				builder.equal(joinSchApptParam.get(SchedulerAppointmentParameter_.schApptParameterIsactive), new Boolean(true)),
		};
		joinSchApptParam.on(predicateParam);
		Predicate[] predicateReason = new Predicate[] {
				builder.equal(joinH113Reason.get(AppReferenceValues_.App_Reference_Values_tableId), new Integer(402)),
				builder.equal(joinH113Reason.get(AppReferenceValues_.App_Reference_Values_reason_type), new Integer(1)),
				builder.equal(joinH113Reason.get(AppReferenceValues_.App_Reference_Values_isActive), new Boolean(true)),
		};
		joinH113Reason.on(predicateReason);
		Predicate[] predicateLocation = new Predicate[] {
				builder.equal(joinSchResLoc.get(SchedulerResource_.schResourceIsdoctor), new Integer(0)),
				builder.equal(joinSchResLoc.get(SchedulerResource_.schResourceLocalserver), new Boolean(true)),
		};
		joinSchResLoc.on(predicateLocation);
		Predicate[] predicateProvider = new Predicate[] {
				builder.notEqual(joinSchResProvider.get(SchedulerResource_.schResourceIsdoctor), new Integer(0)),
				builder.equal(joinSchResProvider.get(SchedulerResource_.schResourceLocalserver), new Boolean(true)),
		};
		joinSchResProvider.on(predicateProvider);
		Predicate[] predicateWorkflow = new Predicate[] {
				builder.equal(joinWorkflow.get(Workflow_.workflowIsactive), new Boolean(true)),
		};
		joinWorkflow.on(predicateWorkflow);
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date today = new Date();
		System.out.println(dateFormat.format(today));
		try {
			today = dateFormat.parse(dateFormat.format(today));
		} catch (ParseException e) {

			e.printStackTrace();
		}
		
		
		Date defaultDate=dateFormat.parse("1900/01/01");
		
		Predicate apptTypePredicate;
		
		if(apptType.equalsIgnoreCase("FUTURE"))
			apptTypePredicate=builder.greaterThanOrEqualTo(root.get(SchedulerAppointment_.schApptDate), today);
		else if(apptType.equalsIgnoreCase("PAST"))
			apptTypePredicate=builder.lessThan(root.get(SchedulerAppointment_.schApptDate), today);
		else if(apptType.equalsIgnoreCase("PRESENT"))
			apptTypePredicate=builder.equal(root.get(SchedulerAppointment_.schApptDate), today);
		else
			apptTypePredicate=builder.greaterThanOrEqualTo(root.get(SchedulerAppointment_.schApptDate), defaultDate);
		
		Predicate patientPredicate=builder.equal(root.get(SchedulerAppointment_.schApptPatientId), patientId);
		
		
		cq.select(builder.construct(PortalSchedulerAppointmentBean.class,
				root.get(SchedulerAppointment_.schApptId),
				builder.function("to_char", String.class, root.get(SchedulerAppointment_.schApptStarttime),builder.literal("HH12:MI AM")),
				builder.function("to_char", String.class, root.get(SchedulerAppointment_.schApptEndtime),builder.literal("HH12:MI AM")),
				root.get(SchedulerAppointment_.schApptDate),
				root.get(SchedulerAppointment_.schApptInterval),
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
				joinH113Type.get(AppReferenceValues_.App_Reference_Values_statusName),
				root.get(SchedulerAppointment_.schApptType),
				joinSchResLoc.get(SchedulerResource_.schResourceName),
				root.get(SchedulerAppointment_.schApptLocation),
				joinSchResProvider.get(SchedulerResource_.schResourceFullname),
				joinSchResProvider.get(SchedulerResource_.schResourceDoctorId),
				joinH113Reason.get(AppReferenceValues_.App_Reference_Values_statusName),
				root.get(SchedulerAppointment_.schApptReason),
				root.get(SchedulerAppointment_.schApptComments),
				joinSchRefDrId.get(ReferringDoctor_.referring_doctor_referringdoctor),
				root.get(SchedulerAppointment_.schApptReferringdoctorId),
				joinSchRefDrId.get(ReferringDoctor_.referring_doctor_phoneno),
				joinSchRefDrId.get(ReferringDoctor_.referring_doctor_fax_number),
				joinWorkflow.get(Workflow_.workflowStatus),
				joinWorkflowStatusName.get(AlertCategory_.alertCategoryDisplayName),
				joinWorkflow.get(Workflow_.workflowStarttime)
				)).where(builder.and(apptTypePredicate,patientPredicate)).orderBy(builder.asc(root.get(SchedulerAppointment_.schApptDate)));

		Query query=em.createQuery(cq);
				
		List<PortalSchedulerAppointmentBean> schedulerAppointments=query.setMaxResults(pageOffset).setFirstResult(pageIndex * pageOffset).getResultList();
		
		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Patient with Id:"+patientId+"requested patient appointments",-1,
				request.getRemoteAddr(),patientId,"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Patient with id "+patientId+"requested patient appointments.","");
		
		return schedulerAppointments;
		
		/*PortalAppointmentsSpecification.getPatientFutureAppointmentsByDate(patientId),PortalAppointmentsSpecification.createPortalApptListRequestByDescDate(pageIndex, pageOffset)*/
		
	}

	@Override
	public List<SchedulerAppointment> getPatientPastAppointmentsList(int patientId, int pageOffset, int pageIndex) {

		List<SchedulerAppointment> apptList=schedulerAppointmentRepository.findAll(PortalAppointmentsSpecification.getPatientPastAppointmentsByDate(patientId),PortalAppointmentsSpecification.createPortalApptListRequestByDescDate(pageIndex, pageOffset)).getContent();

		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Requested past appointments",-1,
				request.getRemoteAddr(),patientId,"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Patient with id "+patientId+"requested past appointments.","");
		
		return apptList;
	}

	@Override
	public List<SchedulerAppointment> getPatientTodaysAppointmentsList(int patientId, int pageOffset, int pageIndex) {

		List<SchedulerAppointment> apptList=schedulerAppointmentRepository.findAll(PortalAppointmentsSpecification.getPatientTodaysAppointmentsByDate(patientId),PortalAppointmentsSpecification.createPortalApptListRequestByDescDate(pageIndex, pageOffset)).getContent();

		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Requested appointments of today",-1,
				request.getRemoteAddr(),patientId,"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Patient with id "+patientId+"requested appointments of today.","");
		
		return apptList;
	}
	
	@Override
	public List<SchedulerAppointment> getPatientTotalAppointmentsList(int patientId, int pageOffset, int pageIndex) {

		List<SchedulerAppointment> apptList=schedulerAppointmentRepository.findAll(PortalAppointmentsSpecification.getPatientTotalAppointments(patientId),PortalAppointmentsSpecification.createPortalApptListRequestByDescDate(pageIndex, pageOffset)).getContent();

		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Requested total appointments",-1,
				request.getRemoteAddr(),patientId,"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Patient with id "+patientId+"requested total appointments.","");
		
		return apptList;
	}


	@Override
	public AppointmentDetailsBean getAppointmentDetails() {

		AppointmentDetailsBean appointmentDetailsBean=new AppointmentDetailsBean();

		List<AppReferenceValues> schApptStatusList=h113Repository.findAll(PortalAppointmentsSpecification.getSchApptStatusList());
		List<AppReferenceValues> schApptTypeList=h113Repository.findAll(PortalAppointmentsSpecification.getSchApptTypeList());
		List<AppReferenceValues> schApptReasonList=h113Repository.findAll(PortalAppointmentsSpecification.getSchApptReasonList());

		appointmentDetailsBean.setSchApptStatusList(schApptStatusList);
		appointmentDetailsBean.setSchApptTypeList(schApptTypeList);
		appointmentDetailsBean.setSchApptReasonList(schApptReasonList);

		return appointmentDetailsBean;
	}

	@Override
	public List<SchedulerResource> getApptBookLocationList() {

		List<SchedulerResource> apptBookLocationList=schedulerResourcesRepository.findAll(PortalAppointmentsSpecification.getAppointmentBookingLocations());

		return apptBookLocationList;
	}	

	@Override
	public List<SchedulerResource> getApptBookDoctorsList(int posId) {

		List<SchedulerResource> apptBookDoctorsList=schedulerResourcesRepository.findAll(PortalAppointmentsSpecification.getAppointmentBookingDoctors(posId));

		return apptBookDoctorsList;
	}

	@Override
	public List<SchedulerResourceCategory> getSchResourceCategoriesList(){

		List<SchedulerResourceCategory> schResourceCategoryList=schedulerResourceCategoryRepository.findAll(PortalAppointmentsSpecification.getSchResourceCategoriesList());

		return schResourceCategoryList;
	}
	
	@Override
	public List<PortalApptRequest> getPortalApptRequestList(int patientId, int pageOffset, int pageIndex) {
		
		List<PortalApptRequest> portalApptRequestList=portalApptRequestRepository.findAll(PortalAppointmentsSpecification.getPatientAppointmentRequests(patientId),PortalAppointmentsSpecification.createPortalRequestApptListRequestByDescDate(pageIndex, pageOffset)).getContent();
		
		
		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Requested patient appointment requests with patientId:" +patientId,-1,
				request.getRemoteAddr(),patientId,"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Requested patient appointment requests with patientId: "+patientId,"");
		
		return portalApptRequestList;
	}




	@Override
	public List<Integer> getSchDateTemplateIdList(int ownerId,Date appDate){SchedulerDateTemplate schedulerDateTemplate=schDateTemplateRepository.findOne(PortalAppointmentsSpecification.getSchDateTemplateIds(ownerId, appDate));

	List<Integer> templateIdList=new ArrayList<Integer>();

	if(schedulerDateTemplate==null||schedulerDateTemplate.getSchDateTemplateTemplateIds()==null){

		String resultTemplateId=getSchTemplateTimeMappingId(ownerId, appDate);
		
		if(resultTemplateId!=null){
		String[] templateArr=resultTemplateId.split(",");
		for(int i=0;i<templateArr.length;i++){
			templateIdList.add(Integer.parseInt(templateArr[i]));
		}
		}

	}
	else{
		String[] templateArr=schedulerDateTemplate.getSchDateTemplateTemplateIds().split(",");

		for(int i=0;i<templateArr.length;i++){
			templateIdList.add(Integer.parseInt(templateArr[i]));
		}

	}

	return templateIdList;
	}

	@Override
	public List<SchedulerTemplateDetail> getApptFreeSlotsByProviderIdAndDate(
			int ownerId, Date appDate) {

		List<Integer> templateIdList= getSchDateTemplateIdList(ownerId,appDate);

		List<SchedulerTemplateDetail> templateDeatailsList=schedulerTemplateDetailRepository.findAll(PortalAppointmentsSpecification.getFreeSlotSchTemplateDetailList(ownerId,appDate,templateIdList));

		return templateDeatailsList;
	}


	public Integer getSchTemplateId(int ownerId, Date apptDate){

		SchedulerTemplate schedulerTemplate=schedulerTemplateRepository.findOne(PortalAppointmentsSpecification.getSchTemplateId(ownerId, apptDate));
		
		return schedulerTemplate.getSchTemplateId();
	}

	public String getSchTemplateTimeMappingId(int ownerId, Date apptDate){

		int templateId=getSchTemplateId(ownerId, apptDate);
		List<SchedulerTemplateTimeMapping> templateTimeMappingList=schedulerTemplateTimeMappingRepository.findAll(PortalAppointmentsSpecification.getSchTemplateTimeMappingId(templateId));
		
		String resultTemplateId="";
		
		for(int i=0; i<templateTimeMappingList.size(); i++){
			
			resultTemplateId=resultTemplateId+","+templateTimeMappingList.get(i).getSchTemplateTimeMappingTemplateDetailId();
		}
		
		if(resultTemplateId.substring(0,1).equals(",")){
			resultTemplateId 			=resultTemplateId.substring(1,resultTemplateId.length());
		}
		else
			resultTemplateId=null;
		
		if(resultTemplateId!=null){
			
			SchedulerDateTemplate schedulerDateTemplate=new SchedulerDateTemplate();
			schedulerDateTemplate.setSchDateTemplateResourceId(ownerId);
			schedulerDateTemplate.setSchDateTemplateTemplateIds(resultTemplateId);
			schedulerDateTemplate.setSchDateTemplateDate(new java.sql.Date(apptDate.getTime()));
			schDateTemplateRepository.saveAndFlush(schedulerDateTemplate);
		}
		
		
		return resultTemplateId;
	}

	@Override
	public AlertEvent createPortalAppointmentRequest(ApptRequestBean apptRequestBean) throws ParseException {
		
		
		PortalApptRequest portalApptRequest=new  PortalApptRequest();
		portalApptRequest.setPortalApptRequestAllotmentSummary("");
		portalApptRequest.setPortalApptRequestApptIds("");
		portalApptRequest.setPortalApptRequestCurrentStatus(apptRequestBean.getPortalApptRequestBean().getPortalApptRequestCurrentStatus());
		portalApptRequest.setPortalApptRequestPatientId(apptRequestBean.getPortalApptRequestBean().getPortalApptRequestPatientId());
		portalApptRequest.setPortalApptRequestReason(apptRequestBean.getPortalApptRequestBean().getPortalApptRequestReason());
		portalApptRequest.setPortalApptRequestReourceId(apptRequestBean.getPortalApptRequestBean().getPortalApptRequestReourceId());
		portalApptRequest.setPortalApptRequestRequestMadeTime(Timestamp.valueOf(apptRequestBean.getPortalApptRequestBean().getPortalApptRequestRequestMadeTime()));
		
		
		PortalApptRequest apptRequest=portalApptRequestRepository.saveAndFlush(portalApptRequest);
		
		int portalApptRequestId=getLatestPortalApptRequestId(apptRequestBean.getAlertEventPatientId());
		
		for(int i=0;i<apptRequestBean.getPortalApptRequestBean().getPortalApptRequestDetailsList().size();i++){
			
			PortalApptRequestDetailBean requestDetail=apptRequestBean.getPortalApptRequestBean().getPortalApptRequestDetailsList().get(i);
			
			PortalApptRequestDetail portalApptRequestDetail=new PortalApptRequestDetail();
			
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			
			portalApptRequestDetail.setPortalApptRequestDetailRequestId(portalApptRequestId);
			portalApptRequestDetail.setPortalApptRequestDetailCurrentStatus(requestDetail.getPortalApptRequestDetailCurrentStatus());
			portalApptRequestDetail.setPortalApptRequestDetailRequestDate(new java.sql.Date(dateFormat.parse(requestDetail.getPortalApptRequestDetailRequestDate()).getTime()));
			portalApptRequestDetail.setPortalApptRequestDetailRequestRank(requestDetail.getPortalApptRequestDetailRequestRank());
			portalApptRequestDetail.setPortalApptRequestDetailTimeStart(Timestamp.valueOf(requestDetail.getPortalApptRequestDetailTimeStart()));
			portalApptRequestDetail.setPortalApptRequestDetailTimeEnd(Timestamp.valueOf(requestDetail.getPortalApptRequestDetailTimeEnd()));
			
			portalApptRequestDetailRepository.saveAndFlush(portalApptRequestDetail);
		}
		
		
		
		PatientPortalAlertConfig demographicAlertCategory=patient_portal_alert_configRespository.findOne(PortalSettingsSpecification.getPortalAlertCategory(4));
		  boolean sendToAll =demographicAlertCategory.getpatient_portal_alert_config_send_to_all();  
        int provider = Integer.parseInt(demographicAlertCategory.getpatient_portal_alert_config_provider());
        int forwardTo = Integer.parseInt(demographicAlertCategory.getpatient_portal_alert_config_forward_to());
        
		AlertEvent alert1=new AlertEvent();
		alert1.setAlertEventCategoryId(19);
		alert1.setAlertEventStatus(1);
		alert1.setAlertEventPatientId(apptRequestBean.getAlertEventPatientId());
		alert1.setAlertEventPatientName(apptRequestBean.getAlertEventPatientName());
		alert1.setAlertEventEncounterId(-1);
		alert1.setAlertEventRefId(apptRequest.getPortalApptRequestId());
		alert1.setAlertEventMessage("Appointment Request from Patient Portal.");
		alert1.setAlertEventRoomId(-1);
		alert1.setAlertEventCreatedDate(new Timestamp(new Date().getTime()));
		alert1.setAlertEventModifiedby(-100);
		alert1.setAlertEventFrom(-100);
		
		if(provider==0 && forwardTo==0)
			alert1.setAlertEventTo(-1);
      else {
        if(sendToAll){
       	 if(forwardTo!=provider){
       		alert1.setAlertEventTo(forwardTo);
       		AlertEvent alert2=new AlertEvent();
    		alert2.setAlertEventCategoryId(19);
    		alert2.setAlertEventStatus(1);
    		alert2.setAlertEventPatientId(apptRequestBean.getAlertEventPatientId());
    		alert2.setAlertEventPatientName(apptRequestBean.getAlertEventPatientName());
    		alert2.setAlertEventEncounterId(-1);
    		alert2.setAlertEventRefId(apptRequest.getPortalApptRequestId());
    		alert2.setAlertEventMessage("Appointment Request from Patient Portal.");
    		alert2.setAlertEventRoomId(-1);
    		alert2.setAlertEventCreatedDate(new Timestamp(new Date().getTime()));
    		alert2.setAlertEventModifiedby(-100);
    		alert2.setAlertEventFrom(-100);
       		alert2.setAlertEventTo(provider);
       		alertEventRepository.saveAndFlush(alert2);
       	 } else {
       		alert1.setAlertEventTo(forwardTo);
       	 }            	 
        }else{
       	 if(forwardTo!=0){
       		alert1.setAlertEventTo(forwardTo);
       	 } else {
       		alert1.setAlertEventTo(provider);
       	}
        }
       }
		
		
		AlertEvent savedAlert=alertEventRepository.saveAndFlush(alert1);
		
		auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
				AuditTrailEnumConstants.LogActionType.CREATE,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Created an appointment request",-1,
				request.getRemoteAddr(),apptRequestBean.getAlertEventPatientId(),"",
				AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Patient with id "+apptRequestBean.getAlertEventPatientId()+"requested an appointment","");
				
		return savedAlert;
	}

	@Override
	public List<SchedulerAppointment> getBookedSlots(int resourceId, Date apptDate) throws JsonProcessingException {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<SchedulerAppointment> root = cq.from(SchedulerAppointment.class);
		Join<SchedulerAppointment, PatientRegistration> joinPatientRegistration=root.join("patRegPatientId",JoinType.LEFT);
		Join<SchedulerAppointment, AppReferenceValues> joinH113Status=root.join("App_Reference_ValuesApptStatus",JoinType.LEFT);
		Join<SchedulerAppointment, AppReferenceValues> joinH113Type=root.join("App_Reference_ValuesApptType",JoinType.LEFT);
		Join<SchedulerAppointment, SchedulerAppointmentParameter> joinSchApptParam=root.join("schApptParam",JoinType.LEFT);
		Join<Join<SchedulerAppointment, SchedulerAppointmentParameter>, AppReferenceValues> joinH113Reason=joinSchApptParam.join("App_Reference_ValuesReason",JoinType.LEFT);
		Join<SchedulerAppointment, SchedulerResource> joinSchResLoc=root.join("schResLoc",JoinType.LEFT);
		Join<SchedulerAppointment, SchedulerResource> joinSchResProvider=root.join("schResProvider",JoinType.LEFT);
		Join<SchedulerAppointment, ReferringDoctor> joinSchRefDrId=root.join("schRefDrId",JoinType.LEFT);
		Join<SchedulerAppointment, Workflow> joinWorkflow=root.join("workflowPatientId",JoinType.LEFT);
		Join<Join<SchedulerAppointment, Workflow>, AlertCategory> joinWorkflowStatusName=joinWorkflow.join("alertCategoryName",JoinType.LEFT);
		
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
		Predicate[] predicateParam = new Predicate[] {
				builder.equal(joinSchApptParam.get(SchedulerAppointmentParameter_.schApptParameterIsactive), new Boolean(true)),
		};
		joinSchApptParam.on(predicateParam);
		Predicate[] predicateReason = new Predicate[] {
				builder.equal(joinH113Reason.get(AppReferenceValues_.App_Reference_Values_tableId), new Integer(402)),
				builder.equal(joinH113Reason.get(AppReferenceValues_.App_Reference_Values_reason_type), new Integer(1)),
				builder.equal(joinH113Reason.get(AppReferenceValues_.App_Reference_Values_isActive), new Boolean(true)),
		};
		joinH113Reason.on(predicateReason);
		Predicate[] predicateLocation = new Predicate[] {
				builder.equal(joinSchResLoc.get(SchedulerResource_.schResourceIsdoctor), new Integer(0)),
				builder.equal(joinSchResLoc.get(SchedulerResource_.schResourceLocalserver), new Boolean(true)),
		};
		joinSchResLoc.on(predicateLocation);
		Predicate[] predicateProvider = new Predicate[] {
				builder.notEqual(joinSchResProvider.get(SchedulerResource_.schResourceIsdoctor), new Integer(0)),
				builder.equal(joinSchResProvider.get(SchedulerResource_.schResourceLocalserver), new Boolean(true)),
		};
		joinSchResProvider.on(predicateProvider);
		Predicate[] predicateWorkflow = new Predicate[] {
				builder.equal(joinWorkflow.get(Workflow_.workflowIsactive), new Boolean(true)),
		};
		joinWorkflow.on(predicateWorkflow);
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date today = new Date();
		System.out.println(dateFormat.format(today));
		try {
			today = dateFormat.parse(dateFormat.format(today));
		} catch (ParseException e) {

			e.printStackTrace();
		}
				
		Predicate apptDatePredicate=builder.equal(root.get(SchedulerAppointment_.schApptDate), apptDate );
		Predicate apptProviderPredicate=builder.equal(joinSchResProvider.get(SchedulerResource_.schResourceId), resourceId);		
		
		cq.select(builder.construct(PortalSchedulerAppointmentBean.class,
				root.get(SchedulerAppointment_.schApptId),
				builder.function("to_char", String.class, root.get(SchedulerAppointment_.schApptStarttime),builder.literal("MM/dd/yyyy HH12:MI AM")),
				builder.function("to_char", String.class, root.get(SchedulerAppointment_.schApptEndtime),builder.literal("MM/dd/yyyy HH12:MI AM")),
				root.get(SchedulerAppointment_.schApptDate),
				root.get(SchedulerAppointment_.schApptInterval),
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
				joinH113Type.get(AppReferenceValues_.App_Reference_Values_statusName),
				root.get(SchedulerAppointment_.schApptType),
				joinSchResLoc.get(SchedulerResource_.schResourceName),
				root.get(SchedulerAppointment_.schApptLocation),
				joinSchResProvider.get(SchedulerResource_.schResourceFullname),
				joinSchResProvider.get(SchedulerResource_.schResourceDoctorId),
				joinH113Reason.get(AppReferenceValues_.App_Reference_Values_statusName),
				root.get(SchedulerAppointment_.schApptReason),
				root.get(SchedulerAppointment_.schApptComments),
				joinSchRefDrId.get(ReferringDoctor_.referring_doctor_referringdoctor),
				root.get(SchedulerAppointment_.schApptReferringdoctorId),
				joinSchRefDrId.get(ReferringDoctor_.referring_doctor_phoneno),
				joinSchRefDrId.get(ReferringDoctor_.referring_doctor_fax_number),
				joinWorkflow.get(Workflow_.workflowStatus),
				joinWorkflowStatusName.get(AlertCategory_.alertCategoryDisplayName),
				joinWorkflow.get(Workflow_.workflowStarttime)
				)).where(builder.and(apptDatePredicate,apptProviderPredicate)).orderBy(builder.asc(root.get(SchedulerAppointment_.schApptStarttime)));

		Query query=em.createQuery(cq);
				
		List<PortalSchedulerAppointmentBean> schedulerAppointments=query.getResultList();
				
		List<SchedulerAppointment> bookedSots=new ArrayList<SchedulerAppointment>();
		for(int i=0;i<schedulerAppointments.size();i++){
			
			SchedulerAppointment appt=new SchedulerAppointment();
			appt.setSchApptComments(schedulerAppointments.get(i).getApptComments());
			appt.setSchApptDate(new java.sql.Date(schedulerAppointments.get(i).getApptDate().getTime()));
			appt.setSchApptEndtime(new Timestamp(new Date(schedulerAppointments.get(i).getApptEndTime()).getTime()));
			appt.setSchApptId(schedulerAppointments.get(i).getApptId());
			appt.setSchApptInterval(schedulerAppointments.get(i).getApptInterval());
			appt.setSchApptLocation(schedulerAppointments.get(i).getApptLocationId());
			appt.setSchApptReason(schedulerAppointments.get(i).getApptReasonId());
			appt.setSchApptResource(schedulerAppointments.get(i).getApptProviderId());
			appt.setSchApptStarttime(new Timestamp(new Date(schedulerAppointments.get(i).getApptStartTime()).getTime()));
			appt.setSchApptStatus(schedulerAppointments.get(i).getApptStatusId());
			bookedSots.add(appt);
		}
				
		return bookedSots;
	}

	@Override
	public List<SchedulerLock> getLockedSlots(int resourceId, Date apptDate) {
		
		List<SchedulerLock> lockedSlotsList=schedulerLockRepository.findAll(PortalAppointmentsSpecification.getLockedSlots(resourceId, apptDate));
		
		return lockedSlotsList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public SchedulerAppointment bookAppointment(SchedulerApptBookingBean schedulerApptBookingBean) {
		
		int schApptId=getNewSchApptId();
		
		SchedulerAppointment schApptBean=new SchedulerAppointment();
		schApptBean.setSchApptId(schApptId);
		schApptBean.setSchApptDate(java.sql.Date.valueOf(schedulerApptBookingBean.getSchApptDate()));
		schApptBean.setSchApptStarttime(Timestamp.valueOf(schedulerApptBookingBean.getSchApptStarttime()));
		schApptBean.setSchApptEndtime(Timestamp.valueOf(schedulerApptBookingBean.getSchApptEndtime()));
		schApptBean.setSchApptInterval(schedulerApptBookingBean.getSchApptInterval());
		schApptBean.setSchApptPatientId(schedulerApptBookingBean.getSchApptPatientId());
		schApptBean.setSchApptPatientname(schedulerApptBookingBean.getSchApptPatientname());
		schApptBean.setSchApptLocation(schedulerApptBookingBean.getSchApptLocation());
		schApptBean.setSchApptResource(schedulerApptBookingBean.getSchApptResource());
		schApptBean.setSchApptStatus(schedulerApptBookingBean.getSchApptStatus());
		schApptBean.setSchApptReason(-2);
		schApptBean.setSchApptType(schedulerApptBookingBean.getSchApptType());
		schApptBean.setSchApptNextconsId(schedulerApptBookingBean.getSchApptNextconsId());
		schApptBean.setSchApptComments(schedulerApptBookingBean.getSchApptComments());
		schApptBean.setSchApptLastmodifiedTime(Timestamp.valueOf(schedulerApptBookingBean.getSchApptLastmodifiedTime()));
		schApptBean.setSchApptLastmodifiedUserId(schedulerApptBookingBean.getSchApptLastmodifiedUserId());
		schApptBean.setSchApptLastmodifiedUsername(schedulerApptBookingBean.getSchApptLastmodifiedUsername());
		schApptBean.setSchApptRoomId(schedulerApptBookingBean.getSchApptRoomId());
		schApptBean.setH555555(schedulerApptBookingBean.getH555555());
		schApptBean.setSchApptStatusgrpId(schedulerApptBookingBean.getSchApptStatusgrpId());
		
		SchedulerAppointment schAppt=schedulerAppointmentRepository.saveAndFlush(schApptBean);
		
		SchedulerAppointmentParameter schApptParam=new SchedulerAppointmentParameter();
		schApptParam.setSchApptParameterApptId(schAppt.getSchApptId());
		schApptParam.setSchApptParameterIsactive(true);
		schApptParam.setSchApptParameterOn(new Timestamp(schAppt.getSchApptDate().getTime()));
		schApptParam.setSchApptParameterType(1);
		schApptParam.setSchApptParameterValueId(schedulerApptBookingBean.getSchApptReason());
		schApptParam.setSchApptParameterId(getNewSchApptParameterId());
		schedulerAppointmentParameterRepository.saveAndFlush(schApptParam);
		
		executeTesttablePrimaryKeyGenerator();
				
		
		/*Creating an alert for the booked appointment*/
		PatientPortalAlertConfig demographicAlertCategory=patient_portal_alert_configRespository.findOne(PortalSettingsSpecification.getPortalAlertCategory(4));
		  boolean sendToAll =demographicAlertCategory.getpatient_portal_alert_config_send_to_all();  
      int provider = Integer.parseInt(demographicAlertCategory.getpatient_portal_alert_config_provider());
      int forwardTo = Integer.parseInt(demographicAlertCategory.getpatient_portal_alert_config_forward_to());
      
		AlertEvent alert1=new AlertEvent();
		alert1.setAlertEventCategoryId(19);
		alert1.setAlertEventStatus(1);
		alert1.setAlertEventPatientId(schedulerApptBookingBean.getSchApptPatientId());
		alert1.setAlertEventPatientName(schedulerApptBookingBean.getSchApptPatientname());
		alert1.setAlertEventEncounterId(-1);
		alert1.setAlertEventRefId(schAppt.getSchApptId());
		alert1.setAlertEventMessage("Appointment Booking from Patient Portal.");
		alert1.setAlertEventRoomId(-1);
		alert1.setAlertEventCreatedDate(new Timestamp(new Date().getTime()));
		alert1.setAlertEventModifiedby(-100);
		alert1.setAlertEventFrom(-100);
		
		if(provider==0 && forwardTo==0)
			alert1.setAlertEventTo(-1);
    else {
      if(sendToAll){
     	 if(forwardTo!=provider){
     		alert1.setAlertEventTo(forwardTo);
     		AlertEvent alert2=new AlertEvent();
    		alert2.setAlertEventCategoryId(19);
    		alert2.setAlertEventStatus(1);
    		alert2.setAlertEventPatientId(schedulerApptBookingBean.getSchApptPatientId());
    		alert2.setAlertEventPatientName(schedulerApptBookingBean.getSchApptPatientname());
    		alert2.setAlertEventEncounterId(-1);
    		alert2.setAlertEventRefId(schAppt.getSchApptId());
    		alert2.setAlertEventMessage("Appointment Booking from Patient Portal.");
    		alert2.setAlertEventRoomId(-1);
    		alert2.setAlertEventCreatedDate(new Timestamp(new Date().getTime()));
    		alert2.setAlertEventModifiedby(-100);
    		alert2.setAlertEventFrom(-100);
     		alert2.setAlertEventTo(provider);
     		alertEventRepository.saveAndFlush(alert2);
     	 } else {
     		alert1.setAlertEventTo(forwardTo);
     	 }            	 
      }else{
     	 if(forwardTo!=0){
     		alert1.setAlertEventTo(forwardTo);
     	 } else {
     		alert1.setAlertEventTo(provider);
     	}
      }
     }
		
   AlertEvent savedAlert=alertEventRepository.saveAndFlush(alert1);
				
   auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
			AuditTrailEnumConstants.LogActionType.CREATE,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Booked an appointment from Patient Portal",-1,
			request.getRemoteAddr(),schAppt.getSchApptPatientId(),"",
			AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Patient with id "+schAppt.getSchApptPatientId()+"booked an appointment","");
   
		/*returning the booked appointment details*/
		return schAppt;
	}
	
	
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

public Integer getLatestPortalApptRequestId(int patientId) {
	
	CriteriaBuilder builder = em.getCriteriaBuilder();
	CriteriaQuery<Object> cq = builder.createQuery();
	Root<PortalApptRequest> root = cq.from(PortalApptRequest.class);
	cq.select(builder.max(root.get(PortalApptRequest_.portalApptRequestId))).where(builder.equal(root.get(PortalApptRequest_.portalApptRequestPatientId), patientId));
	Integer apptID=(Integer) em.createQuery(cq).getSingleResult();
	
	auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
			AuditTrailEnumConstants.LogActionType.READ,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Requested latest portal appointment request Id",-1,
			request.getRemoteAddr(),patientId,"",
			AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Patient with id "+patientId+" requested latest portal appointment request Id","");
   
	return apptID;
}

public List<BookingConfig> getBookingConfigDetails() {
	List<BookingConfig> BookingConfigDetailsList=BookingConfigRepository.findAll();
	return BookingConfigDetailsList;
}


}
