package com.penguinwebstudio.tickets;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class DeleteForm {
	
	private String dEmail;
	
	@NotBlank
	@Pattern(regexp="^[A-Za-z0-9\\-_]+$")
	String ticketId;
	
	@NotBlank 
	@Size(min=1, max=500) 
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
	
	public void setRecaptcha(String recaptcha) {
		this.recaptcha = recaptcha;
	}
	
	public String getRecaptcha() {
		return this.recaptcha;
	}
	
}