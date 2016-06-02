package com.glenwood.glaceemr.server.application.services.chart.print;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.glenwood.glaceemr.server.application.models.LetterHeaderEmp;
import com.glenwood.glaceemr.server.application.models.LetterHeaderPos;
import com.glenwood.glaceemr.server.application.models.print.GenericLetterHeader;
import com.glenwood.glaceemr.server.application.models.print.LetterHeaderContent;
import com.glenwood.glaceemr.server.application.models.print.PatientHeader;
import com.glenwood.glaceemr.server.application.models.print.PatientHeaderDetails;
import com.glenwood.glaceemr.server.application.services.chart.print.genericheader.LetterHeaderService;
import com.glenwood.glaceemr.server.application.services.chart.print.patientheader.PatientHeaderService;
import com.glenwood.glaceemr.server.application.services.employee.EmployeeDataBean;
import com.glenwood.glaceemr.server.application.services.patient.PatientDataBean;
import com.glenwood.glaceemr.server.application.services.pos.DefaultPracticeBean;
import com.glenwood.glaceemr.server.application.services.pos.PosDataBean;

@Component
public class GenerateHeaderBean {

	@Autowired
	LetterHeaderService letterHeaderService;

	@Autowired
	PatientHeaderService patientHeaderService;

	@Autowired
	GenericPrintService genericPrintService;
	
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
	
