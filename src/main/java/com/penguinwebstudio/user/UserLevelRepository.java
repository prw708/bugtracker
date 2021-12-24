package com.penguinwebstudio.user;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserLevelRepository extends MongoRepository<UserLevel, String> {
	
	UserLevel findByUser(ObjectId user);
	
	List<UserLevel> findAll();
	
	public long count();
	
}