package com.tickets.dao;

import com.tickets.model.Ticket;

import java.util.List;

/**
 * Created by Leon on 2017-01-02.
 */
public interface TicketDAOHibernate {
    public void saveOrUpdate(Ticket ticket);
    public void delete(int ticketId);
    public Ticket get(int ticketId);
    public List<Ticket> list(String conditions);
}
