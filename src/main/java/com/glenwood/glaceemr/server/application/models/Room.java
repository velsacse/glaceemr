package com.glenwood.glaceemr.server.application.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="h479")
public class Room {

	@Id
	@Column(name="h479001")
	@NotNull
	private Integer roomId;
	
	@Column(name="h479002")
	private String roomName;
	
	@Column(name="h479003")
	@NotNull
	private Boolean roomIsActive;
	

	/*@OneToMany(cascade=CascadeType.ALL,mappedBy="roomTable")
	@JsonManagedReference
	List<AlertEvent> alertTable;*/

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Boolean getRoomIsActive() {
		return roomIsActive;
	}

	public void setRoomIsActive(Boolean roomIsActive) {
		this.roomIsActive = roomIsActive;
	}
}
