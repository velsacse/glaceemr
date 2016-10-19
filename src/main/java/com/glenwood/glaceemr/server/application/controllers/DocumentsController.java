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
import com.glenwood.glaceemr.server.application.models.PatientDocumentsNotes;
import com.glenwood.glaceemr.server.application.services.Documents.DocumentsService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Controller for PatientDocuments
 * @author Soundarya
 *
 */
@Api(value="/user/Documents",description="To deal with category details",consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="/user/Documents")
public class DocumentsController {

	@Autowired
	DocumentsService documentsService;

	private Logger logger = Logger.getLogger(AlertInboxController.class);

	/**
	 * TO get category list based on patientId, If the patientId is not provided then it displays all category list
	 * @param patientId
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value="/getCategoryList", method = RequestMethod.GET)
	@ApiOperation(value = "Returns list of categories", response = DocumentsCategoryBean.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Successful retrieval of Employee details"),
			@ApiResponse(code = 404, message = "when patient id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
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
	@ApiOperation(value = "Returns  categories", response = FileDetails.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Successful retrieval of Employee details"),
			@ApiResponse(code = 404, message = "when patientId and CategoryId does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
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
	@ApiOperation(value="Returns list of files",response = FileDetails.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Successful retrieval of Employee details"),
			@ApiResponse(code = 404, message = "when File id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
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
	@ApiOperation(value="Returns list of files",response = FileDetails.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Successful retrieval of Employee details"),
			@ApiResponse(code = 404, message = "when File id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
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
	@ApiOperation(value="Returns notes for a document", response=PatientDocumentsNotes.class)
	@ApiResponses(value={
			@ApiResponse(code = 200, message = "Successful retrieval of Employee details"),
			@ApiResponse(code = 404, message = "when Notes id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
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
	@ApiOperation(value="Returns notes for a document", response=PatientDocumentsNotes.class)
	@ApiResponses(value={
			@ApiResponse(code = 200, message = "Successful retrieval of Employee details"),
			@ApiResponse(code = 404, message = "when Notes id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
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
	@ApiOperation(value="Returns that folder", response= FileDetails.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Successful retrieval of folder details"),
			@ApiResponse(code = 404, message = "when that folder id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
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
	@ApiOperation(value="deletes that particular document", response=FileName.class)
	@ApiResponses(value={
			@ApiResponse(code = 200, message = "Successful retrieval of file details"),
			@ApiResponse(code = 404, message = "when file id  does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
	@ResponseBody
	public void  deleteFile(@RequestParam(value="fileNameId", required=true, defaultValue="-1")Integer fileNameId,
			@ApiParam(name="patientId",value="patient id")@RequestParam(value="patientId",required= false, defaultValue="-1") Integer patientId) throws Exception{
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
	@ApiOperation(value="reviewGroupOfDocs", notes="Review Documents based on fileNameId")
	@RequestMapping(value="/reviewGroupOfDocs", method=RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value={
			@ApiResponse(code=200,message = "Successful retrieval of review alerts"),
			@ApiResponse(code=404, message=""),
			@ApiResponse(code=500,message="Internal server error")
	})
	public EMRResponseBean reviewGroupOfDocs(
			@ApiParam(name="fileDetailsId",value="list of file name")@RequestParam(value="fileDetailsId",required=true)String fileDetailsId,
			@ApiParam(name="patientId",value="list of file name")@RequestParam(value="patientId",required=true)Integer patientId,
			@ApiParam(name="categoryId",value="list of file name")@RequestParam(value="categoryId",required=true)Integer categoryId,
			@ApiParam(name="userd", value="login user id")@RequestParam(value="userId",required=true)Integer userId){
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

	@ApiOperation(value="ReviewDocuments", notes="Review Documents based on fileNameId")
	@RequestMapping(value="/reviewDocuments", method=RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value={
			@ApiResponse(code=200,message = "Successful retrieval of review alerts"),
			@ApiResponse(code=404, message=""),
			@ApiResponse(code=500,message="Internal server error")
	})
	public EMRResponseBean reviewDocuments(
			@ApiParam(name="fileNameId",value="list of file name")@RequestParam(value="fileNameId",required=true)Integer fileNameId,
			@ApiParam(name="userId", value="login user id")@RequestParam(value="userId",required=true)Integer userId){
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
	@ApiOperation(value="getDetailsByAlertId", notes="getting details by passing alert id",response= AlertPatientDocMapping.class)
	@ResponseBody
	@ApiResponses(value={
			@ApiResponse(code=200,message = "Successful retrieval of review alerts"),
			@ApiResponse(code=404, message=""),
			@ApiResponse(code=500,message="Internal server error")
	})
	public EMRResponseBean alertByCategory(
			@ApiParam(name="alertId",value="Alert id")@RequestParam(value="alertId",required=true)String alertId,
			@ApiParam(name="patientId",value="patient id")@RequestParam(value="patientId",required= false, defaultValue="-1") Integer patientId){
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

	@ApiOperation(value = "forward alerts", notes = "Create one alert (insert new alert into alert_event table)")
	@RequestMapping(value = "/forward", method = RequestMethod.GET)
	@ResponseBody
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Successful retrieval of modified alerts"),
			@ApiResponse(code = 404, message = "when user id or alert id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")})
	public EMRResponseBean createAlerts(
			@ApiParam(name="fromid",value="logged in user id") @RequestParam(value="fromid",required=true) String fromid,
			@ApiParam(name="toid",value="to id") @RequestParam(value="toid",required=true) String toid,
			@ApiParam(name="status",value="alert status") @RequestParam(value="status",required=true, defaultValue="1") String status,
			@ApiParam(name="alertid",value="alert id") @RequestParam(value="alertid",required=true) String alertid,
			@ApiParam(name="docCategoryid",value="docCategory id") @RequestParam(value="docCategoryid",required=true) String docCategoryid,
			@ApiParam(name="refid",value="reference id") @RequestParam(value="refid",required=true, defaultValue="-1") String refid,
			@ApiParam(name="patientid",value="patient id") @RequestParam(value="patientid",required=true, defaultValue="-1") String patientid,
			@ApiParam(name="encounterid",value="encounter id") @RequestParam(value="encounterid",required=true, defaultValue="-1") String encounterid,
			@ApiParam(name="msg",value="message") @RequestParam(value="msg",required=true, defaultValue="") String msg,
			@ApiParam(name="chartid",value="chart id") @RequestParam(value="chartid",required=true, defaultValue="-1") String chartid,
			@ApiParam(name="roomid",value="room id") @RequestParam(value="roomid",required=true, defaultValue="-1") String roomid,
			@ApiParam(name="parentid",value="parent id") @RequestParam(value="parentid",required=true, defaultValue="-1") String parentid){

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


}