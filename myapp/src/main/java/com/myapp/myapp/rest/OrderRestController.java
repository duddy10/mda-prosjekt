package com.myapp.myapp.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.myapp.dao.ConcertDAO;
import com.myapp.myapp.dao.OrderDAO;
import com.myapp.myapp.dao.UserDAO;
import com.myapp.myapp.entity.Concert;
import com.myapp.myapp.entity.Customer;
import com.myapp.myapp.entity.MyUser;
import com.myapp.myapp.entity.Order;
import com.myapp.myapp.util.JwtUtil;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api")
public class OrderRestController {

	@Autowired
	private OrderDAO oDAO;
	
	@Autowired
	private ConcertDAO concertDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private JwtUtil jwtUtil;

	
	@PostMapping(
			path="/order/buy",
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<JSONObject> updateUser(
			@RequestHeader(value="Authorization") String aurhtorizationHeader,
			@RequestBody JSONObject jsonObject) {
		
		String token = aurhtorizationHeader.replace("Bearer ", "");
		
		if(!jwtUtil.isTokenExpired(token)) {
			
			String username = jwtUtil.extractUsername(token);
			
			MyUser user = userDAO.getUserById(username);
			
			Customer customer = user.getCustomer();
			
			Concert concert = concertDAO.getConcertById(jsonObject.getAsNumber("id").intValue());
			
			Order order = new Order(null, true, customer, concert);
			
			oDAO.save(order);
			
			JSONObject mainObject = new JSONObject();
			
			mainObject.put("id", order.getId());
			mainObject.put("valid", order.getValid());
			mainObject.put("nfcData", order.getNfcData());
			mainObject.put("customerId", order.getCustomer().getId());
			mainObject.put("concertId", order.getConcert().getId());
			return ResponseEntity.status(HttpStatus.OK).body(mainObject);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	

	
}
