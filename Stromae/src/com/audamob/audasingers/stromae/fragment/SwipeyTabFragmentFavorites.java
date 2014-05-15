package com.audamob.audasingers.stromae.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.model.Video;
import com.audamob.audasingers.stromae.tool.adapter.VideosAdapter;
import com.audamob.audasingers.stromae.tool.db.CacheReadWriter;
import com.audamob.audasingers.stromae.view.services.ShareService;

public class SwipeyTabFragmentFavorites extends Fragment {

	private ArrayList<Video> ListFavorits;
	static SwipeyTabFragmentFavorites f;
	String TAG="SwipeyTabFragmentFavorites";
	private ShareService shareService;
	public static Fragment newInstance(String title) {
		f = new SwipeyTabFragmentFavorites();
		Bundle args = new Bundle();
		args.putString("title", title);
		f.setArguments(args);

		return f;
	}

	

	ListView listVideofav;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.audamob_version_deux_activity_video, null);

	

		final String title = getArguments().getString("title");
		ListFavorits = new ArrayList<Video>();
		try {
			ListFavorits = CacheReadWriter.restoreFavoriteList(getActivity());
		} catch (Exception e) {
		}
		
		RelativeLayout emptyFavMsg = (RelativeLayout) root.findViewById(R.id.EmptyFavoritesMsg);
		listVideofav = (ListView) root.findViewById(R.id.ListVideo);
		listVideofav.setAdapter(new VideosAdapter(getActivity(), ListFavorits,VideosAdapter.SIMPLE_FAVORITES_MODE));
		
		//SHOW THE EMPTY MESSAGE IF THE FAVORITES LIST IS EMPTY
		if(ListFavorits.isEmpty()) {
			emptyFavMsg.setVisibility(View.VISIBLE);
			listVideofav.setVisibility(View.INVISIBLE);
		} else  {
			emptyFavMsg.setVisibility(View.INVISIBLE);
			listVideofav.setVisibility(View.VISIBLE);
		}

		return root;

	}

		@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
	}

	
}