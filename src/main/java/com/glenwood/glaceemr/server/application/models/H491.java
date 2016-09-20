package com.glenwood.glaceemr.server.application.models;

import java.io.Serializable;
import java.sql.Timestamp;
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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "h491")
public class H491 implements Serializable {
	/**
	 * 
	 */
	private static long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "sequence", sequenceName = "h491_h491001_seq")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence")
	@Column(name="h491001")
	private Integer h491001;

	@Column(name="h491002")
	private String h491002;

	@Column(name="h491003")
	private String h491003;

	@Column(name="h491004")
	private String h491004;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h491005")
	private Timestamp h491005;

	@Column(name="h491006")
	private String h491006;

	@Column(name="h491007")
	private String h491007;

	@Column(name="h491008")
	private String h491008;

	@Column(name="h491009")
	private String h491009;

	@Column(name="h491010")
	private Integer h491010;

	@Column(name="h491011")
	private String h491011;

	@Column(name="h491012")
	private Integer h491012;

	@Column(name="h491013")
	private Integer h491013;

	@Column(name="h491014")
	private Integer h491014;

	@Column(name="h491015")
	private Integer h491015;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h491016")
	private Timestamp h491016;

	@Column(name="h491017_faxbox")
	private Integer h491017Faxbox;

	@Column(name="h491018_isenabled")
	private Boolean h491018Isenabled;

	@Column(name="h491019_callerid")
	private String h491019Callerid;

	@Column(name="h491020_faxnotes")
	private String h491020Faxnotes;

	@Column(name="h491021_metrofax_id")
	private String h491021MetrofaxId;

	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "h491010", referencedColumnName = "fax_folder_id", insertable = false, updatable = false)
	public FaxFolder faxFolder;

	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "h491012", referencedColumnName = "fax_type_id", insertable = false, updatable = false)
	private FaxType faxType;

	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "h491013", referencedColumnName = "statusid", insertable = false, updatable = false)
	private InFaxStatus faxStatus;

	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "h491014", referencedColumnName = "emp_profile_empid", insertable = false, updatable = false)
	private EmployeeProfile  emplopyeeProfile;

	@ManyToOne(fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name = "h491015", referencedColumnName = "emp_profile_empid", insertable = false, updatable = false)
	private EmployeeProfile  emplopyeeProfile1;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}

	public FaxFolder getFaxFolder() {
		return faxFolder;
	}

	public void setFaxFolder(FaxFolder faxFolder) {
		this.faxFolder = faxFolder;
	}

	public FaxType getFaxType() {
		return faxType;
	}

	public void setFaxType(FaxType faxType) {
		this.faxType = faxType;
	}

	public InFaxStatus getFaxStatus() {
		return faxStatus;
	}

	public void setFaxStatus(InFaxStatus faxStatus) {
		this.faxStatus = faxStatus;
	}

	public EmployeeProfile getEmplopyeeProfile() {
		return emplopyeeProfile;
	}

	public void setEmplopyeeProfile(EmployeeProfile emplopyeeProfile) {
		this.emplopyeeProfile = emplopyeeProfile;
	}

	public EmployeeProfile getEmplopyeeProfile1() {
		return emplopyeeProfile1;
	}

	public void setEmplopyeeProfile1(EmployeeProfile emplopyeeProfile1) {
		this.emplopyeeProfile1 = emplopyeeProfile1;
	}

	public Integer getH491001() {
		return h491001;
	}

	public void setH491001(Integer h491001) {
		this.h491001 = h491001;
	}

	public String getH491002() {
		return h491002;
	}

	public void setH491002(String h491002) {
		this.h491002 = h491002;
	}

	public String getH491003() {
		return h491003;
	}

	public void setH491003(String h491003) {
		this.h491003 = h491003;
	}

	public String getH491004() {
		return h491004;
	}

	public void setH491004(String h491004) {
		this.h491004 = h491004;
	}

	public Timestamp getH491005() {
		return h491005;
	}

	public void setH491005(Timestamp h491005) {
		this.h491005 = h491005;
	}

	public String getH491006() {
		return h491006;
	}

	public void setH491006(String h491006) {
		this.h491006 = h491006;
	}

	public String getH491007() {
		return h491007;
	}

	public void setH491007(String h491007) {
		this.h491007 = h491007;
	}

	public String getH491008() {
		return h491008;
	}

	public void setH491008(String h491008) {
		this.h491008 = h491008;
	}

	public String getH491009() {
		return h491009;
	}

	public void setH491009(String h491009) {
		this.h491009 = h491009;
	}

	public Integer getH491010() {
		return h491010;
	}

	public void setH491010(Integer h491010) {
		this.h491010 = h491010;
	}

	public String getH491011() {
		return h491011;
	}

	public void setH491011(String h491011) {
		this.h491011 = h491011;
	}

	public Integer getH491012() {
		return h491012;
	}

	public void setH491012(Integer h491012) {
		this.h491012 = h491012;
	}

	public Integer getH491013() {
		return h491013;
	}

	public void setH491013(Integer h491013) {
		this.h491013 = h491013;
	}

	public Integer getH491014() {
		return h491014;
	}

	public void setH491014(Integer h491014) {
		this.h491014 = h491014;
	}

	public Integer getH491015() {
		return h491015;
	}

	public void setH491015(Integer h491015) {
		this.h491015 = h491015;
	}

	public Timestamp getH491016() {
		return h491016;
	}

	public void setH491016(Timestamp h491016) {
		this.h491016 = h491016;
	}

	public Integer getH491017Faxbox() {
		return h491017Faxbox;
	}

	public void setH491017Faxbox(Integer h491017Faxbox) {
		this.h491017Faxbox = h491017Faxbox;
	}

	public Boolean getH491018Isenabled() {
		return h491018Isenabled;
	}

	public void setH491018Isenabled(Boolean h491018Isenabled) {
		this.h491018Isenabled = h491018Isenabled;
	}

	public String getH491019Callerid() {
		return h491019Callerid;
	}

	public void setH491019Callerid(String h491019Callerid) {
		this.h491019Callerid = h491019Callerid;
	}

	public String getH491020Faxnotes() {
		return h491020Faxnotes;
	}

	public void setH491020Faxnotes(String h491020Faxnotes) {
		this.h491020Faxnotes = h491020Faxnotes;
	}

	public String getH491021MetrofaxId() {
		return h491021MetrofaxId;
	}

	public void setH491021MetrofaxId(String h491021MetrofaxId) {
		this.h491021MetrofaxId = h491021MetrofaxId;
	}

}