package com.audamob.audasingers.stromae.tool.adapter;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.activity.NewsPage;
import com.audamob.audasingers.stromae.fragment.MainContainerActivityTest;
import com.audamob.audasingers.stromae.model.News;
import com.audamob.audasingers.stromae.tool.view.ImageResizerUtils;
import com.audamob.audasingers.stromae.view.services.ShareService;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class NewsAdapter extends BaseAdapter {
	class ViewHolderImage {
		ImageView image;
		TextView content;
		TextView writer;
		TextView date;
		RelativeLayout itemNews;
		ImageView writerIco;
		RelativeLayout cont;
		ImageView share;
		int id;
	}
	private ArrayList<News> newsList;
	private LayoutInflater mInflater;
	private Activity activity;
	public NewsAdapter(Activity activity, ArrayList<News> listNews) {
		mInflater = (LayoutInflater) activity.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		this.newsList=listNews;
		this.activity=activity;
	}

	public int getCount() {
		return newsList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolderImage holder;

		if (convertView == null) {
			holder = new ViewHolderImage();
			convertView = mInflater.inflate(R.layout.audamob_version_deux_item_news, null);
			ViewGroup mainContainer=(ViewGroup)convertView.findViewById(R.id.MainContainer);
    		Typeface font =Typeface.createFromAsset(activity.getAssets(), "ExoMedium.otf");

    		ImageResizerUtils.setFont(activity, mainContainer, font);
			
			
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.content = (TextView) convertView.findViewById(R.id.Content);
			holder.writer = (TextView) convertView.findViewById(R.id.writer);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.itemNews = (RelativeLayout) convertView
					.findViewById(R.id.itemNews);
			holder.writerIco = (ImageView) convertView
					.findViewById(R.id.imagewriter);
			holder.cont = (RelativeLayout) convertView
					.findViewById(R.id.Layouticon);
			holder.share=(ImageView)convertView.findViewById(R.id.partager);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolderImage) convertView.getTag();
		}

		holder.image.setId(position);
		holder.content.setId(position);
		holder.writer.setId(position);
		holder.writerIco.setId(position);
		holder.cont.setId(position);
		holder.date.setId(position);
		holder.itemNews.setId(position);

		if (Build.VERSION.SDK_INT >= 16) {
			if (newsList.get(position).getWriter()
					.equalsIgnoreCase("MTV News")) {
				holder.writerIco.setBackground(convertView.getResources()
						.getDrawable(R.drawable.audamob_version_deux_mtvnews_c));
			} else if (newsList.get(position).getWriter()
					.equalsIgnoreCase("MTV Buzzworthy")) {
				holder.writerIco.setBackground(convertView.getResources()
						.getDrawable(R.drawable.audamob_version_deux_mtvbuzzworthy_c));
			} else if (newsList.get(position).getWriter()
					.equalsIgnoreCase("VH1 Tuner")) {
				holder.writerIco.setBackground(convertView.getResources()
						.getDrawable(R.drawable.audamob_version_deux_vh1tuner_c));
			}
		} else {
			if (newsList.get(position).getWriter()
					.equalsIgnoreCase("MTV News")) {
				if (Build.VERSION.SDK_INT >= 16) {
					holder.writerIco.setBackground(convertView.getResources()
							.getDrawable(R.drawable.audamob_version_deux_mtvnews_c));
				} else {
					holder.writerIco.setBackgroundDrawable(convertView
							.getResources().getDrawable(R.drawable.audamob_version_deux_mtvnews_c));
				}
			} else if (newsList.get(position).getWriter()
					.equalsIgnoreCase("MTV Buzzworthy")) {
				if (Build.VERSION.SDK_INT >= 16) {
					holder.writerIco.setBackground(convertView.getResources()
							.getDrawable(R.drawable.audamob_version_deux_mtvbuzzworthy_c));
				} else {
					holder.writerIco.setBackgroundDrawable(convertView
							.getResources().getDrawable(
									R.drawable.audamob_version_deux_mtvbuzzworthy_c));
				}
			} else if (newsList.get(position).getWriter()
					.equalsIgnoreCase("VH1 Tuner")) {
				if (Build.VERSION.SDK_INT >= 16) {
					holder.writerIco.setBackground(convertView.getResources()
							.getDrawable(R.drawable.audamob_version_deux_vh1tuner_c));
				} else {
					holder.writerIco.setBackgroundDrawable(convertView
							.getResources().getDrawable(R.drawable.audamob_version_deux_vh1tuner_c));
				}
			}
		}

		
		UrlImageViewHelper.setUrlDrawable(holder.image,newsList.get(position).getImageUrl());

		holder.itemNews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(activity.getBaseContext(),
						NewsPage.class);
				i.putExtra("URL", newsList.get(position).getUrl());
				i.putExtra("TITLE", newsList.get(position).getTitle());
				i.putExtra("TEXT", newsList.get(position).getDesccreption());
				i.putExtra("DATE", newsList.get(position).getDate());
				i.putExtra("WRITER", newsList.get(position).getWriter());
				i.putExtra("urlIMAGE", newsList.get(position).getImageUrl());
				activity.startActivity(i);

				activity.overridePendingTransition(R.anim.push_down_in,
						R.anim.push_down_out);

			}
		});
		holder.share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					MainContainerActivityTest.shareService.setDismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				MainContainerActivityTest.shareService=new ShareService(activity,newsList.get(position).getUrl(), holder.share);
			}
		});
		holder.content.setText(newsList.get(position).getTitle());
		holder.writer.setText(newsList.get(position).getWriter());
		holder.date.setText(newsList.get(position).getDate());
		holder.id = position;

		return convertView;
	}
}
