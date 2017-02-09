package com.glenwood.glaceemr.server.application.services.portal.portalPayments;

import java.io.File;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.AlertCategory;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.Billinglookup;
import com.glenwood.glaceemr.server.application.models.CreditCardPaymentBean;
import com.glenwood.glaceemr.server.application.models.EnsBillsDetails;
import com.glenwood.glaceemr.server.application.models.H093;
import com.glenwood.glaceemr.server.application.models.H810;
import com.glenwood.glaceemr.server.application.models.IntermediateStmt;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.PaymentService;
import com.glenwood.glaceemr.server.application.models.PortalPatientPaymentsSummary;
import com.glenwood.glaceemr.server.application.models.PortalPatientStatementBean;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.ReceiptDetail;
import com.glenwood.glaceemr.server.application.models.ReceiptDetail_;
import com.glenwood.glaceemr.server.application.models.UserinformationCreditcard;
import com.glenwood.glaceemr.server.application.repositories.AlertCategoryRepository;
import com.glenwood.glaceemr.server.application.repositories.AlertEventRepository;
import com.glenwood.glaceemr.server.application.repositories.BillinglookupRepository;
import com.glenwood.glaceemr.server.application.repositories.EnsBillsDetailsRepository;
import com.glenwood.glaceemr.server.application.repositories.H093Repository;
import com.glenwood.glaceemr.server.application.repositories.H810Respository;
import com.glenwood.glaceemr.server.application.repositories.IntermediateStmtRepository;
import com.glenwood.glaceemr.server.application.repositories.NonServiceDetailsRespository;
import com.glenwood.glaceemr.server.application.repositories.PatientInsDetailRepository;
import com.glenwood.glaceemr.server.application.repositories.PaymentServiceRepository;
import com.glenwood.glaceemr.server.application.repositories.PosTableRepository;
import com.glenwood.glaceemr.server.application.repositories.ReceiptDetailRepository;
import com.glenwood.glaceemr.server.application.repositories.UserinformationCreditcardRepository;
import com.glenwood.glaceemr.server.application.services.portal.portalDocuments.SharedFolderUtil;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.PortalSettingsService;
import com.glenwood.glaceemr.server.application.specifications.AlertCategorySpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalAlertSpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalPaymentsSpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalSettingsSpecification;
import com.glenwood.glaceemr.server.utils.GeneratePDF;
import com.glenwood.glaceemr.server.utils.IngenixStatementPreview;

@Service
public class PortalPaymentsServiceImpl implements PortalPaymentsService{


	@Autowired
	NonServiceDetailsRespository nonServiceDetailsRespository;

	@Autowired
	ReceiptDetailRepository receiptDetailRepository;

	@Autowired
	UserinformationCreditcardRepository userinformationCreditcardRepository;

	@Autowired
	PaymentServiceRepository paymentServiceRepository;

	@Autowired
	PatientInsDetailRepository patientInsDetailRepository;

	@Autowired
	H093Repository h093Repository;

	@Autowired
	AlertEventRepository alertEventRepository;
	
	@Autowired
	AlertCategoryRepository alertCategoryRepository;

	@Autowired
	PortalSettingsService portalSettingsService;

	@Autowired
	H810Respository h810Respository;

	@Autowired
	IntermediateStmtRepository intermediateStmtRepository;

	@Autowired
	EnsBillsDetailsRepository ensBillsDetailsRepository;

	@Autowired
	BillinglookupRepository billinglookupRepository;
	
	@Autowired
	PosTableRepository posTableRepository;

	@Autowired
	EntityManager em;

	@Autowired
	EntityManagerFactory emf;

	@Override
	public List<H093> getPatientStatementHistory(int patientId, int chartId,int pageOffset, int pageIndex) {

		List<H093> statementHistoryList=h093Repository.findAll(PortalPaymentsSpecification.getPatientStatementHistory(patientId, chartId), PortalPaymentsSpecification.createPortalStatementHistoryPageRequestByDescDate(pageIndex, pageOffset)).getContent();

		return statementHistoryList;
	}

	@Override
	public List<ReceiptDetail> getPatientPaymentHistory(int patientId, int chartId, int pageOffset, int pageIndex) {

		List<ReceiptDetail> paymentHistoryList=receiptDetailRepository.findAll(PortalPaymentsSpecification.getPatientPaymentHistory(patientId, chartId), PortalPaymentsSpecification.createPortalPaymentHistoryPageRequestByDescDate(pageIndex, pageOffset)).getContent();

		return paymentHistoryList;
	}


