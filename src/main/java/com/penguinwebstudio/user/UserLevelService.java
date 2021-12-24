package com.penguinwebstudio.user;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLevelService {
	
	UserLevelRepository userLevelRepository;
	
	@Autowired
	public UserLevelService(UserLevelRepository repository) {
		this.userLevelRepository = repository;
	}
	
	public UserLevel updateUserLevelByUser(String user) {
		UserLevel userLevel = this.getUser(user);
		userLevel.setExperience(userLevel.getExperience() + 25);
		userLevelRepository.save(userLevel);
		return userLevel;
	}
	
	public List<UserLevel> getUsers() {
		return userLevelRepository.findAll();
	}
	
	public UserLevel getUser(String user) {
		ObjectId objectId = new ObjectId(user);
		return userLevelRepository.findByUser(objectId);
	}
	
	public long getCount() {
		return userLevelRepository.count();
	}

}