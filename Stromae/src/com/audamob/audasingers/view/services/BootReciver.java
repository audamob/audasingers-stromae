package com.audamob.audasingers.view.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Simple receiver that will handle the boot completed intent and send the intent to 
 * launch the BootDemoService.
 * @author BMB
 *
 */
public class BootReciver extends BroadcastReceiver {
 @Override
 public void onReceive(final Context context, final Intent bootintent) {
	 Log.d("Alarm","BootComplited");
   
   // Adding News Service ...
   Intent mServiceNewsIntent = new Intent();
   mServiceNewsIntent.setAction("ServiceNewsUpdater");
   context.startService(mServiceNewsIntent);
  
   // Adding video Updater Service ...
   Intent mServiceVideoUpdaterIntent = new Intent();
   mServiceVideoUpdaterIntent.setAction("ServiceVideoUpdater");
   context.startService(mServiceVideoUpdaterIntent);
  
   // Adding Music Updater Service ...
   Intent mServiceMusicUpdaterIntent = new Intent();
   mServiceMusicUpdaterIntent.setAction("ServiceMusicUpdater");
   context.startService(mServiceMusicUpdaterIntent);
  
 }
}