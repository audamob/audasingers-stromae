package com.audamob.audasingers.stromae.tool.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ImageResizerUtils {
	
	static double YOUTUBE_RAPPORT_HEIGHT_WIDTH=0.5630;
	
	public static int youtubeVideoDimensions(int width) {
		return (int) (width * YOUTUBE_RAPPORT_HEIGHT_WIDTH) ;
	}
	public static void setFont(Context context, ViewGroup group, Typeface font) 
    {
        int count = group.getChildCount();
        View v = null;
        for(int i = 0; i < count; i++) 
        {
            v = group.getChildAt(i);
            if(v instanceof TextView || v instanceof Button || v instanceof EditText/*etc.*/)
                ((TextView)group.getChildAt(i)).setTypeface(font);
            else if(v instanceof ViewGroup)
                setFont(context, (ViewGroup)v, font);
        }

      
    }
	public static int dpToPx(int dp,Context context) {
	    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
	
	public static int getDisplayHightInPx(Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	   
	    return metrics.heightPixels;
	}
	
	public static int getDisplayWidthInPx( Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	   
	    return metrics.widthPixels;
	}
	

}



