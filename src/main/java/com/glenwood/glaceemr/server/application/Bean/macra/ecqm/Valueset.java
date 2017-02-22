package com.glenwood.glaceemr.server.application.Bean.macra.ecqm;

import java.util.ArrayList;
import java.util.List;

public class Valueset
{
	private String name = new String();
	private String oid = new String();
	private String QDMCategory = new String();
	private List<CodeSet> codeSetList = new ArrayList<CodeSet>();
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getOid()
	{
		return oid;
	}
	public void setOid(String oid)
	{
		this.oid = oid;
	}
	public String getQDMCategory()
	{
		return QDMCategory;
	}
	public void setQDMCategory(String qDMCategory)
	{
		QDMCategory = qDMCategory;
	}
	public List<CodeSet> getCodeSetList()
	{
		return codeSetList;
	}
	public void setCodeSetList(List<CodeSet> codeSetList)
	{
		this.codeSetList = codeSetList;
	}
	
	
	public CodeSet getCodeSetByOID(String OID){
		CodeSet codeSet = null;
		for(CodeSet tempCodeSet : this.getCodeSetList()){
			if(tempCodeSet.getCodeSystemOID().equals(OID)){
				codeSet=tempCodeSet;
				break;
			}
		}
		return codeSet;
	}

}
