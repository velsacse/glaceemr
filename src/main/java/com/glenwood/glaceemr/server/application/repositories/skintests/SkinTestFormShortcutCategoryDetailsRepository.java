package com.glenwood.glaceemr.server.application.repositories.skintests;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.glenwood.glaceemr.server.application.models.skintests.SkinTestFormShortcutCategoryDetails;

@Repository
public interface SkinTestFormShortcutCategoryDetailsRepository extends JpaRepository<SkinTestFormShortcutCategoryDetails, Integer>,JpaSpecificationExecutor<SkinTestFormShortcutCategoryDetails>{

}
