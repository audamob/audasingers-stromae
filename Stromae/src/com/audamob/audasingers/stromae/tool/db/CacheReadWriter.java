package com.audamob.audasingers.stromae.tool.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;

import com.audamob.audasingers.stromae.constant.ApplicationConstants;
import com.audamob.audasingers.stromae.model.Music;
import com.audamob.audasingers.stromae.model.News;
import com.audamob.audasingers.stromae.model.PlayList;
import com.audamob.audasingers.stromae.model.Tweet;
import com.audamob.audasingers.stromae.model.Video;

public class CacheReadWriter {

	public static ArrayList<News> restore_News(Activity activity)
			throws IOException, ClassNotFoundException {
		FileInputStream fin = new FileInputStream(activity.getCacheDir()
				.getAbsolutePath() + "/newsnews");
		ObjectInputStream in = new ObjectInputStream(fin);
		ArrayList<News> object = (ArrayList<News>) in.readObject();
		in.close();
		return object;
	}

	public static void sauvegard_News(ArrayList<News> s, Activity activity)
			throws IOException {
		FileOutputStream fout = new FileOutputStream(activity.getCacheDir()
				.getAbsolutePath() + "/newsnews");
		ObjectOutputStream out = new ObjectOutputStream(fout);

		out.writeObject(s);
		out.close();
	}

	public static ArrayList<Tweet> restoreTweets(Activity activity)
			throws IOException, ClassNotFoundException {
		FileInputStream fin = new FileInputStream(activity.getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_TWEETS);
		ObjectInputStream in = new ObjectInputStream(fin);
		ArrayList<Tweet> object = (ArrayList<Tweet>) in.readObject();
		in.close();
		return object;
	}

	public static void sauvegardTweets(ArrayList<Tweet> s, Activity activity)
			throws IOException {
		FileOutputStream fout = new FileOutputStream(activity.getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_TWEETS);
		ObjectOutputStream out = new ObjectOutputStream(fout);

		out.writeObject(s);
		out.close();
	}

	public static ArrayList<Video> restoreFavoriteList(Activity activity)
			throws IOException, ClassNotFoundException {
		ArrayList<Video> list = null;
		try {
			list = restoreListVideos(activity);
		} catch (Exception e) {
			// TODO: handle exception
		}
		ArrayList<Video> favoritesList = new ArrayList<Video>();

		for (Video video : list) {

			if (video.isFavorite())
				favoritesList.add(video);
		}

		return favoritesList;
	}

	public static ArrayList<Video> restoreListVideos(Activity activity)
			throws IOException, ClassNotFoundException {
		FileInputStream fin = new FileInputStream(activity.getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_VIIDEO);
		ObjectInputStream in = new ObjectInputStream(fin);
		ArrayList<Video> object = (ArrayList<Video>) in.readObject();
		Log.i("VIDEOS",
				"liste des videos a afficher du cache : " + object.size());
		in.close();
		return object;
	}

	public static void sauvegardListVideos(ArrayList<Video> s, Activity activity)
			throws IOException {
		FileOutputStream fout = new FileOutputStream(activity.getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_VIIDEO);
		ObjectOutputStream out = new ObjectOutputStream(fout);

		out.writeObject(s);
		out.close();
	}

	public static ArrayList<Music> restoreListMusics(Activity activity)
			throws IOException, ClassNotFoundException {
		try {
			FileInputStream fin = new FileInputStream(activity.getCacheDir()
					.getAbsolutePath() + ApplicationConstants.CACHE_MUSIC);
			ObjectInputStream in = new ObjectInputStream(fin);
			ArrayList<Music> object = (ArrayList<Music>) in.readObject();
			in.close();
			return object;

		} catch (IOException fne) {
			return new ArrayList<Music>();
		}
	}

	public static void sauvegardListMusics(ArrayList<Music> s, Activity activity)
			throws IOException {
		FileOutputStream fout = new FileOutputStream(activity.getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_MUSIC);
		ObjectOutputStream out = new ObjectOutputStream(fout);

		out.writeObject(s);
		out.close();
	}

	public static ArrayList<PlayList> restorePlayListList(Activity activity)
			throws IOException, ClassNotFoundException {
		FileInputStream fin = new FileInputStream(activity.getCacheDir()
				.getAbsolutePath() + "/playlistlist");
		ObjectInputStream in = new ObjectInputStream(fin);
		ArrayList<PlayList> object = (ArrayList<PlayList>) in.readObject();
		in.close();
		return object;
	}

	public static void sauvegardPlayListList(ArrayList<PlayList> list,
			Activity activity) throws IOException {
		FileOutputStream fout = new FileOutputStream(activity.getCacheDir()
				.getAbsolutePath() + "/playlistlist");
		ObjectOutputStream out = new ObjectOutputStream(fout);

		out.writeObject(list);
		out.close();
	}

	// Account Manager
	public static String restore_Account(Activity activity) throws IOException,
			ClassNotFoundException {
		FileInputStream fin = new FileInputStream(activity.getCacheDir()
				.getAbsolutePath() + "/account");
		ObjectInputStream in = new ObjectInputStream(fin);
		String object = (String) in.readObject();
		in.close();
		return object;
	}

	public static void sauvegard_Account(String accountInfos, Activity activity)
			throws IOException {
		FileOutputStream fout = new FileOutputStream(activity.getCacheDir()
				.getAbsolutePath() + "/account");
		ObjectOutputStream out = new ObjectOutputStream(fout);

		out.writeObject(accountInfos);
		out.close();
	}

	// Restore Count Notification
	public static Integer restore_count_notification(Activity activity,
			String path) throws IOException, ClassNotFoundException {
		try {
			FileInputStream fin = new FileInputStream(activity.getCacheDir()
					.getAbsolutePath() + path);
			ObjectInputStream in = new ObjectInputStream(fin);
			Integer object = (Integer) in.readObject();
			in.close();
			return object;

		} catch (IOException fne) {
			return 0;
		}
	}
}
