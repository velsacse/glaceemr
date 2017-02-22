package com.glenwood.glaceemr.server.application.Bean.macra.ecqm;

import java.util.ArrayList;
import java.util.List;

public class CodeSet
{
	private String codeSystem = new String();
	private String codeSystemOID = new String();
	private String codeSystemVersion = new String();
	private String codeListString = new String();
	private List<Code> codeList = new ArrayList<Code>();
	public String getCodeSystem()
	{
		return codeSystem;
	}
	public void setCodeSystem(String codeSystem)
	{
		this.codeSystem = codeSystem;
	}
	public String getCodeSystemOID()
	{
		return codeSystemOID;
	}
	public void setCodeSystemOID(String codeSystemOID)
	{
		this.codeSystemOID = codeSystemOID;
	}
	public String getCodeSystemVersion()
	{
		return codeSystemVersion;
	}
	public void setCodeSystemVersion(String codeSystemVersion)
	{
		this.codeSystemVersion = codeSystemVersion;
	}
	public List<Code> getCodeList()
	{
		return codeList;
	}
	public void setCodeList(List<Code> codeList)
	{
		this.codeList = codeList;
	}
	public String getCodeListString()
	{
		return codeListString;
	}
	public void setCodeListString(String codeListString)
	{
		this.codeListString = codeListString;
	}
}
