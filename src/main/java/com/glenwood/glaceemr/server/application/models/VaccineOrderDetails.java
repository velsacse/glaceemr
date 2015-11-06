package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "vaccine_order_details")
public class VaccineOrderDetails {

	@Id
	@Column(name="vaccine_order_details_id")
	private Integer vaccineOrderDetailsId;

	@Column(name="vaccine_order_details_order_id")
	private Integer vaccineOrderDetailsOrderId;

	@Column(name="vaccine_order_details_vaccine_id")
	private Integer vaccineOrderDetailsVaccineId;

	@Column(name="vaccine_order_details_lot_no")
	private String vaccineOrderDetailsLotNo;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="vaccine_order_details_expiry")
	private Timestamp vaccineOrderDetailsExpiry;

	@Column(name="vaccine_order_details_qty")
	private Integer vaccineOrderDetailsQty;

	@Column(name="vaccine_order_details_qty_used")
	private Integer vaccineOrderDetailsQtyUsed;

	@Column(name="vaccine_order_details_qty_reconcile")
	private Integer vaccineOrderDetailsQtyReconcile;

	@Column(name="vaccine_order_details_type")
	private Integer vaccineOrderDetailsType;

	@Column(name="vaccine_order_details_ischdp")
	private Integer vaccineOrderDetailsIschdp;

	@Column(name="vaccine_order_details_unknown")
	private Integer vaccineOrderDetailsUnknown;

	@Column(name="vaccine_order_details_isactive")
	private Boolean vaccineOrderDetailsIsactive;
		
	@ManyToOne(fetch=FetchType.EAGER)
	@JsonManagedReference
	@JoinColumn(name="vaccine_order_details_order_id", referencedColumnName="vaccine_order_id" , insertable=false, updatable=false)
	private VaccineOrder vaccineOrder;
		
	public VaccineOrder getVaccineOrder() {
		return vaccineOrder;
	}

	public void setVaccineOrder(VaccineOrder vaccineOrder) {
		this.vaccineOrder = vaccineOrder;
	}

	public Integer getVaccineOrderDetailsId() {
		return vaccineOrderDetailsId;
	}

	public void setVaccineOrderDetailsId(Integer vaccineOrderDetailsId) {
		this.vaccineOrderDetailsId = vaccineOrderDetailsId;
	}

	public Integer getVaccineOrderDetailsOrderId() {
		return vaccineOrderDetailsOrderId;
	}

	public void setVaccineOrderDetailsOrderId(Integer vaccineOrderDetailsOrderId) {
		this.vaccineOrderDetailsOrderId = vaccineOrderDetailsOrderId;
	}

	public Integer getVaccineOrderDetailsVaccineId() {
		return vaccineOrderDetailsVaccineId;
	}

	public void setVaccineOrderDetailsVaccineId(Integer vaccineOrderDetailsVaccineId) {
		this.vaccineOrderDetailsVaccineId = vaccineOrderDetailsVaccineId;
	}

	public String getVaccineOrderDetailsLotNo() {
		return vaccineOrderDetailsLotNo;
	}

	public void setVaccineOrderDetailsLotNo(String vaccineOrderDetailsLotNo) {
		this.vaccineOrderDetailsLotNo = vaccineOrderDetailsLotNo;
	}

	public Timestamp getVaccineOrderDetailsExpiry() {
		return vaccineOrderDetailsExpiry;
	}

	public void setVaccineOrderDetailsExpiry(Timestamp vaccineOrderDetailsExpiry) {
		this.vaccineOrderDetailsExpiry = vaccineOrderDetailsExpiry;
	}

	public Integer getVaccineOrderDetailsQty() {
		return vaccineOrderDetailsQty;
	}

	public void setVaccineOrderDetailsQty(Integer vaccineOrderDetailsQty) {
		this.vaccineOrderDetailsQty = vaccineOrderDetailsQty;
	}

	public Integer getVaccineOrderDetailsQtyUsed() {
		return vaccineOrderDetailsQtyUsed;
	}

	public void setVaccineOrderDetailsQtyUsed(Integer vaccineOrderDetailsQtyUsed) {
		this.vaccineOrderDetailsQtyUsed = vaccineOrderDetailsQtyUsed;
	}

	public Integer getVaccineOrderDetailsQtyReconcile() {
		return vaccineOrderDetailsQtyReconcile;
	}

	public void setVaccineOrderDetailsQtyReconcile(
			Integer vaccineOrderDetailsQtyReconcile) {
		this.vaccineOrderDetailsQtyReconcile = vaccineOrderDetailsQtyReconcile;
	}

	public Integer getVaccineOrderDetailsType() {
		return vaccineOrderDetailsType;
	}

	public void setVaccineOrderDetailsType(Integer vaccineOrderDetailsType) {
		this.vaccineOrderDetailsType = vaccineOrderDetailsType;
	}

	public Integer getVaccineOrderDetailsIschdp() {
		return vaccineOrderDetailsIschdp;
	}

	public void setVaccineOrderDetailsIschdp(Integer vaccineOrderDetailsIschdp) {
		this.vaccineOrderDetailsIschdp = vaccineOrderDetailsIschdp;
	}

	public Integer getVaccineOrderDetailsUnknown() {
		return vaccineOrderDetailsUnknown;
	}

	public void setVaccineOrderDetailsUnknown(Integer vaccineOrderDetailsUnknown) {
		this.vaccineOrderDetailsUnknown = vaccineOrderDetailsUnknown;
	}

	public Boolean getVaccineOrderDetailsIsactive() {
		return vaccineOrderDetailsIsactive;
	}

	public void setVaccineOrderDetailsIsactive(Boolean vaccineOrderDetailsIsactive) {
		this.vaccineOrderDetailsIsactive = vaccineOrderDetailsIsactive;
	}
}