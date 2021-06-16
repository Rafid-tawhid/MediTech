package com.hk.meditechauthenticate.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hk.meditechauthenticate.DocumentFragment;
import com.hk.meditechauthenticate.HealthFragment;
import com.hk.meditechauthenticate.PrescriptionFragment;
import com.hk.meditechauthenticate.ProfileFragment;


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
