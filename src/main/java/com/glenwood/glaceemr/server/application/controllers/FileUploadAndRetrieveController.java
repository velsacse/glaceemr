package com.glenwood.glaceemr.server.application.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glenwood.glaceemr.server.application.models.ImageDetailsBean;
import com.glenwood.glaceemr.server.application.services.portal.fileUploadService.FileUploadService;
import com.glenwood.glaceemr.server.application.services.portal.portalDocuments.PortalDocumentsService;
import com.glenwood.glaceemr.server.utils.HUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Transactional
@RequestMapping(value="/user/FileUploadAndRetrieve")
@Api(value="FileUploadAndRetrieveController", description="Handles requests regarding all types of uploads.")
public class FileUploadAndRetrieveController {

	@Autowired
	ObjectMapper objectMapper; 

	@Autowired
	FileUploadService fileUploadAndRetrieveService;
	
	@Autowired
	PortalDocumentsService portalDocumentsService;
	
	@Autowired
	PortalDocumentsController portalDocumentsController;
	
	@Autowired
	HttpServletRequest request;
	
	String pathSeperator = java.io.File.separator;


	@RequestMapping(value="/upload/profilePicture", method = RequestMethod.POST, produces= "text/html")
	@ApiOperation(value = "Uploads user patient picture and returns the saved path", notes = "Uploads user profile picture and returns the saved path.", response = User.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Patient profile picture upload successfull."),
			@ApiResponse(code = 404, message = "Patient profile picture to be uploaded not found."),
			@ApiResponse(code = 500, message = "Internal server error.")})
	@ResponseBody
	public String uploadPatientProfilePicture(
			@ApiParam(name="file", value="") 
			@RequestParam(value="file", required=false, defaultValue="") MultipartFile file,
			@ApiParam(name="patientId", value="") 
			@RequestParam(value="patientId", required=false, defaultValue="0") int patientId)
	{ 
		try{			
			Assert.notNull(file, "uploaded file is empty");
			Assert.notNull(patientId, "entity Id is not passed");

			String absolutePath = fileUploadAndRetrieveService.getSharedFolderPath()+"/photo/";
			Assert.notNull(absolutePath, "absolute path is empty");
			
			byte[] bytes = file.getBytes();
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(absolutePath+patientId+".jpg")));
			stream.write(bytes);
			stream.close();
			System.out.println("File uploaded successfully!");

			return "<html>Profile Picture uploaded successfully!</html>";

		}catch(Exception ex){
			ex.printStackTrace();
			return "Profile picture upload failed";
		}
	}
	
	
	
	@RequestMapping(value="/retrieve/profilePicture", method = RequestMethod.GET)
	@ApiOperation(value = "Retrieves user patient picture and returns base64 String format", notes = "Uploads user profile picture and returns the saved path.", response = User.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Patient profile picture retrieve successfull."),
			@ApiResponse(code = 404, message = "Patient profile picture to be retrieved not found."),
			@ApiResponse(code = 500, message = "Internal server error.")})
	@ResponseBody
	public ImageDetailsBean retrievePatientProfilePicture(
			@ApiParam(name="patientId", value="") 
			@RequestParam(value="patientId", required=false, defaultValue="0") int patientId)
	{ 
		try{
			Assert.notNull(patientId, "entity Id is not passed");

			String absolutePath = fileUploadAndRetrieveService.getSharedFolderPath()+"/photo/";
			Assert.notNull(absolutePath, "absolute path is empty");
			File imageFile=new File(absolutePath+patientId+".jpg");
			byte[] byteFileStore=new byte[(int) imageFile.length()];
			
			FileInputStream fileInputStream=new FileInputStream(imageFile);
			fileInputStream.read(byteFileStore);
			
			String encodedFile=Base64.encodeBase64String(byteFileStore);
			
			ImageDetailsBean imageBean=new ImageDetailsBean();
			
			imageBean.setImageBase64String(encodedFile);
			
			System.out.println("Image retrieved successfully!");
			
			return imageBean;

		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value="/retrieve/image/", method = RequestMethod.GET)
	@ApiOperation(value = "Retrieves user patient picture and returns base64 String format", notes = "Uploads user profile picture and returns the saved path.", response = User.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Patient profile picture retrieve successfull."),
			@ApiResponse(code = 404, message = "Patient profile picture to be retrieved not found."),
			@ApiResponse(code = 500, message = "Internal server error.")})
	@ResponseBody
	public String retrievePatientImageDocuments(@ApiParam(name="patientId", value="") @RequestParam(value="patientId", required=false, defaultValue="0") int patientId,
			@ApiParam(name="fileName", value="") @RequestParam(value="fileName", required=false, defaultValue="0") String fileName,
			@ApiParam(name="fileCategory", value="") @RequestParam(value="fileCategory", required=false, defaultValue="0") String fileCategory)
	{ 
		try{
			Assert.notNull(patientId, "patient Id is not passed");
			Assert.notNull(fileName, "fileName is not passed");
			Assert.notNull(fileCategory, "fileCategory is not passed");
			
			String absolutePath=null;
			
			if(fileCategory.equalsIgnoreCase("SharedDocument"))
				 absolutePath=fileUploadAndRetrieveService.getSharedFolderPath()+pathSeperator+"Attachments"+pathSeperator+patientId+pathSeperator+fileName;
			else if(fileCategory.equalsIgnoreCase("ProfilePicture"))
				 absolutePath=fileUploadAndRetrieveService.getSharedFolderPath()+pathSeperator+"photo"+pathSeperator+patientId+pathSeperator+fileName;
			
			Assert.notNull(absolutePath, "absolute path is empty");
			File imageFile=new File(absolutePath);
			byte[] byteFileStore=new byte[(int) imageFile.length()];
			
			FileInputStream fileInputStream=new FileInputStream(imageFile);
			fileInputStream.read(byteFileStore);
			
			String encodedFile=Base64.encodeBase64String(byteFileStore);
			System.out.println("Image retrieved successfully!");
			return encodedFile;

		}catch(Exception ex){
			ex.printStackTrace();
			return "<html>File retrieved successfully!</html>";
		}
	}
	
	@RequestMapping(value="/retrieve/html/", method = RequestMethod.GET)
	@ApiOperation(value = "Retrieves patient html document in String format.", notes = "Retrieves patient html document in String format.", response = User.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Patient profile picture retrieve successfull."),
			@ApiResponse(code = 404, message = "Patient profile picture to be retrieved not found."),
			@ApiResponse(code = 500, message = "Internal server error.")})
	@ResponseBody
	public String retrievePatientHTMLDocuments(@ApiParam(name="patientId", value="") @RequestParam(value="patientId", required=false, defaultValue="0") int patientId,
			@ApiParam(name="fileName", value="") @RequestParam(value="fileName", required=false, defaultValue="0") String fileName,
			@ApiParam(name="fileCategory", value="") @RequestParam(value="fileCategory", required=false, defaultValue="0") String fileCategory)
	{ 
		try{
			Assert.notNull(patientId, "patient Id is not passed");
			Assert.notNull(fileName, "fileName is not passed");
			Assert.notNull(fileCategory, "fileCategory is not passed");
			
			String absolutePath=null;
			
			if(fileCategory.equalsIgnoreCase("LABHTML"))
				 absolutePath=fileUploadAndRetrieveService.getSharedFolderPath()+pathSeperator+"Attachments"+pathSeperator+patientId+pathSeperator+fileName;
			
			
			Assert.notNull(absolutePath, "absolute path is empty");
			Document htmlDocument=Jsoup.parse(new File(absolutePath), "UTF-8");
			
			return htmlDocument.outerHtml();

		}catch(Exception ex){
			ex.printStackTrace();
			return "File not found.";
		}
	}
	
	
	@RequestMapping(value="/preview/file/{patientId}/{fileName}", method = RequestMethod.GET)
	@ApiOperation(value = "Retrieves user patient picture and returns base64 String format", notes = "Uploads user profile picture and returns the saved path.", response = User.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Patient profile picture retrieve successfull."),
			@ApiResponse(code = 404, message = "Patient profile picture to be retrieved not found."),
			@ApiResponse(code = 500, message = "Internal server error.")})
	@ResponseBody
	public void filePreviewByFileId(@ApiParam(name="patientId", value="") @PathVariable("patientId") int patientId,
			@ApiParam(name="fileName", value="") @PathVariable("fileName") String fileDetailsName,
			@ApiParam(name="isToDownload", value="") @RequestParam(value="isToDownload",required = false)String isToDownLoad,
			HttpServletRequest request,HttpServletResponse response)
	{ 
		try{
			
			 isToDownLoad = HUtil.Nz(isToDownLoad, "false");
			 
		        String filePath = fileUploadAndRetrieveService.getSharedFolderPath()+pathSeperator+"Attachments"+pathSeperator+patientId+pathSeperator+fileDetailsName+".pdf";
		        String fileName = fileUploadAndRetrieveService.parseFilePath(filePath);
		        String fileType = fileUploadAndRetrieveService.parseFileType(filePath);
		        if(isToDownLoad.equals("true")){

		            response.setContentType("application/pdf");   
		            response.setHeader("Content-Transfer-Encoding", "binary");
		            response.setHeader("Content-Disposition","inline; filename=\"" + fileName +"\"");
		        }
		        response.setContentType(fileUploadAndRetrieveService.getResponseContentType(fileType));
		        writeToResponse(filePath,response);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	@RequestMapping(value="/FilePreview/{patientId}/{fileCategory}/{fileName}", method = RequestMethod.GET)
	@ApiOperation(value = "Retrieves user patient picture and returns base64 String format", notes = "Uploads user profile picture and returns the saved path.", response = User.class)
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Patient profile picture retrieve successfull."),
			@ApiResponse(code = 404, message = "Patient profile picture to be retrieved not found."),
			@ApiResponse(code = 500, message = "Internal server error.")})
	@ResponseBody
	public void filePreviewByFileName(@ApiParam(name="patientId", value="") @PathVariable("patientId") int patientId,
			@ApiParam(name="fileCategory", value="") @PathVariable("fileCategory") String fileCategory,
			@ApiParam(name="fileName", value="") @PathVariable("fileName") String fileDetailsName,
			@ApiParam(name="isToDownload", value="") @RequestParam(value="isToDownload",required = false)String isToDownLoad,
			HttpServletRequest request,HttpServletResponse response)
	{ 
		try{
			 isToDownLoad = HUtil.Nz(isToDownLoad, "false");
			 
			 String categoryPath="";
			 if(fileCategory.equalsIgnoreCase("CDA"))
				 categoryPath=pathSeperator+"GlacePortal"+pathSeperator+"CDA"+pathSeperator+patientId+pathSeperator;
			 else if(fileCategory.equalsIgnoreCase("ENSSTMNTS"))
				 categoryPath=pathSeperator+"ENSSTMNTS"+pathSeperator;
			 else if(fileCategory.equalsIgnoreCase("LABPDF"))
				 categoryPath=pathSeperator+"HL7"+pathSeperator+"AttachedDocs"+pathSeperator;
			 else if(fileCategory.equalsIgnoreCase("documents"))
				 categoryPath=pathSeperator+"patientinfo"+pathSeperator+patientId+pathSeperator;
			 else if(fileCategory.equalsIgnoreCase("CONSENTFORM"))
				 categoryPath=pathSeperator+"Attachments"+pathSeperator+patientId+pathSeperator;
			 else if(fileCategory.equalsIgnoreCase("VIS"))
				 categoryPath=pathSeperator+"vismaterial"+pathSeperator;
			 
		        String filePath="";		        
		       
		        filePath = fileUploadAndRetrieveService.getSharedFolderPath()+categoryPath+fileDetailsName+".pdf";
		        
		        String fileName = fileUploadAndRetrieveService.parseFilePath(filePath);
		        String fileType = fileUploadAndRetrieveService.parseFileType(filePath);
		        if(isToDownLoad.equals("true")){

		            response.setContentType("application/pdf");   
		            response.setHeader("Content-Transfer-Encoding", "binary");
		            response.setHeader("Content-Disposition","inline; filename=\"" + fileName +"\"");
		        }
		        response.setContentType(fileUploadAndRetrieveService.getResponseContentType(fileType));
		        writeToResponse(filePath,response);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

    public void writeToResponse(String filePath, HttpServletResponse response)throws Exception{
        
        FileInputStream fin = null;
        BufferedInputStream bin = null;
        ServletOutputStream sos = null;
        try{
            fin = new FileInputStream(filePath);
            bin = new BufferedInputStream(fin);
            sos = response.getOutputStream();
            byte b[] = new byte[1024];
            int length;
            while((length=bin.read(b))!=-1){
                sos.write(b,0,length);
            }
            sos.flush();                
        }catch(Exception e){
            throw e;
        }finally{            
            if(sos!=null)sos.close();
            if(bin!=null)bin.close();
            if(fin!=null)fin.close();
        }

    }

}
