package com.glenwood.glaceemr.server.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.glenwood.glaceemr.server.application.models.AlertArchive;

public interface AlertArchiveRepository extends JpaRepository<AlertArchive, Integer>,JpaSpecificationExecutor<AlertArchive>{

}
