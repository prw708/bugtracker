package com.penguinwebstudio.tickets;

import java.util.ArrayList;

public class CommentThread {

	private String id;
	private int index;
	private String ticketTitle;
	private ArrayList<Comment> comments;
	
	public CommentThread(String id, int index, String ticketTitle, ArrayList<Comment> comments) {
		this.id = id;
		this.index = index;
		this.ticketTitle = ticketTitle;
		this.comments = comments;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public void setTicketTitle(String title) {
		this.ticketTitle = title;
	}
	
	public String getTicketTitle() {
		return this.ticketTitle;
	}
	
	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}
	
	public ArrayList<Comment> getComments() {
		return this.comments;
	}
	
}
