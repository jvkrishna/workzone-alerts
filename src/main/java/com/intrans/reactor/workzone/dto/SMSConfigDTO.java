package com.intrans.reactor.workzone.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.intrans.reactor.workzone.constants.SMSConstants.SMSProviderPropertiesEnum;
import com.intrans.reactor.workzone.constants.SMSConstants.SMSProvidersEnum;

/**
 * DTO Class for storing SMS configuration details.
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Apr 4, 2017
 *
 */
public class SMSConfigDTO implements Serializable {

	private static final long serialVersionUID = -146768510927501952L;

	private SMSProvidersEnum smsProvider;
	private Map<SMSProviderPropertiesEnum, String> providerConfigurations = new HashMap<SMSProviderPropertiesEnum, String>();

	public SMSProvidersEnum getSmsProvider() {
		return smsProvider;
	}

	public void setSmsProvider(SMSProvidersEnum smsProvider) {
		this.smsProvider = smsProvider;
	}

	public Map<SMSProviderPropertiesEnum, String> getProviderConfigurations() {
		return providerConfigurations;
	}

	public void setProviderConfigurations(Map<SMSProviderPropertiesEnum, String> providerConfigurations) {
		this.providerConfigurations = providerConfigurations;
	}

	public void addConfiguration(SMSProviderPropertiesEnum key, String value) {
		this.providerConfigurations.put(key, value);
	}

	public void addAllConfigurations(Map<SMSProviderPropertiesEnum, String> providerConfigs) {
		this.providerConfigurations.putAll(providerConfigs);
	}

}
