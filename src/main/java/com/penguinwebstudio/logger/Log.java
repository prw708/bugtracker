package com.penguinwebstudio.logger;

import java.util.Date;

import org.bson.types.ObjectId;

public class Log {

	private ObjectId id;
	private Date timestamp;
	private String level;
	private String message;
	private String meta;
	
	public Log(
			Date timestamp,
			String level,
			String message,
			String meta
	) {
		this.timestamp = timestamp;
		this.level = level;
		this.message = message;
		this.meta = meta;
	}
	
	public ObjectId getId() {
		return this.id;
	}
	
	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public Date getTimestamp() {
		return this.timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getLevel() {
		return this.level;
	}
	
	public void setLevel(String level) {
		this.level = level;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMeta() {
		return this.meta;
	}
	
	public void setMeta(String meta) {
		this.meta = meta;
	}
	
}
