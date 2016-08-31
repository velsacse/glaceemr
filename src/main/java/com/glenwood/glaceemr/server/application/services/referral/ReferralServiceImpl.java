package com.glenwood.glaceemr.server.application.services.referral;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.controllers.PlanReferralController;
import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.Encounter;	
import com.glenwood.glaceemr.server.application.models.Guarantor;
import com.glenwood.glaceemr.server.application.models.H611;
import com.glenwood.glaceemr.server.application.models.LeafPatient;
import com.glenwood.glaceemr.server.application.models.ProblemList;
import com.glenwood.glaceemr.server.application.models.Referral;
import com.glenwood.glaceemr.server.application.repositories.ChartRepository;
import com.glenwood.glaceemr.server.application.repositories.EmpProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.EncounterRepository;
import com.glenwood.glaceemr.server.application.repositories.GuarantorRepository;
import com.glenwood.glaceemr.server.application.repositories.H611Repository;
import com.glenwood.glaceemr.server.application.repositories.LeafPatientRepository;
import com.glenwood.glaceemr.server.application.repositories.ProblemListRepository;
import com.glenwood.glaceemr.server.application.repositories.ReferralRepository;
import com.glenwood.glaceemr.server.application.repositories.ReferringPhysiciansRepository;
import com.glenwood.glaceemr.server.application.specifications.ChartSpecification;
import com.glenwood.glaceemr.server.application.specifications.EmployeeSpecification;
import com.glenwood.glaceemr.server.application.specifications.EncounterSpecification;
import com.glenwood.glaceemr.server.application.specifications.GuarantorSpecification;
import com.glenwood.glaceemr.server.application.specifications.H611Specfication;
import com.glenwood.glaceemr.server.application.specifications.LeafPatientSpecfication;
import com.glenwood.glaceemr.server.application.specifications.ProblemListSpecification;
import com.glenwood.glaceemr.server.application.specifications.ReferralSpecification;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

/**
 * Service implementation file for ReferralController, PlanReferralController
 * @author software
 */

@Service
public class ReferralServiceImpl implements ReferralService{

	@PersistenceContext
	EntityManager em;

	@Autowired
	ReferralRepository referralRepository; 
	
	@Autowired
	EncounterRepository EncounterRepository;
	
	@Autowired
	EmpProfileRepository empProfileRepository;
	
	@Autowired
	ReferringPhysiciansRepository referringPhysiciansRepository;
	
	@Autowired
	LeafPatientRepository leafPatientRepository;
	
	@Autowired
	ChartRepository chartRepository; 
	
	@Autowired
	H611Repository h611Repository;
	
	@Autowired
	ProblemListRepository problemListRepository; 
	
	@Autowired
	GuarantorRepository guarantorRepository;
	
	private Logger logger = Logger.getLogger(PlanReferralController.class);
	
