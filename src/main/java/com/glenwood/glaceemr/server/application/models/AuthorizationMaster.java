package com.glenwood.glaceemr.server.application.models;

import java.io.Serializable;
import java.sql.Timestamp;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "auth_master")
public class AuthorizationMaster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="auth_master_uniqueid")
	private Integer authMasterUniqueid;

	@Column(name="auth_master_patient_id")
	private Integer authMasterPatientId;

	@Column(name="auth_master_official_notes")
	private String authMasterOfficialNotes;

	@Column(name="auth_master_official_notes2")
	private String authMasterOfficialNotes2;

	@Column(name="auth_master_curr_auth_no")
	private Integer authMasterCurrAuthNo;

	@Column(name="auth_master_alert_notes")
	private Boolean authMasterAlertNotes;

	@Column(name="auth_master_recall_notes")
	private String authMasterRecallNotes;

	@Column(name="auth_master_recall_notes2")
	private String authMasterRecallNotes2;

	@Column(name="auth_master_sysalert")
	private String authMasterSysalert;

	@Column(name="auth_master_splalert")
	private String authMasterSplalert;

	@Column(name="h555555")
	private Integer h555555;

	@Column(name="auth_master_splalert2")
	private String authMasterSplalert2;

	@Column(name="auth_master_official_notes_new")
	private String authMasterOfficialNotesNew;

	@Column(name="auth_master_official_notes_date_new")
	private Date authMasterOfficialNotesDateNew;

	@Column(name="auth_master_recall_notes_new")
	private String authMasterRecallNotesNew;

	@Column(name="auth_master_recall_notes_date_new")
	private Date authMasterRecallNotesDateNew;

	@Column(name="auth_master_ptbalance_alert")
	private String authMasterPtbalanceAlert;
}