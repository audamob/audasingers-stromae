package com.audamob.audasingers.stromae.model;

import java.io.Serializable;

public class Lyric implements Serializable {

	String SongName;
	String Date;
	String Album;
	
	public Lyric(String SongName,String Date,String Album) {
		// TODO Auto-generated constructor stub
		this.SongName=SongName;
		this.Date=Date;
		this.Album=Album;
		
	}
	public String getName() {
		return SongName;
	}
	public String getAlbum() {
		return Album;
	}
	public String getDate() {
		return Date;
	}
}
