package com.glenwood.glaceemr.server.application.services.chart.charges;
/**
 * Charges service methods implementation 
 * @author Tarun
 *
 */
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.models.AssociateServiceDetails;
import com.glenwood.glaceemr.server.application.models.Cpt;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.H066;
import com.glenwood.glaceemr.server.application.models.H076;
import com.glenwood.glaceemr.server.application.models.H213;
import com.glenwood.glaceemr.server.application.models.H611;
import com.glenwood.glaceemr.server.application.models.InitialSettings;
import com.glenwood.glaceemr.server.application.models.NonServiceDetails;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.Quickcpt;
import com.glenwood.glaceemr.server.application.models.Quickcpt_;
import com.glenwood.glaceemr.server.application.models.SaveServicesBean;
import com.glenwood.glaceemr.server.application.models.ServiceDetail;
import com.glenwood.glaceemr.server.application.models.SubmitStatus;
import com.glenwood.glaceemr.server.application.models.UpdateServiceBean;
import com.glenwood.glaceemr.server.application.repositories.AssociateServiceDetailsRepository;
import com.glenwood.glaceemr.server.application.repositories.CptRepository;
import com.glenwood.glaceemr.server.application.repositories.EmployeeProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.EncounterRepository;
import com.glenwood.glaceemr.server.application.repositories.H066Repository;
import com.glenwood.glaceemr.server.application.repositories.H076Repository;
import com.glenwood.glaceemr.server.application.repositories.H213Repository;
import com.glenwood.glaceemr.server.application.repositories.H611Repository;
import com.glenwood.glaceemr.server.application.repositories.InitialSettingsRepository;
import com.glenwood.glaceemr.server.application.repositories.NonServiceDetailsRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientInsDetailRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.repositories.PosTableRepository;
import com.glenwood.glaceemr.server.application.repositories.QuickCptRepository;
import com.glenwood.glaceemr.server.application.repositories.ServiceDetailRepository;
import com.glenwood.glaceemr.server.application.repositories.SubmitStatusRepository;
import com.glenwood.glaceemr.server.application.specifications.ChargesSpecification;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

@Service
public class ChargesServicesImpl implements ChargesServices{
	@Autowired
	EntityManagerFactory emf ;
	
	@Autowired
	PosTableRepository posTableRepository;
	
	@Autowired 
	EmployeeProfileRepository empProfileRepository;
	
	@Autowired
	PatientInsDetailRepository patientInsDetailRepository; 
	
	@Autowired
	QuickCptRepository quickCptRepository;
	
	@Autowired
	H076Repository h076Repository;
	
	@Autowired
	ServiceDetailRepository serviceDetailRepository;
	
	@Autowired
	NonServiceDetailsRepository nonServiceDetailsRepository;
	
	@Autowired
	CptRepository cptRepository;
	
	@Autowired
	PatientRegistrationRepository patientRegistrationRepository;
	
	@Autowired
	H213Repository h213Repository;
	
	@Autowired
	AssociateServiceDetailsRepository associateServiceDetailsRepository;
	
	@Autowired
	InitialSettingsRepository initialSettingsRepository;
	
	@Autowired
	H066Repository h066Repository;
	
	@Autowired
	SubmitStatusRepository submitStatusRepository;
	
	@Autowired
	EncounterRepository encounterRepository;
	
	@Autowired
	H611Repository h611Repository;
	
	@Autowired
	SessionMap sessionMap;
	
	@Override
	public ChargesPageBasicDetailsBean findAllBasicDetails(Integer patientId) {
		patientId=Optional.fromNullable(Integer.parseInt(Strings.emptyToNull(patientId.toString()))).or(-1);
		List<PosTable>  posValue=posTableRepository.findAll(Specifications.where(ChargesSpecification.checkPosIsActive()));
		List<EmployeeProfile> empValue=  empProfileRepository.findAll(Specifications.where(ChargesSpecification.getAllEmployeeDetails()));
		List<PatientInsDetail> insDetails=patientInsDetailRepository.findAll(ChargesSpecification.getInsDetails(patientId));
		List<H076> h076ReferringDr=h076Repository.findAll(ChargesSpecification.getAllReferringDrList());
		ChargesPageBasicDetailsBean chargesBean=new ChargesPageBasicDetailsBean();
		chargesBean.setPosDetails(posValue);
		chargesBean.setEmployeeDetails(empValue);
		chargesBean.setInsDetails(insDetails);
		chargesBean.setH076ReferringDr(h076ReferringDr);
		return chargesBean;
	}

