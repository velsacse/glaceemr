package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "billinglookup")
public class Billinglookup {

	@Id
	@Column(name="blook_id")
	private Integer blookId;

	@Column(name="blook_group")
	private Integer blookGroup;

	@Column(name="blook_intid")
	private Integer blookIntid;

	@Column(name="blook_charid")
	private String blookCharid;

	@Column(name="blook_name")
	private String blookName;

	@Column(name="blook_desc")
	private String blookDesc;

	@Column(name="blook_extra")
	private String blookExtra;

	@Column(name="blook_subgroup")
	private Integer blookSubgroup;

	@Column(name="blook_orderby")
	private Integer blookOrderby;

	@Column(name="blook_isactive")
	private Boolean blookIsactive;

	@Column(name="billinglookup_keycode")
	private String billinglookupKeycode;

	public Integer getBlookId() {
		return blookId;
	}

	public void setBlookId(Integer blookId) {
		this.blookId = blookId;
	}

	public Integer getBlookGroup() {
		return blookGroup;
	}

	public void setBlookGroup(Integer blookGroup) {
		this.blookGroup = blookGroup;
	}

	public Integer getBlookIntid() {
		return blookIntid;
	}

	public void setBlookIntid(Integer blookIntid) {
		this.blookIntid = blookIntid;
	}

	public String getBlookCharid() {
		return blookCharid;
	}

	public void setBlookCharid(String blookCharid) {
		this.blookCharid = blookCharid;
	}

	public String getBlookName() {
		return blookName;
	}

	public void setBlookName(String blookName) {
		this.blookName = blookName;
	}

	public String getBlookDesc() {
		return blookDesc;
	}

	public void setBlookDesc(String blookDesc) {
		this.blookDesc = blookDesc;
	}

	public String getBlookExtra() {
		return blookExtra;
	}

	public void setBlookExtra(String blookExtra) {
		this.blookExtra = blookExtra;
	}

	public Integer getBlookSubgroup() {
		return blookSubgroup;
	}

	public void setBlookSubgroup(Integer blookSubgroup) {
		this.blookSubgroup = blookSubgroup;
	}

	public Integer getBlookOrderby() {
		return blookOrderby;
	}

	public void setBlookOrderby(Integer blookOrderby) {
		this.blookOrderby = blookOrderby;
	}

	public Boolean getBlookIsactive() {
		return blookIsactive;
	}

	public void setBlookIsactive(Boolean blookIsactive) {
		this.blookIsactive = blookIsactive;
	}

	public String getBillinglookupKeycode() {
		return billinglookupKeycode;
	}

	public void setBillinglookupKeycode(String billinglookupKeycode) {
		this.billinglookupKeycode = billinglookupKeycode;
	}
	
}