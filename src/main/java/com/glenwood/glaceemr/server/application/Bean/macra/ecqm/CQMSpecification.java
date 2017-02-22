package com.glenwood.glaceemr.server.application.Bean.macra.ecqm;

import java.util.HashMap;

public class CQMSpecification
{
	private String cmsId = new String();
	private String nqfNumber = new String();
	private HashMap<String, Category> qdmCategory = new HashMap<String, Category>();
	public String getCmsId()
	{
		return cmsId;
	}
	public void setCmsId(String cmsId)
	{
		this.cmsId = cmsId;
	}
	public String getNqfNumber()
	{
		return nqfNumber;
	}
	public void setNqfNumber(String nqfNumber)
	{
		this.nqfNumber = nqfNumber;
	}
	public HashMap<String, Category> getQdmCategory()
	{
		return qdmCategory;
	}
	public void setQdmCategory(HashMap<String, Category> qdmCategory)
	{
		this.qdmCategory = qdmCategory;
	}
}
