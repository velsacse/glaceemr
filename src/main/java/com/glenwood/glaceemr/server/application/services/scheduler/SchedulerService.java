package com.glenwood.glaceemr.server.application.services.scheduler;

import java.util.Date;
import java.util.List;

import com.glenwood.glaceemr.server.application.models.Appointment;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointmentBean;
import com.glenwood.glaceemr.server.application.models.SchedulerResource;
import com.glenwood.glaceemr.server.application.models.SchedulerResourceCategory;

public interface SchedulerService {

	List<SchedulerResource> getResources();

	List<SchedulerAppointmentBean> getAppointments(Date apptDate, Integer[] resourceId);

	List<SchedulerResourceCategory> getResourceCategories();

	String getDefaultResource(String userId);

	Object getTemplates(int userId, Date date);

	Appointment createAppointment(Appointment appointmentDataBean);

	List<Object> getApptBookLocationList();

	List<Object> getApptTypes();
	
	List<Object> getApptStatus();

	List<List<SchedulerAppointmentBean>> getWeekAppointments(Date date,
			Integer[] resourceIds, String viewType);

	Object getWeekTemplates(Integer userId, Date date);
	
	Appointment pasteappointment(Appointment fromJson, Appointment toJson);

	 List<SchedulerAppointmentBean> updateAppointment(Appointment appointment);

	List<SchedulerAppointmentBean> changeAppointmentStatus(Appointment appointment);

	List<SchedulerAppointmentBean> getDetailAppointments(Date date,
			String patientId, String apptId);

	String updateDefaultUsers(String zoomValue, String group, String resource, String defSize, String userId);
	
	List<String> getInitialReportData();
}
