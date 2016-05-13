package com.glenwood.glaceemr.server.application.repositories.skintests;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.glenwood.glaceemr.server.application.models.skintests.SkinTestOrder;

public interface SkinTestOrderRepository extends JpaRepository<SkinTestOrder, Integer> ,JpaSpecificationExecutor<SkinTestOrder> {
	@Query("select current_timestamp() from Users pb where 1=1")
	   Timestamp findCurrentTimeStamp();
}


