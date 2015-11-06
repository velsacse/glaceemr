package com.glenwood.glaceemr.server.application.specifications;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Hl7ExternalTest;
import com.glenwood.glaceemr.server.application.models.Hl7ExternalTest_;
import com.glenwood.glaceemr.server.application.models.Hl7ExternalTestmapping;
import com.glenwood.glaceemr.server.application.models.Hl7ResultInbox;
import com.glenwood.glaceemr.server.application.models.Hl7ResultInbox_;
import com.glenwood.glaceemr.server.application.models.LabAlertforwardstatus;
import com.glenwood.glaceemr.server.application.models.LabAlertforwardstatus_;
import com.glenwood.glaceemr.server.application.models.LabDescpParameters;
import com.glenwood.glaceemr.server.application.models.LabDescription;
import com.glenwood.glaceemr.server.application.models.LabDescription_;
import com.glenwood.glaceemr.server.application.models.LabEntries;
import com.glenwood.glaceemr.server.application.models.LabEntriesParameter;
import com.glenwood.glaceemr.server.application.models.LabEntriesParameter_;
import com.glenwood.glaceemr.server.application.models.LabEntries_;
import com.glenwood.glaceemr.server.application.models.LabGroups;
import com.glenwood.glaceemr.server.application.models.LabGroups_;
import com.glenwood.glaceemr.server.application.models.LabIncludePrevious;
import com.glenwood.glaceemr.server.application.models.LabIncludePrevious_;
import com.glenwood.glaceemr.server.application.models.LabParameterCode;
import com.glenwood.glaceemr.server.application.models.LabParameterCode_;
import com.glenwood.glaceemr.server.application.models.LabParameters;
import com.glenwood.glaceemr.server.application.models.LabParameters_;
import com.glenwood.glaceemr.server.application.models.PatientRegistration;
import com.glenwood.glaceemr.server.application.models.PatientRegistration_;
import com.glenwood.glaceemr.server.application.models.Specimen;
import com.glenwood.glaceemr.server.application.models.Specimen_;

/**
 * @author yasodha
 * 
 * InvestigationSpecification contains all the predicates (rules)
 * required to get the lab data for investigation module.
 */
public class InvestigationSpecification {

	/**
	 * Specification to get test details
	 * @param testId
	 * @return
	 */
	public static Specification<LabDescription> labByTestId(final Integer testId) {
		return new Specification<LabDescription>() {

			@Override
			public Predicate toPredicate(Root<LabDescription> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate vis = cb.equal(root.get(LabDescription_.labDescriptionTestid), testId);
				return vis;
			}
		};
	}
	
	/**
	 * Specification to get the list of labs having testId
	 * @param testId
	 * @return Specification<LabEntries>
	 */
	public static Specification<LabEntries> testIdsWithOrder(final Integer[] testId)
	{
		return new Specification<LabEntries>() {

			@Override
			public Predicate toPredicate(Root<LabEntries> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				query.orderBy(cb.asc(root.get(LabEntries_.labEntriesTestDesc)),cb.desc(cb.coalesce(root.get(LabEntries_.labEntriesPerfOn),cb.function("to_timestamp",Timestamp.class,cb.literal("1900-05-13 16:40:35"),cb.literal("YYYY-MM-DD HH24:MI:SS")))));
				Predicate testIds = root.get(LabEntries_.labEntriesTestId).in((Object[])testId);
				return testIds;
			}
		};
	}

	/**
	 * Specification to get the list of labs having chart Id
	 * @param chart Id
	 * @return Specification<LabEntries>
	 */
	public static Specification<LabEntries> chartId(final Integer chartId)
	{
		return new Specification<LabEntries>() {

			@Override
			public Predicate toPredicate(Root<LabEntries> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate chartIdPred = cb.equal(root.get(LabEntries_.labEntriesChartid),chartId);
				return chartIdPred;
			}
		};
	}

	/**
	 * Specification to get the list of labs having status greater than
	 * @param status
	 * @return Specification<LabEntries>
	 */
	public static Specification<LabEntries> statusGreaterThan(final Integer status)
	{
		return new Specification<LabEntries>() {

			@Override
			public Predicate toPredicate(Root<LabEntries> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate statusGreaterThan = cb.greaterThan(root.get(LabEntries_.labEntriesTestStatus),status);
				return statusGreaterThan;
			}
		};
	}

