package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sch_template_time_mapping")
public class SchedulerTemplateTimeMapping {

	@Id
	@Column(name="sch_template_time_mapping_id")
	private Integer schTemplateTimeMappingId;

	@Column(name="sch_template_time_mapping_template_id")
	private Integer schTemplateTimeMappingTemplateId;

	@Column(name="sch_template_time_mapping_template_detail_id")
	private Integer schTemplateTimeMappingTemplateDetailId;

	public Integer getSchTemplateTimeMappingId() {
		return schTemplateTimeMappingId;
	}

	public void setSchTemplateTimeMappingId(Integer schTemplateTimeMappingId) {
		this.schTemplateTimeMappingId = schTemplateTimeMappingId;
	}

	public Integer getSchTemplateTimeMappingTemplateId() {
		return schTemplateTimeMappingTemplateId;
	}

	public void setSchTemplateTimeMappingTemplateId(
			Integer schTemplateTimeMappingTemplateId) {
		this.schTemplateTimeMappingTemplateId = schTemplateTimeMappingTemplateId;
	}

	public Integer getSchTemplateTimeMappingTemplateDetailId() {
		return schTemplateTimeMappingTemplateDetailId;
	}

	public void setSchTemplateTimeMappingTemplateDetailId(
			Integer schTemplateTimeMappingTemplateDetailId) {
		this.schTemplateTimeMappingTemplateDetailId = schTemplateTimeMappingTemplateDetailId;
	}
	
}
