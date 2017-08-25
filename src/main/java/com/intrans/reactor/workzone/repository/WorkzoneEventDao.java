package com.intrans.reactor.workzone.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.intrans.reactor.workzone.entities.WorkzoneEvent;

public interface WorkzoneEventDao extends MongoRepository<WorkzoneEvent, String> {

	@Query(value = "{'workzone':'?0','completed':false}")
	WorkzoneEvent getCurrentRunningEvent(String workzone);

	List<WorkzoneEvent> findByCompletedIsFalse();

	Page<WorkzoneEvent> findByWorkzone(String workzone, Pageable pageable);

	List<WorkzoneEvent> findByWorkzoneInAndCompletedIsFalse(Collection<String> workzones);
}
