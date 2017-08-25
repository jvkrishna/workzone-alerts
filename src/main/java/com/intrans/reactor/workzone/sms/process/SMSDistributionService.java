package com.intrans.reactor.workzone.sms.process;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.intrans.reactor.handlers.AsyncCallBack;
import com.intrans.reactor.handlers.Callback;
import com.intrans.reactor.workzone.dto.SMSAudience;
import com.intrans.reactor.workzone.dto.SMSConfigDTO;
import com.intrans.reactor.workzone.dto.SMSRequest;
import com.intrans.reactor.workzone.dto.SMSRequestDTO;
import com.intrans.reactor.workzone.sms.providers.SMSProvider;

/**
 * Service class that forms the SMS request objects, and handovers requests to
 * the corresponding providers.
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Apr 4, 2017
 *
 */
@Component
public class SMSDistributionService {

	@Autowired
	@Qualifier("twilio")
	private SMSProvider smsProvider;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	public void distribute(List<SMSRequestDTO> smsRequests, SMSConfigDTO smsConfigDTO) {
		if (!smsRequests.isEmpty()) {
			for (SMSRequestDTO smsRequest : smsRequests) {
				distribute(smsConfigDTO, smsRequest);
			}
		}
	}

	public void distribute(final SMSConfigDTO smsConfiguration, SMSRequestDTO smsRequestDTO) {
		String message = smsRequestDTO.getMessage();
		for (SMSAudience smsRecipient : smsRequestDTO.getSmsRecipients()) {
			final SMSRequest smsRequest = new SMSRequest();
			smsRequest.setSmsAudience(smsRecipient);
			smsRequest.setMessage(message);
			smsRequest.setMediaUrl(smsRequestDTO.getMediaUrl());
			new AsyncCallBack(new Callback() {

				@Override
				public void call() {
					// TODO - Capture response and add failure handlers
					ResponseEntity<String> response = smsProvider.sendSMS(smsConfiguration, smsRequest);
					System.out.println("SMS Status:" + response.getStatusCode());

				}
			}).execute(taskExecutor);

		}
	}

}
