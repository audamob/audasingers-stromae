package com.LiL.Wayne.Activity.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.LiL.Wayne.Activity.R;
import com.LiL.Wayne.Activity.SlidingMenu.ActivityBase;
import com.audamob.audasingers.model.Music;
import com.audamob.audasingers.model.PlayList;
import com.audamob.audasingers.tool.adapter.PlaylistAdapter;
import com.audamob.audasingers.tool.db.CacheReadWriter;
import com.audamob.audasingers.tool.view.ImageResizerUtils;

public class SelectPlaylist extends ActivityBase {
	private SimpleDateFormat FormatterDuration = new SimpleDateFormat("mm:ss");
	private ArrayList<PlayList> playlistMusicList;
	private String TAG="SelectPlaylist";
	private ListView listViewPlayist;
	private Activity activity;
	private Music musicToAdd;
	private int positionMusicToAdd;


	
	
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

		setContentView(R.layout.auda_layout_select_playlist);
		ActionBar actionBar = getActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(false);

		ViewGroup mainContainer = (ViewGroup) findViewById(R.id.MainContainer);
		Typeface font = Typeface.createFromAsset(getAssets(), "ExoMedium.otf");

		ImageResizerUtils.setFont(this, mainContainer, font);
		
		Bundle b = getIntent().getExtras();
		musicToAdd=(Music) b.getSerializable("Music");
		positionMusicToAdd=b.getInt("Position");
		
		
		activity=this;
		playlistMusicList = new ArrayList<PlayList>();
		try {
			playlistMusicList = CacheReadWriter.restorePlayListList(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RelativeLayout CreateNewPlaylist =(RelativeLayout)findViewById(R.id.Create);
		CreateNewPlaylist.setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentNewPlaylist=new Intent(activity,NewPlaylist.class);
				startActivityForResult(intentNewPlaylist, 3);
			}
		});
		listViewPlayist = (ListView) findViewById(R.id.ListView_playist);
		listViewPlayist.setAdapter(new PlaylistAdapter(this, playlistMusicList,musicToAdd,positionMusicToAdd));
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		playlistMusicList.clear();
		try {
			playlistMusicList = CacheReadWriter.restorePlayListList(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listViewPlayist = (ListView) findViewById(R.id.ListView_playist);
		listViewPlayist.setAdapter(new PlaylistAdapter(this, playlistMusicList,musicToAdd,positionMusicToAdd));
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}