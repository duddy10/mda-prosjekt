package com.myapp.myapp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="orders")
public class Order {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="nfc_data")
	private String nfcData;
	
	@Column(name="valid")
	private Boolean valid;
	
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="concert_id")
	private Concert concert;

	public Order () {}

	public Order(String nfcData, Boolean valid, Customer customer, Concert concert) {
		super();
		this.nfcData = nfcData;
		this.valid = valid;
		this.customer = customer;
		this.concert = concert;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNfcData() {
		return nfcData;
	}

	public void setNfcData(String nfcData) {
		this.nfcData = nfcData;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Concert getConcert() {
		return concert;
	}

	public void setConcert(Concert concert) {
		this.concert = concert;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", nfcData=" + nfcData + ", valid=" + valid
				+ ", customer=" + customer + ", concert=" + concert + "]";
	}
	
	

}
