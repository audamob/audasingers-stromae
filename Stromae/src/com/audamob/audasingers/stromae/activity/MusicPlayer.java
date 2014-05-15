package com.audamob.audasingers.stromae.activity;

//Tabatoo
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.SlidingMenu.ActivityBase;
import com.audamob.audasingers.stromae.constant.ApplicationConstants;
import com.audamob.audasingers.stromae.fragment.MainContainerActivityTest;
import com.audamob.audasingers.stromae.model.Music;
import com.audamob.audasingers.stromae.model.PlayList;
import com.audamob.audasingers.stromae.tool.view.ImageResizerUtils;

public class MusicPlayer extends ActivityBase {

	String Lyrics = "";
	static String SongName = "";
	String Album = "";
	static String Feat = "";
	MediaPlayer Mp;
	String iframe = "";
	ImageView Playe, Pause, Next,Previews;

	Boolean PopUpOnshare = false;
	PopupWindow PopUpShare;
	static Activity activity;
	static MusicPlayer MusicPlayerInstance;

	class SharePm {
		String Name;
		Drawable d;
		ResolveInfo ResolveInfo;
	}

	SeekBar seekBar;
	long progres = 0;

	boolean MODE_REPLAY = false;
	boolean MODE_SINGLE = false;
	boolean MODE_PLAYLIST = false;
	// ArrayList<PlayList> PlaylistMusicList;
	ArrayList<Music> ListPlaylist = new ArrayList<Music>();
	private SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
	int currentposition = 0;
	ThreadUpdate_seekBar thUpdate;
	int mode = 1;

	static Boolean play_enbaled, pause_enabled = false;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public boolean enableHomeIconActionSlidingMenu() {
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aaudamob_version_deux_activity_music_player_interface);

		ActionBar actionBar = getActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(false);

		ViewGroup mainContainer = (ViewGroup) findViewById(R.id.MainContainer);
		Typeface font = Typeface.createFromAsset(getAssets(), "ExoMedium.otf");

		ImageResizerUtils.setFont(this, mainContainer, font);

		Bundle b = getIntent().getExtras();
		MusicPlayerInstance = this;

		mode = b.getInt("mode");
		if (mode == 1) {
			SongName = b.getString("name");
			Album = b.getString("album");
			Feat = b.getString("feat");
			Log.e("LyricProblem", "Feat : " + Feat);
			MainContainerActivityTest.Lancer_MediaPlayer(Album, mode);
		}

		if (mode == 2) {

			ListPlaylist = ((PlayList) b.getSerializable("playlist"))
					.getListMusic();
			SongName = ListPlaylist.get(currentposition).getName();
			Album = ListPlaylist.get(currentposition).getUrl();
			Feat = ListPlaylist.get(currentposition).getFeatring();

			MainContainerActivityTest.Lancer_MediaPlayer_mode_playlist(
					ListPlaylist, 0);
		}

