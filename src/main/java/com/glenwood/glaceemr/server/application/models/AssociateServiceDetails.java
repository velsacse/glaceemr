package com.glenwood.glaceemr.server.application.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "associate_service_detail")
public class AssociateServiceDetails {
	
	@Column(name="associate_service_detail_id")
	private Long associateServiceDetailId;

	@Id
	@Column(name="associate_service_detail_service_id")
	private Integer associateServiceDetailServiceId;
	
	@Column(name="associate_service_detail_problem_id")
	private Long associateServiceDetailProblemId;

	@Column(name="associate_service_detail_currentstatus")
	private String associateServiceDetailCurrentstatus;

	@Column(name="associate_service_detail_dosage")
	private String associateServiceDetailDosage;

	@Column(name="associate_service_detail_unknown1")
	private Integer associateServiceDetailUnknown1;

	@Column(name="associate_service_detail_med_notes")
	private String associateServiceDetailMedNotes;

	@Column(name="associate_service_detail_unknown2")
	private String associateServiceDetailUnknown2;

	@Column(name="associate_service_detail_unknown3")
	private Integer associateServiceDetailUnknown3;

	@Column(name="associate_service_detail_unknown4")
	private Integer associateServiceDetailUnknown4;

	@Column(name="associate_service_detail_costplan")
	private Integer associateServiceDetailCostplan;

	@Column(name="associate_service_detail_unknown5")
	private Integer associateServiceDetailUnknown5;

	@Column(name="associate_service_detail_special_dx")
	private String associateServiceDetailSpecialDx;

	@Column(name="associate_service_detail_unknown6")
	private String associateServiceDetailUnknown6;

	@Column(name="associate_service_detail_unknown7")
	private String associateServiceDetailUnknown7;

	@Column(name="associate_service_detail_unknown8")
	private String associateServiceDetailUnknown8;

	@Column(name="h555555")
	private Integer h555555;

	@Column(name="associate_service_detail_unknown9")
	private Long associateServiceDetailUnknown9;
	
	@Column(name="associate_service_detail_third_ins_resubmit")
	private Integer associateServiceDetailThirdInsResubmit;
		
	public Integer getAssociateServiceDetailThirdInsResubmit() {
		return associateServiceDetailThirdInsResubmit;
	}

	public void setAssociateServiceDetailThirdInsResubmit(
			Integer associateServiceDetailThirdInsResubmit) {
		this.associateServiceDetailThirdInsResubmit = associateServiceDetailThirdInsResubmit;
	}

	@JsonInclude(content=Include.NON_NULL)
	@ManyToOne( fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name="associate_service_detail_costplan", referencedColumnName="rate_plan_id" , insertable=false, updatable=false)
	private RatePlan ratePlan;
	
