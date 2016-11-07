package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.Admission;
import com.glenwood.glaceemr.server.application.models.AdmissionRoom;
import com.glenwood.glaceemr.server.application.models.Encounter;
import com.glenwood.glaceemr.server.application.services.chart.admission.AdmissionBean;
import com.glenwood.glaceemr.server.application.services.chart.admission.AdmissionLeafBean;
import com.glenwood.glaceemr.server.application.services.chart.admission.AdmissionService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;


@Api(value = "Admission", description = "Admission", consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="/user/Admission")
public class AdmissionController {


	@Autowired
	AdmissionService admissionService;

	/**
	 * To Create and update Admission details
	 * @param dataJson
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveAdmission",method=RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean createAdmission(@RequestBody AdmissionBean dataJson) throws Exception{
		dataJson = getAdmissionBeanData(dataJson);
		String data= admissionService.saveAdmission(dataJson);
		EMRResponseBean dataToSave=new EMRResponseBean();
		dataToSave.setData(data);
		return dataToSave;
	}
	
	/**
	 * To Get open admission details
	 * @param patientId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getAdmission",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getAdmission(@RequestParam(value="patientId",required=false, defaultValue="") Integer patientId) throws Exception{
		Admission admission= admissionService.getAdmission(patientId);
	    EMRResponseBean emrResponseBean=new EMRResponseBean();
	    emrResponseBean.setData(admission);
	    return emrResponseBean;
	}
	
	
	/**
	 * To Discharge patient (closing admission)
	 * @param patientId
	 * @param loginId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/dischargePatient",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean dischargePatient(@RequestParam(value="patientId",required=false, defaultValue="") Integer patientId,
			   @RequestParam(value="loginId",required=false, defaultValue="") Integer loginId,						 
			   @RequestParam(value="userId",required=false, defaultValue="") Integer userId) throws Exception{
       String discharge= admissionService.dischargePatient(patientId,loginId,userId);
       EMRResponseBean dischargePatient=new EMRResponseBean();
       dischargePatient.setData(discharge);
       return dischargePatient;
    }

	
	/**
	 * To Get selected past Admission details
	 * @param admissionId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getPastAdmission",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getPastAdmission(@RequestParam(value="admissionId",required=false, defaultValue="") Integer admissionId) throws Exception{
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(admissionService.getPastAdmission(admissionId));
		return respBean;
	}
	
	/**
	 * To Get recently closed Admission details
	 * @param patientId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getAdmissionPast",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getPastAdmissionPast(@RequestParam(value="patientId",required=false, defaultValue="") Integer patientId) throws Exception{
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(admissionService.getAdmissionPast(patientId));
		return respBean;
	}
	
	/**
	 * To Get past admission dates
	 * @param patientId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getPastAdmissionDates",method=RequestMethod.GET)
	public EMRResponseBean getPastAdmissionDates(@RequestParam(value="patientId",required=false, defaultValue="") Integer patientId) throws Exception{
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData(admissionService.getPastAdmissionDates(patientId));
		return respBean;
	}
	
	/**
	 * To Get clinical notes
	 * @param encounterId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getLeafDetails",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getLeafDetails(@RequestParam(value="encounterId",required=false, defaultValue="") Integer encounterId,
			  @RequestParam(value="userId",required=false, defaultValue="") Integer userId) throws Exception{
        List<Admission> leafDetails=admissionService.getLeafDetails(encounterId,userId);
        EMRResponseBean getLeafDetails=new EMRResponseBean();
        getLeafDetails.setData(leafDetails);
        return getLeafDetails;
      }
	
	/**
	 * To Get encounter list corresponding to Episode
	 * @param admssEpisode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getAdmissionEncounterDetails",method=RequestMethod.GET)
	@ResponseBody
		public EMRResponseBean getAdmissionEncDetails(@RequestParam(value="admssEpisode",required=false, defaultValue="") Integer admssEpisode) throws Exception{
			String encDetails=admissionService.getAdmissionEncDetails(admssEpisode);
			EMRResponseBean getAdmissionEncDetails=new EMRResponseBean();
			getAdmissionEncDetails.setData(encDetails);
			return getAdmissionEncDetails;
			}
	
	/**
	 * Creating clinical note
	 * @param dataJson
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/openLeaf",method=RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean openAdmissionLeaf(@RequestBody AdmissionBean dataJson) throws Exception{
		dataJson = getAdmissionBeanData(dataJson);
		Encounter leaf=admissionService.openAdmissionLeaf(dataJson);
		EMRResponseBean openAdmissionLeaf=new EMRResponseBean();
		openAdmissionLeaf.setData(leaf);
		return openAdmissionLeaf;
	}
	
	/**
	 * To Get clinical notes and Allergies details
	 * @param admssEpisode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/AdmissionLeafs",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getAdmissionLeafs(@RequestParam(value="admssEpisode",required=false, defaultValue="") Integer admssEpisode) throws Exception{
		AdmissionLeafBean leafs=admissionService.getAdmissionLeafs(admssEpisode);
		EMRResponseBean getAdmissionLeafs=new EMRResponseBean();
		getAdmissionLeafs.setData(leafs);
		return getAdmissionLeafs;
	}
	
	/**
	 * To Get Rooms of selected Block
	 * @param blockId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getRooms",method=RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getRooms(@RequestParam(value="blockId",required=false, defaultValue="-1") Integer blockId) throws Exception{
		List<AdmissionRoom> room=admissionService.getRooms(blockId);
		EMRResponseBean getRooms=new EMRResponseBean();
		getRooms.setData(room);
		return getRooms;
	}	
	
	/**
	 * Setting default values to data
	 * @param dataJson
	 * @return
	 */
	public AdmissionBean getAdmissionBeanData(AdmissionBean dataJson){
		
		dataJson.setAdmissionDate(Optional.fromNullable(dataJson.getAdmissionDate()+"").or("-1"));
		dataJson.setAdmssProvider(Integer.parseInt(Optional.fromNullable(dataJson.getAdmssProvider()+"").or("-1")));
		dataJson.setPos(Integer.parseInt(Optional.fromNullable(dataJson.getPos()+"").or("-1")));
		dataJson.setPatientId(Integer.parseInt(Optional.fromNullable(dataJson.getPatientId()+"").or("-1")));
		dataJson.setSelectedDx(Optional.fromNullable(dataJson.getSelectedDx()+"").or("[]"));
		dataJson.setChartId(Integer.parseInt(Optional.fromNullable(dataJson.getChartId()+"").or("-1")));
		dataJson.setUserId(Integer.parseInt(Optional.fromNullable(dataJson.getUserId()+"").or("-1")));
		dataJson.setLoginId(Integer.parseInt(Optional.fromNullable(dataJson.getLoginId()+"").or("-1")));
		dataJson.setAdmissionEpisode(Integer.parseInt(Optional.fromNullable(dataJson.getAdmissionEpisode()+"").or("-1")));		
		
		return dataJson;
	}
	
	@RequestMapping(value="/SaveDischargeDetails", method=RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean saveDischargeDetails(@RequestBody AdmissionBean dataJson) throws JSONException{
		
		admissionService.saveDishcargeDetails(dataJson);
		EMRResponseBean respBean= new EMRResponseBean();
		respBean.setData("success");
		return respBean;
	}
}