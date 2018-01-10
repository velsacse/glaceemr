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
@Table(name = "risk_assessment")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RiskAssessment {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="risk_assessment_id_seq")
	@SequenceGenerator(name ="risk_assessment_id_seq", sequenceName="risk_assessment_id_seq", allocationSize=1)
	@Column(name="risk_assessment_id")
	private Integer riskAssessmentId;

	@Column(name="risk_assessment_patient_id")
	private Integer riskAssessmentPatientId;

	@Column(name="risk_assessment_encounter_id")
	private Integer riskAssessmentEncounterId;

	@Column(name="risk_assessment_description")
	private String riskAssessmentDescription;

	@Column(name="risk_assessment_code")
	private String riskAssessmentCode;

	@Column(name="risk_assessment_code_system")
	private String riskAssessmentCodeSystem;

	@Column(name="risk_assessment_code_system_name")
	private String riskAssessmentCodeSystemName;

	@Column(name="risk_assessment_result_description")
	private String riskAssessmentResultDescription;

	@Column(name="risk_assessment_result_code")
	private String riskAssessmentResultCode;

	@Column(name="risk_assessment_result_code_system")
	private String riskAssessmentResultCodeSystem;

	@Column(name="risk_assessment_result_code_system_name")
	private String riskAssessmentResultCodeSystemName;

	@Column(name="risk_assessment_code_name")
	private String riskAssessmentCodeName;

	@Column(name="risk_assessment_problem_code")
	private String riskAssessmentProblemCode;

	@Column(name="risk_assessment_problem_code_system")
	private String riskAssessmentProblemCodeSystem;

	@Column(name="risk_assessment_problem_code_system_description")
	private String riskAssessmentProblemCodeSystemDescription;

	@Column(name="risk_assessment_status")
	private Integer riskAssessmentStatus;

	@Column(name="risk_assessment_ordered_by")
	private Integer riskAssessmentOrderedBy;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="risk_assessment_ordered_on")
	private Timestamp riskAssessmentOrderedOn;

	@Column(name="risk_assessment_reviewed_by")
	private Integer riskAssessmentReviewedBy;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="risk_assessment_reviewed_on")
	private Timestamp riskAssessmentReviewedOn;

	@Column(name="risk_assessment_performed_by")
	private Integer riskAssessmentPerformedBy;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="risk_assessment_performed_on")
	private Timestamp riskAssessmentPerformedOn;

	@Column(name="risk_assessment_notdone_type")
	private Integer riskAssessmentNotdoneType;

	@Column(name="risk_assessment_notdone_description")
	private String riskAssessmentNotdoneDescription;

	@Column(name="risk_assessment_notdone_code")
	private String riskAssessmentNotdoneCode;

	@Column(name="risk_assessment_notdone_code_system")
	private String riskAssessmentNotdoneCodeSystem;

	@Column(name="risk_assessment_notes")
	private String riskAssessmentNotes;

	@Column(name="risk_assessment_created_by")
	private Integer riskAssessmentCreatedBy;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="risk_assessment_created_on")
	private Timestamp riskAssessmentCreatedOn;

	@Column(name="risk_assessment_modified_by")
	private Integer riskAssessmentModifiedBy;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="risk_assessment_modified_on")
	private Timestamp riskAssessmentModifiedOn;
}