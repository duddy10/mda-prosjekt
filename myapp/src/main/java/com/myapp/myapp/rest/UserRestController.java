package com.myapp.myapp.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.myapp.dao.UserDAO;
import com.myapp.myapp.entity.MyUser;

@RestController
@RequestMapping("/api")
public class UserRestController {

private UserDAO uDAO;
	
	@Autowired
	public UserRestController(UserDAO uDAO) {
		this.uDAO = uDAO;
	}
	
	@GetMapping("/users")
	public List<MyUser> findAll(){
		return uDAO.findAll();
	}
}
