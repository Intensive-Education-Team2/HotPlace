package com.posturn.hotplace;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MarketMainActivity extends AppCompatActivity{
    private TabLayout tab_layout;
    private ViewPager pager;
    private MarketPagerAdapter marketPagerAdapter;

    private String placeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_activity_main);

        placeName = getIntent().getStringExtra("placeName");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_market);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_place_name_marketinfo);
        toolbarTitle.setText(placeName);
        ImageView toolbarImg = (ImageView) findViewById(R.id.my_favorite);
        toolbarImg.setImageResource(R.drawable.ic_small_star_grey);

        pager = (ViewPager) findViewById(R.id.pager);
        //marketPagerAdapter = new MarketPagerAdapter(getSupportFragmentManager());
        //pager.setAdapter(marketPagerAdapter);
        setupViewPager(pager);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        //tab_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));


        tab_layout.setupWithViewPager(pager);

        setupTabIcons();

    }

    private void setupTabIcons(){

        View viewFirst = getLayoutInflater().inflate(R.layout.market_tab,null);
        ImageView imgfirst = viewFirst.findViewById(R.id.market_tab_img);
        TextView textfirst = viewFirst.findViewById(R.id.market_tab_text);
        imgfirst.setImageResource(R.drawable.category_all_black_18dp);
        textfirst.setText("전체");
        tab_layout.getTabAt(0).setCustomView(viewFirst);

        View viewSecond = getLayoutInflater().inflate(R.layout.market_tab,null);
        ImageView imgSecond = viewSecond.findViewById(R.id.market_tab_img);
        TextView textSecond = viewSecond.findViewById(R.id.market_tab_text);
        imgSecond.setImageResource(R.drawable.category_res_black_18dp);
        textSecond.setText("식당");
        tab_layout.getTabAt(1).setCustomView(viewSecond);

        View viewThird = getLayoutInflater().inflate(R.layout.market_tab,null);
        ImageView imgThird = viewThird.findViewById(R.id.market_tab_img);
        TextView textThird = viewThird.findViewById(R.id.market_tab_text);
        imgThird.setImageResource(R.drawable.category_cafe_black_18dp);
        textThird.setText("카페");
        tab_layout.getTabAt(2).setCustomView(viewThird);

        View viewFourth = getLayoutInflater().inflate(R.layout.market_tab,null);
        ImageView imgFourth = viewFourth.findViewById(R.id.market_tab_img);
        TextView textFourth = viewFourth.findViewById(R.id.market_tab_text);
        imgFourth.setImageResource(R.drawable.category_bar_black_18dp);
        textFourth.setText("술집");
        tab_layout.getTabAt(3).setCustomView(viewFourth);

    }

    private void setupViewPager(ViewPager viewPager){
        marketPagerAdapter = new MarketPagerAdapter(getSupportFragmentManager(),placeName);
        pager.setAdapter(marketPagerAdapter);
    }
}
