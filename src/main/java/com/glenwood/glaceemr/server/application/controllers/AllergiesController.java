package com.glenwood.glaceemr.server.application.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.glenwood.glaceemr.server.application.models.PatientAllergies;
import com.glenwood.glaceemr.server.application.services.chart.Allergies.AllergiesService;
import com.glenwood.glaceemr.server.application.services.chart.Allergies.AllergiesTypeBean;
import com.glenwood.glaceemr.server.application.services.chart.Allergies.AllergyBean;
import com.glenwood.glaceemr.server.utils.EMRResponseBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Controller for Allergies
 * @author Bhagya Lakshmi
 *
 */

@Api(value="/user/Allergies",description="To get Allergies",consumes="application/json")
@RestController
@Transactional
@RequestMapping(value="/user/Allergies")
public class AllergiesController {
	
	@Autowired
	AllergiesService AllergiesService;

	private Logger logger = Logger.getLogger(AllergiesController.class);
	
	/**
	 * Method to retrieve allergy types
	 * @param gender
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/retrievingAllergyType", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of Allergy data"),
            @ApiResponse(code = 404, message = "when gender does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean getAllergyData(@ApiParam(name="gender", value="gender") @RequestParam(value="gender",required=false,defaultValue="-1")int gender)throws Exception
	{
		List<AllergiesTypeBean> allergyTypes=AllergiesService.retrievingAllergyType(gender);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(allergyTypes);
		return emrResponseBean;
	}
	
	/**
	 * Method to get data for allergen search
	 * @param Criteria
	 * @param mode
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/searchAllergyData", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful search of Allergy data"),
            @ApiResponse(code = 404, message = "when Criteria or mode or offset or limit does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean getSearchAllergyData(@ApiParam(name="Criteria", value="Criteria") @RequestParam(value="Criteria",required=false,defaultValue="-1")String Criteria,
			@ApiParam(name="mode", value="mode") @RequestParam(value="mode",required=false,defaultValue="-1")int mode,
			@ApiParam(name="offset", value="offset") @RequestParam(value="offset",required=false,defaultValue="-1")int offset,
			@ApiParam(name="limit", value="limit") @RequestParam(value="limit",required=false,defaultValue="-1")int limit)throws Exception
	{
		AllergyBean SearchAllergyData=AllergiesService.searchAllergy(Criteria,mode,offset,limit);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(SearchAllergyData);
		return emrResponseBean;
	}
	
	/**
	 * Method to retrieve reaction search data
	 * @param Criteria
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/getReactionsOfAllergies", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of reactions of Allergies"),
            @ApiResponse(code = 404, message = "when Criteria or offset or limit does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean getReactionsOfAllergies(@ApiParam(name="Criteria", value="Criteria") @RequestParam(value="Criteria",required=false,defaultValue="-1")String Criteria,
			@ApiParam(name="offset", value="offset") @RequestParam(value="offset",required=false,defaultValue="-1")int offset,
			@ApiParam(name="limit", value="limit") @RequestParam(value="limit",required=false,defaultValue="-1")int limit)throws Exception
	{
		AllergyBean SearchAllergyData=AllergiesService.getReactionsOfAllergies(Criteria,offset,limit);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(SearchAllergyData);
		return emrResponseBean;
	}
	
	/**
	 * Method to check NKDA and NKA (No known Drug Allergies & No known Allergies)
	 * @param ChartId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/getNKDAandNKACheckDetails", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of NKDA and NKA Check details"),
            @ApiResponse(code = 404, message = "when chart id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
	public EMRResponseBean getNKDAandNKACheckDetails(@ApiParam(name="chartId", value="chartId") @RequestParam(value="chartId",required=false,defaultValue="-1")Integer chartId)throws Exception
	{
		List<PatientAllergies> SearchAllergyData=AllergiesService.getNKDAandNKACheckDetails(chartId);
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(SearchAllergyData);
		return emrResponseBean;
	}
	
	/**
	 * Method to save new Allergies
	 * @param save
	 * @param insertParameters
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/saveNewAllergies", method = RequestMethod.POST)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful insertion of new Allergy data"),
            @ApiResponse(code = 404, message = "when save or insertParameters does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
    public void saveNewAllergies(@ApiParam(name="save", value="save") @RequestParam(value="save",required=false,defaultValue="-1")Integer save,
    		@ApiParam(name="insertParameters", value="insertParameters") @RequestParam(value="insertParameters",required=false,defaultValue="-1")String insertParameters)throws Exception
	{
		AllergiesService.saveNewAllergies(save,insertParameters);
		return;
	}
	
	/**
	 * Method to save allergy edit data
	 * @param editData
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/saveAllergyEditData", method = RequestMethod.POST)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful insertion of edit Allergy data"),
            @ApiResponse(code = 404, message = "when edit data field does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
    public void saveNewAllergies(
    		@ApiParam(name="editData", value="editData") @RequestParam(value="editData",required=false,defaultValue="-1")String editData)throws Exception
	{
		AllergiesService.editAllergies(editData);
		return;
	}
	
	/**
	 * Method to delete allergies
	 * @param allergyId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/deleteAllergies", method = RequestMethod.POST)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful deletion of Allergy data"),
            @ApiResponse(code = 404, message = "when allergy id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
    public void deleteAllergies(
    		@ApiParam(name="allergyId", value="allergyId") @RequestParam(value="allergyId",required=false,defaultValue="-1")String allergyId)throws Exception
	{
		AllergiesService.deleteAllergy(allergyId);
		return;
	}
	
	/**
	 * Method to review Allergies
	 * @param reviewData
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/reviewAllergies", method = RequestMethod.POST)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful updation of review details"),
            @ApiResponse(code = 404, message = "when review data does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
    public void reviewAllergies(
    		@ApiParam(name="reviewData", value="reviewData") @RequestParam(value="reviewData",required=false,defaultValue="-1")String reviewData)throws Exception
	{
		AllergiesService.reviewAllergies(reviewData);
		return;
	}
	
	/**
	 * Method to check NKDA
	 * @param nkdaData
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/NKDACheck", method = RequestMethod.POST)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful insertion of NKDA check data"),
            @ApiResponse(code = 404, message = "when chart id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
    public void NKDACheck(
    		@ApiParam(name="nkdaData", value="nkdaData") @RequestParam(value="nkdaData",required=false,defaultValue="-1")String nkdaData)throws Exception
	{
		AllergiesService.nkdaUpdate(nkdaData);
		return;
	}
	
	/**
	 * Method to check NKA
	 * @param nkaData
	 * @throws Exception
	 */
	@RequestMapping(value ="/NKACheck", method = RequestMethod.POST)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful insertion of NKA Check data"),
            @ApiResponse(code = 404, message = "when chart id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
    public void NKACheck(
    		@ApiParam(name="nkaData", value="nkaData") @RequestParam(value="nkaData",required=false,defaultValue="-1")String nkaData)throws Exception
	{
		AllergiesService.nkaUpdate(nkaData);
		return;
	}
	
	/**
	 * Method to uncheck NKDA
	 * @param nkdaData
	 * @throws Exception
	 */
	@RequestMapping(value ="/NKDAUnCheck", method = RequestMethod.POST)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful insertion of NKDA Uncheck data"),
            @ApiResponse(code = 404, message = "when chart id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
    public void NKDAUnCheck(
    		@ApiParam(name="nkdaData", value="nkdaData") @RequestParam(value="nkdaData",required=false,defaultValue="-1")String nkdaData)throws Exception
	{
		AllergiesService.nkdauncheck(nkdaData);
		return;
	}
	
	/**
	 * Method to uncheck NKA
	 * @param nkaData
	 * @throws Exception
	 */
	@RequestMapping(value ="/NKAUnCheck", method = RequestMethod.POST)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful insertion of NKA Uncheck data"),
            @ApiResponse(code = 404, message = "when chart id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
    public void NKAUnCheck(
    		@ApiParam(name="nkaData", value="nkaData") @RequestParam(value="nkaData",required=false,defaultValue="-1")String nkaData)throws Exception
	{
		AllergiesService.nkauncheck(nkaData);
		return;
	}
	
	/**
	 * Method to retrieve Edited allergy data
	 * @param chartId
	 * @param patId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/RetrieveEditAllergData", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of edit Allergy data"),
            @ApiResponse(code = 404, message = "when chart id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
    public EMRResponseBean RetrieveEditAllergData(
    		@ApiParam(name="chartId", value="chartId") @RequestParam(value="chartId",required=false,defaultValue="-1")String chartId,
    		@ApiParam(name="patId", value="patId") @RequestParam(value="patId",required=false,defaultValue="-1")String patId)throws Exception
	{
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(AllergiesService.retrieveDataForEditAllerg(chartId, patId));
		return emrResponseBean;
	}
	
	/**
	 * Method to retrieve In active Allergy data
	 * @param chartId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/RetrieveInActiveAllergData", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of in active Allergy data"),
            @ApiResponse(code = 404, message = "when chart id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
    public EMRResponseBean RetrieveInActiveAllergData(
    		@ApiParam(name="chartId", value="chartId") @RequestParam(value="chartId",required=false,defaultValue="-1")String chartId)throws Exception
	{
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(AllergiesService.retrieveInActiveAllerg(chartId));
		return emrResponseBean;
	}
	
	/**
	 * Method to get last review details
	 * @param chartId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/LastReviewDetails", method = RequestMethod.GET)
    @ApiResponses(value= {
            @ApiResponse(code = 200, message = "Successful retrieval of last review details"),
            @ApiResponse(code = 404, message = "when chart id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
    public EMRResponseBean LastReviewDetails(
    		@ApiParam(name="chartId", value="chartId") @RequestParam(value="chartId",required=false,defaultValue="-1")String chartId)throws Exception
	{
		EMRResponseBean emrResponseBean = new EMRResponseBean();
		emrResponseBean.setData(AllergiesService.lastReviewDetails(chartId).toString());
		return emrResponseBean;
	}
}
