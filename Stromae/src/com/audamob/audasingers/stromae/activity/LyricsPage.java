package com.audamob.audasingers.stromae.activity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.SlidingMenu.ActivityBase;
import com.audamob.audasingers.stromae.constant.ApplicationConstants;
import com.audamob.audasingers.stromae.tool.view.ImageResizerUtils;

public class LyricsPage extends ActivityBase {

	String Lyrics = "";
	String SongName = "";
	String Album = "";
	long Duration;

	String iframe = "";

	Boolean PopUpOnshare = false;
	PopupWindow PopUpShare;
	Activity activity;

	
	
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
		setContentView(R.layout.audamob_version_deux_activity_lyrics);
		ActionBar actionBar = getActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(false);
		
		Bundle b = getIntent().getExtras();
		SongName = b.getString("name");
		Album = b.getString("album");
		activity = this;

		// revmob.showFullscreen(this);
		TextView namesong = (TextView) findViewById(R.id.NameSong);
		namesong.setText("Lyrics : " + SongName);
		
		ViewGroup mainContainer = (ViewGroup) findViewById(R.id.MainContainer);
		Typeface font = Typeface.createFromAsset(getAssets(), "ExoMedium.otf");
		ImageResizerUtils.setFont(this, mainContainer, font);
/*		ImageView share = (ImageView) findViewById(R.id.Share);
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View vv) {
				// TODO Auto-generated method stub

				if (!PopUpOnshare) {
				

				} else {
					PopUpShare.dismiss();
					PopUpOnshare = false;

				}
			}
		});
*/
		ThreadLoad threadLoad = new ThreadLoad(handlerLoad);
		threadLoad.start();
		UpdaterProgressThread p = new UpdaterProgressThread(progressHandler);
		p.start();

	}

	int compteur = 0;
	private Handler progressHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (compteur == 0) {
				if (Build.VERSION.SDK_INT >= 16) {
					((ImageView) findViewById(R.id.Progress))
							.setBackground(getResources().getDrawable(
									R.drawable.audamob_version_deux_progres1));

				} else {

					((ImageView) findViewById(R.id.Progress))
							.setBackgroundDrawable(getResources().getDrawable(
									R.drawable.audamob_version_deux_progres1));
				}

				compteur = 1;
			} else if (compteur == 1) {
				if (Build.VERSION.SDK_INT >= 16) {
					((ImageView) findViewById(R.id.Progress))
							.setBackground(getResources().getDrawable(
									R.drawable.audamob_version_deux_progres2));

				} else {

					((ImageView) findViewById(R.id.Progress))
							.setBackgroundDrawable(getResources().getDrawable(
									R.drawable.audamob_version_deux_progres2));
				}

				compteur = 2;
			} else {
				if (Build.VERSION.SDK_INT >= 16) {
					((ImageView) findViewById(R.id.Progress))
							.setBackground(getResources().getDrawable(
									R.drawable.audamob_version_deux_progres3));

				} else {

					((ImageView) findViewById(R.id.Progress))
							.setBackgroundDrawable(getResources().getDrawable(
									R.drawable.audamob_version_deux_progres3));
				}

				compteur = 0;
			}

			super.handleMessage(msg);
		}
	};
	Boolean Done = false;

	class UpdaterProgressThread extends Thread {
		final static int DONE = 0;
		final static int RUNNING = 1;
		final static int ECOUTE = 2;
		Handler mHandler;
		int mState;
		int total;
		int delay = 1000;
		int tranlationstate;

		public UpdaterProgressThread(Handler h) {
			// TODO Auto-generated constructor stub

			mHandler = h;
		}

		public void run() {
			mState = RUNNING;
			while (!Done) {
				try {
					currentThread().sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mHandler.sendEmptyMessage(5);
			}

		}

		public void setState(int state) {
			mState = state;
		}
	}

	int count = 0;



	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (PopUpOnshare) {
			PopUpShare.dismiss();
			PopUpOnshare = false;
		} else {
			finish();
			overridePendingTransition(R.anim.push_down_out_back,
					R.anim.push_down_in_back);
		}
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
			Done = true;
			((ImageView) findViewById(R.id.Progress)).setVisibility(View.GONE);
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
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// link.open();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
	}

	boolean displayed = false;

}
