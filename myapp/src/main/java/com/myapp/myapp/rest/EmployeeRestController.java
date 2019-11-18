package com.myapp.myapp.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.myapp.dao.EmployeeDAO;
import com.myapp.myapp.entity.Employee;

@RestController
@RequestMapping("api")
public class EmployeeRestController {

	private EmployeeDAO eDAO;
	
	@Autowired
	public EmployeeRestController(EmployeeDAO eDAO) {
		this.eDAO = eDAO;
	}
	
	@GetMapping("/employees")
	public List<Employee> findAll() {
		return eDAO.findAll();
	}
	
}
