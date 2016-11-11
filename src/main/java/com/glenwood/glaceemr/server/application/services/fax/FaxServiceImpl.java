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
import com.glenwood.glaceemr.server.application.models.LoginUsers;
import com.glenwood.glaceemr.server.application.models.LoginUsers_;
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
	public List<H496> getOutboxDetails(Integer h496004, Integer userId,Integer pageNo){

		entityManager = emf.createEntityManager();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<H496> root = cq.from(H496.class);

		List<H496> getOutboxDetails=new ArrayList<H496>();
		try {
			cq.select(builder.construct(FaxOutboxBean.class, 
					root.get(H496_.h496001).alias("faxId"),
					builder.coalesce(root.get(H496_.h496002), ""),
					root.get(H496_.h496003).alias("faxFileName"),
					root.get(H496_.h496004).alias("folderId"),
					root.get(H496_.h496006).alias("scheduleTime"),
					root.get(H496_.h496007).alias("actualDespatchtime"),
					root.get(H496_.h496008).alias("statusId"),
					root.get(H496_.h496009).alias("recipientNumber"),
					root.get(H496_.h496010).alias("recipientName"),
					root.get(H496_.h496012).alias("forwardedToUserId"),
					root.get(H496_.h496013).alias("createdBy"),
					root.get(H496_.h496014).alias("createdDate"),
					builder.coalesce(root.get(H496_.h496015), ""),
					root.get(H496_.h496019).alias("modifiedBy"),
					root.get(H496_.h496022).alias("senderName"),
					root.get(H496_.h496Faxpngfiles).alias("outboxThumbnail"),
					root.get(H496_.h496Attachment).alias("faxAttachments"),
					builder.coalesce(root.get(H496_.h496RetriesNote),"")
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
		}
		return getOutboxDetails;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FaxInboxBean> getInboxDetails(Integer h491010, Integer h491013,Integer h491014, Integer h491017_faxbox,int pageNo) {
		entityManager = emf.createEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<H491> root = cq.from(H491.class);

		List<FaxInboxBean> getInboxDetails=new ArrayList<FaxInboxBean>();
		try{
			cq.select(builder.construct(FaxInboxBean.class, 
					root.get(H491_.h491001).alias("faxId"),
					root.get(H491_.h491005).alias("receiveDate"),
					root.get(H491_.h491006).alias("senderName"),
					root.get(H491_.h491007).alias("recepientName"),
					root.get(H491_.h491009).alias("faxFileName"),
					root.get(H491_.h491010).alias("folderId"), 
					builder.coalesce(root.get(H491_.h491011), ""),
					root.get(H491_.h491012).alias("faxTypeId"),
					root.get(H491_.h491013).alias("statusId"),
					root.get(H491_.h491014).alias("forwardTo"),
					root.get(H491_.h491015).alias("modifiedBy"),
					root.get(H491_.h491016).alias("modifiedDate"),
					builder.coalesce(root.get(H491_.h491020Faxnotes), "")
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

			faxTabCount.setMytotalInbox(h491Repository.count(FaxSpecification.getInboxDetails(1,-1,userId,fax_location)));
			faxTabCount.setMyunreadInbox(h491Repository.count(FaxSpecification.getInboxDetails(1,0,userId,fax_location)));

			faxTabCount.setMytotalSaved(h491Repository.count(FaxSpecification.getInboxDetails(5,-1,userId,fax_location)));
			faxTabCount.setMyunreadSaved(h491Repository.count(FaxSpecification.getInboxDetails(5,0,userId,fax_location)));

			faxTabCount.setMytotalTrash(h491Repository.count(FaxSpecification.getInboxDetails(3,-1,userId,fax_location)));
			faxTabCount.setMyunreadTrash(h491Repository.count(FaxSpecification.getInboxDetails(3,0,userId,fax_location)));

			faxTabCount.setMyoutboxFax(h496Repository.count(FaxSpecification.getOutboxDetails(2,userId)));
			faxTabCount.setMysentFax(h496Repository.count(FaxSpecification.getOutboxDetails(4,userId)));

			tabBasedCount.add(faxTabCount);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
		return tabBasedCount;
	}


	@Override
	public void deleteSelectFax (String faxId,String fileNames,Integer TRASH, Integer userId) {

		List<H491> faxToDelete=new ArrayList<H491>();
		List<Integer> selectedId = new ArrayList<Integer>();

		for(int index=0;index<faxId.split(",").length;index++){
			selectedId.add(index, Integer.parseInt(faxId.split(",")[index]));
		}
		
		
		faxToDelete = h491Repository.findAll(FaxSpecification.getSelectedFaxDetails(selectedId));
		
		H491 faxInboxBean = faxToDelete.get(0);

		String updatedFileNames = getStringDiff(faxInboxBean.getH491009(), fileNames, ",");

		if(fileNames.trim().length()<=0 || updatedFileNames.indexOf('.')<0){

			for(H491 faxDetails : faxToDelete) {

				if(TRASH == 3){
					faxDetails.setH491015(userId);
					faxDetails.setH491018Isenabled(false);
				}else{
					faxDetails.setH491015(userId);
					faxDetails.setH491010(3);
				}
				
				faxDetails = h491Repository.saveAndFlush(faxDetails);

			}
		}

		else{
			
			Date date=new Date();
			faxInboxBean.setH491015(userId);
			faxInboxBean.setH491009(updatedFileNames);
			faxInboxBean.setH491016(new Timestamp(date.getTime()));
			h491Repository.save(faxInboxBean);		

			H491 newFaxInboxBean = new H491();
			int currFaxId = getCurrMaxId();

			newFaxInboxBean.setH491001(currFaxId);
			newFaxInboxBean.setH491002(faxInboxBean.getH491002());
			newFaxInboxBean.setH491003(faxInboxBean.getH491003());
			newFaxInboxBean.setH491004(faxInboxBean.getH491004());
			newFaxInboxBean.setH491005(faxInboxBean.getH491005());
			newFaxInboxBean.setH491006(faxInboxBean.getH491006());
			newFaxInboxBean.setH491007(faxInboxBean.getH491007());
			newFaxInboxBean.setH491008(faxInboxBean.getH491008());
			newFaxInboxBean.setH491009(fileNames);
			newFaxInboxBean.setH491011(faxInboxBean.getH491011());
			newFaxInboxBean.setH491012(faxInboxBean.getH491012());
			newFaxInboxBean.setH491013(faxInboxBean.getH491013());
			newFaxInboxBean.setH491015(userId);
			newFaxInboxBean.setH491016(faxInboxBean.getH491016());
			newFaxInboxBean.setH491017Faxbox(faxInboxBean.getH491017Faxbox());
			if(TRASH == 3){
				newFaxInboxBean.setH491018Isenabled (false);
				newFaxInboxBean.setH491015(userId);
				if(faxInboxBean.getH491014() == 0){
					newFaxInboxBean.setH491014(userId);
				}else{
					newFaxInboxBean.setH491014(faxInboxBean.getH491014());
				}
			}else{
				newFaxInboxBean.setH491015(userId);
				newFaxInboxBean.setH491010(3);
				if(faxInboxBean.getH491014() == 0){
					newFaxInboxBean.setH491014(userId);
				}else{
					newFaxInboxBean.setH491014(faxInboxBean.getH491014());
				}
			}

			h491Repository.saveAndFlush(newFaxInboxBean);
		}
	}

	public String getStringDiff1(String Stringx,String Stringy,String Seperator){
		String xDiffString = "";
		try{
			boolean isExists    = true;
			String xString[]     = Stringx.split(Seperator);
			String yString[]     = Stringy.split(Seperator);
			for(int i=0;i<xString.length;i++){
				for(int j=0;j<yString.length;j++){
					if( xString[i].equals(yString[j] )){
						isExists = true;
						break;
					}else
						isExists = false;
				}
				if (!isExists){
					if(xDiffString=="")
						xDiffString = xString[i];
					else
						xDiffString = xDiffString + Seperator + xString[i];
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

		return xDiffString;
	}

	public Integer getCurrMaxId1() {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<H491> root = cq.from(H491.class);
		cq.select(builder.max(root.get(H491_.h491001)));
		Integer faxId=(Integer) entityManager.createQuery(cq).getSingleResult();

		return faxId+1;
	}

	@Override
	public void deleteSelectOutboxFax (String faxId,Integer TRASH, Integer userId) {

		List<H496> faxToDelete=new ArrayList<H496>();
		List<Integer> selectedId = new ArrayList<Integer>();
		for(int index=0;index<faxId.split(",").length;index++){
			selectedId.add(index, Integer.parseInt(faxId.split(",")[index]));
		}
		faxToDelete = h496Repository.findAll(FaxSpecification.getSelectedFaxDetails1(selectedId));

		for(H496 faxDetails : faxToDelete) {

			faxDetails.setH496008(9);
			faxDetails.setH496019(userId);

			faxDetails = h496Repository.save(faxDetails);
		}
	}

	@Override
	public List<FaxUsersBean> getFaxUserList(){

		List<FaxUsersBean> faxUsers = new ArrayList<FaxUsersBean>();
		int[] groupId = new int[]{-1,-2,-3,-6,-7,-10};

		for(int i=0;i<groupId.length;i++){

			FaxUsersBean usersList = new FaxUsersBean();

			if(groupId[i] == -1){
				usersList.setName("doctors");
			}else if(groupId[i] == -2){
				usersList.setName("nurse");
			}else if(groupId[i] == -3){
				usersList.setName("frontdesk");
			}else if(groupId[i] == -6){
				usersList.setName("ma");
			}else if(groupId[i] == -7){
				usersList.setName("officeManager");
			}else if(groupId[i] == -10){
				usersList.setName("nursePractitioner");
			}

			usersList.setUsersList(getForwardUserList(groupId[i]));

			faxUsers.add(usersList);
		}

		return faxUsers;
	}

	public List<FaxForwardListBean> getForwardUserList(Integer empProfileGroupid) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<EmployeeProfile> root = cq.from(EmployeeProfile.class);
		cq.distinct(true);
		cq.select(

				builder.construct(FaxForwardListBean.class,
						root.get(EmployeeProfile_.empProfileEmpid).alias("hsp001"),
						root.get(EmployeeProfile_.empProfileFullname).alias("hsp002")));
		Predicate condition = null;
		condition = builder.and(builder.equal(root.get(EmployeeProfile_.empProfileIsActive),true),builder.equal(root.get(EmployeeProfile_.empProfileGroupid),empProfileGroupid),builder.notLike(root.get(EmployeeProfile_.empProfileFullname),"test"),builder.notLike(root.get(EmployeeProfile_.empProfileFullname),"demo"));
		cq.where(condition);
		List<Object> empnames=entityManager.createQuery(cq).getResultList();
		List<FaxForwardListBean> detailsBeanList = new ArrayList<FaxForwardListBean>();
		for(int i=0;i<empnames.size();i++)
		{
			FaxForwardListBean detailsBean=(FaxForwardListBean)empnames.get(i);
			detailsBeanList.add(detailsBean);
		}
		return detailsBeanList;
	}

	@Override
	public List<H491> forwardFax(String faxId,String fileNames, Integer forwardTo,Integer folderId,Integer userId){

		List<H491> faxList = new ArrayList<H491>();

		String[] faxIds = faxId.split(",");

		faxList=h491Repository.findAll(FaxSpecification.faxForward(faxIds));
		
		H491 faxInboxBean = faxList.get(0);

		String updatedFileNames = getStringDiff(faxInboxBean.getH491009(), fileNames, ",");

		if(fileNames.trim().length()<=0 || updatedFileNames.indexOf('.')<0){

			H491 aeupdate=null;
			for(H491 fax:faxList){
				Date date=new Date();
				H491 ae =fax;
				ae.setH491014(forwardTo);
				ae.setH491010(folderId);
				ae.setH491015(userId);
				ae.setH491016(new Timestamp(date.getTime()));
				aeupdate=ae;
				h491Repository.save(aeupdate);
			}

		}else{

			Date date=new Date();
			faxInboxBean.setH491015(userId);
			faxInboxBean.setH491009(updatedFileNames);
			faxInboxBean.setH491016(new Timestamp(date.getTime()));
			h491Repository.save(faxInboxBean);		

			H491 newFaxInboxBean = new H491();
			int currFaxId = getCurrMaxId();

			newFaxInboxBean.setH491001(currFaxId);
			newFaxInboxBean.setH491002(faxInboxBean.getH491002());
			newFaxInboxBean.setH491003(faxInboxBean.getH491003());
			newFaxInboxBean.setH491004(faxInboxBean.getH491004());
			newFaxInboxBean.setH491005(faxInboxBean.getH491005());
			newFaxInboxBean.setH491006(faxInboxBean.getH491006());
			newFaxInboxBean.setH491007(faxInboxBean.getH491007());
			newFaxInboxBean.setH491008(faxInboxBean.getH491008());
			newFaxInboxBean.setH491009(fileNames);
			newFaxInboxBean.setH491010(faxInboxBean.getH491010());
			newFaxInboxBean.setH491011(faxInboxBean.getH491011());
			newFaxInboxBean.setH491012(faxInboxBean.getH491012());
			newFaxInboxBean.setH491013(faxInboxBean.getH491013());
			newFaxInboxBean.setH491014(forwardTo);
			newFaxInboxBean.setH491015(userId);
			newFaxInboxBean.setH491016(faxInboxBean.getH491016());
			newFaxInboxBean.setH491017Faxbox(faxInboxBean.getH491017Faxbox());

			h491Repository.saveAndFlush(newFaxInboxBean);

		}


		return faxList;
	}

	public String getStringDiff(String Stringx,String Stringy,String Seperator){
		String xDiffString = "";
		try{
			boolean isExists    = true;
			String xString[]     = Stringx.split(Seperator);
			String yString[]     = Stringy.split(Seperator);
			for(int i=0;i<xString.length;i++){
				for(int j=0;j<yString.length;j++){
					if( xString[i].equals(yString[j] )){
						isExists = true;
						break;
					}else
						isExists = false;
				}
				if (!isExists){
					if(xDiffString=="")
						xDiffString = xString[i];
					else
						xDiffString = xDiffString + Seperator + xString[i];
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

		return xDiffString;
	}

	public Integer getCurrMaxId() {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<H491> root = cq.from(H491.class);
		cq.select(builder.max(root.get(H491_.h491001)));
		Integer faxId=(Integer) entityManager.createQuery(cq).getSingleResult();

		return faxId+1;
	}


	@Override
	public List<H491> getInFaxDetails(final Integer faxId, Integer userId, Integer faxTab, Integer faxFolder) {


		List<H491> inFaxDetails=new ArrayList<H491>();
		inFaxDetails = h491Repository.findAll(FaxSpecification.getInFaxDetails(faxId, faxFolder, faxFolder, faxFolder));

		if(faxId!=-1){
			inFaxDetails = h491Repository.findAll(FaxSpecification.getInFaxDetails(faxId, userId, faxTab, faxFolder));
		}else{
			inFaxDetails = h491Repository.findAll(FaxSpecification.getInFaxDetails(getMaxFaxId(faxFolder), userId, faxTab, faxFolder));
		}

		inFaxDetails.get(0).setH491013(1);
		inFaxDetails.get(0).setH491015(userId);

		h491Repository.save(inFaxDetails.get(0));

		return inFaxDetails;

	}

	@Override
	public List<H496> getOutFaxDetails(Integer userId,Integer faxId, Integer faxTab, Integer faxFolder) {
		List<H496> getOutboxDetails=new ArrayList<H496>();
		try{
			if(faxId!=-1){
				getOutboxDetails = h496Repository.findAll(FaxSpecification.getOutFaxDetails(userId,faxId, faxTab, faxFolder));
			}else{
				getOutboxDetails = h496Repository.findAll(FaxSpecification.getOutFaxDetails(getMaxFaxId(faxFolder),userId,faxTab, faxFolder));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
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
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FaxSignListBean> getSignatureDetails(Integer empId){

		entityManager = emf.createEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<DoctorSign> root = cq.from(DoctorSign.class);
		Join<DoctorSign, EmployeeProfile> join1 = root.join(DoctorSign_.empLoginId,JoinType.INNER);
		Join<DoctorSign, LoginUsers> join2 = root.join(DoctorSign_.empLoginName,JoinType.INNER);

		List<Integer> allowGroupId = new ArrayList<Integer>();

		allowGroupId.add(-1);
		allowGroupId.add(-7);
		allowGroupId.add(-10);

		List<FaxSignListBean> getSignatureDetails = new ArrayList<FaxSignListBean>();
		int loginId = -1;
		boolean accessType = true;

		try {

			loginId = employeeProfileRepository.findOne(FaxSpecification.getEmployeeLoginId(empId)).getEmpProfileLoginid();
			accessType = doctorsignRepository.findOne(FaxSpecification.getSignAccessType(loginId)).getSignaccess();

			if(accessType){

				cq.select(
						builder.construct(FaxSignListBean.class,
								join1.get(EmployeeProfile_.empProfileFullname),
								join1.get(EmployeeProfile_.empProfileLoginid),
								join2.get(LoginUsers_.loginUsersUsername),
								root.get(DoctorSign_.filename)
								)
						);
				cq.where(builder.and(builder.equal(join1.get(EmployeeProfile_.empProfileIsActive), true) ,builder.notLike(root.get(DoctorSign_.filename), ""), join1.get(EmployeeProfile_.empProfileGroupid).in(allowGroupId), builder.equal(root.get(DoctorSign_.signaccess), true)));
    
			}else{

				cq.select(
						builder.construct(FaxSignListBean.class,
								join1.get(EmployeeProfile_.empProfileFullname),
								join1.get(EmployeeProfile_.empProfileLoginid),
								join2.get(LoginUsers_.loginUsersUsername),
								root.get(DoctorSign_.filename)
								)
						);
				cq.where(builder.and(builder.equal(root.get(DoctorSign_.empLoginId),loginId),builder.equal(join1.get(EmployeeProfile_.empProfileIsActive), true)));

			}

			Query query = entityManager.createQuery(cq);

			getSignatureDetails = query.getResultList();

		} catch (Exception e) {
		}
		finally{

		}

		return getSignatureDetails;

	}

	@SuppressWarnings("unused")
	private boolean getAccessType(int loginId) {
		EntityManager em = emf.createEntityManager();
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq = builder.createQuery();
			Root<DoctorSign> root = cq.from(DoctorSign.class);
			Join<DoctorSign, EmployeeProfile> join1 = root.join(DoctorSign_.empLoginId,JoinType.INNER);
			cq.select(
					builder.construct(FaxSignListBean.class,
							join1.get(EmployeeProfile_.empProfileFullname),
							join1.get(EmployeeProfile_.empProfileLoginid),
							root.get(DoctorSign_.filename)

							));
			cq.where(builder.equal(root.get(DoctorSign_.empLoginId), loginId));
			Object obj=em.createQuery(cq).getSingleResult();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return true;
		} finally {
			em.close();
		}
	}

	@Override
	public String lastFaxReceivedTime(Integer userId) {

		String timeDetails = "";

		H491 faxReceivedTime = new H491();

		faxReceivedTime = h491Repository.findOne(FaxSpecification.lastFaxReceivedTime(getMaxFaxId1(0, userId)));

		timeDetails = faxReceivedTime.getH491005().toString();

		faxReceivedTime = h491Repository.findOne(FaxSpecification.lastFaxReceivedTime(getMaxFaxId1(1, userId)));

		timeDetails = timeDetails.concat("~".concat(faxReceivedTime.getH491005().toString()));

		return timeDetails;

	}


	private int getMaxFaxId1(int faxTab, int userId) {

		EntityManager em = emf.createEntityManager();
		try {

			CriteriaBuilder builder = em.getCriteriaBuilder();

			CriteriaQuery<Object> cq = builder.createQuery();

			Root<H491> root = cq.from(H491.class);

			cq.select(builder.max(root.get(H491_.h491001)));

			if(faxTab == 0){
				cq.where(builder.and(builder.equal(root.get(H491_.h491010), 1), builder.equal(root.get(H491_.h491014), userId), builder.equal(root.get(H491_.h491018Isenabled), true)));
			}else{
				cq.where(builder.and(builder.equal(root.get(H491_.h491010), 1), builder.equal(root.get(H491_.h491014), 0),builder.equal(root.get(H491_.h491018Isenabled), true)));
			}

			return Integer.parseInt("" + em.createQuery(cq).getSingleResult());

		} catch(Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
		}
	}

}