package com.glenwood.glaceemr.server.application.models.skintests;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "skin_test_form_shortcut_category_details")
public class SkinTestFormShortcutCategoryDetails {

	@Id
	@Column(name="skin_test_form_shortcut_category_details_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="skin_test_form_shortcut_category_details_id")
	@SequenceGenerator(name="skin_test_form_shortcut_category_details_id",sequenceName="skin_test_form_shortcut_category_details_id",allocationSize=1)
	private Integer skinTestFormShortcutCategoryDetailsId;

	@Column(name="skin_test_form_shortcut_category_details_shortcut_id")
	private Integer skinTestFormShortcutCategoryDetailsShortcutId;

	@Column(name="skin_test_form_shortcut_category_details_allergen_category_id")
	private Integer skinTestFormShortcutCategoryDetailsAllergenCategoryId;

	@Column(name="skin_test_form_shortcut_category_details_category_order_id")
	private Integer skinTestFormShortcutCategoryDetailsCategoryOrderId;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="skin_test_form_shortcut_category_details_allergen_category_id",referencedColumnName="concentrate_group_id",insertable=false,updatable=false)
	@JsonManagedReference
	ConcentrateGroup concentrateGroup;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="skin_test_form_shortcut_category_details_shortcut_id",referencedColumnName="skin_test_form_shortcut_id",insertable=false,updatable=false)
	@JsonBackReference
	SkinTestFormShortcut skinTestFormShortcut;
		
	@OneToMany(mappedBy="skinTestFormShortcutCategoryDetails")
	List<SkinTestFormShortcutAllergenDetails> skinTestFormShortcutAllergenDetails; 
	
	
	public Integer getSkinTestFormShortcutCategoryDetailsId() {
		return skinTestFormShortcutCategoryDetailsId;
	}

	public void setSkinTestFormShortcutCategoryDetailsId(
			Integer skinTestFormShortcutCategoryDetailsId) {
		this.skinTestFormShortcutCategoryDetailsId = skinTestFormShortcutCategoryDetailsId;
	}

	public Integer getSkinTestFormShortcutCategoryDetailsShortcutId() {
		return skinTestFormShortcutCategoryDetailsShortcutId;
	}

	public void setSkinTestFormShortcutCategoryDetailsShortcutId(
			Integer skinTestFormShortcutCategoryDetailsShortcutId) {
		this.skinTestFormShortcutCategoryDetailsShortcutId = skinTestFormShortcutCategoryDetailsShortcutId;
	}

	public Integer getSkinTestFormShortcutCategoryDetailsAllergenCategoryId() {
		return skinTestFormShortcutCategoryDetailsAllergenCategoryId;
	}

	public void setSkinTestFormShortcutCategoryDetailsAllergenCategoryId(
			Integer skinTestFormShortcutCategoryDetailsAllergenCategoryId) {
		this.skinTestFormShortcutCategoryDetailsAllergenCategoryId = skinTestFormShortcutCategoryDetailsAllergenCategoryId;
	}

	public Integer getSkinTestFormShortcutCategoryDetailsCategoryOrderId() {
		return skinTestFormShortcutCategoryDetailsCategoryOrderId;
	}

	public void setSkinTestFormShortcutCategoryDetailsCategoryOrderId(
			Integer skinTestFormShortcutCategoryDetailsCategoryOrderId) {
		this.skinTestFormShortcutCategoryDetailsCategoryOrderId = skinTestFormShortcutCategoryDetailsCategoryOrderId;
	}

	public SkinTestFormShortcut getSkinTestFormShortcut() {
		return skinTestFormShortcut;
	}

	public void setSkinTestFormShortcut(SkinTestFormShortcut skinTestFormShortcut) {
		this.skinTestFormShortcut = skinTestFormShortcut;
	}

	public List<SkinTestFormShortcutAllergenDetails> getSkinTestFormShortcutAllergenDetails() {
		return skinTestFormShortcutAllergenDetails;
	}

	public void setSkinTestFormShortcutAllergenDetails(
			List<SkinTestFormShortcutAllergenDetails> skinTestFormShortcutAllergenDetails) {
		this.skinTestFormShortcutAllergenDetails = skinTestFormShortcutAllergenDetails;
	}

	public ConcentrateGroup getConcentrateGroup() {
		return concentrateGroup;
	}

	public void setConcentrateGroup(ConcentrateGroup concentrateGroup) {
		this.concentrateGroup = concentrateGroup;
	}
	
}