package com.posturn.hotplace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyPlaceActivity extends AppCompatActivity {
    Context context;
    RecyclerView view;
    private String token;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences pref;

    public MyPlaceAdapter myAdapter;
    public ObjectMyplace obmyplace = new ObjectMyplace();
    public ArrayList<String> myplacelist = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myplace_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("My 플레이스");

        view = findViewById(R.id.myplace_main_recycler_view);

        pref = getSharedPreferences("profile", MODE_PRIVATE);
        token = pref.getLong("token",0)+"";
        Task<DocumentSnapshot> tds = db.collection("MyPlace").document(token).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    myplacelist = (ArrayList)ds.get("myplacelist");
                    myAdapter = new MyPlaceAdapter(getApplicationContext(),myplacelist,token);

                    view.setHasFixedSize(true);
                    view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    view.setAdapter(myAdapter);
                }
            }
        });

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

}
