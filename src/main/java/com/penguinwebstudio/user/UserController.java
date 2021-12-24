package com.penguinwebstudio.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.penguinwebstudio.logger.Log;
import com.penguinwebstudio.logger.LogService;
import com.penguinwebstudio.tickets.Comment;
import com.penguinwebstudio.tickets.CommentThread;
import com.penguinwebstudio.tickets.Ticket;
import com.penguinwebstudio.tickets.TicketService;

@Controller
public class UserController {
	
	@Value("${google.recaptcha.key.testing.site}")
	private String recaptchaSiteKey;
	@Value("${google.recaptcha.key.testing.secret}")
	private String recaptchaSecretKey;
	private final TicketService ticketService;

	@Autowired
	public UserController(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@GetMapping(path="/conversations")
	public String conversations(Model model) {
		String loggedInAs = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (loggedInAs.equals("anonymousUser")) {
			loggedInAs = null;
		}
		model.addAttribute("loggedInAs", loggedInAs);
		ArrayList<CommentThread> threads = new ArrayList<CommentThread>();
		List<Ticket> tickets = ticketService.getAllTickets();
		for (Ticket ticket : tickets) {
			for (int i = 0; i < ticket.getComments().size(); i++) {
				ArrayList<Comment> thread = ticket.getComments().get(i);
				for (Comment comment : thread) {
					if (comment.getAuthor().equals(loggedInAs)) {
						threads.add(new CommentThread(
								ticket.getId().toString(),
								i,
								ticket.getTitle(),
								thread
						));
						break;
					}
				}
			}
		}
		if (threads.size() == 0) {
			threads = null;
		} else {
			Collections.sort(threads, new Comparator<CommentThread>() {
				@Override
				public int compare(CommentThread a, CommentThread b) {
					int aLastElement = a.getComments().size() - 1;
					int bLastElement = b.getComments().size() - 1;
					if (aLastElement < 0 || bLastElement < 0) {
						return 0;
					}
					if (a.getComments().get(aLastElement).getPostedOn().after(
							b.getComments().get(bLastElement).getPostedOn()
					)) {
						return -1;
					} else if (a.getComments().get(aLastElement).getPostedOn().before(
							b.getComments().get(bLastElement).getPostedOn()
					)) {
						return 1;
					} else {
						return 0;
					}
				}
			});
		}
		model.addAttribute("threads", threads);
		return "conversations";
	}
	
	@GetMapping(path="/account/login")
	public String login(LoginForm loginForm, Model model) {
		model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
		return "login";
	}
	
}
