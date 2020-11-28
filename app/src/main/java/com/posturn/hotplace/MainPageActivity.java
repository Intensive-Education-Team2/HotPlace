package com.posturn.hotplace;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.internal.Internal;

public class MainPageActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    private TextView morerank;
    private TextView morerecommand;
    private StorageReference mStorageRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ArrayList<ObjectPlace> objectPlaces = new ArrayList<ObjectPlace>();
    public ArrayList<ObjectCount> objectCountsToday = new ArrayList<ObjectCount>();
    public ArrayList<ObjectCount> objectCountsYesterday = new ArrayList<ObjectCount>();
    public ArrayList<ObjectRecommand> objectRecommands = new ArrayList<ObjectRecommand>();

    public ImageView imageViewTop1;
    public ImageView imageViewTop2;
    public ImageView imageViewTop3;
    public ImageView imageViewTop4;
    public ImageView imageViewTop5;

    public TextView textViewTop1;
    public TextView textViewTop2;
    public TextView textViewTop3;
    public TextView textViewTop4;
    public TextView textViewTop5;

    public TextView textViewnear1;
    public TextView textViewnear2;
    public TextView textViewnear3;

    public TextView textViewnearDistance1;
    public TextView textViewnearDistance2;
    public TextView textViewnearDistance3;

    private TextView textViewrecommand1name;
    private TextView textViewrecommand2name;
    private TextView textViewrecommand3name;

    private String today;
    private String yesterday;

    private String userName;
    private String userImg;
    private ImageView userImg_v;
    private TextView userName_v;
    private int login_state;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public GpsTracker gpsTracker = new GpsTracker(MainPageActivity.this);
    public double latitude = gpsTracker.getLatitude();
    public double longitude = gpsTracker.getLongitude();


    @RequiresApi(api = Build.VERSION_CODES.O)
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
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Hotpler");

        gpsTracker = new GpsTracker(MainPageActivity.this);
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        userName = getIntent().getStringExtra("userName");
        userImg = getIntent().getStringExtra("userImg");
        
        //TODO = Here
        loginChecked(userName, userImg);

        setView();

        LocalDate date = LocalDate.of(2020, 11, 25);
        LocalDate dateago = date.minusDays(1L);
        today = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        yesterday = dateago.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if (id == R.id.hotplace_rank) {
                    Intent intent = new Intent(getApplicationContext(), RankActivity.class);
                    startActivity(intent);
                } else if (id == R.id.hotplace_recommand) {
                    Intent intent = new Intent(getApplicationContext(), RecommandActivity.class);
                    startActivity(intent);
                } else if (id == R.id.hotplace_board) {
                    Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                    startActivity(intent);
                } else if (id == R.id.my_hotplace) {
                    Intent intent = new Intent(getApplicationContext(), MyPlaceActivity.class);
                    startActivity(intent);
                } else if (id == R.id.notice) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.login){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }

                mDrawerLayout.closeDrawer(Gravity.LEFT);
                int size = navigationView.getMenu().size();
                for (int i = 0; i < size; i++) {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }

                return true;
            }
        });

        morerank = findViewById(R.id.textView2);
        morerecommand = findViewById(R.id.textView3);

        morerank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RankActivity.class);
                startActivity(intent);
            }
        });

        morerecommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecommandActivity.class);
                startActivity(intent);
            }
        });

        getTodayCountData();

    }

    //툴바 테마
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainpage_menu, menu);
        return true;
    }


    //툴바 기능
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 네비게이션메뉴 버튼
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }

            case R.id.mainpagemap: //지도버튼
                Intent intent = new Intent(getApplicationContext(), MapPageActivity.class);
                startActivity(intent);
                return false;
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
                            //Log.d("TAG", objectCountsToday.toString());
                        }
                    } else {
                    }
                    Collections.sort(objectCountsToday);//소팅
                    Log.d("Sort", objectCountsToday.get(1).getName().toString());

                    getPlaceList();
                    setTop5text();
                } else {
                }
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
                        calculateDistance(latitude, longitude, objectPlaces);
                        setTop5img();
                        getYesterdayCountData();
                    }
                });
    }

    public void setTop5text() {
        textViewTop1.setText(objectCountsToday.get(0).getName());
        textViewTop2.setText(objectCountsToday.get(1).getName());
        textViewTop3.setText(objectCountsToday.get(2).getName());
        textViewTop4.setText(objectCountsToday.get(3).getName());
        textViewTop5.setText(objectCountsToday.get(4).getName());
    }

    public void setTop5img() {
        Picasso.get().load(queryPlaceById(objectPlaces, objectCountsToday.get(0).getName())).into(imageViewTop1);
        Picasso.get().load(queryPlaceById(objectPlaces, objectCountsToday.get(1).getName())).into(imageViewTop2);
        Picasso.get().load(queryPlaceById(objectPlaces, objectCountsToday.get(2).getName())).into(imageViewTop3);
        Picasso.get().load(queryPlaceById(objectPlaces, objectCountsToday.get(3).getName())).into(imageViewTop4);
        Picasso.get().load(queryPlaceById(objectPlaces, objectCountsToday.get(4).getName())).into(imageViewTop5);


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

    private void calculateDistance(double lat, double lon, ArrayList<ObjectPlace> objectPlaces) {
        ArrayList<ObjectDistance> distances = new ArrayList<ObjectDistance>();

        for (int i = 0; i < objectPlaces.size(); i++) {
            distances.add(new ObjectDistance(objectPlaces.get(i).name, getDistance(lat, lon, objectPlaces.get(i).lat, objectPlaces.get(i).lon)));
        }
        Collections.sort(distances);
        setDistance(distances);
    }

    private void setDistance(ArrayList<ObjectDistance> distances) {
        textViewnear1.setText(distances.get(0).name);
        textViewnear2.setText(distances.get(1).name);
        textViewnear3.setText(distances.get(2).name);

        textViewnearDistance1.setText(Double.toString(distances.get(0).distance) + "km");
        textViewnearDistance2.setText(Double.toString(distances.get(1).distance) + "km");
        textViewnearDistance3.setText(Double.toString(distances.get(2).distance) + "km");

    }

    private double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double distance;

        Location locationA = new Location("point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lng1);

        Location locationB = new Location("point B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lng2);

        distance = locationA.distanceTo(locationB);

        return Math.round(distance / 1000 * 100) / 100.0;
    }

    public void calculateRate(ArrayList<ObjectCount> objectCountsToday, ArrayList<ObjectCount> objectCountsYesterday, ArrayList<ObjectRecommand> objectRecommands) {
        for (int i = 0; i < objectCountsToday.size(); i++) {
            double todayCount = objectCountsToday.get(i).count;
            double yesterdayCount = objectCountsYesterday.get(i).count;

            objectRecommands.add(new ObjectRecommand(objectCountsToday.get(i).name, (todayCount - yesterdayCount) / yesterdayCount));
        }
        Collections.sort(objectRecommands);
        setRecommaneText(objectRecommands);
    }

    private void setRecommaneText(ArrayList<ObjectRecommand> objectRecommands){
        textViewrecommand1name.setText(objectRecommands.get(0).name);
        textViewrecommand2name.setText(objectRecommands.get(1).name);
        textViewrecommand3name.setText(objectRecommands.get(2).name);
    }

    private void setView() {
        imageViewTop1 = findViewById(R.id.imageViewRank1);
        imageViewTop2 = findViewById(R.id.imageViewRank2);
        imageViewTop3 = findViewById(R.id.imageViewRank3);
        imageViewTop4 = findViewById(R.id.imageViewRank4);
        imageViewTop5 = findViewById(R.id.imageViewRank5);

        textViewTop1 = findViewById(R.id.textViewRank1);
        textViewTop2 = findViewById(R.id.textViewRank2);
        textViewTop3 = findViewById(R.id.textViewRank3);
        textViewTop4 = findViewById(R.id.textViewRank4);
        textViewTop5 = findViewById(R.id.textViewRank5);

        textViewnear1 = findViewById(R.id.textViewHereName1);
        textViewnear2 = findViewById(R.id.textViewHereName2);
        textViewnear3 = findViewById(R.id.textViewHereName3);

        textViewrecommand1name=findViewById(R.id.recommand1name);
        textViewrecommand2name=findViewById(R.id.recommand2name);
        textViewrecommand3name=findViewById(R.id.recommand3name);

        textViewnearDistance1 = findViewById(R.id.textViewHereDistance1);
        textViewnearDistance2 = findViewById(R.id.textViewHereDistance2);
        textViewnearDistance3 = findViewById(R.id.textViewHereDistance3);


    }

    private void loginChecked(String userName, String userImg){
        if(userName==null){
            login_state = 0;
        }else{
            login_state = 1;
            userImg_v = (ImageView)findViewById(R.id.userimage);
            userName_v = (TextView)findViewById(R.id.username);

            userImg_v.setImageResource(R.drawable.user_male);
            userName_v.setText(userName+"님, 반갑습니다");
        }
    }


}


