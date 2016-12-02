package com.glenwood.glaceemr.server.application.services.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.AlertCategory;
import com.glenwood.glaceemr.server.application.models.AlertCategory_;
import com.glenwood.glaceemr.server.application.models.H076;
import com.glenwood.glaceemr.server.application.models.H076_;
import com.glenwood.glaceemr.server.application.models.H113;
import com.glenwood.glaceemr.server.application.models.H113_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointment;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointmentBean;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointmentParameter;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointmentParameter_;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointment_;
import com.glenwood.glaceemr.server.application.models.SchedulerResource;
import com.glenwood.glaceemr.server.application.models.SchedulerResourceCategory;
import com.glenwood.glaceemr.server.application.models.SchedulerResource_;
import com.glenwood.glaceemr.server.application.models.Workflow;
import com.glenwood.glaceemr.server.application.models.Workflow_;
import com.glenwood.glaceemr.server.application.repositories.SchedulerAppointmentRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerResourceCategoryRepository;
import com.glenwood.glaceemr.server.application.repositories.SchedulerResourcesRepository;
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

	@PersistenceContext
	EntityManager em;
	
	@Override
	public List<SchedulerResource> getResources() {
			List<SchedulerResource> schedulerResources=schedulerResourcesRepository.findAll();
		return schedulerResources;
	}

	@Override
	public List<SchedulerResourceCategory> getResourceCategories() {
			List<SchedulerResourceCategory> schedulerResourceCategories=schedulerResourceCategoryRepository.findAll();
		return schedulerResourceCategories;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SchedulerAppointmentBean> getAppointments(Date apptDate, String resourceId) {
		
		    List<Long> list = new ArrayList<Long>();
		    list.add((long)6);
		    list.add((long)12);
		    list.add((long)45);
		    list.add((long)33);
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq = builder.createQuery();
			Root<SchedulerAppointment> root = cq.from(SchedulerAppointment.class);
			Join<SchedulerAppointment, PatientRegistration> joinPatientRegistration=root.join("patRegPatientId",JoinType.LEFT);
			Join<SchedulerAppointment, H113> joinH113Status=root.join("h113ApptStatus",JoinType.LEFT);
			Join<SchedulerAppointment, H113> joinH113Type=root.join("h113ApptType",JoinType.LEFT);
			Join<SchedulerAppointment, SchedulerAppointmentParameter> joinSchApptParam=root.join("schApptParam",JoinType.LEFT);
			//Join<Join<SchedulerAppointment, SchedulerAppointmentParameter>, H113> joinH113Reason=joinSchApptParam.join("h113Reason",JoinType.LEFT);
			Join<SchedulerAppointment, SchedulerResource> joinSchResLoc=root.join("schResLoc",JoinType.LEFT);
			Join<SchedulerAppointment, H076> joinSchRefDrId=root.join("schRefDrId",JoinType.LEFT);
			Join<SchedulerAppointment, Workflow> joinWorkflow=root.join("workflowPatientId",JoinType.LEFT);
			Join<Join<SchedulerAppointment, Workflow>, AlertCategory> joinWorkflowStatusName=joinWorkflow.join("alertCategoryName",JoinType.LEFT);
			
			Predicate[] predicateStatus = new Predicate[] {
					builder.equal(joinH113Status.get(H113_.h113002), new Integer(1)),
					builder.equal(joinH113Status.get(H113_.h113010), new Boolean(true)),
			};
			joinH113Status.on(predicateStatus);
			Predicate[] predicateType = new Predicate[] {
					builder.equal(joinH113Type.get(H113_.h113002), new Integer(2)),
					builder.equal(joinH113Type.get(H113_.h113010), new Boolean(true)),
			};
			joinH113Type.on(predicateType);
			Predicate[] predicateParam = new Predicate[] {
					builder.equal(joinSchApptParam.get(SchedulerAppointmentParameter_.schApptParameterIsactive), new Boolean(true)),
			};
			joinSchApptParam.on(predicateParam);/*
			Predicate[] predicateReason = new Predicate[] {
					builder.equal(joinH113Reason.get(H113_.h113002), new Integer(402)),
					builder.equal(joinH113Reason.get(H113_.h113008), new Integer(1)),
					builder.equal(joinH113Reason.get(H113_.h113010), new Boolean(true)),
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
					joinH113Status.get(H113_.h113004),
					root.get(SchedulerAppointment_.schApptStatus),
					joinH113Status.get(H113_.h113005),
					joinH113Type.get(H113_.h113004),
					root.get(SchedulerAppointment_.schApptType),
					joinSchResLoc.get(SchedulerResource_.schResourceName),
					root.get(SchedulerAppointment_.schApptLocation),
					builder.function("getapptpara", String.class,builder.literal(1),root.get(SchedulerAppointment_.schApptId)),
					root.get(SchedulerAppointment_.schApptReason),
					joinSchRefDrId.get(H076_.h076020),
					root.get(SchedulerAppointment_.schApptReferringdoctorId),
					joinSchRefDrId.get(H076_.h076010),
					joinSchRefDrId.get(H076_.h076011),
					joinWorkflow.get(Workflow_.workflowStatus),
					joinWorkflowStatusName.get(AlertCategory_.alertCategoryDisplayName),
//					builder.function("date", Date.class, joinWorkflow.get(Workflow_.workflowStarttime))
					joinWorkflow.get(Workflow_.workflowStarttime)
					)).where(builder.and(builder.equal(root.get(SchedulerAppointment_.schApptDate),apptDate),
							builder.equal(root.get(SchedulerAppointment_.schApptResource), Integer.parseInt(resourceId)),
							builder.not(root.get(SchedulerAppointment_.h113ApptStatus).in(list))
							)).orderBy(builder.asc(root.get(SchedulerAppointment_.schApptStarttime)));
			Query query=em.createQuery(cq);
			
			List<SchedulerAppointmentBean> schedulerAppointments=query.getResultList();
			
		return schedulerAppointments;
	}
}