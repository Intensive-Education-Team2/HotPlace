package com.posturn.hotplace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class MarketPagerAdapter extends FragmentStatePagerAdapter {

    public String placeName;

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> pageTitles = new ArrayList<>(Arrays.asList("전체","음식", "카페", "술집"));

    public MarketPagerAdapter(@NonNull FragmentManager fm, String placeName) {
        super(fm);
        this.placeName = placeName;
        fragments.add(new MarketFragment(this.placeName,"all"));
        fragments.add(new MarketFragment(this.placeName,"res"));
        fragments.add(new MarketFragment(this.placeName,"cafe"));
        fragments.add(new MarketFragment(this.placeName,"bar"));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
