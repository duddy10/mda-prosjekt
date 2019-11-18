package com.myapp.myapp.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.myapp.myapp.dao.UserDAO;
import com.myapp.myapp.entity.MyUser;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserDAO uDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// get user from db
		MyUser myUser = uDAO.getUserById(username);
		
		
		
		return new User(myUser.getUsername(), myUser.getPassword(), new ArrayList<>());
	}
	
}
