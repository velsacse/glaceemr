package com.glenwood.glaceemr.server.application.services.Denial;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.models.AdActionhistory;
import com.glenwood.glaceemr.server.application.models.AdActionhistory_;
import com.glenwood.glaceemr.server.application.models.ArProblemList;
import com.glenwood.glaceemr.server.application.models.AssociateServiceDetails;
import com.glenwood.glaceemr.server.application.models.AssociateServiceDetails_;
import com.glenwood.glaceemr.server.application.models.Billinglookup;
import com.glenwood.glaceemr.server.application.models.Cpt;
import com.glenwood.glaceemr.server.application.models.Cpt_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.H172;
import com.glenwood.glaceemr.server.application.models.H172_;
import com.glenwood.glaceemr.server.application.models.InsCompAddr;
import com.glenwood.glaceemr.server.application.models.InsCompAddr_;
import com.glenwood.glaceemr.server.application.models.InsCompany;
import com.glenwood.glaceemr.server.application.models.InsCompany_;
import com.glenwood.glaceemr.server.application.models.NonServiceDetails;
import com.glenwood.glaceemr.server.application.models.NonServiceDetails_;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.PosTable_;
import com.glenwood.glaceemr.server.application.models.ServiceBalances;
import com.glenwood.glaceemr.server.application.models.ServiceBalances_;
import com.glenwood.glaceemr.server.application.models.ServiceDetail;
import com.glenwood.glaceemr.server.application.models.ServiceDetail_;
import com.glenwood.glaceemr.server.application.repositories.ServiceDetailRepository;

@Service
@Transactional
public class DenialServiceImpl implements DenialService {

	@Autowired
	ServiceDetailRepository  serviceDetailRepository;

	@Autowired
	EntityManager em;

	@Override
	public List<ServiceDetail> serviceDetailId(String serviceDetailId) {
		return null;
	}

