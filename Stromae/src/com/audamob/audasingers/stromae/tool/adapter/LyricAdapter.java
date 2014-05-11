package com.audamob.audasingers.stromae.tool.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.activity.LyricsPage;
import com.audamob.audasingers.stromae.model.Lyric;

public class LyricAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Lyric> lyricList;
	private Activity activity;
	private Typeface font;

	public LyricAdapter(Activity activity, ArrayList<Lyric> lyricList) {

		this.activity = activity;
		this.lyricList = lyricList;
		this.font = Typeface.createFromAsset(this.activity.getAssets(),
				"ExoMedium.otf");
		mInflater = (LayoutInflater) this.activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return lyricList.size();
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

			convertView = mInflater.inflate(R.layout.audamob_version_deux_item_lyric, null);

			holder.Logo = (ImageView) convertView.findViewById(R.id.Icon);
			holder.Name = (TextView) convertView.findViewById(R.id.nam);
			holder.Number = (TextView) convertView.findViewById(R.id.Number);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolderImage) convertView.getTag();
		}

		holder.Logo.setId(position);
		holder.Name.setId(position);
		holder.Number.setId(position);
		convertView.findViewById(R.id.ItemSong).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(activity.getBaseContext(),
								LyricsPage.class);
						i.putExtra("name", lyricList.get(position).getName());
						i.putExtra("album", lyricList.get(position).getAlbum());
						activity.startActivity(i);
						activity.overridePendingTransition(R.anim.push_down_in,
								R.anim.push_down_out);
					}
				});
		holder.Name.setTypeface(font);
		holder.Number.setTypeface(font);
		holder.Name.setText(lyricList.get(position).getName());
		holder.Number.setText(lyricList.get(position).getAlbum() + " : "
				+ lyricList.get(position).getDate());
		holder.id = position;

		return convertView;
	}

	class ViewHolderImage {
		ImageView Logo;
		TextView Name;
		TextView Number;
		int id;
	}

}
