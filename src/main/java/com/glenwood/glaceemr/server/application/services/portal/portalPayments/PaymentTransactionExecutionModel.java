package com.glenwood.glaceemr.server.application.services.portal.portalPayments;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.glenwood.glaceemr.server.application.models.CreditCardPaymentBean;
import com.glenwood.glaceemr.server.application.models.PaymentService;
import com.glenwood.glaceemr.server.utils.HUtil;

@SuppressWarnings("deprecation")
@Component
public class PaymentTransactionExecutionModel {

	
	@Autowired
	PortalPaymentsService portalPaymentsService;

	private final static String TransactionType_CreditCard = "Credit Card";
	private final static String TranscationType_ACH_Checking_Account="ACH-Checking Account";
	private final static String TranscationType_ACH_Savings_Account="ACH-Savings Account";
	private final static String NEW_LINE = System.getProperty("line.separator");

	/**
	 * @param creditCardBean : credit card payment details
	 * @param userDetails : xml data of user details of gateway. 
	 * @return xml format data to send the payment request to gateway 
	 * @throws Exception
	 */
	public StringBuilder getCreditCardDetails(CreditCardPaymentBean creditCardBean, String userDetails){

		String TransactionType="Credit Card";
		String Username= creditCardBean.getUsername();
		String AccountName=creditCardBean.getAccountName();
		String CardNumber = creditCardBean.getCardNumber();
		String CardSecurityCode=creditCardBean.getSecurityCode();
		String ExpirationDate_month = String.valueOf(creditCardBean.getExpMonth()).trim();
		if(ExpirationDate_month.length()==1){
			ExpirationDate_month="0"+ExpirationDate_month;
		}
		String Expirationdate_year= String.valueOf(creditCardBean.getExpYear()).trim();
		if(Expirationdate_year.length()==4){
			Expirationdate_year=Expirationdate_year.substring(2, 4);
		}
		String Expirationdate = ExpirationDate_month +Expirationdate_year;
		String Amount =String.valueOf(creditCardBean.getPaymentAmount());
		String TrackData = creditCardBean.getComments();
		String CardName = creditCardBean.getCardHolderName().trim();
		String Reason = creditCardBean.getReason();
		String Address = creditCardBean.getAddress();
		String ZipCode =String.valueOf(creditCardBean.getZipCode());
		DateFormat dateFormat = new SimpleDateFormat("MMddyy");
		Date date = new Date();
		String ProcessDate=dateFormat.format(date);
		String Referenceid=creditCardBean.getPatientId()+"_"+getTimeStamp();
		String POS=creditCardBean.getPosName();


		StringBuilder generatexml = new StringBuilder();
		generatexml.append("<?xml version =\"1.0\"?>");
		generatexml.append("<Message Version=\""+1.0+"\" ID=\""+Username+"\" TimeStamp=\""+getTimeStamp()+"\">");
		generatexml.append("<Header>");
		generatexml.append("<From>");
		generatexml.append("<Credential Domain=\"SharedSecret\">dsdssdsdsd</Credential>");
		generatexml.append("<Account>"+AccountName+"</Account>");
		generatexml.append("<Identity>"+Username+"</Identity>");
		generatexml.append("<Ticket>"+generateRandomNumber()+"</Ticket>");
		generatexml.append("</From>");
		generatexml.append("<To>");
		generatexml.append("<Category>Payment</Category>");
		generatexml.append("<Vendor>Transfirst</Vendor>");
		generatexml.append("<Type>"+TransactionType+"</Type>");
		generatexml.append("<Reason>Charge</Reason>");
		generatexml.append(""+userDetails+"");
		generatexml.append("</To>");
		generatexml.append("</Header>");
		generatexml.append("<Request>");
		generatexml.append("<Amount>"+Amount+"</Amount>");
		generatexml.append("<TrackData>"+TrackData+"</TrackData>");
		generatexml.append("<ProcessDate>"+ProcessDate+"</ProcessDate>");
		generatexml.append("<ReferenceId>"+Referenceid+"</ReferenceId>");
		generatexml.append("<CardNumber>"+CardNumber+"</CardNumber>");
		generatexml.append("<CardSecurityCode>"+CardSecurityCode+"</CardSecurityCode>");
		generatexml.append("<ExpirationDate>"+Expirationdate+"</ExpirationDate>");
		generatexml.append("<CardName>"+CardName+"</CardName>");
		generatexml.append("<Address>"+Address+"</Address>");
		generatexml.append("<Pos>"+POS+"</Pos>");
		generatexml.append("<ZipCode>"+ZipCode+"</ZipCode>");
		generatexml.append("</Request>");
		generatexml.append("</Message>");
		return generatexml;
	}