	/**
	 * Specification to get the list of labs having status not equal to
	 * @param status
	 * @return Specification<LabEntries>
	 */
	public static Specification<LabEntries> statusNotEqual(final Integer status)
	{
		return new Specification<LabEntries>() {

			@Override
			public Predicate toPredicate(Root<LabEntries> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate statusNotEqual = cb.notEqual(root.get(LabEntries_.labEntriesTestStatus),status);
				return statusNotEqual;
			}
		};
	}

	/**
	 * Specification to get the list of labs having status equal to
	 * @param status
	 * @return Specification<LabEntries>
	 */
	public static Specification<LabEntries> statusEqual(final Integer status)
	{
		return new Specification<LabEntries>() {

			@Override
			public Predicate toPredicate(Root<LabEntries> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate statusEqual = cb.equal(root.get(LabEntries_.labEntriesTestStatus),status);
				return statusEqual;
			}
		};
	}

	/**
	 * Specification to get the list of orders having testId
	 * @param testId
	 * @param chartId
	 * @param status greater than
	 * @param status not equal
	 * @return Specification<LabEntries>
	 */
	public static Specification<LabEntries> getOrderDetailsExceptVaccines(final Integer[] testId,final Integer chartId,final Integer statusGreaterThan,final Integer statusNotEqual,final Integer encounterId,final Encounter encounterEntity)
	{
		return new Specification<LabEntries>() {

			@Override
			public Predicate toPredicate(Root<LabEntries> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				query.orderBy(cb.desc(cb.coalesce(root.get(LabEntries_.labEntriesPerfOn),cb.function("to_timestamp",Timestamp.class,cb.literal("1900-05-13 16:40:35"),cb.literal("YYYY-MM-DD HH24:MI:SS")))));
				Predicate testIdPred=root.get(LabEntries_.labEntriesTestId).in((Object[])testId);
				Predicate testStatusGreaterPred=cb.greaterThan(root.get(LabEntries_.labEntriesTestStatus),statusGreaterThan);
				Predicate testStatusNotEqualPred=cb.notEqual(root.get(LabEntries_.labEntriesTestStatus),statusNotEqual);
				Predicate chartIdPred=cb.equal(root.get(LabEntries_.labEntriesChartid),chartId);
				Predicate groupIdVaccPred=cb.notEqual(root.get(LabEntries_.labEntriesGroupid),36);
				Predicate groupIdsPred = null;
				if((encounterId!=-1)&&(encounterEntity.getEncounterStatus()==3)){
					Predicate encounterDatePred=cb.lessThanOrEqualTo(root.get(LabEntries_.labEntriesOrdOn), encounterEntity.getEncounterDate());
					groupIdsPred=cb.and(testIdPred,testStatusGreaterPred,testStatusNotEqualPred,chartIdPred,groupIdVaccPred,encounterDatePred);
				}else{
					groupIdsPred=cb.and(testIdPred,testStatusGreaterPred,testStatusNotEqualPred,chartIdPred,groupIdVaccPred);
				}
				return groupIdsPred;
			}
		};
	}
	
