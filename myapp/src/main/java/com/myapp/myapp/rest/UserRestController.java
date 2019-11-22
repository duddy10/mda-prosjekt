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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.myapp.dao.UserDAO;
import com.myapp.myapp.entity.Concert;
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
		System.out.println(username);
		
		JSONObject mainObject = new JSONObject();
		
		if(!jwtUtil.isTokenExpired(token)) {
			System.out.println("token not expired");
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
	
}
