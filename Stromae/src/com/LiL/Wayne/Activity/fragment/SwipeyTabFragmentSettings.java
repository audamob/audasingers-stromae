package com.LiL.Wayne.Activity.fragment;

import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.LiL.Wayne.Activity.R;
import com.audamob.audasingers.tool.view.ImageResizerUtils;

public class SwipeyTabFragmentSettings extends Fragment {
	private SimpleDateFormat FormatterDuration = new SimpleDateFormat("mm:ss");
	static SwipeyTabFragmentSettings f;
	private Activity activity;

	public static Fragment newInstance(String title) {
		f = new SwipeyTabFragmentSettings();
		Bundle args = new Bundle();
		args.putString("title", title);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
	}

	ListView ListVideo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.auda_layout_activity_settings, null);
		activity = getActivity();
		ViewGroup mainContainer = (ViewGroup) root
				.findViewById(R.id.MainContainer);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"ExoMedium.otf");

		ImageResizerUtils.setFont(getActivity(), mainContainer, font);

		final String title = getArguments().getString("title");

		RelativeLayout Vote = (RelativeLayout) root.findViewById(R.id.VOTE);
		Vote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse("market://details?id=com.LiL.Wayne.Activity" ));
					startActivity(intent);

				} catch (Exception e) {
					Intent i = new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("https://play.google.com/store/apps/details?id=com.LiL.Wayne.Activity"));
					activity.startActivity(i);
				}
			}
		});
		RelativeLayout DONNATE = (RelativeLayout) root
				.findViewById(R.id.DONNATE);
		DONNATE.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
				

				} catch (Exception e) {
					// TODO: handle exception
				}
				
				try {

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse("market://details?id=com.LiL.Wayne.ActivityDonateVersion" ));
					startActivity(intent);

				} catch (Exception e) {
					Intent i = new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("https://play.google.com/store/apps/details?id=com.LiL.Wayne.ActivityDonateVersion"));
					activity.startActivity(i);
				}

			}
		});
		RelativeLayout twitter = (RelativeLayout) root
				.findViewById(R.id.TWITTER);
		twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			

				try {

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse("twitter://user?user_id=2486539399"));
					startActivity(intent);

				} catch (Exception e) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri
							.parse("https://twitter.com/audamob"));
					activity.startActivity(i);
				}
			}
		});
		RelativeLayout facebook = (RelativeLayout) root
				.findViewById(R.id.FACEBOOK);
		facebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse("fb://profile/" + "411481418918147"));
					startActivity(intent);

				} catch (Exception e) {

					Intent i = new Intent(Intent.ACTION_VIEW, Uri
							.parse("https://www.facebook.com/com.audamob"));
					activity.startActivity(i);
				}

				// TODO Auto-generated method stub

			}
		});
		RelativeLayout Contact = (RelativeLayout) root
				.findViewById(R.id.CONTACT);
		Contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("GmailIntent"," exception : ");
				try {
					final Intent intent = new Intent(
							android.content.Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_SUBJECT,
							"Lil Wayne Application : feedback");
					String[] tos = { "lil.wayne@audamob.com" };

					intent.putExtra(Intent.EXTRA_EMAIL, tos);
					
					
					final PackageManager pm =activity
							.getPackageManager();
					final List<ResolveInfo> matches = pm.queryIntentActivities(
							intent, 0);
					ResolveInfo best = null;
					Log.e("GmailIntent"," exception : ");
					for (final ResolveInfo info : matches){
						if (info.activityInfo.packageName.endsWith(".gm")
								|| info.activityInfo.name.toLowerCase()
										.contains("gmail")){
							best = info;
						}
						Log.e("GmailIntent"," exception : "+info);
					}
					if (best != null)
						intent.setClassName(best.activityInfo.packageName,
								best.activityInfo.name);
					activity.startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
					Log.d("GmailIntent"," exception : "+e.getMessage());
				}
			}
		});

		RelativeLayout MarketPlace = (RelativeLayout) root
				.findViewById(R.id.MARKETPLACE);
		MarketPlace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent i = new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("https://play.google.com/store/apps/developer?id=TheATeam"));
					activity.startActivity(i);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		RelativeLayout Version = (RelativeLayout) root
				.findViewById(R.id.VERSIONS);
		Version.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		return root;

	}

}