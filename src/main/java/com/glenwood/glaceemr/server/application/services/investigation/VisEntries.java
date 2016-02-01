package com.glenwood.glaceemr.server.application.services.investigation;

import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

public class VisEntries {

	private Integer patientVisEntriesId;
	
	private String patientVisEntriesCvx;
	
	@JsonSerialize(using = JsonTimestampSerializer.class)
	private Timestamp patientVisEntriesPresentationDate;
	
	private Integer patientVisEntriesVisId;

	public Integer getPatientVisEntriesId() {
		return patientVisEntriesId;
	}

	public void setPatientVisEntriesId(Integer patientVisEntriesId) {
		this.patientVisEntriesId = patientVisEntriesId;
	}

	public String getPatientVisEntriesCvx() {
		return patientVisEntriesCvx;
	}

	public void setPatientVisEntriesCvx(String patientVisEntriesCvx) {
		this.patientVisEntriesCvx = patientVisEntriesCvx;
	}

	public Timestamp getPatientVisEntriesPresentationDate() {
		return patientVisEntriesPresentationDate;
	}

	public void setPatientVisEntriesPresentationDate(
			Timestamp patientVisEntriesPresentationDate) {
		this.patientVisEntriesPresentationDate = patientVisEntriesPresentationDate;
	}

	public Integer getPatientVisEntriesVisId() {
		return patientVisEntriesVisId;
	}

	public void setPatientVisEntriesVisId(Integer patientVisEntriesVisId) {
		this.patientVisEntriesVisId = patientVisEntriesVisId;
	}
}
