/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.audamob.audasingers.stromae.activity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.constant.ApplicationConstants;
import com.audamob.audasingers.stromae.third.youtube.YouTubeFailureRecoveryActivity;
import com.audamob.audasingers.stromae.tool.view.ImageResizerUtils;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Sample activity showing how to properly enable custom fullscreen behavior.
 * <p>
 * This is the preferred way of handling fullscreen because the default
 * fullscreen implementation will cause re-buffering of the video.
 */
public class Videos_Interface extends YouTubeFailureRecoveryActivity implements
		View.OnClickListener, CompoundButton.OnCheckedChangeListener,
		YouTubePlayer.OnFullscreenListener {

	private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9 ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
			: ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

	private LinearLayout baseLayout;
	private YouTubePlayerView playerView;
	RelativeLayout L_playerView;
	private YouTubePlayer player;

	private View otherViews;
	private String Tag;
	private boolean fullscreen;

	String Lyrics = "";
	String SongName = "";
	String YoutubeUrl = "";
	long Duration;
	// ArrayList<Video> FavoriteList;
	// Boolean PopUpOnshare=false;
	// PopupWindow PopUpShare;
	Activity activity;

	class SharePm {
		String Name;
		Drawable d;
		ResolveInfo ResolveInfo;
	}

	int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.audamob_version_deux_activity_video_player);
		baseLayout = (LinearLayout) findViewById(R.id.layout);
		playerView = (YouTubePlayerView) findViewById(R.id.player);
		L_playerView = (RelativeLayout) findViewById(R.id.L_player);

		otherViews = findViewById(R.id.other_views);

		// You can use your own button to switch to fullscreen too

		playerView.initialize(ApplicationConstants.DEVELOPER_KEY, this);

		Bundle b = getIntent().getExtras();
		SongName = b.getString("name");
		YoutubeUrl = b.getString("Url");

		Tag = YoutubeUrl;

		YoutubeUrl = "https://www.youtube.com/watch?v=" + Tag;

		Duration = b.getLong("duration");
		activity = this;

		doLayout();
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		this.player = player;
		setControlsEnabled();
		// Specify that we want to handle fullscreen behavior ourselves.
		player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
		player.setOnFullscreenListener(this);
		if (!wasRestored) {
			player.cueVideo(Tag);
		}
		this.player.setShowFullscreenButton(true);
		player.setShowFullscreenButton(true);
	}

	@Override
	protected YouTubePlayer.Provider getYouTubePlayerProvider() {
		return playerView;
	}

	@Override
	public void onClick(View v) {
		player.setFullscreen(!fullscreen);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int controlFlags = player.getFullscreenControlFlags();
		if (isChecked) {
			// If you use the FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE,
			// your activity's normal UI
			// should never be laid out in landscape mode (since the video will
			// be fullscreen whenever the
			// activity is in landscape orientation). Therefore you should set
			// the activity's requested
			// orientation to portrait. Typically you would do this in your
			// AndroidManifest.xml, we do it
			// programmatically here since this activity demos fullscreen
			// behavior both with and without
			// this flag).
			setRequestedOrientation(PORTRAIT_ORIENTATION);
			controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			controlFlags &= ~YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
		}
		player.setFullscreenControlFlags(controlFlags);
		player.setOnFullscreenListener(new OnFullscreenListener() {

			@Override
			public void onFullscreen(boolean arg0) {
				// TODO Auto-generated method stub
				fullscreen = true;
			}
		});
	}

	private void doLayout() {
		LinearLayout.LayoutParams L_playerParams = (LinearLayout.LayoutParams) L_playerView
				.getLayoutParams();
		RelativeLayout.LayoutParams playerParams = (RelativeLayout.LayoutParams) playerView
				.getLayoutParams();
		if (fullscreen) {
			// When in fullscreen, the visibility of all other views than the
			// player should be set to
			// GONE and the player should be laid out across the whole screen.

			L_playerParams.width = LayoutParams.MATCH_PARENT;
			L_playerParams.height = LayoutParams.MATCH_PARENT;
			playerParams.width = LayoutParams.MATCH_PARENT;
			playerParams.height = LayoutParams.MATCH_PARENT;
			playerParams.leftMargin = 0;

			otherViews.setVisibility(View.GONE);
		} else {
			// This layout is up to you - this is just a simple example
			// (vertically stacked boxes in
			// portrait, horizontally stacked in landscape).
			otherViews.setVisibility(View.VISIBLE);
			ViewGroup.LayoutParams otherViewsParams = otherViews
					.getLayoutParams();
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

				L_playerParams.width = otherViewsParams.width = 0;
				L_playerParams.height = MATCH_PARENT;
				L_playerParams.weight = 3;
				otherViewsParams.height = MATCH_PARENT;
				otherViewsParams.width = 0;

				playerParams.height = WRAP_CONTENT;
				playerParams.width = MATCH_PARENT;

				playerParams.addRule(RelativeLayout.CENTER_VERTICAL);
				playerParams.leftMargin = ImageResizerUtils.dpToPx(10, this);

				baseLayout.setOrientation(LinearLayout.HORIZONTAL);
			}
			setControlsEnabled();
		}
	}

	private void setControlsEnabled() {

		// fullscreenButton.setEnabled(player != null);
	}

	@Override
	public void onFullscreen(boolean isFullscreen) {
		fullscreen = isFullscreen;
		doLayout();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		doLayout();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (!fullscreen) {
			super.onBackPressed();
		} else {
			fullscreen = false;
			this.player.setFullscreen(false);
		}

		super.onBackPressed();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		ThreadLoad threadLoad = new ThreadLoad(handlerLoad);
		threadLoad.start();
	}

	TextView TextLyrics;
	final Handler handlerLoad = new Handler() {

		public void handleMessage(Message msg) {
			try {
				TextLyrics = (TextView) findViewById(R.id.TextLyrics);
				String lyric = msg.getData().toString();
				String cast = "Bundle[{url=";

				lyric = lyric.substring(cast.length() + 1, lyric.length() - 3);

				TextLyrics.setText(lyric);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};

	private class ThreadLoad extends Thread {
		Handler mHandler;

		ThreadLoad(Handler h) {
			mHandler = h;
		}

		public void run() {
			try {
				currentThread().sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String UrlBack = "";
			try {

				String SongNamee = SongName.replace(" ", "_");

				String UrlLyrics = ApplicationConstants.LYRIC_PATH
						+ ApplicationConstants.LYRIC_SINGER_NAME + SongNamee;
				org.jsoup.nodes.Document doc = Jsoup.connect(UrlLyrics).get();
				Elements content = doc.getElementsByTag("div");
				// Elements links = content.getElementsByTag("a");
				for (Element link : content) {
					String linkHref = link.className();
					if (linkHref.equals("lyricbox")) {
						Log.d("Lyrics", "lyrics= " + linkHref);
						String linkText = link.text();
						try {
							linkText = linkText.substring(0,
									linkText.lastIndexOf("Send "));
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							linkText = linkText.substring(0,
									linkText.lastIndexOf("</lyrics> "));
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							linkText = linkText.substring(
									linkText.indexOf("to your Cell") + 12,
									linkText.length());
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							linkText = linkText
									.substring(0, linkText
											.lastIndexOf("we are not licensed"));
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							linkText = linkText.replace(",", "\n\n");
						} catch (Exception e) {
							// TODO: handle exception
						}
						UrlBack = linkText;

					}
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("url", "" + UrlBack);
			msg.setData(b);
			mHandler.sendMessage(msg);

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {
			player.release();
		} catch (Exception e) {
			// TODO: handle exception
		}

		super.onDestroy();
	}
}
