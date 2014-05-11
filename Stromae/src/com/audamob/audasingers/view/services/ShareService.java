package com.audamob.audasingers.view.services;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.LiL.Wayne.Activity.R;
import com.audamob.audasingers.model.SharePm;
import com.audamob.audasingers.tool.adapter.ShareAdapter;

public class ShareService {
	private Activity activity;
	private String textToshare;
	private Boolean isVisible = false;
	private PopupWindow popUpShare;
	private View shareView;
	private ShareService shareService;
	
	
	public ShareService(Activity activity,String textToSHare,View shareView) {
		this.shareView=shareView;
		this.activity=activity;
		this.textToshare=textToSHare;
		// TODO Auto-generated constructor stub
		this.isVisible = true;
		this.shareService=null;
		CreateListe();
	}
	public boolean getIsVisible(){
		return isVisible;
	}
	public void setDismiss(){
		isVisible=false;
		popUpShare.dismiss();
		this.shareService=null;
	}
	public void CreateListe(){
		LayoutInflater vi = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = vi.inflate(R.layout.dialog_options_share, null);
		popUpShare = new PopupWindow(v, 700, 1000);
		popUpShare.setOutsideTouchable(false);
		popUpShare.showAsDropDown(shareView);
		
		ArrayList<SharePm> ListSHare = new ArrayList<SharePm>();
		ListView lv = (ListView) v.findViewById(R.id.ListShare);
		String mimeType = null;
		mimeType = "text/plain";
		Intent shareIntent = new Intent(
				android.content.Intent.ACTION_SEND);
		shareIntent.setType(mimeType);

		shareIntent.putExtra(Intent.EXTRA_TEXT, "");
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		final PackageManager pm = v.getContext()
				.getPackageManager();
		final List<ResolveInfo> activityList = pm
				.queryIntentActivities(shareIntent, 0);
		for (final ResolveInfo app : activityList) {
			SharePm sp = new SharePm();
			sp.d = app.loadIcon(pm);
			sp.Name = app.loadLabel(pm).toString();
			sp.ResolveInfo = app;
			ListSHare.add(sp);
			Log.d("ShareShare", " " + app.loadLabel(pm).toString());
		}
		
		String nom = "aymennnnn";// ImageList.get(currentIndex).getNewLocation();
		String Nomold = "zvzdv";// ImageList.get(currentIndex).getOldLocation();
		lv.setAdapter(new ShareAdapter(ListSHare,this.activity,this.textToshare));
	}
	
	

}
