package com.glenwood.glaceemr.server.application.services.rulesengine;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.Bean.MacraProviderQDM;
import com.glenwood.glaceemr.server.application.repositories.PqrsMeasureRepository;
import com.glenwood.glaceemr.server.application.services.chart.MIPS.QPPConfigurationService;
import com.glenwood.glaceemr.server.application.services.portal.portalSettings.AjaxConnect;
import com.glenwood.glaceemr.server.application.services.pqrsreport.PqrsReportService;

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
	public String getMeasures(Integer providerId,Integer patientId,Integer encounterYear)throws Exception{
		String url="", temp="";
		AjaxConnect ajaxcall=new AjaxConnect();
		try{
			List<MacraProviderQDM> providerInfo = providerConfService.getCompleteProviderInfo(providerId,encounterYear);

			if(providerInfo!=null){

				String[] measureIds = providerInfo.get(0).getMeasures().split(",");

				for(int i=0;i<measureIds.length;i++){
					if(i!=0){
						temp=temp+","+measureIds[i];
					}
					else{
						temp=temp+measureIds[i];
					}
				}
			}
			url=ajaxcall.sendGet("https://hub-icd10.glaceemr.com/DataGateway/PQRSServices/getPQRSMeasuresInfo?measureIds="+temp);

			/*Date startDate = providerInfo.get(0).getMacraProviderConfigurationReportingStart();
			Date EndDate = providerInfo.get(0).getMacraProviderConfigurationReportingEnd();
			int flag = 1;
			pqrsReportService.getPatientServices(providerId, patientId, startDate, EndDate, accountId,flag);*/

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
}