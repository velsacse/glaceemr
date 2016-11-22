package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.services.roomStatus.OrderedBean;
import com.glenwood.glaceemr.server.application.services.roomStatus.RoomInfoBean;
import com.glenwood.glaceemr.server.application.services.roomStatus.RoomStatusBean;
import com.glenwood.glaceemr.server.application.services.roomStatus.RoomStatusService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "RoomStatus", description = "To get the room status", consumes="application/json")
@RestController
@RequestMapping(value="/user/RoomStatus")
public class RoomStatusController {
	
	@Autowired
	RoomStatusService roomStatusService;
    
	/**
     * to get locations
     * @return locations
     * @throws Exception
     */
	@ApiOperation(value="to get Locations",notes="Get the locations")
	@RequestMapping(value="/getLocations",method=RequestMethod.GET)
	@ResponseBody 
	public EMRResponseBean getLocations() throws Exception{
		JSONObject loc = roomStatusService.getPos();
		EMRResponseBean locations = new EMRResponseBean();
		locations.setData(loc.toString().replace("\"", ""));
		return locations;
	}
	
	/**
	 * to get all the information about the room
	 * @param patientId
	 * @param encounterId
	 * @return responseBean
	 * @throws Exception
	 */
	@ApiOperation(value="to get room information",notes="Get the room information ")
	@RequestMapping(value="/getRoomInformation",method=RequestMethod.GET)
	@ResponseBody 
	public EMRResponseBean getPosDetails(@RequestParam(value="patientId",required=false)Integer patientId,@RequestParam(value="encounterId",required=false)Integer encounterId) throws Exception{
		JSONObject locations = roomStatusService.getPos();
		JSONObject providers = roomStatusService.getproviders();
		List<RoomStatusBean> roomStatus = roomStatusService.getRoomStatus();
		List<OrderedBean> ordered = roomStatusService.getOrdered(patientId);
		JSONObject roomDetail = roomStatusService.getRoomName();
		JSONObject activities = roomStatusService.getActivities(patientId,encounterId);
		RoomInfoBean roomInfo = new RoomInfoBean(locations.toString().replace("\"", ""),providers.toString().replace("\"", ""),roomStatus,ordered,roomDetail.toString().replace("\"", ""),activities.toString().replace("\"", ""));
		EMRResponseBean res = new EMRResponseBean();
		res.setData(roomInfo);
		return res;
	}
	
	/**
	 * to get list of todays patient data
	 * @param pos
	 * @return patientsData
	 * @throws Exception
	 */
	@ApiOperation(value="to get list of todays patients data ",notes="Get the list of todays patients data based on pos")
	@RequestMapping(value="/getTodaysPatientsdata",method=RequestMethod.GET)
	@ResponseBody 
	public EMRResponseBean getTodaysPatientsData(@RequestParam(value="pos",required=false)Integer pos) throws Exception{
		List<RoomStatusBean> patientsData = roomStatusService.getTodaysPatientsData(pos);
		EMRResponseBean patientData = new EMRResponseBean();
		patientData.setData(patientsData);
		return patientData;
	}
	
	/**
	 * to update room numbers
	 * @param pos
	 * @param addPatientId
	 * @param roomtoAdd
	 * @return roomNos
	 * @throws Exception
	 */
	@ApiOperation(value="to update room numbers",notes="update the room numbers")
	@RequestMapping(value="/updateRoomNo",method=RequestMethod.GET)
	@ResponseBody 
	public EMRResponseBean updateRoomNo(@RequestParam(value="pos",required=false)Integer pos,@RequestParam(value="addPatientId",required=false)Integer addPatientId,@RequestParam(value="roomtoAdd",required=false)Short roomtoAdd) throws Exception{
		List<Encounter> encounterList = roomStatusService.updateRoomNo(pos,addPatientId,roomtoAdd);
		EMRResponseBean roomNos = new EMRResponseBean();
		roomNos.setData(encounterList);
		return roomNos;
    }
	
	/**
	 * to swap patients within rooms
	 * @param oldRoom
	 * @param newRoom
	 * @param pos
	 * @return List<Encounter>
	 * @throws Exception
	 */
	@ApiOperation(value="to transfer patients",notes="to transfer the patients between rooms")
	@RequestMapping(value="/transferPatients",method=RequestMethod.GET)
	@ResponseBody 
	public EMRResponseBean updateTransferRooms(@RequestParam(value="toSwap",required=false)Short oldRoom,@RequestParam(value="withRoom",required=false)Short newRoom,@RequestParam(value="pos",required=false)Integer pos) throws Exception{
		List<Encounter> encounterList = roomStatusService.updateTransferRooms(oldRoom,newRoom,pos);
		EMRResponseBean transferPatient = new EMRResponseBean();
		transferPatient.setData(encounterList);
	    return transferPatient;
    }
	
	/**
	 * to transfer patients
	 * @param oldRoom
	 * @param newRoom
	 * @param pos
	 * @return List<Encounter>
	 * @throws Exception
	 */
	@ApiOperation(value="to swap rooms",notes="to swap patients within rooms")
	@RequestMapping(value="/swapPatients",method=RequestMethod.GET)
	@ResponseBody 
	public EMRResponseBean updateSwappatients(@RequestParam(value="toSwap",required=false)Short oldRoom,@RequestParam(value="withRoom",required=false)Short newRoom,@RequestParam(value="pos",required=false)Integer pos) throws Exception{
		List<Encounter> toswapPatients = roomStatusService.updateSwapPatients(oldRoom,newRoom,pos);
		EMRResponseBean swapPatient = new EMRResponseBean();
		swapPatient.setData(toswapPatients);
		return swapPatient;
    }
              
 }