package com.glenwood.glaceemr.server.application.services.chart.print.genericheader;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.BillingConfigTable;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.PlaceOfService;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.PosType;
import com.glenwood.glaceemr.server.application.models.print.GenericLetterHeader;
import com.glenwood.glaceemr.server.application.models.print.LetterHeaderContent;
import com.glenwood.glaceemr.server.application.repositories.BillingConfigTableRepository;
import com.glenwood.glaceemr.server.application.repositories.EmpProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.PosTableRepository;
import com.glenwood.glaceemr.server.application.repositories.print.GenericLetterHeaderRepository;
import com.glenwood.glaceemr.server.application.repositories.print.LetterHeaderContentRepository;
import com.glenwood.glaceemr.server.application.services.chart.print.TextFormatter;
import com.glenwood.glaceemr.server.application.services.employee.EmployeeDataBean;
import com.glenwood.glaceemr.server.application.services.pos.PosDataBean;
import com.glenwood.glaceemr.server.application.specifications.BillingConfigTableSpecification;
import com.glenwood.glaceemr.server.application.specifications.EmployeeSpecification;
import com.glenwood.glaceemr.server.application.specifications.PosTableSpecification;
import com.glenwood.glaceemr.server.application.specifications.print.LetterHeaderSpecification;

@Service
public class LetterHeaderServiceImpl implements LetterHeaderService{
	
	@Autowired
	GenericLetterHeaderRepository genericHeaderRepository;
	
	@Autowired
	LetterHeaderContentRepository letterContentRepository;
	
	@Autowired
	EmpProfileRepository employeeProfileRepository;
	
	@Autowired
	PosTableRepository posTableRepository;
	
	@Autowired
	BillingConfigTableRepository billingConfigTableRepository;
	
	@Override
	public List<GenericLetterHeader> getLetterHeaderList() {
		List<GenericLetterHeader> genericHeaderList=genericHeaderRepository.findAll();
		return genericHeaderList;
	}

	@Override
	public List<LetterHeaderContent> getLetterHeaderContentList(int letterHeaderId) {
		List<LetterHeaderContent> letterHeaderContentList=letterContentRepository.findAll(LetterHeaderSpecification.getLetterHeaderContent(letterHeaderId));
		return letterHeaderContentList;
	}

	@Override
	public GenericLetterHeader saveLetterHeader(GenericLetterHeader genericLetterHeader) {
		GenericLetterHeader newgenericLetterHeader=genericHeaderRepository.save(genericLetterHeader);
		return newgenericLetterHeader;
	}

	@Override
	public void deleteLetterHeaderContent(List<LetterHeaderContent> letterHeaderContentList) {
		letterContentRepository.delete(letterHeaderContentList);
	}

	@Override
	public void saveLetterHeaderContent(LetterHeaderContent letterHeaderContent) {
		letterContentRepository.save(letterHeaderContent);
		
	}

	@Override
	public GenericLetterHeader getLetterHeaderDetails(int letterHeaderId) {
		GenericLetterHeader genericHeader=genericHeaderRepository.findOne(letterHeaderId);
		return genericHeader;
	}

	@Override
	public List<EmployeeDataBean> getEmployeeDetails() throws Exception {
		
		TextFormatter textFormat = new TextFormatter();

		List<EmployeeProfile> empList =  employeeProfileRepository.findAll(EmployeeSpecification.getUsersList("asc"));
	
		List<EmployeeDataBean> empolyeeBean = parseEmployeeDetails(empList, textFormat);

		return empolyeeBean;
				
	}
	@Override
	public List<PosDataBean> getPOSDetails() throws Exception {
		
		TextFormatter textFormat = new TextFormatter();
		List<PosTable> posList = posTableRepository.findAll(PosTableSpecification.getPOSDetails());
		
		List<PosDataBean> posBean = parsePOSDetails(posList, textFormat);
		
		return posBean;
				
	}
	/**
	 * Parsing Employee Details
	 * @param empList
	 * @param textFormat
	 * @return
	 * @throws Exception
	 */
	private List<EmployeeDataBean> parseEmployeeDetails(List<EmployeeProfile> empList, TextFormatter textFormat) throws Exception {
		
		List<EmployeeDataBean> empBean = new ArrayList<EmployeeDataBean>();
		
		for(int i=0; i<empList.size(); i++) {
			EmployeeProfile emp = empList.get(i);
			
			if(emp != null) {
				int empId = emp.getEmpProfileEmpid();
				int loginId = emp.getEmpProfileLoginid();
				String state = emp.getEmpProfileState();
				BillingConfigTable billing = billingConfigTableRepository.findOne(BillingConfigTableSpecification.getState(state));
				state = "";
				if(billing != null)
					state = billing.getBillingConfigTableLookupDesc();
				String fullName = textFormat.getFormattedName(emp.getEmpProfileFname(), emp.getEmpProfileMi(), emp.getEmpProfileLname(), emp.getEmpProfileCredentials());
				String fullAddress = textFormat.getAddress(emp.getEmpProfileAddress(), "", emp.getEmpProfileCity(), state, emp.getEmpProfileZip());

				empBean.add(new EmployeeDataBean(empId, loginId, fullName, fullAddress));
			}
		}
		
		return empBean;
	}

	/**
     * Parsing POS details
     * @param posList
     * @param textFormat
     * @return
     */
    private List<PosDataBean> parsePOSDetails(List<PosTable> posList, TextFormatter textFormat) {
       
        List<PosDataBean> posBean = new ArrayList<PosDataBean>();
        for(int i = 0; i < posList.size(); i++) {
            PosTable pos = posList.get(i);
            int relationId = pos.getPosTableRelationId();
            int placeId = pos.getPosTablePlaceId();
            String practice = pos.getPosTablePlaceOfService();
            String comments = pos.getPosTableFacilityComments();
            String name = "";
            if(practice != null && !practice.isEmpty())
                name = "(" + practice + ") ";
            if(comments != null && !comments.isEmpty())
                name = name + comments;
           
            String address = null, state = null, city = null, zip = null, phNum = null, faxNum = null;           
            PlaceOfService placeofService = pos.getPlaceOfService();
            if(placeofService != null) {
                state = placeofService.getPlaceOfServiceState();
                BillingConfigTable billing = billingConfigTableRepository.findOne(BillingConfigTableSpecification.getState(state));
                state = "";
                if(billing != null)
                    state = billing.getBillingConfigTableLookupDesc();
               
                address = placeofService.getPlaceOfServiceAddress();
                city = placeofService.getPlaceOfServiceCity();
                zip = placeofService.getPlaceOfServiceZip();
                phNum = placeofService.getPlaceOfServicePhone();
                faxNum = placeofService.getPlaceOfServiceFax();
            }
           
            String type = "";
            PosType posType = pos.getPosType();
            if(posType != null) {
                type = posType.getPosTypeTypeName();
            }
           
            posBean.add(new PosDataBean(relationId, placeId, name, address, state, city, zip, phNum, faxNum, type));
        }
        return posBean;
    }


}
