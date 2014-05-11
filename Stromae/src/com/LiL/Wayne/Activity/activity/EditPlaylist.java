package com.LiL.Wayne.Activity.activity;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.LiL.Wayne.Activity.R;
import com.audamob.audasingers.model.Music;
import com.audamob.audasingers.model.PlayList;
import com.audamob.audasingers.tool.db.CacheReadWriter;
public class EditPlaylist extends Activity{
	private SimpleDateFormat FormatterDuration = new SimpleDateFormat("mm:ss");
	ArrayList<PlayList> PlaylistMusicList;
	
	
		Activity activity;
		PlayList playList;
        ListView ListVideo;
        ArrayList<Music> ListMusic;
        int plID=0;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        	// TODO Auto-generated method stub
        	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        	  setContentView(R.layout.edit_playlist);
    		  Bundle b=getIntent().getExtras();
    		  final int plID=b.getInt("position");
    		  Log.d("EditPlaylist"," id = "+plID);
    		  this.activity = this;
    		  
            PlaylistMusicList=new ArrayList<PlayList>();
            try {
				PlaylistMusicList=CacheReadWriter.restorePlayListList(activity);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("EditPlaylist"," name = "+PlaylistMusicList.get(plID).getName());
			Log.d("EditPlaylist"," size = "+PlaylistMusicList.get(plID).getListMusic().size());
			
		/*	for(int s=;s<PlaylistMusicList.get(plID).getListMusic().size();s++){
				
			}*/
			
			
			ListMusic = new ArrayList<Music>();
			ListMusic.addAll(PlaylistMusicList.get(plID).getListMusic());
        	ListVideo=(ListView) findViewById(R.id.ListPlayListeditt);
         	ListVideo.setAdapter(new AppAdapter());
         
         	RelativeLayout Done=(RelativeLayout)findViewById(R.id.Done);
         	Done.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
        	TextView deleall=(TextView)findViewById(R.id.deleteAll);
         	deleall.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PlaylistMusicList.remove(plID);
					try {
						CacheReadWriter.sauvegardPlayListList(PlaylistMusicList,activity);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finish();
				}
			});
         	super.onCreate(savedInstanceState);
        }
        
            
        
     	public class AppAdapter extends BaseAdapter {


      		private LayoutInflater mInflater;

      		public AppAdapter() {mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      		}

      		public int getCount() {
      			return ListMusic.size();
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
      				
      				convertView = mInflater.inflate(R.layout.itemeditplaylistmusic, null);
      				
      				holder.Logo = (ImageView) convertView.findViewById(R.id.Icon);
      				holder.Name=(TextView)convertView.findViewById(R.id.nam);
      				
      				
      				convertView.setTag(holder);
      			}
      			else {
      				holder = (ViewHolderImage) convertView.getTag();
      			}
      			
      			holder.Logo.setId(position);
      			holder.Name.setId(position);
      			
      			
      			convertView.findViewById(R.id.ItemSong).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						PlaylistMusicList.get(plID).getListMusic().remove(ListMusic.get(position));
						ListMusic.clear();
						ListMusic.addAll(PlaylistMusicList.get(plID).getListMusic());
			        	ListVideo=(ListView) findViewById(R.id.ListPlayListeditt);
			         	ListVideo.setAdapter(new AppAdapter());
			         	try {
							CacheReadWriter.sauvegardPlayListList(PlaylistMusicList,activity);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
      			
      		
      			holder.Name.setText(ListMusic.get(position).getName());
      		
      			holder.id = position;
      		
      			return convertView;
      		}
      	}
    	class ViewHolderImage {

	  		ImageView Logo;
	  		TextView Name;
	  		int id;
  	 }
   	    	 
}