package com.glenwood.glaceemr.server.application.services.investigation;

public class LS_External {
	Integer testDetailId;
	String labName;
	Integer testId;
	Integer hl7fileid;
	Integer isreviewed;
	public LS_External(Integer testDetailId,String labName,Integer testId,Integer hl7fileid,Integer isreviewed){
		this.testDetailId=testDetailId;
		this.labName=labName;
		this.testId=testId;
		this.hl7fileid=hl7fileid;
		this.isreviewed=isreviewed;
	}
	public Integer getTestDetailId() {
		return testDetailId;
	}
	public void setTestDetailId(Integer testDetailId) {
		this.testDetailId = testDetailId;
	}
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
	}
	public Integer getTestId() {
		return testId;
	}
	public void setTestId(Integer testId) {
		this.testId = testId;
	}
	public Integer getHl7fileid() {
		return hl7fileid;
	}
	public void setHl7fileid(Integer hl7fileid) {
		this.hl7fileid = hl7fileid;
	}
	public Integer getIsreviewed() {
		return isreviewed;
	}
	public void setIsreviewed(Integer isreviewed) {
		this.isreviewed = isreviewed;
	}
}
