package com.posturn.hotplace;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class RankActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TopListAdapter adapter;
    private ListView listView;

    private ArrayList objectCountsToday = new ArrayList();
    private ArrayList objectCountsYesterday= new ArrayList();

    private String today;
    private String yesterday;

    private void populateListView(ArrayList<ObjectCount> objectCountsToday, ArrayList<ObjectCount> objectCountsYesterday) {
        listView = findViewById(R.id.ranklist);
        adapter = new TopListAdapter(this, objectCountsToday, objectCountsYesterday);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("핫플레이스 순위");

        LocalDate date=LocalDate.of(2020,11,25);
        LocalDate dateago=date.minusDays(1L);
        today = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        yesterday=dateago.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        getTodayCountData();
    }

    //툴바 테마
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rank_menu, menu) ;
        return true ;
    }

    //툴바 기능
    @SuppressLint("ShowToast")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{ // 뒤로가기
                onBackPressed();
                return true;
            }
            case R.id.question : {//등록버튼
                Log.v("show","show");
                Toast myToast = Toast.makeText(this.getApplicationContext(),"머신러닝 분류 모델을 이용해 인스타그램 게시물 태그를 분석하여 지역별 게시물 카운트 집계", Toast.LENGTH_SHORT);
                myToast.show();
                return false;
            }
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

    public void getTodayCountData(){
        DocumentReference docRef = db.collection("Test").document(today);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for(int i=0; i<15; i++) {
                            HashMap map = (HashMap) document.get(Integer.toString(i));
                            objectCountsToday.add(new ObjectCount(map.get("name").toString(), Integer.parseInt(map.get("count").toString())));
                            //Log.d("TAG", objectCountsToday.toString());
                        }
                    } else {
                    }
                    Collections.sort(objectCountsToday);//소팅
                    //Log.d("Sort", objectCountsToday.get(0).toString());
                    getYesterdayCountData();
                } else {
                }
            }
        });
    }

    public void getYesterdayCountData(){
        DocumentReference docRef = db.collection("Test").document(yesterday);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for(int i=0; i<15; i++) {
                            HashMap map = (HashMap) document.get(Integer.toString(i));
                            objectCountsYesterday.add(new ObjectCount(map.get("name").toString(), Integer.parseInt(map.get("count").toString())));
                            Log.d("TAG", objectCountsYesterday.toString());
                        }
                    } else {
                    }
                    Collections.sort(objectCountsYesterday);//소팅
                } else {
                }
                Log.d("Sort", objectCountsYesterday.get(0).toString());
                populateListView(objectCountsToday, objectCountsYesterday);
            }
        });
    }
}
