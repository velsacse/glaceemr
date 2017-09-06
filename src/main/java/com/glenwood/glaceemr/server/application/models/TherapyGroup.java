package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "therapy_group")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TherapyGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="therapy_group_therapy_group_id_seq")
	@SequenceGenerator(name ="therapy_group_therapy_group_id_seq", sequenceName="therapy_group_therapy_group_id_seq", allocationSize=1)
	@Column(name="therapy_group_id")
	private Integer therapyGroupId;

	@Column(name="therapy_group_name")
	private String therapyGroupName;

	@Column(name="therapy_group_description")
	private String therapyGroupDescription;

	@Column(name="therapy_group_provider_id")
	private Integer therapyGroupProviderId;

	@Column(name="therapy_group_pos_id")
	private Integer therapyGroupPosId;

	@Column(name="therapy_group_is_active")
	private Boolean therapyGroupIsActive;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="therapy_group_created_date")
	private Timestamp therapyGroupCreatedDate;

	@Column(name="therapy_group_created_by")
	private Integer therapyGroupCreatedBy;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="therapy_group_modified_date")
	private Timestamp therapyGroupModifiedDate;

	@Column(name="therapy_group_modified_by")
	private Integer therapyGroupModifiedBy;

	@Column(name="therapy_group_defaulttime")
	private String therapyGroupDefaulttime;
	
	@Column(name="therapy_group_dx1")
	private String therapyGroupDx1;

	@Column(name="therapy_group_dx2")
	private String therapyGroupDx2;
	
	@Column(name="therapy_group_dx3")
	private String therapyGroupDx3;
	
	@Column(name="therapy_group_dx4")
	private String therapyGroupDx4;
	
	@Column(name="therapy_group_dx5")
	private String therapyGroupDx5;
	
	@Column(name="therapy_group_dx6")
	private String therapyGroupDx6;
	
	@Column(name="therapy_group_dx7")
	private String therapyGroupDx7;
	
	@Column(name="therapy_group_dx8")
	private String therapyGroupDx8;
	
	@Column(name="therapy_group_dx9")
	private String therapyGroupDx9;
	
	@Column(name="therapy_group_dx10")
	private String therapyGroupDx10;
	
	@Column(name="therapy_group_dx11")
	private String therapyGroupDx11;
	
	@Column(name="therapy_group_dx12")
	private String therapyGroupDx12;
	
	@Column(name="therapy_group_dx1desc")
	private String therapyGroupDx1desc;

	@Column(name="therapy_group_dx2desc")
	private String therapyGroupDx2desc;

	@Column(name="therapy_group_dx3desc")
	private String therapyGroupDx3desc;

	@Column(name="therapy_group_dx4desc")
	private String therapyGroupDx4desc;

	@Column(name="therapy_group_dx5desc")
	private String therapyGroupDx5desc;

	@Column(name="therapy_group_dx6desc")
	private String therapyGroupDx6desc;

	@Column(name="therapy_group_dx7desc")
	private String therapyGroupDx7desc;

	@Column(name="therapy_group_dx8desc")
	private String therapyGroupDx8desc;

	@Column(name="therapy_group_dx9desc")
	private String therapyGroupDx9desc;

	@Column(name="therapy_group_dx10desc")
	private String therapyGroupDx10desc;

	@Column(name="therapy_group_dx11desc")
	private String therapyGroupDx11desc;
	
	@Column(name="therapy_group_dx12desc")
	private String therapyGroupDx12desc;
	
	@Column(name="therapy_group_leader_id")
	private Integer therapyGroupLeaderId;
	
	@Column(name="therapy_group_supervisor_id")
	private Integer therapyGroupSupervisorId;

	public Integer getTherapyGroupId() {
		return therapyGroupId;
	}

	public void setTherapyGroupId(Integer therapyGroupId) {
		this.therapyGroupId = therapyGroupId;
	}

	public String getTherapyGroupName() {
		return therapyGroupName;
	}

	public void setTherapyGroupName(String therapyGroupName) {
		this.therapyGroupName = therapyGroupName;
	}

	public String getTherapyGroupDescription() {
		return therapyGroupDescription;
	}

	public void setTherapyGroupDescription(String therapyGroupDescription) {
		this.therapyGroupDescription = therapyGroupDescription;
	}

	public Integer getTherapyGroupProviderId() {
		return therapyGroupProviderId;
	}

	public void setTherapyGroupProviderId(Integer therapyGroupProviderId) {
		this.therapyGroupProviderId = therapyGroupProviderId;
	}

	public Integer getTherapyGroupPosId() {
		return therapyGroupPosId;
	}

	public void setTherapyGroupPosId(Integer therapyGroupPosId) {
		this.therapyGroupPosId = therapyGroupPosId;
	}

	public Boolean getTherapyGroupIsActive() {
		return therapyGroupIsActive;
	}

	public void setTherapyGroupIsActive(Boolean therapyGroupIsActive) {
		this.therapyGroupIsActive = therapyGroupIsActive;
	}

	public Timestamp getTherapyGroupCreatedDate() {
		return therapyGroupCreatedDate;
	}

	public void setTherapyGroupCreatedDate(Timestamp therapyGroupCreatedDate) {
		this.therapyGroupCreatedDate = therapyGroupCreatedDate;
	}

	public Integer getTherapyGroupCreatedBy() {
		return therapyGroupCreatedBy;
	}

	public void setTherapyGroupCreatedBy(Integer therapyGroupCreatedBy) {
		this.therapyGroupCreatedBy = therapyGroupCreatedBy;
	}

	public Timestamp getTherapyGroupModifiedDate() {
		return therapyGroupModifiedDate;
	}

	public void setTherapyGroupModifiedDate(Timestamp therapyGroupModifiedDate) {
		this.therapyGroupModifiedDate = therapyGroupModifiedDate;
	}

	public Integer getTherapyGroupModifiedBy() {
		return therapyGroupModifiedBy;
	}

	public void setTherapyGroupModifiedBy(Integer therapyGroupModifiedBy) {
		this.therapyGroupModifiedBy = therapyGroupModifiedBy;
	}

	public String getTherapyGroupDefaulttime() {
		return therapyGroupDefaulttime;
	}

	public void setTherapyGroupDefaulttime(String therapyGroupDefaulttime) {
		this.therapyGroupDefaulttime = therapyGroupDefaulttime;
	}

	public String getTherapyGroupDx1() {
		return therapyGroupDx1;
	}

	public void setTherapyGroupDx1(String therapyGroupDx1) {
		this.therapyGroupDx1 = therapyGroupDx1;
	}

	public String getTherapyGroupDx2() {
		return therapyGroupDx2;
	}

	public void setTherapyGroupDx2(String therapyGroupDx2) {
		this.therapyGroupDx2 = therapyGroupDx2;
	}

	public String getTherapyGroupDx3() {
		return therapyGroupDx3;
	}

	public void setTherapyGroupDx3(String therapyGroupDx3) {
		this.therapyGroupDx3 = therapyGroupDx3;
	}

	public String getTherapyGroupDx4() {
		return therapyGroupDx4;
	}

	public void setTherapyGroupDx4(String therapyGroupDx4) {
		this.therapyGroupDx4 = therapyGroupDx4;
	}

	public String getTherapyGroupDx5() {
		return therapyGroupDx5;
	}

	public void setTherapyGroupDx5(String therapyGroupDx5) {
		this.therapyGroupDx5 = therapyGroupDx5;
	}

	public String getTherapyGroupDx6() {
		return therapyGroupDx6;
	}

	public void setTherapyGroupDx6(String therapyGroupDx6) {
		this.therapyGroupDx6 = therapyGroupDx6;
	}

	public String getTherapyGroupDx7() {
		return therapyGroupDx7;
	}

	public void setTherapyGroupDx7(String therapyGroupDx7) {
		this.therapyGroupDx7 = therapyGroupDx7;
	}

	public String getTherapyGroupDx8() {
		return therapyGroupDx8;
	}

	public void setTherapyGroupDx8(String therapyGroupDx8) {
		this.therapyGroupDx8 = therapyGroupDx8;
	}

	public String getTherapyGroupDx9() {
		return therapyGroupDx9;
	}

	public void setTherapyGroupDx9(String therapyGroupDx9) {
		this.therapyGroupDx9 = therapyGroupDx9;
	}

	public String getTherapyGroupDx10() {
		return therapyGroupDx10;
	}

	public void setTherapyGroupDx10(String therapyGroupDx10) {
		this.therapyGroupDx10 = therapyGroupDx10;
	}

	public String getTherapyGroupDx11() {
		return therapyGroupDx11;
	}

	public void setTherapyGroupDx11(String therapyGroupDx11) {
		this.therapyGroupDx11 = therapyGroupDx11;
	}

	public String getTherapyGroupDx12() {
		return therapyGroupDx12;
	}

	public void setTherapyGroupDx12(String therapyGroupDx12) {
		this.therapyGroupDx12 = therapyGroupDx12;
	}

	public String getTherapyGroupDx1desc() {
		return therapyGroupDx1desc;
	}

	public void setTherapyGroupDx1desc(String therapyGroupDx1desc) {
		this.therapyGroupDx1desc = therapyGroupDx1desc;
	}

	public String getTherapyGroupDx2desc() {
		return therapyGroupDx2desc;
	}

	public void setTherapyGroupDx2desc(String therapyGroupDx2desc) {
		this.therapyGroupDx2desc = therapyGroupDx2desc;
	}

	public String getTherapyGroupDx3desc() {
		return therapyGroupDx3desc;
	}

	public void setTherapyGroupDx3desc(String therapyGroupDx3desc) {
		this.therapyGroupDx3desc = therapyGroupDx3desc;
	}

	public String getTherapyGroupDx4desc() {
		return therapyGroupDx4desc;
	}

	public void setTherapyGroupDx4desc(String therapyGroupDx4desc) {
		this.therapyGroupDx4desc = therapyGroupDx4desc;
	}

	public String getTherapyGroupDx5desc() {
		return therapyGroupDx5desc;
	}

	public void setTherapyGroupDx5desc(String therapyGroupDx5desc) {
		this.therapyGroupDx5desc = therapyGroupDx5desc;
	}

	public String getTherapyGroupDx6desc() {
		return therapyGroupDx6desc;
	}

	public void setTherapyGroupDx6desc(String therapyGroupDx6desc) {
		this.therapyGroupDx6desc = therapyGroupDx6desc;
	}

	public String getTherapyGroupDx7desc() {
		return therapyGroupDx7desc;
	}

	public void setTherapyGroupDx7desc(String therapyGroupDx7desc) {
		this.therapyGroupDx7desc = therapyGroupDx7desc;
	}

	public String getTherapyGroupDx8desc() {
		return therapyGroupDx8desc;
	}

	public void setTherapyGroupDx8desc(String therapyGroupDx8desc) {
		this.therapyGroupDx8desc = therapyGroupDx8desc;
	}

	public String getTherapyGroupDx9desc() {
		return therapyGroupDx9desc;
	}

	public void setTherapyGroupDx9desc(String therapyGroupDx9desc) {
		this.therapyGroupDx9desc = therapyGroupDx9desc;
	}

	public String getTherapyGroupDx10desc() {
		return therapyGroupDx10desc;
	}

	public void setTherapyGroupDx10desc(String therapyGroupDx10desc) {
		this.therapyGroupDx10desc = therapyGroupDx10desc;
	}

	public String getTherapyGroupDx11desc() {
		return therapyGroupDx11desc;
	}

	public void setTherapyGroupDx11desc(String therapyGroupDx11desc) {
		this.therapyGroupDx11desc = therapyGroupDx11desc;
	}

	public String getTherapyGroupDx12desc() {
		return therapyGroupDx12desc;
	}

	public void setTherapyGroupDx12desc(String therapyGroupDx12desc) {
		this.therapyGroupDx12desc = therapyGroupDx12desc;
	}

	public Integer getTherapyGroupLeaderId() {
		return therapyGroupLeaderId;
	}

	public void setTherapyGroupLeaderId(Integer therapyGroupLeaderId) {
		this.therapyGroupLeaderId = therapyGroupLeaderId;
	}

	public Integer getTherapyGroupSupervisorId() {
		return therapyGroupSupervisorId;
	}

	public void setTherapyGroupSupervisorId(Integer therapyGroupSupervisorId) {
		this.therapyGroupSupervisorId = therapyGroupSupervisorId;
	}
	
	
	
}