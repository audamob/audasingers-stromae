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
		ListVideo.add(new Video("Je Cours", 216000, "wIrI_MOfxG0",689800,12119));
		ListVideo.add(new Video("Papaoutai", 232000, "oiKj0Z_Xnjc",689800,12119));
		ListVideo.add(new Video("Formidable", 294000, "S_xH7noaqTA",689800,12119));
		ListVideo.add(new Video("Tous Les Mêmes", 218000, "CAMWdvo71ls",689800,12119));
		ListVideo.add(new Video("Te Quiero", 213000, "vg1ajGmQFts",689800,12119));
		ListVideo.add(new Video("Peace Or Violence", 187000, "KzMWZXPCGUo",689800,12119));
		ListVideo.add(new Video("Alors On Danse", 234000, "VHoT4N43jK8",689800,12119));
		ListVideo.add(new Video("Dodo", 209000, "Ld01WzdLuFM",689800,12119));
		ListVideo.add(new Video("Pipi au lit", 192000, "84EVfh8INDo",689800,12119));
		ListVideo.add(new Video("Silence", 284000, "C7iSSANfkjM",689800,12119));
		ListVideo.add(new Video("Bienvenue chez moi", 190000, "QOQISCLKaKQ",689800,12119));
		ListVideo.add(new Video("Silence", 284000, "C7iSSANfkjM",689800,12119));
		ListVideo.add(new Video("House leluya", 223000, "nXL8dxHLTdU",689800,12119));
		ListVideo.add(new Video("Rail de Musique", 312000, "vUUrM-YHX8M",689800,12119));
		ListVideo.add(new Video("Humain a lEau", 272000, "49qhh0o2QHw",689800,12119));
		ListVideo.add(new Video("Promoson", 134000, "gNATICsg2jQ",689800,12119));
		ListVideo.add(new Video("Buzz Des Maines", 112000, "zjqhXGwFLD4",689800,12119));
		ListVideo.add(new Video("Faut que t'arrête le rap...", 303000, "DprE4vO8sbM",689800,12119));
		ListVideo.add(new Video("Le Rap C'est Simple", 242000, "ah6gPXbx6s4",689800,12119));

		return ListVideo;
	}
	
	public static ArrayList<Music> getListMusics() {
		ArrayList<Music> musicList;
		musicList = new ArrayList<Music>();
		musicList.add(new Music("Papaoutai",
				"http://mp3.shmidt.net/files_play/3b9c257a91c0c1440ebfc30829b138cb/mp3/S/Stromae/Stromae_-_Papaoutai_mp3.shmidt.net.mp3",32000,7899,228000));

		musicList.add(new Music(
						"Je Cours",
						"http://muz-time.ru/player/download/clubmp3/club1/08-stromae-silence.mp3",32000,7899,276000));
		
		musicList.add(new Music(
						"Silence",
						"http://bigtimebeats.com/wp-content/uploads/She-Will-feat.-Drake-Rick-Ross-Lil-Wayne-BIGTIMEBEATS.com_.mp3",32000,7899,246000));

		musicList.add(new Music(
				"Alors on Danse",
				"http://veckans.superautomatic.com/1827460183/Stromae%20-%20Alors%20on%20Danse.mp3",32000,7899,207000));
		
		musicList.add(new Music(
				"Formidable",
				"http://stream.get-tune.net/file/210663613/37620576/2986789443/053421dd707861d6/Stromae_-_Formidable_Radio_Edition_2013_htp_(get-tune.net).mp3",32000,7899,215000));
		
		musicList.add(new Music(
				"Tous Les Memes",
				"http://dailymusic.ru/sites/default/files/Stromae_-_Tous_Les_Memes_(Album_Version)_(DailyMusic.ru).mp3",32000,7899,210000));
		
		musicList.add(new Music(
				"Peace or Violence",
				"http://media.ru-fm.ru/uploads/songs/0000/0536/Stromae_-_Peace_Or_Violence.mp3",32000,7899,184000));
		
		musicList.add(new Music(
				"Up Saw Liz",
				"http://ia600604.us.archive.org/12/items/FromMstore/upsawliz.mp3",32000,7899,213000));
		
		musicList.add(new Music(
				"Te quiero",
				"http://michaelaxx.free.fr/02-stromae-te_quiero.mp3",32000,7899,195000));
		
		musicList.add(new Music(
				"Bienvenue Chez Moi",
				"http://a.tumblr.com/tumblr_lnlke77QPN1qb8kz6o1.mp3",32000,7899,172000));
		
		musicList.add(new Music(
				"Rail De Musique",
				"http://muz-time.ru/player/download/clubmp3/club1/04-stromae-rail_de_musique.mp3",32000,7899,246000));
		
		musicList.add(new Music(
				"SummerTime",
				"http://ia600607.us.archive.org/4/items/stromae_free/Str6.mp3",32000,7899,186000));
		
		musicList.add(new Music(
				"Dodo",
				"http://a.tumblr.com/tumblr_m97fa9o13w1rzl5ppo1.mp3",32000,7899,237000));
		
		musicList.add(new Music(
				"House'llelujah",
				"http://promodj.com/source/2286129/Stromae - House Llelujah (Original Studio Acapella) EXCLUSIVE! (promodj.com).mp3",32000,7899,205000));
		
		musicList.add(new Music(
				"Ta Fete",
				"http://wap.tegos.ru/new/mp3_full/Stromae_-_Ta_Fete.mp3",32000,7899,174000));
		
		musicList.add(new Music(
				"Batard",
				"http://prostopleer.com/mobile/files/7489324agNn.mp3",32000,7899,207000));
		
		musicList.add(new Music(
				"Ave Cesaria",
				"http://a.tumblr.com/tumblr_mrsrl0SaiS1rznveho1.mp3",32000,7899,248000));
		
		musicList.add(new Music(
				"Moules Frites",
				"http://a.tumblr.com/tumblr_mrmseiCXA31qfkitso1.mp3",32000,7899,158000));
		
		musicList.add(new Music(
				"Carmen",
				"http://a.tumblr.com/tumblr_mrlbu1zmiD1svv2f6o1.mp3",32000,7899,191000));
		
		musicList.add(new Music(
				"Sommeil",
				"http://a.tumblr.com/tumblr_ms36ghTRXc1qgd0eqo1.mp3",32000,7899,218000));
		
		musicList.add(new Music(
				"Avf",
				"http://freshmp3music.ru/music/08.2013/stromae_feat._maitre_gims_orelsan_-_avf_(www.freshmp3music.ru).mp3",32000,7899,224000));
		
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
		LyricList.add(new Lyric("Ta FÍte", "2013", "Racine CarrÈe"));
		LyricList.add(new Lyric("Papaoutai", "2013", "Racine CarrÈe"));
		LyricList.add(new Lyric("B‚tard", "2013", "Racine CarrÈe"));
		LyricList.add(new Lyric("Ave Cesaria", "2013", "Racine CarrÈe"));
		LyricList.add(new Lyric("Tous Les MÍmes", "2013", "Racine CarrÈe"));
		LyricList.add(new Lyric("Formidable", "2013", "Racine CarrÈe"));
		LyricList.add(new Lyric("Moules Frites", "2013", "Racine CarrÈe"));
		LyricList.add(new Lyric("Carmen", "2013", "Racine CarrÈe"));
		LyricList.add(new Lyric("Humain A L'eau", "2013", "Racine CarrÈe"));
		LyricList.add(new Lyric("Quand C'est?", "2013", "Racine CarrÈe"));
		LyricList.add(new Lyric("Sommeil", "2013", "Racine CarrÈe"));
		LyricList.add(new Lyric("Merci", "2013", "Racine CarrÈe"));
		LyricList.add(new Lyric("Avf", "2013", "Racine CarrÈe"));
		LyricList.add(new Lyric("Alors On Danse Remix", "Unkown", "Other Songs"));
		LyricList.add(new Lyric("Buzz Des Maines", "Unkown", "Other Songs"));
		LyricList.add(new Lyric("Promoson", "Unkown", "Other Songs"));
		LyricList.add(new Lyric("Tous Les Memes", "Unkown", "Other Songs"));


		return LyricList;
	}
	
}

