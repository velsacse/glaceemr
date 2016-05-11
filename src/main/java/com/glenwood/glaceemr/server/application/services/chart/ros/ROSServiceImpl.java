package com.glenwood.glaceemr.server.application.services.chart.ros;



import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.glenwood.glaceemr.server.application.models.ClinicalElements;
import com.glenwood.glaceemr.server.application.models.ClinicalSystem;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements;
import com.glenwood.glaceemr.server.application.models.RosElement;
import com.glenwood.glaceemr.server.application.repositories.ClinicalElementsRepository;
import com.glenwood.glaceemr.server.application.repositories.ClinicalSystemRepository;
import com.glenwood.glaceemr.server.application.repositories.EncounterEntityRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientClinicalElementsRepository;
import com.glenwood.glaceemr.server.application.repositories.ROSElementRepository;
import com.glenwood.glaceemr.server.application.services.chart.clinicalElements.ClinicalConstants;
import com.glenwood.glaceemr.server.application.services.chart.clinicalElements.ClinicalDataBean;
import com.glenwood.glaceemr.server.application.services.chart.clinicalElements.ClinicalElementBean;
import com.glenwood.glaceemr.server.application.services.chart.clinicalElements.ClinicalElementsService;
import com.glenwood.glaceemr.server.application.services.chart.clinicalElements.PatientDetailsBean;
import com.glenwood.glaceemr.server.application.services.chart.clinicalElements.PatientElementBean;
import com.glenwood.glaceemr.server.application.specifications.PatientClinicalElementsSpecification;
import com.glenwood.glaceemr.server.application.specifications.ROSSpecification;
import com.glenwood.glaceemr.server.utils.HUtil;

@Service
public class ROSServiceImpl implements ROSService {

	@Autowired
	ClinicalSystemRepository clinicalSystemRepository;

	@Autowired
	ROSElementRepository rosElementRepository;

	@Autowired
	ClinicalElementsRepository clinicalElementsRepository;

	@Autowired
	ClinicalElementsService clinicalElementService;

	@Autowired
	ClinicalDataBean clinicalDataBean;

	@Autowired
	PatientClinicalElementsRepository patientClinicalElementsRepository;

	@Autowired
	EncounterEntityRepository encounterRepository;

	@Autowired
	ROSBean rosBean;

	@Autowired
	PatientDetailsBean patientDetailsBean;
	
	

	@Override
	public List<ROSSystemBean> getROSElements(String clientId,Integer patientId, Integer chartId,Integer encounterId, Integer templateId) {

		getROSElements(templateId);
		setClinicalDataBean(patientId,encounterId,templateId,3,clientId);
		return frameResponseBean();
	}

	private List<ROSSystemBean> frameResponseBean() {
		List<ROSSystemBean> responseROSBean=new ArrayList<ROSSystemBean>();
		LinkedHashMap<Integer,ROSSystemBean> rosHashMap = rosBean.getROS();
		Set<Entry<Integer,ROSSystemBean>> systemHashMap = rosHashMap.entrySet();
		int systemId = -1;

		for(Entry<Integer,ROSSystemBean> currentSystem:systemHashMap){
			systemId = currentSystem.getKey();
			
			ROSSystemBean  rosSystemBean = currentSystem.getValue();
			String elementGWId = "0000000000000000000";
			List<ROSElementBean> roselements=rosSystemBean.getROSElements();
			
			List<ROSElementBean> systemElementsList=new ArrayList<ROSElementBean>();
			for(int i=0;i<roselements.size();i++){
				ROSElementBean rosElementBean = roselements.get(i);
				String elementName      = rosElementBean.getElementName().replace("!@@!", "\\");
				
				String elementPrintText = rosElementBean.getElementPrintText().replace("!@@!", "\\");
				elementGWId=rosElementBean.getElementGWID();
				ClinicalElementBean ceBean = clinicalDataBean.getClinicalElements(elementGWId);
				PatientElementBean patientElementBean=clinicalDataBean.getPatientElements(elementGWId);
				if(ceBean!=null){
					
					if(patientElementBean!=null){
						if(ceBean.getClinicalElementDataType() == ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_BOOLEAN){
							systemElementsList.add(getROSElementBean(elementName,elementPrintText,elementGWId,ceBean.getClinicalElementDataType(),patientElementBean.getPatientClinicalElementBoolean().toString()));
						}else{
							systemElementsList.add(getROSElementBean(elementName,elementPrintText,elementGWId,ceBean.getClinicalElementDataType(),patientElementBean.getPatientClinicalElementText().toString()));
						}
					}else{
						systemElementsList.add(getROSElementBean(elementName,elementPrintText,elementGWId,ceBean.getClinicalElementDataType(),""));
					}
				}
			}
			
			ROSSystemBean rosSystem=new ROSSystemBean();
			if(systemElementsList.size()>0){
				rosSystem.setSytemId(systemId);
				rosSystem.setSystemName((rosSystemBean.getSystemName()).replace("!@@!", "\\"));
				rosSystem.setEandMType(rosSystemBean.getEandMType());
				rosSystem.setDeferredGWID(rosSystemBean.getDeferredGWID());
				rosSystem.setROSElements(systemElementsList);
				responseROSBean.add(rosSystem);
				
			}


		}
		return responseROSBean;
	}


