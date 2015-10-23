package com.glenwood.glaceemr.server.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.glenwood.glaceemr.server.application.models.Encounter;

public interface EncounterRepository extends JpaRepository<Encounter, Integer>,JpaSpecificationExecutor<Encounter>{

}
