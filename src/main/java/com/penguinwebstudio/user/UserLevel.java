package com.penguinwebstudio.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("userlevels")
public class UserLevel {
	private ObjectId id;
	private ObjectId user;
	private int maxSurveys;
	private int maxQuestions;
	private int maxTokens;
	private int maxBoards;
	private int maxProducts;
	private int maxTickets;
	private int level;
	private int experience;
	private int experiencePerLevel;
	
	public UserLevel(ObjectId user, 
			int maxSurveys, 
			int maxQuestions, 
			int maxTokens,
			int maxBoards,
			int maxProducts,
			int maxTickets,
			int level,
			int experience,
			int experiencePerLevel) {
		this.setUser(user);
		this.setMaxSurveys(maxSurveys);
		this.setMaxQuestions(maxQuestions);
		this.setMaxTokens(maxTokens);
		this.setMaxBoards(maxBoards);
		this.setMaxProducts(maxProducts);
		this.setMaxTickets(maxTickets);
		this.setLevel(level);
		this.setExperience(experience);
		this.setExperiencePerLevel(experiencePerLevel);
	}
	
	public ObjectId getId() {
		return this.id;
	}
	
	public void setId(ObjectId id) {
		this.id = id;
	}

	public ObjectId getUser() {
		return this.user;
	}

	public void setUser(ObjectId id) {
		this.user = id;
	}
	
	public int getMaxSurveys() {
		return this.maxSurveys;
	}

	public void setMaxSurveys(int max) {
		this.maxSurveys = max;
	}

	public int getMaxQuestions() {
		return this.maxQuestions;
	}

	public void setMaxQuestions(int max) {
		this.maxQuestions = max;
	}
	
	public int getMaxTokens() {
		return this.maxTokens;
	}

	public void setMaxTokens(int max) {
		this.maxTokens = max;
	}
	
	public int getMaxBoards() {
		return this.maxBoards;
	}

	public void setMaxBoards(int max) {
		this.maxBoards = max;
	}
	
	public int getMaxProducts() {
		return this.maxProducts;
	}

	public void setMaxProducts(int max) {
		this.maxProducts = max;
	}
	
	public int getMaxTickets() {
		return this.maxTickets;
	}

	public void setMaxTickets(int max) {
		this.maxTickets = max;
	}
	
	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getExperience() {
		return this.experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}
	
	public int getExperiencePerLevel() {
		return this.experiencePerLevel;
	}

	public void setExperiencePerLevel(int experience) {
		this.experiencePerLevel = experience;
	}
	
}