		activity = this;
		Pause = (ImageView) findViewById(R.id.Pause);
		Playe = (ImageView) findViewById(R.id.Play);
		Next = (ImageView) findViewById(R.id.Next);
		Next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mode == 2) {
					progress_seek = false;
					MainContainerActivityTest.Next_MediaPlayer();
				}
			}
		});
		Previews = (ImageView) findViewById(R.id.Previeous);
		Previews.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mode == 2) {
					progress_seek = false;
					MainContainerActivityTest.Previous_MediaPlayer();
				}
			}
		});

		seekBar = (SeekBar) findViewById(R.id.PlayerSeekBar);
		seekBar.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		seekBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}

		});
		seekBar.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub

			}
		});
		seekBar.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				progres = progress;
				Log.d("Prog", "change : " + progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				try {
					/*
					 * progress_seek=true; updat_UI_Seek();
					 * UpdaterProgress_seek_Thread up=new
					 * UpdaterProgress_seek_Thread(seekHandler); up.start();
					 */
					MainContainerActivityTest.mediaPlayer.seekTo((int) (progres
							* MainContainerActivityTest.mediaPlayer
									.getDuration() / 100));

				} catch (Exception e) {

				}
			}

		});

		Pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Log.d("MediaPlayer_audasinguer", "Pause ");
				Log.d("MediaPlayer_audasinguer", "Pause : play_enabled "
						+ play_enbaled);
				Log.d("MediaPlayer_audasinguer", "Pause : progress_seek "
						+ progress_seek);
				Log.d("MediaPlayer_audasinguer", "mediaPayerPrepared  "
						+ MainContainerActivityTest.mediaPayerPrepared);
				if (MainContainerActivityTest.mediaPayerPrepared)
					if (play_enbaled && !progress_seek) {
						Pause.setVisibility(View.INVISIBLE);
						Playe.setVisibility(View.VISIBLE);
						try {

							MainContainerActivityTest.mediaPlayer.pause();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
			}
		});
		Playe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.d("MediaPlayer_audasinguer", "onClick ");
				Log.d("MediaPlayer_audasinguer", "mediaPayerPrepared  "
						+ MainContainerActivityTest.mediaPayerPrepared);
				if (MainContainerActivityTest.mediaPayerPrepared)
					if (play_enbaled && !progress_seek) {
						Log.d("MediaPlayer_audasinguer", "onClick 2");

						Pause.setVisibility(View.VISIBLE);
						Playe.setVisibility(View.INVISIBLE);
						try {

							MainContainerActivityTest.mediaPlayer.start();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
			}
		});

		// Updater progress lyric

		// Updater progress lyric
		thUpdate = new ThreadUpdate_seekBar(handlerUpdater);
		thUpdate.start();
		Update_UI(SongName, Feat);

	}

	static int periode_attente = 0;

	public static void Update_UI(String songname, String feat) {
		SongName = songname;
		Feat = feat;
		((ImageView) MusicPlayerInstance.findViewById(R.id.Progress))
				.setVisibility(View.VISIBLE);
		((ImageView) MusicPlayerInstance.findViewById(R.id.replay))
				.setVisibility(View.VISIBLE);
		((ImageView) MusicPlayerInstance.findViewById(R.id.Play))
				.setVisibility(View.VISIBLE);
		((ImageView) MusicPlayerInstance.findViewById(R.id.Pause))
				.setVisibility(View.INVISIBLE);

		if (Build.VERSION.SDK_INT >= 16) {
			((ImageView) MusicPlayerInstance.findViewById(R.id.Play))
					.setBackground(MusicPlayerInstance.getResources()
							.getDrawable(R.drawable.audamob_version_deux_play_desable));

		} else {

			((ImageView) MusicPlayerInstance.findViewById(R.id.Play))
					.setBackgroundDrawable(MusicPlayerInstance.getResources()
							.getDrawable(R.drawable.audamob_version_deux_play_desable));
		}

		TextView namesong = (TextView) MusicPlayerInstance
				.findViewById(R.id.NameSong);

		namesong.setText("Music : " + songname);

		Lyric_loaded = false;
		progress_lyric = true;
		progress_player = true;
		threadLoad = MusicPlayerInstance.new ThreadLoad(handlerLoad);
		threadLoad.start();
		p_player = MusicPlayerInstance.new UpdaterProgress_player_Thread(
				progress_player_handler);
		p_player.start();
		p_lyric_Thread = MusicPlayerInstance.new UpdaterProgress_lyric_Thread(
				progressHandler);
		p_lyric_Thread.start();

	}

	static UpdaterProgress_lyric_Thread p_lyric_Thread;
	static UpdaterProgress_player_Thread p_player;
	static ThreadLoad threadLoad;

	@Override
	protected void onStart() {
		super.onStart();
	}

	boolean displayed = false;

	static int compteur = 0;

	Boolean Done = false;

	int count = 0;



	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (PopUpOnshare) {
			PopUpShare.dismiss();
			PopUpOnshare = false;
		} else {
			MainContainerActivityTest.Stop_MediaPlayer();
			finish();
			overridePendingTransition(R.anim.push_down_out_back,
					R.anim.push_down_in_back);
		}

		threadLoad.setActivate(false);
		thUpdate.setActivate(false);
		p_lyric_Thread.setActivate(false);
		p_player.setActivate(false);

	}

	

	static TextView TextLyrics;

	/*******
	 * 
	 * Update SeekBar Thread
	 * 
	 */
	final Handler handlerUpdater = new Handler() {
		public void handleMessage(Message msg) {
			if (thUpdate.activate) {
				if (MainContainerActivityTest.mediaPlayer.getCurrentPosition() > (MainContainerActivityTest.mediaPlayer
						.getDuration() - 3 * 1000)) {

					if (mode == 1) {
						((ImageView) findViewById(R.id.Play))
								.setVisibility(View.VISIBLE);
						((ImageView) findViewById(R.id.Pause))
								.setVisibility(View.INVISIBLE);
						try {
							MainContainerActivityTest.mediaPlayer.pause();
							MainContainerActivityTest.mediaPlayer.seekTo(0);
						} catch (Exception e) {
							// TODO: handle exception
						}
					} else if (mode == 2) {
						((ImageView) findViewById(R.id.Play))
								.setVisibility(View.VISIBLE);
						play_enbaled = false;
						((ImageView) findViewById(R.id.Pause))
								.setVisibility(View.INVISIBLE);
						pause_enabled = false;
						if (Build.VERSION.SDK_INT >= 16) {
							((ImageView) findViewById(R.id.Play))
									.setBackground(activity.getResources()
											.getDrawable(
													R.drawable.audamob_version_deux_play_desable));

						} else {

							((ImageView) findViewById(R.id.Play))
									.setBackgroundDrawable(activity
											.getResources().getDrawable(
													R.drawable.audamob_version_deux_play_desable));
						}

						MainContainerActivityTest.Next_MediaPlayer();

					}
				}

				try {
					progres = 100
							* MainContainerActivityTest.mediaPlayer
									.getCurrentPosition()
							/ MainContainerActivityTest.mediaPlayer
									.getDuration();
					seekBar.setProgress((int) progres);
					((TextView) findViewById(R.id.SeekStartTime))
							.setText(""
									+ formatter
											.format(MainContainerActivityTest.mediaPlayer
													.getCurrentPosition()));
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					if (MainContainerActivityTest.mediaPlayer.isPlaying())
						((TextView) findViewById(R.id.SeekDuration))
								.setText(formatter
										.format(MainContainerActivityTest.mediaPlayer
												.getDuration()));
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
	};
	boolean update_seek_bar = true;

	private class ThreadUpdate_seekBar extends Thread {

		Handler mHandler;
		boolean activate = true;

		public void setActivate(boolean state) {
			this.activate = state;
		}

		ThreadUpdate_seekBar(Handler h) {
			mHandler = h;
		}

		public void run() {
			while (MainContainerActivityTest.MediaPlayer_Connect && activate) {
				try {
					currentThread().sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (MainContainerActivityTest.MediaPlayer_Connect && activate) {
					Message msg = mHandler.obtainMessage();
					Bundle b = new Bundle();
					b.putString("url", "");
					msg.setData(b);
					mHandler.sendMessage(msg);
				}
			}
		}
	}

	/*******
	 * 
	 * Load Lyric Thread
	 * 
	 */
	static Boolean Lyric_loaded = false;
	final static Handler handlerLoad = new Handler() {
		public void handleMessage(Message msg) {
			
			try {
				if (threadLoad.activate) {
					TextView TextLyrics = (TextView) activity
							.findViewById(R.id.TextLyrics);
					Log.d("Lyric_P","lyric : 111 ");
					String lyric = msg.getData().toString();
					Log.d("Lyric_P","lyric : 112 ");
					String cast = "Bundle[{url=";
					try {
						lyric = lyric.substring(cast.length() + 1,
								lyric.length() - 3);
					} catch (Exception e) {
						// TODO: handle exception
						lyric ="Sorry Lyric not availble";
					}
					
					TextLyrics.setText(lyric);
					Lyric_loaded = true;
					
				}
			} catch (Exception e) {

			}
		}
	};

	public class ThreadLoad extends Thread {
		Handler mHandler;
		boolean activate = true;

		ThreadLoad(Handler h) {
			mHandler = h;
		}

		public void setActivate(boolean state) {
			this.activate = state;
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

				String UrlLyrics = "";
				if (Feat.length() < 1) {
					UrlLyrics = ApplicationConstants.LYRIC_PATH
							+ ApplicationConstants.LYRIC_SINGER_NAME
							+ SongNamee;
				} else {
					UrlLyrics = ApplicationConstants.LYRIC_PATH + Feat + ":"
							+ SongNamee;
				}
				Log.d("Lyric_P","---------> "+UrlLyrics);
				org.jsoup.nodes.Document doc = Jsoup.connect(UrlLyrics).get();
				
				Elements content = doc.getElementsByTag("div");
				
				// Elements links = content.getElementsByTag("a");
				for (Element link : content) {
					String linkHref = link.className();
					if (linkHref.equals("lyricbox")) {
						String linkText = link.text();
						try {
							linkText = linkText.substring(0,
									linkText.lastIndexOf("Send "));
						} catch (Exception e) {
						}
						try {
							linkText = linkText.substring(0,
									linkText.lastIndexOf("</lyrics> "));

						} catch (Exception e) {
						}
						try {
							linkText = linkText.substring(
									linkText.indexOf("to your Cell") + 12,
									linkText.length());

						} catch (Exception e) {
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
			if (this.activate) {

				Message msg = mHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putString("url", "" + UrlBack);
				Log.d("Lyric_P","lyric :kikha "+UrlBack);
				msg.setData(b);
				mHandler.sendMessage(msg);
			}

		}
	}

	/*******
	 * 
	 * Progress Lyric Thread
	 * 
	 */
	static Boolean progress_lyric = true;
	private static Handler progressHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (p_lyric_Thread.activate) {
				if (Lyric_loaded) {
					progress_lyric = false;
					((ImageView) MusicPlayerInstance
							.findViewById(R.id.Progress))
							.setVisibility(View.INVISIBLE);
				} else {
					if (compteur == 0) {
						if (Build.VERSION.SDK_INT >= 16) {
							((ImageView) MusicPlayerInstance
									.findViewById(R.id.Progress))
									.setBackground(MusicPlayerInstance
											.getResources().getDrawable(
													R.drawable.audamob_version_deux_progres1));

						} else {

							((ImageView) MusicPlayerInstance
									.findViewById(R.id.Progress))
									.setBackgroundDrawable(MusicPlayerInstance
											.getResources().getDrawable(
													R.drawable.audamob_version_deux_progres1));
						}

						compteur = 1;
					} else if (compteur == 1) {
						if (Build.VERSION.SDK_INT >= 16) {
							((ImageView) MusicPlayerInstance
									.findViewById(R.id.Progress))
									.setBackground(MusicPlayerInstance
											.getResources().getDrawable(
													R.drawable.audamob_version_deux_progres2));

						} else {

							((ImageView) MusicPlayerInstance
									.findViewById(R.id.Progress))
									.setBackgroundDrawable(MusicPlayerInstance
											.getResources().getDrawable(
													R.drawable.audamob_version_deux_progres2));
						}

						compteur = 2;
					} else {
						if (Build.VERSION.SDK_INT >= 16) {
							((ImageView) MusicPlayerInstance
									.findViewById(R.id.Progress))
									.setBackground(MusicPlayerInstance
											.getResources().getDrawable(
													R.drawable.audamob_version_deux_progres3));

						} else {

							((ImageView) MusicPlayerInstance
									.findViewById(R.id.Progress))
									.setBackgroundDrawable(MusicPlayerInstance
											.getResources().getDrawable(
													R.drawable.audamob_version_deux_progres3));
						}

						compteur = 0;
					}

				}
				super.handleMessage(msg);
			}
		}
	};

	class UpdaterProgress_lyric_Thread extends Thread {
		Handler mHandler;
		int mState;
		boolean activate = true;

		public void setActivate(boolean state) {
			this.activate = state;
		}

		public UpdaterProgress_lyric_Thread(Handler h) {
			mHandler = h;
		}

		public void run() {

			while (progress_lyric && activate) {
				try {
					currentThread().sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = mHandler.obtainMessage();
				mHandler.sendMessage(msg);
			}
		}

		public void setState(int state) {
			mState = state;
		}
	}

	/*******
	 * 
	 * Progress Player Thread
	 * 
	 */

	static Boolean progress_player = true;
	int compteur_player = 0;
	private static Handler progress_player_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (p_player.activate) {
				if (MainContainerActivityTest.MediaPlayer_Playing
						&& progress_player) {

					progress_player = false;

					((ImageView) MusicPlayerInstance.findViewById(R.id.replay))
							.setVisibility(View.INVISIBLE);
					play_enbaled = true;
					pause_enabled = true;
					if (MusicPlayerInstance.mode == 2) {

						// ((ImageView)MusicPlayerInstance.findViewById(R.id.Next)).setBackgroundDrawable(MusicPlayerInstance.getResources().getDrawable(R.drawable.next_enable));
					}

					if (Build.VERSION.SDK_INT >= 16) {

						((ImageView) MusicPlayerInstance
								.findViewById(R.id.Play))
								.setBackground(MusicPlayerInstance
										.getResources().getDrawable(
												R.drawable.audamob_version_deux_play_enable));
					} else {
						((ImageView) MusicPlayerInstance
								.findViewById(R.id.Play))
								.setBackgroundDrawable(MusicPlayerInstance
										.getResources().getDrawable(
												R.drawable.audamob_version_deux_play_enable));

					}

					if (Build.VERSION.SDK_INT >= 16) {
						((ImageView) MusicPlayerInstance
								.findViewById(R.id.Pause))
								.setBackground(MusicPlayerInstance
										.getResources().getDrawable(
												R.drawable.audamob_version_deux_pause_enable));

					} else {
						((ImageView) MusicPlayerInstance
								.findViewById(R.id.Pause))
								.setBackgroundDrawable(MusicPlayerInstance
										.getResources().getDrawable(
												R.drawable.audamob_version_deux_pause_enable));

					}

					((ImageView) MusicPlayerInstance.findViewById(R.id.Play))
							.setVisibility(View.INVISIBLE);
					((ImageView) MusicPlayerInstance.findViewById(R.id.Pause))
							.setVisibility(View.VISIBLE);

				} else {

					if (MusicPlayerInstance.compteur_player == 0) {
						if (Build.VERSION.SDK_INT >= 16) {
							((ImageView) MusicPlayerInstance
									.findViewById(R.id.replay))
									.setBackground(MusicPlayerInstance
											.getResources()
											.getDrawable(
													R.drawable.audamob_version_deux_progress_player_1));
						} else {
							((ImageView) MusicPlayerInstance
									.findViewById(R.id.replay))
									.setBackgroundDrawable(MusicPlayerInstance
											.getResources()
											.getDrawable(
													R.drawable.audamob_version_deux_progress_player_1));
						}
						MusicPlayerInstance.compteur_player = 1;
					} else if (MusicPlayerInstance.compteur_player == 1) {
						if (Build.VERSION.SDK_INT >= 16) {

							((ImageView) MusicPlayerInstance
									.findViewById(R.id.replay))
									.setBackground(MusicPlayerInstance
											.getResources()
											.getDrawable(
													R.drawable.audamob_version_deux_progress_player_2));
						} else {
							((ImageView) MusicPlayerInstance
									.findViewById(R.id.replay))
									.setBackgroundDrawable(MusicPlayerInstance
											.getResources()
											.getDrawable(
													R.drawable.audamob_version_deux_progress_player_2));
						}
						MusicPlayerInstance.compteur_player = 2;
					} else {
						if (Build.VERSION.SDK_INT >= 16) {
							((ImageView) MusicPlayerInstance
									.findViewById(R.id.replay))
									.setBackground(MusicPlayerInstance
											.getResources()
											.getDrawable(
													R.drawable.audamob_version_deux_progress_player_3));
						} else {
							((ImageView) MusicPlayerInstance
									.findViewById(R.id.replay))
									.setBackgroundDrawable(MusicPlayerInstance
											.getResources()
											.getDrawable(
													R.drawable.audamob_version_deux_progress_player_3));
						}
						MusicPlayerInstance.compteur_player = 0;
					}

				}
				super.handleMessage(msg);
			}
		}
	};

	class UpdaterProgress_player_Thread extends Thread {

		Handler mHandler;
		int mState;
		int total;
		boolean activate = true;

		public void setActivate(boolean state) {
			activate = state;
		}

		public UpdaterProgress_player_Thread(Handler h) {
			mHandler = h;
		}

		public void run() {
			Log.d("Newt-Music", " start" + progress_player);
			while (progress_player && activate) {
				try {
					currentThread().sleep(500);
				} catch (InterruptedException e) {
				}
				Message msg = mHandler.obtainMessage();
				mHandler.sendMessage(msg);
			}
		}

		public void setState(int state) {
			mState = state;
		}
	}

	/*******
	 * 
	 * Progress seek Thread
	 * 
	 */
	Boolean progress_seek = false;
	int seek_compt = 0;

	/*
	 * private Handler seekHandler = new Handler() {
	 * 
	 * @Override public void handleMessage(Message msg) {
	 * 
	 * if (MainContainerActivityTest.MediaPlayer_finish_seeking) {
	 * MainContainerActivityTest.MediaPlayer_finish_seeking = false;
	 * progress_seek = false; ((ImageView) findViewById(R.id.replay))
	 * .setVisibility(View.INVISIBLE); } else { if (seek_compt == 0) { if
	 * (Build.VERSION.SDK_INT >= 16) { ((ImageView) findViewById(R.id.replay))
	 * .setBackground(MusicPlayerInstance .getResources().getDrawable(
	 * R.drawable.progress_player_1)); } else { ((ImageView)
	 * findViewById(R.id.replay)) .setBackgroundDrawable(MusicPlayerInstance
	 * .getResources().getDrawable( R.drawable.progress_player_1)); } seek_compt
	 * = 1; } else if (seek_compt == 1) { if (Build.VERSION.SDK_INT >= 16) {
	 * 
	 * ((ImageView) findViewById(R.id.replay))
	 * .setBackground(MusicPlayerInstance .getResources().getDrawable(
	 * R.drawable.progress_player_2)); } else { ((ImageView)
	 * findViewById(R.id.replay)) .setBackgroundDrawable(MusicPlayerInstance
	 * .getResources().getDrawable( R.drawable.progress_player_2)); } seek_compt
	 * = 2; } else { if (Build.VERSION.SDK_INT >= 16) { ((ImageView)
	 * findViewById(R.id.replay)) .setBackground(MusicPlayerInstance
	 * .getResources().getDrawable( R.drawable.progress_player_3)); } else {
	 * ((ImageView) findViewById(R.id.replay))
	 * .setBackgroundDrawable(MusicPlayerInstance .getResources().getDrawable(
	 * R.drawable.progress_player_3)); } seek_compt = 0; } }
	 * super.handleMessage(msg); } };
	 */
	public void updat_UI_Seek() {
		((ImageView) findViewById(R.id.replay)).setVisibility(View.VISIBLE);
	}

	class UpdaterProgress_seek_Thread extends Thread {
		Handler mHandler;
		int mState;

		public UpdaterProgress_seek_Thread(Handler h) {
			mHandler = h;
		}

		public void run() {

			while (progress_seek
					&& MainContainerActivityTest.MediaPlayer_Connect) {
				try {
					currentThread().sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = mHandler.obtainMessage();
				mHandler.sendMessage(msg);
			}
		}

		public void setState(int state) {
			mState = state;
		}
	}

}
