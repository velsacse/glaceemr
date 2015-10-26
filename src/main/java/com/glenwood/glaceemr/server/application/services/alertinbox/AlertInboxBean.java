package com.glenwood.glaceemr.server.application.services.alertinbox;

import java.util.Date;

public class AlertInboxBean {
	
	int alertid;
	String roomname;
	int alertgroup;
	int parentid;
	int modifiedbyid;
	int readbyid;
	String redirecturl;
	int chartid;
	int fromid;
	Boolean read;
	String modifiedbyname;
	int encounterid;
	int toid;
	String alert_category_url_caption;
	String forwardedby;
	int alerttype;
	int subpage;
	String modifiedon;
	String createddate;
	String msg;
	int categoryid;
	String readbyname;
	String patientname;
	int patientid;
	String receivedto;
	Boolean highprivilage;
	String patientphnum;
	int refid;
	int alertsection;
	String patientaccnum;
	int qrflag;
	String displayname;
	int status;
	Boolean expanded;
	int actionmap;
	int categoryorder;
	String categoryname;
	Boolean needdatewisegrouping;
	String qrurl;
	String servicedoctor;
	String readon;
	Date currentmilliseconds;
	String createdtime;
	String createddatetime;
	

	
	public AlertInboxBean(int alertid, int categoryid, int chartid, String patientaccnum, String patientname, String roomname, String categoryname,
			String redirecturl, String displayname, int alertgroup, int alerttype, int fromid, int toid, int refid, int patientid, int parentid,
			int encounterid, String msg, int modifiedbyid, String createddate, String forwardedby, String receivedto,String readbyname, String modifiedbyname,
			boolean read, String alert_category_url_caption, int subpage, String modifiedon, boolean highprivilage, int alertsection, String qrurl, 
			int status, boolean expanded, int actionmap, int categoryorder, boolean needdatewisegrouping, int qrflag, String servicedoctor, String readon, 
			Date currentmilliseconds, String createdtime, String patientphnum, int readbyid) {
		this.alertid = alertid;
		this.roomname = roomname;
		this.alertgroup = alertgroup;
		this.parentid = parentid;
		this.modifiedbyid = modifiedbyid;
		this.readbyid = readbyid;
		this.redirecturl = redirecturl;
		this.chartid = chartid;
		this.fromid = fromid;
		this.read = read;
		this.modifiedbyname = modifiedbyname;
		this.encounterid = encounterid;
		this.toid = toid;
		this.alert_category_url_caption = alert_category_url_caption;
		this.forwardedby = forwardedby;
		this.alerttype = alerttype;
		this.subpage = subpage;
		this.modifiedon = modifiedon;
		this.createddate = createddate;
		this.msg = msg;
		this.categoryid = categoryid;
		this.readbyname = readbyname;
		this.patientname = patientname;
		this.patientid = patientid;
		this.receivedto = receivedto;
		this.highprivilage = highprivilage;
		this.patientphnum = patientphnum;
		this.refid = refid;
		this.alertsection = alertsection;
		this.patientaccnum = patientaccnum;
		this.qrflag = qrflag;
		this.displayname = displayname;
		this.status = status;
		this.expanded = expanded;
		this.actionmap = actionmap;
		this.categoryorder = categoryorder;
		this.categoryname = categoryname;
		this.needdatewisegrouping = needdatewisegrouping;
		this.qrurl = qrurl;
		this.servicedoctor = servicedoctor;
		this.readon = readon;
		this.currentmilliseconds = currentmilliseconds;
		this.createdtime = createdtime;
	}
	
