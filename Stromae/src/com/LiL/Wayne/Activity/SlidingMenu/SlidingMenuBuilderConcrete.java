package com.LiL.Wayne.Activity.SlidingMenu;

import android.content.Intent;

import com.LiL.Wayne.Activity.R;
import com.audamob.audasingers.model.SlidingMenuListItem;

/**
 * @author Andrius Baruckis http://www.baruckis.com
 * 
 *         This is concrete builder class, which extends base builder and can
 *         override default, add new list items click actions.
 * 
 */
public class SlidingMenuBuilderConcrete extends SlidingMenuBuilderBase {

	// We can define actions, which will be called, when we press on separate
	// list items. These actions can override default actions defined inside the
	// base builder. Also, you can create new actions, which will added to the
	// default ones.
	@Override
	public void onListItemClick(SlidingMenuListItem selectedSlidingMenuListItem) {
		
		switch (selectedSlidingMenuListItem.Id) {
		case 0:
			menu.toggle();
			return;

		case 2:
			menu.toggle();
		
			break;

		default:
			break;
		}
	}

}
