package com.glenwood.glaceemr.server.application.services.Denial;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.Bean.ClaimInfoBean;
import com.glenwood.glaceemr.server.application.Bean.CommonActionBean;
import com.glenwood.glaceemr.server.application.Bean.CommonResponseBean;
import com.glenwood.glaceemr.server.application.Bean.PatientInsuranceInfoBean;
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
import com.glenwood.glaceemr.server.application.models.ProblemReport;
import com.glenwood.glaceemr.server.application.models.ProblemReport_;
import com.glenwood.glaceemr.server.application.models.ServiceBalances;
import com.glenwood.glaceemr.server.application.models.ServiceBalances_;
import com.glenwood.glaceemr.server.application.models.ServiceDetail;
import com.glenwood.glaceemr.server.application.models.ServiceDetail_;
import com.glenwood.glaceemr.server.application.repositories.AdActionhistoryRepository;
import com.glenwood.glaceemr.server.application.repositories.NonServiceDetailsRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.repositories.ProblemReportRepository;
import com.glenwood.glaceemr.server.application.repositories.ServiceDetailRepository;
import com.glenwood.glaceemr.server.application.services.chart.insurance.InsuranceDataBean;
import com.glenwood.glaceemr.server.utils.HUtil;

import java.text.SimpleDateFormat;

@Service
@Transactional
public class DenialServiceImpl implements DenialService {

	@Autowired
	ServiceDetailRepository  serviceDetailRepository;

	@Autowired
	PatientRegistrationRepository patregRepo;
	
	@Autowired
	AdActionhistoryRepository adActionHistoryRepository;
	
	@Autowired
	NonServiceDetailsRepository nonServiceDetailRepository;
	
	@Autowired
	ProblemReportRepository problemReportRepository;

	@Autowired
	EntityManager em;
	
	String actionDesc;
	
	public static Integer ref = 0;
	
	@Override
	public List<ServiceDetail> serviceDetailId(String serviceDetailId) {
		return null;
	}


	@Override
	public List<DenialBean> getAllDenial(String fromDate,String toDate){
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
	
	@Override
	public CommonResponseBean billToPatAction(CommonActionBean billToPatBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailSubmitStatus", "T");
			criteriaUpdate.where(builder.equal(root.get(ServiceDetail_.serviceDetailId), billToPatBean.getServiceId()));
			this.em.createQuery(criteriaUpdate).executeUpdate();
			
			actionDesc = "Action Type:"+billToPatBean.getActionDescription()+" Billing Reason: "+billToPatBean.getBillingReason();
			adActionHistorySave(actionDesc, billToPatBean);
			return setResponse(1, billToPatBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, billToPatBean, e);
		}
	}

