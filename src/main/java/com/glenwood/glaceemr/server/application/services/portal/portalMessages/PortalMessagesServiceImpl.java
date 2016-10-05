package com.glenwood.glaceemr.server.application.services.portal.portalMessages;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.AlertCategory;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.H213;
import com.glenwood.glaceemr.server.application.models.H810;
import com.glenwood.glaceemr.server.application.models.PortalMessage;
import com.glenwood.glaceemr.server.application.models.PortalMessageBean;
import com.glenwood.glaceemr.server.application.models.PortalMessageThreadBean;
import com.glenwood.glaceemr.server.application.models.PortalMessage_;
import com.glenwood.glaceemr.server.application.models.PortalPatientPaymentsSummary;
import com.glenwood.glaceemr.server.application.repositories.AlertCategoryRepository;
import com.glenwood.glaceemr.server.application.repositories.AlertEventRepository;
import com.glenwood.glaceemr.server.application.repositories.H810Respository;
import com.glenwood.glaceemr.server.application.repositories.PortalMessageRepository;
import com.glenwood.glaceemr.server.application.specifications.AlertCategorySpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalAlertEventSpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalAlertSpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalMessagesSpecification;
import com.glenwood.glaceemr.server.application.specifications.PortalSettingsSpecification;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;


@Service
public class PortalMessagesServiceImpl implements PortalMessagesService{

	@Autowired
	PortalMessageRepository portalMessageRepository;

	@Autowired
	AlertEventRepository alertEventRepository;
	
	@Autowired
	AlertCategoryRepository alertCategoryRepository;
	
	@Autowired 
	H810Respository h810Respository;

	@Autowired
	EntityManager em;

	@Autowired
	EntityManagerFactory emf;

	@Override
	public PortalMessagesDetailsBean getMessagesDetailsByPatientId(int patientId) {

		PortalMessagesDetailsBean portalMessagesDetailsBean=new PortalMessagesDetailsBean();
		
		int totalMessageThreads=getPortalMessageThreadsListSize(patientId);

		long totalMessages =portalMessageRepository.count(PortalMessagesSpecification.getTotalMessagesByPatientId(patientId));

		long totalInboxMessages=portalMessageRepository.count(PortalMessagesSpecification.getInboxMessagesListByPatientId(patientId));

		long totalSentMessages=portalMessageRepository.count(PortalMessagesSpecification.getSentMessagesListByPatientId(patientId));

		portalMessagesDetailsBean.setTotalMessageThreads(totalMessageThreads);
		
		portalMessagesDetailsBean.setTotalMessages(totalMessages);

		portalMessagesDetailsBean.setTotalInboxMessages(totalInboxMessages);

		portalMessagesDetailsBean.setTotalSentMessages(totalSentMessages);


		return portalMessagesDetailsBean;

	}

	@Override
	public List<PortalMessage> getMessagesByPatientId(int patientId, int offset, int pageIndex) {

		List<PortalMessage> patientMessagesList=portalMessageRepository.findAll(PortalMessagesSpecification.getTotalMessagesByPatientId(patientId),PortalMessagesSpecification.createPortalMessagesPageRequestByDescDate(pageIndex, offset)).getContent();
		return patientMessagesList;
	}

	@Override
	public List<PortalMessageThreadBean> getPatientMessageThreadsByPatientId(int patientId, int offset, int pageIndex) {

		List<PortalMessageThreadBean> threadList=new ArrayList<PortalMessageThreadBean>();

		List<Integer> threadIdList=getPortalMessageThreadsList(patientId, pageIndex, offset);
		for(int i=0;i<threadIdList.size();i++){
			
			PortalMessageThreadBean threadBean=new PortalMessageThreadBean();
			List<PortalMessage> thread=portalMessageRepository.findAll(PortalMessagesSpecification.getMessagesThreadByParentId(threadIdList.get(i)));
			threadBean.setMessageThread(thread);
			threadBean.setPatientId(patientId);
			threadList.add(threadBean);
		}

		return threadList;
	}
	
