package com.glenwood.glaceemr.server.application.services.portal.portalPayments;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
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
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.AlertCategory;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.Billinglookup;
import com.glenwood.glaceemr.server.application.models.CreditCardPaymentBean;
import com.glenwood.glaceemr.server.application.models.EnsBillsDetails;
import com.glenwood.glaceemr.server.application.models.GeneratedBillsHistoryDetails;
import com.glenwood.glaceemr.server.application.models.PatientPortalAlertConfig;
import com.glenwood.glaceemr.server.application.models.InitialSettings;
import com.glenwood.glaceemr.server.application.models.IntermediateStmt;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.PaymentResponse;
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
import com.glenwood.glaceemr.server.application.repositories.GeneratedBillsHistoryDetailsRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientPortalAlertConfigRespository;
import com.glenwood.glaceemr.server.application.repositories.InitialSettingsRepository;
import com.glenwood.glaceemr.server.application.repositories.IntermediateStmtRepository;
import com.glenwood.glaceemr.server.application.repositories.NonServiceDetailsRespository;
import com.glenwood.glaceemr.server.application.repositories.PatientInsDetailRepository;
import com.glenwood.glaceemr.server.application.repositories.PaymentServiceRepository;
import com.glenwood.glaceemr.server.application.repositories.PosTableRepository;
import com.glenwood.glaceemr.server.application.repositories.ReceiptDetailRepository;
import com.glenwood.glaceemr.server.application.repositories.UserinformationCreditcardRepository;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailSaveService;
import com.glenwood.glaceemr.server.application.services.portal.portalDocuments.SharedFolderUtil;
import com.glenwood.glaceemr.server.application.services.portal.portalMedicalSummary.PortalMedicalSummaryService;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.PortalSettingsService;
import com.glenwood.glaceemr.server.application.specifications.AlertCategorySpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalAlertSpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalPaymentsSpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalSettingsSpecification;
import com.glenwood.glaceemr.server.utils.GeneratePDF;
import com.glenwood.glaceemr.server.utils.IngenixStatementPreview;
import com.glenwood.glaceemr.server.utils.MultipartUtility;

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
     GeneratedBillsHistoryDetailsRepository h093Repository;

     @Autowired
     AlertEventRepository alertEventRepository;

     @Autowired
     AlertCategoryRepository alertCategoryRepository;

     @Autowired
     PortalSettingsService portalSettingsService;

     @Autowired
     PatientPortalAlertConfigRespository h810Respository;

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

     @Autowired
     AuditTrailSaveService auditTrailSaveService;

     @Autowired
     HttpServletRequest request;

     @Autowired
     PortalMedicalSummaryService portalMedicalSummaryService;
     
     @Autowired
     WebApplicationContext applicationContext;
     
     @Autowired
     InitialSettingsRepository initialSettingsRepository;

     @Override
     public List<GeneratedBillsHistoryDetails> getPatientStatementHistory(int patientId, int chartId,int pageOffset, int pageIndex) {

         List<GeneratedBillsHistoryDetails> statementHistoryList=h093Repository.findAll(PortalPaymentsSpecification.getPatientStatementHistory(patientId, chartId), PortalPaymentsSpecification.createPortalStatementHistoryPageRequestByDescDate(pageIndex, pageOffset)).getContent();

         return statementHistoryList;
     }

     @Override
     public List<ReceiptDetail> getPatientPaymentHistory(int patientId, int chartId, int pageOffset, int pageIndex) {

         List<ReceiptDetail> paymentHistoryList=receiptDetailRepository.findAll(PortalPaymentsSpecification.getPatientPaymentHistory(patientId, chartId), PortalPaymentsSpecification.createPortalPaymentHistoryPageRequestByDescDate(pageIndex, pageOffset)).getContent();

         return paymentHistoryList;
     }


     @Override
     public PortalPatientPaymentsSummary getPatientPaymentsSummary(int patientId) {

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
     public PaymentResponse processPaymentTransaction(CreditCardPaymentBean paymentDetailsBean) throws JSONException, IOException {

         PaymentTransactionExecutionModel paymentProcessor=new PaymentTransactionExecutionModel();

         UserinformationCreditcard creditCardUserInfo=userinformationCreditcardRepository.findOne(PortalPaymentsSpecification.getUserinformationCreditcard(paymentDetailsBean.getPosId()));

         PaymentService paymentSummary=new PaymentService();

         ReceiptDetail receipt =new ReceiptDetail();
         
         PaymentResponse paymentResponse=applicationContext.getBean(PaymentResponse.class);

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
             receipt=receiptDetailRepository.saveAndFlush(receipt);
             if(paymentSummary.getPaymentServiceStatus().equalsIgnoreCase("success"))
             	paymentResponse.setPaymentSuccess(true);
             else
             	paymentResponse.setPaymentSuccess(false);

             paymentResponse.setMessage("You payment has been processed successfully.");
             paymentResponse.setReceipt(receipt);

         } catch (Exception e) {

         	e.printStackTrace();
             paymentSummary=null;
             paymentResponse.setPaymentSuccess(false);
             paymentResponse.setMessage("Error in processing payment request.");
             paymentResponse.setEmailSent(false);
             return paymentResponse;
             
         }

         PatientPortalAlertConfig paymentAlertCategory=h810Respository.findOne(PortalAlertSpecification.getPortalAlertCategoryByName("Payment Alert"));
         boolean sendToAll =paymentAlertCategory.getpatient_portal_alert_config_send_to_all();
         int provider = Integer.parseInt(paymentAlertCategory.getpatient_portal_alert_config_provider());
         int forwardTo = Integer.parseInt(paymentAlertCategory.getpatient_portal_alert_config_forward_to());

         AlertCategory alertCategory=alertCategoryRepository.findOne(AlertCategorySpecification.getAlertCategoryByName("Payment from Patient Portal"));

         AlertEvent alert1=new AlertEvent();
         alert1.setAlertEventCategoryId(alertCategory.getAlertCategoryId());
         alert1.setAlertEventStatus(1);
         alert1.setAlertEventPatientId(paymentDetailsBean.getPatientId());
         alert1.setAlertEventPatientName(paymentDetailsBean.getUsername());
         alert1.setAlertEventEncounterId(-1);
         alert1.setAlertEventRefId(Integer.parseInt(String.valueOf(paymentSummary.getPaymentServiceTransactionid())));
         alert1.setAlertEventMessage("Payment from Patient Portal.");
         alert1.setAlertEventRoomId(-1);
         alert1.setAlertEventCreatedDate(new Timestamp(new Date().getTime()));
         alert1.setAlertEventModifiedby(-100);
         alert1.setAlertEventFrom(-100);


         if(provider==0 && forwardTo==0)
             alert1.setAlertEventTo(-1);
         else {
             if(sendToAll){
                 if(forwardTo!=provider){
                     alert1.setAlertEventTo(forwardTo);
                     AlertEvent alert2=new AlertEvent();
                     alert2.setAlertEventCategoryId(alertCategory.getAlertCategoryId());
                     alert2.setAlertEventStatus(1);
                     alert2.setAlertEventPatientId(paymentDetailsBean.getPatientId());
                     alert2.setAlertEventPatientName(paymentDetailsBean.getUsername());
                     alert2.setAlertEventEncounterId(-1);
                     alert2.setAlertEventRefId(Integer.parseInt(String.valueOf(paymentSummary.getPaymentServiceTransactionid())));
                     alert2.setAlertEventMessage("Payment from Patient Portal.");
                     alert2.setAlertEventRoomId(-1);
                     alert2.setAlertEventCreatedDate(new Timestamp(new Date().getTime()));
                     alert2.setAlertEventModifiedby(-100);
                     alert2.setAlertEventFrom(-100);
                     alert2.setAlertEventTo(provider);
                     alertEventRepository.saveAndFlush(alert2);
                 } else {
                     alert1.setAlertEventTo(forwardTo);
                 }
             }else{
                 if(forwardTo!=0){
                     alert1.setAlertEventTo(forwardTo);
                 } else {
                     alert1.setAlertEventTo(provider);
                 }
             }
         }

         alertEventRepository.saveAndFlush(alert1);

         auditTrailSaveService.LogEvent(AuditTrailEnumConstants.LogType.GLACE_LOG,AuditTrailEnumConstants.LogModuleType.PATIENTPORTAL,
                 AuditTrailEnumConstants.LogActionType.GENERATE,1,AuditTrailEnumConstants.Log_Outcome.SUCCESS,"Patient with id "+paymentDetailsBean.getPatientId()+" performed a credit card payment transaction through Patient Portal.",-1,
                 request.getRemoteAddr(),paymentDetailsBean.getPatientId(),"",
                 AuditTrailEnumConstants.LogUserType.PATIENT_LOGIN,"Patient with id "+paymentDetailsBean.getPatientId()+" performed a credit card payment transaction through Patient Portal.","");

         if(paymentDetailsBean.getUserEmail()!=null&&!paymentDetailsBean.getUserEmail().equalsIgnoreCase("")){
             
             InitialSettings practiceDetails=initialSettingsRepository.findOne(PortalSettingsSpecification.getPracticeDetails("Practice Name"));

     		String URL ="https://mailer1.glaceemr.com/Mailer/sendMail";

     		String charSet = "UTF-8";

     		String boundary = "===" + System.currentTimeMillis() + "===";

     		MultipartUtility multipartUtility=new MultipartUtility(URL, charSet, boundary);

     		HttpURLConnection httpURLConnection;
     		String mailpassword="demopwd0";

     		int mailtype=1;

     		String subject="Payment from Patient Portal";

     		JSONArray toIds = new JSONArray();
     		toIds.put(paymentDetailsBean.getUserEmail());

     		JSONArray ccids = new JSONArray();

     		JSONArray bccids = new JSONArray();
     		bccids.put("");

     		String accountId="Glenwood";


     		String htmlbody="<html><head></head>"+
     				"<body style='width:100%;color:#1e1e1e;font-size:16px;'>"+
     				"<label style='width:100%;padding:10px 5px;'>Dear "+paymentDetailsBean.getUsername()+",</label>"+
     				"<br><br>"+
     				"<label> Your payment amount of $"+paymentDetailsBean.getPaymentAmount()+" has processed successully. Please login into portal to get print of your payment.</label>"+
     				"<br><br>"+
     				"<label style='width:100%;padding:10px 5px;'>Thanks and Regards,</label>"+
     				"<br>"+
     				"<label style='width:100%;padding:10px 5px;'>"+WordUtils.capitalizeFully(practiceDetails.getInitialSettingsOptionValue())+".</label>"+
     				"<label></label></body></html>";

     		String plaintext="";


     		JSONObject jsonInString = new JSONObject();

     		jsonInString.put("mailtype", mailtype);

     		jsonInString.put("sender","donotreply@glenwoodsystems.com");

     		jsonInString.put("to",toIds);

     		jsonInString.put("cc", ccids);

     		jsonInString.put("bcc", bccids);

     		jsonInString.put("subject",subject);

     		jsonInString.put("plaintext",plaintext);

     		jsonInString.put("htmlbody",htmlbody);

     		jsonInString.put("accountId",accountId);

     		jsonInString.put("mailpassword",mailpassword);

     		multipartUtility.addFormField("mailerResp", jsonInString.toString());

     		httpURLConnection = multipartUtility.execute();

     		try {
     			httpURLConnection.connect();
     		} catch (Exception e) {
     			paymentResponse.setEmailSent(false);
     			return paymentResponse;
     		}
     		

     		System.out.println(httpURLConnection.getResponseCode());

     		if(!(httpURLConnection.getResponseCode()==200)){

     				System.out.println("Email not sent to the patient.");
     	            paymentResponse.setEmailSent(false);
     	            
     		}else
     	            paymentResponse.setEmailSent(true);
     		
             }else
             	paymentResponse.setEmailSent(false);
             

     		return paymentResponse;
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
         String patientAccountId=portalMedicalSummaryService.getPatientPersonalDetails(patientId).getPatientRegistrationAccountno();
         if(!billType.equalsIgnoreCase("IM")){
             GeneratedBillsHistoryDetails patientStatement=h093Repository.findOne(PortalPaymentsSpecification.getPatientStatementFileDetails(patientId, billId));
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
                 stmtPath = stmtPath.replace(".pdf", "_"+stmntBean.getBatchNo()+patientAccountId+".pdf");
             stmntBean.setStatementPath(stmtPath);

         }
         else{

             String fileSeperator = System.getProperty("file.separator");
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
             String stmtImage = SharedFolderUtil.getSharedFolderPath()+"/images/bills/ensptstmt.png";

             //call stmnt preview model here
             //GlaceOutLoggingStream.console("path->" + stmtPath);
             GeneratePDF ff=new GeneratePDF(stmtImage);
             String tempfilename =stmtPath.replace(".xml",patientAccountId+"_temp.pdf");
             ff.setTempFileName(tempfilename); // first call temp file

             IngenixStatementPreview isp =new IngenixStatementPreview();
             Hashtable temphst=new Hashtable();
             if(stmtType.equalsIgnoreCase("IM"))
                 ff.setStmtType("IM");
             temphst=isp.parsingXML(stmtPath);

             Hashtable rst=temphst;
             if(!stmtType.equalsIgnoreCase("IM"))
                 rst=isp.singleAccPreview(temphst, String.valueOf(patientAccountId));
             ff.writeFile(rst);

             ff.addWaterMark(stmtPath.replace(".xml",patientAccountId+".pdf"));
             ff.setTempFileName(sharedPath+fileSeperator+"ENSSTMNTS"+fileSeperator+patientAccountId+"temp.pdf");
             ff.delTempFile(tempfilename);

             stmtPath = stmtPath.replaceAll("\\\\","/");
             stmtPath = stmtPath.substring(stmtPath.lastIndexOf("/")+1,stmtPath.length());

             stmtPath = "ENSSTMNTS"+"/"+ stmtPath.replace(".xml",patientAccountId+".pdf");

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
	