	@Override
	public CommonResponseBean changeCptAction(CommonActionBean changeCptBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonResponseBean changeCptChargesAction(
			CommonActionBean changeCptChargesBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailCharges", changeCptChargesBean.getNewCptCharges());
			criteriaUpdate.set("serviceDetailSubmitStatus", "P");
							
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), changeCptChargesBean.getServiceId()));		
			em.createQuery(criteriaUpdate).executeUpdate();
			
			actionDesc = "Action Type:"+changeCptChargesBean.getActionDescription()+"from "+changeCptChargesBean.getOldCptCharges()+" to "+changeCptChargesBean.getNewCptCharges();
			adActionHistorySave(actionDesc, changeCptChargesBean);
			return setResponse(1, changeCptChargesBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, changeCptChargesBean, e);
		}
	}
	
	@Override
	public CommonResponseBean changeDxAction(CommonActionBean changeDxBean) {
		// TODO Auto-generated method stub
		//Update Query
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailPatientid", changeDxBean.getPatientId());
			criteriaUpdate.set("serviceDetailDx1", changeDxBean.getDx1());
			criteriaUpdate.set("serviceDetailDx2", changeDxBean.getDx2() == null ? "" : changeDxBean.getDx2());
			criteriaUpdate.set("serviceDetailDx3", changeDxBean.getDx3() == null ? "" : changeDxBean.getDx3());
			criteriaUpdate.set("serviceDetailDx4", changeDxBean.getDx4() == null ? "" : changeDxBean.getDx4());
			criteriaUpdate.set("serviceDetailModifieddate", new Timestamp(new java.util.Date().getTime()));
			criteriaUpdate.set("serviceDetailModifiedby", changeDxBean.getModifiedBy());
							
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), changeDxBean.getServiceId()));		
			em.createQuery(criteriaUpdate).executeUpdate();
			
			actionDesc = "Action Type:"+changeDxBean.getActionDescription()+" Comments:"+changeDxBean.getNotes();
			adActionHistorySave(actionDesc, changeDxBean);
			return setResponse(1, changeDxBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, changeDxBean, e);
		}
	}
	
	@Override
	public CommonResponseBean changeMod1Action(CommonActionBean changeMod1Bean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailModifier1", changeMod1Bean.getNewMod1());
			criteriaUpdate.set("serviceDetailModifieddate", new Timestamp(new java.util.Date().getTime()));
			criteriaUpdate.set("serviceDetailModifiedby", changeMod1Bean.getModifiedBy());
			if(changeMod1Bean.getSubmitStatus().equals("R"))
				criteriaUpdate.set("serviceDetailSubmitStatus", "P");
			else if(changeMod1Bean.getSubmitStatus().equals("S"))
				criteriaUpdate.set("serviceDetailSubmitStatus", "Q");
			else if(changeMod1Bean.getSubmitStatus().equals("H"))
				criteriaUpdate.set("serviceDetailSubmitStatus", "G");
							
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), changeMod1Bean.getServiceId()));		
			em.createQuery(criteriaUpdate).executeUpdate();
			
			actionDesc = "Action Type:"+changeMod1Bean.getActionDescription()+" Comments:"+changeMod1Bean.getNotes();
			adActionHistorySave(actionDesc, changeMod1Bean);
			return setResponse(1, changeMod1Bean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, changeMod1Bean, e);
		}
	}
	
	@Override
	public CommonResponseBean changeMod2Action(CommonActionBean changeMod2Bean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailModifier2", changeMod2Bean.getNewMod2());
			criteriaUpdate.set("serviceDetailModifieddate", new Timestamp(new java.util.Date().getTime()));
			criteriaUpdate.set("serviceDetailModifiedby", changeMod2Bean.getModifiedBy());
			if(changeMod2Bean.getSubmitStatus().equals("R"))
				criteriaUpdate.set("serviceDetailSubmitStatus", "P");
			else if(changeMod2Bean.getSubmitStatus().equals("S"))
				criteriaUpdate.set("serviceDetailSubmitStatus", "Q");
			else if(changeMod2Bean.getSubmitStatus().equals("H"))
				criteriaUpdate.set("serviceDetailSubmitStatus", "G");
							
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), changeMod2Bean.getServiceId()));		
			em.createQuery(criteriaUpdate).executeUpdate();
			
			actionDesc = "Action Type:"+changeMod2Bean.getActionDescription()+" Comments:"+changeMod2Bean.getNotes();
			adActionHistorySave(actionDesc, changeMod2Bean);
			return setResponse(1, changeMod2Bean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, changeMod2Bean, e);
		}
	}
	
	@Override
	public CommonResponseBean changePrimaryInsuranceAction(CommonActionBean changePrimaryInsuranceBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		CriteriaQuery<InsuranceDataBean> criteriaQuery = builder.createQuery(InsuranceDataBean.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailPrimaryins", Long.parseLong(changePrimaryInsuranceBean.getPrimaryInsuranceId()));
			criteriaUpdate.set("serviceDetailSubmitStatus", "P");
			criteriaUpdate.set("serviceDetailModifiedby", changePrimaryInsuranceBean.getModifiedBy());
			criteriaUpdate.set("serviceDetailModifieddate", new Timestamp(new java.util.Date().getTime()));
			criteriaUpdate.set("serviceDetailIspaperclaim", -1);
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), changePrimaryInsuranceBean.getServiceId()));			
			this.em.createQuery(criteriaUpdate).executeUpdate();
			
			actionDesc = "Action Type:"+changePrimaryInsuranceBean.getActionDescription();
			adActionHistorySave(actionDesc, changePrimaryInsuranceBean);
			return setResponse(1, changePrimaryInsuranceBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, changePrimaryInsuranceBean, e);
		}
	}

	@Override
	public CommonResponseBean changeSecondaryInsuranceAction(CommonActionBean changeSecondaryInsuranceBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		CriteriaQuery<InsuranceDataBean> criteriaQuery = builder.createQuery(InsuranceDataBean.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		
		try 
		{
			criteriaUpdate.set("serviceDetailSecondaryins", Long.parseLong(changeSecondaryInsuranceBean.getSecondaryInsuranceId()));
			criteriaUpdate.set("serviceDetailSubmitStatus", "P");
			criteriaUpdate.set("serviceDetailModifiedby", changeSecondaryInsuranceBean.getModifiedBy());
			criteriaUpdate.set("serviceDetailModifieddate", new Timestamp(new java.util.Date().getTime()));
			criteriaUpdate.set("serviceDetailIspaperclaim", -1);
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), changeSecondaryInsuranceBean.getServiceId()));			
			this.em.createQuery(criteriaUpdate).executeUpdate();
			
			actionDesc = "Action Type:"+changeSecondaryInsuranceBean.getActionDescription();
			adActionHistorySave(actionDesc, changeSecondaryInsuranceBean);
			return setResponse(1, changeSecondaryInsuranceBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, changeSecondaryInsuranceBean, e);
		}
	}

	@Override
	public CommonResponseBean claimToPrimaryAction(CommonActionBean claimToPrimaryBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailSubmitStatus", "P");
			criteriaUpdate.set("serviceDetailModifiedby", claimToPrimaryBean.getModifiedBy());
			criteriaUpdate.set("serviceDetailModifieddate", new Timestamp(new java.util.Date().getTime()));
			//criteriaUpdate.set("serviceDetailIspaperclaim", claimToPrimaryBean.getIsPaperClaim());
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), claimToPrimaryBean.getServiceId()));			
			this.em.createQuery(criteriaUpdate).executeUpdate();
			
			actionDesc = "Action Type:"+claimToPrimaryBean.getActionDescription();
			adActionHistorySave(actionDesc, claimToPrimaryBean);
			return setResponse(1, claimToPrimaryBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, claimToPrimaryBean, e);
		}
	}

	@Override
	public CommonResponseBean claimToSecondaryAction(CommonActionBean claimToSecondaryBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			actionDesc = "Action Type:"+claimToSecondaryBean.getActionDescription();
			adActionHistorySave(actionDesc, claimToSecondaryBean);
			return setResponse(1, claimToSecondaryBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, claimToSecondaryBean, e);
		}
	}
	
	@Override
	public CommonResponseBean followUpAction(CommonActionBean followUpBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailModifiedby", followUpBean.getModifiedBy());
			criteriaUpdate.set("serviceDetailModifieddate",new java.util.Date());
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), followUpBean.getServiceId()));			
			this.em.createQuery(criteriaUpdate).executeUpdate();
			
			actionDesc = "Action Type:"+followUpBean.getActionDescription();
			adActionHistorySave(actionDesc, followUpBean);
			return setResponse(1, followUpBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, followUpBean, e);
		}
	}
	
	@Override
	public CommonResponseBean markAsApppealAction(CommonActionBean markAsApppealBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailModifiedby", markAsApppealBean.getModifiedBy());
			criteriaUpdate.set("serviceDetailModifieddate",new java.util.Date());
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), markAsApppealBean.getServiceId()));			
			this.em.createQuery(criteriaUpdate).executeUpdate();
			
			actionDesc = "Action Type:"+markAsApppealBean.getActionDescription();
			adActionHistorySave(actionDesc, markAsApppealBean);
			return setResponse(1, markAsApppealBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, markAsApppealBean, e);
		}
	}

	@Override
	public CommonResponseBean markAsCapitationAction(CommonActionBean markAsCapitationBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailSubmitStatus","C");
			criteriaUpdate.set("serviceDetailModifiedby", markAsCapitationBean.getModifiedBy());
			criteriaUpdate.set("serviceDetailModifieddate",new java.util.Date());
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), markAsCapitationBean.getServiceId()));			
			this.em.createQuery(criteriaUpdate).executeUpdate();
			
			actionDesc = "Action Type:"+markAsCapitationBean.getActionDescription();
			adActionHistorySave(actionDesc, markAsCapitationBean);
			return setResponse(1, markAsCapitationBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, markAsCapitationBean, e);
		}
	}

	@Override
	public CommonResponseBean markAsDuplicateAction(CommonActionBean markAsDuplicateBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailArStatus", 8);
			criteriaUpdate.set("serviceDetailModifiedby", markAsDuplicateBean.getModifiedBy());
			criteriaUpdate.set("serviceDetailModifieddate", new java.util.Date());
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), markAsDuplicateBean.getServiceId()));			
			this.em.createQuery(criteriaUpdate).executeUpdate();
			
			actionDesc = "Action Type:"+markAsDuplicateBean.getActionDescription();
			adActionHistorySave(actionDesc, markAsDuplicateBean);
			return setResponse(1, markAsDuplicateBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, markAsDuplicateBean, e);
		}
	}

	@Override
	public CommonResponseBean markAsFullySettledAction(CommonActionBean markAsFullySettledBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		NonServiceDetails nonServiceDetail = new NonServiceDetails();
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailSubmitStatus", "X");
			criteriaUpdate.set("serviceDetailModifiedby", markAsFullySettledBean.getModifiedBy());
			criteriaUpdate.set("serviceDetailModifieddate", new java.util.Date());
			criteriaUpdate.set("serviceDetailIspaperclaim", -1);
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), markAsFullySettledBean.getServiceId()));			
			this.em.createQuery(criteriaUpdate).executeUpdate();
			
			nonServiceDetail.setNonServiceDetailId(Long.parseLong("-1"));
			nonServiceDetail.setNonServiceDetailPatientId(Long.parseLong(markAsFullySettledBean.getPatientId().toString()));
			nonServiceDetail.setNonServiceDetailServiceId(Long.parseLong(markAsFullySettledBean.getServiceId().toString()));
			nonServiceDetail.setNonServiceDetailDateOfPosting(convertToDate(markAsFullySettledBean.getDop()));
			//nonServiceDetail.setNonServiceDetailPaymentCptId(Integer.parseInt(markAsFullySettledBean.getPaymentCptId()));
			nonServiceDetail.setNonServiceDetailComments(markAsFullySettledBean.getNotes());
			nonServiceDetail.setNonServiceDetailPaymentMethod(1);
			nonServiceDetail.setNonServiceDetailDeductible(0.00);
			nonServiceDetail.setNonServiceDetailCoins(0.00);
			nonServiceDetail.setNonServiceDetailDatePaid(convertToDate(markAsFullySettledBean.getDop()));
			nonServiceDetail.setNonServiceDetailRiskWithheld(0.00);
			//nonServiceDetail.setNonServiceDetailPlacedBy(markAsFullySettledBean.getPlacedBy());	
			nonServiceDetail.setNonServiceDetailLastModifiedBy(markAsFullySettledBean.getModifiedBy());
			nonServiceDetail.setNonServiceDetailLastModifiedDate(new Timestamp(new java.util.Date().getTime()));
			nonServiceDetail.setNonServiceDetailPayerId(Long.parseLong(markAsFullySettledBean.getPatientId().toString()));
			nonServiceDetail.setNonServiceDetailCheckId((long)-1);
			nonServiceDetail.setH555555(1);
			nonServiceDetail.setNonServiceDetailIsValid(false);
			nonServiceDetail.setNonServiceDetailRefundStatus((short)-1);
			nonServiceDetail.setNonServiceDetailReverseId((long)-1);
			nonServiceDetail.setNonServiceDetailEntryType(Short.parseShort(String.valueOf(21)));
			//nonServiceDetail.setNonServiceDetailReceiptDate(convertToDate(markAsFullySettledBean.getReceiptDate()));
			nonServiceDetail.setNonServiceDetailPaidBy(4);
			nonServiceDetailRepository.saveAndFlush(nonServiceDetail);
			
			return setResponse(1, markAsFullySettledBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, markAsFullySettledBean, e);
			
		}
	}

	@Override
	public CommonResponseBean markAsOnHoldAction(CommonActionBean markAsOnHoldBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailSubmitStatus", "A");
			criteriaUpdate.set("serviceDetailModifiedby", markAsOnHoldBean.getModifiedBy());
			criteriaUpdate.set("serviceDetailModifieddate",new java.util.Date())	;
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), markAsOnHoldBean.getServiceId()));			
			this.em.createQuery(criteriaUpdate).executeUpdate();
			
			actionDesc = "Action Type:"+markAsOnHoldBean.getActionDescription();
			adActionHistorySave(actionDesc, markAsOnHoldBean);
			return setResponse(1, markAsOnHoldBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, markAsOnHoldBean, e);
		}
	}


	@Override
	public CommonResponseBean markAsUncollectableAction(CommonActionBean markAsUncollectableBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailArStatus", 7);
			criteriaUpdate.set("serviceDetailModifiedby", markAsUncollectableBean.getModifiedBy());
			criteriaUpdate.set("serviceDetailModifieddate",new java.util.Date());
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), markAsUncollectableBean.getServiceId()));	
			criteriaUpdate.set("serviceDetailSubmitStatus", "Y");
			this.em.createQuery(criteriaUpdate).executeUpdate();
			
			actionDesc = "Action Type:"+markAsUncollectableBean.getActionDescription();
			adActionHistorySave(actionDesc, markAsUncollectableBean);
			return setResponse(1, markAsUncollectableBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, markAsUncollectableBean, e);
		}
	}
	
	@Override
	public CommonResponseBean reportAProblem(CommonActionBean reportAProblemBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailModifiedby", reportAProblemBean.getModifiedBy());
			criteriaUpdate.set("serviceDetailModifieddate", new java.util.Date());
			criteriaUpdate.where(builder.equal(root.get("serviceDetailId"), reportAProblemBean.getServiceId()));			
			this.em.createQuery(criteriaUpdate).executeUpdate();
			
			problemReportSave(reportAProblemBean);
			actionDesc = "Action Type:"+reportAProblemBean.getActionDescription();
			reportAProblemBean.setReference(reportAProblemBean.getModifiedBy().toUpperCase()+"-"+1+"-"+ref+"L");
			reportAProblemBean.setProblemId(ref);
			adActionHistorySave(actionDesc, reportAProblemBean);
			ref=0;
			return setResponse(1, reportAProblemBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, reportAProblemBean, e);
		}
	}

	@Override
	public CommonResponseBean toBeCalledAction(CommonActionBean toBeCalledBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set(root.get(ServiceDetail_.serviceDetailArStatus), String.valueOf(11));
            criteriaUpdate.set(root.get(ServiceDetail_.serviceDetailModifiedby), toBeCalledBean.getModifiedBy());
            criteriaUpdate.set(root.get(ServiceDetail_.serviceDetailModifieddate),new Timestamp(new java.util.Date().getTime()));
            criteriaUpdate.where(builder.equal(root.get(ServiceDetail_.serviceDetailId), toBeCalledBean.getServiceId()));
            em.createQuery(criteriaUpdate).executeUpdate();
			
            actionDesc = "Action Type:"+toBeCalledBean.getActionDescription()+" Problem:"+toBeCalledBean.getProblemNotes()+" Next Followup Date:"+toBeCalledBean.getNextFollowupDate();
            adActionHistorySave(actionDesc, toBeCalledBean);
			return setResponse(1, toBeCalledBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, toBeCalledBean, e);
		}
	}

	@Override
	public CommonResponseBean toBeCalledCompletedAction(CommonActionBean toBeCalledCompletedBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set(root.get(ServiceDetail_.serviceDetailArStatus), String.valueOf(13));
            criteriaUpdate.set(root.get(ServiceDetail_.serviceDetailModifiedby), toBeCalledCompletedBean.getModifiedBy());
            criteriaUpdate.set(root.get(ServiceDetail_.serviceDetailModifieddate),new Timestamp(new java.util.Date().getTime()));
            criteriaUpdate.where(builder.equal(root.get(ServiceDetail_.serviceDetailId), toBeCalledCompletedBean.getServiceId()));
            em.createQuery(criteriaUpdate).executeUpdate();
			
            actionDesc = "Action Type:"+toBeCalledCompletedBean.getActionDescription()+" Solution:"+toBeCalledCompletedBean.getProblemNotes();
            adActionHistorySave(actionDesc, toBeCalledCompletedBean);
			return setResponse(1, toBeCalledCompletedBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, toBeCalledCompletedBean, e);
		}
	}

	@Override
	public CommonResponseBean writeoffAction(CommonActionBean writeoffBean) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<ServiceDetail> criteriaUpdate = builder.createCriteriaUpdate(ServiceDetail.class);
		NonServiceDetails nonServiceDetail = new NonServiceDetails();
		Root<ServiceDetail> root = criteriaUpdate.from(ServiceDetail.class);
		try 
		{
			criteriaUpdate.set("serviceDetailSubmitStatus", "X");
			criteriaUpdate.where(builder.equal(root.get(ServiceDetail_.serviceDetailId), writeoffBean.getServiceId()));
			this.em.createQuery(criteriaUpdate).executeUpdate();
			
			nonServiceDetail.setNonServiceDetailId(Long.parseLong("-1"));
			nonServiceDetail.setNonServiceDetailPatientId(Long.parseLong(writeoffBean.getPatientId().toString()));
			nonServiceDetail.setNonServiceDetailServiceId(Long.parseLong(writeoffBean.getServiceId().toString()));
			nonServiceDetail.setNonServiceDetailDateOfPosting(convertToDate(writeoffBean.getDop()));
			//nonServiceDetail.setNonServiceDetailPaymentCptId(Integer.parseInt(writeoffBean.getPaymentCptId()));
			nonServiceDetail.setNonServiceDetailComments(writeoffBean.getNotes());
			nonServiceDetail.setNonServiceDetailPaymentMethod(1);
			nonServiceDetail.setNonServiceDetailDeductible(0.00);
			nonServiceDetail.setNonServiceDetailCoins(0.00);
			nonServiceDetail.setNonServiceDetailDatePaid(convertToDate(writeoffBean.getDop()));
			nonServiceDetail.setNonServiceDetailRiskWithheld(0.00);
			//nonServiceDetail.setNonServiceDetailPlacedBy(writeoffBean.getPlacedBy());	
			nonServiceDetail.setNonServiceDetailLastModifiedBy(writeoffBean.getModifiedBy());
			nonServiceDetail.setNonServiceDetailLastModifiedDate(new Timestamp(new java.util.Date().getTime()));
			nonServiceDetail.setNonServiceDetailPayerId(Long.parseLong(writeoffBean.getPatientId().toString()));
			nonServiceDetail.setNonServiceDetailCheckId((long)-1);
			nonServiceDetail.setH555555(1);
			nonServiceDetail.setNonServiceDetailIsValid(false);
			nonServiceDetail.setNonServiceDetailRefundStatus((short)-1);
			nonServiceDetail.setNonServiceDetailReverseId((long)-1);
			nonServiceDetail.setNonServiceDetailEntryType(Short.parseShort(String.valueOf(21)));
			//nonServiceDetail.setNonServiceDetailReceiptDate(convertToDate(writeoffBean.getReceiptDate()));
			nonServiceDetail.setNonServiceDetailPaidBy(4);
			nonServiceDetailRepository.saveAndFlush(nonServiceDetail);
			
			return setResponse(1, writeoffBean, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return setResponse(2, writeoffBean, e);
		}
	}
	
	public void adActionHistorySave(String actionDesc, CommonActionBean commonActionBean)
	{
		AdActionhistory actionHistory = new AdActionhistory();
		
		actionHistory.setAdAhId(-1);
		actionHistory.setAdAhServiceid((commonActionBean.getServiceId()));
		actionHistory.setAdAhServicestatuscode(commonActionBean.getServiceStatusCode());
		actionHistory.setAdAhActionid(commonActionBean.getActionId());
		actionHistory.setAdAhActiontakendate(new Timestamp(new java.util.Date().getTime()));
		actionHistory.setAdAhActiontakenby(commonActionBean.getModifiedBy());
		//actionHistory.setAdAhNextfollowupactiondate(convertToTimeStamp(claimToPrimaryBean.getNextFollowupActionDate()));
		actionHistory.setAdAhProblemid(commonActionBean.getProblemId());
		actionHistory.setAdAhIsrecent(commonActionBean.getIsRecent());
		actionHistory.setAdAhActionreason(commonActionBean.getActionReason());
		actionHistory.setAdAhActiondescription(actionDesc);
		actionHistory.setAdAhModuleid(2);
		actionHistory.setAdAhDenialid(commonActionBean.getNonServiceId());
		actionHistory.setAdAhActionreference(commonActionBean.getReference());
		adActionHistoryRepository.saveAndFlush(actionHistory);
	}
	
	public void problemReportSave(CommonActionBean commonActionBean) throws ParseException
	{
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date dateofPosting = (java.util.Date)formatter.parse(commonActionBean.getModifiedDate());
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Timestamp currentTime = new Timestamp(dateofPosting.getTime());
		ProblemReport problemReport = new ProblemReport();
		ref=Integer.parseInt((HUtil.Nz(getMaxProblemId(commonActionBean.getModifiedBy()),"0")))+1;
		problemReport.setProblemReportUniqueid(-1);
		problemReport.setProblemReportPracticeId(1);
		problemReport.setProblemReportComplexity(1);
		problemReport.setProblemReportTicketNo("");
		problemReport.setProblemReportProblemId(ref);
		problemReport.setProblemReportPatientName(commonActionBean.getPatientName());
		problemReport.setProblemReportAccountNo(commonActionBean.getPatientAccountId());
		problemReport.setProblemReportProblem(commonActionBean.getProblem());
		
		Integer status=7;
		if(commonActionBean.getProblemStatus().equalsIgnoreCase("Open")){
			status=1;
		}
		problemReport.setProblemReportProblemstatus(status);
		problemReport.setProblemReportPriority(1);
		problemReport.setProblemReportReportedBy(commonActionBean.getModifiedBy());
		problemReport.setProblemReportReportedOn(currentTime);
		problemReport.setProblemReportReportedTo(commonActionBean.getTo());
		problemReport.setProblemReportLoginUser(commonActionBean.getModifiedBy());
		problemReport.setProblemReportLastModified(currentTime);
		problemReport.setProblemReportPatientcall(2);
		problemReport.setProblemReportSubject(commonActionBean.getSubject());
		problemReport.setProblemReportPatientid(commonActionBean.getPatientId());
		problemReport.setProblemReportFilepaths("");
		problemReport.setProblemReportFilenames("");
		problemReport.setProblemReportPatientInsId(commonActionBean.getPrimaryInsuranceId());
		problemReport.setProblemReportProblemDenialrulevalidatorId(-1);
		problemReport.setProblemReportProblemType(Integer.parseInt(commonActionBean.getProblemType()));
		problemReport.setProblemReportProblemTypedesc(commonActionBean.getActionDescription()); 
		problemReportRepository.saveAndFlush(problemReport);
	}
	
	public CommonResponseBean setResponse(int mode, CommonActionBean commonActionBean, Exception exception)
	{
		CommonResponseBean finalResponseBean = new CommonResponseBean();
		if(mode == 1)
		{
			finalResponseBean.setResponseStatus("Success");
			finalResponseBean.setPatientAccountId(commonActionBean.getPatientAccountId());
			finalResponseBean.setAccountServerIp(commonActionBean.getAccountServerIp());
			finalResponseBean.setServiceId(commonActionBean.getServiceId());
			finalResponseBean.setRowId(commonActionBean.getRowId());
			finalResponseBean.setInnerRowId(commonActionBean.getInnerRowId());
			finalResponseBean.setTaskId(commonActionBean.getTaskId());
			finalResponseBean.setDenialId(commonActionBean.getDenialId());
			finalResponseBean.setFailedReason("--");
		}
		else
		{
			finalResponseBean.setResponseStatus("Failed");
			finalResponseBean.setPatientAccountId(commonActionBean.getPatientAccountId());
			finalResponseBean.setAccountServerIp(commonActionBean.getAccountServerIp());
			finalResponseBean.setServiceId(commonActionBean.getServiceId());
			finalResponseBean.setRowId(commonActionBean.getRowId());
			finalResponseBean.setInnerRowId(commonActionBean.getInnerRowId());
			finalResponseBean.setTaskId(commonActionBean.getTaskId());
			finalResponseBean.setDenialId(commonActionBean.getDenialId());
			finalResponseBean.setFailedReason(exception.getMessage());
		}
		return finalResponseBean;
	}
	
	@Override
    public List<PatientInsuranceInfoBean> getPatientInsInfo(Integer patientId,Integer type) {
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PatientInsuranceInfoBean> criteriaQuery = builder.createQuery(PatientInsuranceInfoBean.class);
        Root<PatientInsDetail> root = criteriaQuery.from(PatientInsDetail.class);
        Join<PatientInsDetail, InsCompAddr> insCompAddr=root.join(PatientInsDetail_.insCompAddr,JoinType.INNER);
        Join<InsCompAddr, InsCompany> insComp=insCompAddr.join(InsCompAddr_.insCompany,JoinType.INNER);

        TypedQuery<PatientInsuranceInfoBean> typedQuery = em.createQuery(criteriaQuery
        		.select(builder.construct(PatientInsuranceInfoBean.class,
                          root.get(PatientInsDetail_.patientInsDetailId),
                          root.get(PatientInsDetail_.patientInsDetailIsactive),
                          insComp.get(InsCompany_.insCompanyName),
                          root.get(PatientInsDetail_.patientInsDetailInsno)))
                 .where(builder.equal(root.get(PatientInsDetail_.patientInsDetailPatientid), patientId),
                        builder.equal(root.get(PatientInsDetail_.patientInsDetailInstype), type))
                 .distinct(true));
        
        List<PatientInsuranceInfoBean> resultList = typedQuery.getResultList();
        
        return resultList;
    }


	@Override
	public List<ClaimInfoBean> getServicesByClaim(Integer patientId, String claimNo) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ClaimInfoBean> criteriaQuery = builder.createQuery(ClaimInfoBean.class);
        Root<ServiceDetail> root = criteriaQuery.from(ServiceDetail.class);
        Join<ServiceDetail, H172> serviceClaimJoin = root.join(ServiceDetail_.h172,JoinType.INNER);
        
        TypedQuery<ClaimInfoBean> typedQuery = em.createQuery(criteriaQuery
        		.select(builder.construct(ClaimInfoBean.class,
                          root.get(ServiceDetail_.serviceDetailId),
                          root.get(ServiceDetail_.serviceDetailDos),
                          root.get(ServiceDetail_.serviceDetailCptid),
                          root.get(ServiceDetail_.serviceDetailUnit),
                          root.get(ServiceDetail_.serviceDetailDx1),
                          root.get(ServiceDetail_.serviceDetailDx2),
                          root.get(ServiceDetail_.serviceDetailDx3),
                          root.get(ServiceDetail_.serviceDetailDx4),
                          root.get(ServiceDetail_.serviceDetailCharges),
                          root.get(ServiceDetail_.serviceDetailCopay)))
                 .where(builder.equal(root.get(ServiceDetail_.serviceDetailPatientid), patientId),
                        builder.like(serviceClaimJoin.get(H172_.claimReferenceClaimid), claimNo),
                        builder.isNotNull(serviceClaimJoin.get(H172_.claimReferenceClaimid)))
                 .distinct(true));
        
        List<ClaimInfoBean> resultList = typedQuery.getResultList();
        
        return resultList;
	}
	
	public Integer getMaxProblemId(String user) {
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = builder.createQuery(Object.class);
        Root<ProblemReport> root = criteriaQuery.from(ProblemReport.class);
        ;
        

        TypedQuery<Object> typedQuery = em.createQuery(criteriaQuery
        		.select(builder.max(root.get(ProblemReport_.problemReportProblemId)))
                 .where(builder.like(root.get(ProblemReport_.problemReportTicketNo), user+"%")));
        
        List<Object> resultList = typedQuery.getResultList();
        
        return (Integer)resultList.get(0);
    }


	@Override
	public Integer getDenialReasonId(CommonActionBean commonAction) {
		// TODO Auto-generated method stub.
	String qry="select blook_intid from billinglookup where blook_group=123 and blook_desc ilike '"+HUtil.Nz(commonAction.getDenialReason(),"-1")+"' limit 1";
		
      //Query nativeQuery= (Query) em.createNativeQuery(qry);
      List<Object> result=em.createNativeQuery(qry).getResultList();
      Integer id=(Integer)result.get(0);
      commonAction.setDenialReason(""+id);
	return id;
	}


	@Override
	public Integer getBillingReasonId(CommonActionBean commonAction) {
		// TODO Auto-generated method stub
		
		String qry="select h062001 as billingreasonId from h062 where h062003 is true  and h062002 ilike '"+HUtil.Nz(commonAction.getBillingReason(),"-1")+"' limit 1";
		List<Object> result=em.createNativeQuery(qry).getResultList();
	      Integer id=(Integer)result.get(0);
	      commonAction.setBillingReason(""+id);
		return id;
	}


	@Override
	public Integer getDenialTypeId(CommonActionBean commonAction) {
		// TODO Auto-generated method stub
		// no need of another things
		String qry="select blook_intid from billinglookup where blook_group=121 and blook_desc ilike '"+HUtil.Nz(commonAction.getDenialType(),"-1")+"' limit 1";
		List<Object> result=em.createNativeQuery(qry).getResultList();
	      Integer id=(Integer)result.get(0);
	      commonAction.setDenialType(""+id);
		return id;
	}


	@Override
	public Integer getDenialCategoryId(CommonActionBean commonAction) {
		// TODO Auto-generated method stub
		
		String qry="select blook_intid from billinglookup where blook_group=124 and blook_desc ilike '"+HUtil.Nz(commonAction.getDenialCategory(),"-1")+"' limit 1";
		List<Object> result=em.createNativeQuery(qry).getResultList();
	      Integer id=(Integer)result.get(0);
	      commonAction.setDenialCategory(""+id);
		return id;
	}


	@Override
	public Integer getProblemTypeId(CommonActionBean commonAction) {
		// TODO Auto-generated method stub
		
		String qry="select problem_type_id from problem_type where problem_type_name ilike '"+HUtil.Nz(commonAction.getProblemType(),"-1")+"' limit 1";
		List<Object> result=em.createNativeQuery(qry).getResultList();
	      Integer id=(Integer)result.get(0);
	      commonAction.setProblemType(""+id);
		return id;
	}

}