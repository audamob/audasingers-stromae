package com.audamob.audasingers.stromae.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.select.Elements;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.constant.ApplicationConstants;
import com.audamob.audasingers.stromae.model.Tweet;
import com.audamob.audasingers.stromae.tool.adapter.TweetsAdapter;
import com.audamob.audasingers.stromae.tool.db.CacheReadWriter;

public class SwipeyTabFragmentTweets extends Fragment {
	ArrayList<Tweet> ListTweets;
	EditText SearchEdit, SearchEditAlbum;

	public static Fragment newInstance(String title) {
		SwipeyTabFragmentTweets f = new SwipeyTabFragmentTweets();
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

		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.audamob_version_deux_activity_tweets, null);
		final String title = getArguments().getString("title");

		ListVideo = (ListView) root.findViewById(R.id.ListTweets);
		ListTweets = new ArrayList<Tweet>();
		try {
			ListTweets = CacheReadWriter.restoreTweets(getActivity());

		} catch (Exception e) {
			// TODO: handle exception
		}	
		Collections.reverse(ListTweets);
		ListVideo.setAdapter(new TweetsAdapter(getActivity(), ListTweets));
		Log.d("TWEETS_1", "I am here onResume");

		ThreadUpdateList th = new ThreadUpdateList(handlerLoad);
		th.start();
		return root;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		ArrayList<Tweet> list = new ArrayList<Tweet>();
		for (int t = 0; t < ListTweets.size(); t++) {
			Tweet tweet = ListTweets.get(t);
			tweet.setState("");
			list.add(0, tweet);
		}

		try {
			CacheReadWriter.sauvegardTweets(list, getActivity());

		} catch (IOException e) {
			// TODO Auto-generated catch block

		}
	}

	boolean first = false;
	final Handler handlerLoad = new Handler() {

		public void handleMessage(Message msg) {

			try {

				ListVideo = (ListView) getActivity().findViewById(
						R.id.ListTweets);
				
				ListVideo.setAdapter(new TweetsAdapter(getActivity(),
						ListTweets));

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	int EditSongNameTextlength = 0;
	int EditAlbumNameTextlength = 0;

	private class ThreadUpdateList extends Thread {
		Handler mHandler;
		boolean Done = false;

		ThreadUpdateList(Handler h) {
			mHandler = h;
		}

		public void run() {
			Elements content = null;

			ConfigurationBuilder cb = new ConfigurationBuilder();

			cb.setDebugEnabled(true)
					.setOAuthConsumerKey(ApplicationConstants.O_AUTH_CONSUMER_KEY)
					.setOAuthConsumerSecret(
							ApplicationConstants.O_AUTH_CONSUMER_SECRET)
					.setOAuthAccessToken(
							ApplicationConstants.O_AUTH_ACCESS_TOKEN)
					.setOAuthAccessTokenSecret(
							ApplicationConstants.O_AUTH_ACCESS_TOKEN_SECRET);

			TwitterFactory tf = new TwitterFactory(cb.build());

			Twitter twitter = tf.getInstance();

			try {
				List<Status> statuses;
				String user;
				user = ApplicationConstants.SINGER;
				statuses = twitter.getUserTimeline(user);

				Boolean wasil = true;
				for (int i = 0; (i < statuses.size() && wasil); i++) {

					Status status = statuses.get(i);
					Tweet t = new Tweet(status.getText(),
							status.getCreatedAt(), status.getId(), "");
					if (ListTweets.size() != 0) {
						for (Tweet twee : ListTweets) {

							if (twee.getTweet().equalsIgnoreCase(t.getTweet())) {
								wasil = false;
							}
						}
					}
					if (wasil) {
						ListTweets
								.add(0,
										new Tweet(status.getText(), status
												.getCreatedAt(),
												status.getId(), "New"));
					} else {

					}

				}
				
			} catch (TwitterException te) {
			}

			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("url", "");
			msg.setData(b);
			mHandler.sendMessage(msg);

		}
	}

}