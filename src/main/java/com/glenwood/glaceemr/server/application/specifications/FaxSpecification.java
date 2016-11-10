package com.glenwood.glaceemr.server.application.specifications;

import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import com.glenwood.glaceemr.server.application.models.DoctorSign;
import com.glenwood.glaceemr.server.application.models.DoctorSign_;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile_;
import com.glenwood.glaceemr.server.application.models.FaxFolder;
import com.glenwood.glaceemr.server.application.models.FaxStatus;
import com.glenwood.glaceemr.server.application.models.FaxStatus_;
import com.glenwood.glaceemr.server.application.models.FaxType;
import com.glenwood.glaceemr.server.application.models.H491;
import com.glenwood.glaceemr.server.application.models.H491_;
import com.glenwood.glaceemr.server.application.models.H496;
import com.glenwood.glaceemr.server.application.models.H496_;
import com.glenwood.glaceemr.server.application.models.InFaxStatus;
import com.glenwood.glaceemr.server.application.models.InFaxStatus_;
/**
 * Specification for Fax module
 * @author Anil Kumar
 *
 */
public class FaxSpecification {
	@Autowired
	EntityManager entityManager;

	public static Specification<H496> getOutboxDetails(final Integer h496004, final Integer userId) {
		return new Specification<H496>() {
			@SuppressWarnings("unused")
			@Override
			public Predicate toPredicate(Root<H496> root,
				   CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Join<H496, FaxFolder> h496join= root.join(H496_.faxFolder,JoinType.INNER);
				Join<H496, FaxStatus> statusjoin= root.join(H496_.faxStatus,JoinType.INNER);
				Join<H496, EmployeeProfile> empProfile1join= root.join(H496_.chart_users_1,JoinType.LEFT);
				Join<H496, EmployeeProfile> empProfile2join= root.join(H496_.chart_users_2,JoinType.LEFT);
				Join<H496, EmployeeProfile> empProfile3join= root.join(H496_.chart_users_3,JoinType.LEFT);
				Predicate condition = null;
				if(userId==-1){
					condition = cb.and(cb.equal(root.get(H496_.h496004),h496004),cb.equal(statusjoin.get(FaxStatus_.groupid),7),cb.equal(root.get(H496_.h496023Faxbox),1));
				}else{
					condition = cb.and(cb.equal(root.get(H496_.h496004),h496004),cb.equal(statusjoin.get(FaxStatus_.groupid),7),cb.equal(root.get(H496_.h496023Faxbox),1),cb.equal(root.get(H496_.h496012),userId),cb.equal(root.get(H496_.h496013),userId));
				}
				return cq.where(condition).getRestriction();
			}			
		};
	}
	
	@SuppressWarnings("unused")
	public static Predicate getFaxOutboxDetails(final Integer h496004, final Integer userId, Root<H496> root,
			   CriteriaQuery<?> cq, CriteriaBuilder cb){
		
		Join<H496, FaxFolder> h496join= root.join(H496_.faxFolder,JoinType.INNER);
		Join<H496, FaxStatus> statusjoin= root.join(H496_.faxStatus,JoinType.INNER);
		Join<H496, EmployeeProfile> empProfile1join= root.join(H496_.chart_users_1,JoinType.LEFT);
		Join<H496, EmployeeProfile> empProfile2join= root.join(H496_.chart_users_2,JoinType.LEFT);
		Join<H496, EmployeeProfile> empProfile3join= root.join(H496_.chart_users_3,JoinType.LEFT);
		Predicate condition = null;
		if(userId==-1){
			condition = cb.and(cb.equal(root.get(H496_.h496004),h496004),cb.equal(statusjoin.get(FaxStatus_.groupid),7),cb.equal(root.get(H496_.h496023Faxbox),1));
		}else{
			condition = cb.and(cb.equal(root.get(H496_.h496004),h496004),cb.equal(statusjoin.get(FaxStatus_.groupid),7),cb.equal(root.get(H496_.h496023Faxbox),1),cb.equal(root.get(H496_.h496012),userId),cb.equal(root.get(H496_.h496013),userId));
		}
		
		return cq.where(condition).getRestriction();
		
	}

