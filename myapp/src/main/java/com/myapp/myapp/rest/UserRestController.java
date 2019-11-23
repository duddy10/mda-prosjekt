package com.myapp.myapp.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.myapp.myapp.dao.CustomerDAO;
import com.myapp.myapp.dao.UserDAO;
import com.myapp.myapp.entity.Concert;
import com.myapp.myapp.entity.Customer;
import com.myapp.myapp.entity.MyUser;
import com.myapp.myapp.util.JwtUtil;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api")
public class UserRestController {

	@Autowired
	private UserDAO uDAO;
	
	@Autowired
	private CustomerDAO cDAO;

	@Autowired
	private JwtUtil jwtUtil;
	
	
	@GetMapping("/users")
	public List<MyUser> findAll(){
		return uDAO.findAll();
	}
	
	@GetMapping(
			path= "/user/orders",
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<JSONObject> getConcertsByCustomerId(
			@RequestHeader(value="Authorization") String authorizationHeader
			) {
		
		String token = authorizationHeader.replace("Bearer ", "");
		
		
		String username = jwtUtil.extractUsername(token).toString();
		
		JSONObject mainObject = new JSONObject();
		
		if(!jwtUtil.isTokenExpired(token)) {
			List<Concert> concerts = uDAO.getConcertsByUserId(username);
			
			JSONArray jsonArray = new JSONArray();
			concerts.forEach((concert) -> {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", concert.getId());
				jsonObject.put("title", concert.getTitle());
				jsonObject.put("description", concert.getDescription());
				jsonObject.put("media", concert.getMedia());
				jsonObject.put("price", concert.getPrice());
				jsonArray.add(jsonObject);
			} );
			
			
			
			mainObject.put("concerts", jsonArray);
			return ResponseEntity.status(HttpStatus.OK).body(mainObject);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}	
	}
	
	@GetMapping(
			path= "/user/information",
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<JSONObject> getUserInformationById(
			@RequestHeader(value="Authorization") String authorizationHeader
			) {
		
		String token = authorizationHeader.replace("Bearer ", "");
		
		
		String username = jwtUtil.extractUsername(token).toString();
		
		if(!jwtUtil.isTokenExpired(token)) {
			JSONObject mainObject = new JSONObject();
			MyUser user = uDAO.getUserById(username);
			
			mainObject.put("username", user.getUsername());
			mainObject.put("role", user.getRole());
			mainObject.put("firstName", user.getCustomer().getFirstName());
			mainObject.put("lastName", user.getCustomer().getLastName());
			mainObject.put("phoneNumber", user.getCustomer().getPhoneNumber());
			mainObject.put("email", user.getCustomer().getEmail());
			return ResponseEntity.status(HttpStatus.OK).body(mainObject);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}	
	}
	
	@PutMapping(
			path="/user/create",
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<JSONObject> updateUser(
			@RequestHeader(value="Authorization") String aurhtorizationHeader,
			@RequestBody JSONObject jsonObject) {
		
		String token = aurhtorizationHeader.replace("Bearer ", "");
		
		if(!jwtUtil.isTokenExpired(token)) {
			
			String username = jwtUtil.extractUsername(token);
			
			MyUser user = uDAO.getUserById(username);
			
			Customer customer = user.getCustomer();
			
			customer.setFirstName(jsonObject.getAsString("firstName"));
			customer.setLastName(jsonObject.getAsString("lastName"));
			customer.setPhoneNumber(jsonObject.getAsString("phoneNumber"));
			customer.setEmail(jsonObject.getAsString("email"));
			
			user.setCustomer(customer);
			
			uDAO.update(user);
			
			JSONObject mainObject = new JSONObject();
			
			mainObject.put("username", user.getUsername());
			mainObject.put("role", user.getRole());
			mainObject.put("firstName", customer.getFirstName());
			mainObject.put("lastName", customer.getLastName());
			mainObject.put("phoneNumber", customer.getPhoneNumber());
			mainObject.put("email", customer.getEmail());
			return ResponseEntity.status(HttpStatus.OK).body(mainObject);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@PostMapping(
			path="/user/create",
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<JSONObject> createUser(
			@RequestBody JSONObject jsonObject) {
			
			Customer customer = new Customer(
					jsonObject.getAsString("firstName"),
					jsonObject.getAsString("lsatName"),
					jsonObject.getAsString("email"),
					jsonObject.getAsString("phoneNumber")
					);
			
			MyUser user = new MyUser(
					jsonObject.getAsString("username"),
					jsonObject.getAsString("password"),
					"USER"
					);
			
			user.setCustomer(customer);
			
			try {
				cDAO.save(customer);
				uDAO.save(user);
				JSONObject mainObject = new JSONObject();
				
				mainObject.put("username", user.getUsername());
				mainObject.put("role", user.getRole());
				mainObject.put("firstName", customer.getFirstName());
				mainObject.put("lastName", customer.getLastName());
				mainObject.put("phoneNumber", customer.getPhoneNumber());
				mainObject.put("email", customer.getEmail());
				return ResponseEntity.status(HttpStatus.OK).body(mainObject);
			} catch (Exception e){
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
			}
			
			
			
			
	
	}
	
}
