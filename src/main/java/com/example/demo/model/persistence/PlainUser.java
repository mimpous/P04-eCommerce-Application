package com.example.demo.model.persistence;

public class PlainUser { 
 	private String username;
 	private String password;
  
 	
 	public PlainUser( String userName, String password) {
		this.username = userName;
		this.password = password;
	}
	 
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	
	
}
