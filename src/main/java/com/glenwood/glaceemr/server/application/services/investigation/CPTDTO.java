package com.glenwood.glaceemr.server.application.services.investigation;


public class CPTDTO{
	
	//member variables
	public String cptcode;
	public String description;
	public String shortcutdesc;
	
	public long id;
	public int cpttype;
	public int groupid;
	public int orderby;
	public int hitcount;
		
	public float rvu;
	public float mcallowed;
	public float cost;
	
	public boolean isactive;
	
	//constructors
	private CPTDTO(){}
	
	public static CPTDTO getInstance(long id, String cptcode, String description){
		CPTDTO cpt        = new CPTDTO();
		cpt.id            = id;
		cpt.cptcode       = cptcode;
		cpt.description   = description;
		cpt.shortcutdesc  = null;
		cpt.rvu           = 0;
		cpt.mcallowed     = 0;
		cpt.cost          = 0;
		cpt.isactive      = true;
		cpt.cpttype       = 1;
		cpt.groupid       = 1;
		cpt.orderby       = 1;
		cpt.hitcount      = 1;
		return cpt;
	}
	
	public static CPTDTO getInstance(long id, String cptcode, String description, String shortcutdesc, int cpttype, float cost, float mcallowed, float rvu, int groupid, int orderby, int hitcount, boolean isactive){
		CPTDTO cpt        = new CPTDTO(); 
		cpt.id            = id;
		cpt.cptcode       = cptcode;
		cpt.description   = description;
		cpt.shortcutdesc  = shortcutdesc;
		cpt.rvu           = (rvu<=0)?0:rvu;
		cpt.mcallowed     = (mcallowed<=0)?0:mcallowed;
		cpt.cost          = (cost<=0)?0:cost;
		cpt.isactive      = isactive;
		cpt.cpttype       = (cpttype<=0)?0:cpttype;
		cpt.groupid       = (groupid<=0)?0:groupid;
		cpt.orderby       = (orderby<=0)?0:orderby;
		cpt.hitcount      = (hitcount<=0)?0:hitcount;
		return cpt;
	}

	//getters and setters methods
	public long getID(){
		return this.id;
	}
	
	public String getCPTCode(){
		return this.cptcode;
	}
	
	public void setCPTCode(String cptcode){
		this.cptcode=cptcode;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public String getShort_Cut_Desc(){
		return this.shortcutdesc;
	}
	
	public void setShort_Cut_Desc(String shortcutdesc){
		this.shortcutdesc=shortcutdesc;
	}
	
	public int getCPTType(){
		return this.cpttype;
	}
	
	public void setCPTType(int cpttype){
		this.cpttype=cpttype;
	}
	
	public float getCost(){
		return this.cost;
	}
	
	public void setCost(float cost){
		this.cost=cost;
	}
	
	public float getMCAllowed(){
		return this.mcallowed;
	}
	
	public void setMCAllowed(float mcallowed){
		this.mcallowed=mcallowed;
	}
	
	public float getRVU(){
		return this.rvu;
	}
	
	public void setRVU(float rvu){
		this.rvu=rvu;
	}
	
	public int getGroupID(){
		return this.groupid;
	}
	
	public void setGroupID(int groupid){
		this.groupid=groupid;
	}
	
	public int getOrderBy(){
		return this.orderby;
	}
	
	public void setOrderBy(int orderby){
		this.orderby=orderby;
	}
	
	public int getHitCount(){
		return this.hitcount;
	}
	
	public void setHitCount(int hitcount){
		this.hitcount=hitcount;
	}
	
	public boolean getIsActive(){
		return this.isactive;
	}
	
	public void setIsActive(boolean isactive){
		this.isactive=isactive;
	}
}
