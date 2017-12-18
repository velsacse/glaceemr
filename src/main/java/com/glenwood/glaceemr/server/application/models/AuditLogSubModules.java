package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "auditlog_sub_modules")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AuditLogSubModules {
	/**
	 * @author Jagadeeswar
	 *	Last Modified by Jagadeeswar
	 */
	
	@Column(name = "module_name")
	private Integer moduleName;
	
	@Id
	@Column(name = "module_id")
	private Integer moduleId;
	
	@Column(name = "module_description")
	private Integer moduleDescription;
	
	@Column(name = "parent_module_id")
	private Integer parentModuleId;

	public Integer getModuleName() {
		return moduleName;
	}

	public void setModuleName(Integer moduleName) {
		this.moduleName = moduleName;
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public Integer getModuleDescription() {
		return moduleDescription;
	}

	public void setModuleDescription(Integer moduleDescription) {
		this.moduleDescription = moduleDescription;
	}

	public Integer getParentModuleId() {
		return parentModuleId;
	}

	public void setParentModuleId(Integer parentModuleId) {
		this.parentModuleId = parentModuleId;
	}

	@Override
	public String toString() {
		return "AuditLogSubModules [moduleName=" + moduleName + ", moduleId=" + moduleId + ", moduleDescription="
				+ moduleDescription + ", parentModuleId=" + parentModuleId + "]";
	}
	
	
}
