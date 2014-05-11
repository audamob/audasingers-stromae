package com.LiL.Wayne.Activity.fragment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.LiL.Wayne.Activity.R;
import com.LiL.Wayne.Activity.R.id;
import com.LiL.Wayne.Activity.R.layout;
import com.audamob.audasingers.model.News;
import com.audamob.audasingers.tool.adapter.NewsAdapter;
import com.audamob.audasingers.tool.db.CacheReadWriter;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

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
				R.layout.layout_activity_news, null);
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