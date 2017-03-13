package com.glenwood.glaceemr.server.application.services.pastencounters;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Encounter_;
import com.glenwood.glaceemr.server.application.models.LeafLibrary;
import com.glenwood.glaceemr.server.application.models.LeafLibrary_;
import com.glenwood.glaceemr.server.application.models.LeafPatient;
import com.glenwood.glaceemr.server.application.models.LeafPatient_;

@Service
public class PastEncountersServiceImpl implements PastEncountersService{

	@PersistenceContext
	EntityManager em;
	
	@Override
	public String getPastEncounters(Integer patientId, Integer chartId) throws JSONException {
		
		try{
			if(chartId==-1)
				chartId= getChartId(patientId);

			List<Object[]> result= getPastEncounters(chartId);
			return parsePastEncounters(result);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get list of past encounters
	 * @param chartId
	 * @return
	 */
	private List<Object[]> getPastEncounters(Integer chartId) {
		CriteriaBuilder builder= em.getCriteriaBuilder();
		CriteriaQuery<Object[]> query= builder.createQuery(Object[].class);
		Root<Encounter> root= query.from(Encounter.class);
		Join<Encounter, EmployeeProfile> empJoin= root.join(Encounter_.empProfileEmpId, JoinType.LEFT);
		Join<Encounter, LeafPatient> leafJoin= root.join(Encounter_.leafPatient, JoinType.LEFT);
		Join<LeafPatient, LeafLibrary> libraryJoin= leafJoin.join(LeafPatient_.leafLibraryTable, JoinType.INNER);
		
		query.multiselect(root.get(Encounter_.encounterId),
						   root.get(Encounter_.encounterChiefcomp),
						   	root.get(Encounter_.encounterDate),
						   	 empJoin.get(EmployeeProfile_.empProfileFullname),
						   	  libraryJoin.get(LeafLibrary_.leafLibraryName),
						   	   libraryJoin.get(LeafLibrary_.leafLibraryId),
						   	    libraryJoin.get(LeafLibrary_.leafLibrarySoaptemplateId),
						   	     leafJoin.get(LeafPatient_.leafPatientIscomplete),
						   	      leafJoin.get(LeafPatient_.leafPatientId));
		
		query.where(builder.equal(root.get(Encounter_.encounterChartid), chartId),
					 builder.equal(root.get(Encounter_.encounterType), 1),
					  builder.notEqual(root.get(Encounter_.encounterStatus), -1));
		
		query.orderBy(builder.desc(root.get(Encounter_.encounterId)),
					   builder.asc(leafJoin.get(LeafPatient_.leafPatientLastmoddate)));
		
		List<Object[]> result= null;
		try{
			result= em.createQuery(query).getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return result;
	}

	/**
	 * Parsing results into JSON
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	public String parsePastEncounters(List<Object[]> result) throws JSONException{
		JSONArray encounterList= new JSONArray();		
		for(int i=0; i<result.size();){			
			JSONObject encounter= new JSONObject();
			Object[] resultRow= result.get(i);
			int encounterId= Integer.parseInt(resultRow[0].toString());
			encounter.put("encounterId", encounterId);
			encounter.put("encounterCC", resultRow[1]);
			encounter.put("encounterDate", resultRow[2]);
			encounter.put("serviceDoctor", resultRow[3]);
			JSONArray templateList= new JSONArray();
			for(int j=i; j<result.size(); j++){				
				Object[] row= result.get(j);
				int nxtEncId= Integer.parseInt(row[0].toString());				
				if(encounterId== nxtEncId){
					if(row[8]!=null){
						JSONObject template= new JSONObject();
						template.put("templatename", row[4]);
						template.put("formId", row[5]);
						template.put("templateId", row[6]);
						template.put("isSigned", row[7]);
						template.put("leafId", row[8]);
						templateList.put(template);
					}
				}else
					break;
			}
			encounter.put("clinicalTemplates", templateList);
			if(templateList.length()> 0){
				encounter.put("isTemplates", "yes");
				i= i+templateList.length();
			}
			else{
				encounter.put("isTemplates", "no");
				i= i+1;
			}
			encounterList.put(encounter);
			
		}
		return encounterList.toString();
	}
	
	/**
	 * Get chartId from patientId
	 * @param patientId
	 * @return
	 */
	public Integer getChartId(int patientId){
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<Chart> root = cq.from(Chart.class);
		cq.select(root.get(Chart_.chartId));
		cq.where(builder.equal(root.get(Chart_.chartPatientid), patientId));
		List <Object> resultList = em.createQuery(cq).getResultList();
		Integer chartId= -1;
		if(resultList.size()>0)
			chartId= Integer.parseInt(resultList.get(0).toString());
		return chartId;

	}
	
}
