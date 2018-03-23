package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "reminder_history")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RemainderHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="rem_id")
	@SequenceGenerator(name ="rem_id", sequenceName="rem_id", allocationSize=1)
	@Column(name="reminder_id", nullable=false)
	private Integer reminderId;
	
	@Column(name="reminder_test_id")
	private Integer reminderTestId;

	@Column(name="reminder_user_id")
	private Integer reminderUserId;

	@Column(name="reminder_chart_id")
	private Integer reminderChartId;
	
	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="reminder_created_date")
	private Timestamp reminderCreatedDate;
	
	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="reminder_scheduled_date")
	private Timestamp reminderScheduledDate;
	
	@Column(name="reminder_history_comments")
	private String reminderHistoryComments;
	
	public Integer getReminderId() {
		return reminderId;
	}

	public void setReminderId(Integer reminderId) {
		this.reminderId = reminderId;
	}

	public Integer getReminderTestId() {
		return reminderTestId;
	}

	public void setReminderTestId(Integer reminderTestId) {
		this.reminderTestId = reminderTestId;
	}

	public Integer getReminderUserId() {
		return reminderUserId;
	}

	public void setReminderUserId(Integer reminderUserId) {
		this.reminderUserId = reminderUserId;
	}

	public Integer getReminderChartId() {
		return reminderChartId;
	}

	public void setReminderChartId(Integer reminderChartId) {
		this.reminderChartId = reminderChartId;
	}

	public Timestamp getReminderCreatedDate() {
		return reminderCreatedDate;
	}

	public void setReminderCreatedDate(Timestamp reminderCreatedDate) {
		this.reminderCreatedDate = reminderCreatedDate;
	}

	public String getReminderHistoryComments() {
		return reminderHistoryComments;
	}

	public void setReminderHistoryComments(String reminderHistoryComments) {
		this.reminderHistoryComments = reminderHistoryComments;
	}
	
	public Timestamp getReminderScheduledDate() {
		return reminderScheduledDate;
	}

	public void setReminderScheduledDate(Timestamp reminderScheduledDate) {
		this.reminderScheduledDate = reminderScheduledDate;
	}

}