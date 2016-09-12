package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sch_resource")
public class SchedulerResource {

	@Id
	@Column(name="sch_resource_id")
	private Integer schResourceId;

	@Column(name="sch_resource_doctor_id")
	private Integer schResourceDoctorId;

	@Column(name="sch_resource_name")
	private String schResourceName;

	@Column(name="sch_resource_isdoctor")
	private Integer schResourceIsdoctor;

	@Column(name="sch_resource_connectionstring")
	private String schResourceConnectionstring;

	@Column(name="sch_resource_localserver")
	private Boolean schResourceLocalserver;

	@Column(name="sch_resource_order")
	private Integer schResourceOrder;

	@Column(name="sch_resource_fullname")
	private String schResourceFullname;

	@Column(name="sch_resource_isdoublebook")
	private Boolean schResourceIsdoublebook;

	@Column(name="sch_resource_isexport")
	private Boolean schResourceIsexport;

	public Integer getSchResourceId() {
		return schResourceId;
	}

	public void setSchResourceId(Integer schResourceId) {
		this.schResourceId = schResourceId;
	}

	public Integer getSchResourceDoctorId() {
		return schResourceDoctorId;
	}

	public void setSchResourceDoctorId(Integer schResourceDoctorId) {
		this.schResourceDoctorId = schResourceDoctorId;
	}

	public String getSchResourceName() {
		return schResourceName;
	}

	public void setSchResourceName(String schResourceName) {
		this.schResourceName = schResourceName;
	}

	public Integer getSchResourceIsdoctor() {
		return schResourceIsdoctor;
	}

	public void setSchResourceIsdoctor(Integer schResourceIsdoctor) {
		this.schResourceIsdoctor = schResourceIsdoctor;
	}

	public String getSchResourceConnectionstring() {
		return schResourceConnectionstring;
	}

	public void setSchResourceConnectionstring(String schResourceConnectionstring) {
		this.schResourceConnectionstring = schResourceConnectionstring;
	}

	public Boolean getSchResourceLocalserver() {
		return schResourceLocalserver;
	}

	public void setSchResourceLocalserver(Boolean schResourceLocalserver) {
		this.schResourceLocalserver = schResourceLocalserver;
	}

	public Integer getSchResourceOrder() {
		return schResourceOrder;
	}

	public void setSchResourceOrder(Integer schResourceOrder) {
		this.schResourceOrder = schResourceOrder;
	}

	public String getSchResourceFullname() {
		return schResourceFullname;
	}

	public void setSchResourceFullname(String schResourceFullname) {
		this.schResourceFullname = schResourceFullname;
	}

	public Boolean getSchResourceIsdoublebook() {
		return schResourceIsdoublebook;
	}

	public void setSchResourceIsdoublebook(Boolean schResourceIsdoublebook) {
		this.schResourceIsdoublebook = schResourceIsdoublebook;
	}

	public Boolean getSchResourceIsexport() {
		return schResourceIsexport;
	}

	public void setSchResourceIsexport(Boolean schResourceIsexport) {
		this.schResourceIsexport = schResourceIsexport;
	}
}