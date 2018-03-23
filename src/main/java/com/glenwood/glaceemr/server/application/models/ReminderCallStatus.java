package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "reminder_callstatus")
public class ReminderCallStatus {

	@Id
	@Column(name="reminder_callstatus_id")
	private Integer reminderCallStatusId;

	@Column(name="reminder_callstatus_name")
	private String reminderCallStatusName;


	public Integer getReminderCallStatusId() {
		return reminderCallStatusId;
	}

	public void setReminderCallStatusId(Integer reminderCallStatusId) {
		this.reminderCallStatusId = reminderCallStatusId;
	}

	public String getReminderCallStatusName() {
		return reminderCallStatusName;
	}

	public void setReminderCallStatusName(String reminderCallStatusName) {
		this.reminderCallStatusName = reminderCallStatusName;
	}

}
