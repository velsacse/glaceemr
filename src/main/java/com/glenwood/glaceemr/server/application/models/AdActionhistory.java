package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "ad_actionhistory")
public class AdActionhistory {

	@Id
	@Column(name="ad_ah_id")
	private Integer adAhId;

	@Column(name="ad_ah_serviceid")
	private Integer adAhServiceid;

	@Column(name="ad_ah_denialid")
	private Integer adAhDenialid;

	@Column(name="ad_ah_servicestatuscode")
	private String adAhServicestatuscode;

	@Column(name="ad_ah_actionid")
	private Integer adAhActionid;

	@Column(name="ad_ah_notes")
	private String adAhNotes;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="ad_ah_actiontakendate")
	private Timestamp adAhActiontakendate;

	@Column(name="ad_ah_actiontakenby")
	private String adAhActiontakenby;

	@Column(name="ad_ah_nextfollowupactionid")
	private Integer adAhNextfollowupactionid;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="ad_ah_nextfollowupactiondate")
	private Timestamp adAhNextfollowupactiondate;

	@Column(name="ad_ah_currentstatus")
	private String adAhCurrentstatus;

	@Column(name="ad_ah_problemid")
	private Integer adAhProblemid;

	@Column(name="ad_ah_arcategoryid")
	private Integer adAhArcategoryid;

	@Column(name="ad_ah_moduleid")
	private Integer adAhModuleid;

	@Column(name="ad_ah_isrecent")
	private Boolean adAhIsrecent;

	@Column(name="ad_ah_actionreason")
	private Integer adAhActionreason;

	@Column(name="ad_ah_actionreference")
	private String adAhActionreference;

	@Column(name="ad_ah_actiondescription")
	private String adAhActiondescription;

	@Column(name="ad_ah_servicestatusid")
	private Integer adAhServicestatusid;

	@Column(name="ad_ah_insuranceid")
	private Integer adAhInsuranceid;

	@Column(name="ad_ah_checkdetails")
	private String adAhCheckdetails;

	@Column(name="ad_ah_actiontrackerid")
	private Integer adAhActiontrackerid;

	@Column(name="ad_ah_path")
	private String adAhPath;

	@Column(name="ad_ah_denialreason")
	private Integer adAhDenialreason;

	@Column(name="ad_ah_denialtype")
	private Integer adAhDenialtype;

	@Column(name="ad_ah_denial_category")
	private Integer adAhDenialCategory;

	@Column(name="ad_ah_faxid")
	private Integer adAhFaxid;

	@Column(name="ad_ah_checkid")
	private String adAhCheckid;

	@Column(name="ad_ah_payment")
	private Double adAhPayment;

	@Column(name="ad_ah_amount")
	private Double adAhAmount;

	@Column(name="ad_ah_copay")
	private Double adAhCopay;

	@Column(name="ad_ah_coins")
	private Double adAhCoins;

	@Column(name="ad_ah_deductible")
	private Double adAhDeductible;

	public Integer getAdAhId() {
		return adAhId;
	}

	public void setAdAhId(Integer adAhId) {
		this.adAhId = adAhId;
	}

	public Integer getAdAhServiceid() {
		return adAhServiceid;
	}

	public void setAdAhServiceid(Integer adAhServiceid) {
		this.adAhServiceid = adAhServiceid;
	}

	public Integer getAdAhDenialid() {
		return adAhDenialid;
	}

	public void setAdAhDenialid(Integer adAhDenialid) {
		this.adAhDenialid = adAhDenialid;
	}

	public String getAdAhServicestatuscode() {
		return adAhServicestatuscode;
	}

	public void setAdAhServicestatuscode(String adAhServicestatuscode) {
		this.adAhServicestatuscode = adAhServicestatuscode;
	}

	public Integer getAdAhActionid() {
		return adAhActionid;
	}

	public void setAdAhActionid(Integer adAhActionid) {
		this.adAhActionid = adAhActionid;
	}

	public String getAdAhNotes() {
		return adAhNotes;
	}

	public void setAdAhNotes(String adAhNotes) {
		this.adAhNotes = adAhNotes;
	}

	public Timestamp getAdAhActiontakendate() {
		return adAhActiontakendate;
	}

	public void setAdAhActiontakendate(Timestamp adAhActiontakendate) {
		this.adAhActiontakendate = adAhActiontakendate;
	}

	public String getAdAhActiontakenby() {
		return adAhActiontakenby;
	}

	public void setAdAhActiontakenby(String adAhActiontakenby) {
		this.adAhActiontakenby = adAhActiontakenby;
	}

	public Integer getAdAhNextfollowupactionid() {
		return adAhNextfollowupactionid;
	}

	public void setAdAhNextfollowupactionid(Integer adAhNextfollowupactionid) {
		this.adAhNextfollowupactionid = adAhNextfollowupactionid;
	}

	public Timestamp getAdAhNextfollowupactiondate() {
		return adAhNextfollowupactiondate;
	}

	public void setAdAhNextfollowupactiondate(Timestamp adAhNextfollowupactiondate) {
		this.adAhNextfollowupactiondate = adAhNextfollowupactiondate;
	}

	public String getAdAhCurrentstatus() {
		return adAhCurrentstatus;
	}

	public void setAdAhCurrentstatus(String adAhCurrentstatus) {
		this.adAhCurrentstatus = adAhCurrentstatus;
	}

	public Integer getAdAhProblemid() {
		return adAhProblemid;
	}

	public void setAdAhProblemid(Integer adAhProblemid) {
		this.adAhProblemid = adAhProblemid;
	}

	public Integer getAdAhArcategoryid() {
		return adAhArcategoryid;
	}

	public void setAdAhArcategoryid(Integer adAhArcategoryid) {
		this.adAhArcategoryid = adAhArcategoryid;
	}

	public Integer getAdAhModuleid() {
		return adAhModuleid;
	}

	public void setAdAhModuleid(Integer adAhModuleid) {
		this.adAhModuleid = adAhModuleid;
	}

	public Boolean getAdAhIsrecent() {
		return adAhIsrecent;
	}

	public void setAdAhIsrecent(Boolean adAhIsrecent) {
		this.adAhIsrecent = adAhIsrecent;
	}

	public Integer getAdAhActionreason() {
		return adAhActionreason;
	}

	public void setAdAhActionreason(Integer adAhActionreason) {
		this.adAhActionreason = adAhActionreason;
	}

	public String getAdAhActionreference() {
		return adAhActionreference;
	}

	public void setAdAhActionreference(String adAhActionreference) {
		this.adAhActionreference = adAhActionreference;
	}

	public String getAdAhActiondescription() {
		return adAhActiondescription;
	}

	public void setAdAhActiondescription(String adAhActiondescription) {
		this.adAhActiondescription = adAhActiondescription;
	}

	public Integer getAdAhServicestatusid() {
		return adAhServicestatusid;
	}

	public void setAdAhServicestatusid(Integer adAhServicestatusid) {
		this.adAhServicestatusid = adAhServicestatusid;
	}

	public Integer getAdAhInsuranceid() {
		return adAhInsuranceid;
	}

	public void setAdAhInsuranceid(Integer adAhInsuranceid) {
		this.adAhInsuranceid = adAhInsuranceid;
	}

	public String getAdAhCheckdetails() {
		return adAhCheckdetails;
	}

	public void setAdAhCheckdetails(String adAhCheckdetails) {
		this.adAhCheckdetails = adAhCheckdetails;
	}

	public Integer getAdAhActiontrackerid() {
		return adAhActiontrackerid;
	}

	public void setAdAhActiontrackerid(Integer adAhActiontrackerid) {
		this.adAhActiontrackerid = adAhActiontrackerid;
	}

	public String getAdAhPath() {
		return adAhPath;
	}

	public void setAdAhPath(String adAhPath) {
		this.adAhPath = adAhPath;
	}

	public Integer getAdAhDenialreason() {
		return adAhDenialreason;
	}

	public void setAdAhDenialreason(Integer adAhDenialreason) {
		this.adAhDenialreason = adAhDenialreason;
	}

	public Integer getAdAhDenialtype() {
		return adAhDenialtype;
	}

	public void setAdAhDenialtype(Integer adAhDenialtype) {
		this.adAhDenialtype = adAhDenialtype;
	}

	public Integer getAdAhDenialCategory() {
		return adAhDenialCategory;
	}

	public void setAdAhDenialCategory(Integer adAhDenialCategory) {
		this.adAhDenialCategory = adAhDenialCategory;
	}

	public Integer getAdAhFaxid() {
		return adAhFaxid;
	}

	public void setAdAhFaxid(Integer adAhFaxid) {
		this.adAhFaxid = adAhFaxid;
	}

	public String getAdAhCheckid() {
		return adAhCheckid;
	}

	public void setAdAhCheckid(String adAhCheckid) {
		this.adAhCheckid = adAhCheckid;
	}

	public Double getAdAhPayment() {
		return adAhPayment;
	}

	public void setAdAhPayment(Double adAhPayment) {
		this.adAhPayment = adAhPayment;
	}

	public Double getAdAhAmount() {
		return adAhAmount;
	}

	public void setAdAhAmount(Double adAhAmount) {
		this.adAhAmount = adAhAmount;
	}

	public Double getAdAhCopay() {
		return adAhCopay;
	}

	public void setAdAhCopay(Double adAhCopay) {
		this.adAhCopay = adAhCopay;
	}

	public Double getAdAhCoins() {
		return adAhCoins;
	}

	public void setAdAhCoins(Double adAhCoins) {
		this.adAhCoins = adAhCoins;
	}

	public Double getAdAhDeductible() {
		return adAhDeductible;
	}

	public void setAdAhDeductible(Double adAhDeductible) {
		this.adAhDeductible = adAhDeductible;
	}
	
}