package com.glenwood.glaceemr.server.application.services.investigation;

import java.util.List;

public class GroupName {

	List<LS_Lab> laboratories;
	List<LS_Lab> radiology;
	List<LS_Lab> miscellaneous;
	List<LS_Lab> procedures;
	public List<LS_Lab> getLaboratories() {
		return laboratories;
	}
	public void setLaboratories(List<LS_Lab> laboratories) {
		this.laboratories = laboratories;
	}
	public List<LS_Lab> getRadiology() {
		return radiology;
	}
	public void setRadiology(List<LS_Lab> radiology) {
		this.radiology = radiology;
	}
	public List<LS_Lab> getMiscellaneous() {
		return miscellaneous;
	}
	public void setMiscellaneous(List<LS_Lab> miscellaneous) {
		this.miscellaneous = miscellaneous;
	}
	public List<LS_Lab> getProcedures() {
		return procedures;
	}
	public void setProcedures(List<LS_Lab> procedures) {
		this.procedures = procedures;
	}
	
}
