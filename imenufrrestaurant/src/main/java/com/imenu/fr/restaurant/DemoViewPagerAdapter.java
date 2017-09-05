package com.imenu.fr.restaurant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.imenu.fr.restaurant.fragment.AcceptedFragment;
import com.imenu.fr.restaurant.fragment.PendingFragment;

import java.util.ArrayList;

/**
 *
 */
public class DemoViewPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> fragments = new ArrayList<>();
	private Fragment currentFragment;

	public DemoViewPagerAdapter(FragmentManager fm) {
		super(fm);

		fragments.clear();
		fragments.add(PendingFragment.newInstance());
		fragments.add(AcceptedFragment.newInstance());
		fragments.add(ExtraFragment.newInstance(2));
		fragments.add(ExtraFragment.newInstance(3));
		fragments.add(ExtraFragment.newInstance(4));
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		if (getCurrentFragment() != object) {
			currentFragment = ((Fragment) object);
		}
		super.setPrimaryItem(container, position, object);
	}

	/**
	 * Get the current fragment
	 */
	public Fragment getCurrentFragment() {
		return currentFragment;
	}
}