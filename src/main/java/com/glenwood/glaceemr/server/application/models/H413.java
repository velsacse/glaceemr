package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "h413")
public class H413 {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="public.h413_h413001_seq")
	@SequenceGenerator(name = "public.h413_h413001_seq", sequenceName="public.h413_h413001_seq", allocationSize=1)
	@Column(name="h413001")
	private Integer h413001;

	@Column(name="h413002")
	private Integer h413002;

	@Column(name="h413003")
	private Integer h413003;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h413004")
	private Timestamp h413004;

	@Column(name="h413005")
	private String h413005;

	@Column(name="h413006")
	private String h413006;

	@Column(name="h413007")
	private String h413007;

	@Column(name="h413008")
	private String h413008;

	@Column(name="h413009")
	private String h413009;

	@Column(name="h413010")
	private String h413010;

	@Column(name="h413011")
	private String h413011;

	@Column(name="h413012")
	private String h413012;

	@Column(name="h413013")
	private String h413013;

	@Column(name="h413014")
	private Integer h413014;

	@Column(name="h413015")
	private Integer h413015;

	@Column(name="h413016")
	private String h413016;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h413017")
	private Timestamp h413017;

	@Column(name="h413018")
	private String h413018;

	@Column(name="h413019")
	private String h413019;

	@Column(name="h413020")
	private String h413020;

	@Column(name="h413021")
	private Integer h413021;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h413022")
	private Timestamp h413022;

	@Column(name="h413023")
	private String h413023;

	@Column(name="h413024")
	private String h413024;

	@Column(name="h413025")
	private String h413025;

	@Column(name="h413026")
	private Integer h413026;

	@Column(name="h413027")
	private String h413027;

	@Column(name="h413028")
	private Integer h413028;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h413029")
	private Timestamp h413029;

	@Column(name="h413030")
	private Integer h413030;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h413031")
	private Timestamp h413031;

	@Column(name="h413032")
	private String h413032;

	@Column(name="h413033")
	private String h413033;

	@Column(name="h413034")
	private Integer h413034;

	@Column(name="h413035")
	private Integer h413035;

	@Column(name="h413036")
	private Integer h413036;

	@Column(name="h413037")
	private String h413037;

	@Column(name="h413038")
	private String h413038;

	@Column(name="h413039")
	private String h413039;

	@Column(name="h413040")
	private Integer h413040;

	@Column(name="referreddoctor_loginid")
	private Integer referreddoctorLoginid;

	@Column(name="h413041")
	private Integer h413041;

	@Column(name="referral_order_by")
	private Integer referralOrderBy;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="referral_order_on")
	private Timestamp referralOrderOn;

	@Column(name="referral_cancel_by")
	private Integer referralCancelBy;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="referral_cancel_on")
	private Timestamp referralCancelOn;

	@Column(name="referral_receive_by")
	private Integer referralReceiveBy;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="referral_receive_on")
	private Timestamp referralReceiveOn;

	@Column(name="referral_reviewed_by")
	private Integer referralReviewedBy;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="referral_reviewed_on")
	private Timestamp referralReviewedOn;

	@Column(name="referral_hospitalname")
	private String referralHospitalname;

	@Column(name="referral_guarantorname")
	private String referralGuarantorname;

	@Column(name="referral_no_visits")
	private Integer referralNoVisits;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="referral_expiration_date")
	private Timestamp referralExpirationDate;

	@Column(name="referral_referredtoid")
	private Integer referralReferredtoid;

	@Column(name="summary_care_record_provided")
	private Boolean summaryCareRecordProvided;

	@Column(name="summary_care_record_provided_electronic")
	private Integer summaryCareRecordProvidedElectronic;

	@Column(name="referral_critical_status")
	private Integer referralCriticalStatus;

	@Column(name="referral_delinquency_days")
	private Integer referralDelinquencyDays;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="referral_expect_result_date")
	private Timestamp referralExpectResultDate;

	@Column(name="referral_by_id")
	private Integer referralById;

	public Integer getH413001() {
		return h413001;
	}

	public void setH413001(Integer h413001) {
		this.h413001 = h413001;
	}

	public Integer getH413002() {
		return h413002;
	}

	public void setH413002(Integer h413002) {
		this.h413002 = h413002;
	}

	public Integer getH413003() {
		return h413003;
	}

	public void setH413003(Integer h413003) {
		this.h413003 = h413003;
	}

	public Timestamp getH413004() {
		return h413004;
	}

	public void setH413004(Timestamp h413004) {
		this.h413004 = h413004;
	}

	public String getH413005() {
		return h413005;
	}

	public void setH413005(String h413005) {
		this.h413005 = h413005;
	}

	public String getH413006() {
		return h413006;
	}

	public void setH413006(String h413006) {
		this.h413006 = h413006;
	}

	public String getH413007() {
		return h413007;
	}

	public void setH413007(String h413007) {
		this.h413007 = h413007;
	}

	public String getH413008() {
		return h413008;
	}

	public void setH413008(String h413008) {
		this.h413008 = h413008;
	}

	public String getH413009() {
		return h413009;
	}

	public void setH413009(String h413009) {
		this.h413009 = h413009;
	}

	public String getH413010() {
		return h413010;
	}

	public void setH413010(String h413010) {
		this.h413010 = h413010;
	}

	public String getH413011() {
		return h413011;
	}

	public void setH413011(String h413011) {
		this.h413011 = h413011;
	}

	public String getH413012() {
		return h413012;
	}

	public void setH413012(String h413012) {
		this.h413012 = h413012;
	}

	public String getH413013() {
		return h413013;
	}

	public void setH413013(String h413013) {
		this.h413013 = h413013;
	}

	public Integer getH413014() {
		return h413014;
	}

	public void setH413014(Integer h413014) {
		this.h413014 = h413014;
	}

	public Integer getH413015() {
		return h413015;
	}

	public void setH413015(Integer h413015) {
		this.h413015 = h413015;
	}

	public String getH413016() {
		return h413016;
	}

	public void setH413016(String h413016) {
		this.h413016 = h413016;
	}

	public Timestamp getH413017() {
		return h413017;
	}

	public void setH413017(Timestamp h413017) {
		this.h413017 = h413017;
	}

	public String getH413018() {
		return h413018;
	}

	public void setH413018(String h413018) {
		this.h413018 = h413018;
	}

	public String getH413019() {
		return h413019;
	}

	public void setH413019(String h413019) {
		this.h413019 = h413019;
	}

	public String getH413020() {
		return h413020;
	}

	public void setH413020(String h413020) {
		this.h413020 = h413020;
	}

	public Integer getH413021() {
		return h413021;
	}

	public void setH413021(Integer h413021) {
		this.h413021 = h413021;
	}

	public Timestamp getH413022() {
		return h413022;
	}

	public void setH413022(Timestamp h413022) {
		this.h413022 = h413022;
	}

	public String getH413023() {
		return h413023;
	}

	public void setH413023(String h413023) {
		this.h413023 = h413023;
	}

	public String getH413024() {
		return h413024;
	}

	public void setH413024(String h413024) {
		this.h413024 = h413024;
	}

	public String getH413025() {
		return h413025;
	}

	public void setH413025(String h413025) {
		this.h413025 = h413025;
	}

	public Integer getH413026() {
		return h413026;
	}

	public void setH413026(Integer h413026) {
		this.h413026 = h413026;
	}

	public String getH413027() {
		return h413027;
	}

	public void setH413027(String h413027) {
		this.h413027 = h413027;
	}

	public Integer getH413028() {
		return h413028;
	}

	public void setH413028(Integer h413028) {
		this.h413028 = h413028;
	}

	public Timestamp getH413029() {
		return h413029;
	}

	public void setH413029(Timestamp h413029) {
		this.h413029 = h413029;
	}

	public Integer getH413030() {
		return h413030;
	}

	public void setH413030(Integer h413030) {
		this.h413030 = h413030;
	}

	public Timestamp getH413031() {
		return h413031;
	}

	public void setH413031(Timestamp h413031) {
		this.h413031 = h413031;
	}

	public String getH413032() {
		return h413032;
	}

	public void setH413032(String h413032) {
		this.h413032 = h413032;
	}

	public String getH413033() {
		return h413033;
	}

	public void setH413033(String h413033) {
		this.h413033 = h413033;
	}

	public Integer getH413034() {
		return h413034;
	}

	public void setH413034(Integer h413034) {
		this.h413034 = h413034;
	}

	public Integer getH413035() {
		return h413035;
	}

	public void setH413035(Integer h413035) {
		this.h413035 = h413035;
	}

	public Integer getH413036() {
		return h413036;
	}

	public void setH413036(Integer h413036) {
		this.h413036 = h413036;
	}

	public String getH413037() {
		return h413037;
	}

	public void setH413037(String h413037) {
		this.h413037 = h413037;
	}

	public String getH413038() {
		return h413038;
	}

	public void setH413038(String h413038) {
		this.h413038 = h413038;
	}

	public String getH413039() {
		return h413039;
	}

	public void setH413039(String h413039) {
		this.h413039 = h413039;
	}

	public Integer getH413040() {
		return h413040;
	}

	public void setH413040(Integer h413040) {
		this.h413040 = h413040;
	}

	public Integer getReferreddoctorLoginid() {
		return referreddoctorLoginid;
	}

	public void setReferreddoctorLoginid(Integer referreddoctorLoginid) {
		this.referreddoctorLoginid = referreddoctorLoginid;
	}

	public Integer getH413041() {
		return h413041;
	}

	public void setH413041(Integer h413041) {
		this.h413041 = h413041;
	}

	public Integer getReferralOrderBy() {
		return referralOrderBy;
	}

	public void setReferralOrderBy(Integer referralOrderBy) {
		this.referralOrderBy = referralOrderBy;
	}

	public Timestamp getReferralOrderOn() {
		return referralOrderOn;
	}

	public void setReferralOrderOn(Timestamp referralOrderOn) {
		this.referralOrderOn = referralOrderOn;
	}

	public Integer getReferralCancelBy() {
		return referralCancelBy;
	}

	public void setReferralCancelBy(Integer referralCancelBy) {
		this.referralCancelBy = referralCancelBy;
	}

	public Timestamp getReferralCancelOn() {
		return referralCancelOn;
	}

	public void setReferralCancelOn(Timestamp referralCancelOn) {
		this.referralCancelOn = referralCancelOn;
	}

	public Integer getReferralReceiveBy() {
		return referralReceiveBy;
	}

	public void setReferralReceiveBy(Integer referralReceiveBy) {
		this.referralReceiveBy = referralReceiveBy;
	}

	public Timestamp getReferralReceiveOn() {
		return referralReceiveOn;
	}

	public void setReferralReceiveOn(Timestamp referralReceiveOn) {
		this.referralReceiveOn = referralReceiveOn;
	}

	public Integer getReferralReviewedBy() {
		return referralReviewedBy;
	}

	public void setReferralReviewedBy(Integer referralReviewedBy) {
		this.referralReviewedBy = referralReviewedBy;
	}

	public Timestamp getReferralReviewedOn() {
		return referralReviewedOn;
	}

	public void setReferralReviewedOn(Timestamp referralReviewedOn) {
		this.referralReviewedOn = referralReviewedOn;
	}

	public String getReferralHospitalname() {
		return referralHospitalname;
	}

	public void setReferralHospitalname(String referralHospitalname) {
		this.referralHospitalname = referralHospitalname;
	}

	public String getReferralGuarantorname() {
		return referralGuarantorname;
	}

	public void setReferralGuarantorname(String referralGuarantorname) {
		this.referralGuarantorname = referralGuarantorname;
	}

	public Integer getReferralNoVisits() {
		return referralNoVisits;
	}

	public void setReferralNoVisits(Integer referralNoVisits) {
		this.referralNoVisits = referralNoVisits;
	}

	public Timestamp getReferralExpirationDate() {
		return referralExpirationDate;
	}

	public void setReferralExpirationDate(Timestamp referralExpirationDate) {
		this.referralExpirationDate = referralExpirationDate;
	}

	public Integer getReferralReferredtoid() {
		return referralReferredtoid;
	}

	public void setReferralReferredtoid(Integer referralReferredtoid) {
		this.referralReferredtoid = referralReferredtoid;
	}

	public Boolean getSummaryCareRecordProvided() {
		return summaryCareRecordProvided;
	}

	public void setSummaryCareRecordProvided(Boolean summaryCareRecordProvided) {
		this.summaryCareRecordProvided = summaryCareRecordProvided;
	}

	public Integer getSummaryCareRecordProvidedElectronic() {
		return summaryCareRecordProvidedElectronic;
	}

	public void setSummaryCareRecordProvidedElectronic(
			Integer summaryCareRecordProvidedElectronic) {
		this.summaryCareRecordProvidedElectronic = summaryCareRecordProvidedElectronic;
	}

	public Integer getReferralCriticalStatus() {
		return referralCriticalStatus;
	}

	public void setReferralCriticalStatus(Integer referralCriticalStatus) {
		this.referralCriticalStatus = referralCriticalStatus;
	}

	public Integer getReferralDelinquencyDays() {
		return referralDelinquencyDays;
	}

	public void setReferralDelinquencyDays(Integer referralDelinquencyDays) {
		this.referralDelinquencyDays = referralDelinquencyDays;
	}

	public Timestamp getReferralExpectResultDate() {
		return referralExpectResultDate;
	}

	public void setReferralExpectResultDate(Timestamp referralExpectResultDate) {
		this.referralExpectResultDate = referralExpectResultDate;
	}

	public Integer getReferralById() {
		return referralById;
	}

	public void setReferralById(Integer referralById) {
		this.referralById = referralById;
	}
	
}