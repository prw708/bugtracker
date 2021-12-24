package com.penguinwebstudio.logger;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<Log, String> {

	public Optional<Log> findById(String id);
		
	public void deleteById(String id);
				
	public long count();
			
}
