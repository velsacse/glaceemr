package com.glenwood.glaceemr.server.application.services.portal.portalDocuments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ExportHTMLAsPdf {

	public PortalFileDetailBean getCDAPdfDetails(String htmlCode, int patientId, int encounterId, String requestMode) throws Exception{

		PortalFileDetailBean fileDeatilBean=new PortalFileDetailBean();

		String	sharedFolderPath=SharedFolderUtil.getSharedFolderPath();
		String pathSeperator	=	java.io.File.separator;


		String reportsRootPathStr=sharedFolderPath+"GlacePortal"+pathSeperator;

		File reportsRootPath = new File(reportsRootPathStr);
		if (!reportsRootPath.exists()) {
			if (reportsRootPath.mkdir()) {

			} else {

			}
		}

		String reportsRootCDAPathStr=reportsRootPathStr+"CDA"+pathSeperator;

		File reportsRootCDAPath = new File(reportsRootCDAPathStr);
		if (!reportsRootCDAPath.exists()) {
			if (reportsRootCDAPath.mkdir()) {

			} else {

			}
		}


		String reportsSavePathStr=reportsRootCDAPathStr+patientId+pathSeperator;

		File reportsSavePath = new File(reportsSavePathStr);
		if (!reportsSavePath.exists()) {
			if (reportsSavePath.mkdir()) {

			} else {

			}
		}

		if(requestMode.equalsIgnoreCase("exportAsPdf"))
		{

			String templateFile=reportsSavePathStr+"CDATemplate.html";

			File template = new File(templateFile);
			if (!template.exists()) {
				new FileOutputStream(reportsSavePathStr+"CDATemplate.html", false).close();
			}


			Document html=Jsoup.parse(new File(templateFile), "UTF-8");

			BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(templateFile), "UTF-8"));
			html.getElementsByTag("html").remove();
			html.append(htmlCode);
			htmlWriter.write(html.toString());
			htmlWriter.flush();
			htmlWriter.close();


			String fileName= pdfConverter(reportsSavePathStr, templateFile, "PatientCDA_"+patientId+"_"+encounterId+".pdf");

			fileDeatilBean.setFileName(fileName);
			fileDeatilBean.setFilePath(reportsSavePathStr);
			fileDeatilBean.setFileNameWithLocation(reportsSavePathStr+fileName);
		}

		return fileDeatilBean;

	}


	String paramname = "successfully pdf generated";  	
	public String  pdfConverter(String destinationpath,String htmlfilePath, String fileName){

		String pathSeperator	=	java.io.File.separator;
		try{		

			String destnormal=destinationpath+pathSeperator+fileName;
			Process p2 = Runtime.getRuntime().exec(pathSeperator+"usr"+pathSeperator+"bin"+pathSeperator+"wkhtmltopdf -L 0.5cm -R 0.5cm -T 0.5cm -B 0.5cm "+ htmlfilePath.trim()+" "+destnormal);
			new File(destnormal);
			BufferedReader stdInput2 = new BufferedReader(new  InputStreamReader(p2.getInputStream()));
			BufferedReader stdError2 = new BufferedReader(new InputStreamReader(p2.getErrorStream()));
			while ((stdInput2.readLine()) != null) {

				//return "Error";
			}
			while ((stdError2.readLine()) != null) {

				// return "Error";
			}
			return fileName;

		}catch(Exception e){
			paramname="Please install Wkhtmltopdf";
			return "Error";
		}

	}

}
