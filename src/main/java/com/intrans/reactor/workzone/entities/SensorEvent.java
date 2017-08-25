package com.intrans.reactor.workzone.entities;

import org.springframework.data.mongodb.core.index.Indexed;

import com.intrans.reactor.workzone.constants.WorkzoneConstants.WorkzoneAlertStatus;
import com.intrans.reactor.workzone.utils.DateUtils;

public class SensorEvent {

	@Indexed
	private String name;
	private WorkzoneAlertStatus alert;
	private Double avgSped;
	private boolean completed;
	private Long startTime;
	private Long endTime;
	private Long lastUpdateTime;

	public SensorEvent() {
	}

	public SensorEvent(String name, WorkzoneAlertStatus alert, Double avgSped) {
		super();
		this.name = name;
		this.alert = alert;
		this.avgSped = avgSped;
		this.startTime = DateUtils.getCurrentTime();
		this.lastUpdateTime = DateUtils.getCurrentTime();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WorkzoneAlertStatus getAlert() {
		return alert;
	}

	public void setAlert(WorkzoneAlertStatus alert) {
		this.alert = alert;
	}

	public Double getAvgSped() {
		return avgSped;
	}

	public void setAvgSped(Double avgSped) {
		this.avgSped = avgSped;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
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
