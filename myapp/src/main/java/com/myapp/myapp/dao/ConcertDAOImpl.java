package com.myapp.myapp.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.myapp.myapp.entity.Concert;

@Repository
public class ConcertDAOImpl implements ConcertDAO {
	
	@Autowired
	private EntityManager em;

	@Override
	public List<Concert> findAll() {
		Session currentSession = em.unwrap(Session.class);
		
		Query<Concert> query = currentSession.createQuery("from Concert", Concert.class);
		
		List<Concert> concerts = query.getResultList();
		
		return concerts;
	}

	@Override
	public Concert getConcertById(int id) {
		Session currentSession = em.unwrap(Session.class);
		
		Concert concert = currentSession.get(Concert.class, id);
		
		return concert;
	}

}
