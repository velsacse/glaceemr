package com.glenwood.glaceemr.server.application.models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "h496")
public class H496 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="h496_h496001_seq")
	@SequenceGenerator(name ="h496_h496001_seq", sequenceName="h496_h496001_seq", allocationSize=1)
	@Column(name="h496001")
	private Integer h496001;

	@Column(name="h496002")
	private String h496002;

	@Column(name="h496003")
	private String h496003;

	@Column(name="h496004")
	private Integer h496004;

	@Column(name="h496005")
	private Integer h496005;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h496006")
	private Timestamp h496006;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h496007")
	private Timestamp h496007;

	@Column(name="h496008")
	private Integer h496008;

	@Column(name="h496009")
	private String h496009;

	@Column(name="h496010")
	private String h496010;

	@Column(name="h496011")
	private String h496011;

	@Column(name="h496012")
	private Integer h496012;

	@Column(name="h496013")
	private Integer h496013;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h496014")
	private Timestamp h496014;

	@Column(name="h496015")
	private String h496015;

	@Column(name="h496016")
	private Boolean h496016;

	@Column(name="h496017")
	private String h496017;

	@Column(name="h496018")
	private String h496018;

	@Column(name="h496019")
	private int h496019;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h496020")
	private Timestamp h496020;

	@Column(name="h496021")
	private Integer h496021;

	@Column(name="h496022")
	private String h496022;

	@Column(name="h496023_faxbox")
	private Integer h496023Faxbox;

	@Column(name="h496024_associated_patientid")
	private Integer h496024AssociatedPatientid;

	@Column(name="h496_attachment")
	private String h496Attachment;

	@Column(name="h496_noofretries")
	private Integer h496Noofretries;

	@Column(name="h496_rulename")
	private String h496Rulename;

	@Column(name="h496_displaynumber")
	private String h496Displaynumber;

	@Column(name="h496_is_dial_rule_disabled")
	private Boolean h496IsDialRuleDisabled;

	@Column(name="h496_faxpngfiles")
	private String h496Faxpngfiles;

	@Column(name="h496_tracking_no")
	private String h496TrackingNo;

	@Column(name="h496_retries_note")
	private String h496RetriesNote;

	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "h496001", referencedColumnName = "emp_profile_empid", insertable = false, updatable = false)
	private EmployeeProfile empProfile;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "h496004", referencedColumnName = "fax_folder_id", insertable = false, updatable = false)
	private FaxFolder faxFolder;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "h496008", referencedColumnName = "statusid", insertable = false, updatable = false)
	private FaxStatus faxStatus;
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "h496012", referencedColumnName = "emp_profile_empid", insertable = false, updatable = false)
	private EmployeeProfile chart_users_1;
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "h496013", referencedColumnName = "emp_profile_empid", insertable = false, updatable = false)
	private EmployeeProfile chart_users_2;

	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "h496019", referencedColumnName = "emp_profile_empid", insertable = false, updatable = false)
	private EmployeeProfile chart_users_3;
	
	@OneToMany(mappedBy = "h496" , fetch=FetchType.LAZY)
	List<ConsultFaxTracking> consultFaxTrackings;
	
	public List<ConsultFaxTracking> getConsultFaxTrackings() {
		return consultFaxTrackings;
	}

	public void setConsultFaxTrackings(List<ConsultFaxTracking> consultFaxTrackings) {
		this.consultFaxTrackings = consultFaxTrackings;
	}
	
	public Integer getH496001() {
		return h496001;
	}

	public void setH496001(Integer h496001) {
		this.h496001 = h496001;
	}

	public String getH496002() {
		return h496002;
	}

	public void setH496002(String h496002) {
		this.h496002 = h496002;
	}

	public String getH496003() {
		return h496003;
	}

	public void setH496003(String h496003) {
		this.h496003 = h496003;
	}

	public Integer getH496004() {
		return h496004;
	}

	public void setH496004(Integer h496004) {
		this.h496004 = h496004;
	}

	public Integer getH496005() {
		return h496005;
	}

	public void setH496005(Integer h496005) {
		this.h496005 = h496005;
	}

	public Timestamp getH496006() {
		return h496006;
	}

	public void setH496006(Timestamp h496006) {
		this.h496006 = h496006;
	}

	public Timestamp getH496007() {
		return h496007;
	}

	public void setH496007(Timestamp h496007) {
		this.h496007 = h496007;
	}

	public Integer getH496008() {
		return h496008;
	}

	public void setH496008(Integer h496008) {
		this.h496008 = h496008;
	}

	public String getH496009() {
		return h496009;
	}

	public void setH496009(String h496009) {
		this.h496009 = h496009;
	}

	public String getH496010() {
		return h496010;
	}

	public void setH496010(String h496010) {
		this.h496010 = h496010;
	}

	public String getH496011() {
		return h496011;
	}

	public void setH496011(String h496011) {
		this.h496011 = h496011;
	}

	public Integer getH496012() {
		return h496012;
	}

	public void setH496012(Integer h496012) {
		this.h496012 = h496012;
	}

	public Integer getH496013() {
		return h496013;
	}

	public void setH496013(Integer h496013) {
		this.h496013 = h496013;
	}

	public Timestamp getH496014() {
		return h496014;
	}

	public void setH496014(Timestamp h496014) {
		this.h496014 = h496014;
	}

	public String getH496015() {
		return h496015;
	}

	public void setH496015(String h496015) {
		this.h496015 = h496015;
	}

	public Boolean getH496016() {
		return h496016;
	}

	public void setH496016(Boolean h496016) {
		this.h496016 = h496016;
	}

	public String getH496017() {
		return h496017;
	}

	public void setH496017(String h496017) {
		this.h496017 = h496017;
	}

	public String getH496018() {
		return h496018;
	}

	public void setH496018(String h496018) {
		this.h496018 = h496018;
	}

	public int getH496019() {
		return h496019;
	}

	public void setH496019(int h496019) {
		this.h496019 = h496019;
	}

	public Timestamp getH496020() {
		return h496020;
	}

	public void setH496020(Timestamp h496020) {
		this.h496020 = h496020;
	}

	public Integer getH496021() {
		return h496021;
	}

	public void setH496021(Integer h496021) {
		this.h496021 = h496021;
	}

	public String getH496022() {
		return h496022;
	}

	public void setH496022(String h496022) {
		this.h496022 = h496022;
	}

	public Integer getH496023Faxbox() {
		return h496023Faxbox;
	}

	public void setH496023Faxbox(Integer h496023Faxbox) {
		this.h496023Faxbox = h496023Faxbox;
	}

	public Integer getH496024AssociatedPatientid() {
		return h496024AssociatedPatientid;
	}

	public void setH496024AssociatedPatientid(Integer h496024AssociatedPatientid) {
		this.h496024AssociatedPatientid = h496024AssociatedPatientid;
	}

	public String getH496Attachment() {
		return h496Attachment;
	}

	public void setH496Attachment(String h496Attachment) {
		this.h496Attachment = h496Attachment;
	}

	public Integer getH496Noofretries() {
		return h496Noofretries;
	}

	public void setH496Noofretries(Integer h496Noofretries) {
		this.h496Noofretries = h496Noofretries;
	}

	public String getH496Rulename() {
		return h496Rulename;
	}

	public void setH496Rulename(String h496Rulename) {
		this.h496Rulename = h496Rulename;
	}

	public String getH496Displaynumber() {
		return h496Displaynumber;
	}

	public void setH496Displaynumber(String h496Displaynumber) {
		this.h496Displaynumber = h496Displaynumber;
	}

	public Boolean getH496IsDialRuleDisabled() {
		return h496IsDialRuleDisabled;
	}

	public void setH496IsDialRuleDisabled(Boolean h496IsDialRuleDisabled) {
		this.h496IsDialRuleDisabled = h496IsDialRuleDisabled;
	}

	public String getH496Faxpngfiles() {
		return h496Faxpngfiles;
	}

	public void setH496Faxpngfiles(String h496Faxpngfiles) {
		this.h496Faxpngfiles = h496Faxpngfiles;
	}

	public String getH496TrackingNo() {
		return h496TrackingNo;
	}

	public void setH496TrackingNo(String h496TrackingNo) {
		this.h496TrackingNo = h496TrackingNo;
	}

	public String getH496RetriesNote() {
		return h496RetriesNote;
	}

	public void setH496RetriesNote(String h496RetriesNote) {
		this.h496RetriesNote = h496RetriesNote;
	}

	public FaxFolder getFaxFolder() {
		return faxFolder;
	}

	public void setFaxFolder(FaxFolder faxFolder) {
		this.faxFolder = faxFolder;
	}

	public EmployeeProfile getEmpProfile() {
		return empProfile;
	}

	public void setEmpProfile(EmployeeProfile empProfile) {
		this.empProfile = empProfile;
	}

	public EmployeeProfile getChart_users_1() {
		return chart_users_1;
	}

	public void setChart_users_1(EmployeeProfile chart_users_1) {
		this.chart_users_1 = chart_users_1;
	}

	public EmployeeProfile getChart_users_2() {
		return chart_users_2;
	}

	public void setChart_users_2(EmployeeProfile chart_users_2) {
		this.chart_users_2 = chart_users_2;
	}

	public EmployeeProfile getChart_users_3() {
		return chart_users_3;
	}

	public void setChart_users_3(EmployeeProfile chart_users_3) {
		this.chart_users_3 = chart_users_3;
	}

	public FaxStatus getFaxStatus() {
		return faxStatus;
	}

	public void setFaxStatus(FaxStatus faxStatus) {
		this.faxStatus = faxStatus;
	}
}