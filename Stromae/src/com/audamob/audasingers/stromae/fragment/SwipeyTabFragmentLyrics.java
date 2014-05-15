package com.audamob.audasingers.stromae.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
import com.audamob.audasingers.stromae.data.DataSource;
import com.audamob.audasingers.stromae.model.Lyric;
import com.audamob.audasingers.stromae.tool.adapter.LyricAdapter;

public class SwipeyTabFragmentLyrics extends Fragment {
	private SimpleDateFormat FormatterDuration = new SimpleDateFormat("mm:ss");
	ArrayList<Lyric> LyricList, AllLyricList;
	EditText SearchEdit, SearchEditAlbum;

	public static Fragment newInstance(String title) {
		SwipeyTabFragmentLyrics f = new SwipeyTabFragmentLyrics();
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
				R.layout.audamob_version_deux_activity_lyric, null);
		final String title = getArguments().getString("title");

		LyricList=DataSource.getListLyrics();
		AllLyricList = new ArrayList<Lyric>();

		AllLyricList.addAll(LyricList);
		Log.e("Songs", "size = " + AllLyricList.size());
		ListVideo = (ListView) root.findViewById(R.id.ListAppl);
		ListVideo.setAdapter(new LyricAdapter(getActivity(), LyricList));
		SearchEdit = (EditText) root.findViewById(R.id.searchedit);
		SearchEditAlbum = (EditText) root.findViewById(R.id.searcheditAlbum);
		ThreadUpdateList th = new ThreadUpdateList(handlerLoad);
		th.start();
		return root;

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	final Handler handlerLoad = new Handler() {

		public void handleMessage(Message msg) {

			try {
				LyricList.clear();
				Log.e("Songs", "AllLyric size " + AllLyricList.size());
				for (int i = 0; i < AllLyricList.size(); i++) {

					if (AllLyricList
							.get(i)
							.getName()
							.toLowerCase()
							.contains(
									("" + SearchEdit.getText().toString())
											.toLowerCase())) {
						if (AllLyricList
								.get(i)
								.getAlbum()
								.toLowerCase()
								.contains(
										("" + SearchEditAlbum.getText()
												.toString()).toLowerCase())) {
							LyricList.add(AllLyricList.get(i));
						}
					}
				}
				Log.e("Songs", "change :: " + LyricList.size());
				ListVideo = (ListView) getActivity()
						.findViewById(R.id.ListAppl);
				ListVideo.setAdapter(new LyricAdapter(getActivity(), LyricList));
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
			while (!Done) {
				try {
					currentThread().sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Log.e("Songs","change : "+SearchEdit.getText().length());
				if (SearchEdit.getText().length() == EditSongNameTextlength
						&& SearchEditAlbum.getText().length() == EditAlbumNameTextlength) {
				} else {
					Log.e("Songs", "change");
					EditSongNameTextlength = SearchEdit.getText().length();
					EditAlbumNameTextlength = SearchEditAlbum.getText()
							.length();
					Message msg = mHandler.obtainMessage();
					Bundle b = new Bundle();
					b.putString("url", "");
					msg.setData(b);
					mHandler.sendMessage(msg);
				}
			}
		}
	}



}