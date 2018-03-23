package com.glenwood.glaceemr.server.application.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.Appointment;
import com.glenwood.glaceemr.server.application.models.SchedulerAppointmentBean;
import com.glenwood.glaceemr.server.application.models.SchedulerResource;
import com.glenwood.glaceemr.server.application.models.SchedulerResourceCategory;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogActionType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogModuleType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogUserType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.Log_Outcome;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailSaveService;
import com.glenwood.glaceemr.server.application.services.scheduler.SchedulerService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.SessionMap;
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

	@Autowired
	AuditTrailSaveService auditTrailSaveService;

	@Autowired
	SessionMap sessionMap;

	@Autowired
	HttpServletRequest request;

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
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "Getting Scheduler resources.", sessionMap.getUserID(), request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");

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
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "Getting Scheduler resource categories.", sessionMap.getUserID(), request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");

		return emrResponseBean;
	}

	/**
	 * To get appointment based on date.
	 * @return
	 * @param apptDate
	 * @param resourceId
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/getappointments",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getAppointments(@RequestParam(value="apptdate",required=true,defaultValue="t") String apptDate,
            @RequestParam(value="resourceid",required=true) Integer[] resourceIds){
        SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
        Date date=null;
        try{
            if(!apptDate.equals("-1")){
                date=new Date(apptDate);
                sdf.format(date);
            }
            else{    // If apptDate=-1 ===> Current date
                date=new Date();
                sdf.format(date);
            }
        }
        catch(Exception e){
            System.out.println(" Exception while parsing date :: "+apptDate);
            e.printStackTrace();
        }
     
        List<SchedulerAppointmentBean> schedulerAppointments=schedulerService.getAppointments(date,resourceIds);

        EMRResponseBean emrResponseBean=new EMRResponseBean();
        emrResponseBean.setData(schedulerAppointments);

        auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "Getting Scheduler appointments based on resource.", sessionMap.getUserID(), request.getRemoteAddr(), -1, "resourceId="+resourceIds.toString(), LogUserType.USER_LOGIN, "", "");
        return emrResponseBean;
    } 

	
	/**
	 * To get the week appointments based on date.
	 * @return
	 * @param apptDate
	 * @param resourceId
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/getweekappointments",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getWeekAppointments(@RequestParam(value="apptdate",required=true,defaultValue="t") String apptDate,
            @RequestParam(value="resourceid",required=true) Integer[] resourceIds,
            @RequestParam(value="viewType",required=true) String viewType){
        SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
        Date date=null;
        try{
            if(!apptDate.equals("-1")){
                date=new Date(apptDate);
                sdf.format(date);
            }
            else{    // If apptDate=-1 ===> Current date
                date=new Date();
                sdf.format(date);
            }
        }
        catch(Exception e){
            System.out.println(" Exception while parsing date :: "+apptDate);
            e.printStackTrace();
        }
        
        List<List<SchedulerAppointmentBean>> schedulerAppointments=schedulerService.getWeekAppointments(date,resourceIds,viewType);

        EMRResponseBean emrResponseBean=new EMRResponseBean();
        emrResponseBean.setData(schedulerAppointments);

        auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "Getting Scheduler appointments based on resource.", sessionMap.getUserID(), request.getRemoteAddr(), -1, "resourceId="+resourceIds.toString(), LogUserType.USER_LOGIN, "", "");
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

		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "Getting Scheduler default resource.", sessionMap.getUserID(), request.getRemoteAddr(), -1, "userId="+userId, LogUserType.USER_LOGIN, "", "");

		return emrResponseBean;
	}

	/**
     * To get templates
     * @param userId
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
	@RequestMapping(value="/gettemplates",method=RequestMethod.GET)
    @ResponseBody
    public EMRResponseBean getTemplates(@RequestParam(value="resourceId",required=true) Integer[] userIds,
    		                            @RequestParam(value="date", required=true) String dateString
    		                           ){

        Date date=null;
        EMRResponseBean emrResponseBean=new EMRResponseBean();
        Map<Integer,Object> resultObject = new HashMap<Integer,Object>();
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
            if(!dateString.equals("-1")){
                date=new Date(dateString);
                sdf.format(date);
            }
            else{    // If dateString=-1 ===> Current date
                date=new Date();
                sdf.format(date);
            }
        }
        catch(Exception e){
            System.out.println(" Exception while parsing date :: "+dateString);
            e.printStackTrace();
        }
  
        for(Integer userId: userIds){
            Object o = schedulerService.getTemplates(userId, date);
            resultObject.put(userId,o);
        }
        emrResponseBean.setData(resultObject);

        auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "Getting templates", sessionMap.getUserID(), request.getRemoteAddr(), -1, "resourceId="+userIds, LogUserType.USER_LOGIN, "", "");

        return emrResponseBean;
    } 	
    
    
    /**
     * To get Week templates
     * @param userId
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
	@RequestMapping(value="/getweektemplates",method=RequestMethod.GET)
    @ResponseBody
    public EMRResponseBean getWeekTemplates(@RequestParam(value="resourceId",required=true) Integer[] userIds,
    		                            @RequestParam(value="date", required=true) String dateString
    		                           ){

        Date date=null;
        EMRResponseBean emrResponseBean=new EMRResponseBean();
        
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
            if(!dateString.equals("-1")){
                date=new Date(dateString);
                sdf.format(date);
            }
            else{    // If dateString=-1 ===> Current date
                date=new Date();
                sdf.format(date);
            }
        }
        catch(Exception e){
            System.out.println(" Exception while parsing date :: "+dateString);
            e.printStackTrace();
        }
       // for(Integer userId: userIds){
            Object weekTemplatesData = schedulerService.getWeekTemplates(userIds[0], date);
            //resultObject.put(userId,o);
       // }
        emrResponseBean.setData(weekTemplatesData);

        auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "Getting templates", sessionMap.getUserID(), request.getRemoteAddr(), -1, "resourceId="+userIds, LogUserType.USER_LOGIN, "", "");

        return emrResponseBean;
    } 	

    
    /**
     * To create appointment
     * Sample body content {"schApptDate":"2015-07-22","schApptStarttime":"2015-07-22 14:00:00","schApptEndtime":"2015-07-22 14:20:00","schApptInterval":10,"schApptPatientId":2272,"schApptPatientname":"Tests","schApptLocation":1,"schApptResource":3,"schApptStatus":6,"schApptType":56,"schApptLastmodifiedTime":"2015-07-22 14:00:00","schApptLastmodifiedUserId":"14","schApptRoomId":"2","schApptStatusgrpId":2}
     * @param appointment
     * @return
     */
    @RequestMapping(value="/createappointment", method=RequestMethod.POST)
    @ResponseBody
    public EMRResponseBean createAppointment(@RequestBody(required=true) Appointment appointment){    

        EMRResponseBean emrResponseBean=new EMRResponseBean();
        Appointment createdAppointment=schedulerService.createAppointment(appointment);
        emrResponseBean.setData(createdAppointment);
    	return emrResponseBean;
    } 	
    
    /**
     * To get locations
     * @return
     */
    @RequestMapping(value="/getlocations",method=RequestMethod.GET)
    @ResponseBody
    public EMRResponseBean getLocation(){
    	
        EMRResponseBean emrResponseBean=new EMRResponseBean();
        List<Object> schResource = schedulerService.getApptBookLocationList();
        emrResponseBean.setData(schResource);
        auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "Getting location list", sessionMap.getUserID(), request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
        return emrResponseBean;
    }
    
    /**
     * To get appointment types
     * @return
     */
    @RequestMapping(value="/getappttypes",method=RequestMethod.GET)
    @ResponseBody
    public EMRResponseBean getApptTypes(){
    	
        EMRResponseBean emrResponseBean=new EMRResponseBean();
        List<Object> apptTypes = schedulerService.getApptTypes();
        emrResponseBean.setData(apptTypes);
        auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "Getting appointment types", sessionMap.getUserID(), request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
        return emrResponseBean;
    }
    
    
    /**
     * To get appointment status
     * @return
     */
    @RequestMapping(value="/getapptstatus",method=RequestMethod.GET)
    @ResponseBody
    public EMRResponseBean getApptStatus(){
    	
        EMRResponseBean emrResponseBean=new EMRResponseBean();
        List<Object> apptTypes = schedulerService.getApptStatus();
        emrResponseBean.setData(apptTypes);
        auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "Getting appointment status", sessionMap.getUserID(), request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
        return emrResponseBean;
    }
    
    
    /**
     * To change status
     * @return
     */    
    @RequestMapping(value="/changeappointmentstatus", method=RequestMethod.POST)
    @ResponseBody
    public EMRResponseBean changeAppointmentStatus(@RequestBody(required=true) Appointment appointment){
    	List<SchedulerAppointmentBean> changestatus= schedulerService.changeAppointmentStatus(appointment);
        EMRResponseBean emrResponseBean=new EMRResponseBean();
        emrResponseBean.setData(changestatus);
        auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "changed appointment status", sessionMap.getUserID(), request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
    	return emrResponseBean;
    }
    /**
     * paste appt
     * @return
     */    
    @RequestMapping(value="/pasteappointment", method=RequestMethod.POST)
    @ResponseBody
    public EMRResponseBean pasteappointment(@RequestBody(required=true) Appointment fromjson,@RequestParam(value="tojson",required=false)Appointment tojson){
  
    	Appointment pasteappt= schedulerService.pasteappointment(fromjson,new Appointment());
        EMRResponseBean emrResponseBean=new EMRResponseBean();
        emrResponseBean.setData(pasteappt);
        auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "paste appointment", sessionMap.getUserID(), request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
    	return emrResponseBean;
    }
    
    
    /**
     * To update appointment
     * @param appointment
     * @return
     */
    @RequestMapping(value="/updateappointment", method=RequestMethod.POST)
    @ResponseBody
    public EMRResponseBean updateappointment(@RequestBody(required=true) Appointment appointment){    
        EMRResponseBean emrResponseBean=new EMRResponseBean();
        List<SchedulerAppointmentBean> updateappointment=schedulerService.updateAppointment(appointment);
        emrResponseBean.setData(updateappointment);   
        auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "Updated appointment status", sessionMap.getUserID(), request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
    	return emrResponseBean;
    } 	
   
    
	/**
	 * To get appointment based on date.
	 * @return
	 * @param apptDate
	 * @param resourceId
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/getdetailappointment",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getDetailAppointment(@RequestParam(value="apptdate",required=true,defaultValue="t") String apptDate,
            @RequestParam(value="patientId",required=true) String patientId,
            @RequestParam(value="apptId",required=true) String apptId){
        SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
        System.out.println("appt date string >"+apptDate);
        Date date=null;
        try{
            if(!apptDate.equals("-1")){
                date=new Date(apptDate);
                sdf.format(date);
            }
            else{    // If apptDate=-1 ===> Current date
                date=new Date();
                sdf.format(date);
            }
        }
        catch(Exception e){
            System.out.println(" Exception while parsing date :: "+apptDate);
            e.printStackTrace();
        }
        
        List<SchedulerAppointmentBean> schedulerAppointments=schedulerService.getDetailAppointments(date,patientId,apptId);

        EMRResponseBean emrResponseBean=new EMRResponseBean();
        emrResponseBean.setData(schedulerAppointments);

        auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "Getting Scheduler appointments based on resource.", sessionMap.getUserID(), request.getRemoteAddr(), -1, "patientId="+patientId, LogUserType.USER_LOGIN, "", "");
        return emrResponseBean;
    } 
	
	
	 /**
     * To update the default users
     * @return
     */
    @RequestMapping(value="/updatedefaultusers",method=RequestMethod.GET)
    @ResponseBody
    public EMRResponseBean updateDefaultUsers(@RequestParam(value="zoomVal",required=false) String zoomValue,
    		                                  @RequestParam(value="group",required=true) String group,
    		                                  @RequestParam(value="resource",required=true,defaultValue="-1") String resource,
    		                                  @RequestParam(value="defSize",required=false) String defSize,
    		                                  @RequestParam(value="userId",required=true) String userId){
    	
        EMRResponseBean emrResponseBean=new EMRResponseBean();
        String resources = schedulerService.updateDefaultUsers(zoomValue,group,resource,defSize,userId);
        emrResponseBean.setData(resources);
        auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "Updated default users", sessionMap.getUserID(), request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
        return emrResponseBean;
    }
    
    /**
     * To get the reports data
     * @return
     */
    @RequestMapping(value="/getInitialReportData",method=RequestMethod.GET)
    @ResponseBody
    public EMRResponseBean getInitialReportData(){
    	
        EMRResponseBean emrResponseBean=new EMRResponseBean();
        List<String> resources = schedulerService.getInitialReportData();
        emrResponseBean.setData(resources);
        auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.SCHEDULER, LogActionType.VIEW, -1, Log_Outcome.SUCCESS, "Getting initial report data", sessionMap.getUserID(), request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
        return emrResponseBean;
    }
    
    
    
}
