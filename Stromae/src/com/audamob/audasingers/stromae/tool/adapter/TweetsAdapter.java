package com.audamob.audasingers.stromae.tool.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.model.Tweet;
import com.audamob.audasingers.stromae.tool.view.ImageResizerUtils;
import com.audamob.audasingers.stromae.tool.view.RoundedAvatarDrawable;

public class TweetsAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Tweet> listTweets;
	private Activity activity;
	private RoundedAvatarDrawable rAvatar;
	public TweetsAdapter(Activity activity, ArrayList<Tweet> tweets) {
		Log.d("TWEETS_1", "I am here AppAdapter");
		this.activity = activity;
		listTweets = tweets;
		mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.rAvatar = new RoundedAvatarDrawable(
				BitmapFactory.decodeResource(activity.getResources(), R.drawable.logo_audamob_version_deux_singer_tweet));

		
	}

	public int getCount() {
		return listTweets.size();
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

			convertView = mInflater.inflate(R.layout.audamob_version_deux_item_tweet, null);
			ViewGroup mainContainer=(ViewGroup)convertView.findViewById(R.id.MainContainer);
    		Typeface font =Typeface.createFromAsset(activity.getAssets(), "ExoMedium.otf");

    		ImageResizerUtils.setFont(activity, mainContainer, font);
    		
			holder.Logo = (ImageView) convertView.findViewById(R.id.Icon);
			holder.Name_tweet = (TextView) convertView
					.findViewById(R.id.Name_tweet);
			holder.Number = (TextView) convertView.findViewById(R.id.Number);
			holder.nameArtist = (TextView) convertView
					.findViewById(R.id.Name_tweet_Owner);
			holder.State = (TextView) convertView
					.findViewById(R.id.tweet_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderImage) convertView.getTag();
		}

		holder.Logo.setId(position);
		holder.Name_tweet.setId(position);
		holder.Number.setId(position);
		holder.nameArtist.setId(position);
		holder.State.setId(position);
		convertView.findViewById(R.id.MainContainer).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String url = "https://twitter.com/LilTunechi/";
						final Intent intent = new Intent(Intent.ACTION_VIEW)
								.setData(Uri.parse(url));
						activity.startActivity(intent);
					}
				});

		holder.Name_tweet.setText(listTweets.get(position).getTweet());
		Log.d("TWEETS_1", "alooo      "
				+ listTweets.get(position).getDate().toString());
		String s = "";

		s = listTweets
				.get(position)
				.getDate()
				.toString()
				.substring(
						0,
						listTweets.get(position).getDate().toString().length() - 9);

		holder.Number.setText(s);

		if (listTweets.get(position).getState().length() > 0) {
			holder.State.setText(listTweets.get(position).getState());
		}
		holder.nameArtist.setText("@LilTunechi");
		holder.Logo.setImageDrawable(rAvatar);
		holder.id = position;

		return convertView;
	}
}

class ViewHolderImage {
	TextView nameArtist;
	ImageView Logo;
	TextView Name_tweet;
	TextView Number;
	TextView State;
	int id;
}