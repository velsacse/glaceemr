package com.glenwood.glaceemr.server.application.Bean.macra.data.qdm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Request
{
	private String accountId = new String();
	private int reportingYear;
	private Date startDate;
	private Date endDate;
	private List<Integer> measureIds = new ArrayList<Integer>();
	private Patient patient = new Patient();
	
	public String getAccountId()
	{
		return accountId;
	}
	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}
	public int getReportingYear()
	{
		return reportingYear;
	}
	public void setReportingYear(int reportingYear)
	{
		this.reportingYear = reportingYear;
	}
	public Date getStartDate()
	{
		return startDate;
	}
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}
	public Date getEndDate()
	{
		return endDate;
	}
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
	public List<Integer> getMeasureIds()
	{
		return measureIds;
	}
	public void setMeasureIds(List<Integer> measureIds)
	{
		this.measureIds = measureIds;
	}
	public Patient getPatient()
	{
		return patient;
	}
	public void setPatient(Patient patient)
	{
		this.patient = patient;
	}
	
}