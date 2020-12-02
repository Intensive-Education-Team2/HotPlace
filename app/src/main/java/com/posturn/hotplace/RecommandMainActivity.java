package com.posturn.hotplace;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.HashMap;
import java.util.Map;

public class RecommandMainActivity extends AppCompatActivity {

    public static ArrayList<ArrayList<ObjectPlace>> recommandAllPlaceList = new ArrayList();

    public RecyclerView view;
    public RecommandSectionAdapter rsAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ArrayList<ObjectPlace> objectPlaces = new ArrayList<ObjectPlace>();
    public ArrayList<ObjectCount> objectCountsToday = new ArrayList<ObjectCount>();
    public ArrayList<ObjectCount> objectCountsResToday = new ArrayList<ObjectCount>();
    public ArrayList<ObjectCount> objectCountsCafeToday = new ArrayList<ObjectCount>();
    public ArrayList<ObjectCount> objectCountsBarToday = new ArrayList<ObjectCount>();
    public ArrayList<ObjectCount> objectCountsYesterday = new ArrayList<ObjectCount>();
    public ArrayList<ObjectRecommand> objectRecommands = new ArrayList<ObjectRecommand>();

    private String today;
    private String yesterday;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommand_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("추천 핫플레이스");

        recommandAllPlaceList.clear();

        LocalDate date = LocalDate.of(2020, 11, 29);
        LocalDate dateago = date.minusDays(1L);
        today = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        yesterday = dateago.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        view = findViewById(R.id.recommand_main_recycler_view);

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
        DocumentReference docRef = db.collection("Count_Place").document(today);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for (int i = 0; i < 15; i++) {
                            HashMap map = (HashMap) document.get(Integer.toString(i));
                            objectCountsToday.add(new ObjectCount(map.get("name").toString(), Integer.parseInt(map.get("count").toString())));
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
        DocumentReference docRef = db.collection("Count_Place").document(yesterday);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for (int i = 0; i < 15; i++) {
                            HashMap map = (HashMap) document.get(Integer.toString(i));
                            objectCountsYesterday.add(new ObjectCount(map.get("name").toString(), Integer.parseInt(map.get("count").toString())));
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
                                objectPlaces.add(new ObjectPlace(map.get("name").toString(), (Double) map.get("lat"), (Double) map.get("lon"), map.get("img").toString(), map.get("tag").toString(), Integer.parseInt(map.get("index").toString())));
                            }
                            setRecommandTextImg(objectRecommands);
                        } else {

                        }
                        getTodayResCountData();
                        getTodayCafeCountData();
                        getTodayBarCountData();

                        rsAdapter = new RecommandSectionAdapter(getApplicationContext(), recommandAllPlaceList);

                        view.setHasFixedSize(true);
                        view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        view.setAdapter(rsAdapter);
                    }
                });
    }

    public void setRecommandTextImg(ArrayList<ObjectRecommand> objectRecommands){
        ArrayList<ObjectPlace> opList = new ArrayList<>();
        opList = objectPlaces;

        ArrayList<ObjectPlace> recommandListDay = new ArrayList();

        for(int i =0; i<5; i++){
            for (ObjectPlace op : opList) {
                if (objectRecommands.get(i).name.equals(op.name)) {
                    recommandListDay.add(op);
                }
            }
        }
        recommandAllPlaceList.add(recommandListDay);

    }

    public void setCountTextImg(ArrayList<ObjectCount> objectCounts){
        ArrayList<ObjectPlace> opList = new ArrayList<>();
        opList = objectPlaces;
        ArrayList<ObjectPlace> recommandList = new ArrayList();

            for(int i =0; i<5; i++){
                for (ObjectPlace op : opList) {
                    if (objectCounts.get(i).name.equals(op.name)) {
                        recommandList.add(op);
                    }
                }
            }
        recommandAllPlaceList.add(recommandList);

    }


    public void getTodayResCountData(){
        DocumentReference docRefRes = db.collection("Count_Res").document(today);

        docRefRes.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for (int i = 0; i < 15; i++) {
                            HashMap map = (HashMap) document.get(Integer.toString(i));
                            objectCountsResToday.add(new ObjectCount(map.get("name").toString(), Integer.parseInt(map.get("count").toString())));
                        }
                    } else {
                    }
                } else {
                }
                Collections.sort(objectCountsResToday);
                setCountTextImg(objectCountsResToday);
            }

        });
    }

    public void getTodayCafeCountData(){
        DocumentReference docRefCafe = db.collection("Count_Cafe").document(today);

        docRefCafe.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for (int i = 0; i < 15; i++) {
                            HashMap map = (HashMap) document.get(Integer.toString(i));
                            objectCountsCafeToday.add(new ObjectCount(map.get("name").toString(), Integer.parseInt(map.get("count").toString())));
                        }
                    } else {
                    }
                } else {
                }
                Collections.sort(objectCountsCafeToday);
                setCountTextImg(objectCountsCafeToday);
            }
        });
    }

    public void getTodayBarCountData(){
        DocumentReference docRefBar = db.collection("Count_Bar").document(today);

        docRefBar.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for (int i = 0; i < 15; i++) {
                            HashMap map = (HashMap) document.get(Integer.toString(i));
                            objectCountsBarToday.add(new ObjectCount(map.get("name").toString(), Integer.parseInt(map.get("count").toString())));
                        }
                    } else {
                    }
                } else {
                }
                Collections.sort(objectCountsBarToday);
                setCountTextImg(objectCountsBarToday);
            }
        });
    }



}
