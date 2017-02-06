package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonDateSerializer;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "therapy_session")
public class TherapySession {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="therapy_session_therapy_session_id_seq")
	@SequenceGenerator(name ="therapy_session_therapy_session_id_seq", sequenceName="therapy_session_therapy_session_id_seq", allocationSize=1)
	@Column(name="therapy_session_id")
	private Integer therapySessionId;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="therapy_session_date")
	private Timestamp therapySessionDate;

	@JsonSerialize(using = JsonDateSerializer.class)
	@Column(name="therapy_session_datevalue")
	private Date therapySessionDateValue;
	
	@Column(name="therapy_session_group_id")
	private Integer therapySessionGroupId;

	@Column(name="therapy_session_provider_id")
	private Integer therapySessionProviderId;

	@Column(name="therapy_session_pos_id")
	private Integer therapySessionPosId;

	@Column(name="therapy_session_status")
	private Integer therapySessionStatus;

	@Column(name="therapy_session_note")
	private String therapySessionNote;

	@Column(name="therapy_session_xml_note")
	private String therapySessionXmlNote;
	
	@Column(name="therapy_session_end_time")
	private String therapySessionEndTime;
	
	@Column(name="therapy_session_topic")
	private String therapySessionTopic;
	
	@Column(name="therapy_session_leader_id")
	private Integer therapySessionLeaderId;

	@Column(name="therapy_session_supervisor_id")
	private Integer therapySessionSupervisorId;
	
	
	public Integer getTherapySessionLeaderId() {
		return therapySessionLeaderId;
	}

	public void setTherapySessionLeaderId(Integer therapySessionLeaderId) {
		this.therapySessionLeaderId = therapySessionLeaderId;
	}

	public Integer getTherapySessionSupervisorId() {
		return therapySessionSupervisorId;
	}

	public void setTherapySessionSupervisorId(Integer therapySessionSupervisorId) {
		this.therapySessionSupervisorId = therapySessionSupervisorId;
	}

	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name="therapy_session_provider_id", referencedColumnName="emp_profile_empid" , insertable=false, updatable=false)
	private EmployeeProfile empProfile;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name="therapy_session_group_id", referencedColumnName="therapy_group_id" , insertable=false, updatable=false)
	private TherapyGroup therapyGroup;

	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name="therapy_session_pos_id", referencedColumnName="pos_table_relation_id" , insertable=false, updatable=false)
	private PosTable posTable;
	

	@OneToMany(mappedBy="therapySession", fetch=FetchType.LAZY)
	@JsonManagedReference
	List<TherapySessionDetails> therapySessionDetails;
	
	

	public List<TherapySessionDetails> getTherapySessionDetails() {
		return therapySessionDetails;
	}

	public void setTherapySessionDetails(
			List<TherapySessionDetails> therapySessionDetails) {
		this.therapySessionDetails = therapySessionDetails;
	}

	public PosTable getPosTable() {
		return posTable;
	}

	public void setPosTable(PosTable posTable) {
		this.posTable = posTable;
	}

	public TherapyGroup getTherapyGroup() {
		return therapyGroup;
	}

	public void setTherapyGroup(TherapyGroup therapyGroup) {
		this.therapyGroup = therapyGroup;
	}

	public EmployeeProfile getEmpProfile() {
		return empProfile;
	}

	public void setEmpProfile(EmployeeProfile empProfile) {
		this.empProfile = empProfile;
	}

	public Integer getTherapySessionId() {
		return therapySessionId;
	}

	public Timestamp getTherapySessionDate() {
		return therapySessionDate;
	}

	public Integer getTherapySessionGroupId() {
		return therapySessionGroupId;
	}

	public Integer getTherapySessionProviderId() {
		return therapySessionProviderId;
	}

	public Integer getTherapySessionPosId() {
		return therapySessionPosId;
	}

	public Integer getTherapySessionStatus() {
		return therapySessionStatus;
	}

	public String getTherapySessionNote() {
		return therapySessionNote;
	}

	public String getTherapySessionXmlNote() {
		return therapySessionXmlNote;
	}

	public void setTherapySessionId(Integer therapySessionId) {
		this.therapySessionId = therapySessionId;
	}

	public void setTherapySessionDate(Timestamp therapySessionDate) {
		this.therapySessionDate = therapySessionDate;
	}

	public void setTherapySessionGroupId(Integer therapySessionGroupId) {
		this.therapySessionGroupId = therapySessionGroupId;
	}

	public void setTherapySessionProviderId(Integer therapySessionProviderId) {
		this.therapySessionProviderId = therapySessionProviderId;
	}

	public void setTherapySessionPosId(Integer therapySessionPosId) {
		this.therapySessionPosId = therapySessionPosId;
	}

	public void setTherapySessionStatus(Integer therapySessionStatus) {
		this.therapySessionStatus = therapySessionStatus;
	}

	public void setTherapySessionNote(String therapySessionNote) {
		this.therapySessionNote = therapySessionNote;
	}

	public void setTherapySessionXmlNote(String therapySessionXmlNote) {
		this.therapySessionXmlNote = therapySessionXmlNote;
	}

	public Date getTherapySessionDateValue() {
		return therapySessionDateValue;
	}

	public void setTherapySessionDateValue(Date therapySessionDateValue) {
		this.therapySessionDateValue = therapySessionDateValue;
	}

	public String getTherapySessionEndTime() {
		return therapySessionEndTime;
	}

	public void setTherapySessionEndTime(String therapySessionEndTime) {
		this.therapySessionEndTime = therapySessionEndTime;
	}
	
	public String getTherapySessionTopic() {
		return therapySessionTopic;
	}

	public void setTherapySessionTopic(String therapySessionTopic) {
		this.therapySessionTopic = therapySessionTopic;
	}
	
 }