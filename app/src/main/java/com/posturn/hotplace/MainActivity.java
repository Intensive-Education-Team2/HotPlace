package com.posturn.hotplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ObjectPlace> objectPlaces = new ArrayList<ObjectPlace>();

    private ArrayList<MarketObject> marketObjectsRes = new ArrayList<MarketObject>();
    private ArrayList<MarketObject> marketObjectsCafe = new ArrayList<MarketObject>();
    private ArrayList<MarketObject> marketObjectsBar = new ArrayList<MarketObject>();

    private ArrayList<ObjectCount> objectCounts = new ArrayList<>();

    private ArrayList<ObjectPost> objectPosts = new ArrayList<>();
    private ArrayList<ObjectPost> objectPosts1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        Button writeBtn = findViewById(R.id.read);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


/*플레이스 데이터 추가

                objectPlaces.add(new ObjectPlace("가로수길", 37.5212, 127.0229, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fgarosugil.jpg?alt=media&token=75372fe7-d872-466a-a852-02786ec2034f", "신사역 주변" , 1));
                objectPlaces.add(new ObjectPlace("강남", 37.4979, 127.0276, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fgangnam.jpg?alt=media&token=602b8306-9867-47e3-b4fa-2517841d99d4", "강남역주변", 2));
                objectPlaces.add(new ObjectPlace("건대", 37.5418, 127.0659, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fkunkuk.jpg?alt=media&token=d06ca4fe-024f-4b51-b76c-7324f4f931ed", "설명" , 3));
                objectPlaces.add(new ObjectPlace("경리단길", 37.5388, 126.9876, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fgyunglidangil.jpg?alt=media&token=481f1243-668e-4d3b-8d7a-61af0744e3d8", "설명" , 4));
                objectPlaces.add(new ObjectPlace("망원동", 37.5555, 126.9048, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fmangwondong.jpg?alt=media&token=cd66afd3-bf7f-4243-8714-3f59b82414ce", "설명" , 5));
                objectPlaces.add(new ObjectPlace("삼청동", 37.5860, 126.9814, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fsamcheongdong.jpg?alt=media&token=94314527-e01e-4dc3-bd7c-36f69ea4c4e0", "설명" , 6));
                objectPlaces.add(new ObjectPlace("성수동", 37.5417, 127.0562, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fseongsoodong.jpg?alt=media&token=30917cd4-2497-4139-a2c9-bb079e234eeb", "설명" , 7));
                objectPlaces.add(new ObjectPlace("신림", 37.4844, 126.9298, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fsinlim.jpg?alt=media&token=a5043d45-05fc-445c-b255-ccb5ca93c79c", "설명" , 8));
                objectPlaces.add(new ObjectPlace("신촌", 37.5563, 126.9365, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fsinlim.jpg?alt=media&token=a5043d45-05fc-445c-b255-ccb5ca93c79c", "설명" , 9));
                objectPlaces.add(new ObjectPlace("연남동", 37.5617, 126.9230 , "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fyeonnamdong.jpg?alt=media&token=390bfd24-3cec-4015-8c48-287d52290d21", "설명" , 10));
                objectPlaces.add(new ObjectPlace("왕십리", 37.5626, 127.0387, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fwangsibli.jpg?alt=media&token=aebf82fd-de25-4762-a0a2-3adb88f677e6", "설명" , 11));
                objectPlaces.add(new ObjectPlace("잠실", 37.5139, 127.1000 , "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fjamsil.jpg?alt=media&token=4fc3b8ca-ba08-422b-b9a2-b2807e77dfbb", "설명" , 12));
                objectPlaces.add(new ObjectPlace("한남동", 37.5383, 127.0011, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fhannamdong.jpg?alt=media&token=bdc31c0e-5d2c-4be7-b5b8-05e22f8c32b9", "설명" , 13));
                objectPlaces.add(new ObjectPlace("합정", 37.5496, 126.9139, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fhapjeong.jpg?alt=media&token=22b58049-7030-4c06-aaa0-942b597f08e1", "설명" , 14));
                objectPlaces.add(new ObjectPlace("해방촌", 37.5448, 126.9859, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fhaebangchon.jpg?alt=media&token=3c85fa3c-7d33-4db2-bbb0-a9750227a085", "설명" , 15));
                for(int i=0; i<15; i++){
                    db.collection("HotPlace").document(objectPlaces.get(i).getName()).set(objectPlaces.get(i));
                }
 */


                /*marketObjectsRes.add(new MarketObject("연탄부락", "잠실","res",127.0815144,37.5094960,
                        "연탄불에 구워먹는 두툼한 돼지고기",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fyeontan_jamsil_img.jpg?alt=media&token=a2ad2f92-c5cc-4e30-8407-2fe60f667d9d",
                        "https://m.store.naver.com/places/detail?id=1390330587"));
                marketObjectsRes.add(new MarketObject("피자쿠치나","잠실","res", 127.0885283,37.5045092,
                        "안녕하세요 49가지 토핑",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fpizzacu_jamsil_img.jpg?alt=media&token=76d640c0-494e-4cf5-a983-ca684b2b6965",
                        "https://m.store.naver.com/places/detail?id=1960415505"));
                marketObjectsRes.add(new MarketObject("화덕고깃간", "잠실","res",127.0817185,37.5096743,
                        "별미였죠",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fhwaduk_jamsil_img.jpg?alt=media&token=9cfc68e3-bde7-4775-93ed-c5e7d4e40ba9",
                        "https://m.store.naver.com/places/detail?id=535057415"));

                marketObjectsCafe.add(new MarketObject("블랭크", "잠실","cafe",127.0882841,37.5052921,
                        "잠실,잠실본동",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fblank_jamsil_img.jpg?alt=media&token=7414e8c8-e738-4fb5-a447-8b7b8dbeb9d1",
                        "https://m.store.naver.com/places/detail?id=1409709820"));
                marketObjectsCafe.add(new MarketObject("HOWS", "잠실","cafe",127.0838482,37.5114049,
                        "카페,갤러리,서점이 함께 있는 복합문화공간",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fhows_jamsil_img.jpg?alt=media&token=e53e04b4-9984-4cae-b34a-b11e8ee5a9c8",
                        "https://m.store.naver.com/places/detail?id=1973519999"));
                marketObjectsCafe.add(new MarketObject("밀도 잠실점", "잠실","cafe",127.0838482,37.5114049,
                        "그날의 온도와 습도를 세심하게 고려하여 만드는 카페",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "https://m.store.naver.com/places/detail?id=1566481353"));

                marketObjectsBar.add(new MarketObject("회장님댁 잠실점", "잠실","bar",127.0812985,37.5093398,
                        "서울특별시 송파구 잠실동 180-6 1층",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fhweijang_jamsil_img.jpg?alt=media&token=ce231011-9927-44d0-92d0-07b1ffc7c33e",
                        "https://m.store.naver.com/places/detail?id=1603996969"));
                marketObjectsBar.add(new MarketObject("화심", "잠실","bar",127.0844513,37.5101557,
                        "안주 서비스도 좋은 잠실새내 술집",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fhwasim_jamsil_img.jpg?alt=media&token=0714d779-545e-475b-bf28-fb8fa265b439",
                        "https://m.store.naver.com/places/detail?id=31992223"));
                marketObjectsBar.add(new MarketObject("밀회관", "잠실","bar",127.0850227,37.5099868,
                        "맥주를 디자인하다.",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmilhwegwan_jamsil_img.jpg?alt=media&token=04205f00-6943-4d3f-ad39-a604454dde97",
                        "https://m.store.naver.com/places/detail?id=1912275762"));


                marketObjectsRes.add(new MarketObject("꼼모아", "해방촌","res",126.9877100,37.5434907,
                        "프랑스 요리를 가볍게 즐기는 프렌치 레스토랑",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fggommoa_haebangchon_img.jpg?alt=media&token=8329f776-1d21-405c-bb22-0d81bfb8f364",
                        "https://m.store.naver.com/places/detail?id=36175099"));
                marketObjectsRes.add(new MarketObject("보니스피자펍","해방촌","res", 126.9869245,37.5411986,
                        "이태원 피자펍",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fbonnys_haebangchon_img.jpg?alt=media&token=0af9f1ae-a7cb-4f07-9aca-feac1eb006a9",
                        "https://m.store.naver.com/places/detail?id=31608396"));
                marketObjectsRes.add(new MarketObject("와일드덕칸틴", "해방촌","res",126.9870686,37.5415116,
                        "Neighborhood Wine Joint",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fwildduck_haebangchon_img.jpg?alt=media&token=4c003c0e-7a9d-4aa5-be48-f0d8697eb3b5",
                        "https://m.store.naver.com/places/detail?id=1465004920"));

                marketObjectsCafe.add(new MarketObject("코타티", "해방촌","cafe",126.9881802,37.5434680,
                        "신선한 과일과 자연식품을 사용하여 만든 내추럴 젤라또 샵",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fcotati_haebangchon_img.jpg?alt=media&token=f35d243b-c810-4d39-8340-96d49400c261",
                        "https://m.store.naver.com/places/detail?id=1499075152"));
                marketObjectsCafe.add(new MarketObject("드도트", "해방촌","cafe",126.9881116,37.5432346,
                        "해방촌 유일의 테라스카페 드도트총 64평 규모의 까페",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fdedot_haebangchon_img.jpg?alt=media&token=36076a5b-92b9-4842-a576-bd4bfbfc71e8",
                        "https://m.store.naver.com/places/detail?id=320929221"));
                marketObjectsCafe.add(new MarketObject("HACKNEY", "해방촌","cafe",126.9873077,37.5415386,
                        "당근케익이 맛있는 카페",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fhackney_haebangchon_img.jpg?alt=media&token=44277f63-1d50-4ce1-8c1d-c5976d315fd4",
                        "https://m.store.naver.com/places/detail?id=20789782"));

                marketObjectsBar.add(new MarketObject("Ori", "해방촌","bar",126.9879010,37.5433420,
                        "An original idea",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fori_haebangchon_img.jpg?alt=media&token=b65c510f-662b-4fe5-ba12-7660565f7dd2",
                        "https://m.store.naver.com/places/detail?id=1268532109"));
                marketObjectsBar.add(new MarketObject("네평반", "해방촌","bar",126.9865423,37.5404077,
                        "오리엔탈적인 음식메뉴와 다양한 보드카",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fnepyeongban_haebangchon_img.jpg?alt=media&token=e2897ecb-ebe1-4e5c-96db-4be35cdf45f0",
                        "https://m.store.naver.com/places/detail?id=36630634"));
                marketObjectsBar.add(new MarketObject("SEOUL VINYL", "해방촌","bar",126.9872512,37.5410340,
                        "영업시간 월~목 19시~02시",
                        "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fseoul_vinyl_haebangchon_img.jpg?alt=media&token=9c8d4361-1c2e-4e08-9db9-2918597c13d8",
                        "https://m.store.naver.com/places/detail?id=37383464"));

                for(int i=0; i<3; i++){
                    db.collection("Market/해방촌/res").document(marketObjectsRes.get(i).getMarketName()).set(marketObjectsRes.get(i));
                }
                for(int i=0; i<3; i++){
                    db.collection("Market/해방촌/cafe").document(marketObjectsCafe.get(i).getMarketName()).set(marketObjectsCafe.get(i));
                }
                for(int i=0; i<3; i++){
                    db.collection("Market/해방촌/bar").document(marketObjectsBar.get(i).getMarketName()).set(marketObjectsBar.get(i));
                }

                 */

