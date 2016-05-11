package com.glenwood.glaceemr.server.application.controllers.skintests;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.controllers.PrescriptionController;
import com.glenwood.glaceemr.server.application.models.EmployeeProfile;
import com.glenwood.glaceemr.server.application.models.PosTable;
import com.glenwood.glaceemr.server.application.models.skintests.ConcentrateGroup;
import com.glenwood.glaceemr.server.application.models.skintests.SkinTestOrder;
import com.glenwood.glaceemr.server.application.models.skintests.SkinTestOrderEntry;
import com.glenwood.glaceemr.server.application.services.chart.skintest.DxAndPendingOrdersBean;
import com.glenwood.glaceemr.server.application.services.chart.skintest.SkinTestOrderDetailsSaveJSON;
import com.glenwood.glaceemr.server.application.services.chart.skintest.SkinTestOrderEntrySaveJSON;
import com.glenwood.glaceemr.server.application.services.chart.skintest.SkinTestOrderSaveJSON;
import com.glenwood.glaceemr.server.application.services.chart.skintest.SkinTestSheetsDTO;
import com.glenwood.glaceemr.server.application.services.chart.skintest.SkinTestShortcutBean;
import com.glenwood.glaceemr.server.application.services.chart.skintest.SkinTestingFormService;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.glenwood.glaceemr.server.utils.SessionMap;
import com.wordnik.swagger.annotations.Api;

/**
 * Controller for Skin Testing Forms in GlaceEMR, 
 * contains the methods to create/edit/get the skin testing sheets (Shortcuts) and to perform the skin tests on patients. 
 * 
 * @author software
 *
 */
@Api(value = "Skin Testing Forms", description = "contains the methods to create/edit/get the skin testing sheets (Shortcuts) and to perform the skin tests on patients", consumes="application/json")
@RestController
@RequestMapping(value = "/SkinTestingForms.Action")
public class SkinTestingFormController {
	
	/*@Autowired
	AuditTrailService auditTrailService;*/
	
	@Autowired
	SessionMap sessionMap;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	SkinTestingFormService skinTestingFormService;
	
	private Logger logger = Logger.getLogger(PrescriptionController.class);
	
	/**
	 * To load the allergen categories with its allergens
	 * @return the list of skin test sheets in the practice
	 * @throws Exception
	 */
	@RequestMapping(value ="/loadAllergens", method = RequestMethod.GET) 
	@ResponseBody
	public EMRResponseBean loadAllergens() throws Exception {
		List<ConcentrateGroup> data = skinTestingFormService.getAllergenCategoriesWithAllergens();
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(data);
		return emrResponseBean;
	}
	
	/**
	 * To get the skin test sheets
	 * @param patientid
	 * @return the list of skin test sheets in the practice
	 * @throws Exception
	 */
	@RequestMapping(value ="/getSkinTestSheets", method = RequestMethod.GET) 
	@ResponseBody
	public EMRResponseBean getSkinTestSheets() throws Exception {
		List<SkinTestShortcutBean> shortcuts = skinTestingFormService.getSkinTestSheets();
		List<ConcentrateGroup> totalAllergensData = skinTestingFormService.getAllergenCategoriesWithAllergens();
		SkinTestSheetsDTO sheetsDTO = new SkinTestSheetsDTO();
		sheetsDTO.setShortcutBeans(shortcuts);
		sheetsDTO.setTotalAllergensData(totalAllergensData);
		/*HashMap<String, Object> resultData = new HashMap<String, Object>();
		resultData.put("shortcutsData", shortcuts);
		resultData.put("totalAllergensData", totalAllergensData);*/
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(sheetsDTO);
		return emrResponseBean;
	}
	
