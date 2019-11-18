package com.myapp.myapp.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.myapp.dao.OrderDAO;
import com.myapp.myapp.entity.Order;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api")
public class OrderRestController {

	@Autowired
	private OrderDAO oDAO;

	@GetMapping("/orders")
	public JSONObject fillAll() {
		List<Order> orders = new ArrayList<>();
		orders = oDAO.findAll();
		JSONArray jsonArray = new JSONArray();

		orders.forEach((order) -> {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", order.getId());
			jsonObject.put("nfcData", order.getId());
			jsonObject.put("valid", order.getId());
			jsonObject.put("customerId", order.getId());
			jsonObject.put("concertId", order.getId());

			jsonArray.add(jsonObject);
		});

		JSONObject mainObject = new JSONObject();
		mainObject.put("orders", jsonArray);
		return mainObject;

	}

}
