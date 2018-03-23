package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "sch_user_settings")
public class SchedulerUserSettings {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="sequence")
	@SequenceGenerator(name ="sequence", sequenceName="sch_user_settings_sch_user_settings_id_seq", allocationSize=1)
	
	@Column(name="sch_user_settings_id")
	private Integer schUserSettingsId;

	@Column(name="sch_user_settings_user_id")
	private Integer schUserSettingsUserId;

	@Column(name="sch_user_settings_zoom")
	private Integer schUserSettingsZoom;

	@Column(name="sch_user_settings_issession")
	private Boolean schUserSettingsIsSession;
	
	@Column(name="sch_user_settings_fontsize")
	private String schUserSettingsFontSize;
	
	
	public Integer getSchUserSettingsId() {
		return schUserSettingsId;
	}

	public void setSchUserSettingsId(Integer schUserSettingsId) {
		this.schUserSettingsId = schUserSettingsId;
	}

	public Integer getSchUserSettingsUserId() {
		return schUserSettingsUserId;
	}

	public void setSchUserSettingsUserId(Integer schUserSettingsUserId) {
		this.schUserSettingsUserId = schUserSettingsUserId;
	}

	public Integer getSchUserSettingsZoom() {
		return schUserSettingsZoom;
	}

	public void setSchUserSettingsZoom(Integer schUserSettingsZoom) {
		this.schUserSettingsZoom = schUserSettingsZoom;
	}

	public Boolean getSchUserSettingsIsSession() {
		return schUserSettingsIsSession;
	}

	public void setSchUserSettingsIsSession(Boolean schUserSettingsIsSession) {
		this.schUserSettingsIsSession = schUserSettingsIsSession;
	}

	public String getSchUserSettingsFontSize() {
		return schUserSettingsFontSize;
	}

	public void setSchUserSettingsFontSize(String schUserSettingsFontSize) {
		this.schUserSettingsFontSize = schUserSettingsFontSize;
	}

}
