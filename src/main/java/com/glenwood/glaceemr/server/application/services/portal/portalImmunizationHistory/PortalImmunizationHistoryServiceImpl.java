package com.glenwood.glaceemr.server.application.services.portal.portalImmunizationHistory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.AlertCategory;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.CvxVaccineGroupMapping;
import com.glenwood.glaceemr.server.application.models.H068;
import com.glenwood.glaceemr.server.application.models.H810;
import com.glenwood.glaceemr.server.application.models.ImmunizationRecord;
import com.glenwood.glaceemr.server.application.models.LabDescription;
import com.glenwood.glaceemr.server.application.models.LabDescription_;
import com.glenwood.glaceemr.server.application.models.LabEntries;
import com.glenwood.glaceemr.server.application.models.PatientImmunizationInformation;
import com.glenwood.glaceemr.server.application.models.PortalImmunizationHistoryBean;
import com.glenwood.glaceemr.server.application.models.Vaccine;
import com.glenwood.glaceemr.server.application.models.VaccineReport;
import com.glenwood.glaceemr.server.application.models.VaccineUpdateBean;
import com.glenwood.glaceemr.server.application.models.Vis;
import com.glenwood.glaceemr.server.application.repositories.AlertCategoryRepository;
import com.glenwood.glaceemr.server.application.repositories.AlertEventRepository;
import com.glenwood.glaceemr.server.application.repositories.CvxVaccineGroupRepository;
import com.glenwood.glaceemr.server.application.repositories.H068Repository;
import com.glenwood.glaceemr.server.application.repositories.H810Respository;
import com.glenwood.glaceemr.server.application.repositories.LabDescriptionRepository;
import com.glenwood.glaceemr.server.application.repositories.LabEntriesRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientImmunizationInformationRepository;
import com.glenwood.glaceemr.server.application.repositories.VaccineReportRepository;
import com.glenwood.glaceemr.server.application.repositories.VisRepository;
import com.glenwood.glaceemr.server.application.specifications.AlertCategorySpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalAlertSpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalImmunizationHistorySpecification;


@Service
public class PortalImmunizationHistoryServiceImpl implements PortalImmunizationHistoryService{


	@Autowired
	LabDescriptionRepository labDescriptionRepository;

	@Autowired
	LabEntriesRepository labEntriesRepository;

	@Autowired
	VaccineReportRepository vaccineReportRepository;

	@Autowired
	H068Repository h068Repository;

	@Autowired
	PatientImmunizationInformationRepository patientImmunizationInformationRepository;

	@Autowired
	VisRepository visRepository;

	@Autowired
	CvxVaccineGroupRepository cvxVaccineGroupRepository;

	@Autowired
	H810Respository h810Respository; 

	@Autowired
	AlertEventRepository alertEventRepository;
	
	@Autowired
	AlertCategoryRepository alertCategoryRepository;

	@Autowired
	EntityManagerFactory emf ;

	@PersistenceContext
	EntityManager em;


