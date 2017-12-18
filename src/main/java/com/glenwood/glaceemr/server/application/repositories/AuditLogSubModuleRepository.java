package com.glenwood.glaceemr.server.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.glenwood.glaceemr.server.application.models.AuditLogSubModules;

public interface AuditLogSubModuleRepository
		extends JpaRepository<AuditLogSubModules, Integer>, JpaSpecificationExecutor<AuditLogSubModules> {

	@Query(value = "select a.moduleId from AuditLogSubModules a where a.parentModuleId=?1")
	List<Integer> getSubModuleIds(Integer parentModuleId);
}
