package com.glenwood.glaceemr.server.application.services.chart.print.genericheader;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.print.GenericLetterHeader;
import com.glenwood.glaceemr.server.application.models.print.LetterHeaderContent;
import com.glenwood.glaceemr.server.application.services.employee.EmployeeDataBean;
import com.glenwood.glaceemr.server.application.services.pos.PosDataBean;

public interface LetterHeaderService {
	
	List<GenericLetterHeader> getLetterHeaderList();
	GenericLetterHeader getLetterHeaderDetails(int letterHeaderId);
	List<LetterHeaderContent> getLetterHeaderContentList(int letterHeaderId);
	GenericLetterHeader saveLetterHeader(GenericLetterHeader genericLetterHeader);
	void deleteLetterHeaderContent(List<LetterHeaderContent> letterHeaderContentList);
	void saveLetterHeaderContent(LetterHeaderContent letterHeaderContent);
	List<EmployeeDataBean> getEmployeeDetails() throws Exception;
	List<PosDataBean> getPOSDetails() throws Exception;

}
