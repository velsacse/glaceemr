package com.glenwood.glaceemr.server.application.services.rulesengine;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.Bean.MacraProviderQDM;
import com.glenwood.glaceemr.server.application.models.QualityMeasuresProviderMapping;
import com.glenwood.glaceemr.server.application.repositories.PqrsMeasureRepository;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.QPPConfigurationService;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.AjaxConnect;
import com.glenwood.glaceemr.server.application.services.pqrsreport.PqrsReportService;
import com.glenwood.glaceemr.server.application.specifications.PqrsSpecification;

@Service
@Transactional
public class GlaceRulesServiceImpl implements GlaceRulesService{

	@Autowired
	PqrsMeasureRepository pqrsMeasureRepository;
	
	@Autowired
	QPPConfigurationService providerConfService;
	
	@Autowired
	PqrsReportService pqrsReportService;

	@Override
	public String getMeasures(Integer providerId,Integer patientId,String accountId)throws Exception{
		String measureIds="", url="", temp="";
		List<QualityMeasuresProviderMapping> patiententries=null;
		AjaxConnect ajaxcall=new AjaxConnect();
		
		if(providerId!=null){
			patiententries=pqrsMeasureRepository.findAll(PqrsSpecification.getMeasurenumber(providerId));
		}

		for(int i=0;i<patiententries.size();i++)
		{
			measureIds=patiententries.get(i).getQualityMeasuresProviderMappingMeasureId();
			if(i!=0){
				temp=temp+","+measureIds;
			}
			else{
				temp=temp+measureIds;
			}
		}
		url=ajaxcall.sendGet("https://hub-icd10.glaceemr.com/DataGateway/PQRSServices/getPQRSMeasuresInfo?measureIds="+temp);
		
		/*List<MacraProviderQDM> providerInfo = providerConfService.getCompleteProviderInfo(providerId);

			Date startDate = providerInfo.get(0).getMacraProviderConfigurationReportingStart();
			Date EndDate = providerInfo.get(0).getMacraProviderConfigurationReportingEnd();
			int flag = 1;
		
			pqrsReportService.getPatientServices(providerId, patientId, startDate, EndDate, accountId,flag);*/

			return url;

	}
}