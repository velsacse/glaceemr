package com.glenwood.glaceemr.server.application.services.portalLogin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.H213;
import com.glenwood.glaceemr.server.application.models.H809;
import com.glenwood.glaceemr.server.application.models.InitialSettings;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.PortalUser;
import com.glenwood.glaceemr.server.application.repositories.ChartRepository;
import com.glenwood.glaceemr.server.application.repositories.H213Repository;
import com.glenwood.glaceemr.server.application.repositories.H809Repository;
import com.glenwood.glaceemr.server.application.repositories.InitialSettingsRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientInsDetailRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.repositories.PortalUserRepository;
import com.glenwood.glaceemr.server.application.services.portal.portalMedicalSummary.PortalMedicalSummaryService;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.PortalPatientRegistrationBean;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.PortalRegistrationResponse;
import com.glenwood.glaceemr.server.application.specifications.PortalLoginSpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalSettingsSpecification;
import com.glenwood.glaceemr.server.utils.MD5;
import com.glenwood.glaceemr.server.utils.MultipartUtility;

@Service
public class PortalLoginServiceImpl implements PortalLoginService {

	@Autowired
	PortalUserRepository portalUserRepository; 

	@Autowired
	H809Repository h809Repository;

	@Autowired
	PatientRegistrationRepository patientRegistrationRepository;
	
	@Autowired 
	ChartRepository chartRepository;

	@Autowired
	PatientInsDetailRepository patientInsDetailRepository;
	
	@Autowired
	PortalMedicalSummaryService portalMedicalSummaryService;
	
	@Autowired
	H213Repository h213Repository;
	
	@Autowired
	InitialSettingsRepository initialSettingsRepository;

	@Autowired
	EntityManager em;

	@Autowired
	EntityManagerFactory emf;

	@Override
	public PortalUser findByUserNameIgnoreCase(String userName) {

		PortalUser portalUser= portalUserRepository.findOne(PortalLoginSpecification.patientByUsername(userName));
		
		/*H809 userDetails=h809Repository.findOne(PortalLoginSpecification.patientById(String.valueOf(portalUser.getId())));
		userDetails.setAccessTime(new Timestamp(new Date().getTime()));
		h809Repository.saveAndFlush(userDetails);*/
		portalUser.setAccessTime(new Timestamp(new Date().getTime()));
		portalUserRepository.saveAndFlush(portalUser);

		return portalUser;
	}

	@SuppressWarnings("deprecation")
	@Override
	public PortalRegistrationResponse checkDuplicatePatientData(String username, String dob, String firstName, String lastName) {
		
		PortalRegistrationResponse regResponse=new PortalRegistrationResponse();
		
		if(!dob.equalsIgnoreCase("-1")&&!firstName.equalsIgnoreCase("-1")&&!lastName.equalsIgnoreCase("-1")){
		List<PatientRegistration> patientsList=patientRegistrationRepository.findAll(PortalLoginSpecification.getPatientsList(new java.sql.Date(new Date(dob).getTime()), firstName, lastName));
		
		if(patientsList.size()>0){
			
			regResponse.setSuccess(false);
			regResponse.setMessage("An account exists with this data. Please try to login with your credentials. If you have any queries, please contact our help desk.");
			return regResponse;
		}
		}

		List<H809> portalUsersList= h809Repository.findAll(PortalLoginSpecification.getAllPatientsByUsername(username));
		
		if(portalUsersList.size()>0){
			
			regResponse.setSuccess(false);
			regResponse.setMessage("Username already exists! Please try another username.");
			return regResponse;
		}

		regResponse.setSuccess(true);
		regResponse.setMessage("No duplicate data exists.");
		return regResponse;
	}

