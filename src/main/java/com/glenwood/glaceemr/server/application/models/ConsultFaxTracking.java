package com.glenwood.glaceemr.server.application.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "consult_fax_tracking")

@IdClass(value = consultFaxTrackingPK.class)
public class ConsultFaxTracking {

	
	@Id
	private Integer encounterid;
	
	@Id
	private Integer leaf_id;

	@Id
	private Integer fax_id;
	

	@Column(name="chart_id")
	private Integer chartId;

	@Column(name="leaf_name")
	private String leafName;

	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY) 
	@JoinColumn(name="fax_id", referencedColumnName="h496001" , insertable=false, updatable=false)
	private H496 h496;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY) 
	@JoinColumn(name="leaf_id", referencedColumnName="leaf_library_id" , insertable=false, updatable=false)
	private LeafLibrary leafLibrary;
	
	
	
	
	public Integer getFaxid() {
		return fax_id;
	}

	public void setFaxid(Integer fax_id) {
		this.fax_id = fax_id;
	}

	public LeafLibrary getLeafLibrary() {
		return leafLibrary;
	}

	public void setLeafLibrary(LeafLibrary leafLibrary) {
		this.leafLibrary = leafLibrary;
	}

	public H496 getH496() {
		return h496;
	}

	public void setH496(H496 h496) {
		this.h496 = h496;
	}





	public Integer getChartId() {
		return chartId;
	}

	public void setChartId(Integer chartId) {
		this.chartId = chartId;
	}

	public Integer getEncounterid() {
		return encounterid;
	}

	public void setEncounterid(Integer encounterid) {
		this.encounterid = encounterid;
	}

	public Integer getLeafid() {
		return leaf_id;
	}

	public void setLeafid(Integer leaf_id) {
		this.leaf_id = leaf_id;
	}

	public String getLeafName() {
		return leafName;
	}

	public void setLeafName(String leafName) {
		this.leafName = leafName;
	}
	
	
}