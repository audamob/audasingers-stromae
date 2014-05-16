package com.audamob.audasingers.stromae.tool.adapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.activity.Videos_Interface;
import com.audamob.audasingers.stromae.constant.ApplicationConstants;
import com.audamob.audasingers.stromae.fragment.MainContainerActivityTest;
import com.audamob.audasingers.stromae.model.Video;
import com.audamob.audasingers.stromae.tool.db.CacheReadWriter;
import com.audamob.audasingers.stromae.tool.view.ImageResizerUtils;
import com.audamob.audasingers.stromae.view.services.ShareService;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.android.youtube.player.YouTubeThumbnailView.OnInitializedListener;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class VideosAdapter extends BaseAdapter {
	private SimpleDateFormat FormatterDuration = new SimpleDateFormat("mm:ss");
	private LayoutInflater mInflater;
	private HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader> loaders;
	private Activity activity;
	private ArrayList<Video> ListVideo;
	private int widthYoutubeMiniature;
	public static int SIMPLE_VIDEO_MODE = 1;
	public static int SIMPLE_FAVORITES_MODE = 2;
	private int mode;

	class ViewHolderImage {
		YouTubeThumbnailView youtubeVideoMiniature;
		TextView Name;
		TextView youtubeVideoDuration;
		TextView nbrPlay;
		TextView nbrFavorites;
		ImageView share;
		CheckBox checkbox;

		int id;
	}

	public VideosAdapter(Activity activity, ArrayList<Video> listVideos,
			int modeDisplay) {

		this.activity = activity;
		this.ListVideo = listVideos;
		mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		loaders = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
		this.mode = modeDisplay;
		widthYoutubeMiniature = getWidthYoutubeMiniature();

	}

	public int getWidthYoutubeMiniature() {

		return (ImageResizerUtils.getDisplayWidthInPx(activity) - (int) ImageResizerUtils
				.dpToPx(24, activity)); // 24 est le margin de View/2
	}

	public int getCount() {
		return ListVideo.size();
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

			convertView = mInflater.inflate(
					R.layout.audamob_version_deux_item_video_custom, null);

			Typeface font = Typeface.createFromAsset(activity.getAssets(),
					"ExoMedium.otf");

			ImageResizerUtils.setFont(activity, (ViewGroup) convertView, font);
			holder.youtubeVideoMiniature = (YouTubeThumbnailView) convertView
					.findViewById(R.id.youtubethumbnailview);

			holder.Name = (TextView) convertView.findViewById(R.id.nam);
			holder.youtubeVideoDuration = (TextView) convertView
					.findViewById(R.id.Number);

			holder.checkbox = (CheckBox) convertView
					.findViewById(R.id.checkbox);

			holder.nbrPlay = (TextView) convertView
					.findViewById(R.id.nbrPlayTextView);

			holder.nbrFavorites = (TextView) convertView
					.findViewById(R.id.nbrFavoritesTextView);

			holder.youtubeVideoMiniature.setTag(position);
			holder.youtubeVideoMiniature.initialize(
					ApplicationConstants.DEVELOPER_KEY, listener);
			holder.share = (ImageView) convertView.findViewById(R.id.partager);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderImage) convertView.getTag();

			YouTubeThumbnailLoader loader = null;
			try {
				loader = loaders.get(holder.youtubeVideoMiniature);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (loader == null) {
				// holder.youtubeVideoMiniature.setTag(position);

			} else {
				// 3) The view is already created and already initialized.
				// Simply set the right videoId
				// on the loader.
				UrlImageViewHelper.setUrlDrawable(holder.youtubeVideoMiniature,
						null, R.drawable.audamob_version_deux_bg_opacity);
				try {

					loader.setVideo(ListVideo.get(position).getYoutubeURL());

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					Log.d("YoutubeProblem", "error  " + e.getMessage());
				}
			}

		}

		
		holder.checkbox.setVisibility(View.VISIBLE);
		holder.Name.setId(position);
		holder.youtubeVideoDuration.setId(position);
		holder.checkbox.setId(position);
		holder.nbrFavorites.setId(position);
		holder.nbrPlay.setId(position);
		holder.share.setId(position);
		holder.checkbox.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mode == VideosAdapter.SIMPLE_FAVORITES_MODE) {
					CheckBox cb = (CheckBox) v;
					int id = cb.getId();
					ArrayList<Video> allListe = new ArrayList<Video>();
					try {
						allListe = CacheReadWriter.restoreListVideos(activity);
					} catch (Exception e) {
					}

					for (Video video : allListe) {

						if (video.getYoutubeURL().equalsIgnoreCase(
								ListVideo.get(id).getYoutubeURL())) {
							video.setFavorite(false);
						}
					}
					try {

						CacheReadWriter.sauvegardListVideos(allListe, activity);
						ListVideo.clear();
						ListVideo = CacheReadWriter
								.restoreFavoriteList(activity);

					} catch (Exception e) {
						// TODO: handle exception
					}

				} else {

					// TODO Auto-generated method stub
					CheckBox cb = (CheckBox) v;
					int id = cb.getId();

					if (ListVideo.get(id).isFavorite()) {
						cb.setChecked(false);
						ListVideo.get(id).setFavorite(false);
					} else {
						cb.setChecked(true);
						ListVideo.get(id).setFavorite(true);
					}
					try {
						CacheReadWriter
								.sauvegardListVideos(ListVideo, activity);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		});
		holder.share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					MainContainerActivityTest.shareService.setDismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				MainContainerActivityTest.shareService = new ShareService(
						activity, ApplicationConstants.YOUTUBE_URL
								+ ListVideo.get(position).getYoutubeURL(),
						holder.share);
			}
		});
		holder.checkbox.setChecked(ListVideo.get(position).isFavorite());

		holder.youtubeVideoMiniature.setId(position);

		/*
		 * Calculate width
		 */

		holder.youtubeVideoMiniature.getLayoutParams().height = ImageResizerUtils
				.youtubeVideoDimensions(widthYoutubeMiniature);
		holder.youtubeVideoMiniature.requestLayout();

		holder.youtubeVideoMiniature.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(activity, Videos_Interface.class);
				i.putExtra("name", ListVideo.get(position).getVideoName());
				i.putExtra("Url", ListVideo.get(position).getYoutubeURL());
				i.putExtra("duration", ListVideo.get(position).getDuration());
				activity.startActivity(i);

			}
		});

		holder.Name.setText(ListVideo.get(position).getVideoName());
		holder.youtubeVideoDuration.setText(""
				+ FormatterDuration.format(ListVideo.get(position)
						.getDuration()));
		holder.id = position;

		// Increment the play nbr ...
		Random rPlay = new Random();
		int valeurPlay = 0 + rPlay.nextInt(1000 - 0);
		holder.nbrPlay
				.setText(""
						+ Math.round((ListVideo.get(position).getNbrPlay() + valeurPlay) / 1000)
						+ "K");

		// Increment the fav nbr ...
		Random rFav = new Random();
		int valeurFav = 0 + rFav.nextInt(100 - 0);
		holder.nbrFavorites
				.setText(""
						+ Math.round((ListVideo.get(position).getNbrFavorite() + valeurFav) / 1000)
						+ "K");

		return convertView;
	}

	public void releaseLoaders() {
		for (YouTubeThumbnailLoader loader : loaders.values()) {
			loader.release();
		}
	}

	OnInitializedListener listener = new OnInitializedListener() {

		@Override
		public void onInitializationSuccess(YouTubeThumbnailView view,
				YouTubeThumbnailLoader loader) {
			// TODO Auto-generated method stub
			int videoId = (Integer) view.getTag();
			loaders.put(view, loader);

			loader.setVideo(ListVideo.get(videoId).getYoutubeURL());

		}

		@Override
		public void onInitializationFailure(YouTubeThumbnailView arg0,
				YouTubeInitializationResult arg1) {
			// TODO Auto-generated method stub

		}
	};

}
