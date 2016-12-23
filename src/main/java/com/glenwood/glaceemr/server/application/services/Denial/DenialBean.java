package com.glenwood.glaceemr.server.application.services.Denial;

public class DenialBean {
	
	Integer ptregid;
    Integer serviceId;
    String serviceStatus;
    String accountno;
    String lastName;
    String firstName;
    String middleName;
    String state;
    String denialReasonCode;
    String denialDescription;
    String denialDOP;
    Long serviceCount;
    String submitStatus;
    String dos;
    Double Charges;
    Double Copay;
    String primaryInsurance;
    String primInsID;
    String secondaryInsurance;
    String secInsID;
    String posId;
    String pos;
    Long claimCount;
    Long medclaimCount;
    Long dnlCount;
    Long nonServiceId;
    String cpt;
    String mod1;
    String dx1;
    String dx2;
    String dx3;
    String dx4;
    String bdoctor;
    String sdoctor;
    String mednotes;
    String action_type;
    String action_notes;
    String action_date;
    Double primaryPaidAmount;
    String claimId;
    String reference;
    String claimType;
    String cptDescription;
    Integer unit;
    Double allowed;
    Double secPaidAmount;
    Double insBal;
	public DenialBean(Integer ptregid, Integer serviceId, String serviceStatus,
			String accountno, String lastName, String firstName,
			String middleName, String state, String denialReasonCode,
			String denialDescription, String denialDOP, Long serviceCount,
			String submitStatus, String dos, Double charges, Double copay,
			String primaryInsurance, String primInsID,
			String secondaryInsurance, String secInsID, String posId,
			String pos, Long claimCount, Long medclaimCount,
			Long dnlCount, Long nonServiceId, String cpt, String mod1,
			String dx1, String dx2, String dx3, String dx4, String bdoctor,
			String sdoctor, String mednotes, String action_type,
			String action_notes, String action_date,Double primaryPaidAmount,String claimId,String reference,String cptDescription,Integer unit,Double allowed,
			Double secPaidAmount,Double insBal) {
		super();
		this.ptregid = ptregid;
		this.serviceId = serviceId;
		this.serviceStatus = serviceStatus;
		this.accountno = accountno;
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleName = middleName;
		this.state = state;
		this.denialReasonCode = denialReasonCode;
		this.denialDescription = denialDescription;
		this.denialDOP = denialDOP;
		this.serviceCount = serviceCount;
		this.submitStatus = submitStatus;
		this.dos = dos;
		Charges = charges;
		Copay = copay;
		this.primaryInsurance = primaryInsurance;
		this.primInsID = primInsID;
		this.secondaryInsurance = secondaryInsurance;
		this.secInsID = secInsID;
		this.posId = posId;
		this.pos = pos;
		this.claimCount = claimCount;
		this.medclaimCount = medclaimCount;
		this.dnlCount = dnlCount;
		this.nonServiceId = nonServiceId;
		this.cpt = cpt;
		this.mod1 = mod1;
		this.dx1 = dx1;
		this.dx2 = dx2;
		this.dx3 = dx3;
		this.dx4 = dx4;
		this.bdoctor = bdoctor;
		this.sdoctor = sdoctor;
		this.mednotes = mednotes;
		this.action_type = action_type;
		this.action_notes = action_notes;
		this.action_date = action_date;
		this.primaryPaidAmount=primaryPaidAmount;
		this.claimId=claimId;
		this.reference=reference;
		this.cptDescription=cptDescription;this.unit=unit;this.allowed=allowed;this.secPaidAmount=secPaidAmount;
		this.insBal=insBal;
	}
	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
	public Integer getPtregid() {
		return ptregid;
	}
	public void setPtregid(Integer ptregid) {
		this.ptregid = ptregid;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	public String getAccountno() {
		return accountno;
	}
	public void setAccountno(String accountno) {
		this.accountno = accountno;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDenialReasonCode() {
		return denialReasonCode;
	}
	public void setDenialReasonCode(String denialReasonCode) {
		this.denialReasonCode = denialReasonCode;
	}
	public String getDenialDescription() {
		return denialDescription;
	}
	public void setDenialDescription(String denialDescription) {
		this.denialDescription = denialDescription;
	}
	public String getDenialDOP() {
		return denialDOP;
	}
	public void setDenialDOP(String denialDOP) {
		this.denialDOP = denialDOP;
	}
	public Long getServiceCount() {
		return serviceCount;
	}
	public void setServiceCount(Long serviceCount) {
		this.serviceCount = serviceCount;
	}
	public String getSubmitStatus() {
		return submitStatus;
	}
	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}
	public String getDos() {
		return dos;
	}
	public void setDos(String dos) {
		this.dos = dos;
	}
	public Double getCharges() {
		return Charges;
	}
	public void setCharges(Double charges) {
		Charges = charges;
	}
	public Double getCopay() {
		return Copay;
	}
	public void setCopay(Double copay) {
		Copay = copay;
	}
	public String getPrimaryInsurance() {
		return primaryInsurance;
	}
	public void setPrimaryInsurance(String primaryInsurance) {
		this.primaryInsurance = primaryInsurance;
	}
	public String getPrimInsID() {
		return primInsID;
	}
	public void setPrimInsID(String primInsID) {
		this.primInsID = primInsID;
	}
	public String getSecondaryInsurance() {
		return secondaryInsurance;
	}
	public void setSecondaryInsurance(String secondaryInsurance) {
		this.secondaryInsurance = secondaryInsurance;
	}
	public String getSecInsID() {
		return secInsID;
	}
	public void setSecInsID(String secInsID) {
		this.secInsID = secInsID;
	}
	public String getPosId() {
		return posId;
	}
	public void setPosId(String posId) {
		this.posId = posId;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public Long getClaimCount() {
		return claimCount;
	}
	public void setClaimCount(Long claimCount) {
		this.claimCount = claimCount;
	}
	public Long getMedclaimCount() {
		return medclaimCount;
	}
	public void setMedclaimCount(Long medclaimCount) {
		this.medclaimCount = medclaimCount;
	}
	public Long getDnlCount() {
		return dnlCount;
	}
	public void setDnlCount(Long dnlCount) {
		this.dnlCount = dnlCount;
	}
	public Long getNonServiceId() {
		return nonServiceId;
	}
	public void setNonServiceId(Long nonServiceId) {
		this.nonServiceId = nonServiceId;
	}
	public String getCpt() {
		return cpt;
	}
	public void setCpt(String cpt) {
		this.cpt = cpt;
	}
	public String getMod1() {
		return mod1;
	}
	public void setMod1(String mod1) {
		this.mod1 = mod1;
	}
	public String getDx1() {
		return dx1;
	}
	public void setDx1(String dx1) {
		this.dx1 = dx1;
	}
	public String getDx2() {
		return dx2;
	}
	public void setDx2(String dx2) {
		this.dx2 = dx2;
	}
	public String getDx3() {
		return dx3;
	}
	public void setDx3(String dx3) {
		this.dx3 = dx3;
	}
	public String getDx4() {
		return dx4;
	}
	public void setDx4(String dx4) {
		this.dx4 = dx4;
	}
	public String getBdoctor() {
		return bdoctor;
	}
	public void setBdoctor(String bdoctor) {
		this.bdoctor = bdoctor;
	}
	public String getSdoctor() {
		return sdoctor;
	}
	public void setSdoctor(String sdoctor) {
		this.sdoctor = sdoctor;
	}
	public String getMednotes() {
		return mednotes;
	}
	public void setMednotes(String mednotes) {
		this.mednotes = mednotes;
	}
	public String getAction_type() {
		return action_type;
	}
	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}
	public String getAction_notes() {
		return action_notes;
	}
	public void setAction_notes(String action_notes) {
		this.action_notes = action_notes;
	}
	public String getAction_date() {
		return action_date;
	}
	public void setAction_date(String action_date) {
		this.action_date = action_date;
	}
	public Double getPaidAmount() {
		return primaryPaidAmount;
	}
	public void setPaidAmount(Double paidAmount) {
		this.primaryPaidAmount = paidAmount;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getCptDescription() {
		return cptDescription;
	}
	public void setCptDescription(String cptDescription) {
		this.cptDescription = cptDescription;
	}
	public Integer getUnit() {
		return unit;
	}
	public void setUnit(Integer unit) {
		this.unit = unit;
	}
	public Double getAllowed() {
		return allowed;
	}
	public void setAllowed(Double allowed) {
		this.allowed = allowed;
	}
	public Double getPrimaryPaidAmount() {
		return primaryPaidAmount;
	}
	public void setPrimaryPaidAmount(Double primaryPaidAmount) {
		this.primaryPaidAmount = primaryPaidAmount;
	}
	public Double getSecPaidAmount() {
		return secPaidAmount;
	}
	public void setSecPaidAmount(Double secPaidAmount) {
		this.secPaidAmount = secPaidAmount;
	}
	public Double getInsBal() {
		return insBal;
	}
	public void setInsBal(Double insBal) {
		this.insBal = insBal;
	}
	
    
}
