package com.myapp.myapp.dao;

import java.util.List;

import com.myapp.myapp.entity.Concert;
import com.myapp.myapp.entity.Order;

public interface OrderDAO {

	public List<Order> findAll();

	public Order getOrderById(String id);

	public List<Order> getOrdersByCustomerId(int customerId);

}
