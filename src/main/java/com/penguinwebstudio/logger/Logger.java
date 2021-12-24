package com.penguinwebstudio.logger;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.DispatcherServlet;

public class Logger extends DispatcherServlet {

	private final LogService logService;
	
	@Autowired
	public Logger(LogService logService) {
		this.logService = logService;
	}
	
	@Override
	protected void doDispatch(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getRequestURI().matches("^/images.*") ||
			req.getRequestURI().matches("^/css.*") || 
			req.getRequestURI().matches("^/js.*")) {
			super.doDispatch(req, res);
			return;
		}
		String logMessage = req.getRemoteAddr() + " \"" +
				req.getMethod() + " " + req.getRequestURI() + "\" " + 
				String.valueOf(res.getStatus()) + " \"" + req.getHeader("User-Agent") + "\"";
		logService.addLog(new Log(
				new Date(),
				"info",
				logMessage,
				null
		));
		super.doDispatch(req, res);
	}
	
}