	@Override
	public List<ImmunizationRecord> getPatientImmunizationHistory(int patientId, int chartId, int pageOffset, int pageIndex) {

		PortalImmunizationHistoryBean immunizationHistoryBean=new PortalImmunizationHistoryBean();

		List<LabEntries> labDescImmunizationHistoryList;
		List<VaccineReport> vaccineReportImmunizationHistoryList;

		if(pageOffset>0&&pageIndex>=0){
			labDescImmunizationHistoryList=labEntriesRepository.findAll(PortalImmunizationHistorySpecification.getLabDescImmunizationHistory(patientId, chartId), createLabDescImmunizationHitoryRequestByDescDate(pageIndex, pageOffset)).getContent();
			vaccineReportImmunizationHistoryList=vaccineReportRepository.findAll(PortalImmunizationHistorySpecification.getVaccineReportImmunizationHistory(patientId, chartId), createVaccReportImmunizationHitoryRequestByDescDate(pageIndex, pageOffset)).getContent();
		}
		else{
			labDescImmunizationHistoryList=labEntriesRepository.findAll(PortalImmunizationHistorySpecification.getLabDescImmunizationHistory(patientId, chartId));
			vaccineReportImmunizationHistoryList=vaccineReportRepository.findAll(PortalImmunizationHistorySpecification.getVaccineReportImmunizationHistory(patientId, chartId));
		}


		Set<LabEntries> le = new HashSet<>();
		le.addAll(labDescImmunizationHistoryList);
		labDescImmunizationHistoryList.clear();
		labDescImmunizationHistoryList.addAll(le);

		Set<VaccineReport> vr = new HashSet<>();
		vr.addAll(vaccineReportImmunizationHistoryList);
		vaccineReportImmunizationHistoryList.clear();
		vaccineReportImmunizationHistoryList.addAll(vr);

		List<ImmunizationRecord> recordsList=new ArrayList<ImmunizationRecord>();
		for(int i=0;i<labDescImmunizationHistoryList.size();i++){


			if(labDescImmunizationHistoryList.get(i).getLabDescriptionTable()!=null&&labDescImmunizationHistoryList.get(i).getLabDescriptionTable().getLabDescriptionCvx()!=null){

				List<CvxVaccineGroupMapping> cvxGroupList=cvxVaccineGroupRepository.findAll(PortalImmunizationHistorySpecification.getVaccineGroupMappings(labDescImmunizationHistoryList.get(i).getLabDescriptionTable().getLabDescriptionCvx()));
				for(int c=0;c<cvxGroupList.size();c++){

					ImmunizationRecord record=new ImmunizationRecord();
					
					if(labDescImmunizationHistoryList.get(i).getLabEntriesIsBillable()!=null)
						record.setBillable(labDescImmunizationHistoryList.get(i).getLabEntriesIsBillable());
					else
						record.setBillable(false);

					if(cvxGroupList.get(c)!=null&&
							cvxGroupList.get(c).getCvxVaccineGroupMappingVaccineGroupCode()!=null)
						record.setVaccineGroup(cvxGroupList.get(c).getVaccineGroupMapping().getVaccineGroup());
					
					if(cvxGroupList.get(c)!=null&&
							cvxGroupList.get(c).getVaccineGroupMapping().getVaccineGroupCvx()!=null)
						record.setVaccineCvx(cvxGroupList.get(c).getVaccineGroupMapping().getVaccineGroupCvx());
					
					if(cvxGroupList.get(c)!=null&&
							cvxGroupList.get(c).getVaccineGroupMapping().getVaccineGroupCvx()!=null)
						record.setVaccineSortOrder(cvxGroupList.get(c).getVaccineGroupMapping().getVaccineGroupSortOrder());

					if(labDescImmunizationHistoryList.get(i)!=null)
						record.setVaccineId(labDescImmunizationHistoryList.get(i).getLabEntriesTestId());

					if(labDescImmunizationHistoryList.get(i).getLabEntriesTestDesc()!=null)
						record.setVaccineName(labDescImmunizationHistoryList.get(i).getLabEntriesTestDesc());

					if(labDescImmunizationHistoryList.get(i).getLabEntriesPerfOn()!=null)
						record.setVaccinationPerformedOn(labDescImmunizationHistoryList.get(i).getLabEntriesPerfOn());

					if(labDescImmunizationHistoryList.get(i).getLabPerformedBy()!=null&&
							labDescImmunizationHistoryList.get(i).getLabPerformedBy().getEmpProfileEmpid()!=null)
						record.setVaccinationPerformedById(labDescImmunizationHistoryList.get(i).getLabPerformedBy().getEmpProfileEmpid());

					if(labDescImmunizationHistoryList.get(i).getLabPerformedBy()!=null&&
							labDescImmunizationHistoryList.get(i).getLabPerformedBy().getEmpProfileFullname()!=null)
						record.setVaccinationPerformedByName(labDescImmunizationHistoryList.get(i).getLabPerformedBy().getEmpProfileFullname());

					if(labDescImmunizationHistoryList.get(i).getLabEntriesLabSite()!=null)
						record.setVaccinationPerformedSite(labDescImmunizationHistoryList.get(i).getLabEntriesLabSite());

					if(labDescImmunizationHistoryList.get(i).getLabEntriesTestStatus()!=null)
						record.setVaccinationStatus(labDescImmunizationHistoryList.get(i).getLabEntriesTestStatus());

					if(labDescImmunizationHistoryList.get(i).getVaccineOrderDetails()!=null)
					{
						if(labDescImmunizationHistoryList.get(i).getVaccineOrderDetails().getVaccineOrderDetailsExpiry()!=null)
							record.setVaccineExpDate(labDescImmunizationHistoryList.get(i).getVaccineOrderDetails().getVaccineOrderDetailsExpiry());

						if(labDescImmunizationHistoryList.get(i).getVaccineOrderDetails().getVaccineOrder()!=null&&
								labDescImmunizationHistoryList.get(i).getVaccineOrderDetails().getVaccineOrder().getVaccineManDetails()!=null&&
								labDescImmunizationHistoryList.get(i).getVaccineOrderDetails().getVaccineOrder().getVaccineManDetails().getVaccineManDetailsName()!=null)
							record.setVaccineMfg(labDescImmunizationHistoryList.get(i).getVaccineOrderDetails().getVaccineOrder().getVaccineManDetails().getVaccineManDetailsName());

						if(labDescImmunizationHistoryList.get(i).getVaccineOrderDetails()!=null&&
								labDescImmunizationHistoryList.get(i).getVaccineOrderDetails().getVaccineOrderDetailsLotNo()!=null)
							record.setVaccineLotNo(labDescImmunizationHistoryList.get(i).getVaccineOrderDetails().getVaccineOrderDetailsLotNo());
					}

					recordsList.add(record);
				}

			}

		}

		
		for(int i=0;i<vaccineReportImmunizationHistoryList.size();i++){
			
			if(vaccineReportImmunizationHistoryList.get(i).getLabDescriptionTable()!=null){

				if(vaccineReportImmunizationHistoryList.get(i).getLabDescriptionTable().getLabDescriptionCvx()!=null){
					List<CvxVaccineGroupMapping> cvxGroupList=cvxVaccineGroupRepository.findAll(PortalImmunizationHistorySpecification.getVaccineGroupMappings(vaccineReportImmunizationHistoryList.get(i).getLabDescriptionTable().getLabDescriptionCvx()));
					for(int c=0;c<cvxGroupList.size();c++){

						ImmunizationRecord record=new ImmunizationRecord();
						
						
					     record.setBillable(false);

						if(cvxGroupList.get(c)!=null&&
								cvxGroupList.get(c).getCvxVaccineGroupMappingVaccineGroupCode()!=null)
							record.setVaccineGroup(cvxGroupList.get(c).getVaccineGroupMapping().getVaccineGroup());
						
						if(cvxGroupList.get(c)!=null&&
								cvxGroupList.get(c).getVaccineGroupMapping().getVaccineGroupCvx()!=null)
							record.setVaccineCvx(cvxGroupList.get(c).getVaccineGroupMapping().getVaccineGroupCvx());
						
						if(cvxGroupList.get(c)!=null&&
								cvxGroupList.get(c).getVaccineGroupMapping().getVaccineGroupCvx()!=null)
							record.setVaccineSortOrder(cvxGroupList.get(c).getVaccineGroupMapping().getVaccineGroupSortOrder());

						if(vaccineReportImmunizationHistoryList.get(i).getLabDescriptionTable().getLabDescriptionTestDesc()!=null)
							record.setVaccineName(vaccineReportImmunizationHistoryList.get(i).getLabDescriptionTable().getLabDescriptionTestDesc());

						if(vaccineReportImmunizationHistoryList.get(i).getLabDescriptionTable().getLabDescriptionTestid()!=null)
							record.setVaccineId(vaccineReportImmunizationHistoryList.get(i).getLabDescriptionTable().getLabDescriptionTestid());

						if(vaccineReportImmunizationHistoryList.get(i).getVaccineReportGivenDate()!=null)
							record.setVaccinationPerformedOn(vaccineReportImmunizationHistoryList.get(i).getVaccineReportGivenDate());

						if(vaccineReportImmunizationHistoryList.get(i).getEmployeeProfile()!=null){

							if(vaccineReportImmunizationHistoryList.get(i).getEmployeeProfile().getEmpProfileEmpid()!=null)
								record.setVaccinationPerformedById(vaccineReportImmunizationHistoryList.get(i).getEmployeeProfile().getEmpProfileEmpid());

							if(vaccineReportImmunizationHistoryList.get(i).getEmployeeProfile().getEmpProfileFullname()!=null)
								record.setVaccinationPerformedByName(vaccineReportImmunizationHistoryList.get(i).getEmployeeProfile().getEmpProfileFullname());

							recordsList.add(record);
						}
					}
				}
			}


		}

		/*immunizationHistoryBean.setLabDescImmunizationHistoryList(labDescImmunizationHistoryList);
		immunizationHistoryBean.setVaccineReportImmunizationHistoryList(vaccineReportImmunizationHistoryList);*/

		return recordsList;
	}