	/**
	 * Specification to get the list of orders having testId
	 * @param testId
	 * @param chartId
	 * @param status greater than
	 * @param status not equal
	 * @return Specification<LabEntries>
	 */
	public static Specification<LabEntries> getOrderDetailsVaccines(final Integer[] testId,final Integer chartId,final Integer statusGreaterThan,final Integer statusNotEqual,final Integer encounterId,final Encounter encounterEntity)
	{
		return new Specification<LabEntries>() {

			@Override
			public Predicate toPredicate(Root<LabEntries> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				query.orderBy(cb.desc(cb.coalesce(root.get(LabEntries_.labEntriesPerfOn),cb.function("to_timestamp",Timestamp.class,cb.literal("1900-05-13 16:40:35"),cb.literal("YYYY-MM-DD HH24:MI:SS")))));
				Predicate testIdPred=root.get(LabEntries_.labEntriesTestId).in((Object[])testId);
				Predicate testStatusGreaterPred=cb.greaterThan(root.get(LabEntries_.labEntriesTestStatus),statusGreaterThan);
				Predicate testStatusNotEqualPred=cb.notEqual(root.get(LabEntries_.labEntriesTestStatus),statusNotEqual);
				Predicate chartIdPred=cb.equal(root.get(LabEntries_.labEntriesChartid),chartId);
				Predicate groupIdPred=cb.equal(root.get(LabEntries_.labEntriesGroupid),36);
				Predicate groupIdsPred =cb.and(testIdPred,testStatusGreaterPred,testStatusNotEqualPred,chartIdPred,groupIdPred);
				if((encounterId!=-1)&&(encounterEntity.getEncounterStatus()==3)){
					Predicate encounterDatePred=cb.lessThanOrEqualTo(root.get(LabEntries_.labEntriesOrdOn), encounterEntity.getEncounterDate());
					groupIdsPred =cb.and(testIdPred,testStatusGreaterPred,testStatusNotEqualPred,chartIdPred,groupIdPred,encounterDatePred);
				}else{
					 groupIdsPred =cb.and(testIdPred,testStatusGreaterPred,testStatusNotEqualPred,chartIdPred,groupIdPred);
				}
				return groupIdsPred;
			}
		};
	}
	
	/**
	 * Specification to get the list of orders having testId
	 * @param testId
	 * @param chartId
	 * @param status greater than
	 * @param status not equal
	 * @return Specification<LabEntries>
	 */
	public static Specification<LabEntries> getDetailsVaccinesNotPatDecli(final Integer[] testId,final Integer chartId,final Integer statusGreaterThan,final Integer statusNotEqual)
	{
		return new Specification<LabEntries>() {

			@Override
			public Predicate toPredicate(Root<LabEntries> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				query.orderBy(cb.desc(cb.coalesce(root.get(LabEntries_.labEntriesPerfOn),cb.function("to_timestamp",Timestamp.class,cb.literal("1900-05-13 16:40:35"),cb.literal("YYYY-MM-DD HH24:MI:SS")))));
				Predicate groupIdsPred =cb.and(root.get(LabEntries_.labEntriesTestId).in((Object[])testId),
						cb.greaterThan(root.get(LabEntries_.labEntriesTestStatus),statusGreaterThan),
						cb.notEqual(root.get(LabEntries_.labEntriesTestStatus),statusNotEqual),
						cb.equal(root.get(LabEntries_.labEntriesChartid),chartId),
						cb.equal(root.get(LabEntries_.labEntriesGroupid),36));
				return groupIdsPred;
			}
		};
	}

	/**
	 * Specification to get the list of orders having testId
	 * @param testId
	 * @param chartId
	 * @param status greater than
	 * @param status less than
	 * @return Specification<LabEntries>
	 */
	public static Specification<LabEntries> getDetailsExceptVaccinesNotPatDecli(final Integer[] testId,final Integer chartId,final Integer statusGreaterThan,final Integer statusLessThan)
	{
		return new Specification<LabEntries>() {

			@Override
			public Predicate toPredicate(Root<LabEntries> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				query.orderBy(cb.desc(cb.coalesce(root.get(LabEntries_.labEntriesPerfOn),cb.function("to_timestamp",Timestamp.class,cb.literal("1900-05-13 16:40:35"),cb.literal("YYYY-MM-DD HH24:MI:SS")))),
						cb.asc(root.get(LabEntries_.labEntriesTestStatus)));
				Predicate groupIdsPred =cb.and(root.get(LabEntries_.labEntriesTestId).in((Object[])testId),
						cb.greaterThan(root.get(LabEntries_.labEntriesTestStatus),statusGreaterThan),
						cb.lessThan(root.get(LabEntries_.labEntriesTestStatus),statusLessThan),
						cb.equal(root.get(LabEntries_.labEntriesChartid),chartId),
						cb.notEqual(root.get(LabEntries_.labEntriesGroupid),36));
				return groupIdsPred;
			}
		};
	}

