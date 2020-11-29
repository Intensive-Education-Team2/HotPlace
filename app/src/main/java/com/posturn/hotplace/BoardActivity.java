package com.posturn.hotplace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardActivity extends AppCompatActivity {

    private ArrayList<ObjectPost> objectPosts = new ArrayList<ObjectPost>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context context;

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

        getPostList();
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

    public void getPostList() {
        db.collection("Post")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            String docuID="";
                            String newdocuID="";
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                docuID=document.getId();
                                if(!docuID.equals(newdocuID)){
                                    i=0;
                                }
                                HashMap map = (HashMap) document.get(Integer.toString(i));
                                List list=  Arrays.asList(map.get("hotuser"));
                                objectPosts.add(new ObjectPost(map.get("userimg").toString(), map.get("username").toString(), map.get("place").toString(), (Timestamp) map.get("date"), map.get("img").toString(), map.get("content").toString(), Integer.parseInt(map.get("hot").toString()), list));
                                newdocuID=document.getId();
                                Log.d("TAG", list.toString());
                                i++;
                            }
                        } else {
                        }
                        RecyclerView recyclerView = findViewById(R.id.postRecyclerView) ;
                        recyclerView.setLayoutManager(new LinearLayoutManager(context)) ;
                        PostAdapter adapter = new PostAdapter(objectPosts) ;
                        recyclerView.setAdapter(adapter) ;
                    }
                });
    }
}
