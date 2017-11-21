package com.glenwood.glaceemr.server.application.services.portal.portalAppointments;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.AppointmentDetailsBean;
import com.glenwood.glaceemr.server.application.models.ApptRequestBean;
import com.glenwood.glaceemr.server.application.models.BookingConfig;
import com.glenwood.glaceemr.server.application.models.PortalApptRequest;
import com.glenwood.glaceemr.server.application.models.PortalSchedulerAppointmentBean;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointment;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointmentBean;
import com.glenwood.glaceemr.server.application.models.SchedulerApptBookingBean;
import com.glenwood.glaceemr.server.application.models.SchedulerLock;
import com.glenwood.glaceemr.server.application.models.SchedulerResource;
import com.glenwood.glaceemr.server.application.models.SchedulerResourceCategory;
import com.glenwood.glaceemr.server.application.models.SchedulerTemplateDetail;

public interface PortalAppointmentsService {

	List<SchedulerAppointment> getPatientFutureAppointmentsList(int patientId, int pageOffset, int pageIndex);

	List<SchedulerAppointment> getPatientPastAppointmentsList(int patientId, int pageOffset, int pageIndex);

	List<SchedulerAppointment> getPatientTodaysAppointmentsList(int patientId, int pageOffset, int pageIndex);

	List<PortalApptRequest> getPortalApptRequestList(int patientId, int pageOffset, int pageIndex);

	AppointmentDetailsBean getAppointmentDetails();

	List<SchedulerResource> getApptBookLocationList();

	List<SchedulerResource> getApptBookDoctorsList(int posId);

	List<SchedulerTemplateDetail> getApptFreeSlotsByProviderIdAndDate(int providerId, Date date);

	List<SchedulerResourceCategory> getSchResourceCategoriesList();

	List<Integer> getSchDateTemplateIdList(int ownerId, Date appDate);

	AlertEvent createPortalAppointmentRequest(ApptRequestBean apptRequestBean) throws ParseException;

	List<SchedulerAppointment> getBookedSlots(int resourceId, Date apptDate) throws JsonProcessingException;

	List<SchedulerLock> getLockedSlots(int resourceId, Date apptDate);

	SchedulerAppointment bookAppointment(SchedulerApptBookingBean schedulerApptBookingBean);

	List<SchedulerAppointment> getPatientTotalAppointmentsList(int patientId, int pageOffset, int pageIndex);

	List<PortalSchedulerAppointmentBean> getPatientAppointments(int patientId, int pageOffset, int pageIndex, String apptType) throws ParseException, JsonProcessingException;	

	List<BookingConfig> getBookingConfigDetails();
	
}