	/**
	 * Specification to get the list of labs having testdetailId
	 * @param testdetailId
	 * @return Specification<LabEntries>
	 */
	public static Specification<LabEntries> testdetailIds(final Integer testdetailId)
	{
		return new Specification<LabEntries>() {

			@Override
			public Predicate toPredicate(Root<LabEntries> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate testdetailIds = cb.equal(root.get(LabEntries_.labEntriesTestdetailId),testdetailId);
				return testdetailIds;
			}
		};
	}
	
	/**
	 * Specification to get the list of labs having testId
	 * @param testId
	 * @return Specification<LabDescription>
	 */
	public static Specification<LabDescription> testIdsLabDescription(final Integer[] testId)
	{
		return new Specification<LabDescription>() {

			@Override
			public Predicate toPredicate(Root<LabDescription> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate testIds = root.get(LabDescription_.labDescriptionTestid).in((Object[])testId);
				return testIds;
			}
		};
	}

	/**
	 * Specification to get the list of labs not having group id
	 * @param groupId
	 * @return Specification<LabDescription>
	 */
	public static Specification<LabDescription> groupIdNot(final Integer groupId)
	{
		return new Specification<LabDescription>() {

			@Override
			public Predicate toPredicate(Root<LabDescription> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate groupIdPred = cb.notEqual(cb.coalesce(root.get(LabDescription_.labDescriptionGroupid),-1),groupId);
				return groupIdPred;
			}
		};
	}

	/**
	 * Specification to get the list of labs having group id
	 * @param groupId
	 * @return Specification<LabDescription>
	 */
	public static Specification<LabDescription> groupId(final Integer groupId)
	{
		return new Specification<LabDescription>() {

			@Override
			public Predicate toPredicate(Root<LabDescription> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate groupIdPred = cb.equal(root.get(LabDescription_.labDescriptionGroupid),groupId);
				return groupIdPred;
			}
		};
	}

	/**
	 * Specification to get the list of labs having LOINC code
	 * @param LOINC
	 * @return Specification<LabDescription>
	 */
	public static Specification<LabDescription> loincCodeLabs(final List<String> loinc)
	{
		return new Specification<LabDescription>() {

			@Override
			public Predicate toPredicate(Root<LabDescription> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate loincCode = root.get(LabDescription_.labDescriptionLoinc).in(loinc);
				return loincCode;
			}
		};
	}

	/**
	 * Specification to get the list of labs after mapping with HL7 table
	 * @param codes
	 * @param codessystem
	 * @return Specification<LabDescription>
	 */
	public static Specification<LabDescription> hl7codes(final List<String> codes,final Integer codeSystemNumber)
	{
		return new Specification<LabDescription>() {

			@Override
			public Predicate toPredicate(Root<LabDescription> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Join<Join<LabDescription, Hl7ExternalTestmapping>,Hl7ExternalTest> join=root.join("hl7ExternalTestmappingTable",JoinType.INNER).join("hl7ExternalTestTable", JoinType.INNER);
				Predicate hl7codes = cb.and(cb.equal(join.get(Hl7ExternalTest_.hl7ExternalTestLabcompanyid),codeSystemNumber),join.get(Hl7ExternalTest_.hl7ExternalTestCode).in(codes));
				return hl7codes;
			}
		};
	}

	/**
	 * Specification to get the list of parameter entries having paramId and chart id
	 * @param paramId
	 * @param chartId
	 * @return Specification<LabEntriesParameter>
	 */
	public static Specification<LabEntriesParameter> getParamDetails(final Integer[] paramId,final Integer chartId,final Integer encounterId,final Encounter encounterEntity)
	{
		return new Specification<LabEntriesParameter>() {

			@Override
			public Predicate toPredicate(Root<LabEntriesParameter> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Join<LabEntriesParameter,LabParameters> join= root.join("labParametersTable", JoinType.LEFT);
				query.orderBy(cb.desc(cb.coalesce(root.get(LabEntriesParameter_.labEntriesParameterDate),cb.function("to_timestamp",Timestamp.class,cb.literal("1900-05-13 16:40:35"),cb.literal("YYYY-MM-DD HH24:MI:SS")))),
						cb.desc(root.get(LabEntriesParameter_.labEntriesParameterId)));
				Predicate paramMapId=root.get(LabEntriesParameter_.labEntriesParameterMapid).in((Object[])paramId);
				Predicate isActivePred=cb.equal(join.get(LabParameters_.labParametersIsactive),true);
				Predicate entriesIsActivePred=cb.equal(cb.coalesce(root.get(LabEntriesParameter_.labEntriesParameterIsactive), true),true);
				Predicate chartIdPred=cb.equal(root.get(LabEntriesParameter_.labEntriesParameterChartid), chartId);
				Predicate paramDetails = null;
				if((encounterId!=-1)&&(encounterEntity.getEncounterStatus()==3)){
					Predicate encounterDatePred=cb.lessThanOrEqualTo(root.get(LabEntriesParameter_.labEntriesParameterDate), encounterEntity.getEncounterDate());
					paramDetails = cb.and(paramMapId,isActivePred,entriesIsActivePred,chartIdPred,encounterDatePred);
				}else{
					paramDetails = cb.and(paramMapId,isActivePred,entriesIsActivePred,chartIdPred);
				}
				return paramDetails;
			}
		};
	}
	
