package com.audamob.audasingers.stromae.fragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.data.DataSource;
import com.audamob.audasingers.stromae.model.Music;
import com.audamob.audasingers.stromae.model.PlayList;
import com.audamob.audasingers.stromae.tool.adapter.MusicAdapter;
import com.audamob.audasingers.stromae.tool.db.CacheReadWriter;
import com.audamob.audasingers.stromae.tool.view.ImageResizerUtils;

public class SwipeyTabFragmentMusic extends Fragment {
	private SimpleDateFormat FormatterDuration = new SimpleDateFormat("mm:ss");
	ArrayList<Music> musicList;
	ArrayList<Music> AllLyricList;

	static SwipeyTabFragmentMusic f;

	public static Fragment newInstance(String title) {
		f = new SwipeyTabFragmentMusic();
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

	static ListView ListVideo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.audamob_version_deux_layout_activity_music, null);
		ViewGroup mainContainer = (ViewGroup) root
				.findViewById(R.id.MainContainer);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"ExoMedium.otf");

		ImageResizerUtils.setFont(getActivity(), mainContainer, font);
		final String title = getArguments().getString("title");
		musicList = new ArrayList<Music>();
		try {
			musicList = CacheReadWriter.restoreListMusics(getActivity());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (musicList.size() == 0) {
			Log.d("FragmentMusic", " musicList bien Null  ");
			musicList = DataSource.getListMusics();
			try {
				CacheReadWriter.sauvegardListMusics(musicList, getActivity());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		AllLyricList = new ArrayList<Music>();
		for (int s = 0; s < musicList.size(); s++)
			AllLyricList.add(musicList.get(s));

		ListVideo = (ListView) root.findViewById(R.id.ListAppM);
		ListVideo.setAdapter(new MusicAdapter(getActivity(), musicList,
				MusicAdapter.MODE_MUSIC));
		return root;

	}

	boolean ModePlaylist = false;
	PlayList pl = null;
	int plID = 0;
	ArrayList<PlayList> ArrayPlaylist;

	@Override
	public void onResume() {
		super.onResume();
		TestTest(getActivity());

	}

	int EditSongNameTextlength = 0;

	private void TestTest(Activity activity) {

		try {

			for (Music music : musicList) {

				Log.d("DebugPlaylist", "Musi Name: " + music.getPlaylistName());
			}
			Log.d("DebugPlaylist",
					"********************************************");

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}