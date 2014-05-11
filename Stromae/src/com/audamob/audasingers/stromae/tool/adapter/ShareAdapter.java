package com.audamob.audasingers.stromae.tool.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.model.SharePm;

public class ShareAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<SharePm> listShareApplication;
	private Activity activity;
	private String textToSHare;
	public ShareAdapter(ArrayList<SharePm> listSHare, Activity activity,String textToSHare) {
		this.activity = activity;
		this.textToSHare=textToSHare;
		mInflater = (LayoutInflater) this.activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listShareApplication = listSHare;
	}

	public int getCount() {
		return listShareApplication.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.audamob_version_deux_item_share, null);

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
				startResolvedActivity(listShareApplication
						.get(holder.id).ResolveInfo);
				// PopUpShare.dismiss();
				// PopUpOnshare=false;
				// hidecurrentImage();
			}
		});

		holder.ImageFolder.setId(position);
		holder.NameFolder.setId(position);

		int h = holder.ImageFolder.getId();

		holder.ImageFolder.setImageDrawable(listShareApplication.get(h).d);

		holder.NameFolder.setText(listShareApplication.get(h).Name);
		holder.id = position;
		return convertView;
	}

	private void startResolvedActivity(ResolveInfo info) {

		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		String mimeType = null;
		mimeType = "text/plain";
		shareIntent.setType(mimeType);

		String nom = textToSHare;

		shareIntent.putExtra(Intent.EXTRA_TEXT, nom);
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		final Intent resolvedIntent = new Intent(shareIntent);
		ActivityInfo ai = info.activityInfo;

		resolvedIntent.setComponent(new ComponentName(
				ai.applicationInfo.packageName, ai.name));
		activity.startActivity(resolvedIntent);

	}

	class ViewHolder {

		ImageView ImageFolder;
		TextView NameFolder;
		int id;

	}

	
}