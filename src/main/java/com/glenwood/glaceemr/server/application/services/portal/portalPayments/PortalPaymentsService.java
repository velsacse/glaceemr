package com.glenwood.glaceemr.server.application.services.portal.portalPayments;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.Billinglookup;
import com.glenwood.glaceemr.server.application.models.CreditCardPaymentBean;
import com.glenwood.glaceemr.server.application.models.H093;
import com.glenwood.glaceemr.server.application.models.NonServiceDetails;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PortalPatientStatementBean;
import com.glenwood.glaceemr.server.application.models.PaymentService;
import com.glenwood.glaceemr.server.application.models.PortalPatientPaymentsSummary;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.ReceiptDetail;

public interface PortalPaymentsService {

	List<H093> getPatientStatementHistory(int patientId, int chartId, int pageOffset, int pageIndex);
	
	List<ReceiptDetail> getPatientPaymentHistory(int patientId, int chartId, int pageOffset, int pageIndex);

	PortalPatientPaymentsSummary getPatientPaymentsSummary(int patientId);

	ReceiptDetail processPaymentTransaction(CreditCardPaymentBean paymentDetailsBean);
	
	PaymentService recordPaymentTransaction(PaymentService paymentService);

	PaymentService getPatientLastPaymentSummary(int patientId);

	List<PatientInsDetail> getPatientInsDetails(int patientId);
	
	PortalPatientStatementBean getPatientStatementFileDetails(int patientId, int batchNo, String billType) throws Exception;
	
	List<Billinglookup> getPaymentTypes();
	
	List<Billinglookup> getPayerTypes();
	
	List<PosTable> getTransFirstConfiguredPosList();

}
