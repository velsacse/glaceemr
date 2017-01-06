package com.glenwood.glaceemr.server.application.services.chart.Allergies;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glenwood.glaceemr.server.application.models.AllergiesEncountermap;
import com.glenwood.glaceemr.server.application.models.AllergiesEncountermap_;
import com.glenwood.glaceemr.server.application.models.AllergiesType;
import com.glenwood.glaceemr.server.application.models.AllergiesType_;
import com.glenwood.glaceemr.server.application.models.AllergyClass;
import com.glenwood.glaceemr.server.application.models.AllergyClass_;
import com.glenwood.glaceemr.server.application.models.DrugDetails;
import com.glenwood.glaceemr.server.application.models.DrugDetailsRxnormMap;
import com.glenwood.glaceemr.server.application.models.DrugDetailsRxnormMap_;
import com.glenwood.glaceemr.server.application.models.DrugDetails_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.LabDescription;
import com.glenwood.glaceemr.server.application.models.LabDescription_;
import com.glenwood.glaceemr.server.application.models.PatientAllergies;
import com.glenwood.glaceemr.server.application.models.PatientAllergies_;
import com.glenwood.glaceemr.server.application.models.Unii;
import com.glenwood.glaceemr.server.application.models.Unii_;
import com.glenwood.glaceemr.server.application.repositories.AllergiesEncountermapRepository;
import com.glenwood.glaceemr.server.application.repositories.PatientAllergiesRepository;
import com.glenwood.glaceemr.server.application.repositories.UniiRepository;
import com.glenwood.glaceemr.server.application.specifications.AllergiesSpecification;
import com.glenwood.glaceemr.server.utils.HUtil;
/**
 * Service Implementation for Allergies Module
 * @author Bhagya Lakshmi
 *
 */

@Service
@Transactional
public class AllergiesServiceImpl implements AllergiesService{

	@Autowired
	UniiRepository uniiRepository;
	
	@Autowired
	PatientAllergiesRepository patientAllergiesRepository;
	
	@Autowired
	AllergiesEncountermapRepository allergiesEncountermapRepository;
	
	@PersistenceContext
	EntityManager em;
	

	/**
	 * Method to retrieve allergy types
	 */
	@Override
	public List<AllergiesTypeBean> retrievingAllergyType(int gender) {
		List<AllergiesTypeBean> allergiesTypeList = new ArrayList<AllergiesTypeBean>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<AllergiesType> root = cq.from(AllergiesType.class);
		Predicate typeId=builder.greaterThanOrEqualTo(root.get(AllergiesType_.allergtypeId), 0);
		Predicate isActive=builder.equal(root.get(AllergiesType_.allergtypeIsactive), true);
		Predicate genderPred = root.get(AllergiesType_.allergtypeGender).in(0,gender);
		cq.multiselect(root.get(AllergiesType_.allergtypeId),
				root.get(AllergiesType_.allergtypeName));
		cq.where(typeId,isActive,genderPred);
		cq.orderBy(builder.asc(root.get(AllergiesType_.allergtypeName)));
		List<Object[]> allergiesType = em.createQuery(cq).getResultList();
		for(Object[] values : allergiesType)
		{
			AllergiesTypeBean allergiesTypeBean  =new AllergiesTypeBean();
			allergiesTypeBean.setAllergtypeId((Integer)values[0]);
			allergiesTypeBean.setAllergtypeName(values[1].toString());
			allergiesTypeList.add(allergiesTypeBean);
		}
		return allergiesTypeList;
	}
	
	/**
	 * Method to get data for allergen search
	 * mode basing on allergy type id different modes will allocate 
	 */
	@Override
	public AllergyBean searchAllergy(String value,int mode,int offset,int limit) {
		AllergyBean allergyBean = null;
		if(mode==8)
		{
			allergyBean = new AllergyBean();
			Integer countCode=getCountCode(value);
			List<AllergySearchBean> uniiBeanList = getDrugSearch(value,offset,limit);
			allergyBean.setCountValue(countCode);
			allergyBean.setAllergySearchBean(uniiBeanList);
		}
		if(mode==9)
		{
			allergyBean = new AllergyBean();
			Integer countCode=getLabCountCode(value);
			List<AllergySearchBean> uniiBeanList = getDrugSearchLab(value,offset,limit);
			allergyBean.setCountValue(countCode);
			allergyBean.setAllergySearchBean(uniiBeanList);
		}
		if(mode==0)	
		{
			allergyBean = new AllergyBean();
			Integer countCode=getDrugCountCode(value);
			List<AllergySearchBean> drugDetailsList = getDrugDetailsSearch(value);
			List<AllergySearchBean> allergyClassList = getAllergyClassDetailsSearch(value,offset,limit);
			List<AllergySearchBean> finalList = new ArrayList<AllergySearchBean>();
			finalList.addAll(drugDetailsList);
			finalList.addAll(allergyClassList);
			List<AllergySearchBean> finalListAfterSort=sortBy(finalList);
			List<AllergySearchBean> allergySearchBean = null;
			if(finalListAfterSort.size()>limit)
			{
				allergySearchBean = new ArrayList<AllergySearchBean>();
				if(offset == 0)
				{
					for(int i=offset;i<limit;i++)
					{
							AllergySearchBean AllergySearch = finalListAfterSort.get(i);
							allergySearchBean.add(AllergySearch);
					}
				}
				else
				{
					for(int i=offset;i<=limit;i++)
					{
							AllergySearchBean AllergySearch = finalListAfterSort.get(i-1);
							allergySearchBean.add(AllergySearch);
					}
				}
			}
			else
			{
				allergySearchBean = new ArrayList<AllergySearchBean>();
				if(offset == 0)
				{
					for(int i=offset;i<finalListAfterSort.size();i++)
					{
							AllergySearchBean AllergySearch = finalListAfterSort.get(i);
							allergySearchBean.add(AllergySearch);
					}
				}
				else
				{
					for(int i=offset;i<=finalListAfterSort.size();i++)
					{
							AllergySearchBean AllergySearch = finalListAfterSort.get(i-1);
							allergySearchBean.add(AllergySearch);
					}
				}
			}
			allergyBean.setCountValue(countCode);
			allergyBean.setAllergySearchBean(allergySearchBean);
		}
		return allergyBean;
	}