	/**
	 * Specification to get the list of parameter entries having paramId and chart id
	 * @param paramId
	 * @param chartId
	 * @return Specification<LabEntriesParameter>
	 */
	public static Specification<LabEntriesParameter> getParamDetailsStatus(final Integer[] paramId,final Integer chartId)
	{
		return new Specification<LabEntriesParameter>() {

			@Override
			public Predicate toPredicate(Root<LabEntriesParameter> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Join<LabEntriesParameter,LabParameters> join= root.join("labParametersTable", JoinType.LEFT);
				Join<LabEntriesParameter,LabEntries> join1= root.join("labEntriesTable", JoinType.LEFT);
				query.orderBy(cb.desc(cb.coalesce(root.get(LabEntriesParameter_.labEntriesParameterDate),cb.function("to_timestamp",Timestamp.class,cb.literal("1900-05-13 16:40:35"),cb.literal("YYYY-MM-DD HH24:MI:SS")))),
						cb.asc(join1.get(LabEntries_.labEntriesTestStatus)));
				Predicate paramDetails = cb.and(root.get(LabEntriesParameter_.labEntriesParameterMapid).in((Object[])paramId),
						cb.equal(join.get(LabParameters_.labParametersIsactive),true),
						cb.equal(cb.coalesce(root.get(LabEntriesParameter_.labEntriesParameterIsactive), true),true),
						cb.equal(root.get(LabEntriesParameter_.labEntriesParameterChartid), chartId));
				root.fetch(LabEntriesParameter_.labEntriesTable);
				return paramDetails;
			}
		};
	}

	/**
	 * Specification to get the list of parameter entries having testdetailid
	 * @param testdetailid
	 * @return Specification<LabEntriesParameter>
	 */
	public static Specification<LabEntriesParameter> getParamEntriesTestDetailId(final Integer testdetailid)
	{
		return new Specification<LabEntriesParameter>() {

			@Override
			public Predicate toPredicate(Root<LabEntriesParameter> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate getParams = cb.equal(root.get(LabEntriesParameter_.labEntriesParameterTestdetailid),testdetailid);
				return getParams;
			}
		};
	}

	/**
	 * Specification to get the list of parameter entries not having testdetailid
	 * @param testdetailid
	 * @return Specification<LabEntriesParameter>
	 */
	public static Specification<LabEntriesParameter> getParamEntriesTestDetailIdNot(final Integer testdetailid)
	{
		return new Specification<LabEntriesParameter>() {

			@Override
			public Predicate toPredicate(Root<LabEntriesParameter> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate getParams = cb.notEqual(root.get(LabEntriesParameter_.labEntriesParameterTestdetailid),testdetailid);
				return getParams;
			}
		};
	}


	/**
	 * Specification to get the list of parameter entries having map id
	 * @param testdetailid
	 * @return Specification<LabEntriesParameter>
	 */
	public static Specification<LabEntriesParameter> getParamEntriesMapId(final Integer mapId)
	{
		return new Specification<LabEntriesParameter>() {

			@Override
			public Predicate toPredicate(Root<LabEntriesParameter> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate getParams = cb.equal(root.get(LabEntriesParameter_.labEntriesParameterMapid),mapId);
				return getParams;
			}
		};
	}

