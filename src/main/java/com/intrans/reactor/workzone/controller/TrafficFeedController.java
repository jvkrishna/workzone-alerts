package com.intrans.reactor.workzone.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.intrans.reactor.workzone.dto.WorkzoneAlertDTO;
import com.intrans.reactor.workzone.dto.WorkzoneAlertsFeedResponse;
import com.intrans.reactor.workzone.entities.SensorCameraMapping;
import com.intrans.reactor.workzone.entities.WorkzoneEvent;
import com.intrans.reactor.workzone.entities.WorkzoneSensorInfo;
import com.intrans.reactor.workzone.handlers.ODSHandler;
import com.intrans.reactor.workzone.repository.SensorCameraMappingDao;
import com.intrans.reactor.workzone.repository.WorkzoneEventDao;
import com.intrans.reactor.workzone.repository.WorkzoneSensorInfoDao;
import com.intrans.reactor.workzone.utils.ODSUtils;
import com.intrans.reactor.workzone.utils.TimeliUtils;

/**
 * Controller class for producing REST APIs.
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Feb 6, 2017
 *
 */
@Controller
@RequestMapping(value = TrafficFeedController.ROOT)
public class TrafficFeedController {

	public static final String ROOT = "/feeds";
	public static final String WORKZONE_ALERTS_FEED_URL = "/alertfeed";

	@Autowired
	private ODSHandler odsHandler;

	@Autowired
	private WorkzoneEventDao workzoneEventDao;

	@Autowired
	private SensorCameraMappingDao sensorCameraMappingDao;
	@Autowired
	private WorkzoneSensorInfoDao workzoneSensorInfoDao;
	
	@RequestMapping("")
	public @ResponseBody String home() {
		return "yes";
	}

	/**
	 * Generates real time workzone alerts feed
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = WORKZONE_ALERTS_FEED_URL, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public WorkzoneAlertsFeedResponse getAllWorkzAlertsFeed() throws IOException {
		List<WorkzoneEvent> workzoneEvents = workzoneEventDao.findByCompletedIsFalse();
		// Filter only running events. We should not completely rely only on the
		// completed flag.
		List<WorkzoneEvent> runningWorkzoneEvents = workzoneEvents.stream()
				.filter(we -> we.getSensorEvents().stream().anyMatch(se -> !se.isCompleted()))
				.collect(Collectors.toList());
		List<WorkzoneAlertDTO> workzoneAlertDtos = odsHandler.prepareWorkzoneAlertDTOs(runningWorkzoneEvents);
		WorkzoneAlertsFeedResponse workzoneAlertsFeedResponse = new WorkzoneAlertsFeedResponse();
		workzoneAlertsFeedResponse.setAlerts(workzoneAlertDtos);
		return workzoneAlertsFeedResponse;
	}

	@GetMapping("/sensorcamera/update")
	public ResponseEntity<String> updateSensorCameraRelation() throws IOException {
		String csvFile = "sensor_camera_2017.csv";
		try {
			List<SensorCameraMapping> sensorCameraMappingList = TimeliUtils.createSensorCameraMappingFromCSV(csvFile);
			sensorCameraMappingDao.deleteAll();
			sensorCameraMappingDao.insert(sensorCameraMappingList);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (IOException ie) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/workzonesensor/update")
	public ResponseEntity<String> updateWorkzoneSensorinfo() {
		try {
			List<WorkzoneSensorInfo> workzoneSensorInfos = ODSUtils.createWorkzoneSensorInfoFromFile();
			workzoneSensorInfoDao.deleteAll();
			workzoneSensorInfoDao.save(workzoneSensorInfos);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/wzevents")
	public ResponseEntity<List<WorkzoneEvent>> getAllEventInfo() throws Exception {
		return new ResponseEntity<>(workzoneEventDao.findAll(), HttpStatus.OK);
	}
}
