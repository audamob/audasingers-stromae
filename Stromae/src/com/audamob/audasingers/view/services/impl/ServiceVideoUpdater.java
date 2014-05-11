package com.audamob.audasingers.view.services.impl;

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

import com.audamob.audasingers.constant.ApplicationConstants;
import com.audamob.audasingers.model.Video;
import com.audamob.audasingers.tool.json.JSONParser;
import com.audamob.audasingers.view.services.IServiceDataUpdater;

public class ServiceVideoUpdater extends Service implements
		IServiceDataUpdater<Video> {

	public static final int THREAD_TIMEOUT_SLEEP = 12 * 60 * 100 * 100; // Thread 12H Sleep
	public ArrayList<Video> existingVideosList = new ArrayList<Video>();
	public ArrayList<Video> newVideoList = new ArrayList<Video>();
	public int differenceVideoLists = 0;
	public ArrayList<Video> diffVidList = new ArrayList<Video>();
	public JSONParser jsonParser;
	ServiceVideoUpdater instance;


	/**
	 * 
	 * Fetch Data from existing Cache
	 */
	@Override
	public ArrayList<Video> getExistingDataFromCache() throws IOException,
			ClassNotFoundException {
		FileInputStream fin = new FileInputStream(getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_VIIDEO);
		ObjectInputStream in = new ObjectInputStream(fin);
		ArrayList<Video> videos = (ArrayList<Video>) in.readObject();
		//in.close();
		return videos;
	}

	/**
	 * Set Data In existing Cache
	 */
	@Override
	public void setNewDataInCache(ArrayList<Video> s) throws IOException {
		FileOutputStream fout = new FileOutputStream(getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_VIIDEO);
		ObjectOutputStream out = new ObjectOutputStream(fout);
		out.writeObject(s);
		out.close();
	}

	/**
	 * Fetch data from Server
	 */
	@Override
	public ArrayList<Video> getDataFromServer(String serverUrl) {
		
		Log.i("VIDEOS","serverUrl  : "+serverUrl);
		
		// Creating JSON Parser object
	    jsonParser = new JSONParser();

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(serverUrl, "GET", params);
        
        //Log.i("VIDEOS", json.toString());
        
        //New Video List
        ArrayList<Video> videos = new ArrayList<Video>();
        
        JSONArray videosJSONArray = null;
        
        // ALL JSON node names
        final String TAG_VIDEOS_SET = "videoSet";
        final String TAG_DURATION = "duration";
        final String TAG_NAME = "name";
        final String TAG_URL = "url";
        final String TAG_NBR_PLAY = "nbrPlay";
        final String TAG_NBR_FAVORITE = "nbrFavorite";

        try {
                videosJSONArray = json.getJSONArray(TAG_VIDEOS_SET);
                
                // looping through All messages
                //Log.i("VIDEOS","array" + videosJSONArray.toString());

                for (int i = 0; i < videosJSONArray.length(); i++) {
            		JSONObject c = videosJSONArray.getJSONObject(i);
                    // Create activity Object
                    Video video = new Video(c.getString(TAG_NAME),c.getLong(TAG_DURATION),c.getString(TAG_URL),c.getInt(TAG_NBR_PLAY),c.getInt(TAG_NBR_FAVORITE));
					Log.i("VIDEOS","added Video : "+video.getVideoName()+" - "+video.getDuration()+" - "+video.getYoutubeURL());
                    videos.add(video);
                }

        } catch (JSONException e) {
                e.printStackTrace();
        }

        return videos;
	}
	
	/**
	 * Calculate Diff between the new and the old Videos List
	 * @param videosList
	 * @param newVideoList
	 * @return
	 */
	private ArrayList<Video> calculateDiff(ArrayList<Video> videosList,
			ArrayList<Video> newVideoList) {
		int countDiff = 0;
		
		//Set "tag" property to "old"
		for (Video vid : newVideoList) {
			vid.setTag("old");
			
		}
		ArrayList<Video> listeDiffVideos = new ArrayList<Video>();
		boolean founded = false;
		for (Video newvideo : newVideoList) {
			for (Video oldvideo : videosList) {
				if(oldvideo.getVideoName().equalsIgnoreCase(newvideo.getVideoName())) {					
					founded = true;
					break;
				}
			}
			
			//creer la nouvelle liste diff
			if(!founded) {	
				listeDiffVideos.add(newvideo);
			}
			
			founded = false;
		}
		
			countDiff = listeDiffVideos.size();
			Log.i("VIDEOS","countDiff = "+countDiff);

			//Set "tag" property to "new" only for new added Videos
			for (Video vid : listeDiffVideos) {
				vid.setTag("new");
				
			}
		
		return listeDiffVideos;
	}
	
	/**
	 * Set New Data count in cache
	 */
	@Override
	public void setNewDataCountInCache(int count) throws IOException {
		FileOutputStream fout = new FileOutputStream(getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_COUNT_NEW_VIDEOS);
		ObjectOutputStream out = new ObjectOutputStream(fout);
		out.writeObject(count);
		out.close();		
	}

	@Override
	public void onCreate() {
		instance=this;
		
		 ThreadVideoLoad th=new ThreadVideoLoad(instance);
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

	private class ThreadVideoLoad extends Thread {
		
		ServiceVideoUpdater svu;
		
		ThreadVideoLoad(ServiceVideoUpdater svu){
			this.svu = svu;
		}

		public void run() {

			while (true) {
				
				//Get Existing Videos from Cache 
				try {
					existingVideosList = this.svu.getExistingDataFromCache();
				} catch (IOException e1) {
					Log.e("VIDEOS","IOException cache : "+e1.getMessage());
				} catch (ClassNotFoundException e1) {
					Log.e("VIDEOS","ClassNotFoundException cache : "+e1.getMessage());
				}
				
				try {
					// Get New Data From Server
					newVideoList = this.svu.getDataFromServer(ApplicationConstants.SERVER_JSON_VIDEOS_PATH);
	                Log.i("VIDEOS","Total New Vides : "+newVideoList.size());

					// Calculate Diff for Notification
					if(!newVideoList.isEmpty()) {
						diffVidList = this.svu.calculateDiff(existingVideosList,newVideoList);
		                Log.i("VIDEOS","diffVidList size : "+diffVidList.size());
		                differenceVideoLists = diffVidList.size();

					//Set Count in cache
		            this.svu.setNewDataCountInCache(differenceVideoLists);
	                Log.i("VIDEOS","differenceVideoLists count : "+differenceVideoLists);

					// Set Data In Cache
					existingVideosList.addAll(diffVidList);
					Log.i("VIDEOS","videos To set in cache size : "+existingVideosList.size());

					this.svu.setNewDataInCache(existingVideosList);
	
					//Clear New Videos List
					newVideoList.clear();
					existingVideosList.clear();
					
					}					

				} catch (Exception e) {
					Log.e("VIDEOS","EXCEPTION : "+e.getMessage());
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
