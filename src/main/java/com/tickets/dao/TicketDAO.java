package com.tickets.dao;

import com.tickets.model.Ticket;
import java.util.List;

/**
 * Defines DAO operations for the contact model.
 *
 */
public interface TicketDAO {
	
	public void saveOrUpdate(Ticket ticket);
	
	public void delete(int ticketId);
	
	public Ticket get(int ticketId);
	
	public List<Ticket> list(String condition);
}
