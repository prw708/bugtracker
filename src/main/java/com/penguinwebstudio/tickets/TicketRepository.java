package com.penguinwebstudio.tickets;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepository extends MongoRepository<Ticket, String> {
	
	public Optional<Ticket> findById(String id);
	
	public List<Ticket> findAllByOrderByCreatedOnDesc();
	
	public List<Ticket> findAllByCreatorOrderByCreatedOnDesc(String creator);
	
	public List<Ticket> findAllByStatusOrderByCreatedOnDesc(String status);
	
	public void deleteById(String id);
	
	public long countByCreator(String creator);
	
	public long count();
		
}
