package com.anish.testengine.user.dto;

import java.util.ArrayList;

public class UserDTO {
	private String username;
	private String password;
	private String role;
	private ArrayList<RightDTO> rights;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public ArrayList<RightDTO> getRights() {
		return rights;
	}
	public void setRights(ArrayList<RightDTO> rights) {
		this.rights = rights;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
