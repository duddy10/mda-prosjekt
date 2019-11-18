package com.myapp.myapp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.myapp.myapp.entity.Order;

@Repository
public class OrderDAOImpl implements OrderDAO {

	@Autowired
	private EntityManager em;

	@Override
	@Transactional
	public List<Order> findAll() {

		Session currentSession = em.unwrap(Session.class);
		Query<Order> query = currentSession.createQuery("from Order", Order.class);
		List<Order> orders = query.getResultList();

		return orders;
	}

	@Override
	@Transactional
	public Order getOrderById(String id) {
		Session currentSession = em.unwrap(Session.class);
		Order order = currentSession.get(Order.class, id);
		return order;
	}
}
