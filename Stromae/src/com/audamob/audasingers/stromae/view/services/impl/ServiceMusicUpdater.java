package com.audamob.audasingers.stromae.view.services.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.audamob.audasingers.stromae.constant.ApplicationConstants;
import com.audamob.audasingers.stromae.model.Music;
import com.audamob.audasingers.stromae.tool.json.JSONParser;
import com.audamob.audasingers.stromae.view.services.IServiceDataUpdater;

public class ServiceMusicUpdater extends Service implements
		IServiceDataUpdater<Music> {

	public static final int THREAD_TIMEOUT_SLEEP = 12 * 60 * 100 * 100; // Thread 12H Sleep
	public ArrayList<Music> existingMusicsList = new ArrayList<Music>();
	public ArrayList<Music> newMusicList = new ArrayList<Music>();
	public int differenceMusicLists = 0;
	public ArrayList<Music> diffMusList = new ArrayList<Music>();
	public JSONParser jsonParser;
	ServiceMusicUpdater instance;

	/**
	 * Fetch Data from existing Cache
	 */
	@Override
	public ArrayList<Music> getExistingDataFromCache() throws IOException,
			ClassNotFoundException {
		
		FileInputStream fin = new FileInputStream(getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_MUSIC);
		ObjectInputStream in = new ObjectInputStream(fin);
		ArrayList<Music> musics = (ArrayList<Music>) in.readObject();
		//in.close();
		return musics;
	}

	/**
	 * Set Data In existing Cache
	 */
	@Override
	public void setNewDataInCache(ArrayList<Music> s) throws IOException {
		FileOutputStream fout = new FileOutputStream(getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_MUSIC);
		ObjectOutputStream out = new ObjectOutputStream(fout);
		out.writeObject(s);
		out.close();
	}

	/**
	 * Fetch data from Server
	 */
	@Override
	public ArrayList<Music> getDataFromServer(String serverUrl) {
		
		Log.i("MUSIC","serverUrl  : "+serverUrl);
		
		// Creating JSON Parser object
	    jsonParser = new JSONParser();

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(serverUrl, "GET", params);
        
        //Log.i("VIDEOS", json.toString());
        
        //New Music List
        ArrayList<Music> musics = new ArrayList<Music>();
        
        JSONArray musicsJSONArray = null;
        
        // ALL JSON node names
        final String TAG_MUSICS_SET = "musicSet";
        final String TAG_NAME = "SongName";
        final String TAG_URL = "Url";
        final String TAG_FEATURING = "Featring";
        final String TAG_NBR_PLAY = "nbrPlay";
        final String TAG_NBR_FAVORITE = "nbrFavorite";
        final String TAG_DURATION = "duration";


        try {
        		musicsJSONArray = json.getJSONArray(TAG_MUSICS_SET);
                
                // looping through All messages
                //Log.i("MUSICS","array" + videosJSONArray.toString());

                for (int i = 0; i < musicsJSONArray.length(); i++) {
            		JSONObject c = musicsJSONArray.getJSONObject(i);
            		
                    // Create activity Object
                    Music music = new Music(c.getString(TAG_NAME),c.getString(TAG_URL),c.getString(TAG_FEATURING),c.getInt(TAG_NBR_PLAY),c.getInt(TAG_NBR_FAVORITE),c.getLong(TAG_DURATION));
					Log.i("MUSICS","added Music : "+music.getName() +" - "+music.getUrl()+" - "+music.getFeatring());
					musics.add(music);
                }

        } catch (JSONException e) {
                e.printStackTrace();
        }

        return musics;
	}
	
	
	/**
	 * Calculate Diff between the new and the old Videos List
	 * @param videosList
	 * @param newVideoList
	 * @return
	 */
	private ArrayList<Music> calculateDiff(ArrayList<Music> musicsList,
			ArrayList<Music> newMusicList) {
		
		int countDiff = 0;
		
		//Set "tag" property to "old"
		for (Music m : newMusicList) {
			m.setTag("old");
			
		}
		
		ArrayList<Music> listeDiffMusics = new ArrayList<Music>();
		
		boolean founded = false;
			for (Music newmusic : newMusicList) {
				for (Music oldmusic : musicsList) {
					if(oldmusic.getName().equalsIgnoreCase(newmusic.getName())) {					
						founded = true;
						break;
					}
				}
				
				//creer la nouvelle liste diff
				if(!founded) {	
					listeDiffMusics.add(newmusic);
				}
				
				founded = false;
			}
		
			countDiff = listeDiffMusics.size();
			Log.i("MUSICS","countDiff = "+countDiff);

			//Set "tag" property to "new" only for new added Videos
			for (Music music : listeDiffMusics) {
				music.setTag("new");
			}
		
		return listeDiffMusics;
	}
	
	/**
	 * Set New Data count in cache
	 */
	@Override
	public void setNewDataCountInCache(int count) throws IOException {
		FileOutputStream fout = new FileOutputStream(getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_COUNT_NEW_MUSICS);
		ObjectOutputStream out = new ObjectOutputStream(fout);
		out.writeObject(count);
		out.close();		
	}

	@Override
	public void onCreate() {
		instance=this;
		
		 ThreadMusicLoad th=new ThreadMusicLoad(instance);
		 th.start();	 
	}	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	
	@Override
	public boolean onUnbind(Intent intent) {	
		return super.onUnbind(intent);
	}

	private class ThreadMusicLoad extends Thread {
		
		ServiceMusicUpdater smu;
		
		public ThreadMusicLoad(ServiceMusicUpdater smu) {
			this.smu = smu;
		}

		public void run() {

			while (true) {
				
				//Get Existing Videos from Cache 
				try {
					existingMusicsList = this.smu.getExistingDataFromCache();
				} catch (IOException e1) {
					Log.e("MUSICS","IOException cache : "+e1.getMessage());
				} catch (ClassNotFoundException e1) {
					Log.e("MUSICS","ClassNotFoundException cache : "+e1.getMessage());
				}
				
				try {
					// Get New Data From Server
					newMusicList = this.smu.getDataFromServer(ApplicationConstants.SERVER_JSON_MUSICS_PATH);
	                Log.i("MUSICS","Total New Musics : "+newMusicList.size());

					// Calculate Diff for Notification
					if(!newMusicList.isEmpty()) {
						diffMusList = this.smu.calculateDiff(existingMusicsList,newMusicList);
		                Log.i("MUSICS","diffMusList size : "+diffMusList.size());
		                differenceMusicLists = diffMusList.size();

					//Set Count in cache
		            this.smu.setNewDataCountInCache(differenceMusicLists);
	                Log.i("MUSICS","differenceMusicLists count : "+differenceMusicLists);

					// Set Data In Cache
					existingMusicsList.addAll(diffMusList);
					Log.i("MUSICS","musics To set in cache size : "+existingMusicsList.size());

					this.smu.setNewDataInCache(existingMusicsList);
	
					//Clear New Musics List
					newMusicList.clear();
					existingMusicsList.clear();
					
					}
					
				} catch (Exception e) {
					Log.e("MUSICS","EXCEPTION : "+e.getMessage());
				}
				
				//this Thread turned every 12 hours
				 try {
						this.sleep(THREAD_TIMEOUT_SLEEP);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}

		}


	}
	
}
