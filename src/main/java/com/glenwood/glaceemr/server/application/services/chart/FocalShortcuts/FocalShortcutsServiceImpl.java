package com.glenwood.glaceemr.server.application.services.chart.FocalShortcuts;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.glenwood.glaceemr.server.application.models.ClinicalElements;
import com.glenwood.glaceemr.server.application.models.ClinicalElements_;
import com.glenwood.glaceemr.server.application.models.FocalShortcut;
import com.glenwood.glaceemr.server.application.models.FocalShortcutElements;
import com.glenwood.glaceemr.server.application.models.FocalShortcutElements_;
import com.glenwood.glaceemr.server.application.models.FocalShortcut_;
import com.glenwood.glaceemr.server.application.models.HpiElement;
import com.glenwood.glaceemr.server.application.models.HpiSymptom;
import com.glenwood.glaceemr.server.application.models.HpiSymptomQualifier;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements;
import com.glenwood.glaceemr.server.application.repositories.FocalShortcutElementsRepository;
import com.glenwood.glaceemr.server.application.repositories.FocalShortcutRepository;
import com.glenwood.glaceemr.server.application.repositories.HpiElementRepository;
import com.glenwood.glaceemr.server.application.repositories.HpiSymptomQualifierRepository;
import com.glenwood.glaceemr.server.application.repositories.HpiSymptomRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientClinicalElementsRepository;
import com.glenwood.glaceemr.server.application.services.chart.SaveClinicalData.SaveClinicalDataService;
import com.glenwood.glaceemr.server.application.specifications.FocalShortcutSpecification;
import com.glenwood.glaceemr.server.application.specifications.HpiSpecification;
import com.glenwood.glaceemr.server.application.specifications.PatientClinicalElementsSpecification;
/**
 * Implementation for FocalShortcuts Service
 * @author Bhagya Lakshmi
 *
 */

@Service
@Transactional
public class FocalShortcutsServiceImpl implements FocalShortcutsService{
	
	@Autowired
	FocalShortcutRepository focalShortcutRepository;
	
	@Autowired
	FocalShortcutElementsRepository focalShortcutElementsRepository;
	
	@Autowired
	SaveClinicalDataService saveClinicalDataService;
	
	@Autowired
	HpiSymptomRepository hpiSymptomRepository;
	
	@Autowired
	PatientClinicalElementsRepository patientClinicalElementsRepository;
	
	@Autowired
	HpiSymptomQualifierRepository hpiSymptomQualifierRepository;
	
	@Autowired
	HpiElementRepository hpiElementRepository;
	
	@PersistenceContext
	EntityManager em;
	
	
	
