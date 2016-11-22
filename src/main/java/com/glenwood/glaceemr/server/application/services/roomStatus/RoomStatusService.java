package com.glenwood.glaceemr.server.application.services.roomStatus;

import java.util.List;

import org.json.JSONObject;

import com.glenwood.glaceemr.server.application.models.Encounter;

public interface RoomStatusService {
   
	/**
    * to get pos
	* @param pos 
    * @return list of pos
    */
	JSONObject getPos();
	
	/**
	 * to get providers
	 * @return list of providers
	 */
	JSONObject getproviders();
	
	/**
	 * to get patientsData
	 * @param pos
	 * @return list of patientsData
	 */
	List<RoomStatusBean> getTodaysPatientsData(Integer pos);
	
	/**
	 * to get roomName
	 * @return list of rooms
	 */
	JSONObject getRoomName();
	
	/**
	 * to updateRoomNo
	 * @param pos
	 * @param addPatientId
	 * @param roomtoAdd
	 * @return list of encounter details
	 */
	List<Encounter> updateRoomNo(Integer pos, Integer addPatientId,Short roomtoAdd);
	
	/**
	 * to get RoomStatus
	 * @return list of room status
	 */
	List<RoomStatusBean> getRoomStatus();
	
	/**
	 * to transfer patients 
	 * @param toSwap
	 * @param withroom
	 * @param pos
	 * @return
	 */
	List<Encounter> updateTransferRooms(Short oldRoom, Short newRoom,Integer pos);
	
	/**
	 * to swap patients between rooms
	 * @param toSwap
	 * @param withroom
	 * @param pos
	 * @return list of encounter details after swapping patients
	 */
	List<Encounter> updateSwapPatients(Short oldRoom,Short newRoom,Integer pos);
	
	/**
	 * to get ordered information
	 * @param patientId
	 * @return patientId
	 */
	List<OrderedBean> getOrdered(Integer patientId);
	
	/**
	 * to get activities information
	 * @param patientId
	 * @param encounterId
	 * @return activities
	 */
	JSONObject getActivities(Integer patientId, Integer encounterId);
	

}