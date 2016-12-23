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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "h172")
public class H172 {

	@Id
	@Column(name="h172001")
	private Integer h172001;

	@Column(name="h172002")
	private Integer h172002;

	@Column(name="h172003")
	private Integer h172003;

	@Column(name="h172004")
	private String h172004;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h172005")
	private Timestamp h172005;

	@Column(name="h172006")
	private Date h172006;

	@Column(name="h172007")
	private String h172007;

	@Column(name="h172008")
	private Integer h172008;

	@Column(name="h172009")
	private Integer h172009;

	@Column(name="h172010")
	private String h172010;

	@Column(name="h172011")
	private String h172011;

	@Column(name="h172012")
	private String h172012;

	@Column(name="h172013")
	private String h172013;

	@Column(name="h172014")
	private String h172014;

	@Column(name="h172015")
	private String h172015;

	@Column(name="h172016")
	private String h172016;

	@Column(name="h172017")
	private Double h172017;

	@Column(name="h172018")
	private Double h172018;

	@Column(name="h172019")
	private Integer h172019;

	@Column(name="h172020")
	private Integer h172020;

	@Column(name="h172021")
	private String h172021;

	@Column(name="h172022")
	private String h172022;

	@Column(name="h172023")
	private String h172023;

	@Column(name="h172024")
	private String h172024;

	@Column(name="h172025")
	private String h172025;

	@Column(name="h172026")
	private String h172026;

	@Column(name="h172027")
	private Integer h172027;

	@Column(name="h555555")
	private Integer h555555;

	@Column(name="h172029")
	private String h172029;

	@Column(name="claim_level1_status")
	private String claimLevel1Status;

	@Column(name="claim_level2_status")
	private String claimLevel2Status;

	@Column(name="claim_level3_status")
	private String claimLevel3Status;

	@Column(name="claim_ect_number")
	private String claimEctNumber;

	@Column(name="claim_reference_claimid")
	private String claimReferenceClaimid;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
   	@JsonBackReference
   	@JoinColumn(name="h172003", referencedColumnName="service_detail_id" , insertable=false, updatable=false)
   	private ServiceDetail serviceDetails;

	public Integer getH172001() {
		return h172001;
	}

	public void setH172001(Integer h172001) {
		this.h172001 = h172001;
	}

	public Integer getH172002() {
		return h172002;
	}

	public void setH172002(Integer h172002) {
		this.h172002 = h172002;
	}

	public Integer getH172003() {
		return h172003;
	}

	public void setH172003(Integer h172003) {
		this.h172003 = h172003;
	}

	public String getH172004() {
		return h172004;
	}

	public void setH172004(String h172004) {
		this.h172004 = h172004;
	}

	public Timestamp getH172005() {
		return h172005;
	}

	public void setH172005(Timestamp h172005) {
		this.h172005 = h172005;
	}

	public Date getH172006() {
		return h172006;
	}

	public void setH172006(Date h172006) {
		this.h172006 = h172006;
	}

	public String getH172007() {
		return h172007;
	}

	public void setH172007(String h172007) {
		this.h172007 = h172007;
	}

	public Integer getH172008() {
		return h172008;
	}

	public void setH172008(Integer h172008) {
		this.h172008 = h172008;
	}

	public Integer getH172009() {
		return h172009;
	}

	public void setH172009(Integer h172009) {
		this.h172009 = h172009;
	}

	public String getH172010() {
		return h172010;
	}

	public void setH172010(String h172010) {
		this.h172010 = h172010;
	}

	public String getH172011() {
		return h172011;
	}

	public void setH172011(String h172011) {
		this.h172011 = h172011;
	}

	public String getH172012() {
		return h172012;
	}

	public void setH172012(String h172012) {
		this.h172012 = h172012;
	}

	public String getH172013() {
		return h172013;
	}

	public void setH172013(String h172013) {
		this.h172013 = h172013;
	}

	public String getH172014() {
		return h172014;
	}

	public void setH172014(String h172014) {
		this.h172014 = h172014;
	}

	public String getH172015() {
		return h172015;
	}

	public void setH172015(String h172015) {
		this.h172015 = h172015;
	}

	public String getH172016() {
		return h172016;
	}

	public void setH172016(String h172016) {
		this.h172016 = h172016;
	}

	public Double getH172017() {
		return h172017;
	}

	public void setH172017(Double h172017) {
		this.h172017 = h172017;
	}

	public Double getH172018() {
		return h172018;
	}

	public void setH172018(Double h172018) {
		this.h172018 = h172018;
	}

	public Integer getH172019() {
		return h172019;
	}

	public void setH172019(Integer h172019) {
		this.h172019 = h172019;
	}

	public Integer getH172020() {
		return h172020;
	}

	public void setH172020(Integer h172020) {
		this.h172020 = h172020;
	}

	public String getH172021() {
		return h172021;
	}

	public void setH172021(String h172021) {
		this.h172021 = h172021;
	}

	public String getH172022() {
		return h172022;
	}

	public void setH172022(String h172022) {
		this.h172022 = h172022;
	}

	public String getH172023() {
		return h172023;
	}

	public void setH172023(String h172023) {
		this.h172023 = h172023;
	}

	public String getH172024() {
		return h172024;
	}

	public void setH172024(String h172024) {
		this.h172024 = h172024;
	}

	public String getH172025() {
		return h172025;
	}

	public void setH172025(String h172025) {
		this.h172025 = h172025;
	}

	public String getH172026() {
		return h172026;
	}

	public void setH172026(String h172026) {
		this.h172026 = h172026;
	}

	public Integer getH172027() {
		return h172027;
	}

	public void setH172027(Integer h172027) {
		this.h172027 = h172027;
	}

	public Integer getH555555() {
		return h555555;
	}

	public void setH555555(Integer h555555) {
		this.h555555 = h555555;
	}

	public String getH172029() {
		return h172029;
	}

	public void setH172029(String h172029) {
		this.h172029 = h172029;
	}

	public String getClaimLevel1Status() {
		return claimLevel1Status;
	}

	public void setClaimLevel1Status(String claimLevel1Status) {
		this.claimLevel1Status = claimLevel1Status;
	}

	public String getClaimLevel2Status() {
		return claimLevel2Status;
	}

	public void setClaimLevel2Status(String claimLevel2Status) {
		this.claimLevel2Status = claimLevel2Status;
	}

	public String getClaimLevel3Status() {
		return claimLevel3Status;
	}

	public void setClaimLevel3Status(String claimLevel3Status) {
		this.claimLevel3Status = claimLevel3Status;
	}

	public String getClaimEctNumber() {
		return claimEctNumber;
	}

	public void setClaimEctNumber(String claimEctNumber) {
		this.claimEctNumber = claimEctNumber;
	}

	public String getClaimReferenceClaimid() {
		return claimReferenceClaimid;
	}

	public void setClaimReferenceClaimid(String claimReferenceClaimid) {
		this.claimReferenceClaimid = claimReferenceClaimid;
	}
}