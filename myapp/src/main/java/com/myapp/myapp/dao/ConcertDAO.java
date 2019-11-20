package com.myapp.myapp.dao;

import java.util.List;

import com.myapp.myapp.entity.Concert;

public interface ConcertDAO {
	public List<Concert> findAll();
	
	public Concert getConcertById(int id);
	
	
}
