package com.glenwood.glaceemr.server.application.services.rulesengine;

public interface GlaceRulesService {
	
	public String getMeasures(Integer providerId, Integer patientId,Integer encYear)throws Exception;

}

