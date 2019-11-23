package com.myapp.myapp.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.myapp.myapp.entity.Customer;
import com.myapp.myapp.entity.MyUser;

@Repository
public class CustomerDAOImpl implements CustomerDAO {
	
	private EntityManager em;
	
	@Autowired
	public CustomerDAOImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	@Transactional
	public List<Customer> findAll() {
		Session currentSession = em.unwrap(Session.class);
				
		Query<Customer> query = currentSession.createQuery("from Customer", Customer.class);
		
		List<Customer> customers = query.getResultList();
		
		return customers;
	}
	
	@Override
	@Transactional
	public void update(Customer customer) {
		Session currentSession = em.unwrap(Session.class);
		currentSession.update(customer);
	}
	
	@Override
	@Transactional
	public void save(Customer customer) {
		Session currentSession = em.unwrap(Session.class);
		currentSession.save(customer);
	}

}
