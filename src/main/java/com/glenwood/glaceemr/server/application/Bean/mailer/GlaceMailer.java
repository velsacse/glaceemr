package com.glenwood.glaceemr.server.application.Bean.mailer;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GlaceMailer {

	public static void sendFailureReport(String url_response, String accId) throws IOException {

		String URL ="https://mailer1.glaceemr.com/Mailer/sendMail";

		String charSet = "UTF-8";

		String boundary = "===" + System.currentTimeMillis() + "===";

		MultipartUtility multipartUtility=new MultipartUtility(URL, charSet, boundary);

		HttpURLConnection httpURLConnection;

		String accountId="Glenwood";

		String mailpassword="demopwd0";

		int mailtype=1;		

		String subject = "MIPSPerformance Job Report failed for "+accId;

		String[] toids = {"harikrishna@glenwoodsystems.com","ranjitha@glenwoodsystems.com"};

		String[] ccids = {"ganesh@glenwoodsystems.com"};

		String[] bccid={""};

		String htmlbody=url_response;

		String plaintext="MIPSPerformance Job Report";

		MailerResponse rb=new MailerResponse(mailtype,"donotreply@glenwoodsystems.com"
				,toids,ccids,bccid,subject,htmlbody,plaintext,accountId,mailpassword);

		ObjectMapper mapper=new ObjectMapper();

		String jsonInString = mapper.writeValueAsString(rb);

		multipartUtility.addFormField("mailerResp", jsonInString.toString());

		httpURLConnection = multipartUtility.execute();

		httpURLConnection.connect();

	}
	
	public static String buildMailContentFormat(String accId, int patientId, String responseString, String exceptionTrace){
		
		String mailContent = "";
		
		mailContent += "<html><body><table border='1'>";
		
		mailContent += "<tr><td><b>Account Id: </b></td><td>"+accId+"</td></tr>";
		
		if(patientId!=-1){
			mailContent += "<tr><td><b>Patient Id: </b></td><td>"+patientId+"</td></tr>";
		}
		
		mailContent += "<tr><td><b>Error Message: </b></td><td>"+responseString+"</td></tr>";
		
		mailContent += "<tr><td><b>Exception Trace: </b></td><td>"+exceptionTrace+"</td></tr>";
		
		mailContent += "</table></body></html>";
		
		return mailContent;
		
	}
	
}