	/**
	 * Specification to get the list of parameter entries having parameter id
	 * @param parameter id
	 * @return Specification<LabEntriesParameter>
	 */
	public static Specification<LabEntriesParameter> getParamEntriesId(final Integer parameterId)
	{
		return new Specification<LabEntriesParameter>() {

			@Override
			public Predicate toPredicate(Root<LabEntriesParameter> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate getParamEntriesId = cb.equal(root.get(LabEntriesParameter_.labEntriesParameterId),parameterId);
				return getParamEntriesId;
			}
		};
	}

	/**
	 * Specification to get the list of parameter entries which are active
	 * @param isActive
	 * @return Specification<LabEntriesParameter>
	 */
	public static Specification<LabEntriesParameter> getParamEntriesIsActive(final Boolean isActive)
	{
		return new Specification<LabEntriesParameter>() {

			@Override
			public Predicate toPredicate(Root<LabEntriesParameter> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate getParamEntriesId = cb.equal(cb.coalesce(root.get(LabEntriesParameter_.labEntriesParameterIsactive),true),isActive);
				return getParamEntriesId;
			}
		};
	}

	/**
	 * Specification to get the list of Lab parameters which are active
	 * @param isActive
	 * @return Specification<LabParameters>
	 */
	public static Specification<LabParameters> labParametersIsActive(final Boolean isActive)
	{
		return new Specification<LabParameters>() {

			@Override
			public Predicate toPredicate(Root<LabParameters> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate labParametersIsactive = cb.equal(root.get(LabParameters_.labParametersIsactive),isActive);
				return labParametersIsactive;
			}
		};
	}

	/**
	 * Specification to get the list of distinct Lab parameters which are not empty
	 * @return Specification<LabParameters>
	 */
	public static Specification<LabParameters> labParametersNotEmptyDistinct()
	{
		return new Specification<LabParameters>() {

			@Override
			public Predicate toPredicate(Root<LabParameters> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				query.distinct(true);
				query.orderBy(cb.asc(root.get(LabParameters_.labParametersDisplayname)));
				Predicate labParametersNotEmpty = cb.notEqual(cb.coalesce(cb.trim(root.get(LabParameters_.labParametersDisplayname)), cb.literal("")), cb.literal(""));
				return labParametersNotEmpty;
			}
		};
	}

	/**
	 * Specification to get the list of params having paramId
	 * @param testId
	 * @return Specification<LabParameters>
	 */
	public static Specification<LabParameters> paramIdsLabParameter(final Integer[] paramId)
	{
		return new Specification<LabParameters>() {

			@Override
			public Predicate toPredicate(Root<LabParameters> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate testIds = root.get(LabParameters_.labParametersId).in((Object[])paramId);
				return testIds;
			}
		};
	}


	/**
	 * Specification to get the list of labs having LOINC code
	 * @param LOINC
	 * @return Specification<LabParameters>
	 */
	public static Specification<LabParameters> loincCodeParams(final List<String> loinc)
	{
		return new Specification<LabParameters>() {

			@Override
			public Predicate toPredicate(Root<LabParameters> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Join<LabParameters,LabParameterCode> join=root.join("labParameterCodeTable",JoinType.INNER);
				query.distinct(true);
				Predicate loincCode = cb.and(join.get(LabParameterCode_.labParameterCodeValue).in(loinc),cb.equal(join.get(LabParameterCode_.labParameterCodeSystem),"LOINC"));
				return loincCode;
			}
		};
	}

	/**
	 * Specification to get the list of parameter entries having paramId
	 * @param paramId
	 * @param chartId
	 * @return Specification<LabParameters>
	 */
	public static Specification<LabParameters> getParamDetails(final Integer paramId)
	{
		return new Specification<LabParameters>() {

			@Override
			public Predicate toPredicate(Root<LabParameters> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate getParamDetails = cb.equal(root.get(LabParameters_.labParametersId),paramId);
				return getParamDetails;
			}
		};
	}

