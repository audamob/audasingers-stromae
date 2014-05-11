package com.LiL.Wayne.Activity.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.LiL.Wayne.Activity.R;
import com.LiL.Wayne.Activity.SlidingMenu.ActivityBase;
import com.audamob.audasingers.model.News;
import com.audamob.audasingers.tool.view.ImageResizerUtils;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class NewsPage extends ActivityBase {

	String NewsURl;
	String NewsTITLE;
	String NewsTEXT;
	String NewsIMAGEurl;
	String NewsDATE;
	String NewsWRITER;
	News NEWS;

	Boolean PopUpOnshare = false;
	PopupWindow PopUpShare;
	Activity activity;

	class SharePm {
		String Name;
		Drawable d;
		ResolveInfo ResolveInfo;
	}

	int count = 0;

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
		setContentView(R.layout.auda_layout_activity_news_page);

		ActionBar actionBar = getActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(false);

		ViewGroup mainContainer = (ViewGroup) findViewById(R.id.MainContainer);
		Typeface font = Typeface.createFromAsset(getAssets(), "ExoMedium.otf");

		ImageResizerUtils.setFont(this, mainContainer, font);
		activity = this;

		Bundle b = getIntent().getExtras();
		this.NewsURl = b.getString("URL");
		this.NewsTITLE = b.getString("TITLE");
		this.NewsTEXT = b.getString("TEXT");
		this.NewsIMAGEurl = b.getString("urlIMAGE");
		this.NewsDATE = b.getString("DATE");
		this.NewsWRITER = b.getString("WRITER");
		NEWS = new News(NewsTITLE, NewsDATE, NewsTEXT, NewsURl, NewsWRITER,
				NewsIMAGEurl);
		ImageView imageArticle, logoWriter;
		TextView titleView, textArticleView, writterView, dateView;
		imageArticle = (ImageView) findViewById(R.id.Image_article);
		logoWriter = (ImageView) findViewById(R.id.writer_logo);
		titleView = (TextView) findViewById(R.id.title);
		textArticleView = (TextView) findViewById(R.id.textArticle);
		writterView = (TextView) findViewById(R.id.writter);
		dateView = (TextView) findViewById(R.id.date);

		dateView.setText(NEWS.getDate());
		writterView.setText(NEWS.getWriter());
		textArticleView.setText(NEWS.getDesccreption());
		titleView.setText(NEWS.getTitle());
		if (NEWS.getWriter().equalsIgnoreCase("MTV News")) {

			if (Build.VERSION.SDK_INT >= 16) {
				logoWriter.setBackground(getResources().getDrawable(
						R.drawable.mtvnews_c));
			} else {
				logoWriter.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.mtvnews_c));
			}
		} else if (NEWS.getWriter().equalsIgnoreCase("MTV Buzzworthy")) {
			if (Build.VERSION.SDK_INT >= 16) {
				logoWriter.setBackground(getResources().getDrawable(
						R.drawable.mtvbuzzworthy_c));
			} else {
				logoWriter.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.mtvbuzzworthy_c));
			}
		} else if (NEWS.getWriter().equalsIgnoreCase("VH1 Tuner")) {
			if (Build.VERSION.SDK_INT >= 16) {
				logoWriter.setBackground(getResources().getDrawable(
						R.drawable.vh1tuner_c));
			} else {
				logoWriter.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.vh1tuner_c));
			}
		}

		UrlImageViewHelper.setUrlDrawable(imageArticle, NEWS.getImageUrl());

		/*
		 * ImageView share = (ImageView) findViewById(R.id.Icon_share);
		 * share.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View vv) { // TODO Auto-generated
		 * method stub
		 * 
		 * if (!PopUpOnshare) { PopUpOnshare = true; LayoutInflater vi =
		 * (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 * View v = vi.inflate(R.layout.dialog_options_share, null); PopUpShare
		 * = new PopupWindow(v, 500, 500);
		 * PopUpShare.setOutsideTouchable(false);
		 * PopUpShare.showAsDropDown(activity .findViewById(R.id.Icon_share));
		 * ArrayList<SharePm> ListSHare = new ArrayList<SharePm>(); ListView lv
		 * = (ListView) v.findViewById(R.id.ListShare); String mimeType = null;
		 * mimeType = "text/plain"; Intent shareIntent = new Intent(
		 * android.content.Intent.ACTION_SEND); shareIntent.setType(mimeType);
		 * 
		 * shareIntent.putExtra(Intent.EXTRA_TEXT, "");
		 * shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		 * 
		 * final PackageManager pm = v.getContext() .getPackageManager(); final
		 * List<ResolveInfo> activityList = pm
		 * .queryIntentActivities(shareIntent, 0); for (final ResolveInfo app :
		 * activityList) { SharePm sp = new SharePm(); sp.d = app.loadIcon(pm);
		 * sp.Name = app.loadLabel(pm).toString(); sp.ResolveInfo = app;
		 * ListSHare.add(sp); Log.d("ShareShare", " " +
		 * app.loadLabel(pm).toString()); } count = ListSHare.size(); String nom
		 * = "aymennnnn";// ImageList.get(currentIndex).getNewLocation(); String
		 * Nomold = "zvzdv";// ImageList.get(currentIndex).getOldLocation(); //
		 * Nomold=Nomold.substring(Nomold.lastIndexOf("."), // Nomold.length());
		 * 
		 * lv.setAdapter(new ImageAdapter(ListSHare));
		 * 
		 * } else { PopUpShare.dismiss(); PopUpOnshare = false;
		 * 
		 * } } });
		 */}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
	}

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
		String nom = NEWS.getUrl();// ImageList.get(currentIndex).getNewLocation();

		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "@Lil-Wayne-Android");
		shareIntent.putExtra(Intent.EXTRA_TEXT, "@Lil-Wayne-Android  " + nom);
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		final Intent resolvedIntent = new Intent(shareIntent);
		ActivityInfo ai = info.activityInfo;

		resolvedIntent.setComponent(new ComponentName(
				ai.applicationInfo.packageName, ai.name));
		startActivity(resolvedIntent);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (PopUpOnshare) {
			PopUpOnshare = false;
			PopUpShare.dismiss();
		} else {

			finish();
			overridePendingTransition(R.anim.push_down_out_back,
					R.anim.push_down_in_back);
		}
	}
}
