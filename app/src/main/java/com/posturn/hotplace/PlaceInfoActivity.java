package com.posturn.hotplace;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.ArrayList;

public class PlaceInfoActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private PlaceInfoCtAllFragment placeInfoCtAllFragment;
    private PlaceInfoCtBarFragment placeInfoCtBarFragment;
    private PlaceInfoCtCafeFragment placeInfoCtCafeFragment;
    private PlaceInfoCtResFragment placeInfoCtResFragment;
    private FragmentTransaction transaction;

    private String parseUrl;

    private RecyclerView recyclerView;
    private ArrayList<PlaceInfoObject> list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_info_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("플레이스");


        parseUrl = "https://s.search.naver.com/p/around/search.naver"; // 카테고리 all

        fragmentManager = getSupportFragmentManager();

        placeInfoCtAllFragment = new PlaceInfoCtAllFragment();
        placeInfoCtBarFragment = new PlaceInfoCtBarFragment();
        placeInfoCtCafeFragment = new PlaceInfoCtCafeFragment();
        placeInfoCtResFragment = new PlaceInfoCtResFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.placeinfo_framelayout, placeInfoCtAllFragment).commitAllowingStateLoss();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_placeinfo);

        //AsyncTask 작동시킴(파싱)
        new Description().execute();
    }

    public void clickHandler(View view)
    {
        transaction = fragmentManager.beginTransaction();

        switch(view.getId())
        {
            case R.id.placeinfo_category_all:
                transaction.replace(R.id.placeinfo_framelayout, placeInfoCtAllFragment).commitAllowingStateLoss();
                parseUrl = "https://s.search.naver.com/p/around/search.naver";
                break;
            case R.id.placeinfo_category_bar:
                transaction.replace(R.id.placeinfo_framelayout, placeInfoCtBarFragment).commitAllowingStateLoss();
                parseUrl = "";
                break;
            case R.id.placeinfo_category_cafe:
                transaction.replace(R.id.placeinfo_framelayout, placeInfoCtCafeFragment).commitAllowingStateLoss();
                parseUrl = "https://s.search.naver.com/p/around/search.naver?tab=cafe";
                break;
            case R.id.placeinfo_category_res:
                transaction.replace(R.id.placeinfo_framelayout, placeInfoCtResFragment).commitAllowingStateLoss();
                parseUrl = "https://s.search.naver.com/p/around/search.naver?tab=restaurant";
                break;
        }
    }


    private class Description extends AsyncTask<Void, Void, Void> {

        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //진행다일로그 시작
            progressDialog = new ProgressDialog(PlaceInfoActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            //all, 음식점, 카페, 술집의 상태를 받는다. 각 상태별로 jsoup 대상이 달라진다.


            try {
                Document doc = Jsoup.connect(parseUrl).get();
                Elements mElementDataSize = doc.select("ul[class=smartaround_list_wrap]").select("li"); //필요한 녀석만 꼬집어서 지정
                int mElementSize = mElementDataSize.size(); //목록이 몇개인지 알아낸다. 그만큼 루프를 돌려야 하나깐.

                for(Element elem : mElementDataSize){ //이렇게 요긴한 기능이
                    String my_title = elem.select("li[class=bx_item_base]").attr("data-name"); //place 가게 이름
                    String my_category = elem.select("li[class=bx_item_base]").attr("data-category"); // place 가게 분류
                    String my_link = elem.select("li [class=bx_item_base] a").attr("href");  // place 가게 상세보기 링크
                    String my_imgUrl = elem.select("li [class=bx_item_base] a img").attr("data-img-src"); // place 가게 이미지
                    //Log.d("test", "test" + mTitle);
                    //ArrayList에 계속 추가한다.
                    list.add(new PlaceInfoObject(my_title, my_category, my_imgUrl, my_link));
                }

                //추출한 전체 <li> 출력해 보자.
                Log.d("debug :", "List " + mElementDataSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //ArraList를 인자로 해서 어답터와 연결한다.
            PlaceInfoAdaptor myAdapter = new PlaceInfoAdaptor(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);

            progressDialog.dismiss();
        }
    }
}
