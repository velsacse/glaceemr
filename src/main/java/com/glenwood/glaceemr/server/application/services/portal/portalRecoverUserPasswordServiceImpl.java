package com.glenwood.glaceemr.server.application.services.portal;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.H809;
import com.glenwood.glaceemr.server.application.repositories.H809Repository;
import com.glenwood.glaceemr.server.application.repositories.PortalUserRepository;
import com.glenwood.glaceemr.server.application.specifications.PortalRecoverUserPasswordSpecifiction;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.MultipartUtility;

@Service
public class portalRecoverUserPasswordServiceImpl implements portalRecoverUserPasswordService{

	@Autowired
	H809Repository h809Repository;

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public RecoverPortalPasswordBean authenticateUsernameAndGetSecurityQuestions(
			RecoverPortalPasswordBean recoverBean) throws JsonProcessingException {

		List<H809> portalUsersList=h809Repository.findAll(PortalRecoverUserPasswordSpecifiction.authenticatePortalUser(recoverBean.getUsername()));

		RecoverPortalPasswordBean securityQuestionsBean=new RecoverPortalPasswordBean();
		
		if(portalUsersList.size()>0){
		H809 portalUser=portalUsersList.get(0);
		securityQuestionsBean.setUsername(portalUser.getH809004());
		securityQuestionsBean.setDbname(recoverBean.getDbname());
		securityQuestionsBean.setSecurityQuestion1(portalUser.getSecurityQuestion1());
		securityQuestionsBean.setSecurityQuestion2(portalUser.getSecurityQuestion2());
		securityQuestionsBean.setSecurityQuestion3(portalUser.getSecurityQuestion3());
		}
		return securityQuestionsBean;
	}

	@Override
	public EMRResponseBean authenticateSecurityQuestions(
			RecoverPortalPasswordBean recoverBean) throws IOException, JSONException {

		H809 portalUser=h809Repository.findOne(PortalRecoverUserPasswordSpecifiction.authenticatePortalUser(recoverBean.getUsername()));


		EMRResponseBean bean=new EMRResponseBean();

		bean.setCanUserAccess(true);
		bean.setIsAuthorizationPresent(true);
		bean.setLogin(false);

		boolean isDOBCorrect=false;
		boolean isAccountIDCorrect=false;
		boolean isAnswer1Correct=false;
		boolean isAnswer2Correct=false;
		boolean isAnswer3Correct=false;

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date DOB = new Date(portalUser.getChartH809Table().getPatientRegistrationTable().getPatientRegistrationDob().getTime());

		if(dateFormat.format(DOB).equalsIgnoreCase(recoverBean.getDob()))
			isDOBCorrect=true;
		if(portalUser.getChartH809Table().getPatientRegistrationTable().getPatientRegistrationAccountno().equalsIgnoreCase(recoverBean.getAccountID()))
			isAccountIDCorrect=true;
		if(portalUser.getSecurityAnswer1().equalsIgnoreCase(recoverBean.getSecurityAnswer1()))
			isAnswer1Correct=true;
		if(portalUser.getSecurityAnswer2().equalsIgnoreCase(recoverBean.getSecurityAnswer2()))
			isAnswer2Correct=true;
		if(portalUser.getSecurityAnswer3().equalsIgnoreCase(recoverBean.getSecurityAnswer3()))
			isAnswer3Correct=true;


		StringBuffer sb=new StringBuffer();

		if(!isDOBCorrect||!isAccountIDCorrect||!isAnswer1Correct||!isAnswer2Correct||!isAnswer3Correct)
		{
			if(!isDOBCorrect)
				sb.append("DOB, ");
			if(!isAccountIDCorrect)
				sb.append("Account ID, ");
			if(!isAnswer1Correct)
				sb.append("Answer1, ");
			if(!isAnswer2Correct)
				sb.append("Answer2, ");
			if(!isAnswer3Correct)
				sb.append("Answer3, ");
			
			bean.setSuccess(false);
			bean.setData(sb.toString().substring(0, sb.toString().length()-2)+" authentication failed.");
			return bean;
		}



		String URL ="https://mailer1.glaceemr.com/Mailer/sendMail";

		String charSet = "UTF-8";

		String boundary = "===" + System.currentTimeMillis() + "===";

		MultipartUtility multipartUtility=new MultipartUtility(URL, charSet, boundary);

		HttpURLConnection httpURLConnection;
		String mailpassword="demopwd0";

		int mailtype=1;

		String subject="Password Recovery";

		JSONArray toIds = new JSONArray();
		toIds.put(portalUser.getChartH809Table().getPatientRegistrationTable().getPatientRegistrationMailId());

		JSONArray ccids = new JSONArray();
		ccids.put("brahma@glenwoodsystems.com");

		JSONArray bccids = new JSONArray();
		bccids.put(portalUser.getChartH809Table().getPatientRegistrationTable().getPatientRegistrationMailId());

		String accountId="Glenwood";


		String htmlbody = "<html><head></head><body width='100%'>"+
				"<label style='width:100%;padding:10px 5px;font-size:14px;color:#272727;'>Dear "+portalUser.getChartH809Table().getPatientRegistrationTable().getPatientRegistrationFirstName()+" "+portalUser.getChartH809Table().getPatientRegistrationTable().getPatientRegistrationLastName()+",</label><br><br>"+
				"<label style='width:100%;padding:10px 5px;font-size:14px;color:#272727;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "Your password is "+portalUser.getH809010()+" . Do not share this information for your security reasons.</label><br><br><label style='width:100%;padding:10px 5px;font-size:14px;color:#272727;'>Thanks and Regards,</label>"
				+ "<br><label style='width:100%;padding:10px 5px;font-size:14px;color:#272727;'>Glenwood Systems.</label><label></label>"+
				"</body></html>";

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

		httpURLConnection.connect();

		System.out.println(httpURLConnection.getResponseCode());
		
		if(httpURLConnection.getResponseCode()==200){
			bean.setSuccess(true);
			bean.setData(portalUser.getChartH809Table().getPatientRegistrationTable().getPatientRegistrationMailId());
		}
		else{
			bean.setSuccess(false);
		bean.setData("Email sending failed. Please try after some time.");
		}

		return bean;
	}
}
