package com.penguinwebstudio.bugtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.ContentSecurityPolicyConfig;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.DispatcherServlet;

import com.penguinwebstudio.user.UserService;
import com.penguinwebstudio.logger.LogService;
import com.penguinwebstudio.logger.Logger;
import com.penguinwebstudio.user.CustomAuthenticationProvider;
import com.penguinwebstudio.user.LoginAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserService userService;
	@Autowired
	CustomAuthenticationProvider authProvider;
	
	@Bean
	public LoginAuthenticationFilter getLoginAuthenticationFilter() throws Exception {
		LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter();
		loginAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
		loginAuthenticationFilter.setUsernameParameter("lPhone");
		loginAuthenticationFilter.setPasswordParameter("lField2");
		loginAuthenticationFilter.setRequiresAuthenticationRequestMatcher(
				new AntPathRequestMatcher("/account/login", "POST")
		);
		loginAuthenticationFilter.setAuthenticationFailureHandler(
				new SimpleUrlAuthenticationFailureHandler("/account/login?error")
		);
		return loginAuthenticationFilter;
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers()
			.referrerPolicy(ReferrerPolicy.ORIGIN)
			.and()
			.contentSecurityPolicy(contentSecurityPolicy -> contentSecurityPolicy
					.policyDirectives("font-src 'self' data: fonts.gstatic.com")
					.policyDirectives("style-src 'self' fonts.googleapis.com www.gstatic.com")
					.policyDirectives("script-src 'self' www.gstatic.com/recaptcha/ www.google.com/recaptcha/")
					.policyDirectives("connect-src 'self' www.google.com/recaptcha/")
					.policyDirectives("frame-src recaptcha.google.com/recaptcha/ www.google.com/recaptcha/")
					.policyDirectives("img-src 'self' data:")
					.policyDirectives("media-src 'none'")
					.policyDirectives("object-src 'none'")
					.policyDirectives("form-action 'self'")
					.policyDirectives("frame-ancestors 'none'")
			);
		http.addFilterBefore(getLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		http.authorizeRequests()
				.antMatchers("/", "/closed", "/view/**", "/resources/**", "/css/**", "/js/**", "/images/**").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/account/login")
				.usernameParameter("lPhone")
				.passwordParameter("lField2")
				.permitAll()
				.and()
			.logout()
				.permitAll();
	}
	
	@Autowired
	@Override
	public void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.authenticationProvider(authProvider);
	}
	
}
