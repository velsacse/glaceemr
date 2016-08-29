package com.glenwood.glaceemr.server.application.specifications;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import com.glenwood.glaceemr.server.application.models.Admission;
import com.glenwood.glaceemr.server.application.models.AdmissionRoom;
import com.glenwood.glaceemr.server.application.models.AdmissionRoom_;
import com.glenwood.glaceemr.server.application.models.Admission_;
import com.glenwood.glaceemr.server.application.models.ConsultFaxTracking;
import com.glenwood.glaceemr.server.application.models.ConsultFaxTracking_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.models.Encounter_;
import com.glenwood.glaceemr.server.application.models.H448;
import com.glenwood.glaceemr.server.application.models.H496;
import com.glenwood.glaceemr.server.application.models.H496_;
import com.glenwood.glaceemr.server.application.models.H616;
import com.glenwood.glaceemr.server.application.models.H616_;
import com.glenwood.glaceemr.server.application.models.H629;
import com.glenwood.glaceemr.server.application.models.H629_;
import com.glenwood.glaceemr.server.application.models.LeafGroup;
import com.glenwood.glaceemr.server.application.models.LeafGroup_;
import com.glenwood.glaceemr.server.application.models.LeafLibrary;
import com.glenwood.glaceemr.server.application.models.LeafLibrary_;
import com.glenwood.glaceemr.server.application.models.LeafPatient;
import com.glenwood.glaceemr.server.application.models.LeafPatient_;
import com.glenwood.glaceemr.server.application.models.PatientAllergies;
import com.glenwood.glaceemr.server.application.models.PatientAllergies_;
import com.glenwood.glaceemr.server.application.models.PatientEpisode;
import com.glenwood.glaceemr.server.application.models.PatientEpisode_;
import com.glenwood.glaceemr.server.application.models.ProviderLeafMapping;
import com.glenwood.glaceemr.server.application.models.ProviderLeafMapping_;

public class AdmissionSpecification {

	
	/**
	 * Get the admission details of a patient
	 * 
	 * @param patientId
	 * 
	 * 
	 */
	public static Specification<Admission> getAdmissionByPatientId(final Integer patientId){

		return new Specification<Admission>(){

			@Override
			public Predicate toPredicate(Root<Admission> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				root.fetch(Admission_.empProfile,JoinType.INNER);
				root.fetch(Admission_.posTable,JoinType.INNER);
				root.fetch(Admission_.admissionBlockTable,JoinType.LEFT);
				root.fetch(Admission_.admissionRoomTable,JoinType.LEFT);
				Predicate patPred=cb.equal(root.get(Admission_.admissionPatientId),patientId);
				Predicate admissionActive=cb.equal(root.get(Admission_.admissionStatus),1);
				return cb.and(patPred,admissionActive);
				
				
			}

		};
	}
	
	
	/**
	 * Get all past admission details of a patient
	 * 
	 * @param patientId
	 * 
	 * 
	 */
	
	
	public static Specification<Admission> getPastAdmissionByPatientId(final Integer patientId){

		return new Specification<Admission>(){

			@Override
			public Predicate toPredicate(Root<Admission> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				root.fetch(Admission_.empProfile,JoinType.INNER);
				root.fetch(Admission_.posTable,JoinType.INNER);
				root.fetch(Admission_.admissionBlockTable,JoinType.LEFT);
				root.fetch(Admission_.admissionRoomTable,JoinType.LEFT);
				Predicate patPred=cb.equal(root.get(Admission_.admissionPatientId),patientId);
				Predicate admissionActive=cb.equal(root.get(Admission_.admissionStatus),2);
				query.where(patPred,admissionActive).orderBy(cb.desc(root.get(Admission_.admissionId)));
				return query.getRestriction();
				
				
			}

		};
	}
	
	
	
	/**
	 * Get Leaf Details
	 * 
	 * @param encounterId
	 * @param userId
	 * 
	 * 
	 */
	
	
	public static Specification<LeafGroup> getLeafList(final Integer encounterId,final Integer userId){

		return new Specification<LeafGroup>(){

			@Override
			public Predicate toPredicate(Root<LeafGroup> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				
				Join<LeafGroup, LeafLibrary> leafJoin = root.join(LeafGroup_.leafLibraries,JoinType.INNER);
				Join<LeafLibrary, ProviderLeafMapping> provJoin=leafJoin.join(LeafLibrary_.providerLeafMappings,JoinType.INNER);
				provJoin.on(cb.equal(provJoin.get(ProviderLeafMapping_.userid),userId));
				Join<LeafLibrary,LeafPatient> leafPatJoin= leafJoin.join(LeafLibrary_.leafPatients,JoinType.LEFT);
				leafPatJoin.on(cb.and(cb.equal(leafPatJoin.get(LeafPatient_.leafPatientEncounterId),encounterId),cb.equal(leafPatJoin.get(LeafPatient_.leafPatientIsactive), true)));
				Join<LeafLibrary,H616> h616join = leafJoin.join(LeafLibrary_.h616,JoinType.LEFT);
				Join<LeafLibrary,H448> h448join = leafJoin.join(LeafLibrary_.h448,JoinType.LEFT);
				query.orderBy(cb.asc(root.get(LeafGroup_.leafGroupName)),cb.asc(root.get(LeafGroup_.leafGroupOrder)),cb.asc(leafJoin.get(LeafLibrary_.leafLibraryName)),cb.asc(h616join.get(H616_.h616002)),cb.asc(leafJoin.get(LeafLibrary_.leafLibraryOrder)));
				return cb.and(cb.isNull(leafPatJoin.get(LeafPatient_.leafPatientId)),
						   cb.notEqual(leafJoin.get(LeafLibrary_.leafLibraryType),4),
						   cb.equal(root.get(LeafGroup_.leafGroupIsactive), true),
						   cb.equal(leafJoin.get(LeafLibrary_.leafLibraryIsactive), true));
				
				
			}

		};
	}
	
	
	
