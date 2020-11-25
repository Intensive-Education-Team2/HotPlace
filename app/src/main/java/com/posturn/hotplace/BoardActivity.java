package com.posturn.hotplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

public class BoardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("핫플레이스 게시판");

    }

    //툴바 테마
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.board_menu, menu);
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

            case R.id.boardadd: {//글쓰기
                Intent intent = new Intent(getApplicationContext(), WriteMyPageActivity.class);
                startActivity(intent);
                return false;
            }

            case R.id.boardfilter: {//필터버튼
                Intent intent = new Intent(getApplicationContext(), FilterPageActivity.class);
                startActivity(intent);
                return false;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
