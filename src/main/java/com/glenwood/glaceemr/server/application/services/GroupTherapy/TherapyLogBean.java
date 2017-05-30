package com.glenwood.glaceemr.server.application.services.GroupTherapy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TherapyLogBean {
	Long count;
	Integer groupId;
	Integer providerId;
	Integer posId;
    Integer therapyId;
    Integer status;
	Integer leaderId;
	Integer supervisorId;
	String date;
	String docName;
	String pos;
	String groupName;
	String endTime="";
	String therapyStartDate="";
	String leaderName = "";
	String supervisorName = "";
	String sessionTopic = "";
	
	
	public TherapyLogBean(Integer therapyId,Date date,String sessionTopic,String docName,String leaderName,String supervisorName,String groupName,String pos,Long count,Integer groupId,Integer providerId,Integer leaderId,Integer supervisorId,Integer posId,String endTime,String therapyStartDate,Integer status){
		super();
		this.therapyId=therapyId;
		if(docName!=null)
			this.docName=docName;
		else
			this.docName="";
		if(leaderName!=null)
			this.leaderName=leaderName;
		else
			this.leaderName="";
		if(supervisorName!=null)
			this.supervisorName=supervisorName;
		else
			this.supervisorName="";
		this.count=count;
		this.pos=pos;
		if(therapyStartDate!=null)
			this.therapyStartDate=therapyStartDate;
		else
			this.therapyStartDate="";
		DateFormat mmformat=new SimpleDateFormat("MM/dd/yyyy");
		this.date=mmformat.format(date);
        this.groupName=groupName;
        this.groupId=groupId;
        this.posId=posId;
        if(providerId!=null)
        	this.providerId=providerId;
        else 
        	providerId=-1;
        if(leaderId!=null)
			this.leaderId=leaderId;
		else
			this.leaderId=-1;
        if(supervisorId!=null)
        	this.supervisorId=supervisorId;
        else
        	this.supervisorId=-1;
        this.endTime=endTime;
        /*DateFormat timeFormat=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        this.therapyStartDate=timeFormat.format(therapyStartDate);*/
        this.status=status;
        if(sessionTopic!=null)
			this.sessionTopic=sessionTopic;
		else
			this.sessionTopic="";
	}
	
	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public int getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(int leaderId) {
		this.leaderId = leaderId;
	}

	public int getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(int supervisorId) {
		this.supervisorId = supervisorId;
	}

	public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}

		


	public int getTherapyId() {
		return therapyId;
	}



	public void setTherapyId(int therapyId) {
		this.therapyId = therapyId;
	}



	public int getGroupId() {
		return groupId;
	}



	public Integer getProviderId() {
		return providerId;
	}



	public int getPosId() {
		return posId;
	}



	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}



	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}



	public void setPosId(int posId) {
		this.posId = posId;
	}



	public Long getCount() {
		return count;
	}



	public void setCount(Long count) {
		this.count = count;
	}

    
	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public String getDocName() {
		return docName;
	}

	public String getPos() {
		return pos;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTherapyStartDate() {
		return therapyStartDate;
	}

	public void setTherapyStartDate(String therapyStartDate) {
		this.therapyStartDate = therapyStartDate;
	}

	public String getSessionTopic() {
		return sessionTopic;
	}

	public void setSessionTopic(String sessionTopic) {
		this.sessionTopic = sessionTopic;
	}
	
	
	
}