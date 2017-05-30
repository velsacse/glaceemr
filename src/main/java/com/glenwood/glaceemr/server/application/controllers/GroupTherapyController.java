package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.TherapyGroup;
import com.glenwood.glaceemr.server.application.services.GroupTherapy.AddNewGroupService;
import com.glenwood.glaceemr.server.application.services.GroupTherapy.AddNoteBean;
import com.glenwood.glaceemr.server.application.services.GroupTherapy.AddTherapyBean;
import com.glenwood.glaceemr.server.application.services.GroupTherapy.TherapyGroupBean;
import com.glenwood.glaceemr.server.application.services.GroupTherapy.TherapyLogBean;
import com.glenwood.glaceemr.server.application.services.GroupTherapy.TherapyPatientsBean;
import com.glenwood.glaceemr.server.application.services.GroupTherapy.TherapyPrintBean;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogActionType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogModuleType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.LogUserType;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailEnumConstants.Log_Outcome;
import com.glenwood.glaceemr.server.application.services.audittrail.AuditTrailSaveService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.google.common.base.Optional;

/**
 * Controller for Group therapy
 * @author software
 *
 */
@RestController
@Transactional
@RequestMapping(value = "/user/GroupTherapy.Action")

public class GroupTherapyController {
	
	@Autowired
	AddNewGroupService addNewGroupService;
	
	@Autowired
	AuditTrailSaveService auditTrailSaveService;
	
	@Autowired
	HttpServletRequest request;
	
	private Logger logger = Logger.getLogger(GroupTherapyController.class);
	
	/**
	 * To get the data of providers,pos,groups
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/ListDefaultValues", method = RequestMethod.GET) 
	@ResponseBody
	
	public EMRResponseBean getDefaultValues(@RequestParam(value="userId",required=false,defaultValue="-1")Integer userId)throws Exception{
		Map<String, Object> lists=addNewGroupService.listDefaults(userId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(lists);
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved providers,pos,groups data", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
	
	/**
	 * To save new group
	 * @param data
	 * @throws Exception
	 */
	@RequestMapping(value ="/saveNewGroup",  method=RequestMethod.POST) 
	@ResponseBody
	
