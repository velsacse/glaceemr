package com.glenwood.glaceemr.server.application.services.chart.admission;

import java.util.List;



public class AdmissionBean {
	
	Integer patientId;
	Integer chartId;
	String admissionDate;
	Integer admssProvider;
	Integer pos;
	String selectedDx;
	Integer userId;
	Integer loginId;
	Integer admissionEpisode;
	Integer roomNo;
	String notes;
	
	
	
	public Integer getAdmissionEpisode() {
		return admissionEpisode;
	}
	public void setAdmissionEpisode(Integer admissionEpisode) {
		this.admissionEpisode = admissionEpisode;
	}
	public Integer getChartId() {
		return chartId;
	}
	public void setChartId(Integer chartId) {
		this.chartId = chartId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getLoginId() {
		return loginId;
	}
	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public String getAdmissionDate() {
		return admissionDate;
	}
	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}
	public Integer getAdmssProvider() {
		return admssProvider;
	}
	public void setAdmssProvider(Integer admssProvider) {
		this.admssProvider = admssProvider;
	}
	public Integer getPos() {
		return pos;
	}
	public void setPos(Integer pos) {
		this.pos = pos;
	}
	public String getSelectedDx() {
		return selectedDx;
	}
	public void setSelectedDx(String selectedDx) {
		this.selectedDx = selectedDx;
	}
	public Integer getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(Integer roomNo) {
		this.roomNo = roomNo;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	
}
