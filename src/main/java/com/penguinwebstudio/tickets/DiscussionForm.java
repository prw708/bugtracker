package com.penguinwebstudio.tickets;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class DiscussionForm {

	private String dUsername;
	
	@NotNull
	@Min(-1)
	@Digits(integer=3, fraction=0)
	int threadIndex;
	
	@Size(min=1, max=1000)
	@Pattern(regexp="^[A-Za-z0-9!@#\\$%\\^&\\* \\.,\\?'\"\r\n]+$")
	private String dName;
	
	public String getdUsername() {
		return dUsername;
	}
	
	public void setdUsername(String data) {
		this.dUsername = data;
	}
	
	public void setThreadIndex(int index) {
		this.threadIndex = index;
	}
	
	public int getThreadIndex() {
		return this.threadIndex;
	}
	
	public String getdName() {
		return this.dName;
	}
	
	public void setdName(String comment) {
		this.dName = comment;
	}
	
}
