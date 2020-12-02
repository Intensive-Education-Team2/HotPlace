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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BoardActivity extends AppCompatActivity {

    private ArrayList<ObjectPost> objectPosts = new ArrayList<ObjectPost>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context context;

    private String placeName;

    private RecyclerView recyclerView;
    private PostAdapter adapter;

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

        recyclerView = findViewById(R.id.postRecyclerView) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(context)) ;

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
                Intent intent = new Intent(getApplicationContext(), PostingActivity.class);
                startActivityForResult(intent, 2);
                return false;
            }

            case R.id.boardfilter: {//필터버튼
                Intent intent = new Intent(getApplicationContext(), FilterPageActivity.class);
                startActivityForResult(intent, 1);
                return false;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getPostList() {
        db.collection("AllPost")
                .document("post")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                objectPosts.clear();
                                for(int i=document.getData().size()-1; i>=0;i--){
                                    HashMap map = (HashMap) document.getData().get(Integer.toString(i));
                                    Log.v("qqqq", Integer.toString(document.getData().size()));
                                    //Log.d("TAG", "DocumentSnapshot data: " + document.getData().get(Integer.toString(i)));
                                    List list=  Arrays.asList(map.get("hotuser"));

                                    objectPosts.add(new ObjectPost(map.get("userimg").toString(), map.get("username").toString(), map.get("place").toString(), (Timestamp) map.get("date"), map.get("img").toString(), map.get("content").toString(), Integer.parseInt(map.get("hot").toString()), list));
                                }
                            } else {
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                        adapter = new PostAdapter(objectPosts) ;
                        recyclerView.setAdapter(adapter) ;
                    }
                });
    }

    public void getFilterPost(String placeName){
        db.collection("PlacePost")
                .document(placeName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                objectPosts.clear();
                                for(int i=document.getData().size()-1; i>=0;i--){
                                    HashMap map = (HashMap) document.getData().get(Integer.toString(i));
                                    Log.v("qqqq", Integer.toString(document.getData().size()));
                                    //Log.d("TAG", "DocumentSnapshot data: " + document.getData().get(Integer.toString(i)));
                                    List list=  Arrays.asList(map.get("hotuser"));
                                    objectPosts.add(new ObjectPost(map.get("userimg").toString(), map.get("username").toString(), map.get("place").toString(), (Timestamp) map.get("date"), map.get("img").toString(), map.get("content").toString(), Integer.parseInt(map.get("hot").toString()), list));
                                }
                            } else {
                                objectPosts.clear();
                                Log.d("TAG", "not exist", task.getException());
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                        adapter = new PostAdapter(objectPosts) ;
                        recyclerView.setAdapter(adapter) ;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) { // 정상 반환일 경우에만 동작하겠다
            placeName = data.getExtras().getString("placeName");
            Log.v("name", placeName);

            getFilterPost(placeName);
        }
        if(resultCode ==2){
            getPostList();
        }
    }
}
