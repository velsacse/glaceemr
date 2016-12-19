package com.glenwood.glaceemr.server.application.services.Documents;

import java.sql.Timestamp;
import java.util.Date;

public class PatientConsentFormDetailsBean {
	private Integer patientRegistrationId;
	private String employerName;
	private String empProfilePhoneno;
	private String patientRegistrationLastName;
	private String patientRegistrationAccountno;
	private String patientRegistrationFirstName;
	private String patientRegistrationMidInitial;
	private String patientRegistrationCspno;
	private String patientFullName;
	private Date patientRegistrationDob;
	private String patientRegistrationAddress1;
	private String patientRegistrationCity;
	//private String state;
	private String patientRegistrationZip;
	private String age;
	//private String sex;
	private String guarantorName;
	private String currentDate;
	private String patientRegistrationPhoneNo;
	private String patientRegistrationCellno;
	private String patientRegistrationSsn;
	private String signaturefilename;
	private String schApptStarttime;
	private String patientRegistrationMailId;
	private String patientRegistrationEcontactperson;
	private String patientRegistrationEphoneno;
	private String pharmDetailsPrimaryPhone;
	private String pharmDetailsStoreName;
	
	public String getPatientRegistrationSsn() {
		return patientRegistrationSsn;
	}
	public void setPatientRegistrationSsn(String patientRegistrationSsn) {
		this.patientRegistrationSsn = patientRegistrationSsn;
	}
	public String getSignaturefilename() {
		return signaturefilename;
	}
	public void setSignaturefilename(String signaturefilename) {
		this.signaturefilename = signaturefilename;
	}
	public String getSchApptStarttime() {
		return schApptStarttime;
	}
	public void setSchApptStarttime(String schApptStarttime) {
		this.schApptStarttime = schApptStarttime;
	}
	

