package com.audamob.audasingers.stromae.tool.adapter;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.activity.PlayList_Interface;
import com.audamob.audasingers.stromae.model.Music;
import com.audamob.audasingers.stromae.model.PlayList;
import com.audamob.audasingers.stromae.tool.db.CacheReadWriter;

public class PlaylistAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<PlayList> playlistMusicList;
	private ArrayList<Music> listAllMusic;
	private Activity activity;
	private int mode;
	private Typeface font;
	private Music musicToAdd;
	private int positionMusicToAdd;

	public PlaylistAdapter(Activity activity, ArrayList<PlayList> list,
			Music musicToAdd, int postiobMusicToAdd) {
		this.activity = activity;
		this.playlistMusicList = list;
		font = Typeface.createFromAsset(this.activity.getAssets(),
				"ExoMedium.otf");
		this.musicToAdd = musicToAdd;
		this.positionMusicToAdd = postiobMusicToAdd;
		try {
			listAllMusic = CacheReadWriter.restoreListMusics(activity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (musicToAdd == null) {
			mode = 0;
		} else {
			mode = 1;
		}
		mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return playlistMusicList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final PlaylistHoder holder;
		if (convertView == null) {
			holder = new PlaylistHoder();

			convertView = mInflater.inflate(R.layout.audamob_version_deux_item_playlist, null);

			holder.Name = (TextView) convertView.findViewById(R.id.nam);
			holder.size = (TextView) convertView.findViewById(R.id.Number);
			holder.ItemSong = (RelativeLayout) convertView
					.findViewById(R.id.ItemSong);

			convertView.setTag(holder);
		} else {
			holder = (PlaylistHoder) convertView.getTag();
		}

		holder.Name.setId(position);
		holder.size.setId(position);
		holder.ItemSong.setId(position);

		holder.ItemSong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mode == 0) {

					Intent i = new Intent(activity.getBaseContext(),
							PlayList_Interface.class);
					i.putExtra("playlist", playlistMusicList.get(position));

					activity.startActivity(i);
					activity.overridePendingTransition(R.anim.push_down_in,
							R.anim.push_down_out);
				} else {

					
					
				
					int idPlaylist = position;
					
					
					musicToAdd.setNamePlaylist(playlistMusicList.get(position).getName());

					// juste pour gerer les music playlist
					musicToAdd.setPostionDansLaListe(positionMusicToAdd);

					playlistMusicList.get(idPlaylist).getListMusic()
							.add(musicToAdd);
					try {
						CacheReadWriter.sauvegardPlayListList(
								playlistMusicList, activity);
					} catch (Exception e) {
					}

					
					listAllMusic.get(positionMusicToAdd).setNamePlaylist(playlistMusicList.get(position).getName());

					try {
						CacheReadWriter.sauvegardListMusics(listAllMusic,
								activity);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					TestTest();
					activity.finish();
				}
			}
		});
		holder.Name.setTypeface(font);
		holder.size.setTypeface(font);
		holder.Name.setText(playlistMusicList.get(position).getName());
		holder.size.setText(playlistMusicList.get(position).getListMusic()
				.size()
				+ " songs");
		holder.id = position;

		return convertView;
	}

private void TestTest(){
	
	try {
		
		ArrayList<PlayList> listPlaylist =CacheReadWriter.restorePlayListList(activity);
		
		for (PlayList playList : listPlaylist) {
			Log.d("DebugPlaylist","-------------------------------------------");
			Log.d("DebugPlaylist","Name: "+playList.getName());
			
			for (Music music : playList.getListMusic()) {
				
				Log.d("DebugPlaylist","Musi Name: "+music.getName());
				Log.d("DebugPlaylist","Musi Name: "+music.getPostionDansLaListe());
				Log.d("DebugPlaylist","Musi Name: "+music.getPlaylistName());
			}
			Log.d("DebugPlaylist","********************************************");
			
		}

	} catch (Exception e) {
		// TODO: handle exception
	}
}

}

class PlaylistHoder {

	TextView Name;
	TextView size;
	RelativeLayout ItemSong;
	int id;
}