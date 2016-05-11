package com.glenwood.glaceemr.server.application.repositories.skintests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.glenwood.glaceemr.server.application.models.skintests.SkinTestConcentration;

public interface SkinTestConcentrationRepository extends JpaRepository<SkinTestConcentration, Integer> ,JpaSpecificationExecutor<SkinTestConcentration>{

}
