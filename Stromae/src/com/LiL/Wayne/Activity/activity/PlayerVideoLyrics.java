package com.LiL.Wayne.Activity.activity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.LiL.Wayne.Activity.R;
import com.audamob.audasingers.model.Video;

public class PlayerVideoLyrics extends Activity {

	String Lyrics = "";
	String SongName = "";
	String YoutubeUrl = "";
	long Duration;
	WebView video;
	String iframe = "";
	ArrayList<Video> FavoriteList;

	Boolean PopUpOnshare = false;
	PopupWindow PopUpShare;
	Activity activity;

	class SharePm {
		String Name;
		Drawable d;
		ResolveInfo ResolveInfo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.player_video_lyrics);

		Bundle b = getIntent().getExtras();
		SongName = b.getString("name");
		YoutubeUrl = b.getString("Url");
		Duration = b.getLong("duration");
		activity = this;

		ImageView share = (ImageView) findViewById(R.id.Share);
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View vv) {
				// TODO Auto-generated method stub

				if (!PopUpOnshare) {
					PopUpOnshare = true;
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View v = vi.inflate(R.layout.dialog_options_share, null);
					PopUpShare = new PopupWindow(v, 350, 500);
					PopUpShare.setOutsideTouchable(false);
					PopUpShare.showAsDropDown(activity.findViewById(R.id.Share));
					ArrayList<SharePm> ListSHare = new ArrayList<SharePm>();
					ListView lv = (ListView) v.findViewById(R.id.ListShare);
					String mimeType = null;
					mimeType = "text/plain";
					Intent shareIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					shareIntent.setType(mimeType);

					shareIntent.putExtra(Intent.EXTRA_TEXT, "");
					shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

					final PackageManager pm = v.getContext()
							.getPackageManager();
					final List<ResolveInfo> activityList = pm
							.queryIntentActivities(shareIntent, 0);
					for (final ResolveInfo app : activityList) {
						SharePm sp = new SharePm();
						sp.d = app.loadIcon(pm);
						sp.Name = app.loadLabel(pm).toString();
						sp.ResolveInfo = app;
						ListSHare.add(sp);
						Log.d("ShareShare", " " + app.loadLabel(pm).toString());
					}
					count = ListSHare.size();
					String nom = "aymennnnn";// ImageList.get(currentIndex).getNewLocation();
					String Nomold = "zvzdv";// ImageList.get(currentIndex).getOldLocation();
					// Nomold=Nomold.substring(Nomold.lastIndexOf("."),
					// Nomold.length());

					lv.setAdapter(new ImageAdapter(ListSHare));

				} else {
					PopUpShare.dismiss();
					PopUpOnshare = false;

				}
			}
		});

		TextView namesong = (TextView) findViewById(R.id.NameSong);
		namesong.setText("Lil' Wayne : " + SongName);

		Typeface font1 = Typeface.createFromAsset(getAssets(),
				"AndroidClock.ttf");
		namesong.setTypeface(font1);
		FavoriteList = new ArrayList<Video>();
		try {
			FavoriteList = restoreFavoriteList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageView fav = (ImageView) findViewById(R.id.Favorites);
		Video ss = new Video(SongName, Duration, YoutubeUrl);
		Log.i("FavriteList", "size = " + FavoriteList.size());
		boolean existee = false;
		for (int i = 0; i < FavoriteList.size(); i++) {
			if (FavoriteList.get(i).getVideoName().contains(SongName)) {
				existee = true;
				break;
			}
		}
		if (existee) {
			if (Build.VERSION.SDK_INT >= 16) {

				fav.setBackground(getResources().getDrawable(
						R.drawable.favorite));
			} else {
				fav.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.favorite));
			}
		}
		fav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Video s = new Video(SongName, Duration, YoutubeUrl);
				boolean existe = false;
				for (int i = 0; i < FavoriteList.size(); i++) {
					if (FavoriteList.get(i).getVideoName().contains(SongName)) {
						existe = true;
						break;
					}
				}
				if (existe) {
					ArrayList<Video> temp = new ArrayList<Video>();
					for (int i = 0; i < FavoriteList.size(); i++) {
						if (FavoriteList.get(i).getVideoName().contains(SongName)) {

						} else {
							temp.add(FavoriteList.get(i));
						}

					}
					FavoriteList.clear();
					FavoriteList = temp;
					ImageView fav = (ImageView) findViewById(R.id.Favorites);
					if (Build.VERSION.SDK_INT >= 16) {

						fav.setBackground(getResources().getDrawable(
								R.drawable.nfavorite));
					} else {
						fav.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.nfavorite));
					}
					Log.i("FavriteList", "1");

				} else {
					FavoriteList.add(s);
					ImageView fav = (ImageView) findViewById(R.id.Favorites);
					if (Build.VERSION.SDK_INT >= 16) {

						fav.setBackground(getResources().getDrawable(
								R.drawable.favorite));
					} else {
						fav.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.favorite));

					}
					Log.i("FavriteList", "2");
				}
				try {
					sauvegardFavoriteList(FavoriteList);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.i("FavriteList", "problem" + e.getMessage());
				}

			}
		});

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		// Checks the orientation of the screen for landscape and portrait and
		// set portrait mode always
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
			((RelativeLayout) findViewById(R.id.TOP)).setVisibility(View.GONE);

		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			Toast.makeText(this, "prtrait", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// link.open();
		ThreadLoad threadLoad = new ThreadLoad(handlerLoad);
		threadLoad.start();

	}

	boolean displayed = false;
	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (keyCode == KeyEvent.KEYCODE_BACK) {
	 * 
	 * if(!displayed){ displayed=true; revmob.showFullscreen(this); return
	 * false; }else{
	 * 
	 * 
	 * return super.onKeyDown(keyCode, event); } }else{ return
	 * super.onKeyDown(keyCode, event); }
	 * 
	 * }
	 */
	int count = 0;

	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		ArrayList<SharePm> list;

		public ImageAdapter(ArrayList<SharePm> listSHare) {
			mInflater = (LayoutInflater) getBaseContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			list = listSHare;
		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.item_share, null);

				holder.ImageFolder = (ImageView) convertView
						.findViewById(R.id.IconAPp);
				holder.NameFolder = (TextView) convertView
						.findViewById(R.id.namAPp);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int h = v.getId();
					startResolvedActivity(list.get(holder.id).ResolveInfo);
					// PopUpShare.dismiss();
					// PopUpOnshare=false;
					// hidecurrentImage();
				}
			});

			holder.ImageFolder.setId(position);
			holder.NameFolder.setId(position);

			int h = holder.ImageFolder.getId();

			holder.ImageFolder.setImageDrawable(list.get(h).d);

			holder.NameFolder.setText(list.get(h).Name);
			holder.id = position;
			return convertView;
		}
	}

	class ViewHolder {

		ImageView ImageFolder;
		TextView NameFolder;
		int id;

	}

	private void startResolvedActivity(ResolveInfo info) {

		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		String mimeType = null;
		mimeType = "text/plain";
		shareIntent.setType(mimeType);
		ArrayList<Uri> uris = new ArrayList<Uri>();
		String nom = TextLyrics.getText().toString() + "\n" + YoutubeUrl;// ImageList.get(currentIndex).getNewLocation();

		shareIntent.putExtra(Intent.EXTRA_TEXT, nom);
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		final Intent resolvedIntent = new Intent(shareIntent);
		ActivityInfo ai = info.activityInfo;

		resolvedIntent.setComponent(new ComponentName(
				ai.applicationInfo.packageName, ai.name));
		startActivity(resolvedIntent);

	}

	TextView TextLyrics;
	boolean LoadFirstTime = true;
	final Handler handlerLoad = new Handler() {

		public void handleMessage(Message msg) {
			try {
				TextLyrics = (TextView) findViewById(R.id.TextLyrics);

				try {

					try {
						String path = YoutubeUrl;// total.substring(total.indexOf("00000")+5,
													// total.length());

						DisplayMetrics displaymetrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(
								displaymetrics);
						int height = displaymetrics.heightPixels;
						int width = displaymetrics.widthPixels;

						float newHeight = ((RelativeLayout) findViewById(R.id.webLayout))
								.getHeight();

						float newWhidth = ((RelativeLayout) findViewById(R.id.webLayout))
								.getWidth() - 20;
						float rnewHeight = newWhidth / 400;
						newWhidth = rnewHeight * 300;
						newHeight = ((RelativeLayout) findViewById(R.id.webLayout))
								.getHeight() - 120;

						Log.d("Lyrics", " path = " + newHeight);
						// WebView
						// VideoYoutube=(WebView)findViewById(R.id.VideoYoutube);

						video = (WebView) findViewById(R.id.VideoYoutube);
						video.setBackgroundColor(Color.parseColor("#8F000000"));
						video.getSettings().setDefaultFontSize(10);
						iframe = "<iframe frameborder='0' width='" + newWhidth
								+ "' height='" + newHeight + "' " + "src='"
								+ path + "?logo=0&autoPlay=1' >" + "</iframe>";
						video.getSettings()
								.setUserAgentString(
										"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.36 (KHTML, like Gecko) Chrome/13.0.766.0 Safari/534.36");
						video.getSettings().setJavaScriptEnabled(true);
						video.getSettings().setPluginsEnabled(true);
						video.setFocusableInTouchMode(false);
						video.setFocusable(false);
						video.loadData(iframe, "text/html", "utf-8");
						video.getSettings().setBuiltInZoomControls(true);
						video.getSettings().setSupportZoom(true);

					} catch (Exception e) {
						// TODO: handle exception
						Log.d("Lyrics", "Exception= " + e.getMessage());
					}

					// album.recycle();
				} catch (Exception e) {
					Log.e("Error", "Error");
					e.printStackTrace();
				}
				String UrlBack = "";
				try {

					String SongNamee = SongName.replace(" ", "_");

					String UrlLyrics = "http://lyrics.wikia.com/Lil_Wayne:"
							+ SongNamee;
					org.jsoup.nodes.Document doc = Jsoup.connect(UrlLyrics)
							.get();
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
								linkText = linkText.substring(0, linkText
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
				TextLyrics.setText(UrlBack);
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
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("url", "");
			msg.setData(b);
			mHandler.sendMessage(msg);

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {

			video.removeAllViews();
			video.destroy();

		} catch (Exception e) {
			// TODO: handle exception
		}

		super.onDestroy();
	}

	public ArrayList<Video> restoreFavoriteList() throws IOException,
			ClassNotFoundException {
		FileInputStream fin = new FileInputStream(getCacheDir()
				.getAbsolutePath() + "/favoritesong");
		ObjectInputStream in = new ObjectInputStream(fin);
		ArrayList<Video> object = (ArrayList<Video>) in.readObject();
		in.close();
		return object;
	}

	public void sauvegardFavoriteList(ArrayList<Video> s) throws IOException {
		FileOutputStream fout = new FileOutputStream(getCacheDir()
				.getAbsolutePath() + "/favoritesong");
		ObjectOutputStream out = new ObjectOutputStream(fout);

		out.writeObject(s);
		out.close();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (PopUpOnshare) {
			PopUpShare.dismiss();
			PopUpOnshare = false;
		} else {
			super.onBackPressed();
		}
	}

}
