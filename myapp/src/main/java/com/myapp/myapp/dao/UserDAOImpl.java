package com.myapp.myapp.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.myapp.myapp.entity.MyUser;

@Repository
public class UserDAOImpl implements UserDAO {

	private EntityManager em;
	
	@Autowired
	public UserDAOImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	@Transactional
	public List<MyUser> findAll() {
		Session currentSession = em.unwrap(Session.class);
		
		Query<MyUser> query = currentSession.createQuery("from User", MyUser.class);
		
		List<MyUser> users = query.getResultList();
		
		return users;
	}

	@Override
	@Transactional
	public MyUser getUserById(String id) {
		Session currentSession = em.unwrap(Session.class);
		
		MyUser user = currentSession.get(MyUser.class, id);
		
		return user;
	}

}
