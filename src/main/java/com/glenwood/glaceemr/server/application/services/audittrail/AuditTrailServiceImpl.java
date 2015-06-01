package com.glenwood.glaceemr.server.application.services.audittrail;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

@Service
public class AuditTrailServiceImpl implements AuditTrailService{

	@Override
	public int LogEvent(int Log_Type, int Log_Component, int Event_Type,
			int parent_Event, int Event_Outcome, String Description,
			int User_Id, String SystemIP, String ClientIP, int patientId,
			int chartId, int encounterId, int LoginType,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int LogEvent(int Log_Type, int Log_Component, int Event_Type,
			int parent_Event, int Event_Outcome, String Description,
			int User_Id, String SystemIP, String ClientIP, int patientId,
			int chartId, int encounterId, int LoginType,
			HttpServletRequest request, String newDescription) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int LogEventExternal(int Log_Type, int Log_Component,
			int Event_Type, int parent_Event, int Event_Outcome,
			String Description, int User_Id, String SystemIP, String ClientIP,
			int patientId, int chartId, int encounterId, int LoginType,
			HttpServletRequest request, String newDescription) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int LogEvent(int Log_Type, int Log_Component, int Event_Type,
			int parent_Event, int Event_Outcome, String Description,
			int User_Id, String SystemIP, String ClientIP, int patientId,
			int chartId, int encounterId, int LoginType, String newDescription,
			Vector<Object> logmodules, String parent_event_session) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int LogEvent(int Log_Type, int Log_Component, int Event_Type,
			int parent_Event, int Event_Outcome, String Description,
			int User_Id, String SystemIP, String ClientIP, int patientId,
			int chartId, int encounterId, int LoginType, String newDescription,
			Vector<Object> logmodules, String parent_event_session,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int LogEvent(int Log_Type, int Log_Component, int Event_Type,
			int parent_Event, int Event_Outcome, String Description,
			int User_Id, String SystemIP, String ClientIP, int patientId,
			int chartId, int encounterId, int LoginType, int Event_Id,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int LogSession(int parent_Event, int Outcome, String description,
			String SystemIP, String ClientIP, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int LogEvent(int Log_Type, int Log_Component, int Event_Type,
			int parent_Event, int Event_Outcome, String Description,
			int User_Id, String ClientIP, int patientId, int chartId,
			int encounterId, int LoginType) {
		// TODO Auto-generated method stub
		return 0;
	}

}
