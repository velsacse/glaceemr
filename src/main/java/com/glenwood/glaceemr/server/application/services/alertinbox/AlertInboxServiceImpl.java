package com.glenwood.glaceemr.server.application.services.alertinbox;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.models.AlertArchive;
import com.glenwood.glaceemr.server.application.models.AlertCategory;
import com.glenwood.glaceemr.server.application.models.AlertCategory_;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.AlertEvent_;
import com.glenwood.glaceemr.server.application.models.AlertPrivilege;
import com.glenwood.glaceemr.server.application.models.AlertPrivilege_;
import com.glenwood.glaceemr.server.application.models.Chart;
import com.glenwood.glaceemr.server.application.models.Chart_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.Room;
import com.glenwood.glaceemr.server.application.models.Room_;
import com.glenwood.glaceemr.server.application.repositories.AlertArchiveRepository;
import com.glenwood.glaceemr.server.application.repositories.AlertCategoryRepository;
import com.glenwood.glaceemr.server.application.repositories.AlertInboxRepository;
import com.glenwood.glaceemr.server.application.repositories.ChartRepository;
import com.glenwood.glaceemr.server.application.repositories.EmployeeProfileRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientRegistrationRepository;
import com.glenwood.glaceemr.server.application.specifications.AlertArchiveSpecification;
import com.glenwood.glaceemr.server.application.specifications.AlertCategorySpecification;
import com.glenwood.glaceemr.server.application.specifications.AlertInboxSpecification;
import com.glenwood.glaceemr.server.application.specifications.PatientRegistrationSpecification;
import com.google.common.base.Optional;
/**
 * Service Implementation file for AlertInboxController
 * @author Manikandan
 *
 */
@Service
@Transactional
public class AlertInboxServiceImpl implements AlertInboxService{

	@Autowired
	AlertInboxRepository alertInboxRepository;

	@Autowired
	AlertArchiveRepository alertArchiveRepository;

	@Autowired
	AlertCategoryRepository alertCategoryRepository;

	@Autowired
	PatientRegistrationRepository patientRegistrationRepository;

	@Autowired
	EmployeeProfileRepository empProfileRepository;

	@Autowired
	ChartRepository chartRepository;

	@Autowired
	EntityManagerFactory emf ;

	@PersistenceContext
	EntityManager em;

	private Logger logger = Logger.getLogger(AlertInboxServiceImpl.class);

	/**
	 * To frame query to get shortcut details and to get alert details.
	 * @param userIdList
	 * @param categoryIdList
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<Map<String, List<Map<String, List<Object>>>>> getAlerts(
			String userId,
			List<String> categoryIdList, 
			int pageno, 
			int pagesize) {


		List<Integer> userIdList=constructUserList(userId);

		logger.debug("Creating shortcuts query.");

		List<AlertCountBean> alertCountList= getAlertCount(userId);//em.createQuery(cq).getResultList();
		List<Map<String, List<Map<String, List<Object>>>>> alertList=buildShortCutJson(alertCountList,userIdList,categoryIdList,pageno,pagesize);
		return alertList;
	}

	/**
	 * Construct user id list with user group id and check whether it belongs to "All clinical users"
	 * @param userId
	 * @return
	 */
	private List<Integer> constructUserList(String userId) {

		EmployeeProfile empProfile=empProfileRepository.findOne(AlertInboxSpecification.getUserById(userId));

		List<Integer> userIdList=new ArrayList<Integer>();
		userIdList.add(0);
		userIdList.add(Integer.parseInt(userId));
		userIdList.add(empProfile.getEmpProfileGroupid());

		if(empProfile.getEmpProfileGroupid()==-1||empProfile.getEmpProfileGroupid()==-2||empProfile.getEmpProfileGroupid()==-6||empProfile.getEmpProfileGroupid()==-10)
			userIdList.add(-99);

		return userIdList;		
	}

	/**
	 * Get alerts count based on the user id
	 */
	@Override
	public List<AlertCountBean> getAlertCount(String userId) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();

		List<Integer> userIdList=constructUserList(userId);

		Root<AlertCategory> rootAlertCategory = cq.from(AlertCategory.class);
		Join<AlertCategory, AlertEvent> joinAcAI=rootAlertCategory.join("alertEventCategoryId",JoinType.INNER);
		Join<AlertCategory, AlertPrivilege> joinAcAp=rootAlertCategory.join(AlertCategory_.alertPrivilegeTable,JoinType.INNER);

		Predicate predicatePrivilegeId=builder.equal(joinAcAp.get(AlertPrivilege_.alert_privilege_user_id), new Integer(userId));

		Expression<Integer> exprAlertEventTo=joinAcAI.get(AlertEvent_.alertEventTo);
		Predicate[] restrictions = new Predicate[] {
				builder.equal(joinAcAI.get(AlertEvent_.alertEventStatus), new Integer(1)),
				exprAlertEventTo.in(userIdList)
		};

		joinAcAI.on(restrictions);
		joinAcAp.on(predicatePrivilegeId);
		cq.select(builder.construct(AlertCountBean.class, builder.count(joinAcAI.get(AlertEvent_.alertEventId)),
				rootAlertCategory.get(AlertCategory_.alertCategoryId),
				rootAlertCategory.get(AlertCategory_.alertCategoryName),
				builder.selectCase().when(builder.equal(joinAcAI.get(AlertEvent_.alertEventRead), false), builder.count(joinAcAI.get(AlertEvent_.alertEventId))).otherwise(builder.literal(0)),
				rootAlertCategory.get(AlertCategory_.alertCategoryDisplayOrder)));
		cq.groupBy(rootAlertCategory.get(AlertCategory_.alertCategoryId),
				rootAlertCategory.get(AlertCategory_.alertCategoryName),
				rootAlertCategory.get(AlertCategory_.alertCategoryDisplayOrder),
				joinAcAI.get(AlertEvent_.alertEventRead)
				//				,rootAlertCategory.get(AlertCategory_.alertCategoryGroup)
				);
		cq.orderBy(builder.asc(rootAlertCategory.get(AlertCategory_.alertCategoryId)));

		List<Object> getAlertCounts=em.createQuery(cq).getResultList();

