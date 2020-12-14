package com.posturn.hotplace;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;

import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class MapPageActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Context context;
    private LocationButtonView locationButtonView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private MapFragment mapFragment;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore db_placeinfo = FirebaseFirestore.getInstance();
    private SharedPreferences pref;

    private int myplaceon = 0;
    private ObjectMyplace obmyplace;
    private String token;

    public ArrayList<ObjectPlace> placelist = new ArrayList<ObjectPlace>();
    public ObjectPlace objectPlace;

    public InfoWindow infoWindow;

    public LinearLayout placeinfo;

    public ImageView mappart;
    public ImageView my_star;

    public String name;
    public double lat;
    public double lon;
    public String img;
    public String tag;
    public int index;

    public String imgUri;
    public String tagPlace;

    LatLng coord = new LatLng(37.4985, 127.0299);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("내 주변 핫플레이스");

        pref = getSharedPreferences("profile", MODE_PRIVATE);
        token = pref.getLong("token",0)+"";

        db.collection("MyPlace").document(token).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {
                obmyplace = new ObjectMyplace();
                obmyplace.myplacelist = (ArrayList) ds.get("myplacelist");
            }
        });
        //map 객체 선언
        FragmentManager fm = getSupportFragmentManager();
        mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            NaverMapOptions options = new NaverMapOptions().locationButtonEnabled(false);
            mapFragment = MapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }


        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        naverMapBasicSettings();
    }

    public void naverMapBasicSettings() {
        mapFragment.getMapAsync(this);
        //내위치 버튼
        locationButtonView = findViewById(R.id.location );
        // 내위치 찾기 위한 source
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        placeinfo=findViewById(R.id.placeinfo);
        placeinfo.setVisibility(View.INVISIBLE);
        mappart=findViewById(R.id.mappart);
        mappart.setOnClickListener(clickListener);

        this.naverMap = naverMap;

        db.collection("HotPlace")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map map=document.getData();
                                name = map.get("name").toString();
                                lat =(Double)map.get("lat");
                                lon =(Double)map.get("lon");
                                img = map.get("img").toString();
                                tag = map.get("tag").toString();
                                index = Integer.parseInt(map.get("index").toString());
                                objectPlace = new ObjectPlace(map.get("name").toString(), lat, lon, img, tag, index);
                                placelist.add(objectPlace);

                                Log.v("value",objectPlace.getName());
                                Log.v("please", objectPlace.getName()+" "+objectPlace.getLat()+" "+objectPlace.getLon());

                            }
                        } else {
                        }
                        makeMarking();
                    }
                });

        setMapOption();

        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
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

    public void setMapOption(){
        CameraUpdate cameraUpdate = CameraUpdate.zoomTo(11.5);
        naverMap.moveCamera(cameraUpdate);

        UiSettings uiSettings= naverMap.getUiSettings();
        uiSettings.setCompassEnabled(false); // 기본값 : true
        uiSettings.setScaleBarEnabled(false); // 기본값 : true
        uiSettings.setZoomControlEnabled(false); // 기본값 : true
        uiSettings.setLocationButtonEnabled(false); // 기본값 : false
        locationButtonView.setMap(naverMap);
    }

    public void makeMarking(){//마커 만들기

        Log.v("here", name+" "+lat+" "+lon);

        for(ObjectPlace place : placelist) {
            infoWindow = new InfoWindow();
            Marker marker = new Marker();
            marker.setWidth(80);
            marker.setHeight(110);
            marker.setIcon(OverlayImage.fromResource(R.drawable.marker));
            marker.setPosition(new LatLng(place.lat, place.lon));
            marker.setMap(naverMap);
            infoWindow.open(marker);

            //플레이스 메시지 표시
            infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
                @NonNull
                @Override
                public CharSequence getText(@NonNull InfoWindow infoWindow) {
                    return place.name;
                }
            });
            //마커 클릭 이벤트
            marker.setOnClickListener(overlay -> {
                Animation animation = new AlphaAnimation(0, 1);
                animation.setDuration(500);
                UpdateMyplace();
                myplaceon = 0;
                my_star = findViewById(R.id.place_star);

                for (int i = 0; i < obmyplace.myplacelist.size(); i++) {
                    if (obmyplace.myplacelist.get(i).equals(place.name)) {
                        myplaceon = 1;
                    }
                }
                if (myplaceon == 1) {
                    my_star.setImageResource(R.drawable.ic_bookmarked_strongpink);
                } else {
                    my_star.setImageResource(R.drawable.ic_bookmark_strongpink);
                }

                ImageView placeImg = findViewById(R.id.placeimage);
                TextView placename = findViewById(R.id.placename);
                placename.setText(place.name);

                TextView placeNickName = findViewById(R.id.place_nickname);
                TextView placeDistance = findViewById(R.id.place_distance);

                placeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), MarketMainActivity.class);
                        intent.putExtra("placeName", place.name);
                        startActivity(intent);
                    }
                });

                placename.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), MarketMainActivity.class);
                        intent.putExtra("placeName", place.name);
                        startActivity(intent);
                    }
                });
                my_star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(myplaceon == 0){
                            my_star.setImageResource(R.drawable.ic_bookmarked_strongpink);
                            Toast.makeText( getApplicationContext(), "My 플레이스에 추가되었습니다.", Toast.LENGTH_SHORT ).show();

                            pref = getSharedPreferences("profile", MODE_PRIVATE);

                            String token = pref.getLong("token",0)+"";

                            ObjectMyplace obmyplace = new ObjectMyplace();
                            db.collection("MyPlace").document(token).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        int empty = 0;
                                        DocumentSnapshot ds = task.getResult();
                                        obmyplace.myplacelist = (ArrayList)ds.get("myplacelist");
                                        if(obmyplace.myplacelist.size() == 0){
                                            obmyplace.myplacelist.add(place.name);
                                            db.collection("MyPlace").document(token).set(obmyplace);
                                        }else{
                                            for(int i=0; i<obmyplace.myplacelist.size(); i++){
                                                if(obmyplace.myplacelist.get(i).equals(place.name)){
                                                    empty = 1;
                                                }
                                            }
                                            if(empty == 0){
                                                obmyplace.myplacelist.add(place.name);
                                                db.collection("MyPlace").document(token).set(obmyplace);
                                            }
                                        }
                                    }else{

                                    }
                                    myplaceon = 1;
                                }
                            });
                        }else{
                            my_star.setImageResource(R.drawable.ic_bookmark_strongpink);
                            Toast.makeText( getApplicationContext(), "My 플레이스에 삭제되었습니다.", Toast.LENGTH_SHORT ).show();

                            pref = getSharedPreferences("profile", MODE_PRIVATE);

                            String token = pref.getLong("token",0)+"";

                            ObjectMyplace obmyplace = new ObjectMyplace();
                            db.collection("MyPlace").document(token).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot ds = task.getResult();
                                        obmyplace.myplacelist = (ArrayList)ds.get("myplacelist");
                                        obmyplace.myplacelist.remove(place.name);
                                        db.collection("MyPlace").document(token).set(obmyplace);
                                        UpdateMyplace();
                                    }
                                    myplaceon = 0;
                                }
                            });
                        }
                    }
                });

                db_placeinfo.collection("HotPlace").document(place.name).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        imgUri = documentSnapshot.getString("img");
                        tagPlace = documentSnapshot.getString("tag");
                        Picasso.get().load(imgUri).into(placeImg);
                        placeDistance.setText(getDistance(coord.latitude,coord.longitude,(Double)documentSnapshot.get("lat"),(Double)documentSnapshot.get("lon"))+" km");
                        placeNickName.setText(tagPlace);
                    }
                });
                placeinfo.setVisibility(View.VISIBLE);
                placeinfo.setAnimation(animation);
                Toast.makeText(getApplication(), place.name + " 클릭", Toast.LENGTH_SHORT).show();

                return false;
            });
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.mappart :
                    placeinfo.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplication(), "맵클릭", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private String getDistance(double myLat, double myLon, double targetLat, double targetLon) {
        double distance;

        Location locationA = new Location("point A");
        locationA.setLatitude(myLat);
        locationA.setLongitude(myLon);

        Location locationB = new Location("point B");
        locationB.setLatitude(targetLat);
        locationB.setLongitude(targetLon);

        distance = locationA.distanceTo(locationB) /1000.0;

        return String.format("%.2f",distance);
    }

    private void UpdateMyplace(){
        db.collection("MyPlace").document(token).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {
                obmyplace = new ObjectMyplace();
                obmyplace.myplacelist = (ArrayList) ds.get("myplacelist");
            }
        });
    }
}



