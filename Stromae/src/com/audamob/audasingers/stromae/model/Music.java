package com.audamob.audasingers.stromae.model;

import java.io.Serializable;

public class Music implements Serializable {

	
	String SongName;
	String Url;
	String Featring="";
    String playlistName="";
	String tag;	
	int postionDansLaListe=0;
	int nbrPlay = 45000;
	int nbrFavorite = 76000;
	long duration;

	public Music(String SongName,String url,int nbrPlay,int nbrFavorite,long duration) {
		this.SongName=SongName;
		this.Url=url;
		this.nbrPlay = nbrPlay;
		this.nbrFavorite = nbrFavorite;
		this.duration = duration;
		
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

	public Music(String SongName,String url,String feat,int nbrPlay,int nbrFavorite,long duration) {
		// TODO Auto-generated constructor stub
		this.SongName=SongName;
		this.Url=url;
		this.Featring=feat;
		this.nbrPlay = nbrPlay;
		this.nbrFavorite = nbrFavorite;
		this.duration = duration;
		
	}
	
	public String getName() {
		return SongName;
	}
	
	public String getUrl() {
		return Url;
	}
	
	public String getFeatring() {
		return Featring;
	}
	
	public String getTag() {
		return tag;
	}
	
	public int getPostionDansLaListe() {
		return postionDansLaListe;
	}
	
	public String getPlaylistName() {
		return playlistName;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public void setSongName(String songName) {
		SongName = songName;
	}
	
	public void setUrl(String url) {
		Url = url;
	}
	
	public void setFeatring(String featring) {
		Featring = featring;
	}

	public void setNamePlaylist(String favoriteName) {
		this.playlistName = favoriteName;
	}
	

	
	public void setPostionDansLaListe(int postionDansLaListe) {
		this.postionDansLaListe = postionDansLaListe;
	}
	
	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
