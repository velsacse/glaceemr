package com.glenwood.glaceemr.server.application.services.portal.portalMessages;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.PortalMessage;
import com.glenwood.glaceemr.server.application.models.PortalMessageBean;
import com.glenwood.glaceemr.server.application.models.PortalMessageThreadBean;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;


public interface PortalMessagesService {
	
	PortalMessagesDetailsBean getMessagesDetailsByPatientId(int patientId);

    List<PortalMessage> getInboxMessagesByPatientId(int patientId, int offset, int pageIndex);
    
    List<PortalMessage> getSentMessagesByPatientId(int patientId, int offset, int pageIndex);
    
    List<PortalMessage> getMessagesByPatientId(int patientId, int offset, int pageIndex);

    EMRResponseBean deletePortalMessageByMessageId(int patientId, int messageId);
    
    EMRResponseBean saveNewMessage(PortalMessageBean messageBean);

	List<PortalMessageThreadBean> getPatientMessageThreadsByPatientId(int patientId, int offset, int pageIndex);

	EMRResponseBean deletePortalMessageThread(int patientId, int threadId);
		
}
