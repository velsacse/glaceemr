package com.glenwood.glaceemr.server.application.services.chart.assessment;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.H611;
import com.glenwood.glaceemr.server.application.models.ProblemList;
import com.glenwood.glaceemr.server.application.repositories.AssessmentRepository;
import com.glenwood.glaceemr.server.application.repositories.EncounterRepository;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditLogConstants;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailService;
import com.glenwood.glaceemr.server.application.specifications.AssessmentSpecification;
import com.glenwood.glaceemr.server.application.specifications.ProblemListSpecification;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

@Service
public class AssessmentSummaryServiceImpl implements AssessmentSummaryService{

	@Autowired
	AssessmentRepository assessmentRepository;
		
	@Autowired
	EncounterRepository encounterRepository;
	
	@Autowired
	EntityManagerFactory emf ;
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	AuditTrailService auditTrailService;

	@Autowired
	SessionMap sessionMap;

	@Autowired
	HttpServletRequest request;
	
	private Logger logger = Logger.getLogger(AssessmentSummaryServiceImpl.class);
	
	/**
	 * Method to fetch current visit data
	 */
	@Override
	public List<H611> getCurrentDiagnosis(Integer patientId,Integer encounterId) throws Exception {
		
		logger.debug("Fetching Current Problems");
		logger.error("in getCurrentDiagnosis");
		encounterId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull("" + encounterId)).or("-1"));
		patientId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull("" + patientId)).or("-1"));
		List<H611> dxs = assessmentRepository.findAll(Specifications.where(AssessmentSpecification.DxByPatientId(patientId)).and(AssessmentSpecification.DxByEncounterId(encounterId)));
		auditTrailService.LogEvent(AuditLogConstants.GLACE_LOG,AuditLogConstants.LoginAndLogOut,
				   AuditLogConstants.LOGIN,1,AuditLogConstants.SUCCESS,"Sucessfull login User Name(" +1+")",-1,
				   "127.0.0.1",request.getRemoteAddr(),-1,-1,-1,
				   AuditLogConstants.USER_LOGIN,request,"User (" + sessionMap.getUserID()+ ") logged in through SSO");
		return dxs;
		
	}
 
	
    /**
     * Method to save current visit diagnosis data
     */
	@Override
	public Boolean saveDiagnosis(JSONObject assessListobj) throws Exception {
		
		
		int patientId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull("" + assessListobj.getString("patientId"))).or("-1"));
		int encounterId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull("" + assessListobj.getString("encounterId"))).or("-1"));
		int userId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull("" + assessListobj.getString("userId"))).or("-1"));
		JSONArray assessmentDetailsArray=(JSONArray) assessListobj.get("assessList");
		
		for(int i=0;i<assessmentDetailsArray.length();i++)
		{			
		String assessmentDetailsStr=assessmentDetailsArray.getString(i);
		JSONObject assessmentDetailsObj=new JSONObject(assessmentDetailsStr);			
		String dxCode = Optional.fromNullable(Strings.emptyToNull("" + assessmentDetailsObj.getString("dxCode"))).or(" ");
		String dxDesc = Optional.fromNullable(Strings.emptyToNull("" + assessmentDetailsObj.getString("dxDesc"))).or(" ");
		String dxStatus = Optional.fromNullable(Strings.emptyToNull("" + assessmentDetailsObj.getString("DxStatus"))).or("-2");
		String assessmentComment = Optional.fromNullable(Strings.emptyToNull("" + assessmentDetailsObj.getString("assessmentComment"))).or(" ");
		List<H611> dupDxCode = assessmentRepository.findAll(Specifications.where(AssessmentSpecification.DxByPatientId(patientId)).and(AssessmentSpecification.DxByEncounterId(encounterId)).and(AssessmentSpecification.DxByCode(dxCode)));
			if(dupDxCode.size()<1 &&(!dxCode.trim().equalsIgnoreCase(""))){	
			}
		}
				
		return null;
	}



	/**
	 * Method to fetch data for edit page
	 */
	@Override
	public List<H611> getEditData(Integer patientId, Integer encounterId,String dxCode,
			Integer problemId) {
		patientId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull("" + patientId)).or("-1"));
		encounterId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull("" + encounterId)).or("-1"));
		dxCode = Optional.fromNullable(Strings.emptyToNull("" + encounterId)).or(" ");
		problemId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull("" + problemId)).or("-1"));
		List<H611> dxData = assessmentRepository.findAll(Specifications.where(AssessmentSpecification.getDataToEdit(patientId,encounterId,dxCode,problemId)));
		return dxData;
	}

}