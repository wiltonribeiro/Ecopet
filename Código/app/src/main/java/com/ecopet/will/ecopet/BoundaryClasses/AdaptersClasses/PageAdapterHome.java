package com.ecopet.will.ecopet.BoundaryClasses.AdaptersClasses;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses.FeedFragment;
import com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses.RankingFragment;

/**
 * Created by willrcneto on 15/03/18.
 */

public class PageAdapterHome extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapterHome(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new FeedFragment();
            case 1:
                return new RankingFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}

