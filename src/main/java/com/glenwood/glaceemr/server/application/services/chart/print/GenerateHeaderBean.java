package com.glenwood.glaceemr.server.application.services.chart.print;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.glenwood.glaceemr.server.application.models.print.GenericLetterHeader;
import com.glenwood.glaceemr.server.application.models.print.LetterHeaderContent;
import com.glenwood.glaceemr.server.application.models.print.PatientHeader;
import com.glenwood.glaceemr.server.application.models.print.PatientHeaderDetails;
import com.glenwood.glaceemr.server.application.services.chart.print.genericheader.LetterHeaderService;
import com.glenwood.glaceemr.server.application.services.chart.print.patientheader.PatientHeaderService;

@Component
public class GenerateHeaderBean {

	@Autowired
	LetterHeaderService letterHeaderService;

	@Autowired
	PatientHeaderService patientHeaderService;

	int attributeCount=3;

	public String generateHeader(int letterHeaderId){

		GenericLetterHeader genericLetterheader=letterHeaderService.getLetterHeaderDetails(letterHeaderId);
		List<LetterHeaderContent> letterHeaderContentList=letterHeaderService.getLetterHeaderContentList(letterHeaderId);		

		StringBuilder headerHTML=new StringBuilder();
		if(genericLetterheader.getGenericLetterHeaderType()==2){
			headerHTML.append("<table width='100%' cellpadding='1'>");
			for(int i=0;i<letterHeaderContentList.size();i++){
				headerHTML.append("<tr><td style='width:100%;text-align:center;"+generateStyleString(letterHeaderContentList.get(i).getLetterHeaderContentStyle())+"'>");
				if(letterHeaderContentList.get(i).getLetterHeaderContentVariant()==2){
					headerHTML.append(drawTextHeaderData(letterHeaderContentList.get(i),generateStyleString(letterHeaderContentList.get(i).getLetterHeaderContentStyle())));
				}else if(letterHeaderContentList.get(i).getLetterHeaderContentVariant()==3){
					headerHTML.append(letterHeaderContentList.get(i).getLetterHeaderContentCustom().replace("\n", "<br/>"));
				}else if(letterHeaderContentList.get(i).getLetterHeaderContentVariant()==4){
					if(letterHeaderContentList.get(i).getLetterHeaderContentFlag()!=4){
						headerHTML.append(drawAddressData(letterHeaderContentList.get(i),genericLetterheader.getGenericLetterHeaderAddress().toString()));
					}
				}
				headerHTML.append("</td></tr>");
			}
			headerHTML.append("</table>");
		}

		return headerHTML.toString();
	}

