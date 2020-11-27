package com.posturn.hotplace;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecommandActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ArrayList<ObjectPlace> objectPlaces = new ArrayList<ObjectPlace>();
    public ArrayList<ObjectCount> objectCountsToday = new ArrayList<ObjectCount>();
    public ArrayList<ObjectCount> objectCountsYesterday = new ArrayList<ObjectCount>();
    public ArrayList<ObjectRecommand> objectRecommands = new ArrayList<ObjectRecommand>();

    private String today;
    private String yesterday;

    private TextView placename1;
    private TextView placename2;
    private TextView placename3;
    private TextView placename4;
    private TextView placename5;

    private ImageView placeImage1;
    private ImageView placeImage2;
    private ImageView placeImage3;
    private ImageView placeImage4;
    private ImageView placeImage5;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommand_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("추천 핫플레이스");

        LocalDate date = LocalDate.of(2020, 11, 25);
        LocalDate dateago = date.minusDays(1L);
        today = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        yesterday = dateago.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        setView();
        getTodayCountData();
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

    public void getTodayCountData() {
        DocumentReference docRef = db.collection("Test").document(today);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for (int i = 0; i < 15; i++) {
                            HashMap map = (HashMap) document.get(Integer.toString(i));
                            objectCountsToday.add(new ObjectCount(map.get("name").toString(), Integer.parseInt(map.get("count").toString())));
                            Log.v("11111", String.valueOf(objectCountsToday.get(i).name));
                        }
                    } else {
                    }
                } else {
                }
                getYesterdayCountData();
            }
        });
    }

    public void getYesterdayCountData() {
        DocumentReference docRef = db.collection("Test").document(yesterday);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for (int i = 0; i < 15; i++) {
                            HashMap map = (HashMap) document.get(Integer.toString(i));
                            objectCountsYesterday.add(new ObjectCount(map.get("name").toString(), Integer.parseInt(map.get("count").toString())));
                            Log.v("22222", String.valueOf(objectCountsYesterday.get(i).name));
                        }
                    } else {
                    }
                } else {
                }
                calculateRate(objectCountsToday, objectCountsYesterday, objectRecommands);
            }
        });
    }

    public void calculateRate(ArrayList<ObjectCount> objectCountsToday, ArrayList<ObjectCount> objectCountsYesterday, ArrayList<ObjectRecommand> objectRecommands) {
        for (int i = 0; i < objectCountsToday.size(); i++) {
            double todayCount = objectCountsToday.get(i).count;
            double yesterdayCount = objectCountsYesterday.get(i).count;

            objectRecommands.add(new ObjectRecommand(objectCountsToday.get(i).name, (todayCount - yesterdayCount) / yesterdayCount));
            Log.v("value", String.valueOf(objectRecommands.get(i).percent));
        }
        Collections.sort(objectRecommands);
        getPlaceList();
    }

    public void getPlaceList() {
        db.collection("HotPlace")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map map = document.getData();
                                Log.d("TAG", map.get("name").toString());
                                objectPlaces.add(new ObjectPlace(map.get("name").toString(), (Double) map.get("lat"), (Double) map.get("lon"), map.get("img").toString(), map.get("tag").toString(), Integer.parseInt(map.get("index").toString())));
                            }
                        } else {
                        }
                        setRecommandTextImg(objectPlaces, objectRecommands);
                    }
                });
    }

    public void setRecommandTextImg(ArrayList<ObjectPlace> objectPlaces, ArrayList<ObjectRecommand> objectRecommands){
        placename1.setText(objectRecommands.get(0).name);
        placename2.setText(objectRecommands.get(1).name);
        placename3.setText(objectRecommands.get(2).name);
        placename4.setText(objectRecommands.get(3).name);
        placename5.setText(objectRecommands.get(4).name);

        Picasso.get().load(queryPlaceById(objectPlaces, objectRecommands.get(0).getName())).into(placeImage1);
        Picasso.get().load(queryPlaceById(objectPlaces, objectRecommands.get(1).getName())).into(placeImage2);
        Picasso.get().load(queryPlaceById(objectPlaces, objectRecommands.get(2).getName())).into(placeImage3);
        Picasso.get().load(queryPlaceById(objectPlaces, objectRecommands.get(3).getName())).into(placeImage4);
        Picasso.get().load(queryPlaceById(objectPlaces, objectRecommands.get(4).getName())).into(placeImage5);

    }

    //이미지찾기
    private static String queryPlaceById(ArrayList<ObjectPlace> objectPlaces, String name) {
        ObjectPlace objectPlace = null;
        for (ObjectPlace place : objectPlaces) {
            if (place.name.equals(name)) {
                objectPlace = place;
                break;
            }
        }
        return objectPlace.img;
    }

    private void setView(){
        placename1=findViewById(R.id.th1_placename_1);
        placename2=findViewById(R.id.th1_placename_2);
        placename3=findViewById(R.id.th1_placename_3);
        placename4=findViewById(R.id.th1_placename_4);
        placename5=findViewById(R.id.th1_placename_5);

        placeImage1=findViewById(R.id.th1_place_1);
        placeImage2=findViewById(R.id.th1_place_2);
        placeImage3=findViewById(R.id.th1_place_3);
        placeImage4=findViewById(R.id.th1_place_4);
        placeImage5=findViewById(R.id.th1_place_5);
    }
}
