package com.glenwood.glaceemr.server.application.models;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "h093")
public class H093 {

	@Id
	@Column(name="h093001")
	private Integer h093001;

	@Column(name="h093002")
	private String h093002;

	@Column(name="h093003")
	private Date h093003;

	@Column(name="h093004")
	private Long h093004;

	@Column(name="h093005")
	private String h093005;

	@Column(name="h093006")
	private String h093006;

	@Column(name="h093007")
	private Double h093007;

	@Column(name="h555555")
	private Integer h555555;

	@Column(name="h093008")
	private Long h093008;

	@Column(name="h093009")
	private String h093009;

	@Column(name="h093010")
	private Date h093010;

	@Column(name="h093011")
	private Date h093011;

	@Column(name="h093012")
	private String h093012;

	@Column(name="ens_batch_no")
	private Integer ensBatchNo;

	@Column(name="mailed_status")
	private Integer mailedStatus;
	
	@Column(name="file_path")
	private String filePath;
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumnsOrFormulas({ @JoinColumnOrFormula(formula = @JoinFormula(value = "h093005::integer", referencedColumnName = "h092004")) })
	@JsonManagedReference
	private H092 h092;
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="ens_batch_no", referencedColumnName="batchno", updatable=false, insertable=false)
	@JsonManagedReference
	private EnsBillsDetails ensBillsDetails;
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="h093004", referencedColumnName="patient_registration_id", updatable=false, insertable=false)
	@JsonManagedReference
	private PatientRegistration patientRegistrationDetails;


	public Integer getH093001() {
		return h093001;
	}

	public void setH093001(Integer h093001) {
		this.h093001 = h093001;
	}

	public String getH093002() {
		return h093002;
	}

	public void setH093002(String h093002) {
		this.h093002 = h093002;
	}

	public Date getH093003() {
		return h093003;
	}

	public void setH093003(Date h093003) {
		this.h093003 = h093003;
	}

	public Long getH093004() {
		return h093004;
	}

	public void setH093004(Long h093004) {
		this.h093004 = h093004;
	}

	public String getH093005() {
		return h093005;
	}

	public void setH093005(String h093005) {
		this.h093005 = h093005;
	}

	public String getH093006() {
		return h093006;
	}

	public void setH093006(String h093006) {
		this.h093006 = h093006;
	}

	public Double getH093007() {
		return h093007;
	}

	public void setH093007(Double h093007) {
		this.h093007 = h093007;
	}

	public Integer getH555555() {
		return h555555;
	}

	public void setH555555(Integer h555555) {
		this.h555555 = h555555;
	}

	public Long getH093008() {
		return h093008;
	}

	public void setH093008(Long h093008) {
		this.h093008 = h093008;
	}

	public String getH093009() {
		return h093009;
	}

	public void setH093009(String h093009) {
		this.h093009 = h093009;
	}

	public Date getH093010() {
		return h093010;
	}

	public void setH093010(Date h093010) {
		this.h093010 = h093010;
	}

	public Date getH093011() {
		return h093011;
	}

	public void setH093011(Date h093011) {
		this.h093011 = h093011;
	}

	public String getH093012() {
		return h093012;
	}

	public void setH093012(String h093012) {
		this.h093012 = h093012;
	}

	public Integer getEnsBatchNo() {
		return ensBatchNo;
	}

	public void setEnsBatchNo(Integer ensBatchNo) {
		this.ensBatchNo = ensBatchNo;
	}

	public Integer getMailedStatus() {
		return mailedStatus;
	}

	public void setMailedStatus(Integer mailedStatus) {
		this.mailedStatus = mailedStatus;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public H092 getH092() {
		return h092;
	}

	public void setH092(H092 h092) {
		this.h092 = h092;
	}

	public EnsBillsDetails getEnsBillsDetails() {
		return ensBillsDetails;
	}

	public void setEnsBillsDetails(EnsBillsDetails ensBillsDetails) {
		this.ensBillsDetails = ensBillsDetails;
	}

	public PatientRegistration getPatientRegistrationDetails() {
		return patientRegistrationDetails;
	}

	public void setPatientRegistrationDetails(
			PatientRegistration patientRegistrationDetails) {
		this.patientRegistrationDetails = patientRegistrationDetails;
	}
	
}