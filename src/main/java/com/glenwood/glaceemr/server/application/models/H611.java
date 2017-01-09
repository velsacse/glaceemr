package com.glenwood.glaceemr.server.application.models;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "h611")
public class H611 implements Serializable{
  
	private static final long serialVersionUID = 1L;

	public H611(Integer h611001, String h611005, String h611006, String h611015, Integer h611016, String h611CodingSystemid){
		this.h611001= h611001;
		this.h611005= h611005;
		this.h611006= h611006;
		this.h611015= h611015;
		this.h611016= h611016;
		this.h611CodingSystemid= h611CodingSystemid;
	}
	
	@Id
	@Column(name="h611001")
	private Integer h611001;

	@Column(name="h611002")
	private Integer h611002;

	@Column(name="h611003")
	private Integer h611003;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h611004")
	private Timestamp h611004;

	@Column(name="h611005")
	private String h611005;

	@Column(name="h611006")
	private String h611006;

	@Column(name="h611007")
	private String h611007;

	@Column(name="h611008")
	private Boolean h611008;

	@Column(name="h555555")
	private Integer h555555;

	@Column(name="h611009")
	private Integer h611009;

	@Column(name="h611010")
	private Integer h611010;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h611011")
	private Timestamp h611011;

	@Column(name="h611012")
	private Integer h611012;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h611013")
	private Timestamp h611013;

	@Column(name="h611014")
	private Integer h611014;

	@Column(name="h611015")
	private String h611015;

	@Column(name="h611016")
	private Integer h611016;

	@Column(name="h611_coding_systemid")
	private String h611CodingSystemid;

	@Column(name="assessment_dxcodesystem")
	private String assessmentDxcodesystem;
	
	@Column(name="plan_notes")
	private String planNotes;

	@ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	@JoinColumn(name="h611_coding_systemid", referencedColumnName="coding_system_oid" , insertable=false, updatable=false)
	private CodingSystems codingsystemsTable;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name="h611002", referencedColumnName="encounter_id" , insertable=false, updatable=false)
	Encounter encounter;
	
	public String getPlanNotes() {
		return planNotes;
	}

	public void setPlanNotes(String planNotes) {
		this.planNotes = planNotes;
	}

	public Integer getH611001() {
		return h611001;
	}

	public Integer getH611002() {
		return h611002;
	}

	public Integer getH611003() {
		return h611003;
	}

	public Timestamp getH611004() {
		return h611004;
	}

	public String getH611005() {
		return h611005;
	}

	public String getH611006() {
		return h611006;
	}

	public String getH611007() {
		return h611007;
	}

	public Boolean getH611008() {
		return h611008;
	}

	public Integer getH555555() {
		return h555555;
	}

	public Integer getH611009() {
		return h611009;
	}

	public Integer getH611010() {
		return h611010;
	}

	public Timestamp getH611011() {
		return h611011;
	}

	public Integer getH611012() {
		return h611012;
	}

	public Timestamp getH611013() {
		return h611013;
	}

	public Integer getH611014() {
		return h611014;
	}

	public String getH611015() {
		return h611015;
	}

	public Integer getH611016() {
		return h611016;
	}

	public String getH611CodingSystemid() {
		return h611CodingSystemid;
	}

	public String getAssessmentDxcodesystem() {
		return assessmentDxcodesystem;
	}

	public void setH611001(Integer h611001) {
		this.h611001 = h611001;
	}

	public void setH611002(Integer h611002) {
		this.h611002 = h611002;
	}

	public void setH611003(Integer h611003) {
		this.h611003 = h611003;
	}

	public void setH611004(Timestamp h611004) {
		this.h611004 = h611004;
	}

	public void setH611005(String h611005) {
		this.h611005 = h611005;
	}

	public void setH611006(String h611006) {
		this.h611006 = h611006;
	}

	public void setH611007(String h611007) {
		this.h611007 = h611007;
	}

	public void setH611008(Boolean h611008) {
		this.h611008 = h611008;
	}

	public void setH555555(Integer h555555) {
		this.h555555 = h555555;
	}

	public void setH611009(Integer h611009) {
		this.h611009 = h611009;
	}

	public void setH611010(Integer h611010) {
		this.h611010 = h611010;
	}

	public void setH611011(Timestamp h611011) {
		this.h611011 = h611011;
	}

	public void setH611012(Integer h611012) {
		this.h611012 = h611012;
	}

	public void setH611013(Timestamp h611013) {
		this.h611013 = h611013;
	}

	public void setH611014(Integer h611014) {
		this.h611014 = h611014;
	}

	public void setH611015(String h611015) {
		this.h611015 = h611015;
	}

	public void setH611016(Integer h611016) {
		this.h611016 = h611016;
	}

	public void setH611CodingSystemid(String h611CodingSystemid) {
		this.h611CodingSystemid = h611CodingSystemid;
	}

	public void setAssessmentDxcodesystem(String assessmentDxcodesystem) {
		this.assessmentDxcodesystem = assessmentDxcodesystem;
	}
	
}