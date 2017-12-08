/**
 * Utility class for EMeasure and QDMObject creation
 * 
 * @author krishna
 * 
 */

package com.glenwood.glaceemr.server.application.Bean.macra.ecqm;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.Bean.ClinicalDataQDM;
import com.glenwood.glaceemr.server.application.Bean.InvestigationQDM;
import com.glenwood.glaceemr.server.application.Bean.MedicationQDM;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Assessment;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.DiagnosticStudy;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.FunctionalStatus;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Intervention;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.LabTest;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Negation;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.PhysicalExam;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Procedure;
import com.glenwood.glaceemr.server.application.Bean.mailer.GlaceMailer;
import com.glenwood.glaceemr.server.application.Bean.pqrs.PqrsHubResponse;

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
			
			measureInfo.put(""+emeasure.get(index).getId(), emeasure.get(index).getTitle()+"&&&"+emeasure.get(index).isInverseMeasure());
			
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
	
	public List<EMeasure> getMeasureBeanDetails(Integer year,String measureIds, String sharedPath,String accountId) throws Exception{

		List<EMeasure> measureInfo = new ArrayList<EMeasure>();
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);

		for(int i=0;i<measureIds.split(",").length;i++){
			
			int measureId = Integer.parseInt(measureIds.split(",")[i]);

			measureInfo.add(getMeasureInfo(year,measureId, sharedPath,accountId));

		}

		return measureInfo;

	}
	
	private String buildMailContentFormat(String accId, int patientId, String responseString, String exceptionTrace){
		
		String mailContent = "";
		
		mailContent += "<html><body><table border='1' cellspacing='10' cellpadding='10'>";
		
		mailContent += "<tr><td><b>Account Id: </b></td><td>"+accId+"</td></tr>";
		
		if(patientId!=-1){
			mailContent += "<tr><td><b>Patient Id: </b></td><td>"+patientId+"</td></tr>";
		}
		
		mailContent += "<tr><td><b>Error Message: </b></td><td>"+responseString+"</td></tr>";
		if(!exceptionTrace.equals("-1"))
		mailContent += "<tr><td><b>Exception Trace: </b></td><td>"+exceptionTrace+"</td></tr>";
		
		mailContent += "</table></body></html>";
		
		return mailContent;
		
	}
	
	private void putMeasureInfoDetails(Integer year,int measureId, String sharedPath,String accountId) throws Exception{
		
		String result="";
		try {
			String apiUrl = "http://hub-icd10.glaceemr.com/DataGateway/eCQMServices/getECQMInfoById?ids="+measureId;
			
			result = restTemplate.getForObject(apiUrl, String.class);
		} catch (Exception e) {
			GlaceMailer.sendFailureReport("Error Message: Unable to bring EMeasure Info from hub(Connectivity Problem)",accountId,GlaceMailer.Configure.MU);
			throw e;
		}
		
	    writeStringToJsonFile(year,result, measureId, sharedPath,accountId);
	    
	}
	public String getValueSetOID(String code,EMeasure eMeasure)
	{
		String valueSetString="";
		CQMSpecification specification = null;
		HashMap<String, Category> qdmCatagory = null;
		
		String qdmCategoryNames[] = null;
		
		specification = eMeasure.getSpecification();
		qdmCatagory=specification.getQdmCategory();
		
		qdmCategoryNames = new String[qdmCatagory.keySet().size()];
		
		for(int k=0;k<qdmCatagory.keySet().size();k++){

			String categoryName = qdmCatagory.keySet().toArray()[k].toString();
			qdmCategoryNames[k] = categoryName;
			
		}

		for(int i=0;i<qdmCatagory.size();i++){
			
			Category c = qdmCatagory.get(qdmCategoryNames[i]);
			List<Valueset> valueSetList=c.getValueSet();
			for(int eachValueSet=0;eachValueSet<valueSetList.size();eachValueSet++)
			{
				Valueset valueSet =valueSetList.get(eachValueSet);
				List<CodeSet> codeSetList=valueSet.getCodeSetList();
				for(int eachCodeSet=0;eachCodeSet<codeSetList.size();eachCodeSet++)
				{
					CodeSet codeSet=codeSetList.get(eachCodeSet);
					List<Code> codeList=codeSet.getCodeList();
					for(int eachCode=0;eachCode<codeList.size();eachCode++)
					{
						if(codeList.get(eachCode).getCode().equals(code))
						{
							valueSetString=valueSet.getOid();
							valueSetString+="&&&"+valueSet.getName();
						}
					}
				}
				
			}
		}
		
		return valueSetString;
		
		
	}
	public String getCodeAndCodeSystem(String valueSetOID,EMeasure eMeasure)
	{
		String codeAndCodeSystem="";
		CQMSpecification specification = null;
		HashMap<String, Category> qdmCatagory = null;
		
		String qdmCategoryNames[] = null;
		
		specification = eMeasure.getSpecification();
		qdmCatagory=specification.getQdmCategory();
		
		qdmCategoryNames = new String[qdmCatagory.keySet().size()];
		
		for(int k=0;k<qdmCatagory.keySet().size();k++){

			String categoryName = qdmCatagory.keySet().toArray()[k].toString();
			qdmCategoryNames[k] = categoryName;
			
		}
		for(int i=0;i<qdmCatagory.size();i++){
			
			Category c = qdmCatagory.get(qdmCategoryNames[i]);
			List<Valueset> valueSetList=c.getValueSet();
			for(int eachValueSet=0;eachValueSet<valueSetList.size();eachValueSet++)
			{
				Valueset valueSet =valueSetList.get(eachValueSet);
				if(valueSetOID.equals(valueSet.getOid()))
				{
					List<CodeSet> codeSetList=valueSet.getCodeSetList();
					for(int eachCodeSet=0;eachCodeSet<codeSetList.size();eachCodeSet++)
					{
						if(codeSetList.get(eachCodeSet).getCodeSystem().equals("SNOMEDCT") || codeSetList.get(eachCodeSet).getCodeSystem().equals("LOINC"))
						{	
							codeAndCodeSystem=codeSetList.get(eachCodeSet).getCodeList().get(0).getCode()+"&&&"+codeSetList.get(eachCodeSet).getCodeSystemOID();
							return codeAndCodeSystem;
						}
					}
				}
			}
		}
		return codeAndCodeSystem;
	}
	@SuppressWarnings("unchecked")
	public EMeasure getMeasureInfo(Integer year,int measureId, String sharedPath,String accountId)throws Exception{
		
		EMeasure measureInfo = new EMeasure();
					
		String filename = sharedPath+File.separator+"ECQM"+File.separator+year+File.separator+measureId+".json";
		
		File jsonFile = new File(filename);
		
		if(jsonFile.exists() && jsonFile.isFile()){
			long diff = new Date().getTime() - jsonFile.lastModified();
			if (diff > (long) 7 * 24 * 60 * 60 * 1000) {
				jsonFile.delete();

				putMeasureInfoDetails(year,measureId, sharedPath,accountId);
				
				measureInfo=getMeasureInfo(year,measureId, sharedPath,accountId);
				
			}else{

				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				
				List<EMeasure> emeasureInfo = (List<EMeasure>) mapper.readValue(jsonFile, mapper.getTypeFactory().constructCollectionType(List.class, EMeasure.class));
			
				measureInfo = emeasureInfo.get(0);
			
			}
			
		}else{

			putMeasureInfoDetails(year,measureId, sharedPath,accountId);
			
			measureInfo=getMeasureInfo(year,measureId, sharedPath,accountId);
			
		}
			
		return measureInfo;
		
	}
	
	@SuppressWarnings("resource")
	private void writeStringToJsonFile(Integer year,String jsonContent, int measureId, String sharedPath,String accountId) throws Exception{
		
		String filePath = sharedPath+File.separator+"ECQM"+File.separator+year;
		File sharedFile=new File(sharedPath);
		
		if(sharedFile.canRead() && sharedFile.canExecute())
		{

			String filename = filePath+File.separator+measureId+".json";
			
			File f = new File(filename);
			
			try{
				
				if(!f.exists()){
	
					if(!f.getParentFile().exists()){
						f.getParentFile().mkdirs();
					}
					
					f.createNewFile();
					
				}
				
				FileWriter resultFile = new FileWriter(f);
				
				resultFile.write(jsonContent);
				
				resultFile.flush();
				resultFile.close();
				
				
			}catch(Exception e){
				throw e;
			}
		}
		else{
			GlaceMailer.sendFailureReport("Error Message: Unable to access shared Folder in "+accountId,accountId,GlaceMailer.Configure.MU);
			throw new Exception("Unable to access shared Folder in "+accountId);
		}
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
		
		if(measureObj.containsKey("Functional Status")){
			
			snomedCodeList = getCodeList(measureObj,"Functional Status","SNOMEDCT",snomedCodeList);
			loincCodeList = getCodeList(measureObj,"Functional Status","LOINC",loincCodeList);
			
		}

		codeListForCNM.put("SNOMED", snomedCodeList);
		
		codeListForCNM.put("LOINC", loincCodeList);
		
		return codeListForCNM;
		
	}
	
	public List<LabTest> getLabTestQDMForInvestigation(List<InvestigationQDM> patientInvestigationData, String codeList){
		
		InvestigationQDM eachObj = null;
		LabTest labTestObj;
		List<LabTest> labTestQDM = new ArrayList<LabTest>();
		codeList=","+codeList+",";
		
		for(int i=0;i<patientInvestigationData.size();i++){
			
			eachObj = patientInvestigationData.get(i);
			labTestObj = new LabTest();
			
			if(eachObj.getCode()!=null && !eachObj.getCode().equals("") && !eachObj.getCode().equals("0") && !eachObj.getCode().equals("00000") && codeList.contains(","+eachObj.getCode()+",") && eachObj.getCode().length() > 0){
				
				labTestObj.setCode(eachObj.getCode());
				labTestObj.setResultValue(eachObj.getResultValue());
				if(eachObj.getStringCompanyId().equals("54")){
					labTestObj.setCodeSystemOID("2.16.840.1.113883.6.96");
				}else if(eachObj.getStringCompanyId().equals("51") || eachObj.getStringCompanyId().equals("LOINC")){
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
					if(eachObj.getPerformeOn()!=null)
					{
						labTestObj.setStartDate(eachObj.getPerformeOn());
						labTestObj.setEndDate(eachObj.getPerformeOn());
					}
					else
					{
						labTestObj.setStartDate(eachObj.getCreatedOn());
						labTestObj.setEndDate(eachObj.getCreatedOn());
					}
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
		codeList=","+codeList+",";
		for(int i=0;i<patientInvestigationData.size();i++){
			
			eachObj = patientInvestigationData.get(i);
			diagnosticStudyObj = new DiagnosticStudy();
			
			if(eachObj.getCode()!=null && !eachObj.getCode().equals("") && !eachObj.getCode().equals("0") && !eachObj.getCode().equals("00000") && codeList.contains(","+eachObj.getCode()+",") && eachObj.getCode().length() > 0){
				
				diagnosticStudyObj.setCode(eachObj.getCode());
				diagnosticStudyObj.setResultValue(eachObj.getResultValue());
				if(eachObj.getStringCompanyId().equals("54")){
					diagnosticStudyObj.setCodeSystemOID("2.16.840.1.113883.6.96");
				}else if(eachObj.getStringCompanyId().equals("51") || eachObj.getStringCompanyId().equals("LOINC")){
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
					if(eachObj.getPerformeOn()!=null)
					{
						diagnosticStudyObj.setStartDate(eachObj.getPerformeOn());
						diagnosticStudyObj.setEndDate(eachObj.getPerformeOn());
					}
					else
					{
						diagnosticStudyObj.setStartDate(eachObj.getCreatedOn());
						diagnosticStudyObj.setEndDate(eachObj.getCreatedOn());
					}
				}
				
				diagnosticStudyQDM.add(diagnosticStudyObj);
				
			}
			
			
		}
		if(diagnosisObj!=null){
			diagnosticStudyQDM.addAll(diagnosisObj);
		}
		return diagnosticStudyQDM;
		
	}
	
	public List<PhysicalExam> getPhysicalexam(List<InvestigationQDM> patientInvestigationData, String codeList,List<PhysicalExam> physicalExamFromCNM)
	{
		InvestigationQDM eachObj = null;
		PhysicalExam physicalExamObj;
		List<PhysicalExam> physicalExamQDM = new ArrayList<PhysicalExam>();
		codeList=","+codeList+",";
		for(int i=0;i<patientInvestigationData.size();i++){
			
			eachObj = patientInvestigationData.get(i);
			physicalExamObj = new PhysicalExam();
			
			if(eachObj.getCode()!=null && !eachObj.getCode().equals("") && !eachObj.getCode().equals("0") && !eachObj.getCode().equals("00000") && codeList.contains(","+eachObj.getCode()+",") && eachObj.getCode().length() > 0){
				
				physicalExamObj.setCode(eachObj.getCode());
				physicalExamObj.setResultValue(eachObj.getResultValue());
				if(eachObj.getStringCompanyId().equals("54")){
					physicalExamObj.setCodeSystemOID("2.16.840.1.113883.6.96");
				}else if(eachObj.getStringCompanyId().equals("51") || eachObj.getStringCompanyId().equals("LOINC")){
					physicalExamObj.setCodeSystemOID("2.16.840.1.113883.6.1");
				}
				
				physicalExamObj.setDescription(eachObj.getCodeDescription());
				if(eachObj.getStatus()==3 || eachObj.getStatus()==4 || eachObj.getStatus()==5 || eachObj.getStatus()==6){
					physicalExamObj.setStatus(2);
					physicalExamObj.setStartDate(eachObj.getPerformeOn());
					physicalExamObj.setEndDate(eachObj.getPerformeOn());
				}else if(eachObj.getStatus() == 1){
					physicalExamObj.setStartDate(eachObj.getCreatedOn());
					physicalExamObj.setEndDate(eachObj.getCreatedOn());
				}else if(eachObj.getStatus() == 8){
					physicalExamObj.setStartDate(eachObj.getCreatedOn());
					physicalExamObj.setEndDate(eachObj.getCreatedOn());
					Negation n = new Negation();
					n.setCode("105480006");
					n.setDescription("Refusal of treatment by patient (situation)");
					physicalExamObj.setNegation(n);
				}else{
					if(eachObj.getPerformeOn()!=null)
					{
						physicalExamObj.setStartDate(eachObj.getPerformeOn());
						physicalExamObj.setEndDate(eachObj.getPerformeOn());
					}
					else
					{
						physicalExamObj.setStartDate(eachObj.getCreatedOn());
						physicalExamObj.setEndDate(eachObj.getCreatedOn());
					}
				}
				
				physicalExamQDM.add(physicalExamObj);
				
			}
			
			
		}
		if(physicalExamFromCNM!=null){
			physicalExamQDM.addAll(physicalExamFromCNM);
		}
		return physicalExamQDM;
	}
	
	public List<Intervention> getInterventionQDM(List<InvestigationQDM> patientInvestigationData, String codeList){
		
		InvestigationQDM eachObj = null;
		Intervention interventionObj;
		List<Intervention> interventionQDM = new ArrayList<Intervention>();
		codeList=","+codeList+",";
		for(int i=0;i<patientInvestigationData.size();i++){
			
			eachObj = patientInvestigationData.get(i);
			interventionObj = new Intervention();
			
			if(eachObj.getCode()!=null && !eachObj.getCode().equals("") && !eachObj.getCode().equals("0") && !eachObj.getCode().equals("00000") && codeList.contains(","+eachObj.getCode()+",") && eachObj.getCode().length() > 0){
				
				interventionObj.setCode(eachObj.getCode());
				interventionObj.setCode(eachObj.getResultValue());
				if(eachObj.getStringCompanyId().equals("54")){
					interventionObj.setCodeSystemOID("2.16.840.1.113883.6.96");
				}else if(eachObj.getStringCompanyId().equals("51") || eachObj.getStringCompanyId().equals("LOINC")){
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
					if(eachObj.getPerformeOn()!=null)
					{
						interventionObj.setStartDate(eachObj.getPerformeOn());
						interventionObj.setEndDate(eachObj.getPerformeOn());
					}
					else
					{
						interventionObj.setStartDate(eachObj.getCreatedOn());
						interventionObj.setEndDate(eachObj.getCreatedOn());
					}
				}
				
				interventionQDM.add(interventionObj);
				
			}
			
		}
		
		return interventionQDM;
		
	}
	
	public List<Procedure> getProcedureQDM(List<InvestigationQDM> patientInvestigationData, String codeList,List<Procedure> obj,List<Procedure> ProcedureObj,List<Procedure> ProcBasedOnCPT){

		InvestigationQDM eachObj = null;
		Procedure procedureObj;
		List<Procedure> procedureQDM = new ArrayList<Procedure>();
		codeList=","+codeList+",";
		
		for(int i=0;i<patientInvestigationData.size();i++){

			eachObj = patientInvestigationData.get(i);
			procedureObj = new Procedure();
			
			if(eachObj.getCode()!=null && !eachObj.getCode().equals("") && !eachObj.getCode().equals("0") && !eachObj.getCode().equals("00000") && codeList.contains(","+eachObj.getCode()+",") && eachObj.getCode().length() > 0){

				procedureObj.setCode(eachObj.getCode());
				procedureObj.setResultValue(eachObj.getResultValue());
				if(eachObj.getStringCompanyId().equals("54")){
					procedureObj.setCodeSystemOID("2.16.840.1.113883.6.96");
				}else if(eachObj.getStringCompanyId().equals("51") || eachObj.getStringCompanyId().equals("LOINC")){
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
					if(eachObj.getPerformeOn()!=null)
					{
						procedureObj.setStartDate(eachObj.getPerformeOn());
						procedureObj.setEndDate(eachObj.getPerformeOn());
					}
					else
					{
						procedureObj.setStartDate(eachObj.getCreatedOn());
						procedureObj.setEndDate(eachObj.getCreatedOn());
					}
				}

				procedureQDM.add(procedureObj);

			}


		}if(obj!=null)
			procedureQDM.addAll(obj);

		if(ProcedureObj!=null){
			procedureQDM.addAll(ProcedureObj);
		}
		
		if(ProcBasedOnCPT!=null){
			procedureQDM.addAll(ProcBasedOnCPT);
		}
		
		return procedureQDM;

	}
	
	public List<Procedure> setMedicationObj(List<MedicationQDM> obj){
		List<Procedure> procList=new ArrayList<Procedure>();
		for(int i=0;i<obj.size();i++){
			Procedure procedureObj=new Procedure();
			if(obj.get(0).getReviewStatus()==true){
			procedureObj.setCode("428191000124101");
			procedureObj.setDescription("Documentation of current medications (procedure)");
			procedureObj.setCodeSystemOID("2.16.840.1.113883.6.96");
			procedureObj.setStatus(2);
			Date reviewedDate = obj.get(0).getAttestationDate();
			try {
				procedureObj.setStartDate(reviewedDate);
				procedureObj.setEndDate(reviewedDate);
			} catch (Exception e) {
				e.printStackTrace();
			}

			procList.add(procedureObj);}
		}
		return procList;
	}
	
	public List<Assessment> getAssessmentFromCNM(List<ClinicalDataQDM> clinicalData,String codeList){
		ClinicalDataQDM eachObj= null;
		Assessment assessmentObj;
		List<Assessment> assessmentList=new ArrayList<Assessment>();
		codeList=","+codeList+",";
		for(int i=0;i<clinicalData.size();i++){
			eachObj=clinicalData.get(i);
			assessmentObj=new Assessment();
			if(eachObj.getCode()!=null && !eachObj.getCode().equals("") && !eachObj.getCode().equals("0") && !eachObj.getCode().equals("00000") && codeList.contains(","+eachObj.getCode()+",") && eachObj.getCode().length() > 0){
				assessmentObj.setCode(eachObj.getCode());
				assessmentObj.setCodeSystem(eachObj.getCodeSystem());
				assessmentObj.setCodeSystemOID(eachObj.getCodeSystemOID());
				assessmentObj.setResultCode(eachObj.getResultCode());
				assessmentObj.setResultCodeSystemOID(eachObj.getResultCodeSystem());
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
			if(eachObj.getCode()!=null && !eachObj.getCode().equals("") && !eachObj.getCode().equals("0") && !eachObj.getCode().equals("00000") && codeList.contains(","+eachObj.getCode()+",") && eachObj.getCode().length() > 0){
				phyExamObj.setCode(eachObj.getCode());
				phyExamObj.setCodeSystem(eachObj.getCodeSystem());
				phyExamObj.setCodeSystemOID(eachObj.getCodeSystemOID());
				phyExamObj.setResultCode(eachObj.getResultCode());
				phyExamObj.setResultCodeSystemOID(eachObj.getResultCodeSystem());
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
		codeList=","+codeList+",";
		
		for(int i=0;i<clinicalData.size();i++){
			eachObj=clinicalData.get(i);
			procedureObj =new Procedure();
			
			if(eachObj.getCode()!=null && !eachObj.getCode().equals("") && !eachObj.getCode().equals("0") && !eachObj.getCode().equals("00000") && codeList.contains(","+eachObj.getCode()+",") && eachObj.getCode().length() > 0){
				procedureObj.setCode(eachObj.getCode());
				procedureObj.setCodeSystem(eachObj.getCodeSystem());
				procedureObj.setCodeSystemOID(eachObj.getCodeSystemOID());
				procedureObj.setResultCode(eachObj.getResultCode());
				procedureObj.setResultCodeSystemOID(eachObj.getResultCodeSystem());
				procedureObj.setStartDate(eachObj.getRecordedDate());
				procedureList.add(procedureObj);
			}
		}
		return procedureList;
	}

	public List<FunctionalStatus> getFunctionalStatus(List<ClinicalDataQDM> clinicalData,String codeList){
		ClinicalDataQDM eachObj= null;
		FunctionalStatus functionalStatusObj;
		List<FunctionalStatus> functionalStatusList=new ArrayList<FunctionalStatus>();
		codeList=","+codeList+",";
		
		for(int i=0;i<clinicalData.size();i++){
			eachObj=clinicalData.get(i);
			functionalStatusObj =new FunctionalStatus();
			
			if(eachObj.getCode()!=null && !eachObj.getCode().equals("") && !eachObj.getCode().equals("0") && !eachObj.getCode().equals("00000") && codeList.contains(","+eachObj.getCode()+",") && eachObj.getCode().length() > 0){
				functionalStatusObj.setCode(eachObj.getCode());
				functionalStatusObj.setCodeSystem(eachObj.getCodeSystem());
				functionalStatusObj.setCodeSystemOID(eachObj.getCodeSystemOID());
				functionalStatusObj.setResultCode(eachObj.getResultCode());
				functionalStatusObj.setResultCodeSystemOID(eachObj.getResultCodeSystem());
				functionalStatusObj.setStartDate(eachObj.getRecordedDate());
				functionalStatusList.add(functionalStatusObj);
			}
		}
		return functionalStatusList;
	}
	
	public List<Intervention> getInterventionFromCNM(List<ClinicalDataQDM> clinicalData,String codeList){
		ClinicalDataQDM eachObj= null;
		Intervention interventionObj;
		List<Intervention> interventionList=new ArrayList<Intervention>();
		codeList=","+codeList+",";
		
		for(int i=0;i<clinicalData.size();i++){
			eachObj=clinicalData.get(i);
			interventionObj =new Intervention();
			if(eachObj.getCode()!=null && !eachObj.getCode().equals("") && !eachObj.getCode().equals("0") && !eachObj.getCode().equals("00000") && codeList.contains(","+eachObj.getCode()+",") && eachObj.getCode().length() > 0){
				interventionObj.setCode(eachObj.getCode());
				interventionObj.setCodeSystem(eachObj.getCodeSystem());
				interventionObj.setCodeSystemOID(eachObj.getCodeSystemOID());
				interventionObj.setResultCode(eachObj.getResultCode());
				interventionObj.setResultCodeSystemOID(eachObj.getResultCodeSystem());
				interventionObj.setStartDate(eachObj.getRecordedDate());
				if(eachObj.getStatus()==3)
				{
					Negation negationObj=new Negation();
					negationObj.setCode(eachObj.getNotDoneCode());
					negationObj.setDescription(eachObj.getNotDoneDesc());
					negationObj.setCodeSystemOID(eachObj.getCodeSystemOID());
					interventionObj.setNegation(negationObj);
					
				}
				interventionList.add(interventionObj);
				
			}
		}
		return interventionList;
		
	}

	public List<DiagnosticStudy> getDiagnosisFromCNM(List<ClinicalDataQDM> clinicalData,String codeList){
		ClinicalDataQDM eachObj= null;
		DiagnosticStudy diagnosticObj;
		List<DiagnosticStudy> diagnosticList=new ArrayList<DiagnosticStudy>();
		codeList=","+codeList+",";
		
		for(int i=0;i<clinicalData.size();i++){
			eachObj=clinicalData.get(i);
			diagnosticObj =new DiagnosticStudy();
			if(eachObj.getCode()!=null && !eachObj.getCode().equals("") && !eachObj.getCode().equals("0") && !eachObj.getCode().equals("00000") && codeList.contains(","+eachObj.getCode()+",") && eachObj.getCode().length() > 0){
				diagnosticObj.setCode(eachObj.getCode());
				diagnosticObj.setCodeSystem(eachObj.getCodeSystem());
				diagnosticObj.setCodeSystemOID(eachObj.getCodeSystemOID());
				diagnosticObj.setResultCode(eachObj.getResultCode());
				diagnosticObj.setResultCodeSystemOID(eachObj.getResultCodeSystem());
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
	
	@SuppressWarnings("unchecked")
	public List<PqrsHubResponse> getPpqrsHubReponse(String measureIds) throws Exception{
		
		String apiUrl = "http://hub-icd10.glaceemr.com/DataGateway/PQRSServices/getPQRSMeasuresInfo?measureIds="+measureIds;
	    String result = restTemplate.getForObject(apiUrl, String.class);
	    JSONObject jsonObj = new JSONObject(result);
		Iterator<String> keys = jsonObj.keys();
		String content = "";
		while(keys.hasNext()){
			if(content.equals("")){
				content = jsonObj.getString(keys.next());
			}else{
				content = content.concat(",".concat(jsonObj.getString(keys.next())));
			}
		}
		content = "[".concat(content.concat("]"));
		getPqrsMeasureInfo(content);
	    
		JSONArray arr=new JSONArray(content);
	    ObjectMapper mapper=new ObjectMapper();
	    List<PqrsHubResponse> li=new ArrayList<>();
	   
	    for(int i=0;i<arr.length();i++){
	    PqrsHubResponse cl=new PqrsHubResponse();
		cl = mapper.readValue(arr.getJSONObject(i).toString(), PqrsHubResponse.class);
		li.add(cl);
	    }
		return li;
	}
	
	public void getPqrsMeasureInfo(String data) throws Exception{
	
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
	
	public  HashMap<String, String> getPqrsCodeList(List<PqrsHubResponse> emeasure){

		for(int index = 0;index < emeasure.size();index++)
		{
				measureInfo.put(""+emeasure.get(index).getId(), emeasure.get(index).getTitle());
			}
			setMeasureInfo(measureInfo);
			return measureInfo;
			
		}
	
	
}
