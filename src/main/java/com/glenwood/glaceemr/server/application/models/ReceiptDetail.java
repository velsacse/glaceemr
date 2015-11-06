package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "receipt_detail")
public class ReceiptDetail {

	@Id
	@Column(name="receipt_detail_id")
	private Long receiptDetailId;

	@Column(name="receipt_detail_reference_no")
	private String receiptDetailReferenceNo;

	@Column(name="receipt_detail_deposit_date")
	private Date receiptDetailDepositDate;

	@Column(name="receipt_detail_payer_id")
	private Long receiptDetailPayerId;

	@Column(name="receipt_detail_receipt_amt")
	private Double receiptDetailReceiptAmt;

	@Column(name="receipt_detail_posted_amt")
	private Double receiptDetailPostedAmt;

	@Column(name="receipt_detail_isadded_in_service")
	private Boolean receiptDetailIsaddedInService;

	@Column(name="receipt_detail_last_modified_by")
	private String receiptDetailLastModifiedBy;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="receipt_detail_last_modified_date")
	private Timestamp receiptDetailLastModifiedDate;

	@Column(name="receipt_detail_reference")
	private String receiptDetailReference;

	@Column(name="h555555")
	private Integer h555555;

	@Column(name="receipt_detail_check_date")
	private Date receiptDetailCheckDate;

	@Column(name="receipt_detail_description")
	private String receiptDetailDescription;

	@Column(name="receipt_detail_payer_type")
	private Short receiptDetailPayerType;

	@Column(name="receipt_detail_payment_method")
	private Short receiptDetailPaymentMethod;

	@Column(name="receipt_detail_is_copay")
	private Boolean receiptDetailIsCopay;

	@Column(name="receipt_detail_localuse")
	private String receiptDetailLocaluse;

	@Column(name="receipt_detail_is_active")
	private Boolean receiptDetailIsActive;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="receipt_detail_created_date")
	private Timestamp receiptDetailCreatedDate;

	@Column(name="receipt_detail_created_by")
	private String receiptDetailCreatedBy;

	@Column(name="receipt_detail_payment_code")
	private Integer receiptDetailPaymentCode;

	@Column(name="receipt_detail_adjustment_code")
	private Integer receiptDetailAdjustmentCode;

	@Column(name="receipt_detail_withhold_code")
	private Integer receiptDetailWithholdCode;

	@Column(name="receipt_detail_deductible_code")
	private Integer receiptDetailDeductibleCode;

	@Column(name="receipt_detail_refund_code")
	private Integer receiptDetailRefundCode;

	@Column(name="receipt_detail_denial_code")
	private Integer receiptDetailDenialCode;

	@Column(name="receipt_detail_custom_field1")
	private String receiptDetailCustomField1;

	@Column(name="receipt_detail_custom_field2")
	private Date receiptDetailCustomField2;

	@Column(name="receipt_detail_custom_field3")
	private Double receiptDetailCustomField3;

	@Column(name="receipt_detail_payee_type")
	private Integer receiptDetailPayeeType;

	@Column(name="receipt_detail_payee_id")
	private Integer receiptDetailPayeeId;

	@Column(name="receipt_detail_total_receipt_amt")
	private Double receiptDetailTotalReceiptAmt;

	@Column(name="receipt_detail_payment_reason")
	private Double receiptDetailPaymentReason;

	@Column(name="receipt_detail_given_for_dos")
	private Date receiptDetailGivenForDos;

	@Column(name="receipt_detail_refund_amount")
	private Double receiptDetailRefundAmount;

	@Column(name="receipt_detail_auth_no")
	private String receiptDetailAuthNo;

	@Column(name="receipt_detail_writeoff_amount")
	private Double receiptDetailWriteoffAmount;

	@Column(name="receipt_detail_writeoff_reason")
	private String receiptDetailWriteoffReason;

	@Column(name="receipt_detail_reason_type")
	private Short receiptDetailReasonType;

	@Column(name="receipt_detail_howpaid")
	private String receiptDetailHowpaid;

	@Column(name="receipt_detail_payment_place")
	private String receiptDetailPaymentPlace;
	
	@Column(name="receipt_detail_creditmethod")
	private String receiptDetailCreditmethod;
}