	public String drawTextHeaderData(LetterHeaderContent letterHeaderContent,String style){
		StringBuilder header=new StringBuilder();
		header.append("<table width='100%'>");
		String customText=letterHeaderContent.getLetterHeaderContentCustom().replace("\n", "<br/>");
		if(letterHeaderContent.getLetterHeaderContentFlag()==1){

			header.append("<tr><td style='"+style+"'>Practice Name</td></tr><tr><td style='"+style+"'>"+customText+"</td></tr>");
		}else if(letterHeaderContent.getLetterHeaderContentFlag()==2){

			header.append("<tr><td style='"+style+"'>Principal Doctor</td></tr><tr><td style='"+style+"'>"+customText+"</td></tr>");
		}else if(letterHeaderContent.getLetterHeaderContentFlag()==3){

			header.append("<tr><td style='"+style+"'>Service Doctor</td></tr><tr><td style='"+style+"'>"+customText+"</td></tr>");
		}else if(letterHeaderContent.getLetterHeaderContentFlag()==4){
			header.append("<tr><td style='"+style+"'>Practice Name 1</td><td style='"+style+"'>Practice Name 2</td><td style='"+style+"'>Practice Name 3</td></tr><tr><td colspan='3' style='"+style+"'>"+customText+"</td></tr>");
		}else if(letterHeaderContent.getLetterHeaderContentFlag()==5){
			header.append("<tr><td style='"+style+"'>Doctor Name 1</td><td style='"+style+"'>Doctor Name 2</td><td style='"+style+"'>Doctor Name 3</td></tr><tr><td colspan='3' style='"+style+"'>"+customText+"</td></tr>");
		}else if(letterHeaderContent.getLetterHeaderContentFlag()==6){
			header.append("<tr><td style='"+style+"'>"+customText+"</td></tr>");
		}
		header.append("</table>");
		return header.toString();
	}
	public String drawAddressData(LetterHeaderContent letterHeaderContent,String addressFormat ){
		StringBuilder address=new StringBuilder();
		address.append("<table width='100%'>");


		if((addressFormat).charAt(1)==1){
			if(letterHeaderContent.getLetterHeaderContentFlag()==1){
				address.append("<tr><td >Default Address</td></tr>");
			}else if(letterHeaderContent.getLetterHeaderContentFlag()==2){
				address.append("<tr><td>POS Address</td></tr>");
			}else if(letterHeaderContent.getLetterHeaderContentFlag()==3){
				
				address.append("<tr><td>Address 1</td><td>Address 2</td><td>Address 3</td></tr>");
				
			}
		}
		if((addressFormat).charAt(2)==1){
			if(letterHeaderContent.getLetterHeaderContentFlag()==3){
				
				address.append("<tr><td>Phone 1</td><td>Phone 2</td><td>Phone 3</td></tr>");
				
			}else{
				address.append("<tr><td>Phone</td></tr>");
			}
		}
		if((addressFormat).charAt(3)==1){
			if(letterHeaderContent.getLetterHeaderContentFlag()==3){
				
				address.append("<tr><td>Fax 1</td><td>Fax 2</td><td>Fax 3</td></tr>");
				
			}else{
				address.append("<tr><td>Fax</td></tr>");
			}

		}
		if((addressFormat).charAt(4)==1){
			if(letterHeaderContent.getLetterHeaderContentFlag()==3){
				
				address.append("<tr><td>e-mail 1</td><td>e-mail 2</td><td>e-mail 3</td></tr>");
				
			}else{
				address.append("<tr><td>e-mail</td></tr>");
			}

		}
		if((addressFormat).charAt(5)==1){
			if(letterHeaderContent.getLetterHeaderContentFlag()==3){
				
				address.append("<tr><td>Web portal 1</td><td>Web portal 2</td><td>Web portal 3</td></tr>");
				
			}else{
				address.append("<tr><td>Web portal</td></tr>");
			}

		}
		address.append("</table>");
		return address.toString();
	}
	public String generateStyleString(String styleData){
		StringBuilder style=new StringBuilder();
		if(!styleData.equals("")){

			String[] styleDataArr=styleData.split("#~#");
			if(styleDataArr.length==6){

				if(styleDataArr[0].equals("1")){
					style.append("font-weight:bold;");
				}
				if(styleDataArr[1].equals("1")){
					style.append("font-style:italic;");
				}
				if(styleDataArr[2].equals("1")){
					style.append("text-decoration:underline;");

				}
				style.append("font-family:"+styleDataArr[3]+";");
				style.append("font-size:"+styleDataArr[4]+";");
				style.append("color:#"+styleDataArr[5]+";");

			}
		}
		return style.toString();
	}

