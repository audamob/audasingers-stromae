package com.audamob.audasingers.stromae.view.services.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.activity.Home;
import com.audamob.audasingers.stromae.constant.ApplicationConstants;
import com.audamob.audasingers.stromae.model.News;
import com.audamob.audasingers.stromae.tool.db.CacheReadWriter;
import com.audamob.audasingers.stromae.view.services.IServiceDataUpdater;

public class ServiceNewsUpdater extends Service implements
		IServiceDataUpdater<News> {
	Service instance;
	static String TextUpdate = "";
	static String TextUpdateWidget = "";
	ArrayList<News> List_News = new ArrayList<News>();

	@Override
	public void onCreate() {
		instance = this;

		ThreadLoad th = new ThreadLoad(handlerLoad);
		th.start();

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub

		return super.onUnbind(intent);
	}

	final Handler handlerLoad = new Handler() {

		public void handleMessage(Message msg) {
			try {

			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};
	int ratio = 2;
	boolean updateState = false;
	long LastTimeUpdate = -1;
	long thisTimeUpdate = -1;
	int lastLevelUpdate = -1;

	private class ThreadLoad extends Thread {
		Handler mHandler;
		boolean done = false;

		ThreadLoad(Handler h) {
			mHandler = h;
		}

		public void run() {
			Log.i("UpdateListNews", "je commence: ");
			Elements content = null;
			try {
				currentThread().sleep(5 * 1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List_News = new ArrayList<News>();
			try {
				List_News = restore_News();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (List_News.size() == 20) {
				// Ajouté premier élément de référence;
				try {

					String UrlLyrics = "http://www.mtv.com/artists/lil-wayne/mtv-news/";
					org.jsoup.nodes.Document doc = Jsoup.connect(UrlLyrics)
							.get();
					content = doc.getElementsByClass("metadata");

					Log.i("News", "nombre : " + content.size());
					String title, date, description, imageUrl, url, writer;

					for (int compteur = 0; (compteur < content.size() && List_News
							.size() < 21); compteur++) {
						Element link = content.get(compteur);
						Elements element = link.getElementsByClass("title");
						title = element.text();
						Elements e = ((Jsoup.parse(element.toString()))
								.getElementsByTag("a"));

						url = e.attr("href");

						description = "";
						imageUrl = "";
						element = link.getElementsByClass("dateFormatted");

						writer = element.text();

						date = element.outerHtml().substring(
								element.outerHtml().indexOf("<br />") + 6,
								element.outerHtml().indexOf("</div>"));

						writer.replace(date, "");
						if (writer.contains("MTV News")) {
							writer = "MTV News";
							fetch_MTV_News(title, date, description, url,
									writer, imageUrl);

						}
						if (writer.contains("MTV Buzzworthy")) {
							writer = "MTV Buzzworthy";
							fetch_MTV_Buzzworthy(title, date, description, url,
									writer, imageUrl);
						}
						if (writer.contains("VH1 Tuner")) {
							writer = "VH1 Tuner";
							fetch_VH1_Tuner(title, date, description, url,
									writer, imageUrl);
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			try {
				sauvegard_News(List_News);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Boolean wasil = true;
			while (true) {
				wasil = true;
				try {
					currentThread().sleep(4 * 60 * 60 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List_News.clear();
				List_News = new ArrayList<News>();
				try {
					List_News = restore_News();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {

					String UrlLyrics = "http://www.mtv.com/artists/lil-wayne/mtv-news/";
					org.jsoup.nodes.Document doc = Jsoup.connect(UrlLyrics)
							.get();
					content = doc.getElementsByClass("metadata");

					String title, date, description, imageUrl, url, writer;
					for (int compteur = 0; (compteur < content.size() && wasil); compteur++) {
						Element link = content.get(compteur);
						Elements element = link.getElementsByClass("title");
						title = element.text();
						Elements e = ((Jsoup.parse(element.toString()))
								.getElementsByTag("a"));

						url = e.attr("href");

						description = "";
						imageUrl = "";
						element = link.getElementsByClass("dateFormatted");

						writer = element.text();

						date = element.outerHtml().substring(
								element.outerHtml().indexOf("<br />") + 6,
								element.outerHtml().indexOf("</div>"));

						writer.replace(date, "");
						for (int ttt = 0; ttt < List_News.size(); ttt++) {
							if (List_News.get(ttt).getUrl()
									.equalsIgnoreCase(url)) {
								wasil = false;
							}

						}

						Log.i("UpdateListNews", "je verifie : " + wasil);
						if (wasil) {
							if (writer.contains("MTV News")) {
								writer = "MTV News";
								fetch_MTV_News(title, date, description, url,
										writer, imageUrl);
							}
							if (writer.contains("MTV Buzzworthy")) {
								writer = "MTV Buzzworthy";
								fetch_MTV_Buzzworthy(title, date, description,
										url, writer, imageUrl);
							}
							if (writer.contains("VH1 Tuner")) {
								writer = "VH1 Tuner";
								fetch_VH1_Tuner(title, date, description, url,
										writer, imageUrl);
							}

							List_News.remove(List_News.size() - 1);
							sauvegard_News(List_News);

						}
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}

		}

		public void setDone(boolean b) {
			done = b;
		}

		private void fetch_MTV_News(String title, String date,
				String description, String url, String writer, String imageUrl) {
			// TODO Auto-generated method stub
			Elements content = null;
			try {
				org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
				content = doc.getElementsByClass("article-body");
				description = content.text();
				content = doc.getElementsByClass("thumb-lg");
				imageUrl = content.attr("src");

				News temp = new News(title, date, description, url, writer,
						imageUrl);
				News_Notification(writer, date, title, 0);
				List_News.add(0, temp);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		private void fetch_MTV_Buzzworthy(String title, String date,
				String description, String url, String writer, String imageUrl) {
			// TODO Auto-generated method stub
			Elements content = null;
			try {
				org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
				content = doc.getElementsByClass("entry");
				description = content.text();
				content = Jsoup.parse(content.toString()).getElementsByTag(
						"img");
				imageUrl = content.attr("src");

				News temp = new News(title, date, description, url, writer,
						imageUrl);
				List_News.add(0, temp);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		private void fetch_VH1_Tuner(String title, String date,
				String description, String url, String writer, String imageUrl) {
			// TODO Auto-generated method stub
			Elements content = null;
			try {
				org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
				content = doc.getElementsByClass("post_content");
				description = content.text();
				content = Jsoup.parse(content.toString()).getElementsByTag(
						"img");
				imageUrl = content.attr("src");

				News temp = new News(title, date, description, url, writer,
						imageUrl);
				List_News.add(0, temp);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	public ArrayList<News> restore_News() throws IOException,
			ClassNotFoundException {
		FileInputStream fin = new FileInputStream(getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_NEWS);
		ObjectInputStream in = new ObjectInputStream(fin);
		ArrayList<News> object = (ArrayList<News>) in.readObject();
		in.close();
		return object;
	}

	public void sauvegard_News(ArrayList<News> s) throws IOException {
		FileOutputStream fout = new FileOutputStream(getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_NEWS);
		ObjectOutputStream out = new ObjectOutputStream(fout);

		out.writeObject(s);
		out.close();
	}

	NotificationManager notificationManager;

	public void News_Notification(String writer, String date, String content,
			int id) {

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(1);
		int icon = R.drawable.logo_audamob_version_deux_ic_launcher;
		CharSequence text = content;
		CharSequence contentTitle = ApplicationConstants.SINGER_NAME;
		CharSequence contentText = content;
		long when = System.currentTimeMillis();

		Intent intent = new Intent(this, Home.class);
		PendingIntent contentIntent = PendingIntent.getService(this, 0, intent,
				0);

		Notification notification = new Notification(icon, text, when);
		notification.setLatestEventInfo(this, contentTitle, contentText,
				contentIntent);
		notification.flags = Notification.DEFAULT_ALL;
		int Notification_Id = 1;
		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.audamob_version_deux_notification_layout);
		if (id == 0) {
			contentView.setImageViewResource(R.id.imagewriter,
					R.drawable.audamob_version_deux_mtvnews_c);
		} else if (id == 1) {
			contentView.setImageViewResource(R.id.imagewriter,
					R.drawable.audamob_version_deux_mtvbuzzworthy_c);
		} else {
			contentView.setImageViewResource(R.id.imagewriter,
					R.drawable.audamob_version_deux_vh1tuner_c);
		}

		contentView.setTextViewText(R.id.Content, contentText);
		contentView.setTextViewText(R.id.writer, writer);
		// contentView.setTextViewText(R.id.date, date);
		notification.contentView = contentView;
		notificationManager.notify(Notification_Id, notification);
	}

	@Override
	public ArrayList<News> getExistingDataFromCache() throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNewDataInCache(ArrayList<News> s) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<News> getDataFromServer(String serverUrl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNewDataCountInCache(int count) throws IOException {
		// TODO Auto-generated method stub

	}
}