	/**
	 * Specification to get the list of parameter entries having displayname
	 * @param paramId
	 * @param chartId
	 * @return Specification<LabParameters>
	 */
	public static Specification<LabParameters> getParamDisplayname(final String displayname,final Boolean exactMatch)
	{
		return new Specification<LabParameters>() {

			@Override
			public Predicate toPredicate(Root<LabParameters> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate getParamDetails=null;
				if(!exactMatch)
					getParamDetails=cb.like(cb.upper(root.get(LabParameters_.labParametersDisplayname)), "%"+displayname.toUpperCase()+"%");
				else
					getParamDetails= cb.like(cb.upper(root.get(LabParameters_.labParametersDisplayname)),displayname.toUpperCase());
				return getParamDetails;
			}
		};
	}

	/**
	 * Specification to get the list of parameter entries having units
	 * @param paramId
	 * @param chartId
	 * @return Specification<LabParameters>
	 */
	public static Specification<LabParameters> getParamUnits(final String units,final Boolean exactMatch)
	{
		return new Specification<LabParameters>() {

			@Override
			public Predicate toPredicate(Root<LabParameters> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate getParamUnits=null;
				if(!exactMatch)
					getParamUnits=cb.like(cb.upper(root.get(LabParameters_.labParametersUnits)), "%"+units.toUpperCase()+"%");
				else
					getParamUnits= cb.like(cb.upper(root.get(LabParameters_.labParametersUnits)),units.toUpperCase());
				return getParamUnits;
			}
		};
	}


	/**
	 * Specification to get the list of parameter code entries having code
	 * @param code
	 * @return Specification<LabParameterCode>
	 */
	public static Specification<LabParameterCode> getCode(final String code)
	{
		return new Specification<LabParameterCode>() {

			@Override
			public Predicate toPredicate(Root<LabParameterCode> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate getParamDetails = cb.equal(root.get(LabParameterCode_.labParameterCodeValue),code);
				return getParamDetails;
			}
		};
	}

	/**
	 * Specification to get the list of parameter code entries having code
	 * @param codeSystem
	 * @return Specification<LabParameterCode>
	 */
	public static Specification<LabParameterCode> getCodeSystem(final String codeSystem)
	{
		return new Specification<LabParameterCode>() {

			@Override
			public Predicate toPredicate(Root<LabParameterCode> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate getParamDetails = cb.equal(root.get(LabParameterCode_.labParameterCodeSystem),codeSystem);
				return getParamDetails;
			}
		};
	}


	/**
	 * Specification to get the list of distinct lab groups which are active
	 * @param isActive
	 * @return Specification<LabGroups>
	 */
	public static Specification<LabGroups> labGroupIsActiveDistinct(final Boolean isActive)
	{
		return new Specification<LabGroups>() {

			@Override
			public Predicate toPredicate(Root<LabGroups> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				query.distinct(true);
				query.orderBy(cb.asc(root.get(LabGroups_.labGroupsDesc)));
				Predicate labGroupsIsActiveDistinct = cb.equal(root.get(LabGroups_.labGroupsIsactive),isActive);
				return labGroupsIsActiveDistinct;
			}
		};
	}

	/**
	 * Specification to get the list of distinct hl7 results having id
	 * @param isActive
	 * @return Specification<Hl7ResultInbox>
	 */
	public static Specification<Hl7ResultInbox> hl7ResultsId(final Integer id)
	{
		return new Specification<Hl7ResultInbox>() {

			@Override
			public Predicate toPredicate(Root<Hl7ResultInbox> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate hl7ResultsId = cb.equal(root.get(Hl7ResultInbox_.hl7ResultInboxId),id);
				return hl7ResultsId;
			}
		};
	}

	/**
	 * Specification to get the list of distinct labs having lab id
	 * @param labId
	 * @return Specification<LabIncludePrevious>
	 */
	public static Specification<LabIncludePrevious> labIncludeLabId(final Integer labId)
	{
		return new Specification<LabIncludePrevious>() {

			@Override
			public Predicate toPredicate(Root<LabIncludePrevious> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate labIncludeLabId = cb.equal(root.get(LabIncludePrevious_.labIncludePreviousLabid),labId);
				return labIncludeLabId;
			}
		};
	}

