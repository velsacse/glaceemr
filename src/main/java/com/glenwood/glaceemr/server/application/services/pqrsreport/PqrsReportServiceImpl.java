package com.glenwood.glaceemr.server.application.services.pqrsreport;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.Bean.MacraProviderQDM;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.HttpConnectionUtils;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Patient;
import com.glenwood.glaceemr.server.application.Bean.macra.data.qdm.Request;
import com.glenwood.glaceemr.server.application.Bean.macra.ecqm.EMeasureUtils;
import com.glenwood.glaceemr.server.application.Bean.pqrs.Claim;
import com.glenwood.glaceemr.server.application.Bean.pqrs.MeasureStatus;
import com.glenwood.glaceemr.server.application.Bean.pqrs.PQRSResponse;
import com.glenwood.glaceemr.server.application.Bean.pqrs.ReportResponse;
import com.glenwood.glaceemr.server.application.models.PlaceOfService;
import com.glenwood.glaceemr.server.application.models.PqrsPatientEntries;
import com.glenwood.glaceemr.server.application.models.PqrsPatientEntries_;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresPatientEntries;
import com.glenwood.glaceemr.server.application.models.ServiceDetail;
import com.glenwood.glaceemr.server.application.models.ServiceDetail_;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.repositories.PqrsMeasureRepository;
import com.glenwood.glaceemr.server.application.repositories.PqrsPatientEntriesRepository;
import com.glenwood.glaceemr.server.application.repositories.ProblemListRepository;
import com.glenwood.glaceemr.server.application.repositories.QualityMeasuresPatientEntriesRepository;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.ExportQDM;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.QPPConfigurationService;
import com.glenwood.glaceemr.server.application.services.chart.print.TextFormatter;
import com.glenwood.glaceemr.server.application.specifications.QPPPerformanceSpecification;
import com.glenwood.glaceemr.server.utils.HUtil;

/**
 * @author Pratheeba
 *
 */

@Service
@Transactional
public class PqrsReportServiceImpl implements PqrsReportService{

	@Autowired
	PqrsMeasureRepository pqrsMeasureRepository;

	@PersistenceContext
	EntityManager em;

	@Autowired
	TextFormatter textFormat;

	RestTemplate restTemplate = new RestTemplate();

	@Autowired
	QualityMeasuresPatientEntriesRepository qualityMeasuresPatientEntriesRepository;

	@Autowired
	QPPConfigurationService providerConfService;

	@Autowired
	PqrsPatientEntriesRepository pqrspatiententriesrepository;

	@Autowired
	PatientRegistrationRepository patientInfoRepo;

	@Autowired
	ProblemListRepository diagnosisRepo;