	/**
	 * Method to get focal shortcuts available for particular tab 
	 */
	@Override
	public JSONArray getFocalshortcutsAvailable(String tabId) {
		List<FocalShortcut> focalShortcutsAvailable = focalShortcutRepository.findAll(FocalShortcutSpecification.getByTabId(tabId));
		JSONArray focal = new JSONArray();
		for(int i=0;i<focalShortcutsAvailable.size();i++)
		{
			JSONObject focalObject = new JSONObject();
			try
			{
				focalObject.put("code", focalShortcutsAvailable.get(i).getFocalShortcutName());
				focalObject.put("shrtid", focalShortcutsAvailable.get(i).getFocalShortcutId());
				focalObject.put("desc", focalShortcutsAvailable.get(i).getFocalShortcutDescription());
				focal.put(i, focalObject);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if(focal.toString().equals("[]"))
		   {    focal.put("No Shortcuts");  
		   }
		return focal;
	}
	
	/**
	 * Method to Search the focal shortcut 
	 */
	@Override
	public JSONArray searchFocalShortcuts(Integer tabId,String focalsearch) {
		List<FocalShortcut> focalShortcuts = focalShortcutRepository.findAll(FocalShortcutSpecification.getByFocalShrtNameAndTabId(tabId,focalsearch));
		JSONArray focal = new JSONArray();
		for(int i=0;i<focalShortcuts.size();i++)
		{
			JSONObject focalObject = new JSONObject();
			try
			{
				focalObject.put("code", focalShortcuts.get(i).getFocalShortcutName());
				focalObject.put("shrtid", focalShortcuts.get(i).getFocalShortcutId());
				focal.put(i, focalObject);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if(focal.toString().equals("[]"))
		   {    
			focal.put("No Shortcuts");  
		   }
		return focal;
	}
	
	/**
	 * deleting particular elements in focal shortcut 
	 */
	@Override
	public void deleteElementsInFocalShortcut(String shortcutId) {
		List<FocalShortcutElements> focalShortcutElem = focalShortcutElementsRepository.findAll(FocalShortcutSpecification.getByFocalShortcutEelmentId(shortcutId));
		for(int i=0;i<focalShortcutElem.size();i++){
			FocalShortcutElements focalShortcut = focalShortcutElem.get(i);
			focalShortcut.setFocalShortcutElementsIsactive(false);
			focalShortcutElementsRepository.saveAndFlush(focalShortcut);
		}
	}
	
	/**
	 * Method to update focal shortcut
	 */
	@Override
	public void updateFocalShortcut(String descriptionShortcut, Boolean isActive, String focalId) {
		List<FocalShortcut> focalShortcut = focalShortcutRepository.findAll(FocalShortcutSpecification.getByFocalShortcutId(focalId));
		for(int i=0;i<focalShortcut.size();i++){
			FocalShortcut focalShortcutBean = focalShortcut.get(i);
			focalShortcutBean.setFocalShortcutIsactive(isActive);
			focalShortcutRepository.saveAndFlush(focalShortcutBean);
		}
		
	}

	/**
	 * Method to get focal shortcut data
	 * @return 
	 */
	@Override
	public JSONArray getFocalShortcutData(String focalIndex, Integer tabId,
			Integer patientId, Integer chartId,
			Integer encounterId, Integer templateId) {
		JSONObject saveDataJSON = new JSONObject();
		JSONArray focalReturnArr = new JSONArray();
		try
		{
			saveDataJSON.put("chartId",chartId);
			saveDataJSON.put("templateId",templateId);
			saveDataJSON.put("patientId",patientId);
			saveDataJSON.put("encounterId",encounterId);
			saveDataJSON.put("chartId",chartId);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<ClinicalElements> root = cq.from(ClinicalElements.class);
		/*Join<ClinicalElements, FocalShortcutElements> join1 = root.join(ClinicalElements_.focalShortcutElements,JoinType.INNER);
		Predicate p1 = builder.equal(join1.get(FocalShortcutElements_.focalShortcutElementsMapid),focalIndex);
		Predicate p2 = builder.equal(join1.get(FocalShortcutElements_.focalShortcutElementsIsactive), true);
		join1.on(builder.and(p1,p2));*/
		
		cq.multiselect(
				root.get(ClinicalElements_.clinicalElementsDatatype),
				root.get(ClinicalElements_.clinicalElementsGwid),
				//join1.get(FocalShortcutElements_.focalShortcutElementsValue),
				root.get(ClinicalElements_.clinicalElementsDatatype));
			
		List<Object[]> obj = em.createQuery(cq).getResultList();
		JSONArray elementsToSaveArr = new JSONArray();
		for(Object[] values: obj)
		{
			//Integer dataType = (Integer) values[3];
			String focalConcat = values[2].toString(); // values[2]====>focalShortcutElementsValue
			try {
				/*if(dataType == 2)
				{
					focalConcat="<![CDATA["+values[2].toString()+"]]";
				}
				else
				{
					focalConcat = values[2].toString();
				}*/
				JSONObject elementSaveObj=new JSONObject();
				elementSaveObj.put("elemenetsToSaveType",values[0]); // values[0]====>clinicalElementsDatatype
				elementSaveObj.put("valueToSave",focalConcat);
				elementSaveObj.put("clinicalelementgwid",values[1]); // values[1]===>clinicalElementsGwid
				elementsToSaveArr.put(elementSaveObj);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		JSONArray elementsToDeleteArr = new JSONArray();
		JSONObject elementDeleteObj=new JSONObject();
		try
		{
			elementDeleteObj.put("elemenetsToDeleteType",-1);
			elementDeleteObj.put("valueToDelete","");
			elementDeleteObj.put("clinicalelementgwid","");
		
			elementsToDeleteArr.put(elementDeleteObj);
		
			saveDataJSON.put("ElementsToSave", elementsToSaveArr);
			saveDataJSON.put("ElementsToDelete", elementsToDeleteArr);
			
			saveClinicalDataService.saveData(patientId, encounterId, chartId, templateId, saveDataJSON.toString());
		
			List<FocalShortcut> focalDesc = focalShortcutRepository.findAll(FocalShortcutSpecification.getByFocalShortcutId(focalIndex));
			for(int i=0;i<focalDesc.size();i++)
			{
				JSONObject focalJObj = new JSONObject();
				focalJObj.put("desc", focalDesc.get(i).getFocalShortcutDescription() == null?"":focalDesc.get(i).getFocalShortcutDescription());
				focalJObj.put("code", focalDesc.get(i).getFocalShortcutName() == null?"":focalDesc.get(i).getFocalShortcutName());
				focalReturnArr.put(i, focalJObj);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return focalReturnArr;
	}


	/**
	 * Method to add new focal shortcut
	 */
	@Override
	public JSONArray addNewFocalShorctut(Integer tabId, Integer patientId,
			Integer encounterId, String symptomIds) {
		List<PatientClinicalElements> patElements = null;
		JSONArray result = null;
		if(tabId==1){
			List<HpiSymptom> hpiSymptomId = hpiSymptomRepository.findAll(HpiSpecification.getBySymptomGWId(symptomIds));
			String gwids="";
			List<Integer> hpiSymptomIdList = new ArrayList<Integer>();
			List<Integer> hpiSymptomQualifierIdList = new ArrayList<Integer>();
			for(int i=0;i<hpiSymptomId.size();i++)
			{
				hpiSymptomIdList.add(hpiSymptomId.get(i).getHpiSymptomId());
			}
			List<HpiSymptomQualifier> hpiSymptomQualifierId = hpiSymptomQualifierRepository.findAll(HpiSpecification.getByQualifierSymptomIdList(hpiSymptomIdList));
			for(int j=0;j<hpiSymptomQualifierId.size();j++)
			{
				hpiSymptomQualifierIdList.add(hpiSymptomQualifierId.get(j).getHpiSymptomQualifierId());
			}
			List<HpiElement> hpiElementGwid = hpiElementRepository.findAll(HpiSpecification.getByHpiElementQualifierIdList(hpiSymptomQualifierIdList));
			for(int k=0;k<hpiElementGwid.size();k++)
			{
				gwids=gwids+hpiElementGwid.get(k).getHpiElementGwid()+",";
			}
			gwids=gwids+"0000100100000000000,0000100200000000000";
			patElements = patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getByPatEncGwId(patientId,encounterId,gwids));
			result = new JSONArray();
			for(int i=0;i<patElements.size();i++)
			{
				JSONObject patElementsObj = new JSONObject();
				try
				{
					patElementsObj.put("patient_clinical_elements_gwid", patElements.get(i).getPatientClinicalElementsGwid());
					patElementsObj.put("patient_clinical_elements_value", patElements.get(i).getPatientClinicalElementsValue());
					result.put(i,patElementsObj);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			List<String> gwidList = new ArrayList<String>();
			for(int k=0;k<result.length();k++){
				try {
				JSONObject data=(JSONObject) result.get(k);
				String gwidStr;
					gwidStr = data.get("patient_clinical_elements_gwid").toString().replace("\"","");
					if(!gwidList.contains(gwidStr))
						gwidList.add(gwidStr);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			List<String> symptomList=new ArrayList<String>();
			String gwid=null;
			String type=null;
			String value=null;
			for(int k=0;k<gwidList.size();k++){
				 List<HpiElement> hpiElement = hpiElementRepository.findAll(HpiSpecification.getByHpiElementGwid(gwidList.get(k)));
				 List<Integer> hpiElementQualifierid = new ArrayList<Integer>();
				 for(int i=0;i<hpiElement.size();i++)
				 {
					 hpiElementQualifierid.add(hpiElement.get(i).getHpiElementQualifierid());
				 }
				 if(hpiElementQualifierid.size()>0)
				 {
					 List<HpiSymptomQualifier> hpiSymptomQualifier = hpiSymptomQualifierRepository.findAll(HpiSpecification.getByQualifierSymptomId(hpiElementQualifierid));
					 List<Integer> hpiSymptomQualifierSymId = new ArrayList<Integer>();
					 for(int i=0;i<hpiSymptomQualifier.size();i++)
					 {
						 hpiSymptomQualifierSymId.add(hpiSymptomQualifier.get(i).getHpiSymptomQualifierSymptomId());
					 }
					 if(hpiSymptomQualifierSymId.size()>0)
					 {
						 List<HpiSymptom> hpiSymptom = hpiSymptomRepository.findAll(HpiSpecification.getByHpiSymptomId(hpiSymptomQualifierSymId));
						  for(int l=0;l<hpiSymptom.size();l++)
						  {
							  gwid= hpiSymptom.get(l).getHpiSymptomGwid();
							  	if(!symptomList.contains(gwid))
							  		symptomList.add(gwid);
						  }
					 }
				 }
			 }
	  
			 for(int i=0;i<symptomList.size();i++){
				  String symgwid=symptomList.get(i).trim();
				  List<HpiSymptom> hpiSymptomType = hpiSymptomRepository.findAll(HpiSpecification.getBySymptomGWId(symgwid));
					for(int s=0;s<hpiSymptomType.size();s++){
						type=hpiSymptomType.get(s).getHpiSymptomType().toString();
						 if(type.equals("2"))
							 value="2";
						 else
							 value="1";
					}
				JSONObject symObj=new JSONObject();
				try
				{
				 symObj.put("patient_clinical_elements_gwid", symgwid);
				 symObj.put("patient_clinical_elements_value", value);
				 result.put(symObj);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * Method to save focal Data
	 * @return 
	 */
	@Override
	public JSONArray saveFocalData(String tabid, String shortcutName,
			String focalDescription, String xmlData) {
		JSONArray saveJson= new JSONArray();
		int flag=0;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<FocalShortcut> root = cq.from(FocalShortcut.class);
		cq.select(root.get(FocalShortcut_.focalShortcutName));
		cq.where(builder.and(builder.equal(root.get(FocalShortcut_.focalShortcutName), shortcutName),builder.equal(root.get(FocalShortcut_.focalShortcutTabid), tabid)));
		List<Object> focalShortcut=em.createQuery(cq).getResultList();
		for(int i=0;i<focalShortcut.size();i++)
		{
			if(focalShortcut.get(i).toString().equalsIgnoreCase(shortcutName))
			{
				flag = 1;
			}
		}
		if(flag == 0)
		{
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setByteStream(new ByteArrayInputStream(xmlData.getBytes()));
				Document doc = dBuilder.parse(is);
				doc.getDocumentElement().normalize();
				NodeList nList =doc.getElementsByTagName("data");
				FocalShortcut focalShortcutInsert = new FocalShortcut();
				focalShortcutInsert.setFocalShortcutName(shortcutName);
				focalShortcutInsert.setFocalShortcutTabid(Integer.parseInt(tabid));
				focalShortcutInsert.setFocalShortcutIsactive(true);
				focalShortcutInsert.setFocalShortcutDescription(focalDescription);
				focalShortcutRepository.saveAndFlush(focalShortcutInsert);
				long rs = Long.valueOf(focalShortcutInsert.getFocalShortcutId().toString());
				for (int temp = 0; temp < nList.getLength(); temp++) {
				       Node nNode = nList.item(temp);
				   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					   		FocalShortcutElements focalShortcutElements = new FocalShortcutElements();			
							Element eElement = (Element) nNode;
					        NodeList nlList = eElement.getElementsByTagName("gwid").item(0).getChildNodes();
					        NodeList nlList2 = eElement.getElementsByTagName("value").item(0).getChildNodes();
					        Node nValue = (Node) nlList.item(0);
					        Node nValue2 = (Node) nlList2.item(0);
					        String gwid=nValue.getNodeValue();
					        String value=nValue2.getNodeValue(); 
					        int id=(int) rs;
					        focalShortcutElements.setFocalShortcutElementsGwid(gwid);
					        focalShortcutElements.setFocalShortcutElementsValue(value);
					        focalShortcutElements.setFocalShortcutElementsIsactive(true);
					        focalShortcutElements.setFocalShortcutElementsMapid(id);
					        focalShortcutElementsRepository.save(focalShortcutElements);
					        if(temp % 20 == 0)
					        {
					        	focalShortcutElementsRepository.flush();
					        }
					}
				}
				saveJson.put("sucess");
			    }
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else
			{     
				String failed="shortcut already present";
				saveJson.put(failed.toString());
			}
			return saveJson;	
		}

}