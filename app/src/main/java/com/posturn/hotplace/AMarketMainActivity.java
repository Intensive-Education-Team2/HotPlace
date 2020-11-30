package com.posturn.hotplace;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AMarketMainActivity extends AppCompatActivity {
    private TabLayout tab_layout;
    private ViewPager2 pager;
    private AMarketFragmentAdapter marketFragmentAdapter;

    private String placeName;
    private int myplaceon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_market_activity_main);

        //placeName = getIntent().getStringExtra("placeName");
        placeName = "해방촌";
        myplaceon = 0;

        Toolbar toolbar = (Toolbar) findViewById(R.id.a_toolbar_market);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.a_toolbar_place_name_marketinfo);
        toolbarTitle.setText(placeName);
        ImageView toolbarImg = (ImageView) findViewById(R.id.a_my_favorite);
        toolbarImg.setImageResource(R.drawable.ic_small_star_grey);

        pager = (ViewPager2) findViewById(R.id.a_pager); //ViewPager pager
        setupViewPager(pager); // new MarketPagerAdapter, pager.setAdapter(marketPagerAdapter)

        tab_layout = (TabLayout) findViewById(R.id.a_tab_layout); // TabLayout tab_layout
        //tab_layout.setupWithViewPager(pager); // tab_layout 기능
        new TabLayoutMediator(tab_layout,pager,(tab, position) -> tab.setText("Ob" + (position + 1) )).attach();
        //setupTabIcons();
    }

    private void setupViewPager(ViewPager2 viewPager){
        //AMarketFragmentAdapter mkfragAdapter = new AMarketFragmentAdapter();

        //pager.setAdapter(mkfragAdapter); // Viewpager 기능
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


}