	@Override
	public void getPatientServices(int providerId, int patientId,Date startDate,Date endDate ,String accountID)throws Exception{

		Patient patientrequestObj = new Patient();
		List<Claim>  claimList = new ArrayList<Claim>();
		List<ServiceDetail> serviceBean = new ArrayList<ServiceDetail>();
		Claim claimsingleBean = null;

		String pos = "", cpt = "",  dx1="", dx2="", dx3="", dx4="";
		String mod1="", mod2="", mod3="", mod4="";
		Date startdate = new Date();
		Date enddate = new Date();

		List<String> dxcodes = new ArrayList<String>();
		List<String> modifiers = new ArrayList<String>();

		List<com.glenwood.glaceemr.server.application.Bean.pqrs.Service> service = new ArrayList<com.glenwood.glaceemr.server.application.Bean.pqrs.Service>();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ServiceDetail> cquery = builder.createQuery(ServiceDetail.class);
		Root<ServiceDetail> roottable = cquery.from(ServiceDetail.class);

		Predicate predicate = null;

		Join<ServiceDetail, PlaceOfService> posjoin = roottable.join(ServiceDetail_.placeOfService,JoinType.INNER);

		predicate = builder.and(builder.greaterThanOrEqualTo(roottable.get(ServiceDetail_.serviceDetailDos), startDate),
				builder.lessThanOrEqualTo(roottable.get(ServiceDetail_.serviceDetailDos), endDate),
				builder.equal(roottable.get(ServiceDetail_.serviceDetailSdoctorid), providerId),
				builder.equal(roottable.get(ServiceDetail_.serviceDetailPatientid), patientId));

		cquery.where(predicate);
		cquery.orderBy(builder.desc(roottable.get(ServiceDetail_.serviceDetailDos)));

		serviceBean = em.createQuery(cquery).getResultList();
		
		if(serviceBean.size()>0){
			
			for(int i = 0;i<serviceBean.size();i++){
				
				service = new ArrayList<com.glenwood.glaceemr.server.application.Bean.pqrs.Service>();

				startdate = serviceBean.get(i).getServiceDetailDos();
				enddate = serviceBean.get(i).getServiceDetailDos();
				pos = serviceBean.get(i).getPlaceOfService().getPlaceOfServicePlaceName();
				cpt = serviceBean.get(i).getCpt().getCptCptcode();
				dx1 = HUtil.Nz(serviceBean.get(i).getServiceDetailDx1(),"");
				dx2 = HUtil.Nz(serviceBean.get(i).getServiceDetailDx2(),"");
				dx3 = HUtil.Nz(serviceBean.get(i).getServiceDetailDx3(),"");
				dx4 = HUtil.Nz(serviceBean.get(i).getServiceDetailDx4(),"");

				dxcodes = new ArrayList<String>();
				if(dx1 != null && !dx1.equals("")){		dxcodes.add(dx1);}
				if(dx2 != null && !dx2.equals("")){		dxcodes.add(dx2);}
				if(dx3 != null && !dx3.equals("")){		dxcodes.add(dx3);}
				if(dx4 != null && !dx4.equals("")){		dxcodes.add(dx4);}

				mod1 = serviceBean.get(i).getServiceDetailModifier1();
				mod2 = serviceBean.get(i).getServiceDetailModifier2();
				mod3 = serviceBean.get(i).getServiceDetailModifier3();
				mod4 = serviceBean.get(i).getServiceDetailModifier4();

				modifiers = new ArrayList<String>();
				if(mod1 != null && !mod1.equals("")){		modifiers.add(mod1);}
				if(mod2 != null && !mod2.equals("")){		modifiers.add(mod2);}
				if(mod3 != null && !mod3.equals("")){		modifiers.add(mod3);}
				if(mod4 != null && !mod4.equals("")){		modifiers.add(mod4);}

				com.glenwood.glaceemr.server.application.Bean.pqrs.Service serviceBeannew = new com.glenwood.glaceemr.server.application.Bean.pqrs.Service(cpt,modifiers,dxcodes);
				
				service.add(serviceBeannew);
				
				claimsingleBean = new Claim(startdate,enddate,providerId,pos,service);
				
				claimList.add(claimsingleBean);
				
			}

		}

		ExportQDM qdmData = new ExportQDM();
		EMeasureUtils utils = new EMeasureUtils();
		List<Integer> pqrsMeasures = new ArrayList<Integer>();	
		Request requestObj = new Request();
		PQRSResponse response = new PQRSResponse();

		String hub_url = "http://test.glaceemr.com/glacecds/ECQMServices/validateClaimsReport";

		List<MacraProviderQDM> providerInfo = providerConfService.getCompleteProviderInfo(providerId);

		if(providerInfo!=null){

			String[] measureIds = providerInfo.get(0).getMeasures().split(",");

			for(int i=0;i<measureIds.length;i++){
				pqrsMeasures.add(Integer.parseInt(measureIds[i]));
			}
			String data = "";

			requestObj.setAccountId(accountID);
			requestObj.setReportingYear(providerInfo.get(0).getMacraProviderConfigurationReportingYear());
			requestObj.setStartDate(providerInfo.get(0).getMacraProviderConfigurationReportingStart());
			requestObj.setEndDate(providerInfo.get(0).getMacraProviderConfigurationReportingEnd());
			requestObj.setMeasureIds(pqrsMeasures);
			patientrequestObj = qdmData.getRequestQDM(em,patientInfoRepo, diagnosisRepo, patientId, providerId);
			patientrequestObj.setClaims(claimList);
			requestObj.setPatient(patientrequestObj);
			ObjectMapper objectMapper = new ObjectMapper();
			String requestString = objectMapper.writeValueAsString(requestObj);
			String responseStr = HttpConnectionUtils.postData(hub_url, requestString, HttpConnectionUtils.HTTP_CONNECTION_MODE,"application/json");
			response = objectMapper.readValue(responseStr, PQRSResponse.class);
			response.getAccountId();
			response.getMeasureStatus();
			response.getPatientId();

			Set<Date> date ;
			TimeZone.setDefault(TimeZone.getTimeZone("EDT"));
			Date dos = new Date();
			String measureId = "";
			int performanceIndicator = -1;
			String reporting_year = "";
			int ptnid = -1;

			Map<String,MeasureStatus> measureStatus = new HashMap<String, MeasureStatus>();

			for(int i=0;i<response.getMeasureStatus().size();i++){
				date = response.getMeasureStatus().keySet();
				
				for(Date Key : date){
					
					dos = response.getMeasureStatus().get(Key).getVisitDate();
					measureStatus = response.getMeasureStatus().get(Key).getMeasureStatus();
					
					for(int j=0;j<measureStatus.size();j++){
						Set<String> measureidKey = measureStatus.keySet();
						for(String measureid : measureidKey){
							measureId = measureStatus.get(measureid).getMeasureId()+"";
						}
					}
					SimpleDateFormat parseFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
					Date dateModifedFormat = parseFormat.parse(dos.toString());
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
					String dos1 = format.format(dateModifedFormat);
					long patid = response.getPatientId();
					ptnid = (int) patid;
					reporting_year = providerInfo.get(0).getMacraProviderConfigurationReportingYear().toString();

					List<PqrsPatientEntries> pqrsentriesBean = new ArrayList<PqrsPatientEntries>();
					pqrsentriesBean = checkEntry(dos1,patid, providerId, measureId);

					PqrsPatientEntries pqrsptnEntry = new PqrsPatientEntries();
				
					
					if(pqrsentriesBean.size()>0){

						for(int index=0;index<pqrsentriesBean.size();index++){

							pqrsptnEntry = pqrsentriesBean.get(index);
							if(pqrsptnEntry.getPqrsPatientEntriesPerformanceIndicator() != null)
							{
							performanceIndicator = pqrsptnEntry.getPqrsPatientEntriesPerformanceIndicator();
							}
							
						}

					}
				}
			}

			PqrsPatientEntries pqrsptnEntrynew = new PqrsPatientEntries();

			if(response.getMeasureStatus().size()>0){
				
			Map<String, MeasureStatus> measureStatusPqrs = response.getMeasureStatus().get(response.getMeasureStatus().keySet().toArray()[0]).getMeasureStatus();

			int denominator = 0;
			MeasureStatus patientObj = new MeasureStatus();
			
			if(measureStatusPqrs.size() >0){
			
			 patientObj = measureStatus.get(measureStatusPqrs.keySet().toArray()[0]);
			 denominator = patientObj.getDenominator();
			 
			}

			if(denominator == 1)
			{
				if(performanceIndicator != -1)
					pqrsptnEntrynew.setPqrsPatientEntriesPerformanceIndicator(performanceIndicator);
			}

			makeEntry(response, ptnid, providerId, measureId, reporting_year, pqrsptnEntrynew, denominator );
			}
		}

	}