	public int getPatientHeaderType(int patientHeaderId){
		PatientHeader patientHeader=patientHeaderService.getPatientHeader(patientHeaderId);
		if(patientHeader!=null){
			return patientHeader.getPatientHeaderType();
		}
		return -1;
	}
	public String generatePatientHeader(int patientHeaderId,int pageId){
		List<PatientHeaderDetails> patientHeaderDetails=patientHeaderService.getPatientHeaderDetailList(patientHeaderId, pageId);

		return generatePatientHeaderHTML(patientHeaderDetails);
	}
	public String generatePatientHeader(int patientHeaderId,int pageId,String[] patientDetailsArr){
		List<PatientHeaderDetails> patientHeaderDetails=patientHeaderService.getPatientHeaderDetailList(patientHeaderId, pageId);

		return generatePatientHeaderHTML(patientHeaderDetails,patientDetailsArr);
	}
	public String generatePatientHeaderHTML(List<PatientHeaderDetails> patientHeaderDetails){
		String[] attributes = {"Patient Name", "Age", "DOS", "Gender","Account #","Phone #","DOB","Mobile #","Address","Referring Doctor","Insurance Details","Supervising doctor",
				"Service/Performing Doctor","Ethinicity","Race","Preffered/Primary Language","Care Group"};
		StringBuilder pHeaderHTML=new StringBuilder();


		int i=0;
		int count=0;
		if(patientHeaderDetails.size()>0){
			pHeaderHTML.append("<table style='border-collapse:collapse;' cellpadding='1' cellspacing='0' width='100%'><tr><td colspan='");
			pHeaderHTML.append(attributeCount);
			pHeaderHTML.append("' style='height:8px'><hr/></td></tr>");
			for(;i<patientHeaderDetails.size();i++){
				if(count%attributeCount==0){
					pHeaderHTML.append("<tr>");
				}
				pHeaderHTML.append("<td width='33%'><font style='font-weight:bold;font-size:12px;font-family:Arial;'>");
				pHeaderHTML.append(attributes[patientHeaderDetails.get(i).getComponentId()]);
				pHeaderHTML.append(":</font>&nbsp;<font style='color:#999;font-size:12px;font-family:Arial;'><i>&#60;");
				pHeaderHTML.append(attributes[patientHeaderDetails.get(i).getComponentId()]);
				pHeaderHTML.append("&#62;</i></font></td>");
				count++;
				if(count%attributeCount==0){
					pHeaderHTML.append("</tr>");
				}
			}
			if(count%attributeCount!=0){
				for(;count%attributeCount!=0;count++){
					pHeaderHTML.append("<td width='33%'></td>");
				}
				pHeaderHTML.append("</tr>");
			}
			pHeaderHTML.append("<tr><td colspan='");
			pHeaderHTML.append(attributeCount);
			pHeaderHTML.append("' style='height:8px'><hr/></td></tr>");
			pHeaderHTML.append("</table>");
		}

		return pHeaderHTML.toString();
	}
	
	public String generatePatientHeaderHTML(List<PatientHeaderDetails> patientHeaderDetails, String[] patientDetailsArr){
		String[] attributes = {"Patient Name", "Age", "DOS", "Gender","Account #","Phone #","DOB","Mobile #","Address","Referring Doctor","Insurance Details","Supervising doctor",
				"Service/Performing Doctor","Ethinicity","Race","Preffered/Primary Language","Care Group"};
		StringBuilder pHeaderHTML=new StringBuilder();


		int i=0;
		int count=0;
		if(patientHeaderDetails.size()>0){
			pHeaderHTML.append("<table style='border-collapse:collapse;' cellpadding='1' cellspacing='0' width='100%'><tr><td colspan='");
			pHeaderHTML.append(attributeCount);
			pHeaderHTML.append("' style='height:8px'><hr/></td></tr>");
			for(;i<patientHeaderDetails.size();i++){
				if(count%attributeCount==0){
					pHeaderHTML.append("<tr>");
				}
				pHeaderHTML.append("<td width='33%'><font style='font-weight:bold;font-size:12px;font-family:Arial;'>");
				pHeaderHTML.append(attributes[patientHeaderDetails.get(i).getComponentId()]);
				if(!patientDetailsArr[patientHeaderDetails.get(i).getComponentId()].equalsIgnoreCase("")){
					pHeaderHTML.append(":</font>&nbsp;<font style='font-size:12px;font-family:Arial;'>");
					pHeaderHTML.append(patientDetailsArr[patientHeaderDetails.get(i).getComponentId()]);
				}else{
					pHeaderHTML.append(":</font>&nbsp;<font style='color:#999;font-size:12px;font-family:Arial;'><i>&#60;");
					pHeaderHTML.append(attributes[patientHeaderDetails.get(i).getComponentId()]+"&#62;</i>");
				}
				pHeaderHTML.append("</font></td>");
				count++;
				if(count%attributeCount==0){
					pHeaderHTML.append("</tr>");
				}
			}
			if(count%attributeCount!=0){
				for(;count%attributeCount!=0;count++){
					pHeaderHTML.append("<td width='33%'></td>");
				}
				pHeaderHTML.append("</tr>");
			}
			pHeaderHTML.append("<tr><td colspan='");
			pHeaderHTML.append(attributeCount);
			pHeaderHTML.append("' style='height:8px'><hr/></td></tr>");
			pHeaderHTML.append("</table>");
		}

		return pHeaderHTML.toString();
	}
}
