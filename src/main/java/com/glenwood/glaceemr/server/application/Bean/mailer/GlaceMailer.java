package com.glenwood.glaceemr.server.application.Bean.mailer;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GlaceMailer {

	public static void SendFaxReport(String url_response, String accId) throws IOException {

		String URL ="https://mailer1.glaceemr.com/Mailer/sendMail";

		String charSet = "UTF-8";

		String boundary = "===" + System.currentTimeMillis() + "===";

		MultipartUtility multipartUtility=new MultipartUtility(URL, charSet, boundary);

		HttpURLConnection httpURLConnection;

		String accountId="Glenwood";

		String mailpassword="demopwd0";

		int mailtype=1;		

		String subject = "MIPSPerformance Job Report failed for "+accId;

		String[] toids = {"harikrishna@glenwoodsystems.com"};

		String[] ccids = {""};

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
	
}
