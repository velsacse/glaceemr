package com.glenwood.glaceemr.server.application.services.labresults;

import java.util.Date;

public class Hl7UnmappedresultsBean {
	private Date hl7UnmappedresultsOrdDate;
	private String hl7UnmappedresultsOrdbyLastname;
	private String hl7UnmappedresultsOrdbyFirstname;
	public Hl7UnmappedresultsBean(String hl7UnmappedresultsOrdbyFirstname,
			String hl7UnmappedresultsOrdbyLastname,
			Date hl7UnmappedresultsOrdDate) {
		super();
		this.hl7UnmappedresultsOrdDate = hl7UnmappedresultsOrdDate;
		this.hl7UnmappedresultsOrdbyLastname = hl7UnmappedresultsOrdbyLastname;
		this.hl7UnmappedresultsOrdbyFirstname = hl7UnmappedresultsOrdbyFirstname;
	}
	public Date getHl7UnmappedresultsOrdDate() {
		return hl7UnmappedresultsOrdDate;
	}
	public void setHl7UnmappedresultsOrdDate(Date hl7UnmappedresultsOrdDate) {
		this.hl7UnmappedresultsOrdDate = hl7UnmappedresultsOrdDate;
	}
	public String getHl7UnmappedresultsOrdbyLastname() {
		return hl7UnmappedresultsOrdbyLastname;
	}
	public void setHl7UnmappedresultsOrdbyLastname(
			String hl7UnmappedresultsOrdbyLastname) {
		this.hl7UnmappedresultsOrdbyLastname = hl7UnmappedresultsOrdbyLastname;
	}
	public String getHl7UnmappedresultsOrdbyFirstname() {
		return hl7UnmappedresultsOrdbyFirstname;
	}
	public void setHl7UnmappedresultsOrdbyFirstname(
			String hl7UnmappedresultsOrdbyFirstname) {
		this.hl7UnmappedresultsOrdbyFirstname = hl7UnmappedresultsOrdbyFirstname;
	}
}