	public  List<PqrsPatientEntries> checkEntry(String dos,long patid, Integer providerId,String measureId) {

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		List<PqrsPatientEntries> pqrsentriesBean = new ArrayList<PqrsPatientEntries>();

		try{
			Date startDate=formatter.parse(dos);
			HashMap<Integer, ReportResponse> finalResponse = new HashMap<Integer, ReportResponse>();
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<PqrsPatientEntries> query = builder.createQuery(PqrsPatientEntries.class);
			Root<PqrsPatientEntries> root = query.from(PqrsPatientEntries.class);
			Predicate predicate;
			Expression<Date> dosExpr=builder.function("date", Date.class, root.get(PqrsPatientEntries_.pqrsPatientEntriesDos));
			predicate = builder.and(builder.equal(dosExpr, startDate),
					builder.equal(root.get(PqrsPatientEntries_.pqrsPatientEntriesMeasureId), measureId),
					builder.equal(root.get(PqrsPatientEntries_.pqrsPatientEntriesProviderId), providerId),
					builder.equal(root.get(PqrsPatientEntries_.pqrsPatientEntriesIsActive), true));

			if(patid != 0){
				predicate =	builder.and(predicate,(builder.equal(root.get(PqrsPatientEntries_.pqrsPatientEntriesPatientId), patid)));}

			query.where(predicate);
			query.orderBy(builder.desc(root.get(PqrsPatientEntries_.pqrsPatientEntriesDos)));
			pqrsentriesBean = em.createQuery(query).getResultList();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return pqrsentriesBean;

	}

	public void makeEntry(PQRSResponse  response,int patientId, Integer providerId,String measureId,String reportYear,PqrsPatientEntries pqrsptnEntry, int denominator) {

		Date d = new Date();

		Timestamp curr_time = new Timestamp(d.getTime());

		for(int index = 0;index<response.getMeasureStatus().size();index++){

			Map<String, MeasureStatus> measureStatus = response.getMeasureStatus().get(response.getMeasureStatus().keySet().toArray()[0]).getMeasureStatus();

			for(int i=0;i<measureStatus.size();i++){

				MeasureStatus patientObj = measureStatus.get(measureStatus.keySet().toArray()[i]);

				QualityMeasuresPatientEntries patientData = qualityMeasuresPatientEntriesRepository.findOne(Specifications.where(QPPPerformanceSpecification.isPatientExisting(providerId, measureId, patientId, reportYear, patientObj.getCriteria())));


				if(patientData == null || patientData.equals(null)){
					patientData = new QualityMeasuresPatientEntries();

					patientData.setQualityMeasuresPatientEntriesPatientId(patientId);
					patientData.setQualityMeasuresPatientEntriesMeasureId(measureId);
					patientData.setQualityMeasuresPatientEntriesCriteria(patientObj.getCriteria());
					patientData.setQualityMeasuresPatientEntriesProviderId(providerId);
					patientData.setQualityMeasuresPatientEntriesReportingYear(Integer.parseInt(reportYear));
					patientData.setQualityMeasuresPatientEntriesUpdatedOn(curr_time);

					if(denominator == 1){

						patientData.setQualityMeasuresPatientEntriesDenominator(1);
						patientData.setQualityMeasuresPatientEntriesIpp(1);
						
						if(pqrsptnEntry.getPqrsPatientEntriesPerformanceIndicator() != null && pqrsptnEntry.getPqrsPatientEntriesPerformanceIndicator() != -1){

							if(pqrsptnEntry.getPqrsPatientEntriesPerformanceIndicator() == 1){
								
								patientData.setQualityMeasuresPatientEntriesNumerator(1);
								patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorException(0);
								
							}else if(pqrsptnEntry.getPqrsPatientEntriesPerformanceIndicator() == 2){
								
								patientData.setQualityMeasuresPatientEntriesNumerator(0);
								patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(1);
								patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorException(0);
								
							}else if(pqrsptnEntry.getPqrsPatientEntriesPerformanceIndicator() == 3){
								
								patientData.setQualityMeasuresPatientEntriesNumerator(0);
								patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(1);
								patientData.setQualityMeasuresPatientEntriesDenominatorException(0);
								
							}else if(pqrsptnEntry.getPqrsPatientEntriesPerformanceIndicator() == 4){
								
								patientData.setQualityMeasuresPatientEntriesNumerator(0);
								patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorException(1);
								
							}else{
								
								patientData.setQualityMeasuresPatientEntriesNumerator(0);
								patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorException(0);
								
							}

						}else{
							
							patientData.setQualityMeasuresPatientEntriesNumerator(0);
							patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(0);
							patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(0);
							patientData.setQualityMeasuresPatientEntriesDenominatorException(0);
						}

					}else{
						patientData.setQualityMeasuresPatientEntriesDenominator(0);
						patientData.setQualityMeasuresPatientEntriesIpp(0);
						patientData.setQualityMeasuresPatientEntriesNumerator(0);
						patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(0);
						patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(0);
						patientData.setQualityMeasuresPatientEntriesDenominatorException(0);
					}

					patientData.setQualityMeasuresPatientEntriesMeasurePopulation(patientObj.getMeasurePopulation());
					patientData.setQualityMeasuresPatientEntriesMeasurePopulationExclusion(patientObj.getMeasurePopulationExclusion());
					patientData.setQualityMeasuresPatientEntriesMeasureObservation(new Double(patientObj.getMeasureObservation()).intValue());

					qualityMeasuresPatientEntriesRepository.saveAndFlush(patientData);

				}else{
					patientData.setQualityMeasuresPatientEntriesUpdatedOn(curr_time);
					patientData.setQualityMeasuresPatientEntriesIpp(patientObj.getIpp());

					if(denominator == 1){

						patientData.setQualityMeasuresPatientEntriesDenominator(1);
						patientData.setQualityMeasuresPatientEntriesIpp(1);
						
						if(pqrsptnEntry.getPqrsPatientEntriesPerformanceIndicator() != null && pqrsptnEntry.getPqrsPatientEntriesPerformanceIndicator() != -1){

							if(pqrsptnEntry.getPqrsPatientEntriesPerformanceIndicator() == 1){
								
								patientData.setQualityMeasuresPatientEntriesNumerator(1);
								patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorException(0);
								
							}else if(pqrsptnEntry.getPqrsPatientEntriesPerformanceIndicator() == 2){
								
								patientData.setQualityMeasuresPatientEntriesNumerator(0);
								patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(1);
								patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorException(0);
								
							}else if(pqrsptnEntry.getPqrsPatientEntriesPerformanceIndicator() == 3){
								
								patientData.setQualityMeasuresPatientEntriesNumerator(0);
								patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(1);
								patientData.setQualityMeasuresPatientEntriesDenominatorException(0);
								
							}else if(pqrsptnEntry.getPqrsPatientEntriesPerformanceIndicator() == 4){
								
								patientData.setQualityMeasuresPatientEntriesNumerator(0);
								patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorException(1);
								
							}else{
								
								patientData.setQualityMeasuresPatientEntriesNumerator(0);
								patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(0);
								patientData.setQualityMeasuresPatientEntriesDenominatorException(0);
								
							}

						}else{
							
							patientData.setQualityMeasuresPatientEntriesNumerator(0);
							patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(0);
							patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(0);
							patientData.setQualityMeasuresPatientEntriesDenominatorException(0);
						}

					}else{
						patientData.setQualityMeasuresPatientEntriesDenominator(0);
						patientData.setQualityMeasuresPatientEntriesIpp(0);
						patientData.setQualityMeasuresPatientEntriesNumerator(0);
						patientData.setQualityMeasuresPatientEntriesNumeratorExclusion(0);
						patientData.setQualityMeasuresPatientEntriesDenominatorExclusion(0);
						patientData.setQualityMeasuresPatientEntriesDenominatorException(0);
					}

					patientData.setQualityMeasuresPatientEntriesMeasurePopulation(patientObj.getMeasurePopulation());
					patientData.setQualityMeasuresPatientEntriesMeasurePopulationExclusion(patientObj.getMeasurePopulationExclusion());
					patientData.setQualityMeasuresPatientEntriesMeasureObservation(new Double(patientObj.getMeasureObservation()).intValue());

					qualityMeasuresPatientEntriesRepository.saveAndFlush(patientData);
				}

			}

		}
	}

}