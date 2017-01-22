package com.tickets.dao;

import com.tickets.model.Ticket;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Leon on 2017-01-02.
 */
public class TicketDAOHibernateImpl implements TicketDAOHibernate {

    @Autowired
    private SessionFactory sessionFactory;

    public TicketDAOHibernateImpl(){

    }
    public TicketDAOHibernateImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    @Transactional
    public void saveOrUpdate(Ticket ticket) {
        sessionFactory.getCurrentSession().saveOrUpdate(ticket);

    }

    @Override
    @Transactional
    public void delete(int ticketId) {
        sessionFactory.getCurrentSession().delete(new Ticket(ticketId));
    }

    @Override
    @Transactional
    public Ticket get(int ticketId) {
        String hql = "from Ticket where id=" + ticketId;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        @SuppressWarnings("unchecked")
        List<Ticket> listTicket = (List<Ticket>) query.list();

        if (listTicket != null && !listTicket.isEmpty()) {
            return listTicket.get(0);
        }

        return null;
    }

    @Override
    @Transactional
    public List<Ticket> list(String conditions) {
        String sql = "from Ticket where " + conditions + " order by dueDate desc";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
//        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Ticket.class);
//        criteria.addOrder(Order.desc("dueDate"));


        @SuppressWarnings("unchecked")
        List<Ticket> listTicket = (List<Ticket>) query.list();

        return listTicket;

//        List<Ticket> listTicket = (List<Ticket>) sessionFactory.getCurrentSession()
//                .createCriteria(Ticket.class)
//                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
//
//        return listTicket;
    }
}
