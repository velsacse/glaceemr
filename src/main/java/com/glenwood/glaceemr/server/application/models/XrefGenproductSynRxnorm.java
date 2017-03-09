package com.glenwood.glaceemr.server.application.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "xref_genproduct_syn_rxnorm")
public class XrefGenproductSynRxnorm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	private Integer id;

	@Column(name="genproduct_id")
	private String genproductId;

	@Column(name="rxcui")
	private String rxcui;

	@Column(name="rxnorm_tty")
	private String rxnormTty;

	@Column(name="drug_syn_id")
	private String drugSynId;

	@Column(name="create_date")
	private String createDate;

	@Column(name="update_date")
	private String updateDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGenproductId() {
		return genproductId;
	}

	public void setGenproductId(String genproductId) {
		this.genproductId = genproductId;
	}

	public String getRxcui() {
		return rxcui;
	}

	public void setRxcui(String rxcui) {
		this.rxcui = rxcui;
	}

	public String getRxnormTty() {
		return rxnormTty;
	}

	public void setRxnormTty(String rxnormTty) {
		this.rxnormTty = rxnormTty;
	}

	public String getDrugSynId() {
		return drugSynId;
	}

	public void setDrugSynId(String drugSynId) {
		this.drugSynId = drugSynId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
}