package com.penguinwebstudio.user;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
	
	User findByUsername(String username);
	
	List<User> findAll();
	
	public long count();
	
}