	/**
	 * Specification to get the list of distinct labs having encounter Id
	 * @param encounterId
	 * @return Specification<LabIncludePrevious>
	 */
	public static Specification<LabIncludePrevious> labIncludeEncounterId(final Integer encounterId)
	{
		return new Specification<LabIncludePrevious>() {

			@Override
			public Predicate toPredicate(Root<LabIncludePrevious> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate labIncludeLabId = cb.equal(root.get(LabIncludePrevious_.labIncludePreviousEncounterid),encounterId);
				return labIncludeLabId;
			}
		};
	}

	/**
	 * Specification to get the list of distinct labs having encounter Id
	 * @param encounterId
	 * @return Specification<LabAlertforwardstatus>
	 */
	public static Specification<LabAlertforwardstatus> labAlertForwardLabStatus(final Integer labStatus)
	{
		return new Specification<LabAlertforwardstatus>() {

			@Override
			public Predicate toPredicate(Root<LabAlertforwardstatus> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate labIncludeLabId = cb.equal(root.get(LabAlertforwardstatus_.labAlertforwardstatusLabstatus),labStatus);
				return labIncludeLabId;
			}
		};
	}

	/**
	 * Specification to get the list of specimens having source
	 * @param source
	 * @return Specification<Specimen>
	 */
	public static Specification<Specimen> specimenSource(final String source)
	{
		return new Specification<Specimen>() {

			@Override
			public Predicate toPredicate(Root<Specimen> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate specimenSource = cb.equal(root.get(Specimen_.specimenSource),source);
				return specimenSource;
			}
		};
	}

	/**
	 * Specification to get the list of specimens having col volume
	 * @param source
	 * @return Specification<Specimen>
	 */
	public static Specification<Specimen> specimenColVol(final String colVol)
	{
		return new Specification<Specimen>() {

			@Override
			public Predicate toPredicate(Root<Specimen> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate specimenSource = cb.equal(root.get(Specimen_.specimenCollVol),colVol);
				return specimenSource;
			}
		};
	}

	/**
	 * Specification to get the list of specimens having col volume unit
	 * @param source
	 * @return Specification<Specimen>
	 */
	public static Specification<Specimen> specimenColVolUnit(final String unit)
	{
		return new Specification<Specimen>() {

			@Override
			public Predicate toPredicate(Root<Specimen> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate specimenSource = cb.equal(root.get(Specimen_.specimenCollVolUnit),unit);
				return specimenSource;
			}
		};
	}

	/**
	 * Specification to get the list of specimens having date
	 * @param source
	 * @return Specification<Specimen>
	 */
	public static Specification<Specimen> specimenDate(final Timestamp date) {
		return new Specification<Specimen>() {

			@Override
			public Predicate toPredicate(Root<Specimen> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate specimenSource = cb.equal(root.get(Specimen_.specimenDate),date);
				return specimenSource;
			}
		};
	}

	/**
	 * Specification to get the list of testIds having param
	 * @param params
	 * @return Specification<LabDescpParameters>
	 */
	public static Specification<LabDescpParameters> getParamIdBased(final Integer[] params)
	{
		return new Specification<LabDescpParameters>() {

			@Override
			public Predicate toPredicate(Root<LabDescpParameters> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Join<LabDescpParameters,LabParameters> join=root.join("labParametersTable",JoinType.INNER);
				Predicate getParamIdBased = join.get(LabParameters_.labParametersId).in((Object[])params);
				return getParamIdBased;
			}
		};
	}

	public static Specification<LabEntries> findTest(final String encounterId, final String testDetailId) {
		return new Specification<LabEntries>() {

			@Override
			public Predicate toPredicate(Root<LabEntries> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate checkEncounterId = cb.equal(root.get(LabEntries_.labEntriesEncounterId), Integer.parseInt(encounterId));
				Predicate checkTestDetailId = cb.equal(root.get(LabEntries_.labEntriesTestdetailId), Integer.parseInt(testDetailId));
				return cb.and(checkEncounterId, checkTestDetailId);
			}
		};
	}

	public static Specification<PatientRegistration> checkPatientId(final Integer patientId) {
		return new Specification<PatientRegistration>() {

			@Override
			public Predicate toPredicate(Root<PatientRegistration> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate checkEncounterId = cb.equal(root.get(PatientRegistration_.patientRegistrationId), patientId);
				return checkEncounterId;
			}
		};
	}
}
