package com.glenwood.glaceemr.server.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.glenwood.glaceemr.server.application.models.OrdersetCategorylist;

public interface OrderSetCategoryListRepository extends JpaRepository<OrdersetCategorylist, Integer>,JpaSpecificationExecutor<OrdersetCategorylist>{

}
