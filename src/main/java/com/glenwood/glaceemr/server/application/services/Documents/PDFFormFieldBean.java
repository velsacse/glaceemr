package com.glenwood.glaceemr.server.application.services.Documents;

public class PDFFormFieldBean {
	String height;
	String weight;
	String lastname;
	String firstname;
	String mi;
	String accountno;
	String patientname;
	String dob;
	String currentdate;
	String address1;
	String city;
	String state;
	String zip;
	String age;
	String gender;
	String guarantor;
	String practicename;
	String practiceaddress1;
	String practiceaddress2;
	String patientsign;
	String witnesssign;
	String physiciansign;
	String apptdate;
	String appttime;
	String day_currentdate;
	String primaryInsCompanyName="";
	String primaryInsPolicyId="";
	String primaryInsSubscriberName="";
	String primaryInsSubscriberDob="";
	String primaryInsGroupno="";
	String secondaryInsCompanyName="";
	String secondaryInsPolicyId="";
	String secondaryInsSubscriberName="";
	String secondaryInsSubscriberDob="";
	String secondaryInsGroupno="";
	boolean secondaryExists=false;
	String primaryRelation="";
	String secondaryRelation="";
	String patientssn="";
	String homephone="";
	String cellno="";
	String Sign="";
	String encounter_indate="";
	String current_date12hrformat="";
	String currdrugname="";
	String practicephoneno="";
	String docname="";
	String practicemail="";
	String docphoneno="";
	String cspno="";
	String dxcode="";
	String allergyname="";
	 
	 String serDocotorName;
	 String serDocAddress;
	 String serDocCity;
	 String serDocState;
	 String serDocZip;
	 String serDocPhNumber;
	 String serDocLicenseNo;
	 
	 
	 String mailid;
	 String emergencyperson;
	 String emergencynumber;
	 String pharmacynumber;
	 String pharmacyname;
	 String temp;
	 String cdurgname;
	 String rdocname;
	 
	 String othDocotorName;
	 String othDocCity;
	 String othDocState;
	 String othSpeciality;
	 String othDocPhNumber;
	 
	 String sysbp;
	 String diabp;
	 String cform;
	 String cstrength;
	 String pulse;
	 String respiratory;
	 
