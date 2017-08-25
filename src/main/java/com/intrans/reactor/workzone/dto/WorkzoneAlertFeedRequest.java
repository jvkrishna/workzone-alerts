package com.intrans.reactor.workzone.dto;

import java.io.Serializable;

public class WorkzoneAlertFeedRequest implements Serializable {

	private static final long serialVersionUID = -4807133352180937197L;
	private String workzone;
	private String device;
	private Double avgSpeed;
	private String alert;

	public String getWorkzone() {
		return workzone;
	}

	public void setWorkzone(String workzone) {
		this.workzone = workzone;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public Double getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(Double avgSpeed) {
		this.avgSpeed = avgSpeed;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

}
