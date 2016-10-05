package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "h809")
public class H809 {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="h809_h809001_seq")
	@SequenceGenerator(name ="h809_h809001_seq", sequenceName="h809_h809001_seq", allocationSize=1)
	@Column(name="h809001")
	private Integer h809001;

	@Column(name="h809002")
	private Long h809002;

	@Column(name="h809003")
	private Date h809003;

	@Column(name="h809004")
	private String h809004;

	@Column(name="h809005")
	private String h809005;

	@Column(name="h809006")
	private Integer h809006;

	@Column(name="h809007")
	private String h809007;

	@Column(name="h809008")
	private String h809008;

	@Column(name="h809009")
	private Integer h809009;

	@Column(name="h809010")
	private String h809010;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h809011")
	private Timestamp h809011;

	@Column(name="wrong_entry_count")
	private Integer wrongEntryCount;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="access_time")
	private Timestamp accessTime;

	@Column(name="security_question1")
	private String securityQuestion1;

	@Column(name="security_question2")
	private String securityQuestion2;

	@Column(name="security_question3")
	private String securityQuestion3;

	@Column(name="security_answer1")
	private String securityAnswer1;

	@Column(name="security_answer2")
	private String securityAnswer2;

	@Column(name="security_answer3")
	private String securityAnswer3;
	
	@Column(name="password_reset")
	private Integer passwordReset;
	
	@Column(name="from_portal")
	private boolean fromPortal;
	
	@Column(name="from_portal_isactive")
	private boolean fromPortalIsactive;
	
	@Column(name="h809_token")
	private String h809Token;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name="h809002", referencedColumnName="chart_patientid", insertable=false, updatable=false)
	Chart chartH809Table;

	public Integer getH809001() {
		return h809001;
	}

	public void setH809001(Integer h809001) {
		this.h809001 = h809001;
	}

	public Long getH809002() {
		return h809002;
	}

	public void setH809002(Long h809002) {
		this.h809002 = h809002;
	}

	public Date getH809003() {
		return h809003;
	}

	public void setH809003(Date h809003) {
		this.h809003 = h809003;
	}

	public String getH809004() {
		return h809004;
	}

	public void setH809004(String h809004) {
		this.h809004 = h809004;
	}

	public String getH809005() {
		return h809005;
	}

	public void setH809005(String h809005) {
		this.h809005 = h809005;
	}

	public Integer getH809006() {
		return h809006;
	}

	public void setH809006(Integer h809006) {
		this.h809006 = h809006;
	}

	public String getH809007() {
		return h809007;
	}

	public void setH809007(String h809007) {
		this.h809007 = h809007;
	}

	public String getH809008() {
		return h809008;
	}

	public void setH809008(String h809008) {
		this.h809008 = h809008;
	}

	public Integer getH809009() {
		return h809009;
	}

	public void setH809009(Integer h809009) {
		this.h809009 = h809009;
	}

	public String getH809010() {
		return h809010;
	}

	public void setH809010(String h809010) {
		this.h809010 = h809010;
	}

	public Timestamp getH809011() {
		return h809011;
	}

	public void setH809011(Timestamp h809011) {
		this.h809011 = h809011;
	}

	public Integer getWrongEntryCount() {
		return wrongEntryCount;
	}

	public void setWrongEntryCount(Integer wrongEntryCount) {
		this.wrongEntryCount = wrongEntryCount;
	}

	public Timestamp getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Timestamp accessTime) {
		this.accessTime = accessTime;
	}

	public String getSecurityQuestion1() {
		return securityQuestion1;
	}

	public void setSecurityQuestion1(String securityQuestion1) {
		this.securityQuestion1 = securityQuestion1;
	}

	public String getSecurityQuestion2() {
		return securityQuestion2;
	}

	public void setSecurityQuestion2(String securityQuestion2) {
		this.securityQuestion2 = securityQuestion2;
	}

	public String getSecurityQuestion3() {
		return securityQuestion3;
	}

	public void setSecurityQuestion3(String securityQuestion3) {
		this.securityQuestion3 = securityQuestion3;
	}

	public String getSecurityAnswer1() {
		return securityAnswer1;
	}

	public void setSecurityAnswer1(String securityAnswer1) {
		this.securityAnswer1 = securityAnswer1;
	}

	public String getSecurityAnswer2() {
		return securityAnswer2;
	}

	public void setSecurityAnswer2(String securityAnswer2) {
		this.securityAnswer2 = securityAnswer2;
	}

	public String getSecurityAnswer3() {
		return securityAnswer3;
	}

	public void setSecurityAnswer3(String securityAnswer3) {
		this.securityAnswer3 = securityAnswer3;
	}
	public Integer getPasswordReset() {
		return passwordReset;
	}

	public void setPasswordReset(Integer passwordReset) {
		this.passwordReset = passwordReset;
	}

	public boolean isFromPortal() {
		return fromPortal;
	}

	public void setFromPortal(boolean fromPortal) {
		this.fromPortal = fromPortal;
	}

	public boolean isFromPortalIsactive() {
		return fromPortalIsactive;
	}

	public void setFromPortalIsactive(boolean fromPortalIsactive) {
		this.fromPortalIsactive = fromPortalIsactive;
	}

	public String getH809Token() {
		return h809Token;
	}

	public void setH809Token(String h809Token) {
		this.h809Token = h809Token;
	}

	public Chart getChartH809Table() {
		return chartH809Table;
	}

	public void setChartH809Table(
			Chart chartH809Table) {
		this.chartH809Table = chartH809Table;
	}
	
	
	
}