package com.audamob.audasingers.stromae.fragment;

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

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.constant.ApplicationConstants;
import com.audamob.audasingers.stromae.tool.view.ImageResizerUtils;

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
				R.layout.audamob_version_deux_activity_settings, null);
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
							.parse(ApplicationConstants.MARKET_APP_DETAILS ));
					startActivity(intent);

				} catch (Exception e) {
					Intent i = new Intent(
							Intent.ACTION_VIEW,
							Uri.parse(ApplicationConstants.MARKET_WEB_DETAILS));
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
							.parse(ApplicationConstants.MARKET_PLACE_APP));
					startActivity(intent);

				} catch (Exception e) {
					Intent i = new Intent(
							Intent.ACTION_VIEW,
							Uri.parse(ApplicationConstants.MARKET_PLACE_WEB));
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
							.parse(ApplicationConstants.AUDAMOB_TWITTER_ID));
					startActivity(intent);

				} catch (Exception e) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri
							.parse(ApplicationConstants.AUDAMOB_TWITTER_USERNAME));
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
							.parse(ApplicationConstants.AUDAMOB_FACEBOOK_ID));
					startActivity(intent);

				} catch (Exception e) {

					Intent i = new Intent(Intent.ACTION_VIEW, Uri
							.parse(ApplicationConstants.AUDAMOB_FACEBOOK_USERNAME));
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
							ApplicationConstants.FEEDBAKC_EMAIL_OBJECT);
					
					String[] tos = { ApplicationConstants.FEEDBACK_ADDRESS };

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
							Uri.parse(ApplicationConstants.MARKET_PLACE_WEB));
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