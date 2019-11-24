package com.myapp.myapp.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="concerts")
public class Concert {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="title")
	private String title;
	
	@Column(name="description")
	private String description;
	
	@Column(name="media")
	private String media;

	@Column(name="price")
	private int price;
	
	@Column(name="lat")
	private double lat;
	
	@Column(name="lng")
	private double lng;
	
	@Column(name="date")
	private Timestamp datetime;
	
	
	public Concert() {}

	

	public Concert(int id, String title, String description, String media, int price, double lat, double lng,
			Timestamp datetime) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.media = media;
		this.price = price;
		this.lat = lat;
		this.lng = lng;
		this.datetime = datetime;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}



	public double getLat() {
		return lat;
	}



	public void setLat(double lat) {
		this.lat = lat;
	}



	public double getLng() {
		return lng;
	}



	public void setLng(double lng) {
		this.lng = lng;
	}



	public Timestamp getDatetime() {
		return datetime;
	}



	public void setDatetime(Timestamp datetime) {
		this.datetime = datetime;
	}
	
	
}
