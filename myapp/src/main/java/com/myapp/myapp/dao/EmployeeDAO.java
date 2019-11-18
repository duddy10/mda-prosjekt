package com.myapp.myapp.dao;

import java.util.List;

import com.myapp.myapp.entity.Employee;

public interface EmployeeDAO {
	public List<Employee> findAll();
}
