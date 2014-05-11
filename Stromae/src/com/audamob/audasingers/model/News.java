package com.audamob.audasingers.model;

import java.io.Serializable;

public class News implements Serializable {

	String FeedTitle;
	String FeedDate;
	String FeedDescription;
	String Url;
	String Writer;
	String ImageUrl;
	String ID;
	
	public News(String title,String Date,String description,String url,String writer,String imageUrl) {
		// TODO Auto-generated constructor stub
		this.FeedTitle=title;
		this.FeedDate=Date;
		this.FeedDescription=description;
		this.Url=url;
		this.Writer=writer;
		this.ImageUrl=imageUrl;
		this.ID=url;
	}
	public String getTitle() {
		return FeedTitle;
	}
	public String getDesccreption() {
		return FeedDescription;
	}
	public String getDate() {
		return FeedDate;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public String getUrl() {
		return Url;
	}
	public String getWriter() {
		return Writer;
	}
	public String getID(){
		return ID;
	}
}
