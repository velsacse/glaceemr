package com.glenwood.glaceemr.server.application.repositories.skintests;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.glenwood.glaceemr.server.application.models.skintests.SkinTestFormShortcut;

@Repository
public interface SkinTestFormShortcutRepository extends JpaRepository<SkinTestFormShortcut, Integer> ,JpaSpecificationExecutor<SkinTestFormShortcut> {
	@Query("select current_timestamp() from Users pb where pb.userId=1")
	   Timestamp findCurrentTimeStamp();
}
