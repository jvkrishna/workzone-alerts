package com.intrans.reactor.workzone.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SMS Request Information.
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Apr 4, 2017
 *
 */
public class SMSRequestDTO implements Serializable {

	private static final long serialVersionUID = -6583136250255302651L;

	private List<SMSAudience> smsRecipients = new ArrayList<SMSAudience>();
	private String message;
	private String mediaUrl;

	public List<SMSAudience> getSmsRecipients() {
		return smsRecipients;
	}

	public void setSmsRecipients(List<SMSAudience> smsRecipients) {
		this.smsRecipients = smsRecipients;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void addRecipient(SMSAudience smsAudience) {
		this.smsRecipients.add(smsAudience);
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

}
