package com.drewschrauf.example.robotronic;

public class ExampleListItem {

	private String title;
	private String imageUrl;

	public ExampleListItem(String title, String imageUrl) {
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
