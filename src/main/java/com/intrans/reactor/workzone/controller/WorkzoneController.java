package com.intrans.reactor.workzone.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.intrans.reactor.handlers.AsyncCallBack;
import com.intrans.reactor.handlers.Callback;
import com.intrans.reactor.workzone.constants.WorkzoneConfig;
import com.intrans.reactor.workzone.constants.WorkzoneConstants.AlertSMSType;
import com.intrans.reactor.workzone.constants.WorkzoneConstants.WorkzoneAlertStatus;
import com.intrans.reactor.workzone.dto.WorkzoneAlertFeedRequest;
import com.intrans.reactor.workzone.entities.SensorEvent;
import com.intrans.reactor.workzone.entities.WorkzoneEvent;
import com.intrans.reactor.workzone.repository.WorkzoneEventDao;
import com.intrans.reactor.workzone.sms.SMSHandler;
import com.intrans.reactor.workzone.utils.DateUtils;
import com.intrans.reactor.workzone.utils.ODSUtils;

/**
 * Consumer controller for receiving and processing the work zone statuses.
 * 
 * @author krishnaj
 *
 */
@Controller
@RequestMapping(value = WorkzoneController.ROOT)
public class WorkzoneController {
	public static final String ROOT = "/workzone";

	@Autowired
	private WorkzoneConfig workzoneConfig;

	@Autowired
	private WorkzoneEventDao workzoneEventDao;

	@Autowired
	private SMSHandler smsHandler;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	private BlockingQueue<String> alertEventQueue = new ArrayBlockingQueue<>(1024);
	private BlockingQueue<String> clearanceEventQueue = new ArrayBlockingQueue<>(1024);

