package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "labcompany_details")
public class LabcompanyDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="labcompany_details_labcompany_details_id_seq")
	@SequenceGenerator(name ="labcompany_details_labcompany_details_id_seq", sequenceName="labcompany_details_labcompany_details_id_seq", allocationSize=1)
	@Column(name="labcompany_details_id")
	private Integer labcompanyDetailsId;

	@Column(name="labcompany_details_labname")
	private String labcompanyDetailsLabname;

	@Column(name="labcompany_details_labaddress")
	private String labcompanyDetailsLabaddress;

	@Column(name="labcompany_details_labcity")
	private String labcompanyDetailsLabcity;

	@Column(name="labcompany_details_labstate")
	private String labcompanyDetailsLabstate;

	@Column(name="labcompany_details_labzip")
	private String labcompanyDetailsLabzip;

	@Column(name="labcompany_details_director_name")
	private String labcompanyDetailsDirectorName;

	@Column(name="labcompany_details_clianumber")
	private String labcompanyDetailsClianumber;

	@Column(name="labcompany_details_director_phonenumber")
	private String labcompanyDetailsDirectorPhonenumber;

	@Column(name="labcompany_details_labloccode")
	private String labcompanyDetailsLabloccode;

	@Column(name="labcompany_details_sendingappln")
	private String labcompanyDetailsSendingappln;

	public Integer getLabcompanyDetailsId() {
		return labcompanyDetailsId;
	}

	public void setLabcompanyDetailsId(Integer labcompanyDetailsId) {
		this.labcompanyDetailsId = labcompanyDetailsId;
	}

	public String getLabcompanyDetailsLabname() {
		return labcompanyDetailsLabname;
	}

	public void setLabcompanyDetailsLabname(String labcompanyDetailsLabname) {
		this.labcompanyDetailsLabname = labcompanyDetailsLabname;
	}

	public String getLabcompanyDetailsLabaddress() {
		return labcompanyDetailsLabaddress;
	}

	public void setLabcompanyDetailsLabaddress(String labcompanyDetailsLabaddress) {
		this.labcompanyDetailsLabaddress = labcompanyDetailsLabaddress;
	}

	public String getLabcompanyDetailsLabcity() {
		return labcompanyDetailsLabcity;
	}

	public void setLabcompanyDetailsLabcity(String labcompanyDetailsLabcity) {
		this.labcompanyDetailsLabcity = labcompanyDetailsLabcity;
	}

	public String getLabcompanyDetailsLabstate() {
		return labcompanyDetailsLabstate;
	}

	public void setLabcompanyDetailsLabstate(String labcompanyDetailsLabstate) {
		this.labcompanyDetailsLabstate = labcompanyDetailsLabstate;
	}

	public String getLabcompanyDetailsLabzip() {
		return labcompanyDetailsLabzip;
	}

	public void setLabcompanyDetailsLabzip(String labcompanyDetailsLabzip) {
		this.labcompanyDetailsLabzip = labcompanyDetailsLabzip;
	}

	public String getLabcompanyDetailsDirectorName() {
		return labcompanyDetailsDirectorName;
	}

	public void setLabcompanyDetailsDirectorName(
			String labcompanyDetailsDirectorName) {
		this.labcompanyDetailsDirectorName = labcompanyDetailsDirectorName;
	}

	public String getLabcompanyDetailsClianumber() {
		return labcompanyDetailsClianumber;
	}

	public void setLabcompanyDetailsClianumber(String labcompanyDetailsClianumber) {
		this.labcompanyDetailsClianumber = labcompanyDetailsClianumber;
	}

	public String getLabcompanyDetailsDirectorPhonenumber() {
		return labcompanyDetailsDirectorPhonenumber;
	}

	public void setLabcompanyDetailsDirectorPhonenumber(
			String labcompanyDetailsDirectorPhonenumber) {
		this.labcompanyDetailsDirectorPhonenumber = labcompanyDetailsDirectorPhonenumber;
	}

	public String getLabcompanyDetailsLabloccode() {
		return labcompanyDetailsLabloccode;
	}

	public void setLabcompanyDetailsLabloccode(String labcompanyDetailsLabloccode) {
		this.labcompanyDetailsLabloccode = labcompanyDetailsLabloccode;
	}

	public String getLabcompanyDetailsSendingappln() {
		return labcompanyDetailsSendingappln;
	}

	public void setLabcompanyDetailsSendingappln(
			String labcompanyDetailsSendingappln) {
		this.labcompanyDetailsSendingappln = labcompanyDetailsSendingappln;
	}
}