package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;
import java.util.Map;

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
import com.glenwood.glaceemr.server.application.models.TherapySession;
import com.glenwood.glaceemr.server.application.services.GroupTherapy.AddNewGroupService;
import com.glenwood.glaceemr.server.application.services.GroupTherapy.AddNoteBean;
import com.glenwood.glaceemr.server.application.services.GroupTherapy.AddTherapyBean;
import com.glenwood.glaceemr.server.application.services.GroupTherapy.TherapyGroupBean;
import com.glenwood.glaceemr.server.application.services.GroupTherapy.TherapyLogBean;
import com.glenwood.glaceemr.server.application.services.GroupTherapy.TherapyPatientsBean;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;

/**
 * Controller for Group therapy
 * @author software
 *
 */
@Api(value = "Group Therapy", description = "contains the methods of Group Therapy", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value = "/user/GroupTherapy.Action")

public class GroupTherapyController {
	
	@Autowired
	AddNewGroupService addNewGroupService;
	
	private Logger logger = Logger.getLogger(GroupTherapyController.class);
	
	/**
	 * To get the data of providers,pos,groups
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/ListDefaultValues", method = RequestMethod.GET) 
	@ResponseBody
	
	public EMRResponseBean getDefaultValues()throws Exception{
		Map<String, Object> lists=addNewGroupService.listDefaults();
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(lists);
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
		return emrResponseBean;
	}
	
	/**
	 * to save addNotes
	 */
	@RequestMapping(value="/saveNotes",  method=RequestMethod.POST)
	@ResponseBody
	
	public void saveNotes(@RequestBody AddNoteBean data) throws Exception{
		data = getAddNoteBean(data);
		logger.debug("AddNotes is going to save");
		addNewGroupService.saveNotes(data);
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
		List<TherapySession> therapy=addNewGroupService.createTherapySession(dataToSave);
		logger.debug("In createTherapySession - therapy session created");
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(therapy);
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
	
	public EMRResponseBean listPatientDataByGroup(@RequestParam(value="groupId",required=false,defaultValue="-1")String groupId)throws Exception{
		Map<String, Object> lists=addNewGroupService.getPatientData(groupId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(lists);
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
	private AddNoteBean getAddNoteBean(AddNoteBean data){
		data.setGwid(Optional.fromNullable(data.getGwid()+"").or(""));
		data.setPatientDetailsEnteredBy(Optional.fromNullable(data.getPatientDetailsEnteredBy()+"").or(""));
		data.setPatientDetailsEnteredOn(Optional.fromNullable(data.getPatientDetailsEnteredOn()+"").or(""));
		data.setPatientDetailsId(Integer.parseInt(Optional.fromNullable(data.getPatientDetailsId()+"").or("-1")));
		data.setPatientId(Optional.fromNullable(data.getPatientId()+"").or("-1"));
		data.setPatientDetailsModifiedBy(Optional.fromNullable(data.getPatientDetailsModifiedBy()+"").or(""));
		data.setPatientDetailsModifiedOn(Optional.fromNullable(data.getPatientDetailsModifiedOn()+"").or("-1"));
		data.setSessionId(Integer.parseInt(Optional.fromNullable(data.getSessionId()+"").or("-1")));
		data.setValue(Optional.fromNullable(data.getValue()+"").or("-1"));
		return data;
	}
	
}
