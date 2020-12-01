package com.posturn.hotplace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class FilterPageActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<TextView> filterViews = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_page);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("플레이스 선택");


        setFilterViews();
    }

    //툴바 기능
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

    private void setFilterViews(){
        filterViews.add((TextView) findViewById(R.id.garosoogil));
        filterViews.add((TextView)findViewById(R.id.gangnam));
        filterViews.add((TextView)findViewById(R.id.gundae));
        filterViews.add((TextView)findViewById(R.id.gyeonglidangil));
        filterViews.add((TextView)findViewById(R.id.mangwondong));
        filterViews.add((TextView)findViewById(R.id.samcheongdong));
        filterViews.add((TextView)findViewById(R.id.sungsoodong));
        filterViews.add((TextView)findViewById(R.id.sinlim));
        filterViews.add((TextView)findViewById(R.id.shinchon));
        filterViews.add((TextView)findViewById(R.id.yeonnamdong));
        filterViews.add((TextView)findViewById(R.id.wangsipli));
        filterViews.add((TextView)findViewById(R.id.jamsil));
        filterViews.add((TextView)findViewById(R.id.hannamdong));
        filterViews.add((TextView)findViewById(R.id.habjung));
        filterViews.add((TextView)findViewById(R.id.haebangchon));

        for(TextView view : filterViews){
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        TextView place = findViewById(v.getId());
        String selectPlace= (String) place.getText();
        Log.v("place", selectPlace);

        Intent intent = getIntent();
        intent.putExtra("placeName", selectPlace);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void exitApp(){
        Intent i = new Intent(this,BoardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("EXIT_ROOT",true);
        startActivity(i);
    }

}