	public static Specification<H491> getInboxDetails(final Integer h491010, final Integer h491013, final Integer h491014, final Integer h491017_faxbox) {
		return new Specification<H491>() {
			@SuppressWarnings("unused")
			@Override
			public Predicate toPredicate(Root<H491> root,
				   CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Join<H491, FaxFolder> faxFolderjoin = root.join(H491_.faxFolder,JoinType.INNER);
				Join<H491, InFaxStatus> statusjoin = root.join(H491_.faxStatus,JoinType.INNER);
				Join<H491, FaxType> faxTypejoin = root.join(H491_.faxType,JoinType.INNER);
				Join<H491, EmployeeProfile> empProfile1join = root.join(H491_.emplopyeeProfile,JoinType.LEFT);
				Join<H491, EmployeeProfile> empProfile2join = root.join(H491_.emplopyeeProfile1,JoinType.LEFT);
				Predicate condition = null;
				if(h491013 == -1){
					condition = cb.and(cb.equal(statusjoin.get(InFaxStatus_.groupid),6),cb.equal(root.get(H491_.h491010),h491010),cb.equal(root.get(H491_.h491014),h491014),cb.equal(root.get(H491_.h491017Faxbox),h491017_faxbox),cb.equal(root.get(H491_.h491018Isenabled),true));
				}else{
					condition = cb.and(cb.equal(statusjoin.get(InFaxStatus_.groupid),6),cb.equal(root.get(H491_.h491010),h491010),cb.equal(root.get(H491_.h491013),0),cb.equal(root.get(H491_.h491014),h491014),cb.equal(root.get(H491_.h491017Faxbox),h491017_faxbox),cb.equal(root.get(H491_.h491018Isenabled),true));
				}
				return cq.where(condition).getRestriction();
			}			
		};
	}
	@SuppressWarnings("unused")
	public static Predicate getFaxInboxDetails(final Integer h491010, final Integer h491013, final Integer h491014, final Integer h491017_faxbox,Root<H491> root,CriteriaQuery<?> cq, CriteriaBuilder cb) {
		
				Join<H491, FaxFolder> faxFolderjoin = root.join(H491_.faxFolder,JoinType.INNER);
				Join<H491, InFaxStatus> statusjoin = root.join(H491_.faxStatus,JoinType.INNER);
				Join<H491, FaxType> faxTypejoin = root.join(H491_.faxType,JoinType.INNER);
				Join<H491, EmployeeProfile> empProfile1join = root.join(H491_.emplopyeeProfile,JoinType.LEFT);
				Join<H491, EmployeeProfile> empProfile2join = root.join(H491_.emplopyeeProfile1,JoinType.LEFT);
				Predicate condition = null;
				if(h491013 == -1){
					condition = cb.and(cb.equal(statusjoin.get(InFaxStatus_.groupid),6),cb.equal(root.get(H491_.h491010),h491010),cb.equal(root.get(H491_.h491014),h491014),cb.equal(root.get(H491_.h491017Faxbox),h491017_faxbox),cb.equal(root.get(H491_.h491018Isenabled),true));
				}else{
					condition = cb.and(cb.equal(statusjoin.get(InFaxStatus_.groupid),6),cb.equal(root.get(H491_.h491010),h491010),cb.equal(root.get(H491_.h491013),0),cb.equal(root.get(H491_.h491014),h491014),cb.equal(root.get(H491_.h491017Faxbox),h491017_faxbox),cb.equal(root.get(H491_.h491018Isenabled),true));
				}
				return cq.where(condition).getRestriction();
			}			

	
	public static Specification<H491>faxForward(final String faxId[]){
		return new Specification<H491>() {
			@Override
			public Predicate toPredicate(Root<H491> root,
				   CriteriaQuery<?> cq, CriteriaBuilder cb) {				
				Predicate predicate=cq.where(root.get(H491_.h491001).in(Arrays.asList(faxId))).getRestriction();
				return predicate;
			}
		};
	}

	public static Specification<H491> getInFaxDetails(final Integer faxId, final Integer userId, final Integer faxTab, final Integer faxFolder){
			return new Specification<H491>() {
			@Override
			public Predicate toPredicate(Root<H491> root,
				   CriteriaQuery<?> cq, CriteriaBuilder cb) {
				   Predicate condition = null;
				   if(faxTab == 1){
					   condition = cb.and(cb.equal(root.get(H491_.h491001),faxId), cb.equal(root.get(H491_.h491010),faxFolder), cb.equal(root.get(H491_.h491014),0));
				   }else{
					   condition = cb.and(cb.equal(root.get(H491_.h491001),faxId), cb.equal(root.get(H491_.h491010),faxFolder), cb.equal(root.get(H491_.h491014),userId));
				   }
				return cq.where(condition).getRestriction();
			}
		};
	}
	 
