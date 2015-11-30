package com.glenwood.glaceemr.server.application.services.alertinbox;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
				rootAlertCategory.get(AlertCategory_.alertCategoryDisplayOrder)));
		cq.groupBy(rootAlertCategory.get(AlertCategory_.alertCategoryId),
				rootAlertCategory.get(AlertCategory_.alertCategoryName),
//				joinAcAI.get(AlertEvent_.alertEventRead),
				rootAlertCategory.get(AlertCategory_.alertCategoryDisplayOrder)
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

		List<Long> alertUnreadList=new ArrayList<Long>();
		List<String> categoryIdList=new ArrayList<String>();
		List<String> categoryOrderList=new ArrayList<String>();
		List<String> categoryNameList=new ArrayList<String>();
		List<AlertCountBean> alertCountList=new ArrayList<AlertCountBean>();


		try {

			for(int i=0;i<getAlertCounts.size();i++){
				AlertCountBean sib=(AlertCountBean) getAlertCounts.get(i);

				long unReadCount=sib.getTotalCount();
				String categoryId=sib.getCategoryId();
				String categoryName=sib.getCategoryName();
				String categoryOrder=sib.getCategoryorder();

				alertUnreadList.add(unReadCount);
				categoryIdList.add(categoryId);
				categoryOrderList.add(categoryOrder);
				categoryNameList.add(categoryName);
			}

			for(int i=0;i<getAlertCounts.size();i++){
				AlertCountBean sib=(AlertCountBean) getAlertCounts.get(i);

				Long unReadCount=(long) 0;
				Long totalCount=alertUnreadList.get(i);
				String categoryId=categoryIdList.get(i);
				String categoryName=categoryNameList.get(i);
				String categoryOrder=categoryOrderList.get(i);

//				System.out.println("Category Id>>"+categoryId+"\tCategory Name>>"+categoryName+"\tCount>>"+totalCount);
				categoryNameList.set(i, "-1");

				for(int j=0;j<categoryNameList.size();j++){
					String tempCategoryName=categoryNameList.get(j);
					Long temp=alertUnreadList.get(j);
					if(categoryName.equals(tempCategoryName)&&!(tempCategoryName.equals("-1"))){
						unReadCount=temp;
						totalCount+=unReadCount;
						categoryNameList.set(j, "-1");
						break;
					}
				}

				if(!categoryName.equals("-1")){
					sib.setTotalCount(totalCount);
					sib.setCategoryId(categoryId);
					sib.setCategoryName(categoryName);
					sib.setCategoryorder(categoryOrder);
					sib.setUnReadCount(unReadCount);

					alertCountList.add(sib);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return alertCountList;
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

		List<Long> shortUnreadList=new ArrayList<Long>();
		List<String> shortCategoryIdList=new ArrayList<String>();
		List<String> categoryorderList=new ArrayList<String>();
		List<String> shortNameList=new ArrayList<String>();
		List<Object> shortcutList=new ArrayList<Object>();

		List<Map<String, List<Map<String, List<Object>>>>> alertsArray=new ArrayList<Map<String, List<Map<String, List<Object>>>>>();
		Map map=new HashMap();
		List<Map<String, List<Object>>> alertList=new ArrayList<Map<String,List<Object>>>();

		try {

			for(int i=0;i<getShortCutList.size();i++){
				AlertCountBean sib= getShortCutList.get(i);

				long shortUnread=sib.getTotalCount();
				String shortCategoryId=sib.getCategoryId();
				String shortName=sib.getCategoryName();
				String categoryorder=sib.getCategoryorder();

				shortUnreadList.add(shortUnread);
				shortCategoryIdList.add(shortCategoryId);
				categoryorderList.add(categoryorder);
				shortNameList.add(shortName);
			}

			for(int i=0;i<getShortCutList.size();i++){
				AlertCountBean sib=(AlertCountBean) getShortCutList.get(i);

				Long shortUnread=(long) 0;
				Long shortTotal=shortUnreadList.get(i);
				String shortCategoryId=shortCategoryIdList.get(i);
				String shortName=shortNameList.get(i);
				String categoryorder=categoryorderList.get(i);

				shortNameList.set(i, "-1");

				for(int j=0;j<shortNameList.size();j++){
					String tempShortName=shortNameList.get(j);
					Long tempCount=shortUnreadList.get(j);
					if(shortName.equals(tempShortName)&&!(tempShortName.equals("-1"))){
						shortUnread=tempCount;
						shortTotal+=shortUnread;
						shortNameList.set(j, "-1");
						break;
					}
				}

				if(!shortName.equals("-1")){
					sib.setTotalCount(shortTotal);
					sib.setCategoryId(shortCategoryId);
					sib.setCategoryName(shortName);
					sib.setCategoryorder(categoryorder);
					sib.setUnReadCount(shortUnread);

					shortcutList.add(sib);
				}
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
			map.put("shortcutinfo", shortcutList);

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

				if(aib.getRead().equals("false")){
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
		Predicate predicateApUserId=builder.equal(joinAcAp.get(AlertPrivilege_.alert_privilege_user_id), new Integer(49));
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
				joinAeChart.get(Chart_.chart_id),
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
				builder.coalesce(joinAeChart.get(Chart_.chart_id),-1),
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
		query.setFirstResult((pageno-1)*10);	// Page no (offset)
		query.setMaxResults(pagesize);		// Page size (No of results)
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
			ae.setAlertEventReadby(0);
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
		AlertEvent aeUpdate = null;
		for (AlertEvent alertEvent : alertEventList) {
			//Take individual bean to update the value
			AlertEvent ae=alertEvent;
			//Change the status of alert
			ae.setAlertEventStatus(2);		//2 for closing alert
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
	 * which is used to create new alerts in alert_event table
	 * it will returns the newly created alerts
	 */
	@Override
	public List<AlertEvent> composeAlert(int fromId, List<Integer> toIdList,
			int status, int categoryId, int refId, int patientId,
			int encounterId, String msg, int chartId, int roomId, int parentId) {
		
		String lastName="", firstName="", midInitial="";
		PatientRegistration patient=patientRegistrationRepository.findOne(PatientRegistrationSpecification.PatientId(patientId+""));
		

		lastName=Optional.fromNullable(patient.getPatientRegistrationLastName()).or(" ");
		firstName=Optional.fromNullable(patient.getPatientRegistrationFirstName()).or(" ");
		midInitial=Optional.fromNullable(patient.getPatientRegistrationMidInitial()).or(" ");
		
		List<AlertEvent> alertEventList=null;
		List<Integer> alertId=new ArrayList<>();
		for(int i=0;i<toIdList.size();i++)
		{
			AlertEvent aug=new AlertEvent();
			aug.setAlertEventFrom(fromId);
			aug.setAlertEventTo(toIdList.get(i));
			aug.setAlertEventStatus(status);
			aug.setAlertEventCategoryId(categoryId);
			aug.setAlertEventRefId(refId);
			aug.setAlertEventPatientId(patientId);
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
		
		AlertEvent aeUpdate = null;
		for (AlertEvent alertEvent : alertEventList) {
			//Take individual bean to update the value
			AlertEvent ae=alertEvent;
			//Change the status of alert
			ae.setAlertEventStatus(2);		//2 for closing alert
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
}