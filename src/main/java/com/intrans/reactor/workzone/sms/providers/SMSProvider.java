package com.intrans.reactor.workzone.sms.providers;

import org.springframework.http.ResponseEntity;

import com.intrans.reactor.workzone.dto.SMSConfigDTO;
import com.intrans.reactor.workzone.dto.SMSRequest;

/**
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Apr 4, 2017
 *
 */
public interface SMSProvider {

	/**
	 * Distribute Text Messages for the given recipients
	 * 
	 * @param smsConfiguration
	 * @param smsRequests
	 */
	ResponseEntity<String> sendSMS(SMSConfigDTO smsConfiguration, SMSRequest smsRequest);

	/**
	 * Check the connectivity
	 * 
	 * @param smsConfiguration
	 * @return
	 */
	boolean testConnectivity(SMSConfigDTO smsConfiguration);
}
