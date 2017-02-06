package com.glenwood.glaceemr.server.application.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.Bean.DocumentsCategoryBean;
import com.glenwood.glaceemr.server.application.models.AlertEvent;
import com.glenwood.glaceemr.server.application.models.AlertPatientDocMapping;
import com.glenwood.glaceemr.server.application.models.FileDetails;
import com.glenwood.glaceemr.server.application.models.FileName;
import com.glenwood.glaceemr.server.application.models.FormsTemplate;
import com.glenwood.glaceemr.server.application.models.PatientDocumentsNotes;
import com.glenwood.glaceemr.server.application.services.Documents.DocumentsService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;

/**
 * Controller for PatientDocuments
 * @author Soundarya
 *
 */
@RestController
@Transactional
@RequestMapping(value="/user/Documents")
public class DocumentsController {

	@Autowired
	DocumentsService documentsService;

	private Logger logger = Logger.getLogger(DocumentsController.class);

	/**
	 * TO get category list based on patientId, If the patientId is not provided then it displays all category list
	 * @param patientId
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value="/getCategoryList", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getCategoryList(@RequestParam(value="patientId",required=false, defaultValue="-1") Integer patientId) throws Exception
	{
		List<Object> catDetails= documentsService.getCategoryList(patientId);
		EMRResponseBean result=new EMRResponseBean();
		result.setData(catDetails);
		return result;
	}

	/**
	 * To get particular folders of patient based on patientId and CategoryId
	 * @param patientId
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getFolders", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getFileDetailsByPatientId(@RequestParam(value="patientId",required=true, defaultValue="-1") Integer patientId , @RequestParam(value="categoryId", required=true, defaultValue="-1") Integer categoryId) throws Exception
	{
		List<FileDetails> filecatDetails= documentsService.getFileDetailsByPatientId(patientId,categoryId);
		EMRResponseBean result= new EMRResponseBean();
		result.setData(filecatDetails);
		return result;
	}

	/**
	 * To get the list of files of a particular patient based on categoryId and fileNameId
	 * @param patientId
	 * @param categoryId
	 * @param fileNameId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getFileList",method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getFileList(@RequestParam(value="fileDetailsId", required= true, defaultValue="-1")String fileDetailsId) throws Exception{
		FileDetails fileDetails=documentsService.getFileList(fileDetailsId);
		EMRResponseBean result= new EMRResponseBean();
		result.setData(fileDetails);
		return result;
	}

	/**
	 * To get Info about documents
	 * @param fileNameId
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value="/getInfo",method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getInfo(@RequestParam(value="fileNameId", required= true, defaultValue="-1") Integer fileNameId
			) throws Exception{
		List<FileName> fileName=documentsService.getInfo(fileNameId);
		EMRResponseBean result= new EMRResponseBean();
		result.setData(fileName);
		return result;
	}


	/**
	 * To get Document notes
	 * @param notesFilenameId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getNotesByFileId", method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getDocNotes(@RequestParam(value="notesFilenameId", required= true, defaultValue="-1")Integer notesFilenameId) throws Exception{
		List<PatientDocumentsNotes> patientDocNotes= documentsService.getDocNotesDetails(notesFilenameId);
		EMRResponseBean result= new EMRResponseBean();
		result.setData(patientDocNotes);
		return result;
	}

	/**
	 * To add comments in Notes option
	 * @param notesFilenameId
	 * @param notesPatientNotes
	 * @param userId
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value="/addNotesByFileId", method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean addDocNotes(@RequestParam(value="notesFilenameId", required= true, defaultValue="-1")Integer notesFilenameId,
			@RequestParam(value="notesPatientNotes", required=false, defaultValue="")String notesPatientNotes,
			@RequestParam(value="userId",required=true)Integer userId) throws Exception{
		List<PatientDocumentsNotes> patientDocNotes= documentsService.addDocNotesDetails(notesFilenameId,notesPatientNotes,userId);
		EMRResponseBean result= new EMRResponseBean();
		result.setData(patientDocNotes);
		return result;
	}

	/**
	 * To delete a folder by passing its folderId
	 * @param fileDetailsId
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteFolder",method= RequestMethod.GET)
	@ResponseBody
	public void deleteFolder(@RequestParam(value="fileDetailsId", required= true,defaultValue="-1")String fileDetailsId)throws Exception{
		documentsService.deleteFolder(fileDetailsId);			
	}

	/**
	 * To delete a file by passing its fileId
	 * @param fileNameId
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteFile", method=RequestMethod.GET)
	@ResponseBody
	public void  deleteFile(@RequestParam(value="fileNameId", required=true, defaultValue="-1")Integer fileNameId,
			@RequestParam(value="patientId",required= false, defaultValue="-1") Integer patientId) throws Exception{
		documentsService.deleteFile(fileNameId,patientId);
	}

	/**
	 * To review group of documents
	 * @param fileDetailsId
	 * @param patientId
	 * @param categoryId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/reviewGroupOfDocs", method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean reviewGroupOfDocs(
			@RequestParam(value="fileDetailsId",required=true)String fileDetailsId,
			@RequestParam(value="patientId",required=true)Integer patientId,
			@RequestParam(value="categoryId",required=true)Integer categoryId,
			@RequestParam(value="userId",required=true)Integer userId){
		List<FileDetails> fileDetails=documentsService.reviewGroupOfDocs(fileDetailsId,categoryId,patientId,userId);
		EMRResponseBean result= new EMRResponseBean();
		result.setData(fileDetails);
		return result;

	}

	/**
	 * To review a single file
	 * @param fileNameId
	 * @param userId
	 * @return
	 */

