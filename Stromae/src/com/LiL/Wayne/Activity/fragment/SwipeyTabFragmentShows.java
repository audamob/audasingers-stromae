package com.LiL.Wayne.Activity.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.LiL.Wayne.Activity.R;
import com.audamob.audasingers.model.News;

public class SwipeyTabFragmentShows extends Fragment {
	private SimpleDateFormat FormatterDuration = new SimpleDateFormat("mm:ss");
	ArrayList<News> LyricList;
	EditText SearchEdit, SearchEditAlbum;

	public static Fragment newInstance(String title) {
		SwipeyTabFragmentShows f = new SwipeyTabFragmentShows();
		Bundle args = new Bundle();
		args.putString("title", title);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
	}

	ListView ListVideo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.shows, null);
		final String title = getArguments().getString("title");

		ThreadLoad threadLoad = new ThreadLoad(handlerLoad);
		threadLoad.start();
		UpdaterProgressThread p = new UpdaterProgressThread(progressHandler);
		p.start();

		return root;

	}

	int compteur = 0;
	private Handler progressHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (Build.VERSION.SDK_INT >= 16) {
				if (compteur == 0) {
					((ImageView) getActivity().findViewById(R.id.Progress))
							.setBackground(getResources().getDrawable(
									R.drawable.progres1));
					compteur = 1;
				} else if (compteur == 1) {
					((ImageView) getActivity().findViewById(R.id.Progress))
							.setBackground(getResources().getDrawable(
									R.drawable.progres2));
					compteur = 2;
				} else {
					((ImageView) getActivity().findViewById(R.id.Progress))
							.setBackground(getResources().getDrawable(
									R.drawable.progres3));
					compteur = 0;
				}
			} else {
				if (compteur == 0) {
					((ImageView) getActivity().findViewById(R.id.Progress))
							.setBackgroundDrawable(getResources().getDrawable(
									R.drawable.progres1));
					compteur = 1;
				} else if (compteur == 1) {
					((ImageView) getActivity().findViewById(R.id.Progress))
							.setBackgroundDrawable(getResources().getDrawable(
									R.drawable.progres2));
					compteur = 2;
				} else {
					((ImageView) getActivity().findViewById(R.id.Progress))
							.setBackgroundDrawable(getResources().getDrawable(
									R.drawable.progres3));
					compteur = 0;
				}
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

	final Handler handlerLoad = new Handler() {

		public void handleMessage(Message msg) {
			try {
				WebView article = (WebView) getActivity().findViewById(
						R.id.TextLyrics);

				article.getSettings()
						.setUserAgentString(
								"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.36 (KHTML, like Gecko) Chrome/13.0.766.0 Safari/534.36");
				article.getSettings().setJavaScriptEnabled(true);
				article.getSettings().setPluginsEnabled(true);
				Log.d("display", "" + msg.getData().toString());
				article.loadUrl("http://lilwayne-online.com/tour.aspx");// Data(msg.getData().toString(),"text/css",
																		// "utf-8");

			} catch (Exception e) {
				// TODO: handle exception
			}
			Done = true;
			((ImageView) getActivity().findViewById(R.id.Progress))
					.setVisibility(View.GONE);
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

			String HtmlCode = "";
			try {

				Log.d("Feod", "shows bdina");
				String UrlLyrics = "http://lilwayne-online.com/tour.aspx";
				org.jsoup.nodes.Document doc = Jsoup.connect(UrlLyrics).get();
				Elements content = doc.getElementsByTag("div");
				// Elements links = content.getElementsByTag("a");
				for (Element link : content) {

					String linkHref = link.className();
					if (linkHref.equals("container")) {
						Log.d("Feod", "" + link.html());
						HtmlCode = link.html();

					}
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			HtmlCode = HtmlCode.replace("&nbsp;", "");
			b.putString("url", HtmlCode);
			msg.setData(b);
			mHandler.sendMessage(msg);

		}
	}

}