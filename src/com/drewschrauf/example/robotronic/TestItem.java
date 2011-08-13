package com.drewschrauf.example.robotronic;

public class TestItem {
	
	private String title;
	private String imageUrl;

	public TestItem(String title, String imageUrl) {
		this.title = title;
		this.imageUrl = imageUrl;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}
}