		List<AlertCountBean> alertCountList=parseCategories(getAlertCounts);
		return alertCountList;
	}

	/**
	 * Parse categories to get the total unread alerts count
	 * @param getAlertCounts
	 * @return
	 */
	private List<AlertCountBean> parseCategories(List<Object> getAlertCounts) {
		List<String> categoryIdList=new ArrayList<String>();
		List<String> categoryOrderList=new ArrayList<String>();
		List<String> categoryNameList=new ArrayList<String>();
		List<AlertCountBean> alertCountList=new ArrayList<AlertCountBean>();
		HashMap<String, Long> totalCountMap= new HashMap<String, Long>();
		HashMap<String, Long> unReadCountMap = new HashMap<String, Long>();
		try {
			for(int i=0;i<getAlertCounts.size();i++){
				AlertCountBean sib=(AlertCountBean) getAlertCounts.get(i);
				String categoryId=sib.getCategoryId();
				String categoryName=sib.getCategoryName();
				String categoryOrder=sib.getCategoryorder();
				categoryIdList.add(categoryId);
				categoryOrderList.add(categoryOrder);
				categoryNameList.add(categoryName);
			}
			for(int i=0;i<getAlertCounts.size();i++){
				AlertCountBean sib=(AlertCountBean) getAlertCounts.get(i);
				if(totalCountMap.containsKey(sib.getCategoryId()))
				{
					Long prevTotalCount = totalCountMap.get(sib.getCategoryId());
					totalCountMap.put(sib.getCategoryId(), prevTotalCount+sib.getTotalCount());
				}
				else
				{
					totalCountMap.put(sib.getCategoryId(), sib.getTotalCount());
				}
				if(unReadCountMap.containsKey(sib.getCategoryId()))
				{
					Long prevUnreadCount = unReadCountMap.get(sib.getCategoryId());
					unReadCountMap.put(sib.getCategoryId(), prevUnreadCount+sib.getReadCountSum());
				}
				else
				{
					unReadCountMap.put(sib.getCategoryId(), sib.getReadCountSum());
				}
			}
			List<String> uniqueCategoryId = findDuplicatesList(categoryIdList);
			List<String> uniqueCategoryName = findDuplicatesList(categoryNameList);
			List<String> uniqueCategoryOrder = findDuplicatesList(categoryOrderList);
			for(int i=0;i<uniqueCategoryId.size();i++){
				AlertCountBean sib=(AlertCountBean) getAlertCounts.get(i);
				sib.setTotalCount(totalCountMap.get(uniqueCategoryId.get(i)));
				sib.setCategoryId(uniqueCategoryId.get(i));
				sib.setCategoryName(uniqueCategoryName.get(i));
				sib.setCategoryorder(uniqueCategoryOrder.get(i));
				sib.setUnReadCount(unReadCountMap.get(uniqueCategoryId.get(i)));

				alertCountList.add(sib);
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}

		return alertCountList;
	}
	
	public List<String> findDuplicatesList(List<String> duplicateList) {
		Set<String> stringsSet = new LinkedHashSet<String>();//A Linked hash set 
		//prevents the adding order of the elements
		for (String string: duplicateList) {
		    stringsSet.add(string);
		}
		return new ArrayList<String>(stringsSet);
	}

	public Set<String> findDuplicates(List<String> listContainingDuplicates)
	{ 
	  final Set<String> setToReturn = new HashSet(); 
	  final Set<String> set1 = new HashSet();

	  for (String yourInt : listContainingDuplicates)
	  {
	   if (!set1.add(yourInt))
	   {
	    setToReturn.add(yourInt);
	   }
	  }
	  return setToReturn;
	}

	/**
	 * To modify and rebuild the shortcut result list
	 * @param getShortCutList
	 * @param userIdList
	 * @param categoryIdList
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map<String, List<Map<String, List<Object>>>>> buildShortCutJson(List<AlertCountBean> getShortCutList,
			List<Integer> userIdList, List<String> categoryIdList, int pageno,
			int pagesize) {

		logger.debug("Modifying shortcuts result list.");
		System.out.println("sh list "+getShortCutList.size());

		List<String> shortCategoryIdList=new ArrayList<String>();
		List<Map<String, List<Map<String, List<Object>>>>> alertsArray=new ArrayList<Map<String, List<Map<String, List<Object>>>>>();
		Map map=new HashMap();
		List<Map<String, List<Object>>> alertList=new ArrayList<Map<String,List<Object>>>();

		try {

			for(int i=0;i<getShortCutList.size();i++){
				AlertCountBean sib= getShortCutList.get(i);
				String shortCategoryId=sib.getCategoryId();
				shortCategoryIdList.add(shortCategoryId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Set<String> categoryIdSet=new TreeSet<String>(shortCategoryIdList);
		List<String> categoryIdListTemp=new ArrayList<String>(categoryIdSet);

		if(!categoryIdList.get(0).equals("-1"))
			categoryIdListTemp=categoryIdList;


		try{
			alertList=buildAlertsJsn(categoryIdListTemp,userIdList,pageno,pagesize);
			System.out.println(" ho "+ alertList.toString());
			map.put("alerts", alertList);
			map.put("shortcutinfo", getShortCutList);

			alertsArray.add(map);
		}
		catch(Exception e){}

		return alertsArray;
	}


	/**
	 * To get alerts detail from shortcut categories and to build them. 
	 * @param shortCategoryIdList
	 * @param userIdList
	 * @param pageno
	 * @param pagesize
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map<String, List<Object>>> buildAlertsJsn(List<String> shortCategoryIdList,
			List<Integer> userIdList, int pageno, int pagesize) throws ParseException {

		logger.debug("Getting category based alerts and modifying the bean.");

		List<Map<String, List<Object>>> arrayAlertList=new ArrayList<Map<String,List<Object>>>();
		if(shortCategoryIdList.get(0).equalsIgnoreCase("-1"))
		{
			shortCategoryIdList.clear();
			List<AlertCountBean> totalShortCategory = getAlertCount(userIdList.get(1).toString());
			for(int i=0;i<totalShortCategory.size();i++)
			{
				shortCategoryIdList.add(totalShortCategory.get(i).getCategoryId());
			}
		}

		for(int i=0;i<shortCategoryIdList.size();i++){

			List<Object> alertList=new ArrayList<Object>();
			Map mapAlertsObject=new HashMap();
			Integer categoryId=new Integer(shortCategoryIdList.get(i));
			List<Object> alertsByCategories=getAlertsByCategories(categoryId,userIdList);
			int unreadalerts=0;
			for(int j=0;j<alertsByCategories.size();j++){

				AlertInboxBean aib=(AlertInboxBean) alertsByCategories.get(j);
				String createdDateParsed="",createdDateTime="";

				//HH converts hour in 24 hours format (0-23), day calculation
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				Date dateFromDb = null;

				final String OLD_FORMAT3 =  "yyyy-MM-dd HH:mm:ss";

				SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT3);
				Date currentDate = sdf.parse(aib.getCurrentmilliseconds()+"");
				dateFromDb = format.parse(aib.getCreateddate()+"");

				//in milliseconds
				long diff = currentDate.getTime() - dateFromDb.getTime();

				long diffSeconds = diff / 1000 % 60;
				long diffMinutes = diff / (60 * 1000) % 60;
				long diffHours = diff / (60 * 60 * 1000) % 24;
				long diffDays = diff / (24 * 60 * 60 * 1000);

				String timeBuffer="";

				if(diffDays==0){
					if(diffHours==0){
						if(diffMinutes==0){
							timeBuffer=diffSeconds+" secs";
						}
						else{
							if(diffSeconds==0)
								timeBuffer=diffMinutes+" mins ";
							else
								timeBuffer=diffMinutes+" mins "+diffSeconds+" secs";
						}
					}
					else{
						if(diffMinutes==0)
							timeBuffer=diffHours+" hrs ";
						else
							timeBuffer=diffHours+" hrs "+diffMinutes+" mins ";
					}
				}
				else{
					if(diffHours==0)
						timeBuffer=diffDays+" days ";
					else
						timeBuffer=diffDays+" days "+diffHours+" hrs ";
				}

				createdDateTime=timeBuffer;		

				final String OLD_FORMAT = "yyyy-MM-dd";
				final String NEW_FORMAT = "MM/dd/yyyy";

				// 2015-11-29
				String oldDateString = aib.getCreateddate()+"";

				SimpleDateFormat sdf1 = new SimpleDateFormat(OLD_FORMAT);
				Date d = sdf1.parse(oldDateString);
				sdf1.applyPattern(NEW_FORMAT);
				createdDateParsed = sdf1.format(d);		//29/11/2015


				if(aib.getFromid()==-100){
					aib.setForwardedby("Patient");
				}

				if(aib.getModifiedbyid()==-100){
					aib.setModifiedbyname("Patient");
				}

				if(aib.getToid()==0){
					aib.setReceivedto("All");
				}
				else if(aib.getToid()==-1){
					aib.setReceivedto("Doctors");
				}
				else if(aib.getToid()==-10){
					aib.setReceivedto("Nurse Practitioner");
				}
				else if(aib.getToid()==-6){
					aib.setReceivedto("MA");
				}
				else if(aib.getToid()==-7){
					aib.setReceivedto("Office Manager");
				}
				else if(aib.getToid()==-2){
					aib.setReceivedto("Nurse");
				}
				else if(aib.getToid()==-3){
					aib.setReceivedto("Front Desk");
				}
				else if(aib.getToid()==-5){
					aib.setReceivedto("Admin");
				}

				if(!aib.getRead()){
					unreadalerts++;
				}

				aib.setCreateddate(createdDateParsed);
				aib.setCreateddatetime(createdDateTime);

				if(j==0){
					List<Object> alertCategoryDetailList=new ArrayList<Object>();
					AlertCategoryBean acb=new AlertCategoryBean(
							aib.getAlerttype(), 
							aib.getQrflag(), 
							aib.getSubpage(), 
							aib.getStatus(), 
							aib.getAlertgroup(), 
							aib.getCategoryid(), 
							aib.getDisplayname(), 
							aib.getExpanded(), 
							aib.getAlertsection(), 
							aib.getActionmap(), 
							aib.getCategoryorder(), 
							aib.getCategoryname(), 
							aib.getNeeddatewisegrouping(),
							aib.getQrurl());
					alertCategoryDetailList.add(acb);
					mapAlertsObject.put("alertcategory",alertCategoryDetailList);
				}

				alertList.add(aib);
			}

			mapAlertsObject.put("categorydetail", alertList);
			mapAlertsObject.put("totalalerts",alertList.size());
			mapAlertsObject.put("unreadalerts",unreadalerts);
			arrayAlertList.add(mapAlertsObject);
		}

		return arrayAlertList;
	}


	/**
	 * To form the query to build the alert details. 
	 * @param categoryId
	 * @param userIdList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Object> getAlertsByCategories(Integer categoryId,
			List<Integer> userIdList) {

		logger.trace("Creating alerts query.");
		System.out.println("cate "+categoryId+" s f"+userIdList.toString());

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<AlertCategory> rootAlCate = cq.from(AlertCategory.class);
		Join<AlertCategory,AlertEvent> joinAcAe=rootAlCate.join("alertEventCategoryId",JoinType.INNER);
		Predicate[] predicateCateIdStatus = new Predicate[] {
				joinAcAe.get(AlertEvent_.alertEventTo).in(userIdList),
				builder.equal(joinAcAe.get(AlertEvent_.alertEventStatus), new Integer(1))
		};
		joinAcAe.on(predicateCateIdStatus);
		Join<Join<AlertCategory,AlertEvent>,Room> joinAcAeR=joinAcAe.join("roomTable",JoinType.LEFT);
		Join<AlertCategory,AlertPrivilege> joinAcAp=rootAlCate.join("alertPrivilegeTable",JoinType.INNER);
		Predicate predicateApUserId=builder.equal(joinAcAp.get(AlertPrivilege_.alert_privilege_user_id), userIdList.get(1));
		joinAcAp.on(predicateApUserId);
		Join<Join<AlertEvent,AlertCategory>,Chart> joinAeChart=joinAcAe.join("chartTable",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,EmployeeProfile> joinAeEmpFrom=joinAcAe.join("empProfileTableFrom",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,EmployeeProfile> joinAeEmpTo=joinAcAe.join("empProfileTableTo",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,EmployeeProfile> joinAeEmpReadBy=joinAcAe.join("empProfileTableReadBy",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,EmployeeProfile> joinAeEmpModBy=joinAcAe.join("empProfileTableModifiedBy",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,Encounter> joinAeEncId=joinAcAe.join("encounterTableId",JoinType.LEFT);
		Join<Chart,PatientRegistration> joinChartPatReg=joinAeChart.join("patientRegistrationTable",JoinType.LEFT);
		Join<Encounter,EmployeeProfile> joinEncEmp=joinAeEncId.join("empProfileEmpId",JoinType.LEFT);

		Expression<Integer> exprCategoryId=joinAcAe.get(AlertEvent_.alertEventId);

		cq.select(builder.construct(AlertInboxBean.class, 
				joinAcAe.get(AlertEvent_.alertEventId).alias("eventId"),
				rootAlCate.get(AlertCategory_.alertCategoryId),
				joinAeChart.get(Chart_.chartId),
				joinChartPatReg.get(PatientRegistration_.patientRegistrationAccountno).alias("accNo"),
				joinAcAe.get(AlertEvent_.alertEventPatientName).alias("patientname"),
				builder.coalesce(joinAcAeR.get(Room_.roomName),"-"),
				rootAlCate.get(AlertCategory_.alertCategoryName),
				rootAlCate.get(AlertCategory_.alertCategoryUrl),
				rootAlCate.get(AlertCategory_.alertCategoryDisplayName),
				rootAlCate.get(AlertCategory_.alertCategoryGroup),
				rootAlCate.get(AlertCategory_.alertCategoryType),	
				joinAcAe.get(AlertEvent_.alertEventFrom),
				joinAcAe.get(AlertEvent_.alertEventTo),
				joinAcAe.get(AlertEvent_.alertEventRefId),
				joinAcAe.get(AlertEvent_.alertEventPatientId),
				builder.coalesce(joinAcAe.get(AlertEvent_.alertEventParentalertid),0),
				joinAcAe.get(AlertEvent_.alertEventEncounterId),
				joinAcAe.get(AlertEvent_.alertEventMessage),
				joinAcAe.get(AlertEvent_.alertEventModifiedby),
				builder.function("to_char", String.class, joinAcAe.get(AlertEvent_.alertEventCreatedDate),builder.literal("yyyy-MM-dd HH:mm:ss")),
				joinAeEmpFrom.get(EmployeeProfile_.empProfileFullname).alias("from"),
				joinAeEmpTo.get(EmployeeProfile_.empProfileFullname).alias("to"),
				joinAeEmpReadBy.get(EmployeeProfile_.empProfileFullname).alias("readBy"),
				joinAeEmpModBy.get(EmployeeProfile_.empProfileFullname).alias("modifiedBy"),
				builder.coalesce(joinAcAe.get(AlertEvent_.alertEventRead),true),
				rootAlCate.get(AlertCategory_.alertCategoryUrlCaption),
				rootAlCate.get(AlertCategory_.alertCategorySubpage),
				builder.function("to_char", String.class, joinAcAe.get(AlertEvent_.alertEventModifiedDate),builder.literal("MM/dd/yyyy HH:MI:ss am")),
				builder.coalesce(joinAcAe.get(AlertEvent_.alertEventHighlight),false),
				rootAlCate.get(AlertCategory_.alertCategorySection),
				rootAlCate.get(AlertCategory_.alertCategoryQrUrl),	
				joinAcAe.get(AlertEvent_.alertEventStatus),
				joinAcAp.get(AlertPrivilege_.alert_privilege_expanded),
				rootAlCate.get(AlertCategory_.alertCategoryActionMap),
				rootAlCate.get(AlertCategory_.alertCategoryDisplayOrder),
				joinAcAp.get(AlertPrivilege_.alert_privilege_date_grouping_needed),
				rootAlCate.get(AlertCategory_.alertCategoryQrFlag),
				joinEncEmp.get(EmployeeProfile_.empProfileFullname),
				builder.function("to_char", String.class, joinAcAe.get(AlertEvent_.alertEventCreatedDate),builder.literal("MM/dd/yyyy HH:MI:ss am")),
				builder.currentTimestamp(),
				builder.function("to_char", String.class, joinAcAe.get(AlertEvent_.alertEventCreatedDate),builder.literal("HH:MI am")),
				joinChartPatReg.get(PatientRegistration_.patientRegistrationPhoneNo),
				builder.coalesce(joinAcAe.get(AlertEvent_.alertEventReadby),0)
				))
				.where(builder.isNotNull(exprCategoryId),
						builder.equal(rootAlCate.get(AlertCategory_.alertCategoryId),categoryId))
						.orderBy(builder.desc(joinAcAe.get(AlertEvent_.alertEventCreatedDate)));

		Query query=em.createQuery(cq);

		List<Object> alertsResultList=query.setMaxResults(10).getResultList();
		System.out.println("sizxx "+alertsResultList.size());

		return alertsResultList;
	}

	/**
	 * Get category based alert list with paging
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AlertInboxBean> getAlertsByCategory(String userId, String categoryId, int pageno, int pagesize) {

		logger.trace("Creating alerts query.");

		List<Integer> userIdList=constructUserList(userId);

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<AlertCategory> rootAlCate = cq.from(AlertCategory.class);
		Join<AlertCategory,AlertEvent> joinAcAe=rootAlCate.join("alertEventCategoryId",JoinType.INNER);
		Predicate[] predicateCateIdStatus = new Predicate[] {
				joinAcAe.get(AlertEvent_.alertEventTo).in(userIdList),
				builder.equal(joinAcAe.get(AlertEvent_.alertEventStatus), new Integer(1))
		};
		joinAcAe.on(predicateCateIdStatus);
		Join<Join<AlertCategory,AlertEvent>,Room> joinAcAeR=joinAcAe.join("roomTable",JoinType.LEFT);
		Join<AlertCategory,AlertPrivilege> joinAcAp=rootAlCate.join("alertPrivilegeTable",JoinType.INNER);
		Predicate predicateApUserId=builder.equal(joinAcAp.get(AlertPrivilege_.alert_privilege_user_id), new Integer(userId));
		joinAcAp.on(predicateApUserId);
		Join<Join<AlertEvent,AlertCategory>,Chart> joinAeChart=joinAcAe.join("chartTable",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,EmployeeProfile> joinAeEmpFrom=joinAcAe.join("empProfileTableFrom",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,EmployeeProfile> joinAeEmpTo=joinAcAe.join("empProfileTableTo",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,EmployeeProfile> joinAeEmpReadBy=joinAcAe.join("empProfileTableReadBy",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,EmployeeProfile> joinAeEmpModBy=joinAcAe.join("empProfileTableModifiedBy",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,Encounter> joinAeEncId=joinAcAe.join("encounterTableId",JoinType.LEFT);
		Join<Chart,PatientRegistration> joinChartPatReg=joinAeChart.join("patientRegistrationTable",JoinType.LEFT);
		Join<Encounter,EmployeeProfile> joinEncEmp=joinAeEncId.join("empProfileEmpId",JoinType.LEFT);

		Expression<Integer> exprEventId=joinAcAe.get(AlertEvent_.alertEventId);

		cq.select(builder.construct(AlertInboxBean.class, 
				joinAcAe.get(AlertEvent_.alertEventId).alias("eventId"),
				rootAlCate.get(AlertCategory_.alertCategoryId),
				builder.coalesce(joinAeChart.get(Chart_.chartId),-1),
				joinChartPatReg.get(PatientRegistration_.patientRegistrationAccountno).alias("accNo"),
				joinAcAe.get(AlertEvent_.alertEventPatientName).alias("patientname"),
				builder.coalesce(joinAcAeR.get(Room_.roomName),"-"),
				rootAlCate.get(AlertCategory_.alertCategoryName),
				rootAlCate.get(AlertCategory_.alertCategoryUrl),
				rootAlCate.get(AlertCategory_.alertCategoryDisplayName),
				rootAlCate.get(AlertCategory_.alertCategoryGroup),
				rootAlCate.get(AlertCategory_.alertCategoryType),	
				joinAcAe.get(AlertEvent_.alertEventFrom),
				joinAcAe.get(AlertEvent_.alertEventTo),
				joinAcAe.get(AlertEvent_.alertEventRefId),
				joinAcAe.get(AlertEvent_.alertEventPatientId),
				builder.coalesce(joinAcAe.get(AlertEvent_.alertEventParentalertid),0),
				joinAcAe.get(AlertEvent_.alertEventEncounterId),
				joinAcAe.get(AlertEvent_.alertEventMessage),
				joinAcAe.get(AlertEvent_.alertEventModifiedby),
				builder.function("to_char", String.class, joinAcAe.get(AlertEvent_.alertEventCreatedDate),builder.literal("yyyy-MM-dd HH:mm:ss")),
				joinAeEmpFrom.get(EmployeeProfile_.empProfileFullname).alias("from"),
				joinAeEmpTo.get(EmployeeProfile_.empProfileFullname).alias("to"),
				joinAeEmpReadBy.get(EmployeeProfile_.empProfileFullname).alias("readBy"),
				joinAeEmpModBy.get(EmployeeProfile_.empProfileFullname).alias("modifiedBy"),
				builder.coalesce(joinAcAe.get(AlertEvent_.alertEventRead),true),
				rootAlCate.get(AlertCategory_.alertCategoryUrlCaption),
				rootAlCate.get(AlertCategory_.alertCategorySubpage),
				builder.function("to_char", String.class, joinAcAe.get(AlertEvent_.alertEventModifiedDate),builder.literal("MM/dd/yyyy HH:MI:ss am")),
				builder.coalesce(joinAcAe.get(AlertEvent_.alertEventHighlight),false),
				rootAlCate.get(AlertCategory_.alertCategorySection),
				rootAlCate.get(AlertCategory_.alertCategoryQrUrl),	
				joinAcAe.get(AlertEvent_.alertEventStatus),
				joinAcAp.get(AlertPrivilege_.alert_privilege_expanded),
				rootAlCate.get(AlertCategory_.alertCategoryActionMap),
				rootAlCate.get(AlertCategory_.alertCategoryDisplayOrder),
				joinAcAp.get(AlertPrivilege_.alert_privilege_date_grouping_needed),
				rootAlCate.get(AlertCategory_.alertCategoryQrFlag),
				joinEncEmp.get(EmployeeProfile_.empProfileFullname),
				builder.function("to_char", String.class, joinAcAe.get(AlertEvent_.alertEventCreatedDate),builder.literal("MM/dd/yyyy HH:MI:ss am")),
				builder.currentTimestamp(),
				builder.function("to_char", String.class, joinAcAe.get(AlertEvent_.alertEventCreatedDate),builder.literal("HH:MI am")),
				joinChartPatReg.get(PatientRegistration_.patientRegistrationPhoneNo),
				builder.coalesce(joinAcAe.get(AlertEvent_.alertEventReadby),0)
				))
				.where(builder.isNotNull(exprEventId),
						builder.equal(rootAlCate.get(AlertCategory_.alertCategoryId),categoryId))
						.orderBy(builder.desc(joinAcAe.get(AlertEvent_.alertEventCreatedDate)));

		Query query=em.createQuery(cq);
		if(pagesize != -1){
			query.setFirstResult((pageno-1)*pagesize);	// Page no (offset)
			query.setMaxResults(pagesize);		// Page size (No of results)
		}
		List<Object> alertsResultList=query.getResultList();

		List<AlertInboxBean> aiblist=parseAlerts(alertsResultList);

		return aiblist;
	}

	/**
	 * Parse alert event bean to modify date and time
	 * @param alertsResultList
	 * @return
	 */
	private List<AlertInboxBean> parseAlerts(List<Object> alertsResultList) {

		List<AlertInboxBean> alertList=new ArrayList<AlertInboxBean>();

		try{

			for (Object object : alertsResultList) {

				AlertInboxBean aib=(AlertInboxBean) object;
				String createdDateParsed="",createdDateTime="";
				
				//HH converts hour in 24 hours format (0-23), day calculation
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				Date dateFromDb = null;

				final String OLD_FORMAT3 =  "yyyy-MM-dd HH:mm:ss";

				SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT3);
				Date currentDate = sdf.parse(aib.getCurrentmilliseconds()+"");
				dateFromDb = format.parse(aib.getCreateddate()+"");

				//in milliseconds
				long diff = currentDate.getTime() - dateFromDb.getTime();

				long diffSeconds = diff / 1000 % 60;
				long diffMinutes = diff / (60 * 1000) % 60;
				long diffHours = diff / (60 * 60 * 1000) % 24;
				long diffDays = diff / (24 * 60 * 60 * 1000);

				String timeBuffer="";

				if(diffDays==0){
					if(diffHours==0){
						if(diffMinutes==0){
							timeBuffer=diffSeconds+" secs";
						}
						else{
							if(diffSeconds==0)
								timeBuffer=diffMinutes+" mins ";
							else
								timeBuffer=diffMinutes+" mins "+diffSeconds+" secs";
						}
					}
					else{
						if(diffMinutes==0)
							timeBuffer=diffHours+" hrs ";
						else
							timeBuffer=diffHours+" hrs "+diffMinutes+" mins ";
					}
				}
				else{
					if(diffHours==0)
						timeBuffer=diffDays+" days ";
					else
						timeBuffer=diffDays+" days "+diffHours+" hrs ";
				}

				createdDateTime=timeBuffer;		

				final String OLD_FORMAT = "yyyy-MM-dd";
				final String NEW_FORMAT = "MM/dd/yyyy";

				// 2015-11-29
				String oldDateString = aib.getCreateddate()+"";

				SimpleDateFormat sdf1 = new SimpleDateFormat(OLD_FORMAT);
				Date d = sdf1.parse(oldDateString);
				sdf1.applyPattern(NEW_FORMAT);
				createdDateParsed = sdf1.format(d);		//29/11/2015


				if(aib.getFromid()==-100){
					aib.setForwardedby("Patient");
				}

				if(aib.getModifiedbyid()==-100){
					aib.setModifiedbyname("Patient");
				}

				if(aib.getToid()==0){
					aib.setReceivedto("All");
				}
				else if(aib.getToid()==-1){
					aib.setReceivedto("Doctors");
				}
				else if(aib.getToid()==-10){
					aib.setReceivedto("Nurse Practitioner");
				}
				else if(aib.getToid()==-6){
					aib.setReceivedto("MA");
				}
				else if(aib.getToid()==-7){
					aib.setReceivedto("Office Manager");
				}
				else if(aib.getToid()==-2){
					aib.setReceivedto("Nurse");
				}
				else if(aib.getToid()==-3){
					aib.setReceivedto("Front Desk");
				}
				else if(aib.getToid()==-5){
					aib.setReceivedto("Admin");
				}

				aib.setCreateddate(createdDateParsed);
				aib.setCreateddatetime(createdDateTime);

				alertList.add(aib);

			}
		}
		catch(Exception exception){

		}
		return alertList;
	}

	/**
	 * Highlight the list of alerts
	 */
	@Override
	public List<AlertEvent> hightlightAlert(List<Integer> alertIdList, Integer userId) {
		List<AlertEvent> alertEventList=alertInboxRepository.findAll(AlertInboxSpecification.byAlertId(alertIdList));
		AlertEvent aeUpdate = null;
		for (AlertEvent alertEvent : alertEventList) {
			//Take individual bean to update the value
			AlertEvent ae=alertEvent;
			//Highlight the alert
			ae.setAlertEventHighlight(true);
			//Set modified date
			Date date= new Date();
			ae.setAlertEventModifiedby(userId);
			ae.setAlertEventModifiedDate(new Timestamp(date.getTime()));
			aeUpdate=ae;
			//Update the bean(row).
			alertInboxRepository.saveAndFlush(aeUpdate);
		}

		return alertEventList;
	}


	/**
	 * Un highlight list of alerts  
	 */
	@Override
	public List<AlertEvent> unHightlightAlert(List<Integer> alertIdList, Integer userId) {
		List<AlertEvent> alertEventList=alertInboxRepository.findAll(AlertInboxSpecification.byAlertId(alertIdList));
		AlertEvent aeUpdate = null;
		for (AlertEvent alertEvent : alertEventList) {
			//Take individual bean to update the value
			AlertEvent ae=alertEvent;
			//Un highlight the alert
			ae.setAlertEventHighlight(false);
			//Set modified date
			Date date= new Date();
			ae.setAlertEventModifiedby(userId);
			ae.setAlertEventModifiedDate(new Timestamp(date.getTime()));
			aeUpdate=ae;
			//Update the bean(row).
			alertInboxRepository.saveAndFlush(aeUpdate);
		}

		return alertEventList;
	}


	/**
	 * Change the read status of list of alerts to true 
	 */
	@Override
	public List<AlertEvent> markReadAlert(List<Integer> alertIdList, Integer userId) {
		List<AlertEvent> alertEventList=alertInboxRepository.findAll(AlertInboxSpecification.byAlertId(alertIdList));
		AlertEvent aeUpdate = null;
		for (AlertEvent alertEvent : alertEventList) {
			Date date= new Date();
			//Take individual bean to update the value
			AlertEvent ae=alertEvent;
			//Un highlight the alert
			ae.setAlertEventRead(true);
			ae.setAlertEventReadby(userId);
			ae.setAlertEventModifiedby(userId);
			ae.setAlertEventReadDate(new Timestamp(date.getTime()));
			//Set modified date
			ae.setAlertEventModifiedDate(new Timestamp(date.getTime()));
			aeUpdate=ae;
			//Update the bean(row).
			alertInboxRepository.saveAndFlush(aeUpdate);
		}

		return alertEventList;
	}


	/**
	 * Change the read status of list of alerts to false 
	 */
	@Override
	public List<AlertEvent> markUnReadAlert(List<Integer> alertIdList, Integer userId) {
		List<AlertEvent> alertEventList=alertInboxRepository.findAll(AlertInboxSpecification.byAlertId(alertIdList));
		AlertEvent aeUpdate = null;
		for (AlertEvent alertEvent : alertEventList) {
			Date date= new Date();
			//Take individual bean to update the value
			AlertEvent ae=alertEvent;
			//Un highlight the alert
			ae.setAlertEventRead(false);
			ae.setAlertEventReadby(null);
			ae.setAlertEventModifiedby(userId);
			ae.removeAlertEventReadDate();
			//Set modified date
			ae.setAlertEventModifiedDate(new Timestamp(date.getTime()));
			aeUpdate=ae;
			//Update the bean(row).
			alertInboxRepository.saveAndFlush(aeUpdate);
		}

		return alertEventList;
	}


	/**
	 * Get list of categories based on the category id
	 */
	@Override
	public List<AlertCategory> getCategories(List<Integer> categoryIdList) {
		List<AlertCategory> alertCategoryList=alertCategoryRepository.findAll(AlertInboxSpecification.byCategoryId(categoryIdList));
		return alertCategoryList;
	}


	/**
	 * Forward the list of alerts
	 */
	@Override
	public List<AlertEvent> forwardAlerts(List<Integer> alertIdList, String categoryId, String message, String userId, String forwardTo,	String ishighpriority) {
		List<AlertEvent> alertEventList=alertInboxRepository.findAll(AlertInboxSpecification.byAlertId(alertIdList));
		AlertEvent aeUpdate = null;

		for (AlertEvent alertEvent : alertEventList) {
			//Take individual bean to update the value
			AlertEvent ae=alertEvent;
			//Un highlight the alert
			ae.setAlertEventRead(false);
			ae.setAlertEventReadby(0);
			ae.removeAlertEventReadDate();
			ae.setAlertEventCategoryId(Integer.parseInt(categoryId));
			ae.setAlertEventMessage(message);
			ae.setAlertEventFrom(Integer.parseInt(userId));
			ae.setAlertEventTo(Integer.parseInt(forwardTo));
			ae.setAlertEventHighlight(Boolean.parseBoolean(ishighpriority));
			aeUpdate=ae;
			//Update the bean(row).
			alertInboxRepository.saveAndFlush(aeUpdate);
		}

		return alertEventList;
	}

	@Override
	public List<AlertEvent> deleteAlert(List<Integer> alertIdList, Integer userId) {
		List<AlertEvent> alertEventList=alertInboxRepository.findAll(AlertInboxSpecification.byAlertId(alertIdList));
		for (AlertEvent alertEvent : alertEventList) {
			//Take individual bean to update the value
			AlertEvent ae=alertEvent;
			//Change the status of alert
			ae.setAlertEventStatus(2);		//2 for closing alert
			//Set modified date
			Date date= new Date();
			ae.setAlertEventModifiedby(userId);
			ae.setAlertEventModifiedDate(new Timestamp(date.getTime()));
			ae.setAlertEventClosedDate(new Timestamp(date.getTime()));
			alertInboxRepository.saveAndFlush(ae);

			AlertArchive aa=new AlertArchive();//insert row in alert_archive from alert_event
			aa.setAlertEventCategoryId(ae.getAlertEventCategoryId());
			aa.setAlertEventChartId(ae.getAlertEventChartId());
			aa.setAlertEventClosedDate(ae.getAlertEventClosedDate());
			aa.setAlertEventCreatedDate(ae.getAlertEventCreatedDate());
			aa.setAlertEventEncounterId(ae.getAlertEventEncounterId());
			aa.setAlertEventFrom(ae.getAlertEventFrom());
			aa.setAlertEventFrompage(ae.getAlertEventFrompage());
			aa.setAlertEventHighlight(ae.getAlertEventHighlight());
			aa.setAlertEventId(ae.getAlertEventId());
			aa.setAlertEventIsgroupalert(ae.getAlertEventIsgroupalert());
			aa.setAlertEventMessage(ae.getAlertEventMessage());
			aa.setAlertEventModifiedby(ae.getAlertEventModifiedby());
			if(ae.getAlertEventModifiedDate()!=null)
				aa.setAlertEventModifiedDate(ae.getAlertEventModifiedDate());
			aa.setAlertEventParentalertid(ae.getAlertEventParentalertid());
			aa.setAlertEventPatientId(ae.getAlertEventPatientId());
			aa.setAlertEventPatientName(ae.getAlertEventPatientName());
			aa.setAlertEventRead(ae.getAlertEventRead());
			aa.setAlertEventReadby(ae.getAlertEventReadby());
			if(ae.getAlertEventReadDate()!=null)
				aa.setAlertEventReadDate(ae.getAlertEventReadDate());
			aa.setAlertEventRefId(ae.getAlertEventRefId());
			aa.setAlertEventRoomId(ae.getAlertEventRoomId());
			aa.setAlertEventStatus(ae.getAlertEventStatus());
			aa.setAlertEventStatus1(ae.getAlertEventStatus1());
			aa.setAlertEventTo(ae.getAlertEventTo());
			aa.setAlertEventUnknown(ae.getAlertEventUnknown());
			alertArchiveRepository.saveAndFlush(aa);

			alertInboxRepository.delete(ae); //delete a row from alert_event table
		}

		return alertEventList;
	}

	/**
	 * which is used to create new alerts in alert_event table
	 * it will returns the newly created alerts
	 */
	@Override
	public List<AlertEvent> composeAlert(int fromId, List<Integer> toIdList,
			int status, int categoryId, int refId, int patientId,
			int encounterId, String msg, int chartId, int roomId, int parentId) {

		String lastName="", firstName="", midInitial="";
		int  refid=refId;

		if(categoryId==37){
			CriteriaBuilder qb = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq = qb.createQuery();

			Root<AlertEvent> root = cq.from(AlertEvent.class);
			cq.select(qb.max(root.get(AlertEvent_.alertEventRefId)));
			cq.where(qb.equal(root.get(AlertEvent_.alertEventCategoryId), 37));

			Query query=em.createQuery(cq);
			Object maxIdObj=query.getSingleResult();

			if(maxIdObj!=null)
				refid = (int) maxIdObj;

			if(refid < 1)
				refid = 1;
		}
		PatientRegistration patient=patientRegistrationRepository.findOne(PatientRegistrationSpecification.PatientId(patientId+""));
		if(patient!=null){

			lastName=Optional.fromNullable(patient.getPatientRegistrationLastName()).or(" ");
			firstName=Optional.fromNullable(patient.getPatientRegistrationFirstName()).or(" ");
			midInitial=Optional.fromNullable(patient.getPatientRegistrationMidInitial()).or(" ");
		}

		List<AlertEvent> alertEventList=null;
		List<Integer> alertId=new ArrayList<>();
		for(int i=0;i<toIdList.size();i++)
		{
			AlertEvent aug=new AlertEvent();
			aug.setAlertEventFrom(fromId);
			aug.setAlertEventTo(toIdList.get(i));
			aug.setAlertEventStatus(status);
			aug.setAlertEventCategoryId(categoryId);
			aug.setAlertEventRefId(refid);
			aug.setAlertEventPatientId(patientId);
			if(patient!=null)
				aug.setAlertEventPatientName(lastName+", "+firstName+" "+midInitial);		//Lastname, Firstname MidInitial
			aug.setAlertEventEncounterId(encounterId);
			aug.setAlertEventCreatedDate(new Timestamp(new Date().getTime()));
			aug.setAlertEventMessage(msg);
			aug.setAlertEventModifiedby(fromId);
			aug.setAlertEventChartId(chartId);
			aug.setAlertEventRoomId(roomId);
			aug.setAlertEventParentalertid(parentId);
			alertInboxRepository.save(aug);
			alertId.add(aug.getAlertEventId());
		}
		alertEventList=alertInboxRepository.findAll(AlertInboxSpecification.byAlertId(alertId));
		return alertEventList;

	}

	@Override
	public List<AlertEvent> deleteAlertByEncounter(List<Integer> encounterIdList, Integer userId) {

		List<AlertEvent> alertEventList=alertInboxRepository.findAll(AlertInboxSpecification.byEncounterId(encounterIdList));

		for (AlertEvent alertEvent : alertEventList) {
			//Take individual bean to update the value
			AlertEvent ae=alertEvent;	
			//Change the status of alert
			ae.setAlertEventStatus(2);		//2 for closing alert
			//Set modified date
			Date date= new Date();
			ae.setAlertEventModifiedby(userId);
			ae.setAlertEventClosedDate(new Timestamp(date.getTime()));
			ae.setAlertEventModifiedDate(new Timestamp(date.getTime()));
			alertInboxRepository.saveAndFlush(ae);

			AlertArchive aa=new AlertArchive();//insert row in alert_archive from alert_event
			aa.setAlertEventCategoryId(ae.getAlertEventCategoryId());
			aa.setAlertEventChartId(ae.getAlertEventChartId());
			aa.setAlertEventClosedDate(ae.getAlertEventClosedDate());
			aa.setAlertEventCreatedDate(ae.getAlertEventCreatedDate());
			aa.setAlertEventEncounterId(ae.getAlertEventEncounterId());
			aa.setAlertEventFrom(ae.getAlertEventFrom());
			aa.setAlertEventFrompage(ae.getAlertEventFrompage());
			aa.setAlertEventHighlight(ae.getAlertEventHighlight());
			aa.setAlertEventId(ae.getAlertEventId());
			aa.setAlertEventIsgroupalert(ae.getAlertEventIsgroupalert());
			aa.setAlertEventMessage(ae.getAlertEventMessage());
			aa.setAlertEventModifiedby(ae.getAlertEventModifiedby());
			if(ae.getAlertEventModifiedDate()!=null)
				aa.setAlertEventModifiedDate(ae.getAlertEventModifiedDate());
			aa.setAlertEventParentalertid(ae.getAlertEventParentalertid());
			aa.setAlertEventPatientId(ae.getAlertEventPatientId());
			aa.setAlertEventPatientName(ae.getAlertEventPatientName());	
			aa.setAlertEventRead(ae.getAlertEventRead());
			aa.setAlertEventReadby(ae.getAlertEventReadby());
			if(ae.getAlertEventReadDate()!=null)
				aa.setAlertEventReadDate(ae.getAlertEventReadDate());
			aa.setAlertEventRefId(ae.getAlertEventRefId());
			aa.setAlertEventRoomId(ae.getAlertEventRoomId());
			aa.setAlertEventStatus(ae.getAlertEventStatus());
			aa.setAlertEventStatus1(ae.getAlertEventStatus1());
			aa.setAlertEventTo(ae.getAlertEventTo());
			aa.setAlertEventUnknown(ae.getAlertEventUnknown());
			alertArchiveRepository.saveAndFlush(aa);

			alertInboxRepository.delete(ae); //delete a row from alert_event table

		}

		return alertEventList;
	}

	@Override
	public List<AlertEvent> alertByEncounter(List<Integer> encounterIdList) {
		List<AlertEvent> alertEventList=alertInboxRepository.findAll(AlertInboxSpecification.byEncounterId(encounterIdList));
		List<AlertArchive> alertArchiveList=alertArchiveRepository.findAll(AlertArchiveSpecification.byEncounterId(encounterIdList));

		for (AlertArchive aa : alertArchiveList) {

			AlertEvent ae=new AlertEvent();
			ae.setAlertEventCategoryId(aa.getAlertEventCategoryId());
			ae.setAlertEventChartId(aa.getAlertEventChartId());
			ae.setAlertEventClosedDate(aa.getAlertEventClosedDate());
			ae.setAlertEventCreatedDate(aa.getAlertEventCreatedDate());
			ae.setAlertEventEncounterId(aa.getAlertEventEncounterId());
			ae.setAlertEventFrom(aa.getAlertEventFrom());
			ae.setAlertEventFrompage(aa.getAlertEventFrompage());
			ae.setAlertEventHighlight(aa.getAlertEventHighlight());
			ae.setAlertEventId(aa.getAlertEventId());
			ae.setAlertEventIsgroupalert(aa.getAlertEventIsgroupalert());
			ae.setAlertEventMessage(aa.getAlertEventMessage());
			ae.setAlertEventModifiedby(aa.getAlertEventModifiedby());
			ae.setAlertEventModifiedDate(aa.getAlertEventModifiedDate());
			ae.setAlertEventParentalertid(aa.getAlertEventParentalertid());
			ae.setAlertEventPatientId(aa.getAlertEventPatientId());
			ae.setAlertEventPatientName(aa.getAlertEventPatientName());
			ae.setAlertEventRead(aa.getAlertEventRead());
			ae.setAlertEventReadby(aa.getAlertEventReadby());
			if(aa.getAlertEventReadDate()==null)
				ae.setAlertEventReadDate(new Timestamp(0));
			else
				ae.setAlertEventReadDate(aa.getAlertEventReadDate());
			ae.setAlertEventRefId(aa.getAlertEventRefId());
			ae.setAlertEventRoomId(aa.getAlertEventRoomId());
			ae.setAlertEventStatus(aa.getAlertEventStatus());
			ae.setAlertEventStatus1(aa.getAlertEventStatus1());
			ae.setAlertEventTo(aa.getAlertEventTo());
			ae.setAlertEventUnknown(aa.getAlertEventUnknown());
			ae.setEmpProfileTableFrom(aa.getEmpProfileTableFrom());
			ae.setEmpProfileTableModifiedBy(aa.getEmpProfileTableModifiedBy());
			ae.setEmpProfileTableReadBy(aa.getEmpProfileTableReadBy());
			ae.setEmpProfileTableTo(aa.getEmpProfileTableTo());
			alertEventList.add(ae);
		}

		return alertEventList;
	}

	@Override
	public AlertEvent getAlertId(Integer patientId, Integer encounterId, Integer refId) {
		AlertEvent alertEvent = alertInboxRepository.findOne(AlertInboxSpecification.getAlertData(patientId, encounterId, refId));
		return alertEvent;
	}

	@Override
	public Integer getCategoryId(Integer section, Integer actionMap) {
		AlertCategory alertCategory = alertCategoryRepository.findOne(AlertCategorySpecification.checkSectionAndActionmap(section,actionMap));
		return alertCategory.getAlertCategoryId();
	}

	@Override
	public Integer getAlertCategoryId(Integer actionMap, Integer catType) {
		AlertCategory alertCategory = alertCategoryRepository.findOne(AlertCategorySpecification.checkTypeAndActionmap(actionMap, catType));
		return alertCategory.getAlertCategoryId();
	}

	@Override
	public List<AlertEvent> getAlertsByEncIdAndCatId(Integer encounterId,
			Integer categoryId) {

		List<AlertEvent> alerts=alertInboxRepository.findAll(AlertInboxSpecification.byEncIdAndCatId(encounterId,categoryId));
		return alerts;
	}
	
	/**
	 *method which is used to get conversion based on parent id from alert_archive table 
	 */
	@Override
	public List<AlertEvent> getConversion(String alertid) {
		List<Integer> alertId = new ArrayList<Integer>();
		alertId.add(Integer.parseInt(alertid));

		AlertEvent activeAlert = alertInboxRepository.findOne(AlertInboxSpecification.byAlertId(alertId));
		int parentId = activeAlert.getAlertEventParentalertid();

		List<AlertEvent> alerts = new ArrayList<AlertEvent>();

		if(parentId!=-1){
			List<AlertArchive> inactiveAlerts = alertArchiveRepository.findAll(AlertInboxSpecification.inactiveAlertsByParentid(parentId,37));
			for(int i=0;i<inactiveAlerts.size();i++){

				AlertArchive aa = inactiveAlerts.get(i);
				AlertEvent ae = new AlertEvent();

				ae.setAlertEventCategoryId(aa.getAlertEventCategoryId());
				ae.setAlertEventChartId(aa.getAlertEventChartId());
				if(aa.getAlertEventClosedDate()!=null) 
					ae.setAlertEventClosedDate(aa.getAlertEventClosedDate());
				ae.setAlertEventCreatedDate(aa.getAlertEventCreatedDate());
				ae.setAlertEventEncounterId(aa.getAlertEventEncounterId());
				ae.setAlertEventFrom(aa.getAlertEventFrom());
				ae.setAlertEventFrompage(aa.getAlertEventFrompage());
				ae.setAlertEventHighlight(aa.getAlertEventHighlight());
				ae.setAlertEventId(aa.getAlertEventId());
				ae.setAlertEventIsgroupalert(aa.getAlertEventIsgroupalert());
				ae.setAlertEventMessage(aa.getAlertEventMessage());
				ae.setAlertEventModifiedby(aa.getAlertEventModifiedby());
				if(aa.getAlertEventModifiedDate()!=null)
					ae.setAlertEventModifiedDate(aa.getAlertEventModifiedDate());
				ae.setAlertEventParentalertid(aa.getAlertEventParentalertid());
				ae.setAlertEventPatientId(aa.getAlertEventPatientId());
				ae.setAlertEventPatientName(aa.getAlertEventPatientName());
				ae.setAlertEventRead(aa.getAlertEventRead());
				ae.setAlertEventReadby(aa.getAlertEventReadby());
				if(aa.getAlertEventReadDate()!=null)
					ae.setAlertEventReadDate(aa.getAlertEventReadDate());
				ae.setAlertEventRefId(aa.getAlertEventRefId());
				ae.setAlertEventRoomId(aa.getAlertEventRoomId());
				ae.setAlertEventStatus(aa.getAlertEventStatus());
				ae.setAlertEventStatus1(aa.getAlertEventStatus1());
				ae.setAlertEventTo(aa.getAlertEventTo());
				ae.setAlertEventUnknown(aa.getAlertEventUnknown());
				ae.setEmpProfileTableFrom(aa.getEmpProfileTableFrom());

				alerts.add(ae);
			}
		}
		alerts.add(activeAlert);
		return alerts;
	}
	
	/**
	 * method which is used to create new alert and it will delete existing alert and before that insert an alert to alert_archive
	 */
	@Override
	public List<AlertEvent> forwardIcmAlert(Integer alertid, Integer userId, Integer encounterid,Integer patientid,
			Integer categoryid, Integer forwardto, String message,
			Integer parentalertid) {

		List<AlertEvent> alerts=null;
		int chartid = -1;
		int encId=-1;
		int roomid=-1;

		List<Integer> alertIdList = new ArrayList<Integer>();
		alertIdList.add(alertid);

		try{
			if(patientid!=-1111 && patientid!=-1){
				AlertEvent alertInfo = alertInboxRepository.findOne(AlertInboxSpecification.byAlertId(alertIdList));

				if(alertInfo != null){
					chartid = alertInfo.getAlertEventChartId();
					encId = alertInfo.getAlertEventEncounterId();

					if(encounterid==-1)
						encounterid=encId;
					roomid = alertInfo.getAlertEventRoomId();
				}
			}
		}catch(Exception ex){}

		List<Integer> forwardtoList = new ArrayList<Integer>();
		forwardtoList.add(forwardto);

		alerts=composeAlert(userId,forwardtoList,1,categoryid,-1,patientid,encounterid,message,chartid,roomid,parentalertid);

		deleteAlert(alertIdList, userId);
		return alerts;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map<String, List<Object>>> searchAlerts(String userId,
			List<String> categoryIdList, String patientNameSearchValue,
			String senderNameSearchValue, String receiverNameSearchValue,
			String messageSearchValue, String fromDateSearchValue,
			String toDateSearchValue) {
		List<Map<String, List<Object>>> arrayAlertList=new ArrayList<Map<String,List<Object>>>();
		
		if(categoryIdList.get(0).equalsIgnoreCase("-1"))
		{
			List<AlertCountBean> totalShortCategory = getAlertCount(userId.toString());
			for(int i=0;i<totalShortCategory.size();i++)
			{
				categoryIdList.add(totalShortCategory.get(i).getCategoryId());
			}
		}

		for(int i=0;i<categoryIdList.size();i++){

			List<Object> alertList=new ArrayList<Object>();
			Integer categoryId=new Integer(categoryIdList.get(i));
			List<Object> alertsByCategories=getAlertsBySearch(categoryId,userId,patientNameSearchValue,
					senderNameSearchValue,receiverNameSearchValue,messageSearchValue,fromDateSearchValue,
					toDateSearchValue);
			int unreadalerts=0;
			
			//mapAlertsObject.put("alertcategory",new ArrayList<>());
			if(alertsByCategories.size()>0)
			{
				Map mapAlertsObject=new HashMap();
				for(int j=0;j<alertsByCategories.size();j++){
	
					AlertInboxBean aib=(AlertInboxBean) alertsByCategories.get(j);
					String createdDateParsed="",createdDateTime="";
	
					//HH converts hour in 24 hours format (0-23), day calculation
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
					Date dateFromDb = null;
	
					final String OLD_FORMAT3 =  "yyyy-MM-dd HH:mm:ss";
	
					SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT3);
					Date currentDate = null;
					try {
						currentDate = sdf.parse(aib.getCurrentmilliseconds()+"");
						dateFromDb = format.parse(aib.getCreateddate()+"");
					} catch (ParseException e) {
						e.printStackTrace();
					}
	
					//in milliseconds
					long diff = currentDate.getTime() - dateFromDb.getTime();
	
					long diffSeconds = diff / 1000 % 60;
					long diffMinutes = diff / (60 * 1000) % 60;
					long diffHours = diff / (60 * 60 * 1000) % 24;
					long diffDays = diff / (24 * 60 * 60 * 1000);
	
					String timeBuffer="";
	
					if(diffDays==0){
						if(diffHours==0){
							if(diffMinutes==0){
								timeBuffer=diffSeconds+" secs";
							}
							else{
								if(diffSeconds==0)
									timeBuffer=diffMinutes+" mins ";
								else
									timeBuffer=diffMinutes+" mins "+diffSeconds+" secs";
							}
						}
						else{
							if(diffMinutes==0)
								timeBuffer=diffHours+" hrs ";
							else
								timeBuffer=diffHours+" hrs "+diffMinutes+" mins ";
						}
					}
					else{
						if(diffHours==0)
							timeBuffer=diffDays+" days ";
						else
							timeBuffer=diffDays+" days "+diffHours+" hrs ";
					}
	
					createdDateTime=timeBuffer;		
	
					final String OLD_FORMAT = "yyyy-MM-dd";
					final String NEW_FORMAT = "MM/dd/yyyy";
	
					// 2015-11-29
					String oldDateString = aib.getCreateddate()+"";
	
					SimpleDateFormat sdf1 = new SimpleDateFormat(OLD_FORMAT);
					Date d = null;
					try {
						d = sdf1.parse(oldDateString);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					sdf1.applyPattern(NEW_FORMAT);
					createdDateParsed = sdf1.format(d);		//29/11/2015
	
	
					if(aib.getFromid()==-100){
						aib.setForwardedby("Patient");
					}
	
					if(aib.getModifiedbyid()==-100){
						aib.setModifiedbyname("Patient");
					}
	
					if(aib.getToid()==0){
						aib.setReceivedto("All");
					}
					else if(aib.getToid()==-1){
						aib.setReceivedto("Doctors");
					}
					else if(aib.getToid()==-10){
						aib.setReceivedto("Nurse Practitioner");
					}
					else if(aib.getToid()==-6){
						aib.setReceivedto("MA");
					}
					else if(aib.getToid()==-7){
						aib.setReceivedto("Office Manager");
					}
					else if(aib.getToid()==-2){
						aib.setReceivedto("Nurse");
					}
					else if(aib.getToid()==-3){
						aib.setReceivedto("Front Desk");
					}
					else if(aib.getToid()==-5){
						aib.setReceivedto("Admin");
					}
	
					if(!aib.getRead()){
						unreadalerts++;
					}
	
					aib.setCreateddate(createdDateParsed);
					aib.setCreateddatetime(createdDateTime);
	
					if(j==0){
						List<Object> alertCategoryDetailList=new ArrayList<Object>();
						AlertCategoryBean acb=new AlertCategoryBean(
								aib.getAlerttype(), 
								aib.getQrflag(), 
								aib.getSubpage(), 
								aib.getStatus(), 
								aib.getAlertgroup(), 
								aib.getCategoryid(), 
								aib.getDisplayname(), 
								aib.getExpanded(), 
								aib.getAlertsection(), 
								aib.getActionmap(), 
								aib.getCategoryorder(), 
								aib.getCategoryname(), 
								aib.getNeeddatewisegrouping(),
								aib.getQrurl());
						alertCategoryDetailList.add(acb);
						mapAlertsObject.put("alertcategory",alertCategoryDetailList);
					}
					
					alertList.add(aib);
				}
				mapAlertsObject.put("categorydetail", alertList);
				mapAlertsObject.put("totalalerts",alertList.size());
				mapAlertsObject.put("unreadalerts",unreadalerts);
				arrayAlertList.add(mapAlertsObject);
			}
		}

		return arrayAlertList;
	}

	
	@SuppressWarnings("unchecked")
	public List<Object> getAlertsBySearch(Integer categoryId, String userId, 
			String patientNameSearchValue, String senderNameSearchValue, 
			String receiverNameSearchValue, String messageSearchValue, 
			String fromDateSearchValue, String toDateSearchValue) {
		logger.trace("Creating alerts query.");
		List<Integer> userIdList=constructUserList(userId);
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<AlertCategory> rootAlCate = cq.from(AlertCategory.class);
		Join<AlertCategory,AlertEvent> joinAcAe=rootAlCate.join("alertEventCategoryId",JoinType.INNER);
		Predicate[] predicateCateIdStatus = new Predicate[] {
				joinAcAe.get(AlertEvent_.alertEventTo).in(userIdList),
				builder.equal(joinAcAe.get(AlertEvent_.alertEventStatus), new Integer(1))
		};
		joinAcAe.on(predicateCateIdStatus);
		Join<Join<AlertCategory,AlertEvent>,Room> joinAcAeR=joinAcAe.join("roomTable",JoinType.LEFT);
		Join<AlertCategory,AlertPrivilege> joinAcAp=rootAlCate.join("alertPrivilegeTable",JoinType.INNER);
		Predicate predicateApUserId=builder.equal(joinAcAp.get(AlertPrivilege_.alert_privilege_user_id), Integer.parseInt(userId));
		joinAcAp.on(predicateApUserId);
		Join<Join<AlertEvent,AlertCategory>,Chart> joinAeChart=joinAcAe.join("chartTable",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,EmployeeProfile> joinAeEmpFrom=joinAcAe.join("empProfileTableFrom",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,EmployeeProfile> joinAeEmpTo=joinAcAe.join("empProfileTableTo",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,EmployeeProfile> joinAeEmpReadBy=joinAcAe.join("empProfileTableReadBy",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,EmployeeProfile> joinAeEmpModBy=joinAcAe.join("empProfileTableModifiedBy",JoinType.LEFT);
		Join<Join<AlertEvent,AlertCategory>,Encounter> joinAeEncId=joinAcAe.join("encounterTableId",JoinType.LEFT);
		Join<Chart,PatientRegistration> joinChartPatReg=joinAeChart.join("patientRegistrationTable",JoinType.LEFT);
		Join<Encounter,EmployeeProfile> joinEncEmp=joinAeEncId.join("empProfileEmpId",JoinType.LEFT);
		
		List<Predicate> predList = new LinkedList<Predicate>();
		if(!patientNameSearchValue.equalsIgnoreCase("-1"))
		{
			Predicate patientNameSearchPred = builder.equal(joinAcAe.get(AlertEvent_.alertEventPatientId), Integer.parseInt(patientNameSearchValue));
			predList.add(patientNameSearchPred);
		}
		if(!senderNameSearchValue.equalsIgnoreCase("-1"))
		{
			Predicate senderNameSearchPred = builder.equal(joinAeEmpFrom.get(EmployeeProfile_.empProfileEmpid), Integer.parseInt(senderNameSearchValue));
			predList.add(senderNameSearchPred);
		}
		if(!receiverNameSearchValue.equalsIgnoreCase("-1"))
		{
			Predicate receiverNameSearchPred = builder.equal(joinAeEmpTo.get(EmployeeProfile_.empProfileEmpid), Integer.parseInt(receiverNameSearchValue));
			predList.add(receiverNameSearchPred);
		}
		if(!messageSearchValue.equalsIgnoreCase("-1"))
		{
			Predicate messageSearchPred = builder.like(builder.lower(joinAcAe.get(AlertEvent_.alertEventMessage)), getLikePattern(messageSearchValue));
			predList.add(messageSearchPred);
		}
		if((!fromDateSearchValue.equalsIgnoreCase("-1")))
		{
			Date fromDate = null;
			try {
				fromDate=new SimpleDateFormat("MM/dd/yyyy").parse(fromDateSearchValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Predicate fromSearchPred = builder.greaterThanOrEqualTo(joinAcAe.get(AlertEvent_.alertEventCreatedDate), fromDate);
			predList.add(fromSearchPred);
		} 
		if((!toDateSearchValue.equalsIgnoreCase("-1")))
		{
			Date toDate = null;
			try {
				toDate=new SimpleDateFormat("MM/dd/yyyy").parse(toDateSearchValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Predicate toSearchPred = builder.lessThanOrEqualTo(joinAcAe.get(AlertEvent_.alertEventCreatedDate), toDate);
			predList.add(toSearchPred);
		}
		Predicate searchCond = builder.and(predList.toArray(new Predicate[] {}));
		Expression<Integer> exprCategoryId=joinAcAe.get(AlertEvent_.alertEventId);

		cq.select(builder.construct(AlertInboxBean.class, 
				joinAcAe.get(AlertEvent_.alertEventId).alias("eventId"),
				rootAlCate.get(AlertCategory_.alertCategoryId),
				joinAeChart.get(Chart_.chartId),
				joinChartPatReg.get(PatientRegistration_.patientRegistrationAccountno).alias("accNo"),
				joinAcAe.get(AlertEvent_.alertEventPatientName).alias("patientname"),
				builder.coalesce(joinAcAeR.get(Room_.roomName),"-"),
				rootAlCate.get(AlertCategory_.alertCategoryName),
				rootAlCate.get(AlertCategory_.alertCategoryUrl),
				rootAlCate.get(AlertCategory_.alertCategoryDisplayName),
				rootAlCate.get(AlertCategory_.alertCategoryGroup),
				rootAlCate.get(AlertCategory_.alertCategoryType),	
				joinAcAe.get(AlertEvent_.alertEventFrom),
				joinAcAe.get(AlertEvent_.alertEventTo),
				joinAcAe.get(AlertEvent_.alertEventRefId),
				joinAcAe.get(AlertEvent_.alertEventPatientId),
				builder.coalesce(joinAcAe.get(AlertEvent_.alertEventParentalertid),0),
				joinAcAe.get(AlertEvent_.alertEventEncounterId),
				joinAcAe.get(AlertEvent_.alertEventMessage),
				joinAcAe.get(AlertEvent_.alertEventModifiedby),
				builder.function("to_char", String.class, joinAcAe.get(AlertEvent_.alertEventCreatedDate),builder.literal("yyyy-MM-dd HH:mm:ss")),
				joinAeEmpFrom.get(EmployeeProfile_.empProfileFullname).alias("from"),
				joinAeEmpTo.get(EmployeeProfile_.empProfileFullname).alias("to"),
				joinAeEmpReadBy.get(EmployeeProfile_.empProfileFullname).alias("readBy"),
				joinAeEmpModBy.get(EmployeeProfile_.empProfileFullname).alias("modifiedBy"),
				builder.coalesce(joinAcAe.get(AlertEvent_.alertEventRead),true),
				rootAlCate.get(AlertCategory_.alertCategoryUrlCaption),
				rootAlCate.get(AlertCategory_.alertCategorySubpage),
				builder.function("to_char", String.class, joinAcAe.get(AlertEvent_.alertEventModifiedDate),builder.literal("MM/dd/yyyy HH:MI:ss am")),
				builder.coalesce(joinAcAe.get(AlertEvent_.alertEventHighlight),false),
				rootAlCate.get(AlertCategory_.alertCategorySection),
				rootAlCate.get(AlertCategory_.alertCategoryQrUrl),	
				joinAcAe.get(AlertEvent_.alertEventStatus),
				joinAcAp.get(AlertPrivilege_.alert_privilege_expanded),
				rootAlCate.get(AlertCategory_.alertCategoryActionMap),
				rootAlCate.get(AlertCategory_.alertCategoryDisplayOrder),
				joinAcAp.get(AlertPrivilege_.alert_privilege_date_grouping_needed),
				rootAlCate.get(AlertCategory_.alertCategoryQrFlag),
				joinEncEmp.get(EmployeeProfile_.empProfileFullname),
				builder.function("to_char", String.class, joinAcAe.get(AlertEvent_.alertEventCreatedDate),builder.literal("MM/dd/yyyy HH:MI:ss am")),
				builder.currentTimestamp(),
				builder.function("to_char", String.class, joinAcAe.get(AlertEvent_.alertEventCreatedDate),builder.literal("HH:MI am")),
				joinChartPatReg.get(PatientRegistration_.patientRegistrationPhoneNo),
				builder.coalesce(joinAcAe.get(AlertEvent_.alertEventReadby),0)
				))
				
				.where(builder.isNotNull(exprCategoryId),
						builder.equal(rootAlCate.get(AlertCategory_.alertCategoryId),categoryId),
						searchCond)
						.orderBy(builder.desc(joinAcAe.get(AlertEvent_.alertEventCreatedDate)));

		Query query=em.createQuery(cq);

		List<Object> alertsResultList=query.setMaxResults(10).getResultList();
		System.out.println("sizxx "+alertsResultList.size());

		return alertsResultList;
	}
	
	/**
	 * Returns the formatted the pattern
	 * @param searchTerm
	 * @return
	 */
	private static String getLikePattern(final String searchTerm) {
		StringBuilder pattern = new StringBuilder();
		pattern.append("%");
		pattern.append(searchTerm.toLowerCase());
		pattern.append("%");
		return pattern.toString();
	}

}