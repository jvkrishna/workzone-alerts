package com.intrans.reactor.workzone.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.intrans.reactor.workzone.constants.WorkzoneConfig;
import com.intrans.reactor.workzone.constants.WorkzoneConstants.AlertSMSType;
import com.intrans.reactor.workzone.dto.SMSAudience;
import com.intrans.reactor.workzone.dto.SMSConfigDTO;
import com.intrans.reactor.workzone.dto.SMSRequestDTO;
import com.intrans.reactor.workzone.dto.WorkzoneAlertDTO;
import com.intrans.reactor.workzone.entities.CameraInventory;
import com.intrans.reactor.workzone.entities.SensorEvent;
import com.intrans.reactor.workzone.entities.WorkzoneEvent;
import com.intrans.reactor.workzone.handlers.ODSHandler;
import com.intrans.reactor.workzone.repository.CameraInventoryDao;
import com.intrans.reactor.workzone.sms.process.SMSDistributionService;
import com.intrans.reactor.workzone.utils.ODSUtils;

@Component
public class SMSHandler {

	@Autowired
	private SMSProviderManager smsProviderManager;

	@Autowired
	private SMSDistributionService smsDistributionService;

	@Autowired
	private ODSHandler odsHandler;

	@Autowired
	private WorkzoneConfig workzoneConfig;

	@Autowired
	private CameraInventoryDao cameraInventoryDao;

	/**
	 * Intercepts the workzone event and creates sms request and handovers to
	 * the distribution layer.
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void handleRequest(List<WorkzoneEvent> workzoneEvents, AlertSMSType smsType) throws IOException {
		SMSConfigDTO smsConfig = smsProviderManager.getDefaultSMSConfiguration();
		List<SMSRequestDTO> smsRequests = new ArrayList<>();
		for (WorkzoneEvent workzoneEvent : workzoneEvents) {
			WorkzoneAlertDTO workzoneAlertDTO = odsHandler.prepareWorkzoneAlertDTOs(Arrays.asList(workzoneEvent))
					.stream().findAny().orElse(new WorkzoneAlertDTO());
			String messageContent = odsHandler.prepareWorkzoneAlertMessage(workzoneEvent, workzoneAlertDTO, smsType);
			List<SMSAudience> recipients = ODSUtils.getRecipientsByWorkzone(workzoneConfig.getRecipientsFile(),
					workzoneEvent.getWorkzone());
			CameraInventory cameraInventory = cameraInventoryDao
					.findByDeviceName(StringUtils.deleteWhitespace(workzoneAlertDTO.getCamera()));
			if (cameraInventory == null) {
				SensorEvent sensorEvent = ODSUtils.getStoppedSensorEvents(workzoneEvent, smsType).stream().findAny()
						.get();

				cameraInventory = odsHandler.getCameraInventoryFromSensorName(sensorEvent.getName());
			}
			SMSRequestDTO smsRequestDTO = new SMSRequestDTO();
			smsRequestDTO.setMessage(messageContent);
			smsRequestDTO.setSmsRecipients(recipients);
			if (cameraInventory != null) {
				smsRequestDTO.setMediaUrl(cameraInventory.getImageUrl());
			}
			smsRequests.add(smsRequestDTO);
		}

		smsDistributionService.distribute(smsRequests, smsConfig);

	}

}
