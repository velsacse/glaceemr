package com.glenwood.glaceemr.server.application.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "meds_admin_plan_shortcut")
public class MedsAdminPlanShortcut {

	@Id
	@Column(name="meds_admin_plan_shortcut_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="meds_admin_plan_shortcut_id")
	@SequenceGenerator(name="meds_admin_plan_shortcut_id",sequenceName="meds_admin_plan_shortcut_id",allocationSize=1)
	private Integer medsAdminPlanShortcutId;

	@Column(name="meds_admin_plan_shortcut_name")
	private String medsAdminPlanShortcutName;

	@Column(name="meds_admin_plan_shortcut_desc")
	private String medsAdminPlanShortcutDesc;

	@Column(name="meds_admin_plan_shortcut_is_active")
	private Boolean medsAdminPlanShortcutIsActive;
	
	@Column(name="meds_admin_plan_shortcut_cycles_per_day")
	private Boolean medsAdminPlanShortcutCyclesPerDay;
	
	@OneToMany(mappedBy="medsAdminPlanShortcut", fetch=FetchType.EAGER)
	@JsonManagedReference
	List<MedsAdminPlanShortcutDetails> medsAdminPlanShortcutDetails;
	

	public Integer getMedsAdminPlanShortcutId() {
		return medsAdminPlanShortcutId;
	}

	public void setMedsAdminPlanShortcutId(Integer medsAdminPlanShortcutId) {
		this.medsAdminPlanShortcutId = medsAdminPlanShortcutId;
	}

	public String getMedsAdminPlanShortcutName() {
		return medsAdminPlanShortcutName;
	}

	public void setMedsAdminPlanShortcutName(String medsAdminPlanShortcutName) {
		this.medsAdminPlanShortcutName = medsAdminPlanShortcutName;
	}

	public String getMedsAdminPlanShortcutDesc() {
		return medsAdminPlanShortcutDesc;
	}

	public void setMedsAdminPlanShortcutDesc(String medsAdminPlanShortcutDesc) {
		this.medsAdminPlanShortcutDesc = medsAdminPlanShortcutDesc;
	}

	public Boolean getMedsAdminPlanShortcutIsActive() {
		return medsAdminPlanShortcutIsActive;
	}

	public void setMedsAdminPlanShortcutIsActive(
			Boolean medsAdminPlanShortcutIsActive) {
		this.medsAdminPlanShortcutIsActive = medsAdminPlanShortcutIsActive;
	}
	
	

	public Boolean getMedsAdminPlanShortcutCyclesPerDay() {
		return medsAdminPlanShortcutCyclesPerDay;
	}

	public void setMedsAdminPlanShortcutCyclesPerDay(
			Boolean medsAdminPlanShortcutCyclesPerDay) {
		this.medsAdminPlanShortcutCyclesPerDay = medsAdminPlanShortcutCyclesPerDay;
	}

	public List<MedsAdminPlanShortcutDetails> getMedsAdminPlanShortcutDetails() {
		return medsAdminPlanShortcutDetails;
	}

	public void setMedsAdminPlanShortcutDetails(
			List<MedsAdminPlanShortcutDetails> medsAdminPlanShortcutDetails) {
		this.medsAdminPlanShortcutDetails = medsAdminPlanShortcutDetails;
	}
	
	
}