	@Override
	public PortalPatientPaymentsSummary getPatientPaymentsSummary(int  patientId) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<PatientRegistration> root = cq.from(PatientRegistration.class);
		cq.select(builder.construct(PortalPatientPaymentsSummary.class,root.get(PatientRegistration_.patientRegistrationId),builder.function("ptbalance",Double.class , builder.literal(BigInteger.valueOf(patientId)))
				,builder.function("depositbalance",String.class , builder.literal(BigInteger.valueOf(patientId))),builder.function("insbalance",String.class , builder.literal(BigInteger.valueOf(patientId)),builder.literal(0)),
				root.get(PatientRegistration_.patientRegistrationCopay)));
		cq.where(builder.equal(root.get(PatientRegistration_.patientRegistrationId), patientId));
		List<Object> ptPaymentDetailsResultList=em.createQuery(cq).getResultList();
		List<PortalPatientPaymentsSummary> detailsBeanList = new ArrayList<PortalPatientPaymentsSummary>();
		for(int i=0;i<ptPaymentDetailsResultList.size();i++){
			PortalPatientPaymentsSummary detailsBean=(PortalPatientPaymentsSummary)ptPaymentDetailsResultList.get(i);
			detailsBeanList.add(detailsBean);
		}

		return detailsBeanList.get(0);
	}

	@Override
	public ReceiptDetail processPaymentTransaction(CreditCardPaymentBean paymentDetailsBean) {

		PaymentTransactionExecutionModel paymentProcessor=new PaymentTransactionExecutionModel();

		UserinformationCreditcard creditCardUserInfo=userinformationCreditcardRepository.findOne(PortalPaymentsSpecification.getUserinformationCreditcard(paymentDetailsBean.getPosId()));

		PaymentService paymentSummary=new PaymentService();

		ReceiptDetail receipt =new ReceiptDetail();

		try {
			ObjectMapper mapper=new ObjectMapper();
			paymentSummary=paymentProcessor.processCreditCardPayment(paymentDetailsBean, creditCardUserInfo.getUserinformationConnection(), creditCardUserInfo.getUserinformationDetails());
			/*Saving the Summary and retrieving the same summary from database*/
			paymentSummary=recordPaymentTransaction(paymentSummary);
			/*Adding a receipt entry in ReceiptDetail Table*/
			receipt.setReceiptDetailId(-1L);
			receipt.setReceiptDetailCreatedBy(paymentDetailsBean.getUsername());
			receipt.setReceiptDetailCreatedDate(paymentSummary.getPaymentServiceTime());
			receipt.setReceiptDetailCreditmethod(paymentDetailsBean.getTransactionType());
			receipt.setReceiptDetailDepositDate(new java.sql.Date(paymentSummary.getPaymentServiceTime().getTime()));
			receipt.setReceiptDetailDescription(paymentDetailsBean.getComments());
			receipt.setReceiptDetailIsActive(true);
			receipt.setReceiptDetailLastModifiedBy(paymentDetailsBean.getUsername());
			receipt.setReceiptDetailLastModifiedDate(paymentSummary.getPaymentServiceTime());
			receipt.setReceiptDetailPayerId(Long.valueOf(String.valueOf(paymentDetailsBean.getPatientId())));
			receipt.setReceiptDetailReferenceNo(paymentSummary.getPaymentServiceTransactionid()+"_"+paymentSummary.getPaymentServiceAuthcode());
			receipt.setReceiptDetailHowpaid("3");
			receipt.setReceiptDetailCheckDate(new java.sql.Date(paymentSummary.getPaymentServiceTime().getTime()));
			receipt.setReceiptDetailLocaluse("From Patient Portal");
			receipt.setReceiptDetailPayeeType(1);
			receipt.setReceiptDetailPaymentPlace(paymentDetailsBean.getPosName());
			
			List<Billinglookup> payerTypes=getPayerTypes();
			for(int i=0;i<payerTypes.size();i++){
				if(payerTypes.get(i).getBlookName().equalsIgnoreCase("Patient"))
					receipt.setReceiptDetailPayerType(payerTypes.get(i).getBlookIntid());
			}


			List<Billinglookup> paymentTypes=getPaymentTypes();
			for(int i=0;i<paymentTypes.size();i++){
				if(paymentTypes.get(i).getBlookName().equalsIgnoreCase("Credit Card"))
					receipt.setReceiptDetailPaymentMethod(paymentTypes.get(i).getBlookIntid());
			}
			receipt.setReceiptDetailPaymentPlace(paymentDetailsBean.getPosName());
			//TODO
			//receipt.setReceiptDetailPaymentReason(paymentDetailsBean.getReason());
			receipt.setReceiptDetailPostedAmt(0.00);
			receipt.setReceiptDetailReasonType(Short.valueOf("1"));
			receipt.setReceiptDetailReceiptAmt(paymentSummary.getPaymentServiceAmount());
			receipt.setReceiptDetailTotalReceiptAmt(paymentSummary.getPaymentServiceAmount());
			receiptDetailRepository.saveAndFlush(receipt);

		} catch (Exception e) {

			paymentSummary=null;
			e.printStackTrace();
		}

		H810 paymentAlertCategory=h810Respository.findOne(PortalAlertSpecification.getPortalAlertCategoryByName("Payment Alert"));
		boolean sendToAll =paymentAlertCategory.getH810005();  
		int provider = Integer.parseInt(paymentAlertCategory.getH810003());
		int forwardTo = Integer.parseInt(paymentAlertCategory.getH810004());

		AlertCategory alertCategory=alertCategoryRepository.findOne(AlertCategorySpecification.getAlertCategoryByName("Payment from Patient Portal"));
		
		AlertEvent alert=new AlertEvent();
		alert.setAlertEventCategoryId(alertCategory.getAlertCategoryId());
		alert.setAlertEventStatus(1);
		alert.setAlertEventPatientId(paymentDetailsBean.getPatientId());
		alert.setAlertEventPatientName(paymentDetailsBean.getUsername());
		alert.setAlertEventEncounterId(-1);
		alert.setAlertEventRefId(Integer.parseInt(String.valueOf(paymentSummary.getPaymentServiceTransactionid())));
		alert.setAlertEventMessage("Payment from Patient Portal.");
		alert.setAlertEventRoomId(-1);
		alert.setAlertEventCreatedDate(new Timestamp(new Date().getTime()));
		alert.setAlertEventModifiedby(-100);
		alert.setAlertEventFrom(-100);


		if(provider==0 && forwardTo==0)
			alert.setAlertEventTo(-1);
		else {
			if(sendToAll){
				if(forwardTo!=provider){
					alert.setAlertEventTo(forwardTo);
					AlertEvent alert2=alert;
					alert2.setAlertEventTo(provider);
					alertEventRepository.saveAndFlush(alert2);
				} else {
					alert.setAlertEventTo(forwardTo);
				}            	 
			}else{
				if(forwardTo!=0){
					alert.setAlertEventTo(forwardTo);
				} else {
					alert.setAlertEventTo(provider);
				}
			}
		}

		alertEventRepository.saveAndFlush(alert);

		return receipt;
	}

	public Long getNewReceiptDetailId() {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<ReceiptDetail> root = cq.from(ReceiptDetail.class);
		cq.select(builder.max(root.get(ReceiptDetail_.receiptDetailId)));
		Long receiptDetailId=(Long) em.createQuery(cq).getSingleResult();

		return receiptDetailId+1;
	}

	@Override
	public PaymentService recordPaymentTransaction(PaymentService paymentService) {

		PaymentService paymentSummary=paymentServiceRepository.saveAndFlush(paymentService);

		return paymentSummary;
	}

	@Override
	public PaymentService getPatientLastPaymentSummary(int patientId) {

		PaymentService paymentSummary=paymentServiceRepository.findOne(PortalPaymentsSpecification.getLastPaymentSummary(patientId));

		return paymentSummary;
	}

	@Override
	public List<PatientInsDetail> getPatientInsDetails(int patientId) {

		List<PatientInsDetail> insList=patientInsDetailRepository.findAll(PortalPaymentsSpecification.getPatientInsDetails(patientId));
		return insList;
	}

	@Override
	public PortalPatientStatementBean getPatientStatementFileDetails(int patientId, int billId, String billType) throws Exception {

		PortalPatientStatementBean stmntBean = new PortalPatientStatementBean();
		if(!billType.equalsIgnoreCase("IM")){
			H093 patientStatement=h093Repository.findOne(PortalPaymentsSpecification.getPatientStatementFileDetails(patientId, billId));
			stmntBean.setBatchNo(patientStatement.getEnsBatchNo());

			if(stmntBean.getBatchNo()==0){

				stmntBean.setStatementPath(patientStatement.getFilePath());

				Document consentForm=Jsoup.parse(new File(SharedFolderUtil.getSharedFolderPath()+patientStatement.getFilePath()), "UTF-8");

				stmntBean.setHtmStatementContent(consentForm.outerHtml());

				return stmntBean;
			}
		}
		else{
			IntermediateStmt intermediateStmt= intermediateStmtRepository.findOne(PortalPaymentsSpecification.getPatientIMStatementFileDetails(patientId, billId));
			stmntBean.setBatchNo(intermediateStmt.getIntermediateStmtTrackerid());
		}

		EnsBillsDetails ensBillsDetails=ensBillsDetailsRepository.findOne(PortalPaymentsSpecification.getStatementDetailsByBatchNo(stmntBean.getBatchNo()));

		String stmtPath =null;


		stmntBean.setStatementType(ensBillsDetails.getStmntType());

		if((ensBillsDetails.getPreviewFlag()==1)){
			String fileName = ensBillsDetails.getStmntPath();
			String stmtType = ensBillsDetails.getStmntType();
			fileName = fileName.replaceAll("\\\\","/"); 
			stmtPath = fileName.substring(fileName.lastIndexOf("/")+1,fileName.length());
			stmtPath = "ENSSTMNTS/"+ stmtPath.replace(".xml",".pdf");
			if(!stmtType.equalsIgnoreCase("IM"))
				stmtPath = stmtPath.replace(".pdf", "_"+stmntBean.getBatchNo()+".pdf");
			stmntBean.setStatementPath(stmtPath);

		} 
		else{

			String fileSeperator =  System.getProperty("file.separator");
			String sharedPath=SharedFolderUtil.getSharedFolderPath();

			String fileName=ensBillsDetails.getStmntPath();

			fileName = fileName.replaceAll("\\\\","/");
			stmtPath = fileName.substring(fileName.lastIndexOf("/")+1,fileName.length());
			String stmtType=ensBillsDetails.getStmntType();
			stmtPath =sharedPath+fileSeperator+"ENSSTMNTS"+fileSeperator+ stmtPath;

			if(!stmtType.equalsIgnoreCase("IM"))
				stmtPath=stmtPath.replace(".xml", "_"+stmntBean.getBatchNo()+".xml");
			//stmtPath = sharedPath + fileName;
			stmtPath = stmtPath.replaceAll("\\\\","/");
			String stmtImage = "ensptstmt.png";

			//call stmnt preview model here
			//GlaceOutLoggingStream.console("path->" + stmtPath);
			GeneratePDF ff=new GeneratePDF(stmtImage);
			String tempfilename =stmtPath.replace(".xml",patientId+"_temp.pdf");
			ff.setTempFileName(tempfilename);  // first call temp file

			IngenixStatementPreview isp =new IngenixStatementPreview();
			Hashtable temphst=new Hashtable();
			if(stmtType.equalsIgnoreCase("IM"))
				ff.setStmtType("IM");
			temphst=isp.parsingXML(stmtPath);

			Hashtable rst=temphst;
			if(!stmtType.equalsIgnoreCase("IM"))
				rst=isp.singleAccPreview(temphst, String.valueOf(patientId));
			ff.writeFile(rst);

			ff.addWaterMark(stmtPath.replace(".xml",patientId+".pdf"));
			ff.setTempFileName(sharedPath+fileSeperator+"ENSSTMNTS"+fileSeperator+patientId+"temp.pdf");
			ff.delTempFile(tempfilename);

			stmtPath = stmtPath.replaceAll("\\\\","/");
			stmtPath = stmtPath.substring(stmtPath.lastIndexOf("/")+1,stmtPath.length());

			stmtPath = "ENSSTMNTS"+"/"+ stmtPath.replace(".xml",patientId+".pdf");

			stmntBean.setStatementPath(stmtPath);
		}

		return stmntBean;
	}


	@Override
	public List<Billinglookup> getPaymentTypes(){

		return billinglookupRepository.findAll(PortalSettingsSpecification.getPaymentMethod());

	}

	@Override
	public List<Billinglookup> getPayerTypes(){

		return billinglookupRepository.findAll(PortalSettingsSpecification.getPayerType());

	}

	@Override
	public List<PosTable> getTransFirstConfiguredPosList() {
		
		return posTableRepository.findAll(PortalPaymentsSpecification.getTransFirstConfiguredPos());
	}

}