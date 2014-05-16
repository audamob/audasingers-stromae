package com.audamob.audasingers.stromae.data;

import java.util.ArrayList;

import com.audamob.audasingers.stromae.model.Lyric;
import com.audamob.audasingers.stromae.model.Music;
import com.audamob.audasingers.stromae.model.Video;

public class DataSource {

	
	public static ArrayList<Video> getListVideos() {
		ArrayList<Video> ListVideo;
		ListVideo = new ArrayList<Video>();
		ListVideo.add(new Video("C'est Stromae", 247000, "AChnoHafrW0",32100,1099));
		ListVideo.add(new Video("Up saw liz", 180000, "bD40dWsG6m8",689800,12119));

		return ListVideo;
	}
	
	public static ArrayList<Music> getListMusics() {
		ArrayList<Music> musicList;
		musicList = new ArrayList<Music>();
		musicList.add(new Music("Papaoutai",
				"http://mp3.shmidt.net/files_play/3b9c257a91c0c1440ebfc30829b138cb/mp3/S/Stromae/Stromae_-_Papaoutai_mp3.shmidt.net.mp3",32000,7899,228000));

		musicList
				.add(new Music(
						"Je Cours",
						"http://muz-time.ru/player/download/clubmp3/club1/08-stromae-silence.mp3",32000,7899,276000));
		musicList
				.add(new Music(
						"Silence",
						"http://bigtimebeats.com/wp-content/uploads/She-Will-feat.-Drake-Rick-Ross-Lil-Wayne-BIGTIMEBEATS.com_.mp3",32000,7899,246000));

		
		return musicList;
	}
	
	public static ArrayList<Lyric> getListLyrics() {
		ArrayList<Lyric>	LyricList = new ArrayList<Lyric>();
		LyricList.add(new Lyric("Bienvenue chez moi", "2010", "Cheese"));
		LyricList.add(new Lyric("Te quiero", "2010", "Cheese"));
		LyricList.add(new Lyric("Peace or Violence", "2010", "Cheese"));
		LyricList.add(new Lyric("Rail de musique", "2010", "Cheese"));
		LyricList.add(new Lyric("Alors on danse", "2010", "Cheese"));
		LyricList.add(new Lyric("Summertime", "2010", "Cheese"));
		LyricList.add(new Lyric("Dodo", "2010", "Cheese"));
		LyricList.add(new Lyric("Silence", "2010", "Cheese"));
		LyricList.add(new Lyric("Je cours", "2010", "Cheese"));
		LyricList.add(new Lyric("House'llelujah", "2010", "Cheese"));
		LyricList.add(new Lyric("Alors on danse (90's Remix)", "2010", "Cheese"));
		LyricList.add(new Lyric("Ta Fête", "2013", "Racine Carrée"));
		LyricList.add(new Lyric("Papaoutai", "2013", "Racine Carrée"));
		LyricList.add(new Lyric("Bâtard", "2013", "Racine Carrée"));
		LyricList.add(new Lyric("Ave Cesaria", "2013", "Racine Carrée"));
		LyricList.add(new Lyric("Tous Les Mêmes", "2013", "Racine Carrée"));
		LyricList.add(new Lyric("Formidable", "2013", "Racine Carrée"));
		LyricList.add(new Lyric("Moules Frites", "2013", "Racine Carrée"));
		LyricList.add(new Lyric("Carmen", "2013", "Racine Carrée"));
		LyricList.add(new Lyric("Humain A L'eau", "2013", "Racine Carrée"));
		LyricList.add(new Lyric("Quand C'est?", "2013", "Racine Carrée"));
		LyricList.add(new Lyric("Sommeil", "2013", "Racine Carrée"));
		LyricList.add(new Lyric("Merci", "2013", "Racine Carrée"));
		LyricList.add(new Lyric("Avf", "2013", "Racine Carrée"));
		LyricList.add(new Lyric("Alors On Danse Remix", "Unkown", "Other Songs"));
		LyricList.add(new Lyric("Buzz Des Maines", "Unkown", "Other Songs"));
		LyricList.add(new Lyric("Promoson", "Unkown", "Other Songs"));
		LyricList.add(new Lyric("Tous Les Memes", "Unkown", "Other Songs"));
		
		

		return LyricList;
	}
	
}

