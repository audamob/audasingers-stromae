package com.audamob.audasingers.model;

import java.io.Serializable;

import android.R.string;
import android.provider.ContactsContract.Data;

public class Tweet implements Serializable {

	
	String Tweet;
	java.util.Date Date;
	long Id;
	String State="New";
	
	public Tweet(String tweet,java.util.Date date,long id,String state) {
		// TODO Auto-generated constructor stub
		
		this.Date=date;
		this.Tweet=tweet;
		this.Id=id;
		this.State=state;
		
	}
	public String getTweet() {
		return Tweet;
	}
	public java.util.Date getDate() {
		return Date;
	}
	public long getID(){
		return Id;
	}
	public String getState(){
		return State;
	}
	public void setState(String state){
		State=state;
	}
}
