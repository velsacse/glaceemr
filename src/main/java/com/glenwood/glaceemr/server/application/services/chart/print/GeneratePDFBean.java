package com.glenwood.glaceemr.server.application.services.chart.print;


import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;

@Component
public class GeneratePDFBean {
	
	
	public void generatePDF(String headerHTML,String patientHeaderPage1HTML,String patientHeaderHTML,String footerHTML,int pageVariant) throws DocumentException, IOException{
		Document document = new Document();
		
		PdfWriter writer= PdfWriter.getInstance(document, new FileOutputStream("/home/software/Print.pdf"));
		
		Rectangle rect = new Rectangle(30, 30, 550, 800);
		writer.setBoxSize("art", rect);
		//use string builder
		System.out.println("header:"+headerHTML);
		System.out.println("footer:"+footerHTML);
		HTMLHeaderFooter header=new HTMLHeaderFooter(patientHeaderHTML, footerHTML,pageVariant,0,PageSize.LETTER);
		writer.setPageEvent(header);
		document.setPageSize(PageSize.LETTER);
		ElementList headerEList=XMLWorkerHelper.parseToElementList(headerHTML, null);
		ElementList pheaderEList=XMLWorkerHelper.parseToElementList(patientHeaderPage1HTML, null);
		document.open();
		//document.setPageSize(PageSize.LETTER.rotate());
		if(writer.getPageNumber()==1){
			for (Element e : headerEList) {
				document.add(e);
			}
			for (Element e : pheaderEList) {
				document.add(e);
			}
		}
		document.add(new Paragraph("Content"));
		document.newPage();
		document.add(new Paragraph("\n\n\n\n\n\n\n\nContent"));
		document.newPage();
		document.add(new Paragraph("\n\n\n\n\n\n\n\nContent"));
		// step 5


		document.close();
		System.out.println("after closing:::::::");
	}
	public void generatePDF(String headerHTML,String patientHeaderPage1HTML,String patientHeaderHTML,
			String footerHTML,int pageVariant,Rectangle pdfPageSize,int pageOrientation) throws DocumentException, IOException{
		Document  document=  new Document();
		
		PdfWriter writer= PdfWriter.getInstance(document, new FileOutputStream("/home/software/Print.pdf"));
		
		
		//use string builder
		System.out.println("header:"+headerHTML);
		System.out.println("footer:"+footerHTML);
		HTMLHeaderFooter header=new HTMLHeaderFooter(patientHeaderHTML, footerHTML,pageVariant,
				pageOrientation,pdfPageSize);
		writer.setPageEvent(header);
		if(pageOrientation==1){
			document.setPageSize(pdfPageSize.rotate());
		}else{
			document.setPageSize(pdfPageSize);
		}
		ElementList headerEList=XMLWorkerHelper.parseToElementList(headerHTML, null);
		ElementList pheaderEList=XMLWorkerHelper.parseToElementList(patientHeaderPage1HTML, null);
		document.open();
		if(writer.getPageNumber()==1){
			for (Element e : headerEList) {
				document.add(e);
			}
			for (Element e : pheaderEList) {
				document.add(e);
			}
		}
		document.add(new Paragraph("\n\n\n\n\n\n\n\nContent"));
		document.add(new Paragraph("\n\n\n\n\n\n\n\nContent"));
		document.add(new Paragraph("\n\n\n\n\n\n\n\nContent"));
		document.newPage();
		document.add(new Paragraph("\n\n\n\n\n\n\n\nContent"));
		document.newPage();
		document.add(new Paragraph("\n\n\n\n\n\n\n\nContent"));
		// step 5


		document.close();
		System.out.println("after closing222222222::::::::");
		
	}
}
