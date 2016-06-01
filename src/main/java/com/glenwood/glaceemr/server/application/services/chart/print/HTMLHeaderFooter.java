package com.glenwood.glaceemr.server.application.services.chart.print;

import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;



public class HTMLHeaderFooter extends PdfPageEventHelper {
	
	protected ElementList footer;
	
	protected ElementList patientHeader;
	int pageVariant;
	int pagenumber=0;
	float height=612;
	float width=792;
	PdfTemplate total;
	
	public HTMLHeaderFooter(String patientHeaderHTML, String footerHTML,
			int pageVariant,int pageOrientation,Rectangle pageSize) throws IOException {
		
		
//		System.out.println("footer:"+footerHTML);
		
		footer = XMLWorkerHelper.parseToElementList(footerHTML, null);
		
		patientHeader=XMLWorkerHelper.parseToElementList(patientHeaderHTML, null);
		this.pageVariant=pageVariant;
		if(pageOrientation==0){
			this.height=pageSize.getHeight();
			this.width=pageSize.getWidth();
		}else{
			this.height=pageSize.getWidth();
			this.width=pageSize.getHeight();
		}
	}
	public void onOpenDocument(PdfWriter writer, Document document) {
		total = writer.getDirectContent().createTemplate(30, 16);
	}
	public void onStartPage(PdfWriter writer, Document document) {
		pagenumber++;
	}
	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		try {
//			System.out.println("height:"+height+";width:"+width);
			Rectangle rect = new Rectangle(30, 30, width, height);

			ColumnText ct = new ColumnText(writer.getDirectContent());
			ct.setSimpleColumn(Float.parseFloat("30.0"), Float.parseFloat("30.0"), 
					width-40, height-20);

			if(writer.getPageNumber()!=1){
				
				for (Element e : patientHeader) {
					ct.addElement(e);
				}
			}
			ct.go();
			ct.setSimpleColumn(36, 20, width-10, 50);
			for (Element e : footer) {
				ct.addElement(e);
			}
			ct.go();
			ColumnText ctFooter = new ColumnText(writer.getDirectContent());
			
			if(pageVariant==1){
				ColumnText.showTextAligned(writer.getDirectContent(),
						Element.ALIGN_LEFT, new Phrase(String.format("%d", writer.getPageNumber()),new Font(Font.FontFamily.TIMES_ROMAN, 12)),
								rect.getLeft() ,
								rect.getBottom() - 18, 0);
			}else if(pageVariant==2){
				ColumnText.showTextAligned(writer.getDirectContent(),
						Element.ALIGN_CENTER, new Phrase(
								String.format("%d",  writer.getPageNumber()),new Font(Font.FontFamily.TIMES_ROMAN, 12)),
								(rect.getLeft() + rect.getRight()) / 2,
								rect.getBottom() - 18, 0);
			}else if(pageVariant==3){
				ColumnText.showTextAligned(writer.getDirectContent(),
						Element.ALIGN_RIGHT, new Phrase(
								String.format("%d", writer.getPageNumber()),new Font(Font.FontFamily.TIMES_ROMAN, 12)),
								rect.getRight()-5,
								rect.getBottom() - 18, 0);
			}else if(pageVariant==4){
				ColumnText.showTextAligned(writer.getDirectContent(),
						Element.ALIGN_LEFT, new Phrase(
								String.format("Page %d", writer.getPageNumber()),new Font(Font.FontFamily.TIMES_ROMAN, 12)),
								rect.getLeft() ,
								rect.getBottom() - 18, 0);
			}else if(pageVariant==5){
				ColumnText.showTextAligned(writer.getDirectContent(),
						Element.ALIGN_CENTER, new Phrase(
								String.format("Page %d", writer.getPageNumber()),new Font(Font.FontFamily.TIMES_ROMAN, 12)),
								(rect.getLeft() + rect.getRight()) / 2,
								rect.getBottom() - 18, 0);
			}else if(pageVariant==6){
				ColumnText.showTextAligned(writer.getDirectContent(),
						Element.ALIGN_RIGHT, new Phrase(
								String.format("Page %d", writer.getPageNumber()),new Font(Font.FontFamily.TIMES_ROMAN, 12)),
								rect.getRight()-8,
								rect.getBottom() - 18, 0);
			}else if(pageVariant==7){
				ColumnText.showTextAligned(writer.getDirectContent(),
						Element.ALIGN_LEFT, new Phrase(
								String.format("Page %d of 3", writer.getPageNumber()),new Font(Font.FontFamily.TIMES_ROMAN, 12)),
								rect.getLeft() ,
								rect.getBottom() - 18, 0);
				Image.getInstance(total);
			}else if(pageVariant==8){
				ColumnText.showTextAligned(writer.getDirectContent(),
						Element.ALIGN_CENTER, new Phrase(
								String.format("Page %d of 3", writer.getPageNumber()),new Font(Font.FontFamily.TIMES_ROMAN, 12)),
								(rect.getLeft() + rect.getRight()) / 2,
								rect.getBottom() - 18, 0);
				Image.getInstance(total);
			}else if(pageVariant==9){
				ColumnText.showTextAligned(writer.getDirectContent(),
						Element.ALIGN_RIGHT, new Phrase(
								String.format("Page %d of 3", writer.getPageNumber()),new Font(Font.FontFamily.TIMES_ROMAN, 12)),
								rect.getRight()-15,
								rect.getBottom() - 18, 0);
				ctFooter.addElement(Image.getInstance(total)); 
				
			}


		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}

	public void onCloseDocument(PdfWriter writer, Document document) {
		if(total!=null){
			ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
					new Phrase(String.valueOf(writer.getPageNumber() - 1)), 2, 2, 0);
		}
	}


}
