package com.intrans.reactor.workzone.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Constants file that reads the workzone.properties file.
 * managed b
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Apr 15, 2017
 *
 */
/**
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Apr 16, 2017
 *
 * 
 */
@PropertySource(value = { "classpath:workzone.properties" })
@Component
@ConfigurationProperties
public class WorkzoneConfig {

	private Double slowThreshold;

	private Double stopThreshold;

	private String recipientsFile;

	private Integer minAlertDuration;

	private Integer minClearanceDuration;

	private String wzAlertJob;

	public Double getSlowThreshold() {
		return slowThreshold;
	}

	public void setSlowThreshold(Double slowThreshold) {
		this.slowThreshold = slowThreshold;
	}

	public Double getStopThreshold() {
		return stopThreshold;
	}

	public void setStopThreshold(Double stopThreshold) {
		this.stopThreshold = stopThreshold;
	}

	public String getRecipientsFile() {
		return recipientsFile;
	}

	public void setRecipientsFile(String recipientsFile) {
		this.recipientsFile = recipientsFile;
	}

	public Integer getMinAlertDuration() {
		return minAlertDuration;
	}

	public void setMinAlertDuration(Integer minAlertDuration) {
		this.minAlertDuration = minAlertDuration;
	}

	public Integer getMinClearanceDuration() {
		return minClearanceDuration;
	}

	public void setMinClearanceDuration(Integer minClearanceDuration) {
		this.minClearanceDuration = minClearanceDuration;
	}

	public String getWzAlertJob() {
		return wzAlertJob;
	}

	public void setWzAlertJob(String wzAlertJob) {
		this.wzAlertJob = wzAlertJob;
	}

}
