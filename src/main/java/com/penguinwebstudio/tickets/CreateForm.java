package com.penguinwebstudio.tickets;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class CreateForm {
	
	private String cEmail;
	
	@NotBlank
	@Size(min=1, max=200)
	@Pattern(regexp="^[A-Za-z0-9\\- \\.,!]+$")
	private String cSubtitle;
	
	@NotBlank
	@Size(min=1, max=50)
	@Pattern(regexp="^[A-Za-z0-9\\- \\.,!]+$")
	private String cCreator;
	
	@NotBlank
	@Size(min=1, max=50)
	@Pattern(regexp="^[A-Za-z0-9\\- \\.,!]+$")
	private String cStatus;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private Date cTitle;
	
	@NotBlank
	@Size(min=1, max=50)
	@Pattern(regexp="^[A-Za-z0-9\\- \\.,!]+$")
	private String cDetail;
	
	@NotBlank
	@Size(min=1, max=50)
	@Pattern(regexp="^[A-Za-z0-9\\- \\.,!]+$")
	private String cReporter;
	
	@Size(min=0, max=1000)
	@Pattern(regexp="^[A-Za-z0-9!@#\\$%\\^&\\* \\.,\\?'\"\r\n]*$")
	private String cExplain;
	
	public String getcEmail() {
		return cEmail;
	}
	
	public void setcEmail(String email) {
		this.cEmail = email;
	}
	
	public String getcSubtitle() {
		return cSubtitle;
	}
	
	public void setcSubtitle(String title) {
		this.cSubtitle = title;
	}
	
	public String getcCreator() {
		return cCreator;
	}
	
	public void setcCreator(String reporter) {
		this.cCreator = reporter;
	}
	
	public String getcStatus() {
		return cStatus;
	}
	
	public void setcStatus(String creator) {
		this.cStatus = creator;
	}
	
	public Date getcTitle() {
		return cTitle;
	}
	
	public void setcTitle(Date createdOn) {
		this.cTitle = createdOn;
	}
	
	public String getcDetail() {
		return cDetail;
	}
	
	public void setcDetail(String severity) {
		this.cDetail = severity;
	}
	
	public String getcReporter() {
		return cReporter;
	}
	
	public void setcReporter(String status) {
		this.cReporter = status;
	}
	
	public String getcExplain() {
		return cExplain;
	}
	
	public void setcExplain(String description) {
		this.cExplain = description;
	}
	
}