	public static Pageable createLabDescImmunizationHitoryRequestByDescDate(int pageIndex, int offset) {

		return new PageRequest(pageIndex, offset, Sort.Direction.DESC,"labEntriesPerfOn");
	}

	public static Pageable createVaccReportImmunizationHitoryRequestByDescDate(int pageIndex, int offset) {

		return new PageRequest(pageIndex, offset, Sort.Direction.DESC,"vaccineReportGivenDate");
	}

	/*@Override
	public List<Vaccine> getVaccineList(String searchKey, int pageOffset, int index) {

		List<LabDescription> resultist=labDescriptionRepository.findAll(PortalImmunizationHistorySpecification.getVaccineListBySearchKey(searchKey));

		List<Vaccine> vaccineList=new ArrayList<Vaccine>();

		for(int i=0;i<resultist.size();i++){

			Vaccine vaccine=new Vaccine();
			vaccine.setVaccineId(resultist.get(i).getLabDescriptionTestid());
			vaccine.setVaccineName(resultist.get(i).getLabDescriptionTestDesc());
		}

		return vaccineList;
	}*/


	public List<Vaccine> getVaccineList(String searchKey) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<LabDescription> root = cq.from(LabDescription.class);
		cq.multiselect(builder.construct(Vaccine.class, root.get(LabDescription_.labDescriptionTestid), root.get(LabDescription_.labDescriptionTestDesc), root.get(LabDescription_.labDescriptionCvx)));
		cq.where(builder.equal(root.get(LabDescription_.labDescriptionGroupid), 36), 
				builder.like(builder.upper(root.get(LabDescription_.labDescriptionTestDesc)), "%"+searchKey.toUpperCase()+"%")).orderBy(builder.asc(root.get(LabDescription_.labDescriptionTestDesc)));

