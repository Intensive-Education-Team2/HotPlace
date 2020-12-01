package com.posturn.hotplace;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecommandMainActivity extends AppCompatActivity {

    private ArrayList<ArrayList<ObjectPlace>> recommandAllPlaceList = new ArrayList();

    public RecyclerView view;
    public RecommandSectionAdapter rsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommand_main);
        view = findViewById(R.id.recommand_main_recycler_view);

        initializeData();

    }

    public void initializeData()
    {

        ArrayList<ObjectPlace> recommandList1 = new ArrayList();

        recommandList1.add(new ObjectPlace("Ori", 126.9879010,37.5433420,
                "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fori_haebangchon_img.jpg?alt=media&token=b65c510f-662b-4fe5-ba12-7660565f7dd2",
                "해방촌",1));
        recommandList1.add(new ObjectPlace("dri", 126.9879010,37.5433420,
                "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fori_haebangchon_img.jpg?alt=media&token=b65c510f-662b-4fe5-ba12-7660565f7dd2",
                "해방촌",2));

        recommandAllPlaceList.add(recommandList1);

        ArrayList<ObjectPlace> recommandList2 = new ArrayList();

        recommandList2.add(new ObjectPlace("Ori", 126.9879010,37.5433420,
                "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fori_haebangchon_img.jpg?alt=media&token=b65c510f-662b-4fe5-ba12-7660565f7dd2",
                "해방촌",1));
        recommandList2.add(new ObjectPlace("dri", 126.9879010,37.5433420,
                "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fori_haebangchon_img.jpg?alt=media&token=b65c510f-662b-4fe5-ba12-7660565f7dd2",
                "해방촌",2));
        recommandList2.add(new ObjectPlace("dri", 126.9879010,37.5433420,
                "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fori_haebangchon_img.jpg?alt=media&token=b65c510f-662b-4fe5-ba12-7660565f7dd2",
                "해방촌",2));
        recommandList2.add(new ObjectPlace("dri", 126.9879010,37.5433420,
                "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fori_haebangchon_img.jpg?alt=media&token=b65c510f-662b-4fe5-ba12-7660565f7dd2",
                "해방촌",2));

        recommandAllPlaceList.add(recommandList2);

        ArrayList<ObjectPlace> recommandList3 = new ArrayList();

        recommandList3.add(new ObjectPlace("Ori", 126.9879010,37.5433420,
                "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fori_haebangchon_img.jpg?alt=media&token=b65c510f-662b-4fe5-ba12-7660565f7dd2",
                "해방촌",1));
        recommandList3.add(new ObjectPlace("dri", 126.9879010,37.5433420,
                "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fori_haebangchon_img.jpg?alt=media&token=b65c510f-662b-4fe5-ba12-7660565f7dd2",
                "해방촌",2));
        recommandList3.add(new ObjectPlace("dri", 126.9879010,37.5433420,
                "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%ED%95%B4%EB%B0%A9%EC%B4%8C%2Fori_haebangchon_img.jpg?alt=media&token=b65c510f-662b-4fe5-ba12-7660565f7dd2",
                "해방촌",2));


        recommandAllPlaceList.add(recommandList3);

        rsAdapter = new RecommandSectionAdapter(getApplicationContext(), recommandAllPlaceList);

        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        view.setAdapter(rsAdapter);
    }
}
