package com.hk.meditechuser.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.hk.meditechuser.HealthFragment;
import com.hk.meditechuser.ProfileFragment;

public class GuestPagerAdapter extends FragmentPagerAdapter{
    private int tabCount;
    public GuestPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HealthFragment();
            case 1:
                return new ProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
