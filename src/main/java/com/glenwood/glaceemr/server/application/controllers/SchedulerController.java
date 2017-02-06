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
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
/**
 * 
 * @author Manikandan
 *
 */
@RestController
@Transactional
@RequestMapping(value="/user/Scheduler")
public class SchedulerController {

	@Autowired
	SchedulerService schedulerService;
	
	/**
	 * To get the resources list.
	 * @return
	 */
	@RequestMapping(value="/getresources",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getResources(){
		
			List<SchedulerResource> schedulerResources=schedulerService.getResources();
			EMRResponseBean emrResponseBean=new EMRResponseBean();
			emrResponseBean.setData(schedulerResources);
		
		return emrResponseBean;
	}
	
	/**
	 * To get the resources category list.
	 * @return
	 */
	@RequestMapping(value="/getresourcecategory",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getResourceCategories(){
		
			List<SchedulerResourceCategory> schedulerResourceCategories=schedulerService.getResourceCategories();
			EMRResponseBean emrResponseBean=new EMRResponseBean();
			emrResponseBean.setData(schedulerResourceCategories);
		
		return emrResponseBean;
	}
	
	/**
	 * To get appointment based on date.
	 * @return
	 * @param apptDate
	 * @param resourceId
	 */
	@RequestMapping(value="/getappointments",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getAppointments(@RequestParam(value="apptdate",required=true,defaultValue="t") String apptDate, @RequestParam(value="resourceid") String resourceId){
			
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date date=null; 
			try{
				date=formatter.parse(apptDate);
			}
			catch(Exception e){
				date=new Date();
			}
			
			List<SchedulerAppointmentBean> schedulerAppointments=schedulerService.getAppointments(date,resourceId);

			EMRResponseBean emrResponseBean=new EMRResponseBean();
			emrResponseBean.setData(schedulerAppointments);
			
		return emrResponseBean;
	}
	/**
	 * To get default resource
	 * @return
	 * @param userId
	 */
	@RequestMapping(value="/getdefaultresource",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getAppointments(@RequestParam(value="userid",required=true,defaultValue="-1") String userId){
			
			String resource = schedulerService.getDefaultResource(userId);
			EMRResponseBean emrResponseBean=new EMRResponseBean();
			emrResponseBean.setData(resource);
			
		return emrResponseBean;
	}
}
