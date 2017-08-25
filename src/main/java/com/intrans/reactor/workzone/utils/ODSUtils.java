package com.intrans.reactor.workzone.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import com.intrans.reactor.workzone.constants.WorkzoneConstants.AlertSMSType;
import com.intrans.reactor.workzone.constants.WorkzoneConstants.WorkzoneAlertStatus;
import com.intrans.reactor.workzone.dto.SMSAudience;
import com.intrans.reactor.workzone.entities.SensorEvent;
import com.intrans.reactor.workzone.entities.WorkzoneEvent;
import com.intrans.reactor.workzone.entities.WorkzoneSensorInfo;

/**
 * Utility class.
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Feb 6, 2017
 *
 */
public class ODSUtils {

	/**
	 * Returns all SMS recipients
	 * 
	 * @return
	 * @throws IOException
	 */
	public static List<SMSAudience> getAllRecipients(String filePath) throws IOException {
		SMSAudience[] test = TimeliUtils
				.readJSONValue(IOUtils.toString(new ClassPathResource(filePath).getInputStream()), SMSAudience[].class);
		return Arrays.asList(test);
	}

	/**
	 * Returns all SMS recipients that are associated with a workzone.
	 * 
	 * @param workzoneName
	 * @return
	 * @throws IOException
	 */
	public static List<SMSAudience> getRecipientsByWorkzone(String filePath, final String workzoneName)
			throws IOException {
		List<SMSAudience> allRecipients = getAllRecipients(filePath);
		List<SMSAudience> workzoneRecipients = new ArrayList<>();
		for (SMSAudience recipient : allRecipients) {
			if (recipient.isSendAll()) {
				workzoneRecipients.add(recipient);
			} else if (recipient.getWorkzones().stream().anyMatch(s -> s.equalsIgnoreCase(workzoneName))) {
				workzoneRecipients.add(recipient);
			}
		}
		return workzoneRecipients;
	}

	public static String generateJSONResponse(String id, String description) {
		Map<String, String> response = new HashMap<>();
		response.put("id", id);
		response.put("details", description);
		try {
			return TimeliUtils.writeValueAsJSON(response);
		} catch (IOException e) {
			return "Unable to generate Response";
		}
	}

	public static List<WorkzoneSensorInfo> createWorkzoneSensorInfoFromFile() throws IOException {
		List<WorkzoneSensorInfo> workzoneSensorInfoList = new ArrayList<>();
		Reader in = new InputStreamReader(new ClassPathResource("workzoneSensors.csv").getInputStream());
		Iterator<CSVRecord> records = CSVUtils.parseCSVFileWithHeaders(in).iterator();
		WorkzoneSensorInfo workzoneSensorInfo = null;
		while (records.hasNext()) {
			CSVRecord record = records.next();
			workzoneSensorInfo = new WorkzoneSensorInfo();
			workzoneSensorInfo.setSensorId(record.get("ID"));
			workzoneSensorInfo.setSensorName(StringUtils.deleteWhitespace(record.get("Sensor")));
			workzoneSensorInfo.setLattitude(Double.parseDouble(record.get("Latitude")));
			workzoneSensorInfo.setLongitude(Double.parseDouble(record.get("Longitude")));
			workzoneSensorInfo.setLinearReference(Double.parseDouble(record.get("milepost")));
			workzoneSensorInfo.setRoute(record.get("route"));
			workzoneSensorInfo.setDirection(record.get("direction"));
			workzoneSensorInfo.setProjectName(record.get("name"));
			workzoneSensorInfo.setGroup(record.get("Group"));
			workzoneSensorInfo.setCamera(record.get("Camera"));
			workzoneSensorInfoList.add(workzoneSensorInfo);

		}
		return workzoneSensorInfoList;
	}

	public static List<SensorEvent> getStoppedSensorEvents(WorkzoneEvent workzoneEvent, AlertSMSType alertType) {
		List<SensorEvent> sensorEvents = new ArrayList<>();
		if (alertType == AlertSMSType.ALERT) {
			sensorEvents = workzoneEvent.getSensorEvents().stream()
					.filter(se -> !se.isCompleted() && se.getAlert() == WorkzoneAlertStatus.STOP)
					.collect(Collectors.toList());
		} else if (alertType == AlertSMSType.CLEARANCE) {
			sensorEvents = workzoneEvent.getSensorEvents().stream()
					.filter(se -> se.getAlert() == WorkzoneAlertStatus.STOP).collect(Collectors.toList());
		}
		return sensorEvents;
	}



}
