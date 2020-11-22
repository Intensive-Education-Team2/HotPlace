package com.posturn.hotplace;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com. naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.naver.maps.map.widget.ZoomControlView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapPageActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LocationButtonView locationButtonView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private MapFragment mapFragment;

    private double lat, lon; //위도 경도

    private ArrayList<ObjectPlace> placelist = new ArrayList<ObjectPlace>();

    public InfoWindow infoWindow;

    public LinearLayout placeinfo;

    public ImageView mappart;

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
        setMapOption();
        makeMarking();

        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                //Toast.makeText(getApplicationContext(), lat + ", " + lon, Toast.LENGTH_SHORT).show();
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
        placelist.add(new ObjectPlace("강남", 37.4979, 127.0276, "imgurl", "강남역주변", 1));
        placelist.add(new ObjectPlace("가로수길", 37.5206 , 127.0229, "imgurl", "신사역 주변", 2));

        for(ObjectPlace place : placelist) {
            infoWindow = new InfoWindow();
            Marker marker = new Marker();
            marker.setWidth(150);
            marker.setHeight(200);
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
                TextView placename=findViewById(R.id.placename);
                placename.setText(place.name);
                placeinfo.setVisibility(View.VISIBLE);

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
}