	public String generateHeader(int letterHeaderId, String shareFolderPath, GenericPrintBean genericPrintBean) throws Exception{
		StringBuilder headerHTML=new StringBuilder();
		try{
			GenericLetterHeader genericLetterheader=letterHeaderService.getLetterHeaderDetails(letterHeaderId);
			List<LetterHeaderContent> letterHeaderContentList=letterHeaderService.getLetterHeaderContentList(letterHeaderId);		

			if(genericLetterheader.getGenericLetterHeaderType()==2){					// Text only header
				headerHTML.append(getTextOnlyHeaderHTML(genericLetterheader, letterHeaderContentList, genericPrintBean));
			}

			else if(genericLetterheader.getGenericLetterHeaderType()==5) {				// Custom header
				for(int i=0;i<letterHeaderContentList.size();i++){
					if(letterHeaderContentList.get(i).getLetterHeaderContentVariant()==8){
						headerHTML.append(letterHeaderContentList.get(i).getLetterHeaderContentCustom());
					}
				}
			}

			else if(genericLetterheader.getGenericLetterHeaderType()==1) {				// Image only header
				for(int i=0;i<letterHeaderContentList.size();i++){
					if(letterHeaderContentList.get(i).getLetterHeaderContentVariant()==1){

						String styleStr= letterHeaderContentList.get(i).getLetterHeaderContentStyle();
						String[] styleList = styleStr.split("#~#");
						String height = "", width = "";
						if(styleList.length > 0){
							if(styleList[0] != null && !styleList[0].trim().isEmpty() && !styleList[0].trim().equalsIgnoreCase("-"))
								height = styleList[0]+"px";
							if(styleList.length > 1)
								width = styleList[1]+"px";
						}

						int alignFlag = letterHeaderContentList.get(i).getLetterHeaderContentFlag();
						String align= "center";
						switch(alignFlag) {
						case 0: align= "center"; break;
						case 1: align= "left";   break;
						case 2: align= "right";  break;
						case 3: height= "100%"; width="100%"; break;
						default: align="center";
						}

						if(styleList.length > 0){
							headerHTML.append("<div style='width:100%; text-align:"+align+"'><img id='headerImg0' name='headerImg0' src='"+shareFolderPath+"/"+letterHeaderContentList.get(i).getLetterHeaderContentCustom()+"' height='"+height+"' width='"+width+"' /></div>");
						}
						else {
							headerHTML.append("<div style='width:100%; text-align:"+align+"'><img id='headerImg0' name='headerImg0' src='"+shareFolderPath+"/"+letterHeaderContentList.get(i).getLetterHeaderContentCustom()+"'/></div>");
						}
					}
				}
			}

			else if(genericLetterheader.getGenericLetterHeaderType()==3){				// Text with Image on sides header
				headerHTML.append("<table width='100%' cellpadding='1'>");
				headerHTML.append("<tr>");
				for(int i=0;i<letterHeaderContentList.size();i++){
					if(letterHeaderContentList.get(i).getLetterHeaderContentVariant()==1){
						if(letterHeaderContentList.get(i).getLetterHeaderContentFlag()==1){
							headerHTML.append("<td>"+getGenericImage(letterHeaderContentList.get(i),shareFolderPath,"float:left;")+"</td>");
							break;
						}
					}
				}

				headerHTML.append("<td width='100%'>"+getTextOnlyHeaderHTML(genericLetterheader, letterHeaderContentList, genericPrintBean)+"</td>");

				for(int i=0;i<letterHeaderContentList.size();i++){
					if(letterHeaderContentList.get(i).getLetterHeaderContentVariant()==1){
						if(letterHeaderContentList.get(i).getLetterHeaderContentFlag()==2){
							headerHTML.append("<td>"+getGenericImage(letterHeaderContentList.get(i),shareFolderPath,"float:right;")+"</td>");
							break;
						}
					}
				}	
				headerHTML.append("</tr></table>");
			}
			else if(genericLetterheader.getGenericLetterHeaderType()==4) {				// Logo with two headers
				headerHTML.append("<table width='100%' cellpadding='1'>");
				headerHTML.append("<tr>");
				for(int i=0;i<letterHeaderContentList.size();i++){
					if(letterHeaderContentList.get(i).getLetterHeaderContentVariant() ==1 && letterHeaderContentList.get(i).getLetterHeaderContentFlag()==1 && letterHeaderContentList.get(i).getLetterHeaderContentCustom()!=""){
						headerHTML.append("<td><div align='left' style='align-items: left;width:100%;'>"+getGenericImage(letterHeaderContentList.get(i), shareFolderPath, letterHeaderContentList.get(i).getLetterHeaderContentStyle())+"</div></td>");
					}
				}
				headerHTML.append("<td style='width:80%'>");
				headerHTML.append(getTextOnlyHeaderHTML(genericLetterheader, letterHeaderContentList, genericPrintBean));
				headerHTML.append("<hr/>");
				headerHTML.append(getTextOnlyHeaderHTML2(genericLetterheader, letterHeaderContentList, genericPrintBean));
				headerHTML.append("</td>");
				headerHTML.append("</tr>");
				headerHTML.append("</table>");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return "unable to fetch header details";
		}
		return headerHTML.toString();
	}

	private String getGenericImage(LetterHeaderContent letterHeaderContent, String shareFolderPath, String styleStr) {
		
		StringBuffer imageHTML = new StringBuffer();
		int flag = letterHeaderContent.getLetterHeaderContentFlag();
		String style = getGenericImageStyle(letterHeaderContent);
		imageHTML.append("<img src='"+shareFolderPath+"/"+ letterHeaderContent.getLetterHeaderContentCustom()+"' id='headerImg"+flag+"' name='headerImg"+flag+"' style='"+styleStr+style+"'/>");
		return imageHTML.toString();
	}

	private String getGenericImageStyle(LetterHeaderContent letterHeaderContent) {

		StringBuffer imageStyleHTML = new StringBuffer();
		String styleStr = letterHeaderContent.getLetterHeaderContentStyle();
		String[] styleList = styleStr.split("#~#");
		if(styleList.length > 0){
			if(styleList[0] != null && !styleList[0].trim().isEmpty() && !styleList[0].trim().equalsIgnoreCase("-"))
				imageStyleHTML.append("height:"+styleList[0]+"px;");
			if(styleList.length > 1)
				imageStyleHTML.append("width:"+styleList[1]+"px;");
		}

		return imageStyleHTML.toString();
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
	
	public String drawTextHeaderData(LetterHeaderContent letterHeaderContent,String style, GenericPrintBean genericPrintBean, List<LetterHeaderPos> posList, List<LetterHeaderEmp> empList){
		try{
			StringBuilder header=new StringBuilder();
			header.append("<table width='100%' style='text-align:center;'>");
			String customText=letterHeaderContent.getLetterHeaderContentCustom().replace("\n", "<br/>");
			if(letterHeaderContent.getLetterHeaderContentFlag()==1){	
				DefaultPracticeBean practiceBean = genericPrintBean.getPracticeBean();
				if(practiceBean != null && practiceBean.getPracticeName() != null && !practiceBean.getPracticeName().trim().isEmpty())
					header.append("<tr><td style='"+style+"'>"+practiceBean.getPracticeName()+"</td>");
				if(customText != null && !customText.trim().isEmpty())
					header.append("</tr><tr><td style='"+style+"'>"+customText+"</td></tr>");
			}else if(letterHeaderContent.getLetterHeaderContentFlag()==2){
				PatientDataBean patientBean = genericPrintBean.getPatientBean();
				String principalDr = "";
				if(patientBean != null){
					principalDr = patientBean.getPrincipalDr().getEmpFullname();
					header.append("<tr><td style='"+style+"'>"+principalDr+"</td></tr>");
				}
				if(customText != null && !customText.trim().isEmpty())
					header.append("</tr><tr><td style='"+style+"'>"+customText+"</td></tr>");
			}else if(letterHeaderContent.getLetterHeaderContentFlag()==3){
				PatientDataBean patientBean = genericPrintBean.getPatientBean();
				String serviceDr = "";
				if(patientBean != null){
					serviceDr = patientBean.getServiceDr().getEmpFullname();
					header.append("<tr><td style='"+style+"'>"+serviceDr+"</td></tr>");
				}
				if(customText != null && !customText.trim().isEmpty())
					header.append("</tr><tr><td style='"+style+"'>"+customText+"</td></tr>");
			}else if(letterHeaderContent.getLetterHeaderContentFlag()==4){

				if(posList != null){
					header.append("<tr>");
					float width= getWidth(posList.size());
					for(int i=0; i< posList.size(); i++){
						PosDataBean posBean = genericPrintService.parsePOSDetail(posList.get(i).getPosTable());
						if(posBean != null){
							header.append("<td style='width:"+width+"%;"+style+"'>"+posBean.getPosName()+"</td>");
						}
					}
					header.append("</tr>");
				}
				//				header.append("<tr><td style='"+style+"'>Practice Name 1</td><td style='"+style+"'>Practice Name 2</td><td style='"+style+"'>Practice Name 3</td></tr><tr><td colspan='3' style='"+style+"'>"+customText+"</td></tr>");
			}else if(letterHeaderContent.getLetterHeaderContentFlag()==5){
				
				if(empList != null) {
					header.append("<tr>");
					float width= getWidth(empList.size());
					for(int i=0; i< empList.size(); i++){
						EmployeeDataBean empBean = genericPrintService.parseEmployeeDetail(empList.get(i).getEmpProfile());
						if(empBean != null){
							header.append("<td style='width:"+width+"%;"+style+"'>"+empBean.getEmpFullname()+"</td>");
						}
					}
					header.append("</tr>");
				}
				
				//			header.append("<tr><td style='"+style+"'>Doctor Name 1</td><td style='"+style+"'>Doctor Name 2</td><td style='"+style+"'>Doctor Name 3</td></tr><tr><td colspan='3' style='"+style+"'>"+customText+"</td></tr>");
			}else if(letterHeaderContent.getLetterHeaderContentFlag()==6){
				header.append("<tr><td style='"+style+"'>"+customText+"</td></tr>");
			}
			header.append("</table>");
			return header.toString();
		}catch(Exception e){
			return "";
		}
	}
	
	public String drawAddressData(LetterHeaderContent letterHeaderContent,String addressFormat, String style, GenericPrintBean genericPrintBean, List<LetterHeaderPos> posList, List<LetterHeaderEmp> empList, int headerFlag) throws Exception{
		
		List<String> addressList = new ArrayList<String>();
		List<String> phoneList = new ArrayList<String>();
		List<String> faxList = new ArrayList<String>();
		List<String> mailList = new ArrayList<String>();
		
		addressList.clear();
		phoneList.clear();
		faxList.clear();
		mailList.clear();
		
		if((addressFormat).charAt(1)=='1' && letterHeaderContent.getLetterHeaderContentFlag()==3) {
			if(headerFlag==2){				     // Principal Doctor Address
				EmployeeDataBean empBean =  genericPrintBean.getPatientBean().getPrincipalDr();				
				if(empBean != null){

					addressList.add(parseAddress(empBean.getEmpAddress(), empBean.getEmpCity(), empBean.getEmpState(), empBean.getEmpZip()));
					if(empBean.getEmpPhNum() != null && !empBean.getEmpPhNum().isEmpty())
						phoneList.add(empBean.getEmpPhNum());
					if(empBean.getEmpMailId() != null && !empBean.getEmpMailId().isEmpty())
						mailList.add(empBean.getEmpMailId());
					
				}
			}
			if(headerFlag==3){					// Service Docor Address
				EmployeeDataBean empBean =  genericPrintBean.getPatientBean().getServiceDr();				
				if(empBean != null){
					addressList.add(parseAddress(empBean.getEmpAddress(), empBean.getEmpCity(), empBean.getEmpState(), empBean.getEmpZip()));
					if(empBean.getEmpPhNum() != null && !empBean.getEmpPhNum().isEmpty())
						phoneList.add(empBean.getEmpPhNum());
					if(empBean.getEmpMailId() != null && !empBean.getEmpMailId().isEmpty())
						mailList.add(empBean.getEmpMailId());
				}
			}
			if(headerFlag==4){
				for(int i=0; i< posList.size(); i++){
					PosDataBean posBean = genericPrintService.parsePOSDetail(posList.get(i).getPosTable());
					addressList.add(parseAddress(posBean.getPosAddress(),posBean.getPosCity(),posBean.getPosState(),posBean.getPosZip()));
					phoneList.add(posBean.getPosPhNum());
					faxList.add(posBean.getPosFaxNum());
				}
			}
			if(headerFlag==5){
				for(int i=0; i< empList.size(); i++){
					EmployeeDataBean empBean = genericPrintService.parseEmployeeDetail(empList.get(i).getEmpProfile());
					addressList.add(parseAddress(empBean.getEmpAddress(),empBean.getEmpCity(),empBean.getEmpState(),empBean.getEmpZip()));
					phoneList.add(empBean.getEmpPhNum());
					mailList.add(empBean.getEmpMailId());
				}
			}
		}
		StringBuilder address=new StringBuilder();
		address.append("<table width='100%' style='text-align: center;'>");

		if((addressFormat).charAt(1)=='1'){
			if(letterHeaderContent.getLetterHeaderContentFlag()==1){
				
				DefaultPracticeBean practiceBean = genericPrintBean.getPracticeBean();
				if(practiceBean != null){
					String addressSecondLine = "";
					if(practiceBean.getPracticeCity() !=  null && !practiceBean.getPracticeCity().isEmpty())
						addressSecondLine = practiceBean.getPracticeCity().trim()+", "; 
					if(practiceBean.getPracticeState() != null && !practiceBean.getPracticeState().isEmpty())
						addressSecondLine = addressSecondLine + practiceBean.getPracticeState().trim() +" ";
					if(practiceBean.getPracticeZip() != null && !practiceBean.getPracticeZip().isEmpty())
						addressSecondLine = addressSecondLine + practiceBean.getPracticeZip().trim();
					if(practiceBean.getPracticeStreet() != null && !practiceBean.getPracticeStreet().isEmpty())
						address.append("<tr><td style='"+style+"'>"+practiceBean.getPracticeStreet().trim()+"</td></tr>");
					if(!addressSecondLine.trim().isEmpty())
						address.append("<tr><td style='"+style+"'>"+addressSecondLine+"</td></tr>");
				}
				
			}else if(letterHeaderContent.getLetterHeaderContentFlag()==2){
				
				PatientDataBean patientBean = genericPrintBean.getPatientBean();
				if(patientBean != null){
					List<PosDataBean> posBean = patientBean.getPosDetails();
					if(posBean != null && posBean.size()>0){
						String addressSecondLine = "";
						if(posBean.get(0).getPosCity() !=  null && !posBean.get(0).getPosCity().isEmpty())
							addressSecondLine = posBean.get(0).getPosCity().trim()+", "; 
						if(posBean.get(0).getPosState() != null && !posBean.get(0).getPosState().isEmpty())
							addressSecondLine = addressSecondLine + posBean.get(0).getPosState().trim() +" ";
						if(posBean.get(0).getPosZip() != null && !posBean.get(0).getPosZip().isEmpty())
							addressSecondLine = addressSecondLine + posBean.get(0).getPosZip().trim();
						if(posBean.get(0).getPosAddress() != null && !posBean.get(0).getPosAddress().isEmpty())
							address.append("<tr><td style='"+style+"'>"+posBean.get(0).getPosAddress().trim()+"</td></tr>");
						if(!addressSecondLine.trim().isEmpty())
							address.append("<tr><td style='"+style+"'>"+addressSecondLine+"</td></tr>");
					}
				}
				
			}else if(letterHeaderContent.getLetterHeaderContentFlag()==3){

				address.append("<tr>");				
				for(int i=0; i< addressList.size(); i++)
					if(!addressList.get(i).trim().isEmpty())
						address.append("<td style='"+style+"'>"+addressList.get(i)+"</td>");
				address.append("</tr>");
			}
		}
		if((addressFormat).charAt(2)=='1'){
			
			if(letterHeaderContent.getLetterHeaderContentFlag()==1){
				DefaultPracticeBean practiceBean = genericPrintBean.getPracticeBean();
				if(practiceBean != null){
					if(practiceBean.getPracticePhoneNum() != null && !practiceBean.getPracticePhoneNum().trim().isEmpty()){
						address.append("<tr>");
						address.append("<td style='"+style+"'>Phone: "+practiceBean.getPracticePhoneNum()+"</td>");
						address.append("</tr>");
					}
				}
					
			}
			else if(letterHeaderContent.getLetterHeaderContentFlag()==2){
				PatientDataBean patientBean = genericPrintBean.getPatientBean();
				if(patientBean != null){
					List<PosDataBean> posBean = patientBean.getPosDetails();
					if(posBean != null && posBean.size()>0){
						if(posBean.get(0).getPosPhNum() != null && !posBean.get(0).getPosPhNum().isEmpty()){
							address.append("<tr>");
							address.append("<td style='"+style+"'>Phone: "+posBean.get(0).getPosPhNum()+"</td>");
							address.append("</tr>");
						}
					}
				}
			}
			else if(letterHeaderContent.getLetterHeaderContentFlag()==3){
				
				address.append("<tr>");
				float width= getWidth(phoneList.size());
				for(int i=0; i< phoneList.size(); i++)
					if(!phoneList.get(i).trim().isEmpty())
						address.append("<td style='width:"+width+"%;"+style+"'>Phone: "+phoneList.get(i)+"</td>");
				address.append("</tr>");
				
			}
		}
		if((addressFormat).charAt(3)=='1'){

			if(letterHeaderContent.getLetterHeaderContentFlag()==1){
				DefaultPracticeBean practiceBean = genericPrintBean.getPracticeBean();
				if(practiceBean != null){
					if(practiceBean.getPracticeFaxNum() != null && !practiceBean.getPracticeFaxNum().trim().isEmpty()){
						address.append("<tr>");
						address.append("<td style='"+style+"'>Fax: "+practiceBean.getPracticeFaxNum()+"</td>");
						address.append("</tr>");
					}
				}
					
			}
			else if(letterHeaderContent.getLetterHeaderContentFlag()==2){
				PatientDataBean patientBean = genericPrintBean.getPatientBean();
				if(patientBean != null){
					List<PosDataBean> posBean = patientBean.getPosDetails();
					if(posBean != null && posBean.size()>0){
						if(posBean.get(0).getPosFaxNum() != null && !posBean.get(0).getPosFaxNum().isEmpty()){
							address.append("<tr>");
							address.append("<td style='"+style+"'>Fax: "+posBean.get(0).getPosFaxNum()+"</td>");
							address.append("</tr>");
						}
					}
				}
			}
			else if(letterHeaderContent.getLetterHeaderContentFlag()==3){
					address.append("<tr>");
					float width= getWidth(faxList.size());
					for(int i=0; i<faxList.size(); i++){
						if(!faxList.get(i).trim().isEmpty())
							address.append("<td style='width:"+width+"%;"+style+"'>Fax: "+faxList.get(i)+"</td>");
					}
					address.append("</tr>");
			}

		}
		if((addressFormat).charAt(4)=='1'){
			
			if(letterHeaderContent.getLetterHeaderContentFlag()==1){
				DefaultPracticeBean practiceBean = genericPrintBean.getPracticeBean();
				if(practiceBean != null){
					if(practiceBean.getPracticeEmail() != null && !practiceBean.getPracticeEmail().trim().isEmpty()){
						address.append("<tr>");
						address.append("<td style='"+style+"'>Email: "+practiceBean.getPracticeEmail()+"</td>");
						address.append("</tr>");
					}
				}
			}
			else if(letterHeaderContent.getLetterHeaderContentFlag()==2){
				// Encounter POS don't have Email
			}
			else if(letterHeaderContent.getLetterHeaderContentFlag()==3){
					address.append("<tr>");
					float width= getWidth(mailList.size());
					for(int i=0; i<mailList.size(); i++){
						if(!mailList.get(i).trim().isEmpty())
							address.append("<td style='width:"+width+"%;"+style+"'>Email: "+mailList.get(i)+"</td>");
					}
					address.append("</tr>");
			}

		}
		if((addressFormat).charAt(5)=='1'){

			if(letterHeaderContent.getLetterHeaderContentFlag()==1){
				DefaultPracticeBean practiceBean = genericPrintBean.getPracticeBean();
				if(practiceBean != null){
					if(practiceBean.getPracticeWebAddress() != null && !practiceBean.getPracticeWebAddress().trim().isEmpty()){
						address.append("<tr>");
						address.append("<td style='"+style+"'>Web: "+practiceBean.getPracticeWebAddress()+"</td>");
						address.append("</tr>");
					}
				}
			}
			else if(letterHeaderContent.getLetterHeaderContentFlag()==2){
				// Encounter POS don't have Web address
			}
			else if(letterHeaderContent.getLetterHeaderContentFlag()==3){
				// don't have Web address
			}

		}
		address.append("</table>");
		return address.toString();
	}
	
	private String parseAddress(String address, String city,String state, String zip) {
		
		String addressSecondLine = "";
		String finalAddress = "";
		if(city !=  null && !city.isEmpty())
			addressSecondLine = city.trim()+", "; 
		if(state != null && !state.isEmpty())
			addressSecondLine = addressSecondLine + state.trim() +" ";
		if(zip != null && !zip.isEmpty())
			addressSecondLine = addressSecondLine + zip.trim();
		if(address != null && !address.isEmpty())
			finalAddress = address.trim();
		if(!addressSecondLine.trim().isEmpty())
			finalAddress = finalAddress + "<br/>" + addressSecondLine.trim();
		
		return finalAddress;
	}

	public String drawAddressData(LetterHeaderContent letterHeaderContent,String addressFormat ){
		StringBuilder address=new StringBuilder();
		address.append("<table width='100%'>");

		if((addressFormat).charAt(1)=='1'){
			if(letterHeaderContent.getLetterHeaderContentFlag()==1){
				address.append("<tr><td >Default Address</td></tr>");
			}else if(letterHeaderContent.getLetterHeaderContentFlag()==2){
				address.append("<tr><td>POS Address</td></tr>");
			}else if(letterHeaderContent.getLetterHeaderContentFlag()==3){
				
				address.append("<tr><td>Address 1</td><td>Address 2</td><td>Address 3</td></tr>");
				
			}
		}
		if((addressFormat).charAt(2)=='1'){
			if(letterHeaderContent.getLetterHeaderContentFlag()==3){
				
				address.append("<tr><td>Phone 1</td><td>Phone 2</td><td>Phone 3</td></tr>");
				
			}else{
				address.append("<tr><td>Phone</td></tr>");
			}
		}
		if((addressFormat).charAt(3)=='1'){
			if(letterHeaderContent.getLetterHeaderContentFlag()==3){
				
				address.append("<tr><td>Fax 1</td><td>Fax 2</td><td>Fax 3</td></tr>");
				
			}else{
				address.append("<tr><td>Fax</td></tr>");
			}

		}
		if((addressFormat).charAt(4)=='1'){
			if(letterHeaderContent.getLetterHeaderContentFlag()==3){
				
				address.append("<tr><td>e-mail 1</td><td>e-mail 2</td><td>e-mail 3</td></tr>");
				
			}else{
				address.append("<tr><td>e-mail</td></tr>");
			}

		}
		if((addressFormat).charAt(5)=='1'){
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
	
	private Integer getHeaderFlag(List<LetterHeaderContent> letterHeaderContentList, int variantId) {
		
		for(int i=0; i<letterHeaderContentList.size(); i++){
			if(letterHeaderContentList.get(i).getLetterHeaderContentVariant()==variantId)
				return letterHeaderContentList.get(i).getLetterHeaderContentFlag();
		}
		return -1;
	}
	
	public float getWidth(int size){
		
		float width;
		try{
			width = 100/size;
		}catch(Exception e){
			width = 0;
		}
		return width;
	}
	
	public String getTextOnlyHeaderHTML(GenericLetterHeader genericLetterheader, List<LetterHeaderContent> letterHeaderContentList, GenericPrintBean genericPrintBean) throws Exception{
		
		StringBuffer headerHTML = new StringBuffer();
		headerHTML.append("<table width='100%' cellpadding='1'>");
		List<LetterHeaderPos> posList = letterHeaderService.fetchLetterHeaderPOSList(genericLetterheader.getGenericLetterHeaderId(), 1);
		List<LetterHeaderEmp> empList = letterHeaderService.fetchLetterHeaderEmpList(genericLetterheader.getGenericLetterHeaderId(), 1);

//		List<InitialSettings> initialList= initialSettingsRepository.findAll(Specifications.where(InitialSettingsSpecification.optionType(4)).and(InitialSettingsSpecification.optionVisible(true)));
		for(int i=0;i<letterHeaderContentList.size();i++){
			headerHTML.append("<tr><td style='width:100%;text-align:center;"+generateStyleString(letterHeaderContentList.get(i).getLetterHeaderContentStyle())+"'>");
			if(letterHeaderContentList.get(i).getLetterHeaderContentVariant()==2){
				headerHTML.append(drawTextHeaderData(letterHeaderContentList.get(i),generateStyleString(letterHeaderContentList.get(i).getLetterHeaderContentStyle()),genericPrintBean,posList,empList));
			}else if(letterHeaderContentList.get(i).getLetterHeaderContentVariant()==3){
				headerHTML.append(letterHeaderContentList.get(i).getLetterHeaderContentCustom().replace("\n", "<br/>"));
			}else if(letterHeaderContentList.get(i).getLetterHeaderContentVariant()==4){
				if(letterHeaderContentList.get(i).getLetterHeaderContentFlag()!=4){
					headerHTML.append(drawAddressData(letterHeaderContentList.get(i),genericLetterheader.getGenericLetterHeaderAddress().toString(),generateStyleString(letterHeaderContentList.get(i).getLetterHeaderContentStyle()),genericPrintBean,posList,empList,getHeaderFlag(letterHeaderContentList,2)));
				}
			}
			headerHTML.append("</td></tr>");
		}
		headerHTML.append("</table>");
		
		return headerHTML.toString();
	}
	
public String getTextOnlyHeaderHTML2(GenericLetterHeader genericLetterheader, List<LetterHeaderContent> letterHeaderContentList, GenericPrintBean genericPrintBean) throws Exception{
		
		StringBuffer headerHTML = new StringBuffer();
		headerHTML.append("<table width='100%' cellpadding='1'>");
		List<LetterHeaderPos> posList = letterHeaderService.fetchLetterHeaderPOSList(genericLetterheader.getGenericLetterHeaderId(), 1);
		List<LetterHeaderEmp> empList = letterHeaderService.fetchLetterHeaderEmpList(genericLetterheader.getGenericLetterHeaderId(), 1);
		for(int i=0;i<letterHeaderContentList.size();i++){
			headerHTML.append("<tr><td style='width:100%;text-align:center;"+generateStyleString(letterHeaderContentList.get(i).getLetterHeaderContentStyle())+"'>");
			if(letterHeaderContentList.get(i).getLetterHeaderContentVariant()==5){
				headerHTML.append(drawTextHeaderData(letterHeaderContentList.get(i),generateStyleString(letterHeaderContentList.get(i).getLetterHeaderContentStyle()),genericPrintBean,posList,empList));
			}else if(letterHeaderContentList.get(i).getLetterHeaderContentVariant()==6){
				headerHTML.append(letterHeaderContentList.get(i).getLetterHeaderContentCustom().replace("\n", "<br/>"));
			}else if(letterHeaderContentList.get(i).getLetterHeaderContentVariant()==7){
				if(letterHeaderContentList.get(i).getLetterHeaderContentFlag()!=4){
					headerHTML.append(drawAddressData(letterHeaderContentList.get(i),genericLetterheader.getGenericLetterHeaderAddress().toString(),generateStyleString(letterHeaderContentList.get(i).getLetterHeaderContentStyle()),genericPrintBean,posList,empList,getHeaderFlag(letterHeaderContentList,5)));
				}
			}
			headerHTML.append("</td></tr>");
		}
		headerHTML.append("</table>");
		
		return headerHTML.toString();
	}
}
