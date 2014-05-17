package com.audamob.audasingers.stromae.fragment;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.SlidingMenu.ActivityBase;
import com.audamob.audasingers.stromae.SlidingMenu.SlidingMenuBuilderConcrete;
import com.audamob.audasingers.stromae.activity.DialogActivity;
import com.audamob.audasingers.stromae.activity.MusicPlayer;
import com.audamob.audasingers.stromae.model.Music;
import com.audamob.audasingers.stromae.model.News;
import com.audamob.audasingers.stromae.tool.adapter.SwipeyTabsPagerAdapter;
import com.audamob.audasingers.stromae.tool.db.CacheReadWriter;
import com.audamob.audasingers.stromae.tool.view.ImageResizerUtils;
import com.audamob.audasingers.stromae.view.services.ShareService;
import com.viewpagerindicator.TabPageIndicator;

public class MainContainerActivityTest extends ActivityBase {

	private static final String[] TITLES = { "Settings", "Videos", "Favorites",
			"Music", "Playlist", "Lyrics", "News", "Tweets", "Bio" };

	private TabPageIndicator mTabs;
	private ViewPager mViewPager;
	public static MainContainerActivityTest activity;

	public static int ITEM_VIDEOS = 0;
	public static int ITEM_VIDEOSFAVORITES = 10;
	public static int ITEM_MUSICS = 1;
	public static int ITEM_PLAYLIST = 7;
	public static int ITEM_LYRICS = 2;
	public static int ITEM_NEWS = 3;
	public static int ITEM_TWEETS = 4;
	public static int ITEM_BIO = 5;
	public static int ITEM_SETTINGS = 6;
	public static ShareService shareService;

	// Your need to put this method in every Activity class where you want to
	// have sliding menu.
	@Override
	public Class<?> setSlidingMenu() {
		// Each activity can have it's own sliding menu controlling builder
		// class.
		return SlidingMenuBuilderConcrete.class;
	}

	@Override
	public boolean enableHomeIconActionSlidingMenu() {
		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.audamob_version_deux_activity_maincontainer_test);
		Bundle b = getIntent().getExtras();
		boolean updateversion = b.getBoolean("updateversion");
		Log.i("VERSION", "isupdateversion :" + updateversion);
		if (!updateversion) {
			Intent i = new Intent(this, DialogActivity.class);
			startActivity(i);
		}

		ActionBar actionBar = getActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(false);

		ViewGroup mainContainer = (ViewGroup) findViewById(R.id.MainContainer);
		Typeface font = Typeface.createFromAsset(getAssets(), "Cyberverse.otf");

		ChangeCurrentFragment(ITEM_VIDEOS);

