package com.posturn.hotplace;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MarketMainActivity extends AppCompatActivity{
    private TabLayout tab_layout;
    private ViewPager pager;
    private MarketPagerAdapter marketPagerAdapter;

    private String placeName;
    private int myplaceon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_activity_main);

        //placeName = getIntent().getStringExtra("placeName");
        placeName = "해방촌";
        myplaceon = 0;

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
        setupViewPager(pager); // new MarketPagerAdapter, pager.setAdapter(marketPagerAdapter)

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        tab_layout.setupWithViewPager(pager);
        setupTabIcons();


        toolbarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myplaceon==0) {
                    toolbarImg.setImageResource(R.drawable.ic_small_star_on_thick_grid_dirtyyellow);
                    Toast.makeText( getApplicationContext(), "My 플레이스에 추가되었습니다.", Toast.LENGTH_SHORT ).show();
                    myplaceon = 1;
                }else{
                    toolbarImg.setImageResource(R.drawable.ic_small_star_grey);
                    Toast.makeText( getApplicationContext(), "My 플레이스에서 삭제되었습니다.", Toast.LENGTH_SHORT ).show();
                    myplaceon = 0;
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 뒤로가기
                onBackPressed();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
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
