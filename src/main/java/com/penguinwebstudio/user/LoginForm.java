package com.penguinwebstudio.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginForm {
	
	private String lUsername;
		
	@NotBlank
	@Size(min=1, max=50)
	@Pattern(regexp="^[A-Za-z0-9\\-]+$")
	private String lPhone;
	
	@NotBlank
	@Size(min=8, max=50)
	@Pattern(regexp="^[A-Za-z0-9!@#\\$%\\^&\\*]+$")
	private String lField2;
	
	@NotBlank
	@Size(min=1, max=1000)
	@Pattern(regexp="^[A-Za-z0-9\\-_]+$")
	private String lRecaptcha;
	
	public String getlUsername() {
		return this.lUsername;
	}
	
	public void setlUsernamePhone(String data) {
		this.lUsername = data;
	}
	
	public String getlPhone() {
		return this.lPhone;
	}
	
	public void setlPhone(String username) {
		this.lPhone = username;
	}
	
	public String getlField2() {
		return this.lField2;
	}
	
	public void setlField2(String password) {
		this.lField2 = password;
	}
	
	public String getlRecaptcha() {
		return this.lRecaptcha;
	}
	
	public void setlRecaptcha(String recaptcha) {
		this.lRecaptcha = recaptcha;
	}
	
}
