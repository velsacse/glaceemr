package com.glenwood.glaceemr.server.application.services.chart.clinicalElements;




public class ClinicalElementBean {
	private String clinicalElementName;
	private String clinicalElementNotes;
	private Integer clinicalElementDataType;
	private String clinicalElementCPT;
	private String clinicalElementICD9;
	private String clinicalElementSNOMED;
	private boolean clinicalElementIsActive;
	private boolean clinicalElementIsGlobal;
	private boolean clinicalElementIsHistory;
	private boolean clinicalElementIsEpisode;
	private String clinicalElementTextDimension;
	private Integer clinicalElementGender;
	private Integer clinicalElementIsSelect;
	public ClinicalElementBean(){
		clinicalElementName="";
		clinicalElementNotes="";
		clinicalElementDataType=-1;
		clinicalElementCPT="";
		clinicalElementICD9="";
		clinicalElementSNOMED="";
		clinicalElementIsActive=false;
		clinicalElementIsGlobal=false;
		clinicalElementIsHistory=false;
		clinicalElementIsEpisode=false;
		clinicalElementGender=0;
		clinicalElementTextDimension="10";
		clinicalElementIsSelect = -1;
	}
	public String getClinicalElementName() {
		return clinicalElementName;
	}
	public void setClinicalElementName(String clinicalElementName) {
		this.clinicalElementName = clinicalElementName;
	}
	public String getClinicalElementNotes() {
		return clinicalElementNotes;
	}
	public void setClinicalElementNotes(String clinicalElementNotes) {
		this.clinicalElementNotes = clinicalElementNotes;
	}
	public Integer getClinicalElementDataType() {
		return clinicalElementDataType;
	}
	public void setClinicalElementDataType(Integer clinicalElementDataType) {
		this.clinicalElementDataType = clinicalElementDataType;
	}
	public String getClinicalElementCPT() {
		return clinicalElementCPT;
	}
	public void setClinicalElementCPT(String clinicalElementCPT) {
		this.clinicalElementCPT = clinicalElementCPT;
	}
	public String getClinicalElementICD9() {
		return clinicalElementICD9;
	}
	public void setClinicalElementICD9(String clinicalElementICD9) {
		this.clinicalElementICD9 = clinicalElementICD9;
	}
	public String getClinicalElementSNOMED() {
		return clinicalElementSNOMED;
	}
	public void setClinicalElementSNOMED(String clinicalElementSNOMED) {
		this.clinicalElementSNOMED = clinicalElementSNOMED;
	}
	public Boolean getClinicalElementIsActive() {
		return clinicalElementIsActive;
	}
	public void setClinicalElementIsActive(boolean clinicalElementIsActive) {
		this.clinicalElementIsActive = clinicalElementIsActive;
	}
	public boolean getClinicalElementIsGlobal() {
		return clinicalElementIsGlobal;
	}
	public void setClinicalElementIsGlobal(boolean clinicalElementIsGlobal) {
		this.clinicalElementIsGlobal = clinicalElementIsGlobal;
	}
	public boolean getClinicalElementIsHistory() {
		return clinicalElementIsHistory;
	}
	public void setClinicalElementIsHistory(boolean clinicalElementIsHistory) {
		this.clinicalElementIsHistory = clinicalElementIsHistory;
	}
	public boolean getClinicalElementIsEpisode() {
		return clinicalElementIsEpisode;
	}
	public void setClinicalElementIsEpisode(boolean clinicalElementIsEpisode) {
		this.clinicalElementIsEpisode = clinicalElementIsEpisode;
	}
	public Integer getClinicalElementGender() {
		return clinicalElementGender;
	}
	public void setClinicalElementGender(Integer clinicalElementGender) {
		this.clinicalElementGender = clinicalElementGender;
	}
	public String getClinicalElementTextDimension() {
		return clinicalElementTextDimension;
	}
	public void setClinicalElementTextDimension(String clinicalElementTextDimension) {
		this.clinicalElementTextDimension = clinicalElementTextDimension;
	}
	public Integer getClinicalElementIsSelect() {
		return clinicalElementIsSelect;
	}
	public void setClinicalElementIsSelect(Integer clinicalElementIsSelect) {
		this.clinicalElementIsSelect = clinicalElementIsSelect;
	}
}