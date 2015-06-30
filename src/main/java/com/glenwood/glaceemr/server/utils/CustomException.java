package com.glenwood.glaceemr.server.utils;

import org.apache.log4j.NDC;

public class CustomException extends Exception{
	
	public CustomException(String... values)
	{
		for (String string : values) {
			NDC.push(string+"@Sperator@");
		}
	}
	
	public CustomException(String controller,String mappingMethodValue,String... values)
	{
		NDC.push("Controller : "+controller+"@Sperator@");
		NDC.push("mappingMethodValue : "+mappingMethodValue+"@Sperator@");
		for (String string : values) {
			NDC.push("Param :"+string+"@Sperator@");
		}
	}

}
