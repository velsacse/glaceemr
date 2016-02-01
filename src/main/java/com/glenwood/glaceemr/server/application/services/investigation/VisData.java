package com.glenwood.glaceemr.server.application.services.investigation;

import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

public class VisData {

	private Integer visEntriesId;
	
	private String cvxMapCode;
	
	private String visName;
	
	private String groupName;
	
	private String groupCode;
	
	@JsonSerialize( using = JsonTimestampSerializer.class)
	private Timestamp visPubDate;
	
	@JsonSerialize( using = JsonTimestampSerializer.class)
	private Timestamp visPrsntDate;
	
	private String visFileName;
	
	private Integer visPatientEntiresId;

	public Integer getVisEntriesId() {
		return visEntriesId;
	}

	public void setVisEntriesId(Integer visEntriesId) {
		this.visEntriesId = visEntriesId;
	}

	public String getCvxMapCode() {
		return cvxMapCode;
	}

	public void setCvxMapCode(String cvxMapCode) {
		this.cvxMapCode = cvxMapCode;
	}

	public String getVisName() {
		return visName;
	}

	public void setVisName(String visName) {
		this.visName = visName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public Timestamp getVisPubDate() {
		return visPubDate;
	}

	public void setVisPubDate(Timestamp visPubDate) {
		this.visPubDate = visPubDate;
	}

	public Timestamp getVisPrsntDate() {
		return visPrsntDate;
	}

	public void setVisPrsntDate(Timestamp visPrsntDate) {
		this.visPrsntDate = visPrsntDate;
	}

	public String getVisFileName() {
		return visFileName;
	}

	public void setVisFileName(String visFileName) {
		this.visFileName = visFileName;
	}

	public Integer getVisPatientEntiresId() {
		return visPatientEntiresId;
	}

	public void setVisPatientEntiresId(Integer visPatientEntiresId) {
		this.visPatientEntiresId = visPatientEntiresId;
	}
}
