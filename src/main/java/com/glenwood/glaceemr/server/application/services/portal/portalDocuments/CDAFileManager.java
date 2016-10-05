package com.glenwood.glaceemr.server.application.services.portal.portalDocuments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

//import org.apache.commons.codec.digest.DigestUtils;
//import java.


public class CDAFileManager {
	
    int xmlFileType=0;
	
    
    public static String processCDAFile(int xmlFileType, String fileName) throws Exception{
    	
    	String htmlCode="File Type Not Supported";
    	
    	if(xmlFileType==1){
    		
    		htmlCode=CDADocumentUtil.getHTMLCode(CDAFileManager.readCDAFile(SharedFolderUtil.getSharedFolderPath()+"CDAFiles/cda_Outbox/"+fileName).replaceAll("#~#","\r\n").getBytes(), SharedFolderUtil.getSharedFolderPath()+"PatientCDA/cda_modular_viewer.xsl");
    	}
    	if(xmlFileType==2){
    		
    		String xmlfile = SharedFolderUtil.getSharedFolderPath()+"CDAFiles/cda_Outbox/"+fileName;
    		String xslfile = new File("CCR.xsl").getAbsolutePath();
    		CDAFileManager.xmlTranfomation(xmlfile,xslfile);
    	}
    	
    	return htmlCode;
  
    }
    

	public static void xmlTranfomation(String xmldata,String xslobj)throws Exception{
		StreamSource source = new StreamSource(new StringReader(CDAFileManager.readCDAFile(xmldata).replaceAll("#~#","\r\n")));
		StreamSource stylesource = new StreamSource(new File(xslobj));
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(stylesource);
		PrintWriter out = null;
		StreamResult result = new StreamResult(out);
		transformer.transform(source, result);
	}

	public static String readCDAFile(String filepath)throws Exception{
		FileReader inputFileReader   = null;
		BufferedReader inputStream   = null;
		StringWriter writer = null;
		try{
			inputFileReader   = new FileReader(filepath);
			inputStream   = new BufferedReader(inputFileReader);
			writer = new StringWriter();
			String inLine = null;
			while ((inLine = inputStream.readLine()) != null) {
				writer.write(inLine);
			}
		}catch(Exception e){
			throw e;
		}finally{
			if(writer!=null)writer.close();
			if(inputStream!=null)inputStream.close();
		}		
		return writer.toString();
	}

}