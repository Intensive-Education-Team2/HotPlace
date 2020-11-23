package com.posturn.hotplace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainPageActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.hotplace_rank){
                    Intent intent = new Intent(getApplicationContext(), RankActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.hotplace_recommand){
                    Intent intent = new Intent(getApplicationContext(), RecommandActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.hotplace_board){
                    Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                    startActivity(intent);
                }

                return true;
            }
        });

    }
    //툴바 테마
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainpage_menu, menu) ;
        return true ;
    }


    //툴바 기능
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{ // 네비게이션메뉴 버튼
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }

            case R.id.mainpagemap : //지도버튼
                Intent intent = new Intent(getApplicationContext(), MapPageActivity.class);
                startActivity(intent);
                return false;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }


}


