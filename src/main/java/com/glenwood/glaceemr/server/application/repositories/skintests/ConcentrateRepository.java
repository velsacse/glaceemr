package com.glenwood.glaceemr.server.application.repositories.skintests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.glenwood.glaceemr.server.application.models.skintests.Concentrate;

public interface ConcentrateRepository extends JpaRepository<Concentrate, Integer> ,JpaSpecificationExecutor<Concentrate> {

}