/*카운트 데이터 추가
                Map<String, Object> nestedData = new HashMap<>();

                objectCounts.add(new ObjectCount("가로수길", 1400));
                objectCounts.add(new ObjectCount("강남", 1500));
                objectCounts.add(new ObjectCount("건대", 1300));
                objectCounts.add(new ObjectCount("경리단길", 1200));
                objectCounts.add(new ObjectCount("망원동", 1100));
                objectCounts.add(new ObjectCount("삼청동", 1000));
                objectCounts.add(new ObjectCount("성수동", 900));
                objectCounts.add(new ObjectCount("신림", 100));
                objectCounts.add(new ObjectCount("신촌", 700));
                objectCounts.add(new ObjectCount("연남동", 600));
                objectCounts.add(new ObjectCount("왕십리", 500));
                objectCounts.add(new ObjectCount("잠실", 400));
                objectCounts.add(new ObjectCount("한남동", 300));
                objectCounts.add(new ObjectCount("합정", 200));
                objectCounts.add(new ObjectCount("해방촌", 800));

                for(int i=0; i<15; i++) {
                    nestedData.put(Integer.toString(i), objectCounts.get(i));
                }
                db.collection("Test").document("2020-11-24").set(nestedData);

 */

/*
                Map<String, Object> nestedData = new HashMap<>();

                List hotuser = Arrays.asList("");

                Timestamp ts = new Timestamp(new Date());
                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "버섯베놈", "가로수길", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "WS뷔뷔에스", 531, hotuser));

                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fblank_jamsil_img.jpg?alt=media&token=7414e8c8-e738-4fb5-a447-8b7b8dbeb9d1",
                        "Doris_", "망원동", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "일상 데일리 맛집 소통 인친 맞팔", 13, hotuser));

                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "KITTYKITTY", "성수동", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "오랜만에 성수동 출석~.~", 52, hotuser));

                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "꼬기남자", "경리단길", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "HOT RECIPTE HERE", 121, hotuser));

                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "구찌배찌", "가로수길", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "애플스토어 shit xxx", 71, hotuser));

                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "피카소", "삼청동", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "삼청....낭만....", 44, hotuser));

                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "수키", "강남", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "불금 Fire", 71, hotuser));

                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "Bandal", "가로수길", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "a b c d e랜드", 145, hotuser));



                for (int i = 0; i < objectPosts.size(); i++) {
                    nestedData.put(Integer.toString(i), objectPosts.get(i));
                    if(objectPosts.get(i).place.equals("가로수길")){
                        db.collection("PlacePost").document(objectPosts.get(i).getPlace()).update(nestedData);
                    }
                }
                db.collection("AllPost").document("post").set(nestedData);
                */

                Map<String, Object> nestedData = new HashMap<>();
                Map<String, Object> nestedData1 = new HashMap<>();

                List hotuser = Arrays.asList("");

                Timestamp ts = new Timestamp(new Date());
                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "버섯베놈", "가로수길", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "WS뷔뷔에스", 531, hotuser));
                objectPosts1.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "버섯베놈", "가로수길", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "WS뷔뷔에스", 531, hotuser));
                /////////////////////////

                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fblank_jamsil_img.jpg?alt=media&token=7414e8c8-e738-4fb5-a447-8b7b8dbeb9d1",
                        "Doris_", "망원동", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "일상 데일리 맛집 소통 인친 맞팔", 13, hotuser));

                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "KITTYKITTY", "성수동", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "오랜만에 성수동 출석~.~", 52, hotuser));

                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "꼬기남자", "경리단길", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "HOT RECIPTE HERE", 121, hotuser));

                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "구찌배찌", "가로수길", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "애플스토어 shit xxx", 71, hotuser));

                objectPosts1.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "구찌배찌", "가로수길", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "애플스토어 shit xxx", 71, hotuser));
                ////////////////////////

                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "피카소", "삼청동", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "삼청....낭만....", 44, hotuser));

                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "수키", "강남", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "불금 Fire", 71, hotuser));

                objectPosts.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "Bandal", "가로수길", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "a b c d e랜드", 145, hotuser));
                objectPosts1.add(new ObjectPost("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "Bandal", "가로수길", ts, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fmildo_jamsil_cafe.jpg?alt=media&token=e6456cea-7c5d-47a0-b948-d0543c1c4dd3",
                        "a b c d e랜드", 145, hotuser));
                /////////////////



                for (int i = 0; i < objectPosts.size(); i++) {
                    nestedData.put(Integer.toString(i), objectPosts.get(i));
                }
                db.collection("AllPost").document("post").set(nestedData);

                for (int j = 0; j < objectPosts1.size(); j++) {
                    nestedData1.put(Integer.toString(j), objectPosts1.get(j));
                }
                db.collection("PlacePost").document("가로수길").set(nestedData1);




            }
        });
    }
}