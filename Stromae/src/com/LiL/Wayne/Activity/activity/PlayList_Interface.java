package com.LiL.Wayne.Activity.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.LiL.Wayne.Activity.R;
import com.LiL.Wayne.Activity.SlidingMenu.ActivityBase;
import com.LiL.Wayne.Activity.fragment.MainContainerActivityTest;
import com.audamob.audasingers.model.Music;
import com.audamob.audasingers.model.PlayList;
import com.audamob.audasingers.tool.adapter.MusicAdapter;
import com.audamob.audasingers.tool.db.CacheReadWriter;
import com.audamob.audasingers.tool.view.ImageResizerUtils;

public class PlayList_Interface extends ActivityBase {

	private SimpleDateFormat FormatterDuration = new SimpleDateFormat("mm:ss");
	ArrayList<Music> musicList;

	ListView ListVideo;

	int position;
	String NamePlaylist;
	ArrayList<PlayList> listPLaylist;
	private PlayList playlist;
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public boolean enableHomeIconActionSlidingMenu() {
		return true;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.auda_layout_activity_playlist_music);

		
		ActionBar actionBar = getActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(false);

		ViewGroup mainContainer = (ViewGroup) findViewById(R.id.MainContainer);
		Typeface font = Typeface.createFromAsset(getAssets(), "ExoMedium.otf");

		ImageResizerUtils.setFont(this, mainContainer, font);
		
		Bundle b = getIntent().getExtras();
		position = b.getInt("position");


		playlist=(PlayList) b.getSerializable("playlist");
		musicList=playlist.getListMusic();
		NamePlaylist=playlist.getName();
		
		ListVideo = (ListView) findViewById(R.id.ListAppM);
		ListVideo.setAdapter(new MusicAdapter(this, musicList,MusicAdapter.MODE_PLAYLIST));

	

		RelativeLayout play_all = (RelativeLayout) findViewById(R.id.Layout_Add_video);
		play_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), MusicPlayer.class);
				i.putExtra("name", "");
				i.putExtra("album", "");
				i.putExtra("mode", 2);
				i.putExtra("playlist", playlist);
				startActivity(i);
				overridePendingTransition(R.anim.push_down_in,
						R.anim.push_down_out);
			}
		});

		TextView tx = (TextView) findViewById(R.id.Title);
		tx.setText("Playlist : " + NamePlaylist);

	}

	


	@Override
	public void onResume() {
		super.onResume();
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		if(MainContainerActivityTest.shareService!=null){
			if(MainContainerActivityTest.shareService.getIsVisible()){
				MainContainerActivityTest.shareService.setDismiss();
			}else{
				finish();
				overridePendingTransition(R.anim.push_down_out_back,
						R.anim.push_down_in_back);
			}
		}else{
			finish();
			overridePendingTransition(R.anim.push_down_out_back,
					R.anim.push_down_in_back);
		}
		
	}

}
