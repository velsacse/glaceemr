package com.glenwood.glaceemr.server.application.services.fax;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.DoctorSign;
import com.glenwood.glaceemr.server.application.models.DoctorSign_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.H491;
import com.glenwood.glaceemr.server.application.models.H491_;
import com.glenwood.glaceemr.server.application.models.H496;
import com.glenwood.glaceemr.server.application.models.H496_;
import com.glenwood.glaceemr.server.application.repositories.DoctorsignRepository;
import com.glenwood.glaceemr.server.application.repositories.EmployeeProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.H491Repository;
import com.glenwood.glaceemr.server.application.repositories.H496Repository;
import com.glenwood.glaceemr.server.application.specifications.FaxSpecification;
/**
 * Service implementation for Fax Controller
 * @author Anil Kumar
 *
 */
@Service
public  class FaxServiceImpl implements FaxService{

	@Autowired
	EntityManager entityManager;

	@Autowired
	H496Repository h496Repository;

	@Autowired
	H491Repository h491Repository;

	@Autowired
	EmployeeProfileRepository employeeProfileRepository;

	@Autowired
	DoctorsignRepository doctorsignRepository;

	@Autowired
	EntityManagerFactory emf ;

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getOutboxDetails(Integer h496004, Integer userId,Integer pageNo){

		entityManager = emf.createEntityManager();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<H496> root = cq.from(H496.class);

		List<Object> getOutboxDetails=new ArrayList<Object>();
		//getOutboxDetails = h496Repository.findAll(FaxSpecification.getOutboxDetails(h496004,userId));
		try {
			cq.select(builder.construct(FaxOutboxBean.class, 
					root.get(H496_.h496001).alias("faxId"),
					root.get(H496_.h496002).alias("faxSubject"),
					root.get(H496_.h496003).alias("faxFileName"),
					root.get(H496_.h496004).alias("folderId"),
					root.get(H496_.h496006).alias("scheduleTime"),
					root.get(H496_.h496008).alias("statusId"),
					root.get(H496_.h496010).alias("recipientName"),
					root.get(H496_.h496012).alias("forwardedToUserId"),
					root.get(H496_.h496013).alias("createdBy"),
					root.get(H496_.h496014).alias("createdDate"),
					root.get(H496_.h496015).alias("billingCode"),
					root.get(H496_.h496019).alias("modifiedByUserId"),
					root.get(H496_.h496022).alias("senderName"),
					root.get(H496_.h496Faxpngfiles).alias("outboxThumbnail"),
					root.get(H496_.h496Displaynumber).alias("recipientNumber"),
					root.get(H496_.h496Attachment).alias("faxAttachments")
					)).where(FaxSpecification.getFaxOutboxDetails(h496004, userId, root, cq, builder));
			cq.orderBy(builder.desc(root.get(H496_.h496006)));
			Query query = entityManager.createQuery(cq);
			if(pageNo == 1){
				getOutboxDetails = query.setFirstResult(0).setMaxResults(12).getResultList();
			}else{
				getOutboxDetails = query.setFirstResult( (pageNo-1)*12 ).setMaxResults(12).getResultList();
			}
			
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		finally {
			//entityManager.close();
		}
		return getOutboxDetails;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getInboxDetails(Integer h491010, Integer h491013,Integer h491014, Integer h491017_faxbox,int pageNo) {
		entityManager = emf.createEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<H491> root = cq.from(H491.class);

		List<Object> getInboxDetails=new ArrayList<Object>();
		//getInboxDetails = h491Repository.findAll(FaxSpecification.getInboxDetails(h491010, h491013,h491014,h491017_faxbox));
		try{
			cq.select(builder.construct(FaxInboxBean.class, 
					root.get(H491_.h491001).alias("faxId"),
					root.get(H491_.h491006).alias("transStationId"),
					root.get(H491_.h491010).alias("folderId"), 
					root.get(H491_.h491011).alias("faxSubject"),
					root.get(H491_.h491012).alias("faxTypeId"),
					root.get(H491_.h491013).alias("statusId"),
					root.get(H491_.h491014).alias("forwardTo"),
					root.get(H491_.h491015).alias("receiveDate"),
					root.get(H491_.h491016).alias("modifiedDate"),
					root.get(H491_.h491017Faxbox).alias("recepientName"),
					root.get(H491_.h491020Faxnotes).alias("faxNotes")

					)).where(FaxSpecification.getFaxInboxDetails(h491010, h491013, h491014, h491017_faxbox, root, cq, builder));
			cq.orderBy(builder.desc(root.get(H491_.h491001)),(builder.desc(root.get(H491_.h491015))),(builder.desc(root.get(H491_.h491016))));

			Query query = entityManager.createQuery(cq);

			if(pageNo == 1){
				getInboxDetails = query.setFirstResult(0).setMaxResults(12).getResultList();
			}else{
				getInboxDetails = query.setFirstResult( (pageNo-1)*12 ).setMaxResults(12).getResultList();
			}

		}

		catch(Exception exception) {
			exception.printStackTrace();
		}
		finally {
		//	entityManager.close();
		}
		return getInboxDetails;
	}

	@Override
	public List<FaxFolderBean> getFaxFolderCount(Integer fax_location,Integer userId){
		List<FaxFolderBean> tabBasedCount = new ArrayList<FaxFolderBean>();
		FaxFolderBean faxTabCount = new FaxFolderBean();
		try{
			faxTabCount.setTotalInbox(h491Repository.count(FaxSpecification.getInboxDetails(1,-1,0,fax_location)));
			faxTabCount.setUnreadInbox(h491Repository.count(FaxSpecification.getInboxDetails(1,0,0,fax_location)));

			faxTabCount.setTotalSaved(h491Repository.count(FaxSpecification.getInboxDetails(5,-1,0,fax_location)));
			faxTabCount.setUnreadSaved(h491Repository.count(FaxSpecification.getInboxDetails(5,0,0,fax_location)));

			faxTabCount.setTotalTrash(h491Repository.count(FaxSpecification.getInboxDetails(3,-1,0,fax_location)));
			faxTabCount.setUnreadTrash(h491Repository.count(FaxSpecification.getInboxDetails(3,0,0,fax_location)));

			faxTabCount.setOutboxFax(h496Repository.count(FaxSpecification.getOutboxDetails(2,-1)));
			faxTabCount.setSentFax(h496Repository.count(FaxSpecification.getOutboxDetails(4,-1)));

			tabBasedCount.add(faxTabCount);

			faxTabCount = new FaxFolderBean();

			faxTabCount.setTotalInbox(h491Repository.count(FaxSpecification.getInboxDetails(1,-1,userId,fax_location)));
			faxTabCount.setUnreadInbox(h491Repository.count(FaxSpecification.getInboxDetails(1,0,userId,fax_location)));

			faxTabCount.setTotalSaved(h491Repository.count(FaxSpecification.getInboxDetails(5,-1,userId,fax_location)));
			faxTabCount.setUnreadSaved(h491Repository.count(FaxSpecification.getInboxDetails(5,0,userId,fax_location)));

			faxTabCount.setTotalTrash(h491Repository.count(FaxSpecification.getInboxDetails(3,-1,userId,fax_location)));
			faxTabCount.setUnreadTrash(h491Repository.count(FaxSpecification.getInboxDetails(3,0,userId,fax_location)));

			faxTabCount.setOutboxFax(h496Repository.count(FaxSpecification.getOutboxDetails(2,userId)));
			faxTabCount.setSentFax(h496Repository.count(FaxSpecification.getOutboxDetails(4,userId)));

			tabBasedCount.add(faxTabCount);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//entityManager.close();
		}
		return tabBasedCount;
	}

	@Override
	public void deleteSelectFax (String faxId,Integer TRASH, Integer userId) {
		try {
			Integer selectedIds[] = new Integer[faxId.split(",").length];
			for(int index=0;index<selectedIds.length;index++){
				selectedIds[index] = Integer.parseInt(faxId.split(",")[index]);
			}
			//Update Query
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();						
			CriteriaUpdate<H491> criteriaupdate = builder.createCriteriaUpdate(H491.class);
			Root<H491> root = criteriaupdate.from(H491.class);
			if(TRASH == 3){
				criteriaupdate.set("h491015", userId);
				criteriaupdate.set("h491018Isenabled", false);
			}else{
				criteriaupdate.set("h491015", userId);
				criteriaupdate.set("h491010", 3);
			}
			criteriaupdate.where(builder.in(root.get(H491_.h491001).in(selectedIds)));
			entityManager.createQuery(criteriaupdate).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//entityManager.close();
		}
	}

	@Override
	public void deleteSelectOutboxFax(String faxId, Integer userId, Integer folderId) {
		try {
			Integer selectedIds[] = new Integer[faxId.split(",").length];
			for(int index=0;index<selectedIds.length;index++){
				selectedIds[index] = Integer.parseInt(faxId.split(",")[index]);
			}
			//Update Query
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaUpdate<H496> criteriaupdate = builder.createCriteriaUpdate(H496.class);
			Root<H496> root = criteriaupdate.from(H496.class);
			criteriaupdate.set("h496008", 9 );
			criteriaupdate.set("h496019", userId);
			criteriaupdate.where(builder.in(root.get(H496_.h496001).in(selectedIds)));
			entityManager.createQuery(criteriaupdate).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//entityManager.close();
		}
	}

	@Override
	public List<FaxuserlistBean>getForwardUserList(Integer empProfileGroupid) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<EmployeeProfile> root = cq.from(EmployeeProfile.class);
		cq.distinct(true);
		cq.select(
				builder.construct(FaxuserlistBean.class,
						root.get(EmployeeProfile_.empProfileEmpid),
						root.get(EmployeeProfile_.empProfileFullname)
						));
		Predicate condition = null;
		condition = builder.and(builder.equal(root.get(EmployeeProfile_.empProfileIsActive),true),builder.equal(root.get(EmployeeProfile_.empProfileGroupid),empProfileGroupid),builder.notLike(root.get(EmployeeProfile_.empProfileFullname),"test"),builder.notLike(root.get(EmployeeProfile_.empProfileFullname),"demo"));
		cq.where(condition);
		List<Object> empnames=entityManager.createQuery(cq).getResultList();
		List<FaxuserlistBean> detailsBeanList = new ArrayList<FaxuserlistBean>();
		for(int i=0;i<empnames.size();i++)
		{
			FaxuserlistBean detailsBean=(FaxuserlistBean)empnames.get(i);
			detailsBeanList.add(detailsBean);
		}
		return detailsBeanList;
	}

	@Override
	public List<H491> forwardFax(String faxId,Integer forwardTO,Integer folderID,Integer userId){
		
		String[] faxIds = faxId.split(",");
		
		List<H491> faxList=h491Repository.findAll(FaxSpecification.faxForward(faxIds));
		
		H491 aeupdate=null;
		for(H491 fax:faxList){
			Date date=new Date();
			H491 ae =fax;
			ae.setH491014(forwardTO);
			ae.setH491010(folderID);
			ae.setH491015(userId);
			ae.setH491016(new Timestamp(date.getTime()));
			aeupdate=ae;
			h491Repository.save(aeupdate);
		}
		return faxList;
	}

	@Override
	public List<H491> getInFaxDetails(final Integer faxId, Integer userId, Integer faxTab, Integer faxFolder) {
		List<H491> getInboxDetails=new ArrayList<H491>();
		try{
			if(faxId!=-1){
				getInboxDetails = h491Repository.findAll(FaxSpecification.getInFaxDetails(faxId, userId, faxTab, faxFolder));
			}else{
				getInboxDetails = h491Repository.findAll(FaxSpecification.getInFaxDetails(getMaxFaxId(faxFolder), userId, faxTab, faxFolder));
			}
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaUpdate<H491> criteriaupdate = builder.createCriteriaUpdate(H491.class);
			Root<H491> root = criteriaupdate.from(H491.class);
			criteriaupdate.set("h491013", 1 );
			criteriaupdate.set("h491015", userId);
			criteriaupdate.where(builder.equal(root.get(H491_.h491001), faxId));
			entityManager.createQuery(criteriaupdate).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//entityManager.close();
		}
		return getInboxDetails;
	}

	@Override
	public List<H496> getOutFaxDetails(Integer faxId, Integer faxTab, Integer faxFolder) {
		List<H496> getOutboxDetails=new ArrayList<H496>();
		try{
			if(faxId!=-1){
				getOutboxDetails = h496Repository.findAll(FaxSpecification.getOutFaxDetails(faxId, faxTab, faxFolder));
			}else{
				getOutboxDetails = h496Repository.findAll(FaxSpecification.getOutFaxDetails(getMaxFaxId(faxFolder), faxTab, faxFolder));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//entityManager.close();
		}
		return getOutboxDetails;
	}

	private int getMaxFaxId(int faxFolder) {
		EntityManager em = emf.createEntityManager();
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq = builder.createQuery();
			if(faxFolder == 2 || faxFolder == 4){
				Root<H496> root = cq.from(H496.class);
				cq.select(builder.max(root.get(H496_.h496001)));	
			}else{
				Root<H491> root = cq.from(H491.class);
				cq.select(builder.max(root.get(H491_.h491001)));
			}
			return Integer.parseInt("" + em.createQuery(cq).getSingleResult());
		} catch(Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			//em.close();
		}
	}

	@Override
	public List<DoctorSign> getSignatureDetails(Integer empProfileLoginid ) {
		List<DoctorSign> getSignatureDetails = new ArrayList<DoctorSign>();
		try {
			boolean accessType = getAccessType(empProfileLoginid);
			getSignatureDetails = doctorsignRepository.findAll(FaxSpecification.getSignatureDetails(empProfileLoginid, accessType));
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		finally {
			//entityManager.close();
		}
		return getSignatureDetails;
	}

	private boolean getAccessType(int loginId) {
		EntityManager em = emf.createEntityManager();
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq = builder.createQuery();
			Root<DoctorSign> root = cq.from(DoctorSign.class);
			Join<DoctorSign, EmployeeProfile> join1 = root.join(DoctorSign_.empLoginId,JoinType.INNER);
			cq.select(
					builder.construct(FaxuserlistBean.class,
							join1.get(EmployeeProfile_.empProfileFullname),
							join1.get(EmployeeProfile_.empProfileLoginid),
							root.get(DoctorSign_.filename)
							));
			cq.where(builder.equal(root.get(DoctorSign_.empLoginId), loginId));
			@SuppressWarnings("unused")
			Object obj=em.createQuery(cq).getSingleResult();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return true;
		} finally {
			em.close();
		}
	}	
}	
