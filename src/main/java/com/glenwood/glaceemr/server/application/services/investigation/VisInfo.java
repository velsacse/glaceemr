package com.glenwood.glaceemr.server.application.services.investigation;

import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;



public class VisInfo {
	
	private String cvxVaccineGroupMappingCvxCode;
	
	private String cvxVaccineGroupMappingVaccineGroupCode;
	
	private String cvxVaccineGroupMappingUncertainFormulationCvx;
		
	private Integer visEntriesId;
	
	private String visName;
	
	@JsonSerialize(using = JsonTimestampSerializer.class)
	private Timestamp visPublicationDate;
	
	@JsonSerialize(using = JsonTimestampSerializer.class)
	private Timestamp visPresentDate;
	
	private String visFileMappingFileName;
	
	private Integer visId;
	
	public String getCvxVaccineGroupMappingCvxCode() {
		return cvxVaccineGroupMappingCvxCode;
	}

	public void setCvxVaccineGroupMappingCvxCode(
			String cvxVaccineGroupMappingCvxCode) {
		this.cvxVaccineGroupMappingCvxCode = cvxVaccineGroupMappingCvxCode;
	}

	public String getCvxVaccineGroupMappingVaccineGroupCode() {
		return cvxVaccineGroupMappingVaccineGroupCode;
	}

	public void setCvxVaccineGroupMappingVaccineGroupCode(
			String cvxVaccineGroupMappingVaccineGroupCode) {
		this.cvxVaccineGroupMappingVaccineGroupCode = cvxVaccineGroupMappingVaccineGroupCode;
	}

	public String getCvxVaccineGroupMappingUncertainFormulationCvx() {
		return cvxVaccineGroupMappingUncertainFormulationCvx;
	}

	public void setCvxVaccineGroupMappingUncertainFormulationCvx(
			String cvxVaccineGroupMappingUncertainFormulationCvx) {
		this.cvxVaccineGroupMappingUncertainFormulationCvx = cvxVaccineGroupMappingUncertainFormulationCvx;
	}

	public Integer getVisEntriesId() {
		return visEntriesId;
	}

	public void setVisEntriesId(Integer visEntriesId) {
		this.visEntriesId = visEntriesId;
	}

	public String getVisName() {
		return visName;
	}

	public void setVisName(String visName) {
		this.visName = visName;
	}

	public Timestamp getVisPublicationDate() {
		return visPublicationDate;
	}

	public void setVisPublicationDate(Timestamp visPublicationDate) {
		this.visPublicationDate = visPublicationDate;
	}

	public Timestamp getVisPresentDate() {
		return visPresentDate;
	}

	public void setVisPresentDate(Timestamp visPresentDate) {
		this.visPresentDate = visPresentDate;
	}

	public String getVisFileMappingFileName() {
		return visFileMappingFileName;
	}

	public void setVisFileMappingFileName(String visFileMappingFileName) {
		this.visFileMappingFileName = visFileMappingFileName;
	}

	public Integer getVisId() {
		return visId;
	}

	public void setVisId(Integer visId) {
		this.visId = visId;
	}
}
