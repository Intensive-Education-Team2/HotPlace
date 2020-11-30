package com.posturn.hotplace;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class AMarketPagerAdapter extends FragmentPagerAdapter {
    public String placeName;

    ArrayList<Fragment> fragments = new ArrayList<>();

    public AMarketPagerAdapter(@NonNull FragmentManager fm, String placeName) {
        super(fm);
        this.placeName = placeName;
        fragments.add(new MarketFragment("# 실시간 전체 점포 정보",this.placeName,"all"));
        fragments.add(new MarketFragment("# 실시간 음식 점포 정보",this.placeName,"res"));
        fragments.add(new MarketFragment("# 실시간 카페 점포 정보",this.placeName,"cafe"));
        fragments.add(new MarketFragment("# 실시간 술집 점포 정보",this.placeName,"bar"));
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
