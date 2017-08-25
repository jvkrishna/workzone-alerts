package com.intrans.reactor.workzone.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores SMS related constant values.
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Apr 4, 2017
 *
 */
public class SMSConstants {

	/**
	 * SMS Providers Enum.
	 * 
	 * @author Vamsi Krishna J <br />
	 *         <b>Date:</b> Apr 4, 2017
	 *
	 */
	public enum SMSProvidersEnum {
		TWILIO, NEXMO;
	}

	public enum SMSProviderPropertiesEnum {
		TWILIO_HOST(SMSProvidersEnum.TWILIO, "twilio.host"), TWILIO_FROM(SMSProvidersEnum.TWILIO,
				"twilio.from"), TWILIO_SID(SMSProvidersEnum.TWILIO,
						"twilio.sid"), TWILIO_AUTHTOKEN(SMSProvidersEnum.TWILIO, "twilio.authtoken");

		private SMSProvidersEnum provider;
		private String key;

		private SMSProviderPropertiesEnum(SMSProvidersEnum provider, String key) {
			this.provider = provider;
			this.key = key;
		}

		public SMSProvidersEnum getProvider() {
			return provider;
		}

		public String getKey() {
			return key;
		}

		public static List<SMSProviderPropertiesEnum> getAllProperties(SMSProvidersEnum provider) {
			List<SMSProviderPropertiesEnum> properties = new ArrayList<SMSConstants.SMSProviderPropertiesEnum>();
			for (SMSProviderPropertiesEnum property : values()) {
				if (property.getProvider() == provider) {
					properties.add(property);
				}
			}
			return properties;
		}

	}
}
