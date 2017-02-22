package com.glenwood.glaceemr.server.application.Bean;

import java.util.Date;

public class ClinicalDataQDM {
	private String code;	
	private String codeSystem;
	private String codeSystemOID;
	private String description;
	private Date startDate;
	private Date endDate;
	private Date recordedDate;
	private String resultCode;
	private String resultCodeSystem;
	private String resultValue;
	
	
	public ClinicalDataQDM(String code, String codeSystem,String description,Date recordedDate,String resultCode,String resultCodeSystem,String resultValue) {	
		super();	
		this.code = code;
		this.codeSystem = codeSystem;
		this.description = description;
		this.recordedDate =recordedDate;
		this.resultCode = resultCode;
		this.resultCodeSystem = resultCodeSystem;			
		this.resultValue = resultValue;				
	}
	
	public ClinicalDataQDM(String code, String codeSystem,String description,Date recordedDate,Integer resultCode,String resultCodeSystem,String resultValue) {	
		super();	
		this.code = code;
		this.codeSystem = codeSystem;
		this.description = description;
		this.recordedDate =recordedDate;
		this.resultCode = resultCode.toString();
		this.resultCodeSystem = resultCodeSystem;			
		this.resultValue = resultValue;				
	}
	
 /* public ClinicalDataQDM(String code, String codeSystem, String codeSystemOID, String description,String resultCode,String resultCodeSystem,String resultValue) {
		
		super();
		
		this.code = code;
		this.codeSystem = codeSystem;
		this.codeSystemOID = codeSystemOID;
		this.description = description;
		
		this.resultCode = resultCode;
		this.resultCodeSystem = resultCodeSystem;
		this.resultValue = resultValue;
		
		
	}
	*/

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeSystem() {
		return codeSystem;
	}


	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}

	public String getCodeSystemOID() {
		return codeSystemOID;
	}

	public void setCodeSystemOID(String codeSystemOID) {
		this.codeSystemOID = codeSystemOID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getRecordedDate() {
		return recordedDate;
	}

	public void setRecordedDate(Date recordedDate) {
		this.recordedDate = recordedDate;
	}


	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultCodeSystem() {
		return resultCodeSystem;
	}

	public void setResultCodeSystem(String resultCodeSystem) {
		this.resultCodeSystem = resultCodeSystem;
	}

	public String getResultValue() {
		return resultValue;
	}

	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}

}