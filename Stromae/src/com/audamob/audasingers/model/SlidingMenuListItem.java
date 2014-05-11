package com.audamob.audasingers.model;

import android.graphics.drawable.Drawable;

public class SlidingMenuListItem {
	public int Id;
	public String Name;
	public Drawable IconResourceId;
	

	public SlidingMenuListItem() {
	}

	public SlidingMenuListItem(int id, String name, Drawable iconResourceId) {
		this.Id = id;
		this.Name = name;
		this.IconResourceId = iconResourceId;
		
	}
}
