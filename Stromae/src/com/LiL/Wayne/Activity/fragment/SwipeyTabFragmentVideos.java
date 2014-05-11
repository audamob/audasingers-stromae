package com.LiL.Wayne.Activity.fragment;

import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.LiL.Wayne.Activity.R;
import com.audamob.audasingers.data.DataSource;
import com.audamob.audasingers.model.Video;
import com.audamob.audasingers.tool.adapter.VideosAdapter;
import com.audamob.audasingers.tool.db.CacheReadWriter;
import com.audamob.audasingers.view.services.ShareService;


public class SwipeyTabFragmentVideos extends Fragment {
	private ArrayList<Video> ListVideos;
	private boolean[] HideImageImageselection;
	private ListView ListVideo;
	private VideosAdapter adapter;
	private ShareService shareService;

	static SwipeyTabFragmentVideos f;

	public static Fragment newInstance(String title) {
		f = new SwipeyTabFragmentVideos();
		Bundle args = new Bundle();
		args.putString("title", title);
		f.setArguments(args);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.auda_layout_activity_video, null);
	
		shareService=MainContainerActivityTest.shareService;
		final String title = getArguments().getString("title");

		try {
			ListVideos=CacheReadWriter.restoreListVideos(getActivity());
		} catch(Exception e){
			Log.d("Fav_Video"," error 3 : "+e.getMessage());
		}
		
		if(ListVideos==null){
			ListVideos = DataSource.getListVideos();
			try {
				CacheReadWriter.sauvegardListVideos(ListVideos, getActivity());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d("Fav_Video"," error : "+e.getMessage());
			}
			
		}
		ListVideo = (ListView) root.findViewById(R.id.ListVideo);
		adapter = new VideosAdapter(getActivity(), ListVideos,VideosAdapter.SIMPLE_VIDEO_MODE);
		ListVideo.setAdapter(adapter);
		return root;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		Log.d("Youtube"," hided");
	}

}