package com.glenwood.glaceemr.server.application.repositories;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.glenwood.glaceemr.server.application.models.TherapyGroupPatientMapping;

@Repository
public interface TherapyGroupPatientMappingRepository  extends JpaRepository<TherapyGroupPatientMapping, Integer>,JpaSpecificationExecutor<TherapyGroupPatientMapping> {
}

