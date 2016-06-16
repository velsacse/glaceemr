package com.glenwood.glaceemr.server.application.services.chart.CNMSettings;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.glenwood.glaceemr.server.application.models.CnmSettings;
import com.glenwood.glaceemr.server.application.repositories.CnmSettingsRepository;



@Service
public class CNMSettingsServiceImpl implements CNMSettingsService{

	@Autowired
	CnmSettingsRepository cnmSettingsRepository;
	
	@Override
	public boolean getCNMSettingsIsActiveById(Integer id) {
		return getCNMSetting(cnmSettingsRepository.findAll(CNMSettingsSpecification.getCNMSettingsById(id)));
		
		
	}

	@Override
	public boolean getCNMSettingsIsActiveByName(String name) {
		return getCNMSetting(cnmSettingsRepository.findAll(CNMSettingsSpecification.getCNMSettingsByName(name)));
	}

	@Override
	public boolean getCNMSettingsIsActiveByIdAndName(Integer id, String name) {
		return getCNMSetting(cnmSettingsRepository.findAll(CNMSettingsSpecification.getCNMSettingsByIdAndName(id, name)));
	}
	
	
	private boolean getCNMSetting(List<CnmSettings> cnmSettings) {
		if(cnmSettings.size()>0){
			return cnmSettings.get(0).getCnmSettingsIsactive();
		}else{
			return false;
		}
		
		
	}
	
}