	public EMRResponseBean saveNewGroup(@RequestBody TherapyGroupBean data)throws Exception{
		data = getTherapyGroupBean(data);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(data);
		logger.debug("In saveNewGroup - new group is going to save");
		addNewGroupService.saveNewGroup(data);
		logger.debug("In saveNewGroup - new group saved");
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.CREATE, 0, Log_Outcome.SUCCESS, "successful insertion of new group", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
	
	
	/**
	 * to save addNotes
	 */
	@RequestMapping(value="/saveNotes",  method=RequestMethod.POST)
	@ResponseBody
	
	public void saveNotes(@RequestBody List<AddNoteBean> data) throws Exception{
		data = getAddNoteBean(data);
		logger.debug("AddNotes is going to save");
		addNewGroupService.saveNotes(data);
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.CREATE, 0, Log_Outcome.SUCCESS, "successfully  saved addNotes", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return;
		
	}
	
	
	/**
	 * To get the list of patients which belongs to the particular Group 
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/ListGroupData", method = RequestMethod.GET) 
	@ResponseBody
	
	public EMRResponseBean getGroupData(@RequestParam(value="groupId",required=false,defaultValue="-1")String groupId)throws Exception{
		List<TherapyGroup> lists=addNewGroupService.listGroupData(groupId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(lists);
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved list of patients which belongs to the particular Group", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
	
	/**
	 * To add the patient to the group
	 * @param dataToSave
	 * @throws Exception
	 */
	@RequestMapping(value ="/addPatientToGroup", method = RequestMethod.GET) 
	@ResponseBody
	
	public void addPatientToGroup(@RequestParam(value="dataToSave",required=false,defaultValue="-1")String dataToSave)throws Exception{
		logger.debug("In addPatientToGroup - patiet is adding to group");
		addNewGroupService.addPatientToTherapyGroup(dataToSave);
		logger.debug("In addPatientToGroup - patiet added to group");
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.CREATE, 0, Log_Outcome.SUCCESS, "successfully added patient to the group", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
	}
	
	/**
	 * To save the therapy session data for patients
	 * @param dataToSave
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/createTherapySession", method = RequestMethod.GET) 
	@ResponseBody
	
	public EMRResponseBean createTherapy(@RequestParam(value="dataToSave",required=false,defaultValue="-1")String dataToSave)throws Exception{
		logger.debug("In createTherapySession - creating therapy session");
		Map<String, Object>  therapy=addNewGroupService.createTherapySession(dataToSave);
		logger.debug("In createTherapySession - therapy session created");
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(therapy);
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.CREATE, 0, Log_Outcome.SUCCESS, "successfully inserted therapy session data for patients", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
	
	/**
	 * List patient data by Group like dob,acct#,last therapy session date
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/listPatientDataByGroup", method = RequestMethod.GET) 
	@ResponseBody
	
	public EMRResponseBean listPatientDataByGroup(@RequestParam(value="dataToSearch",required=false,defaultValue="-1")String dataToSearch)throws Exception{
		Map<String, Object> lists=addNewGroupService.getPatientData(dataToSearch);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(lists);
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved list patient data by Group", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
	
	/**
	 * To get the therapy log data
	 * @param dataToSave
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/therapyLog", method = RequestMethod.GET) 
	@ResponseBody
	
	public EMRResponseBean therapyLog(@RequestParam(value="dataToSearch",required=false,defaultValue="-1")String dataToSave)throws Exception{
		
		List<TherapyLogBean> therapy=addNewGroupService.therapySearchLog(dataToSave);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(therapy);
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved therapy log data", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
	
	/**
	 * To list the therapy patients
	 * @param dataToSave
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/listTherapyPatients", method = RequestMethod.GET) 
	@ResponseBody
	
	public EMRResponseBean listTherapyPatients(@RequestParam(value="dataToSearch",required=false,defaultValue="-1")String dataToSave)throws Exception{
		
		List<TherapyPatientsBean> therapy=addNewGroupService.getTherapyPatients(dataToSave);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(therapy);
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successfully retrieved the list of therapy patients", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
	
	/**
	 * To delete the patient from therapy group
	 * @param dataToDelete
	 * @throws Exception
	 */
	@RequestMapping(value ="/deletePatientFromTherapy", method = RequestMethod.GET) 
	@ResponseBody
	
	public void deletePatient(@RequestParam(value="dataToDelete",required=false,defaultValue="-1")String dataToDelete)throws Exception{
		logger.debug("In deletePatientFromTherapy - deleting the patient from therapy session or group");
		addNewGroupService.deleteTherapyPatient(dataToDelete);
		logger.debug("In deletePatientFromTherapy - deleted the patient from therapy session or group");
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.DELETE, 0, Log_Outcome.SUCCESS, "successfully deleted the patient from therapy group", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
	}
	
	/**
	 * to fetch shortcut code
	 * @param mode
	 * @throws Exception
	 */
	@RequestMapping(value="/fetchShortcutCode",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean fetchShortcutCode(@RequestParam(value="group") Integer group){
		group=Integer.parseInt(Optional.fromNullable(group+"").or("-1"));
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(addNewGroupService.fetchShortcutCode(group).toString());
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successful retrieval of shortcut code", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
	
	/**
	 * to fetch shortcut description
	 * @param shortcutId
	 * @throws Exception
	 */
	@RequestMapping(value="/fetchShortcutDescription",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean fetchShortcutData(@RequestParam(value="shortcutId") String shortcutId)
	{
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(addNewGroupService.fetchShortcutData(shortcutId).toString());
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successful retrieval of shortcut description", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
    
	/**
	 * to fetch notes data
	 * @param gwid
	 * @param patientId
	 * @param sessionId
	 * @param isPatient
	 * @return
	 */
	@RequestMapping(value="/fetchNotesData",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean fetchNotesData(@RequestParam(value="gwid") String gwid,@RequestParam(value="patientId") Integer patientId,@RequestParam(value="sessionId") Integer sessionId,@RequestParam(value="isPatient") Boolean isPatient)
	{
	List<AddTherapyBean> notesData=addNewGroupService.fetchNotesData(gwid,patientId,sessionId,isPatient);
	EMRResponseBean emrResponseBean = new EMRResponseBean();
	emrResponseBean.setData(notesData);
	auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successful retrieval of notes data", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
	return emrResponseBean;
	}
	
	/**
	 * to fetch data for AddTherapeuticIntervention
	 * @param gwid
	 * @param sessionId
	 * @param isPatient
	 * @return
	 */
	@RequestMapping(value="/fetchDataforTherapy",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean fetchDataforTherapy(@RequestParam(value="gwid") String gwid,@RequestParam(value="sessionId") Integer sessionId,@RequestParam(value="isPatient") Boolean isPatient)
	{
	List<AddTherapyBean> therapyData=addNewGroupService.fetchDataforTherapy(gwid,sessionId,isPatient);
	EMRResponseBean emrResponseBean = new EMRResponseBean();
	emrResponseBean.setData(therapyData);
	auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successful retrieval of data for AddTherapeuticIntervention", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
	return emrResponseBean;
	}
	
	/**
	 * to get data for print
	 * @param groupId
	 * @param sessionId
	 * @param patientId
	 * @param gwids
	 * @return
	 */
	@RequestMapping(value="/fetchPrintData",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean fetchGroupTherapyPrintData(@RequestParam(value="groupId") Integer groupId,@RequestParam(value="sessionId") Integer sessionId,@RequestParam(value="patientId") Integer patientId,@RequestParam(value="gwids") String gwids)
	{
	TherapyPrintBean therapyPrintBean=addNewGroupService.fetchGrouptherapyPrintData(groupId,sessionId,patientId,gwids);
	EMRResponseBean emrResponseBean = new EMRResponseBean();
	emrResponseBean.setData(therapyPrintBean);
	auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successful retrieval of data for print notes", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
	return emrResponseBean;
	}
	
	
	
	/**
	 * therapyGroupBean for saving Group
	 * @param data
	 * @return
	 */
	private TherapyGroupBean getTherapyGroupBean(TherapyGroupBean data) {
		data.setProviderId(Integer.parseInt(Optional.fromNullable(data.getProviderId()+"").or("-1")));
		data.setPosId(Integer.parseInt(Optional.fromNullable(data.getPosId()+"").or("-1")));
		data.setGroupName(Optional.fromNullable(data.getGroupName()+"").or(""));
		data.setGroupDesc(Optional.fromNullable(data.getGroupDesc()+"").or(""));
		data.setDiagnosis(Optional.fromNullable(data.getDiagnosis()+"").or(""));
		data.setDefaultTherapyTime(Optional.fromNullable(data.getDefaultTherapyTime()+"").or(""));
		data.setLoginId(Integer.parseInt(Optional.fromNullable(data.getLoginId()+"").or("-1")));
		data.setGroupId(Integer.parseInt(Optional.fromNullable(data.getGroupId()+"").or("-1")));
	    data.setIsActive(Boolean.parseBoolean(Optional.fromNullable(data.getIsActive()+"").or("false")));
	    data.setLeaderId(Integer.parseInt(Optional.fromNullable(data.getLeaderId()+"").or("-1")));
        data.setSupervisorId(Integer.parseInt(Optional.fromNullable(data.getSupervisorId()+"").or("-1")));
	    return data;
	}
	
	/**
	 * addNoteBean for saving notes
	 * @param data
	 * @return
	 */
	private List<AddNoteBean> getAddNoteBean(List<AddNoteBean> data){
		for(int i=0;i<data.size();i++){
			data.set(i,data.get(i));
		}
		return data;
	}
	
	/**
	 * To get the list of all open sessions 
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value ="/ListGroupDataandSessionData", method = RequestMethod.GET) 
	@ResponseBody
	
	public EMRResponseBean getGroupandSessionData(@RequestParam(value="dataToSearch",required=false,defaultValue="-1")String dataToSearch)throws Exception{
		Map<String,Object> lists=addNewGroupService.listGroupandSessionData(dataToSearch);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(lists);
		auditTrailSaveService.LogEvent(LogType.GLACE_LOG, LogModuleType.THERAPHYSESSION, LogActionType.VIEW, 0, Log_Outcome.SUCCESS, "successful retrieval of displaying all open sessions", -1, request.getRemoteAddr(), -1, "", LogUserType.USER_LOGIN, "", "");
		return emrResponseBean;
	}
	
}