	@SuppressWarnings("deprecation")
	@Override
	public PortalRegistrationResponse registerNewUserForPortal(PortalPatientRegistrationBean registrationBean, String practiceId) throws IOException, JSONException {

		PortalRegistrationResponse regResponse=new PortalRegistrationResponse();
		
		PatientRegistration patientDetails=new PatientRegistration();

		patientDetails.setPatientRegistrationId(getNewRegistrationId());
		patientDetails.setPatientRegistrationFirstName(WordUtils.capitalize(registrationBean.getPatRegFirstName()));
		patientDetails.setPatientRegistrationLastName(WordUtils.capitalize(registrationBean.getPatRegLastName()));
		patientDetails.setPatientRegistrationMidInitial(WordUtils.capitalize(registrationBean.getPatRegMiddleName()));
		patientDetails.setPatientRegistrationDob(new java.sql.Date(new Date(registrationBean.getPatRegDOB()).getTime()));
		patientDetails.setPatientRegistrationRace(String.valueOf(registrationBean.getPatRegRace()));
		patientDetails.setPatientRegistrationEthnicity(registrationBean.getPatRegEthnicity());
		patientDetails.setPatientRegistrationSex(registrationBean.getPatRegGender());
		patientDetails.setPatientRegistrationPreferredLan("-1");

		patientDetails.setPatientRegistrationAddress1(WordUtils.capitalize(registrationBean.getPatRegAddress1()));
		patientDetails.setPatientRegistrationAddress2(WordUtils.capitalize(registrationBean.getPatRegAddress2()));
		patientDetails.setPatientRegistrationState(registrationBean.getPatRegState());
		patientDetails.setPatientRegistrationCity(registrationBean.getPatRegCity());
		patientDetails.setPatientRegistrationPhoneNo(registrationBean.getPatRegHomePhone());
		patientDetails.setPatientRegistrationWorkNo(registrationBean.getPatRegWorkPhone());
		patientDetails.setPatientRegistrationMailId(registrationBean.getPatRegEmailId());
		patientDetails.setPatientRegistrationZip(registrationBean.getPatRegZip());
		patientDetails.setPatientRegistrationActive(true);
		if(String.valueOf(patientDetails.getPatientRegistrationId()).length()>=6)
			patientDetails.setPatientRegistrationAccountno(String.valueOf(patientDetails.getPatientRegistrationId()));
		else if(String.valueOf(patientDetails.getPatientRegistrationId()).length()==5)
			patientDetails.setPatientRegistrationAccountno("0"+patientDetails.getPatientRegistrationId());
		else if(String.valueOf(patientDetails.getPatientRegistrationId()).length()==4)
			patientDetails.setPatientRegistrationAccountno("00"+patientDetails.getPatientRegistrationId());
		else if(String.valueOf(patientDetails.getPatientRegistrationId()).length()==3)
			patientDetails.setPatientRegistrationAccountno("000"+patientDetails.getPatientRegistrationId());
		else if(String.valueOf(patientDetails.getPatientRegistrationId()).length()==2)
			patientDetails.setPatientRegistrationAccountno("0000"+patientDetails.getPatientRegistrationId());
		else if(String.valueOf(patientDetails.getPatientRegistrationId()).length()==1)
			patientDetails.setPatientRegistrationAccountno("00000"+patientDetails.getPatientRegistrationId());

		patientDetails.setPatientRegistrationPrincipalDoctor(registrationBean.getPatRegPrincipalDoctor());

		patientDetails=patientRegistrationRepository.saveAndFlush(patientDetails);//creating an entry in patient_registration table
		
		/*Execute testtableh213() db function to update the patient_registration max id in h213 table*/
		executeTesttableh213();
		
		Chart patientChart=new Chart();
		
		patientChart.setChartPatientid(patientDetails.getPatientRegistrationId());
		patientChart.setChartUnknown1("");
		patientChart.setChartCreatedby(patientDetails.getPatientRegistrationId());
		patientChart.setChartCreateddate(new Timestamp(new Date().getTime()));
		patientChart.setChartAlreadyseen(false);
		
		
		H213 h213=h213Repository.findOne(PortalLoginSpecification.h213ByCategoryName("chart"));
		patientChart.setChartId(h213.getH213003()+1);
		patientChart=chartRepository.saveAndFlush(patientChart);//creating an entry in chart table
		
		h213.setH213003(patientChart.getChartId());
		h213=h213Repository.saveAndFlush(h213);//updating chart max id in h213 table with chart_id from chart table
		
		
		patientChart=chartRepository.findOne(PortalLoginSpecification.chartByPatientId(patientDetails.getPatientRegistrationId())); 
		
		patientDetails.setPatientRegistrationChartno(String.valueOf(patientChart.getChartId()));//updating chart_id in patient_registration table
		
		H809 portalUser=new H809();
		portalUser.setH809002(Long.valueOf(String.valueOf(patientDetails.getPatientRegistrationId())));
		portalUser.setH809003(new java.sql.Date(new Date().getTime()));
		portalUser.setH809004(registrationBean.getPatRegPreferredUsername());
		MD5 md5 = new MD5();
		md5.setHashString(registrationBean.getPatRegPassword());
		String encryptedPassword=md5.getHashString();
		portalUser.setH809005(encryptedPassword);
		portalUser.setH809006(1);
		portalUser.setH809009(0);
		portalUser.setH809010(registrationBean.getPatRegPassword());
		registrationBean.setSecurityQuestion1(registrationBean.getSecurityQuestion1().replaceAll("'", "''"));
		registrationBean.setSecurityQuestion2(registrationBean.getSecurityQuestion2().replaceAll("'", "''"));
		registrationBean.setSecurityQuestion3(registrationBean.getSecurityQuestion3().replaceAll("'", "''"));
		registrationBean.setSecurityAnswer1(registrationBean.getSecurityAnswer1().replaceAll("'", "''"));
		registrationBean.setSecurityAnswer2(registrationBean.getSecurityAnswer2().replaceAll("'", "''"));
		registrationBean.setSecurityAnswer3(registrationBean.getSecurityAnswer3().replaceAll("'", "''"));
		portalUser.setSecurityQuestion1(registrationBean.getSecurityQuestion1());
		portalUser.setSecurityAnswer1(registrationBean.getSecurityAnswer1());
		portalUser.setSecurityQuestion2(registrationBean.getSecurityQuestion2());
		portalUser.setSecurityAnswer2(registrationBean.getSecurityAnswer2());
		portalUser.setSecurityQuestion3(registrationBean.getSecurityQuestion3());
		portalUser.setSecurityAnswer3(registrationBean.getSecurityAnswer3());
		portalUser.setPasswordReset(0);
		portalUser.setAccessTime(new Timestamp(new Date().getTime()));
		portalUser.setFromPortal(true);		

		portalUser=h809Repository.saveAndFlush(portalUser);//creating an entry in h809 table
		
		
		InitialSettings practiceDetails=initialSettingsRepository.findOne(PortalSettingsSpecification.getPracticeDetails("Practice Name"));
		
		
		String URL ="https://mailer1.glaceemr.com/Mailer/sendMail";

		String charSet = "UTF-8";

		String boundary = "===" + System.currentTimeMillis() + "===";

		MultipartUtility multipartUtility=new MultipartUtility(URL, charSet, boundary);

		HttpURLConnection httpURLConnection;
		String mailpassword="demopwd0";

		int mailtype=1;

		String subject="Patient Portal Account Activation";

		JSONArray toIds = new JSONArray();
		toIds.put(registrationBean.getPatRegEmailId());

		JSONArray ccids = new JSONArray();

		JSONArray bccids = new JSONArray();
		bccids.put("");

		String accountId="Glenwood";


		String htmlbody="<html><head></head>"+
"<body style='width:100%;color:#1e1e1e;font-size:16px;'>"+
"<label style='width:100%;padding:10px 5px;'>Dear "+WordUtils.capitalizeFully(registrationBean.getPatRegFirstName())+" "+WordUtils.capitalizeFully(registrationBean.getPatRegLastName())+",</label>"+
"<br><br>"+
"<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Welcome and thank you for creating your Patient Portal account! To complete the account creation process click on the below link.</label>"+
"<br><br>"+
"<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a style='color:#1773cf' href='https://patientportal.glaceemr.com/glaceportal_login/UserRegistration?requestFor=AccountActivation&userId="+patientDetails.getPatientRegistrationId()+"&practiceId="+practiceId+"'>Click here to activate your Patient Portal account</a></label>"+
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

		httpURLConnection.connect();

		System.out.println(httpURLConnection.getResponseCode());
		
		if(!(httpURLConnection.getResponseCode()==200)){
			
			System.out.println("Email not sent to the patient.");
			regResponse.setSuccess(false);
			regResponse.setMessage("You have been registered successfully! But error in sending the activation link to your registered email address. Please contact our help desk.");
		}else{
			
			regResponse.setSuccess(true);
			regResponse.setMessage("You have been registered successfully! Account activation link has been sent to your registered email address. Please click on the link sent to your email, to activate your account.");
		}
		
		
		return regResponse;
	}

