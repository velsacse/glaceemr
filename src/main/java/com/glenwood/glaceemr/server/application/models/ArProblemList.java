package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ar_problem_list")
public class ArProblemList {

	@Id
	@Column(name="ar_problem_list_id")
	private Integer arProblemListId;

	@Column(name="ar_problem_list_problem")
	private String arProblemListProblem;

	@Column(name="ar_problem_list_problem_description")
	private String arProblemListProblemDescription;

	@Column(name="ar_problem_list_to_be_synchronized")
	private String arProblemListToBeSynchronized;

	@Column(name="ar_problem_list_problem_groupid")
	private Integer arProblemListProblemGroupid;

	@Column(name="ar_problem_list_isfreqentlyused")
	private Integer arProblemListIsfreqentlyused;

	@Column(name="ar_problem_list_iscustom")
	private Integer arProblemListIscustom;

	public Integer getArProblemListId() {
		return arProblemListId;
	}

	public void setArProblemListId(Integer arProblemListId) {
		this.arProblemListId = arProblemListId;
	}

	public String getArProblemListProblem() {
		return arProblemListProblem;
	}

	public void setArProblemListProblem(String arProblemListProblem) {
		this.arProblemListProblem = arProblemListProblem;
	}

	public String getArProblemListProblemDescription() {
		return arProblemListProblemDescription;
	}

	public void setArProblemListProblemDescription(
			String arProblemListProblemDescription) {
		this.arProblemListProblemDescription = arProblemListProblemDescription;
	}

	public String getArProblemListToBeSynchronized() {
		return arProblemListToBeSynchronized;
	}

	public void setArProblemListToBeSynchronized(
			String arProblemListToBeSynchronized) {
		this.arProblemListToBeSynchronized = arProblemListToBeSynchronized;
	}

	public Integer getArProblemListProblemGroupid() {
		return arProblemListProblemGroupid;
	}

	public void setArProblemListProblemGroupid(Integer arProblemListProblemGroupid) {
		this.arProblemListProblemGroupid = arProblemListProblemGroupid;
	}

	public Integer getArProblemListIsfreqentlyused() {
		return arProblemListIsfreqentlyused;
	}

	public void setArProblemListIsfreqentlyused(Integer arProblemListIsfreqentlyused) {
		this.arProblemListIsfreqentlyused = arProblemListIsfreqentlyused;
	}

	public Integer getArProblemListIscustom() {
		return arProblemListIscustom;
	}

	public void setArProblemListIscustom(Integer arProblemListIscustom) {
		this.arProblemListIscustom = arProblemListIscustom;
	}
	
	
}