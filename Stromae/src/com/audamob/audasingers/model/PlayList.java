package com.audamob.audasingers.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayList implements Serializable {

	
	String SongName;
	ArrayList<Music> ListMusic;
	public PlayList(){
		
	}
	public PlayList(String SongName,ArrayList<Music> List) {
		// TODO Auto-generated constructor stub
		this.SongName=SongName;
		this.ListMusic=new ArrayList<Music>();
		this.ListMusic.addAll(List);
		
	}
	public String getName() {
		return SongName;
	}
	public ArrayList<Music> getListMusic() {
		return ListMusic;
	}
}