	public static Specification<LeafLibrary> B(final Integer encounterId,final Integer userId){

		return new Specification<LeafLibrary>(){

			@Override
			public Predicate toPredicate(Root<LeafLibrary> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				
				//Join<LeafGroup, LeafLibrary> leafJoin = root.join(LeafGroup_.leafLibraries,JoinType.INNER);
				Join<LeafLibrary, ProviderLeafMapping> provJoin=root.join(LeafLibrary_.providerLeafMappings,JoinType.INNER);
				provJoin.on(cb.equal(provJoin.get(ProviderLeafMapping_.userid),userId));
				Join<LeafLibrary,LeafPatient> leafPatJoin= root.join(LeafLibrary_.leafPatients,JoinType.LEFT);
				leafPatJoin.on(cb.and(cb.equal(leafPatJoin.get(LeafPatient_.leafPatientEncounterId),encounterId),cb.equal(leafPatJoin.get(LeafPatient_.leafPatientIsactive), true)));
				Join<LeafLibrary,H616> h616join = root.join(LeafLibrary_.h616,JoinType.LEFT);
				root.fetch(LeafLibrary_.h448,JoinType.LEFT);
				//query.orderBy(cb.asc(root.get(LeafGroup_.leafGroupName)),cb.asc(root.get(LeafGroup_.leafGroupOrder)),cb.asc(leafJoin.get(LeafLibrary_.leafLibraryName)),cb.asc(h616join.get(H616_.h616002)),cb.asc(leafJoin.get(LeafLibrary_.leafLibraryOrder)));
				query.orderBy(cb.asc(root.get(LeafLibrary_.leafLibraryName)),cb.asc(h616join.get(H616_.h616002)),cb.asc(root.get(LeafLibrary_.leafLibraryOrder)));
				return cb.and(cb.isNull(leafPatJoin.get(LeafPatient_.leafPatientId)),
						   cb.notEqual(root.get(LeafLibrary_.leafLibraryType),4),
						   //cb.equal(root.get(LeafGroup_.leafGroupIsactive), true),
						   cb.equal(root.get(LeafLibrary_.leafLibraryIsactive), true));
				
				
			}

		};
	}
	
	
	public static Specification<LeafLibrary> A(){

		return new Specification<LeafLibrary>(){

			@Override
			public Predicate toPredicate(Root<LeafLibrary> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				Join<LeafLibrary, LeafGroup> grpFetch=(Join<LeafLibrary, LeafGroup>) root.fetch(LeafLibrary_.leafGroup,JoinType.INNER);
				query.orderBy(cb.asc(grpFetch.get(LeafGroup_.leafGroupName)),cb.asc(grpFetch.get(LeafGroup_.leafGroupOrder)));
				return cb.equal(grpFetch.get(LeafGroup_.leafGroupIsactive), true);
				
				
			}

		};
	}
	
	
	
	
	public static Specification<LeafPatient> getPatientLeafsByEncId(final List<Integer> encounterId){

		return new Specification<LeafPatient>(){

			@Override
			public Predicate toPredicate(Root<LeafPatient> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				root.join(LeafPatient_.encounterTable,JoinType.INNER);
				return null;
				
			}

		};
	}
	
	
	
	
	public static Specification<LeafPatient> getAdmissionPatientLeafsByEncId(final List<Integer> encounterId){

		return new Specification<LeafPatient>(){

			@Override
			public Predicate toPredicate(Root<LeafPatient> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				Join<LeafPatient, LeafLibrary> libJoin =(Join<LeafPatient, LeafLibrary>) root.fetch(LeafPatient_.leafLibraryTable,JoinType.INNER);
				Fetch<LeafPatient, H629> leafPatJoin = root.fetch(LeafPatient_.h629,JoinType.LEFT);
				leafPatJoin.fetch(H629_.empProfile,JoinType.LEFT);
				root.fetch(LeafPatient_.empProfile_createdBy,JoinType.LEFT);
				root.fetch(LeafPatient_.empProfile_lastModifiedBy,JoinType.LEFT);
				root.fetch(LeafPatient_.empProfile_ma_signed_userid,JoinType.LEFT);
				root.fetch(LeafPatient_.empProfile_sign_userid,JoinType.LEFT);
				query.orderBy(cb.asc(root.get(LeafPatient_.leafPatientCreatedDate)));
				return cb.and(root.get(LeafPatient_.leafPatientEncounterId).in(encounterId),
						      cb.notEqual(libJoin.get(LeafLibrary_.leafLibraryType),4),
						      cb.equal(root.get(LeafPatient_.leafPatientIsactive), true));
				
			}

		};
	}
	
	
	
	
	public static Specification<H496> getLeafFaxDetails(final List<Integer> encounterId){

		return new Specification<H496>(){

			@Override
			public Predicate toPredicate(Root<H496> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				
				Join<H496, ConsultFaxTracking>  faxTrackJoin =root.join(H496_.consultFaxTrackings,JoinType.INNER);
				Join<H496, EmployeeProfile> empJoin = root.join(H496_.empProfile,JoinType.INNER);
				Join<ConsultFaxTracking, LeafLibrary> leafLibJoin = faxTrackJoin.join(ConsultFaxTracking_.leafLibrary,JoinType.INNER);
				Join<LeafLibrary,LeafPatient> leafPatJoin = leafLibJoin.join(LeafLibrary_.leafPatients,JoinType.INNER);
				leafLibJoin.on(faxTrackJoin.get(ConsultFaxTracking_.encounterid).in(encounterId));
				leafPatJoin.on(leafPatJoin.get(LeafPatient_.leafPatientEncounterId).in(encounterId));
				return cb.notEqual(leafLibJoin.get(LeafLibrary_.leafLibraryType),4);
			}

		};
	}
	
