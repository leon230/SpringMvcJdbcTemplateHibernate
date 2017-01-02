package com.tickets.dao;

import java.util.List;

import com.tickets.model.ChartKeyValue;
import com.tickets.model.Ticket;
import com.tickets.model.User;

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