	public PatientConsentFormDetailsBean(Integer patientRegistrationId,String employerName,String empProfilePhoneno,String patientRegistrationLastName,String patientRegistrationAccountno,String patientRegistrationFirstName,String patientRegistrationMidInitial,
			String patientRegistrationCspno,String patientFullName,Date patientRegistrationDob,String patientRegistrationAddress1,String patientRegistrationCity,/*String state,*/String patientRegistrationZip,String age,/*String sex,*/String guarantorName,String currentDate,String patientRegistrationPhoneNo, String patientRegistrationCellno,
			String patientRegistrationSsn,String signaturefilename,/*String schApptStarttime,*/String patientRegistrationMailId,String patientRegistrationEcontactperson,String patientRegistrationEphoneno,String pharmDetailsPrimaryPhone,String pharmDetailsStoreName){
		super();

		this.patientRegistrationId=patientRegistrationId;
		this.employerName=employerName;
		this.empProfilePhoneno=empProfilePhoneno;
		this.patientRegistrationLastName=patientRegistrationLastName;
		this.patientRegistrationAccountno=patientRegistrationAccountno;
		this.patientRegistrationFirstName=patientRegistrationFirstName;
		this.patientRegistrationMidInitial=patientRegistrationMidInitial;
		this.patientRegistrationCspno=patientRegistrationCspno;
		this.patientFullName=patientFullName;
		this.patientRegistrationDob=patientRegistrationDob;
		this.patientRegistrationAddress1=patientRegistrationAddress1;
		this.patientRegistrationCity=patientRegistrationCity;
		//this.state=state;
		this.patientRegistrationZip=patientRegistrationZip;
		this.age=age;
		//this.sex=sex;
		this.guarantorName=guarantorName;
		/*this.currentDate=currentDate;*/
		this.patientRegistrationPhoneNo=patientRegistrationPhoneNo;
		this.patientRegistrationCellno=patientRegistrationCellno;
		this.patientRegistrationSsn=patientRegistrationSsn;
		this.signaturefilename=signaturefilename;
		this.schApptStarttime=schApptStarttime;
		this.patientRegistrationMailId=patientRegistrationMailId;
		this.patientRegistrationEcontactperson=patientRegistrationEcontactperson;
		this.patientRegistrationEphoneno=patientRegistrationEphoneno;
		this.pharmDetailsPrimaryPhone=pharmDetailsPrimaryPhone;
		this.pharmDetailsStoreName=pharmDetailsStoreName;

	}
	public Integer getPatientRegistrationId() {
		return patientRegistrationId;
	}
	public void setPatientRegistrationId(Integer patientRegistrationId) {
		this.patientRegistrationId = patientRegistrationId;
	}
	public String getEmployerName() {
		return employerName;
	}
	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}
	public String getEmpProfilePhoneno() {
		return empProfilePhoneno;
	}
	public void setEmpProfilePhoneno(String empProfilePhoneno) {
		this.empProfilePhoneno = empProfilePhoneno;
	}
	public String getPatientRegistrationLastName() {
		return patientRegistrationLastName;
	}
	public void setPatientRegistrationLastName(String patientRegistrationLastName) {
		this.patientRegistrationLastName = patientRegistrationLastName;
	}
	public String getPatientRegistrationAccountno() {
		return patientRegistrationAccountno;
	}
	public void setPatientRegistrationAccountno(String patientRegistrationAccountno) {
		this.patientRegistrationAccountno = patientRegistrationAccountno;
	}
	public String getPatientRegistrationFirstName() {
		return patientRegistrationFirstName;
	}
	public void setPatientRegistrationFirstName(String patientRegistrationFirstName) {
		this.patientRegistrationFirstName = patientRegistrationFirstName;
	}
	public String getPatientRegistrationMidInitial() {
		return patientRegistrationMidInitial;
	}
	public void setPatientRegistrationMidInitial(
			String patientRegistrationMidInitial) {
		this.patientRegistrationMidInitial = patientRegistrationMidInitial;
	}
	public String getPatientRegistrationCspno() {
		return patientRegistrationCspno;
	}
	public void setPatientRegistrationCspno(String patientRegistrationCspno) {
		this.patientRegistrationCspno = patientRegistrationCspno;
	}
	public String getPatientFullName() {
		return patientFullName;
	}
	public void setPatientFullName(String patientFullName) {
		this.patientFullName = patientFullName;
	}
	public Date getPatientRegistrationDob() {
		return patientRegistrationDob;
	}
	public void setPatientRegistrationDob(Date patientRegistrationDob) {
		this.patientRegistrationDob = patientRegistrationDob;
	}
	public String getPatientRegistrationAddress1() {
		return patientRegistrationAddress1;
	}
	public void setPatientRegistrationAddress1(String patientRegistrationAddress1) {
		this.patientRegistrationAddress1 = patientRegistrationAddress1;
	}
	public String getPatientRegistrationCity() {
		return patientRegistrationCity;
	}
	public void setPatientRegistrationCity(String patientRegistrationCity) {
		this.patientRegistrationCity = patientRegistrationCity;
	}
	/*public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}*/
	public String getPatientRegistrationZip() {
		return patientRegistrationZip;
	}
	public void setPatientRegistrationZip(String patientRegistrationZip) {
		this.patientRegistrationZip = patientRegistrationZip;
	}
	/*public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}*/
	public String getGuarantorName() {
		return guarantorName;
	}
	public void setGuarantorName(String guarantorName) {
		this.guarantorName = guarantorName;
	}
	public String getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	public String getPatientRegistrationPhoneNo() {
		return patientRegistrationPhoneNo;
	}
	public void setPatientRegistrationPhoneNo(String patientRegistrationPhoneNo) {
		this.patientRegistrationPhoneNo = patientRegistrationPhoneNo;
	}
	public String getPatientRegistrationCellno() {
		return patientRegistrationCellno;
	}
	public void setPatientRegistrationCellno(String patientRegistrationCellno) {
		this.patientRegistrationCellno = patientRegistrationCellno;
	}
	public String getPatientRegistrationMailId() {
		return patientRegistrationMailId;
	}
	public void setPatientRegistrationMailId(String patientRegistrationMailId) {
		this.patientRegistrationMailId = patientRegistrationMailId;
	}
	public String getPatientRegistrationEcontactperson() {
		return patientRegistrationEcontactperson;
	}
	public void setPatientRegistrationEcontactperson(
			String patientRegistrationEcontactperson) {
		this.patientRegistrationEcontactperson = patientRegistrationEcontactperson;
	}
	public String getPatientRegistrationEphoneno() {
		return patientRegistrationEphoneno;
	}
	public void setPatientRegistrationEphoneno(String patientRegistrationEphoneno) {
		this.patientRegistrationEphoneno = patientRegistrationEphoneno;
	}
	public String getPharmDetailsPrimaryPhone() {
		return pharmDetailsPrimaryPhone;
	}
	public void setPharmDetailsPrimaryPhone(String pharmDetailsPrimaryPhone) {
		this.pharmDetailsPrimaryPhone = pharmDetailsPrimaryPhone;
	}
	public String getPharmDetailsStoreName() {
		return pharmDetailsStoreName;
	}
	public void setPharmDetailsStoreName(String pharmDetailsStoreName) {
		this.pharmDetailsStoreName = pharmDetailsStoreName;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	
}
