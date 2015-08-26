/**
 * 
 */
package com.glenwood.glaceemr.server.utils;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class SessionMap implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int userID;
	String dbName;

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	
}