	@Override
	public List<Quickcpt> findAllQuickCptCodes() {
		EntityManager em = emf.createEntityManager();
		List<Quickcpt> cptCodesList=new ArrayList<Quickcpt>();
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Quickcpt> cq = builder.createQuery(Quickcpt.class);
			Root<Quickcpt> root = cq.from(Quickcpt.class);
			Fetch<Quickcpt, Cpt> resultJoin = root.fetch(Quickcpt_.cpt,JoinType.INNER);
			cq.distinct(true);
			cq.orderBy(builder.asc(root.get(Quickcpt_.groupNo)));
			cq.getRestriction();
			cptCodesList= em.createQuery(cq).getResultList();
			return cptCodesList;
		} catch(Exception e) {
			e.printStackTrace();
			return cptCodesList;
		} finally {
			em.close();
		}
//		List<Quickcpt> cptCodesList=quickCptRepository.findAll(ChargesSpecification.findAllQuickCptCodes());
//		return cptCodesList;
	}

	@Override
	public List<ServiceDetail> getAllServices(Integer patientId) {
		patientId=Optional.fromNullable(Integer.parseInt(Strings.emptyToNull(patientId.toString()))).or(-1);
		List<ServiceDetail> servicesList=serviceDetailRepository.findAll(ChargesSpecification.findAllServices(patientId));
		return servicesList;
	}

	@Override
	public ServiceDetail getServiceDetails(Integer patientId, Integer serviceId) {
		serviceId=Optional.fromNullable(Integer.parseInt(Strings.emptyToNull(serviceId.toString()))).or(-1);
		patientId=Optional.fromNullable(Integer.parseInt(Strings.emptyToNull(patientId.toString()))).or(-1);
		ServiceDetail totalServiceDetails=new ServiceDetail();
		try {
			totalServiceDetails=serviceDetailRepository.findOne(ChargesSpecification.findAllDetailsofAService(patientId,serviceId));
			return totalServiceDetails;
		} catch (Exception e) {
			e.printStackTrace();
			return totalServiceDetails;
		}
		
	}
	
	@Override
	public Boolean deleteMentoinedServices(String serviceIDs,Integer patientId,String modifiedDate,String loginId,String loginName) {
		serviceIDs=Optional.fromNullable(Strings.emptyToNull(serviceIDs.toString())).or("-1");
		patientId=Optional.fromNullable(Integer.parseInt(Strings.emptyToNull(patientId.toString()))).or(-1);
		Boolean confirmation=true;
		try {
			List<Integer> idValues=new ArrayList<Integer>();
			List<String> cptVales=new ArrayList<String>();
			cptVales.add("00COM");
			String array[]=serviceIDs.split(",");
			for(int i=0;i<array.length;i++)
			{
				idValues.add(i, Integer.parseInt(array[i]));
			}
			List<NonServiceDetails> paymentChecking=nonServiceDetailsRepository.findAll(ChargesSpecification.getPaymentInfo(idValues,patientId));
			List<ServiceDetail> servicesList=serviceDetailRepository.findAll(ChargesSpecification.getAllServices(idValues,patientId));
			Cpt  commentEntryCpt=cptRepository.findOne(ChargesSpecification.getCptCodeDetails(cptVales));
			if(paymentChecking.size()>0){
				for(int i=0;i<servicesList.size();i++){
					for(int j=0;j<paymentChecking.size();j++){
						if(paymentChecking.get(j).getNonServiceDetailServiceId().equals(servicesList.get(i).getServiceDetailId())){
							if(Optional.fromNullable(Strings.emptyToNull(paymentChecking.get(j).getNonServiceDetailAmount().toString())).or("0.00").equals("0.00")){
								ServiceDetail updatServcie=servicesList.get(i);
								getServiceEntityToUpdate(updatServcie,commentEntryCpt,modifiedDate,loginId,loginName);
							}
						}
					}
				}
			}else{
//				List<ServiceDetail> sercvicesToDelete=new ArrayList<ServiceDetail>();
				for(int i=0;i<servicesList.size();i++){
					getServiceEntityToUpdate(servicesList.get(i),commentEntryCpt,modifiedDate,loginId,loginName);
				}
			}
//			serviceDetailRepository.delete(serviceDetailRepository.findAll(ChargesSpecification.getAllServices(idValues,patientId)));
		} catch (Exception e) {
			confirmation=false;
			e.printStackTrace();
		}
		
		return confirmation;
	}

	public void getServiceEntityToUpdate(ServiceDetail updatServcie, Cpt commentEntryCpt, String modifiedDate,String loginId,String loginName) {
		String description=" CPT: "+Optional.fromNullable(Strings.emptyToNull(updatServcie.getCpt().getCptCptcode())).or("");
		description=description+" Units: "+Optional.fromNullable(Strings.emptyToNull(updatServcie.getServiceDetailUnit().toString())).or("0");
		description=description+" Charge: "+Optional.fromNullable(Strings.emptyToNull(updatServcie.getServiceDetailCharges().toString())).or("0.00");
		description=description+" Copay: "+Optional.fromNullable(Strings.emptyToNull(updatServcie.getServiceDetailCopay().toString())).or("0.00");
		description=description+" Payment: 0.00";
		updatServcie.setServiceDetailCharges(0.00);
		updatServcie.setServiceDetailCopay(0.00);
		updatServcie.setServiceDetailComments(description);
		updatServcie.setServiceDetailCptid(commentEntryCpt.getCptId());
		updatServcie.setServiceDetailUnknown2(0.00);
		updatServcie.setServiceDetailSubmitStatus("X");
		updatServcie.setServiceDetailExpectedPayments(0.00);
		updatServcie.setServiceDetailModifiedby(loginName);
		updatServcie.setServiceDetailModifieddate(Timestamp.valueOf(modifiedDate));
		serviceDetailRepository.saveAndFlush(updatServcie);
	}

	@Override
	public ServiceDetail getPreviousVisitDxCodes(Integer patientId) {
		patientId=Optional.fromNullable(Integer.parseInt(Strings.emptyToNull(patientId.toString()))).or(-1);
		ServiceDetail recetServiceDetails=new ServiceDetail();
		List<ServiceDetail> serviceDxDetails=new ArrayList<ServiceDetail>();
		try {
			List<ServiceDetail> selectMaxDos=serviceDetailRepository.findAll(ChargesSpecification.getMaxDos(patientId),new PageRequest(0, 1,Direction.DESC,"serviceDetailDos")).getContent();
			if(selectMaxDos.size()>0){
				serviceDxDetails=serviceDetailRepository.findAll(ChargesSpecification.getPreviousVisittDxCodes(patientId,selectMaxDos.get(0).getServiceDetailDos()));
				if(serviceDxDetails.size()>0){
					recetServiceDetails=serviceDxDetails.get(0);
				}
			}
		} catch (Exception e) {
		}
		return recetServiceDetails;
	}
	@Override
	public List<SubmitStatus> getSubmitStausInfo() {
		List<SubmitStatus> submitStatusInfo=submitStatusRepository.findAll();
		return submitStatusInfo;
	}
	@Override
	public List<Cpt> getAllCptCodesDetails(List<String> cptCodes) {
		List<Cpt> cptCodeDetails=new ArrayList<Cpt>();
		try {
			cptCodeDetails=cptRepository.findAll(ChargesSpecification.getCptCodeDetails(cptCodes));
		} catch (Exception e) {
		}
		return cptCodeDetails;
	}
	
	@Override
	public PatientRegistration getGuarantorDetail(Integer patientid) {
		return patientRegistrationRepository.findOne(patientid);
	}

	@Override
	public InitialSettings getDefaultBillingReason() {
		return initialSettingsRepository.findOne(ChargesSpecification.getDefaultBillingReason());
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Boolean insertEnityCreation(SaveServicesBean saveServicesBean, Cpt cptCodeDetails,PatientRegistration forGuarantor, int i,String defaultBillingReason) {
		Boolean confirmation=true;
		Boolean duplicateChk=checkDuplicateService(saveServicesBean,cptCodeDetails);
		if(duplicateChk){
			ServiceDetail serviceInformation=new ServiceDetail();
			try {
				serviceInformation.setServiceDetailId(-1);
				serviceInformation.setServiceDetailPatientid(saveServicesBean.getPatientid());
				DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				java.util.Date dateofService = (java.util.Date)formatter.parse(saveServicesBean.getDateofService());
				java.util.Date dosFrom = (java.util.Date)formatter.parse(saveServicesBean.getDateofFrom());
				java.util.Date dateofPosting = (java.util.Date)formatter.parse(saveServicesBean.getDateofPosting());
				SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
				serviceInformation.setServiceDetailDos(Date.valueOf(newFormat.format(dateofService)));
				serviceInformation.setServiceDetailDosFrom(Date.valueOf(newFormat.format(dosFrom)));
				serviceInformation.setServiceDetailDop(Date.valueOf(newFormat.format(dateofPosting)));
				serviceInformation.setServiceDetailPlacedby(saveServicesBean.getLoginName());
				serviceInformation.setServiceDetailPlaceddate(Date.valueOf(newFormat.format(dateofPosting)));
				serviceInformation.setServiceDetailExpectedDate(Date.valueOf(newFormat.format(addDays(dateofService, 60))));
				serviceInformation.setServiceDetailUnknown2(getExpectedPayments(cptCodeDetails.getCptId(),saveServicesBean.getModifier1(),saveServicesBean.getModifier2(),saveServicesBean.getPlaceofServiceId(),Date.valueOf(newFormat.format(dateofService)).getYear(),Integer.parseInt(saveServicesBean.getPrimaryInsuraceId())));
				serviceInformation.setServiceDetailSubmitStatus("0");
				serviceInformation.setServiceDetailCopay(0.00);
				serviceInformation.setServiceDetailCptid(cptCodeDetails.getCptId());
				serviceInformation.setServiceDetailComments(cptCodeDetails.getCptDescription());
				serviceInformation.setServiceDetailModifier1(saveServicesBean.getModifier1());
				serviceInformation.setServiceDetailModifier2(saveServicesBean.getModifier2());
				serviceInformation.setServiceDetailModifier3(saveServicesBean.getModifier3());
				serviceInformation.setServiceDetailModifier4(saveServicesBean.getModifier4());
				serviceInformation.setServiceDetailSdoctorid(Integer.parseInt(saveServicesBean.getServiceDrId()));
				serviceInformation.setServiceDetailBdoctorid(Integer.parseInt(saveServicesBean.getBillingDrId()));
				serviceInformation.setServiceDetailPosid(Integer.parseInt(saveServicesBean.getPlaceofServiceId()));
				serviceInformation.setServiceDetailUnit(saveServicesBean.getUnits());
				serviceInformation.setServiceDetailCharges((cptCodeDetails.getCptCost()*saveServicesBean.getUnits()));
				serviceInformation.setServiceDetailPrimaryins(Integer.parseInt(saveServicesBean.getPrimaryInsuraceId()));
				serviceInformation.setServiceDetailSecondaryins(Long.parseLong(saveServicesBean.getSecondaryInsuraceId()));
				serviceInformation.setServiceDetailGuarantorId(Long.parseLong(forGuarantor.getPatientRegistrationGuarantorid().toString()));
				serviceInformation.setServiceDetailSource(saveServicesBean.getSourceLocation());
				serviceInformation.setServiceDetailAnesStarttime(saveServicesBean.getAnesStarttime());
				serviceInformation.setServiceDetailAnesEndtime(saveServicesBean.getAnesEndtime());
				serviceInformation.setServiceDetailDontbill(1);
				serviceInformation.setServiceDetailBillingReason(Integer.parseInt(defaultBillingReason));
				serviceInformation.setServiceDetailDx1(saveServicesBean.getDx1());
				serviceInformation.setServiceDetailDx2(saveServicesBean.getDx2());
				serviceInformation.setServiceDetailDx3(saveServicesBean.getDx3());
				serviceInformation.setServiceDetailDx4(saveServicesBean.getDx4());
				serviceInformation.setServiceDetailDx5(saveServicesBean.getDx5());
				serviceInformation.setServiceDetailDx6(saveServicesBean.getDx6());
				serviceInformation.setServiceDetailDx7(saveServicesBean.getDx7());
				serviceInformation.setServiceDetailDx8(saveServicesBean.getDx8());
				serviceInformation.setServiceDetailDx9(saveServicesBean.getDx9());
				serviceInformation.setServiceDetailDx10(saveServicesBean.getDx10());
				serviceInformation.setServiceDetailDx11(saveServicesBean.getDx11());
				serviceInformation.setServiceDetailDx12(saveServicesBean.getDx12());
				serviceInformation.setServiceDetailDx13(saveServicesBean.getDx13());
				serviceInformation.setServiceDetailDx14(saveServicesBean.getDx14());
				serviceInformation.setServiceDetailDx15(saveServicesBean.getDx15());
				serviceInformation.setServiceDetailDx16(saveServicesBean.getDx16());
				serviceInformation.setServiceDetailDx17(saveServicesBean.getDx17());
				serviceInformation.setServiceDetailDx18(saveServicesBean.getDx18());
				serviceInformation.setServiceDetailDx19(saveServicesBean.getDx19());
				serviceInformation.setServiceDetailDx20(saveServicesBean.getDx20());
				serviceInformation.setServiceDetailDx1desc(saveServicesBean.getDx1Desc());
				serviceInformation.setServiceDetailDx2desc(saveServicesBean.getDx2Desc());
				serviceInformation.setServiceDetailDx3desc(saveServicesBean.getDx3Desc());
				serviceInformation.setServiceDetailDx4desc(saveServicesBean.getDx4Desc());
				serviceInformation.setServiceDetailDx5desc(saveServicesBean.getDx5Desc());
				serviceInformation.setServiceDetailDx6desc(saveServicesBean.getDx6Desc());
				serviceInformation.setServiceDetailDx7desc(saveServicesBean.getDx7Desc());
				serviceInformation.setServiceDetailDx8desc(saveServicesBean.getDx8Desc());
				serviceInformation.setServiceDetailDx9desc(saveServicesBean.getDx9Desc());
				serviceInformation.setServiceDetailDx10desc(saveServicesBean.getDx10Desc());
				serviceInformation.setServiceDetailDx11desc(saveServicesBean.getDx11Desc());
				serviceInformation.setServiceDetailDx12desc(saveServicesBean.getDx12Desc());
				serviceInformation.setServiceDetailDx13desc(saveServicesBean.getDx13Desc());
				serviceInformation.setServiceDetailDx14desc(saveServicesBean.getDx14Desc());
				serviceInformation.setServiceDetailDx15desc(saveServicesBean.getDx15Desc());
				serviceInformation.setServiceDetailDx16desc(saveServicesBean.getDx16Desc());
				serviceInformation.setServiceDetailDx17desc(saveServicesBean.getDx17Desc());
				serviceInformation.setServiceDetailDx18desc(saveServicesBean.getDx18Desc());
				serviceInformation.setServiceDetailDx19desc(saveServicesBean.getDx19Desc());
				serviceInformation.setServiceDetailDx20desc(saveServicesBean.getDx20Desc());
				serviceInformation.setServiceDetailDxsystem(saveServicesBean.getDxSystem());
				requestToSaveService(serviceInformation);
			} catch (Exception e) {
				e.printStackTrace();
				confirmation=false;
			}
		}
		return confirmation;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean checkDuplicateService(SaveServicesBean saveServicesBean, Cpt cptCodeDetails) {
		Boolean confirmation=true;
		try {
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			java.util.Date dateofService = (java.util.Date)formatter.parse(saveServicesBean.getDateofService());
			SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
			List<ServiceDetail> serviceDetailEntity=serviceDetailRepository.findAll(ChargesSpecification.checkDupliacteService(saveServicesBean.getPatientid(),Date.valueOf(newFormat.format(dateofService)),saveServicesBean.getModifier1(),saveServicesBean.getModifier2(),saveServicesBean.getModifier3(),saveServicesBean.getModifier4(),String.valueOf(cptCodeDetails.getCptId())));
			if(serviceDetailEntity.size()>0){
				 confirmation=false;
			}
		} catch (Exception e) {
			 confirmation=false;
		}
		return confirmation;
	}

	public java.util.Date addDays(java.util.Date dateofService, int numDays)  
	{  
		long MILLIS_PER_DAY = 24 * 60 * 60 * 1000; 
		return new Date(dateofService.getTime() + numDays * MILLIS_PER_DAY);  
	} 
	public void requestToSaveService(ServiceDetail createdEntity) {
		try {
			AssociateServiceDetails associateServiceInsert=new AssociateServiceDetails();
			serviceDetailRepository.save(createdEntity);
			List<H213> h213=h213Repository.findAll(ChargesSpecification.getServiceDetailMaxId());
			associateServiceInsert.setAssociateServiceDetailId(Long.valueOf(-1));
			associateServiceInsert.setAssociateServiceDetailServiceId(h213.get(0).getH213003());
			associateServiceInsert.setAssociateServiceDetailSpecialDx("");
			associateServiceDetailsRepository.save(associateServiceInsert);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Double getExpectedPayments(Integer cptId, String modifier1,String modifier2, String placeofServiceId, int dosYear,Integer primaryInsuraceId) {
		double expPmt = 0.00;
		if(primaryInsuraceId>0){
			List<H066> h066Pmt=h066Repository.findAll(ChargesSpecification.getExpectedPayment(cptId,modifier1,modifier2,placeofServiceId,dosYear,primaryInsuraceId));
			if(h066Pmt.size()>0)
			expPmt=Double.valueOf(Optional.fromNullable(Strings.emptyToNull(h066Pmt.get(0).getH066007().toString())).or("0.00"));
		}
		return expPmt;
	}

	@Override
	public Boolean updateServiceDetails(UpdateServiceBean updateService) {
		try {
			List<Integer> serviceIds=new ArrayList<Integer>();
			serviceIds.add(updateService.getServiceId());
			ServiceDetail existedServiceEntity=serviceDetailRepository.findOne(ChargesSpecification.getAllServices(serviceIds, updateService.getPatientId()));
			Boolean confirmation=updateServiceEntityCreation(existedServiceEntity,updateService);
			return confirmation;
		} catch (Exception e) {
			return false;
		}
	}

	private Boolean updateServiceEntityCreation(ServiceDetail existedServiceEntity, UpdateServiceBean updateService) {
		Boolean returnValue=true;
		try {
			existedServiceEntity.setServiceDetailCharges(updateService.getCptCost());
			existedServiceEntity.setServiceDetailUnit(updateService.getUnits());
			existedServiceEntity.setServiceDetailModifier1(updateService.getModifier1());
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			java.util.Date dateofService = (java.util.Date)formatter.parse(updateService.getDateofService().toString());
			SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
			existedServiceEntity.setServiceDetailDos(Date.valueOf(newFormat.format(dateofService)));
			existedServiceEntity.setServiceDetailPosid(updateService.getPlaceofServiceId());
			existedServiceEntity.setServiceDetailSdoctorid(updateService.getServiceDrId());
			existedServiceEntity.setServiceDetailBdoctorid(updateService.getBillingDrId());
			existedServiceEntity.setServiceDetailPrimaryins(updateService.getPrimaryInsuraceId());
			existedServiceEntity.setServiceDetailSecondaryins(updateService.getSecondaryInsuraceId());
			existedServiceEntity.setServiceDetailCopay(updateService.getCopay());
			existedServiceEntity.setServiceDetailAnesStarttime(updateService.getAnesStarttime());
			existedServiceEntity.setServiceDetailAnesEndtime(updateService.getAnesEndtime());
			existedServiceEntity.setServiceDetailDx1(updateService.getDx1());
			existedServiceEntity.setServiceDetailDx2(updateService.getDx2());
			existedServiceEntity.setServiceDetailDx3(updateService.getDx3());
			existedServiceEntity.setServiceDetailDx4(updateService.getDx4());
			existedServiceEntity.setServiceDetailDx5(updateService.getDx5());
			existedServiceEntity.setServiceDetailDx6(updateService.getDx6());
			existedServiceEntity.setServiceDetailDx7(updateService.getDx7());
			existedServiceEntity.setServiceDetailDx8(updateService.getDx8());
			existedServiceEntity.setServiceDetailDx9(updateService.getDx9());
			existedServiceEntity.setServiceDetailDx10(updateService.getDx10());
			existedServiceEntity.setServiceDetailDx11(updateService.getDx11());
			existedServiceEntity.setServiceDetailDx12(updateService.getDx12());
			existedServiceEntity.setServiceDetailDx13(updateService.getDx13());
			existedServiceEntity.setServiceDetailDx14(updateService.getDx14());
			existedServiceEntity.setServiceDetailDx15(updateService.getDx15());
			existedServiceEntity.setServiceDetailDx16(updateService.getDx16());
			existedServiceEntity.setServiceDetailDx17(updateService.getDx17());
			existedServiceEntity.setServiceDetailDx18(updateService.getDx18());
			existedServiceEntity.setServiceDetailDx19(updateService.getDx19());
			existedServiceEntity.setServiceDetailDx20(updateService.getDx20());
			existedServiceEntity.setServiceDetailDx1desc(updateService.getDx1Desc());
			existedServiceEntity.setServiceDetailDx2desc(updateService.getDx2Desc());
			existedServiceEntity.setServiceDetailDx3desc(updateService.getDx3Desc());
			existedServiceEntity.setServiceDetailDx4desc(updateService.getDx4Desc());
			existedServiceEntity.setServiceDetailDx5desc(updateService.getDx5Desc());
			existedServiceEntity.setServiceDetailDx6desc(updateService.getDx6Desc());
			existedServiceEntity.setServiceDetailDx7desc(updateService.getDx7Desc());
			existedServiceEntity.setServiceDetailDx8desc(updateService.getDx8Desc());
			existedServiceEntity.setServiceDetailDx9desc(updateService.getDx9Desc());
			existedServiceEntity.setServiceDetailDx10desc(updateService.getDx10Desc());
			existedServiceEntity.setServiceDetailDx11desc(updateService.getDx11Desc());
			existedServiceEntity.setServiceDetailDx12desc(updateService.getDx12Desc());
			existedServiceEntity.setServiceDetailDx13desc(updateService.getDx13Desc());
			existedServiceEntity.setServiceDetailDx14desc(updateService.getDx14Desc());
			existedServiceEntity.setServiceDetailDx15desc(updateService.getDx15Desc());
			existedServiceEntity.setServiceDetailDx16desc(updateService.getDx16Desc());
			existedServiceEntity.setServiceDetailDx17desc(updateService.getDx17Desc());
			existedServiceEntity.setServiceDetailDx18desc(updateService.getDx18Desc());
			existedServiceEntity.setServiceDetailDx19desc(updateService.getDx19Desc());
			existedServiceEntity.setServiceDetailDx20desc(updateService.getDx20Desc());
			existedServiceEntity.setServiceDetailDxsystem(updateService.getDxSystem());
			existedServiceEntity.setServiceDetailModifiedby(updateService.getLoginName());
			java.util.Date currentdate=new java.util.Date();
			Timestamp currentTime = new Timestamp(currentdate.getTime());
			existedServiceEntity.setServiceDetailModifieddate(currentTime);
			serviceDetailRepository.saveAndFlush(existedServiceEntity);
		} catch (Exception e) {
			returnValue=false;
		}
		return returnValue;
	}

	@Override
	public List<H611> getEMRDxCodes(Integer patientId,String dos) {
		patientId=Optional.fromNullable(Integer.parseInt(Strings.emptyToNull(patientId.toString()))).or(-1);
		dos=Optional.fromNullable(Strings.emptyToNull(dos.toString())).or("");
		List<H611> getEMRDxcodes=new ArrayList<H611>();
		try {
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			java.util.Date encounterDate = (java.util.Date)formatter.parse(dos);
			SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
			String startDate=newFormat.format(encounterDate).toString()+" 00:00:00";
			String endDate=newFormat.format(encounterDate).toString()+" 23:59:59";
			List<Encounter> maxEncounterId=encounterRepository.findAll(ChargesSpecification.getMaxEncounterId(patientId,startDate,endDate),new PageRequest(0, 1,Direction.DESC,"encounterId")).getContent();
			if(maxEncounterId.size()>0){
				getEMRDxcodes=h611Repository.findAll(ChargesSpecification.getEMRDxCodes(patientId,maxEncounterId.get(0).getEncounterId())); 
			}
		} catch (Exception e) {
			
		}
		return getEMRDxcodes;
	}

}
