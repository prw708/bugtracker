package com.penguinwebstudio.tickets;

import java.util.Date;

public class Comment {

	private int id;
	private String comment;
	private String author;
	private Date postedOn;
	
	public Comment(int id, String comment, String author, Date postedOn) {
		this.id = id;
		this.comment = comment;
		this.author = author;
		this.postedOn = postedOn;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getComment() {
		return this.comment;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}
	
	public Date getPostedOn() {
		return this.postedOn;
	}
	
}
