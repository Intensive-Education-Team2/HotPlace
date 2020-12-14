package com.posturn.hotplace;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MarketMainActivity extends AppCompatActivity{
    private TabLayout tab_layout;
    private ViewPager pager;
    private MarketPagerAdapter marketPagerAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences pref;
    private ObjectMyplace obmyplace;
    private int IsMyplace = 0;

    private View viewFirst;
    private View viewSecond;
    private View viewThird;
    private View viewFourth;

    private String placeName;
    private int myplaceon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_activity_main);

        placeName = getIntent().getStringExtra("placeName");
        //placeName = "해방촌";
        myplaceon = 0;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(placeName);

        pager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(pager); // new MarketPagerAdapter, pager.setAdapter(marketPagerAdapter)

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //temp.setTextColor(Color.parseColor(black));
                pager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        tab_layout.setupWithViewPager(pager);
        setupTabIcons();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        pref = getSharedPreferences("profile", MODE_PRIVATE);
        String token = pref.getLong("token",0)+"";

        db.collection("MyPlace").document(token).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {
                obmyplace = new ObjectMyplace();
                obmyplace.myplacelist = (ArrayList)ds.get("myplacelist");
                for(int i=0; i<obmyplace.myplacelist.size();i++){
                    if(obmyplace.myplacelist.get(i).equals(placeName)){
                        IsMyplace = 1;
                    }
                }
                if(IsMyplace == 1) {
                    getMenuInflater().inflate(R.menu.market_menu_marked, menu);
                }else{
                    getMenuInflater().inflate(R.menu.market_menu, menu);
                }
            }
        });

        return true;
    }

    //툴바 기능
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 네비게이션메뉴 버튼
                onBackPressed();
                return true;
            }

            case R.id.bookmark: //지도버튼
                if(myplaceon==0) {
                    item.setIcon(R.drawable.ic_bookmarked);
                    Toast.makeText( getApplicationContext(), "My 플레이스에 추가되었습니다.", Toast.LENGTH_SHORT ).show();
                    pref = getSharedPreferences("profile", MODE_PRIVATE);

                    String token = pref.getLong("token",0)+"";

                    ObjectMyplace obmyplace = new ObjectMyplace();
                    db.collection("MyPlace").document(token).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                int empty = 0;
                                DocumentSnapshot ds = task.getResult();
                                obmyplace.myplacelist = (ArrayList)ds.get("myplacelist");
                                if(obmyplace.myplacelist.size() == 0){
                                    obmyplace.myplacelist.add(placeName);
                                    db.collection("MyPlace").document(token).set(obmyplace);
                                }else{
                                    for(int i=0; i<obmyplace.myplacelist.size(); i++){
                                        if(obmyplace.myplacelist.get(i).equals(placeName)){
                                            empty = 1;
                                        }
                                    }
                                    if(empty == 0){
                                        obmyplace.myplacelist.add(placeName);
                                        db.collection("MyPlace").document(token).set(obmyplace);
                                    }
                                }
                            }else{

                            }
                            myplaceon = 1;
                        }
                    });
                }else{
                    item.setIcon(R.drawable.ic_bookmark);
                    Toast.makeText( getApplicationContext(), "My 플레이스에서 삭제되었습니다.", Toast.LENGTH_SHORT ).show();

                    pref = getSharedPreferences("profile", MODE_PRIVATE);

                    String token = pref.getLong("token",0)+"";

                    ObjectMyplace obmyplace = new ObjectMyplace();
                    db.collection("MyPlace").document(token).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot ds = task.getResult();
                                obmyplace.myplacelist = (ArrayList)ds.get("myplacelist");
                                obmyplace.myplacelist.remove(placeName);
                                db.collection("MyPlace").document(token).set(obmyplace);
                            }
                            myplaceon = 0;
                        }
                    });
                }
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupTabIcons(){

        viewFirst = getLayoutInflater().inflate(R.layout.market_tab,null);
        ImageView imgfirst = viewFirst.findViewById(R.id.market_tab_img);
        TextView textfirst = viewFirst.findViewById(R.id.market_tab_text);
        imgfirst.setImageResource(R.drawable.category_all_grey);
        textfirst.setText("전체");
        tab_layout.getTabAt(0).setCustomView(viewFirst);

        viewSecond = getLayoutInflater().inflate(R.layout.market_tab,null);
        ImageView imgSecond = viewSecond.findViewById(R.id.market_tab_img);
        TextView textSecond = viewSecond.findViewById(R.id.market_tab_text);
        imgSecond.setImageResource(R.drawable.category_res_grey);
        textSecond.setText("맛집");
        tab_layout.getTabAt(1).setCustomView(viewSecond);

        viewThird = getLayoutInflater().inflate(R.layout.market_tab,null);
        ImageView imgThird = viewThird.findViewById(R.id.market_tab_img);
        TextView textThird = viewThird.findViewById(R.id.market_tab_text);
        imgThird.setImageResource(R.drawable.category_cafe_grey);
        textThird.setText("카페");
        tab_layout.getTabAt(2).setCustomView(viewThird);

        viewFourth = getLayoutInflater().inflate(R.layout.market_tab,null);
        ImageView imgFourth = viewFourth.findViewById(R.id.market_tab_img);
        TextView textFourth = viewFourth.findViewById(R.id.market_tab_text);
        imgFourth.setImageResource(R.drawable.category_bar_grey);
        textFourth.setText("술집");
        tab_layout.getTabAt(3).setCustomView(viewFourth);

    }

    private void setupViewPager(ViewPager viewPager){
        pref = getSharedPreferences("profile", MODE_PRIVATE);
        String token = pref.getLong("token",0)+"";

        marketPagerAdapter = new MarketPagerAdapter(getSupportFragmentManager(),placeName);
        pager.setAdapter(marketPagerAdapter);


    }

    private void setMyplace(){

    }
}
