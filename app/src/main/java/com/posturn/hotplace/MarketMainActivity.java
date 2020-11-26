package com.posturn.hotplace;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MarketMainActivity extends AppCompatActivity{
    private TabLayout tab_layout;
    private ViewPager pager;
    private MarketPagerAdapter marketPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ab_activity_main);

        pager = (ViewPager) findViewById(R.id.pager);
        marketPagerAdapter = new MarketPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(marketPagerAdapter);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tab_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        tab_layout.setupWithViewPager(pager);
    }
}