	@Override
	public List<DenialBean> getAllDenials(String fromDate,String toDate){
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<ServiceDetail> root = cq.from(ServiceDetail.class);
		Join<ServiceDetail, PatientRegistration> patientReg=root.join(ServiceDetail_.patientRegistration,JoinType.INNER);
		Join<ServiceDetail, PatientInsDetail> primaryIns=root.join(ServiceDetail_.patientInsDetail,JoinType.LEFT);
		Join<ServiceDetail, PatientInsDetail> secondIns=root.join(ServiceDetail_.secInsDetail,JoinType.LEFT);
		Join<PatientInsDetail, InsCompAddr> insCompAddr1=primaryIns.join(PatientInsDetail_.insCompAddr,JoinType.LEFT);
		Join<PatientInsDetail, InsCompAddr> insCompAddr2=secondIns.join(PatientInsDetail_.insCompAddr,JoinType.LEFT);
		Join<InsCompAddr, InsCompany> insCompany1=insCompAddr1.join(InsCompAddr_.insCompany,JoinType.LEFT);
		Join<InsCompAddr, InsCompany> insCompany2=insCompAddr2.join(InsCompAddr_.insCompany,JoinType.LEFT);
		Join<ServiceDetail, EmployeeProfile> billingDoctor=root.join(ServiceDetail_.bdoctors,JoinType.LEFT);
		Join<ServiceDetail, EmployeeProfile> servicegDoctor=root.join(ServiceDetail_.sdoctors,JoinType.LEFT);
		Join<ServiceDetail, NonServiceDetails> serviceDetailJoin=root.join(ServiceDetail_.nonService,JoinType.INNER);
		Join<ServiceDetail, Cpt> cpt=root.join(ServiceDetail_.cpt, JoinType.INNER);
		Join<NonServiceDetails, Cpt> paymentCpt=serviceDetailJoin.join(NonServiceDetails_.cpt, JoinType.INNER);
		Join<NonServiceDetails, AdActionhistory> adAction=serviceDetailJoin.join(NonServiceDetails_.adActionHistory, JoinType.LEFT);
		Join<ServiceDetail, AssociateServiceDetails> associateDetails=root.join(ServiceDetail_.associateDetails,JoinType.LEFT);
		Join<AssociateServiceDetails, ArProblemList> arProblem=associateDetails.join(AssociateServiceDetails_.arProblem, JoinType.LEFT);
		Join<ServiceDetail, PosTable> posDetails=root.join(ServiceDetail_.posTable,JoinType.LEFT); 
		Join<ServiceDetail, Billinglookup> billingLookup=root.join(ServiceDetail_.bllok,JoinType.LEFT);
		Join<ServiceDetail, H172> h1=root.join(ServiceDetail_.h172,JoinType.LEFT);
		Join<ServiceDetail, ServiceBalances> serviceBalance=root.join(ServiceDetail_.serviceBalances,JoinType.LEFT);
		cq.distinct(true);
		cq.select(
				builder.construct(DenialBean.class,
						patientReg.get(PatientRegistration_.patientRegistrationId),
						root.get(ServiceDetail_.serviceDetailId),
						root.get(ServiceDetail_.serviceDetailSubmitStatus),
						patientReg.get(PatientRegistration_.patientRegistrationAccountno),
						patientReg.get(PatientRegistration_.patientRegistrationLastName),
						patientReg.get(PatientRegistration_.patientRegistrationFirstName),
						patientReg.get(PatientRegistration_.patientRegistrationMaidenName),
						patientReg.get(PatientRegistration_.patientRegistrationState),
						serviceDetailJoin.get(NonServiceDetails_.nonServiceDetailRemarkCode),
						serviceDetailJoin.get(NonServiceDetails_.nonServiceDetailComments),
						builder.function("to_char", String.class,serviceDetailJoin.get(NonServiceDetails_.nonServiceDetailDateOfPosting),builder.literal("mm/dd/yyyy")),//serviceDetailJoin.get(NonServiceDetails_.nonServiceDetailDateOfPosting),
						builder.function("count", Long.class, root.get(ServiceDetail_.serviceDetailId)),
						root.get(ServiceDetail_.serviceDetailSubmitStatus),
						builder.function("to_char", String.class,root.get(ServiceDetail_.serviceDetailDos),builder.literal("mm/dd/yyyy")),//root.get(ServiceDetail_.serviceDetailDos),
						root.get(ServiceDetail_.serviceDetailCharges),
						root.get(ServiceDetail_.serviceDetailCopay),
						insCompany1.get(InsCompany_.insCompanyName),
						primaryIns.get(PatientInsDetail_.patientInsDetailPatientinsuranceid),
						insCompany2.get(InsCompany_.insCompanyName),
						secondIns.get(PatientInsDetail_.patientInsDetailPatientinsuranceid),
						posDetails.get(PosTable_.posTablePlaceOfService),
						posDetails.get(PosTable_.posTableFacilityComments),
						builder.function("count", Long.class, h1.get(H172_.h172005)),
						builder.function("count", Long.class, h1.get(H172_.h172005)),
						builder.function("count", Long.class, paymentCpt.get(Cpt_.cptCptcode)),
						serviceDetailJoin.get(NonServiceDetails_.nonServiceDetailId),
						cpt.get(Cpt_.cptCptcode),
						root.get(ServiceDetail_.serviceDetailModifier1),
						root.get(ServiceDetail_.serviceDetailDx1),
						root.get(ServiceDetail_.serviceDetailDx2),
						root.get(ServiceDetail_.serviceDetailDx3),
						root.get(ServiceDetail_.serviceDetailDx4),
						billingDoctor.get(EmployeeProfile_.empProfileFullname),
						servicegDoctor.get(EmployeeProfile_.empProfileFullname),
						associateDetails.get(AssociateServiceDetails_.associateServiceDetailMedNotes),
						adAction.get(AdActionhistory_.adAhActiondescription),
						adAction.get(AdActionhistory_.adAhNotes),
						builder.function("to_char", String.class,adAction.get(AdActionhistory_.adAhActiontakendate),builder.literal("mm/dd/yyyy")),//adAction.get(AdActionhistory_.adAhActiontakendate),
						serviceBalance.get(ServiceBalances_.primaryInsurancePayment),
						h1.get(H172_.claimReferenceClaimid),
						serviceDetailJoin.get(NonServiceDetails_.nonServiceDetailReference),
						cpt.get(Cpt_.cptDescription),
						root.get(ServiceDetail_.serviceDetailUnit),
						root.get(ServiceDetail_.serviceDetailUnknown2),
						serviceBalance.get(ServiceBalances_.secondaryInsurancePayment),
						serviceBalance.get(ServiceBalances_.insuranceBalance)
						));
		cq.where(
				builder.equal(paymentCpt.get(Cpt_.cptCptcode),"DENIAL"),
				builder.isNull(adAction.get(AdActionhistory_.adAhActionid)),
				builder.between(serviceDetailJoin.get(NonServiceDetails_.nonServiceDetailDateOfPosting),convertToDate(fromDate),convertToDate(toDate))
				);
		cq.groupBy(patientReg.get(PatientRegistration_.patientRegistrationId),
				root.get(ServiceDetail_.serviceDetailId),
				root.get(ServiceDetail_.serviceDetailSubmitStatus),
				patientReg.get(PatientRegistration_.patientRegistrationAccountno),
				patientReg.get(PatientRegistration_.patientRegistrationLastName),
				patientReg.get(PatientRegistration_.patientRegistrationFirstName),
				patientReg.get(PatientRegistration_.patientRegistrationMaidenName),
				patientReg.get(PatientRegistration_.patientRegistrationState),
				serviceDetailJoin.get(NonServiceDetails_.nonServiceDetailRemarkCode),
				serviceDetailJoin.get(NonServiceDetails_.nonServiceDetailComments),
				serviceDetailJoin.get(NonServiceDetails_.nonServiceDetailDateOfPosting),
				root.get(ServiceDetail_.serviceDetailSubmitStatus),
				root.get(ServiceDetail_.serviceDetailDos),
				root.get(ServiceDetail_.serviceDetailCharges),
				root.get(ServiceDetail_.serviceDetailCopay),
				insCompany1.get(InsCompany_.insCompanyName),
				primaryIns.get(PatientInsDetail_.patientInsDetailPatientinsuranceid),
				insCompany2.get(InsCompany_.insCompanyName),
				secondIns.get(PatientInsDetail_.patientInsDetailPatientinsuranceid),
				posDetails.get(PosTable_.posTablePlaceOfService),
				posDetails.get(PosTable_.posTableFacilityComments),
				serviceDetailJoin.get(NonServiceDetails_.nonServiceDetailId),
				cpt.get(Cpt_.cptCptcode),
				root.get(ServiceDetail_.serviceDetailModifier1),
				root.get(ServiceDetail_.serviceDetailDx1),
				root.get(ServiceDetail_.serviceDetailDx2),
				root.get(ServiceDetail_.serviceDetailDx3),
				root.get(ServiceDetail_.serviceDetailDx4),
				billingDoctor.get(EmployeeProfile_.empProfileFullname),
				servicegDoctor.get(EmployeeProfile_.empProfileFullname),
				associateDetails.get(AssociateServiceDetails_.associateServiceDetailMedNotes),
				adAction.get(AdActionhistory_.adAhActiondescription),
				adAction.get(AdActionhistory_.adAhNotes),
				adAction.get(AdActionhistory_.adAhActiontakendate),
				h1.get(H172_.claimReferenceClaimid),
				cpt.get(Cpt_.cptDescription),
				root.get(ServiceDetail_.serviceDetailUnit),
				root.get(ServiceDetail_.serviceDetailUnknown2),
				serviceBalance.get(ServiceBalances_.insuranceBalance),
				serviceBalance.get(ServiceBalances_.primaryInsurancePayment),
				serviceBalance.get(ServiceBalances_.secondaryInsurancePayment));
		List<Object> results=em.createQuery(cq).getResultList();
		List<DenialBean> resultList = new ArrayList<DenialBean>();
		for(int i=0;i<results.size();i++)
		{
			DenialBean detailsBean=(DenialBean) results.get(i);
			resultList.add(detailsBean); 
		}
		return resultList;
	}

	public Date convertToDate(String date){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		java.sql.Date sqlStartDate =null;
		try{
			java.util.Date d = df.parse(date);
			sqlStartDate =  new java.sql.Date(d.getTime()); 
			System.out.println(sqlStartDate);
		}catch(Exception e){
			e.printStackTrace();
		}
		return sqlStartDate;
	}
}
