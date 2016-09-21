package com.glenwood.glaceemr.server.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.glenwood.glaceemr.server.application.models.H491;

public interface H491Repository extends JpaRepository<H491, Integer> ,JpaSpecificationExecutor<H491>{

}
