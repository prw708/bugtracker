package com.penguinwebstudio.tickets;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
	
	TicketRepository ticketRepository;
	
	@Autowired
	public TicketService(TicketRepository repository) {
		this.ticketRepository = repository;
	}
	
	public void saveTicket(Ticket ticket) {
		ticketRepository.save(ticket);
	}
	
	public Ticket updateTicketByStatus(String id, String status) {
		Ticket ticket = ticketRepository.findById(id).get();
		ticket.setStatus(status);
		ticketRepository.save(ticket);
		return ticket;
	}
	
	public Ticket updateTicketByComment(int threadIndex, String id, String content, String author, Date postedOn) {
		Ticket ticket = ticketRepository.findById(id).get();
		int commentId = ticket.countComments() + 1;
		Comment comment = new Comment(commentId, content, author, postedOn);
		ticket.addComment(threadIndex, comment);
		ticketRepository.save(ticket);
		return ticket;
	}
	
	public Ticket removeCommentFromTicket(int threadId, String ticketId, int commentId) {
		Ticket ticket = ticketRepository.findById(ticketId).get();
		ticket.deleteComment(threadId, commentId);
		ticketRepository.save(ticket);
		return ticket;
	}
	
	public Ticket getTicketById(String id) {
		return ticketRepository.findById(id).get();
	}
	
	public List<Ticket> getAllTickets() {
		return ticketRepository.findAllByOrderByCreatedOnDesc();
	}
	
	public List<Ticket> getTickets(String creator) {
		return ticketRepository.findAllByCreatorOrderByCreatedOnDesc(creator);
	}
	
	public List<Ticket> getTicketsByStatus(String status) {
		return ticketRepository.findAllByStatusOrderByCreatedOnDesc(status);
	}
	
	public void deleteTicketById(String id) {
		ticketRepository.deleteById(id);
	}
	
	public long getCountByCreator(String creator) {
		return ticketRepository.countByCreator(creator);
	}
	
	public long getCount() {
		return ticketRepository.count();
	}
}