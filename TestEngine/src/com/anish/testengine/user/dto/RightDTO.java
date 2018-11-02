package com.anish.testengine.user.dto;

public class RightDTO {
	private String rightName;
	private String screen;
	
	
	
	public RightDTO(String rightName, String screen) {
		this.rightName = rightName;
		this.screen = screen;
	}
	public String getRightName() {
		return rightName;
	}
	public void setRightName(String rightName) {
		this.rightName = rightName;
	}
	public String getScreen() {
		return screen;
	}
	public void setScreen(String screen) {
		this.screen = screen;
	}
	
	
}