	@PostConstruct
	public void onStartUp() {
		try {
			// Add already running events to the queues
			List<WorkzoneEvent> workzoneEvents = workzoneEventDao.findByCompletedIsFalse();
			if (!CollectionUtils.isEmpty(workzoneEvents)) {
				List<String> alertEvents = workzoneEvents.stream().filter(we -> !we.isAlertSmsSent())
						.map(WorkzoneEvent::getWorkzone).collect(Collectors.toList());
				List<String> clearanceEvents = workzoneEvents.stream()
						.filter(we -> we.isAlertSmsSent() && !we.isClearanceSmsSent()).map(WorkzoneEvent::getWorkzone)
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(alertEvents)) {
					alertEventQueue.addAll(alertEvents);
				}
				if (!CollectionUtils.isEmpty(clearanceEvents)) {
					clearanceEvents.addAll(clearanceEvents);
				}
			}
		} catch (Exception e) {
			System.out.println("Invocation of startup method failed in workzone controller.");
			e.printStackTrace();
		}

	}

	/**
	 * Consume the current workzone status.
	 */
	@PostMapping("/feeds/consume")
	public ResponseEntity<String> consumeWorkzoneFeed(@RequestBody WorkzoneAlertFeedRequest alertFeedReq)
			throws IOException {
		WorkzoneAlertStatus alertStatus = WorkzoneAlertStatus.getWorkzoneAlertStatus(alertFeedReq.getAlert());
		ResponseEntity<String> response = new ResponseEntity<String>(
				ODSUtils.generateJSONResponse(StringUtils.EMPTY, "Ignoring BLANK alert."), HttpStatus.OK);
		// Get Current running event
		WorkzoneEvent workzoneEvent = workzoneEventDao.getCurrentRunningEvent(alertFeedReq.getWorkzone());

		if (workzoneEvent == null) {
			if (alertStatus != WorkzoneAlertStatus.BLANK) {
				Long latestEventId = workzoneEventDao.count() + 1;
				workzoneEvent = new WorkzoneEvent(alertFeedReq.getWorkzone(), latestEventId);
				SensorEvent sensorEvent = new SensorEvent(alertFeedReq.getDevice(), alertStatus,
						alertFeedReq.getAvgSpeed());
				workzoneEvent.setSensorEvents(Arrays.asList(sensorEvent));
				alertEventQueue.add(workzoneEvent.getWorkzone());
				response = new ResponseEntity<String>(ODSUtils.generateJSONResponse(String.valueOf(latestEventId),
						"Created a new Event for this workzone."), HttpStatus.OK);
			} else {
				return response;
			}
		} else {
			// An event already running for this workzone.
			SensorEvent sensorEvent = extractSensorEventByName(workzoneEvent, alertFeedReq.getDevice());
			if (sensorEvent == null) {
				if (alertStatus != WorkzoneAlertStatus.BLANK) {
					sensorEvent = new SensorEvent(alertFeedReq.getDevice(), alertStatus, alertFeedReq.getAvgSpeed());
					workzoneEvent.getSensorEvents().add(sensorEvent);
					// reset workzone endtime.
					workzoneEvent.setEndTime(null);
					response = new ResponseEntity<String>(
							ODSUtils.generateJSONResponse(workzoneEvent.getEventId().toString(),
									"Added a new sensor event for this workzone."),
							HttpStatus.OK);
				} else {
					return response;
				}
			} else {
				if (alertStatus == WorkzoneAlertStatus.BLANK) {
					sensorEvent.setCompleted(true);
					sensorEvent.setEndTime(DateUtils.getCurrentTime());
					// If All sensor events are completed, then end this current
					// event
					if (!workzoneEvent.getSensorEvents().stream().filter(se -> !se.isCompleted()).findAny()
							.isPresent()) {
						workzoneEvent.setEndTime(DateUtils.getCurrentTime());
						// TODO - Set completed?
					}
					response = new ResponseEntity<String>(
							ODSUtils.generateJSONResponse(workzoneEvent.getEventId().toString(), "Ended Sensor Event."),
							HttpStatus.OK);
				} else {
					if (sensorEvent.getAlert() == alertStatus) {
						sensorEvent.setAvgSped(alertFeedReq.getAvgSpeed());
						response = new ResponseEntity<String>(ODSUtils.generateJSONResponse(
								workzoneEvent.getEventId().toString(), "Updated Sensor Event."), HttpStatus.OK);
					} else {
						sensorEvent.setCompleted(true);
						sensorEvent.setEndTime(DateUtils.getCurrentTime());
						SensorEvent nSensorEvent = new SensorEvent(alertFeedReq.getDevice(), alertStatus,
								alertFeedReq.getAvgSpeed());
						workzoneEvent.getSensorEvents().add(nSensorEvent);
						response = new ResponseEntity<String>(ODSUtils.generateJSONResponse(
								workzoneEvent.getEventId().toString(), "Ended " + sensorEvent.getAlert()
										+ " sensor event and Added " + alertStatus + " sensor event"),
								HttpStatus.OK);
					}
				}
				sensorEvent.setLastUpdateTime(DateUtils.getCurrentTime());

			}
			workzoneEvent.setLastUpdateTime(DateUtils.getCurrentTime());
		}
		workzoneEventDao.save(workzoneEvent);
		return response;
	}

	@Scheduled(cron = "0 */1 * * * *")
	public void checkWorkzoneAlerts() {
		if (!alertEventQueue.isEmpty()) {
			Set<String> workzones = new HashSet<>();
			alertEventQueue.drainTo(workzones);
			List<WorkzoneEvent> workzoneEvents = workzoneEventDao.findByWorkzoneInAndCompletedIsFalse(workzones);
			// Completed Events
			List<WorkzoneEvent> completedWorkzoneEvents = workzoneEvents.stream()
					.filter(we -> we.getEndTime() != null && we.getEndTime() <= DateUtils
							.addSeconds(DateUtils.getCurrentDate(), -workzoneConfig.getMinClearanceDuration())
							.getTime())
					.collect(Collectors.toList());
			if (!completedWorkzoneEvents.isEmpty()) {
				completedWorkzoneEvents.forEach(cwe -> {
					cwe.setCompleted(true);
					cwe.setLastUpdateTime(DateUtils.getCurrentTime());
				});
				workzoneEventDao.save(completedWorkzoneEvents);
				workzones.removeAll(
						completedWorkzoneEvents.stream().map(WorkzoneEvent::getWorkzone).collect(Collectors.toSet()));
			}
			// STOP Events
			List<WorkzoneEvent> stoppedWorkzoneEvents = workzoneEvents
					.stream().filter(
							we -> we.getSensorEvents().stream()
									.filter(se -> !se.isCompleted() && se.getAlert() == WorkzoneAlertStatus.STOP)
									.map(SensorEvent::getStartTime).min(Long::compareTo).orElse(Long.MAX_VALUE)
									.longValue() <= DateUtils.addSeconds(DateUtils.getCurrentDate(),
											-workzoneConfig.getMinAlertDuration()).getTime())
					.collect(Collectors.toList());
			if (!stoppedWorkzoneEvents.isEmpty()) {
				triggerSMS(stoppedWorkzoneEvents, AlertSMSType.ALERT);
				stoppedWorkzoneEvents.forEach(swe -> {
					swe.setAlertSmsSent(true);
					swe.setLastUpdateTime(DateUtils.getCurrentTime());
				});
				workzoneEventDao.save(stoppedWorkzoneEvents);
				Set<String> stopWorkzones = stoppedWorkzoneEvents.stream().map(WorkzoneEvent::getWorkzone)
						.collect(Collectors.toSet());
				workzones.removeAll(stopWorkzones);
				clearanceEventQueue.addAll(stopWorkzones);
			}
			alertEventQueue.addAll(workzones);
		}
	}

	@Scheduled(cron = "0 */1 * * * *")
	public void checkWorkzoneClearance() {
		if (!clearanceEventQueue.isEmpty()) {
			Set<String> workzones = new HashSet<>();
			clearanceEventQueue.drainTo(workzones);
			List<WorkzoneEvent> workzoneEvents = workzoneEventDao.findByWorkzoneInAndCompletedIsFalse(workzones);
			// Completed Events
			List<WorkzoneEvent> completedWorkzoneEvents = workzoneEvents.stream()
					.filter(we -> we.getEndTime() != null && we.getEndTime() <= DateUtils
							.addSeconds(DateUtils.getCurrentDate(), -workzoneConfig.getMinClearanceDuration())
							.getTime())
					.collect(Collectors.toList());
			if (!completedWorkzoneEvents.isEmpty()) {
				triggerSMS(completedWorkzoneEvents, AlertSMSType.CLEARANCE);
				completedWorkzoneEvents.forEach(cwe -> {
					cwe.setClearanceSmsSent(true);
					cwe.setCompleted(true);
					cwe.setLastUpdateTime(DateUtils.getCurrentTime());
				});
				workzoneEventDao.save(completedWorkzoneEvents);
				workzones.removeAll(
						completedWorkzoneEvents.stream().map(WorkzoneEvent::getWorkzone).collect(Collectors.toSet()));
			}
			clearanceEventQueue.addAll(workzones);

		}

	}

	public void triggerSMS(List<WorkzoneEvent> workzoneEvents, AlertSMSType smsType) {
		new AsyncCallBack(new Callback() {

			@Override
			public void call() {
				try {
					smsHandler.handleRequest(workzoneEvents, smsType);
				} catch (IOException e) {
					// TODO - Log
					e.printStackTrace();
				}

			}
		}).execute(taskExecutor);

	}

	private SensorEvent extractSensorEventByName(WorkzoneEvent workzoneEvent, String sensorName) {
		return workzoneEvent.getSensorEvents().stream()
				.filter(se -> !se.isCompleted() && StringUtils.equalsIgnoreCase(se.getName(), sensorName)).findFirst()
				.orElse(null);
	}

}
