package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.Bean.MIPSPerformanceBean;
import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.ChartcenterEncounter;
import com.glenwood.glaceemr.server.application.models.ChartcenterEncounter_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Encounter_;
import com.glenwood.glaceemr.server.application.models.MacraMeasuresRate;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.ServiceDetail;
import com.glenwood.glaceemr.server.application.models.ServiceDetail_;
import com.glenwood.glaceemr.server.application.models.StaffPinNumberDetails;
import com.glenwood.glaceemr.server.application.models.StaffPinNumberDetails_;
import com.glenwood.glaceemr.server.application.repositories.MacraMeasuresRateRepository;
import com.glenwood.glaceemr.server.application.specifications.QPPPerformanceSpecification;

@Service
@Transactional
public class MUPerformanceRateServiceImpl implements MUPerformanceRateService{

	@PersistenceContext
	EntityManager em;
	
	@Autowired
	MacraMeasuresRateRepository measuresRepo;
	
	public List<Integer> getPatientsSeenByDos(int providerId, Date startDate, Date endDate){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object> cq = builder.createQuery(Object.class);
        Root<ServiceDetail> root = cq.from(ServiceDetail.class);

        List<Integer> patients=new ArrayList<Integer>();
        
        cq.distinct(true);
        cq.select(root.get(ServiceDetail_.serviceDetailPatientid));
        
        if(endDate == null){
        	
        	cq.where(builder.and(builder.equal(builder.function("DATE", Date.class, root.get(ServiceDetail_.serviceDetailDos)), startDate),
        		builder.equal(root.get(ServiceDetail_.serviceDetailSdoctorid), providerId)));
        	
        }else{
        	
        	cq.where(builder.and(builder.between(builder.function("DATE", Date.class, root.get(ServiceDetail_.serviceDetailDos)), startDate, endDate),
            		builder.equal(root.get(ServiceDetail_.serviceDetailSdoctorid), providerId)));
        	
        }
        
        List<Object> result = em.createQuery(cq).getResultList();
        
        for(int i=0;i<result.size();i++){
            patients.add(Integer.parseInt(result.get(i).toString()));
        }
        
        return patients;
		
	}
	
	public List<Integer> getPatientsSeenByEncounter(int providerId, Date startDate, Date endDate){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object> cq = builder.createQuery(Object.class);
        Root<Chart> rootChart = cq.from(Chart.class);

        List<Integer> patients = new ArrayList<Integer>();
        
        Join<Chart,Encounter> joinchartEncounter=rootChart.join(Chart_.encounterTable,JoinType.INNER);
        
        Join<Chart,ChartcenterEncounter> joinChartCenter=rootChart.join(Chart_.chartCenterEncounter,JoinType.INNER);
        cq.distinct(true);
        cq.select(rootChart.get(Chart_.chartPatientid));
        
        Predicate byDate = null, byDate1 = null;
        
        if(endDate == null){
        	
        	byDate = builder.equal(builder.function("DATE", Date.class, joinchartEncounter.get(Encounter_.encounterDate)), startDate);
            joinchartEncounter.on(byDate);
            
            byDate1 = builder.equal(builder.function("DATE", Date.class, joinChartCenter.get(ChartcenterEncounter_.encounterDate)), startDate);
            joinChartCenter.on(byDate1);
        	
        }else{
        	
        	byDate = builder.between(builder.function("DATE", Date.class, joinchartEncounter.get(Encounter_.encounterDate)), startDate, endDate);
            joinchartEncounter.on(byDate);
            
            byDate1 = builder.between(builder.function("DATE", Date.class, joinChartCenter.get(ChartcenterEncounter_.encounterDate)), startDate, endDate);
            joinChartCenter.on(byDate1);
        	
        }
        
        cq.where(builder.equal(joinchartEncounter.get(Encounter_.encounterType),1),
        		builder.equal(joinchartEncounter.get(Encounter_.encounter_service_doctor),providerId));
        
        List<Object> result = em.createQuery(cq).getResultList();
        
        for(int i=0;i<result.size();i++){
            patients.add(Integer.parseInt(result.get(i).toString()));
        }
        
        return patients;
		
	}
	
	@Override
	public List<Integer> getPatientsSeen(int providerId, Date startDate, Date endDate) {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object> cq = builder.createQuery(Object.class);
        Root<PatientRegistration> root = cq.from(PatientRegistration.class);
        
        List<Integer> patientsList = new ArrayList<Integer>();

        List<Integer> patientsSeenByDos = new ArrayList<Integer>();
        List<Integer> patientsSeenByEncounter = new ArrayList<Integer>();
        
        if(endDate == null){
        	patientsSeenByDos = getPatientsSeenByDos(providerId, startDate, null);
            patientsSeenByEncounter = getPatientsSeenByEncounter(providerId, startDate, null);
        }else{
        	patientsSeenByDos = getPatientsSeenByDos(providerId, startDate, endDate);
            patientsSeenByEncounter = getPatientsSeenByEncounter(providerId, startDate, endDate);
        }
        
        if(patientsSeenByDos.size() > 0 && patientsSeenByEncounter.size() > 0){
        	cq.where(builder.or(root.get(PatientRegistration_.patientRegistrationId).in(patientsSeenByDos),
            		root.get(PatientRegistration_.patientRegistrationId).in(patientsSeenByEncounter)));
        }else if(patientsSeenByDos.size() > 0 && patientsSeenByEncounter.size() <= 0){
        	cq.where(root.get(PatientRegistration_.patientRegistrationId).in(patientsSeenByDos));
        }else if(patientsSeenByDos.size() <= 0 && patientsSeenByEncounter.size() > 0){
        	cq.where(root.get(PatientRegistration_.patientRegistrationId).in(patientsSeenByEncounter));
        }
        
        if(patientsSeenByDos.size() > 0 || patientsSeenByEncounter.size() > 0){
        	
        	cq.distinct(true);
            cq.select(root.get(PatientRegistration_.patientRegistrationId));
            
            List<Object> result = em.createQuery(cq).getResultList();
            
            for(int i=0;i<result.size();i++){
            	patientsList.add(Integer.parseInt(result.get(i).toString()));
            }
            
        }
        
        return patientsList;
        
	}
	
