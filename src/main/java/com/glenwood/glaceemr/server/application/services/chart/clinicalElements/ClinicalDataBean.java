package com.glenwood.glaceemr.server.application.services.chart.clinicalElements;

import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.glenwood.glaceemr.server.application.models.ClinicalElements;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements;
import com.glenwood.glaceemr.server.application.models.PatientClinicalHistory;
import com.glenwood.glaceemr.server.application.models.PatientEpisodeAttributeDetails;
import com.glenwood.glaceemr.server.application.models.cnm.history.PatientClinicalElementsBean;
import com.glenwood.glaceemr.server.application.services.chart.SaveClinicalData.ElementsToDeleteBean;
import com.glenwood.glaceemr.server.application.services.chart.SaveClinicalData.ElementsToSaveBean;
import com.glenwood.glaceemr.server.application.services.chart.SaveClinicalData.PatientEpisodeElementBean;
import com.glenwood.glaceemr.server.application.services.chart.SaveClinicalData.PatientHistoryElementBean;
import com.glenwood.glaceemr.server.utils.HUtil;


@Component
@Scope(value="request",proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ClinicalDataBean {


	public HashMap <String, ClinicalElementBean> clinicalElements=null;
	public HashMap <String,PatientElementBean> patientElements=null;
	public HashMap <String,PatientHistoryElementBean> patientHistoryElements=null;
	public HashMap <String,PatientEpisodeElementBean> patientEpisodeElements=null;
	public HashMap <String,ElementsToSaveBean> elementsToSave=null;
	public HashMap <String,ElementsToDeleteBean> elementsToDelete=null;
	public String clientId="000";

	
	public ClinicalDataBean(){
		clinicalElements=new HashMap<String, ClinicalElementBean>();
		patientElements=new HashMap<String, PatientElementBean>();
		patientHistoryElements=new HashMap<String, PatientHistoryElementBean>();
		patientEpisodeElements=new HashMap<String, PatientEpisodeElementBean>();
		elementsToSave=new HashMap<String, ElementsToSaveBean>();
		elementsToDelete=new HashMap<String, ElementsToDeleteBean>();
	}



	/**
	 * Sets clinical element details to {@link ClinicalElementBean} 
	 * 
	 * @param elementList
	 * 
	 */
	public void setClinicalData(List<ClinicalElements> elementList) {
		for (ClinicalElements element : elementList) {
			ClinicalElementBean clinicalElementBean=new ClinicalElementBean();
			clinicalElementBean.setClinicalElementName(HUtil.Nz(element.getClinicalElementsName(),""));
			clinicalElementBean.setClinicalElementNotes(HUtil.Nz(element.getClinicalElementsNotes(),""));
			clinicalElementBean.setClinicalElementDataType(Integer.parseInt(HUtil.Nz(element.getClinicalElementsDatatype(),"-1")));
			clinicalElementBean.setClinicalElementCPT(HUtil.Nz(element.getClinicalElementsCptcode(),""));
			clinicalElementBean.setClinicalElementICD9(HUtil.Nz(element.getClinicalElementsIcd9code(),""));
			clinicalElementBean.setClinicalElementSNOMED(HUtil.Nz(element.getClinicalElementsSnomed(),""));
			clinicalElementBean.setClinicalElementIsActive(Boolean.parseBoolean(HUtil.Nz(element.getClinicalElementsIsactive(),"false")));
			clinicalElementBean.setClinicalElementIsGlobal(Boolean.parseBoolean(HUtil.Nz(element.getClinicalElementsIsglobal(),"false")));
			clinicalElementBean.setClinicalElementIsHistory(Boolean.parseBoolean(HUtil.Nz(element.getClinicalElementsIshistory(),"false")));
			clinicalElementBean.setClinicalElementIsEpisode(Boolean.parseBoolean(HUtil.Nz(element.getClinicalElementsIsepisode(),"false")));
			clinicalElementBean.setClinicalElementTextDimension(HUtil.Nz(element.getClinicalElementsTextDimension(),""));
			clinicalElementBean.setClinicalElementGender(Integer.parseInt(HUtil.Nz(element.getClinicalElementsGender(),"false")));
			clinicalElementBean.setClinicalElementIsSelect(Integer.parseInt(HUtil.Nz(element.getClinicalElementsIsselect(),"-1")));
			this.setClinicalElements(HUtil.Nz(element.getClinicalElementsGwid(),"-1"), clinicalElementBean);
		}
	}


	/**
	 * 
	 * Sets Patient Clinical Data to {@link PatientElementBean}
	 * 
	 * @param patientElementData
	 */
	public void setPatientClinicalData(List<PatientClinicalElements> patientElementData) {
		for (PatientClinicalElements patData : patientElementData) {
			PatientElementBean patElmtBean=new PatientElementBean();
			String clinicalElementGWId=HUtil.Nz(patData.getPatientClinicalElementsGwid(),"-1");
			int clinicalElementDataType=Integer.parseInt(HUtil.Nz(patData.getClinicalElement().getClinicalElementsDatatype(),"-1"));
			patElmtBean.setPatientClinicalElementId(Integer.parseInt(HUtil.Nz(patData.getPatientClinicalElementsId(),"-1")));
			String elementData=HUtil.Nz(patData.getPatientClinicalElementsValue(),"-1").trim();
			setPatientElementBeanData(clinicalElementDataType,patElmtBean,elementData,clinicalElementGWId);
		}
	}



	/**
	 * 
	 * Sets Patient History Data to {@link PatientElementBean}
	 * 
	 * @param patientHistoryData
	 */
	public void setHistoryDatatoPatElementBean(List<PatientClinicalHistory> patientHistoryData) {

		for (PatientClinicalHistory patHisData : patientHistoryData) {
			PatientElementBean patElmtBean=new PatientElementBean();
			String clinicalElementGWId=HUtil.Nz(patHisData.getPatientClinicalHistoryGwid(),"-1");
			int clinicalElementDataType=Integer.parseInt(HUtil.Nz(patHisData.getClinicalElement().getClinicalElementsDatatype(),"-1"));
			patElmtBean.setPatientClinicalElementId(Integer.parseInt(HUtil.Nz(patHisData.getPatientClinicalHistoryId(),"-1")));
			String elementData=HUtil.Nz(patHisData.getPatientClinicalHistoryValue(),"-1").trim();
			setPatientElementBeanData(clinicalElementDataType,patElmtBean,elementData,clinicalElementGWId);
		}
	}



	/**
	 * Sets Patient History Data to {@link PatientHistoryElementBean}
	 * 
	 * @param patientHistoryData
	 */
	public void setPatientHistoryData(List<PatientClinicalHistory> patientHistoryData) {

		for (PatientClinicalHistory patHisData : patientHistoryData) {
			PatientHistoryElementBean patHistoryElmtBean=new PatientHistoryElementBean();
			String clinicalElementGWId=HUtil.Nz(patHisData.getPatientClinicalHistoryGwid(),"-1");
			int clinicalElementDataType=Integer.parseInt(HUtil.Nz(patHisData.getClinicalElement().getClinicalElementsDatatype(),"-1"));
			patHistoryElmtBean.setpatientHistoryElementId(Integer.parseInt(HUtil.Nz(patHisData.getPatientClinicalHistoryId(),"-1")));
			String elementData=HUtil.Nz(patHisData.getPatientClinicalHistoryValue(),"-1").trim();
			setPatientHistoryBeanData(clinicalElementDataType, patHistoryElmtBean, elementData, clinicalElementGWId);
		}
	}





	/**
	 * Sets Patient Clinical Data to {@link PatientHistoryElementBean}
	 * 
	 * @param patientClinicalData
	 */
	public void setPatientClinData(List<PatientClinicalElements> patientClinicalData) {

		for (PatientClinicalElements patclinicalData : patientClinicalData) {
			PatientHistoryElementBean patHistoryElmtBean=new PatientHistoryElementBean();
			String clinicalElementGWId=HUtil.Nz(patclinicalData.getPatientClinicalElementsGwid(),"-1");
			int clinicalElementDataType=Integer.parseInt(HUtil.Nz(patclinicalData.getClinicalElement().getClinicalElementsDatatype(),"-1"));
			patHistoryElmtBean.setpatientHistoryElementId(Integer.parseInt(HUtil.Nz(patclinicalData.getPatientClinicalElementsId(),"-1")));
			String elementData=HUtil.Nz(patclinicalData.getPatientClinicalElementsValue(),"-1").trim();
			setPatientHistoryBeanData(clinicalElementDataType, patHistoryElmtBean, elementData, clinicalElementGWId);

		}
	}


	/**
	 * Sets Patient Clinical Data to {@link PatientHistoryElementBean}
	 * 
	 * @param patientClinicalData
	 */
	public void setPatientClinData1(List<PatientClinicalElementsBean> patientClinicalData) {

		for (PatientClinicalElementsBean patclinicalData : patientClinicalData) {
			PatientHistoryElementBean patHistoryElmtBean=new PatientHistoryElementBean();
			String clinicalElementGWId=HUtil.Nz(patclinicalData.getPatientClinicalElementsGwid(),"-1");
			int clinicalElementDataType=Integer.parseInt(HUtil.Nz(patclinicalData.getClinicalElementsDatatype(),"-1"));
			patHistoryElmtBean.setpatientHistoryElementId(Integer.parseInt(HUtil.Nz(patclinicalData.getPatientClinicalElementsId(),"-1")));
			String elementData=HUtil.Nz(patclinicalData.getPatientClinicalElementsValue(),"-1").trim();
			setPatientHistoryBeanData(clinicalElementDataType, patHistoryElmtBean, elementData, clinicalElementGWId);
			
		}
	}
	
	

	/**
	 * 
	 * Sets {@link PatientHistoryElementBean} based on clinical Element  Datatype
	 * 
	 * @param clinicalElementDataType
	 * @param patHistoryElmtBean
	 * @param elementData
	 * @param clinicalElementGWId
	 */
	private void setPatientHistoryBeanData(int clinicalElementDataType,PatientHistoryElementBean patHistoryElmtBean,String elementData,String clinicalElementGWId){
		if(clinicalElementDataType==ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_NUMBER){
			patHistoryElmtBean.setpatientHistoryElementNumber(Integer.parseInt(elementData));
		}else if(clinicalElementDataType==ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_TEXT){
			patHistoryElmtBean.setpatientHistoryElementText(elementData.replaceAll("#~#", "\r\n"));
		}else if(clinicalElementDataType==ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_BOOLEAN){
			patHistoryElmtBean.setpatientHistoryElementBoolean(Boolean.parseBoolean(elementData));
		}else if(clinicalElementDataType==ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_SINGLEOPTION || clinicalElementDataType==ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_MULTIPLEOPTION){
			patHistoryElmtBean.setpatientHistoryElementOption(Integer.parseInt(elementData));
			if(clinicalElementDataType==ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_MULTIPLEOPTION){
				clinicalElementGWId=clinicalElementGWId+"_"+elementData;
			}
		}
		this.setPatientHistoryElements(clinicalElementGWId,patHistoryElmtBean);
	}



	/**
	 * 
	 * Sets {@link PatientElementBean} based on clinical Element  Datatype
	 * 
	 * @param clinicalElementDataType
	 * @param patElmtBean
	 * @param elementData
	 * @param clinicalElementGWId
	 */
	private void setPatientElementBeanData(int clinicalElementDataType,PatientElementBean patElmtBean,String elementData,String clinicalElementGWId){

		if(clinicalElementDataType==ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_NUMBER){
			patElmtBean.setPatientClinicalElementNumber(Integer.parseInt(elementData));
		}else if(clinicalElementDataType==ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_TEXT){
			patElmtBean.setPatientClinicalElementText(elementData.replaceAll("#~#", "\r\n"));
		}else if(clinicalElementDataType==ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_BOOLEAN){
			patElmtBean.setPatientClinicalElementBoolean(Boolean.parseBoolean(elementData));
		}else if(clinicalElementDataType==ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_SINGLEOPTION || clinicalElementDataType==ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_MULTIPLEOPTION){
			patElmtBean.setPatientClinicalElementOption(Integer.parseInt(elementData));
			if(clinicalElementDataType==ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_MULTIPLEOPTION){
				clinicalElementGWId=clinicalElementGWId+"_"+elementData;
			}
		}
		else if(clinicalElementDataType==ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_OCX){
			patElmtBean.setPatientClinicalElementText(elementData);
		}
		this.setPatientElements(clinicalElementGWId, patElmtBean);
	}




	/**
	 * Sets patient Clinical Data to {@link PatientElementBean} 
	 * 
	 * @param patientClinicalElements
	 */
	public void setPatientBeanToSave(List<PatientClinicalElements> patientClinicalElements){
		for (PatientClinicalElements patClinicalElement : patientClinicalElements) {
			PatientElementBean patElmtBean=new PatientElementBean();
			String clinicalElementGWId=HUtil.Nz(patClinicalElement.getPatientClinicalElementsGwid(),"-1");
			if(this.getClinicalElements(clinicalElementGWId)!=null){
				int clinicalElementDataType=Integer.parseInt(HUtil.Nz(patClinicalElement.getClinicalElement().getClinicalElementsDatatype(),"-1"));
				String elementData=HUtil.Nz(patClinicalElement.getPatientClinicalElementsValue(),"-1").trim();
				if(clinicalElementDataType==ClinicalConstants.CLINICAL_ELEMENT_DATATYPE_MULTIPLEOPTION){
					clinicalElementGWId=clinicalElementGWId+"_"+elementData;
				}
				patElmtBean.setPatientClinicalElementGWId(clinicalElementGWId);
				patElmtBean.setPatientClinicalElementText(elementData.replaceAll("#~#", "\r\n"));
				this.setPatientElements(clinicalElementGWId, patElmtBean);
			}
		}

	}




	/**
	 * Sets patient Episode Element Data to {@link PatientEpisodeElementBean} 
	 * 
	 * @param patientClinicalElements
	 */
	public void setPatientEpisodeElementsToSave(List<PatientEpisodeAttributeDetails> patientEpisodeAttributeDetails){
		for (PatientEpisodeAttributeDetails patientEpisodeAttributeDet : patientEpisodeAttributeDetails) {
			PatientEpisodeElementBean patEpisodeElmtBean=new PatientEpisodeElementBean();
			String clinicalElementGWId=HUtil.Nz(patientEpisodeAttributeDet.getPatientEpisodeAttributeDetailsGwid(),"-1");
			if(this.getClinicalElements(clinicalElementGWId)!=null){
				patEpisodeElmtBean.setPatientEpisodeElementId(Integer.parseInt(HUtil.Nz(patientEpisodeAttributeDet.getPatientEpisodeAttributeDetailsEpisodeid(),"-1")));
				patEpisodeElmtBean.setPatientEpisodeElementGWId(Long.parseLong(HUtil.Nz(patientEpisodeAttributeDet.getPatientEpisodeAttributeDetailsGwid(),"-1")));
				String elementData=HUtil.Nz(patientEpisodeAttributeDet.getPatientEpisodeAttributeDetailsValue(),"-1").trim();
				patEpisodeElmtBean.setPatientEpisodeElementText(elementData.replaceAll("#~#", "\r\n"));
				this.setPatientEpisodeElements(clinicalElementGWId,patEpisodeElmtBean);
			}
		}
	}


	public ClinicalElementBean getClinicalElements(String elementId) {
		return clinicalElements.get(elementId);
	}
	public void setClinicalElements(String elementId,ClinicalElementBean elementBean) {
		this.clinicalElements.put(elementId, elementBean);
	}
	public PatientElementBean getPatientElements(String elementId) {
		return patientElements.get(elementId);
	}


	public HashMap<String, ClinicalElementBean> getClinicalElements() {
		return clinicalElements;
	}



	public void setClinicalElements(HashMap<String, ClinicalElementBean> clinicalElements) {
		this.clinicalElements = clinicalElements;
	}



	public HashMap<String, PatientElementBean> getPatientElements() {
		return patientElements;
	}



	public void setPatientElements(HashMap<String, PatientElementBean> patientElements) {
		this.patientElements = patientElements;
	}



	public HashMap<String, PatientHistoryElementBean> getPatientHistoryElements() {
		return patientHistoryElements;
	}



	public void setPatientHistoryElements(
			HashMap<String, PatientHistoryElementBean> patientHistoryElements) {
		this.patientHistoryElements = patientHistoryElements;
	}



	public HashMap<String, PatientEpisodeElementBean> getPatientEpisodeElements() {
		return patientEpisodeElements;
	}



	public void setPatientEpisodeElements(
			HashMap<String, PatientEpisodeElementBean> patientEpisodeElements) {
		this.patientEpisodeElements = patientEpisodeElements;
	}



	public HashMap<String, ElementsToSaveBean> getElementsToSave() {
		return elementsToSave;
	}



	public void setElementsToSave(HashMap<String, ElementsToSaveBean> elementsToSave) {
		this.elementsToSave = elementsToSave;
	}



	public HashMap<String, ElementsToDeleteBean> getElementsToDelete() {
		return elementsToDelete;
	}



	public void setElementsToDelete(
			HashMap<String, ElementsToDeleteBean> elementsToDelete) {
		this.elementsToDelete = elementsToDelete;
	}



	public void setPatientElements(String elementId,PatientElementBean patientElements) {
		this.patientElements.put(elementId,patientElements);
	}
	public void setPatientHistoryElements(String elementId,PatientHistoryElementBean patientHistoryElementBean) {
		this.patientHistoryElements.put(elementId,patientHistoryElementBean);
	}
	public PatientHistoryElementBean getPatientHistoryElements(String elementId) {
		return patientHistoryElements.get(elementId);
	}

	public void setPatientEpisodeElements(String elementId,PatientEpisodeElementBean patientEpisodeElementBean) {
		this.patientEpisodeElements.put(elementId,patientEpisodeElementBean);
	}
}