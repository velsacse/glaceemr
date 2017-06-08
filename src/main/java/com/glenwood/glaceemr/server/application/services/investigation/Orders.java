package com.glenwood.glaceemr.server.application.services.investigation;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.ClinicalElementsOptions;
import com.glenwood.glaceemr.server.application.models.ChartStatus;
import com.glenwood.glaceemr.server.application.models.ImmunisationSite;
import com.glenwood.glaceemr.server.application.models.LabEntriesParameter;
import com.glenwood.glaceemr.server.application.models.VaccineOrderDetails;

/**
 * @author yasodha
 * Custom bean to set today's orders complete details
 */
public class Orders {

	LabData labEntries;
	List<Vaccines> vaccineConsentInfo;
	List<ChartStatus> statusList;
	List<ChartStatus> refusalReasonList;
	List<ChartStatus> sourceList;
	List<ImmunisationSite> siteInfo;
	List<LabEntriesParameter> labParameters;
	List<String> docDetails;
	List<String> imageDetails;
	List<ClinicalElementsOptions> audiometryLeftList;
	List<ClinicalElementsOptions> audiometryRightList;
	List<ClinicalElementsOptions> visionLeftList;
	List<ClinicalElementsOptions> visionRightList;
	Boolean isConsentObtained;
	String leftEarValue;
	String rightEarValue;
	String leftEyeValue;
	String rightEyeValue;
	List<ClinicalElementsOptions> glassValueList;
	String glassValue;
	List<VisData> visData;
	List<VaccineOrderDetails> lotDetails;
	String patientWeight;
	
	public List<ClinicalElementsOptions> getGlassValueList() {
		return glassValueList;
	}
	public void setGlassValueList(List<ClinicalElementsOptions> glassValueList) {
		this.glassValueList = glassValueList;
	}
	public String getGlassValue() {
		return glassValue;
	}
	public void setGlassValue(String glassValue) {
		this.glassValue = glassValue;
	}
	public String getLeftEarValue() {
		return leftEarValue;
	}
	public void setLeftEarValue(String leftEarValue) {
		this.leftEarValue = leftEarValue;
	}
	public String getRightEarValue() {
		return rightEarValue;
	}
	public void setRightEarValue(String rightEarValue) {
		this.rightEarValue = rightEarValue;
	}
	public String getLeftEyeValue() {
		return leftEyeValue;
	}
	public void setLeftEyeValue(String leftEyeValue) {
		this.leftEyeValue = leftEyeValue;
	}
	public String getRightEyeValue() {
		return rightEyeValue;
	}
	public void setRightEyeValue(String rightEyeValue) {
		this.rightEyeValue = rightEyeValue;
	}
	public List<ClinicalElementsOptions> getAudiometryLeftList() {
		return audiometryLeftList;
	}
	public void setAudiometryLeftList(
			List<ClinicalElementsOptions> audiometryLeftList) {
		this.audiometryLeftList = audiometryLeftList;
	}
	public List<ClinicalElementsOptions> getAudiometryRightList() {
		return audiometryRightList;
	}
	public void setAudiometryRightList(
			List<ClinicalElementsOptions> audiometryRightList) {
		this.audiometryRightList = audiometryRightList;
	}
	public List<ClinicalElementsOptions> getVisionLeftList() {
		return visionLeftList;
	}
	public void setVisionLeftList(List<ClinicalElementsOptions> visionLeftList) {
		this.visionLeftList = visionLeftList;
	}
	public List<ClinicalElementsOptions> getVisionRightList() {
		return visionRightList;
	}
	public void setVisionRightList(List<ClinicalElementsOptions> visionRightList) {
		this.visionRightList = visionRightList;
	}
	public List<String> getImageDetails() {
		return imageDetails;
	}
	public void setImageDetails(List<String> imageDetails) {
		this.imageDetails = imageDetails;
	}
	public List<String> getDocDetails() {
		return docDetails;
	}
	public void setDocDetails(List<String> docDetails) {
		this.docDetails = docDetails;
	}
	public List<LabEntriesParameter> getLabParameters() {
		return labParameters;
	}
	public void setLabParameters(List<LabEntriesParameter> labParameters) {
		this.labParameters = labParameters;
	}
	public Boolean getIsConsentObtained() {
		return isConsentObtained;
	}
	public void setIsConsentObtained(Boolean isConsentObtained) {
		this.isConsentObtained = isConsentObtained;
	}
	public LabData getLabEntries() {
		return labEntries;
	}
	public void setLabEntries(LabData labEntries) {
		this.labEntries = labEntries;
	}
	public List<ChartStatus> getStatusList() {
		return statusList;
	}
	public void setStatusList(List<ChartStatus> statusList) {
		this.statusList = statusList;
	}
	public List<ChartStatus> getRefusalReasonList() {
		return refusalReasonList;
	}
	public void setRefusalReasonList(List<ChartStatus> refusalReasonList) {
		this.refusalReasonList = refusalReasonList;
	}
	public List<ChartStatus> getSourceList() {
		return sourceList;
	}
	public void setSourceList(List<ChartStatus> sourceList) {
		this.sourceList = sourceList;
	}
	public List<ImmunisationSite> getSiteInfo() {
		return siteInfo;
	}
	public void setSiteInfo(List<ImmunisationSite> siteInfo) {
		this.siteInfo = siteInfo;
	}
	public List<Vaccines> getVaccineConsentInfo() {
		return vaccineConsentInfo;
	}
	public void setVaccineConsentInfo(List<Vaccines> vaccineConsentInfo) {
		this.vaccineConsentInfo = vaccineConsentInfo;
	}
	public List<VisData> getVisData() {
		return visData;
	}
	public void setVisData(List<VisData> visData) {
		this.visData = visData;
	}
	public List<VaccineOrderDetails> getLotDetails() {
		return lotDetails;
	}
	public void setLotDetails(List<VaccineOrderDetails> lotDetails) {
		this.lotDetails = lotDetails;
	}
	public String getPatientWeight() {
		return patientWeight;
	}
	public void setPatientWeight(String patientWeight) {
		this.patientWeight = patientWeight;
	}
}
