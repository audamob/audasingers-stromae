package com.audamob.audasingers.tool.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.LiL.Wayne.Activity.R;
import com.LiL.Wayne.Activity.fragment.MainContainerActivityTest;
import com.LiL.Wayne.Activity.fragment.SwipeyTabFragmentBio;
import com.LiL.Wayne.Activity.fragment.SwipeyTabFragmentFavorites;
import com.LiL.Wayne.Activity.fragment.SwipeyTabFragmentLyrics;
import com.LiL.Wayne.Activity.fragment.SwipeyTabFragmentMusic;
import com.LiL.Wayne.Activity.fragment.SwipeyTabFragmentMusicPlayList;
import com.LiL.Wayne.Activity.fragment.SwipeyTabFragmentNews;
import com.LiL.Wayne.Activity.fragment.SwipeyTabFragmentSettings;
import com.LiL.Wayne.Activity.fragment.SwipeyTabFragmentTweets;
import com.LiL.Wayne.Activity.fragment.SwipeyTabFragmentVideos;

public class SwipeyTabsPagerAdapter extends FragmentStatePagerAdapter implements
		SwipeyTabsAdapter {
	private String[] TabTitles;
	private final Context mContext;
	private ViewPager mViewPager;
	private int TabToLoad;

	public SwipeyTabsPagerAdapter(Context context, FragmentManager fm,
			String[] tabs, ViewPager mviewPager, int TabToLoad) {
		super(fm);
		this.TabTitles = tabs;
		this.mContext = context;
		this.mViewPager = mviewPager;
		this.TabToLoad = TabToLoad;

	}

	@Override
	public Fragment getItem(int position) {
		return CreateSwipeyTab(TabTitles[position], position, TabToLoad);

	}

	/**
	 * Creation Tab
	 * 
	 * @param NameTab
	 * @return
	 */
	public Fragment CreateSwipeyTab(String NameTab, int position,
			int NameFragmentCalss) {

		Fragment f = GetClassFragment(NameFragmentCalss, position);

		Bundle args = new Bundle();
		args.putString("title", NameTab);
		f.setArguments(args);
		return f;

	}

	public Fragment GetClassFragment(int NameFragmentCalss, int position) {

		switch (NameFragmentCalss) {
		case 0:
			switch (position) {
			case 0:
				return new SwipeyTabFragmentVideos();

			default:
				return new SwipeyTabFragmentFavorites();
			}
		case 1:
			return new SwipeyTabFragmentMusic();
		case 2:
			return new SwipeyTabFragmentLyrics();
		case 3:
			switch (position) {
			case 0:
				return new SwipeyTabFragmentNews();

			default:
				return new SwipeyTabFragmentTweets();

			}

		case 4:
			return new SwipeyTabFragmentTweets();
		case 5:
			return new SwipeyTabFragmentBio();
		case 6:
			return new SwipeyTabFragmentSettings();
		case 7:
			return new SwipeyTabFragmentMusicPlayList();
		case 10:
			return new SwipeyTabFragmentFavorites();
		default:
			return new SwipeyTabFragmentSettings();

		}
	}

	@Override
	public int getCount() {
		return TabTitles.length;
	}

	public TextView getTab(final int position, SwipeyTabs root) {
		TextView view = (TextView) LayoutInflater.from(mContext).inflate(
				R.layout.swipeytab_indicator, root, false);
		view.setText(TabTitles[position]);
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mViewPager.setCurrentItem(position);
			}
		});

		return view;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TabTitles[position % TabTitles.length].toUpperCase();
	}

}