	public static Specification<PatientEpisode> getPatientEpisodebyEpisodeId(final Integer patEpisodeId){

		return new Specification<PatientEpisode>(){

			@Override
			public Predicate toPredicate(Root<PatientEpisode> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(PatientEpisode_.patientEpisodeId), patEpisodeId);
			}

		};
	}


	
	public static Specification<Encounter> getAdmissionEncByEpisodeId(final Integer admssEpisodeId){

		return new Specification<Encounter>(){

			@Override
			public Predicate toPredicate(Root<Encounter> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.orderBy(cb.asc(root.get(Encounter_.encounterDate)));
				return cb.equal(root.get(Encounter_.encounterPatientEpisodeid),admssEpisodeId);
			}

		};
	}

	/**
     * @param chartId : used to get patient allergies of a patient of that particular chart id
     * @return BooleanExpression is a predicate
     */
    public static Specification<PatientAllergies> getPatientAllergiesList(final List<Integer> encounterList)
    {
        return new Specification<PatientAllergies>() {

            @Override
            public Predicate toPredicate(Root<PatientAllergies> root,
                    CriteriaQuery<?> cq, CriteriaBuilder cb) {
            	root.fetch(PatientAllergies_.allergiesType,JoinType.INNER);
                Predicate ptAllgStatusPredicate=cb.equal(root.get(PatientAllergies_.patAllergStatus), 1);
                Predicate ptAllgTypeIdPredicate=cb.notLike(root.get(PatientAllergies_.patAllergTypeId).as(String.class), "-");
                Predicate ptAllgEncId=root.get(PatientAllergies_.patAllergEncounterId).in(encounterList);
                cq.where(ptAllgStatusPredicate,ptAllgTypeIdPredicate,ptAllgEncId);
                return cq.getRestriction();
            }

        };
    }

    /**
     * Get rooms in specific block
     * @param encounterList
     * @return
     */
    public static Specification<AdmissionRoom> getRooms(final Integer blockId)
    {
        return new Specification<AdmissionRoom>() {

            @Override
            public Predicate toPredicate(Root<AdmissionRoom> root,
                    CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate blockPred=cb.equal(root.get(AdmissionRoom_.admissionRoomBlockId), blockId);
                cq.where(blockPred).orderBy(cb.asc(root.get(AdmissionRoom_.admissionRoomOrder)));
                return cq.getRestriction();
            }

        };
    }
    
    
	/*public static Specification<LeafLibrary> getFequentList(final Integer userId){

		return new Specification<LeafLibrary>(){

			@Override
			public Predicate toPredicate(Root<LeafLibrary> root,CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<LeafLibrary,LeafPatient> leafLibJoin=root.join(LeafLibrary_.leafPatients,JoinType.INNER);
				Join<LeafLibrary,ProviderLeafMapping> provLeafMapJoin=root.join(LeafLibrary_.providerLeafMappings,JoinType.INNER);
				provLeafMapJoin.on(cb.equal(provLeafMapJoin.get(ProviderLeafMapping_.userid),userId),cb.equal(provLeafMapJoin.get(ProviderLeafMapping_.frequentlused), true));
				query.groupBy(root.get(LeafLibrary_.leafLibraryId));
				query.orderBy(cb.desc(cb.count(root.get(LeafLibrary_.leafLibraryId))));
				//query.distinct(true);
				return query.getRestriction();
			}

		};
	}
*/

	
	
}
