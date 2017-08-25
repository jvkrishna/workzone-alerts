package com.intrans.reactor.workzone.sms.providers;

import static com.intrans.reactor.workzone.constants.SMSConstants.SMSProviderPropertiesEnum.TWILIO_AUTHTOKEN;
import static com.intrans.reactor.workzone.constants.SMSConstants.SMSProviderPropertiesEnum.TWILIO_FROM;
import static com.intrans.reactor.workzone.constants.SMSConstants.SMSProviderPropertiesEnum.TWILIO_HOST;
import static com.intrans.reactor.workzone.constants.SMSConstants.SMSProviderPropertiesEnum.TWILIO_SID;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.intrans.reactor.workzone.constants.SMSConstants.SMSProviderPropertiesEnum;
import com.intrans.reactor.workzone.dto.SMSConfigDTO;
import com.intrans.reactor.workzone.dto.SMSRequest;
import com.intrans.reactor.workzone.utils.TimeliUtils;

@Component(value = "twilio")
public class TwilioSMSProvider implements SMSProvider {

	@Override
	public ResponseEntity<String> sendSMS(SMSConfigDTO smsConfiguration, SMSRequest smsRequest) {
		// TODO add validations
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		Map<SMSProviderPropertiesEnum, String> providerConfigurations = smsConfiguration.getProviderConfigurations();
		formData.add("From", providerConfigurations.get(TWILIO_FROM));
		formData.add("To", smsRequest.getToNumber());
		formData.add("Body", smsRequest.getMessage());
		if (StringUtils.isNotBlank(smsRequest.getMediaUrl())) {
			formData.add("MediaUrl", smsRequest.getMediaUrl());
		}
		// Add basic authentication interceptor.
		restTemplate.getInterceptors().add(TimeliUtils.getBasicAuthInterceptor(providerConfigurations.get(TWILIO_SID),
				providerConfigurations.get(TWILIO_AUTHTOKEN)));
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(formData,
				new HttpHeaders());
		return restTemplate.postForEntity(providerConfigurations.get(TWILIO_HOST), request, String.class);

	}

	@Override
	public boolean testConnectivity(SMSConfigDTO smsConfiguration) {
		// TODO
		return true;
	}

}
