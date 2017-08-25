package com.intrans.reactor.workzone.handlers;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.intrans.reactor.workzone.constants.WorkzoneConstants.AlertSMSType;
import com.intrans.reactor.workzone.dto.WorkzoneAlertDTO;
import com.intrans.reactor.workzone.entities.CameraInventory;
import com.intrans.reactor.workzone.entities.SensorCameraMapping;
import com.intrans.reactor.workzone.entities.SensorEvent;
import com.intrans.reactor.workzone.entities.WorkzoneEvent;
import com.intrans.reactor.workzone.entities.WorkzoneSensorInfo;
import com.intrans.reactor.workzone.repository.CameraInventoryDao;
import com.intrans.reactor.workzone.repository.SensorCameraMappingDao;
import com.intrans.reactor.workzone.repository.WorkzoneSensorInfoDao;
import com.intrans.reactor.workzone.utils.DateUtils;
import com.intrans.reactor.workzone.utils.ODSUtils;

@Component
public class ODSHandler {

	@Autowired
	private CameraInventoryDao cameraInventoryDao;

	@Autowired
	private SensorCameraMappingDao sensorCameraMappingDao;

	@Autowired
	private WorkzoneSensorInfoDao workzoneSensorInfoDao;

	public CameraInventory getCameraInventoryFromSensorName(String sensorName) {
		SensorCameraMapping scm = sensorCameraMappingDao.findBySensorName(StringUtils.deleteWhitespace(sensorName));
		if (scm == null)
			return null;
		return cameraInventoryDao.findByDeviceName(StringUtils.deleteWhitespace(scm.getCameraName()));
	}

	public List<WorkzoneAlertDTO> prepareWorkzoneAlertDTOs(List<WorkzoneEvent> workzoneEvents) throws IOException {
		List<WorkzoneAlertDTO> workzoneAlertDTOs = new ArrayList<>();
		for (WorkzoneEvent workzoneEvent : workzoneEvents) {
			List<SensorEvent> sensorEvents = workzoneEvent.getSensorEvents();
			List<String> sensorNames = new ArrayList<>();
			sensorEvents.forEach(se -> sensorNames.add(StringUtils.deleteWhitespace(se.getName())));
			List<WorkzoneSensorInfo> workzoneSensorInfos = workzoneSensorInfoDao
					.findBySensorNameIgnoreCaseIn(sensorNames);
			WorkzoneSensorInfo workzoneSensorInfo = workzoneSensorInfos.stream().findAny().get();
			WorkzoneAlertDTO workzoneAlertDTO = new WorkzoneAlertDTO();
			workzoneAlertDTO.setWorkzone(workzoneEvent.getWorkzone());
			workzoneAlertDTO.setId(workzoneEvent.getEventId());
			workzoneAlertDTO.setStartTime(DateUtils.getDate(workzoneEvent.getStartTime()));
			// This check is required. For alert feed and alert message, current
			// details are important. But for clearence message these details
			// are irrelevant.
			Predicate<SensorEvent> p1 = (sensorEvents.stream().anyMatch(se -> !se.isCompleted()))
					? se -> !se.isCompleted()
					: se -> true;
			workzoneAlertDTO.setSeverity(
					sensorEvents.stream().filter(p1).map(SensorEvent::getAlert).max(Enum::compareTo).get());
			workzoneAlertDTO.setAvgSpeed((double) sensorEvents.stream().filter(p1).map(SensorEvent::getAvgSped)
					.mapToInt(Double::intValue).min().getAsInt());

			workzoneAlertDTO.setMilepost(new DecimalFormat("#0.0").format(workzoneSensorInfos.stream()
					.map(WorkzoneSensorInfo::getLinearReference).min(Double::compareTo).get()) + " - "
					+ new DecimalFormat("#0.0").format(workzoneSensorInfos.stream()
							.map(WorkzoneSensorInfo::getLinearReference).max(Double::compareTo).get()));
			workzoneAlertDTO.setDirection(workzoneSensorInfo.getDirection());
			workzoneAlertDTO.setRoute(workzoneSensorInfo.getRoute());
			workzoneAlertDTO.setProjectName(workzoneSensorInfo.getProjectName());
			workzoneAlertDTO.setCamera(workzoneSensorInfo.getCamera());
			workzoneAlertDTOs.add(workzoneAlertDTO);
		}
		return workzoneAlertDTOs;
	}

	public String prepareWorkzoneAlertMessage(WorkzoneEvent workzoneEvent, WorkzoneAlertDTO workzoneAlertDTO,
			AlertSMSType smsType) throws IOException {

		List<SensorEvent> sensorEvents = ODSUtils.getStoppedSensorEvents(workzoneEvent, smsType);
		// Prepare message content.
		StringBuilder messageBuilder = new StringBuilder();
		if (smsType == AlertSMSType.ALERT) {
			messageBuilder.append("Work Zone Congestion Alert \n");
			messageBuilder.append(workzoneAlertDTO.getRoute()).append(" ").append(workzoneAlertDTO.getDirection())
					.append(" @ MM ").append(workzoneAlertDTO.getMilepost()).append("\n");
			messageBuilder.append("Began: ").append(DateUtils.formatDate(workzoneEvent.getStartTime(), "h:mm a"))
					.append("\n");
			messageBuilder.append("Current Speeds: ").append(
					sensorEvents.stream().map(SensorEvent::getAvgSped).mapToInt(Double::intValue).min().getAsInt())
					.append(" mph\n");
			messageBuilder.append("Project Name: ").append(workzoneAlertDTO.getProjectName()).append("\n");
			messageBuilder.append("EventID: ").append(workzoneEvent.getEventId());
		} else if (smsType == AlertSMSType.CLEARANCE) {
			// Find the duration in minutes.
			long duration = (workzoneEvent.getEndTime() - workzoneEvent.getStartTime()) / (60 * 1000);
			messageBuilder.append("Congestion Cleared \n");
			messageBuilder.append(workzoneAlertDTO.getRoute()).append(" ").append(workzoneAlertDTO.getDirection())
					.append("\n");
			messageBuilder.append("Duration: ").append(duration).append(" minutes\n");
			messageBuilder.append("Project Name: ").append(workzoneAlertDTO.getProjectName()).append("\n");
			messageBuilder.append("EventID: ").append(workzoneEvent.getEventId());

		}
		return messageBuilder.toString();
	}

}
