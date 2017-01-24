package com.glenwood.glaceemr.server.application.services.chart.MIPS;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.Bean.AttestationBean;
import com.glenwood.glaceemr.server.application.Bean.ReportingInfo;
import com.glenwood.glaceemr.server.application.models.AttestationStatus;
import com.glenwood.glaceemr.server.application.models.AttestationStatus_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.repositories.AttestationStatusRepository;
import com.glenwood.glaceemr.server.application.repositories.EmployeeProfileRepository;
import com.glenwood.glaceemr.server.application.services.employee.EmployeeDataBean;
import com.glenwood.glaceemr.server.application.specifications.AttestationStatusSpecification;


@Service
@Transactional
public class AttestationStatusServiceImpl implements AttestationStatusService{

	@Autowired
	AttestationStatusRepository attestationRepo;
	
	@Autowired
	EmployeeProfileRepository employeeRepo;
	
	@PersistenceContext
	EntityManager em;
	
	@Override
	public List<AttestationStatus> getProviderReportingDetails(Integer reportingYear) {
		
		List<AttestationStatus> response = attestationRepo.findAll(Specifications.where(AttestationStatusSpecification.getReportingStatus(reportingYear)));
        
		return response;
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmployeeDataBean> getActiveProviderList(){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<EmployeeDataBean> cq = builder.createQuery(EmployeeDataBean.class);
		Root<EmployeeProfile> root = cq.from(EmployeeProfile.class);
		
		Predicate[] restrictions = new Predicate[] {
				builder.equal(root.get(EmployeeProfile_.empProfileIsActive),true),
				root.get(EmployeeProfile_.empProfileGroupid).in(-1,-10),
		};
		
		cq.multiselect(new Selection[]{root.get(EmployeeProfile_.empProfileEmpid), root.get(EmployeeProfile_.empProfileFullname)});
		
		cq.where(restrictions);
		
		cq.orderBy(builder.asc(root.get(EmployeeProfile_.empProfileFullname)));
		
		Query query=em.createQuery(cq);
		
		List<EmployeeDataBean> employeeList = query.getResultList();
		
		return employeeList;
		
	}

	@Override
	public List<AttestationStatus> saveReportingDetails(Integer reportingYear, Integer reportingMethod, Integer reportingType, String comments, Integer reportingStatus, Integer providerId) {
		
		if(providerId == -1){
			
			List<EmployeeDataBean> employeeList = getActiveProviderList();
			
			for(int i=0;i<employeeList.size();i++){
				
				insertAttestationRecord(reportingYear,reportingMethod,reportingType,comments,getReportingStatus(reportingStatus),employeeList.get(i).getEmpFullname());
				
			}
			
		}else{
			
			insertAttestationRecord(reportingYear,reportingMethod,reportingType,comments,getReportingStatus(reportingStatus),providerId);
			
		}
		
		List<AttestationStatus> response = attestationRepo.findAll(Specifications.where(AttestationStatusSpecification.getReportingStatus(reportingYear)));
		
		return response;
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public ReportingInfo getAllActiveProviderStatus(Integer reportingYear) {
		
		ReportingInfo finalReportingDataDetails = new ReportingInfo();
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<AttestationBean> cq = builder.createQuery(AttestationBean.class);
		Root<EmployeeProfile> root = cq.from(EmployeeProfile.class);
		
		Join<EmployeeProfile, AttestationStatus> attestationTable = root.join(EmployeeProfile_.reportingProvider, JoinType.LEFT);
		
		Predicate[] restrictions = new Predicate[] {
				builder.equal(root.get(EmployeeProfile_.empProfileIsActive),true),
				root.get(EmployeeProfile_.empProfileGroupid).in(-1,-10),
		};
		
		Selection[] selections= new Selection[] { 
			
			attestationTable.get(AttestationStatus_.reportingYear),
			root.get(EmployeeProfile_.empProfileEmpid),
			root.get(EmployeeProfile_.empProfileFullname),
			attestationTable.get(AttestationStatus_.reportingType),
			attestationTable.get(AttestationStatus_.reportingMethod),
			attestationTable.get(AttestationStatus_.reportingStatus),
			attestationTable.get(AttestationStatus_.reportedDate),
			attestationTable.get(AttestationStatus_.reportingComments),
										
		};
		
		cq.where(restrictions);
		
		cq.multiselect(selections);
		
		List<AttestationBean> reportingInfo = em.createQuery(cq).getResultList();
		
		HashMap<String, HashMap<String, AttestationBean>> completeReportingInfo = new HashMap<String, HashMap<String,AttestationBean>>();
		
		for(int i=0;i<reportingInfo.size();i++){
			
			AttestationBean obj = reportingInfo.get(i);
			
			HashMap<String, AttestationBean> providerInfo = new HashMap<String, AttestationBean>();
			
			if(obj.getReportingYear() == null){
				
				providerInfo.put("MU", obj);
				
				providerInfo.put("PQRS", obj);
				
			}else{
				
				if(completeReportingInfo.containsKey(obj.getReportingProvider())){
					
					providerInfo = completeReportingInfo.get(obj.getReportingProvider());
					
				}
			
				if(obj.getReportingYear().equals(reportingYear)){
				
					if(obj.getReportingType().trim().equals("MU")){
						
						providerInfo.put("MU", obj);
						
					}
					
					if(obj.getReportingType().trim().equals("PQRS")){
					
						providerInfo.put("PQRS", obj);
						
					}
					
				}
				
			}
			
			completeReportingInfo.put(obj.getReportingProvider(), providerInfo);
			
		}
		
		finalReportingDataDetails.setReportingYear(reportingYear);
		
		finalReportingDataDetails.setReportingInfo(completeReportingInfo);
		
		return finalReportingDataDetails;
		
	}
	
	private String getReportingStatus(Integer reportingStatus){
		
		String status = "";
		
		if(reportingStatus == 1){
			status = "Yes";
		}else if(reportingStatus == 2){
			status = "No";
		}else if(reportingStatus == 3){
			status = "Not Required";
		}else if(reportingStatus == 4){
			status = "Not Participated";
		}
		
		return status;
		
	}
	
	private void insertAttestationRecord(Integer reportingYear, Integer reportingMethod, Integer reportingType, String comments, String reportingStatus,Integer providerId){
		
		EmployeeProfile employee = employeeRepo.findOne(providerId);
		
		AttestationStatus providerInfo = new AttestationStatus();
		
		Date date = new Date();
		Timestamp timeStamp = new Timestamp(date.getTime());
		
		providerInfo.setReportedDate(timeStamp);
		providerInfo.setReportingComments(comments);
		providerInfo.setReportingYear(reportingYear);
		providerInfo.setReportingProvider(employee.getEmpProfileFullname());
		providerInfo.setReportingStatus(reportingStatus);
		
		if(reportingType == 0 || reportingType == -1){
			
			providerInfo.setReportingType("MU");
			
			providerInfo.setReportingMethod("None");
			
			attestationRepo.saveAndFlush(providerInfo);
			
		}
		
		providerInfo = new AttestationStatus();
		
		providerInfo.setReportedDate(timeStamp);
		providerInfo.setReportingComments(comments);
		providerInfo.setReportingYear(reportingYear);
		providerInfo.setReportingProvider(employee.getEmpProfileFullname());
		providerInfo.setReportingStatus(reportingStatus);
		
		if(reportingType == 1 || reportingType == -1){
			
			providerInfo.setReportingType("PQRS");
			
			if(reportingMethod == 1){
				
				providerInfo.setReportingMethod("EHR");
				
			}else if(reportingMethod == 2){
				
				providerInfo.setReportingMethod("Registry");
				
			}else if(reportingMethod == 3){
				
				providerInfo.setReportingMethod("Claims");
				
			}
			
			attestationRepo.saveAndFlush(providerInfo);
			
		}
		
	}
	
	private void insertAttestationRecord(Integer reportingYear, Integer reportingMethod, Integer reportingType, String comments, String reportingStatus, String providerName){
		
		AttestationStatus providerInfo = new AttestationStatus();
		
		Date date = new Date();
		Timestamp timeStamp = new Timestamp(date.getTime());
		
		providerInfo.setReportedDate(timeStamp);
		providerInfo.setReportingComments(comments);
		providerInfo.setReportingYear(reportingYear);
		providerInfo.setReportingProvider(providerName);
		providerInfo.setReportingStatus(reportingStatus);
		
		if(reportingType == 0 || reportingType == -1){
			
			providerInfo.setReportingType("MU");
			
			providerInfo.setReportingMethod("None");
			
			attestationRepo.saveAndFlush(providerInfo);
			
		}
		
		providerInfo = new AttestationStatus();
		
		providerInfo.setReportedDate(timeStamp);
		providerInfo.setReportingComments(comments);
		providerInfo.setReportingYear(reportingYear);
		providerInfo.setReportingProvider(providerName);
		providerInfo.setReportingStatus(reportingStatus);
		
		if(reportingType == 1 || reportingType == -1){
			
			providerInfo.setReportingType("PQRS");
			
			if(reportingMethod == 1){
				
				providerInfo.setReportingMethod("EHR");
				
			}else if(reportingMethod == 2){
				
				providerInfo.setReportingMethod("Registry");
				
			}else if(reportingMethod == 3){
				
				providerInfo.setReportingMethod("Claims");
				
			}
			
			attestationRepo.saveAndFlush(providerInfo);
			
		}
		
	}

}
