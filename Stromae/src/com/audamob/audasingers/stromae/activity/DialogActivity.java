package com.audamob.audasingers.stromae.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.constant.ApplicationConstants;
import com.audamob.audasingers.stromae.tool.view.ImageResizerUtils;

public class DialogActivity extends Activity {
	Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.audamob_version_deux_dialog);
		activity=this;
		
		ViewGroup mainContainer = (ViewGroup) findViewById(R.id.MainContainer);
		Typeface font = Typeface.createFromAsset(getAssets(), "ExoMedium.otf");

		ImageResizerUtils.setFont(this, mainContainer, font);
		Bundle b = getIntent().getExtras();

		TextView dialogMsg = (TextView) findViewById(R.id.dialogMsg);
		dialogMsg.setText("There are a new version in play store, get it now !");
		
		TextView cancel = (TextView) findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				finish();
			}
		});
		TextView OK = (TextView) findViewById(R.id.ok);
		OK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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

				finish();
			}
		});

	}
}