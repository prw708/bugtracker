package com.penguinwebstudio.tickets;

import java.util.ArrayList;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("tickets")
public class Ticket {
	private ObjectId id;
	private String title;
	private String reporter;
	private String creator;
	private Date createdOn;
	private String severity;
	private String status;
	private String description;
	private ArrayList<ArrayList<Comment>> comments;
	
	public Ticket(
			String title, 
			String reporter, 
			String creator,
			Date createdOn,
			String severity,
			String status,
			String description) {
		this.setTitle(title);
		this.setReporter(reporter);
		this.setCreator(creator);
		this.setCreatedOn(createdOn);
		this.setSeverity(severity);
		this.setStatus(status);
		this.setDescription(description);
		this.comments = new ArrayList<ArrayList<Comment>>();
	}
	
	public ObjectId getId() {
		return this.id;
	}
	
	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	
	public String getReporter() {
		return this.reporter;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getCreator() {
		return this.creator;
	}
	
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	public Date getCreatedOn() {
		return this.createdOn;
	}
	
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	
	public String getSeverity() {
		return this.severity;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void addComment(int index, Comment comment) {
		if (this.comments.isEmpty() || index >= this.comments.size()) {
			this.comments.add(new ArrayList<Comment>());
			this.comments.get(index).add(comment);
		} else {
			this.comments.get(index).add(comment);
		}
	}
	
	public void deleteComment(int index, int id) {
		ArrayList<Comment> thread = this.comments.get(index);
		for (int i = 0; i < thread.size(); i++) {
			if (thread.get(i).getId() == id) {
				thread.remove(i);
				if (thread.isEmpty()) {
					this.comments.remove(index);
				}
				break;
			}
		}
	}
	
	public int countComments() {
		int size = 0;
		for (ArrayList<Comment> c : this.comments) {
			size += c.size();
		}
		return size;
	}
	
	public ArrayList<ArrayList<Comment>> getComments() {
		return this.comments;
	}
	
}