	@Override
	public void addToMacraMeasuresRate(Integer providerId, List<MIPSPerformanceBean> providerPerformance, int reportingYear,
			Date startDate, Date endDate, boolean isACI) {
		
		MacraMeasuresRate measureRate = new MacraMeasuresRate();
		Date d = new Date();
		Timestamp curr_time = new Timestamp(d.getTime());
		
		for(int i=0;i<providerPerformance.size();i++){
			
			MIPSPerformanceBean performanceByMeasure = providerPerformance.get(i);

			measureRate = measuresRepo.findOne(Specifications.where(QPPPerformanceSpecification.isRecordExisting(providerId, reportingYear, startDate, endDate, performanceByMeasure.getMeasureId(), performanceByMeasure.getCriteria())));
			
			if(measureRate == null){
			
				measureRate = new MacraMeasuresRate();
				
			}
			
			measureRate.setMacraMeasuresRateMeasureId(performanceByMeasure.getMeasureId());
			measureRate.setMacraMeasuresRateCriteria(performanceByMeasure.getCriteria());
			measureRate.setMacraMeasuresRateMeasureType(1);
			measureRate.setMacraMeasuresRatePerformance(performanceByMeasure.getPerformanceRate());
			measureRate.setMacraMeasuresRatePeriodEnd(endDate);
			measureRate.setMacraMeasuresRatePeriodStart(startDate);
			measureRate.setMacraMeasuresRateProviderId(providerId);
			measureRate.setMacraMeasuresRateReporting(performanceByMeasure.getReportingRate());
			measureRate.setMacraMeasuresRateReportingYear(reportingYear);
			
			measureRate.setMacraMeasuresRateIpp(Integer.parseInt(performanceByMeasure.getIppCount()+""));
			measureRate.setMacraMeasuresRateIpplist(performanceByMeasure.getIppPatientsList());
			
			measureRate.setMacraMeasuresRateDenominator(Integer.parseInt(performanceByMeasure.getDenominatorCount()+""));
			measureRate.setMacraMeasuresRateDenominatorlist(performanceByMeasure.getDenominatorPatientsList());

			measureRate.setMacraMeasuresRateDenominatorExclusion(Integer.parseInt(performanceByMeasure.getDenominatorExclusionCount()+""));
			measureRate.setMacraMeasuresRateDenominatorExclusionlist(performanceByMeasure.getDenominatorExclusionPatientsList());
			
			measureRate.setMacraMeasuresRateNumerator(Integer.parseInt(performanceByMeasure.getNumeratorCount()+""));
			measureRate.setMacraMeasuresRateNumeratorlist(performanceByMeasure.getNumeratorPatientsList());
			
			measureRate.setMacraMeasuresRateDenominatorException(Integer.parseInt(performanceByMeasure.getDenominatorExceptionCount()+""));
			measureRate.setMacraMeasuresRateDenominatorExceptionlist(performanceByMeasure.getDenominatorExceptionPatientsList());
			
			measureRate.setMacraMeasuresRateNumeratorExclusion(Integer.parseInt(performanceByMeasure.getNumeratorExclusionCount()+""));
			measureRate.setMacraMeasuresRateNumeratorExclusionlist(performanceByMeasure.getNumeratorExclusionPatientsList());
			
			measureRate.setMacraMeasuresRateNpi(getNPIForProvider(providerId));
			measureRate.setMacraMeasuresRateTin(getTINForProvider(providerId));
			
			if(isACI){
				measureRate.setMacraMeasuresRatePoints(performanceByMeasure.getPoints());
			}
			
			d = new Date();
			curr_time = new Timestamp(d.getTime());
			
			measureRate.setMacraMeasuresRateUpdatedOn(curr_time);
			
			measuresRepo.saveAndFlush(measureRate);
			
		}
		
	}

	/**
	 * Function to return NPI value for provider
	 * @param providerId
	 * @return
	 */
	
	public String getNPIForProvider(int providerId){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<StaffPinNumberDetails> root = cq.from(StaffPinNumberDetails.class);
		
		cq.select(root.get(StaffPinNumberDetails_.staff_pin_number_details_cctpin_number));
		cq.where(builder.equal(root.get(StaffPinNumberDetails_.staff_pin_number_details_profileid), providerId));
		
		List<Object> results = em.createQuery(cq).getResultList();
		
		String npi = "";
		
		if(results.size() > 0){
			npi = results.get(0).toString();
		}
		
		return npi;
		
	}
	
	/**
	 * Function to get TIN number based on selected providerId
	 * 
	 * @param providerId
	 * @param accountId
	 * @param configuredMeasures
	 * @return
	 */
	
	public String getTINForProvider(int providerId){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<EmployeeProfile> root = cq.from(EmployeeProfile.class);
		
		cq.select(root.get(EmployeeProfile_.empProfileSsn));
		cq.where(builder.equal(root.get(EmployeeProfile_.empProfileEmpid), providerId));
		
		List<Object> results = em.createQuery(cq).getResultList();
		
		String ssn = "";
		
		if(results.size() > 0){
			ssn = results.get(0).toString();
		}
		
		return ssn;
		
	}
	
}
