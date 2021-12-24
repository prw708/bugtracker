package com.penguinwebstudio.bugtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.penguinwebstudio.logger.LogService;
import com.penguinwebstudio.tickets.TicketController;
import com.penguinwebstudio.user.UserController;

@SpringBootApplication
@ComponentScan(basePackageClasses=UserController.class)
@ComponentScan(basePackageClasses=TicketController.class)
@ComponentScan(basePackageClasses=WebSecurityConfig.class)
@ComponentScan(basePackageClasses=LogService.class)
public class BugTrackerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BugTrackerApplication.class, args);
	}
	
}
