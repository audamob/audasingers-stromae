package com.LiL.Wayne.Activity.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.LiL.Wayne.Activity.R;
import com.audamob.audasingers.model.Music;
import com.audamob.audasingers.model.PlayList;
import com.audamob.audasingers.tool.db.CacheReadWriter;
import com.audamob.audasingers.tool.view.ImageResizerUtils;

public class NewPlaylist extends Activity {
	private SimpleDateFormat FormatterDuration = new SimpleDateFormat("mm:ss");
	private ArrayList<PlayList> playlistMusicList;
	private EditText newPlaylistName;
	
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.auda_layout_create_playlist);

		ViewGroup mainContainer = (ViewGroup) findViewById(R.id.MainContainer);
		Typeface font = Typeface.createFromAsset(getAssets(), "ExoMedium.otf");

		ImageResizerUtils.setFont(this, mainContainer, font);
		activity=this;
		Bundle b = getIntent().getExtras();
		playlistMusicList = new ArrayList<PlayList>();
		try {
			playlistMusicList = CacheReadWriter.restorePlayListList(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newPlaylistName = (EditText) findViewById(R.id.newPlaylist);
		RelativeLayout Done = (RelativeLayout) findViewById(R.id.Done);
		Done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(newPlaylistName.getWindowToken(), 0);
				String playlistname = newPlaylistName.getText().toString();
				if (playlistname.length() > 0) {
					boolean existe = false;
					for (int i = 0; i < playlistMusicList.size() && !existe; i++) {
						existe=isExiste(playlistname);
					}
					if (!existe) {
						playlistMusicList.add(new PlayList(playlistname,new ArrayList<Music>()));
					}
				} else {
					if (!isExiste("Default PlayList")) {
						playlistMusicList.add(new PlayList("Default PlayList",
								new ArrayList<Music>()));
					}
				}
				try {
					CacheReadWriter.sauvegardPlayListList(playlistMusicList,
							activity);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finish();
			}
		});

	}
	private boolean isExiste(String stringTocheck){
		boolean existe = false;
		for (int i = 0; i < playlistMusicList.size() && !existe; i++) {
			if (playlistMusicList.get(i).getName()
					.equalsIgnoreCase(stringTocheck)) {
				existe = true;
				break;
			}

		}
		return existe;
	}


}