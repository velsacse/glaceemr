package com.glenwood.glaceemr.server.application.services.commonPicker;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.Trimspec;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glenwood.glaceemr.server.application.models.AccountType;
import com.glenwood.glaceemr.server.application.models.AccountType_;
import com.glenwood.glaceemr.server.application.models.Admission;
import com.glenwood.glaceemr.server.application.models.AppReferenceValues;
import com.glenwood.glaceemr.server.application.models.AppReferenceValues_;
import com.glenwood.glaceemr.server.application.models.AuthorizationMaster;
import com.glenwood.glaceemr.server.application.models.AuthorizationMaster_;
import com.glenwood.glaceemr.server.application.models.BillingConfigTable;
import com.glenwood.glaceemr.server.application.models.BillingConfigTable_;
import com.glenwood.glaceemr.server.application.models.Billinglookup;
import com.glenwood.glaceemr.server.application.models.Billinglookup_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.InsUserGroup;
import com.glenwood.glaceemr.server.application.models.InsUserGroup_;
import com.glenwood.glaceemr.server.application.models.PatientInsDetail;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.ReasonUserGroup;
import com.glenwood.glaceemr.server.application.models.ReasonUserGroupMapping;
import com.glenwood.glaceemr.server.application.models.ReasonUserGroupMapping_;
import com.glenwood.glaceemr.server.application.models.ReasonUserGroup_;
import com.glenwood.glaceemr.server.application.models.ReferringDoctor;
import com.glenwood.glaceemr.server.application.models.ReferringDoctor_;
import com.glenwood.glaceemr.server.application.models.SpecialisationReferring;
import com.glenwood.glaceemr.server.application.models.SpecialisationReferring_;
import com.glenwood.glaceemr.server.application.models.ZipCodesMain;
import com.glenwood.glaceemr.server.application.models.ZipCodesMain_;
import com.glenwood.glaceemr.server.application.repositories.AppReferenceValuesRepository;
import com.glenwood.glaceemr.server.application.repositories.InsUserGroupRepository;
import com.glenwood.glaceemr.server.application.repositories.ReasonUserGroupMappingRepository;
import com.glenwood.glaceemr.server.application.repositories.ReferringDoctorRepository;
import com.glenwood.glaceemr.server.utils.HUtil;
/**
 * 
 * @author Sai Swaroop
 *
 */
@Service
@Transactional
public class CommonPickerServiceImpl implements CommonPickerService{



	@PersistenceContext
	EntityManager em;

	@Autowired
	AppReferenceValuesRepository appReferenceValuesRepository;

	@Autowired
	ReasonUserGroupMappingRepository reasonUserGroupMappingRepository;

	@Autowired
	ReferringDoctorRepository referringDoctorRepository;

	@Autowired
	InsUserGroupRepository insUserGroupRepository;

	JSONObject jsnobj	= new JSONObject();

	/**
	 * Get the Reason picker data 
	 */

