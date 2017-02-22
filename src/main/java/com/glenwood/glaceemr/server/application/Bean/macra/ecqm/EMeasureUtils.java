/**
 * Utility class for EMeasure and QDMObject creation
 * 
 * @author krishna
 * 
 */

package com.glenwood.glaceemr.server.application.Bean.macra.ecqm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.Bean.ClinicalDataQDM;
import com.glenwood.glaceemr.server.application.Bean.InvestigationQDM;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Assessment;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.DiagnosticStudy;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Intervention;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.LabTest;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Negation;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.PhysicalExam;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Procedure;

public class EMeasureUtils {

	ObjectMapper mapper = new ObjectMapper();
	JSONArray measureDetails = new JSONArray();
	RestTemplate restTemplate = new RestTemplate();
	HashMap<String, HashMap<String,String>> finalCodeList = new HashMap<String, HashMap<String,String>>();
	HashMap<String, String> measureInfo = new HashMap<String, String>();
	
	/**
	 * This function takes list of
	 * @param emeasure and 
	 * @return the HashMap containing all the QDM Categories and their codeSets containing CodeList
	 */
	public HashMap<String, HashMap<String, String>> getCodelist(List<EMeasure> emeasure){
		
		CQMSpecification specification = null;
		HashMap<String, Category> qdmCatagory = null;
		
		String qdmCategoryNames[] = null;
		
		for(int index = 0;index < emeasure.size();index++){
			
			measureInfo.put(""+emeasure.get(index).getId(), emeasure.get(index).getTitle());
			
			specification = emeasure.get(index).getSpecification();
			qdmCatagory=specification.getQdmCategory();
			
			qdmCategoryNames = new String[qdmCatagory.keySet().size()];
			
			for(int k=0;k<qdmCatagory.keySet().size();k++){

				String categoryName = qdmCatagory.keySet().toArray()[k].toString();
				qdmCategoryNames[k] = categoryName;
				
			}

			for(int i=0;i<qdmCatagory.size();i++){
				
				Category c = qdmCatagory.get(qdmCategoryNames[i]);
				
				String codeSetString = "";
				
				for(int set=0;set<c.getCodeList().keySet().toArray().length;set++){
					
					if(codeSetString.equals("")){
						codeSetString = c.getCodeList().keySet().toArray()[set].toString();
					}else{
						codeSetString = codeSetString.concat(",".concat(c.getCodeList().keySet().toArray()[set].toString()));
					}
					
				}
				
				if(finalCodeList.containsKey(qdmCategoryNames[i])){
					
					if(finalCodeList.get(qdmCategoryNames[i]).size() > 0){
						
						HashMap<String, String> categoryObj = finalCodeList.get(qdmCategoryNames[i]);
						
						int limit = categoryObj.keySet().size();
						
						if(limit<c.getCodeList().keySet().size()){
							
							limit = c.getCodeList().keySet().size();
							
						}else if(limit == c.getCodeList().keySet().size() && limit > 0){
							
							int tempLimit = limit;
							
							for(int keySet = 0;keySet<tempLimit;keySet++){
								
								if(!categoryObj.keySet().toArray()[keySet].equals(c.getCodeList().keySet().toArray()[keySet])){
									limit++;
								}
								
							}
							
						}
						
						for(int codeSet=0;codeSet<limit;codeSet++){
							
							String codeKey = "";
							
							if(codeSet<categoryObj.keySet().size()){
								codeKey = categoryObj.keySet().toArray()[codeSet].toString();
							}else{
								codeKey = codeSetString.split(",")[limit - codeSet -1];
							}
							
							if(categoryObj.containsKey(codeKey)){
								
								if(codeSetString.contains(codeKey) && c.getCodeList().containsKey(codeKey)){
									
									if(c.getCodeList().size() > 0){
										String codeList = categoryObj.get(codeKey);
										categoryObj.put(codeKey, codeList.concat(",".concat(c.getCodeList().get(codeKey))));
									}else{
										categoryObj.put(codeKey, c.getCodeList().get(codeKey));
									}
									
								}else if(c.getCodeList().containsKey(codeKey)){
									categoryObj.put(codeKey, c.getCodeList().get(codeKey));
								}
								
							}else{
								
								categoryObj.put(codeKey, c.getCodeList().get(codeKey));
								
							}
							
						}
						
						finalCodeList.put(qdmCategoryNames[i], categoryObj);
						
					}else{
						finalCodeList.put(qdmCategoryNames[i], c.getCodeList());
					}
					
				}else{
					finalCodeList.put(qdmCategoryNames[i], c.getCodeList());
				}
				
			}
			
		}
		
		setMeasureInfo(measureInfo);
		
		return finalCodeList;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<EMeasure> getMeasureBeanDetails(String measureIds) throws Exception{
		
		String apiUrl = "http://hub-icd10.glaceemr.com/DataGateway/eCQMServices/getECQMInfoById?ids="+measureIds;
		
	    String result = restTemplate.getForObject(apiUrl, String.class);
		
	    getMeasureInfo(result);
	    
		return (List<EMeasure>) mapper.readValue(result, mapper.getTypeFactory().constructCollectionType(List.class, EMeasure.class));
		
	}
	
	public void getMeasureInfo(String data) throws Exception{
		
		JSONArray dataFromHub = new JSONArray(data);
		
	    JSONArray measureDetailsArray =new JSONArray();
	    
	    for(int i=0;i<dataFromHub.length();i++){
            
            String cmsId = dataFromHub.getJSONObject(i).getString("cmsId");
            String title = dataFromHub.getJSONObject(i).getString("title");
            String description = dataFromHub.getJSONObject(i).getString("description");
        
            JSONObject obj = new JSONObject();
            
            obj.put("cmsId", cmsId);
            obj.put("title", title);
            obj.put("description", description);
            
            measureDetailsArray.put(obj);
            
        }
	    
	    setMeasureDetails(measureDetailsArray);
	    
	}
	
	public String getCodeListByCategory(HashMap<String, HashMap<String,String>> measureObj, String categoryName){
		
		String codeList = "";
		
		if(measureObj.containsKey(categoryName)){
			
			HashMap<String, String> codeSet = measureObj.get(categoryName);
	
			for(int i=0;i<codeSet.keySet().toArray().length;i++){
			
				if(codeList.equals("")){
					
					codeList = codeSet.get(codeSet.keySet().toArray()[i].toString());
							
				}else{
					
					codeList = codeList.concat(",".concat(codeSet.get(codeSet.keySet().toArray()[i].toString())));
					
				}
				
			}
			
		}
		
		return codeList;
		
	}
	
	public List<String> getCPTCodes(HashMap<String, HashMap<String,String>> measureObj, String categoryName){

		List<String> codeList=new ArrayList<String>();

		if(measureObj.containsKey(categoryName)){

			HashMap<String, String> codeSet = measureObj.get(categoryName);
			for(int i=0;i<codeSet.keySet().toArray().length;i++){

				if(codeSet.keySet().toArray()[i].equals("CPT")){
					codeList = Arrays.asList(codeSet.get("CPT").toString().split(","));
				}

			}
			
		}
		
		return codeList;
		
	}
	
	public String getCodeList(HashMap<String, HashMap<String,String>> measureObj,String category, String key, String codeList){
		
		if(measureObj.get(category).containsKey(key)){
			
			if(measureObj.get(category).get(key).length() > 0){
				
				if(codeList.length() > 0 && !codeList.equals("")){
					codeList = codeList.concat(",".concat(measureObj.get(category).get(key)));
				}else{
					codeList = measureObj.get(category).get(key);
				}
				
			}
			
		}
		
		return codeList;
		
	}
	
	public HashMap<String, String> getCodeListForCNM(HashMap<String, HashMap<String,String>> measureObj){
		
		HashMap<String, String> codeListForCNM = new HashMap<String, String>();
		
		String snomedCodeList = "";
		String loincCodeList = "";		

		if(measureObj.containsKey("Physical Exam")){
			
			snomedCodeList = getCodeList(measureObj,"Physical Exam","SNOMEDCT",snomedCodeList);
			loincCodeList = getCodeList(measureObj,"Physical Exam","LOINC",loincCodeList);
			
		}
		
		if(measureObj.containsKey("Intervention")){
			
			snomedCodeList = getCodeList(measureObj,"Intervention","SNOMEDCT",snomedCodeList);
			loincCodeList = getCodeList(measureObj,"Intervention","LOINC",loincCodeList);
			
		}
		
		if(measureObj.containsKey("Procedure")){
			
			snomedCodeList = getCodeList(measureObj,"Procedure","SNOMEDCT",snomedCodeList);
			loincCodeList = getCodeList(measureObj,"Procedure","LOINC",loincCodeList);
			
		}
		
		if(measureObj.containsKey("Diagnostic Study")){
			
			snomedCodeList = getCodeList(measureObj,"Diagnostic Study","SNOMEDCT",snomedCodeList);
			loincCodeList = getCodeList(measureObj,"Diagnostic Study","LOINC",loincCodeList);
			
		}
		
		if(measureObj.containsKey("Risk Category/Assessment")){
			
			snomedCodeList = getCodeList(measureObj,"Risk Category/Assessment","SNOMEDCT",snomedCodeList);
			loincCodeList = getCodeList(measureObj,"Risk Category/Assessment","LOINC",loincCodeList);
			
		}
		
		codeListForCNM.put("SNOMED", snomedCodeList);
		
		codeListForCNM.put("LOINC", loincCodeList);
		
		return codeListForCNM;
		
	}
	
	public List<LabTest> getLabTestQDMForInvestigation(List<InvestigationQDM> patientInvestigationData, String codeList){
		
		InvestigationQDM eachObj = null;
		LabTest labTestObj;
		List<LabTest> labTestQDM = new ArrayList<LabTest>();
		
		for(int i=0;i<patientInvestigationData.size();i++){
			
			eachObj = patientInvestigationData.get(i);
			labTestObj = new LabTest();
			
			if(codeList.contains(eachObj.getCode())){
				
				labTestObj.setCode(eachObj.getCode());
				
				if(eachObj.getCompanyId() == 54){
					labTestObj.setCodeSystemOID("2.16.840.1.113883.6.96");
				}else if(eachObj.getCompanyId() == 51){
					labTestObj.setCodeSystemOID("2.16.840.1.113883.6.1");
				}
				
				labTestObj.setDescription(eachObj.getCodeDescription());
				if(eachObj.getStatus()==3 || eachObj.getStatus()==4 || eachObj.getStatus()==5 || eachObj.getStatus()==6){
					labTestObj.setStatus(2);
					labTestObj.setStartDate(eachObj.getPerformeOn());
					labTestObj.setEndDate(eachObj.getPerformeOn());
				}else if(eachObj.getStatus() == 1){
					labTestObj.setStartDate(eachObj.getCreatedOn());
					labTestObj.setEndDate(eachObj.getCreatedOn());
				}else if(eachObj.getStatus() == 8){
					labTestObj.setStartDate(eachObj.getCreatedOn());
					labTestObj.setEndDate(eachObj.getCreatedOn());
					Negation n = new Negation();
					n.setCode("105480006");
					n.setDescription("Refusal of treatment by patient (situation)");
					labTestObj.setNegation(n);
				}else{
					labTestObj.setStartDate(eachObj.getPerformeOn());
					labTestObj.setEndDate(eachObj.getPerformeOn());
				}
				
				labTestQDM.add(labTestObj);
				
			}
			
		}
		
		return labTestQDM;
		
	}
	
	public List<DiagnosticStudy> getDiagnosticStudyQDM(List<InvestigationQDM> patientInvestigationData, String codeList,List<DiagnosticStudy> diagnosisObj){
		
		InvestigationQDM eachObj = null;
		DiagnosticStudy diagnosticStudyObj;
		List<DiagnosticStudy> diagnosticStudyQDM = new ArrayList<DiagnosticStudy>();
		
		for(int i=0;i<patientInvestigationData.size();i++){
			
			eachObj = patientInvestigationData.get(i);
			diagnosticStudyObj = new DiagnosticStudy();
			
			if(codeList.contains(eachObj.getCode())){
				
				diagnosticStudyObj.setCode(eachObj.getCode());
				
				if(eachObj.getCompanyId() == 54){
					diagnosticStudyObj.setCodeSystemOID("2.16.840.1.113883.6.96");
				}else if(eachObj.getCompanyId() == 51){
					diagnosticStudyObj.setCodeSystemOID("2.16.840.1.113883.6.1");
				}
				
				diagnosticStudyObj.setDescription(eachObj.getCodeDescription());
				if(eachObj.getStatus()==3 || eachObj.getStatus()==4 || eachObj.getStatus()==5 || eachObj.getStatus()==6){
					diagnosticStudyObj.setStatus(2);
					diagnosticStudyObj.setStartDate(eachObj.getPerformeOn());
					diagnosticStudyObj.setEndDate(eachObj.getPerformeOn());
				}else if(eachObj.getStatus() == 1){
					diagnosticStudyObj.setStartDate(eachObj.getCreatedOn());
					diagnosticStudyObj.setEndDate(eachObj.getCreatedOn());
				}else if(eachObj.getStatus() == 8){
					diagnosticStudyObj.setStartDate(eachObj.getCreatedOn());
					diagnosticStudyObj.setEndDate(eachObj.getCreatedOn());
					Negation n = new Negation();
					n.setCode("105480006");
					n.setDescription("Refusal of treatment by patient (situation)");
					diagnosticStudyObj.setNegation(n);
				}else{
					diagnosticStudyObj.setStartDate(eachObj.getPerformeOn());
					diagnosticStudyObj.setEndDate(eachObj.getPerformeOn());
				}
				
				diagnosticStudyQDM.add(diagnosticStudyObj);
				
			}
			
			
		}
		if(diagnosisObj!=null){
			diagnosticStudyQDM.addAll(diagnosisObj);
		}
		return diagnosticStudyQDM;
		
	}
	
	public List<Intervention> getInterventionQDM(List<InvestigationQDM> patientInvestigationData, String codeList){
		
		InvestigationQDM eachObj = null;
		Intervention interventionObj;
		List<Intervention> interventionQDM = new ArrayList<Intervention>();
		
		for(int i=0;i<patientInvestigationData.size();i++){
			
			eachObj = patientInvestigationData.get(i);
			interventionObj = new Intervention();
			
			if(codeList.contains(eachObj.getCode())){
				
				interventionObj.setCode(eachObj.getCode());
				
				if(eachObj.getCompanyId() == 54){
					interventionObj.setCodeSystemOID("2.16.840.1.113883.6.96");
				}else if(eachObj.getCompanyId() == 51){
					interventionObj.setCodeSystemOID("2.16.840.1.113883.6.1");
				}
				
				interventionObj.setDescription(eachObj.getCodeDescription());
				if(eachObj.getStatus()==3 || eachObj.getStatus()==4 || eachObj.getStatus()==5 || eachObj.getStatus()==6){
					interventionObj.setStatus(2);
					interventionObj.setStartDate(eachObj.getPerformeOn());
					interventionObj.setEndDate(eachObj.getPerformeOn());
				}else if(eachObj.getStatus() == 1){
					interventionObj.setStartDate(eachObj.getCreatedOn());
					interventionObj.setEndDate(eachObj.getCreatedOn());
				}else if(eachObj.getStatus() == 8){
					interventionObj.setStartDate(eachObj.getCreatedOn());
					interventionObj.setEndDate(eachObj.getCreatedOn());
					Negation n = new Negation();
					n.setCode("105480006");
					n.setDescription("Refusal of treatment by patient (situation)");
					interventionObj.setNegation(n);
				}else{
					interventionObj.setStartDate(eachObj.getPerformeOn());
					interventionObj.setEndDate(eachObj.getPerformeOn());
				}
				
				interventionQDM.add(interventionObj);
				
			}
			
		}
		
		return interventionQDM;
		
	}
	
	public List<Procedure> getProcedureQDM(List<InvestigationQDM> patientInvestigationData, String codeList,List<Date> obj,List<Procedure> ProcedureObj,List<Procedure> ProcBasedOnCPT){

		InvestigationQDM eachObj = null;
		Procedure procedureObj;
		List<Procedure> procedureQDM = new ArrayList<Procedure>();

		for(int i=0;i<patientInvestigationData.size();i++){

			eachObj = patientInvestigationData.get(i);
			procedureObj = new Procedure();
			if(codeList.contains(eachObj.getCode())){

				procedureObj.setCode(eachObj.getCode());

				if(eachObj.getCompanyId() == 54){
					procedureObj.setCodeSystemOID("2.16.840.1.113883.6.96");
				}else if(eachObj.getCompanyId() == 51){
					procedureObj.setCodeSystemOID("2.16.840.1.113883.6.1");
				}

				procedureObj.setDescription(eachObj.getCodeDescription());
				if(eachObj.getStatus()==3 || eachObj.getStatus()==4 || eachObj.getStatus()==5 || eachObj.getStatus()==6){
					procedureObj.setStatus(2);
					procedureObj.setStartDate(eachObj.getPerformeOn());
					procedureObj.setEndDate(eachObj.getPerformeOn());
				}else if(eachObj.getStatus() == 1){
					procedureObj.setStartDate(eachObj.getCreatedOn());
					procedureObj.setEndDate(eachObj.getCreatedOn());
				}else if(eachObj.getStatus() == 8){
					procedureObj.setStartDate(eachObj.getCreatedOn());
					procedureObj.setEndDate(eachObj.getCreatedOn());
					Negation n = new Negation();
					n.setCode("105480006");
					n.setDescription("Refusal of treatment by patient (situation)");
					procedureObj.setNegation(n);
				}else{
					procedureObj.setStartDate(eachObj.getPerformeOn());
					procedureObj.setEndDate(eachObj.getPerformeOn());
				}

				procedureQDM.add(procedureObj);

			}


		}if(obj!=null)
			procedureQDM.addAll(setMedicationObj(obj));

		if(ProcedureObj!=null){
			procedureQDM.addAll(ProcedureObj);
		}
		
		if(ProcBasedOnCPT!=null){
			procedureQDM.addAll(ProcBasedOnCPT);
		}
		
		return procedureQDM;

	}
	
	public List<Procedure> setMedicationObj(List<Date> obj){
		List<Procedure> procList=new ArrayList<Procedure>();
		for(int i=0;i<obj.size();i++){
			Procedure procedureObj=new Procedure();
			procedureObj.setCode("428191000124101");
			procedureObj.setDescription("Documentation of current medications (procedure)");
			procedureObj.setCodeSystemOID("2.16.840.1.113883.6.96");
			procedureObj.setStatus(2);
			Date reviewedDate = obj.get(i);
			try {
				procedureObj.setStartDate(reviewedDate);
				procedureObj.setEndDate(reviewedDate);
			} catch (Exception e) {
				e.printStackTrace();
			}

			procList.add(procedureObj);
		}
		return procList;
	}
	
	public List<Assessment> getAssessmentFromCNM(List<ClinicalDataQDM> clinicalData,String codeList){
		ClinicalDataQDM eachObj= null;
		Assessment assessmentObj;
		List<Assessment> assessmentList=new ArrayList<Assessment>();
		for(int i=0;i<clinicalData.size();i++){
			eachObj=clinicalData.get(i);
			assessmentObj=new Assessment();
			if(codeList.contains(eachObj.getCode())){
				assessmentObj.setCode(eachObj.getCode());
				assessmentObj.setCodeSystem(eachObj.getCodeSystem());
				assessmentObj.setCodeSystemOID(eachObj.getCodeSystemOID());
				assessmentObj.setDescription(eachObj.getDescription());
				assessmentObj.setStartDate(eachObj.getRecordedDate());
				assessmentList.add(assessmentObj);
			}
		}
	return assessmentList;
	}
	
	public List<PhysicalExam> getPhysicalexamFromCNM(List<ClinicalDataQDM> clinicalData,String codeList){
		ClinicalDataQDM eachObj= null;
		PhysicalExam phyExamObj;
		List<PhysicalExam> PhysicalExamList=new ArrayList<PhysicalExam>();
		
		for(int i=0;i<clinicalData.size();i++){
			eachObj=clinicalData.get(i);
			phyExamObj=new PhysicalExam();
			if(codeList.contains(eachObj.getCode())){
			phyExamObj.setCode(eachObj.getCode());
			phyExamObj.setCodeSystem(eachObj.getCodeSystem());
			phyExamObj.setCodeSystemOID(eachObj.getCodeSystemOID());
			phyExamObj.setDescription(eachObj.getDescription());
			phyExamObj.setStartDate(eachObj.getRecordedDate());
			phyExamObj.setResultValue(eachObj.getResultValue());
			PhysicalExamList.add(phyExamObj);
		}
		}
	
	return PhysicalExamList;
	}
	
	public List<Procedure> getProcFromCNM(List<ClinicalDataQDM> clinicalData,String codeList){
		ClinicalDataQDM eachObj= null;
		Procedure procedureObj;
		List<Procedure> procedureList=new ArrayList<Procedure>();
		for(int i=0;i<clinicalData.size();i++){
			eachObj=clinicalData.get(i);
			procedureObj =new Procedure();
			if(codeList.contains(eachObj.getCode())){
				procedureObj.setCode(eachObj.getCode());
				procedureObj.setCodeSystem(eachObj.getCodeSystem());
				procedureObj.setCodeSystemOID(eachObj.getCodeSystemOID());
				procedureObj.setDescription(eachObj.getDescription());
				procedureObj.setStartDate(eachObj.getRecordedDate());
				procedureList.add(procedureObj);
			}
		}
		return procedureList;
	}
	
	public List<Intervention> getInterventionFromCNM(List<ClinicalDataQDM> clinicalData,String codeList){
		ClinicalDataQDM eachObj= null;
		Intervention interventionObj;
		List<Intervention> interventionList=new ArrayList<Intervention>();
		for(int i=0;i<clinicalData.size();i++){
			eachObj=clinicalData.get(i);
			interventionObj =new Intervention();
			if(codeList.contains(eachObj.getCode())){
				interventionObj.setCode(eachObj.getCode());
				interventionObj.setCodeSystem(eachObj.getCodeSystem());
				interventionObj.setCodeSystemOID(eachObj.getCodeSystemOID());
				interventionObj.setDescription(eachObj.getDescription());
				interventionObj.setStartDate(eachObj.getRecordedDate());
				interventionList.add(interventionObj);
			}
		}
		return interventionList;
	}
	
	public List<DiagnosticStudy> getDiagnosisFromCNM(List<ClinicalDataQDM> clinicalData,String codeList){
		ClinicalDataQDM eachObj= null;
		DiagnosticStudy diagnosticObj;
		List<DiagnosticStudy> diagnosticList=new ArrayList<DiagnosticStudy>();
		for(int i=0;i<clinicalData.size();i++){
			eachObj=clinicalData.get(i);
			diagnosticObj =new DiagnosticStudy();
			if(codeList.contains(eachObj.getCode())){
				diagnosticObj.setCode(eachObj.getCode());
				diagnosticObj.setCodeSystem(eachObj.getCodeSystem());
				diagnosticObj.setCodeSystemOID(eachObj.getCodeSystemOID());
				diagnosticObj.setDescription(eachObj.getDescription());
				diagnosticObj.setStartDate(eachObj.getRecordedDate());
				diagnosticList.add(diagnosticObj);
			}
		}
		return diagnosticList;
	}

	public JSONArray getMeasureDetails() {
		return measureDetails;
	}

	public void setMeasureDetails(JSONArray measureDetails) {
		this.measureDetails = measureDetails;
	}

	public HashMap<String, String> getMeasureInfo() {
		return measureInfo;
	}

	public void setMeasureInfo(HashMap<String, String> measureInfo) {
		this.measureInfo = measureInfo;
	}
	
}