	public String getCstrength() {
		return cstrength;
	}
	public void setCstrength(String cstrength) {
		this.cstrength = cstrength;
	}
	public String getPulse() {
		return pulse;
	}
	public void setPulse(String pulse) {
		this.pulse = pulse;
	}
	public String getRespiratory() {
		return respiratory;
	}
	public void setRespiratory(String respiratory) {
		this.respiratory = respiratory;
	}
	public String getCform() {
		return cform;
	}
	public void setCform(String cform) {
		this.cform = cform;
	}
	public String getSysbp() {
		return sysbp;
	}
	public void setSysbp(String sysbp) {
		this.sysbp = sysbp;
	}
	public String getDiabp() {
		return diabp;
	}
	public void setDiabp(String diabp) {
		this.diabp = diabp;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getMi() {
		return mi;
	}
	public void setMi(String mi) {
		this.mi = mi;
	}
	public String getAccountno() {
		return accountno;
	}
	public void setAccountno(String accountno) {
		this.accountno = accountno;
	}
	public String getPatientname() {
		return patientname;
	}
	public void setPatientname(String patientname) {
		this.patientname = patientname;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getCurrentdate() {
		return currentdate;
	}
	public void setCurrentdate(String currentdate) {
		this.currentdate = currentdate;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getGuarantor() {
		return guarantor;
	}
	public void setGuarantor(String guarantor) {
		this.guarantor = guarantor;
	}
	public String getPracticename() {
		return practicename;
	}
	public void setPracticename(String practicename) {
		this.practicename = practicename;
	}
	public String getPracticeaddress1() {
		return practiceaddress1;
	}
	public void setPracticeaddress1(String practiceaddress1) {
		this.practiceaddress1 = practiceaddress1;
	}
	public String getPracticeaddress2() {
		return practiceaddress2;
	}
	public void setPracticeaddress2(String practiceaddress2) {
		this.practiceaddress2 = practiceaddress2;
	}
	public String getPatientsign() {
		return patientsign;
	}
	public void setPatientsign(String patientsign) {
		this.patientsign = patientsign;
	}
	public String getWitnesssign() {
		return witnesssign;
	}
	public void setWitnesssign(String witnesssign) {
		this.witnesssign = witnesssign;
	}
	public String getPhysiciansign() {
		return physiciansign;
	}
	public void setPhysiciansign(String physiciansign) {
		this.physiciansign = physiciansign;
	}
	public String getApptdate() {
		return apptdate;
	}
	public void setApptdate(String apptdate) {
		this.apptdate = apptdate;
	}
	public String getAppttime() {
		return appttime;
	}
	public void setAppttime(String appttime) {
		this.appttime = appttime;
	}
	public String getDay_currentdate() {
		return day_currentdate;
	}
	public void setDay_currentdate(String day_currentdate) {
		this.day_currentdate = day_currentdate;
	}
	public String getPrimaryInsCompanyName() {
		return primaryInsCompanyName;
	}
	public void setPrimaryInsCompanyName(String primaryInsCompanyName) {
		this.primaryInsCompanyName = primaryInsCompanyName;
	}
	public String getPrimaryInsPolicyId() {
		return primaryInsPolicyId;
	}
	public void setPrimaryInsPolicyId(String primaryInsPolicyId) {
		this.primaryInsPolicyId = primaryInsPolicyId;
	}
	public String getPrimaryInsSubscriberName() {
		return primaryInsSubscriberName;
	}
	public void setPrimaryInsSubscriberName(String primaryInsSubscriberName) {
		this.primaryInsSubscriberName = primaryInsSubscriberName;
	}
	public String getPrimaryInsSubscriberDob() {
		return primaryInsSubscriberDob;
	}
	public void setPrimaryInsSubscriberDob(String primaryInsSubscriberDob) {
		this.primaryInsSubscriberDob = primaryInsSubscriberDob;
	}
	public String getPrimaryInsGroupno() {
		return primaryInsGroupno;
	}
	public void setPrimaryInsGroupno(String primaryInsGroupno) {
		this.primaryInsGroupno = primaryInsGroupno;
	}
	public String getSecondaryInsCompanyName() {
		return secondaryInsCompanyName;
	}
	public void setSecondaryInsCompanyName(String secondaryInsCompanyName) {
		this.secondaryInsCompanyName = secondaryInsCompanyName;
	}
	public String getSecondaryInsPolicyId() {
		return secondaryInsPolicyId;
	}
	public void setSecondaryInsPolicyId(String secondaryInsPolicyId) {
		this.secondaryInsPolicyId = secondaryInsPolicyId;
	}
	public String getSecondaryInsSubscriberName() {
		return secondaryInsSubscriberName;
	}
	public void setSecondaryInsSubscriberName(String secondaryInsSubscriberName) {
		this.secondaryInsSubscriberName = secondaryInsSubscriberName;
	}
	public String getSecondaryInsSubscriberDob() {
		return secondaryInsSubscriberDob;
	}
	public void setSecondaryInsSubscriberDob(String secondaryInsSubscriberDob) {
		this.secondaryInsSubscriberDob = secondaryInsSubscriberDob;
	}
	public String getSecondaryInsGroupno() {
		return secondaryInsGroupno;
	}
	public void setSecondaryInsGroupno(String secondaryInsGroupno) {
		this.secondaryInsGroupno = secondaryInsGroupno;
	}
	public boolean isSecondaryExists() {
		return secondaryExists;
	}
	public void setSecondaryExists(boolean secondaryExists) {
		this.secondaryExists = secondaryExists;
	}
	public String getPrimaryRelation() {
		return primaryRelation;
	}
	public void setPrimaryRelation(String primaryRelation) {
		this.primaryRelation = primaryRelation;
	}
	public String getSecondaryRelation() {
		return secondaryRelation;
	}
	public void setSecondaryRelation(String secondaryRelation) {
		this.secondaryRelation = secondaryRelation;
	}
	public String getPatientssn() {
		return patientssn;
	}
	public void setPatientssn(String patientssn) {
		this.patientssn = patientssn;
	}
	public String getHomephone() {
		return homephone;
	}
	public void setHomephone(String homephone) {
		this.homephone = homephone;
	}
	public String getCellno() {
		return cellno;
	}
	public void setCellno(String cellno) {
		this.cellno = cellno;
	}
	public String getSign() {
		return Sign;
	}
	public void setSign(String sign) {
		Sign = sign;
	}
	public String getEncounter_indate() {
		return encounter_indate;
	}
	public void setEncounter_indate(String encounter_indate) {
		this.encounter_indate = encounter_indate;
	}
	public String getCurrent_date12hrformat() {
		return current_date12hrformat;
	}
	public void setCurrent_date12hrformat(String current_date12hrformat) {
		this.current_date12hrformat = current_date12hrformat;
	}
	public String getCurrdrugname() {
		return currdrugname;
	}
	public void setCurrdrugname(String currdrugname) {
		this.currdrugname = currdrugname;
	}
	public String getPracticephoneno() {
		return practicephoneno;
	}
	public void setPracticephoneno(String practicephoneno) {
		this.practicephoneno = practicephoneno;
	}
	public String getDocname() {
		return docname;
	}
	public void setDocname(String docname) {
		this.docname = docname;
	}
	public String getPracticemail() {
		return practicemail;
	}
	public void setPracticemail(String practicemail) {
		this.practicemail = practicemail;
	}
	public String getDocphoneno() {
		return docphoneno;
	}
	public void setDocphoneno(String docphoneno) {
		this.docphoneno = docphoneno;
	}
	public String getCspno() {
		return cspno;
	}
	public void setCspno(String cspno) {
		this.cspno = cspno;
	}
	public String getDxcode() {
		return dxcode;
	}
	public void setDxcode(String dxcode) {
		this.dxcode = dxcode;
	}
	public String getAllergyname() {
		return allergyname;
	}
	public void setAllergyname(String allergyname) {
		this.allergyname = allergyname;
	}
	public String getSerDocotorName() {
		return serDocotorName;
	}
	public void setSerDocotorName(String serDocotorName) {
		this.serDocotorName = serDocotorName;
	}
	public String getSerDocAddress() {
		return serDocAddress;
	}
	public void setSerDocAddress(String serDocAddress) {
		this.serDocAddress = serDocAddress;
	}
	public String getSerDocCity() {
		return serDocCity;
	}
	public void setSerDocCity(String serDocCity) {
		this.serDocCity = serDocCity;
	}
	public String getSerDocState() {
		return serDocState;
	}
	public void setSerDocState(String serDocState) {
		this.serDocState = serDocState;
	}
	public String getSerDocZip() {
		return serDocZip;
	}
	public void setSerDocZip(String serDocZip) {
		this.serDocZip = serDocZip;
	}
	public String getSerDocPhNumber() {
		return serDocPhNumber;
	}
	public void setSerDocPhNumber(String serDocPhNumber) {
		this.serDocPhNumber = serDocPhNumber;
	}
	public String getSerDocLicenseNo() {
		return serDocLicenseNo;
	}
	public void setSerDocLicenseNo(String serDocLicenseNo) {
		this.serDocLicenseNo = serDocLicenseNo;
	}
	public String getMailid() {
		return mailid;
	}
	public void setMailid(String mailid) {
		this.mailid = mailid;
	}
	public String getEmergencyperson() {
		return emergencyperson;
	}
	public void setEmergencyperson(String emergencyperson) {
		this.emergencyperson = emergencyperson;
	}
	public String getEmergencynumber() {
		return emergencynumber;
	}
	public void setEmergencynumber(String emergencynumber) {
		this.emergencynumber = emergencynumber;
	}
	public String getPharmacynumber() {
		return pharmacynumber;
	}
	public void setPharmacynumber(String pharmacynumber) {
		this.pharmacynumber = pharmacynumber;
	}
	public String getPharmacyname() {
		return pharmacyname;
	}
	public void setPharmacyname(String pharmacyname) {
		this.pharmacyname = pharmacyname;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getCdurgname() {
		return cdurgname;
	}
	public void setCdurgname(String cdurgname) {
		this.cdurgname = cdurgname;
	}
	public String getRdocname() {
		return rdocname;
	}
	public void setRdocname(String rdocname) {
		this.rdocname = rdocname;
	}
	public String getOthDocotorName() {
		return othDocotorName;
	}
	public void setOthDocotorName(String othDocotorName) {
		this.othDocotorName = othDocotorName;
	}
	public String getOthDocCity() {
		return othDocCity;
	}
	public void setOthDocCity(String othDocCity) {
		this.othDocCity = othDocCity;
	}
	public String getOthDocState() {
		return othDocState;
	}
	public void setOthDocState(String othDocState) {
		this.othDocState = othDocState;
	}
	public String getOthSpeciality() {
		return othSpeciality;
	}
	public void setOthSpeciality(String othSpeciality) {
		this.othSpeciality = othSpeciality;
	}
	public String getOthDocPhNumber() {
		return othDocPhNumber;
	}
	public void setOthDocPhNumber(String othDocPhNumber) {
		this.othDocPhNumber = othDocPhNumber;
	}
	private String getRelation(int relation)
	{
		String relationVal="";
		switch(relation)
		{
		case 1: relationVal="Self";break;
		case 2: relationVal="Spouse";break;
		case 3: relationVal="Child";break;
		}
		return relationVal;
	}

	public void setPrimaryInsDetail(String xPrimaryInsCompanyName,String xPrimaryInsPolicyId,String xPrimaryInsSubscriberName,String xPrimaryInsSubscriberDob,String xPrimaryInsGroupno,int xRelation)
	{
		if(this.primaryInsCompanyName=="")//Checking for more than one Primary True
		{
		 this.primaryInsCompanyName=xPrimaryInsCompanyName;
		 this.primaryInsPolicyId=xPrimaryInsPolicyId;
		 this.primaryInsSubscriberName=xPrimaryInsSubscriberName;
		 this.primaryInsSubscriberDob=xPrimaryInsSubscriberDob;
		 this.primaryInsGroupno=xPrimaryInsGroupno;
		 this.primaryRelation=getRelation(xRelation);
		}
	}
	public void setSecondaryInsDetail(String xSecondaryInsCompanyName,String xSecondaryInsPolicyId,String xSecondaryInsSubscriberName,String xSecondaryInsSubscriberDob,String xSecondaryInsGroupno,int xRelation)
	{
		if(this.secondaryInsCompanyName=="")//Checking for more than one Secondary True
		{
		 this.secondaryExists=true;
		 this.secondaryInsCompanyName=xSecondaryInsCompanyName;
		 this.secondaryInsPolicyId=xSecondaryInsPolicyId;
		 this.secondaryInsSubscriberName=xSecondaryInsSubscriberName;
		 this.secondaryInsSubscriberDob=xSecondaryInsSubscriberDob;
		 this.secondaryInsGroupno=xSecondaryInsGroupno;
		 this.secondaryRelation=getRelation(xRelation);
		}
	}
}