	public int getAlertid() {
		return alertid;
	}
	public void setAlertid(int alertid) {
		this.alertid = alertid;
	}
	public String getRoomname() {
		return roomname;
	}
	public void setRoomname(String roomname) {
		this.roomname = roomname;
	}
	public int getAlertgroup() {
		return alertgroup;
	}
	public void setAlertgroup(int alertgroup) {
		this.alertgroup = alertgroup;
	}
	public int getParentid() {
		return parentid;
	}
	public void setParentid(int parentid) {
		this.parentid = parentid;
	}
	public int getModifiedbyid() {
		return modifiedbyid;
	}
	public void setModifiedbyid(int modifiedbyid) {
		this.modifiedbyid = modifiedbyid;
	}
	public int getReadbyid() {
		return readbyid;
	}
	public void setReadbyid(int readbyid) {
		this.readbyid = readbyid;
	}
	public String getRedirecturl() {
		return redirecturl;
	}
	public void setRedirecturl(String redirecturl) {
		this.redirecturl = redirecturl;
	}
	public int getChartid() {
		return chartid;
	}
	public void setChartid(int chartid) {
		this.chartid = chartid;
	}
	public int getFromid() {
		return fromid;
	}
	public void setFromid(int fromid) {
		this.fromid = fromid;
	}
	public Boolean getRead() {
		return read;
	}
	public void setRead(Boolean read) {
		this.read = read;
	}
	public String getModifiedbyname() {
		return modifiedbyname;
	}
	public void setModifiedbyname(String modifiedbyname) {
		this.modifiedbyname = modifiedbyname;
	}
	public int getEncounterid() {
		return encounterid;
	}
	public void setEncounterid(int encounterid) {
		this.encounterid = encounterid;
	}
	public int getToid() {
		return toid;
	}
	public void setToid(int toid) {
		this.toid = toid;
	}
	public String getAlert_category_url_caption() {
		return alert_category_url_caption;
	}
	public void setAlert_category_url_caption(String alert_category_url_caption) {
		this.alert_category_url_caption = alert_category_url_caption;
	}
	public String getForwardedby() {
		return forwardedby;
	}
	public void setForwardedby(String forwardedby) {
		this.forwardedby = forwardedby;
	}
	public int getAlerttype() {
		return alerttype;
	}
	public void setAlerttype(int alerttype) {
		this.alerttype = alerttype;
	}
	public int getSubpage() {
		return subpage;
	}
	public void setSubpage(int subpage) {
		this.subpage = subpage;
	}
	public String getModifiedon() {
		return modifiedon;
	}
	public void setModifiedon(String modifiedon) {
		this.modifiedon = modifiedon;
	}
	public String getCreateddate() {
		return createddate;
	}
	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}
	public String getReadbyname() {
		return readbyname;
	}
	public void setReadbyname(String readbyname) {
		this.readbyname = readbyname;
	}
	public String getPatientname() {
		return patientname;
	}
	public void setPatientname(String patientname) {
		this.patientname = patientname;
	}
	public int getPatientid() {
		return patientid;
	}
	public void setPatientid(int patientid) {
		this.patientid = patientid;
	}
	public String getReceivedto() {
		return receivedto;
	}
	public void setReceivedto(String receivedto) {
		this.receivedto = receivedto;
	}
	public Boolean getHighprivilage() {
		return highprivilage;
	}
	public void setHighprivilage(Boolean highprivilage) {
		this.highprivilage = highprivilage;
	}
	public String getPatientphnum() {
		return patientphnum;
	}
	public void setPatientphnum(String patientphnum) {
		this.patientphnum = patientphnum;
	}
	public int getRefid() {
		return refid;
	}
	public void setRefid(int refid) {
		this.refid = refid;
	}
	public int getAlertsection() {
		return alertsection;
	}
	public void setAlertsection(int alertsection) {
		this.alertsection = alertsection;
	}
	public String getPatientaccnum() {
		return patientaccnum;
	}
	public void setPatientaccnum(String patientaccnum) {
		this.patientaccnum = patientaccnum;
	}
	public int getQrflag() {
		return qrflag;
	}
	public void setQrflag(int qrflag) {
		this.qrflag = qrflag;
	}
	public String getDisplayname() {
		return displayname;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	public int getActionmap() {
		return actionmap;
	}
	public void setActionmap(int actionmap) {
		this.actionmap = actionmap;
	}
	public int getCategoryorder() {
		return categoryorder;
	}
	public void setCategoryorder(int categoryorder) {
		this.categoryorder = categoryorder;
	}
	public String getCategoryname() {
		return categoryname;
	}
	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}
	public Boolean getNeeddatewisegrouping() {
		return needdatewisegrouping;
	}
	public void setNeeddatewisegrouping(Boolean needdatewisegrouping) {
		this.needdatewisegrouping = needdatewisegrouping;
	}
	public String getQrurl() {
		return qrurl;
	}
	public void setQrurl(String qrurl) {
		this.qrurl = qrurl;
	}
	public String getServicedoctor() {
		return servicedoctor;
	}
	public void setServicedoctor(String servicedoctor) {
		this.servicedoctor = servicedoctor;
	}
	public String getReadon() {
		return readon;
	}
	public void setReadon(String readon) {
		this.readon = readon;
	}
	public Date getCurrentmilliseconds() {
		return currentmilliseconds;
	}
	public void setCurrentmilliseconds(Date currentmilliseconds) {
		this.currentmilliseconds = currentmilliseconds;
	}
	public String getCreatedtime() {
		return createdtime;
	}
	public void setCreatedtime(String createdtime) {
		this.createdtime = createdtime;
	}
	public String getCreateddatetime() {
		return createdtime;
	}
	public void setCreateddatetime(String createddatetime) {
		this.createddatetime = createddatetime;
	}
}
