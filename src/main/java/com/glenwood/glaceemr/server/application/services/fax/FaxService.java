package com.glenwood.glaceemr.server.application.services.fax;

import java.util.List;
import com.glenwood.glaceemr.server.application.models.H491;
import com.glenwood.glaceemr.server.application.models.H496;
/**
 * Service Interface for Fax service
 * @author Anil Kumar
 *
 */
public interface FaxService {

	public List<H496> getOutboxDetails(Integer h496004, Integer userId,Integer pageNo);

	public List<FaxInboxBean> getInboxDetails(Integer h491010, Integer h491013,Integer h491014, Integer h491017_faxbox,int pageNo);

	public List<FaxFolderBean> getFaxFolderCount(Integer fax_location,Integer userId);

	public void deleteSelectFax(String faxId,String fileNames,Integer TRASH, Integer userId);

	public void deleteSelectOutboxFax(String faxId, Integer folderId,Integer userId) ;

	public List<FaxUsersBean> getFaxUserList();

	public List<H491> forwardFax(String faxId, String fileNames, Integer forwardTo,Integer folderId, Integer userId);

	public  List<H491> getInFaxDetails(Integer faxId, Integer userId, Integer faxTab, Integer faxFolder);

	public List<H496> getOutFaxDetails(Integer userId,Integer faxId, Integer faxTab, Integer faxFolder);

	public List<FaxSignListBean> getSignatureDetails(Integer empId);

	public  String lastFaxReceivedTime(Integer userId);

}