	public ROSElementBean getROSElementBean(String elementName,String elementPrintText,String elementGwid,Integer dataType,String value){
		ROSElementBean elementBean=new ROSElementBean();
		elementBean.setElementName(elementName);
		elementBean.setElementGWID(elementGwid);
		elementBean.setElementPrintText(elementPrintText);
		elementBean.setDataType(dataType);
		elementBean.setValue(value);
		return elementBean;
	}




	public  void getROSElements(Integer templateId) {
		List<ClinicalSystem> rosSystems=clinicalSystemRepository.findAll(ROSSpecification.getActiveROSSystems(templateId));
		List<String> activeRosSystemIds = new ArrayList<String>();
		activeRosSystemIds.add("-1");
		for(int i=0; i<rosSystems.size(); i++){
			ROSSystemBean rosSystemBean=new ROSSystemBean(); 
			rosSystemBean.setSytemId(Integer.parseInt(HUtil.Nz(rosSystems.get(i).getClinicalSystemRosGwid(),"-1")));
			rosSystemBean.setSystemName(HUtil.Nz(rosSystems.get(i).getClinicalSystemName(),"-1"));
			rosSystemBean.setEandMType(HUtil.Nz(rosSystems.get(i).getClinicalSystemRosEandmtype(),"-1"));
			rosSystemBean.setDeferredGWID(HUtil.Nz(rosSystems.get(i).getClinicalSystemRosDeferredGwid(),""));
			activeRosSystemIds.add(rosSystemBean.getSystemId()+"");
			rosBean.getROS().put(rosSystemBean.getSystemId(), rosSystemBean);
		}
		getROSActiveElementBySystem(activeRosSystemIds);
	}


	public void getROSActiveElementBySystem(List<String> activeRosSystemIds){
		List<RosElement> activeROSElements=rosElementRepository.findAll(ROSSpecification.getActiveROSElementBySystem(activeRosSystemIds));
		for(int j=0; j<activeROSElements.size(); j++){
			ROSElementBean rosElementBean = new ROSElementBean();
			RosElement rosList = activeROSElements.get(j);
			rosElementBean.setElementGWID(HUtil.Nz(rosList.getRosElementGwid(),"0000000000000000000"));
			rosElementBean.setElementName(HUtil.Nz(rosList.getRosElementName(),"-1"));
			rosElementBean.setElementPrintText(HUtil.Nz(rosList.getRosElementPrinttext(),"-1"));
			rosBean.getROS().get(rosList.getRosElementSystemId()).getROSElements().add(rosElementBean);
		}
	}

	public List<ClinicalElements> setClinicalDataBean(Integer patientId,Integer encounterId,Integer templateId,Integer tabType,String clientId){
		clinicalDataBean.setClientId(clientId);
		return clinicalElementService.setClinicalDataBean(patientId, encounterId, templateId, tabType,"01004%");

	}

	@Override
	public String getROSNotes(Integer patientId, Integer encounterId) {
		List<PatientClinicalElements> notesDataList=patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getPatClinEleByGWID(encounterId,patientId,"0000100400000000001"));
		return notesDataList.size()>0 ? notesDataList.get(0).getPatientClinicalElementsValue() : "" ;

	}

}
