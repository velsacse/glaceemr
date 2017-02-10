package com.glenwood.glaceemr.server.application.services.GroupTherapy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TherapySessionBean {
	Integer sessionId;
	String sessionStartTime;
	Integer sessionProviderId;
	String sessionProviderName;
	Integer sessionLeaderId;
	Integer sessionSupervisorId;
	String sessionTopic;	
	Integer sessionPosId;
	String sessionPOSName;
	String sessionDateValue;
	Integer sessionStatus;
	String sessionEndTime;

	
	
	public TherapySessionBean(Integer sessionId,String sessionStartTime,Integer sessionProviderId,String sessionProviderName,Integer sessionLeaderId,int sessionSupervisorId,String sessionTopic,Integer sessionPosId,String sessionPOSName,Date sessionDateValue,Integer sessionStatus,String sessionEndTime){
		super();
		DateFormat timeFormat=new SimpleDateFormat("MM/dd/yyyy");
		
		this.sessionId= sessionId;
		this.sessionStartTime=sessionStartTime;
		this.sessionProviderId= sessionProviderId;
		this.sessionProviderName=sessionProviderName;
		this.sessionLeaderId= sessionLeaderId;
		this.sessionSupervisorId= sessionSupervisorId;
		this.sessionTopic= sessionTopic;
		this.sessionPosId= sessionPosId;
		this.sessionPOSName=sessionPOSName;
		this.sessionDateValue=timeFormat.format(sessionDateValue);
		this.sessionStatus= sessionStatus;
		this.sessionEndTime=sessionEndTime;
	}
	
	
	public Integer getSessionId() {
		return sessionId;
	}
	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}
	public String getSessionStartTime() {
		return sessionStartTime;
	}
	public void setSessionStartTime(String sessionStartTime) {
		this.sessionStartTime = sessionStartTime;
	}
	public Integer getSessionProviderId() {
		return sessionProviderId;
	}
	public void setSessionProviderId(Integer sessionProviderId) {
		this.sessionProviderId = sessionProviderId;
	}
	public Integer getSessionPosId() {
		return sessionPosId;
	}
	public void setSessionPosId(Integer sessionPosId) {
		this.sessionPosId = sessionPosId;
	}
	public Integer getSessionStatus() {
		return sessionStatus;
	}
	public void setSessionStatus(Integer sessionStatus) {
		this.sessionStatus = sessionStatus;
	}
	public String getSessionDateValue() {
		return sessionDateValue;
	}
	public void setSessionDateValue(String sessionDateValue) {
		this.sessionDateValue = sessionDateValue;
	}
	public String getSessionEndTime() {
		return sessionEndTime;
	}
	public void setSessionEndTime(String sessionEndTime) {
		this.sessionEndTime = sessionEndTime;
	}
	public String getSessionTopic() {
		return sessionTopic;
	}
	public void setSessionTopic(String sessionTopic) {
		this.sessionTopic = sessionTopic;
	}
	public Integer getSessionLeaderId() {
		return sessionLeaderId;
	}
	public void setSessionLeaderId(Integer sessionLeaderId) {
		this.sessionLeaderId = sessionLeaderId;
	}
	public Integer getSessionSupervisorId() {
		return sessionSupervisorId;
	}
	public void setSessionSupervisorId(Integer sessionSupervisorId) {
		this.sessionSupervisorId = sessionSupervisorId;
	}

	public String getSessionProviderName() {
		return sessionProviderName;
	}

	public void setSessionProviderName(String sessionProviderName) {
		this.sessionProviderName = sessionProviderName;
	}

	public String getSessionPOSName() {
		return sessionPOSName;
	}

	public void setSessionPOSName(String sessionPOSName) {
		this.sessionPOSName = sessionPOSName;
	}
	
	
	
	
}