	/**
	 * @return timestamp
	 */
	public String getTimeStamp(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		String date = sdf.format(new Date());
		return date;
	}

	/**
	 * @return randomly generated method 
	 */
	public long generateRandomNumber(){
		long number;
		Random r = new Random();
		number = r.nextLong();
		if(number<1)number=number + 23213*number;
		return number;
	}


	/**
	 * @param URL : Payment Gateway URL
	 * @param request : xml data of payment details that are to be sent to the gateway. 
	 * @return Transaction summary details 
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public String sendPaymentRequest(String URL,String request)throws Exception{

		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(URL);
		httpPost.getParams().setParameter(ClientPNames.COOKIE_POLICY,CookiePolicy.RFC_2109);
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>(); 
		list.add(new BasicNameValuePair("xml_data",request));
		HttpEntity postParams = new UrlEncodedFormEntity(list);
        httpPost.setEntity(postParams);
		String resp = null;
		try {
			CloseableHttpResponse response = httpClient.execute(httpPost);
			  ResponseHandler<String> responseHandler = new BasicResponseHandler();

			//resp = responseHandler.handleResponse(response);
			  resp =EntityUtils.toString(response.getEntity(), "UTF-8");
			if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
			}else{
				throw new Exception();
			}
		} 
		catch(Exception e){
			e.printStackTrace();
		}finally {
			httpPost.releaseConnection();
		}
		
		return resp;
	}


	public PaymentService processCreditCardPayment(CreditCardPaymentBean creditCardBean, String gatewayURL ,String userdetails) throws Exception {

		String creditCardPaymentDetails=this.getCreditCardDetails(creditCardBean,userdetails).toString();
		String response=sendPaymentRequest(gatewayURL,creditCardPaymentDetails);
		String cardNumber = creditCardBean.getCardNumber().trim();
		String cardinfo = getCardInfo(cardNumber);
		String CardName = creditCardBean.getCardHolderName();
		PaymentService paymentSummary=new PaymentService();
		
		try{
			paymentSummary= parseResponse(URLDecoder.decode(response.replace("xml_data=", ""),"UTF-8"),cardinfo,creditCardBean.getPatientId(),CardName, creditCardBean);
		}

		catch (Exception e) {
			paymentSummary=null;
		}

		return paymentSummary;
		
	}

	public String getCardInfo(String cardDescription){
		String cardname="";
		String firstdigit=cardDescription.substring(0, 1);
		if(firstdigit.equals("3")){
			cardname="Amex";
		}
		String lastfourdigit="";
		char lastdigit=0;
		if(cardname.equals("Amex")){
			lastfourdigit=cardDescription.substring(11,14);
			lastdigit=cardDescription.charAt(14); 
		}else{
			lastfourdigit=cardDescription.substring(12,15);
			lastdigit=cardDescription.charAt(15);
		}
		String cardno=firstdigit +"...."+lastfourdigit+lastdigit;

		return cardno;

	}

	public PaymentService parseResponse(String response,String cardInfo,int patientId,String cardHolderName, CreditCardPaymentBean creditCardBean) throws SAXException, IOException, ParserConfigurationException{
		
		PaymentService paymentSummary=new PaymentService();
		
		try{   
			String Category_name="";
			String Vendor_name="";
			String Type_name="";
			String Reason_name="";
			String Ticket_No="";
			String Identity_name="";
			String Account_name="";
			String Total_amount=null;
			String Reference_id=null;
			String Transaction_status=null;
			String timestamp="";
			String Exception_Status=null;
			String Exception_Message=null;
			String Transaction_id=null;
			String Authorization_code=null;
			String statusDescription = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(response));
			Document doc = db.parse(is);
			NodeList time = doc.getElementsByTagName("Message");
			Element elementd = (Element) time.item(0);
			Attr time_value= elementd.getAttributeNode("TimeStamp");
			timestamp=time_value.getValue();
			NodeList nodes = doc.getElementsByTagName("Header");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				NodeList name = element.getElementsByTagName("From");
				for (int r = 0; r < name.getLength(); r++) {
					Element element_from = (Element) name.item(r);
					NodeList Category = element_from.getElementsByTagName("Category");
					Element line1 = (Element) Category.item(0);
					line1 = (Element) Category.item(0);
					Category_name = getCharacterDataFromElement(line1);
					NodeList Vendor = element_from.getElementsByTagName("Vendor");
					Element line2 = (Element) Category.item(0);
					line2 = (Element) Vendor.item(0);
					Vendor_name=getCharacterDataFromElement(line2);
					NodeList Type = element_from.getElementsByTagName("Type");
					Element line3 = (Element) Category.item(0);
					line3 = (Element) Type.item(0);
					Type_name=getCharacterDataFromElement(line3);
					NodeList Reason = element_from.getElementsByTagName("Reason");
					Element line4 = (Element) Category.item(0);
					line4 = (Element) Reason.item(0);
					Reason_name = getCharacterDataFromElement(line4);
					NodeList Ticket = element_from.getElementsByTagName("Ticket");
					Element line5 = (Element) Category.item(0);
					line5 = (Element) Ticket.item(0);
					Ticket_No = getCharacterDataFromElement(line5);
				}
				NodeList To = element.getElementsByTagName("To");
				for (int q = 0;q< To.getLength(); q++) {
					Element element_to = (Element) To.item(q);
					NodeList Identity = element_to.getElementsByTagName("Identity");
					Element line1 = (Element) Identity.item(0);
					line1 = (Element) Identity.item(0);
					Identity_name=getCharacterDataFromElement(line1);
					NodeList Account = element_to.getElementsByTagName("Account");
					Element line2 = (Element) Account.item(0);
					line2 = (Element) Account.item(0);
					Account_name=getCharacterDataFromElement(line2); 
				}
			}
			NodeList nodes1 = doc.getElementsByTagName("Response");
			for (int s = 0; s < nodes1.getLength(); s++) {
				Element element_response = (Element) nodes1.item(s);
				NodeList Amount = element_response.getElementsByTagName("Amount");
				Element line1 = (Element) Amount.item(0);
				line1 = (Element) Amount.item(0);
				Total_amount=getCharacterDataFromElement(line1);
				NodeList ReferenceId = element_response.getElementsByTagName("ReferenceId");
				Element line2 = (Element) ReferenceId.item(0);
				line2 = (Element) ReferenceId.item(0);
				Reference_id=getCharacterDataFromElement(line2);
				NodeList Status = element_response.getElementsByTagName("Status");
				Element line3 = (Element) Status.item(0);
				line3 = (Element) Status.item(0);
				Transaction_status=getCharacterDataFromElement(line3);
				NodeList TransID = element_response.getElementsByTagName("TransID");
				Element line4 = (Element) TransID.item(0);
				line4 = (Element) TransID.item(0);
				Transaction_id=getCharacterDataFromElement(line4);
				NodeList AuthCode = element_response.getElementsByTagName("ConfirmationCode");
				Element line5 = (Element) AuthCode.item(0);
				line5 = (Element) AuthCode.item(0);
				Authorization_code=HUtil.Nz(getCharacterDataFromElement(line5),"");
				NodeList Description = element_response.getElementsByTagName("Description");
				Element line6 = (Element) Description.item(0);
				line6 = (Element) Description.item(0);
				statusDescription=HUtil.Nz(getCharacterDataFromElement(line6),"");

			}  

			NodeList exception = doc.getElementsByTagName("Exception");
			for(int c=0;c<exception.getLength();c++){
				Element element_response = (Element) exception.item(c);
				NodeList ExceptionStatus = element_response.getElementsByTagName("ExceptionStatus");
				Element line1 = (Element) ExceptionStatus.item(0);
				line1 = (Element) ExceptionStatus.item(0);
				Exception_Status=getCharacterDataFromElement(line1);
				NodeList ExceptionMessage = element_response.getElementsByTagName("ExceptionMessage");
				Element line2 = (Element) ExceptionMessage.item(0);
				line2 = (Element) ExceptionMessage.item(0);
				Exception_Message=getCharacterDataFromElement(line2);
			}
			String ipAddress="";
			String routingNumber="-1";
			paymentSummary=saveTransaction(timestamp, Category_name, Vendor_name, Type_name, Reason_name, Ticket_No, Identity_name, Account_name, Total_amount, Reference_id, Transaction_status, Exception_Status, Exception_Message, Transaction_id, Authorization_code, ipAddress, cardInfo, response, patientId, routingNumber, statusDescription, creditCardBean.getPosName(), cardHolderName);
		}	
		catch (Exception e) {
			paymentSummary=null;
			e.printStackTrace();
		}
		return paymentSummary;
	}

	public PaymentService  saveTransaction(String timestamp,String Category_name,String Vendor_name,String Type_name,String Reason_name,String Ticket_No,String Identity_name,String Account_name,String Total_amount,String Reference_id,String Transaction_status,String Exception_Status,String Exception_Message,String Transaction_id,String Authorization_code,String ipAddress,String cardinfo,String response,int accountno_patient,String routingnumber, String statusDescription,String pos,String cardHolderName) throws Exception{
				
		PaymentService paymentService=new PaymentService();
		
		try{ if(Exception_Message ==null)
			Exception_Message =" ";
		if(Exception_Status==null)
			Exception_Status="-1";
		if(Total_amount==null)
			Total_amount="-1";
		String firstdigit_creditcard=cardinfo.substring(0,1);
		String cardtype_creditcard="";
		if(Type_name.equals("Credit Card")){
			if(firstdigit_creditcard.equals("3"))
				cardtype_creditcard="Amex Card";		 
			if(firstdigit_creditcard.equals("4"))
				cardtype_creditcard="Visa Card";
			if(firstdigit_creditcard.equals("5"))	
				cardtype_creditcard="Master Card";
			if(firstdigit_creditcard.equals("6"))
				cardtype_creditcard="Discover Card";
		}   

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		paymentService.setPaymentServiceTime(new Timestamp(new Date(timestamp).getTime()));
		paymentService.setPaymentServiceUserid(Identity_name);
		paymentService.setPaymentServiceCategory(Category_name);
		paymentService.setPaymentServiceVendor(Vendor_name);
		paymentService.setPaymentServiceType(Type_name);
		paymentService.setPaymentServiceReason(Reason_name);
		paymentService.setPaymentServiceTicket(Long.parseLong(Ticket_No));
		paymentService.setPaymentServiceAmount(Double.parseDouble(Total_amount));
		paymentService.setPaymentServiceReferenceid(Reference_id);
		paymentService.setPaymentServiceStatus(Transaction_status);
		paymentService.setPaymentServicePatientid(accountno_patient);
		paymentService.setPaymentServiceEncounterid(21);
		paymentService.setPaymentServiceException(Exception_Message);
		paymentService.setPaymentServiceErrorcode(Integer.parseInt(Exception_Status));
		paymentService.setPaymentServiceTransactionid(Long.parseLong(Transaction_id));
		paymentService.setPaymentServiceAuthcode(Authorization_code);
		paymentService.setPaymentServiceServerip(ipAddress);
		paymentService.setPaymentServiceCardinfo(cardinfo);
		paymentService.setPaymentServiceResponse(response);
		paymentService.setPaymentServiceCreditType(cardtype_creditcard);
		paymentService.setPaymentServiceRoutingnumber(Integer.parseInt(routingnumber));
		paymentService.setPaymentServiceStatusdescription(statusDescription);
		paymentService.setPaymentServicePos(pos);
		paymentService.setPaymentServiceCardHolderName(cardHolderName);
		paymentService.setPaymentServiceCancredit(true);
		paymentService.setPaymentServiceCanvoidorcredit(true);
		
		}
		catch(Exception e){
			
			paymentService=null;
			e.printStackTrace();
		}
		
		return paymentService;
	}
	
	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "none";
	}

	

}
