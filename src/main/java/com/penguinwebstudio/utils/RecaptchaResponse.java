package com.penguinwebstudio.utils;

import java.sql.Timestamp;

public class RecaptchaResponse {

	private boolean success;
	private double score;
	private String action;
	private Timestamp challenge_ts;
	private String hostname;
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
}
