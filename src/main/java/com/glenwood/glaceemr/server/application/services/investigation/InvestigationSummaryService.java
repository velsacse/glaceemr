package com.glenwood.glaceemr.server.application.services.investigation;

/**
 * @author yasodha
 *
 * InvestigationSummaryService is an interface holds the method
 * declarations and predicates required for investigation summary
 */
public interface InvestigationSummaryService {
	public void  savelab(String requesttosave,Integer encounterIdParam,Integer patientIdParam,Integer chartIdParam,
			Integer userIdParam,String fullDataParam,String isforwardParam,String forwardto,
			String ishighpriorityParam,String testidParam) throws Exception;
}
