package com.glenwood.glaceemr.server.application.models;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "reminder_detail")
public class ReminderDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reminder_detail_reminder_detail_id_seq")
	@SequenceGenerator(name = "reminder_detail_reminder_detail_id_seq", sequenceName = "reminder_detail_reminder_detail_id_seq", allocationSize = 1)
	@Column(name="reminder_detail_id")
	private Integer reminderDetailId;

	@Column(name="reminder_detail_timeslot_id")
	private Integer reminderTimeSlotId;

	@Column(name="reminder_detail_app_date")
	private Date reminderAppDate;

	@Column(name="reminder_detail_app_time")
	private Timestamp reminderAppTime;

	@Column(name="reminder_detail_app_id")
	private Integer reminderApptId;

	@Column(name="reminder_detail_provider_id")
	private String reminderProviderId;

	@Column(name="reminder_detail_provider_name")
	private String reminderDetailProviderName;
	
	@Column(name="reminder_detail_patient_id")
	private Integer reminderDetailPatientId;
	
	@Column(name="reminder_detail_patient_name")
	private String reminderDetailPatientName;

	@Column(name="reminder_detail_phone_no")
	private String reminderDetailPhoneNo;

	@Column(name="reminder_detail_phone_no2")
	private String reminderDetailPhoneNo2;

	@Column(name="reminder_detail_reminder_type")
	private Integer reminderDetailRemiderType;
	
	@Column(name="reminder_detail_reminder_status")
	private Integer reminderDetailReminderStatus;
	
	@Column(name="reminder_detail_reminded_date")
	private Timestamp reminderDetailRemindedDate;

	@Column(name="reminder_detail_attempts_made")
	private Integer reminderDetailAttemptsMade;
	
	@Column(name="reminder_detail_max_attempts")
	private Integer reminderDetailMaxAttempts;
		
	@Column(name="reminder_detail_placed_date")
	private Timestamp reminderDetailPlacedDate;
	
	@Column(name="reminder_detail_reference_id1")
	private String reminderDetailReferenceId1;
	
	@Column(name="reminder_detail_reference_id2")
	private String reminderDetailReferenceId2;
	
	@Column(name="reminder_detail_reference_id3")
	private String reminderDetailReferenceId3;
	
	@Column(name="reminder_detail_reference_id4")
	private String reminderDetailReferenceId4;
	
	@Column(name="reminder_detail_reference_id5")
	private String reminderDetailReferenceId5;
	
	@Column(name="reminder_detail_ismultiple")
	private Boolean reminderDetailIsMultiple;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="reminder_detail_patient_id", referencedColumnName="patient_registration_id" , insertable=false, updatable=false)
	PatientRegistration patientRegistration3;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name="reminder_detail_reminder_status", referencedColumnName="reminder_callstatus_id" , insertable=false, updatable=false)
	private ReminderCallStatus reminderCallStatus;
	
	public Integer getReminderDetailId() {
		return reminderDetailId;
	}

	public void setReminderDetailId(Integer reminderDetailId) {
		this.reminderDetailId = reminderDetailId;
	}

	public Integer getReminderTimeSlotId() {
		return reminderTimeSlotId;
	}

	public void setReminderTimeSlotId(Integer reminderTimeSlotId) {
		this.reminderTimeSlotId = reminderTimeSlotId;
	}

	public Date getReminderAppDate() {
		return reminderAppDate;
	}

	public void setReminderAppDate(Date reminderAppDate) {
		this.reminderAppDate = reminderAppDate;
	}

	public Timestamp getReminderAppTime() {
		return reminderAppTime;
	}

	public void setReminderAppTime(Timestamp reminderAppTime) {
		this.reminderAppTime = reminderAppTime;
	}

	public Integer getReminderApptId() {
		return reminderApptId;
	}

	public void setReminderApptId(Integer reminderApptId) {
		this.reminderApptId = reminderApptId;
	}

	public String getReminderProviderId() {
		return reminderProviderId;
	}

	public void setReminderProviderId(String reminderProviderId) {
		this.reminderProviderId = reminderProviderId;
	}

	public String getReminderDetailProviderName() {
		return reminderDetailProviderName;
	}

	public void setReminderDetailProviderName(String reminderDetailProviderName) {
		this.reminderDetailProviderName = reminderDetailProviderName;
	}

	public Integer getReminderDetailPatientId() {
		return reminderDetailPatientId;
	}

	public void setReminderDetailPatientId(Integer reminderDetailPatientId) {
		this.reminderDetailPatientId = reminderDetailPatientId;
	}

	public String getReminderDetailPatientName() {
		return reminderDetailPatientName;
	}

	public void setReminderDetailPatientName(String reminderDetailPatientName) {
		this.reminderDetailPatientName = reminderDetailPatientName;
	}

	public String getReminderDetailPhoneNo() {
		return reminderDetailPhoneNo;
	}

	public void setReminderDetailPhoneNo(String reminderDetailPhoneNo) {
		this.reminderDetailPhoneNo = reminderDetailPhoneNo;
	}

	public String getReminderDetailPhoneNo2() {
		return reminderDetailPhoneNo2;
	}

	public void setReminderDetailPhoneNo2(String reminderDetailPhoneNo2) {
		this.reminderDetailPhoneNo2 = reminderDetailPhoneNo2;
	}

	public Integer getReminderDetailRemiderType() {
		return reminderDetailRemiderType;
	}

	public void setReminderDetailRemiderType(Integer reminderDetailRemiderType) {
		this.reminderDetailRemiderType = reminderDetailRemiderType;
	}

	public Integer getReminderDetailReminderStatus() {
		return reminderDetailReminderStatus;
	}

	public void setReminderDetailReminderStatus(Integer reminderDetailReminderStatus) {
		this.reminderDetailReminderStatus = reminderDetailReminderStatus;
	}

	public Timestamp getReminderDetailRemindedDate() {
		return reminderDetailRemindedDate;
	}

	public void setReminderDetailRemindedDate(Timestamp reminderDetailRemindedDate) {
		this.reminderDetailRemindedDate = reminderDetailRemindedDate;
	}

	public Integer getReminderDetailAttemptsMade() {
		return reminderDetailAttemptsMade;
	}

	public void setReminderDetailAttemptsMade(Integer reminderDetailAttemptsMade) {
		this.reminderDetailAttemptsMade = reminderDetailAttemptsMade;
	}

	public Integer getReminderDetailMaxAttempts() {
		return reminderDetailMaxAttempts;
	}

	public void setReminderDetailMaxAttempts(Integer reminderDetailMaxAttempts) {
		this.reminderDetailMaxAttempts = reminderDetailMaxAttempts;
	}

	public Timestamp getReminderDetailPlacedDate() {
		return reminderDetailPlacedDate;
	}

	public void setReminderDetailPlacedDate(Timestamp reminderDetailPlacedDate) {
		this.reminderDetailPlacedDate = reminderDetailPlacedDate;
	}

	public String getReminderDetailReferenceId1() {
		return reminderDetailReferenceId1;
	}

	public void setReminderDetailReferenceId1(String reminderDetailReferenceId1) {
		this.reminderDetailReferenceId1 = reminderDetailReferenceId1;
	}

	public String getReminderDetailReferenceId2() {
		return reminderDetailReferenceId2;
	}

	public void setReminderDetailReferenceId2(String reminderDetailReferenceId2) {
		this.reminderDetailReferenceId2 = reminderDetailReferenceId2;
	}

	public String getReminderDetailReferenceId3() {
		return reminderDetailReferenceId3;
	}

	public void setReminderDetailReferenceId3(String reminderDetailReferenceId3) {
		this.reminderDetailReferenceId3 = reminderDetailReferenceId3;
	}

	public String getReminderDetailReferenceId4() {
		return reminderDetailReferenceId4;
	}

	public void setReminderDetailReferenceId4(String reminderDetailReferenceId4) {
		this.reminderDetailReferenceId4 = reminderDetailReferenceId4;
	}

	public String getReminderDetailReferenceId5() {
		return reminderDetailReferenceId5;
	}

	public void setReminderDetailReferenceId5(String reminderDetailReferenceId5) {
		this.reminderDetailReferenceId5 = reminderDetailReferenceId5;
	}

	public Boolean getReminderDetailIsMultiple() {
		return reminderDetailIsMultiple;
	}

	public void setReminderDetailIsMultiple(Boolean reminderDetailIsMultiple) {
		this.reminderDetailIsMultiple = reminderDetailIsMultiple;
	}

	

}
