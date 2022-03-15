package com.penguinwebstudio.tickets;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class DeleteCommentForm {
	
	private String dEmail;
	
	@NotBlank
	@Pattern(regexp="^[A-Za-z0-9\\-_]+$")
	String ticketId;
	
	@NotNull
	@Min(0)
	@Digits(integer=3, fraction=0)
	int threadIndex;
	
	@NotBlank
	@Pattern(regexp="^[A-Za-z0-9\\-_]+$")
	String commentId;
	
	@NotBlank 
	@Size(min=1, max=1000) 
	@Pattern(regexp="^[A-Za-z0-9\\-_]+$")
	String recaptcha;
	
	public void setdEmail(String data) {
		this.dEmail = data;
	}
	
	public String getdEmail() {
		return this.dEmail;
	}
	
	public void setTicketId(String id) {
		this.ticketId = id;
	}
	
	public String getTicketId() {
		return this.ticketId;
	}
	
	public void setThreadIndex(int index) {
		this.threadIndex = index;
	}
	
	public int getThreadIndex() {
		return this.threadIndex;
	}
	
	public void setCommentId(String id) {
		this.commentId = id;
	}
	
	public String getCommentId() {
		return this.commentId;
	}
	
	public void setRecaptcha(String recaptcha) {
		this.recaptcha = recaptcha;
	}
	
	public String getRecaptcha() {
		return this.recaptcha;
	}
	
}
