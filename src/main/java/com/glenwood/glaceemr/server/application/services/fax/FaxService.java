package com.glenwood.glaceemr.server.application.services.fax;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.DoctorSign;
import com.glenwood.glaceemr.server.application.models.H491;
import com.glenwood.glaceemr.server.application.models.H496;
/**
 * Servicw Interface for Fax service
 * @author Anil Kumar
 *
 */
public interface FaxService {

	public List<Object> getOutboxDetails(Integer h496004, Integer userId,Integer pageNo);

	public List<Object> getInboxDetails(Integer h491010, Integer h491013,Integer h491014, Integer h491017_faxbox,int pageNo);

	public List<FaxFolderBean> getFaxFolderCount(Integer fax_location,Integer userId);

	public void deleteSelectFax(String faxId, Integer TRASH, Integer userId);

	public void deleteSelectOutboxFax(String faxId, Integer folderID,Integer userId) ;

	public List<FaxuserlistBean> getForwardUserList(Integer empProfileGroupid);

	public List<H491> forwardFax(String faxId,  Integer forwardTO,Integer folderID, Integer userId);

	public List<H491> getInFaxDetails(Integer faxId, Integer userId, Integer faxTab, Integer faxFolder);

	public List<H496> getOutFaxDetails(Integer faxId, Integer faxTab, Integer faxFolder);

	public List<DoctorSign> getSignatureDetails(Integer empProfileLoginid);

}