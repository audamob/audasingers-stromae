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

import android.app.Activity;
import android.util.Log;

import com.audamob.audasingers.constant.ApplicationConstants;
import com.audamob.audasingers.model.Version;
import com.audamob.audasingers.tool.json.JSONParser;

public class ServiceVersionChecker  {

	public static final int THREAD_TIMEOUT_SLEEP = 12 * 60 * 100 * 100; // Thread 12H Sleep
	public ArrayList<Version> existingVersions = new ArrayList<Version>();
	public ArrayList<Version> newVersions = new ArrayList<Version>();
	public int differenceVideoLists = 0;
	public boolean isUpToDate = true;
	public JSONParser jsonParser;
	ServiceVersionChecker instance;


	/**
	 * 
	 * Fetch Version from existing Cache
	 */
	public ArrayList<Version> getExistingDataFromCache(Activity activity) throws IOException,
			ClassNotFoundException {
		FileInputStream fin = new FileInputStream(activity.getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_VERSION);
		ObjectInputStream in = new ObjectInputStream(fin);
		ArrayList<Version> versions = (ArrayList<Version>) in.readObject();
		//in.close();
		return versions;
	}

	/**
	 * Set Version In existing Cache
	 */
	public void setNewDataInCache(ArrayList<Version> versions,Activity activity) throws IOException {
		FileOutputStream fout = new FileOutputStream(activity.getCacheDir()
				.getAbsolutePath() + ApplicationConstants.CACHE_VERSION);
		ObjectOutputStream out = new ObjectOutputStream(fout);
		out.writeObject(versions);
		out.close();
	}

	/**
	 * Fetch data from Server
	 */
	public ArrayList<Version> getDataFromServer(String serverUrl) {
		
		Log.i("VERSION","serverUrl  : "+serverUrl);
		
		// Creating JSON Parser object
	    jsonParser = new JSONParser();

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(serverUrl, "GET", params);
                
        //New Version List
        ArrayList<Version> versions = new ArrayList<Version>();
        
        JSONArray versionsJSONArray = null;
        
        // ALL JSON node names
        final String TAG_VERSION_SET = "versionSet";
        final String TAG_NAME = "name";
        final String TAG_ID = "id";
        final String TAG_CREATION_DATE = "creationDate";

        try {
                versionsJSONArray = json.getJSONArray(TAG_VERSION_SET);
                
                // looping through All messages
                for (int i = 0; i < versionsJSONArray.length(); i++) {
            		JSONObject c = versionsJSONArray.getJSONObject(i);
                    // Create activity Object
                    Version version = new Version(c.getString(TAG_NAME),c.getInt(TAG_ID),c.getString(TAG_CREATION_DATE),true);
					Log.i("VERSION","added version : "+version.getName()+" - "+version.getId()+" - "+version.getCreationDate());
                    versions.add(version);
                }

        } catch (JSONException e) {
                e.printStackTrace();
        }

        return versions;
	}
	
	/**
	 * Calculate Diff between the new and the old Videos List
	 * @param videosList
	 * @param newVideoList
	 * @return
	 */
	private boolean isUpTodate(ArrayList<Version> versionsList,
			ArrayList<Version> newVersionsList) {
		
		boolean isUpToDate = false;
		
		if(versionsList.get(0).getId() == newVersionsList.get(0).getId()) {
			isUpToDate = true;
		}
	
		return isUpToDate;
	}
	
	/**
	 * Initialiser la version pour la premiere install ..
	 * @return
	 */
	private ArrayList<Version> initVersion() {
		ArrayList<Version> defaultVersionsList = new ArrayList<Version>();
		Version defaultVersion = new Version("Basic",1,"10/05/2014",true);
		defaultVersionsList.add(defaultVersion);
		return defaultVersionsList;
	}

	public ServiceVersionChecker(Activity context) {
		try {
			isUpToDate = getExistingDataFromCache(context).get(0).isState();
		} catch (IOException e) {
			
		} catch (ClassNotFoundException e) {
			
		}
		
		ThreadVersionLoad dt = new ThreadVersionLoad(context);
		dt.start(); 		
	}

	private class ThreadVersionLoad extends Thread {
		
		Activity currentContext;

		
		ThreadVersionLoad(Activity context) {
			currentContext = context;
		}
		
		public void run() {

			while (true) {
				
				//Get Existing Versions from Cache 
				try {
					existingVersions = getExistingDataFromCache(currentContext);
					Log.i("VERSION","existing version in cache : "+existingVersions.get(0).getId());
				} catch (IOException e1) {
					//Si on trouve pas la version, on l'ajout dans le cache 
					try {
						setNewDataInCache(initVersion(),currentContext);
					} catch (IOException e) {
					}
					Log.e("VERSION","IOException cache : "+e1.getMessage());
				} catch (ClassNotFoundException e1) {
					Log.e("VERSION","ClassNotFoundException cache : "+e1.getMessage());
				}
				
				try {
					// Get New Data From Server
					newVersions = getDataFromServer(ApplicationConstants.SERVER_JSON_VERSION_PATH);
	                Log.i("VERSION","Total New Versions : "+newVersions.size());

					// Calculate Diff for Notification
					if(!newVersions.isEmpty() && !existingVersions.isEmpty()  ) {
						isUpToDate = isUpTodate(existingVersions,newVersions);
		                Log.i("VERSION","isUpToDate : "+isUpToDate);

		            //Update the state of version and set it in the cache
		            newVersions.get(0).setState(isUpToDate);
					setNewDataInCache(newVersions,currentContext);
	
					//Clear New Versions List
					newVersions.clear();
					existingVersions.clear();
					
					}					

				} catch (Exception e) {
					Log.e("VERSION","EXCEPTION : "+e.getMessage());
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
