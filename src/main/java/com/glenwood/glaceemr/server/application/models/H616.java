package com.glenwood.glaceemr.server.application.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "h616")
public class H616 {

	@Column(name="h616001")
	private Integer h616001;

	@Id
	@Column(name="h616002")
	private String h616002;

	@Column(name="h616003")
	private Boolean h616003;
	
	
	@OneToMany(mappedBy="h616")
	List<LeafLibrary> leafLibraries;
	
	
	public List<LeafLibrary> getLeafLibraries() {
		return leafLibraries;
	}

	public void setLeafLibraries(List<LeafLibrary> leafLibraries) {
		this.leafLibraries = leafLibraries;
	}

	public Integer getH616001() {
		return h616001;
	}

	public void setH616001(Integer h616001) {
		this.h616001 = h616001;
	}

	public String getH616002() {
		return h616002;
	}

	public void setH616002(String h616002) {
		this.h616002 = h616002;
	}

	public Boolean getH616003() {
		return h616003;
	}

	public void setH616003(Boolean h616003) {
		this.h616003 = h616003;
	}
	
	
}