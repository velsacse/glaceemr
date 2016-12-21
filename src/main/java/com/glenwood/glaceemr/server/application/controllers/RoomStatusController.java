package com.glenwood.glaceemr.server.application.controllers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.Room;
import com.glenwood.glaceemr.server.application.services.roomStatus.ActivitiesData;
import com.glenwood.glaceemr.server.application.services.roomStatus.OrderedData;
import com.glenwood.glaceemr.server.application.services.roomStatus.PosRooms;
import com.glenwood.glaceemr.server.application.services.roomStatus.RoomInfoBean;
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
	public EMRResponseBean getLocations(@RequestParam(value="pos",required=false)Integer pos) throws Exception{
		List<PosTable> loc = roomStatusService.getPos(pos);
		EMRResponseBean locations = new EMRResponseBean();
		locations.setData(loc);
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
	public EMRResponseBean getPosDetails(@RequestParam(value="pos",required=false)Integer pos) throws Exception{
	    List<OrderedData> ordered=null;
		List<ActivitiesData> activities=null; 
		List<PosTable> locations = roomStatusService.getPos(pos);
		List<EmployeeProfile> providers = roomStatusService.getproviders();
		List<PosRooms> roomStatus = roomStatusService.getRoomStatus();
		List<String> patientId=new ArrayList<String>();
		for(int i=0;i<roomStatus.size();i++){
				patientId.add(roomStatus.get(i).getPatId().toString());
				activities = roomStatusService.getActivities(patientId);
				ordered = roomStatusService.getOrdered(patientId);
			}
		List<Room> roomDetail = roomStatusService.getRoomName();
		RoomInfoBean roomInfo = new RoomInfoBean(locations,providers,roomStatus,ordered,roomDetail,activities);
		EMRResponseBean roomInformation = new EMRResponseBean();
		roomInformation.setData(roomInfo);
		return roomInformation;
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
		List<PosRooms> patientsData = roomStatusService.getTodaysPatientsData(pos);
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
	public EMRResponseBean updateRoomNo(@RequestParam(value="pos",required=false)Integer pos,@RequestParam(value="addPatientId",required=false) String addPatientId,@RequestParam(value="roomtoAdd",required=false)Short roomtoAdd) throws Exception{
		JSONObject encounterList = roomStatusService.updateRoomNo(pos,addPatientId,roomtoAdd);
		EMRResponseBean roomNos = new EMRResponseBean();
		roomNos.setData(encounterList.toString());
		return roomNos;
    }
	
	/**
	 * to transfer patients
	 * @param oldRoom
	 * @param newRoom
	 * @param pos
	 * @return List<Encounter>
	 * @throws Exception
	 */
	@ApiOperation(value="to transfer patients",notes="to transfer the patients between rooms")
	@RequestMapping(value="/transferPatients",method=RequestMethod.GET)
	@ResponseBody 
	public EMRResponseBean updateTransferRooms(@RequestParam(value="toswap",required=false)Short toswap,@RequestParam(value="withroom",required=false)Short withroom,@RequestParam(value="pos",required=false)Integer pos) throws Exception{
		JSONObject transferPat=roomStatusService.updateTransferRooms(toswap,withroom,pos);
		EMRResponseBean transferPatient = new EMRResponseBean();
		transferPatient.setData(transferPat.toString());
	    return transferPatient;
    }
	
	/**
	 * to swap patients within rooms
	 * @param oldRoom
	 * @param newRoom
	 * @param pos
	 * @throws Exception
	 */
	@ApiOperation(value="to swap rooms",notes="to swap patients within rooms")
	@RequestMapping(value="/swapPatients",method=RequestMethod.GET)
	@ResponseBody 
	public EMRResponseBean updateSwappatients(@RequestParam(value="toswap",required=false)Short toswap,@RequestParam(value="withroom",required=false)Short withroom,@RequestParam(value="pos",required=false)Integer pos) throws Exception{
		JSONObject swapPatients = roomStatusService.updateSwapPatients(toswap,withroom,pos);
		EMRResponseBean swapPatient = new EMRResponseBean();
		swapPatient.setData(swapPatients.toString());
		return swapPatient;
    }
              
 }