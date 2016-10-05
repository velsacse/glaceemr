package com.glenwood.glaceemr.server.application.services.fileUploadAndRetrieval;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadAndRetrievalService {

	public String getSharedFolderPath();

	public String getFileNameOnCurrentDateAndTime(String originalFileName);

	public String parseFilePath(String file_path);

	public String parseFileType(String file_path);

	public String getAbsoluteFilePath(int patientId,int fileId);

	//public String getAbsoluteFilePath();

	//public String getFileNameOnCurrentDate(String fileName);

	public String getResponseContentType(String fileType);

	//public String readImageToBase64(int fileId, int height, int width);

	//public String encodeImage(byte[] imageByteArray);

	//public byte[] decodeImage(String imageDataString);

	//public String getSavingPath(int entSpfcId, String absolutePath,int folderMapId);

	//public boolean convertTextToPDF(File file1) throws IOException;

	//public boolean convertXLSXToPDF(File file) throws Exception;

	//public boolean savePreviewPDF(File filetoBeSaved, String absolutePath) throws Exception;

	//public boolean saveThumbnail(File fileToBeSaved, String absolutePath);

	//public Date parseDate(String pattern, String expDate);

	//public boolean saveToDisk(MultipartFile file, String path) throws IOException;

	//public boolean copyFileUsingFileChannels(File source, File dest) throws IOException;

	//public String copyFileName(String actualName);

}
