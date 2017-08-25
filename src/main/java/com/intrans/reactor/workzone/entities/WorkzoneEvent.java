package com.intrans.reactor.workzone.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.intrans.reactor.workzone.utils.DateUtils;

@Document
@CompoundIndexes({ @CompoundIndex(name = "workzone_id", def = "{'workzone':1,'eventId':1}") })
public class WorkzoneEvent {

	@Id
	private String id;
	private String workzone;
	private Long eventId;
	private List<SensorEvent> sensorEvents = new ArrayList<>();
	private boolean completed;
	private boolean alertSmsSent;
	private boolean clearanceSmsSent;
	private Long startTime;
	private Long endTime;
	private Long lastUpdateTime;

	public WorkzoneEvent() {
	}

	public WorkzoneEvent(String workzone, Long eventId) {
		super();
		this.workzone = workzone;
		this.eventId = eventId;
		this.startTime = DateUtils.getCurrentTime();
		this.lastUpdateTime = DateUtils.getCurrentTime();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWorkzone() {
		return workzone;
	}

	public void setWorkzone(String workzone) {
		this.workzone = workzone;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public List<SensorEvent> getSensorEvents() {
		return sensorEvents;
	}

	public void setSensorEvents(List<SensorEvent> sensorEvents) {
		this.sensorEvents = sensorEvents;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isAlertSmsSent() {
		return alertSmsSent;
	}

	public void setAlertSmsSent(boolean alertSmsSent) {
		this.alertSmsSent = alertSmsSent;
	}

	public boolean isClearanceSmsSent() {
		return clearanceSmsSent;
	}

	public void setClearanceSmsSent(boolean clearanceSmsSent) {
		this.clearanceSmsSent = clearanceSmsSent;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
