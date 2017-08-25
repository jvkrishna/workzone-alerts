package com.intrans.reactor.workzone.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.intrans.reactor.workzone.entities.CameraInventory;

public interface CameraInventoryDao extends MongoRepository<CameraInventory, String> {

	CameraInventory findByDeviceName(String deviceName);

}