	public Integer getPortalMessageThreadsListSize(int patientId){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<PortalMessage> root = cq.from(PortalMessage.class);
		cq.select(root.get(PortalMessage_.parentid)).where(builder.equal(root.get(PortalMessage_.patientid), patientId)).orderBy(builder.desc(root.get(PortalMessage_.parentid))).distinct(true);
		List<Object> resultList=em.createQuery(cq).getResultList();
		return resultList.size();
	}


	public List<Integer> getPortalMessageThreadsList(int patientId, int index, int offset){

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<PortalMessage> root = cq.from(PortalMessage.class);
		cq.select(root.get(PortalMessage_.parentid)).where(builder.equal(root.get(PortalMessage_.patientid), patientId)).orderBy(builder.desc(root.get(PortalMessage_.parentid))).distinct(true);

		List<Object> resultList=em.createQuery(cq).getResultList();
		
		int firstThreadIndex=offset*(index);
		int lastThreadIndex=(offset*(index+1))-1;
				
		List<Integer> threadIdList = new ArrayList<Integer>();
		
		if(lastThreadIndex>=resultList.size())
			lastThreadIndex=resultList.size()-1;
		for(int i=firstThreadIndex;i<=lastThreadIndex;i++){
			threadIdList.add((Integer)resultList.get(i));
		}
		
		return threadIdList;
	}

	@Override
	public List<PortalMessage> getInboxMessagesByPatientId(int patientId, int offset, int pageIndex) {

		List<PortalMessage> patientInboxMessagesList=portalMessageRepository.findAll(PortalMessagesSpecification.getInboxMessagesListByPatientId(patientId),PortalMessagesSpecification.createPortalMessagesPageRequestByDescDate(pageIndex, offset)).getContent();

		return patientInboxMessagesList;

	}

	@Override
	public List<PortalMessage> getSentMessagesByPatientId(int patientId, int offset, int pageIndex) {

		List<PortalMessage> patientSentMessagesList=portalMessageRepository.findAll(PortalMessagesSpecification.getSentMessagesListByPatientId(patientId),PortalMessagesSpecification.createPortalMessagesPageRequestByDescDate(pageIndex, offset)).getContent();

		return patientSentMessagesList;

	}

	@Override
	public EMRResponseBean deletePortalMessageByMessageId(int patientId,int messageId) {
		portalMessageRepository.delete(portalMessageRepository.findAll(PortalMessagesSpecification.deleteMessageById(messageId)));	

		EMRResponseBean responseBean=new EMRResponseBean();
		responseBean.setLogin(true);
		responseBean.setCanUserAccess(true);
		responseBean.setSuccess(true);
		responseBean.setIsAuthorizationPresent(true);

		return responseBean;
	}
	
	@Override
	public EMRResponseBean deletePortalMessageThread(int patientId, int threadId) {
		
		portalMessageRepository.delete(portalMessageRepository.findAll(PortalMessagesSpecification.deleteMessageThreadById(patientId, threadId)));
		
		EMRResponseBean responseBean=new EMRResponseBean();
		responseBean.setLogin(true);
		responseBean.setCanUserAccess(true);
		responseBean.setSuccess(true);
		responseBean.setIsAuthorizationPresent(true);
		
		return null;
	}

