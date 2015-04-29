package com.glenwood.glaceemr.server.datasource;



import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


public class TennantAwareDataSource extends AbstractRoutingDataSource {
	
	/*@Autowired
	private HttpSession session;*/
	
	@Override
	protected Object determineCurrentLookupKey() {
		
		
		/*if(session.getAttribute("databasename") == null ||session.getAttribute("databasename") == " "||session.getAttribute("databasename") == "") 
//			return "database1";
			return TennantContextHolder.getTennantId();
		else{
			System.out.println("in tennetaware"+session.getAttribute("databasename").toString());
			TennantContextHolder.setTennantId(session.getAttribute("databasename").toString());
		}*/
			

//		return session.getAttribute("databasename");
		

		return TennantContextHolder.getTennantId();
			
		
}
	
	
	
	
	
}