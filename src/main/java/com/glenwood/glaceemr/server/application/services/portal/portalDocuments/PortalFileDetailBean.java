package com.glenwood.glaceemr.server.application.services.portal.portalDocuments;

public class PortalFileDetailBean {

	int patientId;
	
	String fileNameWithLocation;

	String fileName;
	
	String filePath;

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public String getFileNameWithLocation() {
		return fileNameWithLocation;
	}

	public void setFileNameWithLocation(String fileNameWithLocation) {
		this.fileNameWithLocation = fileNameWithLocation;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
	
}
