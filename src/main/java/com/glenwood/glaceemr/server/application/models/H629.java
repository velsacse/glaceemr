package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "h629")
public class H629 {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="h629_h629001_seq")
	@SequenceGenerator(name ="h629_h629001_seq", sequenceName="h629_h629001_seq", allocationSize=1)
	@Column(name="h629001")
	private Integer h629001;

	@Column(name="h629002")
	private String h629002;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h629003")
	private Timestamp h629003;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h629004")
	private Timestamp h629004;

	@Column(name="h629005")
	private Integer h629005;

	@Column(name="h629006")
	private Boolean h629006;

	@Column(name="h629007")
	private String h629007;

	@NotFound(action=NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="h629005", referencedColumnName="emp_profile_empid" , insertable=false, updatable=false)
    EmployeeProfile empProfile;
    
    
	public EmployeeProfile getEmpProfile() {
		return empProfile;
	}

	public void setEmpProfile(EmployeeProfile empProfile) {
		this.empProfile = empProfile;
	}

	public Integer getH629001() {
		return h629001;
	}

	public void setH629001(Integer h629001) {
		this.h629001 = h629001;
	}

	public String getH629002() {
		return h629002;
	}

	public void setH629002(String h629002) {
		this.h629002 = h629002;
	}

	public Timestamp getH629003() {
		return h629003;
	}

	public void setH629003(Timestamp h629003) {
		this.h629003 = h629003;
	}

	public Timestamp getH629004() {
		return h629004;
	}

	public void setH629004(Timestamp h629004) {
		this.h629004 = h629004;
	}

	public Integer getH629005() {
		return h629005;
	}

	public void setH629005(Integer h629005) {
		this.h629005 = h629005;
	}

	public Boolean getH629006() {
		return h629006;
	}

	public void setH629006(Boolean h629006) {
		this.h629006 = h629006;
	}

	public String getH629007() {
		return h629007;
	}

	public void setH629007(String h629007) {
		this.h629007 = h629007;
	}
	
}