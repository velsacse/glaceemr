package com.glenwood.glaceemr.server.application.Bean.macra.ecqm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Category
{
	private String category = new String();
	private List<Valueset> valueSet = new ArrayList<Valueset>();
	private HashMap<String,String> codeList = new HashMap<String, String>();
	
	public String getCategory()
	{
		return category;
	}
	public void setCategory(String category)
	{
		this.category = category;
	}
	public List<Valueset> getValueSet()
	{
		return valueSet;
	}
	public void setValueSet(List<Valueset> valueSet)
	{
		this.valueSet = valueSet;
	}
	public HashMap<String,String> getCodeList()
	{
		return codeList;
	}
	public void setCodeList(HashMap<String,String> codeList)
	{
		this.codeList = codeList;
	}

	public Valueset getValueSetByOID(String OID){
		Valueset valueSet = null;
		for(Valueset tempValueSet : this.valueSet){
			if(tempValueSet.getOid().equals(OID)){
				valueSet=tempValueSet;
				break;
			}
		}
		return valueSet;
	}
	
	
	
	
}
