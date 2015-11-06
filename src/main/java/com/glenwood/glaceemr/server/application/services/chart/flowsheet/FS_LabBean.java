package com.glenwood.glaceemr.server.application.services.chart.flowsheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FS_LabBean {
	private String islab;
	private String labTestId;
	private String labName;
	private String orderedOn;
	private String performedOn;
	private String status;
	private String resultNotes;
	private String gender;
	private String due;
	private String dueCriteria;
	private String testDetailId;
	private String completedDetaildId;
	private String completedIdStatus;
	private String performdatesort;
	private Integer groupId;
	
	public String gettestDetailId() {
		return testDetailId;
	}
	
	public String getCompletedDetaildId() {
		return completedDetaildId;
	}

	
	public String getCompletedIdStatus() {
		return completedIdStatus;
	}

	public void settestDetailId(ArrayList<Integer> testIdCollection) {
		try{
			StringBuilder testDetailIdstr = new StringBuilder();
			for(int counter=0;counter<testIdCollection.size();counter++){
				if(counter == (testIdCollection.size()-1))
					testDetailIdstr.append(testIdCollection.get(counter).toString());
				else
					testDetailIdstr.append(testIdCollection.get(counter).toString()+",");
			}
			this.testDetailId =testDetailIdstr.toString() ;
		}
		catch(Exception e){
			this.testDetailId ="";
			e.printStackTrace();			
		}
	}

	public void setCompletedDetaildId(ArrayList<String> testIdAry) {
		try{
			StringBuilder completedDetailIdstr = new StringBuilder();
			List<Integer> CompStatus = new ArrayList<Integer>();
 			for(int counter=0;counter<testIdAry.size();counter++){
				if(counter == (testIdAry.size()-1)){
					completedDetailIdstr.append(testIdAry.get(counter).toString().split("@#@")[0]);
					CompStatus.add(Integer.parseInt(testIdAry.get(counter).toString().split("@#@")[1]));
				}else{
					completedDetailIdstr.append(testIdAry.get(counter).toString().split("@#@")[0]+"_");
					CompStatus.add(Integer.parseInt(testIdAry.get(counter).toString().split("@#@")[1]));
				}
			}
			this.completedDetaildId =completedDetailIdstr.toString();
			if(CompStatus.size()>0)
				this.completedIdStatus = Collections.max(CompStatus)+"";
			else
				this.completedIdStatus = "";
		}
		catch(Exception e){
			this.completedDetaildId ="";
			this.completedIdStatus = "";
			e.printStackTrace();			
		}
	}
	public String getLabName() {
		return labName;
	}

	public void setLabName(String labName) {
		this.labName = labName;
	}

	public String getOrderedOn() {
		return orderedOn;
	}

	public void setOrderedOn(String orderedOn) {
		this.orderedOn = orderedOn;
	}

	public String getPerformedOn() {
		return performedOn;
	}

	public void setPerformedOn(String performedOn) {
		this.performedOn = performedOn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResultNotes() {
		return resultNotes;
	}

	public void setResultNotes(String resultNotes) {
		this.resultNotes = resultNotes;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDue() {
		return due;
	}

	public void setDue(String due) {
		this.due = due;
	}

	public String getDueCriteria() {
		return dueCriteria;
	}

	public void setDueCriteria(String dueCriteria) {
		this.dueCriteria = dueCriteria;
	}

	public String getLabTestId() {
		return labTestId;
	}

	public void setLabTestId(String labTestId) {
		this.labTestId = labTestId;
	}

	public String getPerformdatesort() {
		return performdatesort;
	}

	public void setPerformdatesort(String performdatesort) {
		this.performdatesort = performdatesort;
	}

	public void setTestDetailId(String testDetailId) {
		this.testDetailId = testDetailId;
	}

	public void setCompletedDetaildId(String completedDetaildId) {
		this.completedDetaildId = completedDetaildId;
	}

	public void setCompletedIdStatus(String completedIdStatus) {
		this.completedIdStatus = completedIdStatus;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getIslab() {
		return islab;
	}

	public void setIslab(String islab) {
		this.islab = islab;
	}
}
