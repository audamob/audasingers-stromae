package com.audamob.audasingers.stromae.model;

import java.io.Serializable;

public class Video implements Serializable {

	
	String videoName;
	long duration;
	String youtubeURL;
	String tag;
	boolean favorite=false;
	int nbrPlay = 45000;
	int nbrFavorite = 76000;
	

	public Video(String videoName,long duration,String Url,int nbrPlay,int nbrFavorite) {
		this.videoName=videoName;
		this.duration=duration;
		this.youtubeURL=Url;
		this.nbrPlay = nbrPlay;
		this.nbrFavorite = nbrFavorite;
		
	}
	
	public Video(String videoName,long duration,String Url) {
		this.videoName=videoName;
		this.duration=duration;
		this.youtubeURL=Url;
		
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String songName) {
		videoName = songName;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long durationSong) {
		this.duration = durationSong;
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String getYoutubeURL() {
		return youtubeURL;
	}

	public void setYoutubeURL(String youtubeURL) {
		this.youtubeURL = youtubeURL;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	

	public int getNbrPlay() {
		return nbrPlay;
	}

	public void setNbrPlay(int nbrPlay) {
		this.nbrPlay = nbrPlay;
	}

	public int getNbrFavorite() {
		return nbrFavorite;
	}

	public void setNbrFavorite(int nbrFavorite) {
		this.nbrFavorite = nbrFavorite;
	}

}
