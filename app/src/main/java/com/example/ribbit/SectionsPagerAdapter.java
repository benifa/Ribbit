package com.example.ribbit;

import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
	Context bContext;

	public SectionsPagerAdapter(Context context,FragmentManager fm) {
		super(fm);
		bContext=context;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		if(position==0)
		return new InboxFragment();
		else
			return new FriendsFragment();
	}

	@Override
	public int getCount() {
		// Show 3 total pages.
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return bContext.getString(R.string.title_section1).toUpperCase(l);
		case 1:
			return bContext.getString(R.string.title_section2).toUpperCase(l);

		}
		return null;
	}
}




