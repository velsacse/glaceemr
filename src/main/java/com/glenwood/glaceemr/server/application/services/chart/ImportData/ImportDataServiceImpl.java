package com.glenwood.glaceemr.server.application.services.chart.ImportData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.repositories.EncounterEntityRepository;
import com.glenwood.glaceemr.server.application.services.chart.clinicalElements.ClinicalElementsService;
import com.glenwood.glaceemr.server.application.specifications.EncounterEntitySpecification;

@Service
public class ImportDataServiceImpl implements ImportDataService{


	@Autowired
	EncounterEntityRepository encounterEntityRepository;

	@Autowired
	ClinicalElementsService clinicalElementsService;


	@Override
	public String getImportEncList(Integer patientId, Integer encounterId) {
		List<Encounter> encounters=encounterEntityRepository.findAll(EncounterEntitySpecification.getPrevEncHavingData(patientId,encounterId));
		List<Integer> encList=new ArrayList<Integer>();
		JSONArray encounterList=new JSONArray();
		for(int k=0;k<encounters.size();k++){
			try {
				if(!encList.contains(encounters.get(k).getEncounterId())){
					encList.add(encounters.get(k).getEncounterId());
					JSONObject enc=new JSONObject();
					enc.put("encounterid",encounters.get(k).getEncounterId());
					String encdate = new SimpleDateFormat("MM/dd/yyyy").format(encounters.get(k).getEncounterDate());
					enc.put("encounterdate",encdate+" "+encounters.get(k).getEncounterChiefcomp());
					encounterList.put(enc);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}


		return encounterList.toString();
	}

	@Override
	public void importData(Integer patientId, Integer encounterId,Integer templateId, Integer chartId, Integer tabId,Integer prevEncounterId) {
		List<String> mappedGwids=clinicalElementsService.delPatientElementByEncID(patientId,encounterId,tabId,templateId);
		clinicalElementsService.deleteNotesData(patientId,encounterId,tabId,templateId);
		clinicalElementsService.insertDataForImport(patientId,encounterId,prevEncounterId,tabId,templateId,mappedGwids);
	}
}