	/**
	 * To create the skin test sheets
	 * @throws Exception
	 */
	@RequestMapping(value ="/createSkinTestSheet", method = RequestMethod.GET) 
	@ResponseBody
	public void createSkinTestSheet(@RequestParam(value="sheetName",required=false,defaultValue="")String sheetName,
			@RequestParam(value="data",required=false,defaultValue="")String data,
			@RequestParam(value="categoryOrder",required=false,defaultValue="")String categoryOrder,
			@RequestParam(value="otherDetails",required=false,defaultValue="")String otherDetails,
			@RequestParam(value="loginId",required=false,defaultValue="-1")Integer loginId) throws Exception {
		JSONObject otherDetails1 = new JSONObject(otherDetails);
		JSONArray data1 = new JSONArray(data);
		JSONArray categoryOrder1 = new JSONArray(categoryOrder);
		skinTestingFormService.saveSkinTestSheet(sheetName,data1, categoryOrder1,otherDetails1,loginId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
	}
	
	/**
	 * To edit the skin test sheets
	 * @throws Exception
	 */
	@RequestMapping(value ="/editSkinTestSheet", method = RequestMethod.GET) 
	@ResponseBody
	public void editSkinTestSheet(@RequestParam(value="sheetId",required=false,defaultValue="")Integer sheetId,
			@RequestParam(value="data",required=false,defaultValue="")JSONArray data,
			@RequestParam(value="categoryOrder",required=false,defaultValue="")JSONArray categoryOrder,
			@RequestParam(value="otherDetails",required=false,defaultValue="")JSONObject otherDetails,
			@RequestParam(value="loginId",required=false,defaultValue="-1")Integer loginId) throws Exception {
		skinTestingFormService.editSkinTestSheet(sheetId,data, categoryOrder,otherDetails,loginId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
	}
	
	/**
	 * To get the list of technicians 
	 * @throws Exception
	 */
	@RequestMapping(value ="/getAllUsers", method = RequestMethod.GET) 
	@ResponseBody
	public EMRResponseBean getAllTechnicians() throws Exception {
		List<EmployeeProfile> usersList = skinTestingFormService.getAllTechnicians();
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(usersList);
		return emrResponseBean;
	}
	
	/**
	 * To get the list of pos 
	 * @throws Exception
	 */
	@RequestMapping(value ="/getAllPos", method = RequestMethod.GET) 
	@ResponseBody
	public EMRResponseBean getAllPos() throws Exception {
		List<PosTable> posList = skinTestingFormService.getAllPos();
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(posList);
		return emrResponseBean;
	}
	
	/**
	 * To save a skin test order
	 * @throws Exception
	 */
	@RequestMapping(value ="/saveSkinTestOrder", method = RequestMethod.POST) 
	@ResponseBody
	public EMRResponseBean saveSkinTestOrder(@RequestBody SkinTestOrderSaveJSON skinTestOrderSaveJSON) throws Exception {
		SkinTestOrder skinTestOrder = skinTestingFormService.saveSkinTestOrder(skinTestOrderSaveJSON);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(skinTestOrder.getSkinTestOrderId());
		return emrResponseBean;
	}
	
	/**
	 * To save a skin test order entry
	 * @throws Exception
	 */
	@RequestMapping(value ="/saveSkinTestOrderEntry", method = RequestMethod.POST) 
	@ResponseBody
	public EMRResponseBean saveSkinTestOrderEntry(@RequestBody SkinTestOrderEntrySaveJSON skinTestOrderEntrySaveJSON) throws Exception {
		SkinTestOrderEntry skinTestOrderEntry = skinTestingFormService.saveSkinTestOrderEntry(skinTestOrderEntrySaveJSON);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(skinTestOrderEntry);
		return emrResponseBean;
	}
	
	/**
	 * To save a skin test order details
	 * @throws Exception
	 */
	@RequestMapping(value ="/saveSkinTestOrderDetails", method = RequestMethod.POST) 
	@ResponseBody
	public EMRResponseBean saveSkinTestOrderDetails(@RequestBody SkinTestOrderDetailsSaveJSON skinTestOrderDetailsSaveJSON) throws Exception {
		SkinTestOrder skinTestOrder = skinTestingFormService.saveSkinTestOrderDetails(skinTestOrderDetailsSaveJSON);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(skinTestOrder);
		return emrResponseBean;
	}
	
	/**
	 * To get the skin test orders
	 * @param patientid
	 * @return the list of skin test orders of the patient
	 * @throws Exception
	 */
	@RequestMapping(value ="/getSkinTestOrders", method = RequestMethod.GET) 
	@ResponseBody
	public EMRResponseBean getSkinTestOrders(@RequestParam(value="patientId",required=false,defaultValue="")Integer patientId) throws Exception {
		List<SkinTestOrder> skinTestOrders = skinTestingFormService.getSkinTestOrders(patientId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(skinTestOrders);
		return emrResponseBean;
	}
	
	/**
	 * To get the skin test order
	 * @param orderid
	 * @return the skin test order
	 * @throws Exception
	 */
	@RequestMapping(value ="/getSkinTestOrderDetails", method = RequestMethod.GET) 
	@ResponseBody
	public EMRResponseBean getSkinTestOrder(@RequestParam(value="orderId",required=false,defaultValue="")Integer orderId) throws Exception {
		SkinTestOrder skinTestOrder = skinTestingFormService.getSkinTestOrderDetails(orderId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(skinTestOrder);
		return emrResponseBean;
	}
	
	/**
	 * To get the patient's Dx list and pending lab orders
	 */
	@RequestMapping(value="/getDxAndPendingLabOrders", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getDxAndPendingLabOrders(@RequestParam(value="patientId",required=false,defaultValue= "-1")Integer patientId,
			@RequestParam(value="chartId",required=false,defaultValue="-1") Integer chartId,
			@RequestParam(value="encounterId",required=false,defaultValue="-1") Integer encounterId) {
		DxAndPendingOrdersBean dxAndPendingOrdersBean = skinTestingFormService.getDxAndPendingOrders(patientId, encounterId, chartId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(dxAndPendingOrdersBean);
		return emrResponseBean;
	}
	
	/**
	 * To get the patient's encounter service doctor or principal doctor.
	 */
	@RequestMapping(value="/getPatientServiceDoctor", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getPatientServiceDoctor(@RequestParam(value="patientId",required=false,defaultValue= "-1")Integer patientId,
			@RequestParam(value="encounterId",required=false,defaultValue="-1") Integer encounterId) {
		EmployeeProfile serivceDoctor = skinTestingFormService.getPatientServiceDoctor(patientId, encounterId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(serivceDoctor);
		return emrResponseBean;
	}
	
	/**
	 * To get the patient's encounter service doctor or principal doctor.
	 */
	@RequestMapping(value="/getPatientPOS", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getPatientPOS(@RequestParam(value="patientId",required=false,defaultValue= "-1")Integer patientId,
			@RequestParam(value="encounterId",required=false,defaultValue="-1") Integer encounterId) {
		PosTable pos = skinTestingFormService.getPatientPOS(patientId, encounterId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(pos);
		return emrResponseBean;
	}
	

	/**
	 * To get the patient's encounter service doctor or principal doctor.
	 */
	@RequestMapping(value="/getIntradermalTestConcentrations", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getIntradermalTestConcentrations() {
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(skinTestingFormService.getTestConcentrations());
		return emrResponseBean;
	}
	
	/**
	 * To update the reviewed details.
	 */
	@RequestMapping(value="/reviewSkinTestOrder", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean reviewSkinTestOrder(@RequestParam(value="orderId",required=false,defaultValue= "-1")Integer orderId,
			@RequestParam(value="reviewedBy",required=false) Integer reviewedBy) {
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(skinTestingFormService.reviewSkinTestOrder(orderId, reviewedBy));
		return emrResponseBean;
	}
	
	/**
	 * To update the completed details.
	 */
	@RequestMapping(value="/completeSkinTestOrder", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean completeSkinTestOrder(@RequestParam(value="orderId",required=false,defaultValue= "-1")Integer orderId,
			@RequestParam(value="completedBy",required=false) Integer completedBy) {
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(skinTestingFormService.completeSkinTestOrder(orderId, completedBy));
		return emrResponseBean;
	}
	
	/**
	 * To save/update the evaluation text of an skin test order
	 */
	@RequestMapping(value="/updateOrderEvaluationText", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean updateOrderEvaluationText(@RequestParam(value="allergensList",required=false,defaultValue= "-1")JSONArray allergensList,
			@RequestParam(value="orderId",required=false,defaultValue= "-1")Integer orderId,
			@RequestParam(value="modifiedBy",required=false) Integer modifiedBy) {
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(skinTestingFormService.updateSkinTestOrderEvaluationText(orderId, allergensList,modifiedBy));
		return emrResponseBean;
	}
	
	/**
	 * To save/update the result comments of an skin test order
	 */
	@RequestMapping(value="/updateOrderResultComments", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean updateOrderResultComments(@RequestParam(value="resultComments",required=false,defaultValue= "-1")String resultComments,
			@RequestParam(value="orderId",required=false,defaultValue= "-1")Integer orderId,
			@RequestParam(value="modifiedBy",required=false) Integer modifiedBy) {
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(skinTestingFormService.updateSkinTestOrderResultComments(orderId, resultComments,modifiedBy));
		return emrResponseBean;
	}
	
	/**
	 * To update the billed status of an test entry
	 */
	@RequestMapping(value="/updateSkinTestEntryBilledStatus", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean updateSkinTestEntryBilledStatus(@RequestParam(value="orderEntryIds",required=false,defaultValue= "-1")JSONArray orderEntryIds,
			@RequestParam(value="billedStatus",required=false) Integer billedStatus,
			@RequestParam(value="modifiedBy",required=false) Integer modifiedBy) {
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		List<SkinTestOrderEntry> entries = skinTestingFormService.updateSkinTestEntryBilledStatus(orderEntryIds, billedStatus,modifiedBy);
//		emrResponseBean.setData(entries.);
		return emrResponseBean;
	}
	
	/**
	 * To get the reviewed user sign details 
	 */
	/*@RequestMapping(value="/getReviewedUserSignDetails",method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean getReviewedUserSignDetails(@RequestParam(value="reviewedBy",required=false,defaultValue="-1") Integer reviewedBy) {
		skinTestingFormService.getReviewedUserSignDetails(reviewedBy);
		return null;
	}*/
	
	/**
	 * To edit the skin test order primary details
	 */
	@RequestMapping(value="/editSkinTestOrder", method = RequestMethod.POST)
	@ResponseBody
	public EMRResponseBean editSkinTestOrder(@RequestBody SkinTestOrderSaveJSON skinTestOrderSaveJSON) throws Exception {
		SkinTestOrder skinTestOrder = skinTestingFormService.editSkinTestOrder(skinTestOrderSaveJSON);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(skinTestOrder.getSkinTestOrderId());
		return emrResponseBean;
	}
	
	/**
	 * To edit the skin test order entry date
	 */
	@RequestMapping(value="/editSkinTestOrderEntryDate", method = RequestMethod.GET)
	@ResponseBody
	public EMRResponseBean editSkinTestOrderEntryDate(@RequestParam(value="newDate",required=false) String newDate,
			@RequestParam(value="orderEntryId",required=false)Integer orderEntryId,
			@RequestParam(value="modifiedBy",required=false)Integer modifiedBy) throws Exception {
		SkinTestOrderEntry orderEntry = skinTestingFormService.editSkinTestOrderEntryDate(orderEntryId,newDate,modifiedBy);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(orderEntry);
		return emrResponseBean;
	}
	
	
}