	/* 
	 * Get list of referrals
	 * @see com.glenwood.glaceemr.server.application.services.referral.ReferralService#getListOfReferralsPlan(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Referral> getListOfReferralsPlan(Integer encounterID, Integer chartID, String dx) {
		
		Integer encounterId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(encounterID.toString())).or("-1"));
		Integer chartId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(chartID.toString())).or("-1"));
		
		List<Referral> result = null;		
		dx = dx.trim();
		
		if(!dx.trim().isEmpty()){
			
			  result= referralRepository.findAll(Specifications
							.where(ReferralSpecification.findByEncounterId(encounterId))
							.and(ReferralSpecification.findByChartId(chartId))
							.and(ReferralSpecification.findByStatusNotEqual(2))
							.and(ReferralSpecification.getBydxCode(dx))
							.and(ReferralSpecification.orderById()));
		
		}
		else{

			  result= referralRepository.findAll(Specifications
							.where(ReferralSpecification.findByEncounterId(encounterId))
							.and(ReferralSpecification.findByChartId(chartId))
							.and(ReferralSpecification.findByStatusNotEqual(2))
							.and(ReferralSpecification.orderById()));

		}
		logger.debug("Getting list of referrals");
		
		return result;
	}
	
	/*
	 * Get referral based on referral id
	 * @see com.glenwood.glaceemr.server.application.services.referral.ReferralService#getReferralPlan(java.lang.Integer)
	 */
	@Override
	public Referral getReferralPlan(Integer referralID) {
		
		Integer referralId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(referralID.toString())).or("-1"));
		Referral result = referralRepository.findOne(Specifications.where(ReferralSpecification.findByRefId(referralId)).and(ReferralSpecification.orderById()));
		logger.debug("Getting referral");
		
		return result;
	}
	
	/*
	 * Save referral
	 * @see com.glenwood.glaceemr.server.application.services.referral.ReferralService#saveReferralPlan(java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public void saveReferralPlan(Integer referralID, String reason, String notes, String dx) {
		
		Integer referralId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(referralID.toString())).or("-1"));
		Iterable<Referral> referralList = referralRepository.findAll(ReferralSpecification.findByRefId(referralId));
		
		Referral referral =null;
		
		for(Referral ref: referralList){
//			Date date = new Date();
			Referral refUpdate = ref;
//			refUpdate.setH413004(new Timestamp(date.getTime()));
			refUpdate.setH413037(reason);
			refUpdate.setH413013(notes);
			refUpdate.setH413011(dx);
			referral = refUpdate;
			referralRepository.saveAndFlush(referral);
		}
		
		logger.debug("Saving referral");
	}

	
	/* 
	 * Cancel referral - changing status to cancelled
	 * @see com.glenwood.glaceemr.server.application.services.referral.ReferralService#cancelReferral(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void cancelReferral(Integer loginID, Integer refID) {
		
		Integer loginId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(loginID.toString())).or("-1"));
		Integer refId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(refID.toString())).or("-1"));
		if(loginId==-1)
			loginId=null;
		Iterable<Referral> referralList = referralRepository.findAll(ReferralSpecification.findByRefId(refId));
		Referral referral = null;
		
		for(Referral ref: referralList){
			Date date = new Date();
			Referral refUpdate = ref;
			refUpdate.setH413041(2);
			refUpdate.setReferralCancelBy(loginId);
			refUpdate.setReferralCancelOn(new Timestamp(date.getTime()));
			referral = refUpdate;
			referralRepository.saveAndFlush(referral);
		}
		
		logger.debug("Cancelling referral");
		
	}
	
	/*
	 * Get referral
	 * (non-Javadoc)
	 * @see com.glenwood.glaceemr.server.application.services.referral.ReferralService#getReferralSummaryDetails(java.lang.Integer, java.lang.Integer)
	 */
	
	@Override
	public ReferralBean getReferral(Integer referralID, Integer chartID, Boolean fromEdit) {
		
		ReferralBean result=null;
		
		Integer referralId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(referralID.toString())).or("-1"));
		Integer chartId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(chartID.toString())).or("-1"));
		
		List<Chart> chart = chartRepository.findAll(ChartSpecification.findByChartId(chartId));
		Integer patientId = -1;
		for(Chart eachchart: chart)
			patientId = eachchart.getChartPatientid();
		
		List<Referral> referralList = referralRepository.findAll(ReferralSpecification.getByRefId(referralId));
		/*EmployeeProfile employee;
		for(Referral referral: referralList){
			if(referral.getH413032()!=null && referral.getH413032().trim().length()>0){
				employee = empProfileRepository.findOne(EmployeeSpecification.findByEmpId(Integer.parseInt(referral.getH413032())));
				if(employee!=null){
					if(employee.getEmpProfileFullname()!=null)
						referral.setH413032(employee.getEmpProfileFullname());
				}
			}
		}*/
		
		List<Encounter> encounterList = EncounterRepository.findAll(Specifications
				   .where(EncounterSpecification.EncounterByChartId(chartId))
				   .and(EncounterSpecification.EncounterByType(1))
				   .and(EncounterSpecification.orderByDate()));

		
		List<Integer> ids = new ArrayList<>();
		for(Encounter enc: encounterList){
			ids.add(enc.getEncounterId());
		}
		
		List<LeafPatient> savedLeafList=null;
		if(referralId!=-1){
			Referral referral = referralRepository.findOne(ReferralSpecification.findByRefId(referralId));
			List<Integer> leafLibraryIdList = new ArrayList<>();
			List<Integer> leafPatientIdList = new ArrayList<>();
			List<Integer> leafencounterIdList = new ArrayList<>();
			if(referral!=null) {
				String template = referral.getH413038();
				if(template==null)
					template="";
				String[] templateArr = template.split("SoapNotesPrint.Action");
				if(!template.isEmpty()){
					for(int i=0;i<templateArr.length;i++){
						templateArr[i]=templateArr[i].replace('$', ' ').trim();
						String[] collect = templateArr[i].split("\\^\\^");
						leafLibraryIdList.add(Integer.parseInt(collect[0]));
						leafPatientIdList.add(Integer.parseInt(collect[1]));
						leafencounterIdList.add(Integer.parseInt(collect[2]));

					}
				}
				savedLeafList = leafPatientRepository.findAll(LeafPatientSpecfication.getSavedLeafDetails(patientId,chartId,leafLibraryIdList,leafPatientIdList,leafencounterIdList));
			}
		}
		
		/*for(LeafPatient leaf: savedLeafList){
			if(leaf.getLeafPatientIsfollowup()!=null && leaf.getLeafPatientIsfollowup()==true) {
				LeafLibrary leafLib = leaf.getLeafLibraryTable();
				String name = leafLib.getLeafLibraryName();
				leafLib.setLeafLibraryName(name+" - Follow up");
			}
		}*/
		
		if(fromEdit==true){
			List<EmployeeProfile> empList = empProfileRepository.findAll(Specifications.where(EmployeeSpecification.findByGroupId())
					.and(EmployeeSpecification.findByEmpFNameNotLike("demo"))
					.and(EmployeeSpecification.findByEmpLNameNotLike("demo"))
					.and(EmployeeSpecification.isActive(true))
					.and(EmployeeSpecification.orderByGroupId()));


			List<LeafPatient> leafList = leafPatientRepository.findAll(LeafPatientSpecfication.getLeafDetails(patientId,chartId,ids));
			
			/*for(LeafPatient leaf: leafList){
				if(leaf.getLeafPatientIsfollowup()!=null && leaf.getLeafPatientIsfollowup()==true) {
					LeafLibrary leafLib = leaf.getLeafLibraryTable();
					String name = leafLib.getLeafLibraryName();
					leafLib.setLeafLibraryName(name+" - Follow up");
				}
			}*/
			
			List<H611> dxList = h611Repository.findAll(H611Specfication.getByEncounterId(ids));

			List<ProblemList> problemList = problemListRepository.findAll(Specifications
					.where(ProblemListSpecification.getPatientProblems(patientId))
					.and(ProblemListSpecification.isActive(true)));
			
			result = new ReferralBean(empList, referralList, leafList, savedLeafList, dxList, problemList,null);
		}
		else
			result = new ReferralBean(null, referralList, null, savedLeafList, null, null,null);
		
		logger.debug("Getting referral details");
		
		return result;
	}
	
	/* 
	 * Get referral
	 * (non-Javadoc)
	 * @see com.glenwood.glaceemr.server.application.services.referral.ReferralService#getReferral(java.lang.Integer, java.lang.Integer)
	 
	@SuppressWarnings("null")
	@Override
	public ReferralBean getReferral(Integer referralID, Integer chartID ) {

		ReferralBean result=null;
		
		Integer referralId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(referralID.toString())).or("-1"));
		Integer chartId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(chartID.toString())).or("-1"));
		
		List<Referral> referralList = referralRepository.findAll(ReferralSpecification.getByRefId(referralId));
		
		List<Encounter> encounterList = EncounterRepository.findAll(Specifications
				   .where(EncounterSpecification.EncounterByChartId(chartId))
				   .and(EncounterSpecification.EncounterByType(1))
				   .and(EncounterSpecification.orderByDate()));

		
		List<Integer> ids = new ArrayList<>();
		for(Encounter enc: encounterList){
			ids.add(enc.getEncounterId());
		}

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<H611> root = cq.from(H611.class);
		
		Join<H611,CodingSystems> dxCodingSystemJoin= root.join("codingsystemsTable",JoinType.INNER);
		Predicate pred=null;
		if(ids.size()>0)
			pred= root.get(H611_.h611002).in(ids);
		else
			pred= root.get(H611_.h611002).in(-999);
		
		cq.distinct(true).select(builder.construct(ReferralDiagnosisBean.class,root.get(H611_.h611005),root.get(H611_.h611006))).where(pred).orderBy(builder.asc(root.get(H611_.h611005)));
		
		List<Object> getResult=em.createQuery(cq).getResultList();
		List<ReferralDiagnosisBean> diagnosisList = new ArrayList<ReferralDiagnosisBean>();
		
		for(int i=0;i<getResult.size();i++){
			ReferralDiagnosisBean beanObj = (ReferralDiagnosisBean) getResult.get(i);
			ReferralDiagnosisBean finalbeanObj = new ReferralDiagnosisBean(beanObj.getDxCode(), beanObj.getDxDescription());
			diagnosisList.add(finalbeanObj);
		}
		
//		List<H611> dxList = h611Repository.findAll(H611Specfication.getByEncounterId(ids));
		
		result = new ReferralBean(null, referralList, null, null, null, null, diagnosisList);
		
		logger.debug("Getting referral details");
		
		return result;
	}*/
	
	
	/* 
	 * Getting list of referrals
	 * (non-Javadoc)
	 * @see com.glenwood.glaceemr.server.application.services.referral.ReferralService#getListOfReferrals(java.lang.Integer)
	 */
	@Override
	public ReferralListBean getListOfReferrals(Integer chartID) {
		
		Integer chartId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(chartID.toString())).or("-1"));

		List<Chart> chart = chartRepository.findAll(ChartSpecification.findByChartId(chartId));
		
		Integer patientId = -1;
		String fname="", lname="", fullname="";
		for(Chart eachchart: chart)
			patientId = eachchart.getChartPatientid();
		
		List<Guarantor> guarantorList = guarantorRepository.findAll(GuarantorSpecification.getByPatientId(patientId));
		for(Guarantor eachGuarantor: guarantorList){
			lname = eachGuarantor.getGuarantorLastName();
			fname = eachGuarantor.getGuarantorFirstName();
		}
		if(lname!=null)
			fullname = lname;
		if(fname!=null)
			fullname = fullname +" "+fname;
		
		List<Referral> referralList = referralRepository.findAll(Specifications
							.where(ReferralSpecification.findByChartId(chartId))
							.and(ReferralSpecification.findByStatusNotEqual(2))
							.and(ReferralSpecification.orderByOrderedDate()));
		
		ReferralListBean referralListBean = new ReferralListBean(referralList, fullname.trim());
		
		logger.debug("Getting list of referrals");
		
		return referralListBean;
	}

	
	/*
	 * Saving referral details
	 * editReferral  -1 : Create new referral
	 * 				  1 : Save referral
	 * (non-Javadoc)
	 * @see com.glenwood.glaceemr.server.application.services.referral.ReferralService#saveReferral(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String saveReferral(Integer chartId, Integer encounterId,
			Integer patientId, Integer referralId, Boolean isMedSumm, Integer editReferral,
			Integer loginId, Integer userId, Integer reftoId,String reason, String apptBy,
			String apptNotes, Short apptConfirmed, String apptContactPerson,
			String apptDate, String apptTime, String authBy, String authNotes,
			String authDate, String authExpDate, Short authNeeded,
			String authNum, Integer numofVisits, Short authDone,
			String consNotes, Short isConsReportReceived,
			String consReportReceivedDate, Short isConsReportReviewed,
			String consReportReviewedDate, String guarantorName,
			String hospitalName, Boolean isSummCare, String orderedDate,
			Short patientNotified, String address, String fax, String phone,
			String specialization, String dx, String referredBy,
			String referredTo, Integer status, String referralNotes,
			String printLeaf) throws JSONException {
		
				String result = "-1";
				if(editReferral==1){
					
					Iterable<Referral> referralList = referralRepository.findAll(ReferralSpecification.findByRefId(referralId));
				
					Referral referral = new Referral();
				
					for(Referral ref: referralList){
						SimpleDateFormat sourceformatter = new SimpleDateFormat("MM/dd/yyyy");
						
						Referral refUpdate = ref;
						Date orderdate=null, apptdate=null, authdate=null, authexpdate=null, consreceivedate=null, consreviewedate=null;
						
						try {
							
							if(status!=-1) {
								Date date = new Date();
								Timestamp dataStamp = new Timestamp(date.getTime());
								referral.setReferralOrderBy(userId);
								if(!orderedDate.isEmpty()){
									orderdate = sourceformatter.parse(orderedDate);
									Timestamp orderdateTimestamp = new Timestamp(orderdate.getTime());
									referral.setReferralOrderOn(orderdateTimestamp);
								}
								else {
									referral.setReferralOrderOn(dataStamp);
								}
								
								if(status==2){
									referral.setReferralCancelBy(loginId);
									referral.setReferralOrderOn(dataStamp);
								}
								if(status==3){
									referral.setReferralReceiveBy(userId);
									referral.setReferralReceiveOn(dataStamp);
								}
								if(status==4){
									referral.setReferralReceiveBy(userId);
									referral.setReferralReceiveOn(dataStamp);
									referral.setReferralReviewedBy(userId);
									referral.setReferralReviewedOn(dataStamp);
								}
							}
							if(apptDate.isEmpty())
								apptDate=null;
							else {
								apptdate = sourceformatter.parse(apptDate);
								Timestamp apptdateTimestamp = new Timestamp(apptdate.getTime());
								referral.setH413022(apptdateTimestamp);
							}
							
							if(authDate.isEmpty())
								authDate=null;
							else {
								authdate = sourceformatter.parse(authDate);
								Timestamp authdateTimestamp = new Timestamp(authdate.getTime());
								referral.setH413017(authdateTimestamp);
							}
								
							if(authExpDate.isEmpty())
								authExpDate=null;
							else {
								authexpdate = sourceformatter.parse(authExpDate);
								Timestamp authexpdateTimestamp = new Timestamp(authexpdate.getTime());
								referral.setReferralExpirationDate(authexpdateTimestamp);
							}
							
							if(consReportReceivedDate.isEmpty())
								consReportReceivedDate=null;
							else {
								consreceivedate = sourceformatter.parse(consReportReceivedDate);
								Timestamp consreceivedTimestamp = new Timestamp(consreceivedate.getTime());
								referral.setH413029(consreceivedTimestamp);
							}
								
							if(consReportReviewedDate.isEmpty())
								consReportReviewedDate=null;
							else {
								consreviewedate = sourceformatter.parse(consReportReviewedDate);
								Timestamp consreviewedTimestamp = new Timestamp(consreviewedate.getTime());
								referral.setH413031(consreviewedTimestamp);
							}
							if(orderedDate.isEmpty())
								orderedDate=null;
							else {
								orderdate = sourceformatter.parse(orderedDate);
								Timestamp orderdateTimestamp = new Timestamp(orderdate.getTime());
								referral.setH413004(orderdateTimestamp);
							}
							
						}catch(ParseException e) {
							e.printStackTrace();
						}
						
						List<EmployeeProfile> emplist = empProfileRepository.findAll(EmployeeSpecification.findByEmpLoginId(loginId));
						Integer groupId = 0;
						for(EmployeeProfile emp: emplist){
							if(emp.getEmpProfileGroupid()==null)
								emp.setEmpProfileGroupid(-2);
							else
								groupId= emp.getEmpProfileGroupid();	
						}
						if(groupId==-1)
						refUpdate.setReferreddoctorLoginid(loginId);
						refUpdate.setReferralReferredtoid(reftoId);
						refUpdate.setH413005(referredBy);
						refUpdate.setH413006(referredTo);
						refUpdate.setH413007(specialization);
						refUpdate.setH413008(address);
						refUpdate.setH413009(phone);
						refUpdate.setH413010(fax);
						refUpdate.setH413011(dx);
						refUpdate.setH413013(referralNotes);
						refUpdate.setH413014(authNeeded);
						refUpdate.setH413015(authDone);
						refUpdate.setH413016(authNum);
						
						refUpdate.setH413018(authBy);
						refUpdate.setH413020(authNotes);
						refUpdate.setH413021(apptConfirmed);
						
						refUpdate.setH413023(apptTime);
						refUpdate.setH413024(apptBy);
						refUpdate.setH413025(apptContactPerson);
						refUpdate.setH413026(patientNotified);
						refUpdate.setH413027(apptNotes);
						refUpdate.setH413028(isConsReportReceived);
						refUpdate.setH413030(isConsReportReviewed);
						
						refUpdate.setH413033(consNotes);

						refUpdate.setH413037(reason);
						refUpdate.setH413038(printLeaf);
						refUpdate.setH413041(status);
						refUpdate.setReferralGuarantorname(guarantorName);
						refUpdate.setReferralNoVisits(numofVisits);
						refUpdate.setReferralHospitalname(hospitalName);
						refUpdate.setSummaryCareRecordProvided(isSummCare);
						referral = refUpdate;
						referralRepository.saveAndFlush(referral);
					}
					logger.debug("Saving referral");
					result="-1";
			} 
			else if(editReferral==-1){
				
				List<Referral> referralList = referralRepository.findAll(ReferralSpecification.orderByIdDesc());
				Referral ref = referralList.get(0);
				int maxid=ref.getH413001();
				
				Referral referral = new Referral();

				referral.setH413001(maxid+1);
				
				SimpleDateFormat sourceformatter = new SimpleDateFormat("MM/dd/yyyy");
				
				Date orderdate=null, apptdate=null, authdate=null, authexpdate=null, consreceivedate=null, consreviewedate=null;
				
				try {
					
					if(status!=-1) {
						Date date = new Date();
						Timestamp dataStamp = new Timestamp(date.getTime());
						referral.setReferralOrderBy(userId);
						if(!orderedDate.isEmpty()){
							orderdate = sourceformatter.parse(orderedDate);
							Timestamp orderdateTimestamp = new Timestamp(orderdate.getTime());
							referral.setReferralOrderOn(orderdateTimestamp);
						}
						else {
							referral.setReferralOrderOn(dataStamp);
						}
						
						if(status==2){
							referral.setReferralCancelBy(loginId);
							referral.setReferralOrderOn(dataStamp);
						}
						if(status==3){
							referral.setReferralReceiveBy(userId);
							referral.setReferralReceiveOn(dataStamp);
						}
						if(status==4){
							referral.setReferralReceiveBy(userId);
							referral.setReferralReceiveOn(dataStamp);
							referral.setReferralReviewedBy(userId);
							referral.setReferralReviewedOn(dataStamp);
						}
					}
					if(apptDate.isEmpty())
						apptDate=null;
					else {
						apptdate = sourceformatter.parse(apptDate);
						Timestamp apptdateTimestamp = new Timestamp(apptdate.getTime());
						referral.setH413022(apptdateTimestamp);
					}
					
					if(authDate.isEmpty())
						authDate=null;
					else {
						authdate = sourceformatter.parse(authDate);
						Timestamp authdateTimestamp = new Timestamp(authdate.getTime());
						referral.setH413017(authdateTimestamp);
					}
						
					if(authExpDate.isEmpty())
						authExpDate=null;
					else {
						authexpdate = sourceformatter.parse(authExpDate);
						Timestamp authexpdateTimestamp = new Timestamp(authexpdate.getTime());
						referral.setReferralExpirationDate(authexpdateTimestamp);
					}
					
					if(consReportReceivedDate.isEmpty())
						consReportReceivedDate=null;
					else {
						consreceivedate = sourceformatter.parse(consReportReceivedDate);
						Timestamp consreceivedTimestamp = new Timestamp(consreceivedate.getTime());
						referral.setH413029(consreceivedTimestamp);
					}
						
					if(consReportReviewedDate.isEmpty())
						consReportReviewedDate=null;
					else {
						consreviewedate = sourceformatter.parse(consReportReviewedDate);
						Timestamp consreviewedTimestamp = new Timestamp(consreviewedate.getTime());
						referral.setH413031(consreviewedTimestamp);
					}
					if(orderedDate.isEmpty())
						orderedDate=null;
					else {
						orderdate = sourceformatter.parse(orderedDate);
						Timestamp orderdateTimestamp = new Timestamp(orderdate.getTime());
						referral.setH413004(orderdateTimestamp);
					}
					
				}catch(ParseException e) {
					e.printStackTrace();
				}
				
				referral.setReferralReferredtoid(reftoId);
				referral.setH413002(chartId);
				referral.setH413003(encounterId);
				referral.setH413005(referredBy);
				referral.setH413006(referredTo);
				referral.setH413007(specialization);
				referral.setH413008(address);
				referral.setH413009(phone);
				referral.setH413010(fax);
				referral.setH413011(dx);
				referral.setH413013(referralNotes);
				referral.setH413014(authNeeded);
				referral.setH413015(authDone);
				referral.setH413016(authNum);
				
				referral.setH413018(authBy);
				referral.setH413020(authNotes);
				referral.setH413021(apptConfirmed);
				
				referral.setH413023(apptTime);
				referral.setH413024(apptBy);
				referral.setH413025(apptContactPerson);
				referral.setH413026(patientNotified);
				referral.setH413027(apptNotes);
				referral.setH413028(isConsReportReceived);
				referral.setH413030(isConsReportReviewed);
				
				referral.setH413033(consNotes);
				referral.setH413034(-1);
				referral.setH413035(patientId);
				referral.setH413037(reason);
				referral.setH413038(printLeaf);
				referral.setH413040(-1);
				referral.setH413041(status);
				referral.setReferreddoctorLoginid(loginId);
				referral.setReferralGuarantorname(guarantorName);
				referral.setReferralNoVisits(numofVisits);
				referral.setReferralHospitalname(hospitalName);
				referral.setSummaryCareRecordProvided(isSummCare);
				referralRepository.save(referral);
				result=""+(maxid+1);
				
				logger.debug("Creating referral");
			}
			return result;
	}

	/* 
	 * Get referral
	 * (non-Javadoc)
	 * @see com.glenwood.glaceemr.server.application.services.referral.ReferralService#getReferral(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ReferralBean getReferral(Integer referralID, Integer chartID, Integer encounterID ) {

		ReferralBean result=null;
		
		Integer referralId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(referralID.toString())).or("-1"));
		Integer chartId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(chartID.toString())).or("-1"));
		Integer encounterId = Integer.parseInt(Optional.fromNullable(Strings.emptyToNull(chartID.toString())).or("-1"));
		
		String patientId = getpatientid(chartId);
		List<Referral> referralList = referralRepository.findAll(ReferralSpecification.getByRefId(referralId));
		
		List<H611> cdingsystems=h611Repository.findAll(ReferralSpecification.getcodingsystems(encounterId,chartId));
		List<ProblemList> problemlist=problemListRepository.findAll(ReferralSpecification.getproblemlist(patientId));
		
		List<H611> cdingsystems1=new ArrayList<>();
		for(int z=0;z<cdingsystems.size()-1;z++){
			String dxcode1=cdingsystems.get(z).getH611005();
			String oid=cdingsystems.get(z).getH611CodingSystemid();
			for(int z1=z+1;z1<cdingsystems.size();z1++){
				String dxcode2=cdingsystems.get(z1).getH611005();
				String oid2=cdingsystems.get(z1).getH611CodingSystemid();
				if(((dxcode1.replaceAll("\\s+","")).equalsIgnoreCase((dxcode2).replaceAll("\\s+","")))&&(oid.replaceAll("\\s+","").equalsIgnoreCase(oid2.replaceAll("\\s+","")))){

					if(cdingsystems.get(z).getH611001()>cdingsystems.get(z1).getH611001()){
						cdingsystems.remove(cdingsystems.get(z1));
					}else{
						cdingsystems.remove(cdingsystems.get(z));
						z--;
					}
				}

			}

		}

		for(int z=0;z<problemlist.size()-1;z++){
			String dxcode1=problemlist.get(z).getProblemListDxCode();
			for(int z1=z+1;z1<problemlist.size();z1++){
				String dxcode2=problemlist.get(z1).getProblemListDxCode();
				if((dxcode1.replaceAll("\\s+","")).equalsIgnoreCase((dxcode2).replaceAll("\\s+",""))){
					if(problemlist.get(z).getProblemListId()>problemlist.get(z1).getProblemListId()){
						problemlist.remove(problemlist.get(z1));
					}else{
						problemlist.remove(problemlist.get(z));
						z--;
					}
				}


			}

		}

		for(int i=0;i<problemlist.size();i++){
			String dxcode=problemlist.get(i).getProblemListDxCode();
			for(int j=0;j<cdingsystems.size();j++){
				String dxcode1=cdingsystems.get(j).getH611005();
				if(dxcode.replaceAll("\\s+","").equalsIgnoreCase(dxcode1.replaceAll("\\s+",""))){
					if(problemlist.get(i).getProblemListId()>cdingsystems.get(j).getH611001()){
						cdingsystems.remove(cdingsystems.get(j));
					}else{
						problemlist.remove(problemlist.get(i));
					}
				}
			}
		}

		result = new ReferralBean(null,referralList,null,null,cdingsystems,problemlist,null);
		
		logger.debug("Getting referral details");
		
		return result;
	}
	
	public String getpatientid(int chartId){
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<Chart> root = cq.from(Chart.class);
		cq.select(root.get(Chart_.chartPatientid));
		cq.where(builder.equal(root.get(Chart_.chartId), chartId));
		List <Object> resultList = em.createQuery(cq).getResultList();
		String patientId="-1";
		if(resultList.size()>0)
			patientId=resultList.get(0).toString();
		return patientId;

	}

}
