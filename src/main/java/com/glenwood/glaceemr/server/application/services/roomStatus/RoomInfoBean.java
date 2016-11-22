package com.glenwood.glaceemr.server.application.services.roomStatus;

import java.util.List;

public class RoomInfoBean {
	private String locations;
	private String providers;
	private List<RoomStatusBean> roomStatus;
	private List<OrderedBean> ordered;
	private String roomDetail;
	private String activities;
	
	public RoomInfoBean(String locations, String providers,List<RoomStatusBean> roomStatus, List<OrderedBean> ordered,String roomDetail, String activities) {
		super();
		this.locations=locations;
		this.providers=providers;
		this.roomStatus=roomStatus;
		this.ordered=ordered;
		this.roomDetail=roomDetail;
		this.activities=activities;
	}

	public String getLocations() {
		return locations;
	}

	public void setLocations(String locations) {
		this.locations = locations;
	}

	public String getProviders() {
		return providers;
	}

	public void setProviders(String providers) {
		this.providers = providers;
	}

	public List<RoomStatusBean> getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(List<RoomStatusBean> roomStatus) {
		this.roomStatus = roomStatus;
	}

	public List<OrderedBean> getOrdered() {
		return ordered;
	}

	public void setOrdered(List<OrderedBean> ordered) {
		this.ordered = ordered;
	}

	public String getRoomDetail() {
		return roomDetail;
	}

	public void setRoomDetail(String roomDetail) {
		this.roomDetail = roomDetail;
	}

	public String getActivities() {
		return activities;
	}

	public void setActivities(String activities) {
		this.activities = activities;
	}

}
