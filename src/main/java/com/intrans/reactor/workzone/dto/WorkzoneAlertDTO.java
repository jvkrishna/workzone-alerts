package com.intrans.reactor.workzone.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intrans.reactor.workzone.constants.WorkzoneConstants.WorkzoneAlertStatus;
import com.intrans.reactor.workzone.utils.DateUtils;

/**
 * DTO file for capturing the alerts feed.
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Mar 28, 2017
 *
 */
public class WorkzoneAlertDTO implements Serializable {

	private static final long serialVersionUID = -8883447265152736369L;

	private Long id;
	private String workzone;
	private String route;
	private String milepost;
	private String direction;
	private WorkzoneAlertStatus severity;
	private Double avgSpeed;
	private String projectName;
	@JsonIgnore
	private String camera;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Chicago")
	private Date startTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Chicago")
	private Date endTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWorkzone() {
		return workzone;
	}

	public void setWorkzone(String workzone) {
		this.workzone = workzone;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getMilepost() {
		return milepost;
	}

	public void setMilepost(String milepost) {
		this.milepost = milepost;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public WorkzoneAlertStatus getSeverity() {
		return severity;
	}

	public void setSeverity(WorkzoneAlertStatus severity) {
		this.severity = severity;
	}

	public Double getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(Double avgSpeed) {
		this.avgSpeed = avgSpeed;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCamera() {
		return camera;
	}

	public void setCamera(String camera) {
		this.camera = camera;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		if (endTime == null) {
			endTime = DateUtils.getCurrentDate();
		}
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
