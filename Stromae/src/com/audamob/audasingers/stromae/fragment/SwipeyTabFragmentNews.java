package com.audamob.audasingers.stromae.fragment;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.model.News;
import com.audamob.audasingers.stromae.tool.adapter.NewsAdapter;
import com.audamob.audasingers.stromae.tool.db.CacheReadWriter;

public class SwipeyTabFragmentNews extends Fragment {

	ArrayList<News> newsList;
	EditText SearchEdit, SearchEditAlbum;
	Typeface font1;

	public static Fragment newInstance(String title) {
		SwipeyTabFragmentNews f = new SwipeyTabFragmentNews();
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
				R.layout.audamob_version_deux_activity_news, null);
		final String title = getArguments().getString("title");
		
		newsList = new ArrayList<News>();
		try {
			newsList = CacheReadWriter.restore_News(getActivity());
		} catch (Exception e) {
			// TODO: handle exception
		}
		Log.i("UpdateListNews", "je fetch : " + newsList.size());
		ListVideo = (ListView) root.findViewById(R.id.ListNews);
		ListVideo.setAdapter(new NewsAdapter(getActivity(), newsList));
		return root;

	}


	


}