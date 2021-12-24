package com.penguinwebstudio.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.google.gson.Gson;
import com.penguinwebstudio.user.LoginForm;
import com.penguinwebstudio.utils.HttpRequests;
import com.penguinwebstudio.utils.RecaptchaResponse;

public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	@Value("${google.recaptcha.key.production.secret}")
	private String recaptchaSecretKey = System.getenv().get("google.recaptcha.key.production.secret");
	
	@Autowired
	Validator loginValidator;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
		LoginForm loginForm = new LoginForm();
		String lUsername = req.getParameter("lUsername");
		loginForm.setlPhone(req.getParameter("lPhone"));
		loginForm.setlField2(req.getParameter("lField2"));
		loginForm.setlRecaptcha(req.getParameter("g-recaptcha-response"));
		Errors errors = new BeanPropertyBindingResult(loginForm, "LoginForm");
		loginValidator.validate(loginForm, errors);
		System.out.println(errors);
		if (errors.hasErrors() || !lUsername.isEmpty()) {
			throw new BadCredentialsException("Invalid username or password.");
		} else {
			String url = "https://www.google.com/recaptcha/api/siteverify";
			String data = "secret=" + recaptchaSecretKey + "&" + "response=" + loginForm.getlRecaptcha();
			String json = null;
			try {
				json = HttpRequests.postJSON(url, data);
			} catch (Exception e) {
				json = null;
			}
			Gson g = new Gson();
			RecaptchaResponse recaptcha = g.fromJson(json, RecaptchaResponse.class);
			if (!recaptcha.isSuccess() || !recaptcha.getAction().equals("submit")) {
				throw new BadCredentialsException("Invalid recaptcha.");
			}
		}
		return super.attemptAuthentication(req, res);
	}
}