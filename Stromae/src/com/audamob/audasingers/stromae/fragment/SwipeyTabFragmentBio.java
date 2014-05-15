package com.audamob.audasingers.stromae.fragment;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.audamob.audasingers.stromae.R;
import com.audamob.audasingers.stromae.model.News;
import com.audamob.audasingers.stromae.tool.view.ImageResizerUtils;
public class SwipeyTabFragmentBio extends Fragment {
	private SimpleDateFormat FormatterDuration = new SimpleDateFormat("mm:ss");
	ArrayList<News> LyricList;
	EditText SearchEdit,SearchEditAlbum;
	
	
	
	    public static Fragment newInstance(String title) {
                SwipeyTabFragmentBio f = new SwipeyTabFragmentBio();
                Bundle args = new Bundle();
                args.putString("title", title);
                f.setArguments(args);
                
                return f;
        }
	    
       
        ListView ListVideo;
        
    	@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

                ViewGroup root = (ViewGroup) inflater.inflate(R.layout.audamob_version_deux_activity_bio, null);
                final String title = getArguments().getString("title");
                ViewGroup mainContainer=(ViewGroup)root.findViewById(R.id.MainContainer);
        		Typeface font =Typeface.createFromAsset(getActivity().getAssets(), "ExoMedium.otf");

        		ImageResizerUtils.setFont(getActivity(), mainContainer, font);
            	return root;
           
        	
        }
    	 
    	
    	  
   	 
       

           	
}