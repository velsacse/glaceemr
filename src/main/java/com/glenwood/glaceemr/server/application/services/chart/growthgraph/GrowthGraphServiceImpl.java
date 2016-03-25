package com.glenwood.glaceemr.server.application.services.chart.growthgraph;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.models.H650;
import com.glenwood.glaceemr.server.application.models.GrowthGraphVitalData;
import com.glenwood.glaceemr.server.application.models.PatientClinicalElements;
import com.glenwood.glaceemr.server.application.models.GrowthGraphPatientData;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.repositories.H650Repository;
import com.glenwood.glaceemr.server.application.repositories.InitialSettingsRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientClinicalElementsRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.specifications.GrowthGraphSpecification;
import com.glenwood.glaceemr.server.application.specifications.InitialSettingsSpecification;
import com.glenwood.glaceemr.server.application.specifications.PatientClinicalElementsSpecification;
import com.glenwood.glaceemr.server.application.specifications.PatientRegistrationSpecification;
import com.glenwood.glaceemr.server.utils.DateUtil;

@Service
@Transactional
public class GrowthGraphServiceImpl implements GrowthGraphService{

	@Autowired
	PatientRegistrationRepository patientRegistrationRepository;
	
	@Autowired
	PatientClinicalElementsRepository patientClinicalElementsRepository;
	
	@Autowired
	InitialSettingsRepository initialSettingsRepository;
	
	@Autowired
	H650Repository h650Repository;
	
	@Autowired
	EntityManagerFactory emf ;

	@PersistenceContext
	EntityManager em;
	
	
	/**
	 * To get default graph id based on patient id
	 * @param patientId
	 * @return default graph id 
	 */
	@Override
	public String getDefaultGraphId(String patientId) {
		
		Integer AgeInDays;
			PatientRegistration patientRegistration=patientRegistrationRepository.findOne(PatientRegistrationSpecification.getPatientPersonalDetails(Integer.parseInt(patientId)));
			AgeInDays		 = DateUtil.dateDiff( DateUtil.DATE , patientRegistration.getPatientRegistrationDob() , new Date()) ;
			String defaultGraphId="";
			if (AgeInDays <= 1095){
				if (patientRegistration.getPatientRegistrationSex()==1)
					defaultGraphId = "1"; // Wt-Age & Ht-Age for Boys 0-36 month
				else
					defaultGraphId = "2";	// Wt-Age & Ht-Age for Girls 0-36 month 
			}
			else{
				if (patientRegistration.getPatientRegistrationSex()==1)
					defaultGraphId = "5"; // Wt-Age & Ht-Age for Boys 2-20 Years
				else
					defaultGraphId = "6";	// Wt-Age & Ht-Age for Girls 2-20 Years
			}
			return defaultGraphId;
	}

	/**
	 * To get patient details based on patient id
	 * @param patientId
	 * @return patient details
	 */
	@Override
	public GrowthGraphPatientData getpatientinfo(String patientId) {
		
		PatientRegistration patientData=patientRegistrationRepository.findOne(
				PatientRegistrationSpecification.getPatientPersonalDetails(Integer.parseInt(patientId)));
		
		int AgeInYear = (int)(DateUtil.dateDiff( DateUtil.DATE , patientData.getPatientRegistrationDob() , new java.util.Date() )/365);
		
		int isNewGrowthGraph=Integer.parseInt(
				initialSettingsRepository.findOne(
						InitialSettingsSpecification.optionName("New Growth Graph")).
						getInitialSettingsOptionValue());
		
		GrowthGraphPatientData patientInfoBean=new GrowthGraphPatientData(
				patientData.getPatientRegistrationAccountno(),
				patientData.getPatientRegistrationFirstName(),
				patientData.getPatientRegistrationId(),
				patientData.getPatientRegistrationSex(),
				AgeInYear,isNewGrowthGraph);
		
		return patientInfoBean;
		
	}

