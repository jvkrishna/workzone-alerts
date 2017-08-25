package com.intrans.reactor.workzone.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Stores configuration constant files from application.properties file.
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Feb 20, 2017
 *
 */
@Component
public class ConfigConstants {
	// Twilio SMS Variables
	@Value("${sms.twilio.host}")
	private String twilioHost;
	@Value("${sms.twilio.sid}")
	private String twilioSid;
	@Value("${sms.twilio.authtoken}")
	private String twilioAuthToken;
	@Value("${sms.twilio.from}")
	private String twilioFrom;

	public String getTwilioHost() {
		return twilioHost;
	}

	public void setTwilioHost(String twilioHost) {
		this.twilioHost = twilioHost;
	}

	public String getTwilioSid() {
		return twilioSid;
	}

	public void setTwilioSid(String twilioSid) {
		this.twilioSid = twilioSid;
	}

	public String getTwilioAuthToken() {
		return twilioAuthToken;
	}

	public void setTwilioAuthToken(String twilioAuthToken) {
		this.twilioAuthToken = twilioAuthToken;
	}

	public String getTwilioFrom() {
		return twilioFrom;
	}

	public void setTwilioFrom(String twilioFrom) {
		this.twilioFrom = twilioFrom;
	}

}
