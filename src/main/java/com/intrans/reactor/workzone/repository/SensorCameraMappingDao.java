package com.intrans.reactor.workzone.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.intrans.reactor.workzone.entities.SensorCameraMapping;

public interface SensorCameraMappingDao extends MongoRepository<SensorCameraMapping, String> {

	SensorCameraMapping findBySensorName(String sensorName);
}