	@RequestMapping(value="/reviewDocuments", method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean reviewDocuments(
			@RequestParam(value="fileNameId",required=true)Integer fileNameId,
			@RequestParam(value="userId",required=true)Integer userId){
		List<FileName> fileName=documentsService.reviewDocuments(fileNameId, userId);
		EMRResponseBean result= new EMRResponseBean();
		result.setData(fileName);
		return result;

	}

	/**
	 * To get details when a message is forwarded from patient documents
	 * @param alertId
	 * @param patientId
	 * @return
	 */
	@RequestMapping(value="/getDetailsByAlertId", method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean alertByCategory(
			@RequestParam(value="alertId",required=true)String alertId,
			@RequestParam(value="patientId",required= false, defaultValue="-1") Integer patientId){
		FileDetails alertPatientDocMapping=documentsService.alertByCategory(alertId,patientId);
		EMRResponseBean result= new EMRResponseBean();
		result.setData(alertPatientDocMapping);
		return result;
	}

	/**
	 * To forward the documents using alerts
	 * @param fromid
	 * @param toid
	 * @param status
	 * @param alertid
	 * @param docCategoryid
	 * @param refid
	 * @param patientid
	 * @param encounterid
	 * @param msg
	 * @param chartid
	 * @param roomid
	 * @param parentid
	 * @return
	 */

	@RequestMapping(value = "/forward", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean createAlerts(
			 @RequestParam(value="fromid",required=true) String fromid,
			 @RequestParam(value="toid",required=true) String toid,
			 @RequestParam(value="status",required=true, defaultValue="1") String status,
			@RequestParam(value="alertid",required=true) String alertid,
			@RequestParam(value="docCategoryid",required=true) String docCategoryid,
			@RequestParam(value="refid",required=true, defaultValue="-1") String refid,
			@RequestParam(value="patientid",required=true, defaultValue="-1") String patientid,
			@RequestParam(value="encounterid",required=true, defaultValue="-1") String encounterid,
			@RequestParam(value="msg",required=true, defaultValue="") String msg,
			@RequestParam(value="chartid",required=true, defaultValue="-1") String chartid,
			 @RequestParam(value="roomid",required=true, defaultValue="-1") String roomid,
			@RequestParam(value="parentid",required=true, defaultValue="-1") String parentid){

		List<Integer> toIdList=new ArrayList<Integer>();
		String[] toIdArray=toid.split(",");
		for (String s : toIdArray) {
			toIdList.add(Integer.parseInt(s));
		}
		logger.debug("Creating the alert with id "+fromid);	
		List<AlertEvent> alertEvent=documentsService.forwardAlert(Integer.parseInt(fromid),toIdList,Integer.parseInt(status),alertid,Integer.parseInt(docCategoryid),Integer.parseInt(refid),Integer.parseInt(patientid),Integer.parseInt(encounterid),msg,Integer.parseInt(chartid),Integer.parseInt(roomid),Integer.parseInt(parentid));
		EMRResponseBean result= new EMRResponseBean();
		result.setData(alertEvent);
		return result;
	}
	
	
	//CONSENT FORMS
	/**
	 * To get list of consent forms
	 * @return 
	 */
	@RequestMapping(value = "/getConsentForms", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getConsentForms(){
		List<FormsTemplate> formTemplate=documentsService.getConsentForms();
		EMRResponseBean result=new EMRResponseBean();
		result.setData(formTemplate);
		return result;
		
	}
	
	/**
	 * To get list of saved consent forms
	 * @param patientId
	 * @return
	 */
	@RequestMapping(value = "/getSavedForms", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getSavedForms(
			@RequestParam(value="patientId",required=true,defaultValue="-1")String patientId){
		List<Object> getSavedForms=documentsService.getSavedForms(patientId);
		EMRResponseBean result=new EMRResponseBean();
		result.setData(getSavedForms);
		return result;
	}

}