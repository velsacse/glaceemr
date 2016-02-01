package com.glenwood.glaceemr.server.application.services.investigation;

import java.util.Hashtable;

@SuppressWarnings("rawtypes")
public class SaveAttachResultData {
	String testDetailId;
	Integer encounterId;
	Integer chartId;
	Integer testId;
	Integer orderedBy;
	String orderedDate;
	Integer performedBy;
	String performedDate;
	String TestName;
	String resultNotes;
	String ResultParamStr;
	Integer status;
	String specimenSource;
	String specimenCondition;
	String specimencollecteddate;
	String isFasting;
	String collectionVolume;
	String clinicalInfo;
	Hashtable labLocCodeDetails;
	String rootPath;
	String resultFileName;
	public String getTestDetailId() {
		return testDetailId;
	}
	public void setTestDetailId(String testDetailId) {
		this.testDetailId = testDetailId;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public Integer getChartId() {
		return chartId;
	}
	public void setChartId(Integer chartId) {
		this.chartId = chartId;
	}
	public Integer getTestId() {
		return testId;
	}
	public void setTestId(Integer testId) {
		this.testId = testId;
	}
	public Integer getOrderedBy() {
		return orderedBy;
	}
	public void setOrderedBy(Integer orderedBy) {
		this.orderedBy = orderedBy;
	}
	public String getOrderedDate() {
		return orderedDate;
	}
	public void setOrderedDate(String orderedDate) {
		this.orderedDate = orderedDate;
	}
	public Integer getPerformedBy() {
		return performedBy;
	}
	public void setPerformedBy(Integer performedBy) {
		this.performedBy = performedBy;
	}
	public String getPerformedDate() {
		return performedDate;
	}
	public void setPerformedDate(String performedDate) {
		this.performedDate = performedDate;
	}
	public String getTestName() {
		return TestName;
	}
	public void setTestName(String testName) {
		TestName = testName;
	}
	public String getResultNotes() {
		return resultNotes;
	}
	public void setResultNotes(String resultNotes) {
		this.resultNotes = resultNotes;
	}
	public String getResultParamStr() {
		return ResultParamStr;
	}
	public void setResultParamStr(String resultParamStr) {
		ResultParamStr = resultParamStr;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getSpecimenSource() {
		return specimenSource;
	}
	public void setSpecimenSource(String specimenSource) {
		this.specimenSource = specimenSource;
	}
	public String getSpecimenCondition() {
		return specimenCondition;
	}
	public void setSpecimenCondition(String specimenCondition) {
		this.specimenCondition = specimenCondition;
	}
	public String getSpecimencollecteddate() {
		return specimencollecteddate;
	}
	public void setSpecimencollecteddate(String specimencollecteddate) {
		this.specimencollecteddate = specimencollecteddate;
	}
	public String getIsFasting() {
		return isFasting;
	}
	public void setIsFasting(String isFasting) {
		this.isFasting = isFasting;
	}
	public String getCollectionVolume() {
		return collectionVolume;
	}
	public void setCollectionVolume(String collectionVolume) {
		this.collectionVolume = collectionVolume;
	}
	public String getClinicalInfo() {
		return clinicalInfo;
	}
	public void setClinicalInfo(String clinicalInfo) {
		this.clinicalInfo = clinicalInfo;
	}
	public Hashtable getLabLocCodeDetails() {
		return labLocCodeDetails;
	}
	public void setLabLocCodeDetails(Hashtable labLocCodeDetails) {
		this.labLocCodeDetails = labLocCodeDetails;
	}
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	public String getResultFileName() {
		return resultFileName;
	}
	public void setResultFileName(String resultFileName) {
		this.resultFileName = resultFileName;
	}
}
