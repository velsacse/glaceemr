package com.glenwood.glaceemr.server.datasource;


public class TennantContextHolder {
	
	
   private static final ThreadLocal<String> contextHolder = 
            new ThreadLocal<String>();

   public static void setTennantId(String tennantId) {
    
      contextHolder.set(tennantId);
   }

   public static String getTennantId() {
      return (String) contextHolder.get();
   }

   public static void clearTennantId() {
      contextHolder.remove();
   }
}