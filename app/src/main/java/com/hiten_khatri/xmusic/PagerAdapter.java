package com.hiten_khatri.xmusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {
    public int num;
    public ArrayList<String> titles;
    public ArrayList<Fragment> fragments;
    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.num=behavior;
        this.titles=new ArrayList<>();
        this.fragments=new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new homeFrag();
            case 1:
                return new songFrag();
            case 2:
                return new albumFrag();
            case 3:
                return new artistFrag();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return num;
    }

    void addFragments(Fragment fragment,String title)
    {
        fragments.add(fragment);
        titles.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
