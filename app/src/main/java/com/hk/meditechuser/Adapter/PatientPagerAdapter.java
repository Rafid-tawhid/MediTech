package com.hk.meditechuser.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hk.meditechuser.DocumentFragment;
import com.hk.meditechuser.HealthFragment;
import com.hk.meditechuser.PrescriptionFragment;
import com.hk.meditechuser.ProfileFragment;


public class PatientPagerAdapter extends FragmentPagerAdapter{
    private int tabCount;
    public PatientPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HealthFragment();
            case 1:
                return new DocumentFragment();
            case 2:
                return new PrescriptionFragment();
            case 3:
                return new ProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
