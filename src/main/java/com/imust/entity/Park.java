package com.imust.entity;

public class Park {
	private int id;
	private String name;
	private int status;

	public String getJingduX() {
		return jingduX;
	}

	public void setJingduX(String jingduX) {
		this.jingduX = jingduX;
	}

	public String getJingduY() {
		return jingduY;
	}

	public void setJingduY(String jingduY) {
		this.jingduY = jingduY;
	}

	private String jingduX;
	private String jingduY;
	private String  diquName;

	public String getDiquName() {
		return diquName;
	}

	public void setDiquName(String diquName) {
		this.diquName = diquName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getDiquId() {
		return diquId;
	}

	public void setDiquId(int diquId) {
		this.diquId = diquId;
	}

	private double price;
	private String address;
	private int  diquId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	
}