	@JsonInclude(content=Include.NON_NULL)
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name="associate_service_detail_unknown3", referencedColumnName="caseno" , insertable=false, updatable=false)
	private CaseTab caseTab;
	
	public Long getAssociateServiceDetailId() {
		return associateServiceDetailId;
	}

	public void setAssociateServiceDetailId(Long associateServiceDetailId) {
		this.associateServiceDetailId = associateServiceDetailId;
	}

	public Integer getAssociateServiceDetailServiceId() {
		return associateServiceDetailServiceId;
	}

	public void setAssociateServiceDetailServiceId(
			Integer associateServiceDetailServiceId) {
		this.associateServiceDetailServiceId = associateServiceDetailServiceId;
	}

	public Long getAssociateServiceDetailProblemId() {
		return associateServiceDetailProblemId;
	}

	public void setAssociateServiceDetailProblemId(
			Long associateServiceDetailProblemId) {
		this.associateServiceDetailProblemId = associateServiceDetailProblemId;
	}

	public String getAssociateServiceDetailCurrentstatus() {
		return associateServiceDetailCurrentstatus;
	}

	public void setAssociateServiceDetailCurrentstatus(
			String associateServiceDetailCurrentstatus) {
		this.associateServiceDetailCurrentstatus = associateServiceDetailCurrentstatus;
	}

	public String getAssociateServiceDetailDosage() {
		return associateServiceDetailDosage;
	}

	public void setAssociateServiceDetailDosage(String associateServiceDetailDosage) {
		this.associateServiceDetailDosage = associateServiceDetailDosage;
	}

	public Integer getAssociateServiceDetailUnknown1() {
		return associateServiceDetailUnknown1;
	}

	public void setAssociateServiceDetailUnknown1(
			Integer associateServiceDetailUnknown1) {
		this.associateServiceDetailUnknown1 = associateServiceDetailUnknown1;
	}

	public String getAssociateServiceDetailMedNotes() {
		return associateServiceDetailMedNotes;
	}

	public void setAssociateServiceDetailMedNotes(
			String associateServiceDetailMedNotes) {
		this.associateServiceDetailMedNotes = associateServiceDetailMedNotes;
	}

	public String getAssociateServiceDetailUnknown2() {
		return associateServiceDetailUnknown2;
	}

	public void setAssociateServiceDetailUnknown2(
			String associateServiceDetailUnknown2) {
		this.associateServiceDetailUnknown2 = associateServiceDetailUnknown2;
	}

	public Integer getAssociateServiceDetailUnknown3() {
		return associateServiceDetailUnknown3;
	}

	public void setAssociateServiceDetailUnknown3(
			Integer associateServiceDetailUnknown3) {
		this.associateServiceDetailUnknown3 = associateServiceDetailUnknown3;
	}

	public Integer getAssociateServiceDetailUnknown4() {
		return associateServiceDetailUnknown4;
	}

	public void setAssociateServiceDetailUnknown4(
			Integer associateServiceDetailUnknown4) {
		this.associateServiceDetailUnknown4 = associateServiceDetailUnknown4;
	}

	public Integer getAssociateServiceDetailCostplan() {
		return associateServiceDetailCostplan;
	}

	public void setAssociateServiceDetailCostplan(
			Integer associateServiceDetailCostplan) {
		this.associateServiceDetailCostplan = associateServiceDetailCostplan;
	}

	public Integer getAssociateServiceDetailUnknown5() {
		return associateServiceDetailUnknown5;
	}

	public void setAssociateServiceDetailUnknown5(
			Integer associateServiceDetailUnknown5) {
		this.associateServiceDetailUnknown5 = associateServiceDetailUnknown5;
	}

	public String getAssociateServiceDetailSpecialDx() {
		return associateServiceDetailSpecialDx;
	}

	public void setAssociateServiceDetailSpecialDx(
			String associateServiceDetailSpecialDx) {
		this.associateServiceDetailSpecialDx = associateServiceDetailSpecialDx;
	}

	public String getAssociateServiceDetailUnknown6() {
		return associateServiceDetailUnknown6;
	}

	public void setAssociateServiceDetailUnknown6(
			String associateServiceDetailUnknown6) {
		this.associateServiceDetailUnknown6 = associateServiceDetailUnknown6;
	}

	public String getAssociateServiceDetailUnknown7() {
		return associateServiceDetailUnknown7;
	}

	public void setAssociateServiceDetailUnknown7(
			String associateServiceDetailUnknown7) {
		this.associateServiceDetailUnknown7 = associateServiceDetailUnknown7;
	}

	public String getAssociateServiceDetailUnknown8() {
		return associateServiceDetailUnknown8;
	}

	public void setAssociateServiceDetailUnknown8(
			String associateServiceDetailUnknown8) {
		this.associateServiceDetailUnknown8 = associateServiceDetailUnknown8;
	}

	public Integer getH555555() {
		return h555555;
	}

	public void setH555555(Integer h555555) {
		this.h555555 = h555555;
	}

	public Long getAssociateServiceDetailUnknown9() {
		return associateServiceDetailUnknown9;
	}

	public void setAssociateServiceDetailUnknown9(
			Long associateServiceDetailUnknown9) {
		this.associateServiceDetailUnknown9 = associateServiceDetailUnknown9;
	}

	public RatePlan getRatePlan() {
		return ratePlan;
	}

	public void setRatePlan(RatePlan ratePlan) {
		this.ratePlan = ratePlan;
	}

	public CaseTab getCaseTab() {
		return caseTab;
	}

	public void setCaseTab(CaseTab caseTab) {
		this.caseTab = caseTab;
	}
	
	
	
}