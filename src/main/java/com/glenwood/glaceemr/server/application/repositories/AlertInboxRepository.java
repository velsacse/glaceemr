package com.glenwood.glaceemr.server.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.glenwood.glaceemr.server.application.models.AlertEvent;

public interface AlertInboxRepository extends JpaRepository<AlertEvent, Integer>,JpaSpecificationExecutor<AlertEvent>{

}
