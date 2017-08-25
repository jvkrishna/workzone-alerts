package com.intrans.reactor.workzone.dto;

import java.io.Serializable;

/**
 * Final SMS request class that will be used in the processing.
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Apr 4, 2017
 *
 */
public class SMSRequest implements Serializable {

	private static final long serialVersionUID = 3826238763241152187L;

	private SMSAudience smsAudience;
	private String message;
	private String mediaUrl;

	public SMSAudience getSmsAudience() {
		return smsAudience;
	}

	public void setSmsAudience(SMSAudience smsAudience) {
		this.smsAudience = smsAudience;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getToNumber() {
		if (this.getSmsAudience() != null)
			return this.getSmsAudience().getMobileNumber();
		return null;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

}
