package com.glenwood.glaceemr.server.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.glenwood.glaceemr.server.application.models.H810;

public interface H810Respository extends JpaRepository<H810,Integer>,JpaSpecificationExecutor<H810> {

}
