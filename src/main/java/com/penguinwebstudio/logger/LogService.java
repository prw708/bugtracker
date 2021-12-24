package com.penguinwebstudio.logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;

@Service
public class LogService {
	
	LogRepository logRepository;
	
	@Autowired
	public LogService(LogRepository repository) {
		this.logRepository = repository;
	}
	
	public void addLog(Log log) {
		logRepository.save(log);
	}
	
}
