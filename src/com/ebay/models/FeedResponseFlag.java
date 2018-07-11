package com.ebay.models;

public class FeedResponseFlag {

	private String contentRange;
	private int statusCode;

	public FeedResponseFlag(String contentRange, int statusCode) {
		super();
		this.contentRange = contentRange;
		this.setStatusCode(statusCode);
	}

	public String getContentRange() {
		return contentRange;
	}

	public void setContentRange(String contentRange) {
		this.contentRange = contentRange;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
