package com.penguinwebstudio.user;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public class User {
	private ObjectId id;
	private String username;
	private String email;
	private String password;
	private Date lastLogin;
	private boolean emailConfirmed;
	private boolean active;
	private boolean admin;
	private String customerId;
	
	public User(String username, 
			String password,
			String email, 
			Date lastLogin,
			boolean emailConfirmed, 
			boolean active, 
			boolean admin, 
			String customerId) {
		this.setUsername(username);
		this.setPassword(password);
		this.setEmail(email);
		this.setLastLogin(lastLogin);
		this.setEmailConfirmed(emailConfirmed);
		this.setActive(active);
		this.setAdmin(admin);
		this.setCustomerId(customerId);
	}

	public ObjectId getId() {
		return this.id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public boolean isEmailConfirmed() {
		return emailConfirmed;
	}

	public void setEmailConfirmed(boolean emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
}
