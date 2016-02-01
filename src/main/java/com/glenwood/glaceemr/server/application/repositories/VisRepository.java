package com.glenwood.glaceemr.server.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.glenwood.glaceemr.server.application.models.Vis;

public interface VisRepository  extends JpaRepository<Vis, Integer>,JpaSpecificationExecutor<Vis> {

}