		List<Object> resultList= em.createQuery(cq).getResultList();
		List<Vaccine> vaccineList = new ArrayList<Vaccine>();

		for(int i=0;i<resultList.size();i++){
			Vaccine vaccine=(Vaccine)resultList.get(i);
			vaccineList.add(vaccine);
		}

		return vaccineList;
	}

	@Override
	public List<H068> getVaccUpdateReasonList() {

		List<H068> vaccUpdateReasonList=h068Repository.findAll(PortalImmunizationHistorySpecification.getVaccineUpdateReasonList());

		return vaccUpdateReasonList;
	}

	@Override
	public List<Vis> getVaccineVISFilesList(String labDescCVX) {

		List<CvxVaccineGroupMapping> groupList=cvxVaccineGroupRepository.findAll(PortalImmunizationHistorySpecification.getVaccineGroupMappings(labDescCVX));

		List<Vis> visList=new ArrayList<Vis>();

		for(int i=0;i<groupList.size();i++){

			List<Vis> filesList=visRepository.findAll(PortalImmunizationHistorySpecification.getVaccineVISFilesList(groupList.get(i).getCvxVaccineGroupMappingVaccineGroupCode()));

			for(int j=0;j<filesList.size();j++)
				visList.add(filesList.get(j));
		}

		Set<Vis> hs = new HashSet<>();
		hs.addAll(visList);
		visList.clear();
		visList.addAll(hs);

		return visList;
	}

	@Override
	public PatientImmunizationInformation requestVaccineUpdate(VaccineUpdateBean vaccineUpdateBean) {

		PatientImmunizationInformation pii=new PatientImmunizationInformation();

		pii.setAdministratedOn(new Timestamp(vaccineUpdateBean.getAdministeredDate().getTime()));
		pii.setPatientId(vaccineUpdateBean.getPatientId());
		pii.setChartId(vaccineUpdateBean.getChartId());
		pii.setVaccineId(vaccineUpdateBean.getVaccineId());
		pii.setVaccineCvx(vaccineUpdateBean.getVaccineCvx());
		pii.setReasonId(vaccineUpdateBean.getUpdateReasonId());
		pii.setNotes(vaccineUpdateBean.getNotes());

		pii=patientImmunizationInformationRepository.saveAndFlush(pii);


		H810 paymentAlertCategory=h810Respository.findOne(PortalAlertSpecification.getPortalAlertCategoryByName("Clinical Intake"));
		boolean sendToAll =paymentAlertCategory.getH810005();  
		int provider = Integer.parseInt(paymentAlertCategory.getH810003());
		int forwardTo = Integer.parseInt(paymentAlertCategory.getH810004());
		
		AlertCategory alertCategory=alertCategoryRepository.findOne(AlertCategorySpecification.getAlertCategoryByName("Clinical Intake"));

		AlertEvent alert=new AlertEvent();
		alert.setAlertEventCategoryId(alertCategory.getAlertCategoryId());
		alert.setAlertEventStatus(1);
		alert.setAlertEventPatientId(vaccineUpdateBean.getPatientId());
		alert.setAlertEventPatientName(vaccineUpdateBean.getPatientName());
		alert.setAlertEventEncounterId(-1);
		alert.setAlertEventRefId(pii.getId());
		alert.setAlertEventMessage("Clinical Intake");
		alert.setAlertEventRoomId(-1);
		alert.setAlertEventCreatedDate(new Timestamp(new Date().getTime()));
		alert.setAlertEventModifiedby(-100);
		alert.setAlertEventFrom(-100);


		if(provider==0 && forwardTo==0)
			alert.setAlertEventTo(-1);
		else {
			if(sendToAll){
				if(forwardTo!=provider){
					alert.setAlertEventTo(forwardTo);
					AlertEvent alert2=alert;
					alert2.setAlertEventTo(provider);
					alertEventRepository.saveAndFlush(alert2);
				} else {
					alert.setAlertEventTo(forwardTo);
				}            	 
			}else{
				if(forwardTo!=0){
					alert.setAlertEventTo(forwardTo);
				} else {
					alert.setAlertEventTo(provider);
				}
			}
		}

		alertEventRepository.saveAndFlush(alert);

		return pii;
	}

}
