package com.audamob.audasingers.stromae.model;

import java.io.Serializable;

public class Version implements Serializable {
	
	private String name;
	private int id;
	private String creationDate;
	private boolean state;
	
	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public Version(String name, int id, String creationDate,boolean state) {
		super();
		this.name = name;
		this.id = id;
		this.creationDate = creationDate;
		this.state = state;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
	

}
