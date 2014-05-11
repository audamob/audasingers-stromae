package com.audamob.audasingers.tool.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.LiL.Wayne.Activity.R;
import com.LiL.Wayne.Activity.activity.MusicPlayer;
import com.LiL.Wayne.Activity.activity.SelectPlaylist;
import com.LiL.Wayne.Activity.fragment.MainContainerActivityTest;
import com.audamob.audasingers.constant.ApplicationConstants;
import com.audamob.audasingers.model.Music;
import com.audamob.audasingers.model.PlayList;
import com.audamob.audasingers.tool.db.CacheReadWriter;
import com.audamob.audasingers.view.services.ShareService;

public class MusicAdapter extends BaseAdapter {
	private SimpleDateFormat FormatterDuration = new SimpleDateFormat("mm:ss");
	private LayoutInflater mInflater;
	private ArrayList<Music> musicList;
	private Activity activity;
	private Typeface font;
	public static int MODE_MUSIC = 0;
	public static int MODE_PLAYLIST = 1;
	private int mode = 0;
	private String TAG = "MusicAdapter";

	private MusicAdapter musicAdapter;

	class ViewHolderImage {
		ImageView Logo;
		TextView Name;
		CheckBox checkbox;
		TextView musicDuration;
		TextView nbrPlayer;
		TextView nbrFavorites;
		ImageView share;
		int id;
	}

	public MusicAdapter(Activity activity, ArrayList<Music> list, int mode) {

		this.activity = activity;
		this.musicList = list;
		font = Typeface.createFromAsset(this.activity.getAssets(),
				"ExoMedium.otf");
		this.mode = mode;
		mInflater = (LayoutInflater) this.activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.musicAdapter = this;
	}

	public int getCount() {
		return musicList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolderImage holder;
		if (convertView == null) {
			holder = new ViewHolderImage();

			convertView = mInflater.inflate(R.layout.auda_item_music, null);

			holder.Logo = (ImageView) convertView.findViewById(R.id.Icon);
			holder.Name = (TextView) convertView.findViewById(R.id.nam);
			holder.musicDuration = (TextView) convertView
					.findViewById(R.id.Number);
			holder.checkbox = (CheckBox) convertView
					.findViewById(R.id.checkbox);
			
			holder.nbrPlayer = (TextView) convertView.findViewById(R.id.nbrPlay);
			
			holder.nbrFavorites = (TextView) convertView
					.findViewById(R.id.nbrPlaylistsTextView);
			holder.share=(ImageView)convertView.findViewById(R.id.partager);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderImage) convertView.getTag();
		}

		holder.Logo.setId(position);
		holder.Name.setId(position);
		holder.checkbox.setId(position);
		holder.nbrFavorites.setId(position);
		holder.nbrPlayer.setId(position);
		holder.share.setId(position);
		convertView.findViewById(R.id.ItemSong).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// TODO Auto-generated method stub
						Intent i = new Intent(activity.getBaseContext(),
								MusicPlayer.class);
						i.putExtra("name", musicList.get(position).getName());
						i.putExtra("album", musicList.get(position).getUrl());
						i.putExtra("feat", musicList.get(position)
								.getFeatring());
						i.putExtra("mode", 1);
						activity.startActivity(i);

						activity.overridePendingTransition(R.anim.push_down_in,
								R.anim.push_down_out);
					}
				});

		holder.Name.setTypeface(font);
		holder.Name.setText(musicList.get(position).getName());
		holder.musicDuration.setText(""+ FormatterDuration.format(musicList.get(position).getDuration()));
holder.share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					MainContainerActivityTest.shareService.setDismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				MainContainerActivityTest.shareService=new ShareService(activity,musicList.get(position).getUrl(), holder.share);
			}
		});
		//Increment the play nbr ...
		Random rPlay = new Random();
		int valeurPlay = 0 + rPlay.nextInt(1000 - 0);
		holder.nbrPlayer.setText(""+ Math.round((musicList.get(position).getNbrPlay() + valeurPlay)/1000)+"K");
		
		//Increment the fav nbr ...
		Random rFav = new Random();
		int valeurFav = 0 + rFav.nextInt(100 - 0);
		holder.nbrFavorites.setText(""+ Math.round((musicList.get(position).getNbrFavorite() + valeurFav)/1000)+"K");
		
		boolean checked = false;
		if (musicList.get(position).getPlaylistName().length()>1) {
			checked = true;
		}
		holder.checkbox.setChecked(checked);
		holder.checkbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (mode == MusicAdapter.MODE_MUSIC) {

					// Dans le cas de list music de la list generale
					
					
					if (!(musicList.get(position).getPlaylistName().length()>1)) {
						Intent i = new Intent(activity.getBaseContext(),
								SelectPlaylist.class);
						i.putExtra("Music", musicList.get(position));
						i.putExtra("Position", position);
						activity.startActivityForResult(i, 3);
					} else {


						try {

							ArrayList<PlayList> list = CacheReadWriter
									.restorePlayListList(activity);

							for (PlayList playList : list) {

								if (playList.getName().equalsIgnoreCase(musicList.get(position).getPlaylistName())) {
									for (Music musicPlaylist : playList
											.getListMusic()) {

										if (musicPlaylist.getUrl().equalsIgnoreCase(musicList.get(position).getUrl())) {

											playList.getListMusic().remove(musicPlaylist);
											break;
										}
									}
									break;

								}
							}
							

							musicList.get(position).setNamePlaylist("");

							CacheReadWriter.sauvegardPlayListList(list,
									activity);
							CacheReadWriter.sauvegardListMusics(musicList,
									activity);

							musicAdapter.notifyDataSetChanged();

						} catch (Exception e) {

							// TODO Auto-generated catch block

						}
						
					}
				} else {
					try {
						
						// Dans le cas de list music d'une playlist 
						
						if(! (musicList.get(position).getPlaylistName().length()>1)) {
							// c'est toujour different 0
						} else {

							Music music = musicList.get(position);
							int postionDansLaListe = music.getPostionDansLaListe();
							String namePlayList = music.getPlaylistName();

						
							ArrayList<Music> listMusic = CacheReadWriter.restoreListMusics(activity);

							listMusic.get(postionDansLaListe).setNamePlaylist("");

							CacheReadWriter.sauvegardListMusics(listMusic,activity);

							ArrayList<PlayList> lisPlaylist = CacheReadWriter
									.restorePlayListList(activity);
							
							for (PlayList playList : lisPlaylist) {

								if (playList.getName().equalsIgnoreCase(namePlayList)) {
									for (Music musicPlaylist : playList
											.getListMusic()) {

										if (musicPlaylist.getUrl().equalsIgnoreCase(listMusic
												.get(postionDansLaListe)
												.getUrl())) {

											playList.getListMusic().remove(
													musicPlaylist);
											break;
										}
									}
									break;

								}
							}

							CacheReadWriter.sauvegardPlayListList(lisPlaylist,
									activity);

							musicList.remove(position);

							musicAdapter.notifyDataSetChanged();

						}

					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			}

		});
		holder.id = position;
		
		

		return convertView;
	}
}