	public Integer getNewRegistrationId() {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<PatientRegistration> root = cq.from(PatientRegistration.class);
		cq.select(builder.max(root.get(PatientRegistration_.patientRegistrationId)));
		Integer regId=(Integer) em.createQuery(cq).getSingleResult();

		return regId+1;
	}

	@Override
	public PortalRegistrationResponse activateUserAccount(int patientId) {
		
		H809 userDetails=h809Repository.findOne(PortalLoginSpecification.inactivePatientById(patientId));
		userDetails.setH809006(1);
		userDetails.setH809009(1);
		userDetails.setPasswordReset(1);
		userDetails=h809Repository.saveAndFlush(userDetails);
		
		PortalRegistrationResponse regResponse=new PortalRegistrationResponse();
		regResponse.setSuccess(true);
		regResponse.setMessage("Your account has been activated successfully. Please login through Patient Portal.");

		return regResponse;
	}
	
	
	public void executeTesttableh213(){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<H213> root = cq.from(H213.class);
		cq.select(builder.function("testtableh213", String.class));
		
		em.createQuery(cq).getResultList();
	}

	@Override
	public PortalRegistrationResponse registerExistingUserForPortal(PortalPatientRegistrationBean registrationBean, String practiceId) throws IOException, JSONException {
		
		PortalRegistrationResponse regResponse=new PortalRegistrationResponse();
		PatientRegistration patientDetails;
		
		if(registrationBean.getPatientId()!=-1){
			patientDetails=portalMedicalSummaryService.getPatientPersonalDetails(registrationBean.getPatientId());
			if(registrationBean.getPatRegEmailId()!=null){
		patientDetails.setPatientRegistrationMailId(registrationBean.getPatRegEmailId());
		patientDetails=patientRegistrationRepository.saveAndFlush(patientDetails);
		}
		
		H809 portalUser=findByPatientId(registrationBean.getPatientId());
		portalUser.setH809004(registrationBean.getPatRegPreferredUsername());
		portalUser.setH809009(1);
		portalUser.setH809006(1);
		portalUser.setPasswordReset(1);
		portalUser.setAccessTime(new Timestamp(new Date().getTime()));
		portalUser.setH809010(registrationBean.getPatRegPassword());
		MD5 md5 = new MD5();
		md5.setHashString(registrationBean.getPatRegPassword());
		String encryptedPassword=md5.getHashString();
		portalUser.setH809005(encryptedPassword);
		portalUser.setWrongEntryCount(0);
		portalUser.setSecurityQuestion1(registrationBean.getSecurityQuestion1());
		portalUser.setSecurityAnswer1(registrationBean.getSecurityAnswer1());
		portalUser.setSecurityQuestion2(registrationBean.getSecurityQuestion2());
		portalUser.setSecurityAnswer2(registrationBean.getSecurityAnswer2());
		portalUser.setSecurityQuestion3(registrationBean.getSecurityQuestion3());
		portalUser.setSecurityAnswer3(registrationBean.getSecurityAnswer3());
		
		h809Repository.saveAndFlush(portalUser);
		
		
		regResponse.setSuccess(true);
		regResponse.setMessage("You have been registered successfully.");
		
		return regResponse;
		}else{
			
			regResponse.setSuccess(false);
			regResponse.setMessage("Registration Failure");
			
			return regResponse;
		}
	}

