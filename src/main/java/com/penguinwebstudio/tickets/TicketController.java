package com.penguinwebstudio.tickets;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.penguinwebstudio.user.User;
import com.penguinwebstudio.user.UserLevel;
import com.penguinwebstudio.user.UserLevelService;
import com.penguinwebstudio.user.UserService;
import com.penguinwebstudio.tickets.TicketService;
import com.penguinwebstudio.utils.HttpRequests;
import com.penguinwebstudio.utils.RecaptchaResponse;

@Controller
public class TicketController {

	@Value("${google.recaptcha.key.production.site}")
	private String recaptchaSiteKey = System.getenv().get("google.recaptcha.key.production.site");
	@Value("${google.recaptcha.key.production.secret}")
	private String recaptchaSecretKey = System.getenv().get("google.recaptcha.key.production.secret");
	
	private final TicketService ticketService;
	private final UserService userService;
	private final UserLevelService userLevelService;

	@Autowired
	public TicketController(TicketService ticketService, UserService userService, UserLevelService userLevelService) {
		this.ticketService = ticketService;
		this.userService = userService;
		this.userLevelService = userLevelService;
	}
	
	@GetMapping(path="")
	public String home(Model model) {
		String loggedInAs = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (loggedInAs.equals("anonymousUser")) {
			loggedInAs = null;
		}
		model.addAttribute("loggedInAs", loggedInAs);
		List<Ticket> tickets = ticketService.getTicketsByStatus("Open");
		tickets.addAll(ticketService.getTicketsByStatus("To Do"));
		tickets.addAll(ticketService.getTicketsByStatus("In Progress"));
		tickets.addAll(ticketService.getTicketsByStatus("Testing"));
		Collections.sort(tickets, new Comparator<Ticket>() {
			@Override
			public int compare(Ticket a, Ticket b) {
				if (a.getCreatedOn().after(b.getCreatedOn())) {
					return -1;
				} else if (a.getCreatedOn().before(b.getCreatedOn())) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		model.addAttribute("title", "Open Tickets");
		model.addAttribute("tickets", tickets);
		return "home";
	}
	
	@GetMapping(path="/closed")
	public String closed(Model model) {
		String loggedInAs = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (loggedInAs.equals("anonymousUser")) {
			loggedInAs = null;
		}
		model.addAttribute("loggedInAs", loggedInAs);
		List<Ticket> tickets = ticketService.getTicketsByStatus("Closed");
		Collections.sort(tickets, new Comparator<Ticket>() {
			@Override
			public int compare(Ticket a, Ticket b) {
				if (a.getCreatedOn().after(b.getCreatedOn())) {
					return -1;
				} else if (a.getCreatedOn().before(b.getCreatedOn())) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		model.addAttribute("title", "Closed Tickets");
		model.addAttribute("tickets", tickets);
		return "home";
	}
	
	@GetMapping(path="/create")
	public String getCreate(CreateForm createForm, Model model) {
		String loggedInAs = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
		model.addAttribute("loggedInAs", loggedInAs);
		model.addAttribute("currentDateTime", dateTimeFormat.format(date));
		return "create";
	}
	
	@PostMapping(path="/create")
	public String postCreate(
			@Valid @ModelAttribute CreateForm createForm, 
			BindingResult bindingResult, 
			Model model, 
			@NotBlank @Size(min=1, max=500) @Pattern(regexp="^[A-Za-z0-9\\-_]+$") @RequestParam(value = "g-recaptcha-response") String recaptcha,
			RedirectAttributes attr
	) {
		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String loggedInAs = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (loggedInAs.equals("anonymousUser")) {
			loggedInAs = null;
			model.addAttribute("notLoggedInError", true);
			model.addAttribute("loggedInAs", loggedInAs);
			model.addAttribute("errors", null);
			model.addAttribute("currentDateTime", dateTimeFormat.format(date));
			model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
			return "create";
		}
		if (bindingResult.hasErrors() || !createForm.getcEmail().isEmpty()) {			
			List<FieldError> errors = bindingResult.getFieldErrors();
			model.addAttribute("errors", errors);
			model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
			model.addAttribute("loggedInAs", loggedInAs);
			model.addAttribute("currentDateTime", dateTimeFormat.format(date));
			return "create";
		} else {			
			String url = "https://www.google.com/recaptcha/api/siteverify";
			String data = "secret=" + recaptchaSecretKey + "&" + "response=" + recaptcha;
			String json = null;
			try {
				json = HttpRequests.postJSON(url, data);
			} catch (Exception e) {
				json = null;
			}
			Gson g = new Gson();
			RecaptchaResponse recaptchaResponse = g.fromJson(json, RecaptchaResponse.class);
			if (!recaptchaResponse.isSuccess() || recaptchaResponse.getScore() < 0.7 || !recaptchaResponse.getAction().equals("create")) {
				model.addAttribute("errors", null);
				model.addAttribute("recaptchaError", true);		
				model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
				model.addAttribute("loggedInAs", loggedInAs);
				model.addAttribute("currentDateTime", dateTimeFormat.format(date));
				return "create";
			}
			int maxTickets = 0;
			long ticketCount = 0;
			User user = null;
			try {
				user = userService.getUser(loggedInAs);
				UserLevel userLevel = userLevelService.getUser(user.getId().toString());
				maxTickets = userLevel.getMaxTickets();
				ticketCount = ticketService.getCountByCreator(loggedInAs);
			} catch (Exception e) {
				maxTickets = 0;
				ticketCount = 0;
			}
			if (ticketCount + 1 <= maxTickets) {
				ticketService.saveTicket(
						new Ticket(
								createForm.getcSubtitle(),
								createForm.getcCreator(),
								createForm.getcStatus(),
								createForm.getcTitle(),
								createForm.getcDetail(),
								createForm.getcReporter(),
								createForm.getcExplain()
						)
				);
				userLevelService.updateUserLevelByUser(user.getId().toString());
				model.addAttribute("loggedInAs", loggedInAs);
				model.addAttribute("errors", null);
				attr.addFlashAttribute("successMessage", "Ticket created successfully!");
				return "redirect:/";
			} else {
				model.addAttribute("maxTicketsError", true);
				model.addAttribute("loggedInAs", loggedInAs);
				model.addAttribute("errors", null);
				model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
				return "create";
			}
		}
	}
	
	@GetMapping(path="/delete")
	public String getDelete(Model model) {
		String loggedInAs = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		List<Ticket> tickets = ticketService.getTickets(loggedInAs);
		model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
		model.addAttribute("loggedInAs", loggedInAs);
		model.addAttribute("tickets", tickets);
		return "delete";
	}
	
	@PostMapping(path="/delete")
	public String postDelete(
			@Valid @ModelAttribute DeleteForm deleteForm,
			BindingResult bindingResult,
			Model model, 
			RedirectAttributes attr
	) {
		String loggedInAs = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (loggedInAs.equals("anonymousUser")) {
			loggedInAs = null;
			model.addAttribute("notLoggedInError", true);
			model.addAttribute("loggedInAs", loggedInAs);
			model.addAttribute("errors", null);
			model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
			return "delete";
		}
		List<Ticket> tickets = ticketService.getTickets(loggedInAs);
		model.addAttribute("tickets", tickets);
		if (bindingResult.hasErrors() || !deleteForm.getdEmail().isEmpty()) {
			model.addAttribute("errors", true);
			model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
			model.addAttribute("loggedInAs", loggedInAs);
			return "delete";
		} else {
			String url = "https://www.google.com/recaptcha/api/siteverify";
			String data = "secret=" + recaptchaSecretKey + "&" + "response=" + deleteForm.getRecaptcha();
			String json = null;
			try {
				json = HttpRequests.postJSON(url, data);
			} catch (Exception e) {
				json = null;
			}
			Gson g = new Gson();
			RecaptchaResponse recaptchaResponse = g.fromJson(json, RecaptchaResponse.class);
			if (!recaptchaResponse.isSuccess() || recaptchaResponse.getScore() < 0.7 || !recaptchaResponse.getAction().equals("delete")) {
				model.addAttribute("errors", null);
				model.addAttribute("recaptchaError", true);
				model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
				model.addAttribute("loggedInAs", loggedInAs);
				return "delete";
			}
			ticketService.deleteTicketById(deleteForm.getTicketId());
		}
		model.addAttribute("loggedInAs", loggedInAs);
		model.addAttribute("errors", null);
		attr.addFlashAttribute("successMessage", "Ticket deleted successfully!");
		return "redirect:/delete";
	}
	
	@GetMapping(path="/view/{ticketId}")
	public String getView(
			CreateForm createForm, 
			DiscussionForm discussionForm,
			@Pattern(regexp="^[A-Za-z0-9]+$") @PathVariable String ticketId, 
			Model model
	) {
		String loggedInAs = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (loggedInAs.equals("anonymousUser")) {
			loggedInAs = null;
		}
		try {
			Ticket ticket = ticketService.getTicketById(ticketId);
			model.addAttribute("ticket", ticket);
		} catch (Exception e) {
			model.addAttribute("noTicketError", true);
		}
		model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
		model.addAttribute("loggedInAs", loggedInAs);
		return "view";
	}
	
	@PostMapping(path="/view/{ticketId}")
	public String postView(
			@Valid @ModelAttribute CreateForm createForm,
			BindingResult bindingResult,
			@ModelAttribute DiscussionForm discussionForm,
			@Pattern(regexp="^[A-Za-z0-9]+$") @PathVariable String ticketId,
			@NotBlank @Size(min=1, max=500) @Pattern(regexp="^[A-Za-z0-9\\-_]+$") @RequestParam(value = "g-recaptcha-response") String recaptcha,
			Model model, 
			RedirectAttributes attr
	) {
		String loggedInAs = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (loggedInAs.equals("anonymousUser")) {
			loggedInAs = null;
			model.addAttribute("notLoggedInError", true);
			try {
				Ticket ticket = ticketService.getTicketById(ticketId);
				model.addAttribute("ticket", ticket);
			} catch (Exception e) {
				model.addAttribute("noTicketError", true);
			}
			model.addAttribute("loggedInAs", loggedInAs);
			model.addAttribute("errors", null);
			model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
			return "view";
		}
		if (bindingResult.hasErrors() || !createForm.getcEmail().isEmpty()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			try {
				Ticket ticket = ticketService.getTicketById(ticketId);
				model.addAttribute("ticket", ticket);
			} catch (Exception e) {
				model.addAttribute("noTicketError", true);
			}
			model.addAttribute("errors", errors);
			model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
			model.addAttribute("loggedInAs", loggedInAs);
			return "view";
		} else {
			String url = "https://www.google.com/recaptcha/api/siteverify";
			String data = "secret=" + recaptchaSecretKey + "&" + "response=" + recaptcha;
			String json = null;
			try {
				json = HttpRequests.postJSON(url, data);
			} catch (Exception e) {
				json = null;
			}
			Gson g = new Gson();
			RecaptchaResponse recaptchaResponse = g.fromJson(json, RecaptchaResponse.class);
			if (!recaptchaResponse.isSuccess() || recaptchaResponse.getScore() < 0.7 || !recaptchaResponse.getAction().equals("view")) {
				try {
					Ticket ticket = ticketService.getTicketById(ticketId);
					model.addAttribute("ticket", ticket);
				} catch (Exception e) {
					model.addAttribute("noTicketError", true);
				}
				model.addAttribute("errors", null);
				model.addAttribute("recaptchaError", true);
				model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
				model.addAttribute("loggedInAs", loggedInAs);
				return "view";
			}
			try {
				Ticket ticket = ticketService.updateTicketByStatus(ticketId, createForm.getcReporter());
				model.addAttribute("ticket", ticket);
			} catch (Exception e) {
				model.addAttribute("noTicketError", true);
			}
			model.addAttribute("loggedInAs", loggedInAs);
			model.addAttribute("errors", null);
			attr.addFlashAttribute("successMessage", "Ticket edited successfully!");
			return "redirect:/view/" + ticketId;
		}
	}
	
	@GetMapping(path="/discussion/{ticketId}")
	public String getDiscussion(
			CreateForm createForm, 
			DiscussionForm discussionForm,
			@Pattern(regexp="^[A-Za-z0-9]+$") @PathVariable String ticketId, 
			Model model
	) {
		String loggedInAs = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (loggedInAs.equals("anonymousUser")) {
			loggedInAs = null;
		}
		try {
			Ticket ticket = ticketService.getTicketById(ticketId);
			model.addAttribute("ticket", ticket);
		} catch (Exception e) {
			model.addAttribute("noTicketError", true);
		}
		model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
		model.addAttribute("loggedInAs", loggedInAs);
		return "view";
	}
	
	@PostMapping(path="/discussion/{ticketId}")
	public String postDiscussion(
			@Valid @ModelAttribute DiscussionForm discussionForm,
			BindingResult bindingResult,
			@ModelAttribute CreateForm createForm,
			@Pattern(regexp="^[A-Za-z0-9]+$") @PathVariable String ticketId,
			@NotBlank @Size(min=1, max=500) @Pattern(regexp="^[A-Za-z0-9\\-_]+$") @RequestParam(value = "g-recaptcha-response") String recaptcha,
			Model model, 
			RedirectAttributes attr
	) {
		String loggedInAs = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (loggedInAs.equals("anonymousUser")) {
			loggedInAs = null;
			model.addAttribute("notLoggedInError", true);
			try {
				Ticket ticket = ticketService.getTicketById(ticketId);
				model.addAttribute("ticket", ticket);
			} catch (Exception e) {
				model.addAttribute("noTicketError", true);
			}
			model.addAttribute("loggedInAs", loggedInAs);
			model.addAttribute("errors", null);
			model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
			return "view";
		}
		if (bindingResult.hasErrors() || !discussionForm.getdUsername().isEmpty()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			try {
				Ticket ticket = ticketService.getTicketById(ticketId);
				model.addAttribute("ticket", ticket);
			} catch (Exception e) {
				model.addAttribute("noTicketError", true);
			}
			model.addAttribute("errors", errors);
			model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
			model.addAttribute("loggedInAs", loggedInAs);
			return "view";
		} else {
			String url = "https://www.google.com/recaptcha/api/siteverify";
			String data = "secret=" + recaptchaSecretKey + "&" + "response=" + recaptcha;
			String json = null;
			try {
				json = HttpRequests.postJSON(url, data);
			} catch (Exception e) {
				json = null;
			}
			Gson g = new Gson();
			RecaptchaResponse recaptchaResponse = g.fromJson(json, RecaptchaResponse.class);
			if (!recaptchaResponse.isSuccess() || recaptchaResponse.getScore() < 0.7 || !recaptchaResponse.getAction().equals("discussion")) {
				try {
					Ticket ticket = ticketService.getTicketById(ticketId);
					model.addAttribute("ticket", ticket);
				} catch (Exception e) {
					model.addAttribute("noTicketError", true);
				}
				model.addAttribute("errors", null);
				model.addAttribute("recaptchaError", true);
				model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
				model.addAttribute("loggedInAs", loggedInAs);
				return "view";
			}
			try {
				int threadIndex;
				if (discussionForm.getThreadIndex() == -1) {
					threadIndex = ticketService.getTicketById(ticketId).getComments().size();
				} else {
					threadIndex = discussionForm.getThreadIndex();
				}
				Ticket ticket = ticketService.updateTicketByComment(
						threadIndex,
						ticketId, 
						discussionForm.getdName(),
						loggedInAs,
						new Date()
				);
				model.addAttribute("ticket", ticket);
			} catch (Exception e) {
				model.addAttribute("noTicketError", true);
			}
			model.addAttribute("loggedInAs", loggedInAs);
			model.addAttribute("errors", null);
			attr.addFlashAttribute("successMessage", "Comment added successfully!");
			return "redirect:/view/" + ticketId;
		}
	}
	
	@PostMapping(path="/discussion/delete/comment")
	public String postDeleteDiscussion(
			@Valid @ModelAttribute DeleteCommentForm deleteCommentForm,
			BindingResult bindingResult,
			@ModelAttribute CreateForm createForm,
			@ModelAttribute DiscussionForm discussionForm,
			Model model, 
			RedirectAttributes attr
	) {
		String loggedInAs = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (loggedInAs.equals("anonymousUser")) {
			loggedInAs = null;
			model.addAttribute("notLoggedInError", true);
			model.addAttribute("loggedInAs", loggedInAs);
			model.addAttribute("errors", null);
			model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
			return "view";
		}
		if (bindingResult.hasErrors() || !deleteCommentForm.getdEmail().isEmpty()) {
			try {
				Ticket ticket = ticketService.getTicketById(deleteCommentForm.getTicketId());
				model.addAttribute("ticket", ticket);
			} catch (Exception e) {
				model.addAttribute("noTicketError", true);
			}
			model.addAttribute("errors", true);
			model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
			model.addAttribute("loggedInAs", loggedInAs);
			return "view";
		} else {
			String url = "https://www.google.com/recaptcha/api/siteverify";
			String data = "secret=" + recaptchaSecretKey + "&" + "response=" + deleteCommentForm.getRecaptcha();
			String json = null;
			try {
				json = HttpRequests.postJSON(url, data);
			} catch (Exception e) {
				json = null;
			}
			Gson g = new Gson();
			RecaptchaResponse recaptchaResponse = g.fromJson(json, RecaptchaResponse.class);
			if (!recaptchaResponse.isSuccess() || recaptchaResponse.getScore() < 0.7 || !recaptchaResponse.getAction().equals("deleteComment")) {
				try {
					Ticket ticket = ticketService.getTicketById(deleteCommentForm.getTicketId());
					model.addAttribute("ticket", ticket);
				} catch (Exception e) {
					model.addAttribute("noTicketError", true);
				}
				model.addAttribute("errors", null);
				model.addAttribute("recaptchaError", true);
				model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
				model.addAttribute("loggedInAs", loggedInAs);
				return "view";
			}
			try {
				Ticket ticket = ticketService.removeCommentFromTicket(
						deleteCommentForm.getThreadIndex(),
						deleteCommentForm.getTicketId(), 
						Integer.parseInt(deleteCommentForm.getCommentId(), 10)
				);
				model.addAttribute("ticket", ticket);
			} catch (Exception e) {
				model.addAttribute("noTicketError", true);
			}
		}
		model.addAttribute("loggedInAs", loggedInAs);
		model.addAttribute("errors", null);
		attr.addFlashAttribute("successMessage", "Comment deleted successfully!");
		return "redirect:/view/" + deleteCommentForm.getTicketId();
	}
	
}
