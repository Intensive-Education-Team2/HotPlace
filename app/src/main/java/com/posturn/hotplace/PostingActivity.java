package com.posturn.hotplace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.Timestamp;

import java.util.List;

public class PostingActivity extends AppCompatActivity implements View.OnClickListener{
    private final int GET_GALLERY_IMAGE = 200;
    private SharedPreferences pref;

    private String userimg;
    private String username;
    private String place;
    private Timestamp date;
    private String img;
    private String content;
    private int hot;
    private List hotuser;

    private Button selectPlace;
    private ImageButton selectImg;
    private EditText editContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("게시글 작성");

        selectPlace=findViewById(R.id.btn_place);
        selectImg=findViewById(R.id.btn_image);
        editContent=findViewById(R.id.edittext_content);

        selectPlace.setOnClickListener(this);
        selectImg.setOnClickListener(this);

        pref = getSharedPreferences("profile", MODE_PRIVATE);
    }

    //툴바 테마
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write_board_menu, menu) ;
        return true ;
    }

    //툴바 기능
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{ // 뒤로가기
                onBackPressed();
                return true;
            }

            case R.id.write : {//등록버튼
                return false;
            }

            default :
                return super.onOptionsItemSelected(item) ;
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_place:
                intent = new Intent(getApplicationContext(), FilterPageActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_image:
                intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode == RESULT_OK) { // 정상 반환일 경우에만 동작하겠다
            String btnText = data.getExtras().getString("placeName");
            selectPlace.setText(btnText);
            selectPlace.setTextColor(Color.parseColor("#ffffff"));
            selectPlace.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_select_radius));
            //selectPlace.setBackgroundColor(Color.parseColor("#EE6C81"));
        }

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            selectImg.setImageURI(selectedImageUri);
            selectImg.setScaleType(ImageView.ScaleType.FIT_CENTER);

        }
    }

}