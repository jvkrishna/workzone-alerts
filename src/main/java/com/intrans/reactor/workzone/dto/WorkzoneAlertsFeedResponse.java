package com.intrans.reactor.workzone.dto;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "alerts")
public class WorkzoneAlertsFeedResponse {

	@JacksonXmlElementWrapper(useWrapping=false)
	@JacksonXmlProperty(localName = "alert")
	protected transient List<WorkzoneAlertDTO> alerts;

	public List<WorkzoneAlertDTO> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<WorkzoneAlertDTO> alerts) {
		this.alerts = alerts;
	}

}
