package com.audamob.audasingers.stromae.SlidingMenu;

import java.util.List;

import android.app.ListFragment;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.constant.ApplicationConstants;
import com.audamob.audasingers.stromae.fragment.MainContainerActivityTest;
import com.audamob.audasingers.stromae.model.SlidingMenuListItem;
import com.audamob.audasingers.stromae.tool.db.CacheReadWriter;
import com.audamob.audasingers.stromae.tool.view.ImageResizerUtils;
import com.audamob.audasingers.stromae.tool.view.RoundedAvatarDrawable;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * @author Andrius Baruckis http://www.baruckis.com
 * 
 *         List fragment, which will be used as a content for sliding out menu.
 * 
 */
public class SlidingMenuListFragment extends ListFragment implements
		OnClickListener {
	protected List<SlidingMenuListItem> slidingMenuList;
	private SlidingMenuBuilderBase slidingMenuBuilderBase;
	SlidingMenu menu;
     
	public SlidingMenuListFragment(SlidingMenu menu) {
		// TODO Auto-generated constructor stub
		this.menu = menu;
	}

	public void setMenuBuilder(SlidingMenuBuilderBase slidingMenuBuilderBase) {
		this.slidingMenuBuilderBase = slidingMenuBuilderBase;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// We set here a custom layout which uses holo light theme colors.
		View SlidingMenuLayout = inflater.inflate(R.layout.audamob_version_deux_fragment_sliding_menu,
				null);

		return SlidingMenuLayout;

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ViewGroup mainContainer=(ViewGroup)getActivity().findViewById(R.id.MainContainer2);
		Typeface font =Typeface.createFromAsset(getActivity().getAssets(), "ExoMedium.otf");

		ImageResizerUtils.setFont(getActivity(), mainContainer, font);
		
		
		Drawable rAvatar = new RoundedAvatarDrawable(
				BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.logo_audamob_version_deux_singer_tweet));
		ImageView profile_image=(ImageView)getActivity().findViewById(R.id.profile_image);
		profile_image.setImageDrawable(rAvatar);
		RelativeLayout MenuHeaderLayout =(RelativeLayout)getActivity().findViewById(R.id.MenuHeaderLayout);
		MenuHeaderLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UpdateUiSelector();
				
				MainContainerActivityTest.ChangeCurrentFragment(MainContainerActivityTest.ITEM_BIO);
				menu.toggle();
			}
		});
		UI_CreateMenuListView();
	}

	
	public void UI_CreateMenuListView() {
		RelativeLayout ItemMenu_1,ItemMenu_11, ItemMenu_2,ItemMenu_22, ItemMenu_3, ItemMenu_4,ItemMenu_6,ItemMenu_7;
		ItemMenu_1 = (RelativeLayout) getActivity().findViewById(
				R.id.ItemMenu_1);
		ItemMenu_11 = (RelativeLayout) getActivity().findViewById(
				R.id.ItemMenu_11);
		ItemMenu_2 = (RelativeLayout) getActivity().findViewById(
				R.id.ItemMenu_2);
		ItemMenu_22 = (RelativeLayout) getActivity().findViewById(
				R.id.ItemMenu_22);
		ItemMenu_3 = (RelativeLayout) getActivity().findViewById(
				R.id.ItemMenu_3);
		ItemMenu_4 = (RelativeLayout) getActivity().findViewById(
				R.id.ItemMenu_4);
	
		
		ItemMenu_7 = (RelativeLayout) getActivity().findViewById(
				R.id.ItemMenu_7);
		ItemMenu_1.setOnClickListener(this);
		ItemMenu_11.setOnClickListener(this);
		ItemMenu_2.setOnClickListener(this);
		ItemMenu_22.setOnClickListener(this);
		
		ItemMenu_3.setOnClickListener(this);
		ItemMenu_4.setOnClickListener(this);
		
	
		ItemMenu_7.setOnClickListener(this);
		


		//Show the Videos or music Notifications On Select Item
		try {
			Log.d("SLIDING MENU","video count notification : "+CacheReadWriter.restore_count_notification(this.getActivity(),ApplicationConstants.CACHE_COUNT_NEW_VIDEOS));
			Log.d("SLIDING MENU","Music count notification : "+CacheReadWriter.restore_count_notification(this.getActivity(),ApplicationConstants.CACHE_COUNT_NEW_MUSICS));

			if(CacheReadWriter.restore_count_notification(this.getActivity(),ApplicationConstants.CACHE_COUNT_NEW_VIDEOS) > 0)  {
				TextView video_notification = (TextView)getActivity().findViewById(R.id.row_title_1_notification);
				video_notification.setText("+ "+CacheReadWriter.restore_count_notification(this.getActivity(),ApplicationConstants.CACHE_COUNT_NEW_VIDEOS) );
				video_notification.setVisibility(View.VISIBLE);
				
			}
			
			if(CacheReadWriter.restore_count_notification(this.getActivity(),ApplicationConstants.CACHE_COUNT_NEW_MUSICS) > 0)  {
				TextView music_notification = (TextView)getActivity().findViewById(R.id.row_title_2_notification);
				music_notification.setText("+ "+CacheReadWriter.restore_count_notification(this.getActivity(),ApplicationConstants.CACHE_COUNT_NEW_MUSICS) );
				music_notification.setVisibility(View.VISIBLE);			
			} 
		} catch (Exception e) {
			Log.e("SLIDING MENU","Exception restore count notification : "+e);
		}

	}

	@Override
	public void onClick(View v) {

		UpdateUiSelector();
		updateItemOnSelect();
		v.setBackground(getActivity().getResources().getDrawable(R.drawable.layout_audamob_version_deux_transparent_color));
		
		switch (v.getId()) {
		case R.id.ItemMenu_1:
			ActifSelector(R.id.selector_1,R.id.seperator_top_item_1,R.id.seperator_bottom_item_1);
			MainContainerActivityTest.ChangeCurrentFragment(MainContainerActivityTest.ITEM_VIDEOS);
			//Hide the Videos Notifications On Select Item
			getActivity().findViewById(R.id.row_title_1_notification).setVisibility(View.INVISIBLE);
			menu.toggle();
			return;
		case R.id.ItemMenu_11:
			ActifSelector(R.id.selector_11,R.id.seperator_top_item_11,R.id.seperator_bottom_item_11);
			MainContainerActivityTest.ChangeCurrentFragment(MainContainerActivityTest.ITEM_VIDEOSFAVORITES);
			menu.toggle();
			return;

		case R.id.ItemMenu_2:
			ActifSelector(R.id.selector_2,R.id.seperator_top_item_2,R.id.seperator_bottom_item_2);
			MainContainerActivityTest.ChangeCurrentFragment(MainContainerActivityTest.ITEM_MUSICS);
			//Hide the Musics Notifications On Select Item
			getActivity().findViewById(R.id.row_title_2_notification).setVisibility(View.INVISIBLE);
			menu.toggle();
			break;
		case R.id.ItemMenu_22:
			ActifSelector(R.id.selector_22,R.id.seperator_top_item_22,R.id.seperator_bottom_item_22);
			MainContainerActivityTest.ChangeCurrentFragment(MainContainerActivityTest.ITEM_PLAYLIST);
			menu.toggle();
			break;	
		case R.id.ItemMenu_3:
			ActifSelector(R.id.selector_3,R.id.seperator_top_item_3,R.id.seperator_bottom_item_3);
			MainContainerActivityTest.ChangeCurrentFragment(MainContainerActivityTest.ITEM_LYRICS);
			menu.toggle();
			return;

		case R.id.ItemMenu_4:
			ActifSelector(R.id.selector_4,R.id.seperator_top_item_4,R.id.seperator_bottom_item_4);
			MainContainerActivityTest.ChangeCurrentFragment(MainContainerActivityTest.ITEM_NEWS);
			menu.toggle();
			return;

		case R.id.ItemMenu_7:
			ActifSelector(R.id.selector_7,R.id.seperator_top_item_7,R.id.seperator_bottom_item_7);
			MainContainerActivityTest.ChangeCurrentFragment(MainContainerActivityTest.ITEM_SETTINGS);
			menu.toggle();
			return;
			
		default:
			break;
		}
	}

	public void ActifSelector(int Rid,int sepTop,int sepBottom) {
		getActivity().findViewById(Rid).setVisibility(View.VISIBLE);
		getActivity().findViewById(sepTop).setVisibility(View.VISIBLE);
		getActivity().findViewById(sepBottom).setVisibility(View.VISIBLE);
	}

	
	public void updateItemOnSelect() {
		RelativeLayout ItemMenu_1,ItemMenu_11, ItemMenu_2,ItemMenu_22, ItemMenu_3, ItemMenu_4,ItemMenu_7;
		ItemMenu_1 = (RelativeLayout) getActivity().findViewById(
				R.id.ItemMenu_1);
		ItemMenu_11 = (RelativeLayout) getActivity().findViewById(
				R.id.ItemMenu_11);
		ItemMenu_2 = (RelativeLayout) getActivity().findViewById(
				R.id.ItemMenu_2);
		ItemMenu_22 = (RelativeLayout) getActivity().findViewById(
				R.id.ItemMenu_22);
		ItemMenu_3 = (RelativeLayout) getActivity().findViewById(
				R.id.ItemMenu_3);
		ItemMenu_4 = (RelativeLayout) getActivity().findViewById(
				R.id.ItemMenu_4);
		ItemMenu_7 = (RelativeLayout) getActivity().findViewById(
				R.id.ItemMenu_7);
		
		ItemMenu_1.setBackground(getActivity().getResources().getDrawable(R.drawable.audamob_version_deux_bg_opacity));
		ItemMenu_11.setBackground(getActivity().getResources().getDrawable(R.drawable.audamob_version_deux_bg_opacity));
		ItemMenu_2.setBackground(getActivity().getResources().getDrawable(R.drawable.audamob_version_deux_bg_opacity));
		ItemMenu_22.setBackground(getActivity().getResources().getDrawable(R.drawable.audamob_version_deux_bg_opacity));	
		ItemMenu_3.setBackground(getActivity().getResources().getDrawable(R.drawable.audamob_version_deux_bg_opacity));
		ItemMenu_4.setBackground(getActivity().getResources().getDrawable(R.drawable.audamob_version_deux_bg_opacity));
		ItemMenu_7.setBackground(getActivity().getResources().getDrawable(R.drawable.audamob_version_deux_bg_opacity));
		
	}
	
	public void UpdateUiSelector() {
		RelativeLayout s1,s11, s2,s22, s3, s4,s7,s1SepTop,s1SepBottom,s11SepTop,s11SepBottom,s2SepTop,s2SepBottom,s22SepTop,s22SepBottom,s3SepTop,s3SepBottom,s4SepTop,s4SepBottom,s7SepTop,s7SepBottom;
		s1 = (RelativeLayout) getActivity().findViewById(R.id.selector_1);
		s11 = (RelativeLayout) getActivity().findViewById(R.id.selector_11);
		s2 = (RelativeLayout) getActivity().findViewById(R.id.selector_2);
		s22 = (RelativeLayout) getActivity().findViewById(R.id.selector_22);
		s3 = (RelativeLayout) getActivity().findViewById(R.id.selector_3);
		s4 = (RelativeLayout) getActivity().findViewById(R.id.selector_4);
		s7 = (RelativeLayout) getActivity().findViewById(R.id.selector_7);
		
		s1SepTop = (RelativeLayout) getActivity().findViewById(R.id.seperator_top_item_1);
		s1SepBottom = (RelativeLayout) getActivity().findViewById(R.id.seperator_bottom_item_1);
		s11SepTop = (RelativeLayout) getActivity().findViewById(R.id.seperator_top_item_11);
		s11SepBottom = (RelativeLayout) getActivity().findViewById(R.id.seperator_bottom_item_11);
		s2SepTop = (RelativeLayout) getActivity().findViewById(R.id.seperator_top_item_2);
		s2SepBottom = (RelativeLayout) getActivity().findViewById(R.id.seperator_bottom_item_2);
		s22SepTop = (RelativeLayout) getActivity().findViewById(R.id.seperator_top_item_22);
		s22SepBottom = (RelativeLayout) getActivity().findViewById(R.id.seperator_bottom_item_22);
		s3SepTop = (RelativeLayout) getActivity().findViewById(R.id.seperator_top_item_3);
		s3SepBottom = (RelativeLayout) getActivity().findViewById(R.id.seperator_bottom_item_3);
		s4SepTop = (RelativeLayout) getActivity().findViewById(R.id.seperator_top_item_4);
		s4SepBottom = (RelativeLayout) getActivity().findViewById(R.id.seperator_bottom_item_4);
		s7SepTop = (RelativeLayout) getActivity().findViewById(R.id.seperator_top_item_7);
		s7SepBottom = (RelativeLayout) getActivity().findViewById(R.id.seperator_bottom_item_7);


		s1.setVisibility(View.INVISIBLE);
		s11.setVisibility(View.INVISIBLE);
		s2.setVisibility(View.INVISIBLE);
		s22.setVisibility(View.INVISIBLE);
		s3.setVisibility(View.INVISIBLE);
		s4.setVisibility(View.INVISIBLE);
		s7.setVisibility(View.INVISIBLE);
		
		s1SepTop.setVisibility(View.INVISIBLE);
		s1SepBottom.setVisibility(View.INVISIBLE);
		s11SepTop.setVisibility(View.INVISIBLE);
		s11SepBottom.setVisibility(View.INVISIBLE);
		s2SepTop.setVisibility(View.INVISIBLE);
		s2SepBottom.setVisibility(View.INVISIBLE);
		s22SepTop.setVisibility(View.INVISIBLE);
		s22SepBottom.setVisibility(View.INVISIBLE);
		s3SepTop.setVisibility(View.INVISIBLE);
		s3SepBottom.setVisibility(View.INVISIBLE);
		s4SepTop.setVisibility(View.INVISIBLE);
		s4SepBottom.setVisibility(View.INVISIBLE);
		s7SepTop.setVisibility(View.INVISIBLE);
		s7SepBottom.setVisibility(View.INVISIBLE);

	}

	

	

}