	/**
	 * To get list of vital data's based on patient id
	 * @param patientId
	 * @return list of vital data's
	 */
	@Override
	public List<GrowthGraphVitalData> getVitalValues(String patientId) {
		
		List<GrowthGraphVitalData> clinicalDataBeans=new ArrayList<GrowthGraphVitalData>();
		
		PatientRegistration patientRegistration=patientRegistrationRepository.findOne(PatientRegistrationSpecification.getPatientPersonalDetails(Integer.parseInt(patientId)));
		
        List<String> gwids=new ArrayList<String>(Arrays.asList("0000200200100023000","0000200200100024000","0000200200100039000"));//Gwdid of height,weight and head circumference
		List<PatientClinicalElements> patientDetails=patientClinicalElementsRepository.findAll(PatientClinicalElementsSpecification.getPatClinicalDataByGWDID(Integer.parseInt(patientId), gwids));
		
		Date prevEncounterDate=patientDetails.get(0).getEncounter().getEncounterDate();
		String currentEncounterDateStr=null;
		String prevEncounterDateStr=null;
		Date currentEncounterDate=null;
		Date encounterDate=null;	
		
		GrowthGraphVitalData dataBean=new GrowthGraphVitalData("", "", "", "-", 0, 0, 0);//Initializing the default values
		
		
		for(int i=0;i<patientDetails.size();i++){

			currentEncounterDate=patientDetails.get(i).getEncounter().getEncounterDate();
			
			SimpleDateFormat form = new SimpleDateFormat("MM/dd/yyyy");
			
			currentEncounterDateStr=form.format(currentEncounterDate);
			prevEncounterDateStr=form.format(prevEncounterDate);	
			
			String gwdId=patientDetails.get(i).getPatientClinicalElementsGwid();
			String value=patientDetails.get(i).getPatientClinicalElementsValue();
			
			
			if(prevEncounterDateStr.equalsIgnoreCase(currentEncounterDateStr)||(i==patientDetails.size()-1)){	//before updating list clinicalDataBeans 
				
			if(gwdId.equalsIgnoreCase("0000200200100039000"))//0000200200100039000 is gwdId of Head Circumference
				dataBean.setHeadCircumference(value);
			if(gwdId.equalsIgnoreCase("0000200200100024000"))//0000200200100024000 is gwdId of weight
				dataBean.setWeight(value);
			if(gwdId.equalsIgnoreCase("0000200200100023000"))//0000200200100023000 is gwdId Height 
				dataBean.setHeight(""+Double.parseDouble(value)*0.393700787);
			
			}
			
			
			if((i==patientDetails.size()-1))
				encounterDate=patientDetails.get(i).getEncounter().getEncounterDate();
			else if(i!=0)
				encounterDate=patientDetails.get(i-1).getEncounter().getEncounterDate();
			
			
			
			if(!(prevEncounterDateStr.equalsIgnoreCase(currentEncounterDateStr))||(i==patientDetails.size()-1)){//it is used to save previous encounter information into bean
				
				int ageInDays = ((DateUtil.dateDiff( DateUtil.DATE , patientRegistration.getPatientRegistrationDob() ,encounterDate)%366)%30) ;
				int ageInYear = (int)(DateUtil.dateDiff( DateUtil.DATE ,patientRegistration.getPatientRegistrationDob() ,encounterDate )/366);
				int ageInMonth = (int)((DateUtil.dateDiff( DateUtil.DATE ,patientRegistration.getPatientRegistrationDob() ,encounterDate)%366)/30);

				dataBean.setAgeInDay(ageInDays);
				dataBean.setAgeInMonth(ageInMonth);
				dataBean.setAgeInYear(ageInYear);

				if(i==patientDetails.size()-1)
					dataBean.setEncounterDate(currentEncounterDateStr);
				else
					dataBean.setEncounterDate(prevEncounterDateStr);

				clinicalDataBeans.add(dataBean);
				dataBean=new GrowthGraphVitalData("", "", "", "-", 0, 0, 0);
			}
			
			
			if(!(prevEncounterDateStr.equalsIgnoreCase(currentEncounterDateStr))){//after updating list clinicalDataBeans
			
				if(gwdId.equalsIgnoreCase("0000200200100039000"))//0000200200100039000 is gwdId of Head Circumference
					dataBean.setHeadCircumference(value);
				if(gwdId.equalsIgnoreCase("0000200200100024000"))//0000200200100024000 is gwdId of weight 
					dataBean.setWeight(value);
				if(gwdId.equalsIgnoreCase("0000200200100023000"))//0000200200100023000 is gwdId of Height 
					dataBean.setHeight(""+Double.parseDouble(value)*0.393700787);
			}
			
			
			prevEncounterDate=currentEncounterDate;
		}
		
		return clinicalDataBeans;
	}

	/**
	 * To get list graph details based on patient id
	 * @param patientId
	 * @return list graph details
	 */
	@Override
	public List<H650> getGraphList(String patientId) {
		

		PatientRegistration patientRegistration=patientRegistrationRepository.findOne(PatientRegistrationSpecification.getPatientPersonalDetails(Integer.parseInt(patientId)));
		int ageInMonth = (int)((DateUtil.dateDiff( DateUtil.DATE ,patientRegistration.getPatientRegistrationDob() ,new Date() )%365)/30);
		Integer sex=patientRegistration.getPatientRegistrationSex();
		
		List<H650> graphData=h650Repository.findAll(GrowthGraphSpecification.byAgeandSex(ageInMonth, sex));
		
		return graphData;
	}
}