	/**
	 * Method to get count of drug codes for drug allergy search
	 * @param value
	 * @return
	 */
	public Integer getDrugCountCode(String value) {
		List<AllergySearchBean> drugDetailsList = getDrugDetailsSearch(value);
		List<AllergySearchBean> allergyClassList = getAllergyClassCountDetails(value);
		List<AllergySearchBean> finalList = new ArrayList<AllergySearchBean>();
		finalList.addAll(drugDetailsList);
		finalList.addAll(allergyClassList);
		return finalList.size();
	}

	/**
	 * Method to get count of Allergy Class
	 * @param value
	 * @return
	 */
	public List<AllergySearchBean> getAllergyClassCountDetails(String value) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<AllergyClass> root = cq.from(AllergyClass.class);
		cq.distinct(true);
		cq.multiselect(
				builder.literal(""),
				builder.concat(builder.trim(root.get(AllergyClass_.allergyClassSearchName)),
						root.get(AllergyClass_.allergyClassName)),
				builder.literal(CodeSystem.RXNORM));
		cq.where(builder.like(builder.lower(builder.trim(root.get(AllergyClass_.allergyClassSearchName))), getLikePattern(value)));
		List<Object[]> allergyClassDetails = em.createQuery(cq).getResultList();
		List<AllergySearchBean> allergyClassList = new ArrayList<AllergySearchBean>();
		for(Object[] values : allergyClassDetails)
		{
			AllergySearchBean allergyClass = new AllergySearchBean();
			allergyClass.setCode(values[0]==null?"":values[0].toString());
			allergyClass.setSubstance(values[1]==null?"":values[1].toString());
			allergyClass.setCodesystemid(values[2]==null?"":values[2].toString());
			allergyClassList.add(allergyClass);
		}
		return allergyClassList;
	}

	/**
	 * Method to get details for Allergy Class Search
	 * @param value
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<AllergySearchBean> getAllergyClassDetailsSearch(String value,
			int offset, int limit) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<AllergyClass> root = cq.from(AllergyClass.class);
		cq.distinct(true);
		cq.select(builder.construct(AllergySearchBean.class,
				builder.literal(""),
				builder.trim(root.get(AllergyClass_.allergyClassSearchName)),
				builder.literal(CodeSystem.RXNORM)));
		cq.where(builder.like(builder.lower(builder.trim(root.get(AllergyClass_.allergyClassSearchName))), getLikePattern(value)));
		List<Object> allergyClassDetails = em.createQuery(cq).getResultList();
		List<AllergySearchBean> allergyClassList = new ArrayList<AllergySearchBean>();
		for(int i=0;i<allergyClassDetails.size();i++)
		{
			AllergySearchBean allergyClass = (AllergySearchBean) allergyClassDetails.get(i);
			allergyClassList.add(allergyClass);
		}
		return allergyClassList;
	}

	/**
	 * Method to get details for drug search
	 * @param value
	 * @return
	 */
	public List<AllergySearchBean> getDrugDetailsSearch(String value) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<DrugDetails> root = cq.from(DrugDetails.class);
		Join<DrugDetails, DrugDetailsRxnormMap> detailsRxJoin= root.join(DrugDetails_.detailsRxnormMap,JoinType.LEFT);
		cq.distinct(true);
		cq.multiselect(
				builder.coalesce(detailsRxJoin.get(DrugDetailsRxnormMap_.drugDetailsRxnormMapRxnormCode),"-1"),
				builder.trim(root.get(DrugDetails_.drugDetailsName)),
				builder.literal(CodeSystem.RXNORM));
		cq.where(builder.and(builder.lessThanOrEqualTo(root.get(DrugDetails_.drugDetailsId),20000),
				builder.like(builder.lower(builder.trim(root.get(DrugDetails_.drugDetailsName))), getLikePattern(value))));
		List<Object[]> allergySearchDetails = em.createQuery(cq).getResultList();
		List<AllergySearchBean> allergySearchBeanList = new ArrayList<AllergySearchBean>();
		for(Object[] values : allergySearchDetails)
		{
			AllergySearchBean allergySearchBean = new AllergySearchBean();
			allergySearchBean.setCode(values[0]==null?"":values[0].toString());
			allergySearchBean.setSubstance(values[1]==null?"":values[1].toString());
			allergySearchBean.setCodesystemid(values[2]==null?"":values[2].toString());
			allergySearchBeanList.add(allergySearchBean);
		}
		return allergySearchBeanList;
	}

	/**
	 * Method to get count for allergy type id 9 (Imm./Inj)
	 * @param Criteria
	 * @return
	 */
	public Integer getLabCountCode(String Criteria) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<LabDescription> root = cq.from(LabDescription.class);
		cq.multiselect(
				builder.count(root.get(LabDescription_.labDescriptionTestid)),
						root.get(LabDescription_.labDescriptionTestid),
						root.get(LabDescription_.labDescriptionTestDesc));
		cq.where(root.get(LabDescription_.labDescriptionGroupid).in("36","5"),builder.equal(root.get(LabDescription_.labDescriptionIsactive), true),builder.like(builder.lower(root.get(LabDescription_.labDescriptionTestDesc)), getLikePattern(HUtil.ValidateSingleQuote(Criteria.toString()))));
		cq.groupBy(root.get(LabDescription_.labDescriptionTestid),root.get(LabDescription_.labDescriptionTestDesc));
		cq.orderBy(builder.asc(root.get(LabDescription_.labDescriptionTestDesc)));
		List<Object[]> countCode=em.createQuery(cq).getResultList();
		Integer getCountCode = countCode.size();
		return getCountCode;
	}

	/**
	 * Method to get details for allergy type id 9 (Imm./Inj)
	 * @param Criteria
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<AllergySearchBean> getDrugSearchLab(String Criteria, int offset, int limit) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<LabDescription> root = cq.from(LabDescription.class);
		cq.multiselect(
				root.get(LabDescription_.labDescriptionTestid),
						root.get(LabDescription_.labDescriptionTestDesc),
						builder.literal("-1").alias("codesystemoid"));
		cq.where(root.get(LabDescription_.labDescriptionGroupid).in("36","5"),builder.equal(root.get(LabDescription_.labDescriptionIsactive), true),builder.like(builder.lower(root.get(LabDescription_.labDescriptionTestDesc)), getLikePattern(HUtil.ValidateSingleQuote(Criteria.toString()))));
		cq.orderBy(builder.asc(root.get(LabDescription_.labDescriptionTestDesc)));
		List<Object[]> labDetails = em.createQuery(cq).setFirstResult(offset).setMaxResults(limit).getResultList();
		List<AllergySearchBean> labDescBeanList = new ArrayList<AllergySearchBean>();
		for(Object[] labDetailsValues : labDetails)
		{
			
			AllergySearchBean labDescBean = new AllergySearchBean();
			labDescBean.setCode(labDetailsValues[0]==null?"":labDetailsValues[0].toString());
			labDescBean.setSubstance(labDetailsValues[1]==null?"":labDetailsValues[1].toString());
			labDescBean.setCodesystemid(labDetailsValues[2]==null?"":labDetailsValues[2].toString());
			labDescBeanList.add(labDescBean);
		}
		return labDescBeanList;
	}

	/**
	 * For allergy type id 2,4,5 (Food Allergy,Environment,Substance) get count 
	 * @param Criteria
	 * @return
	 */
	public Integer getCountCode(String Criteria) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<Unii> root = cq.from(Unii.class);
		cq.multiselect(
				builder.count(root.get(Unii_.uniiCode)),
						root.get(Unii_.uniiSubstance),
						builder.literal(CodeSystem.UNII).alias("codesystemoid"));
		cq.where(builder.like(builder.lower(root.get(Unii_.uniiSubstance)), getLikePattern(HUtil.ValidateSingleQuote(Criteria.toString()))));
		cq.groupBy(root.get(Unii_.uniiCode),root.get(Unii_.uniiSubstance));
		cq.orderBy(builder.asc(root.get(Unii_.uniiSubstance)));
		List<Object[]> countCode=em.createQuery(cq).getResultList();
		Integer getCountCode = countCode.size();
		return getCountCode;
	}

	/**
	 * Allergen search for allergen type id 2,4,5 (Food Allergy,Environment,Substance)
	 * @param Criteria
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<AllergySearchBean> getDrugSearch(String Criteria, int offset, int limit) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<Unii> root = cq.from(Unii.class);
		cq.multiselect(builder.construct(AllergySearchBean.class,
				root.get(Unii_.uniiCode),
						root.get(Unii_.uniiSubstance),
						builder.literal(CodeSystem.UNII).alias("codesystemoid")));
		cq.where(builder.like(builder.lower(root.get(Unii_.uniiSubstance)), getLikePattern(HUtil.ValidateSingleQuote(Criteria.toString()))));
		cq.orderBy(builder.asc(root.get(Unii_.uniiSubstance)));
		List<Object> unii = em.createQuery(cq).setFirstResult(offset).setMaxResults(limit).getResultList();
		List<AllergySearchBean> uniiBeanList = new ArrayList<AllergySearchBean>();
		for(int i=0;i<unii.size();i++)
		{
			AllergySearchBean uniiBean = (AllergySearchBean) unii.get(i);
			uniiBeanList.add(uniiBean);
		}
		return uniiBeanList;
		
	}
	
	/**
	 * Method to search data for allergy reaction
	 */
	@Override
	public AllergyBean getReactionsOfAllergies(String Criteria, int offset, int limit) {
		AllergyBean allergyBean = new AllergyBean();
		List<String> reactionsOfAllergies = new ArrayList<String>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<PatientAllergies> root = cq.from(PatientAllergies.class);
		cq.distinct(true);
		cq.select(root.get(PatientAllergies_.patAllergReactionTo));
		cq.where(builder.like(builder.lower(root.get(PatientAllergies_.patAllergReactionTo)), HUtil.ValidateSingleQuote(getLikePattern(Criteria))));
		cq.orderBy(builder.asc(root.get(PatientAllergies_.patAllergReactionTo)));
		List<Object> reactions = em.createQuery(cq).setFirstResult(offset).setMaxResults(limit).getResultList();
		for(int i=0;i<reactions.size();i++)
		{
			reactionsOfAllergies.add(reactions.get(i).toString());
		}
		allergyBean.setCountValue(reactionsOfAllergies.size());
		allergyBean.setReactionName(reactionsOfAllergies);
		return allergyBean;
		
	}
	
	/**
	 * Method to get NKDA and NKA Check details
	 */
	@Override
	public List<PatientAllergies> getNKDAandNKACheckDetails(Integer ChartId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<PatientAllergies> root = cq.from(PatientAllergies.class);
		cq.multiselect(root.get(PatientAllergies_.patAllergId),
				root.get(PatientAllergies_.patAllergTypeId),
				root.get(PatientAllergies_.patAllergAllergicTo),
				root.get(PatientAllergies_.patAllergStatus));
		cq.where(builder.equal(root.get(PatientAllergies_.patAllergChartId),ChartId));
		List<Object[]> NKAResult = em.createQuery(cq).getResultList();
		List<PatientAllergies> NKDAandNKACheckDetails = new ArrayList<PatientAllergies>();
		for(Object[] values : NKAResult)
		{
			PatientAllergies NKDAandNKACheck = new PatientAllergies();
			NKDAandNKACheck.setPatAllergId(Integer.parseInt(values[0]==null?"-1":values[0].toString()));
			NKDAandNKACheck.setPatAllergTypeId(Integer.parseInt(values[1]==null?"-1":values[1].toString()));
			NKDAandNKACheck.setPatAllergAllergicTo(values[2]==null?"":values[2].toString());
			NKDAandNKACheck.setPatAllergStatus(Integer.parseInt(values[3]==null?"-1":values[3].toString()));
			NKDAandNKACheckDetails.add(NKDAandNKACheck);
		}
		return NKDAandNKACheckDetails;
	}
	
	/**
	 * Method to save New Allergies
	 */
	@Override
	public void saveNewAllergies(Integer save,String insertParameters){
		//em.getTransaction().begin();
		Integer rowId = getPatAllergiesMaxId();
		JSONObject jsonParameters = null;
		String chartId = null,encounterId = null,allergyType = null,severText = null,onSetDate = null,
				reaction = null,alergyName = null,userId = null,code = null,codeSystem = null,resolveDate = null,inactiveReason = null;
		Integer status = null;
		Timestamp current_date = new Timestamp(System.currentTimeMillis());
		System.out.println("current_date"+current_date);
		try {
			String insertParametersDecode=HUtil.Nz(URLDecoder.decode(insertParameters,"UTF-8"),"");
			System.out.println("insertparameters>>>"+insertParametersDecode);
			jsonParameters = new JSONObject(insertParametersDecode);
			chartId = jsonParameters.getString("chartId").toString();
			encounterId = jsonParameters.getString("encounterId");
			allergyType = jsonParameters.getString("typeId");
			status = Integer.parseInt(jsonParameters.getString("stat"));
			severText = jsonParameters.getString("severelevel");
			onSetDate = jsonParameters.getString("onsetDate");
			reaction = jsonParameters.getString("reaction");
			alergyName = jsonParameters.getString("allergyName");
			userId = jsonParameters.getString("userId");
			code = jsonParameters.getString("code");
			codeSystem = jsonParameters.getString("codeSystem");
		} catch (Exception e) {
			e.printStackTrace();
		}
		PatientAllergies patientAllergies = new PatientAllergies();
		patientAllergies.setPatAllergId(rowId+1);
		patientAllergies.setPatAllergChartId(Integer.parseInt(chartId));
		patientAllergies.setPatAllergEncounterId(Integer.parseInt(encounterId));
		System.out.println(">>>>"+rowId+">>>"+chartId+">>>"+encounterId);
		if(save == 1)
		{
			System.out.println("allergyType>>>alergyName>>>userId>>>current_date>>>code>>>reaction>>>onSetDate>>>status>>>severText>>codeSystem>>alergyName"+allergyType+">>"+alergyName+">>"+userId+">>"+current_date+">>"+reaction+">>"+onSetDate+">>"+status+">>"+severText+">>"+codeSystem+">>"+alergyName);
			patientAllergies.setPatAllergTypeId(Integer.parseInt(allergyType));
			patientAllergies.setPatAllergAllergicTo(alergyName);
			patientAllergies.setPatAllergCreatedBy(userId);
			patientAllergies.setPatAllergCreatedOn(current_date);
			patientAllergies.setPatAllergModifiedBy(userId);
			patientAllergies.setPatAllergModifiedOn(current_date);
			patientAllergies.setPatAllergAllergyCode(code);
			patientAllergies.setPatAllergReactionTo(reaction);
			patientAllergies.setPatAllergOnsetDate(onSetDate);
			patientAllergies.setPatAllergStatus(status);
			patientAllergies.setPatAllergResolvedBy("");
			patientAllergies.setPatAllergInactiveBy("");
			patientAllergies.setPatAllergInactiveOn(current_date);
			patientAllergies.setPatAllergSeverity(Integer.parseInt(severText));
			patientAllergies.setPatAllergInteractionXml("");
			patientAllergies.setPatAllergCodeSystem(codeSystem);
			patientAllergies.setPatAllergDrugCategory(alergyName);
			patientAllergies.setPatAllergInactiveReason("");
			patientAllergies.setPatAllergResolvedDate("");
			
			patientAllergiesRepository.saveAndFlush(patientAllergies);
		}
		else if(save == 4)
		{
			try
			{
				resolveDate= HUtil.Nz(jsonParameters.getString("resolveDate"),"");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			patientAllergies.setPatAllergTypeId(Integer.parseInt(allergyType));
			patientAllergies.setPatAllergAllergicTo(alergyName);
			patientAllergies.setPatAllergCreatedBy(userId);
			patientAllergies.setPatAllergCreatedOn(current_date);
			patientAllergies.setPatAllergModifiedBy(userId);
			patientAllergies.setPatAllergModifiedOn(current_date);
			patientAllergies.setPatAllergAllergyCode(code);
			patientAllergies.setPatAllergReactionTo(reaction);
			patientAllergies.setPatAllergOnsetDate(onSetDate);
			patientAllergies.setPatAllergStatus(status);
			patientAllergies.setPatAllergResolvedBy(userId);
			patientAllergies.setPatAllergInactiveBy(null);
			patientAllergies.setPatAllergInactiveOn(current_date);
			patientAllergies.setPatAllergSeverity(Integer.parseInt(severText));
			patientAllergies.setPatAllergInteractionXml(null);
			patientAllergies.setPatAllergCodeSystem(codeSystem);
			patientAllergies.setPatAllergDrugCategory(alergyName);
			patientAllergies.setPatAllergInactiveReason(null);
			patientAllergies.setPatAllergResolvedDate(resolveDate);
			patientAllergiesRepository.saveAndFlush(patientAllergies);
		}
		else if(save == 5)
		{
			try
			{
				inactiveReason= HUtil.Nz(jsonParameters.getString("inactiveReason"),"");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			patientAllergies.setPatAllergTypeId(Integer.parseInt(allergyType));
			patientAllergies.setPatAllergAllergicTo(alergyName);
			patientAllergies.setPatAllergCreatedBy(userId);
			patientAllergies.setPatAllergCreatedOn(current_date);
			patientAllergies.setPatAllergModifiedBy(userId);
			patientAllergies.setPatAllergModifiedOn(current_date);
			patientAllergies.setPatAllergAllergyCode(code);
			patientAllergies.setPatAllergReactionTo(reaction);
			patientAllergies.setPatAllergOnsetDate(onSetDate);
			patientAllergies.setPatAllergStatus(status);
			patientAllergies.setPatAllergResolvedBy(null);
			patientAllergies.setPatAllergInactiveBy(userId);
			patientAllergies.setPatAllergInactiveOn(current_date);
			patientAllergies.setPatAllergSeverity(Integer.parseInt(severText));
			patientAllergies.setPatAllergInteractionXml(null);
			patientAllergies.setPatAllergCodeSystem(codeSystem);
			patientAllergies.setPatAllergDrugCategory(alergyName);
			patientAllergies.setPatAllergInactiveReason(inactiveReason);
			patientAllergies.setPatAllergResolvedDate(null);
			patientAllergiesRepository.saveAndFlush(patientAllergies);
		}
	}

	/**
	 * Method to get MaxId from Patient Allergies 
	 * @return
	 */
	private Integer getPatAllergiesMaxId() {
		Integer maxId = 0;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<PatientAllergies> root = cq.from(PatientAllergies.class);
		cq.select(builder.greatest(root.get(PatientAllergies_.patAllergId)));
		Object maxIdObj = em.createQuery(cq).getSingleResult();
		if(maxIdObj!=null)
		{
			maxId = (Integer) maxIdObj;
		}
		return maxId;
	}
	
	/**
	 * Method to update new data for edited Allergies
	 */
	@Override
	public void editAllergies(String editData){
		JSONObject jsonParameters = null;
		String chartId = null,encounterId = null,allergyType = null,severText = null,onSetDate = null,
				reaction = null,allergyName = null,userId = null,code = null,codeSystem = null,resolveDate = null,inactiveReason = null,id=null;
		Integer status = null;
		try {
			String editDataDecode=HUtil.Nz(URLDecoder.decode(editData,"UTF-8"),"");
			jsonParameters = new JSONObject(editDataDecode);
			chartId = jsonParameters.getString("chartId").toString();
			encounterId = jsonParameters.getString("encounterId");
			allergyType = jsonParameters.getString("typeId");
			status = Integer.parseInt(jsonParameters.getString("stat"));
			id = HUtil.Nz(jsonParameters.getString("id"), "-1");
			severText = jsonParameters.getString("severelevel");
			onSetDate = jsonParameters.getString("onsetDate");
			reaction = jsonParameters.getString("reaction");
			allergyName = jsonParameters.getString("allergyName");
			userId = jsonParameters.getString("userId");
			code = jsonParameters.getString("code");
			codeSystem = jsonParameters.getString("codeSystem");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(status == 4)
		{
			deleteAllergies(id);
		}
		else
		{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaUpdate<PatientAllergies> update = cb.createCriteriaUpdate(PatientAllergies.class);
			Root<PatientAllergies> rootCriteria = update.from(PatientAllergies.class);
			update.set(rootCriteria.get(PatientAllergies_.patAllergChartId), Integer.parseInt(chartId));
			update.set(rootCriteria.get(PatientAllergies_.patAllergEncounterId), Integer.parseInt(encounterId));
			update.set(rootCriteria.get(PatientAllergies_.patAllergTypeId), Integer.parseInt(allergyType));
			update.set(rootCriteria.get(PatientAllergies_.patAllergAllergicTo), allergyName);
			update.set(rootCriteria.get(PatientAllergies_.patAllergModifiedBy), userId);
			update.set(rootCriteria.get(PatientAllergies_.patAllergModifiedOn), patientAllergiesRepository.findCurrentTimeStamp());
			update.set(rootCriteria.get(PatientAllergies_.patAllergAllergyCode), code);
			update.set(rootCriteria.get(PatientAllergies_.patAllergReactionTo), reaction);
			update.set(rootCriteria.get(PatientAllergies_.patAllergOnsetDate), onSetDate);
			update.set(rootCriteria.get(PatientAllergies_.patAllergStatus), status);
			update.set(rootCriteria.get(PatientAllergies_.patAllergSeverity), Integer.parseInt(severText));
			update.set(rootCriteria.get(PatientAllergies_.patAllergCodeSystem), codeSystem);
			if(status == 2)
			{
				try{
					resolveDate= HUtil.Nz(jsonParameters.getString("resolveDate"),"");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				update.set(rootCriteria.get(PatientAllergies_.patAllergResolvedBy), userId);
				update.set(rootCriteria.get(PatientAllergies_.patAllergResolvedDate), resolveDate);
			}
			else if(status == 3){
				try{
				  inactiveReason = HUtil.Nz(jsonParameters.getString("inactiveReason"), "");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				  update.set(rootCriteria.get(PatientAllergies_.patAllergInactiveBy), userId);
				  update.set(rootCriteria.get(PatientAllergies_.patAllergInactiveOn), patientAllergiesRepository.findCurrentTimeStamp());
				  update.set(rootCriteria.get(PatientAllergies_.patAllergInactiveReason), inactiveReason);
			}
			update.where(cb.equal(rootCriteria.get(PatientAllergies_.patAllergId),Integer.parseInt(id)));
			this.em.createQuery(update).executeUpdate();
		}
	}

	/**
	 * Method to delete Allergies
	 */
	@Override
	public String deleteAllergy(String allergyid) {
		try
		{
			JSONObject allergyDetails=new JSONObject(allergyid);
			JSONArray allergyIdsArray=new JSONArray(HUtil.Nz(allergyDetails.getString("allergiesIds"),""));
			for(int i=0;i<allergyIdsArray.length();i++){
			    String eachAllergyId=HUtil.Nz(allergyIdsArray.getString(i),"-1");
			    deleteAllergies(eachAllergyId);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		String status = "success";
		return status;
	}
	
	/**
	 * Method to delete Allergies Criteria Query
	 * @param allergyid
	 */
	public void deleteAllergies(String allergyid) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<PatientAllergies> update = cb.createCriteriaUpdate(PatientAllergies.class);
		Root<PatientAllergies> rootCriteria = update.from(PatientAllergies.class);
		update.set(rootCriteria.get(PatientAllergies_.patAllergStatus), 4);
		update.where(cb.equal(rootCriteria.get(PatientAllergies_.patAllergId),Integer.parseInt(allergyid)));
		this.em.createQuery(update).executeUpdate();
	}
	
	/**
	 * Method to update review allergies
	 */
	@Override
	public void reviewAllergies(String reviewData){
		JSONObject jsonReview;
		String chartId=null,encounterId=null,userId=null;
		try {
			jsonReview = new JSONObject(reviewData);
			chartId = HUtil.Nz(jsonReview.getString("chartId"),"-1");
			encounterId = HUtil.Nz(jsonReview.getString("encounterId"),"-1");
			userId = HUtil.Nz(jsonReview.getString("userId"),"-1");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		boolean isReviewed=true;
		List<AllergiesEncountermap> allergiesEncountermap= allergiesEncountermapRepository.findAll(AllergiesSpecification.getbyChartIdAndReview(chartId));
		String checkstatus;
		if(allergiesEncountermap.size()>0)
		{
			checkstatus = allergiesEncountermap.get(0).getAllergencmapId()==null?"-1":allergiesEncountermap.get(0).getAllergencmapId().toString();
		}
		else
		{
			checkstatus = "-1";
		}
		if(!(checkstatus.equalsIgnoreCase("-1")))
		{
			List<AllergiesEncountermap> allergiesEncountermapEntityList= allergiesEncountermapRepository.findAll(AllergiesSpecification.getbyChartId(chartId));
			for(int i=0;i<allergiesEncountermapEntityList.size();i++)
			{
				AllergiesEncountermap allergiesEncountermapEntity = allergiesEncountermapEntityList.get(i);
				allergiesEncountermapEntity.setAllergencmapReviewed(false);
				allergiesEncountermapRepository.saveAndFlush(allergiesEncountermapEntity);
			}
		}
		Integer greatestAllergenId = getGreatestAllergenId();
		AllergiesEncountermap allergiesEncountermapInsert = new AllergiesEncountermap();
		allergiesEncountermapInsert.setAllergencmapId(greatestAllergenId+1);
		allergiesEncountermapInsert.setAllergencmapChartid(Integer.parseInt(chartId));
		allergiesEncountermapInsert.setAllergencmapEncounterid(Integer.parseInt(encounterId));
		allergiesEncountermapInsert.setAllergencmapAllergid("-1");
		allergiesEncountermapInsert.setAllergencmapReviewed(isReviewed);
		allergiesEncountermapInsert.setAllergencmapReviewedby(userId);
		allergiesEncountermapInsert.setAllergencmapReviewedon(allergiesEncountermapRepository.findCurrentTimeStamp());
		allergiesEncountermapRepository.saveAndFlush(allergiesEncountermapInsert);
	}

	/**
	 * Method to get MaxId from AllergiesEncountermap
	 * @return
	 */
	public Integer getGreatestAllergenId() {
		Integer maxId = 0;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<AllergiesEncountermap> root = cq.from(AllergiesEncountermap.class);
		cq.select(builder.greatest(root.get(AllergiesEncountermap_.allergencmapId)));
		Object maxIdObj = em.createQuery(cq).getSingleResult();
		if(maxIdObj!=null)
		{
			maxId = (Integer) maxIdObj;
		}
		return maxId;
	}
	
	/**
	 * Method to update NKDA check or Uncheck
	 */
	@Override
	public void nkdaUpdate(String nkdaData)
	{
		int ROWID 	= 0;
		int TYPEID  = -1;
		String chartId = null,encounterId = null,userId = null;
		JSONObject jsonNKDA;
		try {
			jsonNKDA = new JSONObject(nkdaData);
			chartId = HUtil.Nz(jsonNKDA.getString("chartId"),"-1");
			encounterId = HUtil.Nz(jsonNKDA.getString("encounterId"),"-1");
			userId = HUtil.Nz(jsonNKDA.getString("userId"),"-1");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		List<PatientAllergies> patAllerg = patientAllergiesRepository.findAll(AllergiesSpecification.getbyChartIdAndTypeId(chartId,TYPEID));
		Integer existId = null;
		if(patAllerg.size()>0)
		{
			existId = patAllerg.get(0).getPatAllergTypeId()==null?null:patAllerg.get(0).getPatAllergTypeId();
		}

		if(existId == null)
		{
			ROWID = getPatAllergiesMaxId()+1;
			Object allergTypeName = getAllergyTypeName(TYPEID);
			PatientAllergies patientAllergies = new PatientAllergies();
			patientAllergies.setPatAllergId(ROWID);
			patientAllergies.setPatAllergChartId(Integer.parseInt(chartId));
			patientAllergies.setPatAllergEncounterId(Integer.parseInt(encounterId));
			patientAllergies.setPatAllergTypeId(TYPEID);
			patientAllergies.setPatAllergAllergicTo(allergTypeName.toString());
			patientAllergies.setPatAllergStatus(Integer.parseInt(userId));
			patientAllergies.setPatAllergCreatedOn(patientAllergiesRepository.findCurrentTimeStamp());
			patientAllergies.setPatAllergCreatedBy(userId);
			patientAllergies.setPatAllergModifiedOn(patientAllergiesRepository.findCurrentTimeStamp());
			patientAllergies.setPatAllergModifiedBy(userId);
			patientAllergies.setPatAllergCodeSystem(Integer.toString(TYPEID));
			patientAllergiesRepository.saveAndFlush(patientAllergies);
		}
		else
		{
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq = builder.createQuery(Object.class);
			Root<PatientAllergies> root = cq.from(PatientAllergies.class);
			cq.select(root.get(PatientAllergies_.patAllergStatus));
			cq.where(builder.and(builder.equal(root.get(PatientAllergies_.patAllergModifiedBy), userId),
					builder.equal(root.get(PatientAllergies_.patAllergChartId), chartId),
					builder.equal(root.get(PatientAllergies_.patAllergTypeId), TYPEID)));
			Object patAllergStatusObj = em.createQuery(cq).getSingleResult();
			String patAllergStatus = patAllergStatusObj==null?"-1":patAllergStatusObj.toString();
			if(!patAllergStatus.equalsIgnoreCase("1"))
			{
				PatientAllergies patAllergUpdate = patientAllergiesRepository.findOne(AllergiesSpecification.getbyChartIdAndTypeId(chartId, TYPEID));
				patAllergUpdate.setPatAllergStatus(1);
				patAllergUpdate.setPatAllergModifiedOn(patientAllergiesRepository.findCurrentTimeStamp());
				patAllergUpdate.setPatAllergModifiedBy(userId);
				patientAllergiesRepository.saveAndFlush(patAllergUpdate);
			}
		}
	}

	/**
	 * Method to get allergy type name for NKA or NKDA update
	 * @param tYPEID
	 * @return
	 */
	private Object getAllergyTypeName(int tYPEID) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object> cq = builder.createQuery(Object.class);
		Root<AllergiesType> root = cq.from(AllergiesType.class);
		cq.select(root.get(AllergiesType_.allergtypeName));
		cq.where(builder.equal(root.get(AllergiesType_.allergtypeId), tYPEID));
		Object allergTypeName = em.createQuery(cq).getSingleResult();
		return allergTypeName;
	}
	
	/**
	 * Method to update NKA check or uncheck
	 */
	@Override
	public void nkaUpdate(String nkaData){
		int ROWID 	= 0;
		int TYPEID  = 0; 
		String chartId = null,encounterId = null,userId = null;
				TYPEID=-2;
				JSONObject jsonNKDA;
				try {
					jsonNKDA = new JSONObject(nkaData);
					chartId = HUtil.Nz(jsonNKDA.getString("chartId"),"-1");
					encounterId = HUtil.Nz(jsonNKDA.getString("encounterId"),"-1");
					userId = HUtil.Nz(jsonNKDA.getString("userId"),"-1");
				} catch (JSONException e) {
					e.printStackTrace();
				}
		List<PatientAllergies> patAllerg = patientAllergiesRepository.findAll(AllergiesSpecification.getbyChartIdAndTypeId(chartId,TYPEID));
		Integer existId = null;
		if(patAllerg.size()>0)
		{
			existId = patAllerg.get(0).getPatAllergTypeId()==null?null:patAllerg.get(0).getPatAllergTypeId();
		}
		
		
		if(existId == null)
		{
			ROWID = getPatAllergiesMaxId()+1;
			Object allergTypeName = getAllergyTypeName(TYPEID);
			PatientAllergies patientAllergies = new PatientAllergies();
			patientAllergies.setPatAllergId(ROWID);
			patientAllergies.setPatAllergChartId(Integer.parseInt(chartId));
			patientAllergies.setPatAllergEncounterId(Integer.parseInt(encounterId));
			patientAllergies.setPatAllergTypeId(TYPEID);
			patientAllergies.setPatAllergAllergicTo(allergTypeName.toString());
			patientAllergies.setPatAllergStatus(1);
			patientAllergies.setPatAllergCreatedOn(patientAllergiesRepository.findCurrentTimeStamp());
			patientAllergies.setPatAllergCreatedBy(userId);
			patientAllergies.setPatAllergModifiedOn(patientAllergiesRepository.findCurrentTimeStamp());
			patientAllergies.setPatAllergModifiedBy(userId);
			patientAllergies.setPatAllergCodeSystem(Integer.toString(TYPEID));
			patientAllergiesRepository.saveAndFlush(patientAllergies);
		}
		else
		{
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq = builder.createQuery(Object.class);
			Root<PatientAllergies> root = cq.from(PatientAllergies.class);
			cq.select(root.get(PatientAllergies_.patAllergStatus));
			cq.where(builder.and(builder.equal(root.get(PatientAllergies_.patAllergModifiedBy), userId),
					builder.equal(root.get(PatientAllergies_.patAllergChartId), chartId),
					builder.equal(root.get(PatientAllergies_.patAllergTypeId), TYPEID)));
			Object patAllergStatusObj = em.createQuery(cq).getSingleResult();
			String patAllergStatus = patAllergStatusObj==null?"-1":patAllergStatusObj.toString();
			if(!patAllergStatus.equalsIgnoreCase("1"))
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaUpdate<PatientAllergies> update = cb.createCriteriaUpdate(PatientAllergies.class);
				Root<PatientAllergies> rootCriteria = update.from(PatientAllergies.class);
				update.set(rootCriteria.get(PatientAllergies_.patAllergStatus), 1);
				update.set(rootCriteria.get(PatientAllergies_.patAllergModifiedOn), patientAllergiesRepository.findCurrentTimeStamp());
				update.set(rootCriteria.get(PatientAllergies_.patAllergModifiedBy), userId);
				update.where(cb.and(cb.equal(rootCriteria.get(PatientAllergies_.patAllergChartId), chartId),cb.equal(rootCriteria.get(PatientAllergies_.patAllergTypeId), TYPEID)));
				this.em.createQuery(update).executeUpdate();
			}
		}
	}
	
	/**
	 * Method to update patient allergies for NKDA Uncheck
	 */
	@Override
	public void nkdauncheck(String nkdaData){
		JSONObject jsonNKDA;
		String chartId = null;
		try {
			jsonNKDA = new JSONObject(nkdaData);
			chartId = HUtil.Nz(jsonNKDA.getString("chartId"),"-1");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		PatientAllergies patAllerg = patientAllergiesRepository.findOne(AllergiesSpecification.getbyChartIdAndTypeId(chartId, -1));
		patAllerg.setPatAllergStatus(2);
		patientAllergiesRepository.saveAndFlush(patAllerg);
	}
	
	/**
	 * Method to update patient allergies for NKA Uncheck
	 */
	@Override
	public void nkauncheck(String nkaData){
		JSONObject jsonNKDA;
		String chartId = null;
		try {
			jsonNKDA = new JSONObject(nkaData);
			chartId = HUtil.Nz(jsonNKDA.getString("chartId"),"-1");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		PatientAllergies patAllerg = patientAllergiesRepository.findOne(AllergiesSpecification.getbyChartIdAndTypeId(chartId, -2));
		patAllerg.setPatAllergStatus(2);
		patientAllergiesRepository.saveAndFlush(patAllerg);
	}
	
	/**
	 * Method to retrieve details for edit Allergies
	 */
	@Override
	public List<PatientAllergies> retrieveDataForEditAllerg(String chartId,String patId)
	{
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<PatientAllergies> root = cq.from(PatientAllergies.class);
		Join<PatientAllergies, AllergiesType> allergType = root.join(PatientAllergies_.allergiesType,JoinType.INNER);
		
		cq.multiselect(root.get(PatientAllergies_.patAllergTypeId),
				root.get(PatientAllergies_.patAllergAllergicTo),
				allergType.get(AllergiesType_.allergtypeName),
				root.get(PatientAllergies_.patAllergDrugCategory),
				root.get(PatientAllergies_.patAllergReactionTo),
				root.get(PatientAllergies_.patAllergOnsetDate),
				root.get(PatientAllergies_.patAllergStatus),
				root.get(PatientAllergies_.patAllergSeverity),
				root.get(PatientAllergies_.patAllergResolvedDate),
				root.get(PatientAllergies_.patAllergAllergyCode),
				root.get(PatientAllergies_.patAllergCodeSystem)
				);
		cq.where(builder.and(builder.notEqual(root.get(PatientAllergies_.patAllergStatus), 3),
				builder.equal(root.get(PatientAllergies_.patAllergChartId), Integer.parseInt(chartId)),
				builder.greaterThan(root.get(PatientAllergies_.patAllergTypeId), 0),
				builder.equal(root.get(PatientAllergies_.patAllergId), Integer.parseInt(patId))));
		
		List<Object[]> editData = em.createQuery(cq).getResultList();
		List<PatientAllergies> patAllergBean = new ArrayList<PatientAllergies>();
		for(Object[] values : editData)
		{
			PatientAllergies patAllerg = new PatientAllergies();
			AllergiesType allergTypeBean = new AllergiesType();
			patAllerg.setPatAllergTypeId(Integer.parseInt(values[0]==null?"-1":values[0].toString()));
			patAllerg.setPatAllergAllergicTo(values[1]==null?"":values[1].toString());
			allergTypeBean.setAllergtypeName(values[2]==null?"":values[2].toString());
			patAllerg.setAllergiesType(allergTypeBean);
			patAllerg.setPatAllergDrugCategory(values[3]==null?"":values[3].toString());
			patAllerg.setPatAllergReactionTo(values[4]==null?"":values[4].toString());
			patAllerg.setPatAllergOnsetDate(values[5]==null?"":values[5].toString());
			patAllerg.setPatAllergStatus(Integer.parseInt(values[6]==null?"-1":values[6].toString()));
			patAllerg.setPatAllergSeverity(Integer.parseInt(values[7]==null?"":values[7].toString()));
			patAllerg.setPatAllergResolvedDate(values[8]==null?"":values[8].toString());
			patAllerg.setPatAllergAllergyCode(values[9]==null?"":values[9].toString());
			patAllerg.setPatAllergCodeSystem(values[10]==null?"":values[10].toString());
			patAllergBean.add(patAllerg);
		}
		return patAllergBean;
	}
	
	/**
	 * Method to retrieve details for In active Allergies
	 */
	@Override
	public List<PatientAllergies> retrieveInActiveAllerg(String chartId)
	{
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<PatientAllergies> root = cq.from(PatientAllergies.class);
		Join<PatientAllergies, AllergiesType> allergType = root.join(PatientAllergies_.allergiesType,JoinType.INNER);
		
		cq.multiselect(root.get(PatientAllergies_.patAllergId),
				root.get(PatientAllergies_.patAllergTypeId),
				allergType.get(AllergiesType_.allergtypeName),
				root.get(PatientAllergies_.patAllergAllergicTo),
				root.get(PatientAllergies_.patAllergDrugCategory),
				root.get(PatientAllergies_.patAllergReactionTo),
				root.get(PatientAllergies_.patAllergOnsetDate),
				root.get(PatientAllergies_.patAllergStatus),
				root.get(PatientAllergies_.patAllergSeverity),
				root.get(PatientAllergies_.patAllergResolvedDate)
				);
		cq.where(builder.and((root.get(PatientAllergies_.patAllergStatus).in(2,3)),
				builder.equal(root.get(PatientAllergies_.patAllergChartId), Integer.parseInt(chartId)),
				builder.greaterThan(root.get(PatientAllergies_.patAllergTypeId), 0)
				));
		cq.orderBy(builder.asc(root.get(PatientAllergies_.patAllergId)));
		List<Object[]> editData = em.createQuery(cq).getResultList();
		List<PatientAllergies> patAllergBean = new ArrayList<PatientAllergies>();
		for(Object[] values : editData)
		{
			PatientAllergies patAllerg = new PatientAllergies();
			AllergiesType allergTypeBean = new AllergiesType();
			patAllerg.setPatAllergId(Integer.parseInt(values[0]==null?"-1":values[0].toString()));
			patAllerg.setPatAllergTypeId(Integer.parseInt(values[1]==null?"-1":values[1].toString()));
			allergTypeBean.setAllergtypeName(values[2]==null?"":values[2].toString());
			patAllerg.setAllergiesType(allergTypeBean);
			patAllerg.setPatAllergAllergicTo(values[3]==null?"":values[3].toString());
			patAllerg.setPatAllergDrugCategory(values[4]==null?"":values[4].toString());
			patAllerg.setPatAllergReactionTo(values[5]==null?"":values[5].toString());
			patAllerg.setPatAllergOnsetDate(values[6]==null?"":values[6].toString());
			patAllerg.setPatAllergStatus(Integer.parseInt(values[7]==null?"-1":values[7].toString()));
			patAllerg.setPatAllergSeverity(Integer.parseInt(values[8]==null?"":values[8].toString()));
			patAllerg.setPatAllergResolvedDate(values[9]==null?"":values[9].toString());
			patAllergBean.add(patAllerg);
		}
		return patAllergBean;
	}
	
	/**
	 * Method to get last review details
	 */
	@Override
	public JSONArray lastReviewDetails(String chartId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);
		Root<AllergiesEncountermap> root = cq.from(AllergiesEncountermap.class);
		Join<AllergiesEncountermap, EmployeeProfile> empJoin = root.join(AllergiesEncountermap_.empProfile,JoinType.INNER);
		cq.multiselect(builder.function("format_name", String.class, builder.coalesce(empJoin.get(EmployeeProfile_.empProfileFname), ""),builder.coalesce(empJoin.get(EmployeeProfile_.empProfileLname),""),
				builder.coalesce(empJoin.get(EmployeeProfile_.empProfileMi), ""),builder.coalesce(empJoin.get(EmployeeProfile_.empProfileCredentials), ""),builder.literal(1)),
				builder.function("glace_timezone", String.class, root.get(AllergiesEncountermap_.allergencmapReviewedon),builder.literal("EDT"),builder.literal("MM/dd/yyyy HH:MI:ss am")).alias("reviewOn")
				);
		cq.where(builder.and(builder.equal(root.get(AllergiesEncountermap_.allergencmapChartid), chartId),
				builder.equal(root.get(AllergiesEncountermap_.allergencmapReviewed), true)));
		cq.orderBy(builder.asc(builder.function("glace_timezone", String.class, root.get(AllergiesEncountermap_.allergencmapReviewedon),builder.literal("EDT"),builder.literal("MM/dd/yyyy HH:MI:ss am"))));
		List<Object[]> lastReviewDetailsObj = em.createQuery(cq).getResultList();
		JSONArray allergEncMapArray = new JSONArray();
		int i=0;
		for(Object[] values : lastReviewDetailsObj)
		{
			JSONObject allergEncMapObj = new JSONObject();
			try {
				allergEncMapObj.put("reviewby", values[0]==null?"":values[0].toString());
				allergEncMapObj.put("reviewon", values[1]==null?"":values[1].toString());
				allergEncMapArray.put(i, allergEncMapObj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			i++;
		}
		return allergEncMapArray;
	}
	
	/**
	 * Returns the formatted the pattern
	 * @param searchTerm
	 * @return
	 */
	private static String getLikePattern(final String searchTerm) {
		StringBuilder pattern = new StringBuilder();
		pattern.append(searchTerm.toLowerCase());
		pattern.append("%");
		return pattern.toString();
	}
	
	/**
	 * Method to sort values for drug allergy search
	 * @param jsonValues
	 * @return
	 */
	public List<AllergySearchBean> sortBy(List<AllergySearchBean> jsonValues) {
		Collections.sort(jsonValues, new Comparator<AllergySearchBean>() {
			@Override
			public int compare(AllergySearchBean arg0, AllergySearchBean arg1) {
				try {
					if (arg0.getSubstance().toString().equalsIgnoreCase("") && arg1.getSubstance().toString().equalsIgnoreCase("")) {
	                    return 0;
	                }
	                if (arg0.getSubstance().toString().equalsIgnoreCase("")) {
	                    return 1;
	                }
	                if (arg1.getSubstance().toString().equalsIgnoreCase("")) {
	                    return -1;
	                }
	                if(!((arg0.getSubstance().toString().equalsIgnoreCase("") && arg1.getSubstance().toString().equalsIgnoreCase("")) || arg0.getSubstance().toString().equalsIgnoreCase("") || arg1.getSubstance().toString().equalsIgnoreCase("")))
	                {
							return arg0.getSubstance().toString().compareTo(arg1.getSubstance().toString());
	                }
					return arg1.getSubstance().toString().compareTo(arg0.getSubstance().toString());
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		});
		return jsonValues;
	}
}
