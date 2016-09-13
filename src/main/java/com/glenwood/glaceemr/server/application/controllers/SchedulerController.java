package com.glenwood.glaceemr.server.application.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.SchedulerAppointmentBean;
import com.glenwood.glaceemr.server.application.models.SchedulerResource;
import com.glenwood.glaceemr.server.application.models.SchedulerResourceCategory;
import com.glenwood.glaceemr.server.application.services.scheduler.SchedulerService;
import com.wordnik.swagger.annotations.Api;
/**
 * 
 * @author Manikandan
 *
 */
@Api(value = "/Scheduler", description = "To get the appointments based on date and resource.", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="Scheduler")
public class SchedulerController {

	@Autowired
	SchedulerService schedulerService;
	
	/**
	 * To get the resources list.
	 * @return
	 */
	@RequestMapping(value="/getresources",method=RequestMethod.GET)
	@ResponseBody
	public List<SchedulerResource> getResources(){
		
			List<SchedulerResource> schedulerResources=schedulerService.getResources();
		
		return schedulerResources;
	}
	
	/**
	 * To get the resources category list.
	 * @return
	 */
	@RequestMapping(value="/getresourcecategory",method=RequestMethod.GET)
	@ResponseBody
	public List<SchedulerResourceCategory> getResourceCategories(){
		
			List<SchedulerResourceCategory> schedulerResourceCategories=schedulerService.getResourceCategories();
		
		return schedulerResourceCategories;
	}
	
	/**
	 * To get appointment based on date.
	 * @return
	 */
	@RequestMapping(value="/getappointments",method=RequestMethod.GET)
	@ResponseBody
	public List<SchedulerAppointmentBean> getAppointments(@RequestParam(value="apptdate",required=true,defaultValue="t") String apptDate,
															@RequestParam(value="resourceid") String resourceId){
			
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date date=null; 
			try{
				date=formatter.parse(apptDate);
			}
			catch(Exception e){
				date=new Date();
			}
			
			List<SchedulerAppointmentBean> schedulerAppointments=schedulerService.getAppointments(date,resourceId);
		
		return schedulerAppointments;
	}
}