	@Override
	public H809 findByPatientId(int patientId) {
		
		H809 portalUser=h809Repository.findOne(PortalLoginSpecification.patientById(String.valueOf(patientId)));
		
		return portalUser;
	}

	@SuppressWarnings("deprecation")
	@Override
	public PortalRegistrationResponse requestSignupCredentials(PortalPatientRegistrationBean registrationBean, String practiceId)throws IOException, JSONException {
		
		PortalRegistrationResponse regResponse=new PortalRegistrationResponse();
		
		List<PatientRegistration> patientsList=new ArrayList<PatientRegistration>();
		
		if(registrationBean.getPatRegFirstName()!=null&&registrationBean.getPatRegLastName()!=null&&registrationBean.getPatRegDOB()!=null&&registrationBean.getPatRegEmailId()!=null)
			patientsList=patientRegistrationRepository.findAll(PortalLoginSpecification.getPatientsList(new java.sql.Date(new Date(registrationBean.getPatRegDOB()).getTime()),
						registrationBean.getPatRegFirstName(), registrationBean.getPatRegLastName(), registrationBean.getPatRegEmailId()));
				
		if(patientsList.size()>1){
			regResponse.setSuccess(false);
			regResponse.setMessage("Multiple users exist with same details. Please contact our customer support.");
		}else if(patientsList.size()<=0){
			regResponse.setSuccess(false);
			regResponse.setMessage("No such user exists with the submitted details.");
		}else{
			
			H809 portalUser=findByPatientId(patientsList.get(0).getPatientRegistrationId());
			
			InitialSettings practiceDetails=initialSettingsRepository.findOne(PortalSettingsSpecification.getPracticeDetails("Practice Name"));
			
			
			String URL ="https://mailer1.glaceemr.com/Mailer/sendMail";

			String charSet = "UTF-8";

			String boundary = "===" + System.currentTimeMillis() + "===";

			MultipartUtility multipartUtility=new MultipartUtility(URL, charSet, boundary);

			HttpURLConnection httpURLConnection;
			String mailpassword="demopwd0";

			int mailtype=1;

			String subject="Patient Portal Account Activation";

			JSONArray toIds = new JSONArray();
			toIds.put(patientsList.get(0).getPatientRegistrationMailId());

			JSONArray ccids = new JSONArray();

			JSONArray bccids = new JSONArray();
			bccids.put("");

			String accountId="Glenwood";


			String htmlbody="<html><head></head>"+
	"<body style='width:100%;color:#1e1e1e;font-size:16px;'>"+
	"<label style='width:100%;padding:10px 5px;'>Dear "+WordUtils.capitalizeFully(registrationBean.getPatRegFirstName())+" "+WordUtils.capitalizeFully(registrationBean.getPatRegLastName())+",</label>"+
	"<br><br>"+
	"<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Welcome and thank you for signing up for Patient Portal!</label>"+
	"<br><br>"+
	"<label style='font-weight:bold;font-size:14px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Username: <span style='font-weight:normal;color:#1e1e1e;font-size:16px;'>"+portalUser.getH809004()+"</span></label>"+
	"<br><br>"+
	"<label style='font-weight:bold;font-size:14px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Password: <span style='font-weight:normal;color:#1e1e1e;font-size:16px;'>"+portalUser.getH809010()+"</span></label>"+
	"<br><br>"+
	"<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a style='color:#1773cf' href='https://patientportal.glaceemr.com/glaceportal_login/portal.jsp?practiceId="+practiceId+"'>Click here to login into portal</a></label>"+
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

			httpURLConnection.connect();

			System.out.println(httpURLConnection.getResponseCode());
			
			if(!(httpURLConnection.getResponseCode()==200)){
				
				System.out.println("Email not sent to the patient.");
				regResponse.setSuccess(false);
				regResponse.setMessage("Error in sending the signup credentials to the registered email address.");
			}else{
				
				regResponse.setSuccess(true);
				regResponse.setMessage("Signup credentials have been sent to your email address.");
			}
		}
				
		
		return regResponse;
	}
	
	

}
