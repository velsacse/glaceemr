package com.glenwood.glaceemr.server.application.services.alertinbox;

public class AlertCountBean {

	public AlertCountBean(long totalCount, int categoryId,
			String categoryName, int categoryorder) {
		super();
		this.totalCount = totalCount;
		this.categoryId = categoryId+"";
		this.categoryName = categoryName;
		this.categoryorder = categoryorder+"";
	}

	long totalCount;
	String categoryId;
	String categoryName;
	String categoryorder;
	Long unReadCount;
	
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryorder() {
		return categoryorder;
	}
	public void setCategoryorder(String categoryorder) {
		this.categoryorder = categoryorder;
	}
	public Long getUnReadCount() {
		return unReadCount;
	}
	public void setUnReadCount(Long unReadCount) {
		this.unReadCount = unReadCount;
	}
	

}