	@SuppressWarnings("unused")
	@Override
	public JSONObject getReasonData(String searchCode, String offset, String limit,
			String group, String selectedVal, String userGroupsRequired,
			String dataRequired) {

		JSONArray finalData=new JSONArray();
		int offsetVal=Integer.parseInt(offset);
		int limitVal=Integer.parseInt(limit);
		List<Object[]> reasonlist;
		List<Object[]> reasonListSize;
		String groupId = "";
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq= cb.createQuery(Object[].class);

		CriteriaBuilder cbuilder=em.getCriteriaBuilder();
		CriteriaQuery<Object> cquery=cbuilder.createQuery();
		Root<ReasonUserGroup> root=cquery.from(ReasonUserGroup.class);	

		Root<AppReferenceValues> appReference=cq.from(AppReferenceValues.class);
		Join<AppReferenceValues,ReasonUserGroupMapping> reasonGrpMapping=appReference.join("reasonUserGroupMappings",JoinType.INNER);
		Join<ReasonUserGroupMapping,ReasonUserGroup> reasonGrp=reasonGrpMapping.join("reasonUserGroup",JoinType.INNER);
		if(dataRequired.equals("true")){
			if(group.equals("-1")){
				cquery.select(root.get(ReasonUserGroup_.reasonUserGroupId));
				cquery.where(cbuilder.equal(root.get(ReasonUserGroup_.reasonUserGrpDefault), true));

				List<Object> resultList = em.createQuery(cquery).getResultList();
				for (int i = 0; i < resultList.size(); i++) {
					groupId = resultList.get(i).toString();
				}
			}
			if(groupId.equals("-1")){
				cq.multiselect(appReference.get(AppReferenceValues_.App_Reference_Values_statusName),
						appReference.get(AppReferenceValues_.App_Reference_Values_statusId));

				Predicate[] reasonGrpMappingCondition=new Predicate[]{
						cb.equal(reasonGrpMapping.get(ReasonUserGroupMapping_.reasonUserGrpMapGrpId),new Integer(1))
				};
				reasonGrpMapping.on(reasonGrpMappingCondition);
				Predicate appRefTableId=cb.equal(appReference.get(AppReferenceValues_.App_Reference_Values_tableId),new Integer(402));
				Predicate reasonType=cb.equal(appReference.get(AppReferenceValues_.App_Reference_Values_reason_type),new Integer(1));
				Predicate isActive=cb.equal(appReference.get(AppReferenceValues_.App_Reference_Values_isActive),new Boolean(true));
				Predicate appRefName = cb.like(cb.lower(cb.trim(Trimspec.BOTH,appReference.get(AppReferenceValues_.App_Reference_Values_statusName))),
						searchCode.trim().toLowerCase()+"%");
				cq.where(appRefTableId,reasonType,isActive,appRefName).orderBy(cb.asc(appReference.get(AppReferenceValues_.App_Reference_Values_statusName)));

				reasonlist=em.createQuery(cq).setFirstResult(offsetVal).setMaxResults(limitVal).getResultList();
				reasonListSize=em.createQuery(cq).getResultList();
			}else{
				cq.multiselect(appReference.get(AppReferenceValues_.App_Reference_Values_statusName),
						appReference.get(AppReferenceValues_.App_Reference_Values_statusId));

				Predicate[] reasonGrpMappingCondition=new Predicate[]{
						cb.equal(reasonGrpMapping.get(ReasonUserGroupMapping_.reasonUserGrpMapGrpId),Integer.parseInt(group))
				};
				reasonGrpMapping.on(reasonGrpMappingCondition);
				Predicate appRefTableId=cb.equal(appReference.get(AppReferenceValues_.App_Reference_Values_tableId),new Integer(402));
				Predicate reasonType=cb.equal(appReference.get(AppReferenceValues_.App_Reference_Values_reason_type),new Integer(1));
				Predicate isActive=cb.equal(appReference.get(AppReferenceValues_.App_Reference_Values_isActive),new Boolean(true));
				Predicate appRefName = cb.like(cb.lower(cb.trim(Trimspec.BOTH,appReference.get(AppReferenceValues_.App_Reference_Values_statusName))),
						searchCode.trim().toLowerCase()+"%");
				cq.where(appRefTableId,reasonType,isActive,appRefName).orderBy(cb.asc(appReference.get(AppReferenceValues_.App_Reference_Values_statusName)));

				reasonlist=em.createQuery(cq).setFirstResult(offsetVal).setMaxResults(limitVal).getResultList();
				reasonListSize=em.createQuery(cq).getResultList();
			}
			JSONArray dataArray = new JSONArray();
			try
			{
				for(Object[] value : reasonlist)
				{
					JSONObject statusObject = new JSONObject();
					statusObject.put("col0", value[0]);
					statusObject.put("id",  String.valueOf(value[1]));
					dataArray.put(statusObject);
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			long count=reasonListSize.size();
			try {
				JSONArray pageData=getNormalPageDetails(count,limitVal,offsetVal);
				jsnobj.put("Data", dataArray);
				jsnobj.put("pages",pageData);
				jsnobj.put("offset",offsetVal);
				jsnobj.put("group",group);

				boolean userGroupsReq=Boolean.parseBoolean(userGroupsRequired);
				if(userGroupsReq){
					JSONArray reasonListGroups=getReasonListGroup();
					jsnobj.put("searchgroups", reasonListGroups);
					jsnobj.put("canManageGroups", "2884");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsnobj;
	}

	/**
	 * Get the Reason Group data 
	 */

	private JSONArray getReasonListGroup() {

		CriteriaBuilder cbuilder=em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cquery= cbuilder.createQuery(Object[].class);
		Root<ReasonUserGroup> root=cquery.from(ReasonUserGroup.class);	
		cquery.multiselect(root.get(ReasonUserGroup_.reasonUserGroupId),
				root.get(ReasonUserGroup_.reasonUserGrpName),
				root.get(ReasonUserGroup_.reasonUserGrpDefault),
				root.get(ReasonUserGroup_.reasonUserGrpIsActive));
		cquery.orderBy(cbuilder.asc(root.get(ReasonUserGroup_.reasonUserGroupId)));

		List<Object[]> result= null;
		try{
			result= em.createQuery(cquery).getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		JSONArray statusArray = new JSONArray();
		try
		{
			for(Object[] value : result)
			{
				JSONObject statusObject = new JSONObject();
				statusObject.put("groupid", String.valueOf(value[0]));
				statusObject.put("groupname",  String.valueOf(value[1]));
				statusObject.put("isdefault",  String.valueOf(value[2]));
				statusObject.put("isactive", String.valueOf(value[3]));
				statusArray.put(statusObject);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return statusArray;
	}

	public JSONArray getNormalPageDetails(long count, int limit,
			int offset) throws Exception {

		JSONArray pageInfo=new JSONArray();
		JSONObject pageInfoData=new JSONObject();
		int total=(int)Math.floor( ( ((double)count) /limit) ) ;

		pageInfoData.put("totalPages", (int) Math.ceil( ( ((double)count) /limit) ) );
		pageInfoData.put("lastOffset", ((total*limit)) );
		if(offset==0)
			pageInfoData.put("prevOffset", 0);
		else
			pageInfoData.put("prevOffset", offset-limit);
		pageInfoData.put("currentPage", (offset/limit)+1 );
		if((offset/limit) == total)
			pageInfoData.put("nextOffset", offset);
		else
			pageInfoData.put("nextOffset", offset+limit);

		int totalPages=(int) Math.ceil( ( ((double)count) /limit) );
		int totalCountGwt=limit*totalPages;
		pageInfoData.put("totalCount",totalCountGwt);

		pageInfo.put(0, pageInfoData);
		return pageInfo;
	}

	/**
	 * Get the add new reason data 
	 */

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getAddNewReasonData() {

		/*CriteriaBuilder cbuilder=em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cquery= cbuilder.createQuery(Object[].class);
		Root<AppReferenceValues> appReference=cquery.from(AppReferenceValues.class);
		Join<AppReferenceValues,BillingConfigTable> billConfJoin=appReference.join("billingConfTable",JoinType.INNER);

		cquery.multiselect(appReference.get(AppReferenceValues_.App_Reference_Values_Id),
				appReference.get(AppReferenceValues_.App_Reference_Values_statusName),
				appReference.get(AppReferenceValues_.App_Reference_Values_reason_type),
				appReference.get(AppReferenceValues_.App_Reference_Values_isActive),
				billConfJoin.get(BillingConfigTable_.billingConfigTableConfigId),
				billConfJoin.get(BillingConfigTable_.billingConfigTableLookupDesc));
		cquery.where(cbuilder.and(cbuilder.equal(appReference.get(AppReferenceValues_.App_Reference_Values_tableId),new Integer(402)),
				cbuilder.equal(billConfJoin.get(BillingConfigTable_.billingConfigTableLookupId),new Integer(402))));
		cquery.orderBy(cbuilder.asc(appReference.get(AppReferenceValues_.App_Reference_Values_statusName)));*/
		String qry="select App_Reference_Values_Id as id, App_Reference_Values_statusName as description,App_Reference_Values_reason_type as hiddentypeid,App_Reference_Values_isActive as isactiveval,billing_config_table_config_id as typeid,billing_config_table_lookup_desc as visitreason  from App_Reference_Values A inner join billing_config_table B on A.App_Reference_Values_reason_type=B.billing_config_table_config_id where  A.App_Reference_Values_tableId = 402 and B.billing_config_table_lookup_id = 402 order by 2";
		List<Object[]> result= null;
		try{
			result= em.createNativeQuery(qry).getResultList();
			JSONArray reasonDataArray = new JSONArray();
			for(Object[] value : result)
			{
				JSONObject reasonObject = new JSONObject();
				reasonObject.put("id", value[0]);
				reasonObject.put("description", value[1]);
				reasonObject.put("hiddentypeid", value[2]);
				reasonObject.put("isactiveval",value[3]);
				reasonObject.put("typeid", value[4]);
				reasonObject.put("visitreason",value[5]);
				reasonDataArray.put(reasonObject);
			}
			jsnobj.put("newAlertData",reasonDataArray);

			CriteriaBuilder cb=em.getCriteriaBuilder();
			CriteriaQuery<Object[]> cq= cb.createQuery(Object[].class);
			Root<BillingConfigTable> rootBillConf=cq.from(BillingConfigTable.class);
			cq.multiselect(rootBillConf.get(BillingConfigTable_.billingConfigTableConfigId),
					rootBillConf.get(BillingConfigTable_.billingConfigTableLookupDesc));
			cq.where(cb.equal(rootBillConf.get(BillingConfigTable_.billingConfigTableLookupId),new Integer(402)));

			List<Object[]> billConfList= em.createQuery(cq).getResultList();
			JSONArray billConfArray = new JSONArray();
			for(Object[] value : billConfList)
			{
				JSONObject billConfObject = new JSONObject();
				billConfObject.put("typeid", value[0]);
				billConfObject.put("visitreason", value[1]);
				billConfArray.put(billConfObject);
			}
			jsnobj.put("visitData",billConfArray);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return jsnobj;

	}

	/**
	 * To save the new reason data 
	 */

	@Override
	public void saveReasonData(String categoryName, String comboId,
			String isActiveVal, String visitReason, String visitTypeId) {

		if (isActiveVal.equals("t")||isActiveVal.equals("1")||isActiveVal.equals("true"))
			isActiveVal="true";
		else
			isActiveVal="false";
		if (comboId.equals("-1")){

			int maxId=getMaxIdList();
			comboId = String.valueOf(maxId);			
			int maximumId=getMaxCategory();
			int reasonGrpMaxId=getMaxIdReasonGroup();

			AppReferenceValues appRefValues=new AppReferenceValues();			
			appRefValues.setApp_Reference_Values_Id(Integer.parseInt(comboId));
			appRefValues.setApp_Reference_Values_tableId(new Integer(402));
			appRefValues.setApp_Reference_Values_statusId(maximumId);
			appRefValues.setApp_Reference_Values_statusName(categoryName);
			appRefValues.setApp_Reference_Values_isActive(Boolean.parseBoolean(isActiveVal));
			appRefValues.setApp_Reference_Values_reason_type(Integer.parseInt(visitTypeId));
			appReferenceValuesRepository.saveAndFlush(appRefValues);

			ReasonUserGroupMapping reasonUsrGrpMapping=new ReasonUserGroupMapping();
			reasonUsrGrpMapping.setReasonUserGroupMapId(reasonGrpMaxId);
			reasonUsrGrpMapping.setReasonUserGrpMapGrpId(maximumId);
			reasonUsrGrpMapping.setReasonUserGrpMapReasonId(new Integer(1));
			reasonUserGroupMappingRepository.saveAndFlush(reasonUsrGrpMapping);

		}else{
			@SuppressWarnings("unused")
			String reasonName=getAppRefReasonName(comboId);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaUpdate<AppReferenceValues> cu = cb.createCriteriaUpdate(AppReferenceValues.class);
			Root<AppReferenceValues> rootCriteria = cu.from(AppReferenceValues.class);
			cu.set(rootCriteria.get(AppReferenceValues_.App_Reference_Values_statusName), categoryName);
			cu.set(rootCriteria.get(AppReferenceValues_.App_Reference_Values_isActive), Boolean.parseBoolean(isActiveVal));
			cu.set(rootCriteria.get(AppReferenceValues_.App_Reference_Values_reason_type), Integer.parseInt(visitTypeId));
			cu.where(cb.equal(rootCriteria.get(AppReferenceValues_.App_Reference_Values_Id),Integer.parseInt(comboId)),
					cb.equal(rootCriteria.get(AppReferenceValues_.App_Reference_Values_tableId),new Integer(402)));
			this.em.createQuery(cu).executeUpdate();
		}
	}

	private int getMaxCategory() {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<AppReferenceValues> root = cq.from(AppReferenceValues.class);

		cq.select(builder.greatest(root.get(AppReferenceValues_.App_Reference_Values_statusId)));
		cq.where(builder.equal(root.get(AppReferenceValues_.App_Reference_Values_tableId), new Integer(402)));

		List<Object> maxList=em.createQuery(cq).getResultList();
		int maxId = 0;
		for (int i = 0; i < maxList.size(); i++) {
			maxId =(int) maxList.get(i);
			maxId=maxId+1;
		}

		return maxId;

	}

	/**
	 * Getting max of AppReference 
	 */
	public int getMaxIdList(){

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<AppReferenceValues> root = cq.from(AppReferenceValues.class);

		cq.select(builder.greatest(root.get(AppReferenceValues_.App_Reference_Values_Id)));

		List<Object> maxList=em.createQuery(cq).getResultList();
		int maxId = 0;
		for (int i = 0; i < maxList.size(); i++) {
			maxId =(int) maxList.get(i);
			maxId=maxId+1;
		}

		return maxId;
	}

	/**
	 * Getting max of Reason Group Mapping table 
	 */
	public int getMaxIdReasonGroup(){

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<ReasonUserGroupMapping> root = cq.from(ReasonUserGroupMapping.class);

		cq.select(builder.greatest(root.get(ReasonUserGroupMapping_.reasonUserGroupMapId)));

		List<Object> maxList=em.createQuery(cq).getResultList();
		int maxId = 0;
		for (int i = 0; i < maxList.size(); i++) {
			maxId =(int) maxList.get(i);
			maxId=maxId+1;
		}

		return maxId;
	}


	/**
	 * Getting the reason Name if already exists 
	 */
	public String getAppRefReasonName(String comboId){

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<AppReferenceValues> root = cq.from(AppReferenceValues.class);

		cq.select(root.get(AppReferenceValues_.App_Reference_Values_statusName));
		cq.where(builder.equal(root.get(AppReferenceValues_.App_Reference_Values_Id), Integer.parseInt(comboId)));

		List<Object> reasonNameList=em.createQuery(cq).getResultList();
		String reasonName="";
		for (int i = 0; i < reasonNameList.size(); i++) {
			reasonName =(String) reasonNameList.get(i);
		}

		return reasonName;
	}

	/**
	 * Get the Referral picker data 
	 */

	@Override
	public JSONObject getReferralData(String searchCode, String lastName,
			String firstName, String npi, String address, String fax,
			String phone, String directEmail, String specialization,
			String group, String selectedValue, String userGroupsRequired,
			String dataRequired, String offset, String limit) throws Exception {

		int offsetVal=Integer.parseInt(offset);
		int limitVal=Integer.parseInt(limit);
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq= cb.createQuery(Object[].class);
		Root<ReferringDoctor> root=cq.from(ReferringDoctor.class);
		Join<ReferringDoctor,SpecialisationReferring> specilizationJoin=root.join("specialisationReferring",JoinType.LEFT);
		Join<ReferringDoctor,BillingConfigTable> billCofigJoin=root.join("billingConfigTableJoin",JoinType.LEFT);
		cq.multiselect(cb.coalesce(specilizationJoin.get(SpecialisationReferring_.specialisation_referring_name), ""),
				cb.coalesce(root.get(ReferringDoctor_.referring_doctor_lastname),""),
				cb.coalesce(root.get(ReferringDoctor_.referring_doctor_firstname),""),
				cb.coalesce(root.get(ReferringDoctor_.referring_doctor_npi),""),
				cb.coalesce(root.get(ReferringDoctor_.referring_doctor_fax_number),""),
				cb.coalesce(root.get(ReferringDoctor_.referring_doctor_phoneno),""),
				root.get(ReferringDoctor_.referring_doctor_uniqueid),
				cb.concat(cb.concat(cb.concat(cb.coalesce(root.get(ReferringDoctor_.referring_doctor_address),""), cb.coalesce(cb.concat(",",root.get(ReferringDoctor_.referring_doctor_city)),"")),cb.coalesce(cb.concat(",",billCofigJoin.get(BillingConfigTable_.billingConfigTableLookupDesc)),"")),cb.coalesce(cb.concat(",", root.get(ReferringDoctor_.referring_doctor_zip)),"")),
				root.get(ReferringDoctor_.directEmailAddress),
				root.get(ReferringDoctor_.referring_doctor_address),
				root.get(ReferringDoctor_.referring_doctor_city),
				cb.coalesce(billCofigJoin.get(BillingConfigTable_.billingConfigTableLookupDesc),""),
				root.get(ReferringDoctor_.referring_doctor_zip),
				root.get(ReferringDoctor_.referring_doctor_credential));
		Predicate[] billCofigMapping=new Predicate[]{
				cb.equal(billCofigJoin.get(BillingConfigTable_.billingConfigTableLookupId),new Integer(5001))
		};
		billCofigJoin.on(billCofigMapping);
		
		List<Predicate> predList = new LinkedList<Predicate>();

		Predicate isActive=cb.equal(root.get(ReferringDoctor_.referring_doctor_isactive), new Boolean(true));
		predList.add(isActive);
		
		if (!lastName.equals("")){
			Predicate lastNamePredicate = cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(ReferringDoctor_.referring_doctor_lastname))),
					lastName.trim().toLowerCase()+"%");
			predList.add(lastNamePredicate);
		}
		if (!firstName.equals("")){
			Predicate firstNamePredicate = cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(ReferringDoctor_.referring_doctor_lastname))),
					firstName.trim().toLowerCase()+"%");
			predList.add(firstNamePredicate);
		}
		if (!npi.equals("")){
			Predicate npiPredicate = cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(ReferringDoctor_.referring_doctor_npi))),
					npi.trim().toLowerCase()+"%");
			predList.add(npiPredicate);
		}
		
		if (!fax.equals("")){
			Predicate faxPredicate = cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(ReferringDoctor_.referring_doctor_fax_number))),
					fax.trim().toLowerCase()+"%");
			predList.add(faxPredicate);
		}
		
		if (!phone.equals("")){
			Predicate phonePredicate = cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(ReferringDoctor_.referring_doctor_phoneno))),
					phone.trim().toLowerCase()+"%");
			predList.add(phonePredicate);
		}
		
		if (!address.equals("")){
			Predicate addressPredicate = cb.or(cb.or(cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(ReferringDoctor_.referring_doctor_address))),
					address.trim().toLowerCase()+"%"),cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(ReferringDoctor_.referring_doctor_city))),
							address.trim().toLowerCase()+"%"),cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(ReferringDoctor_.referring_doctor_zip))),
									address.trim().toLowerCase()+"%")));
			predList.add(addressPredicate);
		}
		
		if (!specialization.equals("-1")){
			Predicate specializationPredicate=cb.equal(specilizationJoin.get(SpecialisationReferring_.specialisation_referring_id),Integer.parseInt(specialization));
			predList.add(specializationPredicate);
		}
		
		if (!group.equals("-1")){
			Predicate groupPredicate=cb.equal(specilizationJoin.get(SpecialisationReferring_.specialisation_referring_id),Integer.parseInt(group));
			predList.add(groupPredicate);
		}
		
		if (!directEmail.equals("-1")){
			Predicate directEmailPredicate = cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(ReferringDoctor_.directEmailAddress))),
					directEmail.trim().toLowerCase()+"%");
			predList.add(directEmailPredicate);
		}
		
		cq.where(predList.toArray(new Predicate[] {}));
		List<Object[]> refDoctorList;
		refDoctorList=em.createQuery(cq).setFirstResult(offsetVal).setMaxResults(limitVal).getResultList();
		List<Object[]> refDoctorListSize=em.createQuery(cq).getResultList();
		JSONArray refDocArray = new JSONArray();
		for(Object[] value : refDoctorList)
		{
			JSONObject billConfObject = new JSONObject();
			billConfObject.put("col0", value[0]);
			billConfObject.put("col1", value[1]);
			billConfObject.put("col2", value[2]);
			billConfObject.put("col3", value[3]);
			billConfObject.put("col4", value[4]);
			billConfObject.put("col5", value[5]);
			billConfObject.put("id", String.valueOf(value[6]));
			billConfObject.put("address", value[7]);
			billConfObject.put("col6", value[8]);
			billConfObject.put("col7", value[9]);
			billConfObject.put("col8", value[10]);
			billConfObject.put("col9", value[11]);
			billConfObject.put("col10", value[12]);
			billConfObject.put("col11", value[13]);
			refDocArray.put(billConfObject);
		}
		jsnobj.put("Data", refDocArray);
		long count=refDoctorListSize.size();
		JSONArray pageData=getNormalPageDetails(count,limitVal,offsetVal);
		jsnobj.put("pages" , pageData);
		jsnobj.put("offset" , offset);
		jsnobj.put("group" , group);
		boolean userGroupsReq=Boolean.parseBoolean(userGroupsRequired);
		if(userGroupsReq){
			JSONArray refferalGroups=getReferralGroups();
			JSONArray specilizations=getSpecializations();
			jsnobj.put("searchgroups",refferalGroups);
			jsnobj.put("specializations",specilizations);
			jsnobj.put("canManageGroups", "2884");		
		}
		return jsnobj;
	}

	private JSONArray getSpecializations() {

		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq= cb.createQuery(Object[].class);
		Root<SpecialisationReferring> root=cq.from(SpecialisationReferring.class);
		cq.multiselect(root.get(SpecialisationReferring_.specialisation_referring_id).alias("value"),
				root.get(SpecialisationReferring_.specialisation_referring_name).alias("text"));
		Predicate refGrpCondition=cb.notEqual(root.get(SpecialisationReferring_.specialisation_referring_id),new Integer(-1));
		cq.where(refGrpCondition);
		List<Object[]> refGroupList= em.createQuery(cq).getResultList();
		JSONArray refGroupArray = new JSONArray();
		JSONObject refGrpObject = new JSONObject();

		for(Object[] value : refGroupList)
		{
			refGrpObject = new JSONObject();
			try {
				refGrpObject.put("text",value[1]);
				refGrpObject.put("value",String.valueOf(value[0]));
				refGroupArray.put(refGrpObject);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return refGroupArray;
	}

	/**
	 * Get the Referral group data 
	 */

	private JSONArray getReferralGroups() {

		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq= cb.createQuery(Object[].class);
		Root<SpecialisationReferring> root=cq.from(SpecialisationReferring.class);
		cq.multiselect(root.get(SpecialisationReferring_.specialisation_referring_id),
				root.get(SpecialisationReferring_.specialisation_referring_name),
				cb.literal("t").alias("isactive"),
				cb.literal("f").alias("isdefault")
				);
		Predicate refGrpCondition=cb.notEqual(root.get(SpecialisationReferring_.specialisation_referring_id),new Integer(-1));
		cq.where(refGrpCondition).orderBy(cb.asc(root.get(SpecialisationReferring_.specialisation_referring_name)));
		List<Object[]> refGroupList= em.createQuery(cq).getResultList();
		JSONArray refGroupArray = new JSONArray();
		JSONObject refGrpObject = new JSONObject();
		try {
			refGrpObject.put("groupname","ALL");
			refGrpObject.put("groupid","-1");
			refGrpObject.put("isactive","t");
			refGrpObject.put("isdefault","t");
			refGroupArray.put(refGrpObject);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		for(Object[] value : refGroupList)
		{

			refGrpObject = new JSONObject();

			try {
				refGrpObject.put("groupname",value[1]);
				refGrpObject.put("groupid",String.valueOf(value[0]));
				refGrpObject.put("isactive",value[2]);
				refGrpObject.put("isdefault",value[3]);
				refGroupArray.put(refGrpObject);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return refGroupArray;
	}

	/**
	 * To save the referral request data 
	 */

	@SuppressWarnings("unused")
	@Override
	public void saveReferralData(String fromSave, String comboId,
			String isActiveVal, String midName, String address, String city,
			String state, String zip, String phoneno, String faxno,
			String firstname, String lastname, String uid, String mid,
			String bid, String credential, String special, String referraltype,
			String refDocname, String prefix, String taxonomy,
			String rdpracname, String txtTitle,String directemail, String emailid) {

		if (isActiveVal.equals("t")||isActiveVal.equals("1")||isActiveVal.equals("true"))
			isActiveVal="true";
		else
			isActiveVal="false";

		String categoryName = lastname+" "+credential+", "+firstname+"  "+midName;
		if (comboId.equals("-1"))
		{
			int maxId=getMaxReffDocId();
			if("".equals(special)){
                int specialityId=Integer.parseInt(special);
				saveRefferDocData(categoryName,isActiveVal,lastname,midName,firstname,address,city,state,zip,phoneno,faxno,uid,mid,bid,credential,-2,referraltype,
						emailid,prefix,taxonomy,directemail,rdpracname,txtTitle);			
			}else{
				int specialityId=Integer.parseInt(special);
				saveRefferDocData(categoryName,isActiveVal,lastname,midName,firstname,address,city,state,zip,phoneno,faxno,uid,mid,bid,credential,specialityId,referraltype,
						emailid,prefix,taxonomy,directemail,rdpracname,txtTitle);
			}
		}else{

			updateReferralDocData(comboId,categoryName,isActiveVal,lastname,midName,firstname,address,city,state,zip,phoneno,faxno,uid,mid,bid,credential,special,referraltype,
					emailid,prefix,taxonomy,directemail,rdpracname,txtTitle);

		}
	}

	/**
	 * Update the referral if already present 
	 */
	private void updateReferralDocData(String comboId,String categoryName, String isActiveVal,
			String lastname, String midName, String firstname, String address,
			String city, String state, String zip, String phoneno,
			String faxno, String uid, String mid, String bid,
			String credential, String special, String referraltype,
			String emailid, String prefix, String taxonomy, String directemail,
			String rdpracname, String txtTitle) {

		 int specialityId=-1;
		 if(special.equals("")){
			special="-1";
			 specialityId=-1;
		}
		int refUniqId=Integer.parseInt(comboId);
		short refType=Short.parseShort(referraltype);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<ReferringDoctor> cu = cb.createCriteriaUpdate(ReferringDoctor.class);
		Root<ReferringDoctor> rootCriteria = cu.from(ReferringDoctor.class);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_isactive),Boolean.parseBoolean(isActiveVal));
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_referringdoctor),categoryName);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_lastname),lastname);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_midinitial),midName);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_firstname),firstname);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_address),address);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_city),city);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_state),state);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_zip),zip);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_phoneno),phoneno);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_fax_number),faxno);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_upin_no),uid);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_medicare_pin),mid);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_npi),bid);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_credential),credential);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_speciality_id),specialityId);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_type),refType);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_mailid),emailid);
		cu.set(rootCriteria.get(ReferringDoctor_.referring_doctor_prefix),prefix);
		cu.set(rootCriteria.get(ReferringDoctor_.taxonomyCode),taxonomy);
		cu.set(rootCriteria.get(ReferringDoctor_.directEmailAddress),directemail);
		cu.set(rootCriteria.get(ReferringDoctor_.practiceName),rdpracname);
		cu.set(rootCriteria.get(ReferringDoctor_.refTitle),txtTitle);
		cu.where(cb.equal(rootCriteria.get(ReferringDoctor_.referring_doctor_uniqueid),refUniqId));
		this.em.createQuery(cu).executeUpdate();
	}

	/**
	 * Adding a new referral  
	 */
	private void saveRefferDocData(String categoryName,
			String isActiveVal, String lastname, String midName,
			String firstname, String address, String city, String state,
			String zip, String phoneno, String faxno, String uid, String mid,
			String bid, String credential, int specialityId, String referraltype,
			String emailid, String prefix, String taxonomy, String directemail,
			String rdpracname, String txtTitle) {

        short refType=Short.parseShort(referraltype);
		ReferringDoctor refDocData=new ReferringDoctor();
		refDocData.setreferring_doctor_referringdoctor(categoryName);
		refDocData.setreferring_doctor_isactive(Boolean.parseBoolean(isActiveVal));
		refDocData.setreferring_doctor_lastname(lastname);
		refDocData.setreferring_doctor_midinitial(midName);
		refDocData.setreferring_doctor_firstname(firstname);
		refDocData.setreferring_doctor_address(address);
		refDocData.setreferring_doctor_city(city);
		refDocData.setreferring_doctor_state(state);
		refDocData.setreferring_doctor_zip(zip);
		refDocData.setreferring_doctor_phoneno(phoneno);
		refDocData.setreferring_doctor_fax_number(faxno);
		refDocData.setreferring_doctor_upin_no(uid);
		refDocData.setreferring_doctor_medicare_pin(mid);
		refDocData.setreferring_doctor_npi(bid);
		refDocData.setreferring_doctor_credential(credential);
		refDocData.setreferring_doctor_speciality_id(specialityId);
		refDocData.setreferring_doctor_type(refType);
		refDocData.setreferring_doctor_mailid(emailid);
		refDocData.setreferring_doctor_prefix(prefix);
		refDocData.setreferring_doctor_iserphysician(false);
		refDocData.setTaxonomyCode(taxonomy);
		refDocData.setDirectEmailAddress(directemail);
		refDocData.setPracticeName(rdpracname);
		refDocData.setRefTitle(txtTitle);
		referringDoctorRepository.saveAndFlush(refDocData);
	}

	/**
	 * Getting max of Reason Group Mapping table 
	 */
	public int getMaxReffDocId(){

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<ReferringDoctor> root = cq.from(ReferringDoctor.class);

		cq.select(builder.greatest(root.get(ReferringDoctor_.referring_doctor_uniqueid)));

		List<Object> maxList=em.createQuery(cq).getResultList();
		int maxId = 0;
		for (int i = 0; i < maxList.size(); i++) {
			maxId =(int) maxList.get(i);
			maxId=maxId+1;
		}

		return maxId;
	}

	@Override
	public JSONObject getAddtionalRefData(String mode, String docId) {
		JSONObject data;

		if(mode.equals("1")){	

			data=getPrefixStateCodeData();	
		}
		else{		

			data=getReferringDocDetails(docId);
		}	
		return data;
	}

	/**
	 *  To get the state code data
	 */
	private JSONObject getPrefixStateCodeData() {

		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq= cb.createQuery(Object[].class);
		Root<BillingConfigTable> root=cq.from(BillingConfigTable.class);
		cq.multiselect(root.get(BillingConfigTable_.billingConfigTableConfigId),
				root.get(BillingConfigTable_.billingConfigTableLookupDesc));
		Predicate lookupId=cb.equal(root.get(BillingConfigTable_.billingConfigTableLookupId),new Integer(5001));
		Predicate isActive=cb.equal(root.get(BillingConfigTable_.billingConfigTableIsActive),new Boolean(true));
		cq.where(lookupId,isActive).orderBy(cb.asc(root.get(BillingConfigTable_.billingConfigTableConfigId)));;
		List<Object[]> refGroupList= em.createQuery(cq).getResultList();

		JSONArray stateCodesArray = new JSONArray();
		JSONObject stateCodesObject = new JSONObject();

		for(Object[] value : refGroupList)
		{
			stateCodesObject = new JSONObject();
			try {
				stateCodesObject.put("id",value[0]);
				stateCodesObject.put("value",value[1]);
				stateCodesArray.put(stateCodesObject);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		CriteriaBuilder cbuilder=em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cquery= cbuilder.createQuery(Object[].class);
		Root<Billinglookup> billLookup=cquery.from(Billinglookup.class);
		cquery.multiselect(billLookup.get(Billinglookup_.blookId).alias("id"),
				billLookup.get(Billinglookup_.blookName).alias("value"));
		Predicate blookId=cbuilder.equal(billLookup.get(Billinglookup_.blookGroup),new Integer(21));
		cquery.where(blookId);
		List<Object[]> billLookupList= em.createQuery(cquery).getResultList();
		JSONArray prefixArray = new JSONArray();
		JSONObject prefixObject = new JSONObject();
		try {
			prefixObject.put("id","");
			prefixObject.put("value","");
			prefixArray.put(prefixObject);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		for(Object[] value : billLookupList)
		{

			prefixObject = new JSONObject();

			try {
				prefixObject.put("id",value[1]);
				prefixObject.put("value",value[1]);
				prefixArray.put(prefixObject);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			jsnobj.put("prefixs", prefixArray);
			jsnobj.put("stateCodes",stateCodesArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsnobj;
	}
	/**
	 * Get the Referral doctor details based on doctorId 
	 */
	private JSONObject getReferringDocDetails(String docId) {

		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq= cb.createQuery(Object[].class);
		Root<ReferringDoctor> root = cq.from(ReferringDoctor.class);
		cq.multiselect(root.get(ReferringDoctor_.referring_doctor_uniqueid),
				cb.concat(cb.concat(root.get(ReferringDoctor_.referring_doctor_firstname),cb.concat(" ",root.get(ReferringDoctor_.referring_doctor_lastname))),cb.concat(" ",root.get(ReferringDoctor_.referring_doctor_credential))),
				root.get(ReferringDoctor_.referring_doctor_isactive),
				root.get(ReferringDoctor_.referring_doctor_lastname),
			    root.get(ReferringDoctor_.referring_doctor_midinitial),
			    root.get(ReferringDoctor_.referring_doctor_firstname),
				root.get(ReferringDoctor_.referring_doctor_address),
				root.get(ReferringDoctor_.referring_doctor_city),
				cb.coalesce(root.get(ReferringDoctor_.referring_doctor_state),"-1"),
				cb.coalesce(root.get(ReferringDoctor_.referring_doctor_zip),""),
				root.get(ReferringDoctor_.referring_doctor_phoneno),
				root.get(ReferringDoctor_.referring_doctor_fax_number),
				cb.coalesce(root.get(ReferringDoctor_.referring_doctor_upin_no),""),
				cb.coalesce(root.get(ReferringDoctor_.referring_doctor_medicare_pin),""),
				cb.coalesce(root.get(ReferringDoctor_.referring_doctor_npi),""),
				root.get(ReferringDoctor_.referring_doctor_credential),
				cb.coalesce(root.get(ReferringDoctor_.referring_doctor_speciality_id),-1),
				cb.coalesce(root.get(ReferringDoctor_.referring_doctor_type),0),
				cb.coalesce(root.get(ReferringDoctor_.referring_doctor_mailid),""),
				cb.coalesce(root.get(ReferringDoctor_.referring_doctor_prefix),""),
				cb.coalesce(root.get(ReferringDoctor_.taxonomyCode),""),
				cb.coalesce(root.get(ReferringDoctor_.directEmailAddress),""),
				cb.coalesce(root.get(ReferringDoctor_.practiceName),""),
				cb.coalesce(root.get(ReferringDoctor_.refTitle),""));
		cq.where(cb.equal(root.get(ReferringDoctor_.referring_doctor_uniqueid),docId));
		List<Object[]> refDocList= em.createQuery(cq).getResultList();
		JSONArray refDocDetailsArray = new JSONArray();
		JSONObject refDocObject = new JSONObject();

		for(Object[] value : refDocList)
		{
			refDocObject = new JSONObject();
			try {
				refDocObject.put("id",value[0]);
				refDocObject.put("description",String.valueOf(value[1]));
				refDocObject.put("isactiveval",value[2]);
				refDocObject.put("lastname",value[3]);
				refDocObject.put("midname",value[4]);
				refDocObject.put("firstname",value[5]);
				refDocObject.put("address",value[6]);
				refDocObject.put("city",value[7]);
				refDocObject.put("state",value[8]);
				refDocObject.put("zip",value[9]);
				refDocObject.put("phoneno",value[10]);
				refDocObject.put("faxno",value[11]);
				refDocObject.put("uid",value[12]);
				refDocObject.put("mid",value[13]);
				refDocObject.put("bid",value[14]);
				refDocObject.put("credential",value[15]);
				refDocObject.put("specialityid",String.valueOf(value[16]));
				refDocObject.put("reftype",value[17]);
				refDocObject.put("emailid",value[18]);
				refDocObject.put("prefix",value[19]);
				refDocObject.put("taxonomy",value[20]);
				refDocObject.put("directemal",value[21]);
				refDocObject.put("pracname",value[22]);
				refDocObject.put("txttitle",value[23]);
				refDocDetailsArray.put(refDocObject);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			jsnobj.put("referralData",refDocDetailsArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsnobj;
	}


	/**
	 * Get the zip codes data
	 */
	@Override
	public JSONObject getZipCodesData(String city, String state, String zip,
			String mode, String limit, String offset) {

		int offsetVal=Integer.parseInt(offset);
		int limitVal=Integer.parseInt(limit);
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq= cb.createQuery(Object[].class);
		Root<ZipCodesMain> root=cq.from(ZipCodesMain.class);
		Join<ZipCodesMain,BillingConfigTable> billingJoin=root.join("billingConfigTable1",JoinType.INNER);
		cq.multiselect(root.get(ZipCodesMain_.zipCodesCity),
				billingJoin.get(BillingConfigTable_.billingConfigTableConfigId),
				billingJoin.get(BillingConfigTable_.billingConfigTableLookupDesc),
				root.get(ZipCodesMain_.zipCodesZip));
		List<Predicate> predList = new LinkedList<Predicate>();

		Predicate billingConfId = cb.equal(billingJoin.get(BillingConfigTable_.billingConfigTableLookupId), new Integer(5001));
		predList.add(billingConfId);

		if (!state.equals("-1")){
			Predicate statePredicate=cb.equal(root.get(ZipCodesMain_.zipCodesState),Integer.parseInt(state));
			predList.add(statePredicate);
		}
		if (!city.equals("-1")){
			Predicate cityPredicate = cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(ZipCodesMain_.zipCodesCity))),
					city.trim().toLowerCase()+"%");
			predList.add(cityPredicate);
		}
		if (!zip.equals("-1")){
			Predicate zipPredicate = cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(ZipCodesMain_.zipCodesZip))),
					zip.trim().toLowerCase()+"%");
			predList.add(zipPredicate);
		}
		cq.where(predList.toArray(new Predicate[] {}));

		List<Object[]> zipList;
		zipList=em.createQuery(cq).setFirstResult(offsetVal).setMaxResults(limitVal).getResultList();
		List<Object[]> zipCodeList= em.createQuery(cq).getResultList();
		long count=zipCodeList.size();
		JSONArray zipCodeArray = new JSONArray();
		JSONObject zipObject = new JSONObject();

		for(Object[] value : zipList)
		{
			zipObject = new JSONObject();
			try {
				zipObject.put("city",value[0]);
				zipObject.put("state",value[1]);
				zipObject.put("desc",value[2]);
				zipObject.put("zipcode",value[3]);
				zipCodeArray.put(zipObject);
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {

			JSONArray pageData=getNormalPageDetails(count,limitVal,offsetVal);
			JSONArray zipCodeData=getAllZipCodesData();
			jsnobj.put("Data",zipCodeArray);
			jsnobj.put("pages",pageData);
			jsnobj.put("offset",offsetVal);
			jsnobj.put("zipCodes",zipCodeData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsnobj;
	}

	private JSONArray getAllZipCodesData() {

		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq= cb.createQuery(Object[].class);
		Root<BillingConfigTable> root=cq.from(BillingConfigTable.class);
		cq.multiselect(root.get(BillingConfigTable_.billingConfigTableConfigId),
				root.get(BillingConfigTable_.billingConfigTableLookupDesc));
		Predicate billingConfId = cb.equal(root.get(BillingConfigTable_.billingConfigTableLookupId), new Integer(5001));
		cq.where(billingConfId).orderBy(cb.asc(root.get(BillingConfigTable_.billingConfigTableConfigId)));
		List<Object[]> zipListData= em.createQuery(cq).getResultList();
		JSONArray zipCodeArray = new JSONArray();
		JSONObject zipObject = new JSONObject();
		for(Object[] value : zipListData)
		{
			zipObject = new JSONObject();
			try {
				zipObject.put("id",value[0]);
				zipObject.put("value",value[1]);
				zipCodeArray.put(zipObject);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return zipCodeArray;
	}

	/**
	 * Get the patient picker data 
	 */
	@SuppressWarnings("unused")
	@Override
	public JSONObject getPatientPickerData(String pageSize, String searchType,
			String searchMode, String currentPage, String firstName,
			String lastName, String dob, String ssn, String accountNo,
			String patientSearchType) {

		JSONArray finalData=new JSONArray();
		int limitVal=Integer.parseInt(pageSize);
		int offsetVal=Integer.parseInt(currentPage);
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq= cb.createQuery(Object[].class);
		Root<PatientRegistration> root=cq.from(PatientRegistration.class);
		Join<PatientRegistration,PatientInsDetail> patientInsJoin=root.join("patientInsuranceTable",JoinType.LEFT);
		Join<PatientRegistration,Admission> admissionJoin=root.join("admission",JoinType.LEFT);
		Join<PatientRegistration,AuthorizationMaster> authMasterJoin=root.join("authMaster",JoinType.LEFT);
		Join<PatientRegistration,EmployeeProfile> empJoin=root.join("empProfile",JoinType.LEFT);
		Join<PatientRegistration,AccountType> acctTypeJoin=root.join("ptAccType",JoinType.LEFT);
		Join<PatientRegistration,ReferringDoctor> referringJoin=root.join("referringPhyTable",JoinType.LEFT);
		Join<ReferringDoctor,SpecialisationReferring> specialiJoin=referringJoin.join("specialisationReferring",JoinType.LEFT);

		Expression<Object> mobileNo;
		if(searchType.equals("66") || searchType.equals("3")){
			mobileNo=cb.selectCase().when(cb.isNull(root.get(PatientRegistration_.patientRegistrationPreferredcontact)), 
					cb.selectCase().when(cb.equal(cb.coalesce(root.get(PatientRegistration_.patientRegistrationPhoneNo),""),""),cb.coalesce(root.get(PatientRegistration_.patientRegistrationCellno),"")).
					otherwise(cb.coalesce(root.get(PatientRegistration_.patientRegistrationPhoneNo), "")))
					.otherwise(cb.selectCase().when(cb.equal(root.get(PatientRegistration_.patientRegistrationPreferredcontact), 1), cb.coalesce(root.get(PatientRegistration_.patientRegistrationPhoneNo),"")).
							when(cb.equal(root.get(PatientRegistration_.patientRegistrationPreferredcontact), 2), cb.coalesce(root.get(PatientRegistration_.patientRegistrationWorkNo),"")).
							when(cb.equal(root.get(PatientRegistration_.patientRegistrationPreferredcontact), 3), cb.coalesce(root.get(PatientRegistration_.patientRegistrationCellno),"")).
							when(cb.equal(root.get(PatientRegistration_.patientRegistrationPreferredcontact), 4), cb.coalesce(root.get(PatientRegistration_.patientRegistrationOtherNo),"")).otherwise(""));
		}else{
			mobileNo=cb.<Object>coalesce(root.get(PatientRegistration_.patientRegistrationPhoneNo), "");
		}

		List<Predicate> predList = new LinkedList<Predicate>();
		if(accountNo.length() > 0){
			Predicate acctNoPredicate = cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(PatientRegistration_.patientRegistrationAccountno))),
					accountNo.trim().toLowerCase()+"%");
			predList.add(acctNoPredicate);
		}
		if(dob.length() > 0){
			Predicate dobPredicate=cb.equal(root.get(PatientRegistration_.patientRegistrationDob),dob);
			predList.add(dobPredicate);
		}
		if(ssn.length() > 0){
			Predicate ssnPredicate = cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(PatientRegistration_.patientRegistrationSsn))),
					ssn.trim().toLowerCase()+"%");
			predList.add(ssnPredicate);
		}
		if(lastName.length() > 0){
			Predicate lastNamePredicate = cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(PatientRegistration_.patientRegistrationLastName))),
					HUtil.handleSymbols(lastName.trim().toLowerCase())+"%");
			predList.add(lastNamePredicate);
		}
		if(firstName.length() > 0){
			Predicate firstNamePredicate = cb.like(cb.lower(cb.trim(Trimspec.BOTH,root.get(PatientRegistration_.patientRegistrationFirstName))),
					HUtil.handleSymbols(firstName.trim().toLowerCase())+"%");
			predList.add(firstNamePredicate);
		}
		if(patientSearchType.equals("active")){
			Predicate isActive=cb.equal(root.get(PatientRegistration_.patientRegistrationActive),true);
			predList.add(isActive);
		}else{
			Predicate isActive=cb.equal(root.get(PatientRegistration_.patientRegistrationActive),false);
			predList.add(isActive);
		}
		cq.distinct(true);
		cq.multiselect(root.get(PatientRegistration_.patientRegistrationId),
				root.get(PatientRegistration_.patientRegistrationLastName),
				cb.coalesce(root.get(PatientRegistration_.patientRegistrationFirstName),""),
				cb.coalesce(root.get(PatientRegistration_.patientRegistrationMidInitial),""),
				cb.coalesce(root.get(PatientRegistration_.patientRegistrationAccountno),""),
				cb.coalesce(root.get(PatientRegistration_.patientRegistrationSsn),""),
				cb.function("to_mmddyyyy",Date.class,root.get(PatientRegistration_.patientRegistrationDob)),
				root.get(PatientRegistration_.patientRegistrationActive),
				mobileNo,
				cb.coalesce(root.get(PatientRegistration_.patientRegistrationWorkNo), ""),
				cb.selectCase().when(cb.equal(root.get(PatientRegistration_.patientRegistrationNeverschedule), true), new Integer(1)).otherwise(new Integer(0)),
				cb.coalesce(authMasterJoin.get(AuthorizationMaster_.authMasterSysalert), ""),
				cb.coalesce(root.get(PatientRegistration_.patientRegistrationNotes), ""),
				cb.coalesce(root.get(PatientRegistration_.patientRegistrationPatientAlert), ""),
				cb.coalesce(empJoin.get(EmployeeProfile_.empProfileFullname),""),
				cb.coalesce(acctTypeJoin.get(AccountType_.accTypeDesc),""),
				cb.coalesce(root.get(PatientRegistration_.patientRegistrationReferingPhysician),new Integer(-1)),
				cb.coalesce(root.get(PatientRegistration_.patientRegistrationNewPatientAlert), "-1"),
				cb.coalesce(specialiJoin.get(SpecialisationReferring_.specialisation_referring_name), ""),
				cb.concat(cb.trim(cb.coalesce(referringJoin.get(ReferringDoctor_.referring_doctor_lastname),"")),cb.trim(cb.concat(",",cb.coalesce(referringJoin.get(ReferringDoctor_.referring_doctor_firstname), "")))),
				cb.selectCase().when(cb.equal(cb.coalesce(root.get(PatientRegistration_.patientRegistrationAuthReq),"false"),"true"), new Integer(1)).otherwise(new Integer(0)),
				cb.coalesce(root.get(PatientRegistration_.patientRegistrationStatus), new Integer(0)));
		cq.where(predList.toArray(new Predicate[] {})).orderBy(cb.asc(root.get(PatientRegistration_.patientRegistrationLastName)));
		List<Object[]> patientList=em.createQuery(cq).setFirstResult(offsetVal).setMaxResults(limitVal).getResultList(); 
		List<Object[]> patientListCount=em.createQuery(cq).getResultList();
		int patientCount=patientListCount.size();
		float totalNoPages = ((float)patientCount/10); 
		JSONArray patientDataArray = new JSONArray();
		JSONObject patientObject = new JSONObject();

		for(Object[] value : patientList)
		{
			patientObject = new JSONObject();
			try {
				patientObject.put("patientId",value[0]);
				patientObject.put("lastname",value[1]);
				patientObject.put("firstname",value[2]);
				patientObject.put("midname",value[3]);
				patientObject.put("accountno",value[4]);
				patientObject.put("patientssn",value[5]);
				patientObject.put("dob",value[6]);
				patientObject.put("isactive",value[7]);
				patientObject.put("cellno",value[8]);
				patientObject.put("workphone",value[9]);
				patientObject.put("neverschedule",value[10]);
				patientObject.put("sysalert",value[11]);
				patientObject.put("notes",value[12]);
				patientObject.put("patientalert",value[13]);
				patientObject.put("empfullname",value[14]);
				patientObject.put("acctdescription",value[15]);
				patientObject.put("hsprefdoc",value[16]);
				patientObject.put("patient_alert",value[17]);
				patientObject.put("refdoc_speciality",value[18]);
				patientObject.put("refdoc_name",value[19]);
				patientObject.put("isauth22",value[20]);
				patientObject.put("isdeceased",value[21]);
				patientDataArray.put(patientObject);
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			jsnobj.put("requestData", patientDataArray);
			jsnobj.put("count", Math.ceil(totalNoPages));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsnobj;
	}

	@Override
	public void saveManageGroupData(String groupname, String defaultvalue,
			String activevalue, String newgroup, String updateindex,
			String groupIdToUpdate) {

		int newGrp=Integer.parseInt(newgroup);
		int updateIndex=Integer.parseInt(updateindex);
		if(newGrp==1){

			int reasonGrpMaxId=getMaxReasonGrpId();
			String grpName=HUtil.ValidateSingleQuote(groupname);
			ReasonUserGroup reasonUsrGrp=new ReasonUserGroup();
			reasonUsrGrp.setReasonUserGroupId(reasonGrpMaxId);
			reasonUsrGrp.setReasonUserGrpName(grpName);
			reasonUsrGrp.setReasonUserGrpIsActive(Boolean.parseBoolean(activevalue));
			reasonUsrGrp.setReasonUserGrpDefault(Boolean.parseBoolean(defaultvalue));
			updateIndex=reasonGrpMaxId;
		}else{
			if(updateIndex==0)
				updateIndex=-101;
			String grpName=HUtil.ValidateSingleQuote(groupname);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaUpdate<ReasonUserGroup> cu = cb.createCriteriaUpdate(ReasonUserGroup.class);
			Root<ReasonUserGroup> rootCriteria = cu.from(ReasonUserGroup.class);
			cu.set(rootCriteria.get(ReasonUserGroup_.reasonUserGroupId),updateIndex);
			cu.set(rootCriteria.get(ReasonUserGroup_.reasonUserGrpName),grpName);
			cu.set(rootCriteria.get(ReasonUserGroup_.reasonUserGrpIsActive),Boolean.parseBoolean(activevalue));
			cu.set(rootCriteria.get(ReasonUserGroup_.reasonUserGrpDefault),Boolean.parseBoolean(defaultvalue));
			cu.where(cb.equal(rootCriteria.get(ReasonUserGroup_.reasonUserGroupId),updateIndex));
			this.em.createQuery(cu).executeUpdate();
		}
		if(defaultvalue.equals("true")){

			if(defaultvalue.equals("true")&& activevalue.equals("true")){

				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaUpdate<InsUserGroup> cu = cb.createCriteriaUpdate(InsUserGroup.class);
				Root<InsUserGroup> rootCriteria = cu.from(InsUserGroup.class);
				cu.set(rootCriteria.get(InsUserGroup_.insUserGroupDefault),new Boolean(false));
				cu.where(cb.not(rootCriteria.get(InsUserGroup_.insUserGroupId).in(updateIndex)));
				this.em.createQuery(cu).executeUpdate();
			}else{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaUpdate<InsUserGroup> cu = cb.createCriteriaUpdate(InsUserGroup.class);
				Root<InsUserGroup> rootCriteria = cu.from(InsUserGroup.class);
				cu.set(rootCriteria.get(InsUserGroup_.insUserGroupDefault),new Boolean(false));
				cu.where(cb.not(rootCriteria.get(InsUserGroup_.insUserGroupId).in(1)));
				this.em.createQuery(cu).executeUpdate();

				CriteriaBuilder cbuilder = em.getCriteriaBuilder();
				CriteriaUpdate<InsUserGroup> cupdate = cbuilder.createCriteriaUpdate(InsUserGroup.class);
				Root<InsUserGroup> root= cupdate.from(InsUserGroup.class);
				cupdate.set(root.get(InsUserGroup_.insUserGroupDefault),new Boolean(true));
				cupdate.where(cbuilder.equal(root.get(InsUserGroup_.insUserGroupId), new Integer(1)));
				this.em.createQuery(cupdate).executeUpdate();

			}
		}
	}

	public int getMaxReasonGrpId(){

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery();
		Root<ReasonUserGroup> root = cq.from(ReasonUserGroup.class);

		cq.select(builder.greatest(root.get(ReasonUserGroup_.reasonUserGroupId)));

		List<Object> maxList=em.createQuery(cq).getResultList();
		int maxId = 0;
		for (int i = 0; i < maxList.size(); i++) {
			maxId =(int) maxList.get(i);
			maxId=maxId+1;
		}

		return maxId;
	}
}