	public static Specification<H491> lastFaxReceivedTime(final Integer faxId){
		return new Specification<H491>() {
		@Override
		public Predicate toPredicate(Root<H491> root,
			   CriteriaQuery<?> cq, CriteriaBuilder cb) {
			   Predicate condition = null;
			   condition = cb.and(cb.equal(root.get(H491_.h491001), faxId));
			return cq.where(condition).getRestriction();
		}
	};
}

	public static Specification<H496> getOutFaxDetails(final Integer userId,final Integer faxId,final Integer faxTab, final Integer faxFolder){
		return new Specification<H496>() {
			@Override
			public Predicate toPredicate(Root<H496> root,
				   CriteriaQuery<?> cq, CriteriaBuilder cb) {
			       Predicate condition = null;
			    		   if(faxTab == 1){
							   condition = cb.and(cb.equal(root.get(H496_.h496001),faxId), cb.equal(root.get(H496_.h496010),faxFolder), cb.equal(root.get(H496_.h496014),0));
						   }else{
							   condition = cb.and(cb.equal(root.get(H496_.h496001),faxId), cb.equal(root.get(H496_.h496010),faxFolder), cb.equal(root.get(H496_.h496014),userId));
						   }  
			return cq.where(condition).getRestriction();
			}
		};
}

	public static Specification<DoctorSign> getSignatureDetails( final Integer empId ,final boolean accessType) {
		return new Specification<DoctorSign>() {
			@SuppressWarnings("unused")
			@Override
			public Predicate toPredicate(Root<DoctorSign> root,
				   CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Integer[] groupId = new Integer[]{-1,-7,-10};
				Predicate condition = null;
				Join<DoctorSign, EmployeeProfile> join1 = root.join(DoctorSign_.empLoginId,JoinType.INNER);
				if(!accessType){
					condition = cb.and(cb.equal(root.get(DoctorSign_.loginid),empId), cb.notEqual(root.get(DoctorSign_.filename), "-1"), cb.equal(join1.get(EmployeeProfile_.empProfileIsActive), true));
				}else{
					condition = cb.and(cb.notEqual(root.get(DoctorSign_.filename), "-1"), cb.equal(join1.get(EmployeeProfile_.empProfileIsActive), true), join1.get(EmployeeProfile_.empProfileGroupid).in(-1,-10), cb.equal(root.get(DoctorSign_.signaccess), true));
				}
					cq.orderBy(cb.desc(join1.get(EmployeeProfile_.empProfileGroupid)), cb.asc(join1.get(EmployeeProfile_.empProfileFullname)));
				return cq.where(condition).getRestriction();
			}
		};
	}
	
	/**
	 * code to return employee login id
	 */
	
	public static Specification<EmployeeProfile> getEmployeeLoginId(final int empId){
		
		return new Specification<EmployeeProfile>() {
		
			@Override
			public Predicate toPredicate(Root<EmployeeProfile> root, CriteriaQuery<?> cq, CriteriaBuilder cb){
				
				Predicate condition = null;
				condition = cb.and(cb.equal(root.get(EmployeeProfile_.empProfileLoginid),empId));
				return cq.where(condition).getRestriction();
				
			}
			
		};
		
	}
	
	/**
	 * code to get sign access type 
	 */
	
	public static Specification<DoctorSign> getSignAccessType(final int loginId){
		
		return new Specification<DoctorSign>() {
		
			@Override
			public Predicate toPredicate(Root<DoctorSign> root, CriteriaQuery<?> cq, CriteriaBuilder cb){
				
				Predicate condition = null;
				condition = cb.and(cb.equal(root.get(DoctorSign_.loginid), loginId));
				return cq.where(condition).getRestriction();
				
			}
			
		};
		
	}
	
	/**
	 * code to return fax details of selected fax Id's
	 */
	
	public static Specification<H491> getSelectedFaxDetails(final List<Integer> faxId){
		
		return new Specification<H491>() {
			
			@Override
			public Predicate toPredicate(Root<H491> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				Expression<Integer> expr=root.get(H491_.h491001);
				Predicate predicate=query.where(expr.in(faxId)).getRestriction();
				return predicate;
				
			}
			
		};
		
	}
public static Specification<H496> getSelectedFaxDetails1(final List<Integer> faxId){
		
		return new Specification<H496>() {
			
			@Override
			public Predicate toPredicate(Root<H496> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				Expression<Integer> expr=root.get(H496_.h496001);
				Predicate predicate=query.where(expr.in(faxId)).getRestriction();
				return predicate;
				
			}
			
		};
		
	}

}
