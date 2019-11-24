package com.myapp.myapp.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.myapp.dao.ConcertDAO;
import com.myapp.myapp.entity.Concert;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api")
public class ConcertRestController {
	
	@Autowired
	private ConcertDAO cDAO;
	
	
	// function to get all concerts and return them as JSON object with a JSON array
	@GetMapping("/concerts")
	public JSONObject findAll(){
		List<Concert> concerts = new ArrayList<>();
		concerts = cDAO.findAll();
		JSONArray jsonArray = new JSONArray();
		concerts.forEach((concert) -> {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", concert.getId());
			jsonObject.put("title", concert.getTitle());
			jsonObject.put("description", concert.getDescription());
			jsonObject.put("media", concert.getMedia());
			jsonObject.put("price", concert.getPrice());
			jsonObject.put("lng", concert.getLng());
			jsonObject.put("lat", concert.getLat());
			jsonObject.put("date", concert.getDatetime());
			jsonArray.add(jsonObject);
		} );
		
		JSONObject mainObject = new JSONObject();
		
		mainObject.put("concerts", jsonArray);
		
		return mainObject;
	}
	
	@GetMapping("/concert/{id}")
	public Concert getConcertById(@PathVariable int id){
		return cDAO.getConcertById(id);
	}
}
