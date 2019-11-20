package com.myapp.myapp.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.myapp.dao.CustomerDAO;
import com.myapp.myapp.entity.Customer;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

	private CustomerDAO cDAO;
	
	@Autowired
	public CustomerRestController(CustomerDAO cDAO) {
		this.cDAO = cDAO;
	}
	
	@GetMapping("/customers")
	public List<Customer> findAll(){
		return cDAO.findAll();
	}
	
	
}
