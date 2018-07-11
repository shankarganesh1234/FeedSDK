package com.ebay.models;

public class FeedRequest {

	private String category_id;
	private String siteId;
	private String date;
	private String snapshot_date;
	private String feed_scope;
	private String token;
	private String type;

	private FeedRequest(FeedRequestBuilder builder) {
		this.category_id = builder.category_id;
		this.siteId = builder.siteId;
		this.date = builder.date;
		this.feed_scope = builder.feed_scope;
		this.token = builder.token;
		this.snapshot_date = builder.snapshot_date;
		this.type = builder.type;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSnapshot_date() {
		return snapshot_date;
	}

	public void setSnapshot_date(String snapshot_date) {
		this.snapshot_date = snapshot_date;
	}

	public String getFeed_scope() {
		return feed_scope;
	}

	public void setFeed_scope(String feed_scope) {
		this.feed_scope = feed_scope;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static class FeedRequestBuilder {
		private String category_id;
		private String siteId;
		private String date;
		private String feed_scope;
		private String token;
		private String snapshot_date;
		private String type;

		public FeedRequestBuilder category_id(final String category_id) {
			this.category_id = category_id;
			return this;
		}

		public FeedRequestBuilder siteId(final String siteId) {
			this.siteId = siteId;
			return this;
		}

		public FeedRequestBuilder date(final String date) {
			this.date = date;
			return this;
		}

		public FeedRequestBuilder feed_scope(final String feed_scope) {
			this.feed_scope = feed_scope;
			return this;
		}

		public FeedRequestBuilder token(final String token) {
			this.token = token;
			return this;
		}

		public FeedRequestBuilder snapshot_date(final String snapshot_date) {
			this.snapshot_date = snapshot_date;
			return this;
		}

		public FeedRequestBuilder type(final String type) {
			this.type = type;
			return this;
		}

		public FeedRequest build() {
			return new FeedRequest(this);
		}
	}
}