	@Override
	public EMRResponseBean saveNewMessage(PortalMessageBean portalMessageBean) {

		System.out.println("In SaveNewMessage");
		
		H810 paymentAlertCategory=h810Respository.findOne(PortalAlertSpecification.getPortalAlertCategoryByName("Message Request"));
		boolean sendToAll =paymentAlertCategory.getH810005();  
        int provider = Integer.parseInt(paymentAlertCategory.getH810003());
        int forwardTo = Integer.parseInt(paymentAlertCategory.getH810004());
        
		AlertCategory alertCategory=alertCategoryRepository.findOne(AlertCategorySpecification.getAlertCategoryByName("Message from Patient Portal"));
        
		AlertEvent alertEventBean=new AlertEvent();
		alertEventBean.setAlertEventCategoryId(alertCategory.getAlertCategoryId());
		alertEventBean.setAlertEventStatus(1);
		alertEventBean.setAlertEventChartId(portalMessageBean.getChartid());
		alertEventBean.setAlertEventPatientId(portalMessageBean.getPatientid());
		alertEventBean.setAlertEventPatientName(portalMessageBean.getMessageSender());
		alertEventBean.setAlertEventEncounterId(-1);
		alertEventBean.setAlertEventHighlight(true);
		alertEventBean.setAlertEventMessage("Message from Patient Portal.");
		alertEventBean.setAlertEventRoomId(-1);
		alertEventBean.setAlertEventCreatedDate(new Timestamp(new Date().getTime()));
		alertEventBean.setAlertEventModifiedDate(new Timestamp(new Date().getTime()));
		alertEventBean.setAlertEventModifiedby(portalMessageBean.getMessasgeBy());
		alertEventBean.setAlertEventFrom(-100);
		alertEventBean.setAlertEventRefId(getNewPortalMessageAlertId());
		alertEventBean.setAlertEventParentalertid(-1);
				
		
		if(provider==0 && forwardTo==0)
			alertEventBean.setAlertEventTo(-1);
        else {
          if(sendToAll){
         	 if(forwardTo!=provider){
         		alertEventBean.setAlertEventTo(forwardTo);
         		AlertEvent alert2=alertEventBean;
         		alert2.setAlertEventTo(provider);
         		alertEventRepository.saveAndFlush(alert2);
         	 } else {
         		alertEventBean.setAlertEventTo(forwardTo);
         	 }            	 
          }else{
         	 if(forwardTo!=0){
         		alertEventBean.setAlertEventTo(forwardTo);
         	 } else {
         		alertEventBean.setAlertEventTo(provider);
         	}
          }
        }
		
		alertEventBean=alertEventRepository.saveAndFlush(alertEventBean);
		
		PortalMessage portalMessage=new PortalMessage();

		portalMessage.setIsactive(portalMessageBean.getIsActive());
		portalMessage.setMdate(Timestamp.valueOf(portalMessageBean.getMessageDate()));
		portalMessage.setMessage(portalMessageBean.getMessage());
		portalMessage.setMessageBy(portalMessageBean.getMessasgeBy());
		portalMessage.setMessageTo(1);
		
		if(portalMessageBean.getParentid()!=-1)
			portalMessage.setParentid(portalMessageBean.getParentid());
		else
			portalMessage.setParentid(getNewPortalMessageParentId());
		
		portalMessage.setPatientid(portalMessageBean.getPatientid());
		portalMessage.setPortalMessageAlertid(alertEventBean.getAlertEventRefId());
		portalMessage.setPortalMessageHasreplied(portalMessageBean.getIsPortalMessageReplied());
		portalMessage.setPortalMessageMapid(portalMessageBean.getMessageMapid());
		portalMessage.setPortalMessageReceiver(portalMessageBean.getMessageReceiver());
		portalMessage.setPortalMessageReply(portalMessageBean.getMessageReply());
		portalMessage.setPortalMessageSender(portalMessageBean.getMessageSender());
		portalMessage.setPortalMessageType(1);
		portalMessage.setStatus(portalMessageBean.getStatus());

		portalMessage=portalMessageRepository.saveAndFlush(portalMessage);
		
		EMRResponseBean responseBean=new EMRResponseBean();
		responseBean.setCanUserAccess(true);
		responseBean.setData("Success");
		responseBean.setIsAuthorizationPresent(true);
		responseBean.setLogin(true);
		responseBean.setSuccess(true);

		return responseBean;
	}
	
	public Integer getNewPortalMessageParentId() {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<PortalMessage> root = cq.from(PortalMessage.class);
		cq.select(builder.max(root.get(PortalMessage_.parentid)));
		Integer parentId=(Integer) em.createQuery(cq).getSingleResult();

		return parentId+1;
	}
	
	public Integer getNewPortalMessageAlertId() {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<PortalMessage> root = cq.from(PortalMessage.class);
		cq.select(builder.max(root.get(PortalMessage_.portalMessageAlertid)));
		Integer msgAlertId=(Integer) em.createQuery(cq).getSingleResult();

		return msgAlertId+1;
	}

}
