package com.LiL.Wayne.Activity.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.LiL.Wayne.Activity.R;
import com.LiL.Wayne.Activity.activity.SelectPlaylist;
import com.audamob.audasingers.model.PlayList;
import com.audamob.audasingers.tool.adapter.PlaylistAdapter;
import com.audamob.audasingers.tool.adapter.VideosAdapter;
import com.audamob.audasingers.tool.db.CacheReadWriter;

public class SwipeyTabFragmentMusicPlayList extends Fragment {
	private SimpleDateFormat FormatterDuration = new SimpleDateFormat("mm:ss");
	public static SwipeyTabFragmentMusicPlayList f;
	String TAG = "SwipeyTabFragmentMusicPlayList";

	public static Fragment newInstance(String title) {
		f = new SwipeyTabFragmentMusicPlayList();
		Bundle args = new Bundle();
		args.putString("title", title);
		f.setArguments(args);

		return f;
	}

	

	private  ListView ListVideo;
	private RelativeLayout emptyPlaylistsMsg;

	private ArrayList<PlayList> PlaylistMusicList;
	ArrayList<PlayList> AllLyricList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.auda_layout_activity_playlist, null);
		final String title = getArguments().getString("title");
		
		emptyPlaylistsMsg = (RelativeLayout) root.findViewById(R.id.EmptyPlaylistsMsg);
		ListVideo = (ListView) root.findViewById(R.id.ListAppPLL);
	
	
		return root;

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		PlaylistMusicList = new ArrayList<PlayList>();
		Log.e(TAG,"niv 1 ");
		try {
			Log.e(TAG,"niv  2");
			PlaylistMusicList = CacheReadWriter
					.restorePlayListList(getActivity());
			Log.e(TAG,"niv 3 "+PlaylistMusicList.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG,"niv 4 "+e.getMessage());
		} 
		Log.e(TAG,"niv 5 :  "+PlaylistMusicList.size());
		ListVideo.setAdapter(new PlaylistAdapter(getActivity(),
				PlaylistMusicList, null, 0));
		
		//SHOW THE EMPTY MESSAGE IF THE PLAYLISTS IS EMPTY
		if(PlaylistMusicList.isEmpty()) {
			emptyPlaylistsMsg.setVisibility(View.VISIBLE);
			ListVideo.setVisibility(View.INVISIBLE);
		} else  {
			emptyPlaylistsMsg.setVisibility(View.INVISIBLE);
			ListVideo.setVisibility(View.VISIBLE);
		}
	}
}