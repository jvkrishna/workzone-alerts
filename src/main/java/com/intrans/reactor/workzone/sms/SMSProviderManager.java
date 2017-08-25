package com.intrans.reactor.workzone.sms;

import static com.intrans.reactor.workzone.constants.SMSConstants.SMSProviderPropertiesEnum.TWILIO_AUTHTOKEN;
import static com.intrans.reactor.workzone.constants.SMSConstants.SMSProviderPropertiesEnum.TWILIO_FROM;
import static com.intrans.reactor.workzone.constants.SMSConstants.SMSProviderPropertiesEnum.TWILIO_HOST;
import static com.intrans.reactor.workzone.constants.SMSConstants.SMSProviderPropertiesEnum.TWILIO_SID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.intrans.reactor.workzone.constants.ConfigConstants;
import com.intrans.reactor.workzone.constants.SMSConstants.SMSProvidersEnum;
import com.intrans.reactor.workzone.dto.SMSConfigDTO;

@Component
public class SMSProviderManager {

	@Autowired
	private ConfigConstants configConstants;

	private SMSConfigDTO twilioSMSConfigDTO;

	@PostConstruct
	public void init() {
		twilioSMSConfigDTO = createTwilioSMSConfigruations();
	}

	public SMSConfigDTO getDefaultSMSConfiguration() {
		return twilioSMSConfigDTO;
	}

	public SMSConfigDTO createTwilioSMSConfigruations() {
		SMSConfigDTO twilioSMSConfig = new SMSConfigDTO();
		twilioSMSConfig.setSmsProvider(SMSProvidersEnum.TWILIO);
		twilioSMSConfig.addConfiguration(TWILIO_HOST, configConstants.getTwilioHost());
		twilioSMSConfig.addConfiguration(TWILIO_SID, configConstants.getTwilioSid());
		twilioSMSConfig.addConfiguration(TWILIO_FROM, configConstants.getTwilioFrom());
		twilioSMSConfig.addConfiguration(TWILIO_AUTHTOKEN, configConstants.getTwilioAuthToken());
		return twilioSMSConfig;
	}

}
