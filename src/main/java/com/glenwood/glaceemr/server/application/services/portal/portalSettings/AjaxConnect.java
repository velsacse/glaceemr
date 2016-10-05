package com.glenwood.glaceemr.server.application.services.portal.portalSettings;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

public class AjaxConnect {
 
	/**
	 * HTTP GET request
	 * @param url
	 * @return response
	 * @throws Exception
	 */
	public String sendGet(String connectionPath) throws Exception {
  
		URL url = new URL(connectionPath);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
 
		/**
		 * add request header
		 * Send GET request
		 */
		con.setRequestMethod("GET");
		con.setRequestProperty("content_type","text/plain");
		con.setDoInput(true);
		con.setDoOutput(true);
		
		int responseCode = con.getResponseCode();
 
		BufferedReader inputBuffer = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();
 
		while ((inputLine = inputBuffer.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		inputBuffer.close();
  
		return responseBuffer.toString();
	}
 
	/**
	 * HTTP POST request
	 * @param url
	 * @return response
	 * @throws Exception
	 */
	public String sendPost(String connectionPath, String postParameters) throws Exception {
 
		URL url = new URL(connectionPath);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		byte[] postData       = postParameters.getBytes( StandardCharsets.UTF_8 );
		int    postDataLength = postData.length;
 
		/**
		 * add request header
		 * Send POST request
		 */
		
		
		con.setInstanceFollowRedirects( false );
		con.setRequestMethod( "POST" );
		con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		con.setRequestProperty( "charset", "utf-8");
		con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		con.setUseCaches( false );
		con.setDoInput(true);
		con.setDoOutput(true);
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(postParameters.getBytes("UTF-8"));
		wr.flush();
		wr.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
		StringBuilder sb = new StringBuilder();
		String output;
		
		
		int responseCode = con.getResponseCode();
		System.out.println("responseCode:::::"+responseCode);
		
		JSONObject responseObj=new JSONObject();
		
		if(responseCode==200)
		{
			while ((output = br.readLine()) != null)
				sb.append(output);
				return sb.toString();
		}else
		{
			return String.valueOf(responseCode);
		}
		
	}
	
		
	
}
