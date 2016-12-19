package com.glenwood.glaceemr.server.application.services.Documents;

public class PDFFormTemplateBean {

	long templateId;
	String templateName;
	String templateFileName;
	int templateType;
	String templateThumbnail;
	public long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getTemplateFileName() {
		return templateFileName;
	}
	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}
	public int getTemplateType() {
		return templateType;
	}
	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}
	public String getTemplateThumbnail() {
		return templateThumbnail;
	}
	public void setTemplateThumbnail(String templateThumbnail) {
		this.templateThumbnail = templateThumbnail;
	}
	public boolean isTemplateIsactive() {
		return templateIsactive;
	}
	public void setTemplateIsactive(boolean templateIsactive) {
		this.templateIsactive = templateIsactive;
	}
	boolean templateIsactive;
	
}