		// mViewPager.setOnPageChangeListener(mTabs);
		// mViewPager.setCurrentItem(1);
		ImageResizerUtils.setFont(this, mainContainer, font);

	}

	public static void ChangeCurrentFragment(int TabToLoad) {

		FragmentManager fragmentManager;
		fragmentManager = activity.getSupportFragmentManager();
		String[] TITLES = CreateTableString(TabToLoad);

		ViewGroup mainContainer = (ViewGroup) activity
				.findViewById(R.id.MainContainer);
		mainContainer.removeAllViews();

		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout content = (LinearLayout) inflater.inflate(
				R.layout.audamob_version_deux_fragment_swyper_frame, null);

		mainContainer.addView(content);

		ViewPager pager = (ViewPager) mainContainer
				.findViewById(R.id.viewpager);
		Log.i("TabIndicator_pbm", "pager = " + pager);

		SwipeyTabsPagerAdapter adapter = new SwipeyTabsPagerAdapter(activity,
				fragmentManager, TITLES, pager, TabToLoad);

		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) mainContainer
				.findViewById(R.id.indicator);
		Log.i("TabIndicator_pbm", "indicator = " + indicator);

		indicator.setViewPager(pager);

	}

	public static String[] CreateTableString(int i) {
		switch (i) {
		case 0:
			String[] tab_0 = { activity.getResources().getString(
					R.string.Video_text) };
			return tab_0;
		case 1:
			String[] tab_1 = { activity.getResources().getString(
					R.string.Music_text) };
			return tab_1;
		case 2:
			String[] tab_2 = { activity.getResources().getString(
					R.string.Lyrics_text) };
			return tab_2;
		case 3:

			ArrayList<News> newsList = new ArrayList<News>();
			try {
				newsList = CacheReadWriter.restore_News(activity);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (newsList.size() > 0) {
				String[] tab_3 = {
						activity.getResources()
								.getString(R.string.NewsMTV_text),
						activity.getResources().getString(R.string.Tweets_text) };
				return tab_3;
			} else {
				String[] tab_33 = {
						
						activity.getResources().getString(R.string.Tweets_text) };
				return tab_33;
			}

		case 4:

			String[] tab_4 = { activity.getResources().getString(
					R.string.Tweets_text) };
			return tab_4;
		case 5:
			String[] tab_5 = { activity.getResources().getString(
					R.string.Bio_text) };
			return tab_5;
		case 10:
			String[] tab_10 = { activity.getResources().getString(
					R.string.tab_favorites) };
			return tab_10;
		case 7:
			String[] tab_7 = { activity.getResources().getString(
					R.string.tab_playlists) };
			return tab_7;
		default:
			String[] tab_6 = { activity.getResources().getString(
					R.string.Settings_text) };
			return tab_6;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();

	}

	/**************
	 * 
	 * Media Player
	 * 
	 **************/
	public static MediaPlayer mediaPlayer = new MediaPlayer();
	public static Boolean MediaPlayer_Connect = false;
	public static Boolean MediaPlayer_Playing = false;
	public static int MediaPlayer_Mode = 1;
	public static String MediaPlayer_Url = "";
	public static Boolean MediaPlayer_finish_seeking = false;
	public static Boolean mediaPayerPrepared = false;

	public static void Stop_MediaPlayer() {
		mediaPayerPrepared = false;
		MediaPlayer_Connect = false;
		try {
			mediaPlayer.stop();
		} catch (Exception e) {
			// TODO: handle exception
		}
		MediaPlayer_Playing = false;
	}

	public static void Next_MediaPlayer() {
		mediaPayerPrepared = false;
		if (Mode_playlist_currentPosition == (Mode_playlist_List.size() - 1)) {
			Mode_playlist_currentPosition = 0;
		} else {
			Mode_playlist_currentPosition = Mode_playlist_currentPosition + 1;
		}
		Stop_MediaPlayer();
		MusicPlayer
				.Update_UI(Mode_playlist_List
						.get(Mode_playlist_currentPosition).getName(),
						Mode_playlist_List.get(Mode_playlist_currentPosition)
								.getFeatring());
		Lancer_MediaPlayer_mode_playlist(Mode_playlist_List,
				Mode_playlist_currentPosition);

	}

	public static void Previous_MediaPlayer() {
		mediaPayerPrepared = false;
		if (Mode_playlist_currentPosition == 0) {
			Mode_playlist_currentPosition = Mode_playlist_List.size() - 1;
		} else {
			Mode_playlist_currentPosition = Mode_playlist_currentPosition - 1;
		}
		Stop_MediaPlayer();
		MusicPlayer
				.Update_UI(Mode_playlist_List
						.get(Mode_playlist_currentPosition).getName(),
						Mode_playlist_List.get(Mode_playlist_currentPosition)
								.getFeatring());
		Lancer_MediaPlayer_mode_playlist(Mode_playlist_List,
				Mode_playlist_currentPosition);

	}

	static int Mode_playlist_currentPosition = 0;
	static ArrayList<Music> Mode_playlist_List = new ArrayList<Music>();

	public static void Lancer_MediaPlayer_mode_playlist(
			ArrayList<Music> ListMusic, int position) {
		mediaPayerPrepared = false;
		Log.d("Sizep", " current " + position);
		Mode_playlist_List = ListMusic;
		Mode_playlist_currentPosition = position;
		MediaPlayer_Connect = true;
		MediaPlayer_Url = Mode_playlist_List.get(Mode_playlist_currentPosition)
				.getUrl();
		;
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(MediaPlayer_Url);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {

				@Override
				public void onSeekComplete(MediaPlayer mp) {
					// TODO Auto-generated method stub
					MediaPlayer_finish_seeking = true;
				}
			});
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					if (MediaPlayer_Connect) {
						mediaPayerPrepared = true;

						mediaPlayer.start();
						MediaPlayer_Playing = true;
					}
				}
			});

		} catch (Exception e) {
			// TODO: handle exception
			Log.d("Exception_Player", "error :  " + e.toString());
		}

	}

	public static void Lancer_MediaPlayer(String url, int Mode) {

		Log.d("MediaPlayer_audasinguer", "start start ");
		MediaPlayer_Mode = Mode;
		MediaPlayer_Connect = true;
		MediaPlayer_Url = url;
		try {
			mediaPlayer.reset();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {

			mediaPlayer.setDataSource(MediaPlayer_Url);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

			mediaPlayer.prepareAsync();

			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					Log.d("MediaPlayer_audasinguer", "Prepared ");
					// TODO Auto-generated method stub
					if (MediaPlayer_Connect) {
						mediaPayerPrepared = true;
						mediaPlayer.start();
						Log.d("MediaPlayer_audasinguer", "started ");
						MediaPlayer_Playing = true;
					}
				}
			});

		} catch (Exception e) {
			// TODO: handle exception

		}

	}

	public static String PlaylistName = "";

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		Log.d("PlayList", " on Activity Result = "
				+ MainContainerActivityTest.PlaylistName);

		if (requestCode == 3) {
			try {
				PlaylistName = imageReturnedIntent.getType();

				Log.d("PlayList", " on Activity Result = "
						+ MainContainerActivityTest.PlaylistName);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (shareService != null) {
			if (shareService.getIsVisible()) {
				shareService.setDismiss();
			} else {
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}
	}
}
