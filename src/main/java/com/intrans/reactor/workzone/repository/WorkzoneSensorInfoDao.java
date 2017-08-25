package com.intrans.reactor.workzone.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.intrans.reactor.workzone.entities.WorkzoneSensorInfo;

public interface WorkzoneSensorInfoDao extends MongoRepository<WorkzoneSensorInfo, String> {
	
	WorkzoneSensorInfo findBySensorNameIgnoreCase(String sensorName);
	
	List<WorkzoneSensorInfo> findBySensorNameIgnoreCaseIn(List<String> sensorNames );

}
