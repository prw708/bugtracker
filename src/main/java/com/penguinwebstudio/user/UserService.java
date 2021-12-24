package com.penguinwebstudio.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	UserRepository userRepository;
	
	@Autowired
	public UserService(UserRepository repository) {
		this.userRepository = repository;
	}
	
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
	public User getUser(String username) {
		return userRepository.findByUsername(username);
	}
	
	public long getCount() {
		return userRepository.count();
	}
}
